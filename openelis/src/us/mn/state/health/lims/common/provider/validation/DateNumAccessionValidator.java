package us.mn.state.health.lims.common.provider.validation;

import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.util.resources.ResourceLocator;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateNumAccessionValidator implements IAccessionNumberValidator {

    private static String nextAccessionNumber;
    private static Object LOCK_OBJECT = new Object();
    private SampleDAO sampleDAO = new SampleDAOImpl();

    void setSampleDAO(SampleDAO sampleDAO) {
        this.sampleDAO = sampleDAO;
    }

    @Override
    public boolean needProgramCode() {
        return false;
    }

    @Override
    public ValidationResults validFormat(String accessionNumber, boolean checkDate) throws IllegalArgumentException {

        if (accessionNumber.length() != getMaxAccessionLength()) {
            return ValidationResults.LENGTH_FAIL;
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
            format.setLenient(false);
            Date datePortion = format.parse(accessionNumber.substring(0, 8));
            if (datePortion == null) {
                return ValidationResults.FORMAT_FAIL;
            }
        } catch (ParseException e) {
            return ValidationResults.FORMAT_FAIL;
        }

        try {
            Integer.parseInt(accessionNumber.substring(8));
        } catch (NumberFormatException e) {
            return ValidationResults.FORMAT_FAIL;
        }

        return ValidationResults.SUCCESS;
    }

    @Override
    public String getInvalidMessage(ValidationResults results) {
        String configLocale = SystemConfiguration.getInstance().getDefaultLocale().toString();
        Locale locale = new Locale(configLocale);

        String message = ResourceLocator.getInstance().getMessageResources().getMessage(locale,
                "sample.entry.invalid.accession.number");
        return message;
    }

    @Override
    public String createFirstAccessionNumber(String programCode) {
        return new SimpleDateFormat("ddMMyyyy").format(new Date()) + "001";
    }

    @Override
    public String incrementAccessionNumber(String currentHighAccessionNumber) {
        if (currentHighAccessionNumber == null || currentHighAccessionNumber.isEmpty()) {
            return null;
        }
        String currentIndex = currentHighAccessionNumber.substring(8);
        int nextIndex = Integer.parseInt(currentIndex) + 1;
        return String.format(currentHighAccessionNumber.substring(0, 8) + "%03d", nextIndex);
    }

    @Override
    public String getNextAvailableAccessionNumber(String programCode) {
        String currentLargestAccessionNumber;
        if (nextAccessionNumber == null || hasNextAccessionNumberExpired()) {
            synchronized (LOCK_OBJECT) {
                if (nextAccessionNumber == null || hasNextAccessionNumberExpired()) {
                    currentLargestAccessionNumber = sampleDAO.getLargestAccessionNumberWithPrefix(new SimpleDateFormat("ddMMyyyy").format(new Date()));
                    if (currentLargestAccessionNumber == null) {
                        nextAccessionNumber = createFirstAccessionNumber(null);
                        return nextAccessionNumber;
                    } else {
                        nextAccessionNumber = currentLargestAccessionNumber;
                    }
                }
            }
        }

        synchronized (LOCK_OBJECT) {
            nextAccessionNumber = incrementAccessionNumber(nextAccessionNumber);
            return nextAccessionNumber;
        }
    }

    @Override
    public int getMaxAccessionLength() {
        return 11;
    }

    @Override
    public boolean accessionNumberIsUsed(String accessionNumber, String recordType) {
        return sampleDAO.getSampleByAccessionNumber(accessionNumber) != null;
    }

    @Override
    public ValidationResults checkAccessionNumberValidity(String accessionNumber, String recordType, String isRequired, String projectFormName) {

        ValidationResults results = validFormat(accessionNumber, true);
        if (results == ValidationResults.SUCCESS && accessionNumberIsUsed(accessionNumber, null)) {
            results = ValidationResults.USED_FAIL;
        }

        return results;
    }

    @Override
    public int getInvarientLength() {
        return 0;
    }

    @Override
    public int getChangeableLength() {
        return 11;
    }

    private boolean hasNextAccessionNumberExpired(){
        String prefix = new SimpleDateFormat("ddMMyyyy").format(new Date());
        return !nextAccessionNumber.startsWith(prefix);
    }
}

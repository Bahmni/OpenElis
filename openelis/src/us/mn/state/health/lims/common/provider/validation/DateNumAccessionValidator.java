/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package us.mn.state.health.lims.common.provider.validation;

import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.util.resources.ResourceLocator;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateNumAccessionValidator implements IAccessionNumberValidator {

    private static String nextAccessionNumber;
    private static final Object LOCK_OBJECT = new Object();
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
        return getDatePrefix() + "001";
    }

    @Override
    public String incrementAccessionNumber(String currentHighAccessionNumber) {
        if (currentHighAccessionNumber == null || currentHighAccessionNumber.isEmpty()) {
            return null;
        }
        int nextIndex = Integer.parseInt(currentHighAccessionNumber.substring(9)) + 1;
        return String.format(currentHighAccessionNumber.substring(0, 9) + "%03d", nextIndex);
    }

    @Override
    public String getNextAvailableAccessionNumber(String programCode) {

        if (nextAccessionNumber == null || hasNextAccessionNumberExpired()) {
            synchronized (LOCK_OBJECT) {
                return  generateNextAccessionNumber();
            }
        }
        synchronized (LOCK_OBJECT) {
            nextAccessionNumber = incrementAccessionNumber(nextAccessionNumber);
            return nextAccessionNumber;
        }

    }

    private String generateNextAccessionNumber() {
        String accessionNumber = null;
        if (nextAccessionNumber == null || hasNextAccessionNumberExpired()) {
            accessionNumber = sampleDAO.getLargestAccessionNumberWithPrefix(getDatePrefix());
            if (accessionNumber == null) {
                nextAccessionNumber = createFirstAccessionNumber(null);
                return nextAccessionNumber;
            }
            nextAccessionNumber = incrementAccessionNumber(accessionNumber);
            return nextAccessionNumber;
        }
        nextAccessionNumber = incrementAccessionNumber(nextAccessionNumber);
        return nextAccessionNumber;
    }

    @Override
    public int getMaxAccessionLength() {
        return 13;
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
        return 12;
    }

    private String getDatePrefix() {
        return new SimpleDateFormat("ddMMyyyy").format(new Date()) + "-";
    }

    private boolean hasNextAccessionNumberExpired() {
        String prefix = getDatePrefix();
        return !nextAccessionNumber.startsWith(prefix);
    }

    static void resetAccessionNumber() {
        nextAccessionNumber = null;
    }
}

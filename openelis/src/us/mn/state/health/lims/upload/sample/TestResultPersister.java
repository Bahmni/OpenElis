package us.mn.state.health.lims.upload.sample;

import org.apache.commons.lang3.StringUtils;
import org.bahmni.csv.EntityPersister;
import org.bahmni.csv.RowResult;
import org.bahmni.feed.openelis.feed.service.EventPublishers;
import org.bahmni.feed.openelis.utils.AuditingService;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.gender.daoimpl.GenderDAOImpl;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestResultPersister implements EntityPersister<CSVSample> {
    private PatientIdentityTypeDAO patientIdentityTypeDAO;
    private PatientIdentityDAO patientIdentityDAO;
    private HealthCenterDAO healthCenterDAO;
    private TestDAO testDAO;
    private SampleDAO sampleDAO;
    private List<String> healthCenterCodes;
    private AuditingService auditingService;

    private List<String> testNames;
    private static String sysUserId;
    private List<String> errorMessages;
    private SampleHumanDAO sampleHumanDAO;

    public TestResultPersister() {
        this(new HealthCenterDAOImpl(), new TestDAOImpl(), new SampleDAOImpl(), new PatientIdentityTypeDAOImpl(), new PatientIdentityDAOImpl(),
                new SampleHumanDAOImpl(), new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()));
    }

    public TestResultPersister(HealthCenterDAO healthCenterDAO, TestDAO testDAO, SampleDAO sampleDAO,
                               PatientIdentityTypeDAO patientIdentityTypeDAO, PatientIdentityDAO patientIdentityDAO,
                               SampleHumanDAO sampleHumanDAO, AuditingService auditingService) {
        this.healthCenterDAO = healthCenterDAO;
        this.testDAO = testDAO;
        this.sampleDAO = sampleDAO;
        this.patientIdentityTypeDAO = patientIdentityTypeDAO;
        this.patientIdentityDAO = patientIdentityDAO;
        this.sampleHumanDAO = sampleHumanDAO;
        this.auditingService = auditingService;

    }

    @Override
    public RowResult<CSVSample> persist(CSVSample csvSample) {
        String patientId = getPatientId(csvSample.patientRegistrationNumber);
        String sampleId = saveSample(csvSample);
        saveSampleHuman(sampleId,patientId);
        return new RowResult<>(csvSample, StringUtils.join(errorMessages, ", "));
    }

    private void saveSampleHuman(String sampleId, String patientId) {
        SampleHuman sampleHuman = new SampleHuman();
        sampleHuman.setPatientId(patientId);
        sampleHuman.setSampleId(sampleId);
        sampleHuman.setSysUserId(getSysUserId());
        sampleHumanDAO.insertData(sampleHuman);
    }

    private String getPatientId(String patientIdentity) {
        PatientIdentityType stIdentityType = patientIdentityTypeDAO.getNamedIdentityType("ST");
        List<PatientIdentity> patientIdentities = patientIdentityDAO.getPatientIdentitiesByValueAndType(patientIdentity, stIdentityType.getId());
        return patientIdentities.get(0).getPatientId();
    }

    private String saveSample(CSVSample csvSample) {
        Sample sample = new Sample();
        sample.setAccessionNumber(csvSample.accessionNumber);
        SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
        String sampleDate = csvSample.sampleDate;
        Date parsedDate = null;
        try {
            parsedDate = datetimeFormatter.parse(sampleDate);
        } catch (ParseException e) {
            errorMessages.add(e.getMessage());
        }
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        sample.setCollectionDate(timestamp);
        sample.setEnteredDate(new java.sql.Date(parsedDate.getTime()));
        sample.setReceivedTimestamp(timestamp);
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Finished));
        sample.setDomain(SystemConfiguration.getInstance().getHumanDomain());
        sample.setSysUserId(getSysUserId());
        return sampleDAO.saveSample(sample);
    }

    @Override
    public RowResult<CSVSample> validate(CSVSample csvSample) {
        StringBuilder errorMessage = new StringBuilder();
        if(isEmpty(csvSample.healthCenter) || !getHealthCenterCodes().contains(csvSample.healthCenter)) {
            errorMessage.append("Invalid Subcenter code.\n");
        }

        errorMessage.append(validateTestNames(csvSample.testResults));
        errorMessage.append(validateAtLeastOneTestIsNonEmpty(csvSample.testResults));
        errorMessage.append(validateAllTestResultsAreValid(csvSample.testResults));

        if(isEmpty(csvSample.accessionNumber)){
            errorMessage.append("AccessionNumber should not be blank.\n");
        }

        if(!csvSample.accessionNumber.matches("^[\\d-]+$")){
            errorMessage.append("AccessionNumber format is invalid.\n");
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(csvSample.sampleDate);
        } catch (ParseException e) {
            errorMessage.append("Date should be in dd-mm-yyyy format.\n");
        }
        return new RowResult<>(csvSample, errorMessage.toString());
    }

    private String getSysUserId() {
        if (sysUserId == null) {
            sysUserId = auditingService.getSysUserId();
        }
        return sysUserId;
    }

    private String validateAllTestResultsAreValid(List<CSVTestResult> testResults) {
        for (CSVTestResult testResult : testResults) {
            if(!testResult.isValid()) {
                return "All Tests should have a result.\n";
            }
        }
        return "";
    }

    private String validateAtLeastOneTestIsNonEmpty(List<CSVTestResult> testResults) {
        for (CSVTestResult testResult : testResults) {
            if(!testResult.isEmpty()) {
                return "";
            }
        }
        return "There should be atleast one Test with a Result.\n";
    }

    private String validateTestNames(List<CSVTestResult> testResults) {
        List<String> invalidTestNames = new ArrayList<>();
        for (CSVTestResult testResult : testResults) {
            if(!testResult.isEmpty() && !getTestNames().contains(testResult.test)){
                invalidTestNames.add(testResult.test);
            }
        }
        return invalidTestNames.isEmpty() ? "" : "Invalid test names: " + StringUtils.join(invalidTestNames.iterator(), ",") + ".\n";
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    private List<String> getHealthCenterCodes() {
        if(healthCenterCodes != null) {
            return healthCenterCodes;
        }
        healthCenterCodes = new ArrayList<>();
        List<HealthCenter> healthCenters = healthCenterDAO.getAll();
        for (HealthCenter healthCenter : healthCenters) {
            healthCenterCodes.add(healthCenter.getName());
        }
        return healthCenterCodes;
    }

    public List<String> getTestNames() {
        if(testNames != null) {
            return testNames;
        }
        testNames = new ArrayList<>();
        List<Test> tests = testDAO.getAllTests(false);
        for (Test test : tests) {
            testNames.add(test.getTestName());
        }
        return testNames;
    }

}

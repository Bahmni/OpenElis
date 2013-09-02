package us.mn.state.health.lims.upload.service;

import org.apache.commons.lang3.StringUtils;
import org.bahmni.csv.RowResult;
import org.bahmni.feed.openelis.utils.AuditingService;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;
import us.mn.state.health.lims.upload.sample.CSVSample;
import us.mn.state.health.lims.upload.sample.CSVTestResult;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestResultPersisterService {
    private List<String> errorMessages;
    private PatientIdentityTypeDAO patientIdentityTypeDAO;
    private PatientIdentityDAO patientIdentityDAO;
    private SampleDAO sampleDAO;
    private SampleHumanDAO sampleHumanDAO;
    private String sysUserId;
    private AuditingService auditingService;
    private SampleItemDAO sampleItemDAO;
    private TypeOfSampleTestDAO typeOfSampleTestDAO;
    private TypeOfSampleDAO typeOfSampleDAO;

    public TestResultPersisterService() {
        this(new PatientIdentityTypeDAOImpl(), new PatientIdentityDAOImpl(), new SampleDAOImpl(), new SampleHumanDAOImpl(),
                new SampleItemDAOImpl(), new TypeOfSampleTestDAOImpl(), new TypeOfSampleDAOImpl(), new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()));
    }

    public TestResultPersisterService(PatientIdentityTypeDAO patientIdentityTypeDAO, PatientIdentityDAO patientIdentityDAO,
                                      SampleDAO sampleDAO, SampleHumanDAO sampleHumanDAO, SampleItemDAO sampleItemDAO,
                                      TypeOfSampleTestDAO typeOfSampleTestDAO, TypeOfSampleDAO typeOfSampleDAO, AuditingService auditingService) {
        this.patientIdentityTypeDAO = patientIdentityTypeDAO;
        this.patientIdentityDAO = patientIdentityDAO;
        this.sampleDAO = sampleDAO;
        this.sampleHumanDAO = sampleHumanDAO;
        this.sampleItemDAO = sampleItemDAO;
        this.typeOfSampleTestDAO = typeOfSampleTestDAO;
        this.typeOfSampleDAO = typeOfSampleDAO;
        this.auditingService = auditingService;
        this.sysUserId = null;
        this.errorMessages = new ArrayList<>();
    }

    public RowResult<CSVSample> persist(CSVSample csvSample) {
        List<Test> tests = new ArrayList<>();
        Map<String,String> testResults = new HashMap<>();
        Sample sample = saveSample(csvSample);
        saveSampleHuman(sample.getId(),csvSample.patientRegistrationNumber);
        for (CSVTestResult testResult : csvSample.testResults) {
            Test test = getTest(testResult);
            tests.add(test);
//            String resultValue = getResultValue(testResult);
//            testResults.put(test.getId(), resultValue);
        }
        saveSampleItems(sample, tests);
        return new RowResult<>(csvSample, StringUtils.join(errorMessages, ", "));
    }

    protected void saveSampleItems(Sample sample, List<Test> tests) {
        List<TypeOfSample> uniqueTypeOfSamples = getUniqueSampleTypesForTests(tests);
        int sortOrder = 1;
        for (TypeOfSample typeOfSample : uniqueTypeOfSamples) {
            SampleItem sampleItem = new SampleItem();
            sampleItem.setSample(sample);
            sampleItem.setTypeOfSample(typeOfSample);
            sampleItem.setSysUserId(getSysUserId());
            sampleItem.setSortOrder(String.valueOf(sortOrder++));
            sampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
            sampleItemDAO.insertData(sampleItem);
        }
    }

    private String getPatientId(String patientIdentity) {
        PatientIdentityType stIdentityType = patientIdentityTypeDAO.getNamedIdentityType("ST");
        List<PatientIdentity> patientIdentities = patientIdentityDAO.getPatientIdentitiesByValueAndType(patientIdentity, stIdentityType.getId());
        return patientIdentities.get(0).getPatientId();
    }

    protected Sample saveSample(CSVSample csvSample) {
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

    protected void saveSampleHuman(String sampleId, String registrationNumber) {
        String patientId = getPatientId(registrationNumber);
        SampleHuman sampleHuman = new SampleHuman();
        sampleHuman.setPatientId(patientId);
        sampleHuman.setSampleId(sampleId);
        sampleHuman.setSysUserId(getSysUserId());
        sampleHumanDAO.insertData(sampleHuman);
    }

    private Test getTest(CSVTestResult testResult) {
        return null;
    }

    private List<TypeOfSample> getUniqueSampleTypesForTests(List<Test> tests) {
        List<String> uniqueTypesOfSampleId = new ArrayList();
        List<TypeOfSample> uniqueTypesOfSample = new ArrayList();
        for (Test test : tests) {
            TypeOfSampleTest typeOfSampleTest = typeOfSampleTestDAO.getTypeOfSampleTestsForTest(test.getId()).get(0);
            if (!uniqueTypesOfSampleId.contains(typeOfSampleTest.getTypeOfSampleId())) {
                String typeOfSampleId = typeOfSampleTest.getTypeOfSampleId();
                uniqueTypesOfSampleId.add(typeOfSampleId);
                uniqueTypesOfSample.add(typeOfSampleDAO.getTypeOfSampleById(typeOfSampleId));
            }
        }
        return uniqueTypesOfSample;
    }

    private String getSysUserId() {
        if (sysUserId == null) {
            sysUserId = auditingService.getSysUserId();
        }
        return sysUserId;
    }

}

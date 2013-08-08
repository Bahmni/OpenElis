package us.mn.state.health.lims.dashboard.daoimpl;

import junit.framework.Assert;
import org.bahmni.feed.openelis.IT;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.analyte.daoimpl.AnalyteDAOImpl;
import us.mn.state.health.lims.analyte.valueholder.Analyte;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.dashboard.valueholder.Order;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;
import us.mn.state.health.lims.testanalyte.daoimpl.TestAnalyteDAOImpl;
import us.mn.state.health.lims.testanalyte.valueholder.TestAnalyte;

import java.util.List;

public class OrderListDAOImplIT extends IT {

    @org.junit.Test
    public void getAllInProgress_shouldReturnAllOrdersWhichAreInProgress() {
        String accessionNumber = "05082013-001";
        String patientIdentityData = "TEST1234567";
        String firstName = "Some";
        String lastName = "One";
        Sample sample = createSample(accessionNumber);
        Patient patient = createPatient(firstName, lastName, patientIdentityData);
        createSampleHuman(sample, patient);
        SampleItem sampleItem = createSampleItem(sample);


        TestDAOImpl testDAO = new TestDAOImpl();
        createATest(testDAO, "SampleTest1");
        createATest(testDAO, "SampleTest2");
        createATest(testDAO, "SampleTest3");

        List<Test> allTests = testDAO.getAllTests(true);
        Analysis analysis_1 = createAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, "Hematology", allTests.get(0));
        Analysis analysis_2 = createAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.NotStarted, "Hematology", allTests.get(1));
        Analysis analysis_3 = createAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.NotStarted, "Hematology", allTests.get(2));

        createResult(analysis_3);

        OrderListDAOImpl orderListDAO = new OrderListDAOImpl();
        List<Order> inProgress = orderListDAO.getAllInProgress();

        Assert.assertTrue(inProgress.contains(new Order(accessionNumber, patientIdentityData, firstName, lastName, sample.getSampleSource().getName(), 2, 3)));
    }

    private Test createATest(TestDAOImpl testDAO, String testName) {
        Test test = createTest(testName);
        testDAO.insertData(test);
        TestAnalyteDAOImpl testAnalyteDAO = new TestAnalyteDAOImpl();
        testAnalyteDAO.insertData(createTestAnalyte(test, new AnalyteDAOImpl().readAnalyte("1")));
        return test;
    }

    private TestAnalyte createTestAnalyte(Test test, Analyte analyte) {
        TestAnalyte testAnalyte = new TestAnalyte();
        testAnalyte.setTest(test);
        testAnalyte.setAnalyte(analyte);
        testAnalyte.setSysUserId("1");
        testAnalyte.setResultGroup("1");
        return testAnalyte;
    }

    private Test createTest(String testName) {
        Test test = new Test();
        test.setTestName(testName);
        test.setDescription(testName);
        test.setIsActive(IActionConstants.YES);
        test.setSortOrder("1");
        test.setOrderable(Boolean.TRUE);
        test.setSysUserId("1");
        return test;
    }

    private Result createResult(Analysis analysis) {
        Result result = new Result();
        result.setSysUserId("1");
        result.setAnalysis(analysis);
        result.setValue("10");
        new ResultDAOImpl().insertData(result);
        return result;
    }

    private Patient createPatient(String firstName, String lastName, String patientIdentityData) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setSysUserId("1");
        new PersonDAOImpl().insertData(person);

        Patient patient = new Patient();
        patient.setPerson(person);
        patient.setGender("M");
        patient.setBirthDate(DateUtil.getNowAsTimestamp());
        patient.setSysUserId("1");
        patient.setLastupdated(DateUtil.getNowAsTimestamp());
        new PatientDAOImpl().insertData(patient);

        PatientIdentity patientIdentity = new PatientIdentity();
        patientIdentity.setPatientId(patient.getId());
        patientIdentity.setIdentityTypeId(PatientIdentityTypeMap.getInstance().getIDForType("ST"));
        patientIdentity.setIdentityData(patientIdentityData);
        patientIdentity.setSysUserId("1");
        new PatientIdentityDAOImpl().insertData(patientIdentity);

        return patient;
    }

    private SampleHuman createSampleHuman(Sample sample, Patient patient) {
        SampleHuman sampleHuman = new SampleHuman();
        sampleHuman.setPatientId(patient.getId());
        sampleHuman.setSampleId(sample.getId());
        sampleHuman.setSysUserId("1");

        new SampleHumanDAOImpl().insertData(sampleHuman);
        return sampleHuman;
    }


    private Analysis createAnalysis(SampleItem sampleItem, StatusOfSampleUtil.AnalysisStatus analysisStatus, String testSectionName, Test test) {
        TestSectionDAO testSectionDAO = new TestSectionDAOImpl();
        TestSection testSection = testSectionDAO.getTestSectionByName(testSectionName);


        Analysis analysis = new Analysis();
        analysis.setSampleItem(sampleItem);
        analysis.setAnalysisType(IActionConstants.ANALYSIS_TYPE_MANUAL);
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(analysisStatus));
        analysis.setTest(test);
        analysis.setSysUserId("1");
        analysis.setTestSection(testSection);
        new AnalysisDAOImpl().insertData(analysis, false);

        return analysis;
    }

    private SampleItem createSampleItem(Sample startedSample) {
        SampleItem enteredSampleItem = new SampleItem();
        enteredSampleItem.setSample(startedSample);
        enteredSampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
        enteredSampleItem.setSortOrder("1");
        enteredSampleItem.setSysUserId("1");
        new SampleItemDAOImpl().insertData(enteredSampleItem);
        return enteredSampleItem;
    }

    private Sample createSample(String accessionNumber) {
        List<SampleSource> sampleSources = new SampleSourceDAOImpl().getAll();
        Sample sample = new Sample();
        sample.setAccessionNumber(accessionNumber);
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Started));
        sample.setEnteredDate(DateUtil.convertStringDateToSqlDate("01/01/2001"));
        sample.setReceivedTimestamp(DateUtil.convertStringDateToTimestamp("01/01/2001 00:00"));
        sample.setSampleSource(sampleSources.get(0));
        sample.setSysUserId("1");
        new SampleDAOImpl().insertDataWithAccessionNumber(sample);
        return sample;
    }

}

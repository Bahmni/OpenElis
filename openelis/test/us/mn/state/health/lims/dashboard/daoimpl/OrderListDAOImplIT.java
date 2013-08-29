package us.mn.state.health.lims.dashboard.daoimpl;

import org.bahmni.feed.openelis.IT;
import org.junit.Before;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.analyte.daoimpl.AnalyteDAOImpl;
import us.mn.state.health.lims.analyte.valueholder.Analyte;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.dashboard.valueholder.Order;
import us.mn.state.health.lims.dashboard.valueholder.TodayStat;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.*;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.*;

public class OrderListDAOImplIT extends IT {

    private String accessionNumber;
    private String accessionNumber2;
    private String accessionNumber3;
    private String patientIdentityData;
    private String firstName;
    private String lastName;
    private Patient patient;
    private List<Test> allTests;

    @Before
    public void setUp() throws Exception {
        accessionNumber = "05082013-001";
        accessionNumber2 = "05082013-002";
        accessionNumber3 = "05082013-003";
        patientIdentityData = "TEST1234567";
        firstName = "Some";
        lastName = "One";
        patient = createPatient(firstName, lastName, patientIdentityData);
        TestDAOImpl testDAO = createTests("SampleTest1", "SampleTest2", "SampleTest3", "SampleTest4");
        allTests = testDAO.getAllTests(true);
    }

    @org.junit.Test
    public void getAllInProgress_shouldReturnAllOrdersWhichAreInProgress() throws ParseException {
        Sample sample = createSample(accessionNumber, false);
        createSampleHuman(sample, patient);
        SampleItem sampleItem = createSampleItem(sample);
        createAnalysis(sampleItem, TechnicalAcceptance, "Hematology", allTests.get(0));
        createAnalysis(sampleItem, NotTested, "Hematology", allTests.get(1));
        createAnalysis(sampleItem, ReferedOut, "Hematology", allTests.get(2));
        createAnalysis(sampleItem, BiologistRejected, "Hematology", allTests.get(3));

        List<Order> inProgress = new OrderListDAOImpl().getAllInProgress();

        assertTrue(inProgress.contains(new Order(accessionNumber, patientIdentityData, firstName, lastName, sample.getSampleSource().getName(), 2, 1, 3)));
    }

    @org.junit.Test
    public void getAllCompleted_shouldReturnAllOrdersWhichAreCompletedBefore24Hours() throws ParseException {
        Sample sample1 = createSample(accessionNumber, true);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1);

        Sample sample2 = createSample(accessionNumber2, true);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2);

        Sample sample3 = createSample(accessionNumber3, false);
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(0));
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(1));

        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.NotTested, "Hematology", allTests.get(1));
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(2));

        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(0));

        List<Order> completedOrders = new OrderListDAOImpl().getAllCompletedBefore24Hours();

        assertTrue(completedOrders.size() >= 1);

        List<String> accessionNumbers = accessionNumbersIn(completedOrders);
        assertTrue(accessionNumbers.contains(accessionNumber));
        assertFalse(accessionNumbers.contains(accessionNumber2));
        assertFalse(accessionNumbers.contains(accessionNumber3));
    }

    @org.junit.Test
    public void shouldNotAddCompletedOrdersToInProgressList()  {
        Sample sample1 = createSample(accessionNumber, false);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1);

        Sample sample2 = createSample(accessionNumber2, false);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(0));
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.Canceled, "Hematology", allTests.get(1));
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.NotTested, "Hematology", allTests.get(1));

        List<Order> inProgressOrder = new OrderListDAOImpl().getAllInProgress();

        assertTrue(inProgressOrder.size() >= 1);
        assertFalse(accessionNumbersIn(inProgressOrder).contains(accessionNumber));
    }

    private List<String> accessionNumbersIn(List<Order> inProgressOrder) {
        List<String> accessionNumbers = new ArrayList<>();
        for (Order order : inProgressOrder) {
            accessionNumbers.add(order.getAccessionNumber());
        }
        return accessionNumbers;
    }

    @org.junit.Test
    public void shouldShowTodaysStat() {
        Sample sample1 = createSample(accessionNumber, true);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1);

        Sample sample2 = createSample(accessionNumber2, true);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2);

        Sample sample3 = createSample(accessionNumber3, false);
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3);

        createAnalysis(sampleItem1,StatusOfSampleUtil.AnalysisStatus.NotTested,"Hematology",allTests.get(0));
        createAnalysis(sampleItem2,StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance,"Hematology",allTests.get(1));
        createAnalysis(sampleItem2,StatusOfSampleUtil.AnalysisStatus.Finalized,"Hematology",allTests.get(2));
        createAnalysis(sampleItem3,StatusOfSampleUtil.AnalysisStatus.Finalized,"Hematology",allTests.get(3));

        TodayStat todayStats = new OrderListDAOImpl().getTodayStats();

        assertEquals(1,todayStats.getAwaitingTestCount());
        assertEquals(1,todayStats.getAwaitingValidationCount());
        assertEquals(0,todayStats.getCompletedTestCount());
        assertEquals(2,todayStats.getTotalSamplesCount());
    }

    private TestDAOImpl createTests(String... samples) {
        TestDAOImpl testDAO = new TestDAOImpl();
        for (String sample : samples) {
            createATest(testDAO, sample);
        }
        return testDAO;
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

    private Sample createSample(String accessionNumber, boolean forToday) {
        List<SampleSource> sampleSources = new SampleSourceDAOImpl().getAll();
        Sample sample = new Sample();
        sample.setAccessionNumber(accessionNumber);
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Started));
        sample.setEnteredDate(DateUtil.convertStringDateToSqlDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        sample.setReceivedTimestamp(DateUtil.convertStringDateToTimestamp("01/01/2001 00:00"));
        sample.setSampleSource(sampleSources.get(0));
        sample.setSysUserId("1");
        if(!forToday){
            sample.setLastupdated(DateUtil.convertStringDateToTimestamp("08/08/2013 00:00:00"));
        }
        new SampleDAOImpl().insertDataWithAccessionNumber(sample);
        return sample;
    }
}

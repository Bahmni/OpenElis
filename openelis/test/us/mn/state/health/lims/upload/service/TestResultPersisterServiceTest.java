package us.mn.state.health.lims.upload.service;

import org.bahmni.feed.openelis.utils.AuditingService;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;
import us.mn.state.health.lims.upload.sample.CSVSample;
import us.mn.state.health.lims.upload.sample.CSVTestResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestResultPersisterServiceTest {
    @Mock
    private AnalysisDAO analysisDAO;
    @Mock
    private SampleDAO sampleDAO;
    @Mock
    private SampleHumanDAO sampleHumanDAO;
    @Mock
    private PatientIdentityDAO patientIdentityDAO;
    @Mock
    private PatientIdentityTypeDAO patientIdentityTypeDAO;
    @Mock
    private ResultDAO resultDAO;
    @Mock
    private AuditingService auditingService;
    @Mock
    private TypeOfSampleTestDAO typeOfSampleTestDAO;
    @Mock
    private SampleItemDAO sampleItemDAO;
    @Mock
    private TestDAO testDAO;
    @Mock
    private TypeOfSampleDAO typeOfSampleDAO;

    private String subscenterNameGAN;
    private String accessionNumber;
    private String testName1;
    private String testName2;
    private List<CSVTestResult> testResults;
    private TestResultPersisterService testResultPersisterService;
    private String testId1;
    private String testId2;
    private Test test1;
    private Test test2;
    private String typeOfSampleId1;
    private String typeOfSampleId2;
    private SampleItem sampleItem1;
    private SampleItem sampleItem2;
    private String sysUserId;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        sysUserId = "123";
        subscenterNameGAN = "GAN";
        accessionNumber = "123456-001";
        testName1 = "Test1";
        testName2 = "Test2";
        testId1 = "1";
        testId2 = "2";
        test1 = new Test();
        test1.setId(testId1);
        test2 = new Test();
        test2.setId(testId2);
        typeOfSampleId1 = "1";
        typeOfSampleId2 = "2";
        sampleItem1 = new SampleItem();
        sampleItem1.setId("1");
        sampleItem2 = new SampleItem();
        sampleItem2.setId("2");
        testResults = Arrays.asList(new CSVTestResult(testName1, "someValueForValue1"), new CSVTestResult(testName2, "someValueForTest2"));

        testResultPersisterService = new TestResultPersisterServiceMock(patientIdentityTypeDAO, patientIdentityDAO, sampleDAO, sampleHumanDAO, sampleItemDAO, typeOfSampleTestDAO, typeOfSampleDAO, auditingService);
    }

    @org.junit.Test
    public void shouldPersistSampleForPersistingTestResults() throws ParseException {
        String sampleDate = "25-02-2012";
        String sysUserId = "123";
        PatientIdentityType stIdentityType = new PatientIdentityType();
        stIdentityType.setId("stIdentityId");
        String patientRegistrationNumber = "patientRegistrationNumber";
        PatientIdentity patientIdentity = new PatientIdentity();
        String patientId = "1";
        Sample sample = new Sample();
        sample.setId("123");
        patientIdentity.setPatientId(patientId);
        CSVSample csvSample_invalidDate = new CSVSample(subscenterNameGAN, "patientRegistrationNumber", accessionNumber, sampleDate, "", testResults);

        when(auditingService.getSysUserId()).thenReturn(sysUserId);
        when(patientIdentityTypeDAO.getNamedIdentityType(anyString())).thenReturn(stIdentityType);
        when(patientIdentityDAO.getPatientIdentitiesByValueAndType(patientRegistrationNumber, stIdentityType.getId())).thenReturn(Arrays.asList(patientIdentity));

        testResultPersisterService.saveSample(csvSample_invalidDate);

        ArgumentCaptor<Sample> sampleCaptor = ArgumentCaptor.forClass(Sample.class);
        verify(sampleDAO).insertDataWithAccessionNumber(sampleCaptor.capture());
        Sample persistedSample = sampleCaptor.getValue();
        assertEquals(accessionNumber, persistedSample.getAccessionNumber());
        assertEquals(sysUserId, persistedSample.getSysUserId());
        assertEquals(sampleDate, new SimpleDateFormat("dd-MM-yyyy").format(persistedSample.getCollectionDate()));
        assertEquals(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Finished), persistedSample.getStatusId());
        assertEquals("H", persistedSample.getDomain());
    }

    @org.junit.Test
    public void shouldPersistSampleHumanForPersistingTestResult() throws Exception {
        String sysUserId = "123";
        String sampleId = "12";
        PatientIdentityType stIdentityType = new PatientIdentityType();
        stIdentityType.setId("stIdentityId");
        String patientRegistrationNumber = "patientRegistrationNumber";
        PatientIdentity patientIdentity = new PatientIdentity();
        String patientId = "1";
        patientIdentity.setPatientId(patientId);
        Sample sample = new Sample();
        sample.setId(sampleId);

        when(auditingService.getSysUserId()).thenReturn(sysUserId);
        when(patientIdentityTypeDAO.getNamedIdentityType(anyString())).thenReturn(stIdentityType);
        when(patientIdentityDAO.getPatientIdentitiesByValueAndType(patientRegistrationNumber, stIdentityType.getId())).thenReturn(Arrays.asList(patientIdentity));

        testResultPersisterService.saveSampleHuman(sampleId, patientRegistrationNumber);

        ArgumentCaptor<SampleHuman> captor = ArgumentCaptor.forClass(SampleHuman.class);
        verify(sampleHumanDAO).insertData(captor.capture());
        SampleHuman sampleHuman = captor.getValue();
        assertEquals(sampleId, sampleHuman.getSampleId());
        assertEquals(patientId, sampleHuman.getPatientId());
    }

    @org.junit.Test
    public void testPersistSampleItems() {
        String statusID = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered);
        TypeOfSampleTest typeOfSampleTest1 = new TypeOfSampleTest();
        typeOfSampleTest1.setTypeOfSampleId(typeOfSampleId1);
        typeOfSampleTest1.setTestId(testId1);
        TypeOfSampleTest typeOfSampleTest2 = new TypeOfSampleTest();
        typeOfSampleTest2.setTypeOfSampleId(typeOfSampleId2);
        typeOfSampleTest2.setTestId(testId2);
        Sample sample = new Sample();
        sample.setId("12");
        TypeOfSample typeOfSample1 = new TypeOfSample();
        typeOfSample1.setId(typeOfSampleId1);
        TypeOfSample typeOfSample2 = new TypeOfSample();
        typeOfSample2.setId(typeOfSampleId2);

        when(auditingService.getSysUserId()).thenReturn(sysUserId);
        when(testDAO.getTestByName(testName1)).thenReturn(test1);
        when(testDAO.getTestByName(testName2)).thenReturn(test2);
        when(typeOfSampleTestDAO.getTypeOfSampleTestsForTest(test1.getId())).thenReturn(Arrays.asList(typeOfSampleTest1));
        when(typeOfSampleTestDAO.getTypeOfSampleTestsForTest(test2.getId())).thenReturn(Arrays.asList(typeOfSampleTest2));
        when(typeOfSampleDAO.getTypeOfSampleById(typeOfSample1.getId())).thenReturn(typeOfSample1);
        when(typeOfSampleDAO.getTypeOfSampleById(typeOfSample2.getId())).thenReturn(typeOfSample2);

        testResultPersisterService.saveSampleItems(sample, Arrays.asList(test1, test2));

        ArgumentCaptor<SampleItem> captor = ArgumentCaptor.forClass(SampleItem.class);
        verify(sampleItemDAO, times(2)).insertData(captor.capture());
        List<SampleItem> persistedSampleItems = captor.getAllValues();

        SampleItem item1 = persistedSampleItems.get(0);
        assertEquals(sample.getId(), item1.getSample().getId());
        assertEquals(typeOfSample1.getId(), item1.getTypeOfSample().getId());
        assertEquals(sysUserId, item1.getSysUserId());
        assertEquals("1", item1.getSortOrder());
        assertEquals(statusID, item1.getStatusId());

        SampleItem item2 = persistedSampleItems.get(1);
        assertEquals(sample.getId(), item2.getSample().getId());
        assertEquals(typeOfSample2.getId(), item2.getTypeOfSample().getId());
        assertEquals(sysUserId, item2.getSysUserId());
        assertEquals("2", item2.getSortOrder());
        assertEquals(statusID, item2.getStatusId());
    }

    @org.junit.Test
    public void shouldPersistAnalysis() throws ParseException {
        String sampleDate = "25-02-2012";
        SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate = datetimeFormatter.parse(sampleDate);
        java.sql.Date analysisDate = new java.sql.Date(parsedDate.getTime());
        when(auditingService.getSysUserId()).thenReturn(sysUserId);

        testResultPersisterService.saveAnalysis(Arrays.asList(test1, test2), sampleDate);

        ArgumentCaptor<Analysis> captor = ArgumentCaptor.forClass(Analysis.class);
        verify(analysisDAO, times(2)).insertData(captor.capture(), anyBoolean());

        List<Analysis> savedAnalysis = captor.getAllValues();
        Analysis analysis1 = savedAnalysis.get(0);
        assertEquals(sysUserId, analysis1.getSysUserId());
        assertEquals(test1.getId(), analysis1.getTest().getId());
        assertEquals(sampleItem1, analysis1.getSampleItem());
        assertEquals(analysisDate, analysis1.getCompletedDate());
        assertEquals(analysisDate, analysis1.getStartedDate());
        assertEquals("MANUAL", analysis1.getAnalysisType());
        assertEquals(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Finalized), analysis1.getStatusId());

        Analysis analysis2 = savedAnalysis.get(1);
        assertEquals(test2.getId(), analysis2.getTest().getId());
        assertEquals(sampleItem2, analysis2.getSampleItem());
    }

    private class TestResultPersisterServiceMock extends TestResultPersisterService {
        private TestResultPersisterServiceMock(PatientIdentityTypeDAO patientIdentityTypeDAO, PatientIdentityDAO patientIdentityDAO,
                                               SampleDAO sampleDAO, SampleHumanDAO sampleHumanDAO, SampleItemDAO sampleItemDao,
                                               TypeOfSampleTestDAO typeOfSampleTestDAO, TypeOfSampleDAO typeOfSampleDao, AuditingService auditingService) {
            super(patientIdentityTypeDAO, patientIdentityDAO, sampleDAO, sampleHumanDAO, sampleItemDao, typeOfSampleTestDAO, typeOfSampleDao, analysisDAO, auditingService);
            this.testToTypeOfSampleMap.put(testId1, typeOfSampleId1);
            this.testToTypeOfSampleMap.put(testId2, typeOfSampleId2);
            this.typeOfSampleToSampleItemMap.put(typeOfSampleId1, sampleItem1);
            this.typeOfSampleToSampleItemMap.put(typeOfSampleId2, sampleItem2);
        }

        @Override
        protected void saveSampleItems(Sample sample, List<Test> tests) {
            super.saveSampleItems(sample, tests);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected Sample saveSample(CSVSample csvSample) throws ParseException {
            return super.saveSample(csvSample);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected void saveSampleHuman(String sampleId, String registrationNumber) {
            super.saveSampleHuman(sampleId, registrationNumber);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected void saveAnalysis(List<Test> tests, String sampleDate) throws ParseException {
            super.saveAnalysis(tests, sampleDate);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}

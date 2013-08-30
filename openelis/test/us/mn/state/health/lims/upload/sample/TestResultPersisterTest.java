package us.mn.state.health.lims.upload.sample;

import org.bahmni.csv.RowResult;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestResultPersisterTest {

    @Mock
    private HealthCenterDAO healthCenterDAO;
    @Mock
    private TestDAO testDAO;
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

    private TestResultPersister testResultPersister;
    private String validSubscenterNameGAN;
    private List<CSVTestResult> validTestResults;
    private String validAccessionNumber;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        validSubscenterNameGAN = "GAN";
        validAccessionNumber = "123456-001";
        validTestResults = Arrays.asList(new CSVTestResult("Test1", "someValueForValue1"), new CSVTestResult("Test2", "someValueForTest2"));

        when(healthCenterDAO.getAll()).thenReturn(Arrays.asList(new HealthCenter(validSubscenterNameGAN, "Ganiyari"), new HealthCenter("SEM", "Semariya")));
        when(testDAO.getAllTests(false)).thenReturn(Arrays.asList(createTest("Test1"), createTest("Test2")));

        testResultPersister = new TestResultPersister(healthCenterDAO, testDAO, sampleDAO, auditingService);
    }

    @org.junit.Test
    public void shouldCheckForValidSubcenterCode() throws Exception {
        CSVSample csvSample_InvalidSubcenterCode = new CSVSample("inValidSubcenterCode", "patientRegistrationNumber", validAccessionNumber, "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(csvSample_InvalidSubcenterCode);

        assertFalse("Invalid subcenter code", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("Invalid Subcenter code"));
    }

    @org.junit.Test
    public void shouldCheckSubcenterCodeIsNotEmpty() throws Exception {
        CSVSample csvSample_EmptySubcenterCode = new CSVSample("", "patientRegistrationNumber", validAccessionNumber, "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(csvSample_EmptySubcenterCode);

        assertFalse("Invalid subcenter code", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("Invalid Subcenter code"));
    }

    @org.junit.Test
    public void shouldCheckForCorrectnessOfTestName() throws Exception {
        List<CSVTestResult> invalidTestResults = Arrays.asList(new CSVTestResult("invalidTestName1", "valueForInvalidTest"), new CSVTestResult("invalidTestName2", "valueForInvalidTest"), validTestResults.get(0));
        CSVSample csvSample_InvalidTestName = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", validAccessionNumber, "25-02-2012", invalidTestResults);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(csvSample_InvalidTestName);

        assertFalse("validation should fail because of invalid test name", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("Invalid test names: invalidTestName1,invalidTestName2"));
    }

    @org.junit.Test
    public void rowWithAtleastOneNonEmptyTestResult_ShouldBeValid() throws Exception {
        CSVTestResult emptyTestResult = new CSVTestResult("", "");
        List<CSVTestResult> testResultsWithOneEmptyTestResult = Arrays.asList(validTestResults.get(0), emptyTestResult, validTestResults.get(1));
        CSVSample csvSample_InvalidTestName = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", validAccessionNumber, "25-02-2012", testResultsWithOneEmptyTestResult);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(csvSample_InvalidTestName);

        assertTrue(sampleRowResult.isSuccessful());
    }

    @org.junit.Test
    public void rowWithAllEmptyTestResult_ShouldBeInValid() throws Exception {
        CSVTestResult emptyTestResult = new CSVTestResult("", "");
        List<CSVTestResult> testResultsWithAllEmptyTestResult = Arrays.asList(emptyTestResult);
        CSVSample csvSample_InvalidTestName = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", validAccessionNumber, "25-02-2012", testResultsWithAllEmptyTestResult);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(csvSample_InvalidTestName);

        assertFalse("validation should fail because of all empty testResults", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("There should be atleast one Test with a Result"));
    }

    @org.junit.Test
    public void allTestShouldHaveSomeResults() throws Exception {
        CSVTestResult invalidTestResult = new CSVTestResult(validTestResults.get(0).test, "");
        List<CSVTestResult> testResultsWithOneInvalidTestResult = Arrays.asList(invalidTestResult, validTestResults.get(1));
        CSVSample csvSample_InvalidTestName = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", validAccessionNumber, "25-02-2012", testResultsWithOneInvalidTestResult);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(csvSample_InvalidTestName);

        assertFalse("validation should fail because of invalid test result", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("All Tests should have a result"));
    }

    @org.junit.Test
    public void accessionNumberShouldNotBeBlank() throws Exception {
        CSVSample csvSample_blankAccessionNumber = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", "", "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(csvSample_blankAccessionNumber);

        assertFalse("validation should fail because of blank accession number", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("AccessionNumber should not be blank"));
    }

    @org.junit.Test
    public void accessionNumberShouldBeInAValidFormat() throws Exception {
        CSVSample csvSample_blankAccessionNumber = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", "invalidAccessionNumber", "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(csvSample_blankAccessionNumber);

        assertFalse("validation should fail because of invalid accession number", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("AccessionNumber format is invalid"));
    }

    @org.junit.Test
    public void sampleDateShouldBeOfValidFormatOf_dd_mm_yyyy() throws Exception {
        CSVSample csvSample_invalidDate = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", "invalidAccessionNumber", "02-25-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(csvSample_invalidDate);

        assertFalse("validation should fail because of invalid date format", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("Date should be in dd-mm-yyyy format"));
    }

    @org.junit.Test
    public void shouldBeValid() throws Exception {
        CSVSample validCsvSample = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", validAccessionNumber, "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = testResultPersister.validate(validCsvSample);

        assertTrue("validation should pass", sampleRowResult.isSuccessful());
    }

    @org.junit.Test
    public void shouldPersistAllEntitiesForPersistingTestResults() throws ParseException {
        String sampleDate = "25-02-2012";
        String sysUserId = "123";
        CSVSample csvSample_invalidDate = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", validAccessionNumber, sampleDate, validTestResults);
        when(auditingService.getSysUserId()).thenReturn(sysUserId);

        testResultPersister.persist(csvSample_invalidDate);

        ArgumentCaptor<Sample> sampleCaptor = ArgumentCaptor.forClass(Sample.class);
        verify(sampleDAO).insertData(sampleCaptor.capture());
        Sample persistedSample = sampleCaptor.getValue();
        assertEquals(validAccessionNumber, persistedSample.getAccessionNumber());
        assertEquals(sysUserId,persistedSample.getSysUserId());
        assertEquals(sampleDate, new SimpleDateFormat("dd-MM-yyyy").format(persistedSample.getCollectionDate()));
        assertEquals(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Finished), persistedSample.getStatusId());
        assertEquals("H", persistedSample.getDomain());
    }

    private Test createTest(final String testName) {
        return new Test() {{
            this.setTestName(testName);
        }};
    }
}

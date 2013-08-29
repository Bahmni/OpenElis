package us.mn.state.health.lims.upload.sample;

import org.bahmni.csv.RowResult;
import org.junit.Before;
import org.mockito.Mock;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.valueholder.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SamplePersisterTest {

    @Mock
    private HealthCenterDAO healthCenterDAO;
    @Mock
    private TestDAO testDAO;

    private SamplePersister samplePersister;
    private String validSubscenterNameGAN;
    private String validTestName;
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

        samplePersister = new SamplePersister(healthCenterDAO, testDAO, null);
    }

    @org.junit.Test
    public void shouldCheckForValidSubcenterCode() throws Exception {
        CSVSample csvSample_InvalidSubcenterCode = new CSVSample("inValidSubcenterCode", "patientRegistrationNumber", validAccessionNumber, "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_InvalidSubcenterCode);

        assertFalse("Invalid subcenter code", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("Invalid Subcenter code"));
    }

    @org.junit.Test
    public void shouldCheckSubcenterCodeIsNotEmpty() throws Exception {
        CSVSample csvSample_EmptySubcenterCode = new CSVSample("", "patientRegistrationNumber", validAccessionNumber, "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_EmptySubcenterCode);

        assertFalse("Invalid subcenter code", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("Invalid Subcenter code"));
    }

    @org.junit.Test
    public void shouldCheckForCorrectnessOfTestName() throws Exception {
        List<CSVTestResult> invalidTestResults = Arrays.asList(new CSVTestResult("invalidTestName1", "valueForInvalidTest"), new CSVTestResult("invalidTestName2", "valueForInvalidTest"), validTestResults.get(0));
        CSVSample csvSample_InvalidTestName = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", validAccessionNumber, "25-02-2012", invalidTestResults);
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_InvalidTestName);

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
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_InvalidTestName);

        assertTrue(sampleRowResult.isSuccessful());
    }

    @org.junit.Test
    public void rowWithAllEmptyTestResult_ShouldBeInValid() throws Exception {
        CSVTestResult emptyTestResult = new CSVTestResult("", "");
        List<CSVTestResult> testResultsWithAllEmptyTestResult = Arrays.asList(emptyTestResult);
        CSVSample csvSample_InvalidTestName = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", validAccessionNumber, "25-02-2012", testResultsWithAllEmptyTestResult);
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_InvalidTestName);

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
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_InvalidTestName);

        assertFalse("validation should fail because of invalid test result", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("All Tests should have a result"));
    }

    @org.junit.Test
    public void accessionNumberShouldNotBeBlank() throws Exception {
        CSVSample csvSample_blankAccessionNumber = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", "", "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_blankAccessionNumber);

        assertFalse("validation should fail because of blank accession number", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("AccessionNumber should not be blank"));
    }

    @org.junit.Test
    public void accessionNumberShouldBeInAValidFormat() throws Exception {
        CSVSample csvSample_blankAccessionNumber = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", "invalidAccessionNumber", "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_blankAccessionNumber);

        assertFalse("validation should fail because of invalid accession number", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("AccessionNumber format is invalid"));
    }

    @org.junit.Test
    public void sampleDateShouldBeOfValidFormatOf_dd_mm_yyyy() throws Exception {
        CSVSample csvSample_invalidDate = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", "invalidAccessionNumber", "02-25-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_invalidDate);

        assertFalse("validation should fail because of invalid date format", sampleRowResult.isSuccessful());

        String[] rowWithErrorColumn = sampleRowResult.getRowWithErrorColumn();
        String erroMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        assertTrue("Error message incorrect", erroMessage.contains("Date should be in dd-mm-yyyy format"));
    }

    @org.junit.Test
    public void shouldBeValid() throws Exception {
        CSVSample csvSample_invalidDate = new CSVSample(validSubscenterNameGAN, "patientRegistrationNumber", validAccessionNumber, "25-02-2012", validTestResults);
        RowResult<CSVSample> sampleRowResult = samplePersister.validate(csvSample_invalidDate);

        assertTrue("validation should pass", sampleRowResult.isSuccessful());
    }

    private Test createTest(final String testName) {
        return new Test() {{
            this.setTestName(testName);
        }};
    }
}

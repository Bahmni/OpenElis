package us.mn.state.health.lims.upload.service;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.resultlimits.dao.ResultLimitDAO;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.typeoftestresult.dao.TypeOfTestResultDAO;
import us.mn.state.health.lims.typeoftestresult.valueholder.TypeOfTestResult;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ResultPersisterServiceTest {
    @Mock
    private TestResultDAO testResultDAO;
    @Mock
    private ResultsLoadUtility utility;
    private String sysUserId;
    private String testId1;
    private String testId2;
    private String testId3;
    private Test test1;
    private Test test2;
    private Test test3;
    private SampleItem sampleItem1;
    private SampleItem sampleItem2;
    private ResultPersisterService resultPersisterService;
    private TypeOfTestResult typeOfTestResult1;
    private TypeOfTestResult typeOfTestResult2;
    @Mock
    private ResultDAO resultDAO;
    @Mock
    private DictionaryDAO dictionaryDAO;
    @Mock
    private ResultLimitDAO resultLimitDAO;
    @Mock
    private TypeOfTestResultDAO typeOfTestResultDAO;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        sysUserId = "123";
        testId1 = "1";
        testId2 = "2";
        testId3 = "3";
        test1 = new Test();
        test1.setId(testId1);
        test2 = new Test();
        test2.setId(testId2);
        test3 = new Test();
        test3.setId(testId3);
        sampleItem1 = new SampleItem();
        sampleItem1.setId("1");
        sampleItem2 = new SampleItem();
        sampleItem2.setId("2");
        typeOfTestResult1 = new TypeOfTestResult();
        typeOfTestResult1.setTestResultType("N");
        typeOfTestResult1.setId("1");
        typeOfTestResult2 = new TypeOfTestResult();
        typeOfTestResult2.setTestResultType("D");
        typeOfTestResult2.setId("2");

        resultPersisterService = new ResultPersisterService(testResultDAO, dictionaryDAO, resultDAO, resultLimitDAO, typeOfTestResultDAO, utility);
    }

    @org.junit.Test
    public void shouldPersistTestResultsOfNumericType() {
        String numericResultValue = "10";
        Analysis analysis1 = new Analysis();
        analysis1.setId("1");
        Patient patient = new Patient();
        double lowNormal = 10.0;
        double highNormal = 20.0;
        ResultLimit resultLimit = new ResultLimit();
        resultLimit.setLowNormal(lowNormal);
        resultLimit.setHighNormal(highNormal);
        resultLimit.setResultTypeId("1");
        resultLimit.setTestId(testId1);


        when(testResultDAO.getTestResultsByTest(testId1)).thenReturn(null);
        when(resultLimitDAO.getAllResultLimitsForTest(testId1)).thenReturn(Arrays.asList(resultLimit));
        when(typeOfTestResultDAO.getAllTypeOfTestResults()).thenReturn(Arrays.asList(typeOfTestResult1,typeOfTestResult2));
        when(utility.getResultLimitForTestAndPatient(test1, patient)).thenReturn(resultLimit);

        resultPersisterService.save(analysis1, test1, numericResultValue, patient, sysUserId);

        ArgumentCaptor<Result> captor = ArgumentCaptor.forClass(Result.class);
        verify(resultDAO).insertData(captor.capture());
        Result savedResult = captor.getValue();
        assertEquals(numericResultValue, savedResult.getValue());
        assertEquals(analysis1.getId(), savedResult.getAnalysisId());
        assertEquals(resultLimit.getHighNormal(), savedResult.getMaxNormal());
        assertEquals(resultLimit.getLowNormal(), savedResult.getMinNormal());
    }

    @org.junit.Test
    public void shouldPersistTestResultsOfDictionaryType() {
        String dictionaryResultValue = "positive";
        Analysis analysis = new Analysis();
        analysis.setId("2");
        Patient patient = null;
        TestResult dictionaryTestResult = new TestResult();
        dictionaryTestResult.setTestResultType("D");
        String dictId = "153";
        dictionaryTestResult.setValue(dictId);
        Dictionary dictionary = new Dictionary();
        dictionary.setId(dictId);
        dictionary.setDictEntry(dictionaryResultValue);

        when(testResultDAO.getTestResultsByTest(testId2)).thenReturn(Arrays.asList(dictionaryTestResult));
        when(resultLimitDAO.getAllResultLimitsForTest(testId2)).thenReturn(null);
        when(dictionaryDAO.getDictionaryByDictEntry(dictionaryResultValue)).thenReturn(dictionary);
        resultPersisterService.save(analysis, test2, dictionaryResultValue, patient, sysUserId);

        ArgumentCaptor<Result> captor = ArgumentCaptor.forClass(Result.class);
        verify(resultDAO).insertData(captor.capture());
        Result savedResult = captor.getValue();
        assertEquals(dictId, savedResult.getValue());
        assertEquals(analysis.getId(), savedResult.getAnalysisId());
    }

    @org.junit.Test
    public void shouldPersistTestResultsOfRemarkType() {
        Patient patient = null;
        String remarkResultValue = "abcdefg";
        Analysis analysis = new Analysis();
        analysis.setId("2");
        TestResult remarkTestResult = new TestResult();
        remarkTestResult.setTestResultType("R");

        when(testResultDAO.getTestResultsByTest(testId3)).thenReturn(Arrays.asList(remarkTestResult));
        when(resultLimitDAO.getAllResultLimitsForTest(testId3)).thenReturn(null);
        resultPersisterService.save(analysis, test3, remarkResultValue, patient, sysUserId);

        ArgumentCaptor<Result> captor = ArgumentCaptor.forClass(Result.class);
        verify(resultDAO).insertData(captor.capture());
        Result savedResult = captor.getValue();
        assertEquals(remarkResultValue, savedResult.getValue());
        assertEquals(analysis.getId(), savedResult.getAnalysisId());
    }

}

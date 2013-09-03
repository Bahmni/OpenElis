package us.mn.state.health.lims.upload.service;

import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;

import java.util.List;

public class ResultPersisterService {
    private TestResultDAO testResultDAO;
    private DictionaryDAO dictionaryDAO;
    private ResultDAO resultDAO;
    private ResultsLoadUtility resultsLoadUtility;

    public ResultPersisterService() {
        this(new TestResultDAOImpl(),new DictionaryDAOImpl(), new ResultDAOImpl(), new ResultsLoadUtility());
    }

    public ResultPersisterService(TestResultDAO testResultDAO, DictionaryDAO dictionaryDAO, ResultDAO resultDAO, ResultsLoadUtility resultsLoadUtility) {
        this.testResultDAO = testResultDAO;
        this.dictionaryDAO = dictionaryDAO;
        this.resultDAO = resultDAO;
        this.resultsLoadUtility = resultsLoadUtility;
    }

    protected void save(Analysis analysis, Test test, String testResultValue, Patient patient, String sysUserId) {
        List<TestResult> testResults = testResultDAO.getTestResultsByTest(test.getId());
        Result result = new Result();
        result.setSysUserId(sysUserId);
        result.setAnalysis(analysis);

        for (TestResult testResult : testResults) {
            String testResultType = testResult.getTestResultType();
            result.setResultType(testResultType);
            switch (testResultType) {
                case "N":
                    saveNumericTestResult(result, testResult, testResultValue, test, patient);
                    break;
                case "R":
                    saveRemarkTestResult(result, testResult, testResultValue);
                    break;
                case "D":
                    Dictionary dictionary = dictionaryDAO.getDictionaryByDictEntry(testResultValue);
                    if (dictionary.getId().equals(testResult.getValue())) {
                        saveDictionaryTestResult(result, testResult);
                    }
                    break;
            }
        }
    }

    private void saveDictionaryTestResult(Result result, TestResult testResult) {
        result.setValue(testResult.getValue());
        result.setTestResult(testResult);
        resultDAO.insertData(result);
    }

    private void saveRemarkTestResult(Result result, TestResult testResult, String testResultValue) {
        saveTestResultValue(result, testResult, testResultValue);
    }

    private void saveNumericTestResult(Result result, TestResult testResult, String testResultValue, Test test, Patient patient) {
        ResultLimit resultLimit = resultsLoadUtility.getResultLimitForTestAndPatient(test, patient);
        result.setMaxNormal(resultLimit.getHighNormal());
        result.setMinNormal(resultLimit.getLowNormal());
        saveTestResultValue(result, testResult, testResultValue);
    }

    private void saveTestResultValue(Result result, TestResult testResult, String testResultValue) {
        result.setValue(testResultValue);
        result.setTestResultId(testResult.getId());
        resultDAO.insertData(result);
    }
}

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
import us.mn.state.health.lims.resultlimits.dao.ResultLimitDAO;
import us.mn.state.health.lims.resultlimits.daoimpl.ResultLimitDAOImpl;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.typeoftestresult.dao.TypeOfTestResultDAO;
import us.mn.state.health.lims.typeoftestresult.daoimpl.TypeOfTestResultDAOImpl;
import us.mn.state.health.lims.typeoftestresult.valueholder.TypeOfTestResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultPersisterService {
    private TestResultDAO testResultDAO;
    private DictionaryDAO dictionaryDAO;
    private ResultDAO resultDAO;
    private ResultsLoadUtility resultsLoadUtility;
    private ResultLimitDAO resultLimitDAO;
    private Map<String, String> typeOfTestResultsTypeToIdMap;
    private TypeOfTestResultDAO typeOfTestResultDAO;

    public ResultPersisterService() {
        this(new TestResultDAOImpl(), new DictionaryDAOImpl(), new ResultDAOImpl(), new ResultLimitDAOImpl(), new TypeOfTestResultDAOImpl(), new ResultsLoadUtility());
    }

    public ResultPersisterService(TestResultDAO testResultDAO, DictionaryDAO dictionaryDAO, ResultDAO resultDAO, ResultLimitDAO resultLimitDAO, TypeOfTestResultDAO typeOfTestResultDAO, ResultsLoadUtility resultsLoadUtility) {
        this.testResultDAO = testResultDAO;
        this.dictionaryDAO = dictionaryDAO;
        this.resultDAO = resultDAO;
        this.resultLimitDAO = resultLimitDAO;
        this.typeOfTestResultDAO = typeOfTestResultDAO;
        this.resultsLoadUtility = resultsLoadUtility;
        this.typeOfTestResultsTypeToIdMap = new HashMap<>();
    }

    protected void save(Analysis analysis, Test test, String testResultValue, Patient patient, String sysUserId) {
        Result result = new Result();
        result.setSysUserId(sysUserId);
        result.setAnalysis(analysis);

        List<TestResult> testResults = testResultDAO.getTestResultsByTest(test.getId());
        if (!(testResults == null)) {
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
        } else {
            List<ResultLimit> resultLimitsForTest = resultLimitDAO.getAllResultLimitsForTest(test.getId());
            if (!(resultLimitsForTest == null) && resultLimitsForTest.get(0).getResultTypeId().equals(getResultTypesId("N"))) {
                saveNumericTestResult(result, null, testResultValue, test, patient);
            }
        }
    }

    private String getResultTypesId(String testResultType) {
        if (typeOfTestResultsTypeToIdMap.size() == 0) {
            List<TypeOfTestResult> typeOfTestResults = typeOfTestResultDAO.getAllTypeOfTestResults();
            for (TypeOfTestResult typeOfTestResult : typeOfTestResults) {
                typeOfTestResultsTypeToIdMap.put(typeOfTestResult.getTestResultType(), typeOfTestResult.getId());
            }
        }
        return typeOfTestResultsTypeToIdMap.get(testResultType);
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
        if (testResult != null)
            result.setTestResultId(testResult.getId());
        resultDAO.insertData(result);
    }
}

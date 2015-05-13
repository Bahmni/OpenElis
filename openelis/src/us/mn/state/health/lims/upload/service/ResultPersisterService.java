/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package us.mn.state.health.lims.upload.service;

import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.daoimpl.ResultSignatureDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.result.valueholder.ResultSignature;
import us.mn.state.health.lims.resultlimits.dao.ResultLimitDAO;
import us.mn.state.health.lims.resultlimits.daoimpl.ResultLimitDAOImpl;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.systemuser.daoimpl.SystemUserDAOImpl;
import us.mn.state.health.lims.systemuser.valueholder.SystemUser;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.typeoftestresult.dao.TypeOfTestResultDAO;
import us.mn.state.health.lims.typeoftestresult.daoimpl.TypeOfTestResultDAOImpl;

import java.util.List;

public class ResultPersisterService {
    private TestResultDAO testResultDAO;
    private DictionaryDAO dictionaryDAO;
    private ResultDAO resultDAO;
    private ResultsLoadUtility resultsLoadUtility;
    private final String NUMERIC_RESULT_TYPE;
    private ResultSignatureDAOImpl resultSignatureDAO;
    private SystemUserDAOImpl systemUserDAO;

    public ResultPersisterService() {
        this(new TestResultDAOImpl(), new DictionaryDAOImpl(), new ResultDAOImpl(), new ResultLimitDAOImpl(), new TypeOfTestResultDAOImpl(), new ResultSignatureDAOImpl(), new SystemUserDAOImpl(), new ResultsLoadUtility());
    }

    public ResultPersisterService(TestResultDAO testResultDAO, DictionaryDAO dictionaryDAO, ResultDAO resultDAO, ResultLimitDAO resultLimitDAO, TypeOfTestResultDAO typeOfTestResultDAO, ResultSignatureDAOImpl resultSignatureDAO, SystemUserDAOImpl systemUserDAO, ResultsLoadUtility resultsLoadUtility) {
        this.testResultDAO = testResultDAO;
        this.dictionaryDAO = dictionaryDAO;
        this.resultDAO = resultDAO;
        this.resultSignatureDAO = resultSignatureDAO;
        this.systemUserDAO = systemUserDAO;
        this.resultsLoadUtility = resultsLoadUtility;
        NUMERIC_RESULT_TYPE = "N";
    }

    protected void save(Analysis analysis, Test test, String testResultValue, Patient patient, String sysUserId) {
        String isReportable = "N";
        Result result = new Result();
        result.setSysUserId(sysUserId);
        result.setAnalysis(analysis);
        result.setIsReportable(isReportable);

        Dictionary dictionary = null;
        List<TestResult> testResults = testResultDAO.getTestResultsByTest(test.getId());
        boolean testIsDictionaryOrRemark = !(testResults == null || testResults.isEmpty());
        if (testIsDictionaryOrRemark) {
            for (TestResult testResult : testResults) {
                String testResultType = testResult.getTestResultType();
                result.setResultType(testResultType);
                switch (testResultType) {
                    case "R":
                        saveRemarkTestResult(result, testResult, testResultValue);
                        break;
                    case "D":
                        dictionary = dictionary == null ? dictionaryDAO.getDictionaryByDictEntry(testResultValue) : dictionary;
                        if (dictionary == null) {
                            throw new LIMSRuntimeException("Wrong entry for result for test " + test.getTestName());
                        } else if (dictionary.getId().equals(testResult.getValue())) {
                            saveDictionaryTestResult(result, testResult);
                        }
                        break;
                }
            }
        } else {
                saveNumericTestResult(result, testResultValue, test, patient);
        }

        SystemUser systemUser = systemUserDAO.getUserById(sysUserId);
        ResultSignature resultSignature = new ResultSignature();
        resultSignature.setIsSupervisor(false);
        resultSignature.setResultId(result.getId());
        resultSignature.setNonUserName(systemUser.getName());
        resultSignature.setSystemUser(systemUser);
        resultSignatureDAO.insertData(resultSignature);
    }

    private void saveNumericTestResult(Result result, String testResultValue, Test test, Patient patient) {
        try {
            Double.parseDouble(testResultValue);
        } catch (Exception e) {
            throw new LIMSRuntimeException("Result should be numbers for test " + test.getTestName());
        }
        ResultLimit resultLimit = resultsLoadUtility.getResultLimitForTestAndPatient(test, patient);
        result.setMaxNormal(resultLimit.getHighNormal());
        result.setMinNormal(resultLimit.getLowNormal());
        String resultLimitId = resultLimit.getId();
        result.setResultLimitId(!StringUtil.isNullorNill(resultLimitId) ? Integer.parseInt(resultLimitId) : null);
        result.setValue(testResultValue);
        result.setResultType(NUMERIC_RESULT_TYPE);
        resultDAO.insertData(result);
    }

    private void saveRemarkTestResult(Result result, TestResult testResult, String testResultValue) {
        result.setResultType(testResult.getTestResultType());
        result.setValue(testResultValue);
        result.setTestResultId(testResult.getId());
        resultDAO.insertData(result);
    }

    private void saveDictionaryTestResult(Result result, TestResult testResult) {
        result.setValue(testResult.getValue());
        result.setTestResult(testResult);
        result.setResultType(testResult.getTestResultType());
        resultDAO.insertData(result);
    }
}

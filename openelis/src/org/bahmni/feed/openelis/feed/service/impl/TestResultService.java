package org.bahmni.feed.openelis.feed.service.impl;

import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;

import java.util.List;

public class TestResultService {

    private final TestResultDAO testResultDAO;

    public TestResultService() {
        testResultDAO = new TestResultDAOImpl();
    }

    public void createOrUpdate(Test test, String testResultType, String codedTestAnswerId) {
        TestResult testResult = new TestResult();
        testResult.setSysUserId("1");
        testResult.setTest(test);
        testResult.setTestResultType(testResultType);
        testResult.setValue(codedTestAnswerId);
        TestResult existingTestResult;
        if (!StringUtil.isNullorNill(codedTestAnswerId)) {
            existingTestResult = testResultDAO.getTestResultsByTestAndDictonaryResult(test.getId(), codedTestAnswerId);
        } else {
            List<TestResult> testResultsByTest = testResultDAO.getTestResultsByTest(test.getId());
            existingTestResult = testResultsByTest.isEmpty() ? null : testResultsByTest.get(0);
        }
        if (existingTestResult == null) {
            testResult.setActive(true);
            testResultDAO.insertData(testResult);
        }else{
            if (!StringUtil.isNullorNill(codedTestAnswerId)){
                existingTestResult.setActive(true);
                existingTestResult.setSysUserId("1");
                testResultDAO.updateData(existingTestResult);
            }
        }
    }

    public void makeCodedAnswersInactive(String testId) {
        testResultDAO.makeTestResultsInactive(testId);
    }
}

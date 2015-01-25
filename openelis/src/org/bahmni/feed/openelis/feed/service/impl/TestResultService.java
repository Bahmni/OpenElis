package org.bahmni.feed.openelis.feed.service.impl;

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

    public void createOrUpdate(Test test, String testResultType) {
        TestResult testResult = new TestResult();
        testResult.setSysUserId("1");
        testResult.setTest(test);
        testResult.setTestResultType(testResultType);
        List<TestResult> existingTestResults = testResultDAO.getTestResultsByTest(test.getId());
        for (TestResult existingTestResult : existingTestResults) {
            existingTestResult.setSysUserId("1");
        }
        if (existingTestResults != null && existingTestResults.size() > 0) {
            testResultDAO.deleteData(existingTestResults);
        }
        testResultDAO.insertData(testResult);
    }
}

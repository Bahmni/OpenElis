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
        List<TestResult> existingTestResults = null;
        if (!StringUtil.isNullorNill(codedTestAnswerId)) {
            existingTestResults = testResultDAO.getTestResultsByTestAndValue(test.getId(), codedTestAnswerId);
        } else {
            existingTestResults = testResultDAO.getTestResultsByTest(test.getId());
        }
        if (existingTestResults == null || existingTestResults.isEmpty()) {
            testResultDAO.insertData(testResult);
        } 
    }

}

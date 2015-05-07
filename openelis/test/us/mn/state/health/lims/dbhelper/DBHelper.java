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
*
* Contributor(s): CIRG, University of Washington, Seattle WA.
*/
package us.mn.state.health.lims.dbhelper;


import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.result.valueholder.ResultSignature;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.systemuser.valueholder.SystemUser;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DBHelper {

    public static Sample createEntireSampleTreeWithResults() {
        Test test = createTest();
        TestResult testResult = createTestResult(test);
        Sample sample = createSample("asdf");
        SampleItem sampleItem = createSampleItem(sample);
        Panel panel = createPanel();
        Analysis analysis = createAnalysis(sampleItem, panel, test);
        createResult(analysis, testResult);
        return sample;
    }

    public static Analysis createAnalysis(SampleItem sampleItem, Panel panel, TestSection testSection, Test test) {
        Analysis analysis = new Analysis();
        analysis.setSampleItem(sampleItem);
        analysis.setAnalysisType(IActionConstants.ANALYSIS_TYPE_MANUAL);
        analysis.setStatusId("1");
        analysis.setSysUserId("1");
        analysis.setTestSection(testSection);
        analysis.setPanel(panel);
        analysis.setTest(test);
        sampleItem.addAnalysis(analysis);
        return analysis;
    }

    public static Analysis createAnalysis(SampleItem sampleItem, Panel panel, Test test) {
        Analysis analysis = new Analysis();
        analysis.setSampleItem(sampleItem);
        analysis.setAnalysisType(IActionConstants.ANALYSIS_TYPE_MANUAL);
        analysis.setStatusId("6");
        analysis.setSysUserId("1");
        analysis.setPanel(panel);
        analysis.setTest(test);
        sampleItem.addAnalysis(analysis);
        return analysis;
    }

    public static SampleItem createSampleItem(Sample startedSample) {
        SampleItem enteredSampleItem = new SampleItem();
        enteredSampleItem.setSample(startedSample);
        startedSample.addSampleItem(enteredSampleItem);
        enteredSampleItem.setStatusId("1");
        enteredSampleItem.setSortOrder("1");
        enteredSampleItem.setSysUserId("1");
        return enteredSampleItem;
    }

    public static Sample createSample(String accessionNumber) {
        Sample startedSample = new Sample();
        startedSample.setAccessionNumber(accessionNumber);
        startedSample.setEnteredDate(new Date());
        startedSample.setStatusId("1");
        startedSample.setSysUserId("1");
        startedSample.setUUID(UUID.randomUUID().toString());
        return startedSample;
    }

    public static Result createResult(Analysis analysis, TestResult testResult) {
        Result result = new Result();
        result.setAnalysis(analysis);
        result.setSysUserId("1");
        result.setSortOrder("1");
        result.setAnalysis(analysis);
        result.setTestResult(testResult);
        result.setResultType("N");
        analysis.addResult(result);
        result.setResultSignatures(createResultSignatures());
        return result;
    }

    private static Set<ResultSignature> createResultSignatures() {
        HashSet<ResultSignature> resultSignatures = new HashSet<>();
        ResultSignature resultSignature = new ResultSignature();
        resultSignature.setSystemUser(createSystemUser());
        resultSignatures.add(resultSignature);
        return resultSignatures;
    }

    private static SystemUser createSystemUser() {
        SystemUser systemUser = new SystemUser();
        systemUser.setExternalId(UUID.randomUUID().toString());
        return systemUser;
    }

    public static TestResult createTestResult(Test test) {
        TestResult testResult = new TestResult();
        testResult.setSysUserId("1");
        testResult.setSortOrder("1");
        testResult.setTest(test);
        testResult.setTestResultType("N");
        return testResult;
    }

    public static Test createAndSaveTest() {
        Test test = createTest();

        new TestDAOImpl().insertData(test);
        return test;
    }

    public static Test createTest() {
        Test test = new Test();
        test.setDescription("desasdfcription");
        test.setTestName("canthavethis");
        test.setSysUserId("1");
        test.setIsActive("Y");
        return test;
    }

    public static Panel createPanel() {
        Panel panel = new Panel();
        panel.setSysUserId("1");
        panel.setDescription("Ionogra");
        panel.setPanelName("Ionogra");
        return panel;
    }

    public static Panel createAndSavePanel() {
        Panel panel = createPanel();

        new PanelDAOImpl().insertData(panel);
        return panel;
    }

    public static Analysis createAndSaveAnalysis(SampleItem sampleItem, StatusOfSampleUtil.AnalysisStatus analysisStatus, String testSectionName, Panel panel, Test test) {

        TestSectionDAO testSectionDAO = new TestSectionDAOImpl();
        TestSection testSection = testSectionDAO.getTestSectionByName(testSectionName);

        Analysis analysis = createAnalysis(sampleItem, panel, testSection, test);
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(analysisStatus));

        new AnalysisDAOImpl().insertData(analysis, false);

        return analysis;
    }

    public static SampleItem createAndSaveSampleItem(Sample startedSample) {
        SampleItem enteredSampleItem = createSampleItem(startedSample);
        enteredSampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
        new SampleItemDAOImpl().insertData(enteredSampleItem);
        return enteredSampleItem;
    }

    public static SampleItem createSaveAndCloseSample(Sample startedSample) {
        SampleItem enteredSampleItem = createSampleItem(startedSample);
        enteredSampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
        new SampleItemDAOImpl().insertData(enteredSampleItem);
        return enteredSampleItem;
    }

    public static Sample createAndSaveSample(String accessionNumber) {
        Sample sample = createSample(accessionNumber);
        sample.setEnteredDate(DateUtil.convertStringDateToSqlDate("01/01/2001"));
        sample.setReceivedTimestamp(DateUtil.convertStringDateToTimestamp("01/01/2001 00:00"));
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Started));
        new SampleDAOImpl().insertDataWithAccessionNumber(sample);
        return sample;
    }


    public static Result createAndSaveResult(Analysis analysis, TestResult testResult) {
        Result result = createResult(analysis, testResult);

        new ResultDAOImpl().insertData(result);
        return result;
    }

    public static TestResult createAndSaveTestResult(Test test) {
        TestResult testResult = createTestResult(test);

        new TestResultDAOImpl().insertData(testResult);
        return testResult;
    }

    public static Patient createPatient() {
        Patient patient = new Patient();
        Person person = new Person();
        person.setFirstName("First Name");
        person.setLastName("Last Name");
        patient.setPerson(person);
        patient.setExternalId(UUID.randomUUID().toString());
        patient.setUuid(UUID.randomUUID().toString());

        return patient;
    }
}

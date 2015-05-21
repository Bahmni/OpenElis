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
 * Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package org.bahmni.openelis.builder;

import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.analyte.daoimpl.AnalyteDAOImpl;
import us.mn.state.health.lims.analyte.valueholder.Analyte;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.note.util.NoteUtil;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.provider.daoimpl.ProviderDAOImpl;
import us.mn.state.health.lims.provider.valueholder.Provider;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.daoimpl.ResultSignatureDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.result.valueholder.ResultSignature;
import us.mn.state.health.lims.resultlimits.daoimpl.ResultLimitDAOImpl;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.systemuser.daoimpl.SystemUserDAOImpl;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;
import us.mn.state.health.lims.testanalyte.daoimpl.TestAnalyteDAOImpl;
import us.mn.state.health.lims.testanalyte.valueholder.TestAnalyte;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;
import us.mn.state.health.lims.unitofmeasure.daoimpl.UnitOfMeasureDAOImpl;
import us.mn.state.health.lims.unitofmeasure.valueholder.UnitOfMeasure;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestSetup {

    public static SampleHuman createSampleHuman(Sample sample, Patient patient) {
        SampleHuman sampleHuman = new SampleHuman();
        sampleHuman.setPatientId(patient.getId());
        sampleHuman.setSampleId(sample.getId());
        sampleHuman.setSysUserId("1");

        new SampleHumanDAOImpl().insertData(sampleHuman);
        return sampleHuman;
    }

    public static Analysis createAnalysis(SampleItem sampleItem, StatusOfSampleUtil.AnalysisStatus analysisStatus, String testSectionName, Test test) {
        return createAnalysis(sampleItem, analysisStatus, testSectionName, test, null);
    }

    public static Analysis createAnalysis(SampleItem sampleItem, StatusOfSampleUtil.AnalysisStatus analysisStatus, String testSectionName, Test test, Panel panel) {
        TestSectionDAO testSectionDAO = new TestSectionDAOImpl();
        TestSection testSection = testSectionDAO.getTestSectionByName(testSectionName);
        Analysis analysis = new Analysis();
        analysis.setSampleItem(sampleItem);
        analysis.setAnalysisType(IActionConstants.ANALYSIS_TYPE_MANUAL);
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(analysisStatus));
        analysis.setTest(test);
        analysis.setSysUserId("1");
        analysis.setTestSection(testSection);
        analysis.setLastupdated(sampleItem.getSample().getLastupdated());
        analysis.setPanel(panel);
        sampleItem.addAnalysis(analysis);
        new AnalysisDAOImpl().insertData(analysis, false);
        return analysis;
    }

    public static SampleItem createSampleItem(Sample startedSample) {
        SampleItem enteredSampleItem = new SampleItem();
        enteredSampleItem.setSample(startedSample);
        enteredSampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
        enteredSampleItem.setSortOrder("1");
        enteredSampleItem.setSysUserId("1");
        startedSample.addSampleItem(enteredSampleItem);
        new SampleItemDAOImpl().insertData(enteredSampleItem);
        return enteredSampleItem;
    }

    public static Sample createSample(String accessionNumber, Date date) {
        List<SampleSource> sampleSources = new SampleSourceDAOImpl().getAll();
        Sample sample = new Sample();
        sample.setAccessionNumber(accessionNumber);
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Started));
        sample.setEnteredDate(DateUtil.convertStringDateToSqlDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        sample.setReceivedTimestamp(DateUtil.convertStringDateToTimestamp("01/01/2001 00:00"));
        sample.setSampleSource(sampleSources.get(0));
        sample.setUUID(UUID.randomUUID().toString());
        sample.setSysUserId("1");
        sample.setLastupdated(new Timestamp(date.getTime()));
        new SampleDAOImpl().insertDataWithAccessionNumber(sample);
        return sample;
    }

    public static TestAnalyte createTestAnalyte(Test test, Analyte analyte) {
        TestAnalyte testAnalyte = new TestAnalyte();
        testAnalyte.setTest(test);
        testAnalyte.setAnalyte(analyte);
        testAnalyte.setSysUserId("1");
        testAnalyte.setResultGroup("1");
        return testAnalyte;
    }

    public static Test createTest(String testName, String unitOfMeasureName) {
        Test test = new Test();
        test.setTestName(testName);
        test.setDescription(testName);
        test.setIsActive(IActionConstants.YES);
        test.setSortOrder("1");
        test.setOrderable(Boolean.TRUE);
        test.setSysUserId("1");
        test.setUnitOfMeasure(getUnitOfMeasure(unitOfMeasureName));
        new TestDAOImpl().insertData(test);
        return test;
    }

    public static Test createTest(String testName, String unitOfMeasureName, Panel panel) {
        Test test = createTest(testName, unitOfMeasureName);

        if (panel != null) {
            createPanelItem(test, panel);
        }

        return test;
    }

    public static void createPanelItem(Test test, Panel panel) {
        PanelItem panelItem = new PanelItem();
        panelItem.setPanel(panel);
        panelItem.setPanelName(panel.getPanelName());
        panelItem.setTest(test);
        panelItem.setTestName(test.getTestName());
        panelItem.setSysUserId("1");
        new PanelItemDAOImpl().insertData(panelItem);
    }

    public static Panel createPanel(String panelName) {
        Panel panel = new Panel();
        panel.setSysUserId("1");
        panel.setPanelName(panelName);
        panel.setDescription(panelName);
        new PanelDAOImpl().insertData(panel);

        return panel;
    }

    public static ExternalReference createExternalReference(String itemId, String type, String uuid) {
        ExternalReference externalReference = new ExternalReference();
        externalReference.setItemId(Integer.parseInt(itemId));

        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        externalReference.setExternalId(uuid);
        externalReference.setType(type);
        new ExternalReferenceDaoImpl().insertData(externalReference);
        return externalReference;
    }

    public static ResultLimit createResultLimit(Test test, Double lowNormal, Double highNormal, Double lowValid, Double highValid, String gender, Double minAge, Double maxAge) {
        ResultLimit resultLimit = new ResultLimit();
        resultLimit.setTestId(test.getId());
        resultLimit.setResultTypeId("4"); //Numeric type
        resultLimit.setGender(gender);
        resultLimit.setSysUserId("1");
        if (minAge != null) resultLimit.setMinAge(minAge);
        if (maxAge != null) resultLimit.setMaxAge(maxAge);
        if (lowNormal != null) resultLimit.setLowNormal(lowNormal);
        if (highNormal != null) resultLimit.setHighNormal(highNormal);
        if (lowValid != null) resultLimit.setLowValid(lowValid);
        if (highValid != null) resultLimit.setHighValid(highValid);
        new ResultLimitDAOImpl().insertData(resultLimit);
        return resultLimit;
    }

    public static TestResult createTestResult(Test test, String testResultType, String value) {
        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setTestResultType(testResultType);
        testResult.setValue(value);
        if (testResultType.equals("D")) {
            testResult.setValue(getDictionaryObject(value).getId());
        }
        testResult.setSysUserId("1");
        new TestResultDAOImpl().insertData(testResult);
        return testResult;
    }

    public static Dictionary getDictionaryObject(String dictEntry) {
        DictionaryDAOImpl dictionaryDAO = new DictionaryDAOImpl();
        Dictionary dictionaryByDictEntry = dictionaryDAO.getDictionaryByDictEntry(dictEntry);
        if (dictionaryByDictEntry != null) {
            return dictionaryByDictEntry;
        }
        return createDictionary(dictEntry, dictEntry);
    }

    public static Result createResult(Analysis analysis, String value, ResultLimit resultLimit) {
        Result result = new Result();
        result.setAnalysis(analysis);
        result.setValue(value);
        result.setSysUserId("1");
        result.setResultType("N");
        result.setMinNormal(resultLimit.getLowNormal());
        result.setMaxNormal(resultLimit.getHighNormal());
        String resultLimitId = resultLimit.getId();
        result.setResultLimitId(!StringUtil.isNullorNill(resultLimitId) ? Integer.parseInt(resultLimitId) : null);

        new ResultDAOImpl().insertData(result);
        analysis.addResult(result);
        createResultSignature(result);


        return result;
    }

    public static ResultSignature createResultSignature(Result result) {
        ResultSignature resultSignature = new ResultSignature();
        resultSignature.setSystemUser(new SystemUserDAOImpl().getUserById("1"));
        resultSignature.setIsSupervisor(false);
        resultSignature.setNonUserName("Some User");
        resultSignature.setResultId(result.getId());
        result.addResultSignature(resultSignature);

        new ResultSignatureDAOImpl().insertData(resultSignature);
        return resultSignature;
    }

    public static Result createResult(Analysis analysis, TestResult testResult, String testResultType) {
        Result result = new Result();
        result.setAnalysis(analysis);
        result.setSysUserId("1");
        result.setResultType(testResultType);
        if (testResult == null) {
            switch (testResultType) {
                case "D":
                    result.setValue("0");
                    break;
                case "N":
                    result.setValue("");
                    break;
            }
        } else {
            result.setValue(testResult.getValue());
        }
        new ResultDAOImpl().insertData(result);
        return result;
    }

    public static TypeOfSample createTypeOfSample(String desc, String localAbbrev) {
        TypeOfSample typeOfSample = new TypeOfSample();
        typeOfSample.setDescription(desc);
        typeOfSample.setDomain("H");
        typeOfSample.setLocalAbbreviation(localAbbrev);
        typeOfSample.setActive(true);
        typeOfSample.setSysUserId("1");
        new TypeOfSampleDAOImpl().insertData(typeOfSample);

        return typeOfSample;
    }


    public static TypeOfSampleTest createTypeOfSampleTest(String testId, String typeOfSampleId) {
        TypeOfSampleTest typeOfSampleTest = new TypeOfSampleTest();
        typeOfSampleTest.setSysUserId("1");
        typeOfSampleTest.setTestId(testId);
        typeOfSampleTest.setTypeOfSampleId(typeOfSampleId);
        new TypeOfSampleTestDAOImpl().insertData(typeOfSampleTest);

        return typeOfSampleTest;
    }

    public static Patient createPatient(String firstName, String middleName, String lastName, String patientIdentityData, String uuid) {
        Person person = createPerson(firstName, middleName, lastName);
        Patient patient = new Patient();
        patient.setPerson(person);
        patient.setGender("M");
        patient.setBirthDate(DateUtil.getNowAsTimestamp());
        patient.setSysUserId("1");
        patient.setLastupdated(DateUtil.getNowAsTimestamp());
        patient.setUuid(uuid);
        new PatientDAOImpl().insertData(patient);

        PatientIdentity patientIdentity = new PatientIdentity();
        patientIdentity.setPatientId(patient.getId());
        patientIdentity.setIdentityTypeId(PatientIdentityTypeMap.getInstance().getIDForType("ST"));
        patientIdentity.setIdentityData(patientIdentityData);
        patientIdentity.setSysUserId("1");
        new PatientIdentityDAOImpl().insertData(patientIdentity);

        return patient;
    }

    public static Person createPerson(String firstName, String middleName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setMiddleName(middleName);
        person.setLastName(lastName);
        person.setSysUserId("1");
        new PersonDAOImpl().insertData(person);
        return person;
    }

    public static Provider createProvider(String firstName, String middleName, String lastName) {
        Person person = createPerson(firstName, middleName, lastName);

        Provider provider = new Provider();
        provider.setPerson(person);
        provider.setSysUserId("1");
        new ProviderDAOImpl().insertData(provider);
        return provider;
    }

    public static Test createTestWithAnalyte(String testName) {
        Test test = createTest(testName, null);
        TestAnalyteDAOImpl testAnalyteDAO = new TestAnalyteDAOImpl();
        testAnalyteDAO.insertData(createTestAnalyte(test, new AnalyteDAOImpl().readAnalyte("1")));
        return test;
    }

    public static UnitOfMeasure getUnitOfMeasure(String unitOfMeasureName) {
        if (unitOfMeasureName == null) {
            return null;
        }

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        UnitOfMeasureDAOImpl unitOfMeasureDAO = new UnitOfMeasureDAOImpl();
        unitOfMeasure.setUnitOfMeasureName(unitOfMeasureName);
        unitOfMeasure.setDescription(unitOfMeasureName);
        unitOfMeasure.setSysUserId("1");
        UnitOfMeasure unitOfMeasureByName = unitOfMeasureDAO.getUnitOfMeasureByName(unitOfMeasure);
        if (unitOfMeasureByName == null) {
            unitOfMeasureDAO.insertData(unitOfMeasure);
        } else {
            unitOfMeasure = unitOfMeasureByName;
        }
        return unitOfMeasure;
    }

    public static Dictionary createDictionary(String dictEntry, String localAbbrev) {
        Dictionary dictionary = new Dictionary();
        dictionary.setDictEntry(dictEntry);
        dictionary.setLocalAbbreviation(localAbbrev);

        dictionary.setSysUserId("1");
        dictionary.setIsActive("Y");
        new DictionaryDAOImpl().insertData(dictionary);
        return dictionary;
    }

    public static TestResult createTestResult(Test test, String testResultType, Dictionary value) {
        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setTestResultType(testResultType);

        if(testResultType.equals("D")) {
            testResult.setValue(value.getId());
        }

        testResult.setSysUserId("1");
        new TestResultDAOImpl().insertData(testResult);
        return testResult;
    }

    public static Note createResultNote(Result result, String noteText) {
        Note note = NoteUtil.createSavableNote(null, noteText, result.getId(), ResultsLoadUtility.getResultReferenceTableId(), "Result Note", "1");
        new NoteDAOImpl().insertData(note);
        return note;
    }
}

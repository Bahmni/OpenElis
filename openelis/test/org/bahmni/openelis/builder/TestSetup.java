package org.bahmni.openelis.builder;

import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.analyte.daoimpl.AnalyteDAOImpl;
import us.mn.state.health.lims.analyte.valueholder.Analyte;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.note.util.NoteUtil;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.result.action.ResultsLogbookUpdateAction;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;
import us.mn.state.health.lims.testanalyte.daoimpl.TestAnalyteDAOImpl;
import us.mn.state.health.lims.testanalyte.valueholder.TestAnalyte;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.unitofmeasure.daoimpl.UnitOfMeasureDAOImpl;
import us.mn.state.health.lims.unitofmeasure.valueholder.UnitOfMeasure;

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
        TestSectionDAO testSectionDAO = new TestSectionDAOImpl();
        TestSection testSection = testSectionDAO.getTestSectionByName(testSectionName);
        Analysis analysis = new Analysis();
        analysis.setSampleItem(sampleItem);
        analysis.setAnalysisType(IActionConstants.ANALYSIS_TYPE_MANUAL);
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(analysisStatus));
        analysis.setTest(test);
        analysis.setSysUserId("1");
        analysis.setTestSection(testSection);
        new AnalysisDAOImpl().insertData(analysis, false);
        return analysis;
    }

    public static SampleItem createSampleItem(Sample startedSample) {
        SampleItem enteredSampleItem = new SampleItem();
        enteredSampleItem.setSample(startedSample);
        enteredSampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
        enteredSampleItem.setSortOrder("1");
        enteredSampleItem.setSysUserId("1");
        new SampleItemDAOImpl().insertData(enteredSampleItem);
        return enteredSampleItem;
    }

    public static Sample createSample(String accessionNumber, boolean forToday) {
        List<SampleSource> sampleSources = new SampleSourceDAOImpl().getAll();
        Sample sample = new Sample();
        sample.setAccessionNumber(accessionNumber);
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Started));
        sample.setEnteredDate(DateUtil.convertStringDateToSqlDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        sample.setReceivedTimestamp(DateUtil.convertStringDateToTimestamp("01/01/2001 00:00"));
        sample.setSampleSource(sampleSources.get(0));
        sample.setUUID(UUID.randomUUID().toString());
        sample.setSysUserId("1");
        if(!forToday){
            sample.setLastupdated(DateUtil.convertStringDateToTimestamp("08/08/2013 00:00:00"));
        }
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

    public static TestResult createTestResult(Test test, String testResultType, String value) {
        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setTestResultType(testResultType);
        testResult.setValue(value);
        if(testResultType.equals("D")) {
            testResult.setValue(getDictionaryObject(value).getId());
        }
        testResult.setSysUserId("1");
        new TestResultDAOImpl().insertData(testResult);
        return testResult;
    }

    public static Dictionary getDictionaryObject(String dictEntry) {
        DictionaryDAOImpl dictionaryDAO = new DictionaryDAOImpl();
        Dictionary dictionaryByDictEntry = dictionaryDAO.getDictionaryByDictEntry(dictEntry);
        if(dictionaryByDictEntry != null) {
            return dictionaryByDictEntry;
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setDictEntry(dictEntry);
        dictionary.setSysUserId("1");
        dictionary.setIsActive("Y");
        dictionaryDAO.insertData(dictionary);
        return dictionary;
    }

    public static Result createResult(Analysis analysis, String value) {
        Result result = new Result();
        result.setAnalysis(analysis);
        result.setValue(value);
        result.setSysUserId("1");
        result.setResultType("N");
        new ResultDAOImpl().insertData(result);
        return result;
    }

    public static Result createResult(Analysis analysis, TestResult testResult, String value) {
        Result result = new Result();
        result.setAnalysis(analysis);
        result.setValue(value);
        result.setSysUserId("1");
        result.setResultType(testResult.getTestResultType());
        if(testResult.getTestResultType().equals("D")) {
            result.setValue(testResult.getValue());
        }
        new ResultDAOImpl().insertData(result);
        return result;
    }

    public static Patient createPatient(String firstName, String lastName, String patientIdentityData) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setSysUserId("1");
        new PersonDAOImpl().insertData(person);

        Patient patient = new Patient();
        patient.setPerson(person);
        patient.setGender("M");
        patient.setBirthDate(DateUtil.getNowAsTimestamp());
        patient.setSysUserId("1");
        patient.setLastupdated(DateUtil.getNowAsTimestamp());
        new PatientDAOImpl().insertData(patient);

        PatientIdentity patientIdentity = new PatientIdentity();
        patientIdentity.setPatientId(patient.getId());
        patientIdentity.setIdentityTypeId(PatientIdentityTypeMap.getInstance().getIDForType("ST"));
        patientIdentity.setIdentityData(patientIdentityData);
        patientIdentity.setSysUserId("1");
        new PatientIdentityDAOImpl().insertData(patientIdentity);

        return patient;
    }

    public static Test createTestWithAnalyte(String testName) {
        Test test = createTest(testName, null);
        TestAnalyteDAOImpl testAnalyteDAO = new TestAnalyteDAOImpl();
        testAnalyteDAO.insertData(createTestAnalyte(test, new AnalyteDAOImpl().readAnalyte("1")));
        return test;
    }

    public static UnitOfMeasure getUnitOfMeasure(String unitOfMeasureName) {
        if(unitOfMeasureName == null) {
            return null;
        }

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        UnitOfMeasureDAOImpl unitOfMeasureDAO = new UnitOfMeasureDAOImpl();
        unitOfMeasure.setUnitOfMeasureName(unitOfMeasureName);
        unitOfMeasure.setDescription(unitOfMeasureName);
        UnitOfMeasure unitOfMeasureByName = unitOfMeasureDAO.getUnitOfMeasureByName(unitOfMeasure);
        if(unitOfMeasureByName == null) {
            unitOfMeasureDAO.insertData(unitOfMeasure);
        } else {
            unitOfMeasure = unitOfMeasureByName;
        }
        return unitOfMeasure;
    }

    public static Note createResultNote(Result result, String noteText) {
        Note note = NoteUtil.createSavableNote(null, noteText, result.getId(), ResultsLoadUtility.getResultReferenceTableId(), "Result Note", "1");
        new NoteDAOImpl().insertData(note);
        return note;
    }
}

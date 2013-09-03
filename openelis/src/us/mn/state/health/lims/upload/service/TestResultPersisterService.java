package us.mn.state.health.lims.upload.service;

import org.apache.commons.lang3.StringUtils;
import org.bahmni.csv.RowResult;
import org.bahmni.feed.openelis.utils.AuditingService;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;
import us.mn.state.health.lims.upload.sample.CSVSample;
import us.mn.state.health.lims.upload.sample.CSVTestResult;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class TestResultPersisterService {
    private List<String> errorMessages;
    private PatientIdentityTypeDAO patientIdentityTypeDAO;
    private PatientIdentityDAO patientIdentityDAO;
    private SampleDAO sampleDAO;
    private SampleHumanDAO sampleHumanDAO;
    private String sysUserId;
    private AuditingService auditingService;
    private SampleItemDAO sampleItemDAO;
    private TypeOfSampleTestDAO typeOfSampleTestDAO;
    private TypeOfSampleDAO typeOfSampleDAO;
    private static final String DEFAULT_ANALYSIS_TYPE = "MANUAL";
    private AnalysisDAO analysisDAO;
    private Map<String, SampleItem> typesOfSamplesAdded;
    private TestResultDAO testResultDao;
    private ResultDAO resultDAO;
    private PatientDAO patientDAO;
    private DictionaryDAO dictionaryDAO;
    private ResultsLoadUtility utility;

    public TestResultPersisterService() {
        this(new PatientIdentityTypeDAOImpl(), new PatientIdentityDAOImpl(), new SampleDAOImpl(), new SampleHumanDAOImpl(),
                new SampleItemDAOImpl(), new TypeOfSampleTestDAOImpl(), new TypeOfSampleDAOImpl(), new AnalysisDAOImpl(),
                new TestResultDAOImpl(), new ResultDAOImpl(), new PatientDAOImpl(), new DictionaryDAOImpl(),
                new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()), new ResultsLoadUtility());
    }

    public TestResultPersisterService(PatientIdentityTypeDAO patientIdentityTypeDAO, PatientIdentityDAO patientIdentityDAO,
                                      SampleDAO sampleDAO, SampleHumanDAO sampleHumanDAO, SampleItemDAO sampleItemDAO,
                                      TypeOfSampleTestDAO typeOfSampleTestDAO, TypeOfSampleDAO typeOfSampleDAO,
                                      AnalysisDAO analysisDAO, TestResultDAO testResultDao, ResultDAO resultDAO,
                                      PatientDAO patientDAO, DictionaryDAO dictionaryDAO, AuditingService auditingService, ResultsLoadUtility utility) {
        this.patientIdentityTypeDAO = patientIdentityTypeDAO;
        this.patientIdentityDAO = patientIdentityDAO;
        this.sampleDAO = sampleDAO;
        this.sampleHumanDAO = sampleHumanDAO;
        this.sampleItemDAO = sampleItemDAO;
        this.typeOfSampleTestDAO = typeOfSampleTestDAO;
        this.typeOfSampleDAO = typeOfSampleDAO;
        this.analysisDAO = analysisDAO;
        this.resultDAO = resultDAO;
        this.patientDAO = patientDAO;
        this.dictionaryDAO = dictionaryDAO;
        this.auditingService = auditingService;
        this.testResultDao = testResultDao;
        this.utility = utility;
        this.sysUserId = null;
        this.errorMessages = new ArrayList<>();
        this.typesOfSamplesAdded = new HashMap<>();
    }

    public RowResult<CSVSample> persist(CSVSample csvSample) {
        try {
            Map<Test, String> testResults = new HashMap<>();
            Sample sample = saveSample(csvSample);
            SampleHuman sampleHuman = saveSampleHuman(sample.getId(), csvSample.patientRegistrationNumber);
            Patient patient = patientDAO.getPatientById(sampleHuman.getPatientId());
            for (CSVTestResult testResult : csvSample.testResults) {
                Test test = getTest(testResult);
                testResults.put(test, testResult.result);
                SampleItem sampleItem = saveSampleItem(sample, test);
                Analysis analysis = saveAnalysis(test, csvSample.sampleDate, sampleItem);
                saveTestResult(analysis, test, testResult.result, patient);
            }
        } catch (Exception e) {
            errorMessages.add(e.getMessage());
        }
        return new RowResult<>(csvSample, StringUtils.join(errorMessages, ", "));
    }

    protected Analysis saveAnalysis(Test test, String sampleDate, SampleItem sampleItem) throws ParseException {
        Analysis analysis = new Analysis();
        analysis.setSysUserId(getSysUserId());
        analysis.setSampleItem(sampleItem);
        analysis.setTest(test);
        SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate = datetimeFormatter.parse(sampleDate);
        java.sql.Date analysisDate = new java.sql.Date(parsedDate.getTime());
        analysis.setStartedDate(analysisDate);
        analysis.setCompletedDate(analysisDate);
        analysis.setIsReportable(test.getIsReportable());
        analysis.setAnalysisType(DEFAULT_ANALYSIS_TYPE);
        analysis.setRevision(SystemConfiguration.getInstance().getAnalysisDefaultRevision());
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Finalized));
        analysis.setTestSection(test.getTestSection());
        analysisDAO.insertData(analysis, false);
        return analysis;
    }

    protected void saveTestResult(Analysis analysis, Test test, String testResultValue, Patient patient) {
        List<TestResult> testResults = testResultDao.getTestResultsByTest(test.getId());
        Result result = new Result();
        result.setSysUserId(getSysUserId());
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
        ResultLimit resultLimit = utility.getResultLimitForTestAndPatient(test, patient);
        result.setMaxNormal(resultLimit.getHighNormal());
        result.setMinNormal(resultLimit.getLowNormal());
        saveTestResultValue(result, testResult, testResultValue);
    }

    private void saveTestResultValue(Result result, TestResult testResult, String testResultValue) {
        result.setValue(testResultValue);
        result.setTestResultId(testResult.getId());
        resultDAO.insertData(result);
    }

    protected SampleItem saveSampleItem(Sample sample, Test test) {
        TypeOfSample sampleType = getSampleType(test);
        int sortOrder = 1;
        if (!typesOfSamplesAdded.containsKey(sampleType.getId())) {
            SampleItem sampleItem = new SampleItem();
            sampleItem.setSample(sample);
            sampleItem.setTypeOfSample(sampleType);
            sampleItem.setSysUserId(getSysUserId());
            sampleItem.setSortOrder(String.valueOf(sortOrder++));
            sampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
            sampleItemDAO.insertData(sampleItem);
            typesOfSamplesAdded.put(sampleType.getId(), sampleItem);
            return sampleItem;
        }
        return typesOfSamplesAdded.get(sampleType.getId());
    }

    protected Sample saveSample(CSVSample csvSample) throws ParseException {
        Sample sample = new Sample();
        sample.setAccessionNumber(csvSample.accessionNumber);
        SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate = datetimeFormatter.parse(csvSample.sampleDate);
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        sample.setCollectionDate(timestamp);
        sample.setEnteredDate(new java.sql.Date(parsedDate.getTime()));
        sample.setReceivedTimestamp(timestamp);
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Finished));
        sample.setDomain(SystemConfiguration.getInstance().getHumanDomain());
        sample.setSysUserId(getSysUserId());
        sampleDAO.insertDataWithAccessionNumber(sample);
        return sample;
    }

    protected SampleHuman saveSampleHuman(String sampleId, String registrationNumber) {
        String patientId = getPatientId(registrationNumber);
        SampleHuman sampleHuman = new SampleHuman();
        sampleHuman.setPatientId(patientId);
        sampleHuman.setSampleId(sampleId);
        sampleHuman.setSysUserId(getSysUserId());
        sampleHumanDAO.insertData(sampleHuman);
        return sampleHuman;
    }

    private String getPatientId(String patientIdentity) {
        PatientIdentityType stIdentityType = patientIdentityTypeDAO.getNamedIdentityType("ST");
        List<PatientIdentity> patientIdentities = patientIdentityDAO.getPatientIdentitiesByValueAndType(patientIdentity, stIdentityType.getId());
        return patientIdentities.get(0).getPatientId();
    }

    private Test getTest(CSVTestResult testResult) {
        return null;
    }

    private TypeOfSample getSampleType(Test test) {
        List<TypeOfSampleTest> typeOfSampleTestsForTest = typeOfSampleTestDAO.getTypeOfSampleTestsForTest(test.getId());
        return typeOfSampleDAO.getTypeOfSampleById(typeOfSampleTestsForTest.get(0).getTypeOfSampleId());
    }

    private String getSysUserId() {
        if (sysUserId == null) {
            sysUserId = auditingService.getSysUserId();
        }
        return sysUserId;
    }
}

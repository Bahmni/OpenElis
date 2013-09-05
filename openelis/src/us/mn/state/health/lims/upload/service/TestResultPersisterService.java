package us.mn.state.health.lims.upload.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bahmni.csv.RowResult;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.hibernate.Transaction;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.upload.sample.CSVSample;
import us.mn.state.health.lims.upload.sample.CSVTestResult;

import java.util.ArrayList;
import java.util.List;

public class TestResultPersisterService {
    private List<String> errorMessages;
    private String sysUserId;
    private AuditingService auditingService;
    private PatientDAO patientDAO;
    private SamplePersisterService samplePersisterService;
    private SampleHumanPersisterService sampleHumanPersisterService;
    private AnalysisPersisterService analysisPersisterService;
    private SampleItemPersisterService sampleItemPersisterService;
    private ResultPersisterService resultPersisterService;
    private TestDAO testDAO;
    private static Logger logger = Logger.getLogger(TestResultPersisterService.class);

    public TestResultPersisterService() {
        this(new SamplePersisterService(), new SampleHumanPersisterService(), new AnalysisPersisterService(), new SampleItemPersisterService(), new ResultPersisterService(),
                new PatientDAOImpl(), new TestDAOImpl(), new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()));
    }

    public TestResultPersisterService(SamplePersisterService samplePersisterService, SampleHumanPersisterService sampleHumanPersisterService,
                                      AnalysisPersisterService analysisPersisterService, SampleItemPersisterService sampleItemPersisterService,
                                      ResultPersisterService resultPersisterService, PatientDAO patientDAO, TestDAO testDAO, AuditingService auditingService) {
        this.samplePersisterService = samplePersisterService;
        this.sampleHumanPersisterService = sampleHumanPersisterService;
        this.analysisPersisterService = analysisPersisterService;
        this.sampleItemPersisterService = sampleItemPersisterService;
        this.resultPersisterService = resultPersisterService;
        this.patientDAO = patientDAO;
        this.testDAO = testDAO;
        this.auditingService = auditingService;
        this.sysUserId = null;
        this.errorMessages = new ArrayList<>();
    }

    public RowResult<CSVSample> persist(CSVSample csvSample) {
        Transaction transaction = null;
        try {
            logger.debug("Persisting " + csvSample);
            transaction = HibernateUtil.getSession().beginTransaction();
            sysUserId = getSysUserId();
            Sample sample = samplePersisterService.save(csvSample, sysUserId);
            String identifier = csvSample.healthCenter + csvSample.patientRegistrationNumber;
            SampleHuman sampleHuman = sampleHumanPersisterService.save(sample.getId(), identifier, sysUserId);
            Patient patient = patientDAO.getPatientById(sampleHuman.getPatientId());
            for (CSVTestResult testResult : csvSample.testResults) {
                if(testResult.isEmpty())
                    continue;
                Test test = getTest(testResult.test);
                SampleItem sampleItem = sampleItemPersisterService.save(sample, test, sysUserId);
                Analysis analysis = analysisPersisterService.save(test, csvSample.sampleDate, sampleItem, sysUserId);
                resultPersisterService.save(analysis, test, testResult.result, patient, sysUserId);
            }
            transaction.commit();
        } catch (Exception e) {
            logger.warn(e);
            if (transaction != null) transaction.rollback();
            return new RowResult<>(csvSample, e);
        }
        return new RowResult<>(csvSample, StringUtils.join(errorMessages, ", "));
    }

    private Test getTest(String testName) {
        return testDAO.getTestByName(testName);
    }

    private String getSysUserId() {
        if (sysUserId == null) {
            sysUserId = auditingService.getSysUserId();
        }
        return sysUserId;
    }
}

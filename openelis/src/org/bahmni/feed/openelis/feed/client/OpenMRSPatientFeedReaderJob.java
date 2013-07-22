package org.bahmni.feed.openelis.feed.client;

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.webclient.WebClient;
import org.hibernate.Transaction;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.mn.state.health.lims.hibernate.HibernateUtil;

public class OpenMRSPatientFeedReaderJob implements Job {
    private AtomFeedProperties atomFeedProperties;
    private AtomFeedClient atomFeedClient;

    private static final String FEED_NAME = "openmrs.patient.feed.uri";
    private static final String AUTH_URI = "openmrs.auth.uri";
    private static final String OPENMRS_USER = "openmrs.user";
    private static final String OPENMRS_PASSWORD = "openmrs.password";
    private static Logger logger = Logger.getLogger(OpenMRSPatientFeedReaderJob.class);
    private WebClient authenticatedWebClient;

    public OpenMRSPatientFeedReaderJob() {
        this(AtomFeedProperties.getInstance(), new AtomFeedClientFactory());
    }

    public OpenMRSPatientFeedReaderJob(AtomFeedProperties atomFeedProperties, AtomFeedClientFactory atomFeedClientFactory) {
        this.atomFeedProperties = atomFeedProperties;
        this.authenticatedWebClient = atomFeedClientFactory.getAuthenticatedWebClient(
                atomFeedProperties.getProperty(AUTH_URI),
                atomFeedProperties.getProperty(OPENMRS_USER),
                atomFeedProperties.getProperty(OPENMRS_PASSWORD)
                );
        this.atomFeedClient = atomFeedClientFactory.getMRSPatientFeedClient(atomFeedProperties,
                FEED_NAME, AUTH_URI, authenticatedWebClient);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Transaction transaction = HibernateUtil.getSession().beginTransaction();
        try {
            atomFeedClient.processEvents();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error(e.getMessage(), e);
        } finally {
            HibernateUtil.closeSession();
        }
    }
}
package org.bahmni.feed.openelis.feed.job;

import org.apache.log4j.Logger;
import org.bahmni.webclients.Authenticator;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.openmrs.OpenMRSLoginAuthenticator;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class OpenELISFeedFailedEventsJob extends OpenELISFeedReaderJob {
    
    protected OpenELISFeedFailedEventsJob(Logger logger) {
        super(logger);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        processFailedEvents(jobExecutionContext);
    }

    protected Authenticator getAuthenticator(ConnectionDetails connectionDetails) {
        return new OpenMRSLoginAuthenticator(connectionDetails);
    }
}

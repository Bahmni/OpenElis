package org.bahmni.feed.openelis.feed.client;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class OpenELISFeedReaderJob implements Job {
    private final Logger logger;

    protected OpenELISFeedReaderJob(Logger logger) {
        logger.info("Started");
        this.logger = logger;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            doExecute(jobExecutionContext);
        } catch (Exception e) {
            try {
                handleException(e);
            } finally {
                logger.error(e.getMessage(), e);
            }
        }
    }

    protected abstract void handleException(Throwable e);

    protected abstract void doExecute(JobExecutionContext jobExecutionContext);
}
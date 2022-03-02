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
package us.mn.state.health.lims.scheduler;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bahmni.feed.openelis.feed.job.event.EventRecordsNumberOffsetMarkerTask;
import org.bahmni.feed.openelis.feed.job.event.EventRecordsPublisherTask;
import org.bahmni.feed.openelis.feed.job.openmrs.*;
import org.hibernate.Transaction;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.JobListenerSupport;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.dataexchange.MalariaSurveilance.MalariaSurveilanceJob;
import us.mn.state.health.lims.dataexchange.aggregatereporting.AggregateReportJob;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.scheduler.dao.CronSchedulerDAO;
import us.mn.state.health.lims.scheduler.daoimpl.CronSchedulerDAOImpl;
import us.mn.state.health.lims.scheduler.valueholder.CronScheduler;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.EverythingMatcher.allJobs;

public class LateStartScheduler {
    private static final String NEVER = "never";
    private static Map<String, Class<? extends Job>> scheduleJobMap;
    private static Logger logger = LogManager.getLogger(LateStartScheduler.class);
    private CronSchedulerDAOImpl cronSchedulerDAO = new CronSchedulerDAOImpl();

    private Scheduler scheduler;

    static {
        scheduleJobMap = new HashMap<>();
        scheduleJobMap.put("sendSiteIndicators", AggregateReportJob.class);
        scheduleJobMap.put("sendMalariaSurviellanceReport", MalariaSurveilanceJob.class);

        scheduleJobMap.put("atom-feed-openmrs-patient", OpenMRSPatientFeedReaderJob.class);
        scheduleJobMap.put("atom-feed-openmrs-patient-failed", OpenMRSPatientFeedFailedEventsJob.class);

        scheduleJobMap.put("atom-feed-openmrs-encounter", OpenMRSEncounterFeedReaderJob.class);
        scheduleJobMap.put("atom-feed-openmrs-encounter-failed", OpenMRSEncounterFeedFailedEventsJob.class);

        scheduleJobMap.put("atom-feed-openmrs-lab", OpenMRSLabFeedReaderJob.class);
        scheduleJobMap.put("atom-feed-openmrs-lab-failed", OpenMRSLabFeedFailedEventsJob.class);

        scheduleJobMap.put("atom-feed-events-offset-marker", EventRecordsNumberOffsetMarkerTask.class);
        scheduleJobMap.put("atom-feed-events-publisher", EventRecordsPublisherTask.class);
    }

    public void restartSchedules() {
        new Restarter().start();
    }

    public class Restarter extends Thread {
        public void run() {
            try {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.shutdown();
                checkAndStartScheduler();
            } catch (SchedulerException e) {
                logger.error("", e);
            }
        }
    }

    public void checkAndStartScheduler() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            List<CronScheduler> schedulers = cronSchedulerDAO.getAllCronSchedules();
            for (CronScheduler schedule : schedulers) {
                scheduleJob(schedule);
            }
            scheduler.getListenerManager().addJobListener(new ElisJobListener(), allJobs());
            scheduler.startDelayed(120);
        } catch (SchedulerException | ParseException e) {
            throw new LIMSRuntimeException(e);
        } finally {
            HibernateUtil.getSession().close();
        }
    }

    private void scheduleJob(CronScheduler schedule) throws SchedulerException, ParseException {
        try {
            if (!schedule.getActive() || NEVER.equals(schedule.getCronStatement())) {
                return;
            }

            logger.info(String.format("Scheduling %s, with cron=%s", schedule.getJobName(), schedule.getCronStatement()));
            String jobName = schedule.getJobName();
            Class<? extends Job> targetJob = scheduleJobMap.get(jobName);
            if (targetJob == null) {
                return;
            }
            JobDetail job = newJob(targetJob).withIdentity(jobName + "Job", jobName).build();
            Trigger trigger = newTrigger().withIdentity(jobName + "Trigger", jobName).withSchedule(cronSchedule(schedule.getCronStatement()))
                    .forJob(jobName + "Job", jobName).build();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            logger.error(String.format("Scheduling of job %s failed", schedule.getJobName()), e);
        }
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown(true);
    }

    private class ElisJobListener extends JobListenerSupport {
        @Override
        public String getName() {
            return "Elis Job Listener";
        }

        @Override
        public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
            super.jobWasExecuted(context, jobException);
            Class<? extends Job> jobClass = context.getJobDetail().getJobClass();

            Set<String> keys = scheduleJobMap.keySet();
            String matchingJobName = null;
            for (String key : keys) {
                if (scheduleJobMap.get(key).equals(jobClass)) {
                    matchingJobName = key;
                    break;
                }
            }

            if (matchingJobName != null) {
                Transaction transaction = null;
                try {
                    logger.info("executed the job : " + matchingJobName);
                    transaction = HibernateUtil.getSession().beginTransaction();
                    CronSchedulerDAO schedulerDAO = new CronSchedulerDAOImpl();
                    CronScheduler cronSchedule = schedulerDAO.getCronScheduleByJobName(matchingJobName);
                    Date previousFireTime = context.getFireTime();
                    if (previousFireTime == null) {
                        previousFireTime = new Date();
                    }
                    cronSchedule.setLastRun(new Timestamp(previousFireTime.getTime()));
                    cronSchedule.setSysUserId("1");
                    schedulerDAO.update(cronSchedule);
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    logger.error("error in updating quartz scheduler last run", e);
                }
            }
        }
    }
}

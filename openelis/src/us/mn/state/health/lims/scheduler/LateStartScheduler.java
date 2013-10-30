/**
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

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.feed.client.OpenERPLabTestFailedEventsJob;
import org.bahmni.feed.openelis.feed.client.OpenERPLabTestFeedJob;
import org.bahmni.feed.openelis.feed.client.OpenMRSPatientFeedFailedEventsJob;
import org.bahmni.feed.openelis.feed.client.OpenMRSPatientFeedReaderJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.dataexchange.MalariaSurveilance.MalariaSurveilanceJob;
import us.mn.state.health.lims.dataexchange.aggregatereporting.AggregateReportJob;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.scheduler.daoimpl.CronSchedulerDAOImpl;
import us.mn.state.health.lims.scheduler.valueholder.CronScheduler;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class LateStartScheduler {
    private static final String NEVER = "never";
    private static Map<String, Class<? extends Job>> scheduleJobMap;
    private static Logger logger = Logger.getLogger(LateStartScheduler.class);

    private Scheduler scheduler;

    static {
        scheduleJobMap = new HashMap<>();
        scheduleJobMap.put("sendSiteIndicators", AggregateReportJob.class);
        scheduleJobMap.put("sendMalariaSurviellanceReport", MalariaSurveilanceJob.class);

        scheduleJobMap.put("atom-feed-openerp-test", OpenERPLabTestFeedJob.class);
        scheduleJobMap.put("atom-feed-openerp-test-failed", OpenERPLabTestFailedEventsJob.class);

        scheduleJobMap.put("atom-feed-openmrs-patient", OpenMRSPatientFeedReaderJob.class);
        scheduleJobMap.put("atom-feed-openmrs-patient-failed", OpenMRSPatientFeedFailedEventsJob.class);
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
            List<CronScheduler> schedulers = new CronSchedulerDAOImpl().getAllCronSchedules();
            for (CronScheduler schedule : schedulers) {
                scheduleJob(schedule);
            }
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
}

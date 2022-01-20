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
*/
package us.mn.state.health.lims.common.servlet.startup;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.SchedulerException;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.scheduler.IndependentThreadStarter;
import us.mn.state.health.lims.scheduler.LateStartScheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class StartStopListener implements ServletContextListener {
    private LateStartScheduler scheduler;
    private IndependentThreadStarter threadStarter;
    private static Logger logger = LogManager.getLogger(StartStopListener.class);

    public void contextDestroyed(ServletContextEvent event) {
        if (threadStarter != null) {
            threadStarter.stopThreads();
        }

        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.error("Scheduler shutdown failed", e);
        }

        try {
            HibernateUtil.closeSessionFactory();
        } catch (Exception e) {
            logger.error("Session Factory close failed. This could lead to hanging threads", e);
        }

        logger.info(String.format("Shutting down"));
    }

    public void contextInitialized(ServletContextEvent event) {
        logger.info(String.format("Initializing"));
        scheduler = new LateStartScheduler();
        scheduler.checkAndStartScheduler();

        if (false) {
            threadStarter = new IndependentThreadStarter();
            threadStarter.startThreads();
        }
        logger.info("Initialized");
    }
}

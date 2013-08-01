package us.mn.state.health.lims.common.servlet.startup;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import us.mn.state.health.lims.scheduler.IndependentThreadStarter;
import us.mn.state.health.lims.scheduler.LateStartScheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class StartStopListener implements ServletContextListener {
    private LateStartScheduler scheduler;
    private IndependentThreadStarter threadStarter;
    private static Logger logger = Logger.getLogger(StartStopListener.class);

    public void contextDestroyed(ServletContextEvent event) {
        if (threadStarter != null) {
            threadStarter.stopThreads();
        }

        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.warn("Scheduler shutdown failed", e);
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
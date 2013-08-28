package us.mn.state.health.lims.common.servlet.startup;

import org.apache.log4j.Logger;
import org.bahmni.fileimport.ImportRegistry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class FileImportCleanupListener implements ServletContextListener {
    private static Logger logger = Logger.getLogger(FileImportCleanupListener.class);

    public void contextDestroyed(ServletContextEvent event) {
        try {
            logger.info("shutting down all fileimport threads");
            ImportRegistry.shutdownAllThreads();
            logger.info("fileimport threads shutdown");
        } catch (Exception e) {
            logger.warn("FileImportCleanup failed", e);
        }
    }

    public void contextInitialized(ServletContextEvent event) {
        logger.info("fileimportcleanuplistener hooked up");
    }
}
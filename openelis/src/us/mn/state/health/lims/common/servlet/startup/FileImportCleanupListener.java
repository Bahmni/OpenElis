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
import org.bahmni.fileimport.ImportRegistry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class FileImportCleanupListener implements ServletContextListener {
    private static Logger logger = LogManager.getLogger(FileImportCleanupListener.class);

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

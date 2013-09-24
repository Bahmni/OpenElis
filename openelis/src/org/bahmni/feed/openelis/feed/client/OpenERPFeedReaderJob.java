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

package org.bahmni.feed.openelis.feed.client;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.event.LabTestFeedEventWorker;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.Job;

public abstract class OpenERPFeedReaderJob implements Job {
    protected AtomFeedClient atomFeedClient;
    private static final String FEED_NAME = "openerp.labtest.feed.uri";

    protected OpenERPFeedReaderJob(AtomFeedClient atomFeedClient) {
        this.atomFeedClient = atomFeedClient;
    }

    protected OpenERPFeedReaderJob() {
        this(new AtomFeedClientFactory().getERPLabTestFeedClient(AtomFeedProperties.getInstance(), FEED_NAME,
                new LabTestFeedEventWorker()));
    }
}

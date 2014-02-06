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

package org.bahmni.feed.openelis.feed;

import org.bahmni.feed.openelis.utils.OpenElisConnectionProvider;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsOffsetMarkersJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.ChunkingEntriesJdbcImpl;
import org.ict4h.atomfeed.server.service.feedgenerator.FeedGenerator;
import org.ict4h.atomfeed.server.service.helper.ResourceHelper;

public class FeedGeneratorFactory {
    public FeedGenerator get(JdbcConnectionProvider provider) {
        AllEventRecordsJdbcImpl allEventRecords = new AllEventRecordsJdbcImpl(provider);
        JdbcConnectionProvider connectionProvider = new OpenElisConnectionProvider();
        AllEventRecordsOffsetMarkersJdbcImpl allEventRecordsOffsetMarkersJdbc = new AllEventRecordsOffsetMarkersJdbcImpl(connectionProvider);
        ChunkingEntriesJdbcImpl allChunkingEntries = new ChunkingEntriesJdbcImpl(provider);
        return new org.ict4h.atomfeed.server.service.feedgenerator.FeedGeneratorFactory().getFeedGenerator(
                allEventRecords, allEventRecordsOffsetMarkersJdbc, allChunkingEntries, new ResourceHelper());
    }
}

package org.bahmni.feed.openelis.feed;

import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.ChunkingEntriesJdbcImpl;
import org.ict4h.atomfeed.server.service.feedgenerator.FeedGenerator;
import org.ict4h.atomfeed.server.service.helper.ResourceHelper;

public class FeedGeneratorFactory {
    public FeedGenerator get(JdbcConnectionProvider provider) {
        AllEventRecordsJdbcImpl allEventRecords = new AllEventRecordsJdbcImpl(provider);
        ChunkingEntriesJdbcImpl allChunkingEntries = new ChunkingEntriesJdbcImpl(provider);
        return new org.ict4h.atomfeed.server.service.feedgenerator.FeedGeneratorFactory().getFeedGenerator(allEventRecords, allChunkingEntries, new ResourceHelper());
    }
}

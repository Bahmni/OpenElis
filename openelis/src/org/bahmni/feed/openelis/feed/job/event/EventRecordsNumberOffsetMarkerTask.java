package org.bahmni.feed.openelis.feed.job.event;

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.feed.job.bahmnireferencedata.ReferenceDataFeedReaderJob;
import org.bahmni.feed.openelis.utils.OpenElisConnectionProvider;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.server.repository.AllEventRecords;
import org.ict4h.atomfeed.server.repository.AllEventRecordsOffsetMarkers;
import org.ict4h.atomfeed.server.repository.ChunkingEntries;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsOffsetMarkersJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.ChunkingEntriesJdbcImpl;
import org.ict4h.atomfeed.server.service.NumberOffsetMarkerServiceImpl;
import org.ict4h.atomfeed.server.service.OffsetMarkerService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class EventRecordsNumberOffsetMarkerTask implements Job {
    private static final int OFFSET_BY_NUMBER_OF_RECORDS_PER_CATEGORY = 1000;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JdbcConnectionProvider connectionProvider = new OpenElisConnectionProvider();
        AllEventRecords allEventRecords = new AllEventRecordsJdbcImpl(connectionProvider);
        AllEventRecordsOffsetMarkers eventRecordsOffsetMarkers = new AllEventRecordsOffsetMarkersJdbcImpl(connectionProvider);
        ChunkingEntries chunkingEntries = new ChunkingEntriesJdbcImpl(connectionProvider);
        OffsetMarkerService markerService = new NumberOffsetMarkerServiceImpl(allEventRecords, chunkingEntries, eventRecordsOffsetMarkers);
        markerService.markEvents(OFFSET_BY_NUMBER_OF_RECORDS_PER_CATEGORY);
    }
}

package org.bahmni.feed.openelis.feed.job.event;

import org.bahmni.feed.openelis.feed.transaction.support.AtomFeedHibernateTransactionManager;
import org.ict4h.atomfeed.server.repository.AllEventRecords;
import org.ict4h.atomfeed.server.repository.AllEventRecordsOffsetMarkers;
import org.ict4h.atomfeed.server.repository.ChunkingEntries;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsOffsetMarkersJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.ChunkingEntriesJdbcImpl;
import org.ict4h.atomfeed.server.service.NumberOffsetMarkerServiceImpl;
import org.ict4h.atomfeed.server.service.OffsetMarkerService;
import org.ict4h.atomfeed.transaction.AFTransactionWorkWithoutResult;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.mn.state.health.lims.hibernate.HibernateUtil;

@DisallowConcurrentExecution
public class EventRecordsNumberOffsetMarkerTask implements Job {
    
    private static final int OFFSET_BY_NUMBER_OF_RECORDS_PER_CATEGORY = 1000;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        AtomFeedHibernateTransactionManager transactionManager = new AtomFeedHibernateTransactionManager();
        AllEventRecords allEventRecords = new AllEventRecordsJdbcImpl(transactionManager);
        AllEventRecordsOffsetMarkers eventRecordsOffsetMarkers = new AllEventRecordsOffsetMarkersJdbcImpl(transactionManager);
        ChunkingEntries chunkingEntries = new ChunkingEntriesJdbcImpl(transactionManager);
        final OffsetMarkerService markerService = new NumberOffsetMarkerServiceImpl(allEventRecords, chunkingEntries, eventRecordsOffsetMarkers);
        
        transactionManager.executeWithTransaction(new AFTransactionWorkWithoutResult() {
            @Override
            protected void doInTransaction() {
                markerService.markEvents(OFFSET_BY_NUMBER_OF_RECORDS_PER_CATEGORY);
            }
            @Override
            public PropagationDefinition getTxPropagationDefinition() {
                return PropagationDefinition.PROPAGATION_REQUIRED;
            }
        });

    }
}

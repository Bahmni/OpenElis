package org.bahmni.feed.openelis.feed.client;

import org.bahmni.feed.openelis.utils.OpenElisConnectionProvider;
import org.ict4h.atomfeed.client.factory.AtomClientFactory;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;

public class AtomFeedClientFactory {
    static AtomFeedClient getFeedClient() {
        JdbcConnectionProvider jdbcConnectionProvider = new OpenElisConnectionProvider();
        AllMarkersJdbcImpl allMarkersJdbc = new AllMarkersJdbcImpl(jdbcConnectionProvider);
        AllFailedEventsJdbcImpl allFailedEventsJdbc = new AllFailedEventsJdbcImpl(jdbcConnectionProvider);

        return new AtomClientFactory().create(allMarkersJdbc,allFailedEventsJdbc);
    }


}
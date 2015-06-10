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
import org.bahmni.feed.openelis.feed.transaction.support.AtomFeedHibernateTransactionManager;
import org.bahmni.webclients.ClientCookies;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.client.service.FeedClient;

import java.net.URI;
import java.net.URISyntaxException;

public class AtomFeedClientFactory {

    public FeedClient getFeedClient(AtomFeedProperties atomFeedProperties, String feedName,
                                        EventWorker eventWorker, ClientCookies cookies) {
        String uri = atomFeedProperties.getProperty(feedName);
        try {

            AtomFeedHibernateTransactionManager transactionManager = new AtomFeedHibernateTransactionManager();
            org.ict4h.atomfeed.client.AtomFeedProperties atomFeedClientProperties = createAtomFeedClientProperties(atomFeedProperties);
            
            AllFeeds allFeeds = new AllFeeds(atomFeedClientProperties, cookies);
            AllMarkersJdbcImpl allMarkers = new AllMarkersJdbcImpl(transactionManager);
            AllFailedEventsJdbcImpl allFailedEvents = new AllFailedEventsJdbcImpl(transactionManager);

            return new AtomFeedClient(allFeeds, allMarkers, allFailedEvents,
                    atomFeedClientProperties, transactionManager, new URI(uri), eventWorker);
            
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Is not a valid URI - %s", uri));
        }
    }

    private org.ict4h.atomfeed.client.AtomFeedProperties createAtomFeedClientProperties(AtomFeedProperties atomFeedProperties) {
        org.ict4h.atomfeed.client.AtomFeedProperties feedProperties = new org.ict4h.atomfeed.client.AtomFeedProperties();
        feedProperties.setConnectTimeout(Integer.parseInt(atomFeedProperties.getFeedConnectionTimeout()));
        feedProperties.setReadTimeout(Integer.parseInt(atomFeedProperties.getFeedReplyTimeout()));
        feedProperties.setMaxFailedEvents(Integer.parseInt(atomFeedProperties.getMaxFailedEvents()));
        feedProperties.setFailedEventMaxRetry(Integer.parseInt(atomFeedProperties.getFailedEventMaxRetry()));
        feedProperties.setControlsEventProcessing(true);
        return feedProperties;
    }
    
}

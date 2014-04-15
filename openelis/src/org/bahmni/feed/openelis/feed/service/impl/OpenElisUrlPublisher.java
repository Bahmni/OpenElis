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

package org.bahmni.feed.openelis.feed.service.impl;

import org.ict4h.atomfeed.server.service.Event;
import org.ict4h.atomfeed.server.service.EventService;
import org.ict4h.atomfeed.transaction.AFTransactionManager;
import org.ict4h.atomfeed.transaction.AFTransactionWorkWithoutResult;
import org.joda.time.DateTime;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

public class OpenElisUrlPublisher {

    public final String URL_PREFIX = "/ws/rest/";
    
    private AFTransactionManager transactionManager;
    private EventService eventService;
    private String category;
    private String messageType;

    public OpenElisUrlPublisher(AFTransactionManager transactionManager, EventService eventService, String category, String messageType) {
        this.transactionManager = transactionManager;
        this.eventService = eventService;
        this.category = category;
        this.messageType = messageType;
    }

    public void publish(String resourcePath, String contextPath) {
        String contentUrl = getContentUrlFor(resourcePath, contextPath);
        final Event event = new Event(UUID.randomUUID().toString(), messageType, DateTime.now(), (URI) null, contentUrl, category);
        transactionManager.executeWithTransaction(
                new AFTransactionWorkWithoutResult() {
                    @Override
                    protected void doInTransaction() {
                        eventService.notify(event);
                    }

                    @Override
                    public PropagationDefinition getTxPropagationDefinition() {
                        return PropagationDefinition.PROPAGATION_REQUIRED;
                    }
                }
        );
        
    }

    public void publish(Collection<String> resourcePaths, String contextPath) {
        for (String resourcePath : resourcePaths) {
            publish(resourcePath, contextPath);
        }
    }

    private String getContentUrlFor(String resourcePath, String contextPath) {
        return contextPath + URL_PREFIX + messageType + "/" + resourcePath;
    }
}

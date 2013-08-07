package org.bahmni.feed.openelis.feed.service.impl;

import org.ict4h.atomfeed.server.service.Event;
import org.ict4h.atomfeed.server.service.EventService;
import org.joda.time.DateTime;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

public class OpenElisUrlPublisher {
    private EventService eventService;
    private String category;
    private final String URL_PREFIX = "/ws/rest/";
    private final String FEED_TITLE = "openelis";

    public OpenElisUrlPublisher(EventService eventService, String category) {
        this.eventService = eventService;
        this.category = category;
    }

    public void publish(String uuid, String contextPath) {
        String contentUrl = getContentUrlFor(uuid, contextPath);
        eventService.notify(new Event(UUID.randomUUID().toString(), FEED_TITLE, DateTime.now(), (URI) null, contentUrl, category));
    }

    public void publish(Collection<String> uuids, String contextPath) {
        for (String uuid : uuids) {
            String contentUrl = getContentUrlFor(uuid, contextPath);
            eventService.notify(new Event(UUID.randomUUID().toString(), FEED_TITLE, DateTime.now(), (URI) null, contentUrl, category));
        }
    }

    private String getContentUrlFor(String uuid, String contextPath) {
        return contextPath + URL_PREFIX + category + "/" + uuid;
    }
}

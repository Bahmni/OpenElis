package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.service.PatientPublisherService;
import org.bahmni.feed.openelis.utils.OpenElisConnectionProvider;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.service.Event;
import org.ict4h.atomfeed.server.service.EventService;
import org.ict4h.atomfeed.server.service.EventServiceImpl;
import org.joda.time.DateTime;

import java.net.URI;
import java.util.UUID;

public class PatientPublisherServiceImpl implements PatientPublisherService {
    private EventService eventService;
    private final String PATIENT_URL_PREFIX = "/ws/rest/patient/";
    private final String FEED_TITLE = "openelis";
    private final String CATEGORY = "patient";

    public PatientPublisherServiceImpl() {
        this(new EventServiceImpl(new AllEventRecordsJdbcImpl(new OpenElisConnectionProvider())));
    }

    public PatientPublisherServiceImpl(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void publish(String patientIdentity, String contextPath) {
        String contentUrl = getContentUrlFor(patientIdentity, contextPath);
        eventService.notify(new Event(UUID.randomUUID().toString(), FEED_TITLE, DateTime.now(), (URI) null, contentUrl, CATEGORY));
    }

    private String getContentUrlFor(String patientIdentity, String contextPath) {
        return contextPath + PATIENT_URL_PREFIX + patientIdentity;
    }
}

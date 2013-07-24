package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.service.PatientPublisherService;
import org.ict4h.atomfeed.jdbc.PropertiesJdbcConnectionProvider;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.service.Event;
import org.ict4h.atomfeed.server.service.EventService;
import org.ict4h.atomfeed.server.service.EventServiceImpl;
import org.joda.time.DateTime;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;

import java.net.URI;
import java.util.UUID;

public class PatientPublisherServiceImpl implements PatientPublisherService {
    private EventService eventService;
    private final String PATIENT_URL_PREFIX = "/patient/";
    private final String FEED_TITLE = "openelis";
    private final String CATEGORY = "patient";

    public PatientPublisherServiceImpl() {
        this(new EventServiceImpl(new AllEventRecordsJdbcImpl(new PropertiesJdbcConnectionProvider())));
    }

    public PatientPublisherServiceImpl(EventService eventService) {
        this.eventService = eventService;
    }

    public void publish(PatientIdentity patient) {
        String contentUrl = getContentUrlFor(patient);
        eventService.notify(new Event(UUID.randomUUID().toString(), FEED_TITLE, DateTime.now(), (URI) null, contentUrl, CATEGORY));
    }

    private String getContentUrlFor(PatientIdentity patientIdentity) {
        return PATIENT_URL_PREFIX + patientIdentity.getIdentityData();
    }
}

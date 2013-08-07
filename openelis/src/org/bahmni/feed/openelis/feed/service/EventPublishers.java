package org.bahmni.feed.openelis.feed.service;

import org.bahmni.feed.openelis.feed.service.impl.OpenElisUrlPublisher;
import org.bahmni.feed.openelis.utils.OpenElisConnectionProvider;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.service.EventService;
import org.ict4h.atomfeed.server.service.EventServiceImpl;

public class EventPublishers {
    private final String PATIENT_CATEGORY = "patient";
    private final String RESULT_CATEGORY = "result";
    private EventService eventService;

    public EventPublishers() {
        this(new EventServiceImpl(new AllEventRecordsJdbcImpl(new OpenElisConnectionProvider())));
    }

    public EventPublishers(EventService eventService) {
        this.eventService = eventService;
    }

    public OpenElisUrlPublisher patientPublisher() {
        return new OpenElisUrlPublisher(eventService, PATIENT_CATEGORY);
    }

    public OpenElisUrlPublisher testResultPublisher() {
        return new OpenElisUrlPublisher(eventService, RESULT_CATEGORY);
    }
}

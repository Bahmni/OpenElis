package org.bahmni.feed.openelis.feed.service.impl;

import org.ict4h.atomfeed.server.service.Event;
import org.ict4h.atomfeed.server.service.EventService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class OpenElisUrlPublisherTest {
    private final String SAMPLE_UUID = "GAN12345";

    @Mock
    EventService eventService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldConstructUrlIdentifierAndProperties() {
        OpenElisUrlPublisher publisher = new OpenElisUrlPublisher(eventService, "patient");

        publisher.publish(SAMPLE_UUID, "/openelis");

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventService).notify(captor.capture());
        Event event = captor.getValue();
        assertEquals("/openelis/ws/rest/patient/" + SAMPLE_UUID, event.getContents());
    }

    @Test
    public void shouldPopulateDefaultFieldsWhenCallingEventService() {
        OpenElisUrlPublisher publisher = new OpenElisUrlPublisher(eventService, "patient");

        publisher.publish(SAMPLE_UUID, "/openelis");

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventService).notify(captor.capture());
        Event event = captor.getValue();
        assertNotNull(event.getUuid());
        assertFalse(event.getUuid().isEmpty());
        assertEquals("patient", event.getCategory());
        assertEquals(null, event.getUri());
        assertNotNull(event.getTimeStamp());
        assertEquals("openelis",event.getTitle());
    }
}

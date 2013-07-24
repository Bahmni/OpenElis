package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.service.PatientPublisherService;
import org.ict4h.atomfeed.server.service.Event;
import org.ict4h.atomfeed.server.service.EventService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientPublisherServiceImplTest {
    private final String SAMPLE_PATIENT_ID = "GAN12345";

    @Mock
    EventService eventService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldConstructUrlOfPatientFromPropertiesAndPatientIdentifier() {
        PatientPublisherService patientPublisherService = new PatientPublisherServiceImpl(eventService);
        PatientIdentity identity = new PatientIdentity();
        identity.setIdentityData(SAMPLE_PATIENT_ID);

        patientPublisherService.publish(identity);

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(eventService).notify(captor.capture());
        Event event = captor.getValue();
        assertEquals("/patient/" + SAMPLE_PATIENT_ID, event.getContents());
    }

    @Test
    public void shouldPopulateDefaultFieldsWhenCallingEventService() {
        PatientPublisherService patientPublisherService = new PatientPublisherServiceImpl(eventService);
        PatientIdentity identity = new PatientIdentity();
        identity.setIdentityData(SAMPLE_PATIENT_ID);

        patientPublisherService.publish(identity);

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

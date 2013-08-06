package us.mn.state.health.lims.ws.handler;

import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;
import org.bahmni.openelis.domain.CompletePatientDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientHandlerTest {

    @Mock
    private BahmniPatientService bahmniPatientService;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void shouldHandlePatientResourceRequests() {
        PatientHandler patientHandler = new PatientHandler(null);

        assertTrue(patientHandler.canHandle("patient"));
        assertTrue(patientHandler.canHandle("Patient"));
        assertFalse(patientHandler.canHandle("notPatient"));
        assertFalse(patientHandler.canHandle(""));
    }

    @Test
    public void shouldDelegateResourceRetrievalToService() {
        PatientHandler patientHandler = new PatientHandler(bahmniPatientService);
        CompletePatientDetails expectedPatientDetails = new CompletePatientDetails(null, null, null, null, null, null);
        when(bahmniPatientService.getCompletePatientDetails("GAN12345")).thenReturn(expectedPatientDetails);

        CompletePatientDetails completePatientDetails = patientHandler.handle("GAN12345");

        assertEquals(expectedPatientDetails, completePatientDetails);
    }
}

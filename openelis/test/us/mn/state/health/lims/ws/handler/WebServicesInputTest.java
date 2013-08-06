package us.mn.state.health.lims.ws.handler;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class WebServicesInputTest {

    @Test
    public void shouldRetrieveResourceName() {
        assertEquals("patient", new WebServicesInput("http://localhost:8080/openelis/ws/rest/patient/GAN12345").getResourceName());
        assertEquals("patient", new WebServicesInput("http://localhost:8080/openelis/ws/rest/patient/GAN12345/").getResourceName());
        assertEquals("patient", new WebServicesInput("http://localhost:8080/openelis/ws/rest/patient/GAN12345/name").getResourceName());
    }

    @Test
    public void shouldRetrieveResourceUuid() {
        assertEquals("GAN12345", new WebServicesInput("http://localhost:8080/openelis/ws/rest/patient/GAN12345").getResourceUuid());
        assertEquals("GAN12345", new WebServicesInput("http://localhost:8080/openelis/ws/rest/patient/GAN12345/").getResourceUuid());
        assertEquals("GAN12345", new WebServicesInput("http://localhost:8080/openelis/ws/rest/patient/GAN12345/name").getResourceUuid());
        assertEquals(null, new WebServicesInput("http://localhost:8080/openelis/ws/rest/patient").getResourceUuid());
    }

    @Test
    public void shouldReturnNullIfUrlIncorrect() {
        assertEquals("patient", new WebServicesInput("/ws/rest/patient/GAN12345").getResourceName());
        assertEquals("GAN12345", new WebServicesInput("/ws/rest/patient/GAN12345").getResourceUuid());

        assertEquals(null, new WebServicesInput("/ws/rest").getResourceName());
        assertEquals(null, new WebServicesInput("/ws/rest").getResourceUuid());

        assertEquals(null, new WebServicesInput("/ws/rest/patient").getResourceName());
        assertEquals(null, new WebServicesInput("/ws/rest/patient").getResourceUuid());

        assertEquals(null, new WebServicesInput("/ws/something/patient/GAN12345").getResourceName());
        assertEquals(null, new WebServicesInput("/ws/something/patient/GAN12345").getResourceUuid());

        assertEquals(null, new WebServicesInput("patient/GAN12345").getResourceName());
        assertEquals(null, new WebServicesInput("patient/GAN12345").getResourceUuid());

        assertEquals(null, new WebServicesInput("completelyWrongUrl").getResourceName());
        assertEquals(null, new WebServicesInput("completelyWrongUrl").getResourceUuid());

    }

}

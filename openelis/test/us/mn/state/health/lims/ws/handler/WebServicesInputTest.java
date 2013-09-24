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

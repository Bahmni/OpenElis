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
    public void shouldCallGetPatientByUUID() throws Exception {
        PatientHandler patientHandler = new PatientHandler(bahmniPatientService);
        CompletePatientDetails completePatientDetails = new CompletePatientDetails(null, null, null, null, null, null);
        when(bahmniPatientService.getPatientByUUID("uuid")).thenReturn(completePatientDetails);
        assertEquals(completePatientDetails, patientHandler.handle("uuid"));
    }
}

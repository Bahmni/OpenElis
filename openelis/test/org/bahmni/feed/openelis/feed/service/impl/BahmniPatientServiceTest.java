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

package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.openelis.domain.CompletePatientDetails;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.*;

public class BahmniPatientServiceTest {

    @Test @Ignore
    public void shouldGetThePatient() throws IOException {
        BahmniPatientService bahmniPatientService = new BahmniPatientService();

        CompletePatientDetails patient = bahmniPatientService.getCompletePatientDetails("GAN12345");

        assertEquals("first", patient.getFirstName());
        assertEquals("last", patient.getLastName());
        assertEquals("GAN12345", patient.getPatientIdentifier());
        assertEquals("M", patient.getGender());
//        assertEquals("level1",patient.getAddress1()); TODO:add when adding the in memory DB
        assertNotNull(patient.getDateOfBirth());
    }

    @Test
    public void shouldReturnNullIfPatientWithGivenIdentifierDoesNotExist() {
        BahmniPatientService bahmniPatientService = new BahmniPatientService();

        CompletePatientDetails nonExistentPatient = bahmniPatientService.getCompletePatientDetails("GAN5678");

        assertNull(nonExistentPatient);
    }
}

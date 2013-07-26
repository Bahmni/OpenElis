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

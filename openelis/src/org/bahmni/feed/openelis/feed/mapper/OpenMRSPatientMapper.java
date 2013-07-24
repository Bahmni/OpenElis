package org.bahmni.feed.openelis.feed.mapper;

import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class OpenMRSPatientMapper {
    private ObjectMapper objectMapper;

    public OpenMRSPatientMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public OpenMRSPatient map(String patientJSON) throws IOException {
        return objectMapper.readValue(patientJSON, OpenMRSPatient.class);
    }
}
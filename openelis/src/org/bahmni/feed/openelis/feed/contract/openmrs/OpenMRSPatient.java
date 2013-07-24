package org.bahmni.feed.openelis.feed.contract.openmrs;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPatient {
    private OpenMRSPerson person;
    private List<OpenMRSPatientIdentifier> identifiers;

    public OpenMRSPatient(OpenMRSPerson person) {
        this.person = person;
    }

    public OpenMRSPatient() {
    }

    public OpenMRSPatient addIdentifier(OpenMRSPatientIdentifier identifier) {
        if (identifiers == null) identifiers = new ArrayList<OpenMRSPatientIdentifier>();

        identifiers.add(identifier);
        return this;
    }

    public OpenMRSPerson getPerson() {
        return person;
    }

    public void setPerson(OpenMRSPerson person) {
        this.person = person;
    }

    public List<OpenMRSPatientIdentifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<OpenMRSPatientIdentifier> identifiers) {
        this.identifiers = identifiers;
    }
}
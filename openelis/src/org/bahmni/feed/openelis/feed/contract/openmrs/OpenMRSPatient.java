package org.bahmni.feed.openelis.feed.contract.openmrs;

import java.util.List;

public class OpenMRSPatient {
    private OpenMRSPerson person;
    private List<OpenMRSPatientIdentifier> identifiers;

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
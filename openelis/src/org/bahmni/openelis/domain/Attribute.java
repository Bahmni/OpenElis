package org.bahmni.openelis.domain;

import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;

public class Attribute {
    private String name;
    private String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Attribute(PatientIdentity patientIdentity, PatientIdentityType patientIdentityType) {
        this.name = patientIdentityType.getIdentityType();
        this.value = patientIdentity.getIdentityData();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

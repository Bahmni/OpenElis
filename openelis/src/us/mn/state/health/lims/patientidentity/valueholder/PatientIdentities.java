package us.mn.state.health.lims.patientidentity.valueholder;

import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityTypes;

import java.util.ArrayList;
import java.util.List;

public class PatientIdentities extends ArrayList<PatientIdentity> {
    public PatientIdentities(List<PatientIdentity> patientIdentities) {
        super(patientIdentities);
    }

    public PatientIdentity findIdentity(String name, PatientIdentityTypes patientIdentityTypes) {
        PatientIdentityType patientIdentityType = patientIdentityTypes.find(name);
        for (PatientIdentity patientIdentity : this) {
            if (patientIdentity.isOfType(patientIdentityType)) return patientIdentity;
        }
        return null;
    }
}
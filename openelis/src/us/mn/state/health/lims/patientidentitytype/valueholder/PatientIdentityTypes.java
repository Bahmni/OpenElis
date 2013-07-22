package us.mn.state.health.lims.patientidentitytype.valueholder;

import java.util.ArrayList;
import java.util.Collection;

public class PatientIdentityTypes extends ArrayList<PatientIdentityType> {
    public PatientIdentityTypes(Collection<? extends PatientIdentityType> c) {
        super(c);
    }

    public PatientIdentityType find(String key) {
        for (PatientIdentityType patientIdentityType : this) {
            if (patientIdentityType.getIdentityType().equals(key)) return patientIdentityType;
        }
        return null;
    }
}
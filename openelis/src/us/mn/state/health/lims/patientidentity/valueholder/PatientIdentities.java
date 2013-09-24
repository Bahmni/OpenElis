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

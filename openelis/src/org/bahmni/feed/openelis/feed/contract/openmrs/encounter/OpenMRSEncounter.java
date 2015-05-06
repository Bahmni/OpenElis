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

package org.bahmni.feed.openelis.feed.contract.openmrs.encounter;

import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSEncounter {
    private String encounterUuid;
    private String patientUuid;
    private List<OpenMRSOrder> testOrders = new ArrayList<>();

    public OpenMRSEncounter() {
    }

    public OpenMRSEncounter(String encounterUuid, String patientUuid, List<OpenMRSOrder> testOrders) {

        this.encounterUuid = encounterUuid;
        this.testOrders = testOrders;
        this.patientUuid = patientUuid;
    }

    public void setEncounterUuid(String encounterUuid) {
        this.encounterUuid = encounterUuid;
    }

    public String getEncounterUuid() {
        return encounterUuid;
    }

    public List<OpenMRSOrder> getTestOrders() {
        return testOrders;
    }

    public void setTestOrders(List<OpenMRSOrder> orders) {
        this.testOrders = orders;
    }

    public boolean hasLabOrder() {
        for (OpenMRSOrder openMRSOrder : testOrders) {
            if (openMRSOrder.isLabOrder())
                return true;
        }
        return false;
    }

    public List<OpenMRSOrder> getLabOrders() {
        List<OpenMRSOrder> labOrders = new ArrayList<>();
        for (OpenMRSOrder openMRSOrder : testOrders) {
            if (openMRSOrder.isLabOrder() && !openMRSOrder.isVoided())
                labOrders.add(openMRSOrder);
        }
        return labOrders;
    }

    public String getPatientUuid() {
        return patientUuid;
    }

    public void setPatientUuid(String patientUuid) {
        this.patientUuid = patientUuid;
    }
}

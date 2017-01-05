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


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSEncounter {
    private String encounterUuid;
    private String patientUuid;
    private String locationUuid;
    private String locationName;
    private List<OpenMRSOrder> orders = new ArrayList<>();
    private List<OpenMRSProvider> providers = new ArrayList<>();

    public OpenMRSEncounter() {
    }

    public OpenMRSEncounter(String encounterUuid, String patientUuid, String locationUuid, String locationName, List<OpenMRSOrder> orders, List<OpenMRSProvider> providers) {

        this.encounterUuid = encounterUuid;
        this.locationUuid = locationUuid;
        this.locationName = locationName;
        this.orders = orders;
        this.patientUuid = patientUuid;
        this.providers = providers;
    }

    public void setEncounterUuid(String encounterUuid) {
        this.encounterUuid = encounterUuid;
    }

    public String getEncounterUuid() {
        return encounterUuid;
    }

    public List<OpenMRSOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<OpenMRSOrder> orders) {
        this.orders = orders;
    }

    public boolean hasLabOrder() {
        for (OpenMRSOrder openMRSOrder : orders) {
            if (openMRSOrder.isLabOrder())
                return true;
        }
        return false;
    }

    public List<OpenMRSOrder> getLabOrders() {
        List<OpenMRSOrder> labOrders = new ArrayList<>();
        for (OpenMRSOrder openMRSOrder : orders) {
            if (openMRSOrder.isLabOrder() && openMRSOrder.getDateStopped()==null && !"DISCONTINUE".equals(openMRSOrder.getAction()))
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

    public List<OpenMRSProvider> getProviders() {
        return providers;
    }

    public void setProviders(List<OpenMRSProvider> providers) {
        this.providers = providers;
    }

    public String getLocationUuid() {
        return locationUuid;
    }

    public void setLocationUuid(String locationUuid) {
        this.locationUuid = locationUuid;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}

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

package org.bahmni.feed.openelis.feed.contract.openmrs;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPatient {
    private String uuid;
    private OpenMRSPerson person;
    private List<OpenMRSPatientIdentifier> identifiers;

    public OpenMRSPatient(OpenMRSPerson person) {
        this.person = person;
    }

    public OpenMRSPatient() {
    }

    public OpenMRSPatient(String uuid, OpenMRSPerson person, List<OpenMRSPatientIdentifier> identifiers) {
        this.uuid = uuid;
        this.person = person;
        this.identifiers = identifiers;
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

    public String getUuid() {
        return uuid;
    }
}

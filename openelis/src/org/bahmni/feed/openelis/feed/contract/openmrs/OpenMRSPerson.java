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
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPerson {
    private OpenMRSName preferredName;
    private String uuid;
    private String gender;
    private Date birthdate;
    private boolean birthdateEstimated;
    private OpenMRSPersonAddress preferredAddress;
    private OpenMRSPersonAttributes attributes;

    public OpenMRSPerson(OpenMRSName preferredName, String uuid, String gender, Date birthdate, boolean birthdateEstimated, OpenMRSPersonAddress preferredAddress) {
        this.preferredName = preferredName;
        this.uuid = uuid;
        this.gender = gender;
        this.birthdate = birthdate;
        this.preferredAddress = preferredAddress;
        this.birthdateEstimated = birthdateEstimated;
    }

    public OpenMRSPerson addAttribute(OpenMRSPersonAttribute attribute) {
        if (attributes == null) attributes = new OpenMRSPersonAttributes();
        attributes.add(attribute);
        return this;
    }

    public OpenMRSPerson() {
    }

    public OpenMRSPersonAddress getPreferredAddress() {
        return preferredAddress;
    }

    public void setPreferredAddress(OpenMRSPersonAddress preferredAddress) {
        this.preferredAddress = preferredAddress;
    }

    public List<OpenMRSPersonAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<OpenMRSPersonAttribute> attributes) {
        this.attributes = new OpenMRSPersonAttributes(attributes);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public OpenMRSName getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(OpenMRSName preferredName) {
        this.preferredName = preferredName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public OpenMRSPersonAttribute findAttributeByAttributeTypeDisplayName(String displayName) {
        for (OpenMRSPersonAttribute personAttribute : attributes) {
            if (personAttribute.getAttributeType().getDisplay().equals(displayName)) {
                return personAttribute;
            }
        }
        return null;
    }

    public boolean isBirthdateEstimated() {
        return birthdateEstimated;
    }

    public void setBirthdateEstimated(boolean birthdateEstimated) {
        this.birthdateEstimated = birthdateEstimated;
    }
}

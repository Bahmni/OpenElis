package org.bahmni.feed.openelis.feed.contract.openmrs;

import java.util.Date;
import java.util.List;

public class OpenMRSPerson {
    private OpenMRSName preferredName;
    private String uuid;
    private String gender;
    private Date birthdate;
    private OpenMRSPersonAddress preferredAddress;
    private OpenMRSPersonAttributes attributes;

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
}
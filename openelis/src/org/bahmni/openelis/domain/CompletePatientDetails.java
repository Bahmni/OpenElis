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

package org.bahmni.openelis.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bahmni.feed.openelis.utils.JsonDateSerializer;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.person.valueholder.Person;

import java.util.Date;
import java.util.List;

public class CompletePatientDetails {
    private Patient patient;
    private Person person;
    private PatientIdentity patientIdentity;
    private List<PersonAddress> personAddresses;
    private List<AddressPart> addressParts;
    private List<Attribute> attributes;

    public CompletePatientDetails(Patient patient, Person person, PatientIdentity patientIdentity, List<PersonAddress> personAddresses, List<AddressPart> addressParts, List<Attribute> attributes) {
        this.patient = patient;
        this.person = person;
        this.patientIdentity = patientIdentity;
        this.personAddresses = personAddresses;
        this.addressParts = addressParts;
        this.attributes = attributes;
    }

    public String getPatientUUID() {
        return patient.getUuid();
    }

    public String getFirstName() {
        return person.getFirstName();
    }

    public String getLastName() {
        return person.getLastName();
    }

    public String getPatientIdentifier() {
        return patientIdentity.getIdentityData();
    }

    public String getGender() {
        return patient.getGender();
    }

    @JsonSerialize(using=JsonDateSerializer.class)
    public Date getDateOfBirth() {
        return patient.getBirthDate();
    }

    public String getAddress1() {
        return partValueFor("level1");
    }

    public String getCityVillage() {
        return partValueFor("level2");
    }

    public String getAddress2() {
        return partValueFor("level3");
    }

    public String getAddress3() {
        return partValueFor("level4");
    }

    public String getCountyDistrict() {
        return partValueFor("level5");
    }

    public String getStateProvince() {
        return partValueFor("level6");
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    private String partValueFor(String partId) {
        for (PersonAddress personAddress : personAddresses) {
            if(personAddress.getAddressPartId().equals(getAddressPartId(partId))){
                return personAddress.getValue();
            }
        }
        return "";
    }

    private String getAddressPartId(String levelName){
        for (AddressPart addressPart : addressParts) {
            if(addressPart.getPartName().equals(levelName)){
                return addressPart.getId();
            }
        }
        return null;
    }
}

package org.bahmni.openelis.domain;

import org.bahmni.feed.openelis.utils.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
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

    public CompletePatientDetails(Patient patient, Person person, PatientIdentity patientIdentity, List<PersonAddress> personAddresses, List<AddressPart> addressParts) {
        this.patient = patient;
        this.person = person;
        this.patientIdentity = patientIdentity;
        this.personAddresses = personAddresses;
        this.addressParts = addressParts;
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
        for (PersonAddress personAddress : personAddresses) {
            if(personAddress.getAddressPartId().equals(getAddressPartId("level1"))){
                return personAddress.getValue();
            }
        }
        return "";
    }

    public String getCityVillage() {
        for (PersonAddress personAddress : personAddresses) {
            if(personAddress.getAddressPartId().equals(getAddressPartId("level2"))){
                return personAddress.getValue();
            }
        }
        return "";
    }

    public String getAddress2() {
        for (PersonAddress personAddress : personAddresses) {
            if(personAddress.getAddressPartId().equals(getAddressPartId("level3"))){
                return personAddress.getValue();
            }
        }
        return "";
    }

    public String getAddress3() {
        for (PersonAddress personAddress : personAddresses) {
            if(personAddress.getAddressPartId().equals(getAddressPartId("level4"))){
                return personAddress.getValue();
            }
        }
        return "";
    }

    public String getCountyDistrict() {
        for (PersonAddress personAddress : personAddresses) {
            if(personAddress.getAddressPartId().equals(getAddressPartId("level5"))){
                return personAddress.getValue();
            }
        }
        return "";
    }

    public String getStateProvince() {
        for (PersonAddress personAddress : personAddresses) {
            if(personAddress.getAddressPartId().equals(getAddressPartId("level6"))){
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

    public String getHealthCenter(){
        HealthCenter healthCenter = patient.getHealthCenter();
        return healthCenter == null? null : healthCenter.getName();
    }
}

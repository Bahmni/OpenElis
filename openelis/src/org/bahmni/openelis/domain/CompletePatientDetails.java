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
    private List<Attribute> attributes;

    public CompletePatientDetails(Patient patient, Person person, PatientIdentity patientIdentity, List<PersonAddress> personAddresses, List<AddressPart> addressParts, List<Attribute> attributes) {
        this.patient = patient;
        this.person = person;
        this.patientIdentity = patientIdentity;
        this.personAddresses = personAddresses;
        this.addressParts = addressParts;
        this.attributes = attributes;
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

    public String getHealthCenter(){
        HealthCenter healthCenter = patient.getHealthCenter();
        return healthCenter == null? null : healthCenter.getName();
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

package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPerson;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPersonAttribute;
import us.mn.state.health.lims.address.dao.AddressPartDAO;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.valueholder.AddressParts;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityTypes;
import us.mn.state.health.lims.person.dao.PersonDAO;
import us.mn.state.health.lims.person.valueholder.Person;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BahmniPatientService {
    private PatientDAO patientDAO;
    private PersonDAO personDAO;
    private PatientIdentityDAO patientIdentityDAO;
    private PersonAddressDAO personAddressDAO;
    private AddressPartDAO addressPartDAO;
    private PatientIdentityTypeDAO patientIdentityTypeDAO;

    public BahmniPatientService(PatientDAO patientDAO, PersonDAO personDAO, PatientIdentityDAO patientIdentityDAO, PersonAddressDAO personAddressDAO, AddressPartDAO addressPartDAO, PatientIdentityTypeDAO patientIdentityTypeDAO) {
        this.patientDAO = patientDAO;
        this.personDAO = personDAO;
        this.patientIdentityDAO = patientIdentityDAO;
        this.personAddressDAO = personAddressDAO;
        this.addressPartDAO = addressPartDAO;
        this.patientIdentityTypeDAO = patientIdentityTypeDAO;
    }

    public void create(OpenMRSPatient openMRSPatient) {
        Person person = new Person();
        OpenMRSPerson openMRSPerson = openMRSPatient.getPerson();
        person.setFirstName(openMRSPerson.getPreferredName().getGivenName());
        person.setLastName(openMRSPerson.getPreferredName().getFamilyName());
        personDAO.insertData(person);

        AddressParts addressParts = new AddressParts(addressPartDAO.getAll());
        List<PersonAddress> personAddressList = new ArrayList<PersonAddress>(addressParts.size());
        personAddressList.add(PersonAddress.create(person, addressParts, "level1", openMRSPerson.getPreferredAddress().getAddress1()));
        personAddressList.add(PersonAddress.create(person, addressParts, "level2", openMRSPerson.getPreferredAddress().getCityVillage()));
        personAddressList.add(PersonAddress.create(person, addressParts, "level3", openMRSPerson.getPreferredAddress().getAddress2()));
        personAddressList.add(PersonAddress.create(person, addressParts, "level4", openMRSPerson.getPreferredAddress().getAddress3()));
        personAddressList.add(PersonAddress.create(person, addressParts, "level5", openMRSPerson.getPreferredAddress().getCountyDistrict()));
        personAddressList.add(PersonAddress.create(person, addressParts, "level6", openMRSPerson.getPreferredAddress().getStateProvince()));
        personAddressDAO.insert(personAddressList);

        Patient patient = new Patient();
        patient.setGender(openMRSPerson.getGender());
        patient.setBirthDate(new Timestamp(openMRSPerson.getBirthdate().getTime()));
        patientDAO.insertData(patient);

        PatientIdentityTypes patientIdentityTypes = new PatientIdentityTypes(patientIdentityTypeDAO.getAllPatientIdenityTypes());
        addPatientIdentity(patient, patientIdentityTypes, "", openMRSPatient.getIdentifiers().get(0).getIdentifier());
        OpenMRSPersonAttribute primaryRelativeAttribute = openMRSPerson.findAttributeByAttributeTypeDisplayName("primaryRelative");
        if (primaryRelativeAttribute != null)
            addPatientIdentity(patient, patientIdentityTypes, "MOTHER", primaryRelativeAttribute.getValue());

        OpenMRSPersonAttribute occupationAttribute = openMRSPerson.findAttributeByAttributeTypeDisplayName("occupation");
        if (occupationAttribute != null)
            addPatientIdentity(patient, patientIdentityTypes, "OCCUPATION", occupationAttribute.getValue());
    }

    private void addPatientIdentity(Patient patient, PatientIdentityTypes patientIdentityTypes, String key, String identifier) {
        PatientIdentityType patientIdentityType = patientIdentityTypes.find(key);
        PatientIdentity patientIdentity = new PatientIdentity();
        patientIdentity.setId(patientIdentityType.getId());
        patientIdentity.setPatientId(patient.getId());
        patientIdentity.setIdentityData(identifier);
        patientIdentityDAO.insertData(patientIdentity);
    }
}
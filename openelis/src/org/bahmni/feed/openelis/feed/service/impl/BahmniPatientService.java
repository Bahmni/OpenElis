package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPerson;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.person.dao.PersonDAO;
import us.mn.state.health.lims.person.valueholder.Person;

public class BahmniPatientService {
    private PatientDAO patientDAO;
    private PersonDAO personDAO;
    private PatientIdentityDAO patientIdentityDAO;
    private PersonAddressDAO personAddressDAO;

    public BahmniPatientService(PatientDAO patientDAO, PersonDAO personDAO, PatientIdentityDAO patientIdentityDAO, PersonAddressDAO personAddressDAO) {
        this.patientDAO = patientDAO;
        this.personDAO = personDAO;
        this.patientIdentityDAO = patientIdentityDAO;
        this.personAddressDAO = personAddressDAO;
    }

    public void create(OpenMRSPatient openMRSPatient) {
        Person person = new Person();
        OpenMRSPerson openMRSPerson = openMRSPatient.getPerson();
        person.setFirstName(openMRSPerson.getPreferredName().getGivenName());
        person.setLastName(openMRSPerson.getPreferredName().getFamilyName());
        person.setStreetAddress(openMRSPerson.getPreferredAddress().getAddress1());
        person.setCity(openMRSPerson.getPreferredAddress().getCountyDistrict());
        person.setState(openMRSPerson.getPreferredAddress().getStateProvince());
        personDAO.insertData(person);
        Patient patient = new Patient();
//        patientDAO.insertData(patient)
    }
}
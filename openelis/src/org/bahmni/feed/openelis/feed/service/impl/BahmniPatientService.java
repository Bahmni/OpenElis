package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPerson;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPersonAttribute;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPersonAttributeType;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.bahmni.openelis.domain.Attribute;
import org.bahmni.openelis.domain.CompletePatientDetails;
import us.mn.state.health.lims.address.dao.AddressPartDAO;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.AddressParts;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityTypes;
import us.mn.state.health.lims.person.dao.PersonDAO;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

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
    private AuditingService auditingService;
    private HealthCenterDAO healthCenterDAO;

    public BahmniPatientService() {
        this(new PatientDAOImpl(), new PersonDAOImpl(), new PatientIdentityDAOImpl(),
                new PersonAddressDAOImpl(), new AddressPartDAOImpl(), new PatientIdentityTypeDAOImpl(),
                new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()), new HealthCenterDAOImpl());
    }

    public BahmniPatientService(PatientDAO patientDAO, PersonDAO personDAO, PatientIdentityDAO patientIdentityDAO,
                                PersonAddressDAO personAddressDAO, AddressPartDAO addressPartDAO, PatientIdentityTypeDAO patientIdentityTypeDAO,
                                AuditingService auditingService, HealthCenterDAO healthCenterDAO) {
        this.patientDAO = patientDAO;
        this.personDAO = personDAO;
        this.patientIdentityDAO = patientIdentityDAO;
        this.personAddressDAO = personAddressDAO;
        this.addressPartDAO = addressPartDAO;
        this.patientIdentityTypeDAO = patientIdentityTypeDAO;
        this.auditingService = auditingService;
        this.healthCenterDAO = healthCenterDAO;
    }

    public void create(OpenMRSPatient openMRSPatient) {
        String sysUserId = auditingService.getSysUserId();
        Person person = new Person();
        OpenMRSPerson openMRSPerson = openMRSPatient.getPerson();
        person.setFirstName(openMRSPerson.getPreferredName().getGivenName());
        person.setLastName(openMRSPerson.getPreferredName().getFamilyName());
        person.setSysUserId(sysUserId);
        personDAO.insertData(person);

        AddressParts addressParts = new AddressParts(addressPartDAO.getAll());
        List<PersonAddress> personAddressList = new ArrayList<PersonAddress>(addressParts.size());
        personAddressList.add(PersonAddress.create(person, addressParts, "level1", openMRSPerson.getPreferredAddress().getAddress1(), sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level2", openMRSPerson.getPreferredAddress().getCityVillage(), sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level3", openMRSPerson.getPreferredAddress().getAddress2(), sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level4", openMRSPerson.getPreferredAddress().getAddress3(), sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level5", openMRSPerson.getPreferredAddress().getCountyDistrict(), sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level6", openMRSPerson.getPreferredAddress().getStateProvince(), sysUserId));
        personAddressDAO.insert(personAddressList);

        Patient patient = new Patient();
        patient.setGender(openMRSPerson.getGender());
        patient.setBirthDate(new Timestamp(openMRSPerson.getBirthdate().getTime()));
        patient.setSysUserId(sysUserId);
        patient.setPerson(person);
        patient.setHealthCenter(healthCenterOf(healthCenterStringFrom(openMRSPerson)));
        patientDAO.insertData(patient);

        PatientIdentityTypes patientIdentityTypes = new PatientIdentityTypes(patientIdentityTypeDAO.getAllPatientIdenityTypes());
        addPatientIdentity(patient, patientIdentityTypes, "ST", openMRSPatient.getIdentifiers().get(0).getIdentifier(), sysUserId);
        OpenMRSPersonAttribute primaryRelativeAttribute = openMRSPerson.findAttributeByAttributeTypeDisplayName(OpenMRSPersonAttributeType.MOTHERS_NAME);
        if (primaryRelativeAttribute != null)
            addPatientIdentity(patient, patientIdentityTypes, "MOTHER", primaryRelativeAttribute.getValue(), sysUserId);

        OpenMRSPersonAttribute occupationAttribute = openMRSPerson.findAttributeByAttributeTypeDisplayName(OpenMRSPersonAttributeType.OCCUPATION);
        if (occupationAttribute != null)
            addPatientIdentity(patient, patientIdentityTypes, "OCCUPATION", occupationAttribute.getValue(), sysUserId);
    }

    private HealthCenter healthCenterOf(String healthCenterString) {
        if (isNullOrEmpty(healthCenterString)) return null;
        HealthCenter healthCenter = healthCenterDAO.getByName(healthCenterString);
        if (healthCenter != null) return healthCenter;
        throw new LIMSRuntimeException("HealthCenter " + healthCenterString + " is not configured in OpenELIS");
    }

    private boolean isNullOrEmpty(String healthCenterString) {
        return healthCenterString == null || healthCenterString.isEmpty();
    }

    private String healthCenterStringFrom(OpenMRSPerson openMRSPerson) {
        return openMRSPerson.findAttributeByAttributeTypeDisplayName(OpenMRSPersonAttributeType.HEALTH_CENTER).getValue();
    }

    public CompletePatientDetails getCompletePatientDetails(String patientId){
        PatientIdentityType identityType = primaryIdentityType();
        List<PatientIdentity> patientIdentities = patientIdentityDAO.getPatientIdentitiesByValueAndType(patientId, identityType.getId());
        if (patientIdentities == null || patientIdentities.size() == 0) {
            return null;
        }
        PatientIdentity identity = patientIdentities.get(0);
        Patient patient = patientDAO.getData(identity.getPatientId());
        Person person = patient.getPerson();

        List<PersonAddress> personAddresses = personAddressDAO.getAddressPartsByPersonId(person.getId());
        List<AddressPart> addressParts = addressPartDAO.getAll();
        List<Attribute> attributes = getAttributes(patient);

        return new CompletePatientDetails(patient,person, identity, personAddresses, addressParts, attributes);
    }

    private PatientIdentityType primaryIdentityType() {
        return patientIdentityTypeDAO.getNamedIdentityType("ST");
    }

    private List<Attribute> getAttributes(Patient patient) {
        List<PatientIdentity> patientIdentitiesForPatient = patientIdentityDAO.getPatientIdentitiesForPatient(patient.getId());
        List<Attribute> attributes = new ArrayList<>();
        for (PatientIdentity patientIdentity : patientIdentitiesForPatient) {
            PatientIdentityType patientIdentityType = patientIdentityTypeDAO.get(patientIdentity.getIdentityTypeId());
            addAttribute(attributes, patientIdentity, patientIdentityType);
        }
        return attributes;
    }

    private void addAttribute(List<Attribute> attributes, PatientIdentity patientIdentity, PatientIdentityType patientIdentityType) {
        if (!patientIdentityType.equals(primaryIdentityType())) {
            attributes.add(new Attribute(patientIdentity, patientIdentityType));
        }
    }

    private void addPatientIdentity(Patient patient, PatientIdentityTypes patientIdentityTypes, String key, String identifier, String sysUserId) {
        PatientIdentityType patientIdentityType = patientIdentityTypes.find(key);
        PatientIdentity patientIdentity = new PatientIdentity();
        patientIdentity.setIdentityTypeId(patientIdentityType.getId());
        patientIdentity.setPatientId(patient.getId());
        patientIdentity.setIdentityData(identifier);
        patientIdentity.setSysUserId(sysUserId);
        patientIdentityDAO.insertData(patientIdentity);
    }
}
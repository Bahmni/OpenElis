package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.contract.openmrs.*;
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
import us.mn.state.health.lims.address.valueholder.PersonAddresses;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.resources.ResourceLocator;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentities;
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

    private static final String PRIMARY_RELATIVE_KEY_NAME = "PRIMARYRELATIVE";
    private static final String OCCUPATION_KEY_NAME = "OCCUPATION";

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

    public void createOrUpdate(OpenMRSPatient openMRSPatient) {
        String sysUserId = auditingService.getSysUserId();
        Patient patient = patientDAO.getPatientByUUID(openMRSPatient.getPerson().getUuid());
        if (patient == null) {
            create(openMRSPatient, sysUserId);
        } else {
            update(patient, openMRSPatient, sysUserId);
        }
    }

    private void update(Patient patient, OpenMRSPatient openMRSPatient, String sysUserId) {
        Person person = patient.getPerson();
        populatePerson(openMRSPatient, sysUserId, person);
        personDAO.updateData(person);

        OpenMRSPerson openMRSPerson = openMRSPatient.getPerson();
        OpenMRSPersonAddress preferredAddress = openMRSPerson.getPreferredAddress();
        AddressParts addressParts = new AddressParts(addressPartDAO.getAll());
        if (preferredAddress != null) {
            PersonAddresses personAddresses = new PersonAddresses(personAddressDAO.getAddressPartsByPersonId(person.getId()));
            PersonAddress level1Address = personAddresses.findByPartName("level1", addressParts);
            level1Address.updateValue(preferredAddress.getAddress1(), sysUserId);
            PersonAddress level2Address = personAddresses.findByPartName("level2", addressParts);
            level2Address.updateValue(preferredAddress.getCityVillage(), sysUserId);
            PersonAddress level3Address = personAddresses.findByPartName("level3", addressParts);
            level3Address.updateValue(preferredAddress.getAddress2(), sysUserId);
            PersonAddress level4Address = personAddresses.findByPartName("level4", addressParts);
            level4Address.updateValue(preferredAddress.getAddress3(), sysUserId);
            PersonAddress level5Address = personAddresses.findByPartName("level5", addressParts);
            level5Address.updateValue(preferredAddress.getCountyDistrict(), sysUserId);
            PersonAddress level6Address = personAddresses.findByPartName("level6", addressParts);
            level6Address.updateValue(preferredAddress.getStateProvince(), sysUserId);
        }

        populatePatient(sysUserId, openMRSPerson, patient);

        PatientIdentityTypes patientIdentityTypes = new PatientIdentityTypes(patientIdentityTypeDAO.getAllPatientIdenityTypes());
        PatientIdentities patientIdentities = new PatientIdentities(patientIdentityDAO.getPatientIdentitiesForPatient(patient.getId()));

        setIdentityData(patientIdentityTypes, patientIdentities, PRIMARY_RELATIVE_KEY_NAME, getAttributeValue(openMRSPerson, OpenMRSPersonAttributeType.PRIMARY_RELATIVE), sysUserId);
        setIdentityData(patientIdentityTypes, patientIdentities, OCCUPATION_KEY_NAME, getAttributeDisplay(openMRSPerson, OpenMRSPersonAttributeType.OCCUPATION), sysUserId);
    }

    private void setIdentityData(PatientIdentityTypes patientIdentityTypes, PatientIdentities patientIdentities, String identityName, String attributeValue, String sysUserId) {
        PatientIdentity patientIdentity = patientIdentities.findIdentity(identityName, patientIdentityTypes);
        patientIdentity.setIdentityData(attributeValue);
        patientIdentity.setSysUserId(sysUserId);
        patientIdentityDAO.updateData(patientIdentity);
    }

    private String getAttributeValue(OpenMRSPerson openMRSPerson, String displayName) {
        OpenMRSPersonAttribute openMRSPersonAttribute = openMRSPerson.findAttributeByAttributeTypeDisplayName(displayName);
        return openMRSPersonAttribute != null ? openMRSPersonAttribute.getValue() : null;
    }

    private String getAttributeDisplay(OpenMRSPerson openMRSPerson, String displayName) {
        OpenMRSPersonAttribute openMRSPersonAttribute = openMRSPerson.findAttributeByAttributeTypeDisplayName(displayName);
        return openMRSPersonAttribute != null ? openMRSPersonAttribute.getDisplay() : null;
    }

    private void create(OpenMRSPatient openMRSPatient, String sysUserId) {
        Person person = new Person();
        OpenMRSPerson openMRSPerson = populatePerson(openMRSPatient, sysUserId, person);
        personDAO.insertData(person);

        AddressParts addressParts = new AddressParts(addressPartDAO.getAll());
        List<PersonAddress> personAddressList = new ArrayList<>(addressParts.size());
        OpenMRSPersonAddress preferredAddress = openMRSPerson.getPreferredAddress();
        if (preferredAddress != null) {
            personAddressList.add(PersonAddress.create(person, addressParts, "level1", preferredAddress.getAddress1(), sysUserId));
            personAddressList.add(PersonAddress.create(person, addressParts, "level2", preferredAddress.getCityVillage(), sysUserId));
            personAddressList.add(PersonAddress.create(person, addressParts, "level3", preferredAddress.getAddress2(), sysUserId));
            personAddressList.add(PersonAddress.create(person, addressParts, "level4", preferredAddress.getAddress3(), sysUserId));
            personAddressList.add(PersonAddress.create(person, addressParts, "level5", preferredAddress.getCountyDistrict(), sysUserId));
            personAddressList.add(PersonAddress.create(person, addressParts, "level6", preferredAddress.getStateProvince(), sysUserId));
        }
        personAddressDAO.insert(personAddressList);

        Patient patient = new Patient();
        patient.setPerson(person);
        populatePatient(sysUserId, openMRSPerson, patient);
        patientDAO.insertData(patient);

        PatientIdentityTypes patientIdentityTypes = new PatientIdentityTypes(patientIdentityTypeDAO.getAllPatientIdenityTypes());
        addPatientIdentity(patient, patientIdentityTypes, "ST", openMRSPatient.getIdentifiers().get(0).getIdentifier(), sysUserId);

        String primaryRelative = getAttributeValue(openMRSPerson, OpenMRSPersonAttributeType.PRIMARY_RELATIVE);
        if (primaryRelative != null) {
            addPatientIdentity(patient, patientIdentityTypes, PRIMARY_RELATIVE_KEY_NAME, primaryRelative, sysUserId);
        }
        String occupation = getAttributeDisplay(openMRSPerson, OpenMRSPersonAttributeType.OCCUPATION);
        if (occupation != null) {
            addPatientIdentity(patient, patientIdentityTypes, OCCUPATION_KEY_NAME, occupation, sysUserId);
        }
    }

    private void populatePatient(String sysUserId, OpenMRSPerson openMRSPerson, Patient patient) {
        patient.setGender(openMRSPerson.getGender());
        if(openMRSPerson.isBirthdateEstimated()) {
            patient.setBirthDateForDisplay(DateUtil.convertDateToAmbiguousStringDate(openMRSPerson.getBirthdate()));
        }
        else {
            patient.setBirthDate(new Timestamp(openMRSPerson.getBirthdate().getTime()));
        }
        patient.setSysUserId(sysUserId);
        patient.setUuid(openMRSPerson.getUuid());
        OpenMRSPersonAttribute healthCenterAttribute = openMRSPerson.findAttributeByAttributeTypeDisplayName(OpenMRSPersonAttributeType.HEALTH_CENTER);
        if (healthCenterAttribute != null)
            patient.setHealthCenter(healthCenterOf(healthCenterAttribute.getValue()));
    }

    private OpenMRSPerson populatePerson(OpenMRSPatient openMRSPatient, String sysUserId, Person person) {
        OpenMRSPerson openMRSPerson = openMRSPatient.getPerson();
        person.setFirstName(openMRSPerson.getPreferredName().getGivenName());
        person.setLastName(openMRSPerson.getPreferredName().getFamilyName());
        person.setSysUserId(sysUserId);
        return openMRSPerson;
    }

    private HealthCenter healthCenterOf(String healthCenterString) {
        if (isNullOrEmpty(healthCenterString)) return null;
        HealthCenter healthCenter = healthCenterDAO.get(healthCenterString);
        if (healthCenter != null) return healthCenter;
        throw new LIMSRuntimeException(String.format("HealthCenter %s is not configured in OpenELIS", healthCenterString));
    }

    private boolean isNullOrEmpty(String healthCenterString) {
        return healthCenterString == null || healthCenterString.isEmpty();
    }

    public CompletePatientDetails getCompletePatientDetails(String patientId) {
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

        return new CompletePatientDetails(patient, person, identity, personAddresses, addressParts, attributes);
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
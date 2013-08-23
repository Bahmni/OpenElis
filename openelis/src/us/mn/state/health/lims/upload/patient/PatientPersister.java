package us.mn.state.health.lims.upload.patient;

import org.apache.log4j.Logger;
import org.bahmni.csv.EntityPersister;
import org.bahmni.csv.RowResult;
import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.hibernate.Transaction;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressParts;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityTypes;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PatientPersister implements EntityPersister<CSVPatient> {
    private static Logger logger = Logger.getLogger(PatientPersister.class);

    @Override
    public RowResult<CSVPatient> persist(CSVPatient csvPatient) {
        Transaction transaction = null;
        try {
            logger.info("Persisting " + csvPatient);
            transaction = HibernateUtil.getSession().getTransaction();
            transaction.begin();

            String sysUserId = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()).getSysUserId();
            create(csvPatient, sysUserId);

            transaction.commit();
        } catch (Exception e) {
            logger.error(e);
            if (transaction != null) transaction.rollback();

            return new RowResult<>(csvPatient, e);
        }
        return RowResult.SUCCESS;
    }

    @Override
    public RowResult<CSVPatient> validate(CSVPatient patient) {
        return RowResult.SUCCESS;
    }

    private void create(CSVPatient csvPatient, String sysUserId) throws ParseException {
        Person person = getPerson(csvPatient, sysUserId);
        new PersonDAOImpl().insertData(person);

        AddressParts addressParts = new AddressParts(new AddressPartDAOImpl().getAll());
        List<PersonAddress> personAddressList = new ArrayList<>(addressParts.size());

        personAddressList.add(PersonAddress.create(person, addressParts, "level1", csvPatient.houseStreetName, sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level2", csvPatient.cityVillage, sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level3", csvPatient.gramPanchayat, sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level4", csvPatient.tehsil, sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level5", csvPatient.district, sysUserId));
        personAddressList.add(PersonAddress.create(person, addressParts, "level6", csvPatient.state, sysUserId));
        new PersonAddressDAOImpl().insert(personAddressList);

        us.mn.state.health.lims.patient.valueholder.Patient patient = new us.mn.state.health.lims.patient.valueholder.Patient();
        patient.setPerson(person);
        populatePatient(sysUserId, csvPatient, patient);

        new PatientDAOImpl().insertData(patient);

        PatientIdentityTypes patientIdentityTypes = new PatientIdentityTypes(new PatientIdentityTypeDAOImpl().getAllPatientIdenityTypes());
        addPatientIdentity(patient, patientIdentityTypes, BahmniPatientService.REGISTRATION_KEY_NAME,
                csvPatient.heathCenter + csvPatient.registrationNumber, sysUserId);
        addPatientIdentity(patient, patientIdentityTypes, BahmniPatientService.PRIMARY_RELATIVE_KEY_NAME,
                csvPatient.fatherOrHusbandsName, sysUserId);
        addPatientIdentity(patient, patientIdentityTypes, BahmniPatientService.OCCUPATION_KEY_NAME,
                csvPatient.occupation, sysUserId);
    }

    private void addPatientIdentity(us.mn.state.health.lims.patient.valueholder.Patient patient, PatientIdentityTypes patientIdentityTypes, String key, String identifier, String sysUserId) {
        PatientIdentityType patientIdentityType = patientIdentityTypes.find(key);
        PatientIdentity patientIdentity = new PatientIdentity();
        patientIdentity.setIdentityTypeId(patientIdentityType.getId());
        patientIdentity.setPatientId(patient.getId());
        patientIdentity.setIdentityData(identifier);
        patientIdentity.setSysUserId(sysUserId);
        new PatientIdentityDAOImpl().insertData(patientIdentity);
    }

    private void populatePatient(String sysUserId, CSVPatient csvPatient, us.mn.state.health.lims.patient.valueholder.Patient patient)
            throws ParseException {
        patient.setGender(csvPatient.gender);
        if(csvPatient.dob != null) {
            patient.setBirthDate(new Timestamp(new SimpleDateFormat("dd-MM-yyyy").parse(csvPatient.dob).getTime()));
        } else {
            patient.setBirthDateForDisplay(csvPatient.age);
        }
        patient.setSysUserId(sysUserId);
        patient.setUuid(UUID.randomUUID().toString());
        patient.setHealthCenter(new HealthCenterDAOImpl().getByName(csvPatient.heathCenter));
    }

    private Person getPerson(CSVPatient csvPatient, String sysUserId) {
        Person person = new Person();
        person.setFirstName(csvPatient.firstName);
        person.setLastName(csvPatient.lastName);
        person.setSysUserId(sysUserId);
        return person;
    }
}

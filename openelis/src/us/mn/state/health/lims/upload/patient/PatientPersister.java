package us.mn.state.health.lims.upload.patient;

import org.apache.log4j.Logger;
import org.bahmni.csv.EntityPersister;
import org.bahmni.csv.RowResult;
import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.hibernate.Transaction;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressParts;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.gender.dao.GenderDAO;
import us.mn.state.health.lims.gender.daoimpl.GenderDAOImpl;
import us.mn.state.health.lims.gender.valueholder.Gender;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityTypes;
import us.mn.state.health.lims.person.dao.PersonDAO;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PatientPersister implements EntityPersister<CSVPatient> {
    private AuditingService auditingService;
    private PersonDAO personDAO;
    private PersonAddressDAO personAddressDAO;
    private PatientDAO patientDAO;
    private PatientIdentityDAO patientIdentityDAO;
    private PatientIdentityTypeDAO patientIdentityTypeDAO;
    private HealthCenterDAO healthCenterDAO;
    private GenderDAO genderDao;

    private static String sysUserId;
    private static PatientIdentityTypes patientIdentityTypes;
    private static AddressParts addressParts;

    private static Logger logger = Logger.getLogger(PatientPersister.class);
    public static final SimpleDateFormat DD_MM_YYYY_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private List<HealthCenter> allHealthCenters;
    private List<Gender> allGenders;

    public PatientPersister() {
        this(new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()), new PersonDAOImpl(),
                new PersonAddressDAOImpl(), new PatientDAOImpl(), new PatientIdentityDAOImpl(),
                new PatientIdentityTypeDAOImpl(), new HealthCenterDAOImpl(), new GenderDAOImpl());
    }

    public PatientPersister(AuditingService auditingService, PersonDAO personDAO, PersonAddressDAO personAddressDAO,
                            PatientDAO patientDAO, PatientIdentityDAO patientIdentityDAO,
                            PatientIdentityTypeDAO patientIdentityTypeDAO, HealthCenterDAO healthCenterDAO, GenderDAO genderDao) {
        this.auditingService = auditingService;
        this.personDAO = personDAO;
        this.personAddressDAO = personAddressDAO;
        this.patientDAO = patientDAO;
        this.patientIdentityDAO = patientIdentityDAO;
        this.patientIdentityTypeDAO = patientIdentityTypeDAO;
        this.healthCenterDAO = healthCenterDAO;
        this.genderDao = genderDao;
    }

    @Override
    public RowResult<CSVPatient> persist(CSVPatient csvPatient) {
        Transaction transaction = null;
        try {
            logger.debug("Persisting " + csvPatient);
            transaction = HibernateUtil.getSession().beginTransaction();

            Person newPerson = getPerson(csvPatient);

            personDAO.insertData(newPerson);
            personAddressDAO.insert(getPersonAddresses(csvPatient, newPerson));
            Patient newPatient = getPatient(csvPatient, newPerson);
            patientDAO.insertData(newPatient);

            patientIdentityDAO.insertData(getPatientIdentity(newPatient, BahmniPatientService.REGISTRATION_KEY_NAME,
                    csvPatient.healthCenter + csvPatient.registrationNumber));
            patientIdentityDAO.insertData(getPatientIdentity(newPatient, BahmniPatientService.PRIMARY_RELATIVE_KEY_NAME,
                    csvPatient.fatherOrHusbandsName));
            patientIdentityDAO.insertData(getPatientIdentity(newPatient, BahmniPatientService.OCCUPATION_KEY_NAME,
                    csvPatient.occupation));

            transaction.commit();
        } catch (Exception e) {
            logger.warn(e);
            if (transaction != null) transaction.rollback();

            return new RowResult<>(csvPatient, e);
        }
        return new RowResult(csvPatient);
    }

    @Override
    public RowResult<CSVPatient> validate(CSVPatient csvPatient) {
        logger.debug("Validating " + csvPatient);

        StringBuilder errorMessage = new StringBuilder();

        if (isEmpty(csvPatient.healthCenter))
            errorMessage.append("Health Center is mandatory.\n");
        if (isEmpty(csvPatient.registrationNumber))
            errorMessage.append("Registration Number is mandatory.\n");
        if (isEmpty(csvPatient.firstName))
            errorMessage.append("First Name is mandatory.\n");
        if (isEmpty(csvPatient.gender))
            errorMessage.append("Gender is mandatory.\n");
        if (isEmpty(csvPatient.cityVillage))
            errorMessage.append("Village is mandatory.\n");
        if (areBothEmpty(csvPatient.age, csvPatient.dob))
            errorMessage.append("Either Age or DOB is mandatory.\n");

        try {
            if (!isEmpty(csvPatient.dob))
                DD_MM_YYYY_DATE_FORMAT.parse(csvPatient.dob);
        } catch (ParseException e) {
            errorMessage.append("DOB should be dd-mm-yyyy.\n");
        }

        try {
            if (!isEmpty(csvPatient.age))
                Integer.parseInt(csvPatient.age);
        } catch (NumberFormatException e) {
            errorMessage.append("Age should be a number.\n");
        }
        try {
            Integer.parseInt(csvPatient.registrationNumber);
        } catch (NumberFormatException e) {
            errorMessage.append("Registration number should be a number.\n");
        }

        if (!isValidGender(csvPatient.gender)) {
            errorMessage.append("Gender is invalid. Valid values are : ").append(getValidGenders()).append("\n");
        }

        if (!isValidHealthCentre(csvPatient.healthCenter)) {
            errorMessage.append("Health Centre is invalid. Valid values are : ").append(getValidHealthCentres()).append("\n");
        }

        if (isEmpty(errorMessage.toString()))
            return new RowResult<>(csvPatient);

        return new RowResult<>(csvPatient, errorMessage.toString());
    }

    private String getValidGenders() {
        StringBuilder allGendersAsString = new StringBuilder();
        List<Gender> allGenders = getAllGenders();
        for (Gender gender : allGenders) {
            allGendersAsString.append(gender.getGenderType()).append(". ");
        }
        return allGendersAsString.toString();

    }

    private String getValidHealthCentres() {
        StringBuilder allHealthCentresAsString = new StringBuilder();
        List<HealthCenter> allHealthCentres = getAllHealthCentres();
        for (HealthCenter healthCentre : allHealthCentres) {
            allHealthCentresAsString.append(healthCentre.getName()).append(". ");
        }
        return allHealthCentresAsString.toString();

    }

    private boolean isValidGender(String patientGender) {
        List<Gender> allGenders = getAllGenders();
        for (Gender aGender : allGenders) {
            if (aGender.matches(patientGender)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidHealthCentre(String healthCenter) {
        List<HealthCenter> allHealthCentres = getAllHealthCentres();
        for (HealthCenter aHealthCentre : allHealthCentres) {
            if (aHealthCentre.matches(healthCenter)) {
                return true;
            }
        }
        return false;
    }

    private boolean areBothEmpty(String aField, String anotherField) {
        return isEmpty(aField) && isEmpty(anotherField);
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    private List<PersonAddress> getPersonAddresses(CSVPatient csvPatient, Person person) {
        List<PersonAddress> personAddressList = new ArrayList<>(getAllAddressParts().size());
        personAddressList.add(PersonAddress.create(person, getAllAddressParts(), "level1", csvPatient.houseStreetName, getSysUserId()));
        personAddressList.add(PersonAddress.create(person, getAllAddressParts(), "level2", csvPatient.cityVillage, getSysUserId()));
        personAddressList.add(PersonAddress.create(person, getAllAddressParts(), "level3", csvPatient.gramPanchayat, getSysUserId()));
        personAddressList.add(PersonAddress.create(person, getAllAddressParts(), "level4", csvPatient.tehsil, getSysUserId()));
        personAddressList.add(PersonAddress.create(person, getAllAddressParts(), "level5", csvPatient.district, getSysUserId()));
        personAddressList.add(PersonAddress.create(person, getAllAddressParts(), "level6", csvPatient.state, getSysUserId()));
        return personAddressList;
    }

    private PatientIdentity getPatientIdentity(Patient patient, String key, String identifier) {
        PatientIdentity patientIdentity = new PatientIdentity();
        patientIdentity.setIdentityTypeId(getPatientIdentityTypes().find(key).getId());
        patientIdentity.setPatientId(patient.getId());
        patientIdentity.setIdentityData(identifier);
        patientIdentity.setSysUserId(getSysUserId());
        return patientIdentity;
    }

    private Patient getPatient(CSVPatient csvPatient, Person person) throws ParseException {
        us.mn.state.health.lims.patient.valueholder.Patient patient = new us.mn.state.health.lims.patient.valueholder.Patient();
        patient.setPerson(person);
        patient.setGender(csvPatient.gender);
        patient.setSysUserId(getSysUserId());
        patient.setUuid(UUID.randomUUID().toString());
        patient.setHealthCenter(healthCenterDAO.getByName(csvPatient.healthCenter));

        if (csvPatient.dob != null && csvPatient.dob.trim().length() > 0) {
            patient.setBirthDate(new Timestamp(DD_MM_YYYY_DATE_FORMAT.parse(csvPatient.dob).getTime()));
        } else {
            Period ageAsPeriod = new Period(Integer.parseInt(csvPatient.age), 0, 0, 0, 0, 0, 0, 0, PeriodType.yearMonthDay());
            LocalDate dateOfBirth = new LocalDate(new Date()).minus(ageAsPeriod);
            patient.setBirthDateForDisplay(DateUtil.convertDateToAmbiguousStringDate(dateOfBirth.toDate()));
        }

        return patient;
    }

    private Person getPerson(CSVPatient csvPatient) {
        Person person = new Person();
        person.setFirstName(csvPatient.firstName);
        person.setLastName(csvPatient.lastName);
        person.setSysUserId(getSysUserId());
        return person;
    }

    private List<HealthCenter> getAllHealthCentres() {
        if (allHealthCenters == null)
            allHealthCenters = healthCenterDAO.getAll();

        return allHealthCenters;
    }

    private List<Gender> getAllGenders() {
        if (allGenders == null)
            allGenders = genderDao.getAllGenders();

        return allGenders;
    }

    private String getSysUserId() {
        if (sysUserId == null) {
            sysUserId = auditingService.getSysUserId();
        }
        return sysUserId;
    }

    private AddressParts getAllAddressParts() {
        if (addressParts == null)
            addressParts = new AddressParts(new AddressPartDAOImpl().getAll());
        return addressParts;
    }

    private PatientIdentityTypes getPatientIdentityTypes() {
        if (patientIdentityTypes == null)
            patientIdentityTypes = new PatientIdentityTypes(patientIdentityTypeDAO.getAllPatientIdenityTypes());

        return patientIdentityTypes;
    }
}

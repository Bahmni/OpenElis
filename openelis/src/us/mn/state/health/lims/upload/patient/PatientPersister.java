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
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.gender.dao.GenderDAO;
import us.mn.state.health.lims.gender.daoimpl.GenderDAOImpl;
import us.mn.state.health.lims.gender.valueholder.Gender;
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
    private GenderDAO genderDao;

    private static String sysUserId;
    private static PatientIdentityTypes patientIdentityTypes;
    private static AddressParts addressParts;

    private static Logger logger = Logger.getLogger(PatientPersister.class);
    private List<Gender> allGenders;
    private String contextPath;

    public PatientPersister(String contextPath) {
        this(new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()), new PersonDAOImpl(),
                new PersonAddressDAOImpl(), new PatientDAOImpl(), new PatientIdentityDAOImpl(),
                new PatientIdentityTypeDAOImpl(), new GenderDAOImpl());

        this.contextPath = contextPath;
    }

    public PatientPersister(AuditingService auditingService, PersonDAO personDAO, PersonAddressDAO personAddressDAO,
                            PatientDAO patientDAO, PatientIdentityDAO patientIdentityDAO,
                            PatientIdentityTypeDAO patientIdentityTypeDAO,
                            GenderDAO genderDao) {
        this.auditingService = auditingService;
        this.personDAO = personDAO;
        this.personAddressDAO = personAddressDAO;
        this.patientDAO = patientDAO;
        this.patientIdentityDAO = patientIdentityDAO;
        this.patientIdentityTypeDAO = patientIdentityTypeDAO;
        this.genderDao = genderDao;
    }

    @Override
    public RowResult<CSVPatient> persist(CSVPatient csvPatient) {
        Transaction transaction = null;
        try {
            logger.debug("Persisting " + csvPatient);
            transaction = HibernateUtil.getSession().beginTransaction();

            Person newPerson = getPerson(csvPatient);
            UUID patientUUID = UUID.randomUUID();
            personDAO.insertData(newPerson);
            personAddressDAO.insert(getPersonAddresses(csvPatient, newPerson));
            Patient newPatient = getPatient(csvPatient, newPerson);
            newPatient.setUuid(patientUUID.toString());
            patientDAO.insertData(newPatient);

            patientIdentityDAO.insertData(getPatientIdentity(newPatient, BahmniPatientService.REGISTRATION_KEY_NAME,
                    csvPatient.registrationNumber));
            patientIdentityDAO.insertData(getPatientIdentity(newPatient, BahmniPatientService.PRIMARY_RELATIVE_KEY_NAME,
                    csvPatient.fatherOrHusbandsName));
            patientIdentityDAO.insertData(getPatientIdentity(newPatient, BahmniPatientService.OCCUPATION_KEY_NAME,
                    csvPatient.occupation));

            if (transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            logger.warn(e);
            if (transaction != null && transaction.isActive()) { 
                transaction.rollback();
            }

            return new RowResult<>(csvPatient, e);
        }
        return new RowResult(csvPatient);
    }

    @Override
    public RowResult<CSVPatient> validate(CSVPatient csvPatient) {
        logger.debug("Validating " + csvPatient);

        String registrationNumberFormat = getStNumberFormat();
        registrationNumberFormat = registrationNumberFormat.substring(1, registrationNumberFormat.length()-1);
        StringBuilder errorMessage = new StringBuilder();

        if (isEmpty(csvPatient.registrationNumber))
            errorMessage.append("Registration Number is mandatory.\n");
        if (isEmpty(csvPatient.firstName))
            errorMessage.append("First Name is mandatory.\n");
        if (isEmpty(csvPatient.lastName))
            errorMessage.append("Last Name is mandatory.\n");
        if (isEmpty(csvPatient.gender))
            errorMessage.append("Gender is mandatory.\n");
        if (isEmpty(csvPatient.cityVillage))
            errorMessage.append("Village is mandatory.\n");
        if (areBothEmpty(csvPatient.age, csvPatient.dob))
            errorMessage.append("Either Age or DOB is mandatory.\n");
        if(csvPatient.registrationNumber == null || !csvPatient.registrationNumber.matches(registrationNumberFormat))
            errorMessage.append("PatientID does not conform to the allowed format.\n");
        try {
            if (!isEmpty(csvPatient.dob)) {
                Date parsedDate = getSimpleDateFormat().parse(csvPatient.dob);
                if (parsedDate.after(new Date())) {
                    errorMessage.append("DOB should be dd-mm-yyyy and should be a valid date.\n");
                }
            }
        } catch (ParseException e) {
            errorMessage.append("DOB should be dd-mm-yyyy and should be a valid date.\n");
        }

        try {
            if (!isEmpty(csvPatient.age))
                Integer.parseInt(csvPatient.age);
        } catch (NumberFormatException e) {
            errorMessage.append("Age should be a number.\n");
        }

        if (!isValidGender(csvPatient.gender)) {
            errorMessage.append("Gender is invalid. Valid values are : ").append(getValidGenders()).append("\n");
        }

        if (isEmpty(errorMessage.toString()))
            return new RowResult<>(csvPatient);

        return new RowResult<>(csvPatient, errorMessage.toString());
    }

    protected String getStNumberFormat() {
        return ConfigurationProperties.getInstance().getPropertyValue(ConfigurationProperties.Property.ST_NUMBER_FORMAT);
    }

    private SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleDateFormat.setLenient(false);
        return simpleDateFormat;
    }

    private String getValidGenders() {
        StringBuilder allGendersAsString = new StringBuilder();
        List<Gender> allGenders = getAllGenders();
        for (Gender gender : allGenders) {
            allGendersAsString.append(gender.getGenderType()).append(". ");
        }
        return allGendersAsString.toString();

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

        if (csvPatient.dob != null && csvPatient.dob.trim().length() > 0) {
            patient.setBirthDate(new Timestamp(getSimpleDateFormat().parse(csvPatient.dob).getTime()));
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
        person.setMiddleName(csvPatient.middleName);
        person.setSysUserId(getSysUserId());
        return person;
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

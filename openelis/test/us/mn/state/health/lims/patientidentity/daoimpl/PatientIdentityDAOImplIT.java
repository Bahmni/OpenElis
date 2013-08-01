package us.mn.state.health.lims.patientidentity.daoimpl;

import org.bahmni.feed.openelis.IT;
import org.junit.Test;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.exception.LIMSValidationException;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;

public class PatientIdentityDAOImplIT extends IT {
    @Test(expected = LIMSDuplicateRecordException.class)
    public void insertData_checks_for_presence_of_duplicate_ST_Number() {
        String patientIdentityTypeId = PatientIdentityTypeMap.getInstance().getIDForType("ST");
        Patient patient_1 = createPatient("name1");
        addPatientIdentity(patient_1, patientIdentityTypeId, "someSTValue");

        Patient patient_2 = createPatient("name2");
        addPatientIdentity(patient_2, patientIdentityTypeId, "someSTValue");
    }

    private PatientIdentity addPatientIdentity(Patient patient, String patientIdentityTypeId, String identityValue) {
        PatientIdentity identity = new PatientIdentity();
        identity.setPatientId(patient.getId());
        identity.setIdentityTypeId(patientIdentityTypeId);
        identity.setSysUserId("1");
        identity.setIdentityData(identityValue);
        identity.setLastupdatedFields();
        new PatientIdentityDAOImpl().insertData(identity);
        return identity;
    }

    private Patient createPatient(String name) {
        PatientDAOImpl patientDAO = new PatientDAOImpl();
        Person person = createPerson(name);
        Patient patient = new Patient();
        patient.setPerson(person);
        patient.setBirthDate(DateUtil.getNowAsTimestamp());
        patient.setSysUserId("1");
        patientDAO.insertData(patient);
        return patient;
    }

    private Person createPerson(String name) {
        Person person = new Person();
        person.setSysUserId("1");
        person.setFirstName(name);
        new PersonDAOImpl().insertData(person);
        return person;
    }
}


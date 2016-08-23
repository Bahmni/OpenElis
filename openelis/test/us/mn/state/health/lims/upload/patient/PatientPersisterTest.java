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

import junit.framework.Assert;
import org.bahmni.csv.RowResult;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.gender.dao.GenderDAO;
import us.mn.state.health.lims.gender.valueholder.Gender;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.person.dao.PersonDAO;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PatientPersisterTest {
    public static final String VALID_GENDER_TYPE = "M";
    public static final String VALID_HEALTH_CENTRE = "GAN";
    private TestablePatientPersister patientPersister;

    @Before
    public void setup() {
        GenderDAO genderDao = mock(GenderDAO.class);
        Gender validGender = new Gender();
        validGender.setGenderType(VALID_GENDER_TYPE);
        when(genderDao.getAllGenders()).thenReturn(Arrays.asList(validGender));

        patientPersister = new TestablePatientPersister(null, null, null, null, null, null, genderDao);
    }

    @Test
    public void age_or_dob_is_mandatory_for_validation() {
        CSVPatient csvPatient = new CSVPatient();
        csvPatient.age = "85";
        csvPatient.cityVillage = "ganiyari";
        csvPatient.district = "ganiyari";
        csvPatient.firstName = "firstName";
        csvPatient.lastName = "lastName";
        csvPatient.registrationNumber = "12345";
        csvPatient.gender = VALID_GENDER_TYPE;

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        Assert.assertEquals(new RowResult(csvPatient), rowResultForValidPatient);
    }

    @Test
    public void accept_valid_dob() {
        checkForValidDate("31-12-2001", true);
    }

    @Test
    public void fail_if_day_is_invalid_for_dob() {
        checkForValidDate("32-12-2001", false);
    }

    @Test
    public void fail_for_future_dob() {
        checkForValidDate("06-09-4100", false); //if this test fails .. run
    }

    @Test
    public void valid_gender_for_validation() {
        CSVPatient csvPatient = new CSVPatient();
        csvPatient.age = "85";
        csvPatient.cityVillage = "ganiyari";
        csvPatient.district = "ganiyari";
        csvPatient.firstName = "firstName";
        csvPatient.registrationNumber = "12345";
        csvPatient.gender = VALID_GENDER_TYPE;

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        Assert.assertFalse("Gender is invalid", rowResultForValidPatient.isSuccessful());
    }

    @Test
    public void valid_dob_for_validation() {
        CSVPatient csvPatient = new CSVPatient();
        csvPatient.dob = "85";
        csvPatient.cityVillage = "ganiyari";
        csvPatient.district = "ganiyari";
        csvPatient.firstName = "firstName";
        csvPatient.registrationNumber = "12345";
        csvPatient.gender = VALID_GENDER_TYPE;

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        Assert.assertFalse("DOB is invalid", rowResultForValidPatient.isSuccessful());

        String[] rowWithErrorColumn = rowResultForValidPatient.getRowWithErrorColumn();
        String errorMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        Assert.assertTrue("DOB should be dd-mm-yyyy", errorMessage.contains("DOB should be dd-mm-yyyy"));
    }

    @Test
    public void valid_registrationNumber_for_validation() {
        CSVPatient csvPatient = new CSVPatient();
        csvPatient.dob = "85";
        csvPatient.cityVillage = "ganiyari";
        csvPatient.district = "ganiyari";
        csvPatient.firstName = "firstName";
        csvPatient.registrationNumber = "abcd";
        csvPatient.gender = VALID_GENDER_TYPE;

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        Assert.assertFalse("Registration Number is invalid", rowResultForValidPatient.isSuccessful());

        String[] rowWithErrorColumn = rowResultForValidPatient.getRowWithErrorColumn();
        String errorMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        Assert.assertTrue(errorMessage.contains("PatientID does not conform to the allowed format."));
    }

    @Test
    public void all_mandatory_fields_for_validation() {
        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(new CSVPatient());

        Assert.assertFalse("Mandatory fields need to be populated", rowResultForValidPatient.isSuccessful());

        String[] rowWithErrorColumn = rowResultForValidPatient.getRowWithErrorColumn();
        String errorMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Registration Number is mandatory."));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("First Name is mandatory"));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Last Name is mandatory"));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Gender is mandatory"));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Village is mandatory"));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Either Age or DOB is mandatory"));
    }
    
    @Test
    public void shouldValidatePatientIdentifierAgainstConfiguration() {
        CSVPatient csvPatient = new CSVPatient();
        csvPatient.dob = "85";
        csvPatient.cityVillage = "ganiyari";
        csvPatient.district = "ganiyari";
        csvPatient.firstName = "firstName";
        csvPatient.registrationNumber = "12345";
        csvPatient.gender = VALID_GENDER_TYPE;
        patientPersister.setStNumberFormat("/([a-zA-Z]*)(\\d+\\/\\d+)/");

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        Assert.assertFalse("Registration Number is invalid", rowResultForValidPatient.isSuccessful());

        String[] rowWithErrorColumn = rowResultForValidPatient.getRowWithErrorColumn();
        String errorMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        Assert.assertTrue(errorMessage.contains("PatientID does not conform to the allowed format."));

    }

    private void checkForValidDate(String dateArg, boolean isValid) {
        CSVPatient csvPatient = new CSVPatient();
        csvPatient.dob = dateArg;
        csvPatient.cityVillage = "ganiyari";
        csvPatient.district = "ganiyari";
        csvPatient.firstName = "firstName";
        csvPatient.lastName = "lastName";
        csvPatient.registrationNumber = "12345";
        csvPatient.gender = VALID_GENDER_TYPE;

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        if (isValid) {
            Assert.assertTrue("Is valid date", rowResultForValidPatient.isSuccessful());
            return;
        }
        Assert.assertFalse("Should be invalid date", rowResultForValidPatient.isSuccessful());

        String[] rowWithErrorColumn = rowResultForValidPatient.getRowWithErrorColumn();
        String errorMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        Assert.assertTrue("DOB should be dd-mm-yyyy", errorMessage.contains("DOB should be dd-mm-yyyy and should be a valid date"));
    }

    public class TestablePatientPersister extends PatientPersister{
        private String stNumberFormat = "/([a-zA-Z]*)(\\d+)/";

        public TestablePatientPersister(AuditingService auditingService, PersonDAO personDAO,PersonAddressDAO personAddressDAO, PatientDAO patientDAO,PatientIdentityDAO patientIdentityDAO, PatientIdentityTypeDAO patientIdentityTypeDAO, GenderDAO genderDao) {
            super(auditingService, personDAO, personAddressDAO, patientDAO, patientIdentityDAO, patientIdentityTypeDAO, genderDao);
        }

        @Override
        protected String getStNumberFormat() {
            return stNumberFormat;
        }

        public void setStNumberFormat(String stNumberFormat) {
            this.stNumberFormat = stNumberFormat;
        }
    }
}

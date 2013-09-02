package us.mn.state.health.lims.upload.patient;

import junit.framework.Assert;
import org.bahmni.csv.RowResult;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.gender.dao.GenderDAO;
import us.mn.state.health.lims.gender.valueholder.Gender;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PatientPersisterTest {
    public static final String VALID_GENDER_TYPE = "M";
    public static final String VALID_HEALTH_CENTRE = "GAN";
    private PatientPersister patientPersister;

    @Before
    public void setup() {
        GenderDAO genderDao = mock(GenderDAO.class);
        Gender validGender = new Gender();
        validGender.setGenderType(VALID_GENDER_TYPE);
        when(genderDao.getAllGenders()).thenReturn(Arrays.asList(validGender));

        HealthCenterDAO healthCenterDAO = mock(HealthCenterDAO.class);
        HealthCenter validHealthCenter = new HealthCenter();
        validHealthCenter.setName(VALID_HEALTH_CENTRE);
        when(healthCenterDAO.getAll()).thenReturn(Arrays.asList(validHealthCenter));

        patientPersister = new PatientPersister(null, null, null, null, null, null, healthCenterDAO, genderDao, null);
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
        csvPatient.healthCenter = VALID_HEALTH_CENTRE;

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        Assert.assertEquals(new RowResult(csvPatient), rowResultForValidPatient);
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
        csvPatient.healthCenter = "InVALID_HEALTH_CENTRE";

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        Assert.assertFalse("Gender is invalid", rowResultForValidPatient.isSuccessful());
    }

    @Test
    public void valid_healthcenter_for_validation() {
        CSVPatient csvPatient = new CSVPatient();
        csvPatient.age = "85";
        csvPatient.cityVillage = "ganiyari";
        csvPatient.district = "ganiyari";
        csvPatient.firstName = "firstName";
        csvPatient.registrationNumber = "12345";
        csvPatient.gender = VALID_GENDER_TYPE;
        csvPatient.healthCenter = "InVALID_HEALTH_CENTRE";

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        Assert.assertFalse("HealthCenter is invalid", rowResultForValidPatient.isSuccessful());

        String[] rowWithErrorColumn = rowResultForValidPatient.getRowWithErrorColumn();
        String errorMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        Assert.assertTrue("Health Center is invalid", errorMessage.contains("Health Centre is invalid. Valid values are"));
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
        csvPatient.healthCenter = VALID_HEALTH_CENTRE;

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
        csvPatient.healthCenter = VALID_HEALTH_CENTRE;

        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(csvPatient);

        Assert.assertFalse("Registration Number is invalid", rowResultForValidPatient.isSuccessful());

        String[] rowWithErrorColumn = rowResultForValidPatient.getRowWithErrorColumn();
        String errorMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        Assert.assertTrue("Registration number should be a number", errorMessage.contains("Registration number should be a number"));
    }

    @Test
    public void all_mandatory_fields_for_validation() {
        RowResult<CSVPatient> rowResultForValidPatient = patientPersister.validate(new CSVPatient());

        Assert.assertFalse("Mandatory fields need to be populated", rowResultForValidPatient.isSuccessful());

        String[] rowWithErrorColumn = rowResultForValidPatient.getRowWithErrorColumn();
        String errorMessage = rowWithErrorColumn[rowWithErrorColumn.length - 1];
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Health Center is mandatory"));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Registration Number is mandatory."));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("First Name is mandatory"));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Gender is mandatory"));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Village is mandatory"));
        Assert.assertTrue("Mandatory fields need to be populated", errorMessage.contains("Either Age or DOB is mandatory"));
    }
}

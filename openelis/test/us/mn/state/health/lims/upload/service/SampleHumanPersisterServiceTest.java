package us.mn.state.health.lims.upload.service;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SampleHumanPersisterServiceTest {
    @Mock
    private PatientIdentityTypeDAO patientIdentityTypeDAO;
    @Mock
    private PatientIdentityDAO patientIdentityDAO;
    @Mock
    private SampleHumanDAO sampleHumanDAO;
    private SampleHumanPersisterService sampleHumanPersisterService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        sampleHumanPersisterService = new SampleHumanPersisterService(patientIdentityTypeDAO, sampleHumanDAO, patientIdentityDAO);
    }

    @org.junit.Test
    public void shouldPersistSampleHumanForPersistingTestResult() throws Exception {
        String sysUserId = "123";
        String sampleId = "12";
        PatientIdentityType stIdentityType = new PatientIdentityType();
        stIdentityType.setId("stIdentityId");
        String patientRegistrationNumber = "patientRegistrationNumber";
        PatientIdentity patientIdentity = new PatientIdentity();
        String patientId = "1";
        patientIdentity.setPatientId(patientId);
        Sample sample = new Sample();
        sample.setId(sampleId);

        when(patientIdentityTypeDAO.getNamedIdentityType(anyString())).thenReturn(stIdentityType);
        when(patientIdentityDAO.getPatientIdentitiesByValueAndType(patientRegistrationNumber, stIdentityType.getId())).thenReturn(Arrays.asList(patientIdentity));

        sampleHumanPersisterService.save(sampleId, patientRegistrationNumber, sysUserId);

        ArgumentCaptor<SampleHuman> captor = ArgumentCaptor.forClass(SampleHuman.class);
        verify(sampleHumanDAO).insertData(captor.capture());
        SampleHuman sampleHuman = captor.getValue();
        assertEquals(sampleId, sampleHuman.getSampleId());
        assertEquals(patientId, sampleHuman.getPatientId());
    }

}

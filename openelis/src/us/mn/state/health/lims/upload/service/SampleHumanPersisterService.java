package us.mn.state.health.lims.upload.service;

import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;

import java.util.List;

public class SampleHumanPersisterService {
    private PatientIdentityTypeDAO patientIdentityTypeDAO;
    private SampleHumanDAO sampleHumanDAO;
    private PatientIdentityDAO patientIdentityDAO;

    public SampleHumanPersisterService() {
        this(new PatientIdentityTypeDAOImpl(), new SampleHumanDAOImpl(), new PatientIdentityDAOImpl());
    }

    public SampleHumanPersisterService(PatientIdentityTypeDAO patientIdentityTypeDAO, SampleHumanDAO sampleHumanDAO, PatientIdentityDAO patientIdentityDAO) {
        this.patientIdentityTypeDAO = patientIdentityTypeDAO;
        this.sampleHumanDAO = sampleHumanDAO;
        this.patientIdentityDAO = patientIdentityDAO;
    }

    public SampleHuman save(String sampleId, String registrationNumber, String sysUserId) {
        String patientId = getPatientId(registrationNumber);
        SampleHuman sampleHuman = new SampleHuman();
        sampleHuman.setPatientId(patientId);
        sampleHuman.setSampleId(sampleId);
        sampleHuman.setSysUserId(sysUserId);
        sampleHumanDAO.insertData(sampleHuman);
        return sampleHuman;
    }

    private String getPatientId(String patientIdentity) {
        PatientIdentityType stIdentityType = patientIdentityTypeDAO.getNamedIdentityType("ST");
        List<PatientIdentity> patientIdentities = patientIdentityDAO.getPatientIdentitiesByValueAndType(patientIdentity, stIdentityType.getId());
        return patientIdentities.get(0).getPatientId();
    }

}

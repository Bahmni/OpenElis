package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.feed.contract.openmrs.*;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;

import java.util.UUID;

@Ignore
public class BahmniPatientServiceIT extends IT {
    @Ignore
    public void testCreate() throws Exception {
        BahmniPatientService bahmniPatientService = new BahmniPatientService(new PatientDAOImpl(), new PersonDAOImpl(), new PatientIdentityDAOImpl(), new PersonAddressDAOImpl(), new AddressPartDAOImpl(), new PatientIdentityTypeDAOImpl());

        OpenMRSPersonAddress address = new OpenMRSPersonAddress("line1", "line2", "line3", "village1", "district1", "state1");
        OpenMRSPerson person = new OpenMRSPerson(new OpenMRSName("firstName1", "lastName1"), UUID.randomUUID().toString(), "F", new LocalDate(2001, 11, 26).toDate(), address);
        OpenMRSPersonAttribute attribute1 = new OpenMRSPersonAttribute("value1", new OpenMRSPersonAttributeType(OpenMRSPersonAttributeType.ATTRIBUTE1_NAME));
        OpenMRSPersonAttribute attribute2 = new OpenMRSPersonAttribute("value2", new OpenMRSPersonAttributeType(OpenMRSPersonAttributeType.ATTRIBUTE2_NAME));
        OpenMRSPatient patient = new OpenMRSPatient(person.addAttribute(attribute1).addAttribute(attribute2));
        patient.addIdentifier(new OpenMRSPatientIdentifier("id1"));

        bahmniPatientService.create(patient);
    }
}
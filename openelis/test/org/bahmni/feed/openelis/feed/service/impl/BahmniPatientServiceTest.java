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

package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.openelis.domain.CompletePatientDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.address.dao.AddressPartDAO;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.AddressParts;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.address.valueholder.PersonAddresses;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.person.valueholder.Person;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BahmniPatientServiceTest {
    @Mock
    private PatientDAO patientDAO;
    @Mock
    private PatientIdentityDAO patientIdentityDAO;
    @Mock
    private PersonAddressDAO personAddressDAO;
    @Mock
    private AddressPartDAO addressDAO;
    @Mock
    private PatientIdentityTypeDAO patientIdentityTypeDAO;

    @Before
    public void before() {
        initMocks(this);
    }


    @Test
    public void shouldGetPatientByUUID() throws Exception {
        Patient patient = new Patient();
        patient.setUuid("uuid");
        patient.setId("321");
        Person person = new Person();
        person.setId("321");
        patient.setPerson(person);
        PatientIdentity patientIdentity = new PatientIdentity();
        patientIdentity.setId("321");
        patientIdentity.setIdentityData("SOMEID");
        patientIdentity.setIdentityTypeId("ID");
        ArrayList<PatientIdentity> patientIdentityList = new ArrayList<>();
        patientIdentityList.add(patientIdentity);
        when(patientDAO.getPatientByUUID(patient.getUuid())).thenReturn(patient);
        when(patientIdentityDAO.getPatientIdentitiesForPatient("321")).thenReturn(patientIdentityList);
        when(personAddressDAO.getAddressPartsByPersonId("321")).thenReturn(new PersonAddresses(new ArrayList<PersonAddress>(), new AddressParts(new ArrayList<AddressPart>())));
        when(addressDAO.getAll()).thenReturn(new ArrayList<AddressPart>());
        when(patientIdentityTypeDAO.get("ID")).thenReturn(new PatientIdentityType());
        BahmniPatientService bahmniPatientService = new BahmniPatientService(patientDAO, null, patientIdentityDAO,
                personAddressDAO, addressDAO, patientIdentityTypeDAO, null);
        CompletePatientDetails result = bahmniPatientService.getPatientByUUID("uuid");
        assertEquals(patientIdentity.getIdentityData(), result.getPatientIdentifier());
        assertEquals(patient.getUuid(), result.getPatientUUID());
    }
}

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

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.feed.contract.openmrs.*;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.joda.time.LocalDate;
import org.junit.Test;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

import java.util.UUID;

public class BahmniPatientServiceIT extends IT {
    @Test
    public void testCreate() throws Exception {
        AuditingService auditingService = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl());
        BahmniPatientService bahmniPatientService = new BahmniPatientService(new PatientDAOImpl(), new PersonDAOImpl(), new PatientIdentityDAOImpl(), new PersonAddressDAOImpl(), new AddressPartDAOImpl(), new PatientIdentityTypeDAOImpl(), auditingService, new HealthCenterDAOImpl());

        OpenMRSPersonAddress address = new OpenMRSPersonAddress("line1", "line2", "line3", "village1", "district1", "state1");
        OpenMRSPerson person = new OpenMRSPerson(new OpenMRSName("random", "lastName1"), UUID.randomUUID().toString(), "F", new LocalDate(2001, 11, 26).toDate(), false, address);
        OpenMRSPersonAttribute attribute1 = new OpenMRSPersonAttribute(createPersonAttributeTypeValue("value1"), new OpenMRSPersonAttributeType(OpenMRSPersonAttributeType.PRIMARY_RELATIVE), "dispaly");
        OpenMRSPersonAttribute attribute2 = new OpenMRSPersonAttribute(createPersonAttributeTypeValue("value2"), new OpenMRSPersonAttributeType(OpenMRSPersonAttributeType.OCCUPATION), "dispaly");
        OpenMRSPersonAttribute attribute3 = new OpenMRSPersonAttribute(createPersonAttributeTypeValue("2"), new OpenMRSPersonAttributeType(OpenMRSPersonAttributeType.HEALTH_CENTER), "dispaly");
        OpenMRSPatient openMRSPatient = new OpenMRSPatient(person.addAttribute(attribute1).addAttribute(attribute2).addAttribute(attribute3));
        openMRSPatient.addIdentifier(new OpenMRSPatientIdentifier("id1"));

        bahmniPatientService.createOrUpdate(openMRSPatient);
        HibernateUtil.getSession().flush();
        //this would result in call to update
        bahmniPatientService.createOrUpdate(openMRSPatient);
        HibernateUtil.getSession().flush();
    }

    private OpenMRSPersonAttributeTypeValue createPersonAttributeTypeValue(String value){
        OpenMRSPersonAttributeTypeValue openMRSPersonAttributeTypeValue = new OpenMRSPersonAttributeTypeValue();
        openMRSPersonAttributeTypeValue.setDisplay(value);
        return openMRSPersonAttributeTypeValue;
    }
}

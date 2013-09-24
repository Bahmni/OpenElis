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

package org.bahmni.feed.openelis.feed.event;

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.bahmni.feed.openelis.feed.mapper.OpenMRSPatientMapper;
import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.bahmni.webclients.WebClient;
import org.hibernate.Session;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

//analogous to a controller becasue it receives the request which is an event in this case
public class PatientFeedWorker extends OpenElisEventWorker {
    private WebClient webClient;
    private String urlPrefix;
    private static Logger logger = Logger.getLogger(PatientFeedWorker.class);

    public PatientFeedWorker(WebClient webClient, String urlPrefix) {
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            String content = event.getContent();
            String patientJSON = webClient.get(URI.create(urlPrefix + content), new HashMap<String, String>(0));
            OpenMRSPatientMapper openMRSPatientMapper = new OpenMRSPatientMapper(ObjectMapperRepository.objectMapper);
            OpenMRSPatient openMRSPatient = openMRSPatientMapper.map(patientJSON);
            logInfo(openMRSPatient);
            BahmniPatientService bahmniPatientService = new BahmniPatientService();
            bahmniPatientService.createOrUpdate(openMRSPatient);
        } catch (IOException e) {
            throw new LIMSRuntimeException(e);
        } finally {
            ElisHibernateSession session = (ElisHibernateSession) HibernateUtil.getSession();
            session.clearSession();
        }
    }

    private void logInfo(OpenMRSPatient openMRSPatient) {
        if (openMRSPatient.getIdentifiers().size() != 0)
            logger.info(String.format("Processing patient with ID=%s", openMRSPatient.getIdentifiers().get(0).getIdentifier()));
    }
}

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
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

//analogous to a controller becasue it receives the request which is an event in this case
public class PatientFeedEventWorker extends OpenElisEventWorker {
    private HttpClient webClient;
    private String urlPrefix;
    private static Logger logger = Logger.getLogger(PatientFeedEventWorker.class);

    public PatientFeedEventWorker(HttpClient webClient, String urlPrefix) {
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            String content = event.getContent();
            String patientJSON = webClient.get(URI.create(urlPrefix + content));
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

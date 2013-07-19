package org.bahmni.feed.openelis.feed.event;

import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;
import org.bahmni.feed.openelis.webclient.WebClient;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

//analogous to a controller becasue it receives the request which is an event in this case
public class PatientFeedWorker extends MyEventWorker {
    private WebClient webClient;
    private String urlPrefix;

    public PatientFeedWorker(WebClient webClient, String urlPrefix) {
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            String content = event.getContent();
            String patientJSON = webClient.get(URI.create(urlPrefix + content), new HashMap<String, String>(0));
            OpenMRSPatient openMRSPatient = objectMapper.readValue(event.getContent(), OpenMRSPatient.class);
            BahmniPatientService bahmniPatientService = new BahmniPatientService(new PatientDAOImpl(), new PersonDAOImpl(), new PatientIdentityDAOImpl(), new PersonAddressDAOImpl());
            bahmniPatientService.create(openMRSPatient);
        } catch (IOException e) {
            throw new LIMSRuntimeException(e);
        }
    }
}
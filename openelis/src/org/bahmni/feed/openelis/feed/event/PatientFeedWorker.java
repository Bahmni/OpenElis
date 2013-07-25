package org.bahmni.feed.openelis.feed.event;

import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.bahmni.feed.openelis.feed.mapper.OpenMRSPatientMapper;
import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.bahmni.feed.openelis.webclient.WebClient;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
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
public class PatientFeedWorker implements EventWorker {
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
            OpenMRSPatientMapper openMRSPatientMapper = new OpenMRSPatientMapper(ObjectMapperRepository.objectMapper);
            OpenMRSPatient openMRSPatient = openMRSPatientMapper.map(patientJSON);
            AuditingService auditingService = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl());
            BahmniPatientService bahmniPatientService = new BahmniPatientService(new PatientDAOImpl(), new PersonDAOImpl(), new PatientIdentityDAOImpl(),
                    new PersonAddressDAOImpl(), new AddressPartDAOImpl(), new PatientIdentityTypeDAOImpl(), auditingService);
            bahmniPatientService.create(openMRSPatient);
        } catch (IOException e) {
            throw new LIMSRuntimeException(e);
        }
    }
}
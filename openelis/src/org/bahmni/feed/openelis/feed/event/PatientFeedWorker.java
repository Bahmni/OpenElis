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
            bahmniPatientService.create(openMRSPatient);
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
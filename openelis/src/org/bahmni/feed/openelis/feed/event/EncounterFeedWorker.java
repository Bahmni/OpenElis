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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import org.bahmni.feed.openelis.feed.mapper.encounter.OpenMRSEncounterMapper;
import org.bahmni.feed.openelis.feed.service.impl.SampleSourceService;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.organization.dao.OrganizationTypeDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationTypeDAOImpl;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.provider.dao.ProviderDAO;
import us.mn.state.health.lims.provider.daoimpl.ProviderDAOImpl;
import us.mn.state.health.lims.requester.dao.RequesterTypeDAO;
import us.mn.state.health.lims.requester.daoimpl.RequesterTypeDAOImpl;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;

import java.net.URI;

//analogous to a controller because it receives the request which is an event in this case
public class EncounterFeedWorker extends OpenElisEventWorker {
    private HttpClient webClient;
    private String urlPrefix;

    private AuditingService auditingService;
    private SampleDAOImpl sampleDAO;
    private ExternalReferenceDao externalReferenceDao;
    private PanelItemDAO panelItemDAO;
    private TypeOfSampleTestDAO typeOfSampleTestDAO;
    private TypeOfSampleDAO typeOfSampleDAO;
    private RequesterTypeDAO requesterTypeDAO;
    private OrganizationTypeDAO organizationTypeDAO;
    private SampleSourceDAO sampleSourceDAO;
    private PatientDAO patientDAO;
    private TestDAO testDAO;
    private AnalysisDAO analysisDAO;
    private SampleItemDAO sampleItemDAO;
    private ProviderDAO providerDAO;
    private SampleSourceService sampleSourceService;

    private static Logger logger = LogManager.getLogger(EncounterFeedWorker.class);

    public static final String ACCESSION_STRATEGY = "accessionStrategy";
    private static final String GROUP_BY_SAMPLE = "groupBySample";

    public EncounterFeedWorker(HttpClient webClient, String urlPrefix, ExternalReferenceDao externalReferenceDao,
                               AuditingService auditingService, PanelItemDAO panelItemDAO,
                               TypeOfSampleTestDAO typeOfSampleTestDAO, TypeOfSampleDAO typeOfSampleDAO,
                               RequesterTypeDAO requesterTypeDAO, OrganizationTypeDAO organizationTypeDAO,
                               SampleSourceDAO sampleSourceDAO, SampleDAOImpl sampleDAO, PatientDAO patientDAO,
                               TestDAO testDAO, AnalysisDAO analysisDAO, SampleItemDAOImpl sampleItemDAO, ProviderDAO providerDAO, SampleSourceService sampleSourceService) {
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
        this.externalReferenceDao = externalReferenceDao;
        this.auditingService = auditingService;
        this.panelItemDAO = panelItemDAO;
        this.typeOfSampleTestDAO = typeOfSampleTestDAO;
        this.typeOfSampleDAO = typeOfSampleDAO;
        this.requesterTypeDAO = requesterTypeDAO;
        this.organizationTypeDAO = organizationTypeDAO;
        this.sampleSourceDAO = sampleSourceDAO;
        this.sampleDAO = sampleDAO;
        this.patientDAO = patientDAO;
        this.testDAO = testDAO;
        this.analysisDAO = analysisDAO;
        this.sampleItemDAO = sampleItemDAO;
        this.providerDAO = providerDAO;
        this.sampleSourceService = sampleSourceService;
    }

    public EncounterFeedWorker(HttpClient authenticatedWebClient, String urlPrefix) {
        this(authenticatedWebClient, urlPrefix, new ExternalReferenceDaoImpl(),
                new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()),
                new PanelItemDAOImpl(), new TypeOfSampleTestDAOImpl(), new TypeOfSampleDAOImpl(),
                new RequesterTypeDAOImpl(), new OrganizationTypeDAOImpl(), new SampleSourceDAOImpl(),
                new SampleDAOImpl(), new PatientDAOImpl(), new TestDAOImpl(), new AnalysisDAOImpl(),
                new SampleItemDAOImpl(), new ProviderDAOImpl(), new SampleSourceService(new SampleSourceDAOImpl(), new ExternalReferenceDaoImpl()));
    }

    @Override
    public void process(Event event) {
        try {
            String content = event.getContent();
            String encounterJSON = webClient.get(URI.create(urlPrefix + content));

            OpenMRSEncounterMapper openMRSEncounterMapper = new OpenMRSEncounterMapper(ObjectMapperRepository.objectMapper);
            OpenMRSEncounter openMRSEncounter = openMRSEncounterMapper.map(encounterJSON);
            if (openMRSEncounter.hasLabOrder()) {
                process(openMRSEncounter);
            }
        } catch (Exception e) {
            throw new LIMSRuntimeException(e);
        } finally {
            ElisHibernateSession session = (ElisHibernateSession) HibernateUtil.getSession();
            session.clearSession();
        }
    }

    public void process(OpenMRSEncounter openMRSEncounter) {
        logInfo(openMRSEncounter);
        String sysUserId = auditingService.getSysUserId();
        EncounterFeedProcessor feedProcessor;
        SiteInformation siteInformation = new SiteInformationDAOImpl().getSiteInformationByName(ACCESSION_STRATEGY);
        String accessionStrategy = siteInformation != null ? siteInformation.getValue() : "";

        if(GROUP_BY_SAMPLE.equals(accessionStrategy)) {
            feedProcessor = new GroupBySampleTypeFeedProcessor(sampleDAO, externalReferenceDao, panelItemDAO,
                    typeOfSampleTestDAO, typeOfSampleDAO, requesterTypeDAO, organizationTypeDAO, patientDAO, testDAO,
                    analysisDAO, sampleItemDAO, providerDAO, sampleSourceService);
        } else {
            feedProcessor = new GroupByEncounterFeedProcessor(sampleDAO, externalReferenceDao, panelItemDAO,
                    typeOfSampleTestDAO, typeOfSampleDAO, requesterTypeDAO, organizationTypeDAO, patientDAO, testDAO,
                    analysisDAO, sampleItemDAO, providerDAO, sampleSourceService);
        }

        feedProcessor.process(openMRSEncounter, sysUserId);
    }

    private void logInfo(OpenMRSEncounter openMRSEncounter) {
        logger.info(String.format("Processing encounter with ID='%s'", openMRSEncounter.getEncounterUuid()));
    }
}

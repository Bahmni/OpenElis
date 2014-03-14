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
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSOrder;
import org.bahmni.feed.openelis.feed.mapper.encounter.OpenMRSEncounterMapper;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.domain.Event;
import org.joda.time.DateTime;
import us.mn.state.health.lims.address.valueholder.OrganizationAddress;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.organization.dao.OrganizationTypeDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationTypeDAOImpl;
import us.mn.state.health.lims.organization.valueholder.OrganizationType;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.requester.dao.RequesterTypeDAO;
import us.mn.state.health.lims.requester.daoimpl.RequesterTypeDAOImpl;
import us.mn.state.health.lims.requester.valueholder.RequesterType;
import us.mn.state.health.lims.sample.bean.SampleTestCollection;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.util.AccessionNumberUtil;
import us.mn.state.health.lims.sample.util.AnalysisBuilder;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;
import us.mn.state.health.lims.upload.action.AddSampleService;

import java.io.IOException;
import java.net.URI;
import java.sql.Date;
import java.util.*;

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

    private static long provider_requester_type_id;
    private static String referring_org_type_id;
    private static Logger logger = Logger.getLogger(EncounterFeedWorker.class);

    public EncounterFeedWorker(HttpClient webClient, String urlPrefix, ExternalReferenceDao externalReferenceDao,
                               AuditingService auditingService, PanelItemDAO panelItemDAO,
                               TypeOfSampleTestDAO typeOfSampleTestDAO, TypeOfSampleDAO typeOfSampleDAO,
                               RequesterTypeDAO requesterTypeDAO, OrganizationTypeDAO organizationTypeDAO,
                               SampleSourceDAO sampleSourceDAO, SampleDAOImpl sampleDAO, PatientDAO patientDAO,
                               TestDAO testDAO, AnalysisDAO analysisDAO, SampleItemDAOImpl sampleItemDAO) {
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
    }

    public EncounterFeedWorker(HttpClient authenticatedWebClient, String urlPrefix) {
        this(authenticatedWebClient, urlPrefix, new ExternalReferenceDaoImpl(),
                new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()),
                new PanelItemDAOImpl(), new TypeOfSampleTestDAOImpl(), new TypeOfSampleDAOImpl(),
                new RequesterTypeDAOImpl(), new OrganizationTypeDAOImpl(), new SampleSourceDAOImpl(),
                new SampleDAOImpl(), new PatientDAOImpl(), new TestDAOImpl(), new AnalysisDAOImpl(), new SampleItemDAOImpl());
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
        } catch (IOException e) {
            throw new LIMSRuntimeException(e);
        } finally {
            ElisHibernateSession session = (ElisHibernateSession) HibernateUtil.getSession();
            session.clearSession();
        }
    }

    public void process(OpenMRSEncounter openMRSEncounter) {
        logInfo(openMRSEncounter);
        FeedProcessState processState = new FeedProcessState();
        Sample sample = sampleDAO.getSampleByUUID(openMRSEncounter.getUuid());
        if (sample != null) {
            updateSample(openMRSEncounter, sample, processState);
        } else {
            createSample(openMRSEncounter, processState);
        }
    }


    private void createSample(OpenMRSEncounter openMRSEncounter, FeedProcessState processState) {
        String sysUserId = auditingService.getSysUserId();
        Date nowAsSqlDate = DateUtil.getNowAsSqlDate();

        Patient patient = getPatient(openMRSEncounter.getPatient());
        Sample sample = getSample(sysUserId, nowAsSqlDate, openMRSEncounter.getUuid());
        SampleHuman sampleHuman = getSampleHuman(sysUserId);
        List<SampleTestCollection> sampleTestCollections = getSampleTestCollections(openMRSEncounter, sysUserId, nowAsSqlDate, sample, processState);

        AnalysisBuilder analysisBuilder = getAnalysisBuilder(processState);

        AddSampleService addSampleService = new AddSampleService(false);
        addSampleService.persist(analysisBuilder, false, null, null,
                new ArrayList<OrganizationAddress>(), sample,
                sampleTestCollections, new ArrayList<ObservationHistory>(), sampleHuman,
                patient.getId(), null, null, sysUserId,
                getProviderRequesterTypeId(), getReferringOrgTypeId());
    }

    private void updateSample(OpenMRSEncounter openMRSEncounter, Sample sample, FeedProcessState processState) {
        String sysUserId = auditingService.getSysUserId();
        Date nowAsSqlDate = DateUtil.getNowAsSqlDate();

        List<SampleTestCollection> sampleTestCollections = getSampleTestCollections(openMRSEncounter, sysUserId, nowAsSqlDate, sample, processState);
        List<Analysis> existingAnalyses = analysisDAO.getAnalysesBySampleIdExcludedByStatusId(sample.getId(), getCancelledAnalysisStatusIds());
        TestOrderDiff testOrderDiff = new TestOrderDiff(sampleTestCollections, existingAnalyses);

        AnalysisBuilder analysisBuilder = getAnalysisBuilder(processState);

        addTestsToExistingSample(sample, sysUserId, nowAsSqlDate, analysisBuilder, testOrderDiff);
        cancelAnalysisForDeletedTests(sample, sysUserId, testOrderDiff);
        setCorrectPanelIdForUnchangedTests(sample, sysUserId, analysisBuilder, testOrderDiff);
    }

    private void setCorrectPanelIdForUnchangedTests(Sample sample, String sysUserId, AnalysisBuilder analysisBuilder, TestOrderDiff testOrderDiff) {
        List<Analysis> analysesIntersection = analysisDAO.getAnalysesBySampleIdExcludedByStatusId(sample.getId(), getCancelledAnalysisStatusIds());
        for (Analysis analysis : analysesIntersection) {
            if (testOrderDiff.getTestsIntersection().contains(analysis.getTest())) {
                analysis.setPanel(analysisBuilder.getPanelForTest(analysis.getTest()));
                analysis.setSysUserId(sysUserId);
                analysisDAO.updateData(analysis);
            }
        }
    }

    private void cancelAnalysisForDeletedTests(Sample sample, String sysUserId, TestOrderDiff testOrderDiff) {
        List<Integer> toBeDeletedTestIds = new ArrayList<>();
        for (Test test : testOrderDiff.getTestsDeleted()) {
            toBeDeletedTestIds.add(Integer.parseInt(test.getId()));
        }
        List<Analysis> analysisToBeCanceled = analysisDAO.getAnalysisBySampleAndTestIds(sample.getId(), toBeDeletedTestIds);
        for (Analysis analysis : analysisToBeCanceled) {
            analysis.setSysUserId(sysUserId);
            analysis.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled));
            analysisDAO.updateData(analysis);
        }
    }

    private void addTestsToExistingSample(Sample sample, String sysUserId, Date nowAsSqlDate, AnalysisBuilder analysisBuilder, TestOrderDiff testOrderDiff) {
        List<SampleItem> existingSampleItems = sampleItemDAO.getSampleItemsBySampleId(sample.getId());
        int sortOrder = existingSampleItems.size();
        String analysisRevision = SystemConfiguration.getInstance().getAnalysisDefaultRevision();

        for (Test test : testOrderDiff.getTestsAdded()) {
            SampleItem sampleItem = findOrCreateSampleItem(existingSampleItems, test, sample, sysUserId, sortOrder);
            Analysis analysis = analysisBuilder.populateAnalysis(analysisRevision, sampleItem, test, sysUserId, nowAsSqlDate);
            analysisDAO.insertData(analysis, false); // false--do not check
        }
    }

    private SampleItem findOrCreateSampleItem(List<SampleItem> existingSampleItems, Test test, Sample sample, String sysUserId, int sortOrder) {
        TypeOfSample typeOfSample = TypeOfSampleUtil.getTypeOfSampleForTest(test.getId());
        for (SampleItem item : existingSampleItems) {
            if (item.getTypeOfSample().getId().equals(typeOfSample.getId())) {
                return item;
            }
        }
        SampleItem sampleItem = buildSampleItem(sysUserId, sample, ++sortOrder, typeOfSample);
        sampleItemDAO.insertData(sampleItem);
        existingSampleItems.add(sampleItem);
        return sampleItem;
    }

    private SampleHuman getSampleHuman(String sysUserId) {
        SampleHuman sampleHuman = new SampleHuman();
        sampleHuman.setSysUserId(sysUserId);
        return sampleHuman;
    }

    private Patient getPatient(OpenMRSPatient openMRSPatient) {
        String patientUUID = openMRSPatient.getUuid();
        Patient patient = patientDAO.getPatientByUUID(patientUUID);
        if (patient == null) {
            throw new RuntimeException(String.format("Patient with uuid '%s' not found in ELIS", patientUUID));
        }
        return patient;
    }

    private List<SampleTestCollection> getSampleTestCollections(OpenMRSEncounter openMRSEncounter, String sysUserId, Date nowAsSqlDate, Sample sample, FeedProcessState processState) {
        List<SampleTestCollection> sampleTestCollections = new ArrayList<>();
        SampleItemTestCache sampleItemTestCache = new SampleItemTestCache(typeOfSampleTestDAO, typeOfSampleDAO, sample, sysUserId);
        for (OpenMRSOrder labOrder : openMRSEncounter.getLabOrders()) {
            sampleItemTestCache.add(getTests(labOrder, processState));
        }

        for (SampleItem sampleItem : sampleItemTestCache.getSampleItems()) {
            SampleTestCollection sampleTestCollection = new SampleTestCollection(sampleItem, sampleItemTestCache.getTests(sampleItem), nowAsSqlDate);
            sampleTestCollections.add(sampleTestCollection);
        }
        return sampleTestCollections;
    }

    private static SampleItem buildSampleItem(String sysUserId, Sample sample, int sampleItemIdIndex, TypeOfSample typeOfSample) {

        // TODO : Mujir - should this be empty??
        String collector = "";

        SampleItem item = new SampleItem();
        item.setSysUserId(sysUserId);
        item.setSample(sample);
        item.setTypeOfSample(typeOfSample);
        item.setSortOrder(Integer.toString(sampleItemIdIndex));
        item.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
        item.setCollector(collector);
        return item;
    }

    private List<Test> getTests(OpenMRSOrder labOrder, FeedProcessState processState) {
        String externalReferenceTestOrPanelUUID = labOrder.getTestOrPanelUUID();
        if (labOrder.isLabOrderForPanel()) {
            return getTestsForPanel(labOrder.getLabTestName(), externalReferenceTestOrPanelUUID, processState);
        }

        return getTest(labOrder.getLabTestName(), externalReferenceTestOrPanelUUID);
    }

    private List<Test> getTest(String labTestName, String externalReferenceTestOrPanelUUID) {
        String productTypeTest = "Test";
        ExternalReference data = externalReferenceDao.getData(externalReferenceTestOrPanelUUID, productTypeTest);
        if (data == null) {
            throw new RuntimeException(
                    String.format("Test '%s' was not setup properly. No external reference for Test with uuid '%s' found in external_reference table ",
                            labTestName, externalReferenceTestOrPanelUUID));
        }
        long testId = data.getItemId();
        Test test = testDAO.getTestById(String.valueOf(testId));

        List<Test> tests = new ArrayList<>();
        tests.add(test);
        return tests;
    }

    private List<Test> getTestsForPanel(String panelName, String externalReferencePanelUUID, FeedProcessState processState) {
        List<Test> tests = new ArrayList<>();
        String productTypePanel = "Panel";
        ExternalReference data = externalReferenceDao.getData(externalReferencePanelUUID, productTypePanel);
        if (data == null) {
            throw new RuntimeException(
                    String.format("Panel '%s' was not setup properly. No external reference for Panel with uuid '%s' found in external_reference table ",
                            panelName, externalReferencePanelUUID));
        }

        long panelId = data.getItemId();
        List panelItemsForPanel = panelItemDAO.getPanelItemsForPanel(String.valueOf(panelId));
        for (Object obj : panelItemsForPanel) {
            PanelItem panelItem = (PanelItem) obj;
            processState.appendToPanelIds(panelItem.getPanel().getId());
            tests.add(panelItem.getTest());
        }

        return tests;
    }

    private Sample getSample(String sysUserId, Date nowAsSqlDate, String openMRSEncounterUuid) {
        String accessionNumber = AccessionNumberUtil.getNextAccessionNumber("");

        Sample sample = new Sample();
        sample.setSysUserId(sysUserId);
        sample.setAccessionNumber(accessionNumber);

        // TODO : Mujir - remove this hardcoding??? Read this from the event???
        sample.setSampleSource(sampleSourceDAO.getByName("OPD"));

        // TODO : Mujir - remove this hardcoding???? Read this from the event????
        // TODO: Aarthy - Send encounter Date Time as part of event
        sample.setEnteredDate(new java.util.Date());
        Date receivedDate =new Date(new DateTime(nowAsSqlDate).withTime(0,0,0,0).getMillis());
        sample.setReceivedDate(receivedDate);

//        if (useReceiveDateForCollectionDate) {
//            sample.setCollectionDateForDisplay(collectionDateFromRecieveDate);
//        }

        sample.setDomain(SystemConfiguration.getInstance().getHumanDomain());
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Entered));

        // Mujir - create an external reference to order id in openelis.. this can go in Sample against the accession number
        sample.setUUID(openMRSEncounterUuid);

        return sample;
    }

    private AnalysisBuilder getAnalysisBuilder(FeedProcessState processState) {
        AnalysisBuilder analysisBuilder = new AnalysisBuilder();
        analysisBuilder.augmentPanelIdToPanelMap(processState.getPanelIds());
        return analysisBuilder;
    }

    private void logInfo(OpenMRSEncounter openMRSEncounter) {
        logger.info(String.format("Processing encounter with ID='%s'", openMRSEncounter.getUuid()));
    }

    private HashSet<Integer> getCancelledAnalysisStatusIds() {
        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        return new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));
    }

    private long getProviderRequesterTypeId() {
        if(provider_requester_type_id > 0) {
            return provider_requester_type_id;
        }

        RequesterType providerRequesterType = requesterTypeDAO.getRequesterTypeByName("provider");
        if (providerRequesterType != null) {
            provider_requester_type_id = Long.parseLong(providerRequesterType.getId());
        }
        return provider_requester_type_id;
    }

    private String getReferringOrgTypeId() {
        if(referring_org_type_id != null) {
            return referring_org_type_id;
        }

        OrganizationType orgType = organizationTypeDAO.getOrganizationTypeByName("referring clinic");
        if (orgType != null) {
            referring_org_type_id = orgType.getId();
        }
        return referring_org_type_id;
    }


    private static class SampleItemTestCache {
        private TypeOfSampleTestDAO typeOfSampleTestDAO;
        private TypeOfSampleDAO typeOfSampleDAO;
        private final Sample sample;
        private final String sysUserId;
        private Map<String, SampleItem> typeOfSampleSampleItemMap = new HashMap<>();
        private Map<SampleItem, Set<Test>> sampleItemTestMap = new HashMap<>();
        private int sampleItemIdIndex = 0;

        public SampleItemTestCache(TypeOfSampleTestDAO typeOfSampleTestDAO, TypeOfSampleDAO typeOfSampleDAO, Sample sample, String sysUserId) {
            this.typeOfSampleTestDAO = typeOfSampleTestDAO;
            this.typeOfSampleDAO = typeOfSampleDAO;
            this.sample = sample;
            this.sysUserId = sysUserId;
        }

        public void add(List<Test> tests) {
            SampleItem sampleItem = findOrCreateSampleItemFromCache(tests.get(0));
            Set<Test> existingTests = sampleItemTestMap.get(sampleItem);
            if(existingTests == null) {
                sampleItemTestMap.put(sampleItem, new HashSet<Test>(tests));
            } else {
                existingTests.addAll(tests);
            }
        }

        public List<Test> getTests(SampleItem sampleItem) {
            return new ArrayList<>(sampleItemTestMap.get(sampleItem));
        }

        public Set<SampleItem> getSampleItems() {
            return sampleItemTestMap.keySet();
        }

        private SampleItem findOrCreateSampleItemFromCache(Test test) {
            TypeOfSampleTest typeOfSampleTestForTest = typeOfSampleTestDAO.getTypeOfSampleTestForTest(test.getId());
            SampleItem sampleItem = typeOfSampleSampleItemMap.get(typeOfSampleTestForTest.getTypeOfSampleId());
            if (sampleItem == null) {
                TypeOfSample typeOfSample = typeOfSampleDAO.getTypeOfSampleById(typeOfSampleTestForTest.getTypeOfSampleId());
                sampleItem = buildSampleItem(sysUserId, sample, ++sampleItemIdIndex, typeOfSample);
                typeOfSampleSampleItemMap.put(typeOfSample.getId(), sampleItem);
            }

            return sampleItem;
        }
    }

    private static class FeedProcessState {
        private StringBuilder panelIdsBuilder = new StringBuilder();

        private String getPanelIds() {
            return panelIdsBuilder.toString();
        }

        private void appendToPanelIds(String panelId) {
            this.panelIdsBuilder.append(panelId).append(",");
        }
    }

    private static class TestOrderDiff {
        private List<SampleTestCollection> sampleTestCollections;
        private List<Analysis> existingAnalyses;

        public TestOrderDiff(List<SampleTestCollection> sampleTestCollections, List<Analysis> existingAnalyses) {
            this.sampleTestCollections = sampleTestCollections;
            this.existingAnalyses = existingAnalyses;
        }

        private Set<Test> getTestsAdded() {
            Set<Test> newTests = getNewlyOrderedTests();
            newTests.removeAll(getPreviouslyOrderedTest());
            return newTests;
        }

        private Set<Test> getTestsDeleted() {
            Set<Test> existingTests = getPreviouslyOrderedTest();
            existingTests.removeAll(getNewlyOrderedTests());
            return existingTests;
        }

        private Set<Test> getTestsIntersection() {
            Set<Test> newTests = getNewlyOrderedTests();
            Set<Test> existingTests = getPreviouslyOrderedTest();
            newTests.retainAll(existingTests);
            return newTests;
        }

        private Set<Test> getPreviouslyOrderedTest() {
            Set<Test> existingTests = new HashSet<>();
            for (Analysis analysis : existingAnalyses) {
                existingTests.add(analysis.getTest());
            }
            return existingTests;
        }

        private Set<Test> getNewlyOrderedTests() {
            Set<Test> newTests = new HashSet<>();
            for (SampleTestCollection sampleTestCollection : sampleTestCollections) {
                newTests.addAll(sampleTestCollection.tests);
            }
            return newTests;
        }
    }
}

package org.bahmni.feed.openelis.feed.event;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.SampleTestOrderCollection;
import org.bahmni.feed.openelis.feed.contract.TestOrder;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSOrder;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSProvider;
import us.mn.state.health.lims.address.valueholder.OrganizationAddress;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.organization.dao.OrganizationTypeDAO;
import us.mn.state.health.lims.organization.valueholder.OrganizationType;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.provider.dao.ProviderDAO;
import us.mn.state.health.lims.provider.valueholder.Provider;
import us.mn.state.health.lims.requester.dao.RequesterTypeDAO;
import us.mn.state.health.lims.requester.valueholder.RequesterType;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.util.AnalysisBuilder;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;
import us.mn.state.health.lims.upload.action.AddSampleService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class EncounterFeedProcessor {

    private SampleDAO sampleDAO;
    private ExternalReferenceDao externalReferenceDao;
    private PanelItemDAO panelItemDAO;
    private TypeOfSampleTestDAO typeOfSampleTestDAO;
    private TypeOfSampleDAO typeOfSampleDAO;
    private RequesterTypeDAO requesterTypeDAO;
    private OrganizationTypeDAO organizationTypeDAO;
    private PatientDAO patientDAO;
    private TestDAO testDAO;
    private AnalysisDAO analysisDAO;
    private SampleItemDAO sampleItemDAO;
    private ProviderDAO providerDAO;

    private static long provider_requester_type_id;
    private static String referring_org_type_id;

    public abstract void process(OpenMRSEncounter openMRSEncounter, String sysUserId);

    protected void setRequiredObjects(SampleDAO sampleDAO, ExternalReferenceDao externalReferenceDao,
                                      PanelItemDAO panelItemDAO, TypeOfSampleTestDAO typeOfSampleTestDAO,
                                      TypeOfSampleDAO typeOfSampleDAO, RequesterTypeDAO requesterTypeDAO,
                                      OrganizationTypeDAO organizationTypeDAO, PatientDAO patientDAO, TestDAO testDAO,
                                      AnalysisDAO analysisDAO, SampleItemDAO sampleItemDAO, ProviderDAO providerDAO) {

        this.sampleDAO = sampleDAO;
        this.externalReferenceDao = externalReferenceDao;
        this.panelItemDAO = panelItemDAO;
        this.typeOfSampleTestDAO = typeOfSampleTestDAO;
        this.typeOfSampleDAO = typeOfSampleDAO;
        this.requesterTypeDAO = requesterTypeDAO;
        this.organizationTypeDAO = organizationTypeDAO;
        this.patientDAO = patientDAO;
        this.testDAO = testDAO;
        this.analysisDAO = analysisDAO;
        this.sampleItemDAO = sampleItemDAO;
        this.providerDAO = providerDAO;
    }

    protected void updateSample(Sample sample, FeedProcessState processState, String sysUserId, List<OpenMRSOrder> orders) {
        Date nowAsSqlDate = DateUtil.getNowAsSqlDate();

        List<SampleTestOrderCollection> sampleTestOrderCollectionList = getSampleTestCollections(orders, sysUserId, nowAsSqlDate, sample, processState);
        List<Analysis> existingAnalyses = analysisDAO.getAnalysesBySampleIdExcludedByStatusId(sample.getId(), getCancelledAnalysisStatusIds());
        TestOrderDiff testOrderDiff = new TestOrderDiff(sampleTestOrderCollectionList, existingAnalyses);

        AnalysisBuilder analysisBuilder = getAnalysisBuilder(processState);

        addTestsToExistingSample(sample, sysUserId, nowAsSqlDate, analysisBuilder, testOrderDiff);
        cancelAnalysisForDeletedTests(sample, sysUserId, testOrderDiff);
        updateAnalysisForNotDeletedTests(sample, sysUserId, testOrderDiff);
        if (sample != null) {
            setCorrectPanelIdForUnchangedTests(sample, sysUserId, analysisBuilder, testOrderDiff);
        }
    }

    protected void createSample(OpenMRSEncounter openMRSEncounter, FeedProcessState processState, String sysUserId, List<OpenMRSOrder> orders) {
        if (!openMRSEncounter.hasLabOrder() || isEmpty(openMRSEncounter.getLabOrders())) {
            return;
        }

        Date nowAsSqlDate = DateUtil.getNowAsSqlDate();

        Patient patient = getPatient(openMRSEncounter.getPatientUuid());
        Sample sample = getSample(sysUserId, nowAsSqlDate, openMRSEncounter, orders);
        SampleHuman sampleHuman = getSampleHuman(sysUserId);
        List<SampleTestOrderCollection> sampleTestOrderCollectionList = getSampleTestCollections(orders, sysUserId, nowAsSqlDate, sample, processState);

        AnalysisBuilder analysisBuilder = getAnalysisBuilder(processState);
        OpenMRSProvider openMRSProvider = openMRSEncounter.getProviders().get(0);
        Provider providerByPersonName = providerDAO.getProviderByPersonName(openMRSProvider.getName());
        String requesterId = null;
        if (providerByPersonName != null) {
            requesterId = providerByPersonName.getId();
        }

        AddSampleService addSampleService = new AddSampleService(false);
        addSampleService.persist(analysisBuilder, false, null, null,
                new ArrayList<OrganizationAddress>(), sample,
                sampleTestOrderCollectionList, new ArrayList<ObservationHistory>(), sampleHuman,
                patient.getId(), null, requesterId, sysUserId,
                getProviderRequesterTypeId(), getReferringOrgTypeId());
    }

    protected abstract Sample getSample(String sysUserId, Date nowAsSqlDate, OpenMRSEncounter openMRSEncounter, List<OpenMRSOrder> orders);

    private List<SampleTestOrderCollection> getSampleTestCollections(List<OpenMRSOrder> orders, String sysUserId, Date nowAsSqlDate, Sample sample, FeedProcessState processState) {
        List<SampleTestOrderCollection> sampleTestOrderCollectionList = new ArrayList<>();
        SampleItemTestCache sampleItemTestCache = new SampleItemTestCache(typeOfSampleTestDAO, typeOfSampleDAO, sample, sysUserId);
        for (OpenMRSOrder labOrder : orders) {
            sampleItemTestCache.add(getTests(labOrder, processState));
        }

        for (SampleItem sampleItem : sampleItemTestCache.getSampleItems()) {
            SampleTestOrderCollection sampleTestCollection = new SampleTestOrderCollection(sampleItem, sampleItemTestCache.getTests(sampleItem), nowAsSqlDate);
            sampleTestOrderCollectionList.add(sampleTestCollection);
        }
        return sampleTestOrderCollectionList;
    }


    protected void filterNewTestsAdded(OpenMRSEncounter openMRSEncounter, String sysUserId) {
        //Pick only those samples which are collected.
        List<Sample> currentEncounterSamples = sampleDAO.getAllSamplesByUuidAndWithAccessionNumber(openMRSEncounter.getEncounterUuid());

        if(currentEncounterSamples.size() == 0)
            return;

        List<Analysis> currentEncounterAnalyses = new ArrayList<>();
        for (Sample sample : currentEncounterSamples) {
            currentEncounterAnalyses.addAll(analysisDAO.getAnalysesBySampleId(sample.getId()));
        }
        updateCommentsAndRemoveTestOrders(currentEncounterAnalyses, openMRSEncounter, sysUserId);
    }

    private void updateCommentsAndRemoveTestOrders(List<Analysis> existingAnalysis, OpenMRSEncounter openMRSEncounter, String sysUserId) {
        Set<OpenMRSOrder> ordersToRemove = new HashSet<OpenMRSOrder>();
        for (Analysis analysis : existingAnalysis) {
            boolean isPresent = false;
            OpenMRSOrder currentOrder = null;
            for (OpenMRSOrder order : openMRSEncounter.getLabOrders()) {
                currentOrder = order;
                if(order.getConcept().isSet() && analysis.getPanel()!=null && order.getLabTestName().equals(analysis.getPanel().getPanelName())){
                    analysis.setComment(order.getCommentToFulfiller());
                    isPresent = true;
                    break;
                }
                else if (order.getLabTestName().equals(analysis.getTest().getTestName())) {
                    analysis.setComment(order.getCommentToFulfiller());
                    isPresent = true;
                    break;
                }
            }
            if(isPresent){
                analysis.setSysUserId(sysUserId);
                analysisDAO.updateData(analysis);
                ordersToRemove.add(currentOrder);
            }
        }
        openMRSEncounter.getOrders().removeAll(ordersToRemove);
    }

    protected boolean isEmpty(List<OpenMRSOrder> labOrders) {
        return labOrders==null || labOrders.size()==0 ;
    }

    private void updateAnalysisForNotDeletedTests(Sample sample, String sysUserId, TestOrderDiff testOrderDiff) {
        //Update comments for intersection
        Map<Integer, TestOrder> commentsMapOfTestIntersection = new HashMap<>();

        for (TestOrder testOrder : testOrderDiff.getTestsIntersection()) {
            commentsMapOfTestIntersection.put(Integer.parseInt(testOrder.getTest().getId()), testOrder);
        }

        List<Analysis> analysisToBeUpdated = analysisDAO.getAnalysisBySampleAndTestIds(sample.getId(), new ArrayList<Integer>(commentsMapOfTestIntersection.keySet()));

        for (Analysis analysis : analysisToBeUpdated) {
            analysis.setSysUserId(sysUserId);
            analysis.setComment(commentsMapOfTestIntersection.get(Integer.valueOf(analysis.getTest().getId())).getComment());
            analysisDAO.updateData(analysis);
        }
    }

    private void setCorrectPanelIdForUnchangedTests(Sample sample, String sysUserId, AnalysisBuilder analysisBuilder, TestOrderDiff testOrderDiff) {
        List<Analysis> analysesIntersection = analysisDAO.getAnalysesBySampleIdExcludedByStatusId(sample.getId(), getCancelledAnalysisStatusIds());
        for (Analysis analysis : analysesIntersection) {
            if (testOrderDiff.getTestsIntersection().contains(new TestOrder(analysis.getTest(),analysis.getComment()))) {
                analysis.setPanel(analysisBuilder.getPanelForTest(analysis.getTest()));
                analysis.setSysUserId(sysUserId);
                analysisDAO.updateData(analysis);
            }
        }
    }

    private void cancelAnalysisForDeletedTests(Sample sample, String sysUserId, TestOrderDiff testOrderDiff) {
        //Perform Cancels
        Map<Integer,TestOrder> commentsMapOfTestToBeDeleted = new HashMap<>();

        for (TestOrder testOrder : testOrderDiff.getTestsDeleted()) {
            commentsMapOfTestToBeDeleted.put(Integer.parseInt(testOrder.getTest().getId()), testOrder);
        }
        List<Analysis> analysisToBeCanceled = analysisDAO.getAnalysisBySampleAndTestIds(sample.getId(), new ArrayList<Integer>(commentsMapOfTestToBeDeleted.keySet()));

        for (Analysis analysis : analysisToBeCanceled) {
            analysis.setSysUserId(sysUserId);
            analysis.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled));
            analysis.setComment(commentsMapOfTestToBeDeleted.get(Integer.valueOf(analysis.getTest().getId())).getComment());
            analysisDAO.updateData(analysis);
        }

        cleanUpDanglingItems(sample,sysUserId);
    }

    private void cleanUpDanglingItems(Sample sample,String sysUserId) {
        Set excludedAnalysisStatusList = new HashSet<Integer>();
        excludedAnalysisStatusList.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled)));

        List<SampleItem> sampleItems = sampleItemDAO.getSampleItemsBySampleId(sample.getId());

        for(SampleItem item: sampleItems){
            List<Analysis> analysisList = analysisDAO.getAnalysesBySampleItemsExcludingByStatusIds(item, excludedAnalysisStatusList);
            if(analysisList.size()==0){
                item.setSysUserId(sysUserId);
                item.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Canceled));
                sampleItemDAO.updateData(item);
            }
        }
    }

    private void addTestsToExistingSample(Sample sample, String sysUserId, Date nowAsSqlDate, AnalysisBuilder analysisBuilder, TestOrderDiff testOrderDiff) {
        Set includedSampleStatusList = new HashSet<Integer>();
        includedSampleStatusList.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered)));

        List<SampleItem> existingSampleItems = sampleItemDAO.getSampleItemsBySampleIdAndStatus(sample.getId(),includedSampleStatusList);
        int sortOrder = existingSampleItems.size();
        String analysisRevision = SystemConfiguration.getInstance().getAnalysisDefaultRevision();

        for (TestOrder test : testOrderDiff.getTestsAdded()) {
            SampleItem sampleItem = findOrCreateSampleItem(existingSampleItems, test, sample, sysUserId, sortOrder);
            Analysis analysis = analysisBuilder.populateAnalysis(analysisRevision, sampleItem, test, sysUserId, nowAsSqlDate);
            analysisDAO.insertData(analysis, false); // false--do not check
        }
    }

    private SampleItem findOrCreateSampleItem(List<SampleItem> existingSampleItems, TestOrder testOrder, Sample sample, String sysUserId, int sortOrder) {
        TypeOfSample typeOfSample = TypeOfSampleUtil.getTypeOfSampleForTest(testOrder.getTest().getId());
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

    protected Patient getPatient(String patientUUID) {
        Patient patient = patientDAO.getPatientByUUID(patientUUID);
        if (patient == null) {
            throw new RuntimeException(String.format("Patient with uuid '%s' not found in ELIS", patientUUID));
        }
        return patient;
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

    private List<TestOrder> getTests(OpenMRSOrder labOrder, FeedProcessState processState) {
        String externalReferenceTestOrPanelUUID = labOrder.getTestOrPanelUUID();
        if (labOrder.isLabOrderForPanel()) {
            return getTestsForPanel(labOrder, externalReferenceTestOrPanelUUID, processState);
        }

        return getTest(labOrder, externalReferenceTestOrPanelUUID);
    }

    private List<TestOrder> getTest(OpenMRSOrder labOrder, String externalReferenceTestOrPanelUUID) {
        String productTypeTest = "Test";
        ExternalReference data = externalReferenceDao.getData(externalReferenceTestOrPanelUUID, productTypeTest);
        if (data == null) {
            throw new RuntimeException(
                    String.format("Test '%s' was not setup properly. No external reference for Test with uuid '%s' found in external_reference table ",
                            labOrder.getLabTestName(), externalReferenceTestOrPanelUUID));
        }
        long testId = data.getItemId();
        Test test = testDAO.getTestById(String.valueOf(testId));

        List<TestOrder> tests = new ArrayList<>();
        tests.add(new TestOrder(test,labOrder.getCommentToFulfiller()));
        return tests;
    }

    private List<TestOrder> getTestsForPanel(OpenMRSOrder labOrder, String externalReferencePanelUUID, FeedProcessState processState) {
        List<TestOrder> testOrders = new ArrayList<>();
        String productTypePanel = "Panel";
        ExternalReference data = externalReferenceDao.getData(externalReferencePanelUUID, productTypePanel);
        if (data == null) {
            throw new RuntimeException(
                    String.format("Panel '%s' was not setup properly. No external reference for Panel with uuid '%s' found in external_reference table ",
                            labOrder.getLabTestName(), externalReferencePanelUUID));
        }

        long panelId = data.getItemId();
        List panelItemsForPanel = panelItemDAO.getPanelItemsForPanel(String.valueOf(panelId));
        for (Object obj : panelItemsForPanel) {
            PanelItem panelItem = (PanelItem) obj;
            processState.appendToPanelIds(panelItem.getPanel().getId());
            testOrders.add(new TestOrder(panelItem.getTest(), labOrder.getCommentToFulfiller()));
        }

        return testOrders;
    }

    private AnalysisBuilder getAnalysisBuilder(FeedProcessState processState) {
        AnalysisBuilder analysisBuilder = new AnalysisBuilder();
        analysisBuilder.augmentPanelIdToPanelMap(processState.getPanelIds());
        return analysisBuilder;
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

    protected class FeedProcessState {
        private StringBuilder panelIdsBuilder = new StringBuilder();

        private String getPanelIds() {
            return panelIdsBuilder.toString();
        }

        private void appendToPanelIds(String panelId) {
            this.panelIdsBuilder.append(panelId).append(",");
        }
    }

    private static class SampleItemTestCache {
        private TypeOfSampleTestDAO typeOfSampleTestDAO;
        private TypeOfSampleDAO typeOfSampleDAO;
        private final Sample sample;
        private final String sysUserId;
        private Map<String, SampleItem> typeOfSampleSampleItemMap = new HashMap<>();
        private Map<SampleItem, Set<TestOrder>> sampleItemTestMap = new HashMap<>();
        private int sampleItemIdIndex = 0;

        public SampleItemTestCache(TypeOfSampleTestDAO typeOfSampleTestDAO, TypeOfSampleDAO typeOfSampleDAO, Sample sample, String sysUserId) {
            this.typeOfSampleTestDAO = typeOfSampleTestDAO;
            this.typeOfSampleDAO = typeOfSampleDAO;
            this.sample = sample;
            this.sysUserId = sysUserId;
        }

        public void add(List<TestOrder> tests) {
            SampleItem sampleItem = findOrCreateSampleItemFromCache(tests.get(0));
            Set<TestOrder> existingTests = sampleItemTestMap.get(sampleItem);
            if(existingTests == null) {
                sampleItemTestMap.put(sampleItem, new HashSet<TestOrder>(tests));
            } else {
                existingTests.addAll(tests);
            }
        }

        public List<TestOrder> getTests(SampleItem sampleItem) {
            return new ArrayList<>(sampleItemTestMap.get(sampleItem));
        }

        public Set<SampleItem> getSampleItems() {
            return sampleItemTestMap.keySet();
        }

        private SampleItem findOrCreateSampleItemFromCache(TestOrder wrapper) {
            TypeOfSampleTest typeOfSampleTestForTest = typeOfSampleTestDAO.getTypeOfSampleTestForTest(wrapper.getTest().getId());
            SampleItem sampleItem = typeOfSampleSampleItemMap.get(typeOfSampleTestForTest.getTypeOfSampleId());
            if (sampleItem == null) {
                TypeOfSample typeOfSample = typeOfSampleDAO.getTypeOfSampleById(typeOfSampleTestForTest.getTypeOfSampleId());
                sampleItem = buildSampleItem(sysUserId, sample, ++sampleItemIdIndex, typeOfSample);
                typeOfSampleSampleItemMap.put(typeOfSample.getId(), sampleItem);
            }

            return sampleItem;
        }
    }

    private static class TestOrderDiff {
        private List<SampleTestOrderCollection> sampleTestOrderCollectionList;
        private List<Analysis> existingAnalyses;

        private TestOrderDiff(List<SampleTestOrderCollection> sampleTestOrderCollectionList, List<Analysis> existingAnalyses) {
            this.sampleTestOrderCollectionList = sampleTestOrderCollectionList;
            this.existingAnalyses = existingAnalyses;
        }

        private Set<TestOrder> getTestsAdded() {
            Set<TestOrder> newTests = getNewlyOrderedTests();
            newTests.removeAll(getPreviouslyOrderedTest());
            return newTests;
        }

        private Set<TestOrder> getTestsDeleted() {
            Set<TestOrder> existingTests = getPreviouslyOrderedTest();
            existingTests.removeAll(getNewlyOrderedTests());
            return existingTests;
        }

        private Set<TestOrder> getTestsIntersection() {
            Set<TestOrder> newTests = getNewlyOrderedTests();
            Set<TestOrder> existingTests = getPreviouslyOrderedTest();
            newTests.retainAll(existingTests);
            return newTests;
        }

        private Set<TestOrder> getPreviouslyOrderedTest() {
            Set<TestOrder> existingTests = new HashSet<>();
            for (Analysis analysis : existingAnalyses) {
                existingTests.add(new TestOrder(analysis.getTest(), analysis.getComment()));
            }
            return existingTests;
        }

        private Set<TestOrder> getNewlyOrderedTests() {
            Set<TestOrder> newTests = new HashSet<>();
            for (SampleTestOrderCollection sampleTestOrderCollection : sampleTestOrderCollectionList) {
                newTests.addAll(sampleTestOrderCollection.tests);
            }
            return newTests;
        }
    }
}

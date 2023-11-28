package org.bahmni.feed.openelis.feed.event;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSOrder;
import org.bahmni.feed.openelis.feed.service.impl.SampleSourceService;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.organization.dao.OrganizationTypeDAO;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.provider.dao.ProviderDAO;
import us.mn.state.health.lims.requester.dao.RequesterTypeDAO;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GroupBySampleTypeFeedProcessor extends EncounterFeedProcessor{

    public static final String ONE = "1";
    public static final String LOW = "0";
    private static final String STAT = "STAT";

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
    private SampleSourceService sampleSourceService;
    private static Logger logger = LogManager.getLogger(GroupBySampleTypeFeedProcessor.class);

    public GroupBySampleTypeFeedProcessor(SampleDAO sampleDAO, ExternalReferenceDao externalReferenceDao,
                                          PanelItemDAO panelItemDAO, TypeOfSampleTestDAO typeOfSampleTestDAO,
                                          TypeOfSampleDAO typeOfSampleDAO, RequesterTypeDAO requesterTypeDAO,
                                          OrganizationTypeDAO organizationTypeDAO, PatientDAO patientDAO, TestDAO testDAO,
                                          AnalysisDAO analysisDAO, SampleItemDAO sampleItemDAO, ProviderDAO providerDAO,
                                          SampleSourceService sampleSourceService) {
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
        this.sampleSourceService = sampleSourceService;
    }

    public void process(OpenMRSEncounter openMRSEncounter, String sysUserId) {
        setRequiredObjects(sampleDAO, externalReferenceDao, panelItemDAO, typeOfSampleTestDAO, typeOfSampleDAO,
                requesterTypeDAO, organizationTypeDAO, patientDAO, testDAO, analysisDAO, sampleItemDAO, providerDAO);
        FeedProcessState processState = new FeedProcessState();
        filterNewTestsAdded(openMRSEncounter, sysUserId);
        List<OpenMRSOrder> labOrders = openMRSEncounter.getLabOrders();
        HashMap<String, List<OpenMRSOrder>> ordersBySampleType = groupOrdersBySampleType(labOrders);
        String encounterUuid = openMRSEncounter.getEncounterUuid();

        for (String sampleType : ordersBySampleType.keySet()) {
            Sample sample = sampleDAO.getSampleByUuidAndSampleTypeIdAndWithoutAccessionNumber(encounterUuid, sampleType);
            List<OpenMRSOrder> orders = ordersBySampleType.get(sampleType);

            if (sample != null) {
                updateSamplePriority(sample, orders);
                updateSample(sample, processState, sysUserId, orders);
            }
            else {
                createSample(openMRSEncounter, processState, sysUserId, orders);
            }
        }

        updateSampleForAllRemovedTests(sysUserId, processState, ordersBySampleType, encounterUuid);
    }

    private void updateSampleForAllRemovedTests(String sysUserId, FeedProcessState processState, HashMap<String, List<OpenMRSOrder>> ordersBySampleType, String encounterUuid) {
        List<Sample> samplesByEncounterUuid = sampleDAO.getSamplesByEncounterUuid(encounterUuid);

        if(ordersBySampleType.size() != samplesByEncounterUuid.size()) {
            List<Integer> sampleTypeIdsAsIntegers = getSampleTypeIds(ordersBySampleType.keySet());
            for (Sample sample : samplesByEncounterUuid) {
                if (StringUtils.isEmpty(sample.getAccessionNumber()) && !sampleItemDAO.isTypeOfSampleAndSampleExists(sample.getId(), sampleTypeIdsAsIntegers)) {
                    updateSample(sample, processState, sysUserId, new ArrayList<OpenMRSOrder>());
                }
            }
        }
    }

    protected Sample getSample(String sysUserId, Date nowAsSqlDate, OpenMRSEncounter openMRSEncounter, List<OpenMRSOrder> orders) {
        Sample sample = new Sample();
        sample.setSysUserId(sysUserId);
        sample.setAccessionNumber(null);
        sample.setSampleSource(sampleSourceService.getSampleSource(openMRSEncounter));

        // TODO : Mujir - remove this hardcoding???? Read this from the event????
        // TODO: Aarthy - Send encounter Date Time as part of event
        sample.setEnteredDate(new java.util.Date());
        sample.setReceivedDate(nowAsSqlDate);
        sample.setDomain(SystemConfiguration.getInstance().getHumanDomain());
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Entered));

        // Mujir - create an external reference to order id in openelis.. this can go in Sample against the accession number
        sample.setUUID(openMRSEncounter.getEncounterUuid());
        sample.setPriority(getPriority(orders));

        return sample;
    }

    private String getPriority(List<OpenMRSOrder> orders) {
        for (OpenMRSOrder order : orders) {
            if (STAT.equals(order.getUrgency())) {
                return ONE;
            }
        }
        return LOW;
    }

    private void updateSamplePriority(Sample sample, List<OpenMRSOrder> orders) {
        if("1".equals(sample.getPriority())) {
            return;
        }
        sample.setPriority(getPriority(orders));
    }


    private List<Integer> getSampleTypeIds(Set<String> sampleIds) {
        List<Integer> ids = new ArrayList<>();
        try {
            for (String id : sampleIds) {
                ids.add(Integer.parseInt(id));
            }
        }
        catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        return ids;
    }

    private HashMap<String, List<OpenMRSOrder>> groupOrdersBySampleType(List<OpenMRSOrder> orders) {
        HashMap<String, List<OpenMRSOrder>> ordersGroup = new HashMap<>();

        for (OpenMRSOrder order : orders) {
            String sampleTypeId;
            String uuid = order.getConcept().getUuid();
            if (order.isLabOrderForPanel()) {
                ExternalReference panel = externalReferenceDao.getData(uuid, "Panel");
                List panelItems = panelItemDAO.getPanelItemsForPanel(String.valueOf(panel.getItemId()));
                PanelItem panelItem = (PanelItem) panelItems.get(0);
                sampleTypeId = typeOfSampleTestDAO.getTypeOfSampleTestForTest(panelItem.getTest().getId()).getTypeOfSampleId();
            } else {
                ExternalReference test = externalReferenceDao.getData(uuid, "Test");
                sampleTypeId = typeOfSampleTestDAO.getTypeOfSampleTestForTest(String.valueOf(test.getItemId())).getTypeOfSampleId();
            }
            List<OpenMRSOrder> openMRSOrders = ordersGroup.get(sampleTypeId);
            List<OpenMRSOrder> sameSampleTypeOrders = openMRSOrders == null ? new ArrayList<OpenMRSOrder>() : openMRSOrders;
            sameSampleTypeOrders.add(order);
            ordersGroup.put(sampleTypeId, sameSampleTypeOrders);
        }

        return ordersGroup;
    }
}

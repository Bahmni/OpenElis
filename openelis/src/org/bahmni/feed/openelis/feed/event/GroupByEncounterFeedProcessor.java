package org.bahmni.feed.openelis.feed.event;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSOrder;
import org.bahmni.feed.openelis.feed.service.impl.SampleSourceService;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.organization.dao.OrganizationTypeDAO;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
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
import java.util.List;

public class GroupByEncounterFeedProcessor extends EncounterFeedProcessor{

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

    public GroupByEncounterFeedProcessor(SampleDAO sampleDAO, ExternalReferenceDao externalReferenceDao,
                                         PanelItemDAO panelItemDAO, TypeOfSampleTestDAO typeOfSampleTestDAO,
                                         TypeOfSampleDAO typeOfSampleDAO, RequesterTypeDAO requesterTypeDAO,
                                         OrganizationTypeDAO organizationTypeDAO, PatientDAO patientDAO,
                                         TestDAO testDAO, AnalysisDAO analysisDAO,
                                         SampleItemDAO sampleItemDAO, ProviderDAO providerDAO,
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
        Sample sample = sampleDAO.getSampleByUuidAndWithoutAccessionNumber(openMRSEncounter.getEncounterUuid());
        if (sample != null) {
            updateSample(sample, processState, sysUserId, openMRSEncounter.getLabOrders());
        } else {
            createSample(openMRSEncounter, processState, sysUserId, openMRSEncounter.getLabOrders());
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

        return sample;
    }
}

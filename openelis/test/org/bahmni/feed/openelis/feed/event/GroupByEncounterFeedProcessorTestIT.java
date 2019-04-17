package org.bahmni.feed.openelis.feed.event;

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSConcept;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSConceptName;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSEncounter;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSOrder;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.OpenMRSProvider;
import org.bahmni.feed.openelis.feed.service.impl.SampleSourceService;
import org.junit.Assert;
import org.junit.Before;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.organization.daoimpl.OrganizationTypeDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.provider.daoimpl.ProviderDAOImpl;
import us.mn.state.health.lims.requester.daoimpl.RequesterTypeDAOImpl;
import us.mn.state.health.lims.requester.daoimpl.SampleRequesterDAOImpl;
import us.mn.state.health.lims.requester.valueholder.SampleRequester;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.util.AccessionNumberUtil;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.bahmni.openelis.builder.TestSetup.createExternalReference;
import static org.bahmni.openelis.builder.TestSetup.createPanel;
import static org.bahmni.openelis.builder.TestSetup.createPanelItem;
import static org.bahmni.openelis.builder.TestSetup.createPatient;
import static org.bahmni.openelis.builder.TestSetup.createProvider;
import static org.bahmni.openelis.builder.TestSetup.createTest;
import static org.bahmni.openelis.builder.TestSetup.createTypeOfSample;
import static org.bahmni.openelis.builder.TestSetup.createTypeOfSampleTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupByEncounterFeedProcessorTestIT extends IT {

    private String identifier;
    private String firstName;
    private String lastName;
    private String patientUUID;
    private TypeOfSample serumSample;
    private TypeOfSample semenSample;
    private Panel apttPanel;
    private String apttPanelConceptUUID;
    private String apttControlTestConceptUUID;
    private Test apttControlTest;
    private Test fbsTest;
    private String fbsTestConceptUUID;
    private Panel semenAnalysisPanel;
    private String semenAnalysiPanelConceptUUID;
    private Test viscosityTest;
    private String viscosityConceptUUID;
    private String cultureTestConceptUUID;
    private Test cultureTest;
    private String protienTestConceptUUID;
    private Test protienTest;
    private Patient patient;
    private Panel bioChemPanel;
    private String bioChemPanelConceptUUID;

    @Before
    public void setUp() {

        //the typeofsamples are cached in a static hashmap.  This is required for clearing before
        //each test
        TypeOfSampleUtil.clearTestCache();

        identifier = "identifier";
        firstName = "firstName";
        lastName = "lastName";
        patientUUID = UUID.randomUUID().toString();
        patient = createPatient(firstName, "middleName", lastName, identifier, patientUUID);

        serumSample = createTypeOfSample("seeerum", "Seeerum");
        semenSample = createTypeOfSample("seemen", "Seemen");

        // aptt panel with two tests of sample serum
        apttPanel = createPanel("Aptt Panel Test");
        apttPanelConceptUUID = UUID.randomUUID().toString();
        ExternalReference anaemiaPanelExternalReference = createExternalReference(apttPanel.getId(), "Panel", apttPanelConceptUUID);

        bioChemPanel = createPanel("Routine Blood Panel Test");
        bioChemPanelConceptUUID = UUID.randomUUID().toString();
        ExternalReference routineBloodPanelExternalReference = createExternalReference(bioChemPanel.getId(), "Panel", bioChemPanelConceptUUID);

        apttControlTestConceptUUID = UUID.randomUUID().toString();
        apttControlTest = createTest("Haemoglobin Test", null);
        createPanelItem(apttControlTest, apttPanel);
        createPanelItem(apttControlTest, bioChemPanel);
        ExternalReference haemoglobinTestExternalReference = createExternalReference(apttControlTest.getId(), "Test", apttControlTestConceptUUID);

        fbsTest = createTest("Blood Smear Test", null, apttPanel);
        fbsTestConceptUUID = UUID.randomUUID().toString();
        ExternalReference testExternalReference = createExternalReference(fbsTest.getId(), "Test", fbsTestConceptUUID);

        // Semen Analysis panel with one test of sample semen
        semenAnalysisPanel = createPanel("Diabetics Panel Test");
        semenAnalysiPanelConceptUUID = UUID.randomUUID().toString();
        ExternalReference diabeticsPanelExternalReference = createExternalReference(semenAnalysisPanel.getId(), "Panel", semenAnalysiPanelConceptUUID);

        viscosityTest = createTest("Diabetics Test", null, semenAnalysisPanel);
        viscosityConceptUUID = UUID.randomUUID().toString();
        ExternalReference diabeticsTestExternalReference = createExternalReference(viscosityTest.getId(), "Test", viscosityConceptUUID);

        //culture test that doesnot belong to any panel and sample urine
        cultureTestConceptUUID = UUID.randomUUID().toString();
        cultureTest = createTest("lone Test", null, null);
        ExternalReference loneTestExternalReference = createExternalReference(cultureTest.getId(), "Test", cultureTestConceptUUID);

        //protein test does not belong to any panel and sample blood
        protienTestConceptUUID = UUID.randomUUID().toString();
        protienTest = createTest("sickle Test", null, null);
        ExternalReference sickleTestExternalReference = createExternalReference(protienTest.getId(), "Test", protienTestConceptUUID);

        createTypeOfSampleTest(apttControlTest.getId(), serumSample.getId());
        createTypeOfSampleTest(fbsTest.getId(), serumSample.getId());
        createTypeOfSampleTest(viscosityTest.getId(), semenSample.getId());
        createTypeOfSampleTest(cultureTest.getId(), semenSample.getId());
        createTypeOfSampleTest(protienTest.getId(), serumSample.getId());

        createProvider("person1", "middleName", "lastName");
    }

    @org.junit.Test
    public void shouldCreateDifferentSampleItemForTestOfDifferentTypeOfSample() {

        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(apttControlTest.getTestName(), apttControlTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(semenAnalysisPanel.getPanelName(), semenAnalysiPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(cultureTest.getTestName(), cultureTestConceptUUID, false);

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID,
                Arrays.asList(openMRSAneamiaConcept, openMRSHaemoglobinConcept, openMRSDiabeticsConcept, openMRSLoneConcept));

        
        getGroupByEncounterFP().process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2, sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(serumSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(2,analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, apttControlTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, fbsTest));

        SampleItem sampleItemForUrine = getSampleItemForSampleType(semenSample, sampleItems);
        List<Analysis> analysisListForUrine = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForUrine);
        assertEquals(2, analysisListForUrine.size());
        assertTrue(containsTestInAnalysis(analysisListForUrine, viscosityTest));
        assertTrue(containsTestInAnalysis(analysisListForUrine, cultureTest));
    }

    @org.junit.Test
    public void shouldCreateOnlyOneSampleItemForPanelOrTestsOfSameSampleType() {

        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        OpenMRSConcept openMRSSickleConcept = createOpenMRSConcept(protienTest.getTestName(), protienTestConceptUUID, false);

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, Arrays.asList(openMRSAneamiaConcept, openMRSSickleConcept));

        
        getGroupByEncounterFP().process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());

        List<SampleRequester> requesters = new SampleRequesterDAOImpl().getRequestersForSampleId(sample.getId());
        assertEquals(1, requesters.size());
        SampleItem sampleItemForBlood = getSampleItemForSampleType(serumSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(3,analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, apttControlTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, fbsTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, protienTest));
    }


    @org.junit.Test
    public void shouldNotAddTestTwiceInSampleItemIfSameTestIsAddedAsPartOfTwoOrders() {

        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(apttControlTest.getTestName(), apttControlTestConceptUUID, false);

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, Arrays.asList(openMRSAneamiaConcept, openMRSHaemoglobinConcept));

        
        getGroupByEncounterFP().process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(serumSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(2, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, apttControlTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, fbsTest));
    }

    @org.junit.Test
    public void shouldUpdateEncounterWhenNewOrderIsAdded() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(apttControlTest.getTestName(), apttControlTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(semenAnalysisPanel.getPanelName(), semenAnalysiPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(cultureTest.getTestName(), cultureTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupByEncounterFeedProcessor groupByEncounterFP = getGroupByEncounterFP();
        groupByEncounterFP.process(openMRSEncounter, "1");

        assertEquals(1, new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).size());

        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        groupByEncounterFP.process(openMRSEncounter, "1");

        assertEquals(1, new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).size());
        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2,sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(serumSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(2, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, apttControlTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, fbsTest));

        SampleItem sampleItemForUrine = getSampleItemForSampleType(semenSample, sampleItems);
        List<Analysis> analysisListForUrine = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForUrine);
        assertEquals(2, analysisListForUrine.size());
        assertTrue(containsTestInAnalysis(analysisListForUrine, viscosityTest));
        assertTrue(containsTestInAnalysis(analysisListForUrine, cultureTest));
    }

    @org.junit.Test
    public void shouldUpdateEncounterWhenOrderIsDeleted() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(apttControlTest.getTestName(), apttControlTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(semenAnalysisPanel.getPanelName(), semenAnalysiPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(cultureTest.getTestName(), cultureTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept, openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupByEncounterFeedProcessor groupByEncounterFP = getGroupByEncounterFP();
        groupByEncounterFP.process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2, sampleItems.size());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        groupByEncounterFP.process(openMRSEncounter, "1");

        sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(sample.getId(), cancelledStatusIds);
        assertEquals(3, cancelledAnalysis.size());
        assertTrue(containsTestInAnalysis(cancelledAnalysis, fbsTest));
        assertTrue(containsTestInAnalysis(cancelledAnalysis, cultureTest));
        assertTrue(containsTestInAnalysis(cancelledAnalysis, viscosityTest));

        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleIdExcludedByStatusId(sample.getId(), cancelledStatusIds);
        assertEquals(1, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, apttControlTest));
    }

    @org.junit.Test
    public void shouldNotUpdateEncounterWhenOrderIsDeletedAfterTheSampleIsCollected() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(apttControlTest.getTestName(), apttControlTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(semenAnalysisPanel.getPanelName(), semenAnalysiPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(cultureTest.getTestName(), cultureTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept, openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupByEncounterFeedProcessor groupByEncounterFP = getGroupByEncounterFP();
        groupByEncounterFP.process(openMRSEncounter, "1");

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        groupByEncounterFP.process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(sample.getId(), cancelledStatusIds);
        assertEquals(0, cancelledAnalysis.size()); //Nothing is cancelled.
    }

    @org.junit.Test
    public void shouldUpdateEncounterWhenOrderIsBothDeletedAndAddedAfterTheSampleIsCollected() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(apttControlTest.getTestName(), apttControlTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(semenAnalysisPanel.getPanelName(), semenAnalysiPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(cultureTest.getTestName(), cultureTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupByEncounterFeedProcessor groupByEncounterFP = getGroupByEncounterFP();
        groupByEncounterFP.process(openMRSEncounter, "1");

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept));  // tests for blood and urine sample type

        groupByEncounterFP.process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2,sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(sample.getId(), cancelledStatusIds);
        assertEquals(0, cancelledAnalysis.size());

        sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(1);
        sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());
        SampleItem sampleItemForUrine = getSampleItemForSampleType(semenSample, sampleItems);
        assertNotNull(sampleItemForUrine);

    }


    @org.junit.Test
    public void shouldUpdateEncounterWhenTestPanelIsDeletedFromOrder_whenOneTestBelongsToMultiplePanel() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        OpenMRSConcept openMRSRoutineBloodConcept = createOpenMRSConcept(bioChemPanel.getPanelName(), bioChemPanelConceptUUID, true);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSAneamiaConcept, openMRSRoutineBloodConcept); // haemoglobin is common test
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupByEncounterFeedProcessor groupByEncounterFP = getGroupByEncounterFP();
        groupByEncounterFP.process(openMRSEncounter, "1");

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept));  // tests for blood and urine sample type
        groupByEncounterFP.process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(sample.getId(), cancelledStatusIds);
        assertEquals(1, cancelledAnalysis.size());
        assertTrue(containsTestInAnalysis(cancelledAnalysis, fbsTest));

        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleIdExcludedByStatusId(sample.getId(),cancelledStatusIds);
        assertEquals(1, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, apttControlTest));
        assertTrue(analysisListForBlood.get(0).getPanel().getPanelName().equals(bioChemPanel.getPanelName()));
    }

    @org.junit.Test
    public void shouldNotReadVoidedOrders() {
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(apttControlTest.getTestName(), apttControlTestConceptUUID, false);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(cultureTest.getTestName(), cultureTestConceptUUID, false);

        String labOrderType = createLabOrderType();
        List<OpenMRSOrder> voidedOrders = createOpenMRSOrders(Arrays.asList(openMRSHaemoglobinConcept), labOrderType, true);
        List<OpenMRSOrder> nonVoidedOrders = createOpenMRSOrders(Arrays.asList(openMRSLoneConcept), labOrderType, false);
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, new ArrayList<OpenMRSConcept>());
        openMRSEncounter.setOrders(Arrays.asList(voidedOrders.get(0), nonVoidedOrders.get(0)));

        
        getGroupByEncounterFP().process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1,sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(serumSample, sampleItems);
        assertNull(sampleItemForBlood);

        SampleItem sampleItemForUrine = getSampleItemForSampleType(semenSample, sampleItems);
        List<Analysis> analysisListForUrine = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForUrine);
        assertEquals(1, analysisListForUrine.size());
        assertTrue(containsTestInAnalysis(analysisListForUrine, cultureTest));
    }

    @org.junit.Test
    public void shouldCreateANewSampleWhenSampleIsCollectedAndDoctorUpdatesTheOrder(){
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(apttControlTest.getTestName(), apttControlTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(semenAnalysisPanel.getPanelName(), semenAnalysiPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(cultureTest.getTestName(), cultureTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupByEncounterFeedProcessor groupByEncounterFP = getGroupByEncounterFP();
        groupByEncounterFP.process(openMRSEncounter, "1");

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());

        //Doctor created more orders.
        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        groupByEncounterFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samples.size());

        Sample sampleAlreadyCollected = samples.get(0);
        assertEquals(SystemConfiguration.getInstance().getSampleStatusEntry2Complete(),sampleAlreadyCollected.getStatus());

        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sampleAlreadyCollected.getId());
        assertEquals(1,sampleItems.size()); //it is blood sample


        Sample newSampleCreated = samples.get(1);
        sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(newSampleCreated.getId());

        SampleItem sampleItemForUrine = getSampleItemForSampleType(semenSample, sampleItems);
        List<Analysis> analysisListForUrine = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForUrine);
        assertEquals(2, analysisListForUrine.size());
        assertTrue(containsTestInAnalysis(analysisListForUrine, viscosityTest));
        assertTrue(containsTestInAnalysis(analysisListForUrine, cultureTest));

        SampleItem sampleItemForBlood = getSampleItemForSampleType(serumSample, sampleItems);
        Assert.assertNull(sampleItemForBlood);
    }

    @org.junit.Test
    public void shouldNotAddSamePanelMultipleTimes(){
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(apttPanel.getPanelName(), apttPanelConceptUUID, true);
        List<OpenMRSConcept> labTests = Arrays.asList(openMRSAneamiaConcept);
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupByEncounterFeedProcessor groupByEncounterFP = getGroupByEncounterFP();
        groupByEncounterFP.process(openMRSEncounter, "1");

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());
        setStopDate(openMRSEncounter.getLabOrders().get(0));
        //Try to sync the same Panel once again.  It should not create a new panel.

        addNewOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept));  // tests for blood and urine sample type
        groupByEncounterFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(1, samples.size());

    }

    @org.junit.Test
    public void shouldCreateSampleWithLocationMappedSampleSource() throws Exception {
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(apttControlTest.getTestName(), apttControlTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(semenAnalysisPanel.getPanelName(), semenAnalysiPanelConceptUUID, true);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupByEncounterFeedProcessor groupByEncounterFP = getGroupByEncounterFP();
        groupByEncounterFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(1, samples.size());

        assertEquals("randomLocationName", samples.get(0).getSampleSource().getName());

        List<OpenMRSConcept> newLabTests = Arrays.asList(openMRSDiabeticsConcept);
        openMRSEncounter = createOpenMRSEncounter(patientUUID, newLabTests);
        groupByEncounterFP.process(openMRSEncounter, "1");

        samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samples.size());

        assertEquals("randomLocationName", samples.get(1).getSampleSource().getName());

    }

    private void setStopDate(OpenMRSOrder openMRSOrder) {
        openMRSOrder.setDateStopped(new Date());
    }

    private void collectSamplesForPatient(String patientId, String status) {
        //Sample is already collected.
        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patientId);

        SampleDAO sampleDAO = new SampleDAOImpl();
        Sample sample = samples.get(0);
        sample.setAccessionNumber(AccessionNumberUtil.getNextAccessionNumber(""));
        sample.setStatus(status);
        sample.setStatusId(status);
        sampleDAO.updateData(sample);
    }

    private Boolean containsTestInAnalysis(List<Analysis> analysisList, Test test) {
        for (Analysis analysis : analysisList) {
            if(analysis.getTest().getId().equals(test.getId())){
                return true;
            }
        }
        return false;
    }

    private SampleItem getSampleItemForSampleType(TypeOfSample typeOfSample, List<SampleItem> sampleItems) {
        for (SampleItem sampleItem : sampleItems) {
            if(sampleItem.getTypeOfSampleId().equals(typeOfSample.getId())){
                return sampleItem;
            }
        }
        return null;
    }

    private OpenMRSEncounter createOpenMRSEncounter(String patientUUID, List<OpenMRSConcept> openmrsConcepts) {
        OpenMRSProvider openMRSProvider = new OpenMRSProvider("abcd-1234", "person1 middleName lastName");

        String labOrderType = createLabOrderType();
        List<OpenMRSOrder> openMRSOrders = createOpenMRSOrders(openmrsConcepts, labOrderType);

        String encounterUUID = UUID.randomUUID().toString();
        return new OpenMRSEncounter(encounterUUID, patientUUID, "randomLocationUuid", "randomLocationName", openMRSOrders, Arrays.asList(openMRSProvider));
    }

    private void addNewOrders(OpenMRSEncounter openMRSEncounter, List<OpenMRSConcept> concepts) {
        List<OpenMRSOrder> orders = openMRSEncounter.getOrders();
        String labOrderType = orders.get(0).getOrderType();
        List<OpenMRSOrder> newOrders = createOpenMRSOrders(concepts, labOrderType);
        orders.addAll(newOrders);
        openMRSEncounter.setOrders(orders);
    }

    private void deleteOrders(OpenMRSEncounter openMRSEncounter, List<OpenMRSConcept> concepts) {
        List<OpenMRSOrder> orders = openMRSEncounter.getOrders();
        List<OpenMRSOrder> toBeRemovedOrders = new ArrayList<>();
        for (OpenMRSOrder order : orders) {
            if(concepts.contains(order.getConcept())) {
                toBeRemovedOrders.add(order);
            }
        }
        orders.removeAll(toBeRemovedOrders);
        openMRSEncounter.setOrders(orders);
    }

    private List<OpenMRSOrder> createOpenMRSOrders(List<OpenMRSConcept> openmrsConcepts, String openMRSOrderType, Boolean voided) {
        List<OpenMRSOrder> openMRSOrders = new ArrayList<>();
        for (OpenMRSConcept openmrsConcept : openmrsConcepts) {
            String orderUUID = UUID.randomUUID().toString();
            OpenMRSOrder openMRSOrder = new OpenMRSOrder(orderUUID, openMRSOrderType, openmrsConcept, voided);
            if(voided){
                openMRSOrder.setDateStopped(new Date());
            }
            openMRSOrders.add(openMRSOrder);
        }
        return openMRSOrders;
    }

    private List<OpenMRSOrder> createOpenMRSOrders(List<OpenMRSConcept> openmrsConcepts, String openMRSOrderType) {
        return createOpenMRSOrders(openmrsConcepts, openMRSOrderType, false);
    }

    private String createLabOrderType() {
        return "Lab Order";
    }

    private OpenMRSConcept createOpenMRSConcept(String conceptName, String conceptUUID, Boolean isSet) {
        OpenMRSConceptName anaemiaConceptName = new OpenMRSConceptName(conceptName);
        OpenMRSConcept openMRSConcept = new OpenMRSConcept(conceptUUID, anaemiaConceptName, isSet);
        return openMRSConcept;
    }

    private GroupByEncounterFeedProcessor getGroupByEncounterFP() {
        return new GroupByEncounterFeedProcessor(new SampleDAOImpl(),
                new ExternalReferenceDaoImpl(), new PanelItemDAOImpl(), new TypeOfSampleTestDAOImpl(),
                new TypeOfSampleDAOImpl(), new RequesterTypeDAOImpl(), new OrganizationTypeDAOImpl(), new PatientDAOImpl(),
                new TestDAOImpl(), new AnalysisDAOImpl(), new SampleItemDAOImpl(), new ProviderDAOImpl(),
                new SampleSourceService(new SampleSourceDAOImpl(), new ExternalReferenceDaoImpl()));
    }
}
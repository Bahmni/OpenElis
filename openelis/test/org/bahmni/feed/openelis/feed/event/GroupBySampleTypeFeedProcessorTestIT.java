package org.bahmni.feed.openelis.feed.event;

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

public class GroupBySampleTypeFeedProcessorTestIT extends IT {

    private String identifier;
    private String firstName;
    private String lastName;
    private String patientUUID;
    private TypeOfSample bloodSample;
    private TypeOfSample urineSample;
    private Panel anaemiaPanel;
    private String anaemiaPanelConceptUUID;
    private String haemoglobinTestConceptUUID;
    private Test haemoglobinTest;
    private Test bloodSmearTest;
    private String bloodSmearTestConceptUUID;
    private Panel diabeticsPanel;
    private String diabeticsPanelConceptUUID;
    private Test diabeticsTest;
    private String diabeticsTestConceptUUID;
    private String loneTestConceptUUID;
    private Test loneTest;
    private String sickleTestConceptUUID;
    private Test sickleTest;
    private Patient patient;
    private Panel routineBloodPanel;
    private String routineBloodPanelConceptUUID;

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

        bloodSample = createTypeOfSample("bloood", "BLD");
        urineSample = createTypeOfSample("URIINE", "URN");

        // anaemia panel with two tests of sample blood
        anaemiaPanel = createPanel("Anaemia Panel Test");
        anaemiaPanelConceptUUID = UUID.randomUUID().toString();
        ExternalReference anaemiaPanelExternalReference = createExternalReference(anaemiaPanel.getId(), "Panel", anaemiaPanelConceptUUID);

        routineBloodPanel = createPanel("Routine Blood Panel Test");
        routineBloodPanelConceptUUID = UUID.randomUUID().toString();
        ExternalReference routineBloodPanelExternalReference = createExternalReference(routineBloodPanel.getId(), "Panel", routineBloodPanelConceptUUID);

        haemoglobinTestConceptUUID = UUID.randomUUID().toString();
        haemoglobinTest = createTest("Haemoglobin Test", null);
        createPanelItem(haemoglobinTest, anaemiaPanel);
        createPanelItem(haemoglobinTest, routineBloodPanel);
        ExternalReference haemoglobinTestExternalReference = createExternalReference(haemoglobinTest.getId(), "Test", haemoglobinTestConceptUUID);

        bloodSmearTest = createTest("Blood Smear Test", null, anaemiaPanel);
        bloodSmearTestConceptUUID = UUID.randomUUID().toString();
        ExternalReference testExternalReference = createExternalReference(bloodSmearTest.getId(), "Test", bloodSmearTestConceptUUID);

        // Urine panel with one test of sample urine
        diabeticsPanel = createPanel("Diabetics Panel Test");
        diabeticsPanelConceptUUID = UUID.randomUUID().toString();
        ExternalReference diabeticsPanelExternalReference = createExternalReference(diabeticsPanel.getId(), "Panel", diabeticsPanelConceptUUID);

        diabeticsTest = createTest("Diabetics Test", null, diabeticsPanel);
        diabeticsTestConceptUUID = UUID.randomUUID().toString();
        ExternalReference diabeticsTestExternalReference = createExternalReference(diabeticsTest.getId(), "Test", diabeticsTestConceptUUID);

        //lone test that doesnot belong to any panel and sample urine
        loneTestConceptUUID = UUID.randomUUID().toString();
        loneTest = createTest("lone Test", null, null);
        ExternalReference loneTestExternalReference = createExternalReference(loneTest.getId(), "Test", loneTestConceptUUID);

        //sickle test diesnot belong to any panel and sample blood
        sickleTestConceptUUID = UUID.randomUUID().toString();
        sickleTest = createTest("sickle Test", null, null);
        ExternalReference sickleTestExternalReference = createExternalReference(sickleTest.getId(), "Test", sickleTestConceptUUID);

        createTypeOfSampleTest(haemoglobinTest.getId(), bloodSample.getId());
        createTypeOfSampleTest(bloodSmearTest.getId(), bloodSample.getId());
        createTypeOfSampleTest(diabeticsTest.getId(), urineSample.getId());
        createTypeOfSampleTest(loneTest.getId(), urineSample.getId());
        createTypeOfSampleTest(sickleTest.getId(), bloodSample.getId());

        createProvider("person1", "middleName", "lastName");
    }

    @org.junit.Test
    public void shouldCreateDifferentSampleItemForTestOfDifferentTypeOfSample() {

        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID,
                Arrays.asList(openMRSAneamiaConcept, openMRSHaemoglobinConcept, openMRSDiabeticsConcept, openMRSLoneConcept));


        getGroupByTypeFP().process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samples.size());

        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(0).getId());
        sampleItems.addAll(new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(1).getId()));

        assertEquals(2, sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(2, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, haemoglobinTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, bloodSmearTest));

        SampleItem sampleItemForUrine = getSampleItemForSampleType(urineSample, sampleItems);
        List<Analysis> analysisListForUrine = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForUrine);
        assertEquals(2, analysisListForUrine.size());
        assertTrue(containsTestInAnalysis(analysisListForUrine, diabeticsTest));
        assertTrue(containsTestInAnalysis(analysisListForUrine, loneTest));
    }

    @org.junit.Test
    public void shouldCreateOnlyOneSampleItemForPanelOrTestsOfSameSampleType() {

        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSSickleConcept = createOpenMRSConcept(sickleTest.getTestName(), sickleTestConceptUUID, false);

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, Arrays.asList(openMRSAneamiaConcept, openMRSSickleConcept));


        getGroupByTypeFP().process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());

        List<SampleRequester> requesters = new SampleRequesterDAOImpl().getRequestersForSampleId(sample.getId());
        assertEquals(1, requesters.size());
        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(3, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, haemoglobinTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, bloodSmearTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, sickleTest));
    }


    @org.junit.Test
    public void shouldNotAddTestTwiceInSampleItemIfSameTestIsAddedAsPartOfTwoOrders() {

        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, Arrays.asList(openMRSAneamiaConcept, openMRSHaemoglobinConcept));


        getGroupByTypeFP().process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(2, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, haemoglobinTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, bloodSmearTest));
    }

    @org.junit.Test
    public void shouldUpdateEncounterWhenNewOrderOfSameSampleTypeIsAdded() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        assertEquals(1, new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).size());

        addNewOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept));  // tests for blood sample type
        groupByTypeFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(1, samples.size());
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(0).getId());

        assertEquals(1, sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(2, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, haemoglobinTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, bloodSmearTest));
    }

    @org.junit.Test
    public void shouldCreateNewSampleRowAndUpdateEncounterWhenNewOrderOfDifferentSampleTypeIsAdded() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        assertEquals(1, new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).size());

        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        groupByTypeFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samples.size());
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(0).getId());
        sampleItems.addAll(new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(1).getId()));

        assertEquals(2, sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(2, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, haemoglobinTest));
        assertTrue(containsTestInAnalysis(analysisListForBlood, bloodSmearTest));

        SampleItem sampleItemForUrine = getSampleItemForSampleType(urineSample, sampleItems);
        List<Analysis> analysisListForUrine = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForUrine);
        assertEquals(2, analysisListForUrine.size());
        assertTrue(containsTestInAnalysis(analysisListForUrine, diabeticsTest));
        assertTrue(containsTestInAnalysis(analysisListForUrine, loneTest));
    }

    @org.junit.Test
    public void shouldUpdateEncounterWhenOrderIsDeleted() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept, openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samples.size());

        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(0).getId());
        sampleItems.addAll(new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(1).getId()));
        assertEquals(2, sampleItems.size());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        groupByTypeFP.process(openMRSEncounter, "1");

        samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(0).getId());
        sampleItems.addAll(new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(1).getId()));
        assertEquals(2, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(samples.get(0).getId(), cancelledStatusIds);
        cancelledAnalysis.addAll(new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(samples.get(1).getId(), cancelledStatusIds));
        assertEquals(3, cancelledAnalysis.size());
        assertTrue(containsTestInAnalysis(cancelledAnalysis, bloodSmearTest));
        assertTrue(containsTestInAnalysis(cancelledAnalysis, loneTest));
        assertTrue(containsTestInAnalysis(cancelledAnalysis, diabeticsTest));

        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleIdExcludedByStatusId(samples.get(0).getId(), cancelledStatusIds);
        analysisListForBlood.addAll(new AnalysisDAOImpl().getAnalysesBySampleIdExcludedByStatusId(samples.get(1).getId(), cancelledStatusIds));
        assertEquals(1, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, haemoglobinTest));
    }

    @org.junit.Test
    public void shouldNotUpdateEncounterWhenOrderIsDeletedAfterTheSampleIsCollected() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept, openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        groupByTypeFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samples.size());
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(0).getId());
        sampleItems.addAll(new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(1).getId()));
        assertEquals(2, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(samples.get(0).getId(), cancelledStatusIds);
        cancelledAnalysis.addAll(new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(samples.get(1).getId(), cancelledStatusIds));
        assertEquals(0, cancelledAnalysis.size()); //Nothing is cancelled.
    }

    @org.junit.Test
    public void shouldUpdateEncounterWhenOrderIsBothDeletedAndAddedAfterTheSampleIsCollected() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept));  // tests for blood and urine sample type

        groupByTypeFP.process(openMRSEncounter, "1");

        List<Sample> samplesForPatient = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(samplesForPatient.get(0).getId());
        sampleItems.addAll(new SampleItemDAOImpl().getSampleItemsBySampleId(samplesForPatient.get(1).getId()));
        assertEquals(2, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(samplesForPatient.get(0).getId(), cancelledStatusIds);
        cancelledAnalysis.addAll(new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(samplesForPatient.get(1).getId(), cancelledStatusIds));
        assertEquals(0, cancelledAnalysis.size());

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());

        List<SampleItem> sampleItemList = new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(0).getId());
        sampleItemList.addAll(new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(1).getId()));
        sampleItemList.addAll(new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(2).getId()));
        assertEquals(2, sampleItems.size());
        SampleItem sampleItemForUrine = getSampleItemForSampleType(urineSample, sampleItems);
        assertNotNull(sampleItemForUrine);

    }


    @org.junit.Test
    public void shouldUpdateEncounterWhenTestPanelIsDeletedFromOrder_whenOneTestBelongsToMultiplePanel() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSRoutineBloodConcept = createOpenMRSConcept(routineBloodPanel.getPanelName(), routineBloodPanelConceptUUID, true);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSAneamiaConcept, openMRSRoutineBloodConcept); // haemoglobin is common test
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept));  // tests for blood and urine sample type
        groupByTypeFP.process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(sample.getId(), cancelledStatusIds);
        assertEquals(1, cancelledAnalysis.size());
        assertTrue(containsTestInAnalysis(cancelledAnalysis, bloodSmearTest));

        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleIdExcludedByStatusId(sample.getId(), cancelledStatusIds);
        assertEquals(1, analysisListForBlood.size());
        assertTrue(containsTestInAnalysis(analysisListForBlood, haemoglobinTest));
        assertTrue(analysisListForBlood.get(0).getPanel().getPanelName().equals(routineBloodPanel.getPanelName()));
    }

    @org.junit.Test
    public void shouldNotReadVoidedOrders() {
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        String labOrderType = createLabOrderType();
        List<OpenMRSOrder> voidedOrders = createOpenMRSOrders(Arrays.asList(openMRSHaemoglobinConcept), labOrderType, true);
        List<OpenMRSOrder> nonVoidedOrders = createOpenMRSOrders(Arrays.asList(openMRSLoneConcept), labOrderType, false);
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, new ArrayList<OpenMRSConcept>());
        openMRSEncounter.setOrders(Arrays.asList(voidedOrders.get(0), nonVoidedOrders.get(0)));


        getGroupByTypeFP().process(openMRSEncounter, "1");

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        assertNull(sampleItemForBlood);

        SampleItem sampleItemForUrine = getSampleItemForSampleType(urineSample, sampleItems);
        List<Analysis> analysisListForUrine = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForUrine);
        assertEquals(1, analysisListForUrine.size());
        assertTrue(containsTestInAnalysis(analysisListForUrine, loneTest));
    }

    @org.junit.Test
    public void shouldCreateANewSampleWhenSampleIsCollectedAndDoctorUpdatesTheOrder() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());

        //Doctor created more orders.
        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        groupByTypeFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samples.size());

        Sample sampleAlreadyCollected = samples.get(0);
        assertEquals(SystemConfiguration.getInstance().getSampleStatusEntry2Complete(), sampleAlreadyCollected.getStatus());

        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sampleAlreadyCollected.getId());
        assertEquals(1, sampleItems.size()); //it is blood sample


        Sample newSampleCreated = samples.get(1);
        sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(newSampleCreated.getId());

        SampleItem sampleItemForUrine = getSampleItemForSampleType(urineSample, sampleItems);
        List<Analysis> analysisListForUrine = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForUrine);
        assertEquals(2, analysisListForUrine.size());
        assertTrue(containsTestInAnalysis(analysisListForUrine, diabeticsTest));
        assertTrue(containsTestInAnalysis(analysisListForUrine, loneTest));

        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        Assert.assertNull(sampleItemForBlood);
    }

    @org.junit.Test
    public void shouldNotAddSamePanelMultipleTimes() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        List<OpenMRSConcept> labTests = Arrays.asList(openMRSAneamiaConcept);
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());
        setStopDate(openMRSEncounter.getLabOrders().get(0));
        //Try to sync the same Panel once again.  It should not create a new panel.

        addNewOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept));  // tests for blood and urine sample type
        groupByTypeFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(1, samples.size());

    }

    @org.junit.Test
    public void shouldCreateSampleWithLocationMappedSampleSource() throws Exception {
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);

        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(1, samples.size());

        assertEquals("randomLocationName", samples.get(0).getSampleSource().getName());

        List<OpenMRSConcept> newLabTests = Arrays.asList(openMRSDiabeticsConcept);
        openMRSEncounter = createOpenMRSEncounter(patientUUID, newLabTests);
        groupByTypeFP.process(openMRSEncounter, "1");

        samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samples.size());

        assertEquals("randomLocationName", samples.get(1).getSampleSource().getName());

    }

    @org.junit.Test
    public void shouldAddPriorityToTheSampleFromOrdersUrgency() {
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);

        String labOrderType = createLabOrderType();
        List<OpenMRSOrder> mrsOrders = createOpenMRSOrders(Arrays.asList(openMRSHaemoglobinConcept), labOrderType);

        mrsOrders.get(0).setUrgency("STAT");

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, new ArrayList<OpenMRSConcept>());
        openMRSEncounter.setOrders(mrsOrders);

        getGroupByTypeFP().process(openMRSEncounter, "1");

        List<Sample> samplesForPatient = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(1, samplesForPatient.size());

        assertEquals("1", samplesForPatient.get(0).getPriority());
    }

    @org.junit.Test
    public void shouldCreateDifferentSamplesForDifferentSampleTypeOrders() {
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, Arrays.asList(openMRSHaemoglobinConcept, openMRSLoneConcept));

        getGroupByTypeFP().process(openMRSEncounter, "1");

        List<Sample> samplesForPatient = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samplesForPatient.size());
    }

    @org.junit.Test
    public void shouldDeleteEntireOrderIfAllTestsAreDeletedForASampleType() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept, openMRSAneamiaConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);
        GroupBySampleTypeFeedProcessor groupByTypeFP = getGroupByTypeFP();
        groupByTypeFP.process(openMRSEncounter, "1");

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(1, samples.size());

        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(0).getId());
        assertEquals(1, sampleItems.size());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept, openMRSHaemoglobinConcept));  // tests for blood and urine sample type
        groupByTypeFP.process(openMRSEncounter, "1");

        samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(samples.get(0).getId());
        assertEquals(1, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(samples.get(0).getId(), cancelledStatusIds);
        assertEquals(2, cancelledAnalysis.size());
        assertTrue(containsTestInAnalysis(cancelledAnalysis, bloodSmearTest));
        assertTrue(containsTestInAnalysis(cancelledAnalysis, haemoglobinTest));
    }

    private void setStopDate(OpenMRSOrder openMRSOrder) {
        openMRSOrder.setDateStopped(new Date());
    }

    private void collectSamplesForPatient(String patientId, String status) {
        //Sample is already collected.
        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patientId);

        SampleDAO sampleDAO = new SampleDAOImpl();
        for (Sample sample : samples) {
            sample.setAccessionNumber(AccessionNumberUtil.getNextAccessionNumber(""));
            sample.setStatus(status);
            sample.setStatusId(status);
            sampleDAO.updateData(sample);
        }

    }

    private Boolean containsTestInAnalysis(List<Analysis> analysisList, Test test) {
        for (Analysis analysis : analysisList) {
            if (analysis.getTest().getId().equals(test.getId())) {
                return true;
            }
        }
        return false;
    }

    private SampleItem getSampleItemForSampleType(TypeOfSample typeOfSample, List<SampleItem> sampleItems) {
        for (SampleItem sampleItem : sampleItems) {
            if (sampleItem.getTypeOfSampleId().equals(typeOfSample.getId())) {
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
            if (concepts.contains(order.getConcept())) {
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
            if (voided) {
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
        return new OpenMRSConcept(conceptUUID, anaemiaConceptName, isSet);
    }

    private GroupBySampleTypeFeedProcessor getGroupByTypeFP() {
        return new GroupBySampleTypeFeedProcessor(new SampleDAOImpl(),
                new ExternalReferenceDaoImpl(), new PanelItemDAOImpl(), new TypeOfSampleTestDAOImpl(),
                new TypeOfSampleDAOImpl(), new RequesterTypeDAOImpl(), new OrganizationTypeDAOImpl(), new PatientDAOImpl(),
                new TestDAOImpl(), new AnalysisDAOImpl(), new SampleItemDAOImpl(), new ProviderDAOImpl(),
                new SampleSourceService(new SampleSourceDAOImpl(), new ExternalReferenceDaoImpl()));
    }
}
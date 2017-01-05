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

package org.bahmni.feed.openelis.feed.mapper.encounter;

import org.apache.commons.io.IOUtils;
import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.*;
import org.bahmni.feed.openelis.feed.event.EncounterFeedWorker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.requester.daoimpl.SampleRequesterDAOImpl;
import us.mn.state.health.lims.requester.valueholder.SampleRequester;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.util.AccessionNumberUtil;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

import static org.bahmni.openelis.builder.TestSetup.*;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class EncounterFeedWorkerIT extends IT {

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
        initMocks(this);

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
    @Ignore("Mujir - need to setup all data needed by elis. Using this more ")
    public void shouldProcessEvent() throws IOException {
        String json = deserialize("sampleOpenMRSEncounter.json");

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        OpenMRSEncounterMapper openMRSEncounterMapper = new OpenMRSEncounterMapper(ObjectMapperRepository.objectMapper);
        OpenMRSEncounter openMRSEncounter = openMRSEncounterMapper.map(json);
        encounterFeedWorker.process(openMRSEncounter);
    }

    private String deserialize(String fileName) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(fileName);
        assertNotNull(inputStream);
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        return writer.toString();
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

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2, sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(2,analysisListForBlood.size());
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

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());

        List<SampleRequester> requesters = new SampleRequesterDAOImpl().getRequestersForSampleId(sample.getId());
        assertEquals(1, requesters.size());
        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(3,analysisListForBlood.size());
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

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

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
    public void shouldUpdateEncounterWhenNewOrderIsAdded() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        assertEquals(1, new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).size());

        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        encounterFeedWorker.process(openMRSEncounter);

        assertEquals(1, new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).size());
        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2,sampleItems.size());

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
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2, sampleItems.size());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        encounterFeedWorker.process(openMRSEncounter);

        sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(sample.getId(), cancelledStatusIds);
        assertEquals(3, cancelledAnalysis.size());
        assertTrue(containsTestInAnalysis(cancelledAnalysis, bloodSmearTest));
        assertTrue(containsTestInAnalysis(cancelledAnalysis, loneTest));
        assertTrue(containsTestInAnalysis(cancelledAnalysis, diabeticsTest));

        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleIdExcludedByStatusId(sample.getId(), cancelledStatusIds);
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
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        encounterFeedWorker.process(openMRSEncounter);

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
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept));  // tests for blood and urine sample type

        encounterFeedWorker.process(openMRSEncounter);

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
        SampleItem sampleItemForUrine = getSampleItemForSampleType(urineSample, sampleItems);
        assertNotNull(sampleItemForUrine);

    }


    @org.junit.Test
    public void shouldUpdateEncounterWhenTestPanelIsDeletedFromOrder_whenOneTestBelongsToMultiplePanel() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSRoutineBloodConcept = createOpenMRSConcept(routineBloodPanel.getPanelName(), routineBloodPanelConceptUUID, true);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSAneamiaConcept, openMRSRoutineBloodConcept); // haemoglobin is common test
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept));  // tests for blood and urine sample type
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1, sampleItems.size());

        String analysisCancelStatus = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled);
        HashSet<Integer> cancelledStatusIds = new HashSet<>(Arrays.asList(Integer.parseInt(analysisCancelStatus)));

        List<Analysis> cancelledAnalysis = new AnalysisDAOImpl().getAnalysesBySampleIdAndStatusId(sample.getId(), cancelledStatusIds);
        assertEquals(1, cancelledAnalysis.size());
        assertTrue(containsTestInAnalysis(cancelledAnalysis, bloodSmearTest));

        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleIdExcludedByStatusId(sample.getId(),cancelledStatusIds);
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

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1,sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        assertNull(sampleItemForBlood);

        SampleItem sampleItemForUrine = getSampleItemForSampleType(urineSample, sampleItems);
        List<Analysis> analysisListForUrine = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForUrine);
        assertEquals(1, analysisListForUrine.size());
        assertTrue(containsTestInAnalysis(analysisListForUrine, loneTest));
    }

    @org.junit.Test
    public void shouldCreateANewSampleWhenSampleIsCollectedAndDoctorUpdatesTheOrder(){
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        //this test shouldnt be added to sample Item blood as this test is included in anaemia panel
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());

        //Doctor created more orders.
        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        encounterFeedWorker.process(openMRSEncounter);

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(2, samples.size());

        Sample sampleAlreadyCollected = samples.get(0);
        assertEquals(SystemConfiguration.getInstance().getSampleStatusEntry2Complete(),sampleAlreadyCollected.getStatus());

        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sampleAlreadyCollected.getId());
        assertEquals(1,sampleItems.size()); //it is blood sample


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
    public void shouldNotAddSamePanelMultipleTimes(){
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        List<OpenMRSConcept> labTests = Arrays.asList(openMRSAneamiaConcept);
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        //The lab assistant has collected the sample.
        collectSamplesForPatient(patient.getId(), SystemConfiguration.getInstance().getSampleStatusEntry2Complete());
        setStopDate(openMRSEncounter.getLabOrders().get(0));
        //Try to sync the same Panel once again.  It should not create a new panel.

        addNewOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept));  // tests for blood and urine sample type
        encounterFeedWorker.process(openMRSEncounter);

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(1, samples.size());

    }

    @org.junit.Test
    public void shouldCreateSampleWithLocationMappedSampleSource() throws Exception {
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, labTests);
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        List<Sample> samples = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId());
        assertEquals(1, samples.size());

        assertEquals("randomLocationName", samples.get(0).getSampleSource().getName());

        List<OpenMRSConcept> newLabTests = Arrays.asList(openMRSDiabeticsConcept);
        openMRSEncounter = createOpenMRSEncounter(patientUUID, newLabTests);
        encounterFeedWorker.process(openMRSEncounter);

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

}

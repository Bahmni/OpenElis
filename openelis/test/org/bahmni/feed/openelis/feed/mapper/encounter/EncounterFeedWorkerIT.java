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
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSName;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatientIdentifier;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPerson;
import org.bahmni.feed.openelis.feed.contract.openmrs.encounter.*;
import org.bahmni.feed.openelis.feed.event.EncounterFeedWorker;
import org.junit.Before;
import org.junit.Ignore;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import java.util.*;

import java.io.*;

import static org.bahmni.openelis.builder.TestSetup.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

        identifier = "identifier";
        firstName = "firstName";
        lastName = "lastName";
        patientUUID = UUID.randomUUID().toString();
        patient = createPatient(firstName, lastName, identifier, patientUUID);

        bloodSample = createTypeOfSample("blood", "BLD");
        urineSample = createTypeOfSample("URINE", "URN");

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
        org.junit.Assert.assertNotNull(inputStream);
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

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, identifier, firstName, lastName,
                Arrays.asList(openMRSAneamiaConcept, openMRSHaemoglobinConcept, openMRSDiabeticsConcept, openMRSLoneConcept));

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2,sampleItems.size());

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

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, identifier, firstName, lastName, Arrays.asList(openMRSAneamiaConcept, openMRSSickleConcept));

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1,sampleItems.size());

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

        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, identifier, firstName, lastName, Arrays.asList(openMRSAneamiaConcept, openMRSHaemoglobinConcept));

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1,sampleItems.size());

        SampleItem sampleItemForBlood = getSampleItemForSampleType(bloodSample, sampleItems);
        List<Analysis> analysisListForBlood = new AnalysisDAOImpl().getAnalysesBySampleItem(sampleItemForBlood);
        assertEquals(2,analysisListForBlood.size());
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
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, identifier, firstName, lastName, labTests);
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        addNewOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        encounterFeedWorker.process(openMRSEncounter);

        assertEquals(1, new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).size());
        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2,sampleItems.size());

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
    public void shouldUpdateEncounterWhenOrderIsDeleted() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSHaemoglobinConcept = createOpenMRSConcept(haemoglobinTest.getTestName(), haemoglobinTestConceptUUID, false);
        OpenMRSConcept openMRSDiabeticsConcept = createOpenMRSConcept(diabeticsPanel.getPanelName(), diabeticsPanelConceptUUID, true);
        OpenMRSConcept openMRSLoneConcept = createOpenMRSConcept(loneTest.getTestName(), loneTestConceptUUID, false);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSHaemoglobinConcept, openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept); //test for only blood sample type
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, identifier, firstName, lastName, labTests);
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSLoneConcept, openMRSAneamiaConcept, openMRSDiabeticsConcept));  // tests for blood and urine sample type
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(2,sampleItems.size());

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
    public void shouldUpdateEncounterWhenTestPanelIsDeletedFromOrder_whenOneTestBelongsToMultiplePanel() {
        OpenMRSConcept openMRSAneamiaConcept = createOpenMRSConcept(anaemiaPanel.getPanelName(), anaemiaPanelConceptUUID, true);
        OpenMRSConcept openMRSRoutineBloodConcept = createOpenMRSConcept(routineBloodPanel.getPanelName(), routineBloodPanelConceptUUID, true);

        List<OpenMRSConcept> labTests = Arrays.asList(openMRSAneamiaConcept, openMRSRoutineBloodConcept); // haemoglobin is common test
        OpenMRSEncounter openMRSEncounter = createOpenMRSEncounter(patientUUID, identifier, firstName, lastName, labTests);
        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(openMRSEncounter);

        deleteOrders(openMRSEncounter, Arrays.asList(openMRSAneamiaConcept));  // tests for blood and urine sample type
        encounterFeedWorker.process(openMRSEncounter);

        Sample sample = new SampleHumanDAOImpl().getSamplesForPatient(patient.getId()).get(0);
        List<SampleItem> sampleItems = new SampleItemDAOImpl().getSampleItemsBySampleId(sample.getId());
        assertEquals(1,sampleItems.size());

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

    private OpenMRSEncounter createOpenMRSEncounter(String patientUUID, String identifier, String firstName, String lastName, List<OpenMRSConcept> openmrsConcepts) {
        OpenMRSName openMRSName = new OpenMRSName(firstName, lastName);
        OpenMRSPerson openMRSPerson = new OpenMRSPerson(openMRSName, patientUUID, "M", DateUtil.convertStringDateToTimestamp("01/01/2001 00:00"), false, null);
        OpenMRSPatient openMRSPatient = new OpenMRSPatient(patientUUID,openMRSPerson ,Arrays.asList(new OpenMRSPatientIdentifier(identifier)));

        OpenMRSOrderType labOrderType = createLabOrderType();
        List<OpenMRSOrder> openMRSOrders = createOpenMRSOrders(openmrsConcepts, labOrderType);

        String encounterUUID = UUID.randomUUID().toString();
        return new OpenMRSEncounter(encounterUUID, openMRSPatient, openMRSOrders);
    }

    private void addNewOrders(OpenMRSEncounter openMRSEncounter, List<OpenMRSConcept> concepts) {
        List<OpenMRSOrder> orders = openMRSEncounter.getOrders();
        OpenMRSOrderType labOrderType = orders.get(0).getOrderType();
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

    private List<OpenMRSOrder> createOpenMRSOrders(List<OpenMRSConcept> openmrsConcepts, OpenMRSOrderType openMRSOrderType) {
        List<OpenMRSOrder> openMRSOrders = new ArrayList<>();
        for (OpenMRSConcept openmrsConcept : openmrsConcepts) {
            String orderUUID = UUID.randomUUID().toString();
            OpenMRSOrder openMRSOrder = new OpenMRSOrder(orderUUID, openMRSOrderType, openmrsConcept);
            openMRSOrders.add(openMRSOrder);
        }
        return openMRSOrders;
    }

    private OpenMRSOrderType createLabOrderType() {
        String orderTypeName = "Lab Order";
        String orderTypeUUID = UUID.randomUUID().toString();
        return new OpenMRSOrderType(orderTypeUUID, orderTypeName, false);
    }

    private OpenMRSConcept createOpenMRSConcept(String conceptName, String conceptUUID, Boolean isSet) {
        OpenMRSConceptName anaemiaConceptName = new OpenMRSConceptName(conceptName);
        OpenMRSConcept openMRSConcept = new OpenMRSConcept(conceptUUID, anaemiaConceptName, isSet);
        return openMRSConcept;
    }

}

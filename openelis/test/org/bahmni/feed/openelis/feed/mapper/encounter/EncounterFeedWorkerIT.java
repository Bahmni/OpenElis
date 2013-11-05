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
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import java.util.ArrayList;
import java.util.Arrays;

import java.io.*;
import java.util.List;
import java.util.UUID;

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

        haemoglobinTestConceptUUID = UUID.randomUUID().toString();
        haemoglobinTest = createTest("Haemoglobin Test", null, anaemiaPanel);
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



    private Boolean containsTestInAnalysis(List<Analysis> analysisList, Test haemoglobinTest) {
        for (Analysis analysis : analysisList) {
            if(analysis.getTest().getId() == haemoglobinTest.getId()){
                return true;
            }
        }
        return false;
    }

    private SampleItem getSampleItemForSampleType(TypeOfSample typeOfSample, List<SampleItem> sampleItems) {
        for (SampleItem sampleItem : sampleItems) {
            if(sampleItem.getTypeOfSampleId() == typeOfSample.getId()){
                return sampleItem;
            }
        }
        return null;
    }

    private OpenMRSEncounter createOpenMRSEncounter(String patientUUID, String identifier, String firstName, String lastName, List<OpenMRSConcept> openmrsConcepts) {
        String orderTypeName = "Lab Order";

        OpenMRSName openMRSName = new OpenMRSName(firstName, lastName);
        OpenMRSPerson openMRSPerson = new OpenMRSPerson(openMRSName, patientUUID, "M", DateUtil.convertStringDateToTimestamp("01/01/2001 00:00"), false, null);
        OpenMRSPatient openMRSPatient = new OpenMRSPatient(patientUUID,openMRSPerson ,Arrays.asList(new OpenMRSPatientIdentifier(identifier)));

        String orderTypeUUID = UUID.randomUUID().toString();
        OpenMRSOrderType openMRSOrderType = new OpenMRSOrderType(orderTypeUUID, orderTypeName, false);

        List<OpenMRSOrder> openMRSOrders = new ArrayList<>();
        for (OpenMRSConcept openmrsConcept : openmrsConcepts) {
            String orderUUID = UUID.randomUUID().toString();
            OpenMRSOrder openMRSOrder = new OpenMRSOrder(orderUUID, openMRSOrderType, openmrsConcept);
            openMRSOrders.add(openMRSOrder);
        }

        String encounterUUID = UUID.randomUUID().toString();
        return new OpenMRSEncounter(encounterUUID, openMRSPatient, openMRSOrders);
    }

    private OpenMRSConcept createOpenMRSConcept(String conceptName, String conceptUUID, Boolean isSet) {
        OpenMRSConceptName anaemiaConceptName = new OpenMRSConceptName(conceptName);
        OpenMRSConcept openMRSConcept = new OpenMRSConcept(conceptUUID, anaemiaConceptName, isSet);
        return openMRSConcept;
    }

}

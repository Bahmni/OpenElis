package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.IT;
import org.bahmni.openelis.domain.TestResultDetails;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.bahmni.openelis.builder.TestSetup.createResult;
import static org.bahmni.openelis.builder.TestSetup.createResultNote;
import static org.bahmni.openelis.builder.TestSetup.createTest;
import static org.bahmni.openelis.builder.TestSetup.createAnalysis;
import static org.bahmni.openelis.builder.TestSetup.createPatient;
import static org.bahmni.openelis.builder.TestSetup.createSample;
import static org.bahmni.openelis.builder.TestSetup.createSampleHuman;
import static org.bahmni.openelis.builder.TestSetup.createSampleItem;

public class TestResultServiceIT extends IT {

    @org.junit.Test
    public void shouldReturnResultDetails() throws Exception {
        String accessionNumber = "10102013-001";
        String testName = "Test Platelet Count";
        String firstName = "First";
        String lastName = "Last";
        String unitOfMeasureName = "cumm";
        String resultValue = "10000";


        Patient patient = createPatient(firstName, lastName, "GAN123");
        Sample sample = createSample(accessionNumber, true);
        SampleHuman sampleHuman = createSampleHuman(sample, patient);
        SampleItem sampleItem = createSampleItem(sample);
        Test test = createTest(testName, unitOfMeasureName);
        Analysis analysis = createAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, "New", test);
        Result result = createResult(analysis, resultValue);
        createResultNote(result, "Some note 1");
        createResultNote(result, "Some note 2");

        TestResultService testResultService = new TestResultService();
        TestResultDetails testResultDetails = testResultService.detailsFor(result.getId());

        assertEquals(resultValue, testResultDetails.getResult());
        assertEquals(accessionNumber, testResultDetails.getAccessionNumber());
        assertEquals(testName, testResultDetails.getTestName());
        assertEquals(sample.getUUID(), testResultDetails.getOrderId());
        assertEquals(patient.getUuid(), testResultDetails.getPatientExternalId());
        assertEquals(firstName, testResultDetails.getPatientFirstName());
        assertEquals(lastName, testResultDetails.getPatientLastName());
        assertEquals(unitOfMeasureName, testResultDetails.getTestUnitOfMeasurement());
        assertEquals(result.getId(), testResultDetails.getResultId());

        assertTrue(testResultDetails.getNotes().contains("Some note 1"));
        assertTrue(testResultDetails.getNotes().contains("Some note 2"));
    }
}

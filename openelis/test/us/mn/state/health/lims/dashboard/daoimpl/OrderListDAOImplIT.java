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

package us.mn.state.health.lims.dashboard.daoimpl;

import org.bahmni.feed.openelis.IT;
import org.junit.Before;
import us.mn.state.health.lims.dashboard.valueholder.Order;
import us.mn.state.health.lims.dashboard.valueholder.TodayStat;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.bahmni.openelis.builder.TestSetup.createTestWithAnalyte;
import static org.bahmni.openelis.builder.TestSetup.createAnalysis;
import static org.bahmni.openelis.builder.TestSetup.createPatient;
import static org.bahmni.openelis.builder.TestSetup.createSample;
import static org.bahmni.openelis.builder.TestSetup.createSampleHuman;
import static org.bahmni.openelis.builder.TestSetup.createSampleItem;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.*;

public class OrderListDAOImplIT extends IT {

    private String accessionNumber;
    private String accessionNumber2;
    private String accessionNumber3;
    private String patientIdentityData;
    private String firstName;
    private String lastName;
    private Patient patient;
    private List<Test> allTests;

    @Before
    public void setUp() throws Exception {
        accessionNumber = "05082013-001";
        accessionNumber2 = "05082013-002";
        accessionNumber3 = "05082013-003";
        patientIdentityData = "TEST1234567";
        firstName = "Some";
        lastName = "One";
        patient = createPatient(firstName, lastName, patientIdentityData);
        TestDAOImpl testDAO = createTests("SampleTest1", "SampleTest2", "SampleTest3", "SampleTest4");
        allTests = testDAO.getAllTests(true);
    }

    @org.junit.Test
    public void getAllInProgress_shouldReturnAllOrdersWhichAreInProgress() throws ParseException {
        Sample sample = createSample(accessionNumber, false);
        createSampleHuman(sample, patient);
        SampleItem sampleItem = createSampleItem(sample);
        createAnalysis(sampleItem, TechnicalAcceptance, "Hematology", allTests.get(0));
        createAnalysis(sampleItem, NotTested, "Hematology", allTests.get(1));
        createAnalysis(sampleItem, ReferedOut, "Hematology", allTests.get(2));
        createAnalysis(sampleItem, BiologistRejected, "Hematology", allTests.get(3));

        List<Order> inProgress = new OrderListDAOImpl().getAllInProgress();

        assertTrue(inProgress.contains(new Order(accessionNumber, patientIdentityData, firstName, lastName, sample.getSampleSource().getName(), 2, 1, 3)));
    }

    @org.junit.Test
    public void getAllCompleted_shouldReturnAllOrdersWhichAreCompletedBefore24Hours() throws ParseException {
        Sample sample1 = createSample(accessionNumber, true);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1);

        Sample sample2 = createSample(accessionNumber2, true);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2);

        Sample sample3 = createSample(accessionNumber3, false);
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(0));
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(1));

        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.NotTested, "Hematology", allTests.get(1));
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(2));

        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(0));

        List<Order> completedOrders = new OrderListDAOImpl().getAllCompletedBefore24Hours();

        assertTrue(completedOrders.size() >= 1);

        List<String> accessionNumbers = accessionNumbersIn(completedOrders);
        assertTrue(accessionNumbers.contains(accessionNumber));
        assertFalse(accessionNumbers.contains(accessionNumber2));
        assertFalse(accessionNumbers.contains(accessionNumber3));
    }

    @org.junit.Test
    public void shouldNotAddCompletedOrdersToInProgressList()  {
        Sample sample1 = createSample(accessionNumber, false);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1);

        Sample sample2 = createSample(accessionNumber2, false);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(0));
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.Canceled, "Hematology", allTests.get(1));
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.NotTested, "Hematology", allTests.get(1));

        List<Order> inProgressOrder = new OrderListDAOImpl().getAllInProgress();

        assertTrue(inProgressOrder.size() >= 1);
        assertFalse(accessionNumbersIn(inProgressOrder).contains(accessionNumber));
    }

    private List<String> accessionNumbersIn(List<Order> inProgressOrder) {
        List<String> accessionNumbers = new ArrayList<>();
        for (Order order : inProgressOrder) {
            accessionNumbers.add(order.getAccessionNumber());
        }
        return accessionNumbers;
    }

    @org.junit.Test
    public void shouldShowTodaysStat() {
        Sample sample1 = createSample(accessionNumber, true);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1);

        Sample sample2 = createSample(accessionNumber2, true);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2);

        Sample sample3 = createSample(accessionNumber3, false);
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3);

        createAnalysis(sampleItem1,StatusOfSampleUtil.AnalysisStatus.NotTested,"Hematology",allTests.get(0));
        createAnalysis(sampleItem2,StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance,"Hematology",allTests.get(1));
        createAnalysis(sampleItem2,StatusOfSampleUtil.AnalysisStatus.Finalized,"Hematology",allTests.get(2));
        createAnalysis(sampleItem3,StatusOfSampleUtil.AnalysisStatus.Finalized,"Hematology",allTests.get(3));

        TodayStat todayStats = new OrderListDAOImpl().getTodayStats();

        assertEquals(1,todayStats.getAwaitingTestCount());
        assertEquals(1,todayStats.getAwaitingValidationCount());
        assertEquals(0,todayStats.getCompletedTestCount());
        assertEquals(2,todayStats.getTotalSamplesCount());
    }

    private TestDAOImpl createTests(String... samples) {
        TestDAOImpl testDAO = new TestDAOImpl();
        for (String sample : samples) {
            createTestWithAnalyte(sample);
        }
        return testDAO;
    }
}

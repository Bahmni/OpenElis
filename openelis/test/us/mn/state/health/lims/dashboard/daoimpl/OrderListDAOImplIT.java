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
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import java.util.Date;
import java.util.List;

import static org.bahmni.openelis.builder.TestSetup.createAnalysis;
import static org.bahmni.openelis.builder.TestSetup.createPatient;
import static org.bahmni.openelis.builder.TestSetup.createSample;
import static org.bahmni.openelis.builder.TestSetup.createSampleHuman;
import static org.bahmni.openelis.builder.TestSetup.createSampleItem;
import static org.bahmni.openelis.builder.TestSetup.createTestWithAnalyte;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrderListDAOImplIT extends IT {
    private String accessionNumber1;
    private String accessionNumber2;
    private String accessionNumber3;
    private String patientIdentityData;
    private String firstName;
    private String lastName;
    private Patient patient;
    private List<Test> allTests;
    private Date today;
    private Date dateInThePast;

    @Before
    public void setUp() throws Exception {
        accessionNumber1 = "05082013-001";
        accessionNumber2 = "05082013-002";
        accessionNumber3 = "05082013-003";
        patientIdentityData = "TEST1234567";
        firstName = "Some";
        lastName = "One";
        patient = createPatient(firstName, lastName, patientIdentityData);
        TestDAOImpl testDAO = createTests("SampleTest1", "SampleTest2", "SampleTest3", "SampleTest4");
        allTests = testDAO.getAllTests(true);
        today = new Date();
        dateInThePast = new Date(today.getTime() - 10 * 24 * 60 * 60 * 1000);
    }

    @org.junit.Test
    public void shouldGetAllTestsToday() {
    Sample sample1 = createSample(accessionNumber1, today);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1);

        Sample sample2 = createSample(accessionNumber2, today);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2);

        Sample sample3 = createSample(accessionNumber3, dateInThePast);
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3);

        createAnalysis(sampleItem1,StatusOfSampleUtil.AnalysisStatus.NotTested,"Hematology",allTests.get(0));
        createAnalysis(sampleItem1,StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance,"Hematology",allTests.get(1));
        createAnalysis(sampleItem2,StatusOfSampleUtil.AnalysisStatus.Finalized,"Hematology",allTests.get(2));
        createAnalysis(sampleItem3,StatusOfSampleUtil.AnalysisStatus.Finalized,"Hematology",allTests.get(3));

        List<Order> allToday = new OrderListDAOImpl().getAllToday();

        assertEquals(2, allToday.size());

        assertTrue(hasAccessionNumber(allToday, accessionNumber1));
        assertTrue(hasAccessionNumber(allToday, accessionNumber2));
        assertFalse(hasAccessionNumber(allToday, accessionNumber3));

        assertEquals( 1, getByAccessionNumber(allToday, accessionNumber1).getPendingTestCount());
        assertEquals( 1, getByAccessionNumber(allToday, accessionNumber1).getPendingValidationCount());
        assertEquals( 2, getByAccessionNumber(allToday, accessionNumber1).getTotalTestCount());
        assertEquals( false, getByAccessionNumber(allToday, accessionNumber1).getIsCompleted());

        assertEquals( 0, getByAccessionNumber(allToday, accessionNumber2).getPendingTestCount());
        assertEquals( 0, getByAccessionNumber(allToday, accessionNumber2).getPendingValidationCount());
        assertEquals( 1, getByAccessionNumber(allToday, accessionNumber2).getTotalTestCount());
        assertEquals( true, getByAccessionNumber(allToday, accessionNumber2).getIsCompleted());
    }

    @org.junit.Test
    public void shouldGetAllPendingTestsBeforeToday() {
        Sample sample1 = createSample(accessionNumber1, dateInThePast);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1);

        Sample sample2 = createSample(accessionNumber2, today);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2);

        Sample sample3 = createSample(accessionNumber3, dateInThePast);
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "Hematology", allTests.get(0));
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, "Hematology", allTests.get(1));
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(2));
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.Finalized, "Hematology", allTests.get(3));

        List<Order> allPendingBeforeToday = new OrderListDAOImpl().getAllPendingBeforeToday();

        assertEquals(1, allPendingBeforeToday.size());

        assertTrue(hasAccessionNumber(allPendingBeforeToday, accessionNumber1));
        assertFalse(hasAccessionNumber(allPendingBeforeToday, accessionNumber2));
        assertFalse(hasAccessionNumber(allPendingBeforeToday, accessionNumber3));

        assertEquals( 1, getByAccessionNumber(allPendingBeforeToday, accessionNumber1).getPendingTestCount());
        assertEquals( 1, getByAccessionNumber(allPendingBeforeToday, accessionNumber1).getPendingValidationCount());
        assertEquals( 2, getByAccessionNumber(allPendingBeforeToday, accessionNumber1).getTotalTestCount());
        assertEquals(false, getByAccessionNumber(allPendingBeforeToday, accessionNumber1).getIsCompleted());
    }

    private Order getByAccessionNumber(List<Order> orders, String accessionNumber) {
        for (Order order: orders) {
            if(order.getAccessionNumber().equals(accessionNumber)){
                return order;
            }
        }
        return null;
    }

    private boolean hasAccessionNumber(List<Order> orders, String accessionNumber) {
        return getByAccessionNumber(orders, accessionNumber) != null;
    }

    private TestDAOImpl createTests(String... samples) {
        TestDAOImpl testDAO = new TestDAOImpl();
        for (String sample : samples) {
            createTestWithAnalyte(sample);
        }
        return testDAO;
    }
}

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
import org.bahmni.openelis.builder.TestSetup;
import org.junit.Before;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.dashboard.valueholder.Order;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.bahmni.openelis.builder.TestSetup.createTestWithAnalyte;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OrderListDAOImplIT extends IT {
    private String accessionNumber1;
    private String accessionNumber2;
    private String accessionNumber3;
    private Patient patient;
    private List<Test> allTests;
    private Date today;
    private Date dateInThePast;
    private TestSection newTestSection;
    private TestSection itTestSection;
    private TypeOfSample urine;
    private TypeOfSample blood;

    @Before
    public void setUp() throws Exception {
        blood = getTypeOfSample("BloodTest", "sampel-uuid_1");
        urine = getTypeOfSample("UrineTest", "sampel-uuid_12");
        accessionNumber1 = "05082013-001";
        accessionNumber2 = "05082013-002";
        accessionNumber3 = "05082013-003";
        String patientIdentityData = "TEST1234567";
        String firstName = "Some";
        String lastName = "One";
        patient = createPatient(firstName, lastName, patientIdentityData);
        TestDAOImpl testDAO = createTests("SampleTest1", "SampleTest2", "SampleTest3", "SampleTest4", "SampleTest5");
        allTests = testDAO.getAllTests(true);
        today = new Date();
        dateInThePast = new Date(today.getTime() - 10 * 24 * 60 * 60 * 1000);
        newTestSection = getTestSection("New");
        itTestSection = getTestSection("user");
    }

    @org.junit.Test
    public void shouldGetAllTestsToday() {
        Sample sample1 = createSample(accessionNumber1, today, "1");
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, urine);

        Sample sample2 = createSample(accessionNumber2, today, "0");
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2, blood);

        Sample sample3 = createSample(accessionNumber3, dateInThePast, "0");
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3, urine);

        Test test0 = allTests.get(0);
        Test test1 = allTests.get(1);
        Test test2 = allTests.get(2);
        Test test3 = allTests.get(3);
        Test test4 = allTests.get(4);

        test0.setTestSection(newTestSection);
        test1.setTestSection(newTestSection);
        test2.setTestSection(newTestSection);
        test3.setTestSection(newTestSection);
        test4.setTestSection(itTestSection);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test0);
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, "New", test1);
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test4);
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.Finalized, "New", test2);
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.Finalized, "user", test3);

        List<Order> allToday = new OrderListDAOImpl(true).getAllToday();

        assertEquals(2, allToday.size());

        assertTrue(hasAccessionNumber(allToday, accessionNumber1));
        assertTrue(hasAccessionNumber(allToday, accessionNumber2));
        assertFalse(hasAccessionNumber(allToday, accessionNumber3));

        Order sampleByAccessionNumber1 = getByAccessionNumber(allToday, accessionNumber1);
        Order sampleByAccessionNumber2 = getByAccessionNumber(allToday, accessionNumber2);

        assertNotNull(sampleByAccessionNumber1);
        assertNotNull(sampleByAccessionNumber2);
        assertEquals(2, sampleByAccessionNumber1.getPendingTestCount());
        assertEquals(1, sampleByAccessionNumber1.getPendingValidationCount());
        assertEquals(3, sampleByAccessionNumber1.getTotalTestCount());
        assertEquals("New,user", sampleByAccessionNumber1.getSectionNames());
        assertFalse(sampleByAccessionNumber1.getIsCompleted());
        assertEquals("UrineTest", sampleByAccessionNumber1.getSampleType());
        assertEquals("High", sampleByAccessionNumber1.getPriority());

        assertEquals(0, sampleByAccessionNumber2.getPendingTestCount());
        assertEquals(0, sampleByAccessionNumber2.getPendingValidationCount());
        assertEquals(1, sampleByAccessionNumber2.getTotalTestCount());
        assertEquals("New", sampleByAccessionNumber2.getSectionNames());
        assertTrue(sampleByAccessionNumber2.getIsCompleted());
        assertEquals("BloodTest", sampleByAccessionNumber2.getSampleType());
        assertEquals("Low", sampleByAccessionNumber2.getPriority());
    }

    @org.junit.Test
    public void shouldGetAllTestsTodayWhenConfigIsNotEnabled() {
        Sample sample1 = createSample(accessionNumber1, today, "0");
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, blood);

        Sample sample2 = createSample(accessionNumber2, today, "0");
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2, urine);

        Sample sample3 = createSample(accessionNumber3, dateInThePast, "0");
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3, urine);

        Test test0 = allTests.get(0);
        Test test1 = allTests.get(1);
        Test test2 = allTests.get(2);
        Test test3 = allTests.get(3);
        Test test4 = allTests.get(4);

        test0.setTestSection(newTestSection);
        test1.setTestSection(newTestSection);
        test2.setTestSection(newTestSection);
        test3.setTestSection(newTestSection);
        test4.setTestSection(itTestSection);

        createAnalysis(sampleItem1,StatusOfSampleUtil.AnalysisStatus.NotTested,"New", test0);
        createAnalysis(sampleItem1,StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance,"New", test1);
        createAnalysis(sampleItem1,StatusOfSampleUtil.AnalysisStatus.NotTested,"New", test4);
        createAnalysis(sampleItem2,StatusOfSampleUtil.AnalysisStatus.Finalized,"New", test2);
        createAnalysis(sampleItem3,StatusOfSampleUtil.AnalysisStatus.Finalized,"user", test3);

        List<Order> allToday = new OrderListDAOImpl(false).getAllToday();

        assertEquals(2, allToday.size());

        assertTrue(hasAccessionNumber(allToday, accessionNumber1));
        assertTrue(hasAccessionNumber(allToday, accessionNumber2));
        assertFalse(hasAccessionNumber(allToday, accessionNumber3));

        Order sampleByAccessionNumber1 = getByAccessionNumber(allToday, accessionNumber1);
        Order sampleByAccessionNumber2 = getByAccessionNumber(allToday, accessionNumber2);

        assertNotNull(sampleByAccessionNumber1);
        assertNotNull(sampleByAccessionNumber2);
        assertEquals( 2, sampleByAccessionNumber1.getPendingTestCount());
        assertEquals( 1, sampleByAccessionNumber1.getPendingValidationCount());
        assertEquals( 3, sampleByAccessionNumber1.getTotalTestCount());
        assertEquals("New,user", sampleByAccessionNumber1.getSectionNames());
        assertEquals( false, sampleByAccessionNumber1.getIsCompleted());
        assertNull(sampleByAccessionNumber1.getSampleType());
        assertNull(sampleByAccessionNumber1.getPriority());

        assertEquals( 0, sampleByAccessionNumber2.getPendingTestCount());
        assertEquals( 0, sampleByAccessionNumber2.getPendingValidationCount());
        assertEquals( 1, sampleByAccessionNumber2.getTotalTestCount());
        assertEquals("New", sampleByAccessionNumber2.getSectionNames());
        assertEquals( true, sampleByAccessionNumber2.getIsCompleted());
        assertNull(sampleByAccessionNumber2.getSampleType());
        assertNull(sampleByAccessionNumber2.getPriority());
    }

    @org.junit.Test
    public void shouldGetSampleNotCollectedToday() {
        Sample sample1 = createSample(null, today, "0");
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, blood);

        Sample sample2 = createSample(null, today, "1");
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2, blood);

        Sample sample3 = createSample(null, dateInThePast, "0");
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3, urine);

        Sample sample4 = createSample(accessionNumber3, dateInThePast, "0");
        createSampleHuman(sample4, patient);
        SampleItem sampleItem4 = createSampleItem(sample4, blood);

        Sample sample5 = createSample(accessionNumber1, today, "0");
        createSampleHuman(sample5, patient);
        SampleItem sampleItem5 = createSampleItem(sample5, urine);

        Test test0 = allTests.get(0);
        Test test1 = allTests.get(1);
        Test test2 = allTests.get(2);
        Test test3 = allTests.get(3);
        Test test4 = allTests.get(4);

        test0.setTestSection(newTestSection);
        test1.setTestSection(itTestSection);
        test2.setTestSection(newTestSection);
        test3.setTestSection(newTestSection);
        test4.setTestSection(newTestSection);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test0);
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.NotTested, "user", test1);
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test2);
        createAnalysis(sampleItem4, StatusOfSampleUtil.AnalysisStatus.Finalized, "New", test3);
        createAnalysis(sampleItem5, StatusOfSampleUtil.AnalysisStatus.Finalized, "New", test4);

        List<Order> sampleNotCollectedToday = new OrderListDAOImpl(true).getAllSampleNotCollectedToday();

        assertEquals(2, sampleNotCollectedToday.size());
        assertEquals("New", sampleNotCollectedToday.get(0).getSectionNames());
        assertEquals("user", sampleNotCollectedToday.get(1).getSectionNames());
        assertEquals("Low", sampleNotCollectedToday.get(0).getPriority());
        assertEquals("High", sampleNotCollectedToday.get(1).getPriority());
        assertEquals("BloodTest", sampleNotCollectedToday.get(1).getSampleType());
        assertEquals("BloodTest", sampleNotCollectedToday.get(1).getSampleType());
    }

    @org.junit.Test
    public void shouldGetSampleNotCollectedTodayWhenConfigIsNotEnabled() {
        Sample sample1 = createSample(null, today, null);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, blood);

        Sample sample2 = createSample(null, today, null);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2, blood);

        Sample sample3 = createSample(null, dateInThePast, null);
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3, urine);

        Sample sample4 = createSample(accessionNumber3, dateInThePast, null);
        createSampleHuman(sample4, patient);
        SampleItem sampleItem4 = createSampleItem(sample4, blood);

        Sample sample5 = createSample(accessionNumber1, today, null);
        createSampleHuman(sample5, patient);
        SampleItem sampleItem5 = createSampleItem(sample5, urine);

        Test test0 = allTests.get(0);
        Test test1 = allTests.get(1);
        Test test2 = allTests.get(2);
        Test test3 = allTests.get(3);
        Test test4 = allTests.get(4);

        test0.setTestSection(newTestSection);
        test1.setTestSection(itTestSection);
        test2.setTestSection(newTestSection);
        test3.setTestSection(newTestSection);
        test4.setTestSection(newTestSection);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test0);
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.NotTested, "user", test1);
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test2);
        createAnalysis(sampleItem4, StatusOfSampleUtil.AnalysisStatus.Finalized, "New", test3);
        createAnalysis(sampleItem5, StatusOfSampleUtil.AnalysisStatus.Finalized, "New", test4);

        List<Order> sampleNotCollectedToday = new OrderListDAOImpl(false).getAllSampleNotCollectedToday();

        assertEquals(2, sampleNotCollectedToday.size());
        assertEquals("New", sampleNotCollectedToday.get(0).getSectionNames());
        assertEquals("user", sampleNotCollectedToday.get(1).getSectionNames());
        assertNull(sampleNotCollectedToday.get(0).getPriority());
        assertNull(sampleNotCollectedToday.get(1).getPriority());
        assertNull(sampleNotCollectedToday.get(0).getSampleType());
        assertNull(sampleNotCollectedToday.get(1).getSampleType());
    }

    @org.junit.Test
    public void shouldGetSampleNotCollectedPendingBeforeToday() {
        Sample sample1 = createSample(null, today, "0");
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, blood);

        Sample sample2 = createSample(null, today, "0");
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2, blood);

        Sample sample3 = createSample(null, dateInThePast, "0");
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3, blood);

        Sample sample4 = createSample(accessionNumber3, dateInThePast, "0");
        createSampleHuman(sample4, patient);
        SampleItem sampleItem4 = createSampleItem(sample4, blood);

        Test test0 = allTests.get(0);
        Test test1 = allTests.get(1);
        Test test2 = allTests.get(2);
        Test test3 = allTests.get(3);
        Test test4 = allTests.get(4);

        test0.setTestSection(newTestSection);
        test1.setTestSection(itTestSection);
        test2.setTestSection(newTestSection);
        test3.setTestSection(newTestSection);
        test4.setTestSection(itTestSection);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test0);
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.NotTested, "user", test1);
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test2);
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test4);
        createAnalysis(sampleItem4, StatusOfSampleUtil.AnalysisStatus.Finalized, "user", test3);

        List<Order> sampleNotCollectedPendingBeforeToday = new OrderListDAOImpl(true).getAllSampleNotCollectedPendingBeforeToday();

        assertEquals(1, sampleNotCollectedPendingBeforeToday.size());
        assertEquals("New,user", sampleNotCollectedPendingBeforeToday.get(0).getSectionNames());
        assertEquals("BloodTest", sampleNotCollectedPendingBeforeToday.get(0).getSampleType());
        assertEquals("Low", sampleNotCollectedPendingBeforeToday.get(0).getPriority());
    }

    @org.junit.Test
    public void shouldGetSampleNotCollectedPendingBeforeTodayWhenConfigIsNotEnabled() {
        Sample sample1 = createSample(null, today, null);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, blood);

        Sample sample2 = createSample(null, today, null);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2, blood);

        Sample sample3 = createSample(null, dateInThePast, null);
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3, blood);

        Sample sample4 = createSample(accessionNumber3, dateInThePast, null);
        createSampleHuman(sample4, patient);
        SampleItem sampleItem4 = createSampleItem(sample4, blood);

        Test test0 = allTests.get(0);
        Test test1 = allTests.get(1);
        Test test2 = allTests.get(2);
        Test test3 = allTests.get(3);
        Test test4 = allTests.get(4);

        test0.setTestSection(newTestSection);
        test1.setTestSection(itTestSection);
        test2.setTestSection(newTestSection);
        test3.setTestSection(newTestSection);
        test4.setTestSection(itTestSection);

        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test0);
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.NotTested, "user", test1);
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test2);
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test4);
        createAnalysis(sampleItem4, StatusOfSampleUtil.AnalysisStatus.Finalized, "user", test3);

        List<Order> sampleNotCollectedPendingBeforeToday = new OrderListDAOImpl(false).getAllSampleNotCollectedPendingBeforeToday();

        assertEquals(1, sampleNotCollectedPendingBeforeToday.size());
        assertEquals("New,user", sampleNotCollectedPendingBeforeToday.get(0).getSectionNames());
        assertNull(sampleNotCollectedPendingBeforeToday.get(0).getSampleType());
        assertNull(sampleNotCollectedPendingBeforeToday.get(0).getPriority());
    }

    @org.junit.Test
    public void shouldMoveSampleFrom_SampleNotCollectedPendingBeforeToday_To_AllTodayAndAllPendingOrderBeforeToday_AfterSampleCollection() {
        Sample sample1 = createSample(null, dateInThePast, "0");
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, urine);

        Test test = allTests.get(0);
        test.setTestSection(newTestSection);
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test);

        OrderListDAOImpl orderListDAO = new OrderListDAOImpl(true);
        List<Order> allSampleNotCollectedPendingBeforeToday = orderListDAO.getAllSampleNotCollectedPendingBeforeToday();
        assertEquals(1, allSampleNotCollectedPendingBeforeToday.size());
        assertEquals("New", allSampleNotCollectedPendingBeforeToday.get(0).getSectionNames());
        assertEquals(0, orderListDAO.getAllPendingBeforeToday().size());

        sample1.setAccessionNumber(accessionNumber1);
        sample1.setLastupdated(new Timestamp(today.getTime()));
        new SampleDAOImpl().updateData(sample1);

        assertEquals(0, orderListDAO.getAllSampleNotCollectedPendingBeforeToday().size());
        List<Order> allPendingBeforeToday = orderListDAO.getAllPendingBeforeToday();
        assertEquals(1, allPendingBeforeToday.size());
        assertEquals("New", allPendingBeforeToday.get(0).getSectionNames());
        List<Order> allToday = orderListDAO.getAllToday();
        assertEquals(1, allToday.size());
        assertEquals("New", allToday.get(0).getSectionNames());
    }

    @org.junit.Test
    public void shouldMoveSampleFrom_PendingBeforeToday_To_AllToday_AfterChangeInAnalysis() {
        Sample sample1 = createSample(accessionNumber1, dateInThePast, "0");
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, blood);

        Test test0 = allTests.get(0);
        Test test1 = allTests.get(1);
        test0.setTestSection(newTestSection);
        test1.setTestSection(newTestSection);

        Analysis analysis1 = createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test0);
        Analysis analysis2 = createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test1);

        OrderListDAOImpl orderListDAO = new OrderListDAOImpl(true);
        List<Order> allPendingBeforeToday = orderListDAO.getAllPendingBeforeToday();
        assertEquals(1, allPendingBeforeToday.size());
        assertEquals("New", allPendingBeforeToday.get(0).getSectionNames());
        assertEquals(0, orderListDAO.getAllToday().size());

        analysis1.setLastupdated(new Timestamp(today.getTime()));
        new AnalysisDAOImpl().updateData(analysis1);

        assertEquals(1, orderListDAO.getAllPendingBeforeToday().size());
        assertEquals(1, orderListDAO.getAllToday().size());

        analysis2.setLastupdated(new Timestamp(today.getTime()));
        new AnalysisDAOImpl().updateData(analysis2);

        assertEquals(0, orderListDAO.getAllPendingBeforeToday().size());
        assertEquals(1, orderListDAO.getAllToday().size());
    }


    @org.junit.Test
    public void shouldGetAllPendingTestsBeforeToday() {
        Sample sample1 = createSample(accessionNumber1, dateInThePast, "0");
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, blood);

        Sample sample2 = createSample(accessionNumber2, today, "0");
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2, urine);

        Sample sample3 = createSample(accessionNumber3, dateInThePast, "0");
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3, blood);

        Test test0 = allTests.get(0);
        Test test1 = allTests.get(1);
        Test test2 = allTests.get(2);
        Test test3 = allTests.get(3);
        test0.setTestSection(newTestSection);
        test1.setTestSection(itTestSection);
        test2.setTestSection(itTestSection);
        test3.setTestSection(newTestSection);


        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test0);
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, "user", test1);
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.Finalized, "user", test2);
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.Finalized, "New", test3);

        List<Order> allPendingBeforeToday = new OrderListDAOImpl(true).getAllPendingBeforeToday();

        assertEquals(1, allPendingBeforeToday.size());

        assertTrue(hasAccessionNumber(allPendingBeforeToday, accessionNumber1));
        assertFalse(hasAccessionNumber(allPendingBeforeToday, accessionNumber2));
        assertFalse(hasAccessionNumber(allPendingBeforeToday, accessionNumber3));

        Order order = getByAccessionNumber(allPendingBeforeToday, accessionNumber1);
        assertEquals(1, order.getPendingTestCount());
        assertEquals(1, order.getPendingValidationCount());
        assertEquals(2, order.getTotalTestCount());
        assertEquals(false, order.getIsCompleted());
        assertEquals("BloodTest", order.getSampleType());
        assertEquals("Low", order.getPriority());
    }

    @org.junit.Test
    public void shouldGetAllPendingTestsBeforeTodayWhenConfigIsNotEnabled() {
        Sample sample1 = createSample(accessionNumber1, dateInThePast, null);
        createSampleHuman(sample1, patient);
        SampleItem sampleItem1 = createSampleItem(sample1, blood);

        Sample sample2 = createSample(accessionNumber2, today, null);
        createSampleHuman(sample2, patient);
        SampleItem sampleItem2 = createSampleItem(sample2, urine);

        Sample sample3 = createSample(accessionNumber3, dateInThePast, null);
        createSampleHuman(sample3, patient);
        SampleItem sampleItem3 = createSampleItem(sample3, blood);

        Test test0 = allTests.get(0);
        Test test1 = allTests.get(1);
        Test test2 = allTests.get(2);
        Test test3 = allTests.get(3);
        test0.setTestSection(newTestSection);
        test1.setTestSection(itTestSection);
        test2.setTestSection(itTestSection);
        test3.setTestSection(newTestSection);


        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.NotTested, "New", test0);
        createAnalysis(sampleItem1, StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, "user", test1);
        createAnalysis(sampleItem2, StatusOfSampleUtil.AnalysisStatus.Finalized, "user", test2);
        createAnalysis(sampleItem3, StatusOfSampleUtil.AnalysisStatus.Finalized, "New", test3);

        List<Order> allPendingBeforeToday = new OrderListDAOImpl(false).getAllPendingBeforeToday();

        assertEquals(1, allPendingBeforeToday.size());

        assertTrue(hasAccessionNumber(allPendingBeforeToday, accessionNumber1));
        assertFalse(hasAccessionNumber(allPendingBeforeToday, accessionNumber2));
        assertFalse(hasAccessionNumber(allPendingBeforeToday, accessionNumber3));

        Order order = getByAccessionNumber(allPendingBeforeToday, accessionNumber1);
        assertEquals(1, order.getPendingTestCount());
        assertEquals(1, order.getPendingValidationCount());
        assertEquals(2, order.getTotalTestCount());
        assertEquals(false, order.getIsCompleted());
        assertNull(order.getSampleType());
        assertNull(order.getPriority());
    }

    private Order getByAccessionNumber(List<Order> orders, String accessionNumber) {
        for (Order order : orders) {
            if (order.getAccessionNumber().equals(accessionNumber)) {
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

    private TestSection getTestSection(String name) {
        return new TestSectionDAOImpl().getTestSectionByName(name);
    }

    private Patient createPatient(String firstName, String lastName, String patientIdentityData) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setSysUserId("1");
        new PersonDAOImpl().insertData(person);

        Patient patient = new Patient();
        patient.setPerson(person);
        patient.setGender("M");
        patient.setBirthDate(DateUtil.getNowAsTimestamp());
        patient.setSysUserId("1");
        patient.setLastupdated(DateUtil.getNowAsTimestamp());
        new PatientDAOImpl().insertData(patient);

        PatientIdentity patientIdentity = new PatientIdentity();
        patientIdentity.setPatientId(patient.getId());
        patientIdentity.setIdentityTypeId(PatientIdentityTypeMap.getInstance().getIDForType("ST"));
        patientIdentity.setIdentityData(patientIdentityData);
        patientIdentity.setSysUserId("1");
        new PatientIdentityDAOImpl().insertData(patientIdentity);

        return patient;
    }

    private SampleHuman createSampleHuman(Sample sample, Patient patient) {
        return TestSetup.createSampleHuman(sample, patient);
    }

    private Analysis createAnalysis(SampleItem sampleItem, StatusOfSampleUtil.AnalysisStatus analysisStatus, String testSectionName, Test test) {
        Analysis analysis = new Analysis();
        analysis.setSampleItem(sampleItem);
        analysis.setAnalysisType(IActionConstants.ANALYSIS_TYPE_MANUAL);
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(analysisStatus));
        analysis.setTest(test);
        analysis.setSysUserId("1");
        analysis.setLastupdated(sampleItem.getSample().getLastupdated());
        analysis.setTestSection(getTestSection(testSectionName));
        new AnalysisDAOImpl().insertData(analysis, false);
        return analysis;
    }

    private SampleItem createSampleItem(Sample startedSample, TypeOfSample blood) {
        SampleItem enteredSampleItem = new SampleItem();
        enteredSampleItem.setSample(startedSample);
        enteredSampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
        enteredSampleItem.setSortOrder("1");
        enteredSampleItem.setSysUserId("1");
        enteredSampleItem.setTypeOfSample(blood);
        new SampleItemDAOImpl().insertData(enteredSampleItem);
        return enteredSampleItem;
    }

    private TypeOfSample getTypeOfSample(String localAbbreviation, String uuid) {
        TypeOfSample typeOfSample = new TypeOfSample();
        typeOfSample.setLocalAbbreviation(localAbbreviation);
        typeOfSample.setUuid(uuid);
        typeOfSample.setDescription(localAbbreviation);
        typeOfSample.setDomain("H");
        typeOfSample.setSysUserId("1");
        new TypeOfSampleDAOImpl().insertData(typeOfSample);

        return typeOfSample;
    }

    private Sample createSample(String accessionNumber, Date date, String priority) {
        List<SampleSource> sampleSources = new SampleSourceDAOImpl().getAll();
        Sample sample = new Sample();
        sample.setAccessionNumber(accessionNumber);
        sample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Started));
        sample.setEnteredDate(DateUtil.convertStringDateToSqlDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        sample.setReceivedTimestamp(DateUtil.convertStringDateToTimestamp("01/01/2001 00:00"));
        sample.setSampleSource(sampleSources.get(0));
        sample.setSysUserId("1");
        sample.setPriority(priority);
        sample.setLastupdated(new Timestamp(date.getTime()));
        new SampleDAOImpl().insertDataWithAccessionNumber(sample);
        return sample;
    }
}

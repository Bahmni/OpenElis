package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.CodedTestAnswer;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.MinimalResource;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataDepartment;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTest;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestServiceIT extends IT {

    private TestService testService;
    private TestResultDAO testResultDAO;
    private TestDAOImpl testDAO;
    private DictionaryDAOImpl dictDAO;
    private ExternalReferenceDaoImpl externalReferenceDao;
    private ReferenceDataTest referenceDataTest;
    private String testUuid;
    private TestSectionService testSectionService;
    private ReferenceDataTest referenceDataTest1;
    private ReferenceDataTest codedReferenceDataTest;

    @Before
    public void setUp() throws Exception {
        testService = new TestService();
        testResultDAO = new TestResultDAOImpl();
        testSectionService = new TestSectionService();
        dictDAO = new DictionaryDAOImpl();
        testDAO = new TestDAOImpl();
        externalReferenceDao = new ExternalReferenceDaoImpl();
        testUuid = UUID.randomUUID().toString();
        referenceDataTest = new ReferenceDataTest(testUuid, "Test Desc", true, new Date(), "Test Name", null, "short", 23, "Numeric", "uom");
        referenceDataTest1 = new ReferenceDataTest(testUuid, "Test Desc", true, new Date(), "Text Test", null, "short", 24, "Text", "uom");
        codedReferenceDataTest = new ReferenceDataTest(testUuid, "Test Desc", true, new Date(), "Test Coded1", null, "short", 24, "Coded", "uom");
        session.createSQLQuery("insert into site_information(id, name, value_type, value) values(nextVal( 'site_information_seq' ), 'defaultOrganizationName','text', 'Haiti')").executeUpdate();
    }

    private CodedTestAnswer getCodedTestAnswer(String uuid, String name) {
        CodedTestAnswer codedTestAnswer1 = new CodedTestAnswer();
        codedTestAnswer1.setUuid(uuid);
        codedTestAnswer1.setName(name);
        return codedTestAnswer1;


    }

    @Test
    public void save_a_new_test_with_dummy_department_and_no_sample_association() throws Exception {
        testService.createOrUpdate(referenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Name");
        assertEquals("New", savedTest.getTestSection().getTestSectionName());
        assertEquals("Test Name", savedTest.getDescription());
        assertEquals("uom", savedTest.getUnitOfMeasure().getUnitOfMeasureName());
    }


    @Test
    public void update_an_existing_test() throws Exception {
        testService.createOrUpdate(referenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Name");
        assertEquals("New", savedTest.getTestSection().getTestSectionName());
        assertEquals("Test Name", savedTest.getDescription());
        assertEquals("uom", savedTest.getUnitOfMeasure().getUnitOfMeasureName());
        assertEquals("23", savedTest.getSortOrder());
        referenceDataTest = new ReferenceDataTest(testUuid, "Test Desc", true, new Date(), "New Test Name", null, "short", 244, "Numeric", "uom");
        testService.createOrUpdate(referenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test updatedTest = testDAO.getTestByDescription("New Test Name");
        assertNotNull(updatedTest.getId());
        assertEquals(savedTest.getId(), updatedTest.getId());
        assertEquals("New Test Name", updatedTest.getTestName());
        assertEquals("244", updatedTest.getSortOrder());
        assertEquals("New", updatedTest.getTestSection().getTestSectionName());
    }

    @Test
    public void update_test_section_on_existing_test() throws Exception {
        testService.createOrUpdate(referenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Name");
        assertEquals("New", savedTest.getTestSection().getTestSectionName());
        String departmentUuid = UUID.randomUUID().toString();
        ReferenceDataDepartment referenceDataDepartment = new ReferenceDataDepartment(departmentUuid, new Date(), "Dept Desc", true, new Date(), "Bio Department");
        referenceDataDepartment.addTest(new MinimalResource(referenceDataTest.getId(), referenceDataTest.getName()));
        testSectionService.createOrUpdate(referenceDataDepartment);
        savedTest = testDAO.getTestByName("Test Name");
        assertEquals("Bio Department", savedTest.getTestSection().getTestSectionName());
    }

    @Test
    public void update_existing_test_result() throws Exception {
        testService.createOrUpdate(referenceDataTest1);
        testService.createOrUpdate(referenceDataTest1);
        testService.createOrUpdate(referenceDataTest1);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Text Test");
        assertEquals("New", savedTest.getTestSection().getTestSectionName());
        String departmentUuid = UUID.randomUUID().toString();
        ReferenceDataDepartment referenceDataDepartment = new ReferenceDataDepartment(departmentUuid, new Date(), "Dept Desc", true, new Date(), "Bio Department");
        referenceDataDepartment.addTest(new MinimalResource(referenceDataTest1.getId(), referenceDataTest1.getName()));
        testSectionService.createOrUpdate(referenceDataDepartment);
        savedTest = testDAO.getTestByName("Text Test");
        assertEquals("Bio Department", savedTest.getTestSection().getTestSectionName());

        List<TestResult> testResultsByTest = testResultDAO.getTestResultsByTest(savedTest.getId());
        testService.createOrUpdate(referenceDataTest1);
        assertEquals(1, testResultsByTest.size());
    }

    @Test
    public void add_test_with_coded_answers() throws Exception {
        codedReferenceDataTest.setCodedTestAnswer(Arrays.asList(getCodedTestAnswer("uuid", "A+ve"), getCodedTestAnswer("uuid2", "A-ve")));
        testService.createOrUpdate(codedReferenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Coded1");
        List<TestResult> testResultsByTest = testResultDAO.getTestResultsByTest(savedTest.getId());
        assertEquals(2, testResultsByTest.size());
        Dictionary ApositiveDict = dictDAO.getDictionaryByDictEntry("A+ve");
        Dictionary AnegativeDict = dictDAO.getDictionaryByDictEntry("A-ve");
        assertNotNull(ApositiveDict);
        assertNotNull(AnegativeDict);
        ExternalReference ApositiveExtRef = externalReferenceDao.getDataByItemId(ApositiveDict.getId(), TestService.CATEGORY_TEST_CODED_ANS);
        assertEquals("uuid", ApositiveExtRef.getExternalId());
        ExternalReference AnegativeExtRef = externalReferenceDao.getDataByItemId(AnegativeDict.getId(), TestService.CATEGORY_TEST_CODED_ANS);
        assertEquals("uuid2", AnegativeExtRef.getExternalId());
    }

    @Test
    public void check_coded_answer_deleteion() throws Exception {
        CodedTestAnswer code1 = getCodedTestAnswer("uuid", "A+ve");
        codedReferenceDataTest.setCodedTestAnswer(Arrays.asList(code1, getCodedTestAnswer("uuid2", "A-ve")));
        testService.createOrUpdate(codedReferenceDataTest);
        codedReferenceDataTest.setCodedTestAnswer(Arrays.asList(code1));
        testService.createOrUpdate(codedReferenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Coded1");
        List<TestResult> testResultsByTest = testResultDAO.getTestResultsByTest(savedTest.getId());
        assertEquals(1, testResultsByTest.size());
        Dictionary AnegativeDict = dictDAO.getDictionaryByDictEntry("A-ve");
        TestResult inactiveTestResult = testResultDAO.getTestResultsByTestAndDictonaryResult(savedTest.getId(), AnegativeDict.getId());
        assertFalse(inactiveTestResult.isActive());

    }

}
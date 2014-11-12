package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataDepartment;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTest;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestServiceIT extends IT {

    private TestService testService;
    private TestDAOImpl testDAO;
    private ReferenceDataTest referenceDataTest;
    private String testUuid;
    private TestSectionService testSectionService;

    @Before
    public void setUp() throws Exception {
        testService = new TestService();
        testSectionService = new TestSectionService();
        testDAO = new TestDAOImpl();
        testUuid = UUID.randomUUID().toString();
        referenceDataTest = new ReferenceDataTest(testUuid, "Test Desc", true, new Date(), "Test Name", null, "short", 23, "Numeric", "uom");
    }

    @Test
    public void save_a_new_test_with_dummy_department_and_no_sample_association() throws Exception {
        testService.createOrUpdate(referenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Name");
        assertEquals("New", savedTest.getTestSection().getTestSectionName());
        assertEquals("Test Desc", savedTest.getDescription());
        assertEquals("uom", savedTest.getUnitOfMeasure().getUnitOfMeasureName());
    }


    @Test
    public void update_an_existing_test() throws Exception {
        testService.createOrUpdate(referenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Name");
        assertEquals("New", savedTest.getTestSection().getTestSectionName());
        assertEquals("Test Desc", savedTest.getDescription());
        assertEquals("uom", savedTest.getUnitOfMeasure().getUnitOfMeasureName());
        assertEquals("23", savedTest.getSortOrder());
        referenceDataTest = new ReferenceDataTest(testUuid, "Test Desc", true, new Date(), "New Test Name", null, "short", 244, "Numeric", "uom");
        testService.createOrUpdate(referenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test updatedTest = testDAO.getTestByDescription("Test Desc");
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
        referenceDataDepartment.addTest(referenceDataTest);
        Organization organization = (Organization) new OrganizationDAOImpl().getAllOrganizations().get(0);
        testSectionService.createOrUpdate(referenceDataDepartment, organization.getOrganizationName());
        savedTest = testDAO.getTestByName("Test Name");
        assertEquals("Bio Department", savedTest.getTestSection().getTestSectionName());
    }
}
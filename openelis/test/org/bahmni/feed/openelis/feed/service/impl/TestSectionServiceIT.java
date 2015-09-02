package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.MinimalResource;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataDepartment;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTest;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.organization.dao.OrganizationDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.TestSection;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestSectionServiceIT extends IT{

    private TestSectionService testSectionService;
    private TestSectionDAO testSectionDAO;
    private OrganizationDAO organizationDAO;
    private AuditingService auditingService;
    private TestService testService;
    private PanelService panelService;
    private TestDAOImpl testDAO;
    private SiteInformationDAO siteInformationDAO;
    private ReferenceDataDepartment dummyDepartment;

    @Before
    public void setUp() throws Exception {
        dummyDepartment = new ReferenceDataDepartment(UUID.randomUUID().toString(), new Date(), "Dummy Desc", true, new Date(), "New");
        auditingService = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl());
        testSectionDAO = new TestSectionDAOImpl();
        organizationDAO = new OrganizationDAOImpl();
        testService = new TestService();
        panelService = new PanelService();
        siteInformationDAO = new SiteInformationDAOImpl();
        testSectionService = new TestSectionService(testSectionDAO, organizationDAO, auditingService, testService, panelService, siteInformationDAO);
        testDAO = new TestDAOImpl();
    }

    @Test
    public void shouldCreateADepartmentWithSomeTests() throws Exception {
        session.createSQLQuery("insert into site_information(id, name, value_type, value) values(nextVal( 'site_information_seq' ), 'defaultOrganizationName','text', 'Haiti')").executeUpdate();

        String departmentId = UUID.randomUUID().toString();
        ReferenceDataDepartment referenceDataDepartment = new ReferenceDataDepartment(departmentId, new Date(), "Dept Desc", true, new Date(), "Bio Department");
        String testId = UUID.randomUUID().toString();
        testSectionService.createOrUpdate(dummyDepartment);

        ReferenceDataTest referenceDataTest = new ReferenceDataTest(testId, "Test Desc", true, new Date(), "Test Name", null, "short", 23, "Test", "uom");
        testService.createOrUpdate(referenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Name");
        assertEquals("New", savedTest.getTestSection().getTestSectionName());
        assertEquals("Test Name", savedTest.getTestName());

        testSectionService.createOrUpdate(referenceDataDepartment);
        TestSection savedTestSection = testSectionDAO.getTestSectionByUUID(departmentId);
        assertNotNull(savedTestSection);
        assertEquals(savedTestSection.getUUID(), departmentId);
        assertEquals("Bio Department", savedTestSection.getTestSectionName());
        assertEquals("Bio Department", savedTestSection.getDescription());
        referenceDataDepartment.addTest(new MinimalResource(referenceDataTest.getId(), referenceDataTest.getName()));
        testSectionService.createOrUpdate(referenceDataDepartment);
        savedTest = testDAO.getTestByName("Test Name");
        assertEquals("Bio Department", savedTest.getTestSection().getTestSectionName());
        assertEquals("Test Name", savedTest.getTestName());
        assertEquals("Test Name", savedTest.getDescription());
    }
}
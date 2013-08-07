package org.bahmni.feed.openelis.feed.event;

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.login.dao.LoginDAO;
import us.mn.state.health.lims.login.valueholder.Login;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LabTestFeedEventWorkerIT extends IT {
    static final String externalReference = "07a5f352-ad6e-4638-9c99-2d5af364a920";
    static final String panelType = "Panel";
    static final String testType = "Test";

    static final String PANEL_EVENT_CONTENT = " {\"category\": \""+panelType+"\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"status\": \"active\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    static final String TEST_EVENT_CONTENT = " {\"category\": \""+testType+"\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"status\": \"active\",\"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    static final String TEST_EVENT_CONTENT_DELETE = " {\"category\": \""+testType+"\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"status\": \"deleted\",\"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    static final String TEST_EVENT_CONTENT_INACTIVE = " {\"category\": \""+testType+"\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"status\": \"inactive\",\"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    private static final String PANEL_EVENT_CONTENT_INACTIVE = " {\"category\": \""+panelType+"\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"status\": \"inactive\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    private static final String PANEL_EVENT_CONTENT_DELETE = " {\"category\": \""+panelType+"\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"status\": \"deleted\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    @Mock
    LoginDAO loginDAO;
    @Mock
    SiteInformationDAO siteInformationDAO;
    ExternalReferenceDaoImpl externalReferenceDao;
    LabTestFeedEventWorker eventWorker;
    private TestDAOImpl testDAO;
    private PanelDAOImpl panelDao;

    @Before
    public void setUp() {
        initMocks(this);
        eventWorker = new LabTestFeedEventWorker(new AuditingService(loginDAO, siteInformationDAO));
        externalReferenceDao = new ExternalReferenceDaoImpl();
        testDAO = new TestDAOImpl();
        panelDao = new PanelDAOImpl();

        when(loginDAO.getUserProfile(any(String.class))).thenReturn(createLoginInfo());
        when(siteInformationDAO.getSiteInformationByName(any(String.class))).thenReturn(createSiteInfo());
    }

    @Test
    public void shouldCreatePanel() throws Exception {
        eventWorker.process(new Event("4234332", PANEL_EVENT_CONTENT));

        PanelDAO panelDao = new PanelDAOImpl();
        Panel panel = panelDao.getPanelByName("ECHO");
        assertNotNull(panel);
    }

    @Test
    public void shouldCreateTest() {
        eventWorker.process(new Event("4234332", TEST_EVENT_CONTENT));
        us.mn.state.health.lims.test.valueholder.Test test = testDAO.getTestByName("ECHO");
        assertNotNull(test);
    }

    @Test
    public void shouldDeleteTest() {
        eventWorker.process(new Event("4234332", TEST_EVENT_CONTENT));
        TestDAOImpl testDAO = new TestDAOImpl();
        us.mn.state.health.lims.test.valueholder.Test test = testDAO.getTestByName("ECHO");
        assertNotNull(test);
        assertNotNull(externalReferenceDao.getData(externalReference, testType));

        eventWorker.process(new Event("4234333", TEST_EVENT_CONTENT_DELETE));
        test = testDAO.getTestById(test.getId());
        assertEquals(IActionConstants.NO, test.getIsActive());
        assertNull(externalReferenceDao.getData(externalReference, testType));
    }

    @Test
    public void shouldInactivateTest() {
        eventWorker.process(new Event("4234332", TEST_EVENT_CONTENT));
        us.mn.state.health.lims.test.valueholder.Test test = testDAO.getTestByName("ECHO");
        assertNotNull(test);
        assertNotNull(externalReferenceDao.getData(externalReference, testType));

        eventWorker.process(new Event("4234333", TEST_EVENT_CONTENT_INACTIVE));
        testDAO = new TestDAOImpl();
        test = testDAO.getTestById(test.getId());
        assertEquals(IActionConstants.NO, test.getIsActive());
        assertNotNull(externalReferenceDao.getData(externalReference, testType));
    }

    @Test
    public void shouldDeletePanel() {
        eventWorker.process(new Event("4234332", PANEL_EVENT_CONTENT));
        Panel panel = panelDao.getPanelByName("ECHO");
        assertNotNull(panel);
        assertNotNull(externalReferenceDao.getData(externalReference, panelType));

        eventWorker.process(new Event("4234333", PANEL_EVENT_CONTENT_DELETE));
        panel = panelDao.getPanelById(panel.getId());
        assertEquals(IActionConstants.NO, panel.getIsActive());
        assertNull(externalReferenceDao.getData(externalReference, panelType));
    }

    @Test
    public void shouldInactivatePanel() {
        eventWorker.process(new Event("4234332", PANEL_EVENT_CONTENT));
        Panel panel = panelDao.getPanelByName("ECHO");
        assertNotNull(panel);
        assertNotNull(externalReferenceDao.getData(externalReference, panelType));

        eventWorker.process(new Event("4234333", PANEL_EVENT_CONTENT_INACTIVE));
        panel = panelDao.getPanelById(panel.getId());
        assertEquals(IActionConstants.NO, panel.getIsActive());
        assertNotNull(externalReferenceDao.getData(externalReference, panelType));
    }

    private SiteInformation createSiteInfo() {
        SiteInformation siteInfo = new SiteInformation();
        siteInfo.setValue("admin");
        return siteInfo;
    }

    private Login createLoginInfo() {
        Login loginInfo = new Login();
        loginInfo.setSysUserId("1");
        return loginInfo;
    }
}
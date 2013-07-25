package org.bahmni.feed.openelis.feed.event;

import junit.framework.Assert;
import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.login.dao.LoginDAO;
import us.mn.state.health.lims.login.valueholder.Login;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LabTestFeedEventWorkerIT extends IT {
    static final String PANEL_EVENT_CONTENT = " {\"category\": \"Panel\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    static final String TEST_EVENT_CONTENT = " {\"category\": \"Test\",\"uuid\": \"07a5f352-ad6e-4638-9c99-2d5af364a920\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    @Mock
    LoginDAO loginDAO;
    @Mock
    SiteInformationDAO siteInformationDAO;
    ExternalReferenceDaoImpl externalReferenceDao;
    LabTestFeedEventWorker eventWorker;

    @Before
    public void setUp() {
        initMocks(this);
        eventWorker = new LabTestFeedEventWorker(new AuditingService(loginDAO, siteInformationDAO));
        externalReferenceDao = new ExternalReferenceDaoImpl();
        when(loginDAO.getUserProfile(any(String.class))).thenReturn(createLoginInfo());
        when(siteInformationDAO.getSiteInformationByName(any(String.class))).thenReturn(createSiteInfo());
    }

    @Test
    public void shouldCreatePanel() throws Exception {
        eventWorker.process(new Event("4234332", PANEL_EVENT_CONTENT));

        PanelDAO panelDao = new PanelDAOImpl();
        Panel panel = panelDao.getPanelByName("ECHO");
        Assert.assertNotNull(panel);
    }

    @Test
    public void shouldCreateTest() {
        eventWorker.process(new Event("4234332", TEST_EVENT_CONTENT));
        TestDAOImpl testDAO = new TestDAOImpl();
        us.mn.state.health.lims.test.valueholder.Test test = testDAO.getTestByName("ECHO");
        Assert.assertNotNull(test);
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
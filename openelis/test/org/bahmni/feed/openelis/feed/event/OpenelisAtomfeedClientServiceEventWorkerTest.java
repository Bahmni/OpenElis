package org.bahmni.feed.openelis.feed.event;

import junit.framework.Assert;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.utils.AtomfeedClientUtils;
import org.hibernate.Transaction;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.dao.LoginDAO;
import us.mn.state.health.lims.login.valueholder.Login;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class OpenelisAtomfeedClientServiceEventWorkerTest {
    static final String EVENT_CONTENT = " {\"category\": \"Panel\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";

    @Mock
    LoginDAO loginDAO;

    @Mock
    SiteInformationDAO siteInformationDAO;

    ExternalReferenceDaoImpl externalReferenceDao;


    OpenelisAtomfeedClientServiceEventWorker eventWorker;
    @Before
    public void setUp(){
        initMocks(this);
        eventWorker = new OpenelisAtomfeedClientServiceEventWorker("hosts/feed/recent");
        externalReferenceDao = new ExternalReferenceDaoImpl();
    }

    @Test
    public void testProcess() throws Exception {
        AtomfeedClientUtils.setLoginDao(loginDAO);
        AtomfeedClientUtils.setSiteInformationDao(siteInformationDAO);
        when(loginDAO.getUserProfile(any(String.class))).thenReturn(createLoginInfo());
        when(siteInformationDAO.getSiteInformationByName(any(String.class))).thenReturn(createSiteInfo());
        eventWorker.process(createEvent());


        PanelDAO panelDao = new PanelDAOImpl();
        Panel panel = panelDao.getPanelByName("ECHO");
        Assert.assertNotNull(panel);
        panel.setSysUserId("1");
        Assert.assertEquals("Panel Name :","ECHO",panel.getPanelName());
        List<Panel> panels = new ArrayList<Panel>();
        panels.add(panel);

        Transaction transaction = HibernateUtil.getSession().beginTransaction();
        panelDao.deleteData(panels);
        ExternalReference externalReference = externalReferenceDao.getData("193", "Panel");
        if(externalReference != null)
            externalReferenceDao.deleteData(externalReference);
        externalReference = externalReferenceDao.getData("193", "Test");
        if(externalReference != null)
            externalReferenceDao.deleteData(externalReference);

        transaction.commit();


        panel = panelDao.getPanelByName("ECHO");
        Assert.assertNull(panel);

    }



    private Event createEvent(){
        Event event = new Event("4234332",EVENT_CONTENT);
        return event;
    }


    private SiteInformation createSiteInfo(){
        SiteInformation siteInfo = new SiteInformation();
        siteInfo.setValue("admin");
        return  siteInfo;
    }

    private Login createLoginInfo(){
        Login loginInfo = new Login() ;
        loginInfo.setSysUserId("1");
        return loginInfo;
    }
}

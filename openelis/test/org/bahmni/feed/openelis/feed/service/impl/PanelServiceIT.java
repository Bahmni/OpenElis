package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.MinimalResource;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataPanel;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTest;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PanelServiceIT extends IT {

    private ReferenceDataTest referenceDataTest;
    private TestService testService;
    private TestDAOImpl testDAO;
    private ReferenceDataPanel referenceDataPanel;
    private PanelService panelService;
    private PanelDAO panelDAO;
    private PanelItemDAO panelItemDAO;

    @Before
    public void setUp() throws Exception {
        String testId = UUID.randomUUID().toString();
        testService = new TestService();
        referenceDataTest = new ReferenceDataTest(testId, "Test Desc", true, new Date(), "Test Name", null, "short", 23, "Test", "uom");
        testDAO = new TestDAOImpl();
        String panelUuid = UUID.randomUUID().toString();
        referenceDataPanel = new ReferenceDataPanel(panelUuid, "Panel Description", true, new Date(), "Panel Name", "short name", 24);
        panelService = new PanelService();
        panelDAO = new PanelDAOImpl();
        panelItemDAO = new PanelItemDAOImpl();

    }

    @Test
    public void associate_tests_with_panel() throws Exception {
        testService.createOrUpdate(referenceDataTest);

        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Name");
        assertEquals("Test Name", savedTest.getTestName());
        assertEquals("New", savedTest.getTestSection().getTestSectionName());

        referenceDataPanel.addTest(new MinimalResource(referenceDataTest.getId(), referenceDataTest.getName()));
        panelService.createOrUpdate(referenceDataPanel);

        Panel savedPanel = panelDAO.getPanelByName("Panel Name");
        assertNotNull(savedPanel);
        assertEquals("Panel Name", savedPanel.getDescription());

        List panelTestAssoc = panelItemDAO.getPanelItemsForPanel(savedPanel.getId());
        assertNotNull(panelTestAssoc);
        assertEquals(1, panelTestAssoc.size());
        PanelItem panelItem = (PanelItem) panelTestAssoc.get(0);
        assertEquals(savedTest.getId(), panelItem.getTest().getId());
    }

    @Test
    public void create_panel_without_tests() throws Exception {
        panelService.createOrUpdate(referenceDataPanel);

        Panel savedPanel = panelDAO.getPanelByName("Panel Name");
        assertNotNull(savedPanel);
        assertEquals("Panel Name", savedPanel.getDescription());

        List panelTestAssoc = panelItemDAO.getPanelItemsForPanel(savedPanel.getId());
        assertEquals(0, panelTestAssoc.size());
    }
}
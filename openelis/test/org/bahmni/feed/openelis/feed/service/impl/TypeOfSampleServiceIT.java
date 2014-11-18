package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.MinimalResource;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataPanel;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataSample;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTest;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSamplePanelDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleTestDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSamplePanel;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeOfSampleServiceIT extends IT {

    private ReferenceDataSample referenceDataSample;
    private ReferenceDataTest referenceDataTest;
    private TestService testService;
    private TestDAOImpl testDAO;
    private TypeOfSampleService typeOfSampleService;
    private TypeOfSampleDAO typeOfSampleDAO;
    private String sampleUuid;
    private TypeOfSampleTestDAOImpl typeOfSampleTestDAO;
    private ReferenceDataPanel referenceDataPanel;
    private PanelService panelService;
    private PanelDAOImpl panelDAO;
    private TypeOfSamplePanelDAOImpl typeOfSamplePanelDao;

    @Before
    public void setUp() throws Exception {
        sampleUuid = UUID.randomUUID().toString();
        referenceDataSample = new ReferenceDataSample(sampleUuid, new Date(), true, new Date(), "Sample Name", "short Name", 10);
        String testId = UUID.randomUUID().toString();
        referenceDataTest = new ReferenceDataTest(testId, "Test Desc", true, new Date(), "Test Name", null, "short", 23, "Test", "uom");
        String panelUuid = UUID.randomUUID().toString();
        referenceDataPanel = new ReferenceDataPanel(panelUuid, "Panel Description", true, new Date(), "Panel Name", "short name", 24);
        testService = new TestService();
        testDAO = new TestDAOImpl();
        typeOfSampleService = new TypeOfSampleService();
        typeOfSampleDAO = new TypeOfSampleDAOImpl();
        typeOfSampleTestDAO = new TypeOfSampleTestDAOImpl();
        panelService = new PanelService();
        panelDAO = new PanelDAOImpl();
        typeOfSamplePanelDao = new TypeOfSamplePanelDAOImpl();
    }

    @Test
    public void sample_save_should_form_test_to_sample_associations() throws Exception {
        testService.createOrUpdate(referenceDataTest);
        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Name");
        assertEquals("Test Name", savedTest.getTestName());
        assertEquals("New", savedTest.getTestSection().getTestSectionName());

        referenceDataSample.addTest(new MinimalResource(referenceDataTest.getId(), referenceDataTest.getName()));
        typeOfSampleService.createOrUpdate(referenceDataSample);
        TypeOfSample savedSample = typeOfSampleDAO.getTypeOfSampleByUUID(sampleUuid);
        assertEquals("Sample Name", savedSample.getDescription());
        assertEquals("Sample Name", savedSample.getLocalAbbreviation());
        TypeOfSampleTest sampleTestAssoc = typeOfSampleTestDAO.getTypeOfSampleTestForTest(savedTest.getId());
        assertNotNull(sampleTestAssoc);
        assertEquals(savedTest.getId(), sampleTestAssoc.getTestId());
        assertEquals(savedSample.getId(), sampleTestAssoc.getTypeOfSampleId());
    }



    @Test
    public void sample_save_should_form_panel_to_sample_associations() throws Exception {
        testService.createOrUpdate(referenceDataTest);
        panelService.createOrUpdate(referenceDataPanel);

        us.mn.state.health.lims.test.valueholder.Test savedTest = testDAO.getTestByName("Test Name");
        assertEquals("Test Name", savedTest.getTestName());
        assertEquals("New", savedTest.getTestSection().getTestSectionName());


        Panel savedPanel = panelDAO.getPanelByName("Panel Name");
        assertEquals("Panel Name", savedPanel.getPanelName());

        referenceDataSample.addTest(new MinimalResource(referenceDataTest.getId(), referenceDataTest.getName()));
        referenceDataSample.addPanel(new MinimalResource(referenceDataPanel.getId(), referenceDataPanel.getName()));
        typeOfSampleService.createOrUpdate(referenceDataSample);

        TypeOfSample savedSample = typeOfSampleDAO.getTypeOfSampleByUUID(sampleUuid);

        assertEquals("Sample Name", savedSample.getDescription());
        assertEquals("Sample Name", savedSample.getLocalAbbreviation());

        TypeOfSampleTest sampleTestAssoc = typeOfSampleTestDAO.getTypeOfSampleTestForTest(savedTest.getId());

        assertNotNull(sampleTestAssoc);
        assertEquals(savedTest.getId(), sampleTestAssoc.getTestId());
        assertEquals(savedSample.getId(), sampleTestAssoc.getTypeOfSampleId());

        TypeOfSamplePanel samplePanelAssoc = typeOfSamplePanelDao.getTypeOfSamplePanelForPanel(savedPanel.getId());
        assertNotNull(samplePanelAssoc);
        assertEquals(savedPanel.getId(), samplePanelAssoc.getPanelId());
        assertEquals(savedSample.getId(), samplePanelAssoc.getTypeOfSampleId());
    }
}
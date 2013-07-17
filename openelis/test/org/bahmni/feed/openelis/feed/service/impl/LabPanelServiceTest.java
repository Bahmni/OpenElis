package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.valueholder.Panel;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LabPanelServiceTest {

    @Mock
    PanelDAO panelDAO;
    @Mock
    ExternalReferenceDao externalReferenceDao;
    static final String EVENT_CONTENT = " {\"category\": \"Panel\",\"description\": \"Test Panel\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    LabPanelService labPanelService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        labPanelService = new LabPanelService(panelDAO,externalReferenceDao);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldInsertNewIfExternalReferenceNotFound() throws IOException {
        when(externalReferenceDao.getData("193", "Panel")).thenReturn(null);

        LabObject labObject = new LabObject("193","Lab Panel","lab panel desc","1", "Panel");
        Panel panel = new Panel();
        panel.setPanelName("Lab Panel");
        panel.setDescription("lab panel desc");


        labPanelService.saveLabObject(labObject);

        verify(panelDAO).insertData(panel);
    }

    @Test
    public void shouldUpdateIfExternalReferenceFound() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        Panel panel = new Panel();
        panel.setPanelName("Lab Panel");
        panel.setDescription("lab panel desc");

        when(externalReferenceDao.getData("193", "Panel")).thenReturn(reference);
        when(panelDAO.getPanelById("293")).thenReturn(panel);


        LabObject labObject = new LabObject("193","Lab Panel","lab panel desc","1", "Panel");

        labPanelService.saveLabObject(labObject);

//        verify(panelDAO).updateData(panel);
    }
}

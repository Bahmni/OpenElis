package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.valueholder.Panel;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LabPanelServiceTest {
    @Mock
    PanelDAO panelDAO;
    @Mock
    ExternalReferenceDao externalReferenceDao;
    LabPanelService labPanelService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        labPanelService = new LabPanelService(panelDAO,externalReferenceDao);
    }

    @Test
    public void shouldInsertNewIfExternalReferenceNotFound() throws Exception {
        when(externalReferenceDao.getData("193", "Panel")).thenReturn(null);

        LabObject labObject = new LabObject("193","Lab Panel","lab panel desc","1", "Panel", "active");
        Panel panel = new Panel();
        panel.setPanelName("Lab Panel");
        panel.setDescription("Lab Panel");

        labPanelService.process(labObject);
        verify(panelDAO).insertData(panel);
    }

    @Test
    public void shouldUpdateIfExternalReferenceFound() throws Exception {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        Panel panel = new Panel();
        panel.setPanelName("Lab Panel");
        panel.setDescription("Lab Panel");

        when(externalReferenceDao.getData("193", "Panel")).thenReturn(reference);
        when(panelDAO.getPanelById("293")).thenReturn(panel);


        LabObject labObject = new LabObject("193","Lab Panel","lab panel desc","1", "Panel", "active");

        labPanelService.process(labObject);

//        verify(panelDAO).updateData(panel);
    }
}

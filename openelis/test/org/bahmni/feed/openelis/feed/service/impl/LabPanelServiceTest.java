package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.valueholder.Panel;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
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
        LabObject labObject = new LabObject("193","Lab Panel","lab panel desc","1", "Panel", "active");
        Panel panel = new Panel();
        panel.setPanelName("Lab Panel");
        panel.setDescription("Lab Panel");
        when(externalReferenceDao.getData("193", "Panel")).thenReturn(null);

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
        LabObject labObject = new LabObject("193","Lab Panel","lab panel desc new","1", "Panel", "active");

        when(externalReferenceDao.getData("193", "Panel")).thenReturn(reference);
        when(panelDAO.getPanelById("293")).thenReturn(panel);

        labPanelService.process(labObject);

        verify(panelDAO).updateData(panel);
    }

    @Test
    public void shouldInactivateExistingActivePanel() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");
        LabObject labObject = new LabObject("193","Lab Panel","Lab Panel desc new","1", "Panel","inactive");
        Panel panel = new Panel();
        panel.setPanelName("Lab Panel");
        panel.setDescription("Lab Panel desc old");
        panel.setId("293");
        panel.setIsActive("Y");

        when(externalReferenceDao.getData("193", "Panel")).thenReturn(reference);
        when(panelDAO.getPanelById("293")).thenReturn(panel);

        labPanelService.save(labObject);

        ArgumentCaptor<Panel> panelArgumentCaptor = ArgumentCaptor.forClass(Panel.class);
        verify(panelDAO).updateData(panelArgumentCaptor.capture());
        Panel savedPanel = panelArgumentCaptor.getValue();
        assertEquals(labObject.getDescription(), savedPanel.getDescription());
        assertEquals("293", savedPanel.getId());
        assertEquals("N", savedPanel.getIsActive());
    }

    @Test
    public void shouldActivateExistingInactiveTest() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");
        LabObject labObject = new LabObject("193","Lab Panel","Lab Panel desc new","1", "Panel","active");
        Panel panel = new Panel();
        panel.setPanelName("Lab Panel");
        panel.setDescription("Lab Panel desc old");
        panel.setId("293");
        panel.setIsActive("N");

        when(externalReferenceDao.getData("193", "Panel")).thenReturn(reference);
        when(panelDAO.getPanelById("293")).thenReturn(panel);

        labPanelService.save(labObject);

        ArgumentCaptor<Panel> panelArgumentCaptor = ArgumentCaptor.forClass(Panel.class);
        verify(panelDAO).updateData(panelArgumentCaptor.capture());
        Panel savedPanel = panelArgumentCaptor.getValue();
        assertEquals(labObject.getDescription(), savedPanel.getDescription());
        assertEquals("293", savedPanel.getId());
        assertEquals("Y", savedPanel.getIsActive());
    }

    @Test
    public void shouldSaveNewInactivatePanel() throws IOException {
        LabObject labObject = new LabObject("193","Lab Panel","lab panel desc new","1", "Panel","inactive");
        when(externalReferenceDao.getData("193", "Panel")).thenReturn(null);

        labPanelService.save(labObject);

        ArgumentCaptor<Panel> panelCaptor = ArgumentCaptor.forClass(Panel.class);
        verify(panelDAO).insertData(panelCaptor.capture());
        Panel panelCaptorValue = panelCaptor.getValue();
        assertEquals(labObject.getName(), panelCaptorValue.getPanelName());
        assertEquals("N", panelCaptorValue.getIsActive());
    }

    @Test
    public void shouldDeletePanelIfNoReferencesExistToPanel() throws IOException {
        ExternalReference reference = new ExternalReference(293, "193", "Panel");
        LabObject labObject = new LabObject("193","Lab Panel","lab panel desc new","1", "Panel","deleted");
        Panel panel = new Panel();
        panel.setPanelName("Lab Panel");
        panel.setDescription("lab panel desc old");
        panel.setId("293");
        panel.setIsActive("N");
        when(externalReferenceDao.getData(labObject.getExternalId(), labObject.getCategory())).thenReturn(reference);

        labPanelService.delete(labObject);

        verify(externalReferenceDao).deleteData(reference);
        verify(panelDAO).deleteById("293", labObject.getSysUserId());
    }

}

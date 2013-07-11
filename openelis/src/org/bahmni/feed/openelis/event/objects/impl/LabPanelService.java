package org.bahmni.feed.openelis.event.objects.impl;

import org.bahmni.feed.openelis.event.objects.EventObject;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;

import java.io.IOException;
import java.util.HashMap;


public class LabPanelService extends TransactionalEventObject  implements EventObject {

    private PanelDAO panelDAO = new PanelDAOImpl();
    private ExternalReferenceDao externalReferenceDao = new ExternalReferenceDaoImpl();

    private String sysUserId;
    private String externalId;

    public LabPanelService(String sysUserId){
        this.sysUserId = sysUserId;
    }

    LabPanelService(PanelDAO panelDAO, ExternalReferenceDao externalReferenceDao) {
        this.panelDAO = panelDAO;
        this.externalReferenceDao = externalReferenceDao;
    }

    protected void saveEvent(Event event) throws IOException {
        Panel panel = mapToPanel(event);
        ExternalReference data = externalReferenceDao.getData(externalId);
        if(data ==null){
            panelDAO.insertData(panel);
            data = new ExternalReference(Long.parseLong(panel.getId()),externalId,"panel");
            externalReferenceDao.insertData(data)  ;
        }
        else {
            Panel panelById = panelDAO.getPanelById(String.valueOf(data.getItemId()));
            updatePanelFieldsIfNotEmpty(panel, panelById);
            panelDAO.updateData(panelById);
        }
    }

    private void updatePanelFieldsIfNotEmpty(Panel panel, Panel panelById) {
        if(isSet(panel.getName())){
            panelById.setName(panel.getName());
        }
        if(isSet(panel.getDescription())){
            panelById.setDescription(panel.getDescription());
        }
        if(isSet(panel.getSysUserId())){
            panelById.setSysUserId(panel.getSysUserId());
        }
    }

    private boolean isSet(String value){
       return value != null && !value.isEmpty();
    }

    private Panel mapToPanel(Event event) throws IOException {
        Panel panel = new us.mn.state.health.lims.panel.valueholder.Panel();
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;

        panel.setPanelName((String) paramMap.get("name"));
        String desc = (String) paramMap.get("description");
        if(desc == null || desc.isEmpty()){
            desc = (String) paramMap.get("name");
        }
        panel.setDescription(desc);
        externalId = String.valueOf(paramMap.get("id"));
        panel.setSysUserId(sysUserId);

        return panel;
    }

    void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }
}

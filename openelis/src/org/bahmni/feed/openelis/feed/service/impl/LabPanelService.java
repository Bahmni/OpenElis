package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.bahmni.feed.openelis.feed.service.LabService;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;

import java.io.IOException;


public class LabPanelService extends TransactionalService implements LabService {

    private PanelDAO panelDAO = new PanelDAOImpl();
    private ExternalReferenceDao externalReferenceDao = new ExternalReferenceDaoImpl();
    private String labProductType;

    public LabPanelService(){
        labProductType = AtomFeedProperties.getInstance().getProductTypePanel();
    }

    LabPanelService(PanelDAO panelDAO, ExternalReferenceDao externalReferenceDao) {
        this.panelDAO = panelDAO;
        this.externalReferenceDao = externalReferenceDao;
    }

    protected void saveLabObject(LabObject labObject) throws IOException {
        Panel panel = mapToPanel(labObject);
        ExternalReference data = externalReferenceDao.getData(labObject.getExternalId(), labProductType);
        if(data ==null){
            panelDAO.insertData(panel);
            if(isEmpty(panel)) {
                data = new ExternalReference(Long.parseLong(panel.getId()),labObject.getExternalId(),labProductType);
            }
            externalReferenceDao.insertData(data)  ;
        }
        else {
            Panel panelById = panelDAO.getPanelById(String.valueOf(data.getItemId()));
            updatePanelFieldsIfNotEmpty(panel, panelById);
            panelDAO.updateData(panelById);
        }
    }

    private boolean isEmpty(Panel panel) {
        return panel.getId() != null && !panel.getId().isEmpty();
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

    private Panel mapToPanel(LabObject labObject) throws IOException {
        Panel panel = new us.mn.state.health.lims.panel.valueholder.Panel();
        panel.setPanelName(labObject.getName());
        String desc = labObject.getDescription();
        if(desc == null || desc.isEmpty()){
            desc = labObject.getName();
        }
        panel.setDescription(desc);
        panel.setSysUserId(labObject.getSysUserId());
        return panel;
    }


}

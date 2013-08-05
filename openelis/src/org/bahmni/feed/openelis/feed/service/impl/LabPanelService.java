package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.bahmni.feed.openelis.feed.service.LabService;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;

import java.io.IOException;


public class LabPanelService extends LabService {

    private PanelDAO panelDAO = new PanelDAOImpl();
    private ExternalReferenceDao externalReferenceDao = new ExternalReferenceDaoImpl();
    private String labProductType;

    public LabPanelService() {
        labProductType = AtomFeedProperties.getInstance().getProductTypePanel();
    }

    LabPanelService(PanelDAO panelDAO, ExternalReferenceDao externalReferenceDao) {
        this();
        this.panelDAO = panelDAO;
        this.externalReferenceDao = externalReferenceDao;
    }

    @Override
    protected void save(LabObject labObject) throws IOException {
        Panel panel = mapToPanel(labObject);
        String externalId = labObject.getExternalId();
        ExternalReference data = externalReferenceDao.getData(externalId, labProductType);
        if (data == null) {
            panelDAO.insertData(panel);
            if(hasId(panel)){
                ExternalReference externalReference = new ExternalReference(Long.parseLong(panel.getId()), externalId, labProductType);
                externalReferenceDao.insertData(externalReference);
            }
        } else {
            Panel panelById = panelDAO.getPanelById(String.valueOf(data.getItemId()));
            if (panelById != null) {
                updatePanelFieldsIfNotEmpty(panel, panelById);
                panelDAO.updateData(panelById);
            }
        }
    }

    @Override
    protected void delete(LabObject labObject) {
        ExternalReference externalReference = getExternalReference(labObject);
        if(externalReference != null){
            org.hibernate.Transaction tx = HibernateUtil.getSession().beginTransaction();
            externalReferenceDao.deleteData(externalReference);
            String panelId = String.valueOf(externalReference.getItemId());
            panelDAO.deleteById(panelId, labObject.getSysUserId());
            tx.commit();
        }
    }

    private ExternalReference getExternalReference(LabObject labObject) {
        return externalReferenceDao.getData(labObject.getExternalId(),labObject.getCategory());
    }

    private boolean hasId(Panel panel) {
        return panel.getId() != null && !panel.getId().isEmpty();
    }

    private void updatePanelFieldsIfNotEmpty(Panel panel, Panel panelById) {
        if (isSet(panel.getPanelName())) {
            panelById.setPanelName(panel.getPanelName());
        }
        if (isSet(panel.getDescription())) {
            panelById.setDescription(panel.getDescription());
        }
        if (isSet(panel.getSysUserId())) {
            panelById.setSysUserId(panel.getSysUserId());
        }
        if(isSet(panel.getIsActive())){
            panelById.setIsActive(panel.getIsActive());
        }
    }

    private boolean isSet(String value) {
        return value != null && !value.isEmpty();
    }


    private Panel mapToPanel(LabObject labObject) throws IOException {
        Panel panel = new us.mn.state.health.lims.panel.valueholder.Panel();
        panel.setPanelName(labObject.getName());
        String description = labObject.getDescription();
        if(description == null || description.isEmpty()){
            description = labObject.getName();
        }
        panel.setDescription(description);
        panel.setSysUserId(labObject.getSysUserId());
        setActiveStatus(panel, labObject.getStatus());
        return panel;
    }

    private void setActiveStatus(Panel panel, String status) {
        if(status == null || status.isEmpty() )
            return;
        panel.setIsActive(status.equalsIgnoreCase("active") ? IActionConstants.YES : IActionConstants.NO);
    }

}

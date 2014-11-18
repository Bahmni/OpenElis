package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.utils.AuditingService;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSamplePanelDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSamplePanel;

import java.util.ArrayList;
import java.util.List;

public class TypeOfSamplePanelService {

    private final TypeOfSamplePanelDAOImpl typeOfSamplePanelDAO;
    private final AuditingService auditingService;

    public TypeOfSamplePanelService() {
        typeOfSamplePanelDAO = new TypeOfSamplePanelDAOImpl();
        auditingService = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl());
    }

    public void createOrUpdate(String typeOfSampleId, String panelId) {
        String sysUserId = auditingService.getSysUserId();
        TypeOfSamplePanel typeOfSamplePanel = createTypeOfSamplePanel(typeOfSampleId, panelId, sysUserId);
        TypeOfSamplePanel existingAssoc = typeOfSamplePanelDAO.getTypeOfSamplePanelForPanel(panelId);
        if (existingAssoc == null) {
            typeOfSamplePanelDAO.insertData(typeOfSamplePanel);
        } else {
            updateAssociation(sysUserId, typeOfSamplePanel, existingAssoc);
        }
    }

    private TypeOfSamplePanel createTypeOfSamplePanel(String typeOfSampleId, String panelId, String sysUserId) {
        TypeOfSamplePanel typeOfSamplePanel = new TypeOfSamplePanel();
        typeOfSamplePanel.setPanelId(panelId);
        typeOfSamplePanel.setTypeOfSampleId(typeOfSampleId);
        typeOfSamplePanel.setSysUserId(sysUserId);
        return typeOfSamplePanel;
    }

    private void updateAssociation(String sysUserId, TypeOfSamplePanel typeOfSamplePanel, TypeOfSamplePanel existingAssoc) {
        if (!existingAssoc.getTypeOfSampleId().equals(typeOfSamplePanel.getTypeOfSampleId())) {
            typeOfSamplePanelDAO.deleteData(new String[]{existingAssoc.getId()}, sysUserId);
            typeOfSamplePanelDAO.insertData(typeOfSamplePanel);
        }
    }

    public void deleteAllAssociations(String typeOfSampleId, String sysUserId) {
        List<TypeOfSamplePanel> testsForSample = typeOfSamplePanelDAO.getTypeOfSamplePanelsForSampleType(typeOfSampleId);
        ArrayList<String> sampleIds = new ArrayList<>();
        for (TypeOfSamplePanel typeOfSamplePanel : testsForSample) {
            sampleIds.add(typeOfSamplePanel.getId());
        }
        typeOfSamplePanelDAO.deleteData(sampleIds.toArray(new String[]{}), sysUserId);
    }
}

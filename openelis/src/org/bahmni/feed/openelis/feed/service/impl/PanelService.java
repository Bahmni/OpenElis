/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package org.bahmni.feed.openelis.feed.service.impl;


import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.MinimalResource;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataPanel;
import org.bahmni.feed.openelis.utils.AuditingService;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSException;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PanelService {

    public static final String CATEGORY_PANEL = "Panel";
    private static final String CATEGORY_TEST = "Test";
    private AuditingService auditingService;
    private PanelDAO panelDAO;
    private ExternalReferenceDao externalReferenceDao;
    private TestDAO testDAO;
    private PanelItemDAO panelItemDAO;

    public PanelService() {
        this.externalReferenceDao = new ExternalReferenceDaoImpl();
        this.auditingService = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl());
        this.panelDAO = new PanelDAOImpl();
        this.testDAO = new TestDAOImpl();
        this.panelItemDAO = new PanelItemDAOImpl();
    }

    public void createOrUpdate(ReferenceDataPanel referenceDataPanel) throws IOException, LIMSException {
        try {


            String sysUserId = auditingService.getSysUserId();
            ExternalReference data = externalReferenceDao.getData(referenceDataPanel.getId(), CATEGORY_PANEL);
            if (data == null) {
                Panel panel = new Panel();
                panel = populatePanel(panel, referenceDataPanel, sysUserId);
                panelDAO.insertData(panel);
                saveTestsForPanel(panel, referenceDataPanel, sysUserId);
                saveExternalReference(referenceDataPanel, panel);
            } else {
                Panel panel = panelDAO.getPanelById(String.valueOf(data.getItemId()));
                populatePanel(panel, referenceDataPanel, sysUserId);
                panelDAO.updateData(panel);
                saveTestsForPanel(panel, referenceDataPanel, sysUserId);
            }
        } catch (Exception e) {
            throw new LIMSException(String.format("Error while saving panel - %s", referenceDataPanel.getName()), e);
        }
    }

    private void saveExternalReference(ReferenceDataPanel referenceDataPanel, Panel panel) {
        ExternalReference data;
        data = new ExternalReference(Long.parseLong(panel.getId()), referenceDataPanel.getId(), CATEGORY_PANEL);
        externalReferenceDao.insertData(data);
    }

    private Panel populatePanel(Panel panel, ReferenceDataPanel referenceDataPanel, String sysUserId) throws IOException {
        panel.setPanelName(referenceDataPanel.getName());
        panel.setDescription(referenceDataPanel.getName());
        panel.setSysUserId(sysUserId);
        panel.setIsActive(referenceDataPanel.getIsActive() ? IActionConstants.YES : IActionConstants.NO);
        panel.setLastupdated(new Timestamp(new Date().getTime()));
        panel.setSortOrderInt(referenceDataPanel.getSortOrder());
        return panel;
    }

    private void saveTestsForPanel(Panel panel, ReferenceDataPanel referenceDataPanel, String sysUserId) throws LIMSException {
        deleteExistingPanels(panel, sysUserId);
        List<MinimalResource> tests = referenceDataPanel.getTests();
        for (int i = 0; i < tests.size(); i++) {
            MinimalResource referenceDataTest = tests.get(i);
            ExternalReference reference = externalReferenceDao.getData(referenceDataTest.getUuid(), CATEGORY_TEST);
            if (reference == null) {
                throw new LIMSException(String.format("%s test not found", referenceDataTest.getName()));
            }
            Test test = testDAO.getTestById(String.valueOf(reference.getItemId()));
            if (test == null) {
                throw new LIMSException(String.format("%s test not found", referenceDataTest.getName()));
            }
            PanelItem panelItem = createPanelItem(panel, test, String.valueOf(i + 1), sysUserId);
            panelItemDAO.insertData(panelItem);
        }
    }

    private void deleteExistingPanels(Panel panel, String sysUserId) {
        List<PanelItem> panelItems = panelItemDAO.getPanelItemByPanel(panel, false);
        for (PanelItem panelItem : panelItems) {
            panelItem.setSysUserId(sysUserId);
        }
        panelItemDAO.deleteData(panelItems);
    }

    public PanelItem createPanelItem(Panel panel, Test test, String sortOrder, String sysUserId) {
        PanelItem panelItem = new PanelItem();
        panelItem.setPanel(panel);
        panelItem.setPanelName(panel.getPanelName());
        panelItem.setTest(test);
        panelItem.setTestName(test.getTestName());
        panelItem.setSortOrder(sortOrder);
        panelItem.setSysUserId(sysUserId);
        return panelItem;
    }

    public Panel getPanel(MinimalResource panel) {
        return panelDAO.getPanelByName(panel.getName());
    }
}



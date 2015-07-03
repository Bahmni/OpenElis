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

package us.mn.state.health.lims.sample.util;

import org.apache.commons.validator.GenericValidator;
import org.bahmni.feed.openelis.feed.contract.SampleTestOrderCollection;
import org.bahmni.feed.openelis.feed.contract.TestOrder;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.sample.bean.SampleTestCollection;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisBuilder {
    private static final String DEFAULT_ANALYSIS_TYPE = "MANUAL";
    private PanelItemDAO panelItemDAO = new PanelItemDAOImpl();
    private PanelDAO panelDAO = new PanelDAOImpl();
    private Map<String, Panel> panelIdPanelMap;

    public Analysis populateAnalysis(String analysisRevision, SampleTestOrderCollection sampleTestCollection, TestOrder testOrder) {
        Date collectionDateTime = DateUtil.convertStringDateTimeToSqlDate(sampleTestCollection.collectionDate);
        return populateAnalysis(analysisRevision, sampleTestCollection.item, testOrder, sampleTestCollection.item.getSysUserId(), collectionDateTime);
    }

    public Analysis populateAnalysis(String analysisRevision, SampleTestCollection sampleTestCollection, Test test) {
        Date collectionDateTime = DateUtil.convertStringDateTimeToSqlDate(sampleTestCollection.collectionDate);
        return populateAnalysis(analysisRevision, sampleTestCollection.item, test, sampleTestCollection.item.getSysUserId(), collectionDateTime);
    }

    public Analysis populateAnalysis(String analysisRevision, SampleItem sampleItem, Test test, String sysUserId, Date collectionDate) {
        Panel panel = getPanelForTest(test);

        Analysis analysis = new Analysis();
        analysis.setTest(test);
        analysis.setPanel(panel);
        analysis.setIsReportable(test.getIsReportable());
        analysis.setAnalysisType(DEFAULT_ANALYSIS_TYPE);
        analysis.setSampleItem(sampleItem);
        analysis.setSysUserId(sysUserId);
        analysis.setRevision(analysisRevision);
        analysis.setStartedDate(collectionDate);
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.NotTested));
        analysis.setTestSection(test.getTestSection());

        return analysis;
    }
        public Analysis populateAnalysis(String analysisRevision, SampleItem sampleItem, TestOrder testOrder, String sysUserId, Date collectionDate) {
        Test test = testOrder.getTest();
            Analysis analysis = populateAnalysis(analysisRevision, sampleItem, test, sysUserId, collectionDate);
            analysis.setComment(testOrder.getComment());
            return analysis;
    }

    public void augmentPanelIdToPanelMap(String panelIDs) {
        if( panelIdPanelMap == null){
            panelIdPanelMap = new HashMap<String, Panel>();
        }

        if(panelIDs != null){
            String[] ids = panelIDs.split(",");
            for( String id : ids){
                if( !GenericValidator.isBlankOrNull(id)){
                    panelIdPanelMap.put(id, panelDAO.getPanelById(id));
                }
            }
        }
    }

    public Panel getPanelForTest(Test test) {
        List<PanelItem> panelItems = panelItemDAO.getPanelItemByTest(test);

        for( PanelItem panelItem : panelItems){
            Panel panel = panelIdPanelMap.get(panelItem.getPanel().getId());
            if( panel != null){
                return panel;
            }
        }

        return null;
    }
}

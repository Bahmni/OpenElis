package us.mn.state.health.lims.sample.util;

import org.apache.commons.validator.GenericValidator;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.sample.bean.SampleTestCollection;
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

    public Analysis populateAnalysis(String analysisRevision, SampleTestCollection sampleTestCollection, Test test) {
        Date collectionDateTime = DateUtil.convertStringDateTimeToSqlDate(sampleTestCollection.collectionDate);

        Panel panel = getPanelForTest(test);

        Analysis analysis = new Analysis();
        analysis.setTest(test);
        analysis.setPanel(panel);
        analysis.setIsReportable(test.getIsReportable());
        analysis.setAnalysisType(DEFAULT_ANALYSIS_TYPE);
        analysis.setSampleItem(sampleTestCollection.item);
        analysis.setSysUserId(sampleTestCollection.item.getSysUserId());
        analysis.setRevision(analysisRevision);
        analysis.setStartedDate(collectionDateTime);
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.NotTested));
        analysis.setTestSection(test.getTestSection());
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

    private Panel getPanelForTest(Test test) {
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
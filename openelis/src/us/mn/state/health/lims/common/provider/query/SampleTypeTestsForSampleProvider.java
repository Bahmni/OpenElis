package us.mn.state.health.lims.common.provider.query;

import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.XMLUtil;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SampleTypeTestsForSampleProvider extends BaseQueryProvider{
    private SampleItemDAO sampleItemDao = new SampleItemDAOImpl();
    private AnalysisDAO analysisDAO = new AnalysisDAOImpl();
    
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sampleId = request.getParameter("sampleId");
        StringBuilder xml = new StringBuilder();

        String result = createSearchResultXML(sampleId, xml);

        ajaxServlet.sendData(xml.toString(), result, request, response);
    }

    private String createSearchResultXML(String sampleId, StringBuilder xml) {
        String success = VALID;
        Set<Integer> includedSampleStatusList = new HashSet<Integer>();
        includedSampleStatusList.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered)));

        Set excludedAnalysisStatusList = new HashSet<Integer>();
        excludedAnalysisStatusList.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Canceled)));

        List<SampleItem> sampleItems = sampleItemDao.getSampleItemsBySampleIdAndStatus(sampleId, includedSampleStatusList);

        xml.append("<samples>");
        for (SampleItem sampleItem : sampleItems) {
            xml.append("<sample>");
            XMLUtil.appendKeyValue("sampleType", sampleItem.getTypeOfSampleId(), xml);
            List<Analysis> analysesBySampleItems = analysisDAO.getAnalysesBySampleItemsExcludingByStatusIds(sampleItem,excludedAnalysisStatusList);
            Set<String> selectedPanelList = new HashSet<>();
            for (Analysis analysis : analysesBySampleItems) {
                XMLUtil.appendKeyValue("test", analysis.getTest().getId(), xml);
                if(analysis.getPanel() != null) {
                    selectedPanelList.add(analysis.getPanel().getId());
                }
            }
            for (String panel : selectedPanelList) {
                XMLUtil.appendKeyValue("panel", panel, xml);
            }

            xml.append("</sample>");
        }
        xml.append("</samples>");

        return success;

    }

}

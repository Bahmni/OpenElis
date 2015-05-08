package us.mn.state.health.lims.common.provider.query;

import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.XMLUtil;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        List<SampleItem> sampleItems = sampleItemDao.getSampleItemsBySampleId(sampleId);

        xml.append("<samples>");
        for (SampleItem sampleItem : sampleItems) {
            xml.append("<sample>");
            XMLUtil.appendKeyValue("sampleType", sampleItem.getTypeOfSampleId(), xml);
            List<Analysis> analysesBySampleItems = analysisDAO.getAnalysesBySampleItem(sampleItem);
            for (Analysis analysis : analysesBySampleItems) {
                XMLUtil.appendKeyValue("test", analysis.getTest().getId(), xml);
            }
            xml.append("</sample>");
        }
        xml.append("</samples>");


        return success;

    }
}

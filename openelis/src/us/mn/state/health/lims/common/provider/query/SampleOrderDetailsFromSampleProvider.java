package us.mn.state.health.lims.common.provider.query;

import us.mn.state.health.lims.common.util.XMLUtil;
import us.mn.state.health.lims.requester.dao.SampleRequesterDAO;
import us.mn.state.health.lims.requester.daoimpl.SampleRequesterDAOImpl;
import us.mn.state.health.lims.requester.valueholder.SampleRequester;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SampleOrderDetailsFromSampleProvider extends BaseQueryProvider{
    private SampleDAO sampleDao = new SampleDAOImpl();
    private SampleRequesterDAO sampleRequesterDAO = new SampleRequesterDAOImpl();

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sampleId = request.getParameter("sampleId");
        StringBuilder xml = new StringBuilder();

        String result = createSearchResultXML(sampleId, xml);

        ajaxServlet.sendData(xml.toString(), result, request, response);
    }

    private String createSearchResultXML(String sampleId, StringBuilder xml) {
        String success = VALID;
        Sample sample = sampleDao.getSampleByID(sampleId);
        List<SampleRequester> requesters = sampleRequesterDAO.getRequestersForSampleId(sampleId);

        xml.append("<sampleDetails>");
        XMLUtil.appendKeyValue("sampleSource", sample.getSampleSource().getId(), xml);
        XMLUtil.appendKeyValue("sampleReceivedDateForDisplay", sample.getReceivedDateForDisplay(), xml);
        if(requesters.size() >0) {
            XMLUtil.appendKeyValue("sampleRequester", String.valueOf(requesters.get(0).getRequesterId()), xml);
        }
        xml.append("</sampleDetails>");


        return success;

    }
}

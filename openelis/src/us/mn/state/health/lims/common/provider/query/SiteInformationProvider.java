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
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.common.provider.query;

import us.mn.state.health.lims.common.util.XMLUtil;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SiteInformationProvider extends BaseQueryProvider {

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        StringBuilder xml = new StringBuilder();
        String result = VALID;
        SiteInformationDAO siteInformationDAO = new SiteInformationDAOImpl();
        SiteInformation defaultSampleSource = siteInformationDAO.getSiteInformationByName("defaultSampleSource");
        if (defaultSampleSource == null) {
            xml.append("empty");
        } else {
            createReturnXML(defaultSampleSource, xml);
        }

        ajaxServlet.sendData(xml.toString(), result, request, response);

    }


    private void createReturnXML(SiteInformation siteInformation, StringBuilder xml) {
        SampleSourceDAO sampleSourceDAO = new SampleSourceDAOImpl();
        SampleSource sampleSource = sampleSourceDAO.getByName(siteInformation.getValue(), true);
        if(sampleSource != null){
            XMLUtil.appendKeyValue("defaultSampleSource", siteInformation.getValue(), xml);
            XMLUtil.appendKeyValue("defaultSampleSourceID", sampleSource.getId(), xml);
        }

    }

}

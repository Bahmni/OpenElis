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

package us.mn.state.health.lims.healthcenter.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.codehaus.jackson.map.ObjectMapper;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HealthCenterListAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HealthCenterDAOImpl healthCenterDAO = new HealthCenterDAOImpl();
        List<HealthCenter> healthCenters = healthCenterDAO.getAll();

        List<String> activeHealthCenters = new ArrayList();
        for (HealthCenter healthCenter : healthCenters) {
            if (healthCenter.isActive()) {
                activeHealthCenters.add(healthCenter.getName());
            }
        }
        response.setContentType("application/json");
        ObjectMapperRepository.objectMapper.writeValue(response.getWriter(), activeHealthCenters);

        return null;
    }

    @Override
    protected String getPageTitleKey() {
        return "healthcenter.add.title";
    }

    @Override
    protected String getPageSubtitleKey() {
        return "healthcenter.add.title";
    }
}

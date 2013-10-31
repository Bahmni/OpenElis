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
import org.apache.struts.action.DynaActionForm;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HealthCenterDeactivateAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<HealthCenter> healthCenters = (List<HealthCenter>) ((DynaActionForm)form).get("healthCenters");
        String[] deactivatedHealthCenterNames = request.getParameterValues("hcname");
        deactivateHealthCenters(getDeactivatedHealthCenters(healthCenters, deactivatedHealthCenterNames));

        return mapping.findForward("list");
    }

    @Override
    protected String getPageTitleKey() {
        return null;
    }

    @Override
    protected String getPageSubtitleKey() {
        return null;
    }

    private List<HealthCenter> getDeactivatedHealthCenters (List<HealthCenter> healthCenters,String[] deactivatedHealthCenterNames){
        List<HealthCenter> deactivatedHealthCenters = new ArrayList<>();
        for(String centerName:deactivatedHealthCenterNames) {
            for (HealthCenter healthCenter : healthCenters) {
                if (healthCenter.getName().equals(centerName)) {
                    deactivatedHealthCenters.add(healthCenter);
                }
            }
        }
        return deactivatedHealthCenters;
    }

    private void deactivateHealthCenters(List<HealthCenter> healthCenters){
        HealthCenterDAO healthCenterDAO = new HealthCenterDAOImpl();
        for(HealthCenter healthCenter:healthCenters){
            healthCenter.setActive(false);
            healthCenterDAO.update(healthCenter);
        }
    }
}

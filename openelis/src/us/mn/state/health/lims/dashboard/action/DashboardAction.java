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

package us.mn.state.health.lims.dashboard.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.json.simple.JSONObject;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.dashboard.dao.OrderListDAO;
import us.mn.state.health.lims.dashboard.daoimpl.OrderListDAOImpl;
import us.mn.state.health.lims.dashboard.valueholder.TodayStat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DashboardAction extends BaseAction {
    private OrderListDAO orderListDAO = new OrderListDAOImpl();

    public DashboardAction() {
    }

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaForm = (DynaActionForm) form;

        String escapedPendingOrderListJson = asJson(orderListDAO.getAllInProgress());
        String escapedCompletedOrderListJson = asJson(orderListDAO.getAllCompletedBefore24Hours());

        dynaForm.set("inProgressOrderList", escapedPendingOrderListJson);
        dynaForm.set("completedOrderList", escapedCompletedOrderListJson);
        dynaForm.set("todayStats", getTodaysStats());

        return mapping.findForward("success");
    }

    private String getTodaysStats() throws IOException {
        TodayStat todayStats = orderListDAO.getTodayStats();
        String statsAsJson = ObjectMapperRepository.objectMapper.writeValueAsString(todayStats);
        return JSONObject.escape(statsAsJson);
    }

    @Override
    protected String getPageTitleKey() {
        return "Dashboard";
    }

    @Override
    protected String getPageSubtitleKey() {
        return "Dashboard";
    }

    private String asJson(List objects) throws IOException {
        String listJson = ObjectMapperRepository.objectMapper.writeValueAsString(objects);
        return JSONObject.escape(listJson);
    }
}

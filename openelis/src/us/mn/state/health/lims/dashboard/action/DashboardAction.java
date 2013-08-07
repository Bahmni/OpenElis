package us.mn.state.health.lims.dashboard.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.codehaus.jackson.JsonGenerator;
import org.json.simple.JSONObject;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.dashboard.daoimpl.OrderListDAOImpl;
import us.mn.state.health.lims.dashboard.valueholder.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DashboardAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Order> allInProgress = new OrderListDAOImpl().getAllInProgress();
        DynaActionForm dynaForm = (DynaActionForm) form;
        String orderListJson = ObjectMapperRepository.objectMapper.writeValueAsString(allInProgress);
        String escapedOrderListJson = JSONObject.escape(orderListJson);
        dynaForm.set("inProgressOrderList", escapedOrderListJson);

        return mapping.findForward("success");
    }

    @Override
    protected String getPageTitleKey() {
        return "Dashboard";
    }

    @Override
    protected String getPageSubtitleKey() {
        return "Dashboard";
    }
}

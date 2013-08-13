package us.mn.state.health.lims.dashboard.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.json.simple.JSONObject;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.dashboard.daoimpl.OrderListDAOImpl;
import us.mn.state.health.lims.dashboard.valueholder.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DashboardAction extends BaseAction {


    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaForm = (DynaActionForm) form;

        OrderListDAOImpl orderListDAO = new OrderListDAOImpl();

        String escapedPendingOrderListJson = getAllOrdersJson(orderListDAO.getAllInProgress());

        String escapedCompletedOrderListJson = getAllOrdersJson(orderListDAO.getAllCompletedBefore24Hours());

        dynaForm.set("inProgressOrderList", escapedPendingOrderListJson);
        dynaForm.set("completedOrderList", escapedCompletedOrderListJson);

        return mapping.findForward("success");
    }

    private String getAllOrdersJson(List<Order> orders) throws IOException {
        String orderListJson = ObjectMapperRepository.objectMapper.writeValueAsString(orders);
        return JSONObject.escape(orderListJson);
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

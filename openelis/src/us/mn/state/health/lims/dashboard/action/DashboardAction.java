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
import java.util.List;

public class DashboardAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaForm = (DynaActionForm) form;

        OrderListDAOImpl orderListDAO = new OrderListDAOImpl();

        List<Order> allInProgress = orderListDAO.getAllInProgress();
        String pendingOrderListJson = ObjectMapperRepository.objectMapper.writeValueAsString(allInProgress);
        String escapedPendingOrderListJson = JSONObject.escape(pendingOrderListJson);

        List<Order> allCompleted = orderListDAO.getAllCompletedBefore24Hours();
        String completedOrderListJson = ObjectMapperRepository.objectMapper.writeValueAsString(allCompleted);
        String escapedCompletedOrderListJson = JSONObject.escape(completedOrderListJson);

        dynaForm.set("inProgressOrderList", escapedPendingOrderListJson);
        dynaForm.set("completedOrderList", escapedCompletedOrderListJson);

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

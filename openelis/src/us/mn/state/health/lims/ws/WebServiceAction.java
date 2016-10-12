package us.mn.state.health.lims.ws;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.mn.state.health.lims.login.action.LoginValidateAction;
import us.mn.state.health.lims.login.dao.UserModuleDAO;
import us.mn.state.health.lims.login.daoimpl.UserModuleDAOImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static us.mn.state.health.lims.common.action.IActionConstants.FWD_SUCCESS;

public class WebServiceAction extends Action {

    private final Logger logger = LoggerFactory.getLogger(WebServiceAction.class);
    private UserModuleDAO userModuleDAO = new UserModuleDAOImpl();
    private LoginValidateAction loginValidateAction = new LoginValidateAction();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (!userModuleDAO.isSessionExpired(request) || authorized(mapping, form, request, response)) {
            return performAction(mapping, form, request, response);
        }
        return invalidAccessAttempt(form, request, response);
    }

    private boolean authorized(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        ActionForward actionForward = loginValidateAction.execute(mapping, form, request, response);
        return actionForward != null && FWD_SUCCESS.equals(actionForward.getName());
    }

    private ActionForward invalidAccessAttempt(ActionForm form, HttpServletRequest request,
                                               HttpServletResponse response) {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        logger.warn(String.format("Unauthorized atomfeed access attempt from %s using username %s", request
                .getRemoteAddr(), dynaActionForm.get("loginName")));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }

    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        return null;
    }
}

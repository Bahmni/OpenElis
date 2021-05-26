package us.mn.state.health.lims.login.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.log.LogEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutPageAction extends LoginBaseAction {

	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
        LogEvent.logWarn("LogoutPageAction", "performAction", request.getSession().getId());
        if (alreadyLoggedIn(request)) {
            request.getSession().invalidate();
            request.getSession(true);
        }
        return mapping.findForward("success");
	}

	protected String getPageTitleKey() {
		return "login.title";
	}

	protected String getPageSubtitleKey() {
		return "login.subTitle";
	}
}

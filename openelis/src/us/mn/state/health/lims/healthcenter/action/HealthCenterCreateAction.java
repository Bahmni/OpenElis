package us.mn.state.health.lims.healthcenter.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HealthCenterCreateAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("success"); //To change body of implemented methods use File | Settings | File Templates.
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

package us.mn.state.health.lims.upload;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadForm extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(FWD_SUCCESS);
    }

    @Override
    protected String getPageTitleKey() {
        return "action.upload";
    }

    @Override
    protected String getPageSubtitleKey() {
        return "action.upload";
    }
}

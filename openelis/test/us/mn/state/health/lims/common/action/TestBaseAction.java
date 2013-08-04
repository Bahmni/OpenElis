package us.mn.state.health.lims.common.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.junit.Ignore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Ignore
public class TestBaseAction extends BaseAction {
    public TestBaseAction() {
    }

    public TestBaseAction(String currentUserId, ActionServlet actionServlet) {
        this.servlet = actionServlet;
        this.currentUserId = currentUserId;
    }

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }

    @Override
    protected String getPageTitleKey() {
        return null;
    }

    @Override
    protected String getPageSubtitleKey() {
        return null;
    }
}
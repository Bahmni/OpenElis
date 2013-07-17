package us.mn.state.health.lims.healthcenter.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HealthCenterMenuAction  extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HealthCenterDAOImpl healthCenterDAO = new HealthCenterDAOImpl();
        List menuList = null;

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.initialize(mapping);
        PropertyUtils.setProperty(dynaForm, "healthCenters", healthCenterDAO.getAll());

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
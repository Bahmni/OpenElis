package us.mn.state.health.lims.healthcenter.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Transaction;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HealthCenterCreateAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(request.getMethod().equalsIgnoreCase("GET")) {
            return performGet(mapping, request);
        }
        return performPost(mapping, (BaseActionForm) form, request);
    }

    private ActionForward performGet(ActionMapping mapping, HttpServletRequest request) {
        request.setAttribute("currentAction", "addNew");
        return mapping.findForward("success");
    }

    private ActionForward performPost(ActionMapping mapping, BaseActionForm form, HttpServletRequest request) {
        createHealthCenter(form.getString("name"), form.getString("description"));
        return mapping.findForward("list");
    }

    private void createHealthCenter(String name,String description) {
        Transaction tx = HibernateUtil.getSession().beginTransaction();
        HealthCenterDAO healthCenterDAO = new HealthCenterDAOImpl();
        healthCenterDAO.add(new HealthCenter(name,description));
        tx.commit();
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

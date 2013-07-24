package us.mn.state.health.lims.healthcenter.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.hibernate.Transaction;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HealthCenterDeactivateAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<HealthCenter> healthCenters = (List<HealthCenter>) ((DynaActionForm)form).get("healthCenters");
        String[] deactivatedHealthCenterNames = request.getParameterValues("hcname");
        deactivateHealthCenters(getDeactivatedHealthCenters(healthCenters, deactivatedHealthCenterNames));

        return mapping.findForward("list");
    }

    @Override
    protected String getPageTitleKey() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getPageSubtitleKey() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private List<HealthCenter> getDeactivatedHealthCenters (List<HealthCenter> healthCenters,String[] deactivatedHealthCenterNames){
        List<HealthCenter> deactivatedHealthCenters = new ArrayList<>();
        for(String centerName:deactivatedHealthCenterNames) {
            for (HealthCenter healthCenter : healthCenters) {
                if (healthCenter.getName().equals(centerName)) {
                    deactivatedHealthCenters.add(healthCenter);
                }
            }
        }
        return deactivatedHealthCenters;
    }

    private void deactivateHealthCenters(List<HealthCenter> healthCenters){
        HealthCenterDAO healthCenterDAO = new HealthCenterDAOImpl();
        Transaction tx = HibernateUtil.getSession().beginTransaction();
        for(HealthCenter healthCenter:healthCenters){
            healthCenter.setActive(false);
            healthCenterDAO.update(healthCenter);
        }
        tx.commit();
    }
}

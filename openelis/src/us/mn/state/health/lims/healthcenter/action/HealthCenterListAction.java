package us.mn.state.health.lims.healthcenter.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HealthCenterListAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Boolean allHealthCenter = Boolean.valueOf(request.getParameter("allHealthCenter"));

        HealthCenterDAOImpl healthCenterDAO = new HealthCenterDAOImpl();
        List<HealthCenter> healthCenters = healthCenterDAO.getAllActive();
        List<String> activeHealthCenters = new ArrayList();
        if(allHealthCenter){
            for (HealthCenter healthCenter : healthCenters) {
                activeHealthCenters.add(healthCenter.getName());
            }
        }
        else{
            for (HealthCenter healthCenter : healthCenters) {
                if(healthCenter.isAllowPatientCreation()){
                    activeHealthCenters.add(healthCenter.getName());
                }
            }
        }

        response.setContentType("application/json");
        ObjectMapperRepository.objectMapper.writeValue(response.getWriter(), activeHealthCenters);

        return null;
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

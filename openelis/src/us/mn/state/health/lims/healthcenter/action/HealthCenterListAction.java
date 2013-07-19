package us.mn.state.health.lims.healthcenter.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HealthCenterListAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HealthCenterDAOImpl healthCenterDAO = new HealthCenterDAOImpl();
        List<HealthCenter> healthCenters = healthCenterDAO.getAll();

        List<String> activeHealthCenters = new ArrayList();
        for (HealthCenter healthCenter : healthCenters) {
            if(healthCenter.isActive()){
                activeHealthCenters.add(healthCenter.getName());
            }
        }
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), activeHealthCenters);

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
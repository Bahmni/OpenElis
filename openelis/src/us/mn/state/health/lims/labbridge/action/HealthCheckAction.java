package us.mn.state.health.lims.labbridge.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public class HealthCheckAction extends Action {
    private final String APPLICATION_JSON = "application/json";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> payload = new LinkedHashMap<String, Object>();
        payload.put("status", "success");
        payload.put("message", "Health check successful");
        response.setContentType(APPLICATION_JSON);
        ObjectMapperRepository.objectMapper.writeValue(response.getWriter(), payload);
        return null;
    }
}



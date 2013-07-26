package us.mn.state.health.lims.patient.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;
import org.bahmni.openelis.domain.CompletePatientDetails;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PatientRetrieveAction extends Action {

    private BahmniPatientService bahmniPatientService;

    public PatientRetrieveAction() {
        this(new BahmniPatientService());
    }

    public PatientRetrieveAction(BahmniPatientService bahmniPatientService) {
        this.bahmniPatientService = bahmniPatientService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String patientId = getPathVariable(request.getRequestURI());
        if (patientId == null || patientId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        CompletePatientDetails completePatientDetails = bahmniPatientService.getCompletePatientDetails(patientId);
        if (completePatientDetails == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), completePatientDetails);

        return null;
    }

    private String getPathVariable(String request) {
        request = trimSpacesAndTrailingSlash(request);
        String[] urlParts = request.split("/");
        return urlParts[urlParts.length - 1];
    }

    private String trimSpacesAndTrailingSlash(String request) {
        request = request.trim();
        if (request.endsWith("/")) request = request.substring(0, request.length() -2);
        return request;
    }
}

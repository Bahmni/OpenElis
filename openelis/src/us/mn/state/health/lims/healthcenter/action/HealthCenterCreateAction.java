package us.mn.state.health.lims.healthcenter.action;

import org.apache.struts.action.*;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;

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
        setGlobalMessage(new ActionMessage("healthcenter.add.title",null,null),request);
        return mapping.findForward("success");
    }

    private ActionForward performPost(ActionMapping mapping, BaseActionForm form, HttpServletRequest request) {
        try {
            if(form.getString("name") == null || form.getString("name").isEmpty()){
                return returnError("errors.emptyField"," health center name",mapping,request);
            }
            createHealthCenter(form.getString("name"), form.getString("description"));
        }catch(LIMSRuntimeException ex){
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
            return returnError("errors.HealthCenter.DuplicateRecord","Health Center",mapping,request);
        }
        return mapping.findForward("list");
    }

    private  ActionForward returnError(String message,String placeHolderValue,ActionMapping mapping, HttpServletRequest request){
        ActionMessages errorMessages = new ActionMessages();
        ActionError error = new ActionError(message,placeHolderValue,null);
        errorMessages.add(ActionMessages.GLOBAL_MESSAGE, error);
        saveErrors(request, errorMessages);
        request.setAttribute("currentAction","addNew");
        return mapping.findForward("fail");
    }

    private void setGlobalMessage(ActionMessage message,HttpServletRequest request){
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, message);
        saveMessages(request,messages);
    }

    private void createHealthCenter(String name,String description) {
        HealthCenterDAO healthCenterDAO = new HealthCenterDAOImpl();
        healthCenterDAO.add(new HealthCenter(name,description));
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

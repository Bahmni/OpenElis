package us.mn.state.health.lims.typeofteststatus.action;
/**
 * @author Buvaneswari Arun
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.resources.ResourceLocator;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.login.valueholder.UserSessionData;
import us.mn.state.health.lims.typeoftestresult.dao.TypeOfTestResultDAO;
import us.mn.state.health.lims.typeoftestresult.daoimpl.TypeOfTestResultDAOImpl;
import us.mn.state.health.lims.typeoftestresult.valueholder.TypeOfTestResult;
import us.mn.state.health.lims.typeofteststatus.dao.TypeOfTestStatusDAO;
import us.mn.state.health.lims.typeofteststatus.daoimpl.TypeOfTestStatusDAOImpl;
import us.mn.state.health.lims.typeofteststatus.valueholder.AllowedTestStatusTypes;
import us.mn.state.health.lims.typeofteststatus.valueholder.TypeOfTestStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TypeOfTestStatusUpdateAction extends BaseAction {

    private boolean isNew = false;

    protected ActionForward performAction(ActionMapping mapping,
                                          ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        // The first job is to determine if we are coming to this action with an
        // ID parameter in the request. If there is no parameter, we are
        // creating a new TypeOfResultStatus.
        // If there is a parameter present, we should bring up an existing
        // TypeOfResultStatus to edit.
        String forward = FWD_SUCCESS;
        request.setAttribute(ALLOW_EDITS_KEY, "true");
        request.setAttribute(PREVIOUS_DISABLED, "false");
        request.setAttribute(NEXT_DISABLED, "false");

        String id = request.getParameter(ID);
        if (StringUtil.isNullorNill(id) || "0".equals(id)) {
            isNew = true;
        } else {
            isNew = false;
        }

        BaseActionForm dynaForm = (BaseActionForm) form;

        // server-side validation (validation.xml)
        ActionMessages errors = dynaForm.validate(mapping, request);
        if (errors != null && errors.size() > 0) {
            saveErrors(request, errors);
            // since we forward to jsp - not Action we don't need to repopulate
            // the lists here
            return mapping.findForward(FWD_FAIL);
        }

        String start = (String) request.getParameter("startingRecNo");
        String direction = (String) request.getParameter("direction");

        TypeOfTestStatus typeOfTestStatus = new TypeOfTestStatus();
        //get sysUserId from login module
        UserSessionData usd = (UserSessionData)request.getSession().getAttribute(USER_SESSION_DATA);
        String sysUserId = String.valueOf(usd.getSystemUserId());
        typeOfTestStatus.setSysUserId(sysUserId);

        // populate valueholder from form
        PropertyUtils.copyProperties(typeOfTestStatus, dynaForm);

        try {

            TypeOfTestStatusDAO typeOfTestStatusDAO = new TypeOfTestStatusDAOImpl();

            if (!isNew) {
                // UPDATE
                typeOfTestStatusDAO.updateData(typeOfTestStatus);
            } else {
                // INSERT
                typeOfTestStatusDAO.insertData(typeOfTestStatus);
            }
        } catch (LIMSRuntimeException lre) {
            //bugzilla 2154
            LogEvent.logError("TypeOfTestStatusUpdateAction","performAction()",lre.toString());
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
            errors = new ActionMessages();
            java.util.Locale locale = (java.util.Locale) request.getSession()
                    .getAttribute("org.apache.struts.action.LOCALE");
            ActionError error = null;
            if (lre.getException() instanceof org.hibernate.StaleObjectStateException) {
                error = new ActionError("errors.OptimisticLockException", null,
                        null);
            } else {
                // bugzilla 1482
                if (lre.getException() instanceof LIMSDuplicateRecordException) {
                    String messageKey = "typeofteststatus.name";
                    String msg = ResourceLocator.getInstance()
                            .getMessageResources().getMessage(locale,
                                    messageKey);
                    error = new ActionError("errors.DuplicateRecord",
                            msg, null);

                } else {
                    error = new ActionError("errors.UpdateException", null,
                            null);
                }
            }
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(request, errors);
            request.setAttribute(Globals.ERROR_KEY, errors);
            //bugzilla 1485: allow change and try updating again (enable save button)
            //request.setAttribute(IActionConstants.ALLOW_EDITS_KEY, "false");
            request.setAttribute(PREVIOUS_DISABLED, "true");
            request.setAttribute(NEXT_DISABLED, "true");
            forward = FWD_FAIL;
        }
        request.setAttribute("allowedStatusTypes", AllowedTestStatusTypes.getAllAllowedTestStatusTypes());
        if (forward.equals(FWD_FAIL))
            return mapping.findForward(forward);

        // initialize the form
        dynaForm.initialize(mapping);
        // repopulate the form from valueholder
        PropertyUtils.copyProperties(dynaForm, typeOfTestStatus);

        if ("true".equalsIgnoreCase(request.getParameter("close"))) {
            forward = FWD_CLOSE;
        }

        if (typeOfTestStatus.getId() != null && !typeOfTestStatus.getId().equals("0")) {
            request.setAttribute(ID, typeOfTestStatus.getId());

        }

        //bugzilla 1400
        if (isNew) forward = FWD_SUCCESS_INSERT;
        //bugzilla 1467 added direction for redirect to NextPreviousAction
        return getForward(mapping.findForward(forward), typeOfTestStatus.getId(), start, direction);

    }

    protected String getPageTitleKey() {
        if (isNew) {
            return "typeofteststatus.add.title ";
        } else {
            return "typeofteststatus.edit.title";
        }
    }

    protected String getPageSubtitleKey() {
        if (isNew) {
            return "typeofteststatus.add.subtitle";
        } else {
            return "typeofteststatus.edit.subtitle";
        }
    }
}

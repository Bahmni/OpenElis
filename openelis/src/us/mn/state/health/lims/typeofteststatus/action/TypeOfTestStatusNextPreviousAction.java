package us.mn.state.health.lims.typeofteststatus.action;
/**
 * @author Buvaneswari Arun
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.typeofteststatus.dao.TypeOfTestStatusDAO;
import us.mn.state.health.lims.typeofteststatus.daoimpl.TypeOfTestStatusDAOImpl;
import us.mn.state.health.lims.typeofteststatus.valueholder.TypeOfTestStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class TypeOfTestStatusNextPreviousAction extends BaseAction {

    private boolean isNew = false;

    protected ActionForward performAction(ActionMapping mapping,
                                          ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        // The first job is to determine if we are coming to this action with an
        // ID parameter in the request. If there is no parameter, we are
        // creating a new Analyte.
        // If there is a parameter present, we should bring up an existing
        // Analyte to edit.
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


        String start = (String) request.getParameter("startingRecNo");
        String direction = (String) request.getParameter("direction");

        TypeOfTestStatus typeOfTestStatus = new TypeOfTestStatus();

        typeOfTestStatus.setId(id);
        try {

            TypeOfTestStatusDAO typeOfTestStatusDAO = new TypeOfTestStatusDAOImpl();
            //retrieve analyte by id since the name may have changed
            typeOfTestStatusDAO.getData(typeOfTestStatus);

            if (FWD_NEXT.equals(direction)) {
                //bugzilla 1427 pass in name not id
                List typeOfTestStatuses = typeOfTestStatusDAO.getNextTypeOfTestStatusRecord(typeOfTestStatus.getStatusName());
                if (typeOfTestStatuses != null && typeOfTestStatuses.size() > 0) {
                    typeOfTestStatus = (TypeOfTestStatus) typeOfTestStatuses.get(0);
                    typeOfTestStatusDAO.getData(typeOfTestStatus);
                    if (typeOfTestStatuses.size() < 2) {
                        // disable next button
                        request.setAttribute(NEXT_DISABLED, "true");
                    }
                    id = typeOfTestStatus.getId();
                } else {
                    // just disable next button
                    request.setAttribute(NEXT_DISABLED, "true");
                }
            }

            if (FWD_PREVIOUS.equals(direction)) {
                //bugzilla 1427 pass in name not id
                List typeOfResultStatuses = typeOfTestStatusDAO.getPreviousTypeOfTestStatusRecord(typeOfTestStatus.getStatusName());
                if (typeOfResultStatuses != null && typeOfResultStatuses.size() > 0) {
                    typeOfTestStatus = (TypeOfTestStatus) typeOfResultStatuses.get(0);
                    typeOfTestStatusDAO.getData(typeOfTestStatus);
                    if (typeOfResultStatuses.size() < 2) {
                        // disable previous button
                        request.setAttribute(PREVIOUS_DISABLED, "true");
                    }
                    id = typeOfTestStatus.getId();
                } else {
                    // just disable next button
                    request.setAttribute(PREVIOUS_DISABLED, "true");
                }
            }

        } catch (LIMSRuntimeException lre) {
            //bugzilla 2154
            LogEvent.logError("TypeOfTestResultNextPreviousAction","performAction()",lre.toString());
            request.setAttribute(ALLOW_EDITS_KEY, "false");
            // disable previous and next
            request.setAttribute(PREVIOUS_DISABLED, "true");
            request.setAttribute(NEXT_DISABLED, "true");
            forward = FWD_FAIL;
        }
        if (forward.equals(FWD_FAIL))
            return mapping.findForward(forward);


        if (typeOfTestStatus.getId() != null && !typeOfTestStatus.getId().equals("0")) {
            request.setAttribute(ID, typeOfTestStatus.getId());

        }

        return getForward(mapping.findForward(forward), id, start);

    }

    protected String getPageTitleKey() {
        return null;
    }

    protected String getPageSubtitleKey() {
        return null;
    }

}


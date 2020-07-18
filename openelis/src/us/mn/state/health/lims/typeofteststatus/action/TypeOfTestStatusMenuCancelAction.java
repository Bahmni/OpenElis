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
import org.apache.struts.action.DynaActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TypeOfTestStatusMenuCancelAction extends TypeOfTestStatusMenuAction {

    protected ActionForward performAction(ActionMapping mapping,
                                          ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        DynaActionForm dynaForm = (DynaActionForm) form;

        request.setAttribute("menuDefinition", DEFAULT);

        return mapping.findForward(FWD_CLOSE);

    }

}

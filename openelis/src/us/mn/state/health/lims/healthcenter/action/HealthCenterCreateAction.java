/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package us.mn.state.health.lims.healthcenter.action;

import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.exception.LIMSInvalidSTNumberException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.validator.ActionError;
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
        setGlobalMessage(new ActionMessage("healthcenter.add.title",null,null),request);
        return mapping.findForward("success");
    }

    private ActionForward performPost(ActionMapping mapping, BaseActionForm form, HttpServletRequest request) {
        Transaction tx = HibernateUtil.getSession().beginTransaction();
        try {
            if(form.getString("name") == null || form.getString("name").isEmpty()){
                return returnError("errors.emptyField"," health center name",mapping,request);
            }
            createHealthCenter(form.getString("name"), form.getString("description"));
            tx.commit();
        }catch(LIMSRuntimeException ex){
            tx.rollback();
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
        HibernateUtil.closeSession();
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

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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.*;
import org.hibernate.Transaction;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.daoimpl.HealthCenterDAOImpl;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public class HealthCenterUpdateAction extends BaseAction {

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(request.getMethod().equalsIgnoreCase("GET")) {
            return performGet(mapping, form, request);
        }
        return performPost(mapping, (BaseActionForm) form, request);
    }

    private ActionForward performGet(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        request.setAttribute("currentAction", "edit");
        setGlobalMessage(new ActionMessage("healthcenter.edit.title",null,null),request);
        HealthCenterDAO healthCenterDAO = new HealthCenterDAOImpl();
        HealthCenter healthCenter = healthCenterDAO.getByName(request.getParameter("name"));
        PropertyUtils.copyProperties(form, healthCenter);
        return mapping.findForward("success"); //To change body of implemented methods use File | Settings | File Templates.
    }

    private ActionForward performPost(ActionMapping mapping, BaseActionForm form, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        updateHealthCenter(request, form);
        return mapping.findForward("list");
    }

    private void updateHealthCenter(HttpServletRequest request, BaseActionForm form) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        HealthCenterDAO healthCenterDAO = new HealthCenterDAOImpl();
        HealthCenter healthCenter = healthCenterDAO.getByName(request.getParameter("name"));
        if(healthCenter != null) {
            PropertyUtils.copyProperties(healthCenter, form);
            Transaction tx = HibernateUtil.getSession().beginTransaction();
            healthCenterDAO.update(healthCenter);
            tx.commit();
        }
    }

    @Override
    protected String getPageTitleKey() {
        return "healthcenter.add.title";
    }

    @Override
    protected String getPageSubtitleKey() {
        return "healthcenter.add.title";
    }

    private void setGlobalMessage(ActionMessage message,HttpServletRequest request){
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, message);
        saveMessages(request,messages);
    }
}

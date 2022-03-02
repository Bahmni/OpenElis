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
package us.mn.state.health.lims.panelitem.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import us.mn.state.health.lims.method.daoimpl.MethodDAOImpl;
import us.mn.state.health.lims.method.valueholder.Method;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.panelitem.dao.PanelItemDAO;
import us.mn.state.health.lims.panelitem.daoimpl.PanelItemDAOImpl;
import us.mn.state.health.lims.panelitem.valueholder.PanelItem;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author diane benz
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class PanelItemUpdateAction extends BaseAction {

    private final PanelDAO panelDAO;
    private final TestDAO testDAO;
    private final PanelItemDAO panelItemDAO;
    private final MethodDAOImpl methodDAO;
    private boolean isNew = false;
    private final Logger logger = LogManager.getLogger(this.getClass());

    public PanelItemUpdateAction() {
        super();
        panelDAO = new PanelDAOImpl();
        testDAO = new TestDAOImpl();
        panelItemDAO = new PanelItemDAOImpl();
        methodDAO = new MethodDAOImpl();
    }

    protected ActionForward performAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// The first job is to determine if we are coming to this action with an
		// ID parameter in the request. If there is no parameter, we are
		// creating a new PanelItem.
		// If there is a parameter present, we should bring up an existing
		// PanelItem to edit.

		String forward = FWD_SUCCESS;
		request.setAttribute(ALLOW_EDITS_KEY, "true");
		request.setAttribute(PREVIOUS_DISABLED, "false");
		request.setAttribute(NEXT_DISABLED, "false");
		
		String id = request.getParameter(ID);

        isNew = StringUtil.isNullorNill(id) || "0".equals(id);

		BaseActionForm dynaForm = (BaseActionForm) form;

		// server-side validation (validation.xml)
		ActionMessages errors = dynaForm.validate(mapping, request);
		try {
			errors = validateAll(request, errors, dynaForm);
		} catch (Exception e) {
    		//bugzilla 2154
			LogEvent.logError("PanelItemUpdateAction","performAction()",e.toString());
			ActionError error = new ActionError("errors.ValidationException",null, null);
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}

		if (errors != null && errors.size() > 0) {
			saveErrors(request, errors);
            request.setAttribute(Globals.ERROR_KEY, errors);
            // since we forward to jsp - not Action we don't need to repopulate
			// the lists here
			return mapping.findForward(FWD_FAIL);
		}

		String start = request.getParameter("startingRecNo");
		String direction = request.getParameter("direction");

        PanelItem panelItem = new PanelItem();
        String parentPanelName = (String) dynaForm.get("parentPanelName");
        String testName = (String) dynaForm.get("testName");

        //get sysUserId from login module
        UserSessionData usd = (UserSessionData)request.getSession().getAttribute(USER_SESSION_DATA);
        String sysUserId = String.valueOf(usd.getSystemUserId());
        panelItem.setSysUserId(sysUserId);


        // populate valueholder from form
        PropertyUtils.copyProperties(panelItem, dynaForm);
        Panel parentPanel = getPanelByName(parentPanelName);
        Test test = getTestByName(testName);

        panelItem.setPanel(parentPanel);
		panelItem.setTest(test);

		try {

            if (isNew) {
                panelItemDAO.insertData(panelItem);
            } else {
                panelItemDAO.updateData(panelItem);
            }

		} catch (LIMSRuntimeException lre) {
            //bugzilla 2154
            LogEvent.logErrorStack("PanelItemUpdateAction","performAction()", lre);
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
			errors = new ActionMessages();
			java.util.Locale locale = (java.util.Locale) request.getSession()
			.getAttribute("org.apache.struts.action.LOCALE");
			ActionError error = null;
			if (lre.getException() instanceof org.hibernate.StaleObjectStateException) {
				// how can I get popup instead of struts error at the top of
				// page?
				// ActionMessages errors = dynaForm.validate(mapping, request);
				error = new ActionError("errors.OptimisticLockException", null,
						null);

			} else {
				// bugzilla 1482
				if (lre.getException() instanceof LIMSDuplicateRecordException) {
					String messageKey = "panelitem.panelitem";
					String msg = ResourceLocator.getInstance().getMessageResources().getMessage(locale,messageKey);
					error = new ActionError("errors.DuplicateRecord",msg, null);
				} else {
					error = new ActionError("errors.UpdateException", null,null);
				}
			}

			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
			saveErrors(request, errors);
			request.setAttribute(Globals.ERROR_KEY, errors);
			//bugzilla 1485: allow change and try updating again (enable save button)
			//request.setAttribute(IActionConstants.ALLOW_EDITS_KEY, "false");
			// disable previous and next
			request.setAttribute(PREVIOUS_DISABLED, "true");
			request.setAttribute(NEXT_DISABLED, "true");
			forward = FWD_FAIL;
        }
		if (forward.equals(FWD_FAIL))
			return mapping.findForward(forward);

		// initialize the form
		dynaForm.initialize(mapping);
		// repopulate the form from valueholder
		PropertyUtils.copyProperties(dynaForm, panelItem);

		if ("true".equalsIgnoreCase(request.getParameter("close"))) {
			forward = FWD_CLOSE;
		}

		if (panelItem.getId() != null && !panelItem.getId().equals("0")) {
            id = panelItem.getId();
			request.setAttribute(ID, panelItem.getId());
		}

		//bugzilla 1400
		if (isNew) forward = FWD_SUCCESS_INSERT;
		//bugzilla 1467 added direction for redirect to NextPreviousAction
		return getForward(mapping.findForward(forward), panelItem.getId(), start, direction);

	}

    protected String getPageTitleKey() {
		if (isNew) {
			return "panelitem.add.title";
		} else {
			return "panelitem.edit.title";
		}
	}

	protected String getPageSubtitleKey() {
		if (isNew) {
			return "panelitem.add.title";
		} else {
			return "panelitem.edit.title";
		}
	}

	protected ActionMessages validateAll(HttpServletRequest request,
			ActionMessages errors, BaseActionForm dynaForm) throws Exception {

		// parent panelItem validation against database
		String panelName = (String) dynaForm.get("parentPanelName");

		if (!StringUtil.isNullorNill(panelName)) {

			Panel panel = getPanelByName(panelName);

			String messageKey = "panelitem.panelParent";

			if (panel == null) {
				// the panelItem is not in database - not valid parentPanelItem
                addError(errors, messageKey, "errors.invalid");
			}
		}

		// method name validation against database
		String methodNameSelected = (String) dynaForm.get("methodName");

		if (!StringUtil.isNullorNill(methodNameSelected)) {
			Method method = new Method();
			method.setMethodName(methodNameSelected);
			method = methodDAO.getMethodByName(method);

			String messageKey = "panelitem.methodName";

			if (method == null) {
				// the panelItem is not in database - not valid parentPanelItem
                addError(errors, messageKey,"errors.invalid" );
			}
		}

		// test name validation against database
		String testNameSelected = (String) dynaForm.get("testName");

		if (!StringUtil.isNullorNill(testNameSelected)) {
            Test test = getTestByName(testNameSelected);;

			String messageKey = "panelitem.testName";

			if (test == null) {
				// the panelItem is not in database - not valid parentPanelItem
                addError(errors, messageKey,"errors.invalid" );
			}
		}

	    String sortOrder = (String) dynaForm.get("sortOrder");
        try {
            Integer.parseInt(sortOrder);
        } catch (NumberFormatException nfe) {
            logger.warn("Sort order " + sortOrder + " is invalid. Setting to zero");
            sortOrder = "0";
            dynaForm.set("sortOrder", "0");
        }

        // Bugzilla 2207 check for duplicate sort order item
        String id = (String) dynaForm.get("id");

	    if (!StringUtil.isNullorNill(panelName) && !StringUtil.isNullorNill (sortOrder)) {
		    PanelItem panelItem = new PanelItem();
		    panelItem.setPanelName(panelName);
		    panelItem.setSortOrder(sortOrder);

		    if (!StringUtil.isNullorNill(id)) {
		        panelItem.setId(id);
		     }

		    String messageKey = "panelitem.sortOrder";

		    if (panelItemDAO.getDuplicateSortOrderForPanel(panelItem, isNew)) {
			// There is already one with the same panel name and sort order id in the database
                addError(errors, messageKey, "errors.DuplicateItem");
		    }
	    }
	    return errors;
	}

    private void addError(ActionMessages errors, String messageKey, String key) throws Exception {
        ActionError error = new ActionError(key,getMessageForKey(messageKey), null);
        errors.add(ActionMessages.GLOBAL_MESSAGE, error);
    }

    private Panel getPanelByName(String parentPanelName) {
        return panelDAO.getPanelByName(parentPanelName);
    }

    private Test getTestByName(String testName) {
        return testDAO.getTestByName(testName);
    }

}

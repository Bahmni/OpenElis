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
package us.mn.state.health.lims.login.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.log.LogEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Hung Nguyen (Hung.Nguyen@health.state.mn.us)
 */
public class LogoutPageAction extends LoginBaseAction {

	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
        LogEvent.logWarn("LogoutPageAction", "performAction", request.getSession().getId());
        if (alreadyLoggedIn(request)) {
            request.getSession().invalidate();
            request.getSession(true);
        }
        return mapping.findForward("success");
	}

	protected String getPageTitleKey() {
		return "login.title";
	}

	protected String getPageSubtitleKey() {
		return "login.subTitle";
	}
}

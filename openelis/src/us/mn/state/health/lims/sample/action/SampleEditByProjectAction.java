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

package us.mn.state.health.lims.sample.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.patient.saving.RequestType;


/**
 * The SampleEditAndViewByProjectAction class represents the edit and view (read-only)
 * form of the application for the Retro-CI workflow
 * 
 */
public class SampleEditByProjectAction extends BaseSampleEntryAction {

	protected RequestType requestType = RequestType.UNKNOWN;
	
	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String forward = "success";

		request.getSession().setAttribute(IActionConstants.SAVE_DISABLED, IActionConstants.TRUE);
		
		BaseActionForm dynaForm = (BaseActionForm) form;
		
		// Initialize the form.
		dynaForm.initialize(mapping);
		
		String requestType = request.getParameter("type");
		setRequest(requestType);

	
		return mapping.findForward(forward);
		
	}

	protected String getPageSubtitleKey() {
		String key = null;

		switch (requestType) {
			case READWRITE: {
				key = "banner.menu.editSample.ReadWrite";
				break;
			}
			case READONLY: {
				key = "banner.menu.editSample.ReadOnly";
				break;
			}
			
			default: {
				key = "banner.menu.editSample.ReadOnly";
			}
		}

		return key;
	}

	protected void setRequest(String request) {
		if (!GenericValidator.isBlankOrNull(request)) {

			if (request.equals("readwrite")) {
				requestType = RequestType.READWRITE;
			} else if (request.equals("readonly")) {
				requestType = RequestType.READONLY;
			} 
		}
	}
	

	
}

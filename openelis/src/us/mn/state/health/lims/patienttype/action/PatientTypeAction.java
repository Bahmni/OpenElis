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

/**
 * Project : LIS<br>
 * File name : PatientTypeAction.java<br>
 * Description : 
 * @author TienDH
 * @date Aug 20, 2007
 */
package us.mn.state.health.lims.patienttype.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.patienttype.dao.PatientTypeDAO;
import us.mn.state.health.lims.patienttype.daoimpl.PatientTypeDAOImpl;
import us.mn.state.health.lims.patienttype.valueholder.PatientType;

/**
 * @author tiendh
 */
public class PatientTypeAction extends BaseAction {

	private boolean isNew = false;

	protected final ActionForward performAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String id = request.getParameter(ID);

		String forward = FWD_SUCCESS;
		request.setAttribute(ALLOW_EDITS_KEY, TRUE);
		request.setAttribute(PREVIOUS_DISABLED, TRUE);
		request.setAttribute(NEXT_DISABLED, TRUE);

		DynaActionForm dynaForm = (DynaActionForm) form;
		// initialize the form
		dynaForm.initialize(mapping);

		PatientType patientType = new PatientType();

		isNew = id == null || id.equals("0");
		
		if ( !isNew) {
			patientType.setId(id);
			PatientTypeDAO patientTypeDAO = new PatientTypeDAOImpl();
			patientTypeDAO.getData(patientType);
			request.setAttribute(ID, patientType.getId());
			
			List patientTypes = patientTypeDAO.getNextPatientTypeRecord(patientType.getId());
			if (patientTypes.size() > 0) {
				request.setAttribute(NEXT_DISABLED, FALSE);
			}
			patientTypes = patientTypeDAO.getPreviousPatientTypeRecord(patientType.getId());
			if (patientTypes.size() > 0) {
				request.setAttribute(PREVIOUS_DISABLED, FALSE);
			}
		}

		PropertyUtils.copyProperties(dynaForm, patientType);

		return mapping.findForward(forward);
	}

	protected String getPageTitleKey() {
		return isNew ? "patienttype.add.title" : "patienttype.edit.title";
	}

	protected String getPageSubtitleKey() {
		return isNew ? "patienttype.add.title" : "patienttype.edit.title";
	}

}

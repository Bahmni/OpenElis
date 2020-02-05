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
package us.mn.state.health.lims.typeofteststatus.action;
/**
 * @author Buvaneswari Arun
 *
 */
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import us.mn.state.health.lims.common.action.BaseMenuAction;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.typeofteststatus.dao.TypeOfTestStatusDAO;
import us.mn.state.health.lims.typeofteststatus.daoimpl.TypeOfTestStatusDAOImpl;

public class TypeOfTestStatusMenuAction extends BaseMenuAction {

	protected List createMenuList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		List typeOfTestStatus = new ArrayList();

		String stringStartingRecNo = (String) request
				.getAttribute("startingRecNo");
		int startingRecNo = Integer.parseInt(stringStartingRecNo);

		TypeOfTestStatusDAO typeOfTestStatusDAO = new TypeOfTestStatusDAOImpl();
		typeOfTestStatus = typeOfTestStatusDAO.getPageOfTypeOfTestStatus(startingRecNo);

		request.setAttribute("menuDefinition", "typeOfTestStatusMenuDefinition");
		
		request.setAttribute(MENU_TOTAL_RECORDS, String.valueOf(typeOfTestStatusDAO
				.getTotalTypeOfTestStatusCount()));
		request.setAttribute(MENU_FROM_RECORD, String.valueOf(startingRecNo));
		int numOfRecs = 0;
		if (typeOfTestStatus != null) {
			if (typeOfTestStatus.size() > SystemConfiguration.getInstance()
					.getDefaultPageSize()) {
				numOfRecs = SystemConfiguration.getInstance()
						.getDefaultPageSize();
			} else {
				numOfRecs = typeOfTestStatus.size();
			}
			numOfRecs--;
		}
		int endingRecNo = startingRecNo + numOfRecs;
		request.setAttribute(MENU_TO_RECORD, String.valueOf(endingRecNo));
		//end bugzilla 1411
		
		return typeOfTestStatus;
	}

	protected String getPageTitleKey() {
		return "typeofteststatus.browse.title";
	}

	protected String getPageSubtitleKey() {
		return "typeofteststatus.browse.title";
	}

	protected int getPageSize() {
		return SystemConfiguration.getInstance().getDefaultPageSize();
	}

	protected String getDeactivateDisabled() {
		return "true";
	}

}

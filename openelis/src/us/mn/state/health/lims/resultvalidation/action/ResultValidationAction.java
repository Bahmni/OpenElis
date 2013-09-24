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
 * Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package us.mn.state.health.lims.resultvalidation.action;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.resultvalidation.action.util.ResultValidationPaging;
import us.mn.state.health.lims.resultvalidation.bean.AnalysisItem;
import us.mn.state.health.lims.resultvalidation.util.ResultsValidationUtility;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ResultValidationAction extends BaseResultValidationAction {

    private List<Integer> validationStatus = new ArrayList<>();

	@Override
	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		BaseActionForm dynaForm = (BaseActionForm) form;

		request.getSession().setAttribute(SAVE_DISABLED, "true");
		String testSectionName = (request.getParameter("type"));
		String testName = (request.getParameter("test"));

		ResultValidationPaging paging = new ResultValidationPaging();
		String newPage = request.getParameter("page");

		if (getResultsForAPage(newPage)) {
			// Initialize the form.
			dynaForm.initialize(mapping);

            ResultsValidationUtility resultsValidationUtility = new ResultsValidationUtility();
			setRequestType(testSectionName);
			setStatus(testSectionName);

			if (!GenericValidator.isBlankOrNull(testSectionName)) {
                List<AnalysisItem> resultList;
                if (shouldGetAllSections(testSectionName)) {
                    resultList = resultsValidationUtility.getResultValidationListByStatus(getToBeValidatedStatuses());
                } else {
                    String sectionName = Character.toUpperCase(testSectionName.charAt(0)) + testSectionName.substring(1);
                    String dbSectionName = getDBSectionName(sectionName);
                    resultList = resultsValidationUtility.getResultValidationList(dbSectionName, testName, validationStatus);
                }
				paging.setDatabaseResults(request, dynaForm, resultList);
			}
			
		} else {
			paging.page(request, dynaForm, newPage);
		}

        return mapping.findForward(FWD_SUCCESS);
	}

    private boolean getResultsForAPage(String newPage) {
        return GenericValidator.isBlankOrNull(newPage);
    }

    public void setStatus(String testSection) {
		validationStatus = new ArrayList<>();

		if ("serology".equals(testSection)) {
			validationStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptance)));
			validationStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.Canceled)));
			// This next status determines if NonConformity analysis can still
			// be displayed on bio. validation page. We are awaiting feedback on
			// RetroCI
			// validationStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.NonConforming)));
		} else {
			validationStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptance)));
			validationStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptanceRO)));
		}
	}
}

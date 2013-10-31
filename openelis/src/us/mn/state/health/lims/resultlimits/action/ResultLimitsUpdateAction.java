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
package us.mn.state.health.lims.resultlimits.action;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.hibernate.StaleObjectStateException;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.resultlimits.dao.ResultLimitDAO;
import us.mn.state.health.lims.resultlimits.daoimpl.ResultLimitDAOImpl;
import us.mn.state.health.lims.resultlimits.form.ResultLimitsLink;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.typeoftestresult.dao.TypeOfTestResultDAO;
import us.mn.state.health.lims.typeoftestresult.daoimpl.TypeOfTestResultDAOImpl;
import us.mn.state.health.lims.typeoftestresult.valueholder.TypeOfTestResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ResultLimitsUpdateAction extends BaseAction {

	private boolean isNew = false;
	private static TestDAO testDAO = new TestDAOImpl();
    ResultLimitDAO resultLimitDAO = new ResultLimitDAOImpl();

	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setAttribute(ALLOW_EDITS_KEY, "true");
		request.setAttribute(PREVIOUS_DISABLED, "false");
		request.setAttribute(NEXT_DISABLED, "false");

		String id = request.getParameter(ID);
		isNew = id == null || id.equals("0") || id.equalsIgnoreCase("null");

		String forward = FWD_SUCCESS;

		BaseActionForm dynaForm = (BaseActionForm) form;

		String start = request.getParameter("startingRecNo");
		String direction = request.getParameter("direction");

		forward = validateAndUpdateResultLimits(mapping, request, dynaForm, isNew);
        return mapping.findForward(forward);
	}

	public String validateAndUpdateResultLimits(ActionMapping mapping, HttpServletRequest request,
			BaseActionForm dynaForm, boolean newLimit) {
		String forward;
		// server-side validation (validation.xml)
		ActionMessages errors = dynaForm.validate(mapping, request);

        try {
            ResultLimitsLink limitsLink = (ResultLimitsLink) dynaForm.get("limit");
            ResultLimit resultLimit = limitsLink.populateResultLimit(null);

            //the session.merge is not loading the resultLimit from the DB correctly
            //I don't understand why but this block is the workaround until I do.
            if (!newLimit) {
                resultLimitDAO.getData(resultLimit);
                limitsLink.populateResultLimit(resultLimit);
            }

            resultLimit.setSysUserId(getSysUserId(request));

            validateForAllDuplicateEntries(resultLimit);

            persistLimit(resultLimit);
            forward = FWD_SUCCESS_INSERT;
        } catch (LIMSDuplicateRecordException e) {
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
            ActionError error = new ActionError("errors.ResultLimits.DuplicateEntryException", null, null);
            return addErrors(request, error, errors);
        } catch (LIMSRuntimeException lre) {
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
			ActionError error = null;
			if (lre.getException() instanceof StaleObjectStateException) {
				error = new ActionError("errors.OptimisticLockException", null, null);
			}else {
				error = new ActionError("errors.UpdateException", null, null);
			}
            return addErrors(request, error, errors);
		}
		return forward;
	}

    private void validateForAllDuplicateEntries(ResultLimit resultLimit) throws LIMSDuplicateRecordException {
        String testId = resultLimit.getTestId();
        if(resultLimit.ageLimitsAreDefault()){
            List<ResultLimit> resultLimits = resultLimitDAO.getAllResultLimitsForTest(testId);
            String gender = resultLimit.getGender();
            for (ResultLimit limit : resultLimits) {
                if(limit.getGender() != null && gender != null && limit.getGender().equals(gender))
                    throw new LIMSDuplicateRecordException("duplicateEntry");
            }
        }

        TestResultDAO testResultDAO = new TestResultDAOImpl();
        TypeOfTestResultDAO typeOfTestResultDAO = new TypeOfTestResultDAOImpl();
        TypeOfTestResult typeOfTestResult = typeOfTestResultDAO.getTypeOfTestResultById(resultLimit.getResultTypeId());
        String testResultTypeName = typeOfTestResult.getTestResultType();

        List<TestResult> testResults = testResultDAO.getAllTestResultsPerTest(testId);
        for (TestResult testResult : testResults) {
            if(!testResult.getTestResultType().equals(testResultTypeName)){
                throw new LIMSDuplicateRecordException("duplicateEntry");
            }
        }

    }

    private String addErrors(HttpServletRequest request, ActionError error, ActionMessages errors) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        saveErrors(request, errors);
        request.setAttribute(Globals.ERROR_KEY, errors);
        // disable previous and next
        request.setAttribute(PREVIOUS_DISABLED, TRUE);
        request.setAttribute(NEXT_DISABLED, TRUE);
        return FWD_FAIL;
    }

    private void persistLimit(ResultLimit resultLimit) {
		ResultLimitDAO resultLimitDAO = new ResultLimitDAOImpl();
		if (GenericValidator.isBlankOrNull(resultLimit.getId())) {
			resultLimitDAO.insertData(resultLimit);
		} else {
			resultLimitDAO.updateData(resultLimit);
		}
	}

	private void persistTest(Test test) {
		if( test != null){
			test.setSysUserId(currentUserId);
			testDAO.updateData(test);
		}

	}
	protected String getPageTitleKey() {
		return isNew ? "resultlimits.add.title" : "resultlimits.edit.title";
	}

	protected String getPageSubtitleKey() {
		return isNew ? "resultlimits.add.title" : "resultlimits.edit.title";
	}
}

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
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.result.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.inventory.action.InventoryUtility;
import us.mn.state.health.lims.inventory.form.InventoryKitItem;
import us.mn.state.health.lims.login.dao.UserModuleDAO;
import us.mn.state.health.lims.login.daoimpl.UserModuleDAOImpl;
import us.mn.state.health.lims.organization.util.OrganizationUtils;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.referral.util.ReferralUtil;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.action.util.ResultsPaging;
import us.mn.state.health.lims.role.daoimpl.RoleDAOImpl;
import us.mn.state.health.lims.role.valueholder.Role;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.test.beanItems.TestResultItem;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.userrole.dao.UserRoleDAO;
import us.mn.state.health.lims.userrole.daoimpl.UserRoleDAOImpl;

import us.mn.state.health.lims.typeofteststatus.daoimpl.TypeOfTestStatusDAOImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AccessionResultsAction extends BaseAction {

	private String accessionNumber;
	private Sample sample;
	private String sampleType;
	private InventoryUtility inventoryUtility = new InventoryUtility();
	private static SampleDAO sampleDAO = new SampleDAOImpl();
	private static UserModuleDAO userModuleDAO = new UserModuleDAOImpl();
	private static String RESULT_EDIT_ROLE_ID;
	private static final String REFERRAL_LAB = "referralLab";

	static{
		Role editRole = new RoleDAOImpl().getRoleByName("Results modifier");

		if( editRole != null){
			RESULT_EDIT_ROLE_ID = editRole.getId();
		}
	}

	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String forward = FWD_SUCCESS;

		request.getSession().setAttribute(SAVE_DISABLED, TRUE);

		DynaActionForm dynaForm = (DynaActionForm) form;
		PropertyUtils.setProperty(dynaForm, "referralReasons", ReferralUtil.getReferralReasons());
		PropertyUtils.setProperty(dynaForm, "referralOrganizations", OrganizationUtils.getReferralOrganizations());

		//TYPE_OF_TEST_STATUS 		
		PropertyUtils.setProperty(dynaForm, "typeofteststatuses", new TypeOfTestStatusDAOImpl().getAllActiveTestStatus());
		

		ResultsPaging paging = new ResultsPaging();
		String newPage = request.getParameter("page");
		if (GenericValidator.isBlankOrNull(newPage)) {

			accessionNumber = request.getParameter("accessionNumber");
			sampleType = request.getParameter("sampleType");
			PropertyUtils.setProperty(dynaForm, "displayTestKit", false);

			if (!GenericValidator.isBlankOrNull(accessionNumber)) {
				ResultsLoadUtility resultsUtility = new ResultsLoadUtility(currentUserId);
				//This is for Haiti_LNSP if it gets more complicated use the status set stuff
				resultsUtility.addExcludedAnalysisStatus(AnalysisStatus.ReferredIn);
				resultsUtility.addExcludedAnalysisStatus(AnalysisStatus.Canceled);

				resultsUtility.setLockCurrentResults(modifyResultsRoleBased() && userNotInRole(request));
				ActionMessages errors = new ActionMessages();
				errors = validateAll(request, errors, dynaForm);

				if (errors != null && errors.size() > 0) {
					saveErrors(request, errors);
					request.setAttribute(ALLOW_EDITS_KEY, "false");

					setEmptyResults(dynaForm);

					return mapping.findForward(FWD_FAIL);
				}

				PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.TRUE);

				getSample();

				if (!GenericValidator.isBlankOrNull(sample.getId())) {
					Patient patient = getPatient();
					resultsUtility.addIdentifingPatientInfo(patient, dynaForm);

					List<TestResultItem> results = resultsUtility.getGroupedTestsForSample(sample, sampleType);

					if (resultsUtility.inventoryNeeded()) {
						addInventory(dynaForm);
						PropertyUtils.setProperty(dynaForm, "displayTestKit", true);
					} else {
						addEmptyInventoryList(dynaForm);
					}

					paging.setDatabaseResults(request, dynaForm, results);

                    setReferredOutForTests(dynaForm);
				} else {
					setEmptyResults(dynaForm);
				}
			} else {
				PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.FALSE);
			}
		} else {
			paging.page(request, dynaForm, newPage);
		}

		return mapping.findForward(forward);
	}

    private void setReferredOutForTests(DynaActionForm dynaForm) throws Exception {

        Object testResultObject = PropertyUtils.getProperty(dynaForm, "testResult");
        Object referralReasonsObject = PropertyUtils.getProperty(dynaForm, "referralReasons");
        Object referralOrganizationsObject = PropertyUtils.getProperty(dynaForm, "referralOrganizations");

        String referralReasonId = null;
        String referralOrganizationId = null;
        List<IdValuePair> referralReasonsList = null;
        List<IdValuePair> referralOrganizationsList = null;


        if (referralReasonsObject != null) {
            referralReasonsList = (ArrayList) referralReasonsObject;
            for (IdValuePair referralReasonItem : referralReasonsList) {
                if (referralReasonItem.getValue().equals("Auto Referred Out")) {
                    referralReasonId = referralReasonItem.getId();
                }
            }
        }

        if (referralOrganizationsObject != null) {
            referralOrganizationsList = (ArrayList) referralOrganizationsObject;
            for (IdValuePair referralOrganization : referralOrganizationsList) {
                if (referralOrganization.getValue().equals("External Lab")) {
                    referralOrganizationId = referralOrganization.getId();
                }
            }
        }

        List<TestResultItem> testResultItems = null;
        if (testResultObject != null) {
            testResultItems = (ArrayList) testResultObject;
        }


        for (TestResultItem testResultItem : testResultItems) {
            if (testResultItem.getTestId() != null) {
                Test test = getTestById(testResultItem.getTestId());
                if (test.getIsReferredOut() && !testResultItem.isReferredOut() && (testResultItem.getResult() == null)) {
                    testResultItem.setReferredOut(test.getIsReferredOut());
                    testResultItem.setReferralReasonId(referralReasonId);
                    testResultItem.setReferralOrganizationId(referralOrganizationId);
                }
            }
        }

        PropertyUtils.setProperty(dynaForm, "testResult", testResultItems);

    }

    private boolean modifyResultsRoleBased() {
        return "true".equals(ConfigurationProperties.getInstance().getPropertyValue(Property.roleRequiredForModifyResults));
    }

    private boolean userNotInRole(HttpServletRequest request) {
        if (userModuleDAO.isUserAdmin(request)) {
            return false;
        }

        UserRoleDAO userRoleDAO = new UserRoleDAOImpl();

        List<String> roleIds = userRoleDAO.getRoleIdsForUser(currentUserId);

        return !roleIds.contains(RESULT_EDIT_ROLE_ID);
    }

    private void setEmptyResults(DynaActionForm dynaForm) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        PropertyUtils.setProperty(dynaForm, "testResult", new ArrayList<TestResultItem>());
        PropertyUtils.setProperty(dynaForm, "displayTestKit", false);
        addEmptyInventoryList(dynaForm);
    }

    private void addInventory(DynaActionForm dynaForm) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        List<InventoryKitItem> list = inventoryUtility.getExistingActiveInventory();
        PropertyUtils.setProperty(dynaForm, "inventoryItems", list);
    }

    private void addEmptyInventoryList(DynaActionForm dynaForm) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        PropertyUtils.setProperty(dynaForm, "inventoryItems", new ArrayList<InventoryKitItem>());
    }

    private ActionMessages validateAll(HttpServletRequest request, ActionMessages errors, DynaActionForm dynaForm) {

        Sample sample = sampleDAO.getSampleByAccessionNumber(accessionNumber);

        if (sample == null) {
            ActionError error = new ActionError("sample.edit.sample.notFound", accessionNumber, null, null);
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }

        return errors;
    }

    private Patient getPatient() {
        SampleHumanDAO sampleHumanDAO = new SampleHumanDAOImpl();
        return sampleHumanDAO.getPatientForSample(sample);
    }

    private Test getTestById(String testId) {
        TestDAO testDAO = new TestDAOImpl();
        return testDAO.getTestById(testId);
    }


    private void getSample() {
	    if(sampleType == null)
        sample = sampleDAO.getSampleByAccessionNumber(accessionNumber);
	    else
	        sample = sampleDAO.getSampleByAccessionNumberAndType(accessionNumber, sampleType);
    }

    protected String getPageTitleKey() {
        return "banner.menu.results";

    }

    protected String getPageSubtitleKey() {
        return "banner.menu.results";
    }

}

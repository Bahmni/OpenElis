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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.inventory.action.InventoryUtility;
import us.mn.state.health.lims.inventory.form.InventoryKitItem;
import us.mn.state.health.lims.organization.util.OrganizationUtils;
import us.mn.state.health.lims.patient.dao.PatientDAO;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.referral.util.ReferralUtil;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.action.util.ResultsPaging;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.test.beanItems.TestResultItem;
import us.mn.state.health.lims.typeofteststatus.daoimpl.TypeOfTestStatusDAOImpl;

public class PatientResultsAction extends BaseAction {

	private ResultsLoadUtility resultsUtility;
	private InventoryUtility inventoryUtility = new InventoryUtility();

	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		resultsUtility = new ResultsLoadUtility(currentUserId);
		String forward = FWD_SUCCESS;

		request.getSession().setAttribute(SAVE_DISABLED, TRUE);
		request.setAttribute(ACCESSION_SEARCH_WITH_PATIENT, FALSE);

		DynaActionForm dynaForm = (DynaActionForm) form;
		PropertyUtils.setProperty(dynaForm, "displayTestKit", Boolean.FALSE);
		PropertyUtils.setProperty(dynaForm, "referralReasons", ReferralUtil.getReferralReasons());
		PropertyUtils.setProperty(dynaForm, "referralOrganizations", OrganizationUtils.getReferralOrganizations());
		PropertyUtils.setProperty(dynaForm, "typeofteststatuses", new TypeOfTestStatusDAOImpl().getAllActiveTestStatus());

		ResultsPaging paging = new ResultsPaging();
		String newPage = request.getParameter("page");
		if (GenericValidator.isBlankOrNull(newPage)) {

			String patientID = request.getParameter("patientID");

			if (!GenericValidator.isBlankOrNull(patientID)) {
				List<TestResultItem> results = new ArrayList<TestResultItem>();
				PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.TRUE);
				Patient patient = getPatient(patientID);

				String statusRules = ConfigurationProperties.getInstance().getPropertyValueUpperCase(Property.StatusRules);
				if (statusRules.equals(	IActionConstants.STATUS_RULES_RETROCI)) {
					resultsUtility.addExcludedAnalysisStatus(AnalysisStatus.TechnicalRejected);
					resultsUtility.addExcludedAnalysisStatus(AnalysisStatus.Canceled);
				}else if (statusRules.equals(IActionConstants.STATUS_RULES_HAITI) ||
						  statusRules.equals(IActionConstants.STATUS_RULES_HAITI_LNSP) ) {
					resultsUtility.addExcludedAnalysisStatus(AnalysisStatus.ReferredIn);
					resultsUtility.addExcludedAnalysisStatus(AnalysisStatus.Canceled);
				}

				results = resultsUtility.getGroupedTestsForPatient(patient);

				PropertyUtils.setProperty(dynaForm, "testResult", results);

				// move this out of results utility
				resultsUtility.addIdentifingPatientInfo(patient, dynaForm);

				if (resultsUtility.inventoryNeeded()) {
					addInventory(dynaForm);
					PropertyUtils.setProperty(dynaForm, "displayTestKit", true);
				} else {
					addEmptyInventoryList(dynaForm);
				}

				 paging.setDatabaseResults(request, dynaForm, results);

			} else {
				PropertyUtils.setProperty(dynaForm, "buttonText", StringUtil.getMessageForKey("resultsentry.patient.search"));
				PropertyUtils.setProperty(dynaForm, "testResult", new ArrayList<TestResultItem>());
				PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.FALSE);
			}
		} else {
			paging.page(request, dynaForm, newPage);
		}

		if(request.getMethod().equals("POST") && request.getParameter("patientID") == null) {
			ActionRedirect actionRedirect = new ActionRedirect(mapping.findForward("postSuccess"));
			String page = request.getParameter("page") == null ? "1" : request.getParameter("page");
			actionRedirect.addParameter("page", page);
			return actionRedirect;
		}
		return mapping.findForward(forward);
	}

	private void addInventory(DynaActionForm dynaForm) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		List<InventoryKitItem> list = inventoryUtility.getExistingActiveInventory();
		PropertyUtils.setProperty(dynaForm, "inventoryItems", list);
	}

	private void addEmptyInventoryList(DynaActionForm dynaForm) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		PropertyUtils.setProperty(dynaForm, "inventoryItems", new ArrayList<InventoryKitItem>());
	}

	private Patient getPatient(String patientID) {
		PatientDAO patientDAO = new PatientDAOImpl();
		Patient patient = new Patient();
		patient.setId(patientID);
		patientDAO.getData(patient);

		return patient;
	}

	protected String getPageTitleKey() {
		return "banner.menu.results";

	}

	protected String getPageSubtitleKey() {
		return "banner.menu.results";
	}

}

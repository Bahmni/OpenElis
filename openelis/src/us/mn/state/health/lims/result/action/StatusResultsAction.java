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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.formfields.FormFields;
import us.mn.state.health.lims.common.formfields.FormFields.Field;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.inventory.action.InventoryUtility;
import us.mn.state.health.lims.inventory.form.InventoryKitItem;
import us.mn.state.health.lims.organization.util.OrganizationUtils;
import us.mn.state.health.lims.referral.util.ReferralUtil;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.action.util.ResultsPaging;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.OrderStatus;
import us.mn.state.health.lims.test.beanItems.TestResultItem;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofteststatus.daoimpl.TypeOfTestStatusDAOImpl;

public class StatusResultsAction extends BaseAction implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final boolean FORWARD_SORT_ORDER = true;
	private final AnalysisDAO analysisDAO = new AnalysisDAOImpl();
	private final TestDAO testDAO = new TestDAOImpl();
	private final SampleDAO sampleDAO = new SampleDAOImpl();
	private Collection<String> analysisIDs;
	private ResultsLoadUtility resultsUtility;
	private final InventoryUtility inventoryUtility = new InventoryUtility();
	private boolean useSampleStatus = FormFields.getInstance().useField(Field.SearchSampleStatus);

	private static Set<Integer> excludedStatusIds;

	static{
		//currently this is the only one being excluded for Haiti_LNSP.  If it gets more complicate use the status sets
		excludedStatusIds = new HashSet<Integer>();
		excludedStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferredIn)));
		excludedStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.Canceled)));
	}

	@Override
	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		resultsUtility = new ResultsLoadUtility(currentUserId);
		String forward = FWD_SUCCESS;

		request.getSession().setAttribute(SAVE_DISABLED, TRUE);

		String newRequest = request.getParameter("blank");

		DynaActionForm dynaForm = (DynaActionForm) form;
		PropertyUtils.setProperty(dynaForm, "referralReasons", ReferralUtil.getReferralReasons());
		PropertyUtils.setProperty(dynaForm, "referralOrganizations", OrganizationUtils.getReferralOrganizations());
		PropertyUtils.setProperty(dynaForm, "typeofteststatuses", new TypeOfTestStatusDAOImpl().getAllActiveTestStatus());

		ResultsPaging paging = new ResultsPaging();

		String newPage = request.getParameter("page");
		if (GenericValidator.isBlankOrNull(newPage)) {
		List<TestResultItem> tests = new ArrayList<TestResultItem>();
		if (GenericValidator.isBlankOrNull(newRequest) || newRequest.equals("false")) {
			tests = setSearchResults(dynaForm);

			if( ConfigurationProperties.getInstance().isPropertyValueEqual(Property.patientDataOnResultsByRole, "true") &&   
					!userHasPermissionForModule(request, "PatientResults")){
				for( TestResultItem resultItem : tests){
					resultItem.setPatientInfo("---");
				}	
			}
			
			paging.setDatabaseResults(request, dynaForm, tests);
		} else {
			setEmptyResults(dynaForm);
		}

		setSelectionLists(dynaForm);
		} else {
			paging.page(request, dynaForm, newPage);
		}

		return mapping.findForward(forward);
	}

	private List<TestResultItem>  setSearchResults(DynaActionForm dynaForm) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List<TestResultItem> tests = getSelectedTests(dynaForm);
		PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.TRUE);

		if (resultsUtility.inventoryNeeded()) {
			addInventory(dynaForm);
			PropertyUtils.setProperty(dynaForm, "displayTestKit", true);
		} else {
			addEmptyInventoryList(dynaForm);
			PropertyUtils.setProperty(dynaForm, "displayTestKit", false);
		}

		return tests;
	}

	private void setEmptyResults(DynaActionForm dynaForm) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		PropertyUtils.setProperty(dynaForm, "testResult", new ArrayList<TestResultItem>());
		PropertyUtils.setProperty(dynaForm, "displayTestKit", false);
		PropertyUtils.setProperty(dynaForm, "collectionDate", "");
		PropertyUtils.setProperty(dynaForm, "recievedDate", "");
		PropertyUtils.setProperty(dynaForm, "selectedAnalysisStatus", "");
		PropertyUtils.setProperty(dynaForm, "selectedTest", "");
		PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.FALSE);
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

	private void setSelectionLists(DynaActionForm dynaForm) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		List<DropPair> analysisStatusList = getAnalysisStatusTypes();
		List<DropPair> testList = getTestTypes();

		PropertyUtils.setProperty(dynaForm, "analysisStatusSelections", analysisStatusList);
		PropertyUtils.setProperty(dynaForm, "testSelections", testList);

		if( useSampleStatus){
			List<DropPair> sampleStatusList = getSampleStatusTypes();
			PropertyUtils.setProperty(dynaForm, "sampleStatusSelections", sampleStatusList);
		}
	}

	private List<TestResultItem> getSelectedTests(DynaActionForm dynaForm) {

		/*
		 * N.B. Once we have any analysis in the list the only thing we can do
		 * is reduce what should be included in the list since we are finding
		 * the intersection of the search criteria. We will use analysis ID's as
		 * the indicator, we can not use object references
		 */
		analysisIDs = new HashSet<String>();

		String collectionDate = dynaForm.getString("collectionDate");
		String recievedDate = dynaForm.getString("recievedDate");
		String analysisStatus = dynaForm.getString("selectedAnalysisStatus");
		String sampleStatus = dynaForm.getString("selectedSampleStatus");
		String test = (String) dynaForm.getString("selectedTest");

		List<Analysis> filteredAnalysisList = new ArrayList<Analysis>();

		if (!GenericValidator.isBlankOrNull(collectionDate)) {
			filteredAnalysisList = getAnalysisForCollectionDate(collectionDate);

			if (filteredAnalysisList.isEmpty()) {
				return new ArrayList<TestResultItem>();
			}
			addAnalysisIds(filteredAnalysisList);
		}

		if (!GenericValidator.isBlankOrNull(recievedDate)) {
			List<Analysis> recievedDateList = getAnalysisForRecievedDate(recievedDate);

			if (recievedDateList.isEmpty()) {
				return new ArrayList<TestResultItem>();
			}

			filteredAnalysisList = absorbNewList(filteredAnalysisList, recievedDateList);
		}

		if (!(GenericValidator.isBlankOrNull(analysisStatus) || analysisStatus.equals("0"))) {
			List<Analysis> statusList = getAnalysisForAnalysisStatus(analysisStatus);

			if (statusList.isEmpty()) {
				return new ArrayList<TestResultItem>();
			}

			filteredAnalysisList = absorbNewList(filteredAnalysisList, statusList);
		}

		if ( useSampleStatus &&
			!(GenericValidator.isBlankOrNull(sampleStatus) || sampleStatus.equals("0"))) {

			List<Analysis> statusList = getAnalysisForSampleStatus(sampleStatus);

			if (statusList.isEmpty()) {
				return new ArrayList<TestResultItem>();
			}

			filteredAnalysisList = absorbNewList(filteredAnalysisList, statusList);
		}

		if (!(GenericValidator.isBlankOrNull(test) || test.equals("0"))) {
			List<Analysis> testList = getAnalysisForTest(test);

			if (testList.isEmpty()) {
				return new ArrayList<TestResultItem>();
			}

			filteredAnalysisList = absorbNewList(filteredAnalysisList, testList);
		}

		return buildTestItems(filteredAnalysisList);
	}

	private List<Analysis> absorbNewList(List<Analysis> filteredAnalysisList,
			List<Analysis> newCriteriaList) {

		if (filteredAnalysisList.isEmpty()) {
			filteredAnalysisList = newCriteriaList;
			addAnalysisIds(filteredAnalysisList);
		} else {
			adjustAnalysisIdList(newCriteriaList);
			trimAnalysisList(filteredAnalysisList);
		}

		return filteredAnalysisList;
	}

	private void adjustAnalysisIdList(Collection<Analysis> newList) {
		Collection<String> newListIds = new HashSet<String>();

		for (Analysis analysis : newList) {
			newListIds.add(analysis.getId());
		}

		Collection<String> copyOfAnalysisIDs = new HashSet<String>();
		copyOfAnalysisIDs.addAll(analysisIDs);

		for (String masterId : copyOfAnalysisIDs) {
			if (!newListIds.contains(masterId)) {
				analysisIDs.remove(masterId);
			}
		}
	}

	private void addAnalysisIds(Collection<Analysis> filteredAnalysisList) {

		for (Analysis analysis : filteredAnalysisList) {
			analysisIDs.add(analysis.getId());
		}
	}

	private void trimAnalysisList(Collection<Analysis> filteredAnalysisList) {
		Collection<Analysis> iterateList = new ArrayList<Analysis>();

		iterateList.addAll(filteredAnalysisList);

		for (Analysis analysis : iterateList) {
			if (!analysisIDs.contains(analysis.getId())) {
				filteredAnalysisList.remove(analysis);
			}
		}

	}

	private List<Analysis> getAnalysisForCollectionDate(String collectionDate) {
		Date date = DateUtil.convertStringDateToSqlDate(collectionDate);
		return analysisDAO.getAnalysisStartedOnExcludedByStatusId(date, excludedStatusIds);
	}

	private List<Analysis> getAnalysisForRecievedDate(String recievedDate) {
		List<Sample> sampleList = sampleDAO.getSamplesReceivedOn(recievedDate);

		return getAnalysisListForSampleItems(sampleList);
	}

	private List<Analysis> getAnalysisListForSampleItems(List<Sample> sampleList) {
		List<Analysis> analysisList = new ArrayList<Analysis>();
		SampleItemDAO sampleItemDAO = new SampleItemDAOImpl();

		for (Sample sample : sampleList) {
			List<SampleItem> sampleItemList = sampleItemDAO.getSampleItemsBySampleId(sample.getId());

			for (SampleItem sampleItem : sampleItemList) {
				List<Analysis> analysisListForItem = analysisDAO.getAnalysesBySampleItemsExcludingByStatusIds(sampleItem, excludedStatusIds);

				analysisList.addAll(analysisListForItem);
			}
		}

		return analysisList;
	}

	private List<Analysis> getAnalysisForAnalysisStatus(String status) {
		return analysisDAO.getAnalysesForStatusId(status);
	}

	private List<Analysis> getAnalysisForSampleStatus(String sampleStatus) {
		return analysisDAO.getAnalysesBySampleStatusIdExcludingByStatusId(sampleStatus, excludedStatusIds);
	}



	@SuppressWarnings("unchecked")
	private List<Analysis> getAnalysisForTest(String testId) {
		List<Integer> excludedStatusIntList = new ArrayList<Integer>();
		excludedStatusIntList.addAll(excludedStatusIds);
		return analysisDAO.getAllAnalysisByTestAndExcludedStatus(testId, excludedStatusIntList );
	}

	private List<TestResultItem> buildTestItems(List<Analysis> filteredAnalysisList) {
		return resultsUtility.getGroupedTestsForAnalysisList(filteredAnalysisList, FORWARD_SORT_ORDER);
	}

	private List<DropPair> getAnalysisStatusTypes() {

		List<DropPair> list = new ArrayList<DropPair>();
		list.add(new DropPair("0", ""));

		if( ConfigurationProperties.getInstance().getPropertyValueUpperCase(Property.StatusRules).equals(IActionConstants.STATUS_RULES_HAITI)||
				ConfigurationProperties.getInstance().getPropertyValueUpperCase(Property.StatusRules).equals(IActionConstants.STATUS_RULES_HAITI_LNSP)){
			list.add(new DropPair(StatusOfSampleUtil.getStatusID(AnalysisStatus.NotTested),StatusOfSampleUtil.getStatusName(AnalysisStatus.NotTested) ));
			list.add(new DropPair(StatusOfSampleUtil.getStatusID(AnalysisStatus.BiologistRejected),StatusOfSampleUtil.getStatusName(AnalysisStatus.BiologistRejected) ));
		}else if( ConfigurationProperties.getInstance().getPropertyValueUpperCase(Property.StatusRules).equals(IActionConstants.STATUS_RULES_RETROCI)){
			list.add(new DropPair(StatusOfSampleUtil.getStatusID(AnalysisStatus.NotTested),StatusOfSampleUtil.getStatusName(AnalysisStatus.NotTested) ));
			list.add(new DropPair(StatusOfSampleUtil.getStatusID(AnalysisStatus.Canceled),StatusOfSampleUtil.getStatusName(AnalysisStatus.Canceled) ));
			list.add(new DropPair(StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptance),StatusOfSampleUtil.getStatusName(AnalysisStatus.TechnicalAcceptance) ));
			list.add(new DropPair(StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalRejected),StatusOfSampleUtil.getStatusName(AnalysisStatus.TechnicalRejected) ));
			list.add(new DropPair(StatusOfSampleUtil.getStatusID(AnalysisStatus.BiologistRejected),StatusOfSampleUtil.getStatusName(AnalysisStatus.BiologistRejected) ));
		}

		return list;
	}

	private List<DropPair> getSampleStatusTypes() {

		List<DropPair> list = new ArrayList<DropPair>();
		list.add(new DropPair("0", ""));

		if( ConfigurationProperties.getInstance().getPropertyValueUpperCase(Property.StatusRules).equals(IActionConstants.STATUS_RULES_RETROCI)){
			list.add(new DropPair(StatusOfSampleUtil.getStatusID(OrderStatus.Entered),StatusOfSampleUtil.getStatusName(OrderStatus.Entered) ));
			list.add(new DropPair(StatusOfSampleUtil.getStatusID(OrderStatus.Started),StatusOfSampleUtil.getStatusName(OrderStatus.Started) ));
		}
		return list;
	}

	
	private List<DropPair> getTestTypes() {

		List<Test> testList = testDAO.getAllActiveTests(false);

		List<DropPair> list = new ArrayList<DropPair>();
		list.add(new DropPair("0", ""));

		for (Test test : testList) {
			list.add(new DropPair(test.getId(), test.getDescription()));
		}

		return list;
	}

	@Override
	protected String getPageTitleKey() {
		return "banner.menu.results";

	}

	@Override
	protected String getPageSubtitleKey() {
		return "banner.menu.results";
	}

	public class DropPair implements Serializable {

		private static final long serialVersionUID = 1L;

		public String getId() {
			return id;
		}

		public String getDescription() {
			return description;
		}

		private final String id;
		private final String description;

		public DropPair(String id, String description) {
			this.id = id;
			this.description = description;
		}
	}

}

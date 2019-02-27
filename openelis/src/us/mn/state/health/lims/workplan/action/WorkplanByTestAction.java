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
package us.mn.state.health.lims.workplan.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.services.QAService;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.test.beanItems.TestResultItem;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.result.action.util.SortByAccessionNumberAndSequence;

public class WorkplanByTestAction extends BaseWorkplanAction {

	private final AnalysisDAO analysisDAO = new AnalysisDAOImpl();
	private final boolean SORT_FORWARD = true;
	private static boolean HAS_NFS_PANEL = false;
	private List<Test> testList;

	String testType = "";
	String testName = "";
	String department;

	static {
		HAS_NFS_PANEL = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.CONDENSE_NFS_PANEL, "true");
	}

	@Override
	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		BaseActionForm dynaForm = (BaseActionForm) form;

		request.getSession().setAttribute(SAVE_DISABLED, "true");

		// Initialize the form.
		dynaForm.initialize(mapping);
		String workplan = (request.getParameter("type"));
		setRequestType(workplan);

		if (HAS_NFS_PANEL) {
			setNFSTestIdList();
		}

		List<TestResultItem> workplanTests = new ArrayList<TestResultItem>();

		// workplan by test
		testType = request.getParameter("selectedSearchID");

		if (!GenericValidator.isBlankOrNull(testType)) {

			if (testType.equals("NFS")) {
				testName = "NFS";
				workplanTests = getWorkplanForNFSTest();
			} else {
				testName = getTestName(testType);
				workplanTests = getWorkplanByTest(testType);
			}

			new SortByAccessionNumberAndSequence().sort(workplanTests, SORT_FORWARD);
			PropertyUtils.setProperty(dynaForm, "testTypeID", testType);
			PropertyUtils.setProperty(dynaForm, "testName", testName);
			PropertyUtils.setProperty(dynaForm, "workplanTests", workplanTests);
			PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.TRUE);

		} else {
			// no search done, set workplanTests as empty
			PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.FALSE);
			PropertyUtils.setProperty(dynaForm, "testName", null);
			PropertyUtils.setProperty(dynaForm, "workplanTests", new ArrayList<TestResultItem>());
		}

		setUpTestDropdownList();

		PropertyUtils.setProperty(dynaForm, "searchTypes", testList);
		PropertyUtils.setProperty(dynaForm, "workplanType", request.getParameter("type"));
		PropertyUtils.setProperty(dynaForm, "searchLabel", StringUtil.getMessageForKey("workplan.test.types"));
		PropertyUtils.setProperty(dynaForm, "searchAction", "WorkPlanByTest.do");

		return mapping.findForward(FWD_SUCCESS);
	}

	private void setUpTestDropdownList() {

		List<Test> allTestsList = testDAO.getAllActiveTests(false);

		testList = new ArrayList<Test>();

		if( HAS_NFS_PANEL){
			adjustNFSTests(allTestsList);
		}else{
			testList = allTestsList;
		}
		
		Collections.sort(testList, new TestDescriptionComparator());

	}

	private void adjustNFSTests(List<Test> allTestsList) {
		// add NFS to the list
		Test nfstest = new Test();
		nfstest.setTestName("NFS");
		nfstest.setDescription("NFS");
		nfstest.setId("NFS");
		allTestsList.add(nfstest);

		// remove NFS subtests
		for (Test test : allTestsList) {
			if (!nfsTestIdList.contains(test.getId())) {
				testList.add(test);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<TestResultItem> getWorkplanByTest(String testType) {

		List<Analysis> testList = new ArrayList<Analysis>();
		List<TestResultItem> workplanTestList = new ArrayList<TestResultItem>();
		String currentAccessionNumber = new String();
		int sampleGroupingNumber = 0;

		if (!(GenericValidator.isBlankOrNull(testType) || testType.equals("0"))) {

			testList = (List<Analysis>) analysisDAO.getAllAnalysisByTestAndStatus(testType, statusList);

			if (testList.isEmpty()) {
				return new ArrayList<TestResultItem>();
			}

			for (Analysis analysis : testList) {
				TestResultItem testResultItem = new TestResultItem();
				Sample sample = analysis.getSampleItem().getSample();
				testResultItem.setAccessionNumber(sample.getAccessionNumber());
				testResultItem.setReceivedDate(sample.getReceivedDateForDisplay());
				testResultItem.setNonconforming(QAService.isAnalysisParentNonConforming(analysis));

				if (!testResultItem.getAccessionNumber().equals(currentAccessionNumber)) {
					sampleGroupingNumber++;
					currentAccessionNumber = testResultItem.getAccessionNumber();
				}
				testResultItem.setSampleGroupingNumber(sampleGroupingNumber);

				workplanTestList.add(testResultItem);
			}

		}

		return workplanTestList;
	}

	@SuppressWarnings("unchecked")
	private List<TestResultItem> getWorkplanForNFSTest() {

		List<Analysis> testList = new ArrayList<Analysis>();
		List<TestResultItem> workplanTestList = new ArrayList<TestResultItem>();
		String currentAccessionNumber = new String();
		int sampleGroupingNumber = 0;

		TestResultItem testResultItem = new TestResultItem();

		List<String> testIdList = new ArrayList<String>();

		if (!(GenericValidator.isBlankOrNull(testType) || testType.equals("0"))) {

			testList = analysisDAO.getAllAnalysisByTestsAndStatus(nfsTestIdList, statusList);

			if (testList.isEmpty()) {
				return new ArrayList<TestResultItem>();
			}

			for (Analysis analysis : testList) {

				Sample sample = analysis.getSampleItem().getSample();
				String analysisAccessionNumber = sample.getAccessionNumber();

				if (!analysisAccessionNumber.equals(currentAccessionNumber)) {
					sampleGroupingNumber++;
					currentAccessionNumber = analysisAccessionNumber;
					testIdList = new ArrayList<String>();

				}
				testResultItem = new TestResultItem();
				testResultItem.setAccessionNumber(currentAccessionNumber);
				testResultItem.setReceivedDate(sample.getReceivedDateForDisplay());
				testResultItem.setSampleGroupingNumber(sampleGroupingNumber);
				testResultItem.setNonconforming(QAService.isAnalysisParentNonConforming(analysis));
				testIdList.add(analysis.getTest().getId());

				if (allNFSTestsRequested(testIdList)) {
					workplanTestList.add(testResultItem);
				}

			}

		}

		return workplanTestList;
	}

	private String getTestName(String testId) {

		Test test = new Test();
		test.setId(testId);

		test = testDAO.getTestById(test);

		return test.getName();
	}

	class TestDescriptionComparator implements Comparator<Test> {

		public int compare(Test p1, Test p2) {

			return p1.getTestName().toUpperCase().compareTo(p2.getTestName().toUpperCase());
		}

	}

}

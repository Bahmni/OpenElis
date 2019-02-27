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
package us.mn.state.health.lims.workplan.action;

import java.util.ArrayList;
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
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.TestSection;

import us.mn.state.health.lims.result.action.util.SortByAccessionNumberAndSequence;

public class WorkplanByTestSectionAction extends BaseWorkplanAction {

	private final boolean SORT_FORWARD = true;
	private final AnalysisDAO analysisDAO = new AnalysisDAOImpl();
	private static boolean HAS_NFS_PANEL = false;

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
		String workplan = request.getParameter("type");
		setRequestType(workplan);
		
		List<TestResultItem> workplanTests = new ArrayList<TestResultItem>();

		nfsTestIdList = new ArrayList<String>();
		if( HAS_NFS_PANEL){
			setNFSTestIdList();
		}

		// workplan by department
		if (!GenericValidator.isBlankOrNull(workplan)) {

			// get tests based on test section
			workplanTests = getWorkplanByTestSection(workplan);
			PropertyUtils.setProperty(dynaForm, "workplanTests", workplanTests);
			PropertyUtils.setProperty(dynaForm, "searchFinished", Boolean.TRUE);

		} else {
			// set workplanTests as empty
			PropertyUtils.setProperty(dynaForm, "workplanTests", new ArrayList<TestResultItem>());
		}
		new SortByAccessionNumberAndSequence().sort(workplanTests, SORT_FORWARD);
		PropertyUtils.setProperty(dynaForm, "workplanType", workplan);
		PropertyUtils.setProperty(dynaForm, "testName", getTestName(workplan));

		return mapping.findForward(FWD_SUCCESS);
	}

	private String getTestName(String type) {
		return StringUtil.getContextualMessageForKey("test.section." + type);
	}

	@SuppressWarnings("unchecked")
	private List<TestResultItem> getWorkplanByTestSection(String testSection) {

		List<Analysis> testList = new ArrayList<Analysis>();
		List<TestResultItem> workplanTestList = new ArrayList<TestResultItem>();
		String currentAccessionNumber = new String();
		int sampleGroupingNumber = 0;
		List<String> testIdList = new ArrayList<String>();
		List<TestResultItem> nfsTestItemList = new ArrayList<TestResultItem>();
		boolean isNFSTest = false;
		TestResultItem testResultItem = new TestResultItem();

		if (!(GenericValidator.isBlankOrNull(testSection))) {

			String sectionId = getTestSectionId();
			testList = (List<Analysis>) analysisDAO.getAllAnalysisByTestSectionAndStatus(sectionId, statusList, true);

			if (testList.isEmpty()) {
				return new ArrayList<TestResultItem>();
			}

			for (Analysis analysis : testList) {
				Sample sample = analysis.getSampleItem().getSample();
				String analysisAccessionNumber = sample.getAccessionNumber();

				if (!analysisAccessionNumber.equals(currentAccessionNumber)) {

					if (isNFSTest) {
						if (!allNFSTestsRequested(testIdList)) {
							// add nfs subtests
							for (TestResultItem nfsTestItem : nfsTestItemList) {
								workplanTestList.add(nfsTestItem);
							}
						}
					}

					sampleGroupingNumber++;
					currentAccessionNumber = analysisAccessionNumber;
					testIdList = new ArrayList<String>();
					nfsTestItemList = new ArrayList<TestResultItem>();
					isNFSTest = false;
				}
				testResultItem = new TestResultItem();
				testResultItem.setTestName(analysis.getTest().getTestName());
				testResultItem.setAccessionNumber(currentAccessionNumber);
				testResultItem.setReceivedDate(sample.getReceivedDateForDisplay());
				testResultItem.setSampleGroupingNumber(sampleGroupingNumber);
				testResultItem.setTestId(analysis.getTest().getId());
				testResultItem.setNonconforming(QAService.isAnalysisParentNonConforming(analysis));
				testIdList.add(testResultItem.getTestId());

				if (nfsTestIdList.contains(testResultItem.getTestId())) {
					isNFSTest = true;
					nfsTestItemList.add(testResultItem);
				}
				if (isNFSTest) {

					if (allNFSTestsRequested(testIdList)) {
						testResultItem.setTestName("NFS");
						workplanTestList.add(testResultItem);
					} else if (!nfsTestItemList.isEmpty() && (testList.size() - 1) == testList.indexOf(analysis)) {
						// add nfs subtests
						for (TestResultItem nfsTestItem : nfsTestItemList) {
							workplanTestList.add(nfsTestItem);
						}
					}
				} else {
					workplanTestList.add(testResultItem);
				}
			}

		}

		return workplanTestList;
	}

	private String getTestSectionId() {

		TestSection testSection = new TestSection();
		String testSectionName = getTestSectionName();
		testSection.setTestSectionName(testSectionName);

		TestSectionDAO testSectionDAO = new TestSectionDAOImpl();
		testSection = testSectionDAO.getTestSectionByName(testSection);

		return testSection == null ? null : testSection.getId();
	}

}

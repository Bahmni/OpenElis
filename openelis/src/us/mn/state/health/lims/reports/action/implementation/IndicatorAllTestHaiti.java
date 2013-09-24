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
package us.mn.state.health.lims.reports.action.implementation;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.reports.action.implementation.reportBeans.HaitiAggregateReportData;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import java.util.*;

/**
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * Copyright (C) CIRG, University of Washington, Seattle WA. All Rights
 * Reserved.
 * 
 */
public abstract class IndicatorAllTestHaiti extends HaitiIndicatorReport implements IReportCreator,
		IReportParameterSetter {

	private List<HaitiAggregateReportData> reportItems;
	private Map<String, TestBucket> testNameToBucketList;
	private Map<String, TestBucket> concatSection_TestToBucketMap;
	private List<TestBucket> testBucketList;
	private static String NOT_STARTED_STATUS_ID;
	private static String REFERRED_STATUS_ID;
	private static String FINALIZED_STATUS_ID;
	private static String TECH_ACCEPT_ID;
	private static String TECH_REJECT_ID;
	private static String BIOLOGIST_REJECT_ID;
	private static String USER_TEST_SECTION_ID;

	static {
		NOT_STARTED_STATUS_ID = StatusOfSampleUtil.getStatusID(AnalysisStatus.NotTested);
		REFERRED_STATUS_ID = StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferedOut);
		FINALIZED_STATUS_ID = StatusOfSampleUtil.getStatusID(AnalysisStatus.Finalized);
		TECH_ACCEPT_ID = StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptance);
		TECH_REJECT_ID = StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalRejected);
		BIOLOGIST_REJECT_ID = StatusOfSampleUtil.getStatusID(AnalysisStatus.BiologistRejected);
		USER_TEST_SECTION_ID = new TestSectionDAOImpl().getTestSectionByName("user").getId();
	}

	@Override
	protected String reportFileName() {
		return "HaitiLabAggregate";
	}

	public JRDataSource getReportDataSource() throws IllegalStateException {
		return errorFound ? new JRBeanCollectionDataSource(errorMsgs) : new JRBeanCollectionDataSource(reportItems);
	}

	public void initializeReport(BaseActionForm dynaForm) {
		super.initializeReport();
		setDateRange(dynaForm);

		createReportParameters();

		setTestMapForAllTests();

		setAnalysisForDateRange();

		mergeLists();

		setTestAggregates();

	}

	private void setTestMapForAllTests() {
		testNameToBucketList = new HashMap<String, TestBucket>();
		concatSection_TestToBucketMap = new HashMap<String, TestBucket>();
		testBucketList = new ArrayList<TestBucket>();

		TestDAO testDAO = new TestDAOImpl();
		List<Test> testList = testDAO.getAllActiveTests(false);

		for (Test test : testList) {
			TestBucket bucket = new TestBucket();

			bucket.testName = test.getTestName();
			bucket.testSection = test.getTestSection().getLocalizedName();

			testNameToBucketList.put(test.getTestName(), bucket);
			testBucketList.add(bucket);
		}

	}

	private void setAnalysisForDateRange() {
		AnalysisDAO analysisDAO = new AnalysisDAOImpl();
		List<Analysis> analysisList = analysisDAO.getAnalysisStartedOrCompletedInDateRange(lowDate, highDate);

		for (Analysis analysis : analysisList) {
			Test test = analysis.getTest();

			if (test != null) {
				TestBucket testBucket = null;
				if (USER_TEST_SECTION_ID.equals(analysis.getTestSection().getId())) {
					String concatedName = analysis.getTestSection().getLocalizedName()
							+ analysis.getTest().getLocalizedName();
					testBucket = concatSection_TestToBucketMap.get(concatedName);
					if (testBucket == null) {
						testBucket = new TestBucket();
						testBucket.testName = test.getReportingDescription();
						testBucket.testSection = analysis.getTestSection().getLocalizedName();
						concatSection_TestToBucketMap.put(concatedName, testBucket);
					}
				} else {
					testBucket = testNameToBucketList.get(test.getTestName());
				}

				if (testBucket != null) {
					if (NOT_STARTED_STATUS_ID.equals(analysis.getStatusId())) {
						testBucket.notStartedCount++;
					} else if (inProgress(analysis)) {
						testBucket.inProgressCount++;
					} else if (FINALIZED_STATUS_ID.equals(analysis.getStatusId())) {
						testBucket.finishedCount++;
					}
				}
			}
		}
	}

    private String getSortOrder(String sortOrder) {
        return sortOrder == null? "0" : sortOrder;
    }

    private boolean inProgress(Analysis analysis) {
		return REFERRED_STATUS_ID.equals(analysis.getStatusId()) ||
			   TECH_ACCEPT_ID.equals(analysis.getStatusId()) ||
			   TECH_REJECT_ID.equals(analysis.getStatusId()) ||
			   BIOLOGIST_REJECT_ID.equals(analysis.getStatusId());
	}
	
	private void mergeLists() {

		for (TestBucket bucket : concatSection_TestToBucketMap.values()) {
			testBucketList.add(bucket);
		}

		Collections.sort(testBucketList, new Comparator<TestBucket>() {
			@Override
			public int compare(TestBucket o1, TestBucket o2) {
                int compareSections = compareSections(o1, o2);
                return compareSections == 0 ? compareTest(o1, o2) : compareSections;
			}

            private int compareSections(TestBucket o1, TestBucket o2) {
                return o1.testSection.compareTo(o2.testSection);
            }

            private int compareTest(TestBucket o1, TestBucket o2) {
                return o1.testName.compareTo(o2.testName);
            }
        });

	}

	@Override
	protected String getNameForReportRequest() {
		return StringUtil.getMessageForKey("openreports.all.tests.aggregate");
	}

	private void setTestAggregates() {
		reportItems = new ArrayList<HaitiAggregateReportData>();

		for (TestBucket bucket : testBucketList) {
			if ((bucket.finishedCount + bucket.notStartedCount + bucket.inProgressCount) > 0) {
				HaitiAggregateReportData data = new HaitiAggregateReportData();

				data.setFinished(bucket.finishedCount);
				data.setNotStarted(bucket.notStartedCount);
				data.setInProgress(bucket.inProgressCount);
				data.setTestName(bucket.testName);
				data.setSectionName(bucket.testSection);

				reportItems.add(data);
			}
		}
	}

	private class TestBucket {
		public String testName = "";
		public String testSection = "";
		public int notStartedCount = 0;
		public int inProgressCount = 0;
		public int finishedCount = 0;
	}

	@Override
	protected String getNameForReport() {
		return StringUtil.getContextualMessageForKey("openreports.all.tests.aggregate");
	}
}

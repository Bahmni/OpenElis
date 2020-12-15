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
 * Copyright (C) ITECH, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package us.mn.state.health.lims.reports.action.implementation;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.reports.action.implementation.reportBeans.HaitiClinicalPatientData;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.util.AccessionNumberUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.common.util.DateUtil;

import java.util.*;

public class PatientHaitiClinical extends HaitiPatientReport implements IReportCreator, IReportParameterSetter {

	private AnalysisDAO analysisDAO = new AnalysisDAOImpl();
	private static Set<Integer> analysisStatusIds;
	private boolean isLNSP = false;
	protected List<HaitiClinicalPatientData> clinicalReportItems;

	static {
		analysisStatusIds = new HashSet<>();
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.BiologistRejected)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.Finalized)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.NonConforming_depricated)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.NotTested)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferedOut)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferredIn)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptance)));
//		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.Canceled)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptanceRO)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.FinalizedRO)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.BiologistRejectedRO)));
		analysisStatusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.MarkedAsDone)));
	}

	public PatientHaitiClinical() {
		super();
	}

	public PatientHaitiClinical(boolean isLNSP) {
		super();
		this.isLNSP = isLNSP;
	}

	@Override
	protected String reportFileName() {
		return "PatientReportHaitiClinical";
	}

	@Override
	protected void createReportItems() {
		List<Analysis> analysisList = analysisDAO.getAnalysesBySampleIdAndStatusId(reportSample.getId(), analysisStatusIds);

		currentConclusion = null;
		for (Analysis analysis : analysisList) {
			// case if there was a confirmation sample with no test specified
			if (analysis.getTest() != null) {
				reportAnalysis = analysis;
				HaitiClinicalPatientData resultsData = reportAnalysisResults();
				reportItems.add(resultsData);
			}
		}
	}

    @Override
	protected void postSampleBuild() {
		if (reportItems.isEmpty()) {
			HaitiClinicalPatientData reportItem = reportAnalysisResults();
			reportItem.setTestSection(StringUtil.getMessageForKey("report.no.results"));
			clinicalReportItems.add(reportItem);
		} else {
			buildReport();
		}

	}

	private void buildReport() {
		Collections.sort(reportItems, new Comparator<HaitiClinicalPatientData>() {
			@Override
			public int compare(HaitiClinicalPatientData o1, HaitiClinicalPatientData o2) {

				Date orderDate1 = DateUtil.convertStringDateToSqlDate(o1.getOrderDate());
				Date orderDate2 = DateUtil.convertStringDateToSqlDate(o2.getOrderDate());
				int dateSort = orderDate1.compareTo(orderDate2);

				if (dateSort != 0) {
					return dateSort;
				}

				String o1AccessionNumber = AccessionNumberUtil.getAccessionNumberFromSampleItemAccessionNumber( o1.getAccessionNumber() );
				String o2AccessionNumber = AccessionNumberUtil.getAccessionNumberFromSampleItemAccessionNumber( o2.getAccessionNumber() );
				int accessionSort = o1AccessionNumber.compareTo(o2AccessionNumber);

				if (accessionSort != 0) {
					return accessionSort;
				}

				if (o1.getSectionSortOrder() > o2.getSectionSortOrder()) {
					return 1;
				} else if (o1.getSectionSortOrder() < o2.getSectionSortOrder()) {
					return -1;
				}

				int o1Panel = Integer.MAX_VALUE;
				int o2Panel = Integer.MAX_VALUE;
				if( o1.getPanel() != null){
					o1Panel = o1.getPanel().getSortOrderInt();
				}
				if( o2.getPanel() != null){
					o2Panel = o2.getPanel().getSortOrderInt();
				}
				
				int panelSort = o1Panel - o2Panel;
				
				if( panelSort != 0){
					return panelSort;
				}
				
				return o1.getTestSortOrder() - o2.getTestSortOrder();
			}
		});

		String currentPanelId = null;
		for (HaitiClinicalPatientData reportItem : reportItems) {
			if( reportItem.getPanel() != null && !reportItem.getPanel().getId().equals(currentPanelId)){
				currentPanelId = reportItem.getPanel().getId();
				reportItem.setSeparator(true);
			}else if( reportItem.getPanel() == null && currentPanelId != null){
				currentPanelId = null;
				reportItem.setSeparator(true);
			}

            int hyphenPosition = reportItem.getAccessionNumber().lastIndexOf("-");
            reportItem.setAccessionNumber(reportItem.getAccessionNumber().substring(0, hyphenPosition));
			reportItem.setCompleteFlag(StringUtil.getMessageForKey(sampleCompleteMap.get(reportItem.getAccessionNumber()) ? "report.status.complete" : "report.status.partial"));
		}
	}

	@Override
	protected String getReportNameForParameterPage() {
		return StringUtil.getMessageForKey("openreports.patientTestStatus");
	}

	public JRDataSource getReportDataSource() throws IllegalStateException {
		if (!initialized) {
			throw new IllegalStateException("initializeReport not called first");
		}

		return errorFound ? new JRBeanCollectionDataSource(errorMsgs) : new JRBeanCollectionDataSource(reportItems);
	}

	@Override
	protected String getSiteLogo() {
		return isLNSP ? "HaitiLNSP.jpg" : "labLogo.jpg";
	}

	@Override
	protected void initializeReportItems() {
		super.initializeReportItems();
		clinicalReportItems = new ArrayList<HaitiClinicalPatientData>();
	}

	@Override
	protected void setReferredResult(HaitiClinicalPatientData data, Result result) {
		data.setResult(data.getResult() + (augmentResultWithFlag() ? getResultFlag(result, "E") : ""));
		data.setAlerts(getResultFlag(result, "E"));
	}

	@Override
	protected boolean appendUOMToRange() {
		return false;
	}

	@Override
	protected boolean augmentResultWithFlag() {
		return false;
	}

	@Override
	protected boolean useReportingDescription() {
		return false;
	}
}

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

package us.mn.state.health.lims.reports.action.implementation;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.validator.GenericValidator;

import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.reports.action.implementation.reportBeans.EIDReportData;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sampleorganization.dao.SampleOrganizationDAO;
import us.mn.state.health.lims.sampleorganization.daoimpl.SampleOrganizationDAOImpl;
import us.mn.state.health.lims.sampleorganization.valueholder.SampleOrganization;

public abstract class PatientEIDReport extends RetroCIPatientReport {


    protected static final long YEAR = 1000L * 60L * 60L * 24L * 365L;
	protected static final long THREE_YEARS = YEAR * 3L;
	protected static final long WEEK = YEAR / 52L;
	protected static final long MONTH = YEAR / 12L;

	protected List<EIDReportData> reportItems;

	protected void initializeReportItems() {
		reportItems = new ArrayList<EIDReportData>();
	}

	protected String getReportNameForReport() {
		return StringUtil.getMessageForKey("reports.label.patient.EID");
	}

	public JRDataSource getReportDataSource() throws IllegalStateException {
		if (!initialized) {
			throw new IllegalStateException("initializeReport not called first");
		}

		return errorFound ? new JRBeanCollectionDataSource(errorMsgs) : new JRBeanCollectionDataSource(reportItems);
	}

	protected void createReportItems() {
		EIDReportData data = new EIDReportData();

		setPatientInfo(data);
		setTestInfo(data);

		reportItems.add(data);

	}

	@SuppressWarnings("unchecked")
	protected void setTestInfo(EIDReportData data) {
		boolean atLeastOneAnalysisNotValidated = false;
		AnalysisDAO analysisDAO = new AnalysisDAOImpl();
		List<Analysis> analysisList = analysisDAO.getAnalysesBySampleId(reportSample.getId());
		DictionaryDAO dictionaryDAO = new DictionaryDAOImpl();

		ResultDAO resultDAO = new ResultDAOImpl();

		Date maxCompleationDate = null;
		long maxCompleationTime = 0L;
		String invalidValue = StringUtil.getMessageForKey("report.test.status.inProgress");

		for (Analysis analysis : analysisList) {

			if (analysis.getCompletedDate() != null) {
				if (analysis.getCompletedDate().getTime() > maxCompleationTime) {
					maxCompleationDate = DateUtil.convertTimestampToSqlDate(analysis.getCompletedDate());
					maxCompleationTime = maxCompleationDate.getTime();
				}

			}

			String testName = analysis.getTest().getTestName();

			List<Result> resultList = resultDAO.getResultsByAnalysis(analysis);
			

			boolean valid = ANALYSIS_FINALIZED_STATUS_ID.equals(analysis.getStatusId());
			if (!valid) {
				atLeastOneAnalysisNotValidated = true;
			}

			if (testName.equals("DNA PCR")) {
				if (valid) {
					String resultValue = "";
					if( resultList.size() > 0){
						resultValue = resultList.get( resultList.size() - 1).getValue();
					}
					Dictionary dictionary = new Dictionary();
					dictionary.setId(resultValue);
					dictionaryDAO.getData(dictionary);
					data.setHiv_status(dictionary.getDictEntry());
				} else {
					data.setHiv_status(invalidValue);
				}
			}

		}
		if (maxCompleationDate != null) {
			data.setCompleationdate(DateUtil.convertSqlDateToStringDate(maxCompleationDate));
		}

		String observation = getObservationValues(OBSERVATION_WHICH_PCR_ID);

		if (!GenericValidator.isBlankOrNull(observation)) {
			Dictionary dictionary = new Dictionary();
			dictionary.setId(observation);
			dictionaryDAO.getData(dictionary);
			data.setPcr_type(dictionary.getDictEntry());
		}

		data.setStatus(atLeastOneAnalysisNotValidated ? StringUtil.getMessageForKey("report.status.partial") : StringUtil
				.getMessageForKey("report.status.complete"));
	}

	protected void setPatientInfo(EIDReportData data) {

		SampleOrganizationDAO orgDAO = new SampleOrganizationDAOImpl();

		data.setSubjectno(reportPatient.getNationalId());
		data.setSitesubjectno(reportPatient.getExternalId());
		data.setBirth_date(reportPatient.getBirthDateForDisplay());
		data.setGender(reportPatient.getGender());
		data.setCollectiondate(reportSample.getCollectionDateForDisplay());
		SampleOrganization sampleOrg = new SampleOrganization();
		sampleOrg.setSampleId(reportSample.getId());
		orgDAO.getDataBySample(sampleOrg);
		data.setServicename(sampleOrg.getId() == null ? "" : sampleOrg.getOrganization().getOrganizationName());
		data.setAccession_number(reportSample.getAccessionNumber());

		Timestamp collectionDate = reportSample.getCollectionDate();

		if (collectionDate != null) {
			long collectionTime = collectionDate.getTime() - reportPatient.getBirthDate().getTime();

			if (collectionTime < THREE_YEARS) {
				data.setAgeWeek(String.valueOf((int) Math.floor(collectionTime / WEEK)));
			} else {
				data.setAgeMonth(String.valueOf((int) Math.floor(collectionTime / MONTH)));
			}

		}

	}

	protected String getProjectId() {
		return EID_STUDY_ID;
	}

}

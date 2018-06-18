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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.validator.GenericValidator;

import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.note.util.NoteUtil;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.organization.dao.OrganizationDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.person.dao.PersonDAO;
import us.mn.state.health.lims.person.daoimpl.PersonDAOImpl;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.referencetables.dao.ReferenceTablesDAO;
import us.mn.state.health.lims.referencetables.daoimpl.ReferenceTablesDAOImpl;
import us.mn.state.health.lims.referencetables.valueholder.ReferenceTables;
import us.mn.state.health.lims.reports.action.implementation.reportBeans.ConfirmationData;
import us.mn.state.health.lims.reports.action.implementation.reportBeans.ErrorMessages;
import us.mn.state.health.lims.requester.dao.RequesterTypeDAO;
import us.mn.state.health.lims.requester.dao.SampleRequesterDAO;
import us.mn.state.health.lims.requester.daoimpl.RequesterTypeDAOImpl;
import us.mn.state.health.lims.requester.daoimpl.SampleRequesterDAOImpl;
import us.mn.state.health.lims.requester.valueholder.SampleRequester;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;

public class HaitiConfirmationReport extends HaitiIndicatorReport implements IReportCreator, IReportParameterSetter {

	private List<ConfirmationData> reportItems;
	private static AnalysisDAO analysisDAO = new AnalysisDAOImpl();
	private static SampleItemDAO sampleItemDAO = new SampleItemDAOImpl();
	private static SampleRequesterDAO requesterDAO = new SampleRequesterDAOImpl();
	private static PersonDAO personDAO = new PersonDAOImpl();
	private static ResultDAO resultDAO = new ResultDAOImpl();
	private static DictionaryDAO dictionaryDAO = new DictionaryDAOImpl();
	private static OrganizationDAO organizationDAO = new OrganizationDAOImpl();
	private static String SAMPLE_ITEM_TABLE_ID;
	private static long PERSON_REQUESTER_TYPE_ID;
	private static long ORG_REQUESTER_TYPE_ID;

	static {
		ReferenceTablesDAO refTableDAO = new ReferenceTablesDAOImpl();
		ReferenceTables refTable = new ReferenceTables();
		refTable.setTableName("SAMPLE_ITEM");
		SAMPLE_ITEM_TABLE_ID = refTableDAO.getReferenceTableByName(refTable).getId();

		RequesterTypeDAO requesterTypeDAO = new RequesterTypeDAOImpl();
		PERSON_REQUESTER_TYPE_ID = Long.parseLong(requesterTypeDAO.getRequesterTypeByName("provider").getId());
		ORG_REQUESTER_TYPE_ID = Long.parseLong(requesterTypeDAO.getRequesterTypeByName("organization").getId());
	}

	@Override
	protected String reportFileName() {
		return "ConfirmationSummary";
	}

	public JRDataSource getReportDataSource() throws IllegalStateException {
		return errorFound ? new JRBeanCollectionDataSource(errorMsgs) : new JRBeanCollectionDataSource(reportItems);
	}

	public void initializeReport(BaseActionForm dynaForm) {
		super.initializeReport();
		setDateRange(dynaForm);

		createReportParameters();

		setConfirmationData();
	}

	private void setConfirmationData() {
		reportItems = new ArrayList<ConfirmationData>();

		List<Analysis> qualifiedAnalysis = getAllConfirmationAnalysisInDateRange();

		if (qualifiedAnalysis.isEmpty()) {
			errorFound = true;
			ErrorMessages msgs = new ErrorMessages();
			msgs.setMsgLine1(StringUtil.getMessageForKey("report.error.message.noPrintableItems"));
			errorMsgs.add(msgs);
			return;
		}

		Set<Sample> referredSamples = getSamplesForAnalysis(qualifiedAnalysis);

		for (Sample sample : referredSamples) {
			reportItems.addAll(createConfirmationBeanFromSample(sample));
		}

		Collections.sort(reportItems, new Comparator<ConfirmationData>() {

			@Override
			public int compare(ConfirmationData o1, ConfirmationData o2) {
				int orgCompare = o1.getOrganizationName().compareTo(o2.getOrganizationName());
				if (orgCompare == 0) {
					return o1.getLabAccession().compareTo(o2.getLabAccession());
				} else {
					return orgCompare;
				}
			}
		});
	}

	private List<Analysis> getAllConfirmationAnalysisInDateRange() {
		return analysisDAO
				.getAnalysisStartedOnRangeByStatusId(lowDate, highDate, StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferredIn));
	}

	private Set<Sample> getSamplesForAnalysis(List<Analysis> qualifiedAnalysis) {
		Set<Sample> sampleSet = new HashSet<Sample>();

		for (Analysis analysis : qualifiedAnalysis) {
			sampleSet.add(analysis.getSampleItem().getSample());
		}
		return sampleSet;
	}

	private List<ConfirmationData> createConfirmationBeanFromSample(Sample sample) {

		List<ConfirmationData> dataList = new ArrayList<ConfirmationData>();

		String accessionNumber = sample.getAccessionNumber();
		String orgName = getOrganizationNameForSample(sample);
		Person requester = getRequesterForSample(sample);
		String requestDate = DateUtil.convertSqlDateToStringDate(sample.getReceivedDate());

		List<SampleItem> sampleItemList = sampleItemDAO.getSampleItemsBySampleId(sample.getId());

		for (SampleItem sampleItem : sampleItemList) {
			ConfirmationData data = new ConfirmationData();

			data.setLabAccession(accessionNumber + "-" + sampleItem.getSortOrder());
			data.setSampleType(TypeOfSampleUtil.getTypeOfSampleNameForId(sampleItem.getTypeOfSampleId()));
			data.setOrganizationName(StringUtil.replaceNullWithEmptyString(orgName));
			data.setRequesterAccession(sampleItem.getExternalId());
			data.setNote(getNoteForSampleItem(sampleItem));
			data.setRequesterEMail(StringUtil.replaceNullWithEmptyString(requester.getEmail()));
			data.setRequesterFax(StringUtil.replaceNullWithEmptyString(requester.getFax()));
			data.setRequesterName(StringUtil.replaceNullWithEmptyString(requester.getFirstName()) + " "
					+ StringUtil.replaceNullWithEmptyString(requester.getLastName()));
			data.setRequesterPhone(StringUtil.replaceNullWithEmptyString(requester.getWorkPhone()));
			data.setReceptionDate(requestDate);
			addResults(data, sampleItem);
			dataList.add(data);
		}

		return dataList;
	}

	private Person getRequesterForSample(Sample sample) {
		List<SampleRequester> requesters = requesterDAO.getRequestersForSampleId(sample.getId());

		for (SampleRequester requester : requesters) {
			if (PERSON_REQUESTER_TYPE_ID == requester.getRequesterTypeId()) {
				return personDAO.getPersonById(String.valueOf(requester.getRequesterId()));
			}
		}

		return new Person();
	}

	private String getOrganizationNameForSample(Sample sample) {
		List<SampleRequester> requesters = requesterDAO.getRequestersForSampleId(sample.getId());

		for (SampleRequester requester : requesters) {
			if (ORG_REQUESTER_TYPE_ID == requester.getRequesterTypeId()) {
				return organizationDAO.getOrganizationById(String.valueOf(requester.getRequesterId())).getOrganizationName();
			}
		}

		return "";
	}

	private String getNoteForSampleItem(SampleItem sampleItem) {
		List<Note> notes = NoteUtil.getNotesForObjectAndTable(sampleItem.getId(), SAMPLE_ITEM_TABLE_ID);
		return notes == null || notes.isEmpty() ? "" : notes.get(0).getText();
	}

	private void addResults(ConfirmationData data, SampleItem sampleItem) {
		List<Analysis> analysisList = analysisDAO.getAnalysesBySampleItem(sampleItem);

		List<String> requestTestList = new ArrayList<String>();
		List<String> requestResultList = new ArrayList<String>();
		List<String> labTestList = new ArrayList<String>();
		List<String> labResultList = new ArrayList<String>();
		List<String> compleationDate = new ArrayList<String>();

		for (Analysis analysis : analysisList) {
			if (analysis.getStatusId().equals(StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferredIn))) {
				String testName = analysis.getTest() == null ? StringUtil.getMessageForKey("test.name.notSpecified") : analysis.getTest()
						.getTestName();
				requestTestList.add(testName);
				requestResultList.add(getResultsForAnalysis(analysis));
			} else {
				labTestList.add(analysis.getTest().getTestName());
				labResultList.add(getResultsForAnalysis(analysis));
				compleationDate.add(getCompleationDate(analysis));
			}
		}

		data.setLabResult(labResultList);
		data.setLabTest(labTestList);
		data.setRequesterTest(requestTestList);
		data.setRequesterResult(requestResultList);
		data.setCompleationDate(compleationDate);
	}

	@SuppressWarnings("unchecked")
	private String getResultsForAnalysis(Analysis analysis) {
		List<Result> results = resultDAO.getResultsByAnalysis(analysis);

		if (results != null && !results.isEmpty()) {
			String type = results.get(0).getResultType();

			if ("M".equals(type) || "D".equals(type)) {
				StringBuilder builder = new StringBuilder();
				boolean firstNumber = true;
				for (Result result : results) {
					if (!firstNumber) {
						builder.append(", ");
					}
					firstNumber = false;

					if (!(GenericValidator.isBlankOrNull(result.getValue()) || "0".equals(result.getValue()))) {
						builder.append(dictionaryDAO.getDictionaryById(result.getValue()).getLocalizedName());
					}
				}

				return builder.toString();
			} else {
				return getResultsWithUOM(results.get(0).getValue(), analysis);
			}
		}

		return "";
	}

	private String getResultsWithUOM(String value, Analysis analysis) {
		if (analysis.getTest().getUnitOfMeasure() != null) {
			return value + " " + analysis.getTest().getUnitOfMeasure().getUnitOfMeasureName();
		}

		return value;
	}

	private String getCompleationDate(Analysis analysis) {
		return DateUtil.convertTimestampToStringDate(analysis.getCompletedDate());
	}

	@Override
	protected String getNameForReportRequest() {
		return StringUtil.getMessageForKey("report.confirmation.title");
	}

	@Override
	protected String getSiteLogo(){
		return "HaitiLNSP.jpg";
	}

	@Override
	protected String getNameForReport() {
		return StringUtil.getContextualMessageForKey("report.confirmation.title");
	}

	@Override
	protected String getLabNameLine1() {
		return ConfigurationProperties.getInstance().getPropertyValue(Property.SiteName);
	}

	@Override
	protected String getLabNameLine2() {
		return "";
	}
}

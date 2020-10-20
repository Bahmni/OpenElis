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
package us.mn.state.health.lims.resultvalidation.action;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.feed.service.EventPublishers;
import org.bahmni.feed.openelis.feed.service.impl.OpenElisUrlPublisher;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.note.util.NoteUtil;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.resultvalidation.action.util.ResultValidationPaging;
import us.mn.state.health.lims.resultvalidation.bean.AnalysisItem;
import us.mn.state.health.lims.resultvalidation.util.ResultsValidationUtility;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.OrderStatus;
import us.mn.state.health.lims.systemuser.dao.SystemUserDAO;
import us.mn.state.health.lims.systemuser.daoimpl.SystemUserDAOImpl;
import us.mn.state.health.lims.systemuser.valueholder.SystemUser;
import us.mn.state.health.lims.testanalyte.dao.TestAnalyteDAO;
import us.mn.state.health.lims.testanalyte.daoimpl.TestAnalyteDAOImpl;
import us.mn.state.health.lims.testanalyte.valueholder.TestAnalyte;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

public class ResultValidationSaveAction extends BaseResultValidationAction {

	private static final DecimalFormat TWO_DECIMAL_FORMAT = new DecimalFormat("##.##");
	// DAOs
	private AnalysisDAO analysisDAO = new AnalysisDAOImpl();
	private SampleDAO sampleDAO = new SampleDAOImpl();
	private TestResultDAO testResultDAO = new TestResultDAOImpl();
	private TestAnalyteDAO testAnalyteDAO = new TestAnalyteDAOImpl();
	private ResultDAO resultDAO = new ResultDAOImpl();
	private final NoteDAO noteDAO = new NoteDAOImpl();
    private OpenElisUrlPublisher accessionPublisher = new EventPublishers().accessionPublisher();


    // Update Lists
	private List<Analysis> analysisUpdateList;
    private ArrayList<Note> noteUpdateList;
	private ArrayList<Result> resultUpdateList;

	private SystemUser systemUser;

	private static final String RESULT_TYPE = "I";
	private static final String RESULT_SUBJECT = "Result Note";


    @Override
	protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String forward = FWD_SUCCESS;
        String referer = request.getParameter("referer");
        String accessionNumber = request.getParameter(ACCESSION_NUMBER);

        request.getSession().setAttribute(SAVE_DISABLED, "true");

		BaseActionForm dynaForm = (BaseActionForm) form;

		ResultValidationPaging paging = new ResultValidationPaging();
		paging.updatePagedResults(request, dynaForm);
		List<AnalysisItem> resultItemList = paging.getResults(request);

		String testSectionName = (String) dynaForm.get("testSection");
		String testName = (String) dynaForm.get("testName");
		String accessionNotes = (String) dynaForm.get("accessionNotes");

		createSystemUser();

		noteUpdateList = new ArrayList<>();
		resultUpdateList = new ArrayList<>();
		analysisUpdateList = new ArrayList<>();

		if (testSectionName.equals("serology")) {
			createUpdateElisaList(resultItemList);
		} else {
			createUpdateList(resultItemList);
		}

		try {
			for (Analysis analysis : analysisUpdateList) {
				analysisDAO.updateData(analysis);
			}

			for (Result result : resultUpdateList) {
				resultDAO.updateData(result);
			}
			checkAndUpdateStatusOfFinishedSamples(resultItemList);
            createOrUpdateAccessionNotes(accessionNotes, accessionNumber, referer);
            createOrUpdateNotes(noteUpdateList);
            publishEditedAccessionNumbers(resultItemList, request);
		} catch (LIMSRuntimeException lre) {
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
            throw lre;
		}

        if(referer != null && referer.matches("LabDashboard")) {
			return mapping.findForward(FWD_DASHBOARD);
		}

		if (GenericValidator.isBlankOrNull(testSectionName)) {
            Map<String, String> params = new HashMap<>();

            if (accessionNumber != null && !accessionNumber.trim().isEmpty()) {
                forward =  FWD_SUCCESS_FOR_ACCESSION_NUMBER;
                params.put(ACCESSION_NUMBER, accessionNumber);
            }

            params.put("forward", forward);

            return getForwardWithParameters(mapping.findForward(forward), params);
		} else {
			Map<String, String> params = new HashMap<>();
			params.put("type", testSectionName);
			params.put("test", testName);

            if (accessionNumber != null && !accessionNumber.trim().isEmpty()) {
                forward =  FWD_SUCCESS_FOR_ACCESSION_NUMBER;
                params.put(ACCESSION_NUMBER, accessionNumber);
            }

			params.put("forward", forward);

            return getForwardWithParameters(mapping.findForward(forward), params);
		}

	}

    private void createOrUpdateAccessionNotes(String accessionNotes, String accessionNumber, String referer) {
        if(shouldSaveAccessionNotes(accessionNotes, referer)){
            Note note = new Note();
            note.setReferenceId(sampleDAO.getSampleByAccessionNumber(accessionNumber).getId());
            note.setReferenceTableId(NoteUtil.getTableReferenceId(ResultsValidationUtility.SAMPLE_TABLE_NAME));
            note.setNoteType(RESULT_TYPE);
            note.setSubject(ResultsValidationUtility.ACCESSION_NOTE_SUBJECT);
            note.setText(accessionNotes);
            note.setSysUserId(currentUserId);
            note.setSystemUser(systemUser);
            note.setSystemUserId(currentUserId);
            noteUpdateList.add(note);
        }
    }

    private boolean shouldSaveAccessionNotes(String accessionNotes, String referer) {
        return referer.equals("LabDashboard") && !GenericValidator.isBlankOrNull(accessionNotes);
    }


    private void createOrUpdateNotes(ArrayList<Note> noteUpdateList) {
        for (Note note : noteUpdateList) {
            if (note != null) {
                if (note.getId() == null) {
                    noteDAO.insertData(note);
                } else {
                    noteDAO.updateData(note);
                }
            }
        }
    }

    private void publishEditedAccessionNumbers(List<AnalysisItem> resultItemList, HttpServletRequest request) {
        Set<String> editedAccessionNumbers = new HashSet<>();
        Set<String> editedSampleUuids = new HashSet<>();
        for (AnalysisItem analysisItem : resultItemList) {
            if(analysisItem.getIsAccepted()) {
                editedAccessionNumbers.add(analysisItem.getAccessionNumber());
            }
        }
        for (String accessionNumber : editedAccessionNumbers) {
            Sample sample = sampleDAO.getSampleByAccessionNumber(accessionNumber);
            if(sample != null) {
                editedSampleUuids.add(sample.getUUID());
            }
        }
        accessionPublisher.publish(editedSampleUuids, request.getContextPath());
    }

    private void createUpdateList(List<AnalysisItem> analysisItems) {

		List<String> analysisIdList = new ArrayList<>();

		for (AnalysisItem analysisItem : analysisItems) {
			if (!analysisItem.isReadOnly() && (analysisItem.getIsAccepted() || analysisItem.getIsRejected())) {
				String analysisId = analysisItem.getAnalysisId();

				Analysis analysis = new Analysis();
				analysis.setId(analysisId);
				analysisDAO.getData(analysis);
				analysis.setSysUserId(currentUserId);


				if (!analysisIdList.contains(analysisId)) {

					if (analysisItem.getIsAccepted()) {
						if(StringUtil.isNullorNill(analysisItem.getResult()) && StringUtil.isNullorNill(analysisItem.getUploadedFilePath()) && !analysisItem.isAbnormal())
						{
							analysis.getMarkedAsDone();
						}
						else {
							analysis.finalizeResult();
						}
						analysis.setReleasedDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
						analysisIdList.add(analysisId);
						analysisUpdateList.add(analysis);
					}

					if (analysisItem.getIsRejected()) {
                        analysis.reject();
						analysisIdList.add(analysisId);
						analysisUpdateList.add(analysis);
					}
				}

				if (areResults(analysisItem)) {
					Result result = createResultFromAnalysisItem(analysisItem, analysis);
					resultUpdateList.add(result);
				}
			}
            createNote(analysisItem);
		}
	}

	private void createUpdateElisaList(List<AnalysisItem> resultItems) {

		for (AnalysisItem resultItem : resultItems) {

			if (resultItem.getIsAccepted()) {

				List<Analysis> acceptedAnalysisList = createAnalysisFromElisaAnalysisItem(resultItem);

				for (Analysis analysis : acceptedAnalysisList) {
					analysis.finalizeResult();
					analysisUpdateList.add(analysis);
				}
			}

			if (resultItem.getIsRejected()) {
				List<Analysis> rejectedAnalysisList = createAnalysisFromElisaAnalysisItem(resultItem);

				for (Analysis analysis : rejectedAnalysisList) {
                    analysis.reject();
					analysisUpdateList.add(analysis);
				}

			}
		}
	}

	private List<Analysis> createAnalysisFromElisaAnalysisItem(AnalysisItem analysisItem) {

		List<Analysis> analysisList = new ArrayList<>();

		Analysis analysis = new Analysis();

		if (!GenericValidator.isBlankOrNull(analysisItem.getMurexResult())) {
			analysis = getAnalysisFromId(analysisItem.getMurexAnalysisId());
			analysisList.add(analysis);
		}
		if (!GenericValidator.isBlankOrNull(analysisItem.getBiolineResult())) {
			analysis = getAnalysisFromId(analysisItem.getBiolineAnalysisId());
			analysisList.add(analysis);
		}
		if (!GenericValidator.isBlankOrNull(analysisItem.getIntegralResult())) {
			analysis = getAnalysisFromId(analysisItem.getIntegralAnalysisId());
			analysisList.add(analysis);
		}
		if (!GenericValidator.isBlankOrNull(analysisItem.getVironostikaResult())) {
			analysis = getAnalysisFromId(analysisItem.getVironostikaAnalysisId());
			analysisList.add(analysis);
		}
		if (!GenericValidator.isBlankOrNull(analysisItem.getGenieIIResult())) {
			analysis = getAnalysisFromId(analysisItem.getGenieIIAnalysisId());
			analysisList.add(analysis);
		}
		if (!GenericValidator.isBlankOrNull(analysisItem.getGenieII10Result())) {
			analysis = getAnalysisFromId(analysisItem.getGenieII10AnalysisId());
			analysisList.add(analysis);
		}
		if (!GenericValidator.isBlankOrNull(analysisItem.getGenieII100Result())) {
			analysis = getAnalysisFromId(analysisItem.getGenieII100AnalysisId());
			analysisList.add(analysis);
		}
		if (!GenericValidator.isBlankOrNull(analysisItem.getWesternBlot1Result())) {
			analysis = getAnalysisFromId(analysisItem.getWesternBlot1AnalysisId());
			analysisList.add(analysis);
		}
		if (!GenericValidator.isBlankOrNull(analysisItem.getWesternBlot2Result())) {
			analysis = getAnalysisFromId(analysisItem.getWesternBlot2AnalysisId());
			analysisList.add(analysis);
		}
		if (!GenericValidator.isBlankOrNull(analysisItem.getP24AgResult())) {
			analysis = getAnalysisFromId(analysisItem.getP24AgAnalysisId());
			analysisList.add(analysis);
		}if (!GenericValidator.isBlankOrNull(analysisItem.getInnoliaResult())) {
			analysis = getAnalysisFromId(analysisItem.getInnoliaAnalysisId());
			analysisList.add(analysis);
		}

		analysisList.add(analysis);

		return analysisList;
	}

	private void checkAndUpdateStatusOfFinishedSamples(List<AnalysisItem> resultItemList) {
		String currentSampleId = "";
		boolean sampleFinished = true;
		List<Analysis> analysisList;

		for (AnalysisItem analysisItem : resultItemList) {
			String analysisSampleId = sampleDAO.getSampleByAccessionNumber(analysisItem.getAccessionNumber()).getId();
			if (!analysisSampleId.equals(currentSampleId)) {
				currentSampleId = analysisSampleId;
				analysisList = analysisDAO.getAnalysesBySampleId(currentSampleId);

				for (Analysis analysis : analysisList) {
					if (!analysis.isFinished()) {
						sampleFinished = false;
						break;
					}
				}

                Sample sample = new Sample();
                sample.setId(currentSampleId);
                sampleDAO.getData(sample);
                if (sampleFinished) {
                    sample.setStatusId(StatusOfSampleUtil.getStatusID(OrderStatus.Finished));
                    sampleDAO.updateData(sample);
                }
                sampleFinished = true;
			}
        }
    }

	private Analysis getAnalysisFromId(String id) {
		Analysis analysis = new Analysis();
		analysis.setId(id);
		analysisDAO.getData(analysis);
		analysis.setSysUserId(currentUserId);

		return analysis;
	}

	private void createNote(AnalysisItem testResult) {
		Note note = null;

		if (!GenericValidator.isBlankOrNull(testResult.getNoteId())) {
			note = new Note();
			note.setId(testResult.getNoteId());
			noteDAO.getData(note);
		} else if (areNotes(testResult)) {
			note = new Note();
			note.setReferenceId(testResult.getResultId());
			note.setReferenceTableId(ResultsLoadUtility.getResultReferenceTableId());
			note.setNoteType(RESULT_TYPE);
			note.setSubject(RESULT_SUBJECT);
		}

		if (note != null) {
			note.setText(testResult.getNote());
			note.setSysUserId(currentUserId);
			note.setSystemUser(systemUser);
			note.setSystemUserId(currentUserId);
			noteUpdateList.add(note);
		}
	}

	private Result createResultFromAnalysisItem(AnalysisItem analysisItem, Analysis analysis) {

		Result result = new Result();

		if (GenericValidator.isBlankOrNull(analysisItem.getResultId())) {
			result.setAnalysis(analysis);
			result.setAnalysisId(analysisItem.getAnalysisId());
			result.setResultType(analysisItem.getResultType());

			TestAnalyte testAnalyte = getTestAnalyteForResult(result);

			if (testAnalyte != null) {
				result.setAnalyte(testAnalyte.getAnalyte());
			}

		} else {
			result.setId(analysisItem.getResultId());
			resultDAO.getData(result);
		}
		
		TestResult testResult = getTestResult(analysisItem);

		if (testResult != null) {
			result.setTestResult(testResult);
		}
		
		if( analysisItem.getResult() != null && !analysisItem.getResult().equals(result.getValue())){
			String analysisResult = analysisItem.getResult();
			if(analysisItem.isDisplayResultAsLog()){
				try {
					Double value = Math.log10(Double.parseDouble(analysisResult));
					analysisResult += "(" + String.valueOf(Double.valueOf(TWO_DECIMAL_FORMAT.format(value))) + ")";					
				} catch (NumberFormatException e) {
					// no-op use original number
				}
			}
					
			result.setValue(analysisResult);
            result.setAbnormal(analysisItem.isAbnormal());

            analysis.setRevision(String.valueOf(Integer.parseInt(analysis.getRevision()) + 1 ));
			analysis.setEnteredDate(DateUtil.getNowAsTimestamp());
		}
		result.setSysUserId(currentUserId);
		result.setSortOrder("0");

		return result;
	}

	protected TestResult getTestResult(AnalysisItem analysisItem ) {
		TestResult testResult = null;
		if ("D".equals(analysisItem.getResultType())) {
			testResult = testResultDAO.getTestResultsByTestAndDictonaryResult(analysisItem.getTestId(), analysisItem.getResult());
		} else {
			List<TestResult> testResultList = testResultDAO.getTestResultsByTest(analysisItem.getTestId());
			// we are assuming there is only one testResult for a numeric type
			// result
			if (!testResultList.isEmpty()) {
				testResult = testResultList.get(0);
			}
		}
		return testResult;
	}

	private TestAnalyte getTestAnalyteForResult(Result result) {

		if (result.getTestResult() != null) {
			@SuppressWarnings("unchecked")
			List<TestAnalyte> testAnalyteList = testAnalyteDAO.getAllTestAnalytesPerTest(result.getTestResult().getTest());

			if (testAnalyteList.size() > 0) {
				int distanceFromRoot = 0;

				Analysis parentAnalysis = result.getAnalysis().getParentAnalysis();

				while (parentAnalysis != null) {
					distanceFromRoot++;
					parentAnalysis = parentAnalysis.getParentAnalysis();
				}

				int index = Math.min(distanceFromRoot, testAnalyteList.size() - 1);

				return testAnalyteList.get(index);
			}
		}
		return null;
	}

	private boolean areNotes(AnalysisItem item) {
		return !GenericValidator.isBlankOrNull(item.getNote());
	}

	private boolean areResults(AnalysisItem item) {
		return !(GenericValidator.isBlankOrNull(item.getResult()) || ("D".equals(item.getResultType()) && "0".equals(item.getResult())));
	}

	private void createSystemUser() {
		systemUser = new SystemUser();
		systemUser.setId(currentUserId);
		SystemUserDAO systemUserDAO = new SystemUserDAOImpl();
		systemUserDAO.getData(systemUser);
	}

}

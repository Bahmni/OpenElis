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
package us.mn.state.health.lims.referral.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.bahmni.feed.openelis.feed.service.EventPublishers;
import org.bahmni.feed.openelis.feed.service.impl.OpenElisUrlPublisher;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.StaleObjectStateException;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.note.util.NoteUtil;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.organization.dao.OrganizationDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.referral.action.beanitems.IReferralResultTest;
import us.mn.state.health.lims.referral.action.beanitems.ReferralItem;
import us.mn.state.health.lims.referral.action.beanitems.ReferredTest;
import us.mn.state.health.lims.referral.dao.ReferralDAO;
import us.mn.state.health.lims.referral.dao.ReferralResultDAO;
import us.mn.state.health.lims.referral.daoimpl.ReferralDAOImpl;
import us.mn.state.health.lims.referral.daoimpl.ReferralResultDAOImpl;
import us.mn.state.health.lims.referral.valueholder.Referral;
import us.mn.state.health.lims.referral.valueholder.ReferralResult;
import us.mn.state.health.lims.result.action.util.NumericResult;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.daoimpl.ResultFileUploadDaoImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.result.valueholder.ResultType;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.OrderStatus;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ReferredOutUpdateAction extends BaseAction {
    private List<ReferralItem> modifiedItems;
    private List<ReferralItem> canceledItems;
    private Set<Sample> parentSamples;
    private List<Sample> modifiedSamples;
    private ActionErrors errors;

    private final ReferralDAO referralDAO = new ReferralDAOImpl();
    private final ReferralResultDAO referralResultDAO = new ReferralResultDAOImpl();
    private final OrganizationDAO organizationDAO = new OrganizationDAOImpl();

    private boolean alwaysValidate = ConfigurationProperties.getInstance().isPropertyValueEqual(ConfigurationProperties.Property.ALWAYS_VALIDATE_RESULTS, "true");

    private final SampleDAO sampleDAO = new SampleDAOImpl();
    private final AnalysisDAO analysisDAO = new AnalysisDAOImpl();
    private final NoteDAO noteDAO = new NoteDAOImpl();
    private final TestResultDAO testResultDAO = new TestResultDAOImpl();

    private static final String RESULT_SUBJECT = "Result Note";
    private TestDAO testDAO = new TestDAOImpl();
    private ResultsLoadUtility resultsLoadUtility;
    private SampleHumanDAO sampleHumanDAO = new SampleHumanDAOImpl();
    private OpenElisUrlPublisher accessionPublisher = new EventPublishers().accessionPublisher();
    private ResultFileUploadDaoImpl resultFileUploadDao = new ResultFileUploadDaoImpl();


    @Override
    protected String getPageSubtitleKey() {
        return "referral.out.manage";
    }

    @Override
    protected String getPageTitleKey() {
        return "referral.out.manage";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        resultsLoadUtility = new ResultsLoadUtility(currentUserId);
        parentSamples = new HashSet<>();
        modifiedSamples = new ArrayList<>();
        errors = new ActionErrors();

        request.getSession().setAttribute(SAVE_DISABLED, TRUE);
        List<ReferralItem> referralItems = (List<ReferralItem>) PropertyUtils.getProperty(form, "referralItems");
        selectModifiedAndCanceledItems(referralItems);
        validateModifiedItems();

        if (errors.size() > 0) {
            saveErrors(request, errors);
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
        }

        ArrayList<ReferralSet> referralSets;
        ArrayList<ReferralResult> removableReferralResults = new ArrayList<>();
        try {
            referralSets = createReferralSets(removableReferralResults);
        } catch (LIMSRuntimeException e) {
            saveErrors(request, errors);
            request.setAttribute(Globals.ERROR_KEY, errors);

            return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
        }

        try {
            saveOrUpdateReferralResult(referralSets, request.getContextPath());

            for (ReferralResult referralResult : removableReferralResults) {
                referralResult.setSysUserId(currentUserId);
                referralResultDAO.deleteData(referralResult);
            }

            setStatusOfParentSamples();

            for (Sample sample : modifiedSamples) {
                sampleDAO.updateData(sample);
            }

        } catch (LIMSRuntimeException lre) {
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);

            ActionError error;
            if (lre.getException() instanceof StaleObjectStateException) {
                error = new ActionError("errors.OptimisticLockException", null, null);
            } else {
                lre.printStackTrace();
                error = new ActionError("error.system", null, null);
            }

            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(request, errors);
            request.setAttribute(Globals.ERROR_KEY, errors);
            request.setAttribute(ALLOW_EDITS_KEY, "false");
            return mapping.findForward(FWD_FAIL);
        }

        return mapping.findForward(IActionConstants.FWD_SUCCESS);
    }

    private void saveOrUpdateReferralResult(ArrayList<ReferralSet> referralSets, String contextPath) {
        Set<String> accessionsToPublish = new HashSet<>();
        for (ReferralSet referralSet : referralSets) {
            referralDAO.updateData(referralSet.referral);
            for (ReferralResult referralResult : referralSet.existingReferralResults) {
                referralResultDAO.saveOrUpdateData(referralResult);
                addToPublishSet(referralResult,accessionsToPublish);
            }

            for (ReferralResult referralResult : referralSet.newReferralResults) {
                referralResultDAO.saveOrUpdateData(referralResult);
                addToPublishSet(referralResult,accessionsToPublish);
            }

            if (referralSet.note != null) {
                if (referralSet.note.getId() == null) {
                    noteDAO.insertData(referralSet.note);
                } else {
                    noteDAO.updateData(referralSet.note);
                }
            }
        }

        publishSavedReferralResult(accessionsToPublish,contextPath);

    }

    private void addToPublishSet(ReferralResult referralResult, Set<String> accessionsToPublish) {
        if(canPublishReferralResult(referralResult)){
            accessionsToPublish.add(referralResult.getResult().getAnalysis().getSampleItem().getSample().getUUID());
        }
    }

    private boolean canPublishReferralResult(ReferralResult referralResult) {
        return StatusOfSampleUtil.isPublishableAnalysis(referralResult.getResult().getAnalysis().getStatusId());
    }

    private void publishSavedReferralResult(Set<String> accessionsToPublish,String contextPath) {
        accessionPublisher.publish(accessionsToPublish,contextPath);
    }

    private void selectModifiedAndCanceledItems(List<ReferralItem> referralItems) {
        modifiedItems = new ArrayList<>();
        canceledItems = new ArrayList<>();

        for (ReferralItem item : referralItems) {
            if (item.isCanceled()) {
                canceledItems.add(item);
            } else if (item.isModified()) {
                modifiedItems.add(item);
            }
        }
    }

    private void validateModifiedItems() {
        for (ReferralItem referralItem : modifiedItems) {
            validateModifedItem(referralItem);
        }
    }

    private void validateModifedItem(ReferralItem referralItem) {
        // if an institution has not been entered then there may not be a test
        if (!institutionEntered(referralItem) && testEntered(referralItem)) {
            ActionError error = new ActionError("error.referral.missingInstitution", referralItem.getAccessionNumber(), null);
            errors.add(ActionErrors.GLOBAL_MESSAGE, error);
        }

        if (referralItem.isFailedValidation() && StringUtils.isBlank(referralItem.getNote())){
            ActionError error = new ActionError("error.referral.missingNote", referralItem.getAccessionNumber(), null);
            errors.add(ActionErrors.GLOBAL_MESSAGE, error);
        }

        // if a test has not been entered then there can not be a result or
        // report date
        if (!testEntered(referralItem) && (reportDateEntered(referralItem) || resultEntered(referralItem))) {
            ActionError error = new ActionError("error.referral.missingTest", referralItem.getAccessionNumber(), null);
            errors.add(ActionErrors.GLOBAL_MESSAGE, error);
        }

        if (!GenericValidator.isBlankOrNull(referralItem.getReferredResult()) && "N".equals(referralItem.getReferredResultType())) {
            List<ActionError> actionErrors = new NumericResult(referralItem.getReferredResult()).validate();
            for (ActionError actionError : actionErrors) {
                errors.add(ActionErrors.GLOBAL_MESSAGE, actionError);
            }
        }
    }

    private boolean institutionEntered(ReferralItem referralItem) {
        return !"0".equals(referralItem.getReferredInstituteId());
    }

    private boolean testEntered(IReferralResultTest resultTest) {
        return !"0".equals(resultTest.getReferredTestId());
    }

    private boolean reportDateEntered(IReferralResultTest resultTest) {
        return !GenericValidator.isBlankOrNull(resultTest.getReferredReportDate());
    }

    private boolean resultEntered(IReferralResultTest resultTest) {
        return !(GenericValidator.isBlankOrNull(resultTest.getReferredResult()) && "0".equals(resultTest.getReferredDictionaryResult())) || (resultTest.getUploadedFile() != null);
    }

    private ArrayList<ReferralSet> createReferralSets(ArrayList<ReferralResult> removableReferralResults) throws LIMSRuntimeException {
        ArrayList<ReferralSet> referralSetList = new ArrayList<>();

        for (ReferralItem item : canceledItems) {
            referralSetList.add(createCanceledReferralSet(item));
        }

        for (ReferralItem item : modifiedItems) {
            referralSetList.add(createModifiedSet(item, removableReferralResults));
        }
        return referralSetList;
    }

    private ReferralSet createCanceledReferralSet(ReferralItem item) {
        Referral referral = referralDAO.getReferralById(item.getReferralId());
        referral.cancelReferOut(currentUserId);

        Sample sample = referral.getAnalysis().getSampleItem().getSample();
        parentSamples.add(sample);

        ReferralSet referralSet = new ReferralSet();
        referralSet.referral = referral;
        return referralSet;
    }

    private ReferralSet createModifiedSet(ReferralItem referralItem, ArrayList<ReferralResult> removableReferralResults) throws LIMSRuntimeException {
        // place all existing referral results in list
        ReferralSet referralSet = new ReferralSet();
        referralSet.setDbReferralResults(referralResultDAO.getReferralResultsForReferral(referralItem.getReferralId()));
        Referral referral = referralDAO.getReferralById(referralItem.getReferralId());

        referralSet.referral = referral;
        referral.setCanceled(false);
        referral.setSysUserId(currentUserId);
        referral.setOrganization(organizationDAO.getOrganizationById(referralItem.getReferredInstituteId()));
        referral.setSentDate(DateUtil.convertStringDateToTruncatedTimestamp(referralItem.getReferredSendDate()));
        referral.setRequesterName(referralItem.getReferrer());

        referralSet.note = NoteUtil.createSavableNote(null,
                referralItem.getNote(),
                referralItem.getCasualResultId(),
                ResultsLoadUtility.getResultReferenceTableId(),
                RESULT_SUBJECT,
                currentUserId);
        if (resultEntered(referralItem)) {
            createReferralResults(referralItem, referralSet);

            if (referralItem.getAdditionalTests() != null) {
                for (ReferredTest existingAdditionalTest : referralItem.getAdditionalTests()) {
                    // nothing to do, because on insert we reused what we could
                    // then deleted all old referralResults (see below).
                    // removableReferralResults.add(getRemovableReferralableResults(existingAdditionalTest));
                    if (!existingAdditionalTest.isRemove()) {
                        createReferralResults(existingAdditionalTest, referralSet);
                    }
                }
            }
        }

        List<ReferredTest> newAdditionalTests = getNewTests(referralItem.getAdditionalTestsXMLWad());

        for (ReferredTest newReferralTest : newAdditionalTests) {
            newReferralTest.setReferralId(referralItem.getReferralId());
            createReferralResults(newReferralTest, referralSet);
        }

        // any leftovers get deleted
        removeReferralResults(removableReferralResults, referralSet);

        return referralSet;
    }

    private void removeReferralResults(ArrayList<ReferralResult> removableReferralResults, ReferralSet referralSet) {
        List<ReferralResult> dbReferralResults = referralSet.getDbReferralResults();
        for (ReferralResult dbReferralResult : dbReferralResults) {
            Result result = dbReferralResult.getResult();
            if (result.getValue()!= null && !result.getValue().isEmpty()){
                removableReferralResults.add(dbReferralResult);
            }
        }
    }

    /**
     * Reuse any existing referrableResults, placing the submitted results in
     * them. Then any remaining referral results are removable.
     * *
     */
    private void createReferralResults(IReferralResultTest referralItem, ReferralSet referralSet) {
        Test test = testDAO.getTestById(referralItem.getReferredTestId());
        Sample sample = referralDAO.getReferralById(referralItem.getReferralId()).getAnalysis().getSampleItem().getSample();
        Patient patient = sampleHumanDAO.getPatientForSample(sample);

        String referredResultType = getReferredResultType(referralItem, test);
        ResultLimit limit = resultsLoadUtility.getResultLimitForTestAndPatient(test, patient);

        saveUploadedFile(referralItem);

        if (ResultType.MultiSelect.code().equals(referredResultType)) {
            String multiResult = referralItem.getReferredMultiDictionaryResult();
            multiResult = (multiResult != null) ? multiResult : "";

            // Is this where I'm falling apart?
            String[] ids = multiResult.trim().split(",");
            for (String id : ids) {
                ReferralResult referralResult = referralSet.getNextReferralResult();
                referralItem.setReferredDictionaryResult(id);  // move particular multi result into (single) dictionary result.
                try {
                    referralResult.fillResult(referralItem, currentUserId, limit, referredResultType);
                } catch (LIMSRuntimeException e) {
                    ActionError error = new ActionError(e.getMessage());
                    errors.add(ActionErrors.GLOBAL_MESSAGE, error);
                }
            }
        } else {
            ReferralResult referralResult = referralSet.getNextReferralResult();
            referralResult.fillResult(referralItem, currentUserId, limit, referredResultType);
            if(!alwaysValidate){
                referralResult.getResult().getAnalysis().finalizeResult();
            }
        }
    }

    private void saveUploadedFile(IReferralResultTest referralItem) {
        try {
            String encodedFileName = resultFileUploadDao.upload(referralItem.getUploadedFile());
            referralItem.setUploadedFileName(encodedFileName);
        } catch (IOException | URISyntaxException e) {
            referralItem.setUploadedFileName(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getReferredResultType(IReferralResultTest referredTest, Test test) {
        /* referredTest.getReferredResultType() is not always accurate
         * alpha-numeric and numeric are not differentiated
		 */

        String referredResultType = referredTest.getReferredResultType();

        if (!"D".equals(referredResultType) && !"M".equals(referredResultType) && test != null) {
            @SuppressWarnings("unchecked")
            List<TestResult> testResults = testResultDAO.getAllTestResultsPerTest(test);

            if (!testResults.isEmpty()) {
                referredResultType = testResults.get(0).getTestResultType();
            }

        }

        return referredResultType;
    }

    @SuppressWarnings("unchecked")
    private List<ReferredTest> getNewTests(String xml) {
        List<ReferredTest> newTestList = new ArrayList<>();

        if (GenericValidator.isBlankOrNull(xml)) {
            return newTestList;
        }

        try {
            Document testsDom = DocumentHelper.parseText(xml);

            for (Iterator<Element> i = testsDom.getRootElement().elementIterator("test"); i.hasNext(); ) {
                Element testItem = i.next();

                String testId = testItem.attribute("testId").getValue();
                String resultType = testItem.attribute("resultType").getValue();
                String value = testItem.attribute("result").getValue();
                String reported = testItem.attribute("report").getValue();

                ReferredTest referralTest = new ReferredTest();
                referralTest.setReferredTestId(testId);
                referralTest.setReferredResultType(resultType);
                referralTest.setReferredResult(value);
                referralTest.setReferredDictionaryResult(value);
                referralTest.setReferredMultiDictionaryResult(value);
                referralTest.setReferredReportDate(reported);

                newTestList.add(referralTest);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new LIMSRuntimeException(e);
        }

        return newTestList;
    }

    private void setStatusOfParentSamples() {
        for (Sample sample : parentSamples) {
            List<Analysis> analysisList = analysisDAO.getAnalysesBySampleId(sample.getId());

            String finalizedId = StatusOfSampleUtil.getStatusID(AnalysisStatus.Finalized);
            boolean allAnalysisFinished = true;

            if (analysisList != null) {
                for (Analysis childAnalysis : analysisList) {
                    Referral referral = referralDAO.getReferralByAnalysisId(childAnalysis.getId());
                    List<ReferralResult> referralResultList;

                    if (referral == null || referral.getId() == null) {
                        referralResultList = new ArrayList<>();
                    } else {
                        referralResultList = referralResultDAO.getReferralResultsForReferral(referral.getId());
                    }

                    if (referralResultList.isEmpty()) {
                        if (!finalizedId.equals(childAnalysis.getStatusId())) {
                            allAnalysisFinished = false;
                            break;
                        }
                    } else {
                        for (ReferralResult referralResult : referralResultList) {
                            if (referralResult.getResult() == null || GenericValidator.isBlankOrNull(referralResult.getResult().getValue())) {
                                assert referral != null;
                                if (!(referral.isCanceled() && finalizedId.equals(childAnalysis.getStatusId()))) {
                                    allAnalysisFinished = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (allAnalysisFinished) {
                sample.setStatusId(StatusOfSampleUtil.getStatusID(OrderStatus.Finished));
                sample.setSysUserId(currentUserId);
                modifiedSamples.add(sample);
            }
        }
    }

    class ReferralSet {
        private Referral referral;
        private Note note;
        private List<ReferralResult> existingReferralResults = new ArrayList<>();
        private List<ReferralResult> newReferralResults = new ArrayList<>();
        private List<ReferralResult> dbReferralResults = new ArrayList<>();

        public List<ReferralResult> getDbReferralResults() {
            return dbReferralResults;
        }

        public void setDbReferralResults(List<ReferralResult> dbReferralResults) {
            this.dbReferralResults = dbReferralResults;
        }

        ReferralResult getNextReferralResult() {
            ReferralResult referralResult;
            if (dbReferralResults.size() > 0) {
                referralResult = dbReferralResults.remove(0);
                existingReferralResults.add(referralResult);
            } else {
                referralResult = new ReferralResult();
                newReferralResults.add(referralResult);
            }
            return referralResult;
        }
    }
}

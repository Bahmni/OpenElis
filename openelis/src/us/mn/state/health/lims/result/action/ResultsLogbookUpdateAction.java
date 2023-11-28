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
package us.mn.state.health.lims.result.action;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.upload.FormFile;
import org.bahmni.feed.openelis.feed.service.EventPublishers;
import org.bahmni.feed.openelis.feed.service.impl.OpenElisUrlPublisher;
import org.hibernate.HibernateException;
import org.hibernate.StaleObjectStateException;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.formfields.FormFields;
import us.mn.state.health.lims.common.formfields.FormFields.Field;
import us.mn.state.health.lims.common.services.IResultSaveService;
import us.mn.state.health.lims.common.services.registration.ResultUpdateRegister;
import us.mn.state.health.lims.common.services.registration.interfaces.IResultUpdate;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.note.util.NoteUtil;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.organization.dao.OrganizationDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.referral.dao.ReferralDAO;
import us.mn.state.health.lims.referral.dao.ReferralResultDAO;
import us.mn.state.health.lims.referral.dao.ReferralTypeDAO;
import us.mn.state.health.lims.referral.daoimpl.ReferralDAOImpl;
import us.mn.state.health.lims.referral.daoimpl.ReferralResultDAOImpl;
import us.mn.state.health.lims.referral.daoimpl.ReferralTypeDAOImpl;
import us.mn.state.health.lims.referral.valueholder.Referral;
import us.mn.state.health.lims.referral.valueholder.ReferralResult;
import us.mn.state.health.lims.referral.valueholder.ReferralType;
import us.mn.state.health.lims.result.action.util.ResultSet;
import us.mn.state.health.lims.result.action.util.ResultUtil;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.action.util.ResultsPaging;
import us.mn.state.health.lims.result.action.util.ResultsValidation;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.dao.ResultInventoryDAO;
import us.mn.state.health.lims.result.dao.ResultSignatureDAO;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.daoimpl.ResultFileUploadDaoImpl;
import us.mn.state.health.lims.result.daoimpl.ResultInventoryDAOImpl;
import us.mn.state.health.lims.result.daoimpl.ResultSignatureDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.result.valueholder.ResultInventory;
import us.mn.state.health.lims.result.valueholder.ResultSignature;
import us.mn.state.health.lims.resultlimits.dao.ResultLimitDAO;
import us.mn.state.health.lims.resultlimits.daoimpl.ResultLimitDAOImpl;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.OrderStatus;
import us.mn.state.health.lims.systemuser.daoimpl.SystemUserDAOImpl;
import us.mn.state.health.lims.systemuser.valueholder.SystemUser;
import us.mn.state.health.lims.test.beanItems.TestResultItem;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testanalyte.valueholder.TestAnalyte;
import us.mn.state.health.lims.testreflex.action.util.TestReflexBean;
import us.mn.state.health.lims.testreflex.action.util.TestReflexUtil;
import us.mn.state.health.lims.testresult.dao.TestResultDAO;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.teststatus.dao.TestStatusDAO;
import us.mn.state.health.lims.teststatus.daoimpl.TestStatusDAOImpl;
import us.mn.state.health.lims.teststatus.valueholder.TestStatus;
import us.mn.state.health.lims.typeofteststatus.dao.TypeOfTestStatusDAO;
import us.mn.state.health.lims.typeofteststatus.daoimpl.TypeOfTestStatusDAOImpl;
import us.mn.state.health.lims.typeofteststatus.valueholder.TypeOfTestStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ResultsLogbookUpdateAction extends BaseAction implements IResultSaveService {

    private List<TestResultItem> modifiedItems;
    private List<ResultSet> modifiedResults;
    private List<ResultSet> newResults;
    private List<Analysis> modifiedAnalysis;
    private List<Result> deletableResults;
    private AnalysisDAO analysisDAO = new AnalysisDAOImpl();
    private ResultDAO resultDAO = new ResultDAOImpl();
    private TestResultDAO testResultDAO = new TestResultDAOImpl();
    private ResultSignatureDAO resultSigDAO = new ResultSignatureDAOImpl();
    private ResultInventoryDAO resultInventoryDAO = new ResultInventoryDAOImpl();
    private NoteDAO noteDAO = new NoteDAOImpl();
    private SampleDAO sampleDAO = new SampleDAOImpl();
    private SampleHumanDAO sampleHumanDAO = new SampleHumanDAOImpl();
    private ReferralDAO referralDAO = new ReferralDAOImpl();
    private ReferralResultDAO referralResultDAO = new ReferralResultDAOImpl();
    private ResultLimitDAO resultLimitDAO = new ResultLimitDAOImpl();
    private static Logger logger = LogManager.getLogger(ResultsLogbookUpdateAction.class);
    private static final String RESULT_SUBJECT = "Result Note";
    private OrganizationDAO organizationDAO = new OrganizationDAOImpl();

    private static String REFERRAL_CONFORMATION_ID;

    private boolean useTechnicianName = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.resultTechnicianName, "true");
    private boolean alwaysValidate = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.ALWAYS_VALIDATE_RESULTS, "true");
    private boolean supportReferrals = FormFields.getInstance().useField(Field.ResultsReferral);
    private String statusRuleSet = ConfigurationProperties.getInstance().getPropertyValueUpperCase(Property.StatusRules);
    private Analysis previousAnalysis;
    private ResultsValidation resultValidation = new ResultsValidation();
    private OpenElisUrlPublisher accessionPublisher = new EventPublishers().accessionPublisher();

    static {
        ReferralTypeDAO referralTypeDAO = new ReferralTypeDAOImpl();
        ReferralType referralType = referralTypeDAO.getReferralTypeByName("Confirmation");
        if (referralType != null) {
            REFERRAL_CONFORMATION_ID = referralType.getId();
        }
    }

    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String forward = FWD_SUCCESS;
        String referer = request.getParameter("referer");
        List<IResultUpdate> updaters = ResultUpdateRegister.getRegisteredUpdaters();

        BaseActionForm dynaForm = (BaseActionForm) form;

        resultValidation.setSupportReferrals(supportReferrals);
        resultValidation.setUseTechnicianName(useTechnicianName);

        ResultsPaging paging = new ResultsPaging();
        paging.updatePagedResults(request, dynaForm);
        List<TestResultItem> tests = paging.getResults(request);

        setModifiedItems(tests);

        ActionMessages errors = resultValidation.validateModifiedItems(modifiedItems);

        if (errors.size() > 0) {
            saveErrors(request, errors);
            request.setAttribute(Globals.ERROR_KEY, errors);

            return mapping.findForward(FWD_VALIDATION_ERROR);
        }

        initializeLists();
        createResultsFromItems();


        try {
            for (ResultSet resultSet : newResults) {

                resultDAO.insertData(resultSet.result);

                if (resultSet.signature != null) {
                    resultSet.signature.setResultId(resultSet.result.getId());
                    resultSigDAO.insertData(resultSet.signature);
                }

                if (resultSet.testKit != null && resultSet.testKit.getInventoryLocationId() != null) {
                    resultSet.testKit.setResultId(resultSet.result.getId());
                    resultInventoryDAO.insertData(resultSet.testKit);
                }

                if (resultSet.note != null) {
                    resultSet.note.setReferenceId(resultSet.result.getId());
                    noteDAO.insertData(resultSet.note);
                }

                if (resultSet.newReferral != null) {
                    insertNewReferralAndReferralResult(resultSet);
                }


            }



            for (ResultSet resultSet : modifiedResults) {
                resultDAO.updateData(resultSet.result);

                if (resultSet.signature != null) {
                    resultSet.signature.setResultId(resultSet.result.getId());
                    if (resultSet.alwaysInsertSignature) {
                        resultSigDAO.insertData(resultSet.signature);
                    } else {
                        resultSigDAO.updateData(resultSet.signature);
                    }
                }

                if (resultSet.testKit != null && resultSet.testKit.getInventoryLocationId() != null) {
                    resultSet.testKit.setResultId(resultSet.result.getId());
                    if (resultSet.testKit.getId() == null) {
                        resultInventoryDAO.insertData(resultSet.testKit);
                    } else {
                        resultInventoryDAO.updateData(resultSet.testKit);
                    }
                }

                if (resultSet.note != null) {
                    resultSet.note.setReferenceId(resultSet.result.getId());
                    if (resultSet.note.getId() == null) {
                        noteDAO.insertData(resultSet.note);
                    } else {
                        noteDAO.updateData(resultSet.note);
                    }
                }

                if (resultSet.newReferral != null) {
                    // we can't just create a referral with a blank result,
                    // because referral page assumes a referralResult and a
                    // result.
                    insertNewReferralAndReferralResult(resultSet);
                }

                if (resultSet.existingReferral != null) {
                    referralDAO.updateData(resultSet.existingReferral);
                }

            }



            for (Analysis analysis : modifiedAnalysis) {
                analysisDAO.updateData(analysis);
            }

            removeDeletedResults();

            setTestReflexes();

            setSampleStatus();

            publishFinalizedResultAccessionNumber(newResults, request);
            publishFinalizedResultAccessionNumber(modifiedResults, request);

            for (IResultUpdate updater : updaters) {
                updater.transactionalUpdate(this);
            }

        } catch (LIMSRuntimeException lre) {
            logger.error("Could not update Results", lre);
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);


            ActionError error = null;

            if (lre instanceof LIMSDuplicateRecordException) {
                //error = new ActionError("errors.StaleViewException", ((LIMSDuplicateRecordException) lre).getObjectDescription(), "http://google.com?q=mujir", null);
                error = new ActionError("errors.StaleViewException", null, null);
            }

            if (lre.getException() instanceof StaleObjectStateException) {
                error = new ActionError("errors.OptimisticLockException", null, null);
            }

            if (error == null) {
                lre.printStackTrace();
                error = new ActionError("errors.UpdateException", null, null);
            }

            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(request, errors);
            request.setAttribute(Globals.ERROR_KEY, errors);

            return mapping.findForward("error");
        }

        try {
            for (IResultUpdate updater : updaters) {
                updater.postTransactionalCommitUpdate(this);
            }
        } catch (HibernateException e) {
            request.setAttribute(IActionConstants.REQUEST_FAILED, true);
        }


        setSuccessFlag(request, forward);

        if (referer != null && referer.matches("LabDashboard")) {
            return mapping.findForward(FWD_DASHBOARD);
        }

        if (GenericValidator.isBlankOrNull(dynaForm.getString("logbookType"))) {
            return mapping.findForward(forward);
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", dynaForm.getString("logbookType"));
            params.put("forward", forward);
            return getForwardWithParameters(mapping.findForward(forward), params);
        }
    }

    private void publishFinalizedResultAccessionNumber(List<ResultSet> resultSets, HttpServletRequest request) {
        accessionPublisher.publish(getAccessionNumbersToPublish(resultSets), request.getContextPath());
    }

    private Set<String> getAccessionNumbersToPublish(List<ResultSet> resultSets) {
        Set<String> accessionNumbers = new HashSet();
        for (ResultSet resultSet : resultSets) {
            if (StatusOfSampleUtil.isPublishableAnalysis(resultSet.result.getAnalysis().getStatusId())) {
                accessionNumbers.add(resultSet.sample.getUUID());
            }
        }
        return accessionNumbers;
    }


    private void insertNewReferralAndReferralResult(ResultSet resultSet) {
        if (StringUtils.isNotBlank(resultSet.newReferral.getOrganizationId())) {
            Organization organization = organizationDAO.getOrganizationById(resultSet.newReferral.getOrganizationId());
            organizationDAO.getData(organization);
            resultSet.newReferral.setOrganization(organization);
        }
        referralDAO.insertData(resultSet.newReferral);
        ReferralResult referralResult = new ReferralResult();
        referralResult.setReferralId(resultSet.newReferral.getId());
        referralResult.setSysUserId(currentUserId);
        referralResult.setResult(resultSet.result);
        referralResultDAO.saveOrUpdateData(referralResult);
    }

    private void removeDeletedResults() {
        for (Result result : deletableResults) {
            List<ResultSignature> signatures = resultSigDAO.getResultSignaturesByResult(result);
            List<ReferralResult> referrals = referralResultDAO.getReferralsByResultId(result.getId());
            SystemUser systemUser = new SystemUserDAOImpl().getUserById(currentUserId);

            for (ResultSignature signature : signatures) {
                signature.setSystemUser(systemUser);
            }

            resultSigDAO.deleteData(signatures);

            for (ReferralResult referral : referrals) {
                referral.setSysUserId(currentUserId);
                referralResultDAO.deleteData(referral);
            }

            result.setSysUserId(currentUserId);
            resultDAO.deleteData(result);
        }

    }

    protected void setTestReflexes() {
        TestReflexUtil testReflexUtil = new TestReflexUtil();
        testReflexUtil.setCurrentUserId(currentUserId);
        testReflexUtil.addNewTestsToDBForReflexTests(convertToTestReflexBeanList(newResults));
        testReflexUtil.updateModifiedReflexes(convertToTestReflexBeanList(modifiedResults));
    }

    private List<TestReflexBean> convertToTestReflexBeanList(List<ResultSet> resultSetList) {
        List<TestReflexBean> reflexBeanList = new ArrayList<TestReflexBean>();

        for (ResultSet resultSet : resultSetList) {
            TestReflexBean reflex = new TestReflexBean();
            reflex.setPatient(resultSet.patient);
            reflex.setReflexSelectionId(resultSet.actionSelectionId);
            reflex.setResult(resultSet.result);
            reflex.setSample(resultSet.sample);
            reflexBeanList.add(reflex);
        }

        return reflexBeanList;
    }

    private void setSampleStatus() {
        Set<Sample> sampleSet = new HashSet<Sample>();

        for (ResultSet resultSet : newResults) {
            sampleSet.add(resultSet.sample);
        }

        String sampleTestingStartedId = StatusOfSampleUtil.getStatusID(OrderStatus.Started);
        String sampleNonConformingId = StatusOfSampleUtil.getStatusID(OrderStatus.NonConforming_depricated);

        for (Sample sample : sampleSet) {
            if (!(sample.getStatusId().equals(sampleNonConformingId) || sample.getStatusId().equals(sampleTestingStartedId))) {
                Sample newSample = new Sample();
                newSample.setId(sample.getId());
                sampleDAO.getData(newSample);

                newSample.setStatusId(sampleTestingStartedId);
                newSample.setSysUserId(currentUserId);
                sampleDAO.updateData(newSample);
            }
        }
    }

    private void setModifiedItems(List<TestResultItem> allItems) {
        modifiedItems = new ArrayList<TestResultItem>();

        for (TestResultItem item : allItems) {
            if(item.getIsReferredOutValueChanged() && item.isReferredOut()){
                item.setReferredOut(false);
                item.setReferralReasonId(null);
                item.setReferralOrganizationId(null);
            }
            if (item.getIsModified() && (
                    ResultUtil.areResults(item) ||
                            ResultUtil.areNotes(item) ||
                            item.isReferredOut() ||
                            ResultUtil.areFiles(item) || item.isTestStatusModified())

            ) {
                modifiedItems.add(item);
            }
        }
    }

    private void createResultsFromItems() {

        for (TestResultItem testResultItem : modifiedItems) {

            Analysis analysis = analysisDAO.getAnalysisById(testResultItem.getAnalysisId());
            createFileForResult(testResultItem);
            List<Result> results = createResultFromTestResultItem(testResultItem, analysis);

            for (Result result : results) {
                addResult(result, testResultItem, analysis);

                if (resultHasValueOrIsReferral(testResultItem, result)) {
                    updateAndAddAnalysisToModifiedList(testResultItem, testResultItem.getTestDate(), analysis);
                }
            }
            TypeOfTestStatus originalTestStatus = testResultItem.getTypeOfTestStatus();
            String originalTestStatusId = "0";
            if(originalTestStatus != null ) {
                originalTestStatusId = originalTestStatus.getId();
            }
            if(!originalTestStatusId.equals(testResultItem.getTypeOfTestStatusId())) {
                updateTestStatus(testResultItem);
            }
        }
    }

    private void updateTestStatus(TestResultItem testResultItem) {


        TestStatusDAO testStatusDAO = new TestStatusDAOImpl();
        TestStatus testStatus = new TestStatus();
        testStatus.setTestId(testResultItem.getTestId());
        testStatus.setTestStatusId(testResultItem.getTypeOfTestStatusId());
        if("0".equals(testResultItem.getTypeOfTestStatusId())) {
            testStatusDAO.deleteData(testStatus);
        } else {
            testStatusDAO.insertOrUpdate(testStatus);
        }
    }

    private void createFileForResult(TestResultItem testResultItem) {
        try {
            String encodedFileName = new ResultFileUploadDaoImpl().upload(testResultItem.getUploadedFile());
            testResultItem.setUploadedFileName(encodedFileName);
        } catch (IOException e) {
            testResultItem.setUploadedFileName(null);
        } catch (URISyntaxException e) {
            testResultItem.setUploadedFileName(null);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initializeLists() {
        modifiedResults = new ArrayList<ResultSet>();
        newResults = new ArrayList<ResultSet>();
        modifiedAnalysis = new ArrayList<Analysis>();
        deletableResults = new ArrayList<Result>();
    }

    protected boolean resultHasValueOrIsReferral(TestResultItem testResultItem, Result result) {
        return result != null && !GenericValidator.isBlankOrNull(result.getValue())
                || (supportReferrals && testResultItem.isReferredOut());
    }

    private void addResult(Result result, TestResultItem testResultItem, Analysis analysis) {
        boolean newResult = result.getId() == null;
        boolean newAnalysisInLoop = analysis != previousAnalysis;

        ResultSignature technicianResultSignature = null;

        if (useTechnicianName && newAnalysisInLoop) {
            technicianResultSignature = createTechnicianSignatureFromResultItem(testResultItem);
        }

        ResultInventory testKit = createTestKitLinkIfNeeded(testResultItem, ResultsLoadUtility.TESTKIT);

        Note note = NoteUtil.createSavableNote(null, testResultItem.getNote(), testResultItem.getResultId(),
                ResultsLoadUtility.getResultReferenceTableId(), RESULT_SUBJECT, currentUserId);


        setAnalysisStatus(testResultItem, analysis);

        analysis.setEnteredDate(DateUtil.getNowAsTimestamp());

        if (newResult) {
            analysis.setEnteredDate(DateUtil.getNowAsTimestamp());
            analysis.setRevision("1");
        } else if (newAnalysisInLoop) {
            analysis.setRevision(String.valueOf(Integer.parseInt(analysis.getRevision()) + 1));
        }

        Sample sample = sampleDAO.getSampleByAccessionNumber(testResultItem.getAccessionNumber());
        Patient patient = null;

        if ("H".equals(sample.getDomain())) {
            SampleHuman sampleHuman = new SampleHuman();
            sampleHuman.setSampleId(sample.getId());
            sampleHumanDAO.getDataBySample(sampleHuman);

            patient = new Patient();
            patient.setId(sampleHuman.getPatientId());
        }

        Referral referral = null;
        Referral existingReferral = null;

        if (supportReferrals) {
            // referredOut means the referral checkbox was checked, repeating
            // analysis means that we have multi-select results, so only do one.
            if (testResultItem.isReferredOut() && newAnalysisInLoop) {
                // If it is a new result or there is no referral ID that means
                // that a new referral has to be created if it was checked and
                // it was canceled then we are un-canceling a canceled referral
                if (newResult || GenericValidator.isBlankOrNull(testResultItem.getReferralId())) {
                    referral = new Referral();
                    referral.setReferralTypeId(REFERRAL_CONFORMATION_ID);
                    referral.setSysUserId(currentUserId);
                    referral.setRequestDate(new Timestamp(new Date().getTime()));
                    referral.setRequesterName(testResultItem.getTechnician());
                    referral.setAnalysis(analysis);
                    referral.setReferralReasonId(testResultItem.getReferralReasonId());
                    referral.setOrganizationId(testResultItem.getReferralOrganizationId());
                } else if (testResultItem.isReferralCanceled()) {
                    existingReferral = referralDAO.getReferralById(testResultItem.getReferralId());
                    existingReferral.setCanceled(false);
                    existingReferral.setSysUserId(currentUserId);
                    existingReferral.setRequesterName(testResultItem.getTechnician());
                    existingReferral.setReferralReasonId(testResultItem.getReferralReasonId());
                }
            }
        }

        String actionSelectionId = testResultItem.getReflexSelectionId();

        if (newResult) {
            newResults.add(new ResultSet(result, technicianResultSignature, testKit, note, patient, sample, actionSelectionId, referral,
                    existingReferral));
        } else {
            modifiedResults.add(new ResultSet(result, technicianResultSignature, testKit, note, patient, sample, actionSelectionId,
                    referral, existingReferral));
        }


        previousAnalysis = analysis;
    }

    private void setAnalysisStatus(TestResultItem testResultItem, Analysis analysis) {
        if (supportReferrals && testResultItem.isReferredOut()) {
            analysis.setStatusId(StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferedOut));
        } else {
            analysis.setStatusId(getStatusForTestResult(testResultItem));
        }
    }

    private String getStatusForTestResult(TestResultItem testResult) {
        if (alwaysValidate || !testResult.isValid()) {
            return StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptance);
        } else {
            ResultLimit resultLimit = resultLimitDAO.getResultLimitById(testResult.getResultLimitId());
            if (resultLimit != null && resultLimit.isAlwaysValidate()) {
                return StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptance);
            }

            return StatusOfSampleUtil.getStatusID(AnalysisStatus.Finalized);
        }
    }

    private ResultInventory createTestKitLinkIfNeeded(TestResultItem testResult, String testKitName) {
        ResultInventory testKit = null;

        if ((TestResultItem.ResultDisplayType.SYPHILIS == testResult.getRawResultDisplayType() || TestResultItem.ResultDisplayType.HIV == testResult
                .getRawResultDisplayType()) && ResultsLoadUtility.TESTKIT.equals(testKitName)) {

            testKit = creatTestKit(testResult, testKitName, testResult.getTestKitId());
        }

        return testKit;
    }

    private ResultInventory creatTestKit(TestResultItem testResult, String testKitName, String testKitId) throws LIMSRuntimeException {
        ResultInventory testKit;
        testKit = new ResultInventory();

        if (!GenericValidator.isBlankOrNull(testKitId)) {
            testKit.setId(testKitId);
            resultInventoryDAO.getData(testKit);
        }

        testKit.setInventoryLocationId(testResult.getTestKitInventoryId());
        testKit.setDescription(testKitName);
        testKit.setSysUserId(currentUserId);
        return testKit;
    }

    private void updateAndAddAnalysisToModifiedList(TestResultItem testResultItem, String testDate, Analysis analysis) {
        String testMethod = testResultItem.getAnalysisMethod();
        analysis.setAnalysisType(testMethod);
        analysis.setStartedDateForDisplay(testDate);

        //This needs to be refactored -- part of the logic is in getStatusForTestResult
        if (statusRuleSet.equals(IActionConstants.STATUS_RULES_RETROCI)) {
            if (analysis.getStatusId() != StatusOfSampleUtil.getStatusID(AnalysisStatus.Canceled)) {
                analysis.setCompletedDate(DateUtil.convertStringDateToSqlDate(testDate));
                analysis.setStatusId(StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptance));
            }
        } else if (analysis.getStatusId() == StatusOfSampleUtil.getStatusID(AnalysisStatus.Finalized) ||
                analysis.getStatusId() == StatusOfSampleUtil.getStatusID(AnalysisStatus.TechnicalAcceptance) ||
                (analysis.getStatusId() == StatusOfSampleUtil.getStatusID(AnalysisStatus.ReferedOut) && !GenericValidator
                        .isBlankOrNull(testResultItem.getResultValue()))) {
            analysis.setCompletedDate(DateUtil.convertStringDateToSqlDate(testDate));
        }

        analysis.setSysUserId(currentUserId);
        modifiedAnalysis.add(analysis);
    }

    @SuppressWarnings("unchecked")
    private List<Result> createResultFromTestResultItem(TestResultItem testResultItem, Analysis analysis) {
        List<Result> results = new ArrayList<Result>();

        if ("M".equals(testResultItem.getResultType())) {
            String[] multiResults = testResultItem.getMultiSelectResultValues().split(",");
            List<Result> existingResults = resultDAO.getResultsByAnalysis(analysis);

            for (int i = 0; i < multiResults.length; i++) {
                Result existingResultFromDB = null;
                for (Result existingResult : existingResults) {
                    if (multiResults[i].equals(existingResult.getValue())) {
                        existingResultFromDB = existingResult;
                        break;
                    }
                }

                if (existingResultFromDB != null) {
                    existingResults.remove(existingResultFromDB);
                    existingResultFromDB.setSysUserId(currentUserId);
                    results.add(existingResultFromDB);
                    continue;
                }
                Result result = new Result();

                setTestResultsForDictionaryResult(testResultItem.getTestId(), multiResults[i], result);
                setNewResultValues(testResultItem, analysis, result);
                setStandardResultValues(multiResults[i], result);
                result.setSortOrder(getResultSortOrder(analysis, result.getValue()));
                result.setAbnormal(testResultItem.getAbnormal());
                if(testResultItem.getUploadedFileName() != null) {
                    result.setUploadedFileName(testResultItem.getUploadedFileName());
                }
                results.add(result);
            }
            deletableResults.addAll(existingResults);
        } else {
            Result result = new Result();
            Result qualifiedResult = null;

            boolean newResult = GenericValidator.isBlankOrNull(testResultItem.getResultId());
            boolean isQualifiedResult = "Q".equals(testResultItem.getResultType());

            if (!newResult) {
                result.setId(testResultItem.getResultId());
                resultDAO.getData(result);
                if (!GenericValidator.isBlankOrNull(testResultItem.getQualifiedResultId())) {
                    qualifiedResult = new Result();
                    qualifiedResult.setId(testResultItem.getQualifiedResultId());
                    resultDAO.getData(qualifiedResult);
                }
            }

            if ("D".equals(testResultItem.getResultType()) || isQualifiedResult) {
                setTestResultsForDictionaryResult(testResultItem.getTestId(), testResultItem.getResultValue(), result);
            } else {
                List<TestResult> testResultList = testResultDAO.getTestResultsByTest(testResultItem.getTestId());
                // we are assuming there is only one testResult for a numeric
                // type result
                if (!testResultList.isEmpty()) {
                    result.setTestResult(testResultList.get(0));
                }
            }

            if (newResult) {
                setNewResultValues(testResultItem, analysis, result);
                if (isQualifiedResult) {
                    qualifiedResult = new Result();
                    setNewResultValues(testResultItem, analysis, qualifiedResult);
                    qualifiedResult.setResultType("A");
                    qualifiedResult.setParentResult(result);
                }
            } else {
                setAnalyteForResult(result);
            }

            setStandardResultValues(testResultItem.getResultValue(), result);
            result.setAbnormal(testResultItem.getAbnormal());
            if(testResultItem.getUploadedFileName() != null){
                result.setUploadedFileName(testResultItem.getUploadedFileName());
            }
            results.add(result);

            if (isQualifiedResult) {
                setStandardResultValues(testResultItem.getQualifiedResultValue(), qualifiedResult);
                results.add(qualifiedResult);
            }
        }

        return results;
    }

    private String getResultSortOrder(Analysis analysis, String resultValue) {
        TestResult testResult = testResultDAO.getTestResultsByTestAndDictonaryResult(analysis.getTest().getId(), resultValue);
        return testResult == null ? "0" : testResult.getSortOrder();
    }

    private void setStandardResultValues(String value, Result result) {
        result.setValue(value);
        result.setSysUserId(currentUserId);
        result.setSortOrder("0");
    }

    private void setNewResultValues(TestResultItem testResultItem, Analysis analysis, Result result) {
        result.setAnalysis(analysis);
        result.setAnalysisId(testResultItem.getAnalysisId());
        result.setIsReportable(testResultItem.getReportable());
        result.setResultType(testResultItem.getResultType());
        result.setMinNormal(testResultItem.getLowerNormalRange());
        result.setMaxNormal(testResultItem.getUpperNormalRange());
        String resultLimitId = testResultItem.getResultLimitId();
        result.setResultLimitId(!StringUtil.isNullorNill(resultLimitId) ? Integer.parseInt(resultLimitId) : null);

        setAnalyteForResult(result);
    }

    private void setAnalyteForResult(Result result) {
        TestAnalyte testAnalyte = ResultUtil.getTestAnalyteForResult(result);

        if (testAnalyte != null) {
            result.setAnalyte(testAnalyte.getAnalyte());
        }
    }

    private TestResult setTestResultsForDictionaryResult(String testId, String dictValue, Result result) {
        TestResult testResult = null;
        testResult = testResultDAO.getTestResultsByTestAndDictonaryResult(testId, dictValue);

        if (testResult != null) {
            result.setTestResult(testResult);
        }

        return testResult;
    }

    private ResultSignature createTechnicianSignatureFromResultItem(TestResultItem testResult) {
        ResultSignature sig = null;

        // The technician signature may be blank if the user changed a
        // conclusion and then changed it back. It will be dirty
        // but will not need a signature
        if (!GenericValidator.isBlankOrNull(testResult.getTechnician())) {
            sig = new ResultSignature();

            if (!GenericValidator.isBlankOrNull(testResult.getTechnicianSignatureId())) {
                sig.setId(testResult.getTechnicianSignatureId());
                resultSigDAO.getData(sig);
            }

            sig.setIsSupervisor(false);
            sig.setNonUserName(testResult.getTechnician());

            SystemUser systemUser = new SystemUserDAOImpl().getUserById(currentUserId);
            sig.setSystemUser(systemUser);
        }
        return sig;
    }


    protected ActionForward getForward(ActionForward forward, String accessionNumber) {
        ActionRedirect redirect = new ActionRedirect(forward);
        if (!StringUtil.isNullorNill(accessionNumber))
            redirect.addParameter(ACCESSION_NUMBER, accessionNumber);

        return redirect;

    }

    protected ActionForward getForward(ActionForward forward, String accessionNumber, String analysisId) {
        ActionRedirect redirect = new ActionRedirect(forward);
        if (!StringUtil.isNullorNill(accessionNumber))
            redirect.addParameter(ACCESSION_NUMBER, accessionNumber);

        if (!StringUtil.isNullorNill(analysisId))
            redirect.addParameter(ANALYSIS_ID, analysisId);

        return redirect;

    }

    @Override
    protected String getPageSubtitleKey() {
        return "banner.menu.results";
    }

    @Override
    protected String getPageTitleKey() {
        return "banner.menu.results";
    }

    @Override
    public String getCurrentUserId() {
        return currentUserId;
    }

    @Override
    public List<ResultSet> getNewResults() {
        return newResults;
    }

    @Override
    public List<ResultSet> getModifiedResults() {
        return modifiedResults;
    }
}

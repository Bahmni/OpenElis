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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.note.util.NoteUtil;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.organization.dao.OrganizationDAO;
import us.mn.state.health.lims.organization.daoimpl.OrganizationDAOImpl;
import us.mn.state.health.lims.organization.util.OrganizationUtils;
import us.mn.state.health.lims.organization.valueholder.Organization;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.referral.action.beanitems.IReferralResultTest;
import us.mn.state.health.lims.referral.action.beanitems.ReferralItem;
import us.mn.state.health.lims.referral.action.beanitems.ReferredTest;
import us.mn.state.health.lims.referral.dao.ReferralDAO;
import us.mn.state.health.lims.referral.dao.ReferralResultDAO;
import us.mn.state.health.lims.referral.daoimpl.ReferralDAOImpl;
import us.mn.state.health.lims.referral.daoimpl.ReferralResultDAOImpl;
import us.mn.state.health.lims.referral.util.ReferralUtil;
import us.mn.state.health.lims.referral.valueholder.Referral;
import us.mn.state.health.lims.referral.valueholder.ReferralResult;
import us.mn.state.health.lims.result.dao.ResultDAO;
import us.mn.state.health.lims.result.daoimpl.ResultDAOImpl;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.result.valueholder.ResultType;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusRules;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.NonNumericTests;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReferredOutAction extends BaseAction {

    private static final String REFERRAL_LAB = "referralLab";
    private static ReferralResultDAO referralResultDAO = new ReferralResultDAOImpl();
    private static TypeOfSampleDAO typeOfSampleDAO = new TypeOfSampleDAOImpl();
    private static ResultDAO resultDAO = new ResultDAOImpl();
    private static SampleHumanDAO sampleHumanDAO = new SampleHumanDAOImpl();
    private static DictionaryDAO dictionaryDAO = new DictionaryDAOImpl();
    private static ReferralDAO referralDAO = new ReferralDAOImpl();
    private List<NonNumericTests> nonNumericTests;
    private static String RESULT_REFERENCE_TABLE_ID = NoteUtil.getTableReferenceId("RESULT");
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Override
    protected String getPageSubtitleKey() {
        return "referral.out.manage";
    }

    @Override
    protected String getPageTitleKey() {
        return "referral.out.manage";
    }

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm dynaForm = (DynaActionForm) form;
        String patientSTNumber = request.getParameter("patientSTNumber");
        int pageNumber = getPageNumber(request);
        int pageSize = getPageSize(request);
        Integer referralResultsCount = getReferralResultsCount();

        setRequestAttributes(request,referralResultsCount);

        request.getSession().setAttribute(SAVE_DISABLED, TRUE);

        List<ReferralItem> referralItems = getReferralItems(patientSTNumber,pageSize,pageNumber);
        PropertyUtils.setProperty(dynaForm, "referralItems", referralItems);
        PropertyUtils.setProperty(dynaForm, "referralReasons", ReferralUtil.getReferralReasons());

        PropertyUtils.setProperty(dynaForm, "referralOrganizations", OrganizationUtils.getReferralOrganizations());

        nonNumericTests = getNonNumericTests(referralItems);
        PropertyUtils.setProperty(dynaForm, "nonNumericTests", nonNumericTests);

        fillInDictionaryValuesForReferralItems(referralItems);

        return mapping.findForward(IActionConstants.FWD_SUCCESS);
    }

    private void setRequestAttributes(HttpServletRequest request, Integer referralResultsCount) {
        int pageNumber = getPageNumber(request);
        int pageSize = getPageSize(request);
        Integer toRecord = pageNumber * pageSize + pageSize <= referralResultsCount?pageNumber * pageSize + pageSize:referralResultsCount;
        // so that it doesnt display 'records 1-0 of 0' when there are no records
        Integer fromRecord = pageNumber * pageSize < toRecord?pageNumber * pageSize +1 : pageNumber * pageSize;
        Boolean previousDisabled = pageNumber >0?false:true;
        Boolean nextDisabled = (pageNumber +1)* pageSize >referralResultsCount?true:false;
        request.setAttribute(IActionConstants.MENU_TOTAL_RECORDS,referralResultsCount);
        request.setAttribute(IActionConstants.MENU_FROM_RECORD,fromRecord);
        request.setAttribute(IActionConstants.MENU_TO_RECORD,toRecord);
        request.setAttribute(IActionConstants.RECORDS_PAGE_SIZE,pageSize);
        request.setAttribute(IActionConstants.PREVIOUS_DISABLED,previousDisabled);
        request.setAttribute(IActionConstants.NEXT_DISABLED,nextDisabled);
    }

    private Integer getReferralResultsCount() {
        return Long.valueOf(referralDAO.getAllUncanceledOpenReferralsCount()).intValue();
    }

    private int getPageSize(HttpServletRequest request) {
        String pageSize = (String) request.getAttribute("pageSize");
        if(StringUtils.isBlank(pageSize)){
            return DEFAULT_PAGE_SIZE;
        }
        return Integer.valueOf(pageSize);
    }

    private int getPageNumber(HttpServletRequest request) {
        String pageNumber = request.getParameter("pageNumber");
        if(StringUtils.isBlank(pageNumber)){
            return 0;
        }
        return Integer.valueOf(pageNumber);
    }

    private void fillInDictionaryValuesForReferralItems(List<ReferralItem> referralItems) {
        for (ReferralItem referralItem : referralItems) {
            String referredResultType = referralItem.getReferredResultType();
            if (isSelectList(referredResultType)) {
                referralItem.setDictionaryResults(getDictionaryValuesForTest(referralItem.getReferredTestId()));
            }

            if (referralItem.getAdditionalTests() != null) {
                for (ReferredTest test : referralItem.getAdditionalTests()) {
                    if (isSelectList(test.getReferredResultType())) {
                        test.setDictionaryResults(getDictionaryValuesForTest(test.getReferredTestId()));
                    }
                }
            }
        }
    }

    private List<ReferralItem> getReferralItems(String patientSTNumber, int pageSize, int pageNumber) {
        List<ReferralItem> referralItems = new ArrayList<>();

        List<Referral> referralList;
        if (StringUtils.isNotBlank(patientSTNumber)) {
            patientSTNumber = patientSTNumber.toUpperCase();
            referralList = referralDAO.getAllUncanceledOpenReferralsByPatientSTNumber(patientSTNumber);
        } else {
            referralList = referralDAO.getAllUncanceledOpenReferrals(pageSize,pageNumber);
        }

        for (Referral referral : referralList) {
            ReferralItem referralItem = getReferralItem(referral);
            if (referralItem != null) {
                referralItems.add(referralItem);
            }
        }
        return referralItems;
    }

    private ReferralItem getReferralItem(Referral referral) {
        List<ReferralResult> referralResults = new ArrayList<>(referral.getReferralResults());

        ReferralItem referralItem = new ReferralItem();
        referralItem.setCanceled(false);
        referralItem.setReferredResultType("N");

        Analysis analysis = referral.getAnalysis();
        SampleItem sampleItem = analysis.getSampleItem();
        TypeOfSample typeOfSample = sampleItem.getTypeOfSample();
        referralItem.setSampleType(typeOfSample.getLocalizedName());

        referralItem.setAccessionNumber(sampleItem.getSample().getAccessionNumber());
        referralItem.setReferringTestName(referral.getAnalysis().getTest().getLocalizedName());
        referralItem.setReferredTestId(referral.getAnalysis().getTest().getId());

        List<Result> resultList = new ArrayList<>(analysis.getResults());
        String resultString = "";

        if (!resultList.isEmpty()) {
            Result result = resultList.get(0);
            resultString = getAppropriateResultValue(resultList);
            referralItem.setUploadedFileName(result.getUploadedFileName());
            referralItem.setCasualResultId(result.getId());

            List<Note> notes = new ArrayList<>(result.getNotes());
            if (!(notes == null || notes.isEmpty())) {
                Collections.sort(notes, new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
                    }
                });

                StringBuilder noteBuilder = new StringBuilder();
                for (Note note : notes) {
                    noteBuilder.append(note.getText());
                    noteBuilder.append("<br/>");
                }
                noteBuilder.setLength(noteBuilder.lastIndexOf("<br/>"));
                referralItem.setPastNotes(noteBuilder.toString());
            }
        }

        referralItem.setReferralId(referral.getId());
        if (!referralResults.isEmpty()) {
            referralResults = setReferralItem(referralItem, referralResults);
            if (referralResults.size() >= 1) {
                referralItem.setAdditionalTests(getAdditionalReferralTests(referralResults/* PAH, referral */));
            }
        }
        referralItem.setReferralResults(resultString);
        referralItem.setReferralDate(DateUtil.convertTimestampToStringDate(referral.getRequestDate()));
        referralItem.setReferredSendDate(getSendDateOrDefault(referral));
        referralItem.setReferrer(referral.getRequesterName());
        referralItem.setReferralReasonId(referral.getReferralReasonId());
        referralItem.setTestSelectionList(getTestsForTypeOfSample(typeOfSample));
        referralItem.setReferralId(referral.getId());
        if (referral.getOrganization() != null) {
            referralItem.setReferredInstituteId(referral.getOrganization().getId());
        }
        referralItem.setFailedValidation(new StatusRules().hasFailedValidation(analysis.getStatusId()));
        referralItem.setPatientNumber(getPatientNumber(sampleItem));
        return referralItem;
    }

    private String getPatientNumber(SampleItem sampleItem) {
        if (!"H".equals(sampleItem.getSample().getDomain()))
            return null;

        Patient patientForSample = sampleItem.getSample().getPatient();
        List<PatientIdentity> identityList = new ArrayList<>(patientForSample.getPatientIdentities());
        return PatientIdentityTypeMap.getInstance().getIdentityValue(identityList, "ST");
    }

    private String getSendDateOrDefault(Referral referral) {
        if (referral.getSentDate() == null) {
            return DateUtil.getCurrentDateAsText();
        } else {
            return DateUtil.convertTimestampToStringDate(referral.getSentDate());
        }
    }

    private List<ReferredTest> getAdditionalReferralTests(List<ReferralResult> referralResults /*, Referral referral */) {
        List<ReferredTest> testList = new ArrayList<>();

        while (referralResults.size() > 0) {
            ReferralResult referralResult = referralResults.get(0); // use the top one to load various bits of information.
            ReferredTest referralTest = new ReferredTest();
            referralTest.setReferralId(referralResult.getReferral().getId());
            referralResults = setReferralItem(referralTest, referralResults); // remove one or more referralResults from the list as needed (for multiResults).
            if (referralResult.getReferralReportDate() == null) {
                referralTest.setReferredReportDate(DateUtil.getCurrentDateAsText());
            } else {
                referralTest.setReferredReportDate(DateUtil.convertTimestampToStringDate(referralResult.getReferralReportDate()));
            }
            referralTest.setReferralResultId(referralResult.getId());
            testList.add(referralTest);
        }
        return testList;
    }

    /**
     * Move everything appropriate to the referralItem including one or more of the referralResults from the given list.
     * Note: This method removes an item from the referralResults list.
     *
     * @param referralItem
     * @param referralResults
     */
    private List<ReferralResult> setReferralItem(IReferralResultTest referralItem, List<ReferralResult> referralResults) {
        List<ReferralResult> leftOvers = new ArrayList<>(referralResults);
        ReferralResult baseResult = referralResults.remove(0);
        leftOvers.remove(0);
//		referralItem.setReferredTestId(baseResult.getTestId());
        referralItem.setReferredReportDate(DateUtil.convertTimestampToStringDate(baseResult.getReferralReportDate()));
        Result result = baseResult.getResult();
        String resultType = (result != null) ? result.getResultType() : ResultType.Numeric.code();
        referralItem.setReferredResultType(resultType);
        if (!"M".equals(resultType)) {
            if (result != null && result.getId() != null) {
                String resultValue = GenericValidator.isBlankOrNull(result.getValue()) ? "" : result.getValue();
                referralItem.setReferredResult(resultValue);
                referralItem.setReferredDictionaryResult(resultValue);
            }
        } else {
            String multiResultValue = GenericValidator.isBlankOrNull(result.getValue()) ? "" : result.getValue();
            for (ReferralResult referralResult : referralResults) {
                if (baseResult.getTestId().equals(referralResult.getTestId())) {
                    multiResultValue += ", " + referralResult.getResult().getValue();
                    leftOvers.remove(referralResult);
                }
            }
            referralItem.setReferredMultiDictionaryResult(multiResultValue);
        }
        return leftOvers;
    }

    private List<IdValuePair> getDictionaryValuesForTest(String testId) {
        if (!GenericValidator.isBlankOrNull(testId)) {
            for (NonNumericTests test : nonNumericTests) {
                if (testId.equals(test.testId)) {
                    return test.dictionaryValues;
                }
            }
        }
        return new ArrayList<>();
    }

    private String getAppropriateResultValue(List<Result> results) {
        Result result = results.get(0);
        if ("D".equals(result.getResultType())) {
            Dictionary dictionary = dictionaryDAO.getDictionaryById(result.getValue());
            if (dictionary != null) {
                return dictionary.getLocalizedName();
            }
        } else if ("M".equals(result.getResultType())) {
            Dictionary dictionary = new Dictionary();
            StringBuilder multiResult = new StringBuilder();

            for (Result subResult : results) {
                dictionary.setId(subResult.getValue());
                dictionaryDAO.getData(dictionary);

                if (dictionary.getId() != null) {
                    multiResult.append(dictionary.getLocalizedName());
                    multiResult.append(", ");
                }
            }

            if (multiResult.length() > 0) {
                multiResult.setLength(multiResult.length() - 2); //remove last ", "
            }

            return multiResult.toString();
        } else {
            String resultValue = GenericValidator.isBlankOrNull(result.getValue()) ? "" : result.getValue();

            if (!GenericValidator.isBlankOrNull(resultValue) &&
                    result.getAnalysis().getTest().getUnitOfMeasure() != null) {
                resultValue += " " + result.getAnalysis().getTest().getUnitOfMeasure().getName();
            }
            return resultValue;
        }
        return "";
    }

    private List<IdValuePair> getTestsForTypeOfSample(TypeOfSample typeOfSample) {
        List<Test> testList = TypeOfSampleUtil.getTestListBySampleTypeId(typeOfSample.getId(), null, false);

        List<IdValuePair> valueList = new ArrayList<>();
        for (Test test : testList) {
            valueList.add(new IdValuePair(test.getId(), test.getLocalizedName()));
        }
        return valueList;
    }

    private List<NonNumericTests> getNonNumericTests(List<ReferralItem> referralItems) {
        Set<Integer> testIdSet = new HashSet<>();
        for (ReferralItem item : referralItems) {
            for (IdValuePair pair : item.getTestSelectionList()) {
                testIdSet.add(Integer.valueOf(pair.getId()));
            }
        }

        return new TestDAOImpl().getAllNonNumericTests(new ArrayList<Integer>(testIdSet));
    }

    /**
     * The types of testResults which mean that there will be a list of choices/options to select from.
     *
     * @param testResultType
     * @return true if it is one of the types which means a list of choices, false otherwise.
     */
    public static boolean isSelectList(String testResultType) {
        return "D".equals(testResultType) || "M".equals(testResultType);
    }

}

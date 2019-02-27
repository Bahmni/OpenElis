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
package us.mn.state.health.lims.test.beanItems;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.upload.FormFile;
import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.result.action.util.ResultItem;
import us.mn.state.health.lims.result.valueholder.Result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class TestResultItem implements ResultItem, Serializable{

	private static final long serialVersionUID = 1L;
	private String accessionNumber;
	private String sequenceNumber;
	private boolean showSampleDetails = true;
	/*
	 * What the h*** is a group separator?
	 * It is a work around for the grouped results forms.  If the issue were just displaying
	 * there wouldn't be an issue with using nested collections but Struts 1.x makes it difficult
	 * to recover the entered data with nested iterators so we are using a single iterator with marked
	 * TestResultItems to do the grouping correctly.
	 */
	private boolean isGroupSeparator = false;
	private int sampleGroupingNumber = 1;  //display only -- groups like samples together


	
	private static String NO = "no";
	@SuppressWarnings("unused")
	private static String YES = "yes";

    public boolean isResultValueBlankOrNull() {
        return GenericValidator.isBlankOrNull(getResult().getValue());
    }

    public enum Method{ DNA, MANUAL, AUTO; }
	public enum ResultDisplayType { TEXT, POS_NEG, POS_NEG_IND, HIV, SYPHILIS; }

	private String sampleSource;
	private String testDate;
	private String receivedDate;
	/*N.B. test method is the type of test it is (HIV etc).
	 *  analysisMethod is the way the analysis is done automatic or manual
	 */
	private Timestamp collectionDate;
	private String testMethod;
	private String analysisMethod;
	private String testName;
	private String testId;
	private String testKit1InventoryId;
	private String testKitId;
	private boolean testKitInactive = false;
	private double upperNormalRange = 0;
	private double lowerNormalRange = 0;
	private double upperAbnormalRange;
	private double lowerAbnormalRange;
	private String resultValue;
	private String remarks;
	private String technician;
	private boolean reportable;
	private String patientName;
	private String patientInfo;
	private String patientIdentity;
	private String unitsOfMeasure = "";

//	private String testSortNumber;
	private String resultType;
	private ResultDisplayType resultDisplayType = ResultDisplayType.TEXT;
	private boolean isModified = false;
	private String analysisId;
	private String analysisStatusId;
	private String resultId;
	private Result result;
	private String technicianSignatureId;
	private String supervisorSignatureId;
	private String resultLimitId;
	private List<IdValuePair> dictionaryResults;
	private String remove = NO;
	private String note;
	private String pastNotes;
	private boolean valid = true;
	private boolean normal = true;
	private boolean notIncludedInWorkplan = false;
	private int reflexStep = 0;
	private boolean isUserChoiceReflex = false;
	private boolean userChoicePending;
	private String reflexSelectionId;
	private String siblingReflexKey;
	private String thisReflexKey;
	private boolean readOnly = false;
	private boolean referredOut = false;
	private boolean referralCanceled = false;
	private String referralId = "";
	private String referralReasonId = "";
	private String referralOrganizationId = "";
	private String multiSelectResultValues;
	private String initialSampleCondition;
	private String sampleType;
	private boolean failedValidation = false;
	private boolean nonconforming = false;
	private String testSortOrder = null;
	private boolean isReflexGroup = false;
	private int reflexParentGroup = 0;
	private boolean isChildReflex = false;
	private boolean displayResultAsLog = false;
	private String qualifiedDictonaryId = null;
	private String qualifiedResultValue = "";
	private String qualifiedResultId;
    private boolean abnormal;
    private List<IdValuePair> abnormalTestResult;
    private FormFile uploadedFile;
    private String uploadedFileName;
	private boolean isReferredOutValueChanged = false;

    public String getAccessionNumber() {
		return accessionNumber;
	}
    public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}
	public boolean getIsReferredOutValueChanged() { return isReferredOutValueChanged; }
	public void setIsReferredOutValueChanged(boolean isReferredOutValueChanged) { this.isReferredOutValueChanged = isReferredOutValueChanged; }
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public boolean isShowSampleDetails() {
		return showSampleDetails;
	}
	public void setShowSampleDetails(boolean showSampleDetails) {
		this.showSampleDetails = showSampleDetails;
	}
	public boolean getIsGroupSeparator() {
		return isGroupSeparator;
	}
	public void setIsGroupSeparator(boolean isGroupSeparator) {
		this.isGroupSeparator = isGroupSeparator;
	}
	public int getSampleGroupingNumber() {
		return sampleGroupingNumber;
	}
	public void setSampleGroupingNumber(int sampleGroupingNumber) {
		this.sampleGroupingNumber = sampleGroupingNumber;
	}
	public String getTestKit1InventoryId() {
		return testKit1InventoryId;
	}
	public void setTestKit1InventoryId(String testKit1InventoryId) {
		this.testKit1InventoryId = testKit1InventoryId;
	}
	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}
	public boolean isReferredOut() {
		return referredOut;
	}
	public void setReferredOut(boolean referredOut) {
		this.referredOut = referredOut;
	}

	public String getTechnicianSignatureId() {
		return technicianSignatureId;
	}
	public void setTechnicianSignatureId(String technicianId) {
		this.technicianSignatureId = technicianId;
	}
	public String getSupervisorSignatureId() {
		return supervisorSignatureId;
	}
	public void setSupervisorSignatureId(String superviserId) {
		this.supervisorSignatureId = superviserId;
	}

	public String getReferralOrganizationId() {
		return referralOrganizationId;
	}

	public void setReferralOrganizationId(String referralOrganizationId) {
		this.referralOrganizationId = referralOrganizationId;
	}

	public String getTestKitInventoryId() {
		return testKit1InventoryId;
	}
	public void setTestKitInventoryId(String testKit1) {
		this.testKit1InventoryId = testKit1;
	}

	public void setTestKitInactive(boolean testKitInactive) {
		this.testKitInactive = testKitInactive;
	}

	public boolean getTestKitInactive() {
		return testKitInactive;
	}
	public String getTestKitId() {
		return testKitId;
	}
	public void setTestKitId(String testKitId) {
		this.testKitId = testKitId;
	}

	public String getResultDisplayType() {
		return resultDisplayType.toString();
	}

	public ResultDisplayType getRawResultDisplayType() {
		return resultDisplayType;
	}

	public void setResultDisplayType(ResultDisplayType resultType) {
		this.resultDisplayType = resultType;
	}

	public ResultDisplayType getEnumResultType() {
		return resultDisplayType;
	}

	public String getUnitsOfMeasure() {
		return unitsOfMeasure;
	}
	public void setUnitsOfMeasure(String unitsOfMeasure) {
		this.unitsOfMeasure = unitsOfMeasure;
	}

	public double getUpperNormalRange() {
		return upperNormalRange;
	}
	public void setUpperNormalRange(double upperNormalRange) {
		this.upperNormalRange = upperNormalRange;
	}
	public double getLowerNormalRange() {
		return lowerNormalRange;
	}
	public void setLowerNormalRange(double lowerNormalRange) {
		this.lowerNormalRange = lowerNormalRange;
	}
	public double getUpperAbnormalRange() {
		return upperAbnormalRange;
	}
	public void setUpperAbnormalRange(double upperAbnormalRange) {
		this.upperAbnormalRange = upperAbnormalRange;
	}
	public double getLowerAbnormalRange() {
		return lowerAbnormalRange;
	}
	public void setLowerAbnormalRange(double lowerAbnormalRange) {
		this.lowerAbnormalRange = lowerAbnormalRange;
	}
	
	public String getReportable() {
		return reportable ? "Y" : "N";
	}
	public void setReportable(boolean reportable) {
		this.reportable = reportable;
	}

	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientInfo() {
		return patientInfo;
	}
	public void setPatientInfo(String patientInfo) {
		this.patientInfo = patientInfo;
	}
    public String getPatientIdentity() {
        return patientIdentity;
    }
    public void setPatientIdentity(String patientIdentity) {
        this.patientIdentity = patientIdentity;
    }

    public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	public String getRemove() {
		return remove;
	}
	public void setRemove(String remove) {
		this.remove = remove;
	}

	public boolean isRemoved(){
		return remove==NO;
	}

/*	public void setTestSortNumber(String testSortNumber) {
		this.testSortNumber = testSortNumber;
	}
	public String getTestSortNumber() {
		return testSortNumber;
	}
*/	public String getSampleSource() {
		return sampleSource;
	}
	public void setSampleSource(String sampleSource) {
		this.sampleSource = sampleSource;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public String getTestMethod() {
		return testMethod;
	}
	public void setTestMethod(Method method) {
		testMethod = method == Method.AUTO ? "Auto" : "Manual";
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public String getNormalRange() {
		return String.valueOf(lowerNormalRange) + "-" + String.valueOf(upperNormalRange);
	}

	public String getResultValue() {
		return resultValue;
	}
	public void setResultValue(String results) {
		this.resultValue = results;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getTechnician() {
		return technician;
	}
	public void setTechnician(String technicien) {
		this.technician = technicien;
	}
	public void setIsModified(boolean isModified) {
		this.isModified = isModified;
	}
	public boolean getIsModified() {
		return isModified;
	}
	public String getAnalysisId() {
		return analysisId;
	}
	public void setAnalysisId(String analysisId) {
		this.analysisId = analysisId;
	}
	public void setAnalysisStatusId(String analysisStatusId) {
		this.analysisStatusId = analysisStatusId;
	}
	public String getAnalysisStatusId() {
		return analysisStatusId;
	}
	public String getResultId() {
		return resultId;
	}
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public void setDictionaryResults(List<IdValuePair> dictonaryResults) {
		this.dictionaryResults = dictonaryResults;
	}
	public List<IdValuePair> getDictionaryResults() {
		return dictionaryResults == null ? new ArrayList<IdValuePair>() : dictionaryResults;
	}
	
	public String getResultLimitId() {
		return resultLimitId;
	}
	public void setResultLimitId(String resultLimitId) {
		this.resultLimitId = resultLimitId;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setAnalysisMethod(String analysisMethod) {
		this.analysisMethod = analysisMethod;
	}
	public String getAnalysisMethod() {
		return analysisMethod;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public boolean isValid() {
		return valid;
	}
	public void setNotIncludedInWorkplan(boolean include) {
		this.notIncludedInWorkplan = include;
	}
	public boolean isNotIncludedInWorkplan() {
		return notIncludedInWorkplan;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getReceivedDate() {
		return receivedDate;
	}

	public void setCollectionDate(Timestamp collectionDate) {
		this.collectionDate = collectionDate;
	}
	public Timestamp getCollectionDate() {
		return collectionDate;
	}
	public void setReflexStep(int reflexStep) {
		this.reflexStep = reflexStep;
	}
	public int getReflexStep() {
		return reflexStep;
	}
	public void setResult(Result result) {
		setResultId(result == null ? "" : result.getId());
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

	
	public void setUserChoiceReflex(boolean isUserChoiceReflex) {
		this.isUserChoiceReflex = isUserChoiceReflex;
	}
	public boolean isUserChoiceReflex() {
		return isUserChoiceReflex;
	}

	public void setSiblingReflexKey(String siblingReflexKey) {
		this.siblingReflexKey = siblingReflexKey;
	}
	public String getSiblingReflexKey() {
		return siblingReflexKey;
	}
	public void setThisReflexKey(String thisReflexKey) {
		this.thisReflexKey = thisReflexKey;
	}
	public String getThisReflexKey() {
		return thisReflexKey;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}
	public String getReferralId() {
		return referralId;
	}
	public void setReferralReasonId(String referralReasonId) {
		this.referralReasonId = referralReasonId;
	}
	public String getReferralReasonId() {
		return referralReasonId;
	}
	public void setReferralCanceled(boolean referralCanceled) {
		this.referralCanceled = referralCanceled;
	}
	public boolean isReferralCanceled() {
		return referralCanceled;
	}
	public void setMultiSelectResultValues(String newMultiSelectResults) {
		this.multiSelectResultValues = newMultiSelectResults;
	}
	public String getMultiSelectResultValues() {
		return multiSelectResultValues;
	}

	public void setInitialSampleCondition(String initialSampleCondition) {
		this.initialSampleCondition = initialSampleCondition;
	}
	public String getInitialSampleCondition() {
		return initialSampleCondition;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public String getSampleType() {
		return sampleType;
	}
	public void setFailedValidation(boolean failedValidation) {
		this.failedValidation = failedValidation;
	}
	public boolean isFailedValidation() {
		return failedValidation;
	}
	public void setPastNotes(String pastNotes) {
		this.pastNotes = pastNotes;
	}
	public String getPastNotes() {
		return pastNotes;
	}
	@Override
	public String getSequenceAccessionNumber() {
		return getAccessionNumber() + "-" + getSequenceNumber();
	}
	public String getTestSortOrder() {
		return testSortOrder;
	}
	public void setTestSortOrder(String testSortOrder) {
		this.testSortOrder = testSortOrder;
	}
	public int getReflexParentGroup() {
		return reflexParentGroup;
	}
	public void setReflexParentGroup(int reflexParentGroup) {
		this.reflexParentGroup = reflexParentGroup;
	}
	public boolean isChildReflex() {
		return isChildReflex;
	}
	public void setChildReflex(boolean isChildReflex) {
		this.isChildReflex = isChildReflex;
	}
	public boolean isReflexGroup() {
		return isReflexGroup;
	}
	public void setReflexGroup(boolean isReflexGroup) {
		this.isReflexGroup = isReflexGroup;
	}
	public boolean isNormal() {
		return normal;
	}
	public void setNormal(boolean normal) {
		this.normal = normal;
	}
	public boolean isDisplayResultAsLog() {
		return displayResultAsLog;
	}
	public void setDisplayResultAsLog(boolean displayResultAsLog) {
		this.displayResultAsLog = displayResultAsLog;
	}
	public String getReflexSelectionId() {
		return reflexSelectionId;
	}
	public void setReflexSelectionId(String reflexSelectionId) {
		this.reflexSelectionId = reflexSelectionId;
	}
	public boolean isUserChoicePending() {
		return userChoicePending;
	}
	public void setUserChoicePending(boolean userChoicePending) {
		this.userChoicePending = userChoicePending;
	}
	public boolean isNonconforming() {
		return nonconforming;
	}
	public void setNonconforming(boolean nonconforming) {
		this.nonconforming = nonconforming;
	}
	public String getQualifiedDictonaryId() {
		return qualifiedDictonaryId;
	}
	public void setQualifiedDictonaryId(String qualifiedDictonaryId) {
		this.qualifiedDictonaryId = qualifiedDictonaryId;
	}
	public String getQualifiedResultValue() {
		return qualifiedResultValue;
	}
	public void setQualifiedResultValue(String qualifiedResultValue) {
		this.qualifiedResultValue = qualifiedResultValue;
	}
	public String getQualifiedResultId() {
		return qualifiedResultId;
	}
	public void setQualifiedResultId(String qualifiedResultId) {
		this.qualifiedResultId = qualifiedResultId;
	}
    public boolean getAbnormal() {
        return abnormal;
    }
    public void setAbnormal(boolean abnormal) {
        this.abnormal = abnormal;
    }
    public List<IdValuePair> getAbnormalTestResult() {
        return abnormalTestResult;
    }
    public void setAbnormalTestResult(List<IdValuePair> abnormalTestResult) {
        this.abnormalTestResult = abnormalTestResult;
    }
    public FormFile getUploadedFile() {
        return uploadedFile;
    }
    public void setUploadedFile(FormFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public String getAbnormalTestResultMap() {
        StringBuilder map = new StringBuilder();

        List<IdValuePair> abnormalResults = getAbnormalTestResult();
        for (int i = 0; i < abnormalResults.size(); i++) {
            map.append("{");
            map.append("'id':'").append(abnormalResults.get(i).getId()).append("'");
            map.append(",");
            map.append("'value':'").append(abnormalResults.get(i).getValue()).append("'");
            map.append("}");
            if (i < abnormalResults.size() - 1)
                map.append(",");
        }
        return map.toString();
    }
}

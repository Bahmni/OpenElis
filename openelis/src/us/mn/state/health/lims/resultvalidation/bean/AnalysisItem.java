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
package us.mn.state.health.lims.resultvalidation.bean;

import java.sql.Timestamp;
import java.util.List;

import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;

import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptanceRO;

public class AnalysisItem {

    private String id;

    private String units;

    private String testName;

    private String accessionNumber;

    private String result;

    private String receivedDate;

    private boolean isAccepted = false;

    private boolean isRejected = false;

    private boolean isManual = false;

    private String errorMessage;

    private String note;

    private String noteId;

    private String statusId;

    private String sampleId;

    private String analysisId;

    private String testId;

    private String resultId;

    private String resultType;

    private String completeDate;

    private boolean isPositive = false;

    private boolean isHighlighted = false;

    private Timestamp lastUpdated;

    private int sampleGroupingNumber = 0;

    private String testSortNumber;

    private String integralResult;

    private String integralAnalysisId;

    private String murexResult;

    private String murexAnalysisId;

    private String vironostikaResult;

    private String vironostikaAnalysisId;

    private String genieIIResult;

    private String genieIIAnalysisId;

    private String genieII100Result;

    private String genieII100AnalysisId;

    private String genieII10Result;

    private String genieII10AnalysisId;

    private String westernBlot1Result;

    private String westernBlot1AnalysisId;

    private String westernBlot2Result;

    private String westernBlot2AnalysisId;

    private String p24AgResult;

    private String p24AgAnalysisId;

    private String biolineResult;

    private String biolineAnalysisId;

    private String innoliaResult;

    private String innoliaAnalysisId;

    private String finalResult;

    private String nextTest;

    /* this is very specific to showing calculated results, generalize if there are more than just log calculations */
    private boolean displayResultAsLog = false;

    private boolean showAcceptReject = true;

    private List<IdValuePair> dictionaryResults;

    private boolean isMultipleResultForSample = false;

    private boolean readOnly = false;

    private boolean isReflexGroup = false;

    private boolean isChildReflex = false;

    private boolean nonconforming = false;
    private boolean abnormal;

    private double upperAbnormalRange;
    private double lowerAbnormalRange;
    private Double minNormal;
    private Double maxNormal;
    private String uploadedFilePath;


    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(String uploadedFileName) {
        this.uploadedFilePath = uploadedFileName;
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

    public List<IdValuePair> getAbnormalTestResult() {
        return abnormalTestResult;
    }

    public void setAbnormalTestResult(List<IdValuePair> abnormalTestResult) {
        this.abnormalTestResult = abnormalTestResult;
    }

    private List<IdValuePair> abnormalTestResult;

    public AnalysisItem() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTestName() {
        return this.testName;
    }

    public boolean isReferredOut() {
        return StatusOfSampleUtil.getStatusID(TechnicalAcceptanceRO).equals(statusId);
    }

    public String getTestDisplayName() {
        if (StatusOfSampleUtil.getStatusID(TechnicalAcceptanceRO).equals(statusId)) {
            return this.testName + "(R)";
        }
        return this.testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getUnits() {
        return units;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsRejected(boolean isRejected) {
        this.isRejected = isRejected;
    }

    public boolean getIsRejected() {
        return isRejected;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestId() {
        return this.testId;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getCompleteDate() {
        return this.completeDate;
    }

    public void setPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }

    public boolean getPositive() {
        return isPositive;
    }

    public void setIsHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public boolean getIsHighlighted() {
        return isHighlighted;
    }

    public void setLastUpdated(Timestamp lastupdated) {
        this.lastUpdated = lastupdated;

    }

    public Timestamp getLastUpdated() {
        return this.lastUpdated;
    }

    public void setSampleGroupingNumber(int sampleGroupingNumber) {
        this.sampleGroupingNumber = sampleGroupingNumber;
    }

    public int getSampleGroupingNumber() {
        return sampleGroupingNumber;
    }

    public void setTestSortNumber(String testSortNumber) {
        this.testSortNumber = testSortNumber;
    }

    public String getTestSortNumber() {
        return testSortNumber;
    }

    public void setManual(boolean isManual) {
        this.isManual = isManual;
    }

    public boolean getManual() {
        return isManual;
    }

    public String getIntegralResult() {
        return integralResult;
    }

    public void setIntegralResult(String integralResult) {
        this.integralResult = integralResult;
    }

    public void setIntegralAnalysisId(String integralAnalysisId) {
        this.integralAnalysisId = integralAnalysisId;
    }

    public String getIntegralAnalysisId() {
        return integralAnalysisId;
    }

    public String getMurexResult() {
        return murexResult;
    }

    public void setMurexResult(String murexResult) {
        this.murexResult = murexResult;
    }

    public void setMurexAnalysisId(String murexAnalysisId) {
        this.murexAnalysisId = murexAnalysisId;
    }

    public String getMurexAnalysisId() {
        return murexAnalysisId;
    }

    public String getVironostikaResult() {
        return vironostikaResult;
    }

    public void setVironostikaResult(String vironostikaResult) {
        this.vironostikaResult = vironostikaResult;
    }

    public void setVironostikaAnalysisId(String vironostikaAnalysisId) {
        this.vironostikaAnalysisId = vironostikaAnalysisId;
    }

    public String getVironostikaAnalysisId() {
        return vironostikaAnalysisId;
    }

    public String getGenieIIResult() {
        return genieIIResult;
    }

    public void setGenieIIResult(String genieIIResult) {
        this.genieIIResult = genieIIResult;
    }

    public void setGenieIIAnalysisId(String genieIIAnalysisId) {
        this.genieIIAnalysisId = genieIIAnalysisId;
    }

    public String getGenieIIAnalysisId() {
        return genieIIAnalysisId;
    }

    public String getGenieII100Result() {
        return this.genieII100Result;
    }

    public void setGenieII100Result(String genieII100Result) {
        this.genieII100Result = genieII100Result;
    }

    public void setGenieII100AnalysisId(String genieII100AnalysisId) {
        this.genieII100AnalysisId = genieII100AnalysisId;
    }

    public String getGenieII100AnalysisId() {
        return genieII100AnalysisId;
    }

    public String getGenieII10Result() {
        return genieII10Result;
    }

    public void setGenieII10Result(String genieII10Result) {
        this.genieII10Result = genieII10Result;
    }

    public void setGenieII10AnalysisId(String genieII10AnalysisId) {
        this.genieII10AnalysisId = genieII10AnalysisId;
    }

    public String getGenieII10AnalysisId() {
        return genieII10AnalysisId;
    }

    public String getWesternBlot1Result() {
        return westernBlot1Result;
    }

    public void setWesternBlot1Result(String westernBlot1Result) {
        this.westernBlot1Result = westernBlot1Result;
    }

    public void setWesternBlot1AnalysisId(String westernBlot1AnalysisId) {
        this.westernBlot1AnalysisId = westernBlot1AnalysisId;
    }

    public String getWesternBlot1AnalysisId() {
        return westernBlot1AnalysisId;
    }

    public String getWesternBlot2Result() {
        return westernBlot2Result;
    }

    public void setWesternBlot2Result(String westernBlot2Result) {
        this.westernBlot2Result = westernBlot2Result;
    }

    public void setWesternBlot2AnalysisId(String westernBlot2AnalysisId) {
        this.westernBlot2AnalysisId = westernBlot2AnalysisId;
    }

    public String getWesternBlot2AnalysisId() {
        return westernBlot2AnalysisId;
    }

    public String getP24AgResult() {
        return p24AgResult;
    }

    public void setP24AgResult(String p24AgResult) {
        this.p24AgResult = p24AgResult;
    }

    public void setP24AgAnalysisId(String p24AgAnalysisId) {
        this.p24AgAnalysisId = p24AgAnalysisId;
    }

    public String getP24AgAnalysisId() {
        return p24AgAnalysisId;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setNextTest(String nextTest) {
        this.nextTest = nextTest;
    }

    public String getNextTest() {
        return nextTest;
    }

    public void setDictionaryResults(List<IdValuePair> dictionaryResults) {
        this.dictionaryResults = dictionaryResults;
    }

    public List<IdValuePair> getDictionaryResults() {
        return dictionaryResults;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setDisplayResultAsLog(boolean displayResultAsLog) {
        this.displayResultAsLog = displayResultAsLog;
    }

    public boolean isDisplayResultAsLog() {
        return displayResultAsLog;
    }

    public void setShowAcceptReject(boolean showAcceptReject) {
        this.showAcceptReject = showAcceptReject;
    }

    public boolean isShowAcceptReject() {
        return showAcceptReject;
    }

    public void setMultipleResultForSample(boolean isMultipleResultForSample) {
        this.isMultipleResultForSample = isMultipleResultForSample;
    }

    public boolean isMultipleResultForSample() {
        return isMultipleResultForSample;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isReflexGroup() {
        return isReflexGroup;
    }

    public void setReflexGroup(boolean isReflexGroup) {
        this.isReflexGroup = isReflexGroup;
    }

    public boolean isChildReflex() {
        return isChildReflex;
    }

    public void setChildReflex(boolean isChildReflex) {
        this.isChildReflex = isChildReflex;
    }

    public String getBiolineResult() {
        return biolineResult;
    }

    public void setBiolineResult(String biolineResult) {
        this.biolineResult = biolineResult;
    }

    public String getBiolineAnalysisId() {
        return biolineAnalysisId;
    }

    public void setBiolineAnalysisId(String biolineAnalysisID) {
        this.biolineAnalysisId = biolineAnalysisID;
    }

    public boolean isNonconforming() {
        return nonconforming;
    }

    public void setNonconforming(boolean nonconforming) {
        this.nonconforming = nonconforming;
    }

    public String getInnoliaResult() {
        return innoliaResult;
    }

    public void setInnoliaResult(String innoliaResult) {
        this.innoliaResult = innoliaResult;
    }

    public String getInnoliaAnalysisId() {
        return innoliaAnalysisId;
    }

    public void setInnoliaAnalysisId(String innoliaAnalysisId) {
        this.innoliaAnalysisId = innoliaAnalysisId;
    }


    public void setAbnormal(boolean abnormal) {
        this.abnormal = abnormal;
    }

    public boolean isAbnormal() {
        return abnormal;
    }

    public void setMinNormal(Double minNormal) {
        this.minNormal = minNormal;
    }

    public void setMaxNormal(Double maxNormal) {
        this.maxNormal = maxNormal;
    }

    public Double getMinNormal() {
        return minNormal;
    }

    public Double getMaxNormal() {
        return maxNormal;
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

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
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.analysis.valueholder;

import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.util.resources.ResourceLocator;
import us.mn.state.health.lims.common.valueholder.BaseObject;
import us.mn.state.health.lims.common.valueholder.ValueHolder;
import us.mn.state.health.lims.common.valueholder.ValueHolderInterface;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.*;

public class Analysis extends BaseObject {

    private static final long serialVersionUID = 1L;

    private String id;
    private String comment;
    private ValueHolderInterface sampleItem;
    private String analysisType;
    private ValueHolderInterface testSection;
    private String testSectionName;
    private Test test;
    private String testName;
    private String revision;
    private String status;
    private Timestamp startedDate = null;
    private String startedDateForDisplay = null;
    private Timestamp completedDate = null;
    private Timestamp enteredDate = null;
    private String completedDateForDisplay = null;
    private Date releasedDate = null;
    private String releasedDateForDisplay = null;
    private Date printedDate = null;
    private String printedDateForDisplay = null;
    private String isReportable;
    private Date soSendReadyDate = null;
    private String soSendReadyDateForDisplay = null;
    private String soClientReference;
    private Date soNotifyReceivedDate = null;
    private String soNotifyReceivedDateForDisplay = null;
    private Date soNotifySendDate = null;
    private String soNotifySendDateForDisplay = null;
    private Date soSendDate = null;
    private String soSendDateForDisplay = null;
    private String soSendEntryBy;
    private Date soSendEntryDate = null;
    private String soSendEntryDateForDisplay = null;
    private ValueHolderInterface parentAnalysis;
    private ValueHolderInterface parentResult;
    private Panel panel;
    private Boolean triggeredReflex = false;
    private String statusId;
    private String assignedSortedTestTreeDisplayValue;
    private Set<Result> results;

    public String getAssignedSortedTestTreeDisplayValue() {
        return assignedSortedTestTreeDisplayValue;
    }

    public void setAssignedSortedTestTreeDisplayValue(
            String assignedSortedTestTreeDisplayValue) {
        this.assignedSortedTestTreeDisplayValue = assignedSortedTestTreeDisplayValue;
    }

    private List children;


    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public Analysis() {
        super();
        sampleItem = new ValueHolder();
        testSection = new ValueHolder();
        parentAnalysis = new ValueHolder();
        parentResult = new ValueHolder();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public SampleItem getSampleItem() {
        return (SampleItem) this.sampleItem.getValue();
    }

    public void setSampleItem(SampleItem sampleItem) {
        this.sampleItem.setValue(sampleItem);
    }

    public Timestamp getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Timestamp completedDate) {
        this.completedDate = completedDate;
        this.completedDateForDisplay = completedDate != null ? DateUtil.convertTimestampToStringDateAndTime(
                completedDate) : "";
    }

    public String getCompletedDateForDisplay() {
        return completedDateForDisplay;
    }

    public void setCompletedDateForDisplay(String completedDateForDisplay) {
        this.completedDateForDisplay = completedDateForDisplay;
        String pattern=DateUtil.checkStringDateAndReturnPattern(completedDateForDisplay);
        this.completedDate = DateUtil.convertStringDateToTimestampWithPattern(
                this.completedDateForDisplay, pattern);
    }


    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Timestamp getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Timestamp startedDate) {
        this.startedDate = startedDate;
        // also update String date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.startedDateForDisplay = DateUtil.convertTimestampToStringDate(
                startedDate, locale);
    }

    public String getStartedDateForDisplay() {
        return startedDateForDisplay;
    }

    public void setStartedDateForDisplay(String startedDateForDisplay) {
        this.startedDateForDisplay = startedDateForDisplay;
        // also update the java.sql.Date
        String pattern = DateUtil.checkStringDateAndReturnPattern(startedDateForDisplay);
        this.startedDate = DateUtil.convertStringDateToTimestampWithPattern(
                this.startedDateForDisplay, pattern);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsReportable() {
        return isReportable;
    }

    public void setIsReportable(String isReportable) {
        this.isReportable = isReportable;
    }

    public Date getPrintedDate() {
        return printedDate;
    }

    public void setPrintedDate(Date printedDate) {
        this.printedDate = printedDate;

        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.printedDateForDisplay = DateUtil.convertSqlDateToStringDate(
                printedDate, locale);
    }

    public String getPrintedDateForDisplay() {
        return printedDateForDisplay;
    }

    public void setPrintedDateForDisplay(String printedDateForDisplay) {
        this.printedDateForDisplay = printedDateForDisplay;
        // also update the java.sql.Date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.printedDate = DateUtil.convertStringDateToSqlDate(
                this.printedDateForDisplay, locale);
    }

    public Date getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
        // also update String date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.releasedDateForDisplay = DateUtil.convertSqlDateToStringDate(
                releasedDate, locale);
    }

    public String getReleasedDateForDisplay() {
        return releasedDateForDisplay;
    }

    public void setReleasedDateForDisplay(String releasedDateForDisplay) {
        this.releasedDateForDisplay = releasedDateForDisplay;
        // also update the java.sql.Date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.releasedDate = DateUtil.convertStringDateToSqlDate(
                this.releasedDateForDisplay, locale);
    }

    public String getSoClientReference() {
        return soClientReference;
    }

    public void setSoClientReference(String soClientReference) {
        this.soClientReference = soClientReference;
    }

    public Date getSoNotifyReceivedDate() {
        return soNotifyReceivedDate;
    }

    public void setSoNotifyReceivedDate(Date soNotifyReceivedDate) {
        this.soNotifyReceivedDate = soNotifyReceivedDate;
        // also update String date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soNotifyReceivedDateForDisplay = DateUtil
                .convertSqlDateToStringDate(soNotifyReceivedDate, locale);
    }

    public String getSoNotifyReceivedDateForDisplay() {
        return soNotifyReceivedDateForDisplay;
    }

    public void setSoNotifyReceivedDateForDisplay(
            String soNotifyReceivedDateForDisplay) {
        this.soNotifyReceivedDateForDisplay = soNotifyReceivedDateForDisplay;
        // also update the java.sql.Date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soNotifyReceivedDate = DateUtil.convertStringDateToSqlDate(
                this.soNotifyReceivedDateForDisplay, locale);
    }

    public Date getSoNotifySendDate() {
        return soNotifySendDate;
    }

    public void setSoNotifySendDate(Date soNotifySendDate) {
        this.soNotifySendDate = soNotifySendDate;
        // also update String date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soNotifySendDateForDisplay = DateUtil
                .convertSqlDateToStringDate(soNotifySendDate, locale);
    }

    public String getSoNotifySendDateForDisplay() {
        return soNotifySendDateForDisplay;
    }

    public void setSoNotifySendDateForDisplay(String soNotifySendDateForDisplay) {
        this.soNotifySendDateForDisplay = soNotifySendDateForDisplay;
        // also update the java.sql.Date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soNotifySendDate = DateUtil.convertStringDateToSqlDate(
                this.soNotifySendDateForDisplay, locale);
    }

    public Date getSoSendDate() {
        return soSendDate;
    }

    public void setSoSendDate(Date soSendDate) {
        this.soSendDate = soSendDate;
        // also update String date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soSendDateForDisplay = DateUtil.convertSqlDateToStringDate(
                soSendDate, locale);
    }

    public String getSoSendDateForDisplay() {
        return soSendDateForDisplay;
    }

    public void setSoSendDateForDisplay(String soSendDateForDisplay) {
        this.soSendDateForDisplay = soSendDateForDisplay;
        // also update the java.sql.Date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soSendDate = DateUtil.convertStringDateToSqlDate(
                this.soSendDateForDisplay, locale);
    }

    public String getSoSendEntryBy() {
        return soSendEntryBy;
    }

    public void setSoSendEntryBy(String soSendEntryBy) {
        this.soSendEntryBy = soSendEntryBy;
    }

    public Date getSoSendEntryDate() {
        return soSendEntryDate;
    }

    public void setSoSendEntryDate(Date soSendEntryDate) {
        this.soSendEntryDate = soSendEntryDate;
        // also update String date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soSendEntryDateForDisplay = DateUtil.convertSqlDateToStringDate(
                soSendEntryDate, locale);
    }

    public String getSoSendEntryDateForDisplay() {
        return soSendEntryDateForDisplay;
    }

    public void setSoSendEntryDateForDisplay(String soSendEntryDateForDisplay) {
        this.soSendEntryDateForDisplay = soSendEntryDateForDisplay;
        // also update the java.sql.Date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soSendEntryDate = DateUtil.convertStringDateToSqlDate(
                this.soSendEntryDateForDisplay, locale);
    }

    public Date getSoSendReadyDate() {
        return soSendReadyDate;
    }

    public void setSoSendReadyDate(Date soSendReadyDate) {
        this.soSendReadyDate = soSendReadyDate;
        // also update String date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soSendReadyDateForDisplay = DateUtil.convertSqlDateToStringDate(
                soSendReadyDate, locale);
    }

    public String getSoSendReadyDateForDisplay() {
        return soSendReadyDateForDisplay;
    }

    public void setSoSendReadyDateForDisplay(String soSendReadyDateForDisplay) {
        this.soSendReadyDateForDisplay = soSendReadyDateForDisplay;
        // also update the java.sql.Date
        String locale = SystemConfiguration.getInstance().getDefaultLocale()
                .toString();
        this.soSendReadyDate = DateUtil.convertStringDateToSqlDate(
                this.soSendReadyDateForDisplay, locale);
    }

    public TestSection getTestSection() {
        return (TestSection) this.testSection.getValue();
    }

    public void setTestSection(TestSection testSection) {
        this.testSection.setValue(testSection);
    }

    public String getTestSectionName() {
        return this.testSectionName;
    }

    public void setTestSectionName(String testSectionName) {
        this.testSectionName = testSectionName;
    }

    public String getTestName() {
        return this.testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Analysis getParentAnalysis() {
        return (Analysis) this.parentAnalysis.getValue();
    }

    public void setParentAnalysis(Analysis parentAnalysis) {
        this.parentAnalysis.setValue(parentAnalysis);
    }

    public Result getParentResult() {
        return (Result) this.parentResult.getValue();
    }

    public void setParentResult(Result parentResult) {
        this.parentResult.setValue(parentResult);
    }

    public void setTriggeredReflex(Boolean triggeredReflex) {
        this.triggeredReflex = triggeredReflex;
    }

    public Boolean getTriggeredReflex() {
        return triggeredReflex;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setEnteredDate(Timestamp enteredDate) {
        this.enteredDate = enteredDate;
    }

    public Timestamp getEnteredDate() {
        return enteredDate;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public boolean isFinished() {
        return StatusOfSampleUtil.analysisFinishedStatusIds().contains(Integer.parseInt(getStatusId()));
    }

    public void readyForTechnicalAcceptance() {
        if (StatusOfSampleUtil.getStatusID(ReferedOut).equals(statusId)
                || StatusOfSampleUtil.getStatusID(BiologistRejectedRO).equals(statusId)) {
            this.setStatusId(StatusOfSampleUtil.getStatusID(TechnicalAcceptanceRO));
        } else {
            this.setStatusId(StatusOfSampleUtil.getStatusID(TechnicalAcceptance));
        }
    }

    public void cancelReferOut() {
        this.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.NotTested));
    }

    public void reject() {
        if (StatusOfSampleUtil.getStatusID(TechnicalAcceptanceRO).equals(statusId)) {
            this.setStatusId(StatusOfSampleUtil.getStatusID(BiologistRejectedRO));
        } else {
            this.setStatusId(StatusOfSampleUtil.getStatusID(BiologistRejected));
        }
    }

    public void finalizeResult() {
        if (StatusOfSampleUtil.getStatusID(TechnicalAcceptanceRO).equals(statusId)) {
            setStatusId(StatusOfSampleUtil.getStatusID(FinalizedRO));
        } else {
            this.setStatusId(StatusOfSampleUtil.getStatusID(Finalized));
        }
    }

    public Set<Result> getResults() {
        if (results == null) results = new HashSet<>();
        return results;
    }

    public void setResults(Set<Result> results) {
        this.results = results;
    }

    public void addResult(Result result) {
        getResults().add(result);
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

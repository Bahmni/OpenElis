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
package us.mn.state.health.lims.referral.valueholder;

import org.apache.commons.validator.GenericValidator;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.valueholder.BaseObject;
import us.mn.state.health.lims.common.valueholder.ValueHolder;
import us.mn.state.health.lims.common.valueholder.ValueHolderInterface;
import us.mn.state.health.lims.referral.action.beanitems.IReferralResultTest;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.result.valueholder.ResultType;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;

import java.sql.Timestamp;

public class ReferralResult extends BaseObject {

    private static final long serialVersionUID = 1L;

    private String id;
    private Referral referral;
    private String referralId;
    private String testId;
    private Timestamp referralReportDate;
    private ValueHolderInterface result = new ValueHolder();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Timestamp getReferralReportDate() {
        return referralReportDate;
    }

    public void setReferralReportDate(Timestamp referralReportDate) {
        this.referralReportDate = referralReportDate;
    }

    public void setResult(Result result) {
        this.result.setValue(result);
    }

    public Result getResult() {
        return (Result) result.getValue();
    }

    public void fillResult(IReferralResultTest referralItem, String currentUserId, ResultLimit limit, String referredResultType) {
        this.setSysUserId(currentUserId);

        setReferredResultReportDate(referralItem.getReferredReportDate());
        setTestId(referralItem);
        this.setReferralId(referralItem.getReferralId());

        if (getResult() == null) {
            setResult(new Result());
        }

        setResultValues(referralItem, currentUserId, limit, referredResultType);

        if(referralItem.isMarkedAsDone()) {
            getResult().getAnalysis().readyForTechnicalAcceptance();
        }
        getResult().setAbnormal(referralItem.isAbnormal());
    }

    private void setReferredResultReportDate(String referredReportDate) throws LIMSRuntimeException {
        if (!GenericValidator.isBlankOrNull(referredReportDate)) {
            this.setReferralReportDate(DateUtil.convertStringDateToTruncatedTimestamp(referredReportDate));
        }
    }

    private void setTestId(IReferralResultTest referralTest) {
        if (!"0".equals(referralTest.getReferredTestId())) {
            this.setTestId(referralTest.getReferredTestId());
        }
    }

    /**
     * If the referredTest.referredResultType is "M" the particular value to
     * translate into the result should already be loaded in
     * referredTest.referredDictionaryResult
     */
    private void setResultValues(IReferralResultTest referredTest, String currentUserId, ResultLimit limit, String referredResultType) {
        getResult().setSysUserId(currentUserId);
        getResult().setSortOrder("0");
        getResult().setMinNormal(limit.getLowNormal());
        getResult().setMaxNormal(limit.getHighNormal());
        if(StringUtil.isNullorNill(getResult().getUploadedFileName())) {
            getResult().setUploadedFileName(referredTest.getUploadedFileName());
        }
        else {
            if(referredTest.getUploadedFileName()!=null && !getResult().getUploadedFileName().equals(referredTest.getUploadedFileName())) {
                getResult().setUploadedFileName(referredTest.getUploadedFileName());
            }
        }
        String limitId = limit.getId();
        getResult().setResultLimitId(!StringUtil.isNullorNill(limitId) ? Integer.parseInt(limitId) : null);

        getResult().setResultType(referredResultType);
        if (ResultType.Dictionary.code().equals(referredResultType) || ResultType.MultiSelect.code().equals(referredResultType)) {
            String dicResult = referredTest.getReferredDictionaryResult();
            if (!(GenericValidator.isBlankOrNull(dicResult) || "0".equals(dicResult))) {
                getResult().setValue(dicResult);
            }
        } else {
            getResult().setValue(referredTest.getReferredResult());
        }
    }

    public Referral getReferral() {
        return referral;
    }

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    public String getReferralId() {
        return referralId;
    }

    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }
}

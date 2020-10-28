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
package us.mn.state.health.lims.test.valueholder;

import org.hibernate.proxy.HibernateProxy;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.valueholder.EnumValueItemImpl;
import us.mn.state.health.lims.common.valueholder.ValueHolder;
import us.mn.state.health.lims.common.valueholder.ValueHolderInterface;
import us.mn.state.health.lims.label.valueholder.Label;
import us.mn.state.health.lims.method.valueholder.Method;
import us.mn.state.health.lims.scriptlet.valueholder.Scriptlet;
import us.mn.state.health.lims.testtrailer.valueholder.TestTrailer;
import us.mn.state.health.lims.unitofmeasure.valueholder.UnitOfMeasure;

import java.lang.Boolean;
import java.sql.Date;

/**
 * @author benzd1
 */
public class Test extends EnumValueItemImpl {

	private static final long serialVersionUID = 1L;

	private String id;

	private String methodName;

	private ValueHolderInterface method;

	private String labelName;

	private ValueHolderInterface label;

	private String testTrailerName;


    private ValueHolderInterface testTrailer;

	private String testSectionName;

	private ValueHolderInterface testSection;

	private String scriptletName;

	private ValueHolderInterface scriptlet;

	// private String testReflxId;

	private String testName;

	private String description;

	private String loinc;

	private String reportingDescription;

	private String stickerRequiredFlag;

	private String alternateTestDisplayValue;

	private Date activeBeginDate = null;

	private String activeBeginDateForDisplay;

	private Date activeEndDate = null;

	private String activeEndDateForDisplay;

	private String isReportable;

	private String timeHolding;

	private String timeWait;

	private String timeAverage;

	private String timeWarning;

	private String timeMax;

	private String labelQuantity;

	private UnitOfMeasure unitOfMeasure;

	private String sortOrder;

	private String localAbbrev;

	private Boolean orderable;

	private Boolean isReferredOut = false;

	private  Boolean isReferredOutValueChanged = false;

	private String referenceInfo;

	public Test() {
		super();
		this.method = new ValueHolder();
		this.label = new ValueHolder();
		this.testTrailer = new ValueHolder();
		this.testSection = new ValueHolder();
		this.scriptlet = new ValueHolder();
	}

	public Boolean getIsReferredOutValueChanged() {
		return isReferredOutValueChanged;
	}

	public void setIsReferredOutValueChanged(Boolean isReferredOutValueChanged) {
		this.isReferredOutValueChanged = isReferredOutValueChanged;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setId(String id) {
		this.id = id;
		this.key = id;
	}
	public String getReferenceInfo() {
		return referenceInfo;
	}

	public void setReferenceInfo(String referenceInfo) {
		this.referenceInfo = referenceInfo;
	}

	public String getId() {
		return id;
	}

	public Date getActiveBeginDate() {
		return activeBeginDate;
	}

	public void setActiveBeginDate(Date activeBeginDate) {
		this.activeBeginDate = activeBeginDate;
		// also update String date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.activeBeginDateForDisplay = DateUtil.convertSqlDateToStringDate(
				activeBeginDate, locale);
	}

	public String getActiveBeginDateForDisplay() {
		return activeBeginDateForDisplay;
	}

	public void setActiveBeginDateForDisplay(String activeBeginDateForDisplay) {
		this.activeBeginDateForDisplay = activeBeginDateForDisplay;
		// also update the java.sql.Date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.activeBeginDate = DateUtil.convertStringDateToSqlDate(
				this.activeBeginDateForDisplay, locale);
	}

	public Date getActiveEndDate() {
		return activeEndDate;
	}

	public void setActiveEndDate(Date activeEndDate) {
		this.activeEndDate = activeEndDate;
		// also update String date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.activeEndDateForDisplay = DateUtil.convertSqlDateToStringDate(
				activeEndDate, locale);
	}

	public String getActiveEndDateForDisplay() {
		return activeEndDateForDisplay;
	}

	public void setActiveEndDateForDisplay(String activeEndDateForDisplay) {
		this.activeEndDateForDisplay = activeEndDateForDisplay;
		// also update the java.sql.Date
		String locale = SystemConfiguration.getInstance().getDefaultLocale()
				.toString();
		this.activeEndDate = DateUtil.convertStringDateToSqlDate(
				this.activeEndDateForDisplay, locale);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsReportable() {
		return isReportable;
	}

	public void setIsReportable(String isReportable) {
		this.isReportable = isReportable;
	}

	public String getLabelQuantity() {
		return labelQuantity;
	}

	public void setLabelQuantity(String labelQuantity) {
		this.labelQuantity = labelQuantity;
	}

	public String getLoinc() {
		return loinc;
	}

	public void setLoinc(String loinc) {
		this.loinc = loinc;
	}

	public String getReportingDescription() {
		return reportingDescription;
	}

	public void setReportingDescription(String reportingDescription) {
		this.reportingDescription = reportingDescription;
	}

	public String getStickerRequiredFlag() {
		return stickerRequiredFlag;
	}

	public void setStickerRequiredFlag(String stickerRequiredFlag) {
		this.stickerRequiredFlag = stickerRequiredFlag;
	}


	public String getTimeAverage() {
		return timeAverage;
	}

	public void setTimeAverage(String timeAverage) {
		this.timeAverage = timeAverage;
	}

	public String getTimeHolding() {
		return timeHolding;
	}

	public void setTimeHolding(String timeHolding) {
		this.timeHolding = timeHolding;
	}

	public String getTimeMax() {
		return timeMax;
	}

	public void setTimeMax(String timeMax) {
		this.timeMax = timeMax;
	}

	public String getTimeWait() {
		return timeWait;
	}

	public void setTimeWait(String timeWait) {
		this.timeWait = timeWait;
	}

	public String getTimeWarning() {
		return timeWarning;
	}

	public void setTimeWarning(String timeWarning) {
		this.timeWarning = timeWarning;
	}

	public String getUnitOfMeasureId() {
		return unitOfMeasure == null ? null : unitOfMeasure.getId();
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
		this.name = testName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getTestTrailerName() {
		return testTrailerName;
	}

	public void setTestTrailerName(String testTrailerName) {
		this.testTrailerName = testTrailerName;
	}

	public String getTestSectionName() {
		return testSectionName;
	}

	public void setTestSectionName(String testSectionName) {
		this.testSectionName = testSectionName;
	}

	public String getScriptletName() {
		return scriptletName;
	}

	public void setScriptletName(String scriptletName) {
		this.scriptletName = scriptletName;
	}

	public void setMethod(Method method) {
		this.method.setValue(method);
	}

	protected void setMethodHolder(ValueHolderInterface method) {
		this.method = method;
	}

	public Method getMethod() {
		return (Method) this.method.getValue();
	}

	protected ValueHolderInterface getMethodHolder() {
		return this.method;
	}

	public void setLabel(Label label) {
		this.label.setValue(label);
	}

	protected void setLabelHolder(ValueHolderInterface label) {
		this.label = label;
	}

	public Label getLabel() {
		return (Label) this.label.getValue();
	}

	protected ValueHolderInterface getLabelHolder() {
		return this.label;
	}

	public void setTestTrailer(TestTrailer testTrailer) {
		this.testTrailer.setValue(testTrailer);
	}

	protected void setTestTrailerHolder(ValueHolderInterface testTrailer) {
		this.testTrailer = testTrailer;
	}

	public TestTrailer getTestTrailer() {
		return (TestTrailer) this.testTrailer.getValue();
	}

	protected ValueHolderInterface getTestTrailerHolder() {
		return this.testTrailer;
	}

	public void setTestSection(TestSection testSection) {
		this.testSection.setValue(testSection);
	}

	protected void setTestSectionHolder(ValueHolderInterface testSection) {
		this.testSection = testSection;
	}

	public TestSection getTestSection() {
		return (TestSection) this.testSection.getValue();
	}

	protected ValueHolderInterface getTestSectionHolder() {
		return this.testSection;
	}

	public void setScriptlet(Scriptlet scriptlet) {
		this.scriptlet.setValue(scriptlet);
	}

	protected void setScriptletHolder(ValueHolderInterface scriptlet) {
		this.scriptlet = scriptlet;
	}

	public Scriptlet getScriptlet() {
		return (Scriptlet) this.scriptlet.getValue();
	}

	protected ValueHolderInterface getScriptletHolder() {
		return this.scriptlet;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return this.unitOfMeasure;
	}

	public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getTestDisplayValue() {
		if (!StringUtil.isNullorNill(this.testName)) {
			return testName + "-" + description;
		} else {
			return description;
		}
	}

	public String getAlternateTestDisplayValue() {
		if (!StringUtil.isNullorNill(this.description)) {
			alternateTestDisplayValue = description + "-" + testName;
		} else {
			alternateTestDisplayValue = testName;
		}
		return alternateTestDisplayValue;
	}

	public void setAlternateTestDisplayValue(String alternateTestDisplayValue) {
		this.alternateTestDisplayValue = alternateTestDisplayValue;
	}

	@Override
	protected String getDefaultLocalizedName() {
		return testName;
	}

	public void setLocalAbbrev(String localAbbrev) {
		this.localAbbrev = localAbbrev;
	}

	public String getLocalAbbrev() {
		return localAbbrev;
	}

	public Boolean getOrderable() {
		return orderable;
	}

	public void setOrderable(Boolean orderable) {
		this.orderable = orderable;
	}


	public Boolean getIsReferredOut() {
		return isReferredOut;
	}

	public void setIsReferredOut(Boolean isReferredOut) {
		this.isReferredOut = isReferredOut;
	}


	public Test unwrap(Object proxy) {
        if (proxy instanceof HibernateProxy) {
            return  (Test) ((HibernateProxy) proxy).getHibernateLazyInitializer()
                    .getImplementation();

        } else {
            return (Test)proxy;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
		o = unwrap(o);
		if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;

        if (activeBeginDate != null ? !activeBeginDate.equals(test.activeBeginDate) : test.activeBeginDate != null)
            return false;
        if (activeBeginDateForDisplay != null ? !activeBeginDateForDisplay.equals(test.activeBeginDateForDisplay) : test.activeBeginDateForDisplay != null)
            return false;
        if (activeEndDate != null ? !activeEndDate.equals(test.activeEndDate) : test.activeEndDate != null)
            return false;
        if (activeEndDateForDisplay != null ? !activeEndDateForDisplay.equals(test.activeEndDateForDisplay) : test.activeEndDateForDisplay != null)
            return false;
        if (alternateTestDisplayValue != null ? !alternateTestDisplayValue.equals(test.alternateTestDisplayValue) : test.alternateTestDisplayValue != null)
            return false;
        if (description != null ? !description.equals(test.description) : test.description != null) return false;
        if (id != null ? !id.equals(test.id) : test.id != null) return false;
        if (isReportable != null ? !isReportable.equals(test.isReportable) : test.isReportable != null) return false;
        if (label != null ? !label.equals(test.label) : test.label != null) return false;
        if (labelName != null ? !labelName.equals(test.labelName) : test.labelName != null) return false;
        if (labelQuantity != null ? !labelQuantity.equals(test.labelQuantity) : test.labelQuantity != null)
            return false;
        if (localAbbrev != null ? !localAbbrev.equals(test.localAbbrev) : test.localAbbrev != null) return false;
        if (loinc != null ? !loinc.equals(test.loinc) : test.loinc != null) return false;
        if (method != null ? !method.equals(test.method) : test.method != null) return false;
        if (methodName != null ? !methodName.equals(test.methodName) : test.methodName != null) return false;
        if (orderable != null ? !orderable.equals(test.orderable) : test.orderable != null) return false;
        if (reportingDescription != null ? !reportingDescription.equals(test.reportingDescription) : test.reportingDescription != null)
            return false;
        if (scriptlet != null ? !scriptlet.equals(test.scriptlet) : test.scriptlet != null) return false;
        if (scriptletName != null ? !scriptletName.equals(test.scriptletName) : test.scriptletName != null)
            return false;
        if (sortOrder != null ? !sortOrder.equals(test.sortOrder) : test.sortOrder != null) return false;
        if (stickerRequiredFlag != null ? !stickerRequiredFlag.equals(test.stickerRequiredFlag) : test.stickerRequiredFlag != null)
            return false;
        if (!testName.equals(test.testName)) return false;
        if (testSection != null ? !testSection.equals(test.testSection) : test.testSection != null) return false;
        if (testSectionName != null ? !testSectionName.equals(test.testSectionName) : test.testSectionName != null)
            return false;
        if (testTrailer != null ? !testTrailer.equals(test.testTrailer) : test.testTrailer != null) return false;
        if (testTrailerName != null ? !testTrailerName.equals(test.testTrailerName) : test.testTrailerName != null)
            return false;
        if (timeAverage != null ? !timeAverage.equals(test.timeAverage) : test.timeAverage != null) return false;
        if (timeHolding != null ? !timeHolding.equals(test.timeHolding) : test.timeHolding != null) return false;
        if (timeMax != null ? !timeMax.equals(test.timeMax) : test.timeMax != null) return false;
        if (timeWait != null ? !timeWait.equals(test.timeWait) : test.timeWait != null) return false;
        if (timeWarning != null ? !timeWarning.equals(test.timeWarning) : test.timeWarning != null) return false;
        if (unitOfMeasure != null ? !unitOfMeasure.equals(test.unitOfMeasure) : test.unitOfMeasure != null)
            return false;
        if (getUnitOfMeasureId() != null ? !getUnitOfMeasureId().equals(test.getUnitOfMeasureId()) : test.getUnitOfMeasureId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (labelName != null ? labelName.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (testTrailerName != null ? testTrailerName.hashCode() : 0);
        result = 31 * result + (testTrailer != null ? testTrailer.hashCode() : 0);
        result = 31 * result + (testSectionName != null ? testSectionName.hashCode() : 0);
        result = 31 * result + (testSection != null ? testSection.hashCode() : 0);
        result = 31 * result + (scriptletName != null ? scriptletName.hashCode() : 0);
        result = 31 * result + (scriptlet != null ? scriptlet.hashCode() : 0);
        result = 31 * result + (getUnitOfMeasureId() != null ? getUnitOfMeasureId().hashCode() : 0);
        result = 31 * result + testName.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (loinc != null ? loinc.hashCode() : 0);
        result = 31 * result + (reportingDescription != null ? reportingDescription.hashCode() : 0);
        result = 31 * result + (stickerRequiredFlag != null ? stickerRequiredFlag.hashCode() : 0);
        result = 31 * result + (alternateTestDisplayValue != null ? alternateTestDisplayValue.hashCode() : 0);
        result = 31 * result + (activeBeginDate != null ? activeBeginDate.hashCode() : 0);
        result = 31 * result + (activeBeginDateForDisplay != null ? activeBeginDateForDisplay.hashCode() : 0);
        result = 31 * result + (activeEndDate != null ? activeEndDate.hashCode() : 0);
        result = 31 * result + (activeEndDateForDisplay != null ? activeEndDateForDisplay.hashCode() : 0);
        result = 31 * result + (isReportable != null ? isReportable.hashCode() : 0);
        result = 31 * result + (timeHolding != null ? timeHolding.hashCode() : 0);
        result = 31 * result + (timeWait != null ? timeWait.hashCode() : 0);
        result = 31 * result + (timeAverage != null ? timeAverage.hashCode() : 0);
        result = 31 * result + (timeWarning != null ? timeWarning.hashCode() : 0);
        result = 31 * result + (timeMax != null ? timeMax.hashCode() : 0);
        result = 31 * result + (labelQuantity != null ? labelQuantity.hashCode() : 0);
        result = 31 * result + (unitOfMeasure != null ? unitOfMeasure.hashCode() : 0);
        result = 31 * result + (sortOrder != null ? sortOrder.hashCode() : 0);
        result = 31 * result + (localAbbrev != null ? localAbbrev.hashCode() : 0);
        result = 31 * result + (orderable != null ? orderable.hashCode() : 0);
        result = 31 * result + (isReferredOut != null ? isReferredOut.hashCode() : 0);
        return result;
    }
}

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

package us.mn.state.health.lims.sample.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SampleEditItem {
	private String accessionNumber;
	private String analysisId;
	private String sampleType;
	private String testName;
	private String sampleItemId;
	private String testId;
	private boolean canCancel = false;
	private boolean canceled;
	private boolean add;
	private String status;
	private String sortOrder;
	private boolean canRemoveSample = false;
	private boolean removeSample;
    private String panelName;
    private boolean isPanel = false;
    private List<SampleEditItem> panelTests = new ArrayList<SampleEditItem>();
    private boolean shouldDisplaySampleTypeInformation;

    public String getAccessionNumber() {
		return accessionNumber;
	}
	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getSampleItemId() {
		return sampleItemId;
	}
	public void setSampleItemId(String sampleItemId) {
		this.sampleItemId = sampleItemId;
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public boolean isCanCancel() {
		return canCancel;
	}
	public void setCanCancel(boolean hasResults) {
		this.canCancel = hasResults;
	}
	public boolean isCanceled() {
		return canceled;
	}
	public void setCanceled(boolean remove) {
		this.canceled = remove;
	}

	public boolean isAdd() {
		return add;
	}
	public void setAdd(boolean add) {
		this.add = add;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setAnalysisId(String analysisId) {
		this.analysisId = analysisId;
	}
	public String getAnalysisId() {
		return analysisId;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public boolean isCanRemoveSample() {
		return canRemoveSample;
	}
	public void setCanRemoveSample(boolean canRemoveSample) {
		this.canRemoveSample = canRemoveSample;
	}
	public boolean isRemoveSample() {
		return removeSample;
	}
	public void setRemoveSample(boolean removeSample) {
		this.removeSample = removeSample;
	}
    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

    public boolean isPanel() {
        return org.apache.commons.lang.StringUtils.isNotBlank(panelName);
    }

    public void setPanel(boolean isPanel) {
        this.isPanel = isPanel;
    }

    public List<SampleEditItem> getPanelTests() {
        return panelTests;
    }

    public void setPanelTests(List<SampleEditItem> panelTests) {
        this.panelTests = panelTests;
    }

    public void addPanelTest(SampleEditItem sampleItem) {
        this.panelTests.add(sampleItem);
    }

    public boolean shouldDisplaySampleTypeInformation() {
        return shouldDisplaySampleTypeInformation;
    }

    public void setShouldDisplaySampleTypeInformation(boolean shouldDisplaySampleTypeInformation) {
        this.shouldDisplaySampleTypeInformation = shouldDisplaySampleTypeInformation;
    }
}

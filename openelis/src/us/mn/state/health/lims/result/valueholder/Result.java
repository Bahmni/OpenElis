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
package us.mn.state.health.lims.result.valueholder;

import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.analyte.valueholder.Analyte;
import us.mn.state.health.lims.common.valueholder.EnumValueItemImpl;
import us.mn.state.health.lims.common.valueholder.ValueHolder;
import us.mn.state.health.lims.common.valueholder.ValueHolderInterface;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashSet;
import java.util.Set;

public class Result extends EnumValueItemImpl {


	private static final long serialVersionUID = 1L;

	private String id;
	private Analysis analysis;
	private String analysisId;
	private ValueHolderInterface analyte;
	private String analyteId;
	private TestResult testResult;
	private String testResultId;
	private String sortOrder;
	private String isReportable;
	private String resultType;
	private String value;
	private Double minNormal;
	private Double maxNormal;
	private ValueHolder parentResult;
    private Set<ResultSignature> resultSignatures;
    private String uploadedFileName;
    private Set<Note> notes;
    private Integer resultLimitId;

    private boolean abnormal;

	public Result() {
		super();
		analyte = new ValueHolder();
		parentResult = new ValueHolder();
	}

	// ANALYSIS
	public Analysis getAnalysis() {
		return this.analysis;
	}

	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
        this.setAnalysisId(analysis.getId());
	}

    // ANALYTE
	public Analyte getAnalyte() {
		return (Analyte) this.analyte.getValue();
	}

	public void setAnalyte(ValueHolderInterface analyte) {
		this.analyte = analyte;
	}

	public void setAnalyte(Analyte analyte) {
		this.analyte.setValue(analyte);
	}

	protected ValueHolderInterface getAnalyteHolder() {
		return this.analyte;
	}

	protected void setAnalyteHolder(ValueHolderInterface analyte) {
		this.analyte = analyte;
	}

	public String getIsReportable() {
		return isReportable;
	}

	public void setIsReportable(String isReportable) {
		this.isReportable = isReportable;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	// TEST_RESULT
	public TestResult getTestResult() {
		return this.testResult;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult= testResult;
	}

	public String getValue() {
		return StringEscapeUtils.unescapeHtml4(value);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public String getAnalysisId() {
		return analysisId;
	}

	public void setAnalysisId(String analysisId) {
		this.analysisId = analysisId;
	}

	public String getAnalyteId() {
		if( analyteId == null){
			if( getAnalyte() != null){
				analyteId = getAnalyte().getId();
			}
		}
		return analyteId;
	}

	public void setAnalyteId(String analyteId) {
		this.analyteId = analyteId;
	}

	public String getTestResultId() {
		if( testResultId == null){
			if( getTestResult() != null){
				testResultId = getTestResult().getId();
			}
		}
		return testResultId;
	}

	public void setTestResultId(String testResultId) {
		this.testResultId = testResultId;
	}

	public Double getMinNormal() {
		return minNormal;
	}

	public void setMinNormal(Double minNormal) {
		this.minNormal = minNormal;
	}

	public Double getMaxNormal() {
		return maxNormal;
	}

	public void setMaxNormal(Double maxNormal) {
		this.maxNormal = maxNormal;
	}

	public Result getParentResult() {
		return (Result)parentResult.getValue();
	}

	public void setParentResult(Result parentResult) {
		this.parentResult.setValue( parentResult );
	}

    public boolean getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(boolean abnormal) {
        this.abnormal = abnormal;
    }

    public boolean canHaveMultipleValues() {
        return this.testResult != null && this.testResult.canHaveMultipleValues();
    }

    public boolean isDictionary() {
        return resultType != null && resultType.equals("D");
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public Integer getResultLimitId() {
        return resultLimitId;
    }

    public void setResultLimitId(Integer resultLimitId) {
        this.resultLimitId = resultLimitId;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id='" + id + '\'' +
                ", analysis=" + analysis +
                ", analysisId='" + analysisId + '\'' +
                ", analyte=" + analyte +
                ", analyteId='" + analyteId + '\'' +
                ", testResult=" + testResult +
                ", testResultId='" + testResultId + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", isReportable='" + isReportable + '\'' +
                ", resultType='" + resultType + '\'' +
                ", value='" + value + '\'' +
                ", minNormal=" + minNormal +
                ", maxNormal=" + maxNormal +
                ", parentResult=" + parentResult +
                '}';
    }

    public Set<ResultSignature> getResultSignatures() {
        if (resultSignatures == null) resultSignatures = new HashSet<>();
        return resultSignatures;
    }

    public void setResultSignatures(Set<ResultSignature> resultSignatures) {
        this.resultSignatures = resultSignatures;
    }

    public void addResultSignature(ResultSignature resultSignature) {
        getResultSignatures().add(resultSignature);
    }

    public boolean isValid() {
        if (resultType.equals("N")) return value != null;
        if (resultType.equals("D")) return !value.equals("0") && !value.equals("");
        if (resultType.equals("R")) return value != null && !resultType.isEmpty();
        return false;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }
}

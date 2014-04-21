package org.bahmni.openelis.domain;

import org.bahmni.feed.openelis.utils.JsonTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.sql.Timestamp;

public class TestDetail {
    private String testName;
    private String testUnitOfMeasurement;
    private String testUuid;
    private String panelUuid;
    private Double minNormal;
    private Double maxNormal;
    private String result;
    private String notes;
    private String resultType;
    private String providerUuid;
    private Boolean isAbnormal;
    private Timestamp dateTime;
    private String status;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestUnitOfMeasurement() {
        return testUnitOfMeasurement;
    }

    public void setTestUnitOfMeasurement(String testUnitOfMeasurement) {
        this.testUnitOfMeasurement = testUnitOfMeasurement;
    }

    public String getTestUuid() {
        return testUuid;
    }

    public void setTestUuid(String testUuid) {
        this.testUuid = testUuid;
    }

    public String getPanelUuid() {
        return panelUuid;
    }

    public void setPanelUuid(String panelUuid) {
        this.panelUuid = panelUuid;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String latestNotes) {
        this.notes = latestNotes;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public void setProviderUuid(String providerUuid) {
        this.providerUuid = providerUuid;
    }

    public String getProviderUuid() {
        return providerUuid;
    }

    public void setIsAbnormal(boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public Boolean isAbnormal() {
        return isAbnormal;
    }

    public void setAbnormal(Boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    @JsonSerialize(using=JsonTimeSerializer.class)
    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

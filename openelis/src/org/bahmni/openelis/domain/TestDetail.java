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
package org.bahmni.openelis.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bahmni.feed.openelis.utils.JsonTimeSerializer;

import java.sql.Timestamp;

public class TestDetail {
    private String testName;
    private String testUnitOfMeasurement;
    private String testUuid;
    private String panelUuid;
    private Double minNormal;
    private Double maxNormal;
    private String resultUuid;
    private String result;
    private String notes;
    private String resultType;
    private String providerUuid;
    private Boolean isAbnormal;
    private Timestamp dateTime;
    private String status;
    private String uploadedFileName;

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

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public String getResultUuid() {
        return resultUuid;
    }

    public void setResultUuid(String resultUuid) {
        this.resultUuid = resultUuid;
    }
}

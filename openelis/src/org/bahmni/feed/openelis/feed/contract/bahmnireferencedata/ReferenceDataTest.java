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
package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.CodedTestAnswer;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceDataTest {
    private String id;
    private String description;
    private Boolean isActive;
    private Date lastUpdated;
    private String name;
    private String shortName;
    private Integer sortOrder;
    private String resultType;
    private String testUnitOfMeasure;

    public Collection<CodedTestAnswer> getCodedTestAnswer() {
        return codedTestAnswer;
    }

    public void setCodedTestAnswer(Collection<CodedTestAnswer> codedTestAnswer) {
        this.codedTestAnswer = codedTestAnswer;
    }

    private Collection<CodedTestAnswer> codedTestAnswer;

    public ReferenceDataTest() {
    }

    public ReferenceDataTest(String id, String description, Boolean isActive, Date lastUpdated, String name, String sampleUuid, String shortName, Integer sortOrder, String resultType, String testUnitOfMeasure) {
        this.id = id;
        this.description = description;
        this.isActive = isActive;
        this.lastUpdated = lastUpdated;
        this.name = name;
        this.shortName = shortName;
        this.sortOrder = sortOrder;
        this.resultType = resultType;
        this.testUnitOfMeasure = testUnitOfMeasure;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTestUnitOfMeasure() {
        return testUnitOfMeasure;
    }

    public void setTestUnitOfMeasure(String testUnitOfMeasure) {
        this.testUnitOfMeasure = testUnitOfMeasure;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}

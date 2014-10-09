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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public  class ReferenceDataTest {
    private String id;
    private ReferenceDataDepartment department;
    private String description;
    private Boolean isActive;
    private Date lastUpdated;
    private String name;
    private ReferenceDataSample sample;
    private String shortName;
    private Integer sortOrder;
    private String testUnitOfMeasure;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ReferenceDataDepartment getDepartment() {
        return department;
    }

    public void setDepartment(ReferenceDataDepartment department) {
        this.department = department;
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

    public ReferenceDataSample getSample() {
        return sample;
    }

    public void setSample(ReferenceDataSample sample) {
        this.sample = sample;
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
}

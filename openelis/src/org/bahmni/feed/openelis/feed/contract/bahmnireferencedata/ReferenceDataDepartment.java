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

package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceDataDepartment {

    private String id;
    private Date dateCreated;
    private String description;
    private Boolean isActive;
    private Date lastUpdated;
    private String name;
    private List<MinimalResource> tests;


    public ReferenceDataDepartment() {
    }

    public ReferenceDataDepartment(String id, Date dateCreated, String description, Boolean isActive, Date lastUpdated, String name, List<MinimalResource> tests) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.description = description;
        this.isActive = isActive;
        this.lastUpdated = lastUpdated;
        this.name = name;
        this.tests = tests;
    }

    public ReferenceDataDepartment(String id, Date dateCreated, String description, Boolean isActive, Date lastUpdated, String name) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.description = description;
        this.isActive = isActive;
        this.lastUpdated = lastUpdated;
        this.name = name;
        this.tests = new ArrayList<>();
    }

    public List<MinimalResource> getTests() {
        if (tests == null) {
            this.tests = new ArrayList<>();
        }
        return tests;
    }

    public void setTests(List<MinimalResource> tests) {
        this.tests = tests;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTest(MinimalResource labTest) {
        this.getTests().add(labTest);
    }
}

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


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceDataSample {
    private String id;
    private Date dateCreated;
    private Boolean isActive;
    private Date lastUpdated;
    private String name;
    private String shortName;
    private Integer sortOrder;
    private List<MinimalResource> tests;
    private List<MinimalResource> panels;

    public ReferenceDataSample(String id, Date dateCreated, Boolean isActive, Date lastUpdated, String name, String shortName, Integer sortOrder) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.isActive = isActive;
        this.lastUpdated = lastUpdated;
        this.name = name;
        this.shortName = shortName;
        this.sortOrder = sortOrder;
        this.tests = new ArrayList<>();
        this.panels = new ArrayList<>();
    }

    public ReferenceDataSample() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<MinimalResource> getTests() {
        if (tests == null) {
            this.tests = new ArrayList<>();
        }
        return tests;
    }

    public void setTests(List<MinimalResource> tests) {
        this.tests = tests;
    }

    public List<MinimalResource> getPanels() {
        if (panels == null) {
            this.panels = new ArrayList<>();
        }
        return panels;
    }

    public void setPanels(List<MinimalResource> panels) {
        this.panels = panels;
    }

    public void addTest(MinimalResource labTest) {
        this.getTests().add(labTest);
    }

    public void addPanel(MinimalResource panel) {
        this.getPanels().add(panel);
    }
}

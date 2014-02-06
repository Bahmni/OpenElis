package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceDataPanel {
    
    private String id;
    private String description;
    private Boolean isActive;
    private Date lastUpdated;
    private String name;
    private ReferenceDataSample sample;
    private String shortName;
    private Integer sortOrder;
    private List<ReferenceDataTest> tests;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<ReferenceDataTest> getTests() {
        return tests;
    }

    public void setTests(List<ReferenceDataTest> tests) {
        this.tests = tests;
    }
}

package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceDataAllTestsAndPanels {
    private String id;
    private Date dateCreated;
    private Date lastUpdated;
    private String shortName;
    private String description;
    private String name;
    private Boolean isActive;
    private ReferenceDataTestAndPanels testsAndPanels;

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public ReferenceDataTestAndPanels getTestsAndPanels() {
        return testsAndPanels;
    }

    public void setTestsAndPanels(ReferenceDataTestAndPanels testsAndPanels) {
        this.testsAndPanels = testsAndPanels;
    }
}

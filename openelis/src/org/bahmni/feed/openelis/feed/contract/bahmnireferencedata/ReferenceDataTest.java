package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceDataTest {
    private String id;
    private Date dateCreated;
    private ReferenceDataDepartment department;
    private String description;
    private Boolean isActive;
    private Date lastUpdated;
    private String name;
    private List<ReferenceDataPanel> panel;
    private ReferenceDataSample sample;
    private String shortName;
    private Integer sortOrder;

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

    public List<ReferenceDataPanel> getPanel() {
        return panel;
    }

    public void setPanel(List<ReferenceDataPanel> panel) {
        this.panel = panel;
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
}

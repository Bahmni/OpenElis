package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceDataAllSamples {
    private String id;
    private Date dateCreated;
    private Date lastUpdated;
    private String shortName;
    private String description;
    private String name;
    private Boolean isActive;
    private List<ReferenceDataSample> samples= new ArrayList<>();

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

    public void setSamples(List<ReferenceDataSample> samples) {
        this.samples = samples;
    }

    public String getId() {
        return id;
    }

    public List<ReferenceDataSample> getSamples() {
        return samples;
    }
}

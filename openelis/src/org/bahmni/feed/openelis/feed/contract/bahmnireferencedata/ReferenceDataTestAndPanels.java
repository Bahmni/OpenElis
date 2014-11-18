package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReferenceDataTestAndPanels {

    private List<ReferenceDataTest> tests;
    private List<ReferenceDataPanel> panels;
    private String id;
    private Date dateCreated;
    private Date lastUpdated;
    private String name;
    private Boolean isActive;

    public ReferenceDataTestAndPanels() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public List<ReferenceDataPanel> getPanels() {
        if (this.panels == null) {
            this.panels = new ArrayList<>();
        }
        return panels;
    }

    public void setPanels(List<ReferenceDataPanel> panels) {
        this.panels = panels;
    }

    public List<ReferenceDataTest> getTests() {
        if (this.tests == null) {
            this.tests = new ArrayList<>();
        }
        return tests;
    }

    public void setTests(List<ReferenceDataTest> tests) {
        this.tests = tests;
    }

    public void addTest(ReferenceDataTest referenceDataTest) {
        this.getTests().add(referenceDataTest);
    }

    public void addPanel(ReferenceDataPanel referenceDataPanel) {
        this.getPanels().add(referenceDataPanel);
    }
}

package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceDataAllTestsAndPanels {
    private String id;
    private Date dateCreated;
    private Date lastUpdated;
    private String shortName;
    private String description;
    private String name;
    private Boolean isActive;
    private List<ReferenceDataTest> tests= new ArrayList<>();
    private List<ReferenceDataPanel> panels= new ArrayList<>();

    public String getId() {
        return id;
    }

    public List<ReferenceDataTest> getTests() {
        return tests;
    }

    public List<ReferenceDataPanel> getPanels() {
        return panels;
    }

}

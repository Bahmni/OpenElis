package org.bahmni.feed.openelis.feed.contract.openmrs;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPersonAttributeType {
    public static final String ATTRIBUTE1_NAME = "primaryRelative";
    public static final String ATTRIBUTE2_NAME = "occupation";
    private String display;

    public OpenMRSPersonAttributeType() {
    }

    public OpenMRSPersonAttributeType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
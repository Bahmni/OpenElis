package org.bahmni.feed.openelis.feed.contract.openmrs;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPersonAttributeType {
    public static final String PRIMARY_RELATIVE = "primaryRelative";
    public static final String OCCUPATION = "occupation";
    public static final String HEALTH_CENTER = "Health Center";
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
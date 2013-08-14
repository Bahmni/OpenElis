package org.bahmni.feed.openelis.feed.contract.openmrs;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPersonAttribute {
    private String display;
    private String value;
    private OpenMRSPersonAttributeType attributeType;

    public OpenMRSPersonAttribute(String value, OpenMRSPersonAttributeType attributeType, String dispaly) {
        this.value = value;
        this.attributeType = attributeType;
        this.display = dispaly;
    }

    public OpenMRSPersonAttribute() {
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public OpenMRSPersonAttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(OpenMRSPersonAttributeType attributeType) {
        this.attributeType = attributeType;
    }
}
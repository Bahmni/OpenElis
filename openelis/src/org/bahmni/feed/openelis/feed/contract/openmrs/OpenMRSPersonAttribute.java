package org.bahmni.feed.openelis.feed.contract.openmrs;

public class OpenMRSPersonAttribute {
    private String value;
    private OpenMRSPersonAttributeType attributeType;

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
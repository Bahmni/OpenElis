/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package org.bahmni.feed.openelis.feed.contract.openmrs;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPersonAttribute {
    private String display;
    private OpenMRSPersonAttributeTypeValue value;
    private OpenMRSPersonAttributeType attributeType;

    public OpenMRSPersonAttribute(OpenMRSPersonAttributeTypeValue value, OpenMRSPersonAttributeType attributeType, String dispaly) {
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
        return value.getDisplay();
    }

    public void setValue(OpenMRSPersonAttributeTypeValue value) {
        this.value = value;
    }

    public OpenMRSPersonAttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(OpenMRSPersonAttributeType attributeType) {
        this.attributeType = attributeType;
    }
}

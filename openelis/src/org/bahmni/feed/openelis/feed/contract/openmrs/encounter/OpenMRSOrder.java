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

package org.bahmni.feed.openelis.feed.contract.openmrs.encounter;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSOrder {
    public static final String LAB_ORDER_TYPE = "Lab Order";

    private String uuid;
    private String orderType;

    public String getCommentToFulfiller() {
        return commentToFulfiller;
    }

    public void setCommentToFulfiller(String commentToFulfiller) {
        this.commentToFulfiller = commentToFulfiller;
    }

    private String commentToFulfiller;
    private String action;
    private Date dateStopped;

    private Boolean voided;
    private OpenMRSConcept concept;

    public OpenMRSOrder() {
    }

    public OpenMRSOrder(String uuid, String orderType, OpenMRSConcept concept, Boolean voided) {
        this.uuid = uuid;
        this.orderType = orderType;
        this.voided = voided;
        this.concept = concept;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOrderType() {
        return orderType;
    }

    public OpenMRSConcept getConcept() {
        return concept;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setConcept(OpenMRSConcept concept) {
        this.concept = concept;
    }

    public boolean isLabOrder() {
        return LAB_ORDER_TYPE.equals(orderType);
    }

    public boolean isLabOrderForPanel() {
        return concept != null && concept.isSet();
    }

    public Boolean isVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public String getLabTestName() {
        if (!isLabOrder())
            return null;
        return concept.getName().getName();
    }

    public String getTestOrPanelUUID() {
        if (!isLabOrder())
            return null;
        return concept.getUuid();
    }

    public Date getDateStopped() {
        return dateStopped;
    }

    public void setDateStopped(Date dateStopped) {
        this.dateStopped = dateStopped;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

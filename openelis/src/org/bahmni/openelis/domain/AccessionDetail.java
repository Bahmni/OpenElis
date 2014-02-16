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

package org.bahmni.openelis.domain;

import org.bahmni.feed.openelis.utils.JsonTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.sql.Timestamp;
import java.util.List;

public class AccessionDetail {
    private String accessionUuid;
    private String patientUuid;
    private String patientIdentifier;
    private String patientFirstName;
    private String patientLastName;
    private Timestamp dateTime;
    private List<TestDetail> testDetails;

    public String getAccessionUuid() {
        return accessionUuid;
    }

    public void setAccessionUuid(String accessionUuid) {
        this.accessionUuid = accessionUuid;
    }

    public String getPatientUuid() {
        return patientUuid;
    }

    public void setPatientUuid(String patientUuid) {
        this.patientUuid = patientUuid;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public List<TestDetail> getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(List<TestDetail> tests) {
        this.testDetails = tests;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    @JsonSerialize(using=JsonTimeSerializer.class)
    public Timestamp getDateTime() {
        return dateTime;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }
}

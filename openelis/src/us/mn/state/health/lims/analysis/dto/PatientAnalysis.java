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
 * Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package us.mn.state.health.lims.analysis.dto;

import us.mn.state.health.lims.analysis.valueholder.Analysis;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PatientAnalysis {
    private BigDecimal id;

    private BigDecimal pat_id;
    private String national_id;
    private String external_id;
    private String gender;
    private Timestamp birth_date;
    private String first_name;
    private String last_name;
    private String identity_data;


    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Timestamp getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Timestamp birth_date) {
        this.birth_date = birth_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getIdentity_data() {
        return identity_data;
    }

    public void setIdentity_data(String identity_data) {
        this.identity_data = identity_data;
    }


    public Analysis getAnalysis() {
        Analysis analysis = new Analysis();
        analysis.setId(id.toString());
        return analysis;
    }

    public BigDecimal getPat_id() {
        return pat_id;
    }

    public void setPat_id(BigDecimal pat_id) {
        this.pat_id = pat_id;
    }
}

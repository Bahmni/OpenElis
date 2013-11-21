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

package us.mn.state.health.lims.sample.bean;

import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.test.valueholder.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class SampleTestCollection {
    public SampleItem item;
    public List<Test> tests;
    public String collectionDate;
    public List<ObservationHistory> initialSampleConditionIdList;

    public SampleTestCollection(SampleItem item, List<Test> tests, String collectionDate, List<ObservationHistory> initialConditionList) {
        this.item = item;
        this.tests = tests;
        this.collectionDate = collectionDate;
        initialSampleConditionIdList = initialConditionList;
    }

    public SampleTestCollection(SampleItem item, List<Test> tests, Date collectionDate) {
        // TODO : Mujir - is DateUtil.convertSqlDateToStringDate(nowAsSqlDate) ok?
        this(item, tests, DateUtil.formatDateTimeAsText(collectionDate), new ArrayList<ObservationHistory>());
    }
}

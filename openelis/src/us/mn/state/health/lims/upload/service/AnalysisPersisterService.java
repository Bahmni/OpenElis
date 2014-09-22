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

package us.mn.state.health.lims.upload.service;

import java.sql.Timestamp;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnalysisPersisterService {

    private static final String DEFAULT_ANALYSIS_TYPE = "MANUAL";
    private AnalysisDAO analysisDAO;

    public AnalysisPersisterService() {
        this(new AnalysisDAOImpl());
    }

    public AnalysisPersisterService(AnalysisDAO analysisDAO) {
        this.analysisDAO = analysisDAO;
    }

    public Analysis save(Test test, String sampleDate, SampleItem sampleItem, String sysUserId) throws ParseException {
        Analysis analysis = new Analysis();
        analysis.setSysUserId(sysUserId);
        analysis.setSampleItem(sampleItem);
        analysis.setTest(test);
        SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate = datetimeFormatter.parse(sampleDate);
        java.sql.Date analysisDate = new java.sql.Date(parsedDate.getTime());
        analysis.setStartedDate(analysisDate);
        analysis.setCompletedDate(analysisDate);
        analysis.setEnteredDate(new Timestamp(analysisDate.getTime()));
        analysis.setIsReportable(test.getIsReportable());
        analysis.setAnalysisType(DEFAULT_ANALYSIS_TYPE);
        analysis.setRevision(SystemConfiguration.getInstance().getAnalysisDefaultRevision());
        analysis.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Finalized));
        analysis.setTestSection(test.getTestSection());
        analysisDAO.insertData(analysis, true);
        return analysis;
    }

}

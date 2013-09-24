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

package us.mn.state.health.lims.analysis.daoimpl;

import junit.framework.Assert;
import org.junit.Test;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import junit.framework.TestListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalysisDAOImplTest {
    @Test
    public void getAllByAccessionNumberAndStatus_returns_empty_list_for_null_accession_number() {
        List<Analysis> actualAnalysises = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatus(null, Arrays.asList(StatusOfSampleUtil.AnalysisStatus.BiologistRejected));
        Assert.assertTrue("should not return analysis for null accession number", actualAnalysises.isEmpty());
    }

    @Test
    public void getAllByAccessionNumberAndStatus_returns_empty_list_for_null_status() {
        List<Analysis> actualAnalysises = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatus("12345", null);
        Assert.assertTrue("should not return analysis for null status", actualAnalysises.isEmpty());
    }

    @Test
    public void getAllByAccessionNumberAndStatus_returns_empty_list_for_empty_statuses() {
        List<Analysis> actualAnalysises = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatus("12345", new ArrayList<StatusOfSampleUtil.AnalysisStatus>());
        Assert.assertTrue("should not return analysis for null status", actualAnalysises.isEmpty());
    }
}

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
import org.bahmni.feed.openelis.IT;
import org.junit.Test;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.daoimpl.SampleItemDAOImpl;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.TestSection;

import java.util.Arrays;
import java.util.List;

public class AnalysisDAOImplIT extends IT {

    @Test
    public void getAllByAccessionNumberAndStatus_returns_matching_analysis() {
        StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        String accessionNumber = "12345";
        Sample startedSample = createAndSaveSample(accessionNumber);
        SampleItem enteredSampleItem = createAndSaveSampleItem(startedSample);

        createAndSaveAnalysis(enteredSampleItem, toBeValidatedAnalysisStatus, "Hematology");
        createAndSaveAnalysis(enteredSampleItem, toBeValidatedAnalysisStatus, "Hematology");

        List<Analysis> actualAnalyses = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatus(accessionNumber, Arrays.asList(toBeValidatedAnalysisStatus));

        Assert.assertTrue("analyses should have same accessionNumber", matchesAccessionNumberAndStatus(actualAnalyses, accessionNumber, toBeValidatedAnalysisStatus));
    }

    @Test
    public void getAllByAccessionNumberAndStatus_does_not_return_non_matching_accession_numbers() {
        String accessionNumber = "12345";

        StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        Sample nonMatchingAccessionNumberSample = createAndSaveSample("23456");
        SampleItem nonMatchingSampleItem = createAndSaveSampleItem(nonMatchingAccessionNumberSample);
        createAndSaveAnalysis(nonMatchingSampleItem, toBeValidatedAnalysisStatus, "Hematology");

        List<Analysis> actualAnalyses = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatus(accessionNumber, Arrays.asList(toBeValidatedAnalysisStatus));

        Assert.assertTrue("should not return analysis with non matching accessionNumber", actualAnalyses.isEmpty());
    }

    @Test
    public void getAllByAccessionNumberAndStatus_does_not_return_non_matching_analysis_status() {
        String accessionNumber = "12345";
        StatusOfSampleUtil.AnalysisStatus nonMatchingStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        Sample nonMatchingAccessionNumberSample = createAndSaveSample(accessionNumber);
        SampleItem nonMatchingSampleItem = createAndSaveSampleItem(nonMatchingAccessionNumberSample);
        createAndSaveAnalysis(nonMatchingSampleItem, nonMatchingStatus, "Hematology");

        List<Analysis> actualAnalyses = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatus(accessionNumber,
                Arrays.asList(StatusOfSampleUtil.AnalysisStatus.BiologistRejected));

        Assert.assertTrue("should not return analysis with non matching analysis status", actualAnalyses.isEmpty());
    }

    @Test
    public void getAllByStatus_returns_all_analyses() {
        SampleItem matchingSampleItemWithATestSection = createAndSaveSampleItem(createAndSaveSample("12345"));
        Analysis firstMatchingAnalysis = createAndSaveAnalysis(matchingSampleItemWithATestSection, StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, "Hematology");

        SampleItem matchingSampleItemWithADifferentTestSection = createAndSaveSampleItem(createAndSaveSample("123456"));
        Analysis secondMatchingAnalysis = createAndSaveAnalysis(matchingSampleItemWithADifferentTestSection, StatusOfSampleUtil.AnalysisStatus.NotTested, "Biochemistry");

        SampleItem nonMatchingSampleItemWithDifferentStatus = createAndSaveSampleItem(createAndSaveSample("1234567"));
        Analysis nonMatchingAnalysis = createAndSaveAnalysis(nonMatchingSampleItemWithDifferentStatus, StatusOfSampleUtil.AnalysisStatus.BiologistRejected, "Biochemistry");

        List<Analysis> actualAnalyses = new AnalysisDAOImpl().getAllAnalysisByStatus(
                Arrays.asList(StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, StatusOfSampleUtil.AnalysisStatus.NotTested));

        Assert.assertTrue("should return analysis with matching analysis status", actualAnalyses.contains(firstMatchingAnalysis));
        Assert.assertTrue("should return analysis with matching analysis status", actualAnalyses.contains(secondMatchingAnalysis));
        Assert.assertTrue("should not return analysis with non matching analysis status", !actualAnalyses.contains(nonMatchingAnalysis));
    }

    private boolean matchesAccessionNumberAndStatus(List<Analysis> actualAnalyses, String accessionNumber,
                                                    StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus) {
        for (Analysis actualAnalysis : actualAnalyses) {
            if ((!actualAnalysis.getSampleItem().getSample().getAccessionNumber().equals(accessionNumber)) &&
                    (!actualAnalysis.getStatusId().equals(StatusOfSampleUtil.getStatusID(toBeValidatedAnalysisStatus))))
                return false;
        }
        return true;
    }

    private Analysis createAndSaveAnalysis(SampleItem sampleItem, StatusOfSampleUtil.AnalysisStatus analysisStatus, String testSectionName) {

        TestSectionDAO testSectionDAO = new TestSectionDAOImpl();
        TestSection testSection = testSectionDAO.getTestSectionByName(testSectionName);

        Analysis analysisToBeValidated = new Analysis();
        analysisToBeValidated.setSampleItem(sampleItem);
        analysisToBeValidated.setAnalysisType(IActionConstants.ANALYSIS_TYPE_MANUAL);
        analysisToBeValidated.setStatusId(StatusOfSampleUtil.getStatusID(analysisStatus));
        analysisToBeValidated.setSysUserId("1");
        analysisToBeValidated.setTestSection(testSection);

        new AnalysisDAOImpl().insertData(analysisToBeValidated, false);

        return analysisToBeValidated;
    }

    private SampleItem createAndSaveSampleItem(Sample startedSample) {
        SampleItem enteredSampleItem = new SampleItem();
        enteredSampleItem.setSample(startedSample);
        enteredSampleItem.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered));
        enteredSampleItem.setSortOrder("1");
        enteredSampleItem.setSysUserId("1");
        new SampleItemDAOImpl().insertData(enteredSampleItem);
        return enteredSampleItem;
    }

    private Sample createAndSaveSample(String accessionNumber) {
        Sample startedSample = new Sample();
        startedSample.setAccessionNumber(accessionNumber);
        startedSample.setStatusId(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Started));
        startedSample.setEnteredDate(DateUtil.convertStringDateToSqlDate("01/01/2001"));
        startedSample.setReceivedTimestamp(DateUtil.convertStringDateToTimestamp("01/01/2001 00:00"));
        startedSample.setSysUserId("1");
        new SampleDAOImpl().insertDataWithAccessionNumber(startedSample);
        return startedSample;
    }
}

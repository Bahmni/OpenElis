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

import org.bahmni.feed.openelis.IT;
import org.junit.Test;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.dbhelper.DBHelper;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AnalysisDAOImplIT extends IT {

    @Test
    public void getAllByAccessionNumberAndStatus_returns_matching_analysis() {
        StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        String accessionNumber = "12345";
        Sample startedSample = DBHelper.createAndSaveSample(accessionNumber);
        SampleItem enteredSampleItem = DBHelper.createAndSaveSampleItem(startedSample);

        DBHelper.createAndSaveAnalysis(enteredSampleItem, toBeValidatedAnalysisStatus, "Hematology", null, null);
        DBHelper.createAndSaveAnalysis(enteredSampleItem, toBeValidatedAnalysisStatus, "Hematology", null, null);

        List<Analysis> actualAnalyses = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatusAndSampleType(accessionNumber, Arrays.asList(toBeValidatedAnalysisStatus), "");

        assertTrue("analyses should have same accessionNumber", matchesAccessionNumberAndStatus(actualAnalyses, accessionNumber, toBeValidatedAnalysisStatus));
    }

    @Test
    public void shouldGetAllAnalysisByAccessionNumberAndStatusAndSampleType_when_sample_type_is_not_empty() {
        StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        String accessionNumber = "12345";
        Sample startedSample = DBHelper.createAndSaveSample(accessionNumber);
        SampleItem enteredSampleItem = DBHelper.createAndSaveSampleItem(startedSample, "Blood");

        DBHelper.createAndSaveAnalysis(enteredSampleItem, toBeValidatedAnalysisStatus, "Hematology", null, null);
        DBHelper.createAndSaveAnalysis(enteredSampleItem, toBeValidatedAnalysisStatus, "Hematology", null, null);

        List<Analysis> bloodAnalysis = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatusAndSampleType(accessionNumber, Arrays.asList(toBeValidatedAnalysisStatus), "Blood");

        assertEquals(2, bloodAnalysis.size());
        assertTrue("analyses should have same accessionNumber", matchesAccessionNumberAndStatus(bloodAnalysis, accessionNumber, toBeValidatedAnalysisStatus));

        assertTrue(new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatusAndSampleType(accessionNumber, Arrays.asList(toBeValidatedAnalysisStatus), "Urine").isEmpty());
    }

    @Test
    public void getAllByAccessionNumberAndStatus_does_not_return_non_matching_accession_numbers() {
        String accessionNumber = "12345";

        StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        Sample nonMatchingAccessionNumberSample = DBHelper.createAndSaveSample("23456");
        SampleItem nonMatchingSampleItem = DBHelper.createAndSaveSampleItem(nonMatchingAccessionNumberSample);
        DBHelper.createAndSaveAnalysis(nonMatchingSampleItem, toBeValidatedAnalysisStatus, "Hematology", null, null);

        List<Analysis> actualAnalyses = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatusAndSampleType(accessionNumber, Arrays.asList(toBeValidatedAnalysisStatus), "");

        assertTrue("should not return analysis with non matching accessionNumber", actualAnalyses.isEmpty());
    }

    @Test
    public void getAllByAccessionNumberAndStatus_does_not_return_non_matching_analysis_status() {
        String accessionNumber = "12345";
        StatusOfSampleUtil.AnalysisStatus nonMatchingStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        Sample nonMatchingAccessionNumberSample = DBHelper.createAndSaveSample(accessionNumber);
        SampleItem nonMatchingSampleItem = DBHelper.createAndSaveSampleItem(nonMatchingAccessionNumberSample);
        DBHelper.createAndSaveAnalysis(nonMatchingSampleItem, nonMatchingStatus, "Hematology", null, null);

        List<Analysis> actualAnalyses = new AnalysisDAOImpl().getAllAnalysisByAccessionNumberAndStatusAndSampleType(accessionNumber,
        Arrays.asList(StatusOfSampleUtil.AnalysisStatus.BiologistRejected), "");

        assertTrue("should not return analysis with non matching analysis status", actualAnalyses.isEmpty());
    }

    @Test
    public void getAllByStatus_returns_all_analyses() {
        SampleItem matchingSampleItemWithATestSection = DBHelper.createAndSaveSampleItem(DBHelper.createAndSaveSample("12345"));
        Analysis firstMatchingAnalysis = DBHelper.createAndSaveAnalysis(matchingSampleItemWithATestSection, StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, "Hematology", null, null);

        SampleItem matchingSampleItemWithADifferentTestSection = DBHelper.createAndSaveSampleItem(DBHelper.createAndSaveSample("123456"));
        Analysis secondMatchingAnalysis = DBHelper.createAndSaveAnalysis(matchingSampleItemWithADifferentTestSection, StatusOfSampleUtil.AnalysisStatus.NotTested, "Biochemistry", null, null);

        SampleItem nonMatchingSampleItemWithDifferentStatus = DBHelper.createAndSaveSampleItem(DBHelper.createAndSaveSample("1234567"));
        Analysis nonMatchingAnalysis = DBHelper.createAndSaveAnalysis(nonMatchingSampleItemWithDifferentStatus, StatusOfSampleUtil.AnalysisStatus.BiologistRejected, "Biochemistry", null, null);

        List<Analysis> actualAnalyses = new AnalysisDAOImpl().getAllAnalysisByStatus(
                Arrays.asList(StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, StatusOfSampleUtil.AnalysisStatus.NotTested));

        assertTrue("should return analysis with matching analysis status", actualAnalyses.contains(firstMatchingAnalysis));
        assertTrue("should return analysis with matching analysis status", actualAnalyses.contains(secondMatchingAnalysis));
        assertTrue("should not return analysis with non matching analysis status", !actualAnalyses.contains(nonMatchingAnalysis));
    }

    @Test
    public void getAnalysisBySampleIds_shouldGiveAllTheAnalysisForGivenSampleIds() {
        StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        String accessionNumber1 = "12345";
        Sample sample1 = DBHelper.createAndSaveSample(accessionNumber1);
        SampleItem sampleItem1 = DBHelper.createAndSaveSampleItem(sample1, "Blood");

        String accessionNumber2 = "12346";
        Sample sample2 = DBHelper.createAndSaveSample(accessionNumber2);
        SampleItem sampleItem2 = DBHelper.createAndSaveSampleItem(sample2, "Urine");

        DBHelper.createAndSaveAnalysis(sampleItem1, toBeValidatedAnalysisStatus, "Hematology", null, null);
        DBHelper.createAndSaveAnalysis(sampleItem1, toBeValidatedAnalysisStatus, "Hematology", null, null);

        DBHelper.createAndSaveAnalysis(sampleItem2, toBeValidatedAnalysisStatus, "Hematology", null, null);

        List<Analysis> bloodAnalysis = new AnalysisDAOImpl().getAnalysisBySampleIds(Arrays.asList(Integer.parseInt(sample1.getId()), Integer.parseInt(sample2.getId())), new HashSet<Integer>(), "Blood");

        assertEquals(2, bloodAnalysis.size());
        assertTrue("analyses should have same accessionNumber", matchesAccessionNumberAndStatus(bloodAnalysis, accessionNumber1, toBeValidatedAnalysisStatus));

    }

    @Test
    public void getAnalysisBySampleIds_shouldGiveAllTheAnalysisForGivenSampleIdsWhenSampleTypeIsEmpty() {
        StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        String accessionNumber1 = "12345";
        Sample sample1 = DBHelper.createAndSaveSample(accessionNumber1);
        SampleItem sampleItem1 = DBHelper.createAndSaveSampleItem(sample1, "Blood");

        String accessionNumber2 = "12346";
        Sample sample2 = DBHelper.createAndSaveSample(accessionNumber2);
        SampleItem sampleItem2 = DBHelper.createAndSaveSampleItem(sample2, "Urine");

        DBHelper.createAndSaveAnalysis(sampleItem1, toBeValidatedAnalysisStatus, "Hematology", null, null);
        DBHelper.createAndSaveAnalysis(sampleItem1, toBeValidatedAnalysisStatus, "Hematology", null, null);

        DBHelper.createAndSaveAnalysis(sampleItem2, toBeValidatedAnalysisStatus, "Hematology", null, null);

        List<Analysis> analysisList = new AnalysisDAOImpl().getAnalysisBySampleIds(Arrays.asList(Integer.parseInt(sample1.getId()), Integer.parseInt(sample2.getId())), new HashSet<Integer>(), "");

        assertEquals(3, analysisList.size());

        assertTrue( matchesAccessionNumberAndStatus(analysisList, accessionNumber1, toBeValidatedAnalysisStatus));
        assertTrue( matchesAccessionNumberAndStatus(analysisList, accessionNumber2, toBeValidatedAnalysisStatus));

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

}

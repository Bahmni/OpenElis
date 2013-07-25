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

import java.util.Arrays;
import java.util.List;

public class AnalysisDAOImplIT extends IT {

    @Test
    public void getAllByAccessionNumberAndStatus_returns_matching_analysis() {
        StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        String accessionNumber = "12345";
        Sample startedSample = createAndSaveSample(accessionNumber);
        SampleItem enteredSampleItem = createAndSaveSampleItem(startedSample);

        createAndSaveAnalysis(enteredSampleItem, toBeValidatedAnalysisStatus);
        createAndSaveAnalysis(enteredSampleItem, toBeValidatedAnalysisStatus);

        List<Analysis> actualAnalysises = new AnalysisDAOImpl().getAllByAccessionNumberAndStatus(accessionNumber, Arrays.asList(toBeValidatedAnalysisStatus));

        Assert.assertTrue("analysises should have same accessionNumber", matchesAccessionNumberAndStatus(actualAnalysises, accessionNumber, toBeValidatedAnalysisStatus));
    }

    @Test
    public void getAllByAccessionNumberAndStatus_does_not_return_non_matching_accession_numbers() {
        String accessionNumber = "12345";

        StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        Sample nonMatchingAccessionNumberSample = createAndSaveSample("23456");
        SampleItem nonMatchingSampleItem = createAndSaveSampleItem(nonMatchingAccessionNumberSample);
        createAndSaveAnalysis(nonMatchingSampleItem, toBeValidatedAnalysisStatus);

        List<Analysis> actualAnalysises = new AnalysisDAOImpl().getAllByAccessionNumberAndStatus(accessionNumber, Arrays.asList(toBeValidatedAnalysisStatus));

        Assert.assertTrue("should not return analysis with non matching accessionNumber", actualAnalysises.isEmpty());
    }

    @Test
    public void getAllByAccessionNumberAndStatus_does_not_return_non_matching_analysis_status() {
        String accessionNumber = "12345";
        StatusOfSampleUtil.AnalysisStatus nonMatchingStatus = StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
        Sample nonMatchingAccessionNumberSample = createAndSaveSample(accessionNumber);
        SampleItem nonMatchingSampleItem = createAndSaveSampleItem(nonMatchingAccessionNumberSample);
        createAndSaveAnalysis(nonMatchingSampleItem, nonMatchingStatus);

        List<Analysis> actualAnalysises = new AnalysisDAOImpl().getAllByAccessionNumberAndStatus(accessionNumber, Arrays.asList(StatusOfSampleUtil.AnalysisStatus.TechnicalRejected));

        Assert.assertTrue("should not return analysis with non matching analysis status", actualAnalysises.isEmpty());
    }

    private boolean matchesAccessionNumberAndStatus(List<Analysis> actualAnalysises, String accessionNumber, StatusOfSampleUtil.AnalysisStatus toBeValidatedAnalysisStatus) {
        for (Analysis actualAnalysis : actualAnalysises) {
            if ((!actualAnalysis.getSampleItem().getSample().getAccessionNumber().equals(accessionNumber)) &&
                    (!actualAnalysis.getStatusId().equals(StatusOfSampleUtil.getStatusID(toBeValidatedAnalysisStatus))))
                return false;
        }
        return true;
    }

    private Analysis createAndSaveAnalysis(SampleItem sampleItem, StatusOfSampleUtil.AnalysisStatus analysisStatus) {
        Analysis analysisToBeValidated = new Analysis();
        analysisToBeValidated.setSampleItem(sampleItem);
        analysisToBeValidated.setAnalysisType(IActionConstants.ANALYSIS_TYPE_MANUAL);
        analysisToBeValidated.setStatusId(StatusOfSampleUtil.getStatusID(analysisStatus));
        analysisToBeValidated.setSysUserId("1");

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

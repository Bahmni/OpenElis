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
*
* Contributor(s): CIRG, University of Washington, Seattle WA.
*/

package us.mn.state.health.lims.sample.daoimpl;

import org.bahmni.feed.openelis.IT;
import org.junit.Test;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.dbhelper.DBHelper;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SampleDAOImplTest extends IT{

    SampleDAO sampleDAO = new SampleDAOImpl();

    @Test
    public void shouldBeAbleToRetrieveCompleteSampleTreeByUuid() {
        String accessionNumber = "12347";

        Sample sample = DBHelper.createAndSaveSample(accessionNumber);
        SampleItem sampleItem = DBHelper.createAndSaveSampleItem(sample);
        Panel panel = DBHelper.createAndSavePanel();
        us.mn.state.health.lims.test.valueholder.Test test = DBHelper.createAndSaveTest();
        Analysis analysis = DBHelper.createAndSaveAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.NotTested, "Hematology", panel, test);
        TestResult testResult = DBHelper.createAndSaveTestResult(test);
        Result result = DBHelper.createAndSaveResult(analysis, testResult);

        int statusId = Integer.parseInt(SystemConfiguration.getInstance().getSampleStatusEntry2Complete()); //This is completed

        changeSampleStatus(sample, statusId);

        List<Sample> retrievedSamples = new SampleDAOImpl().getSamplesByUuidAndStatus(sample.getUUID(), statusId);
        assertEquals(1, retrievedSamples.size());
        Sample sampleRetrieved = retrievedSamples.get(0);
        assertNotNull(sampleRetrieved);
        SampleItem retrievedSampleItem = (SampleItem) sampleRetrieved.getSampleItems().toArray()[0];
        assertNotNull(retrievedSampleItem);
        Analysis retrievedAnalysis = (Analysis) retrievedSampleItem.getAnalyses().toArray()[0];
        assertNotNull(retrievedAnalysis);
        assertNotNull(retrievedAnalysis.getPanel());
        Result retrievedResults = (Result) retrievedAnalysis.getResults().toArray()[0];
        assertNotNull(retrievedResults);
        assertNotNull(retrievedResults.getTestResult());
        assertNotNull(retrievedResults.getTestResult().getTest());
    }

    private void changeSampleStatus(Sample sample, int status){
        sample.setStatus(SystemConfiguration.getInstance().getSampleStatusEntry2Complete());
        sample.setStatusId(SystemConfiguration.getInstance().getSampleStatusEntry2Complete());
        sampleDAO.updateData(sample);

    }

    @Test
    public void shouldRetrieveSamplesByUuid() {
        String accessionNumber = "12347";

        Sample sample = DBHelper.createAndSaveSample(accessionNumber);
        SampleItem sampleItem = DBHelper.createAndSaveSampleItem(sample);
        Panel panel = DBHelper.createAndSavePanel();
        us.mn.state.health.lims.test.valueholder.Test test = DBHelper.createAndSaveTest();
        Analysis analysis = DBHelper.createAndSaveAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.NotTested, "Hematology", panel, test);
        TestResult testResult = DBHelper.createAndSaveTestResult(test);
        Result result = DBHelper.createAndSaveResult(analysis, testResult);

        List<Sample> retrievedSamples = new SampleDAOImpl().getSamplesByEncounterUuid(sample.getUUID());

        assertEquals(retrievedSamples.size(), 1);
        assertNotNull(retrievedSamples.get(0));
        SampleItem retrievedSampleItem = (SampleItem) retrievedSamples.get(0).getSampleItems().toArray()[0];
        assertNotNull(retrievedSampleItem);
        Analysis retrievedAnalysis = (Analysis) retrievedSampleItem.getAnalyses().toArray()[0];
        assertNotNull(retrievedAnalysis);
        assertNotNull(retrievedAnalysis.getPanel());
        Result retrievedResults = (Result) retrievedAnalysis.getResults().toArray()[0];
        assertNotNull(retrievedResults);
        assertNotNull(retrievedResults.getTestResult());
        assertNotNull(retrievedResults.getTestResult().getTest());
    }

    @Test
    public void shouldGetSampleByAccessionAndSampleType() {
        String accessionNumber = "29052018-001";
        String sampleType = "Blood";

        Sample sample = DBHelper.createAndSaveSample(accessionNumber);
        SampleItem sampleItem = DBHelper.createAndSaveSampleItem(sample);
        TypeOfSample typeOfSample = DBHelper.createAndSaveTypeOfSample("Blood");
        sampleItem.setTypeOfSample(typeOfSample);
        Panel panel = DBHelper.createAndSavePanel();
        us.mn.state.health.lims.test.valueholder.Test test = DBHelper.createAndSaveTest();
        Analysis analysis = DBHelper.createAndSaveAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.NotTested, "Hematology", panel, test);
        TestResult testResult = DBHelper.createAndSaveTestResult(test);
        Result result = DBHelper.createAndSaveResult(analysis, testResult);

        Sample actualSample = new SampleDAOImpl().getSampleByAccessionNumberAndType(accessionNumber, sampleType);

        assertNotNull(actualSample);
        assertEquals(accessionNumber, actualSample.getAccessionNumber());
        Set<SampleItem> sampleItems = actualSample.getSampleItems();

        assertEquals(1, sampleItems.size());
        Iterator<SampleItem> iterator = sampleItems.iterator();
        SampleItem item = iterator.next();

        assertEquals(sampleType, item.getTypeOfSample().getDescription());
    }

    @Test
    public void shouldGetSampleByUuidAndSampleType() {
        String uuid = "56789-7865";
        String sampleType = "Blood";

        Sample sample = DBHelper.createSampleWithUuid(uuid);
        SampleItem sampleItem = DBHelper.createAndSaveSampleItem(sample);
        TypeOfSample typeOfSample = DBHelper.createAndSaveTypeOfSample("Blood");
        sampleItem.setTypeOfSample(typeOfSample);
        Panel panel = DBHelper.createAndSavePanel();
        us.mn.state.health.lims.test.valueholder.Test test = DBHelper.createAndSaveTest();
        Analysis analysis = DBHelper.createAndSaveAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.NotTested, "Hematology", panel, test);
        TestResult testResult = DBHelper.createAndSaveTestResult(test);
        Result result = DBHelper.createAndSaveResult(analysis, testResult);

        Sample actualSample = new SampleDAOImpl().getSampleByUuidAndSampleTypeIdAndWithoutAccessionNumber(uuid, typeOfSample.getId());
        assertNotNull(actualSample);

        assertEquals(uuid, actualSample.getUUID());
        Set<SampleItem> sampleItems = actualSample.getSampleItems();

        assertEquals(1, sampleItems.size());
        Iterator<SampleItem> iterator = sampleItems.iterator();
        SampleItem item = iterator.next();

        assertEquals(sampleType, item.getTypeOfSample().getDescription());
    }
}

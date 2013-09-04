package us.mn.state.health.lims.upload.service;

import org.junit.Before;
import org.mockito.Mock;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AnalysisPersisterServiceTest {

    private AnalysisPersisterService analysisPersisterService;

    @Mock
    private AnalysisDAO analysisDAO;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        analysisPersisterService = new AnalysisPersisterService(analysisDAO);
    }

    @org.junit.Test
    public void shouldPersistAnalysis() throws ParseException {
        Test test1 = new Test();
        test1.setId("1");
        SampleItem sampleItem1 = new SampleItem();
        sampleItem1.setId("1");
        String sampleDate = "25-02-2012";
        SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedDate = datetimeFormatter.parse(sampleDate);
        java.sql.Date analysisDate = new java.sql.Date(parsedDate.getTime());
        String sysUserId = "123";

        Analysis analysis = analysisPersisterService.save(test1, sampleDate, sampleItem1, sysUserId);

        verify(analysisDAO).insertData(analysis, true);
        assertEquals(sysUserId, analysis.getSysUserId());
        assertEquals(test1.getId(), analysis.getTest().getId());
        assertEquals(sampleItem1.getId(), analysis.getSampleItem().getId());
        assertEquals(analysisDate, analysis.getCompletedDate());
        assertEquals(analysisDate, analysis.getStartedDate());
        assertEquals("MANUAL", analysis.getAnalysisType());
        assertEquals(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Finalized), analysis.getStatusId());
    }
}

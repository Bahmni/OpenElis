package us.mn.state.health.lims.upload.service;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.upload.sample.CSVSample;
import us.mn.state.health.lims.upload.sample.CSVTestResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SamplePersisterServiceTest {
    @Mock
    private SampleSourceDAO sampleSourceDAO;
    @Mock
    private SampleDAO sampleDAO;
    private SamplePersisterService samplePersisterService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        samplePersisterService = new SamplePersisterService(sampleDAO, sampleSourceDAO);

    }

    @org.junit.Test
    public void shouldPersistSampleForPersistingTestResults() throws ParseException {
        String sampleDate = "25-02-2012";
        String sysUserId = "123";
        final String sampleSource = "source";
        List<CSVTestResult> testResults = Arrays.asList(new CSVTestResult("test1", "someValueForValue1"), new CSVTestResult("test2", "someValueForTest2"));
        String accessionNumber = "123";
        CSVSample csvSample = new CSVSample("gan", "patientRegistrationNumber", accessionNumber, sampleDate, sampleSource, testResults);

        when(sampleSourceDAO.getAll()).thenReturn(Arrays.<SampleSource>asList(new SampleSource(){{this.setName(sampleSource);}}));

        samplePersisterService.save(csvSample, sysUserId);

        ArgumentCaptor<Sample> sampleCaptor = ArgumentCaptor.forClass(Sample.class);
        verify(sampleDAO).insertDataWithAccessionNumber(sampleCaptor.capture());
        Sample persistedSample = sampleCaptor.getValue();
        assertEquals(accessionNumber, persistedSample.getAccessionNumber());
        assertEquals(sysUserId, persistedSample.getSysUserId());
        assertEquals(sampleDate, new SimpleDateFormat("dd-MM-yyyy").format(persistedSample.getCollectionDate()));
        assertEquals(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.OrderStatus.Finished), persistedSample.getStatusId());
        assertEquals("H", persistedSample.getDomain());
        assertEquals(sampleSource, persistedSample.getSampleSource().getName());
    }
}

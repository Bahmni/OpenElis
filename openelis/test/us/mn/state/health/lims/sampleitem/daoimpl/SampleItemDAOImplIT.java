package us.mn.state.health.lims.sampleitem.daoimpl;

import org.bahmni.feed.openelis.IT;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static us.mn.state.health.lims.dbhelper.DBHelper.createAndSaveSampleItemWithSampleAndTypeOfSample;
import static us.mn.state.health.lims.dbhelper.DBHelper.createAndSaveTypeOfSample;
import static us.mn.state.health.lims.dbhelper.DBHelper.createSampleWithUuid;

public class SampleItemDAOImplIT extends IT {
    private SampleItemDAOImpl sampleItemDAO;
    private Sample sample;

    @Before
    public void setUp() {
        sampleItemDAO = new SampleItemDAOImpl();
        sample = createSampleWithUuid("567jhg-876ghj-sdgf56");
    }

    @Test
    public void shouldReturnFalseWhenSampleIdsAreEmpty() {
        assertFalse(sampleItemDAO.isTypeOfSampleAndSampleExists("23", Collections.<Integer>emptyList()));
    }

    @Test
    public void shouldReturnTrueWhenSampleExistsWithGivenSampleTypeIds() {
        TypeOfSample bloodSample = createAndSaveTypeOfSample("Blooood Sample Type");
        createAndSaveSampleItemWithSampleAndTypeOfSample(sample, bloodSample);

        List<Integer> typeOfSampleIds = Arrays.asList(Integer.parseInt(bloodSample.getId()), 1234);
        assertTrue(sampleItemDAO.isTypeOfSampleAndSampleExists(sample.getId(), typeOfSampleIds));
    }

    @Test
    public void shouldReturnFalseWhenSampleDoesNotExistsWithGivenSampleTypeIds() {
        TypeOfSample urineSample = createAndSaveTypeOfSample("Urinee Sample Type");
        List<Integer> typeOfSampleIds = Collections.singletonList(Integer.parseInt(urineSample.getId()));
        assertFalse(sampleItemDAO.isTypeOfSampleAndSampleExists(sample.getId(), typeOfSampleIds));
    }
}
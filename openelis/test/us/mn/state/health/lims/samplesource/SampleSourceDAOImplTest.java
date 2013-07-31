package us.mn.state.health.lims.samplesource;

import org.bahmni.feed.openelis.IT;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;

import java.util.List;

import static junit.framework.Assert.assertTrue;

public class SampleSourceDAOImplTest extends IT {

    SampleSourceDAOImpl sampleSourceDAO;

    @Before
    public void setUp() throws Exception {
        sampleSourceDAO = new SampleSourceDAOImpl();
    }

    @Test
    public void shouldReturnAllSampleSourcesOrderBySampleOrder() throws Exception {
        //Depends on the reference data that is already in the database
        List<SampleSource> allSampleSources = sampleSourceDAO.getAll();

        for(int i = 0; i < allSampleSources.size() - 1; i++) {
            Integer displayOrder = allSampleSources.get(i).getDisplayOrder();
            Integer displayOrderOfNextSampleSource = allSampleSources.get(i + 1).getDisplayOrder();

            assertTrue(displayOrder <= displayOrderOfNextSampleSource);
        }
    }

}

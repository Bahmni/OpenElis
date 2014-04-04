package us.mn.state.health.lims.sample.daoimpl;

import org.bahmni.feed.openelis.IT;
import org.bahmni.openelis.builder.TestSetup;
import org.junit.Test;
import us.mn.state.health.lims.common.provider.query.PatientSearchResults;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SearchResultsDAOImpIT extends IT{
    @Test
    public void shouldSearhByFirstName() throws Exception {
        TestSetup.createPatient("FirstName1", "MiddleName", "LastName", "BAM1000111", UUID.randomUUID().toString());
        TestSetup.createPatient("FirstName2", "MiddleName", "LastName", "BAM2000222", UUID.randomUUID().toString());

        List<PatientSearchResults> results = new SearchResultsDAOImp().getSearchResults(null, "FirstName1", null, null, null, null, null, null);

        assertEquals(1, results.size());
        assertEquals("FirstName1", results.get(0).getFirstName());
        assertEquals("MiddleName", results.get(0).getMiddleName());
        assertEquals("M", results.get(0).getGender());
        assertEquals("BAM1000111", results.get(0).getSTNumber());
    }

    @Test
    public void shouldSearhByMiddleName() throws Exception {
        TestSetup.createPatient("FirstName1", "MiddleName1", "LastName", "BAM1000111", UUID.randomUUID().toString());
        TestSetup.createPatient("FirstName2", "MiddleName1", "LastName", "BAM2000222", UUID.randomUUID().toString());
        TestSetup.createPatient("FirstName3", "MiddleName2", "LastName", "BAM2000333", UUID.randomUUID().toString());

        List<PatientSearchResults> results = new SearchResultsDAOImp().getSearchResults(null, null, "MiddleName1", null, null, null, null, null);

        assertEquals(2, results.size());
    }
}

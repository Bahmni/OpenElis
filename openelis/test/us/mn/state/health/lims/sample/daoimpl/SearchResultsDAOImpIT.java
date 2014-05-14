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
 * Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
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

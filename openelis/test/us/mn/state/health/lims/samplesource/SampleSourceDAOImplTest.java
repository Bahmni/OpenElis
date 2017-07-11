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

    @Test
    public void shouldReturnOnlyActiveSampleSources() throws Exception {
        //Depends on the reference data that is already in the database


        assertTrue(sampleSourceDAO.getAll().size() == 5);

        SampleSource inactiveSampleSource = new SampleSource();
        inactiveSampleSource.setName("Inactive Sample Source");
        inactiveSampleSource.setActive(false);
        sampleSourceDAO.add(inactiveSampleSource);

        assertTrue(sampleSourceDAO.getAll().size() == 6);
        assertTrue(sampleSourceDAO.getAllActive().size() == 5);

        List<SampleSource> allSampleSources = sampleSourceDAO.getAllActive();

        for(int i = 0; i < allSampleSources.size() - 1; i++) {
            assertTrue(allSampleSources.get(i).isActive());
        }
    }

}

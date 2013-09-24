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

package us.mn.state.health.lims.healthcenter.daoimpl;

import org.bahmni.feed.openelis.IT;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.util.List;

import static junit.framework.Assert.*;

public class HealthCenterDAOImplTest extends IT {
    HealthCenterDAO healthCenterDAO;

    @Before
    public void setUp() throws Exception {
        healthCenterDAO = new HealthCenterDAOImpl();
    }

    @Test
    public void shouldAddHealthCenter(){
        HealthCenter healthCenter = new HealthCenter("TST", "Test");
        assertNull(healthCenter.getId());

        healthCenterDAO.add(healthCenter);

        assertNotNull(healthCenter.getId());
    }

    @Test
    public void ShouldReturnAllHealthCenters() throws Exception {
        HealthCenter tst1 = new HealthCenter("TST1", "Test 1");
        HealthCenter tst2 = new HealthCenter("TST2", "Test 2");
        healthCenterDAO.add(tst1);
        healthCenterDAO.add(tst2);

        List<HealthCenter> healthCenters = healthCenterDAO.getAll();

        assertTrue(healthCenters.contains(tst1));
        assertTrue(healthCenters.contains(tst2));
    }

    @Test
    public void ShouldUpdate() throws Exception {
        HealthCenter healthCenter = new HealthCenter("TST", "Test");
        healthCenterDAO.add(healthCenter);
        healthCenter.setDescription("changed");

        healthCenterDAO.update(healthCenter);

        HealthCenter healthCenterInDB = healthCenterDAO.getByName("TST");
        assertEquals("changed", healthCenterInDB.getDescription());
    }
}

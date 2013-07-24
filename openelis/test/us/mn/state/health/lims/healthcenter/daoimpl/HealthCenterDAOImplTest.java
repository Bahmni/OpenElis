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

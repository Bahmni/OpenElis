package us.mn.state.health.lims.healthcenter.daoimpl;

import org.hibernate.Query;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.util.List;

public class HealthCenterDAOImpl implements HealthCenterDAO {
    public HealthCenter get(String id) {
        return (HealthCenter) HibernateUtil.getSession().get(HealthCenter.class, id);
    }

    @Override
    public List<HealthCenter> getAll() {
        String sql = "from HealthCenter";
        org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
        return query.list();
    }

    @Override
    public HealthCenter getByName(String name) {
        HealthCenter healthCenter = null;
        String sql = "from HealthCenter where name = :param";
        Query query = HibernateUtil.getSession().createQuery(sql);
        query.setParameter("param", name);

        List<HealthCenter> list = query.list();
        if ((list != null) && !list.isEmpty()) {
            healthCenter = list.get(0);
        }
        return healthCenter;
    }

    @Override
    public void add(HealthCenter healthCenter) {
        String id = (String) HibernateUtil.getSession().save(healthCenter);
        healthCenter.setId(id);
    }

    @Override
    public void update(HealthCenter healthCenter) {
        HealthCenter healthCenterInDb = getByName(healthCenter.getName());
        if (healthCenterInDb != null) {
            healthCenter.setId(healthCenterInDb.getId());
            HibernateUtil.getSession().merge(healthCenter);
        }
    }

    @Override
    public void deactivate(String centerName) {

    }
}

package us.mn.state.health.lims.healthcenter.daoimpl;

import org.hibernate.Query;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.util.List;

public class HealthCenterDAOImpl implements HealthCenterDAO {
    public HealthCenter get(String id) {
        return (HealthCenter) HibernateUtil.getSession().get(HealthCenter.class, id);
    }

    @Override
    public List<HealthCenter> getAll() throws LIMSRuntimeException{
        try {
            String sql = "from HealthCenter";
            Query query = HibernateUtil.getSession().createQuery(sql);
            return query.list();
        } catch (Exception e) {
            LogEvent.logErrorStack("HealthCenterDAOImpl", "getAll()", e);
            throw new LIMSRuntimeException("Error in HealthCenterDAOImpl getAll()", e);
        }
    }

    @Override
    public HealthCenter getByName(String name) throws LIMSRuntimeException {
        try {
            HealthCenter healthCenter = null;
            String sql = "from HealthCenter where name = :param";
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setParameter("param", name);

            List<HealthCenter> list = query.list();
            if ((list != null) && !list.isEmpty()) {
                healthCenter = list.get(0);
            }
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
            return healthCenter;
        } catch (Exception e) {
            LogEvent.logErrorStack("HealthCenterDAOImpl", "getByName(String name)", e);
            throw new LIMSRuntimeException("Error in HealthCenterDAOImpl getByName(String name)", e);
        }
    }

    @Override
    public void add(HealthCenter healthCenter) throws LIMSRuntimeException{
        try {
            String id = (String) HibernateUtil.getSession().save(healthCenter);
            healthCenter.setId(id);
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
        } catch (Exception e) {
            LogEvent.logErrorStack("HealthCenterDAOImpl", "add(HealthCenter healthCenter)", e);
            throw new LIMSRuntimeException("Error in HealthCenterDAOImpl add(HealthCenter healthCenter)", e);
        }
    }

    @Override
    public void update(HealthCenter healthCenter) throws LIMSRuntimeException {
        try {
            HealthCenter healthCenterInDb = getByName(healthCenter.getName());
            if (healthCenterInDb != null) {
                healthCenter.setId(healthCenterInDb.getId());
                HibernateUtil.getSession().merge(healthCenter);
            }
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
        } catch (Exception e) {
            LogEvent.logErrorStack("HealthCenterDAOImpl", "update(HealthCenter healthCenter)", e);
            throw new LIMSRuntimeException("Error in HealthCenterDAOImpl update(HealthCenter healthCenter)", e);
        }
    }

    @Override
    public void deactivate(String centerName) {

    }
}

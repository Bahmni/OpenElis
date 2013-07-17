package us.mn.state.health.lims.healthcenter.daoimpl;

import org.hibernate.Query;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.healthcenter.dao.HealthCenterDAO;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class HealthCenterDAOImpl implements HealthCenterDAO{
    @Override
    public List<HealthCenter> getAll() {
        List<HealthCenter> list = new ArrayList();
        try {
            String sql = "from HealthCenter";
            org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
            list = query.list();
            flushAndClear();
        } catch(Exception e) {
            LogEvent.logError("HealthCenterDAOImpl","getAll()",e.toString());
            throw new LIMSRuntimeException("Error in HealthCenter getAll()", e);
        }
        return list;
    }

    @Override
    public HealthCenter getByName(String name) {
        HealthCenter healthCenter = null;
        try {
            String sql = "from HealthCenter where name = :param";
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setParameter("param", name);

            List<HealthCenter> list = query.list();
            if ((list != null) && !list.isEmpty()) {
                healthCenter = list.get(0);
            }
            flushAndClear();
        } catch (Exception e) {
            throw new LIMSRuntimeException("Exception occurred in getHealthCenter", e);
        }
        return healthCenter;
    }

    @Override
    public void add(HealthCenter healthCenter) {
        try {
            String id = (String) HibernateUtil.getSession().save(healthCenter);
            healthCenter.setId(id);
            flushAndClear();
        } catch (Exception e) {
            LogEvent.logError("HealthCenterDAOImpl", "add()", e.getMessage());
            throw new LIMSRuntimeException("Error in Healthcenter add", e);
        }
    }

    @Override
    public void update(HealthCenter healthCenter) {
        HealthCenter healthCenterInDb = getByName(healthCenter.getName());
        if(healthCenterInDb != null){
            healthCenter.setId(healthCenterInDb.getId());
            HibernateUtil.getSession().merge(healthCenter);
            flushAndClear();
        }
    }

    @Override
    public void deactivate(String centerName){

    }

    private void flushAndClear() {
        HibernateUtil.getSession().flush();
        HibernateUtil.getSession().clear();
    }
}

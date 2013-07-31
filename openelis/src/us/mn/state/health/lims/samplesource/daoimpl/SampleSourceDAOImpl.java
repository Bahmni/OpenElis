package us.mn.state.health.lims.samplesource.daoimpl;

import org.hibernate.Query;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;

import java.util.List;

public class SampleSourceDAOImpl implements SampleSourceDAO{

    @Override
    public List<SampleSource> getAll() {
        List<SampleSource> list;
        try {
            String sql = "from SampleSource order by display_order asc";
            org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
            list = query.list();
            flushAndClear();
        } catch(Exception e) {
            LogEvent.logErrorStack("SampleSourceDAOImpl", "getAll()", e);
            throw new LIMSRuntimeException("Error in SampleSource getAll()", e);
        }
        return list;
    }

    @Override
    public SampleSource getByName(String name) {
        SampleSource sampleSource = null;
        try {
            String sql = "from SampleSource where name = :param";
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setParameter("param", name);

            List<SampleSource> list = query.list();
            if ((list != null) && !list.isEmpty()) {
                sampleSource = list.get(0);
            }
            flushAndClear();
        } catch (Exception e) {
            throw new LIMSRuntimeException("Exception occurred in getHealthCenter", e);
        }
        return sampleSource;
    }

    @Override
    public SampleSource get(String id) {
        return (SampleSource) HibernateUtil.getSession().get(SampleSource.class, id);
    }

    @Override
    public void add(SampleSource sampleSource) {
        try {
            String id = (String) HibernateUtil.getSession().save(sampleSource);
            sampleSource.setId(id);
            flushAndClear();
        } catch (Exception e) {
            LogEvent.logError("SampleSourceDAOImpl", "add()", e.getMessage());
            throw new LIMSRuntimeException("Error in SampleSource add", e);
        }
    }

    @Override
    public void update(SampleSource sampleSource) {
        SampleSource sampleSourceInDB = getByName(sampleSource.getName());
        if(sampleSourceInDB != null){
            sampleSource.setId(sampleSourceInDB.getId());
            HibernateUtil.getSession().merge(sampleSource);
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

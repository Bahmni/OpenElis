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

package us.mn.state.health.lims.samplesource.daoimpl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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

    public List<SampleSource> getAllActive() {
        List<SampleSource> list;
        try {
            String sql = "from SampleSource where active = true order by display_order asc";
            org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
            list = query.list();
            flushAndClear();
        } catch(Exception e) {
            LogEvent.logErrorStack("SampleSourceDAOImpl", "getAllActive()", e);
            throw new LIMSRuntimeException("Error in SampleSource getAllActive()", e);
        }
        return list;
    }



    @Override
    public SampleSource getByName(String name, boolean caseInsensitiveComparision) {
        SampleSource sampleSource = null;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(SampleSource.class);
            if (caseInsensitiveComparision) {
                criteria.add(Restrictions.eq("name", name).ignoreCase());
            } else {
                criteria.add(Restrictions.eq("name", name));
            }
            List<SampleSource> list = criteria.list();
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
        SampleSource sampleSourceInDB = getByName(sampleSource.getName(), false);
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

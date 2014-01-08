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

package org.bahmni.feed.openelis.externalreference.daoimpl;


import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.util.List;

public class ExternalReferenceDaoImpl extends BaseDAOImpl implements ExternalReferenceDao {
    @Override
    public boolean insertData(ExternalReference externalReference) throws LIMSRuntimeException {
        try {
            String id = (String) HibernateUtil.getSession().save(externalReference);
            externalReference.setId(id);
            HibernateUtil.getSession().flush();
        } catch (Exception e) {
            LogEvent.logError("ExternalReferenceDaoImpl", "insertData()", e.toString());
            throw new LIMSRuntimeException("Error in ExternalReference insertData()", e);
        }

        return true;
    }

    @Override
    public ExternalReference getData(String externalReferenceId, String type) throws LIMSRuntimeException {
        String sql = "from ExternalReference e where e.externalId =:param1 and e.type=:param2 ";
        org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
        query.setParameter("param1", externalReferenceId);
        query.setParameter("param2", type);

        List refs = query.list();
        if (!refs.isEmpty())
            return (ExternalReference) refs.get(0);
        return null;
    }

    @Override
    public ExternalReference getDataByItemId(String itemId, String category) throws LIMSRuntimeException {
        String sql = "from ExternalReference e where e.itemId =:param1 and e.type=:param2 ";
        org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
        query.setParameter("param1", Integer.parseInt(itemId));
        query.setParameter("param2", category);

        List refs = query.list();
        if (!refs.isEmpty())
            return (ExternalReference) refs.get(0);
        return null;
    }

    @Override
    public void deleteData(ExternalReference data) throws LIMSRuntimeException {
        data = getData(data.getExternalId(), data.getType());
        if (data != null) {
            HibernateUtil.getSession().delete(data);
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
        }
    }

}

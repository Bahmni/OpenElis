package org.bahmni.feed.openelis.externalreference.daoimpl;


import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.hibernate.HibernateException;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.patient.valueholder.Patient;

import java.util.List;

public class ExternalReferenceDaoImpl extends BaseDAOImpl implements ExternalReferenceDao {

    @Override
    public boolean insertData(ExternalReference externalReference) throws LIMSRuntimeException {

        try {
            String id = (String) HibernateUtil.getSession().save(externalReference);
            externalReference.setId(id);

            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
        } catch (Exception e) {
            LogEvent.logError("ExternalReferenceDaoImpl", "insertData()", e.toString());
            throw new LIMSRuntimeException("Error in ExternalReference insertData()", e);
        }

        return true;
    }



    @Override
    public ExternalReference getData(String externalReferenceId) throws LIMSRuntimeException {
        try{
            ExternalReference reference = (ExternalReference) HibernateUtil.getSession().get(	ExternalReference.class, externalReferenceId );
            closeSession();
            return reference;
        }catch(HibernateException e){
            handleException(e, "getData(externalReferenceId)");
        }

        return null;
    }

    public void deleteData(ExternalReference data) throws LIMSRuntimeException {
                data = (ExternalReference)getData(data.getId());
                HibernateUtil.getSession().delete(data);
                HibernateUtil.getSession().flush();
                HibernateUtil.getSession().clear();
            }

 }

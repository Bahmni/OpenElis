package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.bahmni.feed.openelis.feed.service.LabService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.io.IOException;

public abstract class TransactionalService implements LabService {

    public void save(LabObject labObject) {
        Transaction tx= null;
        Session session = HibernateUtil.getSession();
        try {
            tx = session.beginTransaction();
            saveLabObject(labObject);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            LogEvent.logError(" Event", "save", e.toString());
            e.printStackTrace();
            throw new LIMSRuntimeException("Error in LabService event save", e);

        }finally {
            session.flush();
            session.clear();
        }

    }

    protected abstract void saveLabObject(LabObject labObject) throws IOException;


}

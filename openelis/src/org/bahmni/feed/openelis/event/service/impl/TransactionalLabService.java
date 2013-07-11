package org.bahmni.feed.openelis.event.service.impl;

import org.bahmni.feed.openelis.event.object.LabObject;
import org.bahmni.feed.openelis.event.service.LabService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.io.IOException;

public abstract class TransactionalLabService implements LabService {

    public void save(LabObject labObject) {
        Transaction tx= null;
        Session session = HibernateUtil.getSession();
        try {
            tx = session.beginTransaction();
            saveEvent(labObject);
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

    protected abstract void saveEvent(LabObject labObject) throws IOException;


}

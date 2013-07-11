package org.bahmni.feed.openelis.event.objects.impl;

import org.bahmni.feed.openelis.event.objects.EventObject;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.io.IOException;

public abstract class TransactionalEventObject implements EventObject{

    public void save(Event event) {
        Transaction tx= null;
        Session session = HibernateUtil.getSession();
        try {
            tx = session.beginTransaction();
            saveEvent(event);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            LogEvent.logError(" Event", "save", e.toString());
            e.printStackTrace();
            throw new LIMSRuntimeException("Error in LabPanelService event save", e);

        }finally {
            session.flush();
            session.clear();
        }

    }

    protected abstract void saveEvent(Event event) throws IOException;


}

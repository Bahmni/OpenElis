package org.bahmni.feed.openelis.feed.event;

import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;

public abstract class OpenElisEventWorker implements EventWorker {
    @Override
    public void cleanUp(Event event) {
        ElisHibernateSession session = (ElisHibernateSession) HibernateUtil.getSession();
        session.clearSession();
    }
}
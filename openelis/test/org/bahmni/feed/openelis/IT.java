package org.bahmni.feed.openelis;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;

@Ignore
public abstract class IT {
    private Transaction transaction;
    private Session session;

    @Before
    public void before() {
        session = HibernateUtil.getSession();
        transaction = session.beginTransaction();
    }

    @After
    public void after() {
        try {
            session.flush();
            ((ElisHibernateSession) session).clearSession();
        } finally {
            transaction.rollback();
        }
    }
}
package org.bahmni.feed.openelis.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class OpenElisConnectionProvider implements JdbcConnectionProvider {
    @Override
    public Connection getConnection() throws SQLException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.getTransaction();
        if (transaction == null || !transaction.isActive()) {
            session.beginTransaction();
        }
        Connection connection = session.connection();
        return connection;
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        HibernateUtil.closeSession();
    }
}

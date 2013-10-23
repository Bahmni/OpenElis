package org.bahmni.feed.openelis.utils;

import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class OpenElisConnectionProvider implements JdbcConnectionProvider {
    @Override
    public Connection getConnection() throws SQLException {
        return HibernateUtil.getSession().connection();
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        HibernateUtil.closeSession();
    }
}

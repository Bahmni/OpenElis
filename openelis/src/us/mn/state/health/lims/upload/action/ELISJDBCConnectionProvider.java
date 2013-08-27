package us.mn.state.health.lims.upload.action;

import org.bahmni.fileimport.dao.JDBCConnectionProvider;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.sql.Connection;

public class ELISJDBCConnectionProvider implements JDBCConnectionProvider {
    @Override
    public Connection getConnection() {
        return HibernateUtil.getSession().connection();
    }
}

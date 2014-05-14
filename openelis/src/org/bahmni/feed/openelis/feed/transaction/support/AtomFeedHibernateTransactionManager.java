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
 * Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package org.bahmni.feed.openelis.feed.transaction.support;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.transaction.AFTransactionManager;
import org.ict4h.atomfeed.transaction.AFTransactionWork;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.sql.Connection;
import java.sql.SQLException;


public class AtomFeedHibernateTransactionManager implements AFTransactionManager, JdbcConnectionProvider {

    private enum TxStatus {ONGOING, NEW}
    
    @Override
    public <T> T executeWithTransaction(AFTransactionWork<T> action) throws RuntimeException {
        TxStatus transactionStatus = null;
        try {
            transactionStatus = getTransactionStatus();
            if (transactionStatus.equals(TxStatus.NEW)) {
                startTransaction();
            }
            T result = action.execute();
            if (transactionStatus.equals(TxStatus.NEW)) {
                commit();
            }
            return result;
        } catch (Exception e) {
            if ((transactionStatus != null) && (transactionStatus.equals(TxStatus.NEW))) { 
                rollback();
            }
            throw new RuntimeException(e);
        }
    }

    private TxStatus getTransactionStatus() {
        Transaction transaction = getCurrentSession().getTransaction();
        if (transaction != null) {
            if (transaction.isActive()) {
                return TxStatus.ONGOING;
            }
        }
        return TxStatus.NEW;
    }

    @Override
    public Connection getConnection() throws SQLException {
        //TODO: ensure that only connection associated with current thread current transaction is given
        return getCurrentSession().connection();
    }

    public void startTransaction() {
        Transaction transaction = getCurrentSession().getTransaction();
        if (transaction == null || !transaction.isActive()) {
            getCurrentSession().beginTransaction();
        }
    }

    public void commit() {
        Transaction transaction = getCurrentSession().getTransaction();
        if (!transaction.wasCommitted()) {
            transaction.commit();
        }
    }

    public void rollback() {
        Transaction transaction = getCurrentSession().getTransaction();
        if (!transaction.wasRolledBack()) {
            transaction.rollback();
        }
    }

    private Session getCurrentSession() {
        return HibernateUtil.getSession();
    }

}

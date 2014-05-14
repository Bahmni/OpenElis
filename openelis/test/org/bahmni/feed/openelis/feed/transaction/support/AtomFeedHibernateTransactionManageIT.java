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

import org.ict4h.atomfeed.transaction.AFTransactionWorkWithoutResult;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicInteger;

@Ignore
public class AtomFeedHibernateTransactionManageIT {

    @Test
    public void shouldNestTransactionWithHibernate() {
        final AtomFeedHibernateTransactionManager txMgr = new AtomFeedHibernateTransactionManager();
        try {
            txMgr.executeWithTransaction(new AFTransactionWorkWithoutResult() {
                public void doInTransaction() {
                    try {
                        Connection outerCon1 = txMgr.getConnection();
                        System.out.println("outer connection outer 1st time:" + outerCon1);
                    } catch (Exception e1) {
                        System.out.println("connection fetch outer 1st time :" + e1);
                    }

                    for (int i=1; i <= 2; i++) {
                        System.out.println("********** Exec counter "+i);
                        final AtomicInteger loopCounter = new AtomicInteger(i);
                        try {
                            txMgr.executeWithTransaction(new AFTransactionWorkWithoutResult() {
                                public void doInTransaction() {
                                    try {
                                        if (loopCounter.get() == 2) {
                                            throw new Exception("Throw exception for 2nd iteration");
                                        }
                                        Connection innerCon = txMgr.getConnection();
                                        System.out.println("inner connection:"  + innerCon);
                                    } catch(Exception e2) {
                                        System.out.println("connection fetch inner :" + e2);
                                    }
                                }

                                @Override
                                public PropagationDefinition getTxPropagationDefinition() {
                                    return PropagationDefinition.PROPAGATION_REQUIRES_NEW;
                                }
                            });
                        } catch (Exception innerTxEx) {
                            System.out.println("********** Exec counter "+ i);
                            System.out.println("inner Tx :" + innerTxEx);
                        }
                    }

                    try {
                        Connection outerCon2 = txMgr.getConnection();
                        System.out.println("outer connection outer 2nd time:" + outerCon2);
                    } catch (Exception e3) {
                        System.out.println("connection fetch outer 2nd time :" + e3);
                    }
                }

                @Override
                public PropagationDefinition getTxPropagationDefinition() {
                    return PropagationDefinition.PROPAGATION_REQUIRES_NEW;
                }
            });
        } catch (Exception outerEx) {
            System.out.println("Outer Exception:" + outerEx);
        }
    }

}

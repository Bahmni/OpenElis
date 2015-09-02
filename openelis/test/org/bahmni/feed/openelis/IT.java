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
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

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
    protected Transaction transaction;
    protected Session session;

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

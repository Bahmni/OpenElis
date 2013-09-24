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

package org.bahmni.feed.openelis.externalreference.daoimpl;

import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import static junit.framework.Assert.assertNull;

public class ExternalReferenceDaoImplIT {

    ExternalReferenceDaoImpl externalReferenceDao = new ExternalReferenceDaoImpl();


    @Test
    public void testInsertData() throws Exception {
        String externalReferenceId = "1123456";
        String panel = "Panel";
        ExternalReference reference = externalReferenceDao.getData(externalReferenceId, panel);

        Transaction transaction = null;
        if(reference != null){
            transaction = HibernateUtil.getSession().beginTransaction();
            externalReferenceDao.deleteData(reference);
            transaction.commit();
        }

        reference = new ExternalReference();
        reference.setExternalId(externalReferenceId);
        reference.setItemId(1123457);
        reference.setType(panel);
        transaction = HibernateUtil.getSession().beginTransaction();

        externalReferenceDao.insertData(reference);

        reference = externalReferenceDao.getData(externalReferenceId, panel);
        Assert.assertNotNull(reference);

        externalReferenceDao.deleteData(reference);

        assertNull(externalReferenceDao.getData(externalReferenceId,panel));

        transaction.commit();
    }

    @After
    public void tearDown() throws Exception {
        HibernateUtil.getSession().close();
    }

}

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
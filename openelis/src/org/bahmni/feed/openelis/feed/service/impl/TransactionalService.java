package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.feed.service.LabService;

public abstract class TransactionalService implements LabService {

//    public void save(LabObject labObject) {
//        Transaction tx= null;
//        Session session = HibernateUtil.getSession();
//        try {
////            tx = session.beginTransaction();
//            saveLabObject(labObject);
////            tx.commit();
//        } catch (Exception e) {
//            tx.rollback();
//            LogEvent.logError(" Event", "save", e.toString());
//            e.printStackTrace();
//            throw new LIMSRuntimeException("Error in LabService event save", e);
//
//        }finally {
////            session.flush();
////            session.clear();
//        }
//
//    }

//    protected abstract void saveLabObject(LabObject labObject) throws IOException;


}

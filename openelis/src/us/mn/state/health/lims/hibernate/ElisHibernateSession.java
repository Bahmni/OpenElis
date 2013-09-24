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

package us.mn.state.health.lims.hibernate;

import org.hibernate.*;
import org.hibernate.stat.SessionStatistics;

import java.io.Serializable;
import java.sql.Connection;

public class ElisHibernateSession implements Session {
    private Session session;

    public ElisHibernateSession(Session session) {
        this.session = session;
    }

    @Override
    public void reconnect(Connection connection) throws HibernateException {
        session.reconnect(connection);
    }

    @Override
    public EntityMode getEntityMode() {
        return session.getEntityMode();
    }

    @Override
    public Session getSession(EntityMode entityMode) {
        return session.getSession(entityMode);
    }

    @Override
    public void flush() throws HibernateException {
        session.flush();
    }

    @Override
    public void setFlushMode(FlushMode flushMode) {
        session.setFlushMode(flushMode);
    }

    @Override
    public FlushMode getFlushMode() {
        return session.getFlushMode();
    }

    @Override
    public void setCacheMode(CacheMode cacheMode) {
        session.setCacheMode(cacheMode);
    }

    @Override
    public CacheMode getCacheMode() {
        return session.getCacheMode();
    }

    @Override
    public SessionFactory getSessionFactory() {
        return session.getSessionFactory();
    }

    @Override
    public Connection connection() throws HibernateException {
        return session.connection();
    }

    @Override
    public Connection close() throws HibernateException {
        return session.close();
    }

    @Override
    public void cancelQuery() throws HibernateException {
        session.cancelQuery();
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
    }

    @Override
    public boolean isConnected() {
        return session.isConnected();
    }

    @Override
    public boolean isDirty() throws HibernateException {
        return session.isDirty();
    }

    @Override
    public Serializable getIdentifier(Object o) throws HibernateException {
        return session.getIdentifier(o);
    }

    @Override
    public boolean contains(Object o) {
        return session.contains(o);
    }

    @Override
    public void evict(Object o) throws HibernateException {
        session.evict(o);
    }

    @Override
    public Object load(Class aClass, Serializable serializable, LockMode lockMode) throws HibernateException {
        return session.load(aClass, serializable, lockMode);
    }

    @Override
    public Object load(String s, Serializable serializable, LockMode lockMode) throws HibernateException {
        return session.load(s, serializable, lockMode);
    }

    @Override
    public Object load(Class aClass, Serializable serializable) throws HibernateException {
        return session.load(aClass, serializable);
    }

    @Override
    public Object load(String s, Serializable serializable) throws HibernateException {
        return session.load(s, serializable);
    }

    @Override
    public void load(Object o, Serializable serializable) throws HibernateException {
        session.load(o, serializable);
    }

    @Override
    public void replicate(Object o, ReplicationMode replicationMode) throws HibernateException {
        session.replicate(o, replicationMode);
    }

    @Override
    public void replicate(String s, Object o, ReplicationMode replicationMode) throws HibernateException {
        session.replicate(s, o, replicationMode);
    }

    @Override
    public Serializable save(Object o) throws HibernateException {
        return session.save(o);
    }

    @Override
    public Serializable save(String s, Object o) throws HibernateException {
        return session.save(s, o);
    }

    @Override
    public void saveOrUpdate(Object o) throws HibernateException {
        session.saveOrUpdate(o);
    }

    @Override
    public void saveOrUpdate(String s, Object o) throws HibernateException {
        session.saveOrUpdate(s, o);
    }

    @Override
    public void update(Object o) throws HibernateException {
        session.update(o);
    }

    @Override
    public void update(String s, Object o) throws HibernateException {
        session.update(s, o);
    }

    @Override
    public Object merge(Object o) throws HibernateException {
        return session.merge(o);
    }

    @Override
    public Object merge(String s, Object o) throws HibernateException {
        return session.merge(s, o);
    }

    @Override
    public void persist(Object o) throws HibernateException {
        session.persist(o);
    }

    @Override
    public void persist(String s, Object o) throws HibernateException {
        session.persist(s, o);
    }

    @Override
    public void delete(Object o) throws HibernateException {
        session.delete(o);
    }

    @Override
    public void delete(String s, Object o) throws HibernateException {
        session.delete(s, o);
    }

    @Override
    public void lock(Object o, LockMode lockMode) throws HibernateException {
        session.lock(o, lockMode);
    }

    @Override
    public void lock(String s, Object o, LockMode lockMode) throws HibernateException {
        session.lock(s, o, lockMode);
    }

    @Override
    public void refresh(Object o) throws HibernateException {
        session.refresh(o);
    }

    @Override
    public void refresh(Object o, LockMode lockMode) throws HibernateException {
        session.refresh(o, lockMode);
    }

    @Override
    public LockMode getCurrentLockMode(Object o) throws HibernateException {
        return session.getCurrentLockMode(o);
    }

    @Override
    public Transaction beginTransaction() throws HibernateException {
        return session.beginTransaction();
    }

    @Override
    public Transaction getTransaction() {
        return session.getTransaction();
    }

    @Override
    public Criteria createCriteria(Class aClass) {
        return session.createCriteria(aClass);
    }

    @Override
    public Criteria createCriteria(Class aClass, String s) {
        return session.createCriteria(aClass, s);
    }

    @Override
    public Criteria createCriteria(String s) {
        return session.createCriteria(s);
    }

    @Override
    public Criteria createCriteria(String s, String s2) {
        return session.createCriteria(s, s2);
    }

    @Override
    public Query createQuery(String s) throws HibernateException {
        return session.createQuery(s);
    }

    @Override
    public SQLQuery createSQLQuery(String s) throws HibernateException {
        return session.createSQLQuery(s);
    }

    @Override
    public Query createFilter(Object o, String s) throws HibernateException {
        return session.createFilter(o, s);
    }

    @Override
    public Query getNamedQuery(String s) throws HibernateException {
        return session.getNamedQuery(s);
    }

    @Override
    public void clear() {
    }

    @Override
    public Object get(Class aClass, Serializable serializable) throws HibernateException {
        return session.get(aClass, serializable);
    }

    @Override
    public Object get(Class aClass, Serializable serializable, LockMode lockMode) throws HibernateException {
        return session.get(aClass, serializable, lockMode);
    }

    @Override
    public Object get(String s, Serializable serializable) throws HibernateException {
        return session.get(s, serializable);
    }

    @Override
    public Object get(String s, Serializable serializable, LockMode lockMode) throws HibernateException {
        return session.get(s, serializable, lockMode);
    }

    @Override
    public String getEntityName(Object o) throws HibernateException {
        return session.getEntityName(o);
    }

    @Override
    public Filter enableFilter(String s) {
        return session.enableFilter(s);
    }

    @Override
    public Filter getEnabledFilter(String s) {
        return session.getEnabledFilter(s);
    }

    @Override
    public void disableFilter(String s) {
        session.disableFilter(s);
    }

    @Override
    public SessionStatistics getStatistics() {
        return session.getStatistics();
    }

    @Override
    public void setReadOnly(Object o, boolean b) {
        session.setReadOnly(o, b);
    }

    @Override
    public Connection disconnect() throws HibernateException {
        return session.disconnect();
    }

    @Override
    public void reconnect() throws HibernateException {
        session.reconnect();
    }

    public void clearSession() {
        session.clear();
    }
}

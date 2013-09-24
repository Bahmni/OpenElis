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
package us.mn.state.health.lims.address.daoimpl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import us.mn.state.health.lims.address.dao.PersonAddressDAO;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.person.valueholder.Person;

import java.util.List;

public class PersonAddressDAOImpl extends BaseDAOImpl implements PersonAddressDAO {
    private static AuditTrailDAO auditDAO = new AuditTrailDAOImpl();

    @SuppressWarnings("unchecked")
    @Override
    public List<PersonAddress> getAddressPartsByPersonId(String personId) throws LIMSRuntimeException {
        String sql = "from PersonAddress pa where pa.compoundId.targetId = :personId";

        try {
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setInteger("personId", Integer.parseInt(personId));
            List<PersonAddress> addressPartList = query.list();
            closeSession();
            return addressPartList;
        } catch (HibernateException e) {
            handleException(e, "getAddressPartsByPersonId");
        }

        return null;
    }

    @Override
    public void insert(PersonAddress personAddress) throws LIMSRuntimeException {
        try {
            save(personAddress);
            closeSession();
        } catch (HibernateException e) {
            handleException(e, "insert");
        }
    }

    private void save(PersonAddress personAddress) {
        HibernateUtil.getSession().save(personAddress);
        auditDAO.saveNewHistory(personAddress, personAddress.getSysUserId(), "person_address");
    }

    @Override
    public void insert(List<PersonAddress> personAddresses) throws LIMSRuntimeException {
        for (PersonAddress personAddress : personAddresses) {
            save(personAddress);
        }
        HibernateUtil.getSession().flush();
    }

    @Override
    public void update(PersonAddress personAddress) throws LIMSRuntimeException {

        PersonAddress oldData = readPersonAddress(personAddress);

        try {
            auditDAO.saveHistory(personAddress, oldData, personAddress.getSysUserId(), IActionConstants.AUDIT_TRAIL_UPDATE, "person_address");

            HibernateUtil.getSession().merge(personAddress);
            closeSession();
            HibernateUtil.getSession().evict(personAddress);
            HibernateUtil.getSession().refresh(personAddress);
        } catch (HibernateException e) {
            handleException(e, "update");
        }
    }

    public PersonAddress readPersonAddress(PersonAddress personAddress) {
        try {
            PersonAddress oldPersonAddress = (PersonAddress) HibernateUtil.getSession().get(PersonAddress.class, personAddress.getCompoundId());
            closeSession();

            return oldPersonAddress;
        } catch (HibernateException e) {
            handleException(e, "readPersonAddress");
        }

        return null;
    }

    @Override
    public PersonAddress getByPersonIdAndPartId(String personId, String addressPartId) throws LIMSRuntimeException {
        String sql = "from PersonAddress pa where pa.compoundId.targetId = :personId and pa.compoundId.addressPartId = :partId";

        try {
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setInteger("personId", Integer.parseInt(personId));
            query.setInteger("partId", Integer.parseInt(addressPartId));
            PersonAddress addressPart = (PersonAddress) query.uniqueResult();
            closeSession();
            return addressPart;
        } catch (HibernateException e) {
            handleException(e, "getByPersonIdAndPartId");
        }

        return null;
    }

    @Override
    public void deleteAddressOfPerson(Person person) {
        try {
            List<PersonAddress> addressParts = getAddressPartsByPersonId(person.getId());
            Session session = HibernateUtil.getSession();
            for (PersonAddress personAddress : addressParts) {
                session.delete(personAddress);
            }
        } catch (HibernateException e) {
            handleException(e, "getByPersonIdAndPartId");
        }
    }
}

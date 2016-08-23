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
package us.mn.state.health.lims.referral.daoimpl;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.patientidentitytype.util.PatientIdentityTypeMap;
import us.mn.state.health.lims.referral.dao.ReferralDAO;
import us.mn.state.health.lims.referral.valueholder.Referral;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/*
 */
public class ReferralDAOImpl extends BaseDAOImpl implements ReferralDAO {

    private AuditTrailDAO auditDAO = new AuditTrailDAOImpl();

    public boolean insertData(Referral referral) throws LIMSRuntimeException {
        try {
            String id = (String) HibernateUtil.getSession().save(referral);
            referral.setId(id);

            auditDAO.saveNewHistory(referral, referral.getSysUserId(), "referral");
            closeSession();
        } catch (HibernateException e) {
            handleException(e, "insertData");
        }

        return true;
    }

    public Referral getReferralById(String referralId) throws LIMSRuntimeException {
        try {
            Referral referral = (Referral) HibernateUtil.getSession().get(Referral.class, referralId);
            closeSession();
            return referral;
        } catch (HibernateException e) {
            handleException(e, "getReferralById");
        }

        return null;
    }

    public Referral getReferralByAnalysisId(String analysisId) throws LIMSRuntimeException {

        if (!GenericValidator.isBlankOrNull(analysisId)) {
            String sql = "From Referral r where r.analysis.id = :analysisId";

            try {
                Query query = HibernateUtil.getSession().createQuery(sql);
                query.setInteger("analysisId", Integer.parseInt(analysisId));
                Referral referral = (Referral) query.uniqueResult();
                closeSession();
                return referral;
            } catch (HibernateException e) {
                handleException(e, "getReferralByAnalysisId");
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Referral> getAllUncanceledOpenReferrals(int resultPageSize,int resultPageNumber) throws LIMSRuntimeException {
        String sql = "select r From Referral r, Analysis a, Test t, SampleItem si, Sample s where r.analysis.id = a.id and a.test.id = t.id " +
                "and a.sampleItem.id = si.id and si.sample.id = s.id " +
                "and a.statusId in (" + StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.ReferedOut) + "," + StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.BiologistRejectedRO) + ") " +
                "and r.canceled = 'false' " +
                "order by date(r.requestDate) DESC, s.accessionNumber DESC , t.sortOrder ASC";

        try {
            Query query = HibernateUtil.getSession().createQuery(sql).setMaxResults(resultPageSize).setFirstResult(resultPageSize*resultPageNumber);
            List<Referral> referrals = query.list();
            closeSession();
            return referrals;
        } catch (HibernateException e) {
            handleException(e, "getAllUncanceledOpenReferrals");
        }
        return null;
    }

    @Override
    public long getAllUncanceledOpenReferralsCount() throws LIMSRuntimeException {
        String sql = "select count(*) From Referral r where" +
                " r.analysis.statusId in (" + StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.ReferedOut) + "," + StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.BiologistRejectedRO) + ")" +
                " and r.canceled = 'false'";
        try {
            Long resultCount = (Long) HibernateUtil.getSession().createQuery(sql).uniqueResult();
            closeSession();
            return resultCount;
        } catch (HibernateException e) {
            handleException(e, "getAllUncanceledOpenReferralsCount");
        }
        return -1;
    }

    public List<Referral> getAllUncanceledOpenReferralsByPatientSTNumber(String patientSTNumber) throws LIMSRuntimeException {
        String sql = "select r From Referral r, SampleHuman sh, PatientIdentity pi, Test t, SampleItem si, Sample s"+
                " where r.analysis.sampleItem.sample.id = sh.sampleId and r.analysis.test.id = t.id and r.analysis.sampleItem.id = si.id and si.sample.id = s.id" +
                " and r.analysis.statusId in (" + StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.ReferedOut) + "," + StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.BiologistRejectedRO) + ")" +
                " and sh.patientId = pi.patientId and pi.identityTypeId = "+ PatientIdentityTypeMap.getInstance().getIDForType("ST") +
                " and pi.identityData = :patientSTNumber " +
                " and r.canceled = 'false' " +
                " order by date(r.requestDate) desc, s.accessionNumber DESC , t.sortOrder ASC";
        try {
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setString("patientSTNumber", patientSTNumber);
            List<Referral> referrals = query.list();
            closeSession();
            return referrals;
        } catch (HibernateException e) {
            handleException(e, "getAllUncanceledOpenReferralsByPatientSTNumber");
        }
        return null;
    }

    private Referral readResult(String referralId) {
        try {
            Referral referral = (Referral) HibernateUtil.getSession().get(Referral.class, referralId);
            closeSession();
            return referral;
        } catch (HibernateException e) {
            handleException(e, "readResult");
        }

        return null;
    }

    public void updateData(Referral referral) throws LIMSRuntimeException {
        Referral oldData = readResult(referral.getId());

        try {
            auditDAO.saveHistory(referral, oldData, referral.getSysUserId(), IActionConstants.AUDIT_TRAIL_UPDATE, "referral");
        } catch (HibernateException e) {
            handleException(e, "updateData");
        }

        try {
            HibernateUtil.getSession().merge(referral);
            HibernateUtil.getSession().flush();
            HibernateUtil.getSession().clear();
            HibernateUtil.getSession().evict(referral);
            HibernateUtil.getSession().refresh(referral);
        } catch (HibernateException e) {
            handleException(e, "updateData");
        }

    }

    @SuppressWarnings("unchecked")
    public List<Referral> getAllReferralsBySampleId(String id) throws LIMSRuntimeException {
        if (!GenericValidator.isBlankOrNull(id)) {
            String sql = "FROM Referral r WHERE r.analysis.sampleItem.sample.id = :sampleId order by r.requestDate desc";

            try {
                Query query = HibernateUtil.getSession().createQuery(sql);
                query.setInteger("sampleId", Integer.parseInt(id));
                List<Referral> referralList = query.list();
                closeSession();
                return referralList;

            } catch (HibernateException e) {
                handleException(e, "getAllReferralsBySampleId");
            }

        }

        return new ArrayList<>();
    }

    /**
     * @see us.mn.state.health.lims.referral.dao.ReferralDAO#getAllReferralsByOrganization(java.lang.String, java.sql.Date, java.sql.Date)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Referral> getAllReferralsByOrganization(String organizationId, Date lowDate, Date highDate) {
        String sql = "FROM Referral r WHERE r.organization.id = :organizationId AND r.requestDate >= :lowDate AND r.requestDate <= :highDate order by r.requestDate desc";

        try {
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setInteger("organizationId", Integer.parseInt(organizationId));
            query.setDate("lowDate", lowDate);
            query.setDate("highDate", highDate);
            List<Referral> referralList = query.list();
            closeSession();
            return referralList;
        } catch (HibernateException e) {
            handleException(e, "getAllReferralsByOrganization");
        }
        return new ArrayList<>();
    }

}

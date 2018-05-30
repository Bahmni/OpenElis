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
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.analysis.daoimpl;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.dto.PatientAnalysis;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author diane benz
 */
public class AnalysisDAOImpl extends BaseDAOImpl implements AnalysisDAO {

	@SuppressWarnings("rawtypes")
	public void deleteData(List analyses) throws LIMSRuntimeException {
		// add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			for (int i = 0; i < analyses.size(); i++) {
				Analysis data = (Analysis) analyses.get(i);

				Analysis oldData = (Analysis) readAnalysis(data.getId());
				Analysis newData = new Analysis();

				String sysUserId = data.getSysUserId();
				String event = IActionConstants.AUDIT_TRAIL_DELETE;
				String tableName = "ANALYSIS";
				auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
			}
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "AuditTrail deleteData()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis AuditTrail deleteData()", e);
		}

		try {
			for (int i = 0; i < analyses.size(); i++) {
				Analysis data = (Analysis) analyses.get(i);
				// bugzilla 2206
				data = (Analysis) readAnalysis(data.getId());
				HibernateUtil.getSession().delete(data);
				HibernateUtil.getSession().flush();
				HibernateUtil.getSession().clear();
			}
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "deleteData()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis deleteData()", e);
		}
	}

	/*
	 * Warning: duplicateCheck uses SystemConfiguration setting for excluding
	 * status (non-Javadoc)
	 * 
	 * @see
	 * us.mn.state.health.lims.analysis.dao.AnalysisDAO#insertData(us.mn.state
	 * .health.lims.analysis.valueholder.Analysis, boolean)
	 */
	public boolean insertData(Analysis analysis, boolean duplicateCheck) throws LIMSRuntimeException {

		try {

			if (duplicateCheck) {
				if (duplicateAnalysisExists(analysis)) {
					throw new LIMSDuplicateRecordException("Duplicate record exists for this sample and test "
							+ analysis.getTest().getTestDisplayValue());
				}
			}
			String id = (String) HibernateUtil.getSession().save(analysis);
			analysis.setId(id);

			// bugzilla 1824 inserts will be logged in history table
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = analysis.getSysUserId();
			String tableName = "ANALYSIS";
			auditDAO.saveNewHistory(analysis, sysUserId, tableName);

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (Exception e) {
			LogEvent.logErrorStack("AnalysisDAOImpl", "insertData()", e);
			throw new LIMSRuntimeException("Error in Analysis insertData()", e);
		}

		return true;
	}

	public void updateData(Analysis analysis) throws LIMSRuntimeException {
		Analysis oldData = (Analysis) readAnalysis(analysis.getId());
		Analysis newData = analysis;

		// add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = analysis.getSysUserId();
			String event = IActionConstants.AUDIT_TRAIL_UPDATE;
			String tableName = "ANALYSIS";
			auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
		} catch (Exception e) {
			LogEvent.logError("AnalysisDAOImpl", "AuditTrail updateData()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis AuditTrail updateData()", e);
		}

		try {
			HibernateUtil.getSession().merge(analysis);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().evict(analysis);
			HibernateUtil.getSession().refresh(analysis);
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "updateData()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis updateData()", e);
		}
	}

	public void getData(Analysis analysis) throws LIMSRuntimeException {

		try {
			Analysis analysisClone = (Analysis) HibernateUtil.getSession().get(Analysis.class, analysis.getId());
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			if (analysisClone != null) {
				PropertyUtils.copyProperties(analysis, analysisClone);
			} else {
				analysis.setId(null);
			}
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getData()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getData()", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public List getAllAnalyses() throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from Analysis a order by a.id";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAllAnalyses()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAllAnalyses()", e);
		}

		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getPageOfAnalyses(int startingRecNo) throws LIMSRuntimeException {
		List list = new Vector();

		try {
			// calculate maxRow to be one more than the page size
			int endingRecNo = startingRecNo + (SystemConfiguration.getInstance().getDefaultPageSize() + 1);

			String sql = "from Analysis a order by a.id";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(startingRecNo - 1);
			query.setMaxResults(endingRecNo - 1);

			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getPageOfAnalyses()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getPageOfAnalyses()", e);
		}

		return list;
	}

	public Analysis readAnalysis(String idString) {
		Analysis analysis = null;
		try {
			analysis = (Analysis) HibernateUtil.getSession().get(Analysis.class, idString);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "readAnalysis()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis readAnalysis()", e);
		}

		return analysis;
	}

	@SuppressWarnings("rawtypes")
	public List getAnalyses(String filter) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from Analysis a where upper(a.analysisType) like upper(:param) order by upper(a.analysisType)";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", filter + "%");
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAnalyses()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAnalyses(String filter)", e);
		}

		return list;

	}

	@SuppressWarnings("rawtypes")
	public List getNextAnalysisRecord(String id) throws LIMSRuntimeException {

		return getNextRecord(id, "Analysis", Analysis.class);

	}

	@SuppressWarnings("rawtypes")
	public List getPreviousAnalysisRecord(String id) throws LIMSRuntimeException {

		return getPreviousRecord(id, "Analysis", Analysis.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAllAnalysesPerTest(Test test) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			// bugzilla 1773 sort by accession number
			// bugzilla 2300 exclude canceled tests
			String sql = "from Analysis a where a.test = :testId and (a.status is null or a.status NOT IN (:exclusionList)) order by a.sampleItem.sample.accessionNumber";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("testId", Integer.parseInt(test.getId()));
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			// bugzilla 2577
			query.setParameterList("exclusionList", statusesToExclude);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAllAnalysesPerTest()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAllAnalysesPerTest()", e);
		}

		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getAllAnalysisByTestAndStatus(String testId, List<Integer> statusIdList) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from Analysis a where a.test = :testId and a.statusId IN (:statusIdList) order by a.sampleItem.sample.accessionNumber";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("testId", Integer.parseInt(testId));
			query.setParameterList("statusIdList", statusIdList);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAllAnalysisByTestAndStatuses()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAllAnalysisByTestAndStatuses()", e);
		}

		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getAllAnalysisByTestsAndStatus(List<String> testIdList, List<Integer> statusIdList) throws LIMSRuntimeException {
		List list = new Vector();
		List<Integer> testList = new ArrayList<Integer>();
		try {
			String sql = "from Analysis a where a.test.id IN (:testList) and a.statusId IN (:statusIdList) order by a.sampleItem.sample.accessionNumber";

			for (String testId : testIdList) {
				testList.add(Integer.parseInt(testId));
			}

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameterList("testList", testList);
			query.setParameterList("statusIdList", statusIdList);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAllAnalysisByTestsAndStatuses()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAllAnalysisByTestsAndStatuses()", e);
		}

		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getAllAnalysisByTestAndExcludedStatus(String testId, List<Integer> statusIdList) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from Analysis a where a.test = :testId and a.statusId not IN (:statusIdList) and a.sampleItem.sample.accessionNumber is not null order by a.sampleItem.sample.accessionNumber";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("testId", Integer.parseInt(testId));
			query.setParameterList("statusIdList", statusIdList);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAllAnalysisByTestAndExcludedStatuses()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAllAnalysisByTestAndExcludedStatuses()", e);
		}

		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getAllAnalysisByTestSectionAndStatus(String testSectionId, List<Integer> statusIdList, boolean sortedByDateAndAccession)
			throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from Analysis a where a.test.testSection.id = :testSectionId and a.statusId IN (:statusIdList)";

			if (sortedByDateAndAccession) {
				sql += " order by a.sampleItem.sample.receivedTimestamp  asc, a.sampleItem.sample.accessionNumber";
			}

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("testSectionId", Integer.parseInt(testSectionId));
			query.setParameterList("statusIdList", statusIdList);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAllAnalysisByTestSectionAndStatuses()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAllAnalysisByTestSectionAndStatuses()", e);
		}

		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getAllAnalysisByTestSectionAndExcludedStatus(String testSectionId, List<Integer> statusIdList) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from Analysis a where a.test.testSection.id = :testSectionId and a.statusId NOT IN (:statusIdList) order by a.sampleItem.sample.receivedTimestamp asc, a.sampleItem.sample.accessionNumber ";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("testSectionId", Integer.parseInt(testSectionId));
			query.setParameterList("statusIdList", statusIdList);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAllAnalysisByTestSectionAndExcludedStatuses()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAllAnalysisByTestSectionAndExcludedStatuses()", e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysesBySampleItem(SampleItem sampleItem) throws LIMSRuntimeException {
		List<Analysis> list = null;

		try {

			String sql = "from Analysis a where a.sampleItem.id = :sampleItemId";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("sampleItemId", Integer.parseInt(sampleItem.getId()));

			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			LogEvent.logError("AnalysisDAOImpl", "getAnalysesBySampleItem()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAnalysesBySampleItem()", e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysesBySampleItemsExcludingByStatusIds(SampleItem sampleItem, Set<Integer> statusIds)
			throws LIMSRuntimeException {
		if (statusIds == null || statusIds.isEmpty()) {
			return getAnalysesBySampleItem(sampleItem);
		}

		List<Analysis> analysisList = null;

		try {
			String sql = "from Analysis a where a.sampleItem.id = :sampleItemId and a.statusId not in ( :statusList )";

			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("sampleItemId", Integer.parseInt(sampleItem.getId()));
			query.setParameterList("statusList", statusIds);

			analysisList = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAnalysesBySampleItemsExcludingByStatusIds()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAnalysesBySampleItemsExcludingByStatusIds()", e);
		}

		return analysisList;
	}

	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysisBySampleIds(List<Integer> sampleIds, Set<Integer> excludedAnalysisStatus, String sampleType) throws LIMSRuntimeException {
		List<Analysis> analysis = null;
		try {
			String sql = "from Analysis a where a.sampleItem.sample.id  in (:sampleIds)";

			if(StringUtils.isNotEmpty(sampleType))
				sql += " and a.sampleItem.typeOfSample.localAbbreviation= :sampleType";

			if(!excludedAnalysisStatus.isEmpty()) {
				sql += " and a.statusId NOT IN (:excludedAnalysisStatus)";
			}
			sql += " ORDER BY a.sampleItem.sortOrder";

			Query query = HibernateUtil.getSession().createQuery(sql);
			if(!excludedAnalysisStatus.isEmpty()) {
				query.setParameterList("excludedAnalysisStatus", excludedAnalysisStatus);
			}
			query.setParameterList("sampleIds", sampleIds);

			if(StringUtils.isNotEmpty(sampleType))
				query.setParameter("sampleType", sampleType);

			analysis = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			return analysis;

		} catch (Exception e) {
			LogEvent.logError("ResultDAOImpl", "getResultByAnalysis()", e.toString());
			throw new LIMSRuntimeException("Error in Result getResultByAnalysis()", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Analysis> getAnalysesBySampleStatusIdExcludingByStatusId(String statusId, Set<Integer> statusIds)
			throws LIMSRuntimeException {
		if (statusIds == null || statusIds.isEmpty()) {
			return getAnalysesBySampleStatusId(statusId);
		}

		String sql = "from Analysis a where a.sampleItem.sample.statusId = :sampleStatus and a.statusId not in (:excludedStatusIds)";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("sampleStatus", Integer.parseInt(statusId));
			query.setParameterList("excludedStatusIds", statusIds);

			List<Analysis> analysisList = query.list();
			closeSession();
			return analysisList;

		} catch (HibernateException e) {
			handleException(e, "getAnalysesBySampleStatusIdExcludingByStatusId");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysesBySampleStatusId(String statusId) throws LIMSRuntimeException {
		List<Analysis> analysisList = null;

		try {
			String sql = "from Analysis a where a.sampleItem.sample.statusId = :sampleStatusId";

			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("sampleStatusId", Integer.parseInt(statusId));

			analysisList = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAnalysesBySampleItemsExcludingByStatusIds()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAnalysesBySampleItemsExcludingByStatusIds()", e);
		}

		return analysisList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Analysis> getAnalysesBySampleIdExcludedByStatusId(String id, Set<Integer> statusIds) throws LIMSRuntimeException {
		if (statusIds == null || statusIds.isEmpty()) {
			return getAnalysesBySampleId(id);
		}

		String sql = "from Analysis a where a.sampleItem.sample.id = :sampleId and a.statusId not in ( :excludedIds)";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("sampleId", Integer.parseInt(id));
			query.setParameterList("excludedIds", statusIds);
			List<Analysis> analysisList = query.list();
			closeSession();
			return analysisList;
		} catch (HibernateException e) {
			handleException(e, "getAnalysesBySampleIdExcludedByStatusId");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Analysis> getAnalysesBySampleIdAndStatusId(String id, Set<Integer> statusIds) throws LIMSRuntimeException {
		if (statusIds == null || statusIds.isEmpty()) {
			return getAnalysesBySampleId(id);
		}

		String sql = "from Analysis a where a.sampleItem.sample.id = :sampleId and a.statusId in ( :statusIds) order by a.testSection.id, a.panel.id";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("sampleId", Integer.parseInt(id));
			query.setParameterList("statusIds", statusIds);
			List<Analysis> analysisList = query.list();
			closeSession();
			return analysisList;
		} catch (HibernateException e) {
			handleException(e, "getAnalysesBySampleIdAndStatusId");
		}

		return null;
	}

	/**
	 * bugzilla 1993 (part of 1942) getAnalysesReadyToBeReported() - returns the
	 * tests that should be updated with a printed date of today's date (see
	 * ResultsReport)
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAnalysesReadyToBeReported() throws LIMSRuntimeException {

		List list = new Vector();

		try {

			List analysisStatusesToInclude = new ArrayList();
			analysisStatusesToInclude.add(SystemConfiguration.getInstance().getAnalysisStatusReleased());

			List sampleStatusesToInclude = new ArrayList();
			sampleStatusesToInclude.add(SystemConfiguration.getInstance().getSampleStatusEntry2Complete());
			sampleStatusesToInclude.add(SystemConfiguration.getInstance().getSampleStatusReleased());

			list = HibernateUtil.getSession().getNamedQuery("analysis.getAnalysesReadyToBeReported")
					.setParameterList("analysisStatusesToInclude", analysisStatusesToInclude)
					.setParameterList("sampleStatusesToInclude", sampleStatusesToInclude).list();

		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAnalysesReadyToBeReported()", e.toString());

			throw new LIMSRuntimeException("Error in getAnalysesReadyToBeReported()", e);
		} finally {
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		}

		return list;

	}

	// bugzilla 1798
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAllChildAnalysesByResult(Result result) throws LIMSRuntimeException {
		List list = new Vector();

		try {
			// bugzilla 2300 exclude canceled tests
			String sql = "from Analysis a where a.parentResult = :param and a.status NOT IN (:param2)";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", result.getId());
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param2", statusesToExclude);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getallChildAnalysesByResult()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getallChildAnalysesByResult()", e);
		}

		return list;
	}

	// bugzilla 2227
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getMaxRevisionAnalysesBySample(SampleItem sampleItem) throws LIMSRuntimeException {
		List list = new Vector();
		try {

			String sql = "from Analysis a where (a.sampleItem.id, a.test.id, a.revision) IN "
					+ "(select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " + "group by b.sampleItem.id, b.test.id) "
					+ "and a.sampleItem.id = :param " +
					// bugzilla 2300 exclude canceled tests
					"and a.status NOT IN (:param2) " + "order by a.test.id, a.revision desc";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", sampleItem.getId());
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param2", statusesToExclude);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getMaxRevisionAnalysesBySample()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getMaxRevisionAnalysesBySample()", e);
		}

		return list;
	}

	// bugzilla 2300 (separate method for sample tracking)
	@SuppressWarnings("rawtypes")
	public List getMaxRevisionAnalysesBySampleIncludeCanceled(SampleItem sampleItem) throws LIMSRuntimeException {

		List list = new Vector();
		try {

			String sql = "from Analysis a where (a.sampleItem.id, a.test.id, a.revision) IN "
					+ "(select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " + "group by b.sampleItem.id, b.test.id) "
					+ "and a.sampleItem.id = :param " + "order by a.test.id, a.revision desc";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", sampleItem.getId());
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getMaxRevisionAnalysesBySample()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getMaxRevisionAnalysesBySample()", e);
		}

		return list;
	}

	// bugzilla 2227
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getRevisionHistoryOfAnalysesBySample(SampleItem sampleItem) throws LIMSRuntimeException {
		List list = new Vector();

		try {
			String sql = "from Analysis a where (a.sampleItem.id, a.test.id, a.revision) NOT IN "
					+ "(select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " + "group by b.sampleItem.id, b.test.id) "
					+ "and a.sampleItem.id = :param " +
					// bugzilla 2300 exclude canceled tests
					"and a.status NOT IN (:param2) " + "order by a.test.id, a.revision desc";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", sampleItem.getId());
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param2", statusesToExclude);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getRevisionHistoryOfAnalysesBySample()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getRevisionHistoryOfAnalysesBySample()", e);
		}

		return list;
	}

	// bugzilla 2227
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getRevisionHistoryOfAnalysesBySampleAndTest(SampleItem sampleItem, Test test, boolean includeLatestRevision)
			throws LIMSRuntimeException {
		List list = new Vector();

		try {
			String sql = "";
			if (includeLatestRevision) {
				sql = "from Analysis a " + "where a.sampleItem.id = :param " +
				// bugzilla 2300 exclude canceled tests
						"and a.status NOT IN (:param3) " + "and a.test.id = :param2 " + "order by a.test.id, a.revision desc";
			} else {
				sql = "from Analysis a where (a.sampleItem.id, a.test.id, a.revision) NOT IN "
						+ "(select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " + "group by b.sampleItem.id, b.test.id) "
						+ "and a.sampleItem.id = :param " +
						// bugzilla 2300 exclude canceled tests
						"and a.status NOT IN (:param3) " + "and a.test.id = :param2 " + "order by a.test.id, a.revision desc";
			}

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", sampleItem.getId());
			query.setParameter("param2", test.getId());
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param3", statusesToExclude);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getRevisionHistoryOfAnalysesBySample()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getRevisionHistoryOfAnalysesBySample()", e);
		}

		return list;

	}

	// bugzilla 2227
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAllMaxRevisionAnalysesPerTest(Test test) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from Analysis a where (a.sampleItem.id, a.revision) IN "
					+ "(select b.sampleItem.id, max(b.revision) from Analysis b " + "group by b.sampleItem.id) " + "and a.test = :param " +
					// bugzilla 2300 exclude canceled tests
					"and a.status NOT IN (:param2) " + "order by a.sampleItem.sample.accessionNumber";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", test.getId());
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param2", statusesToExclude);

			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAllMaxRevisionAnalysesPerTest()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getAllMaxRevisionAnalysesPerTest()", e);
		}

		return list;
	}

	// bugzilla 2227, 2258
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getMaxRevisionAnalysesReadyToBeReported() throws LIMSRuntimeException {
		List list = new Vector();

		try {

			List analysisStatusesToInclude = new ArrayList();
			analysisStatusesToInclude.add(SystemConfiguration.getInstance().getAnalysisStatusReleased());

			List sampleStatusesToInclude = new ArrayList();
			sampleStatusesToInclude.add(SystemConfiguration.getInstance().getSampleStatusEntry2Complete());
			sampleStatusesToInclude.add(SystemConfiguration.getInstance().getSampleStatusReleased());

			list = HibernateUtil.getSession().getNamedQuery("analysis.getMaxRevisionAnalysesReadyToBeReported")
					.setParameterList("analysisStatusesToInclude", analysisStatusesToInclude)
					.setParameterList("sampleStatusesToInclude", sampleStatusesToInclude).list();

		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getMaxRevisionAnalysesReadyToBeReported()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getMaxRevisionAnalysesReadyToBeReported()", e);
		} finally {
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		}

		return list;

	}

	// bugzilla 1900
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getMaxRevisionAnalysesReadyForReportPreviewBySample(List accessionNumbers) throws LIMSRuntimeException {
		List list = new Vector();

		try {

			List analysisStatusesToInclude = new ArrayList();
			// see question in 1900 should this be released or results completed
			// status?
			// answer: results completed
			analysisStatusesToInclude.add(SystemConfiguration.getInstance().getAnalysisStatusResultCompleted());

			List sampleStatusesToInclude = new ArrayList();
			sampleStatusesToInclude.add(SystemConfiguration.getInstance().getSampleStatusEntry2Complete());
			// see question in 1900 - should this be included? Yes
			sampleStatusesToInclude.add(SystemConfiguration.getInstance().getSampleStatusReleased());

			if (accessionNumbers != null && accessionNumbers.size() > 0) {
				list = HibernateUtil.getSession().getNamedQuery("analysis.getMaxRevisionAnalysesReadyForPreviewBySample")
						.setParameterList("analysisStatusesToInclude", analysisStatusesToInclude)
						.setParameterList("sampleStatusesToInclude", sampleStatusesToInclude)
						.setParameterList("samplesToInclude", accessionNumbers).list();
			}

		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getMaxRevisionAnalysesReadyForReportPreviewBySample()", e.toString());
			throw new LIMSRuntimeException("Error in getMaxRevisionAnalysesReadyForReportPreviewBySample()", e);
		} finally {
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		}

		return list;

	}

	// bugzilla 1856
	@SuppressWarnings("rawtypes")
	public List getAnalysesAlreadyReportedBySample(Sample sample) throws LIMSRuntimeException {
		List list = new Vector();

		try {

			list = HibernateUtil.getSession().getNamedQuery("analysis.getAnalysesAlreadyReportedBySample")
					.setParameter("sampleId", sample.getId()).list();

		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getAnalysesAlreadyReportedBySample()", e.toString());
			throw new LIMSRuntimeException("Error in getAnalysesAlreadyReportedBySample()", e);
		} finally {
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		}

		return list;

	}

	// bugzilla 2264
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getMaxRevisionPendingAnalysesReadyToBeReportedBySample(Sample sample) throws LIMSRuntimeException {
		List list = new Vector();

		try {

			List analysisStatusesToInclude = new ArrayList();
			analysisStatusesToInclude.add(SystemConfiguration.getInstance().getAnalysisStatusAssigned());
			// bugzilla 2264 per Nancy add results completed status to pending
			// tests
			analysisStatusesToInclude.add(SystemConfiguration.getInstance().getAnalysisStatusResultCompleted());

			list = HibernateUtil.getSession().getNamedQuery("analysis.getMaxRevisionPendingAnalysesReadyToBeReportedBySample")
					.setParameter("sampleId", sample.getId()).setParameterList("analysisStatusesToInclude", analysisStatusesToInclude)
					.list();

		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getMaxRevisionPendingAnalysesReadyToBeReportedBySample()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getMaxRevisionPendingAnalysesReadyToBeReportedBySample()", e);
		} finally {
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		}

		return list;
	}

	// bugzilla 1900
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getMaxRevisionPendingAnalysesReadyForReportPreviewBySample(Sample sample) throws LIMSRuntimeException {
		List list = new Vector();

		try {

			List analysisStatusesToInclude = new ArrayList();
			analysisStatusesToInclude.add(SystemConfiguration.getInstance().getAnalysisStatusAssigned());
			// see question in 1900 do we need to include this?
			// Answer NO
			// analysisStatusesToInclude.add(SystemConfiguration.getInstance().getAnalysisStatusResultCompleted());

			list = HibernateUtil.getSession().getNamedQuery("analysis.getMaxRevisionPendingAnalysesReadyToBeReportedBySample")
					.setParameter("sampleId", sample.getId()).setParameterList("analysisStatusesToInclude", analysisStatusesToInclude)
					.list();

		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getMaxRevisionPendingAnalysesReadyForReportPreviewBySample()", e.toString());
			throw new LIMSRuntimeException("Error in getMaxRevisionPendingAnalysesReadyForReportPreviewBySample()", e);
		} finally {
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		}

		return list;
	}

	// bugzilla 2265
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Analysis getPreviousAnalysisForAmendedAnalysis(Analysis analysis) throws LIMSRuntimeException {
		Analysis previousAnalysis = null;
		try {
			// Use an expression to read in the Analysis whose
			// revision is 1 less than the analysis passed in

			// bugzilla 2300 exclude canceled tests
			String sql = "from Analysis a where a.revision = :param and a.sampleItem = :param2 and a.test = :param3 and a.status NOT IN (:param4)";
			Query query = HibernateUtil.getSession().createQuery(sql);

			String revisionString = analysis.getRevision();
			int revision = 0;
			if (!StringUtil.isNullorNill(revisionString)) {
				try {
					revision = Integer.parseInt(revisionString);
				} catch (NumberFormatException nfe) {

				}
			}

			query.setParameter("param", String.valueOf((revision - 1)));
			query.setParameter("param2", analysis.getSampleItem());
			query.setParameter("param3", analysis.getTest());
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param4", statusesToExclude);
			List list = query.list();
			if ((list != null) && !list.isEmpty()) {
				previousAnalysis = (Analysis) list.get(0);
			}
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getPreviousAnalysisForAmendedAnalysis()", e.toString());
			throw new LIMSRuntimeException("Exception occurred in getPreviousAnalysisForAmendedAnalysis", e);
		}
		return previousAnalysis;

	}

	// bugzilla 2013
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean duplicateAnalysisExists(Analysis analysis) throws LIMSRuntimeException {
		try {

			List list;

			// not case sensitive hemolysis and Hemolysis are considered
			// duplicates
			// bugzilla 2300 exclude canceled analyses
			String sql = "from Analysis a where a.sampleItem = :param and a.test = :param2 and a.status NOT IN (:param3)";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", analysis.getSampleItem());
			query.setParameter("param2", analysis.getTest());
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param3", statusesToExclude);

			list = query.list();
			return (list.size() > 0);

		} catch (Exception e) {
			LogEvent.logErrorStack("AnalysisDAOImpl", "duplicateAnalysisExists()", e);
			throw new LIMSRuntimeException("Error in duplicateAnalysisExists()", e);
		}
	}

	// bugzilla 2300
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getMaxRevisionAnalysisBySampleAndTest(Analysis analysis) throws LIMSRuntimeException {

		try {
			Analysis anal = null;
			String sql = "from Analysis a where (a.sampleItem.id, a.test.id, a.revision) IN "
					+ "(select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " + "group by b.sampleItem.id, b.test.id) "
					+ "and a.sampleItem = :param " +
					// bugzilla 2300 exclude canceled tests
					"and a.status NOT IN (:param3) " + "and a.test = :param2";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", analysis.getSampleItem().getId());
			query.setParameter("param2", analysis.getTest().getId());
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param3", statusesToExclude);
			anal = (Analysis) query.uniqueResult();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			if (anal != null) {
				PropertyUtils.copyProperties(analysis, anal);
			} else {
				analysis.setId(null);
			}

		} catch (Exception e) {

			LogEvent.logError("AnalysisDAOImpl", "getMaxRevisionAnalysisBySampleAndTest()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getMaxRevisionAnalysisBySampleAndTest()", e);
		}

	}

	// bugzilla 2532
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getMaxRevisionParentTestAnalysesBySample(SampleItem sampleItem) throws LIMSRuntimeException {

		List list = new Vector();
		try {

			String sql = "from Analysis a where (a.sampleItem.id, a.test.id, a.revision) IN "
					+ "(select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " + "group by b.sampleItem.id, b.test.id) "
					+ "and a.sampleItem.id = :param " +
					// bugzilla 2300 exclude canceled tests
					"and a.status NOT IN (:param2) " +
					// bugzilla 2532 don't include tests linked as child tests
					// on a sample
					"and a.parentAnalysis is null " + "order by a.test.id, a.revision desc";

			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", sampleItem.getId());
			// bugzilla 2300 exclude canceled tests
			List statusesToExclude = new ArrayList();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param2", statusesToExclude);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			LogEvent.logError("AnalysisDAOImpl", "getMaxRevisionAnalysesBySample()", e.toString());
			throw new LIMSRuntimeException("Error in Analysis getMaxRevisionAnalysesBySample()", e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysesForStatusId(String statusId) throws LIMSRuntimeException {

		List<Analysis> list = null;

		try {
			String sql = "from Analysis a where a.statusId = :statusId";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("statusId", Integer.parseInt(statusId));

			list = query.list();
			closeSession();
			return list;
		} catch (HibernateException he) {
			handleException(he, "getAnalysisForStatusId");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Analysis> getAnalysisStartedOnExcludedByStatusId(Date collectionDate, Set<Integer> statusIds) throws LIMSRuntimeException {
		if (statusIds == null || statusIds.isEmpty()) {
			return getAnalysisStartedOn(collectionDate);
		}

		String sql = "from Analysis a where a.startedDate = :startedDate and a. and a.statusId not in ( :statusList )";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setDate("sampleItemId", collectionDate);
			query.setParameterList("statusList", statusIds);

			List<Analysis> analysisList = query.list();
			closeSession();
			return analysisList;
		} catch (HibernateException e) {
			handleException(e, "getAnalysisStartedOnExcludedByStatusId");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysisStartedOn(Date collectionDate) throws LIMSRuntimeException {

		try {
			String sql = "from Analysis a where a.startedDate = :startedDate";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setDate("startedDate", collectionDate);

			List<Analysis> list = query.list();
			closeSession();
			return list;

		} catch (HibernateException he) {
			handleException(he, "getAnalysisStartedOn");
		}

		return null;
	}

	/**
	 * @see us.mn.state.health.lims.analysis.dao.AnalysisDAO#getAnalysisBySampleAndTestIds(java.lang.String,
	 *      java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysisBySampleAndTestIds(String sampleId, List<Integer> testIds) {
		List<Analysis> list;
		try {
			if (testIds == null || testIds.size() == 0) {
				return new ArrayList<>();
			}
			String sql = "from Analysis a WHERE a.sampleItem.sample.id = :sampleId AND a.test.id IN ( :testIds )";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("sampleId", Integer.valueOf(sampleId));
			query.setParameterList("testIds", testIds);

			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (HibernateException he) {
			LogEvent.logError("AnalysisDAOImpl", "getAnalysisBySampleAndTestIds()", he.toString());
			throw new LIMSRuntimeException("Error in getAnalysisStartedOn()", he);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysisByTestSectionAndCompletedDateRange(String sectionID, Date lowDate, Date highDate)
			throws LIMSRuntimeException {

		String sql = "From Analysis a where a.testSection.id = :testSectionId and a.completedDate BETWEEN :lowDate AND :highDate";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("testSectionId", Integer.parseInt(sectionID));
			query.setDate("lowDate", lowDate);
			query.setDate("highDate", highDate);

			List<Analysis> list = query.list();
			closeSession();
			return list;
		} catch (HibernateException he) {
			handleException(he, "getAnalysisByTestSectionAndCompletedDateRange");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysisStartedOrCompletedInDateRange(Date lowDate, Date highDate) throws LIMSRuntimeException {
		String sql = "From Analysis a where a.startedDate BETWEEN :lowDate AND :highDate or a.completedDate BETWEEN :lowDate AND :highDate";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setDate("lowDate", lowDate);
			query.setDate("highDate", highDate);

			List<Analysis> list = query.list();
			closeSession();
			return list;
		} catch (HibernateException he) {
			handleException(he, "getAnalysisStartedOrCompletedInDateRange");
		}

		return null;

	}


	@SuppressWarnings("unchecked")
	public List<Analysis> getAnalysesBySampleId(String id) throws LIMSRuntimeException {
		List<Analysis> list = null;
		if (!GenericValidator.isBlankOrNull(id)) {
			try {
				String sql = "from Analysis a where a.sampleItem.sample.id = :sampleId";

				org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
				query.setInteger("sampleId", Integer.parseInt(id));

				list = query.list();
				closeSession();
			} catch (Exception e) {
				handleException(e, "getAnalysesBySampleId");
			}
		}
		return list;
	}

    @SuppressWarnings("unchecked")
    public List<Analysis> getAllAnalysisByTestSectionAndStatus(String testSectionId, List<Integer> analysisStatusList,
                                                               List<Integer> sampleStatusList) throws LIMSRuntimeException {

        String sql = "From Analysis a WHERE a.test.testSection.id = :testSectionId AND a.statusId IN (:analysisStatusList) AND a.sampleItem.sample.statusId IN (:sampleStatusList)";

        try {
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setInteger("testSectionId", Integer.parseInt(testSectionId));
            query.setParameterList("analysisStatusList", analysisStatusList);
            query.setParameterList("sampleStatusList", sampleStatusList);

            List<Analysis> analysisList = query.list();

            closeSession();

            return analysisList;

        } catch (HibernateException e) {
            handleException(e, "getAllAnalysisByTestSectionAndStatus");
        }

        return null;
    }

    @SuppressWarnings("unchecked")
	public List<PatientAnalysis> getAllPatientAnalysisByTestSectionAndStatus(String testSectionId, List<Integer> analysisStatusList,
			List<Integer> sampleStatusList) throws LIMSRuntimeException {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select  a.ID ,  ");
        sqlBuilder.append(" pat.id as pat_id,pat.national_id,pat.external_id,pat.gender,pat.birth_date,p.first_name,p.last_name,pi.identity_data from clinlims.analysis a ");
        sqlBuilder.append(" join clinlims.test t on t.id = a.test_id ");
        sqlBuilder.append(" join clinlims.sample_item si on si.id = a.sampitem_id ");
        sqlBuilder.append(" join clinlims.sample s on s.id = si.samp_id ");
        sqlBuilder.append(" join clinlims.sample_human sh on sh.samp_id = s.id ");
        sqlBuilder.append(" join clinlims.patient pat on pat.id = sh.patient_id  ");
        sqlBuilder.append(" join clinlims.person p on pat.person_id = p.id  ");
        sqlBuilder.append(" join clinlims.patient_identity pi on pi.patient_id = pat.id  ");
        sqlBuilder.append(" join clinlims.patient_identity_type pit on pit.id = pi.identity_type_id  ");
        sqlBuilder.append(" where a.status_id IN (:analysisStatusList) AND t.test_section_id = :testSectionId AND s.status_id IN (:sampleStatusList) AND pit.identity_type ='ST'" );



        try{
            Query query = HibernateUtil.getSession().createSQLQuery(sqlBuilder.toString()) ;

            query.setResultTransformer(Transformers.aliasToBean(PatientAnalysis.class));

            query.setInteger("testSectionId", Integer.parseInt(testSectionId));
            query.setParameterList("analysisStatusList", analysisStatusList);
            query.setParameterList("sampleStatusList", sampleStatusList);

            List<PatientAnalysis> analysisList = query.list();

            closeSession();
            return analysisList;

		} catch (HibernateException e) {
			handleException(e, "getAllPatientAnalysisByTestSectionAndStatus");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Analysis> getAnalysisStartedOnRangeByStatusId(Date lowDate, Date highDate, String statusID) throws LIMSRuntimeException {
		String sql = "From Analysis a where a.statusId = :statusID and a.startedDate BETWEEN :lowDate AND :highDate";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("statusID", Integer.parseInt(statusID));
			query.setDate("lowDate", lowDate);
			query.setDate("highDate", highDate);

			List<Analysis> analysisList = query.list();
			closeSession();
			return analysisList;

		} catch (HibernateException e) {
			handleException(e, "getAnalysisStartedOnRangeByStatusId");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Analysis> getAnalysisCompleteInRange(Timestamp lowDate, Timestamp highDate) throws LIMSRuntimeException {
		String sql = "From Analysis a where a.completedDate >= :lowDate AND a.completedDate < :highDate";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setTimestamp("lowDate", lowDate);
			query.setTimestamp("highDate", highDate);

			List<Analysis> analysisList = query.list();
			closeSession();
			return analysisList;

		} catch (HibernateException e) {
			handleException(e, "getAnalysisCompletedInRange");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Analysis> getAnalysisEnteredAfterDate(Timestamp date) throws LIMSRuntimeException {
		String sql = "From Analysis a where a.enteredDate > :date";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setTimestamp("date", date);

			List<Analysis> analysisList = query.list();
			closeSession();
			return analysisList;

		} catch (HibernateException e) {
			handleException(e, "getAnalysisEnteredAfterDate");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Analysis> getAnalysisByAccessionAndTestId(String accessionNumber, String testId) throws LIMSRuntimeException {
		if (GenericValidator.isBlankOrNull(accessionNumber) || GenericValidator.isBlankOrNull(testId)) {
			return new ArrayList<Analysis>();
		}

		String sql = "From Analysis a where a.sampleItem.sample.accessionNumber = :accessionNumber and a.test.id = :testId";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setString("accessionNumber", accessionNumber);
			query.setInteger("testId", Integer.parseInt(testId));
			List<Analysis> analyses = query.list();
			closeSession();
			return analyses;
		} catch (HibernateException e) {
			handleException(e, "getAnalysisByAccessionAndTestId");
		}

		return null;
	}

	@Override
	public List<Analysis> getAllAnalysisByAccessionNumberAndStatusAndSampleType(String accessionNumber, List<StatusOfSampleUtil.AnalysisStatus> analysisStatuses, String sampleType) {
		if (GenericValidator.isBlankOrNull(accessionNumber) || analysisStatuses == null || analysisStatuses.isEmpty()) {
			return new ArrayList<>();
		}
		String sql = "From Analysis a where a.sampleItem.sample.accessionNumber = :accessionNumber and a.statusId IN (:statusIdList)";

		if (StringUtils.isNotEmpty(sampleType))
			sql += " and a.sampleItem.typeOfSample.localAbbreviation= :sampleType";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setString("accessionNumber", accessionNumber);
			query.setParameterList("statusIdList", getStatusIds(analysisStatuses));
			if(StringUtils.isNotEmpty(sampleType))
				query.setParameter("sampleType", sampleType);
			return query.list();
		} catch (HibernateException e) {
			handleException(e, "getAllAnalysisByAccessionNumberAndStatusAndSampleType");
		}
		return null;
	}

    @Override
    public List<Analysis> getAllAnalysisByStatus(List<StatusOfSampleUtil.AnalysisStatus> analysisStatuses) {
        if (analysisStatuses == null || analysisStatuses.isEmpty()) {
            return new ArrayList<>();
        }
        String sql = "From Analysis a where a.statusId IN (:statusIdList)";
        try {
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setParameterList("statusIdList", getStatusIds(analysisStatuses));
            return query.list();
        } catch (HibernateException e) {
            handleException(e, "getAllAnalysisByStatus");
        }
        return null;
    }

    @Override
    public List<Analysis> getAnalysisByTestNamesAndCompletedDateRange(List<String> testNames, Date lowDate, Date highDate)
            throws LIMSRuntimeException {
        if (testNames.isEmpty()) {
            return new ArrayList<Analysis>();
        }

        String sql = "From Analysis a where a.test.testName in (:testNames) and a.completedDate BETWEEN :lowDate AND :highDate";

        try {
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setParameterList("testNames", testNames);
            query.setDate("lowDate", lowDate);
            query.setDate("highDate", highDate);

            @SuppressWarnings("unchecked")
            List<Analysis> list = query.list();
            closeSession();
            return list;
        } catch (HibernateException he) {
            handleException(he, "getAnalysisByTestNamesAndCompletedDateRange");
        }

        return null;
    }

    private List<Integer> getStatusIds(List<StatusOfSampleUtil.AnalysisStatus> analysisStatuses) {
        List<Integer> statusIds = new ArrayList<>();
        for (StatusOfSampleUtil.AnalysisStatus analysisStatus : analysisStatuses) {
            statusIds.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(analysisStatus)));
        }
        return statusIds;
    }

	@Override
	public List<Analysis> getAnalysisByTestDescriptionAndCompletedDateRange(List<String> descriptions, Date lowDate, Date highDate)
			throws LIMSRuntimeException {
		if (descriptions.isEmpty()) {
			return new ArrayList<Analysis>();
		}

		String sql = "From Analysis a where a.test.description in (:descriptions) and a.completedDate BETWEEN :lowDate AND :highDate";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameterList("descriptions", descriptions);
			query.setDate("lowDate", lowDate);
			query.setDate("highDate", highDate);

			@SuppressWarnings("unchecked")
			List<Analysis> list = query.list();
			closeSession();
			return list;
		} catch (HibernateException he) {
			handleException(he, "getAnalysisByTestDescriptionsAndCompletedDateRange");
		}

		return null;
	}
	@Override
	public List<Analysis> getAnalysesBySampleItemIdAndStatusId(String sampleItemId, String statusId) throws LIMSRuntimeException {
		try {
			String sql = "from Analysis a where a.sampleItem.id = :sampleItemId and a.statusId = :statusId";

			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("sampleItemId", Integer.parseInt(sampleItemId));
			query.setInteger("statusId", Integer.parseInt(statusId));

			@SuppressWarnings("unchecked")
			List<Analysis> analysisList = query.list();
			closeSession();
			return analysisList;
		} catch (Exception e) {
			handleException(e, "getAnalysesBySampleItemIdAndStatusId");
		}

		return null; //will never get here
	}

	@Override
	public Analysis getAnalysisById(String analysisId) throws LIMSRuntimeException {
		try {
			Analysis analysis = (Analysis) HibernateUtil.getSession().get(Analysis.class, analysisId);
			closeSession();
			return analysis;
		} catch (Exception e) {
			handleException(e, "getAnalysisById");
		}
		
		return null;
	}
}

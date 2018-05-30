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
package us.mn.state.health.lims.sample.daoimpl;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * @author diane benz
 */
public class SampleDAOImpl extends BaseDAOImpl implements SampleDAO {

	private static final String ACC_NUMBER_SEQ_BEGIN = "000001";

	public void deleteData(List samples) throws LIMSRuntimeException {
		// add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			for (int i = 0; i < samples.size(); i++) {
				Sample data = (Sample) samples.get(i);

				Sample oldData = (Sample) readSample(data.getId());
				Sample newData = new Sample();

				String sysUserId = data.getSysUserId();
				String event = IActionConstants.AUDIT_TRAIL_DELETE;
				String tableName = "SAMPLE";
				auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "AuditTrail deleteData()", e.toString());
			throw new LIMSRuntimeException("Error in Sample AuditTrail deleteData()", e);
		}

		try {
			for (int i = 0; i < samples.size(); i++) {
				Sample data = (Sample) samples.get(i);
				//bugzilla 2206
				data = (Sample) readSample(data.getId());
				HibernateUtil.getSession().delete(data);
				HibernateUtil.getSession().flush();
				HibernateUtil.getSession().clear();
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "deleteData()", e.toString());
			throw new LIMSRuntimeException("Error in Sample deleteData()", e);
		}
	}

	//Note: the accession number is a business rule and should be externalized
	public boolean insertData(Sample sample) throws LIMSRuntimeException {

		try {
			sample.setAccessionNumber(getNextAccessionNumber());
			String id = (String) HibernateUtil.getSession().save(sample);
			sample.setId(id);

			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = sample.getSysUserId();
			String tableName = "SAMPLE";
			auditDAO.saveNewHistory(sample, sysUserId, tableName);

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (Exception e) {
			LogEvent.logError("SampleDAOImpl", "insertData()", e.toString());
			throw new LIMSRuntimeException("Error in Sample insertData()", e);
		}

		return true;
	}

	/**
	 * Insert the specified Sample, which has already been assigned an
	 * accession number by the user from the input view.
	 *
	 * @return boolean True if the insert was successful.
	 * @param    Sample        The Sample to be inserted into the database.
	 */
	public boolean insertDataWithAccessionNumber(Sample sample)
			throws LIMSRuntimeException {
		try {

			String id = (String) HibernateUtil.getSession().save(sample);
			sample.setId(id);

			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			auditDAO.saveNewHistory(sample, sample.getSysUserId(), "SAMPLE");

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "insertData()", e.toString());
			throw new LIMSRuntimeException("Error in Sample insertData()", e);
		}
		return true;
	}

	public void updateData(Sample sample) throws LIMSRuntimeException {

		Sample oldData = (Sample) readSample(sample.getId());
		Sample newData = sample;

		// add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = sample.getSysUserId();
			String event = IActionConstants.AUDIT_TRAIL_UPDATE;
			String tableName = "SAMPLE";
			auditDAO.saveHistory(newData, oldData, sysUserId, event, tableName);
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "updateData()", e.toString());
			throw new LIMSRuntimeException("Error in Sample AuditTrail updateData()", e);
		}

		try {
			Session session = HibernateUtil.getSession();
			session.merge(sample);
			session.flush();
			session.clear();
			session.evict(sample);
			session.refresh(sample);
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "updateData()", e.toString());
			throw new LIMSRuntimeException("Error in Sample updateData()", e);
		}
	}

	public void getData(Sample sample) throws LIMSRuntimeException {
		try {
			Sample samp = (Sample) HibernateUtil.getSession().get(Sample.class, sample.getId());

			if (samp != null) {
				// set the display dates for STARTED_DATE, COMPLETED_DATE
				String locale = SystemConfiguration.getInstance().getDefaultLocale().toString();
				samp.setEnteredDateForDisplay(DateUtil.convertSqlDateToStringDate(
						new java.sql.Date(samp.getEnteredDate().getTime()), locale));
				samp.setReceivedDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getReceivedDate(), locale));
				Timestamp ts = samp.getCollectionDate();
				samp.setCollectionDateForDisplay(DateUtil.convertTimestampToStringDate(ts, locale));

				samp.setCollectionTimeForDisplay(DateUtil.convertTimestampToStringTime(ts, locale));

				samp.setTransmissionDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getTransmissionDate(), locale));
				samp.setReleasedDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getReleasedDate(), locale));

				// set sample projects
				String sql = "from SampleProject sp where samp_id = :sampleId";
				Query query = HibernateUtil.getSession().createQuery(sql);
				query.setParameter("sampleId", Integer.parseInt(samp.getId()));
				List list = query.list();
//				HibernateUtil.getSession().flush();
//				HibernateUtil.getSession().clear();

				samp.setSampleProjects(list);
				// set the display date for RECEIVED_DATE
				samp.setReceivedDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getReceivedDate(), locale));

				PropertyUtils.copyProperties(sample, samp);
			} else {
				sample.setId(null);
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "getData()", e.toString());
			throw new LIMSRuntimeException("Error in Sample getData()", e);
		}
	}

	public void getSampleByAccessionNumber(Sample sample) throws LIMSRuntimeException {
		try {
			String sql = "from Sample s where s.accessionNumber = :param";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", sample.getAccessionNumber());
			List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			Sample samp = null;
			if (list.size() > 0)
				samp = (Sample) list.get(0);

			if (samp != null) {
				// set the display dates for STARTED_DATE, COMPLETED_DATE
				String locale = SystemConfiguration.getInstance().getDefaultLocale().toString();
				samp.setEnteredDateForDisplay(DateUtil.convertSqlDateToStringDate(new java.sql.Date(samp.getEnteredDate().getTime()), locale));
				samp.setReceivedDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getReceivedDate(), locale));
				Timestamp ts = samp.getCollectionDate();
				samp.setCollectionDateForDisplay(DateUtil.convertTimestampToStringDate(ts, locale));

				// System.out.println("I am in SampleDAOImple getData just set
				// collection date for display " +
				// samp.getCollectionDateForDisplay());
				samp.setCollectionTimeForDisplay(DateUtil.convertTimestampToStringTime(ts, locale));
				// System.out.println("I am in SampleDAOImple getData just set
				// collection time for display " +
				// samp.getCollectionTimeForDisplay());
				samp.setTransmissionDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getTransmissionDate(), locale));
				samp.setReleasedDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getReleasedDate(), locale));

				// set sample projects
				sql = "from SampleProject sp where samp_id = :param";
				query = HibernateUtil.getSession().createQuery(sql);
				query.setInteger("param", Integer.parseInt(samp.getId()));
				List sp = query.list();
				HibernateUtil.getSession().flush();
				HibernateUtil.getSession().clear();
				samp.setSampleProjects(sp);

				PropertyUtils.copyProperties(sample, samp);
			} else {
				sample.setId(null);
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "getDataByAccessionNumber()", e.toString());
			throw new LIMSRuntimeException("Error in Sample getDataByAccessionNumber()", e);
		}
	}

	public List getAllSamples() throws LIMSRuntimeException {
		List samples = new Vector();
		try {
			String sql = "from Sample";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			samples = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			Sample samp = new Sample();
			// System.out.println("Getallsamples 2");
			String locale = SystemConfiguration.getInstance()
					.getDefaultLocale().toString();
			// set the display dates for ENTERED_DATE, RECEIVED_DATE,
			// COLLECTION_DATE, RELEASED_DATE, TRANSMISSION_DATE
			for (int i = 0; i < samples.size(); i++) {
				samp = (Sample) samples.get(i);
				samp.setEnteredDateForDisplay(DateUtil.convertSqlDateToStringDate(new java.sql.Date(samp.getEnteredDate().getTime()), locale));
				samp.setReceivedDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getReceivedDate(), locale));
				samp.setCollectionDateForDisplay(DateUtil.convertTimestampToStringDate(samp.getCollectionDate(), locale));
				samp.setTransmissionDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getTransmissionDate(), locale));
				samp.setReleasedDateForDisplay(DateUtil.convertSqlDateToStringDate(samp.getReleasedDate(), locale));
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "getAllSamples()", e.toString());
			throw new LIMSRuntimeException("Error in Sample getAllSamples()", e);
		}

		return samples;
	}

	public List getPageOfSamples(int startingRecNo) throws LIMSRuntimeException {
		List samples = new Vector();
		try {

			// calculate maxRow to be one more than the page size
			int endingRecNo = startingRecNo + (SystemConfiguration.getInstance().getDefaultPageSize() + 1);

			String sql = "from Sample s order by s.id";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(startingRecNo - 1);
			query.setMaxResults(endingRecNo - 1);

			samples = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			Sample samp = new Sample();
			String locale = SystemConfiguration.getInstance()
					.getDefaultLocale().toString();

			// set the display dates for STARTED_DATE, COMPLETED_DATE
			for (int i = 0; i < samples.size(); i++) {
				samp = (Sample) samples.get(i);
				samp.setEnteredDateForDisplay(DateUtil
						.convertSqlDateToStringDate(new java.sql.Date(samp.getEnteredDate().getTime()),
                                locale));
				samp.setReceivedDateForDisplay(DateUtil
						.convertSqlDateToStringDate(samp.getReceivedDate(),
                                locale));
				samp.setCollectionDateForDisplay(DateUtil
						.convertTimestampToStringDate(samp.getCollectionDate(),
                                locale));
				samp.setTransmissionDateForDisplay(DateUtil
						.convertSqlDateToStringDate(samp.getTransmissionDate(),
                                locale));
				samp.setReleasedDateForDisplay(DateUtil
						.convertSqlDateToStringDate(samp.getReleasedDate(),
                                locale));
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "getPageOfSamples()", e.toString());
			throw new LIMSRuntimeException("Error in Sample getPageOfSamples()", e);
		}

		return samples;
	}

	public Sample readSample(String idString) {
		Sample samp = null;
		try {
			samp = (Sample) HibernateUtil.getSession().get(Sample.class, idString);
//			HibernateUtil.getSession().flush();
//			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "readSample()", e.toString());
			throw new LIMSRuntimeException("Error in Sample readSample()", e);
		}
		return samp;
	}


	public String getNextAccessionNumber() throws LIMSRuntimeException {
		String accessionNumber = null;
		String lastAccessionNumber = null;

		// get the current year
		Calendar cal = Calendar.getInstance();
		int currentYear = cal.get(Calendar.YEAR);

		try {
			String sql = "select max(s.accessionNumber) from Sample s";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);

			List reports = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			if (reports != null && reports.get(0) != null) {
				if (reports.get(0) != null) {
					lastAccessionNumber = (String) reports.get(0);
					if (lastAccessionNumber != null) {
						String lastAccessionNumberYear = lastAccessionNumber.substring(0, 4);
						if (lastAccessionNumberYear.equals(String.valueOf(currentYear))) {
							String seqNumber = lastAccessionNumber.substring(4);
							int sequence = Integer.parseInt(seqNumber);
							sequence++;
							String stringSequenceShort = String.valueOf(sequence);
							String stringSequenceLong = "";
							if (stringSequenceShort.length() < 6) {
								int zeroPaddingLength = 6 - stringSequenceShort.length();
								StringBuffer zeros = new StringBuffer();
								for (int i = 0; i < zeroPaddingLength; i++) {
									zeros.append("0");
								}
								stringSequenceLong = zeros + stringSequenceShort;
							}
							if (stringSequenceShort.length() == 6) {
								stringSequenceLong = stringSequenceShort;
							}
							if (stringSequenceShort.length() > 6) {// we
								// are
								// over
								// the
								// limit
								//System.out.println("Error in Sample getNextAccessionNumber() max sequence number reached");
								throw new LIMSRuntimeException("Error in Sample getNextAccessionNumber() max sequence number reached");
							}
							accessionNumber = currentYear + stringSequenceLong;
						} else {
							// start with new sequence - new year
							accessionNumber = currentYear + ACC_NUMBER_SEQ_BEGIN;
						}
					} else {
						// nothing retrieved (start from scratch)
						accessionNumber = currentYear + ACC_NUMBER_SEQ_BEGIN;
					}
				} else {
					// nothing retrieved (start from scratch)
					accessionNumber = currentYear + ACC_NUMBER_SEQ_BEGIN;
				}
			} else {
				//fixed bug - don't throw an exception - if no samples in database start from scratch
				//nothing retrieved (start from scratch)
				accessionNumber = currentYear + ACC_NUMBER_SEQ_BEGIN;
			}

		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "getNextAccessionNumber()", e.toString());
			throw new LIMSRuntimeException("Error in Sample getNextAccessionNumber()", e);
		}
		return accessionNumber;
	}


	public List getNextSampleRecord(String id) throws LIMSRuntimeException {

		return getNextRecord(id, "Sample", Sample.class);

	}

	public List getPreviousSampleRecord(String id) throws LIMSRuntimeException {

		return getPreviousRecord(id, "Sample", Sample.class);
	}

	/**
	 * Get the Sample for the specified accession number.
	 *
	 * @param    String        The accession number of the Sample being sought.
	 * @return Sample        The Sample for the specified accession number, or
	 * null if the accession number does not exist.
	 */
	@SuppressWarnings("unchecked")
	public Sample getSampleByAccessionNumber(String accessionNumber)
			throws LIMSRuntimeException {
		Sample sample = null;
		try {
			String sql = "from Sample s where accession_number = :param";
			Query query = HibernateUtil.getSession().createQuery(sql);

			query.setParameter("param", accessionNumber);
			List<Sample> list = query.list();
			if ((list != null) && !list.isEmpty()) {
				sample = list.get(0);
			}
		} catch (Exception e) {
			throw new LIMSRuntimeException("Exception occurred in getSampleForAccessionNumber", e);
		}
		return sample;
	}

	@Override
	public Sample getSampleByAccessionNumberAndType(String accessionNumber, String sampleType) throws LIMSRuntimeException {
		Sample sample = null;
		try {
			String sql = "select si.sample from SampleItem si inner join si.sample inner join si.typeOfSample where " +
					"si.sample.accessionNumber = :accessionNumber and si.typeOfSample.localAbbreviation = :sampleType";
			Query query = HibernateUtil.getSession().createQuery(sql);

			query.setParameter("accessionNumber", accessionNumber);
			query.setParameter("sampleType", sampleType);
			List<Sample> list = query.list();
			if ((list != null) && !list.isEmpty()) {
				sample = list.get(0);
			}
		} catch (Exception e) {
			throw new LIMSRuntimeException("Exception occurred in getSampleForAccessionNumber", e);
		}
		return sample;
	}


	public List getSamplesByStatusAndDomain(List statuses, String domain) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from Sample s where status in (:param1) and domain = :param2";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameterList("param1", statuses);
			query.setParameter("param2", domain);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {


			//bugzilla 2154
			LogEvent.logError("SampleDAOImpl", "getAllSampleByStatusAndDomain()", e.toString());
			throw new LIMSRuntimeException("Error in Sample getAllSampleByStatusAndDomain()", e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Sample> getSamplesWithPendingQaEvents(Sample sample, boolean filterByQaEventCategory, String qaEventCategoryId, boolean filterByDomain) throws LIMSRuntimeException {
		List<Sample> list;

		try {

			String sql = "";

			if (filterByDomain) {
				if (filterByQaEventCategory) {
					sql = "from Sample s where s.id IN " +
							"(select sqe.sample.id from SampleQaEvent sqe where sqe.completedDate is null and sqe.qaEvent.category = :param2) " +
							" or s.id IN " +
							"(select aqe.analysis.sampleItem.sample.id from AnalysisQaEvent aqe where aqe.completedDate is null and aqe.qaEvent.category = :param2 and " +
							//bugzilla 2300 exclude canceled tests
							"aqe.analysis.status NOT IN (:param3) and" +
							//make sure we only pick the max revision analyses that have qa events pending
							"(aqe.analysis.sampleItem.id, aqe.analysis.test.id, aqe.analysis.revision) IN (select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " +
							" group by b.sampleItem.id, b.test.id)) " +
							"and s.domain = :param " +
							"order by s.accessionNumber";
				} else {
					sql = "from Sample s where s.id IN " +
							"(select sqe.sample.id from SampleQaEvent sqe where sqe.completedDate is null) " +
							" or s.id IN " +
							"(select aqe.analysis.sampleItem.sample.id from AnalysisQaEvent aqe where aqe.completedDate is null and " +
							//bugzilla 2300 exclude canceled tests
							"aqe.analysis.status NOT IN (:param3) and " +
							//make sure we only pick the max revision analyses that have qa events pending
							"(aqe.analysis.sampleItem.id, aqe.analysis.test.id, aqe.analysis.revision) IN (select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " +
							" group by b.sampleItem.id, b.test.id)) " +
							"and s.domain = :param " +
							"order by s.accessionNumber";
				}
			} else {
				if (filterByQaEventCategory) {
					sql = "from Sample s where s.id IN " +
							"(select sqe.sample.id from SampleQaEvent sqe where sqe.completedDate is null and sqe.qaEvent.category = :param2) " +
							" or s.id IN " +
							"(select aqe.analysis.sampleItem.sample.id from AnalysisQaEvent aqe where aqe.completedDate is null and aqe.qaEvent.category = :param2 and " +
							//bugzilla 2300 exclude canceled tests
							"aqe.analysis.status NOT IN (:param3) and" +
							//make sure we only pick the max revision analyses that have qa events pending
							"(aqe.analysis.sampleItem.id, aqe.analysis.test.id, aqe.analysis.revision) IN (select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " +
							" group by b.sampleItem.id, b.test.id)) " +
							"order by s.accessionNumber";
				} else {
					sql = "from Sample s where s.id IN " +
							"(select sqe.sample.id from SampleQaEvent sqe where sqe.completedDate is null) " +
							" or s.id IN " +
							"(select aqe.analysis.sampleItem.sample.id from AnalysisQaEvent aqe where aqe.completedDate is null and " +
							//bugzilla 2300 exclude canceled tests
							"aqe.analysis.status NOT IN (:param3) and " +
							//make sure we only pick the max revision analyses that have qa events pending
							"(aqe.analysis.sampleItem.id, aqe.analysis.test.id, aqe.analysis.revision) IN (select b.sampleItem.id, b.test.id, max(b.revision) from Analysis b " +
							" group by b.sampleItem.id, b.test.id)) " +
							"order by s.accessionNumber";
				}
			}

			Query query = HibernateUtil.getSession().createQuery(sql);
			if (filterByDomain) {
				query.setParameter("param", sample.getDomain());
			}
			if (filterByQaEventCategory) {
				query.setParameter("param2", qaEventCategoryId);
			}

			List<String> statusesToExclude = new ArrayList<String>();
			statusesToExclude.add(SystemConfiguration.getInstance().getAnalysisStatusCanceled());
			query.setParameterList("param3", statusesToExclude);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			LogEvent.logError("SampleDAOImpl", "getSamplesWithPendingQaEvents()", e.toString());
			throw new LIMSRuntimeException("Error in Sample getSamplesWithPendingQaEvents()", e);
		}

		return list;
	}

	public List<Sample> getSamplesReceivedOn(String receivedDate) throws LIMSRuntimeException {
		//covers full day so handles time stamps
		return getSamplesReceivedInDateRange(receivedDate, receivedDate);
	}

	/**
	 * @see us.mn.state.health.lims.sample.dao.SampleDAO#getSamplesReceivedWithin(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Sample> getSamplesReceivedInDateRange(String receivedDateStart, String receivedDateEnd) throws LIMSRuntimeException {
		List<Sample> list = null;

		Calendar start = getCalendarForDateString(receivedDateStart);
		if (GenericValidator.isBlankOrNull(receivedDateEnd)) {
			receivedDateEnd = receivedDateStart;
		}
		Calendar end = getCalendarForDateString(receivedDateEnd);
		// worried about time stamps including time information, so might be missed comparing to midnight (00:00:00.00) on the last day of range.
		end.add(Calendar.DAY_OF_YEAR, 1);
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
		try {
			String sql = "from Sample as s where s.receivedTimestamp >= :start AND s.receivedTimestamp < :end";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setCalendarDate("start", start);
			query.setCalendarDate("end", end);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (HibernateException he) {
			LogEvent.logError("SampleDAOImpl", "getSamplesReceivedInDateRange()", he.toString());
			throw new LIMSRuntimeException("Error in Sample getSamplesReceivedInDateRange()", he);
		}
		return list;
	}


	@SuppressWarnings("unchecked")
	public List<Sample> getSamplesCollectedOn(String collectionDate) throws LIMSRuntimeException {
		List<Sample> list = null;

		Calendar calendar = getCalendarForDateString(collectionDate);

		try {
			String sql = "from Sample as sample where sample.collectionDate = :date";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setCalendarDate("date", calendar);
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (HibernateException he) {
			LogEvent.logError("SampleDAOImpl", "getSamplesRecievedOn()", he.toString());
			throw new LIMSRuntimeException("Error in Sample getSamplesRecievedOn()", he);
		}

		return list;
	}


	private Calendar getCalendarForDateString(String recievedDate) {
		String localeName = SystemConfiguration.getInstance().getDefaultLocale().toString();
		Locale locale = new Locale(localeName);
		Calendar calendar = Calendar.getInstance(locale);

		Date date = DateUtil.convertStringDateToSqlDate(recievedDate, localeName);
		calendar.setTime(date);
		return calendar;
	}

	@SuppressWarnings("unchecked")
	public List<Sample> getSamplesByProjectAndStatusIDAndAccessionRange(String projectId, List<Integer> inclusiveStatusIdList, String minAccession,
																		String maxAccession) throws LIMSRuntimeException {

		String sql = "select from Sample s where s.statusId in (:statusList) and " +
				"s.accessionNumber >= :minAccess and s.accessionNumber <= :maxAccess and " +
				"s.id in (select sp.sample.id from SampleProject sp where sp.project.id = :projectId)";
		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameterList("statusList", inclusiveStatusIdList);
			query.setInteger("projectId", Integer.parseInt(projectId));
			query.setString("minAccess", minAccession);
			query.setString("maxAccess", maxAccession);

			List<Sample> sampleList = query.list();

			closeSession();

			return sampleList;
		} catch (HibernateException e) {
			handleException(e, "getSamplesByProjectAndStatusIDAndAccessionRange");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Sample> getSamplesByAccessionRange(String minAccession, String maxAccession) throws LIMSRuntimeException {

		String sql = "select from Sample s where s.accessionNumber >= :minAccess and s.accessionNumber <= :maxAccess";
		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setString("minAccess", minAccession);
			query.setString("maxAccess", maxAccession);

			List<Sample> sampleList = query.list();

			closeSession();

			return sampleList;
		} catch (HibernateException e) {
			handleException(e, "getSamplesByAccessionRange");
		}

		return null;
	}

	public String getLargestAccessionNumber() throws LIMSRuntimeException {
		String greatestAccessionNumber = null;

		try {
			String sql = "select max(s.accessionNumber) from Sample s";
			Query query = HibernateUtil.getSession().createQuery(sql);

			greatestAccessionNumber = (String) query.uniqueResult();

		} catch (Exception e) {
			LogEvent.logError("SampleDAOImpl", "getLargestAccessionNumber()", e.toString());
			throw new LIMSRuntimeException("Exception occurred in SampleDAOImpl.getLargestAccessionNumber", e);
		}

		return greatestAccessionNumber;
	}

	public String getLargestAccessionNumberWithPrefix(String prefix) throws LIMSRuntimeException {
		String greatestAccessionNumber = null;

		try {
			String sql = "select max(s.accessionNumber) from Sample s where s.accessionNumber like :prefix";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("prefix", prefix + "%");
			greatestAccessionNumber = (String) query.uniqueResult();
		} catch (Exception e) {
			LogEvent.logError("SampleDAOImpl", "getLargestAccessionNumberWithPrefix()", e.toString());
			throw new LIMSRuntimeException("Exception occurred in SampleNumberDAOImpl.getLargestAccessionNumberWithPrefix", e);
		}

		return greatestAccessionNumber;
	}

	@Override
	public String getLargestAccessionNumberMatchingPattern(String startingWith, int accessionSize) throws LIMSRuntimeException {
		String greatestAccessionNumber = null;

		try {
			String sql = "select max(s.accessionNumber) from Sample s where s.accessionNumber LIKE :starts and length(s.accessionNumber) = :numberSize";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("starts", startingWith + "%");
			query.setInteger("numberSize", accessionSize);
			greatestAccessionNumber = (String) query.uniqueResult();
		} catch (Exception e) {
			handleException(e, "getLargestAccessionNumberMatchingPattern");
		}

		return greatestAccessionNumber;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sample> getSamplesWithPendingQaEventsByService(String serviceId) throws LIMSRuntimeException {
		String sql = "Select sqa.sample From SampleQaEvent sqa where sqa.sample.id IN (select sa.sample.id from SampleOrganization sa where sa.organization.id = :serviceId) ";

		try {
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("serviceId", Integer.parseInt(serviceId));
			List<Sample> samples = query.list();
			closeSession();
			return samples;
		} catch (HibernateException e) {
			handleException(e, "getSamplesWithPendingQaEventsByService");
		}
		return null;
	}

	@Override
	public Sample getSampleByUuidAndWithoutAccessionNumber(String uuid) {
		try {
			String sql = "from Sample as sample where sample.uuid = :uuid and sample.accessionNumber is null";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("uuid", uuid);
			return (Sample) query.uniqueResult();
		} catch (HibernateException he) {
			LogEvent.logErrorStack("SampleDAOImpl", "getSampleByUuidAndWithoutAccessionNumber(String uuid)", he);
			throw new LIMSRuntimeException("Error in Sample getSampleByUuidAndWithoutAccessionNumber(String uuid)", he);
		}
	}

	@Override
	public Sample getSampleByUuidAndSampleTypeIdAndWithoutAccessionNumber(String uuid, String sampleTypeId) {
		try {
			String sql = "select si.sample from SampleItem si inner join si.sample where si.sample.accessionNumber is null and si.sample.uuid = :uuid and si.typeOfSample = :sampleTypeId";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("uuid", uuid);
			query.setParameter("sampleTypeId", Integer.parseInt(sampleTypeId));
			return (Sample) query.uniqueResult();
		} catch (HibernateException he) {
			LogEvent.logErrorStack("SampleDAOImpl", "getSampleByUuidAndWithoutAccessionNumber(String uuid, String sampleTypeId)", he);
			throw new LIMSRuntimeException("Error in Sample getSampleByUuidAndWithoutAccessionNumber(String uuid, String sampleTypeId)", he);
		}
	}

	@Override
	public List<Sample> getAllSamplesByUuidAndWithAccessionNumber(String uuid) {
		try {
			String sql = "from Sample as sample where sample.uuid = :uuid and sample.accessionNumber is not null";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("uuid", uuid);
			return query.list();
		} catch (HibernateException he) {
			LogEvent.logErrorStack("SampleDAOImpl", "getAllSamplesByUuidAndWithAccessionNumber(String uuid)", he);
			throw new LIMSRuntimeException("Error in Sample getAllSamplesByUuidAndWithAccessionNumber(String uuid)", he);
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Sample> getSamplesByEncounterUuid(String uuid) {
        try {
            String sql = "from Sample as sample where sample.uuid = :uuid";
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setParameter("uuid", uuid);
            return query.list();
        } catch (HibernateException he) {
            LogEvent.logErrorStack("SampleDAOImpl", "getSamplesByEncounterUuid(String uuid)", he);
            throw new LIMSRuntimeException("Error in Sample getSamplesByEncounterUuid(String uuid)", he);

        }
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<Sample> getSamplesByUuidAndStatus(String uuid, int statusId) {
        try {
            String sql = "from Sample as sample where sample.uuid = :uuid and sample.statusId = :status";
            Query query = HibernateUtil.getSession().createQuery(sql);
            query.setParameter("uuid", uuid);
            query.setParameter("status", statusId);

            return query.list();
        } catch (HibernateException he) {
            LogEvent.logErrorStack("SampleDAOImpl", "getSamplesByEncounterUuid(String uuid)", he);
            throw new LIMSRuntimeException("Error in Sample getSamplesByEncounterUuid(String uuid)", he);

        }
    }


    @SuppressWarnings("unchecked")
	@Override
	public Sample getSampleByID(String id) {
		try{
			String sql = "from Sample as sample where sample.id = :id";
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("id", Integer.parseInt(id));
			return (Sample) query.uniqueResult();
		} catch(HibernateException he) {
			LogEvent.logErrorStack("SampleDAOImpl", "getSampleByID(String id)", he);
			throw new LIMSRuntimeException("Error in Sample getSampleByID(String id)", he);
		}
	}
}
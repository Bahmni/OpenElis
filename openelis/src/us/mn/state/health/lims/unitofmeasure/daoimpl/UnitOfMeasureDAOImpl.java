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
package us.mn.state.health.lims.unitofmeasure.daoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;

import org.hibernate.HibernateException;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSDuplicateRecordException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.unitofmeasure.dao.UnitOfMeasureDAO;
import us.mn.state.health.lims.unitofmeasure.valueholder.UnitOfMeasure;

/**
 * @author diane benz
 */
public class UnitOfMeasureDAOImpl extends BaseDAOImpl implements
		UnitOfMeasureDAO {

	public void deleteData(List unitOfMeasures) throws LIMSRuntimeException {
		//add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			for (int i = 0; i < unitOfMeasures.size(); i++) {
				UnitOfMeasure data = (UnitOfMeasure)unitOfMeasures.get(i);
			
				UnitOfMeasure oldData = (UnitOfMeasure)readUnitOfMeasure(data.getId());
				UnitOfMeasure newData = new UnitOfMeasure();

				String sysUserId = data.getSysUserId();
				String event = IActionConstants.AUDIT_TRAIL_DELETE;
				String tableName = "UNIT_OF_MEASURE";
				auditDAO.saveHistory(newData,oldData,sysUserId,event,tableName);
			}
		}  catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","AuditTrail deleteData()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure AuditTrail deleteData()", e);
		}  
		
		try {		
			for (int i = 0; i < unitOfMeasures.size(); i++) {
				UnitOfMeasure data = (UnitOfMeasure) unitOfMeasures.get(i);
				//bugzilla 2206
				data = (UnitOfMeasure)readUnitOfMeasure(data.getId());
				HibernateUtil.getSession().delete(data);
				HibernateUtil.getSession().flush();
				HibernateUtil.getSession().clear();
			}			
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","deleteData()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure deleteData()", e);
		} 
	}

	public boolean insertData(UnitOfMeasure unitOfMeasure) throws LIMSRuntimeException {
		try {
			// bugzilla 1482 throw Exception if record already exists
			if (duplicateUnitOfMeasureExists(unitOfMeasure)) {
				throw new LIMSDuplicateRecordException(
						"Duplicate record exists for "
								+ unitOfMeasure.getUnitOfMeasureName());
			}
			
			String id = (String)HibernateUtil.getSession().save(unitOfMeasure);
			unitOfMeasure.setId(id);
			
			//bugzilla 1824 inserts will be logged in history table
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = unitOfMeasure.getSysUserId();
			String tableName = "UNIT_OF_MEASURE";
			auditDAO.saveNewHistory(unitOfMeasure,sysUserId,tableName);
			
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();						
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","insertData()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure insertData()", e);
		}
		
		return true;
	}

	public void updateData(UnitOfMeasure unitOfMeasure) throws LIMSRuntimeException {
		// bugzilla 1482 throw Exception if record already exists
		try {
			if (duplicateUnitOfMeasureExists(unitOfMeasure)) {
				throw new LIMSDuplicateRecordException(
						"Duplicate record exists for "
								+ unitOfMeasure.getUnitOfMeasureName());
			}
		} catch (Exception e) {
    		//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","updateData()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure updateData()",
					e);
		}
		
		UnitOfMeasure oldData = (UnitOfMeasure)readUnitOfMeasure(unitOfMeasure.getId());
		UnitOfMeasure newData = unitOfMeasure;

		//add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = unitOfMeasure.getSysUserId();
			String event = IActionConstants.AUDIT_TRAIL_UPDATE;
			String tableName = "UNIT_OF_MEASURE";
			auditDAO.saveHistory(newData,oldData,sysUserId,event,tableName);
		}  catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","AuditTrail updateData()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure AuditTrail updateData()", e);
		}  
				
		try {
			HibernateUtil.getSession().merge(unitOfMeasure);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().evict(unitOfMeasure);
			HibernateUtil.getSession().refresh(unitOfMeasure);			
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","updateData()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure updateData()", e);
		}
	}

	public void getData(UnitOfMeasure unitOfMeasure) throws LIMSRuntimeException {
		try {
			UnitOfMeasure uom = (UnitOfMeasure)HibernateUtil.getSession().get(UnitOfMeasure.class, unitOfMeasure.getId());
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			if (uom != null) {
			  PropertyUtils.copyProperties(unitOfMeasure, uom);
			} else {
				unitOfMeasure.setId(null);
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","getData()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure getData()", e);
		}
	}

	public List getAllUnitOfMeasures() throws LIMSRuntimeException {
		List list = new Vector();
		try {
			String sql = "from UnitOfMeasure";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			//query.setMaxResults(10);
			//query.setFirstResult(3);				
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","getAllUnitOfMeasures()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure getAllUnitOfMeasures()", e);
		}

		return list;
	}

	public List getPageOfUnitOfMeasures(int startingRecNo) throws LIMSRuntimeException {
		List list = new Vector();
		try {
			// calculate maxRow to be one more than the page size
			int endingRecNo = startingRecNo + (SystemConfiguration.getInstance().getDefaultPageSize() + 1);
			
			//bugzilla 1399
			String sql = "from UnitOfMeasure u order by u.unitOfMeasureName";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(startingRecNo-1);
			query.setMaxResults(endingRecNo-1); 
					
			list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","getPageOfUnitOfMeasures()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure getPageOfUnitOfMeasures()", e);
		}

		return list;
	}

	public UnitOfMeasure readUnitOfMeasure(String idString) {
		UnitOfMeasure data = null;
		try {
			data = (UnitOfMeasure)HibernateUtil.getSession().get(UnitOfMeasure.class, idString);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","readUnitOfMeasure()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure readUnitOfMeasure()", e);
		}			
		
		return data;
	}
	
	public List getNextUnitOfMeasureRecord(String id)
			throws LIMSRuntimeException {

		return getNextRecord(id, "UnitOfMeasure", UnitOfMeasure.class);

	}

	public List getPreviousUnitOfMeasureRecord(String id)
			throws LIMSRuntimeException {

		return getPreviousRecord(id, "UnitOfMeasure", UnitOfMeasure.class);
	}

	public UnitOfMeasure getUnitOfMeasureByName(UnitOfMeasure unitOfMeasure) throws LIMSRuntimeException {
		try {
			String sql = "from UnitOfMeasure u where trim(lower(u.unitOfMeasureName)) = :param";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setParameter("param", unitOfMeasure.getUnitOfMeasureName().trim().toLowerCase());

			List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			UnitOfMeasure data = null;
			if ( list.size() > 0 )
				data = (UnitOfMeasure)list.get(0);
			
			return data;
			
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","getUnitOfMeasureByName()",e.toString());
			throw new LIMSRuntimeException("Error in UnitOfMeasure getUnitOfMeasureByName()", e);
		}

	}
	
	//bugzilla 1411
	public Integer getTotalUnitOfMeasureCount() throws LIMSRuntimeException {
		return getTotalCount("UnitOfMeasure", UnitOfMeasure.class);
	}
	
	//overriding BaseDAOImpl bugzilla 1427 pass in name not id
	public List getNextRecord(String id, String table, Class clazz) throws LIMSRuntimeException {	
				
		List list = new Vector();
		try {			
			String sql = "from "+table+" t where name >= "+ enquote(id) + " order by t.unitOfMeasureName";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(1);
			query.setMaxResults(2); 	
			
			list = query.list();		
			
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","getNextRecord()",e.toString());
			throw new LIMSRuntimeException("Error in getNextRecord() for " + table, e);
		}
		
		return list;		
	}

	//overriding BaseDAOImpl bugzilla 1427 pass in name not id
	public List getPreviousRecord(String id, String table, Class clazz) throws LIMSRuntimeException {		
		
		List list = new Vector();
		try {			
			String sql = "from "+table+" t order by t.unitOfMeasureName desc where name <= "+ enquote(id);
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(1);
			query.setMaxResults(2); 	
			
			list = query.list();					
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","getPreviousRecord()",e.toString());
			throw new LIMSRuntimeException("Error in getPreviousRecord() for " + table, e);
		} 

		return list;
	}
	
	// bugzilla 1482
	private boolean duplicateUnitOfMeasureExists(UnitOfMeasure unitOfMeasure) throws LIMSRuntimeException {
		try {

			// not case sensitive hemolysis and Hemolysis are considered
			// duplicates
			String sql = "from UnitOfMeasure t where trim(lower(t.unitOfMeasureName)) = :param and t.id != :param2";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(
					sql);
			query.setParameter("param", unitOfMeasure.getUnitOfMeasureName().trim());
	
			// initialize with 0 (for new records where no id has been generated
			// yet
			String unitOfMeasureId = "0";
			if (!StringUtil.isNullorNill(unitOfMeasure.getId())) {
				unitOfMeasureId = unitOfMeasure.getId();
			}
			query.setInteger("param2", Integer.parseInt(unitOfMeasureId));

			List list = query.list();

            return list.size() > 0;

		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("UnitOfMeasureDAOImpl","duplicateUnitOfMeasureExists()",e.toString());
			throw new LIMSRuntimeException(
					"Error in duplicateUnitOfMeasureExists()", e);
		}
	}

    public UnitOfMeasure getUnitOfMeasureById(String id) {
        try {
            UnitOfMeasure unitOfMeasure = (UnitOfMeasure)HibernateUtil.getSession().get(UnitOfMeasure.class, id);
            closeSession();
            return unitOfMeasure;
        } catch (HibernateException e) {
            handleException(e, "getDataById");
        }

        return null;
    }
}

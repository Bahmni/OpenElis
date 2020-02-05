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
package us.mn.state.health.lims.teststatus.daoimpl;

import java.util.List;


import org.hibernate.Query;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.teststatus.valueholder.TestStatus;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.teststatus.dao.TestStatusDAO;


public class TestStatusDAOImpl extends BaseDAOImpl implements TestStatusDAO {

	public boolean insertData(TestStatus testStatus) throws LIMSRuntimeException {
		try {
			String id = (String)HibernateUtil.getSession().save(testStatus);

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (Exception e) { 
			LogEvent.logError("TestStatusDAOImpl","insertData()",e.toString());
			throw new LIMSRuntimeException("Error in TestStatus insertData()",e);
		}

		return true;
	}


	public TestStatus getTestStatusByTestId(String testId)throws LIMSRuntimeException {
		TestStatus testStatus = null;
		try {
			String sql = "from TestStatus ts where ts.testId = :testId";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("testId", Integer.parseInt(testId));
			
			List list = query.list();

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

			if (list != null && list.size() > 0) {
				return (TestStatus) list.get(0);
			}
		} catch (Exception e) { 
			LogEvent.logError("TestStatusDAOImpl","getTestStatusByTestId()",e.toString());
			throw new LIMSRuntimeException("Error in TestStatus getTestStatusByTestId()", e);
		}

		return testStatus;
	}

	@Override
	public void insertOrUpdate(TestStatus testStatus) {
		TestStatus existingTestStatusRecord = getTestStatusByTestId(testStatus.getTestId());
		if(existingTestStatusRecord != null) {
			testStatus.setId(existingTestStatusRecord.getId());
			updateData(testStatus);
		} else {
			insertData(testStatus);
		}
	}

	@Override
	public void deleteData(TestStatus testStatus) {
		try {
			TestStatus data = (TestStatus) getTestStatusByTestId(testStatus.getTestId());
			if(data != null) {
				HibernateUtil.getSession().delete(data);
				HibernateUtil.getSession().flush();
				HibernateUtil.getSession().clear();
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("testStatusDAOImpl","deleteData()",e.toString());
			throw new LIMSRuntimeException("Error in TestStatus deleteData()",e);
		}
	}

	public void updateData(TestStatus newTestStatus) throws LIMSRuntimeException {
		try {
			HibernateUtil.getSession().merge(newTestStatus);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().evict(newTestStatus);
			HibernateUtil.getSession().refresh(newTestStatus);
		} catch (Exception e) {
			LogEvent.logError("testStatusDAOImpl", "updateData()", e.toString());
			throw new LIMSRuntimeException("Error in testStatus updateData()", e);
		}
	}
}

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
package us.mn.state.health.lims.testanalyte.daoimpl;

import org.hibernate.Session;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testanalyte.dao.TestAnalyteTestResultDAO;

import java.util.List;

/**
 * @author diane benz
 */
/**
 * @author diane benz
 */
public class TestAnalyteTestResultDAOImpl extends BaseDAOImpl implements TestAnalyteTestResultDAO {

    /*
     * (non-Javadoc)
     *
     * @see us.mn.state.health.lims.testanalyte.dao.TestAnalyteTestResultDAO#getPageOfTestAnalyteTestResults(int,
     *      us.mn.state.health.lims.test.valueholder.Test)
     */
	public List getPageOfTestAnalyteTestResults(int startingRecNo, Test test) throws LIMSRuntimeException {
		Session session = null;
		
		List testAnalyteTestResults;

		String testId = "0";

		if (test != null) {
			testId = test.getId();
		}

		try {
			session = HibernateUtil.getSession();
			
			// calculate maxRow to be one more than the page size
			int endingRecNo = startingRecNo + (SystemConfiguration.getInstance().getDefaultPageSize() + 1);

			//String query;
			if (!StringUtil.isNullorNill(testId)) {						
				String sql = "from TestAnalyteTestResult t where t.testId = :param";
				org.hibernate.Query query = session.createQuery(sql);
				query.setParameter("param", test.getId());
				
				query.setFirstResult(startingRecNo-1);
				query.setMaxResults(endingRecNo-1); 
						
				testAnalyteTestResults = query.list();
				session.flush();
				session.clear();
				
			} else {
				String sql = "from TestAnalyteTestResult";
				org.hibernate.Query query = session.createQuery(sql);
				query.setFirstResult(startingRecNo-1);
				query.setMaxResults(endingRecNo-1); 
						
				testAnalyteTestResults = query.list();
				session.flush();				
			}

		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("TestAnalyteTestResultDAOImpl","getPageOfTestAnalyteTestResults()",e.toString());
			throw new LIMSRuntimeException("Error in TestAnalyteTestResult getPageOfTestAnalyteTestResults()",e);
		}

		return testAnalyteTestResults;
	}
}

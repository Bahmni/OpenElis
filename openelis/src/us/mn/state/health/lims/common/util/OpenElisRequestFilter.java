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
package us.mn.state.health.lims.common.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Transaction;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import javax.servlet.*;
import java.io.IOException;

public class OpenElisRequestFilter implements Filter {
    private Logger logger = LogManager.getLogger(this.getClass());

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("UTF8");
        Transaction transaction = null;
        try {
            transaction = HibernateUtil.getSession().beginTransaction();
            chain.doFilter(request, response);
            Boolean hasFailed = (Boolean)request.getAttribute(IActionConstants.REQUEST_FAILED);
            if (hasFailed == null || !hasFailed) {
                commit(transaction);
            } else {
                rollback(transaction);
            }
        } catch (Throwable t) {
            rollback(transaction);
            logger.error("Exception in request ", t);
            throw t;
        } finally {
            HibernateUtil.closeSession();
        }

	}

    private void rollback(Transaction transaction) {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }

    private void commit(Transaction transaction) {
        if (transaction.isActive()) {
            transaction.commit();
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
	}
}

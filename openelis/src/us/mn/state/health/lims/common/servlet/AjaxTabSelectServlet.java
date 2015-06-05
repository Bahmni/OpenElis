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
package us.mn.state.health.lims.common.servlet;

import us.mn.state.health.lims.common.provider.data.BaseDataProvider;
import us.mn.state.health.lims.common.provider.data.DataProviderFactory;
import us.mn.state.health.lims.common.servlet.data.AjaxServlet;
import us.mn.state.health.lims.common.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author diane benz
 * bugzilla 2443
 */
public class AjaxTabSelectServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		process(request);
	}

	private void process(HttpServletRequest request) {
		String activeTab = request.getParameter("activeTab");
		request.getSession(true).setAttribute("activeTab", activeTab);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req);
	}
}

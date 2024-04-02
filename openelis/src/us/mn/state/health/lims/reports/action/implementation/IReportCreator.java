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
package us.mn.state.health.lims.reports.action.implementation;

import java.util.Map;
import java.util.List;

import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;

public interface IReportCreator {
    public String INCOMPLETE_PARAMS = "Incompleate parameters";
	public String INVALID_PARAMS = "Invalid parameters";
	public String SUCCESS = IActionConstants.FWD_SUCCESS;

	public void initializeReport(BaseActionForm dynaForm);
	public String getContentType();
	public String getResponseHeaderName();
	public String getResponseHeaderContent();
	public Map<String, Object> getReportParameters() throws IllegalStateException;
	public byte[] runReport( ) throws Exception;
	public void setReportPath( String path);
	public List<String> getReportedOrders();

}

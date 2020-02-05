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
package us.mn.state.health.lims.teststatus.valueholder;

import us.mn.state.health.lims.common.valueholder.BaseObject;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofteststatus.valueholder.TypeOfTestStatus;

public class TestStatus extends BaseObject {
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String testId;
	
	private String testStatusId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getTestStatusId() {
		return testStatusId;
	}

	public void setTestStatusId(String testStatusId) {
		this.testStatusId = testStatusId;
	}
}

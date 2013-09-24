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

package us.mn.state.health.lims.systemusermodule.valueholder;

import us.mn.state.health.lims.common.valueholder.BaseObject;
import us.mn.state.health.lims.common.valueholder.ValueHolder;
import us.mn.state.health.lims.common.valueholder.ValueHolderInterface;
import us.mn.state.health.lims.systemmodule.valueholder.SystemModule;

abstract public class PermissionModule extends BaseObject {

	private static final long serialVersionUID = 1L;

	private String id;
	private String hasSelect;
	private String hasAdd;
	private String hasUpdate;
	private String hasDelete;
	private String systemModuleId;

	private ValueHolderInterface systemModule;
	
	public PermissionModule() {
			super();
			this.systemModule = new ValueHolder();
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}

	public void setHasSelect(String hasSelect) {
		this.hasSelect = hasSelect;
	}
	public String getHasSelect() {
		return hasSelect;
	}
	
	public void setHasAdd(String hasAdd) {
		this.hasAdd = hasAdd;
	}
	public String getHasAdd() {
		return hasAdd;
	}
	
	public void setHasUpdate(String hasUpdate) {
		this.hasUpdate = hasUpdate;
	}
	public String getHasUpdate() {
		return hasUpdate;
	}

	public void setHasDelete(String hasDelete) {
		this.hasDelete = hasDelete;
	}
	public String getHasDelete() {
		return hasDelete;
	}

	protected void setSystemModuleHolder(ValueHolderInterface systemModule) {
		this.systemModule = systemModule;
	}

	protected ValueHolderInterface getSystemModuleHolder() {
		return this.systemModule;
	}

	public void setSystemModule(SystemModule systemModule) {
		this.systemModule.setValue(systemModule);
	}

	public SystemModule getSystemModule() {
		return (SystemModule) this.systemModule.getValue();
	}

	public void setSystemModuleId(String systemModuleId) {
		this.systemModuleId = systemModuleId;
	}
	public String getSystemModuleId() {
		return systemModuleId;
	}

	abstract public String getPermissionAgentId();
	
	abstract public PermissionAgent getPermissionAgent();
	
	abstract public void setPermissionAgent(PermissionAgent agent);
}

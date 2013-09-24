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

import us.mn.state.health.lims.common.valueholder.ValueHolder;
import us.mn.state.health.lims.common.valueholder.ValueHolderInterface;
import us.mn.state.health.lims.role.valueholder.Role;

/**
 * The primary purpose of this class is to make the code more literate.  It adds no new behavior
 * @author pauls
 *
 */
public class RoleModule extends PermissionModule {

	private static final long serialVersionUID = 1L;
	
	private String roleId;
	private ValueHolderInterface role;
	
	public RoleModule() {
		super();
		this.role = new ValueHolder();
	}

	protected void setRoleHolder(ValueHolderInterface role) {
		this.role = role;
	}

	protected ValueHolderInterface getRoleHolder() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role.setValue(role);
	}

	public Role getRole() {
		return (Role) this.role.getValue();
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleId() {
		return roleId;
	}

	@Override
	public String getPermissionAgentId() {
		return getRoleId();
	}

	@Override
	public PermissionAgent getPermissionAgent() {
		return getRole();
	}

	@Override
	public void setPermissionAgent(PermissionAgent agent) {
		setRole((Role)agent);		
	}	
	
	
	
	
	
	
	
}

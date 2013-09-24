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

package us.mn.state.health.lims.systemuser.valueholder;

public class UnifiedSystemUser {
	private static final String ID_SEPARATOR = "-";
	
	private String lastName;
	private String firstName;
	private String loginName;
	private String expDate;
	private String locked;
	private String disabled;
	private String active;
	private String timeout;
	private String systemUserId = " ";
	private String loginUserId = " ";
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getLocked() {
		return locked;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeOut) {
		this.timeout = timeOut;
	}
	public String getSystemUserId() {
		return systemUserId;
	}
	public void setSystemUserId(String systemUserId) {
		this.systemUserId = systemUserId;
	}
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}
	public String getLoginUserId() {
		return loginUserId;
	}
	
	public void setActive(String active) {
		this.active = active;
	}
	public String getActive() {
		return active;
	}
	public String getCombinedUserID(){
		return getSystemUserId() + ID_SEPARATOR + getLoginUserId();
	}
	
	public static String getSystemUserIDFromCombinedID( String combinedId){
		int separatorIndex = combinedId.indexOf(ID_SEPARATOR);
		
		return separatorIndex == 0 ? null : combinedId.substring(0, separatorIndex);
	}
	
	public static String getLoginUserIDFromCombinedID( String combinedId){
		int separatorIndex = combinedId.indexOf(ID_SEPARATOR);
		
		return separatorIndex == combinedId.length() - 1 ? null : combinedId.substring(separatorIndex + 1);
	}
}

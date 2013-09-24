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

package us.mn.state.health.lims.resultlimits.valueholder;

import us.mn.state.health.lims.common.valueholder.BaseObject;

public class ResultLimit extends BaseObject {

	private static final long serialVersionUID = 1L;

	private String id;
	private String testId;
	private String resultTypeId;
	private String gender;
	private double minAge = 0;
	private double maxAge = Double.POSITIVE_INFINITY;
	private double lowNormal = Double.NEGATIVE_INFINITY;
	private double highNormal = Double.POSITIVE_INFINITY;
	private double lowValid = Double.NEGATIVE_INFINITY;
	private double highValid = Double.POSITIVE_INFINITY;
	private String dictionaryNormalId;
	private boolean alwaysValidate;
	
	
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
	public String getResultTypeId() {
		return resultTypeId;
	}
	public void setResultTypeId(String resultTypeId) {
		this.resultTypeId = resultTypeId;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public double getMinAge() {
		return minAge;
	}
	public void setMinAge(double minAge) {
		this.minAge = minAge;
	}
	public double getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(double maxAge) {
		this.maxAge = maxAge;
	}
	public double getLowNormal() {
		return lowNormal;
	}
	public void setLowNormal(double lowNormal) {
		this.lowNormal = lowNormal;
	}
	public double getHighNormal() {
		return highNormal;
	}
	public void setHighNormal(double highNormal) {
		this.highNormal = highNormal;
	}
	public double getLowValid() {
		return lowValid;
	}
	public void setLowValid(double lowValid) {
		this.lowValid = lowValid;
	}
	public double getHighValid() {
		return highValid;
	}
	public void setHighValid(double highValid) {
		this.highValid = highValid;
	}
	
	public boolean ageLimitsAreDefault(){
		return minAge == 0 && maxAge == Double.POSITIVE_INFINITY;
	}
	public String getDictionaryNormalId() {
		return dictionaryNormalId;
	}
	public void setDictionaryNormalId(String dictionaryNormalId) {
		this.dictionaryNormalId = dictionaryNormalId;
	}
	public boolean isAlwaysValidate() {
		return alwaysValidate;
	}
	public void setAlwaysValidate(boolean alwaysValidate) {
		this.alwaysValidate = alwaysValidate;
	}
}

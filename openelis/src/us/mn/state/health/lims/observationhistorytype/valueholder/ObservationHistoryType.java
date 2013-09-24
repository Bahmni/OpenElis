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

package us.mn.state.health.lims.observationhistorytype.valueholder;

import java.sql.Timestamp;

import us.mn.state.health.lims.common.valueholder.BaseObject;
import us.mn.state.health.lims.common.valueholder.SimpleBaseEntity;

/**
 * DemographicHistoryType entity.
 * @author MyEclipse Persistence Tools
 * @author pahill 
 * @since 2010-04-09
 */

public class ObservationHistoryType extends	BaseObject implements SimpleBaseEntity<String> {
	private static final long serialVersionUID = 1L;
	
	// Fields

	private String id;
	private String typeName;
	private String description;

	// Constructors

	public ObservationHistoryType() {
	}

	/** minimal constructor */
	public ObservationHistoryType(String id, String typeName, String description) {
		this.setId(id);
		this.typeName = typeName;
		this.description = description;
	}

	/** full constructor */
	public ObservationHistoryType(String id, String typeName, String description,
			Timestamp lastupdated) {
		this.setId(id);
		this.typeName = typeName;
		this.description = description;
		this.setLastupdated(lastupdated);
	}

	// Property accessors
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

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

package us.mn.state.health.lims.patienttype.valueholder;

import us.mn.state.health.lims.common.valueholder.EnumValueItemImpl;


/**
 * This is an object that contains data related to the patient_type table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="patient_type"
 */

public abstract class BasePatientType  extends EnumValueItemImpl {

	
	private static final long serialVersionUID = -7636195859201443397L;
	public static String REF = "PatientType";
	public static String PROP_TYPE = "type";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_LASTUPDATED = "lastupdated";
	public static String PROP_ID = "id";


	// constructors
	public BasePatientType () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePatientType (String id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private String id;

	// fields
	private String type;
	private String description;	



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="id"
     */
	public String getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (String id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: type
	 */
	public String getType () {
		return type;
	}

	/**
	 * Set the value related to the column: type
	 * @param type the type value
	 */
	public void setType (String type) {
		this.type = type;
	}



	/**
	 * Return the value associated with the column: description
	 */
	public String getDescription () {
		return description;
	}

	/**
	 * Set the value related to the column: description
	 * @param description the description value
	 */
	public void setDescription (String description) {
		this.description = description;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof PatientType)) return false;
		else {
			PatientType patientType = (PatientType) obj;
			if (null == this.getId() || null == patientType.getId()) return false;
			else return (this.getId().equals(patientType.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}

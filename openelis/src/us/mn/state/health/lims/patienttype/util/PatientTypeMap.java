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

package us.mn.state.health.lims.patienttype.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patienttype.dao.PatientTypeDAO;
import us.mn.state.health.lims.patienttype.daoimpl.PatientTypeDAOImpl;
import us.mn.state.health.lims.patienttype.valueholder.PatientType;

public class PatientTypeMap {

	private static PatientTypeMap s_instance = null;
	private Map<String, String> m_map;
	
	
	public static PatientTypeMap getInstance(){
		
		if( s_instance == null){
			s_instance = new PatientTypeMap();
		}
		
		return s_instance;
	}
	
	/*
	 * Will force the a new fetch of the map and any new PatientIdentityTypes in the DB will be picked up
	 * 
	 * Expected user will be the code which inserts new types into the DB
	 */
	public static void reset(){
		s_instance = null;
	}
	
	@SuppressWarnings("unchecked")
	private PatientTypeMap(){
		m_map = new HashMap<String, String>();
		
		PatientTypeDAO patientTypeDAO = new PatientTypeDAOImpl();

		List<PatientType> patientTypes = patientTypeDAO.getAllPatientTypes();

		for( PatientType patientType : patientTypes){
			m_map.put(patientType.getType(), patientType.getId());
		}
	}
	
	public String getIDForType( String type){
		return m_map.get(type);
	}
	
	public String getIdentityValue(List<PatientIdentity> identityList, String type) {

		String id = getIDForType(type);

		if (!GenericValidator.isBlankOrNull(id)) {
			for (PatientIdentity identity : identityList) {
				if (id.equals(identity.getIdentityTypeId())) {
					return identity.getIdentityData();
				}
			}
		}
		return "";
	}
}

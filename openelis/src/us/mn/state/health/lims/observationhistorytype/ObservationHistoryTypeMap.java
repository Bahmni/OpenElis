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

package us.mn.state.health.lims.observationhistorytype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

import us.mn.state.health.lims.observationhistorytype.dao.ObservationHistoryTypeDAO;
import us.mn.state.health.lims.observationhistorytype.daoImpl.ObservationHistoryTypeDAOImpl;
import us.mn.state.health.lims.observationhistorytype.valueholder.ObservationHistoryType;

/**
 * Provides two way lookup of Observation History types from ID to Type and from type to ID.
 * @author pahill
 * @since 2010-04-12
 */
public class ObservationHistoryTypeMap {

	private static ObservationHistoryTypeMap s_instance = null;
	private final Map<String, String> type2Id;
	private final Map<String, String> id2Type;
	
	public static ObservationHistoryTypeMap getInstance(){
		
		if( s_instance == null){
			s_instance = new ObservationHistoryTypeMap();
		}		
		return s_instance;
	}
	
	/**
	 * Will force a new fetch of the map and any new PatientIdentityTypes in the DB will be picked up
	 * Expected user will be the code which inserts new types into the DB
	 */
	public static void reset(){
		s_instance = null;
	}
	
	private ObservationHistoryTypeMap(){
		type2Id = new HashMap<String, String>();
		id2Type = new HashMap<String, String>();
		
		ObservationHistoryTypeDAO dao = new ObservationHistoryTypeDAOImpl();
		List<ObservationHistoryType> list = dao.getAll();
		
		for( ObservationHistoryType item : list){
			mapNewItem(item);
		}
	}

	private void mapNewItem(ObservationHistoryType item) {
		type2Id.put(item.getTypeName(), item.getId());
		id2Type.put(item.getId(), item.getTypeName());
	}
	
	public String getIDForType( String type){
		if( GenericValidator.isBlankOrNull(type)){
			return null;
		}
		
		return type2Id.get(type);
	}

	public String getTypeFromId(String id) {
		if( GenericValidator.isBlankOrNull(id) ) {
			return null;
		}
		return id2Type.get(id);
	}	
}

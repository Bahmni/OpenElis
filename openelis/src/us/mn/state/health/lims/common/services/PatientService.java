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
* Copyright (C) ITECH, University of Washington, Seattle WA.  All Rights Reserved.
*
*/
package us.mn.state.health.lims.common.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

import us.mn.state.health.lims.address.daoimpl.AddressPartDAOImpl;
import us.mn.state.health.lims.address.daoimpl.PersonAddressDAOImpl;
import us.mn.state.health.lims.address.valueholder.AddressPart;
import us.mn.state.health.lims.address.valueholder.PersonAddress;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.patient.daoimpl.PatientDAOImpl;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;

public class PatientService {
	
	private static String PATIENT_GUID_IDENTITY;
	private static String PATIENT_NATIONAL_IDENTITY;
	private static String PATIENT_ST_IDENTITY;
	private static Map<String, String> addressPartIdToNameMap = new HashMap<String, String>();
	private PatientIdentityDAO patientIdentityDAO = new PatientIdentityDAOImpl();
	private DictionaryDAO dictionaryDAO = new DictionaryDAOImpl();
	private Patient patient;
	
	static{
		PatientIdentityType patientType = new PatientIdentityTypeDAOImpl().getNamedIdentityType("GUID");
		if( patientType != null){
			PATIENT_GUID_IDENTITY = patientType.getId();
		}
		
		patientType = new PatientIdentityTypeDAOImpl().getNamedIdentityType("NATIONAL");
		if( patientType != null){
			PATIENT_NATIONAL_IDENTITY = patientType.getId();
		}
		
		patientType = new PatientIdentityTypeDAOImpl().getNamedIdentityType("ST");
		if( patientType != null){
			PATIENT_ST_IDENTITY = patientType.getId();
		}
				
		List<AddressPart> parts = new AddressPartDAOImpl().getAll();
		
		for( AddressPart part : parts){
			addressPartIdToNameMap.put(part.getId(), part.getPartName());
		}
	}
	
	public PatientService(Patient patient){
		this.patient = patient;
		if (patient.getPerson() == null) {
			new PatientDAOImpl().getData(this.patient);
		}
	}

	public String getGUID(){
		return getIdentityInfo(PATIENT_GUID_IDENTITY);
	}
	
	public String getNationalId(){
		if( !GenericValidator.isBlankOrNull(patient.getNationalId())){
			return patient.getNationalId();
		}else{
			return getIdentityInfo(PATIENT_NATIONAL_IDENTITY);
		}
	}
	
	public String getSTNumber(){
		return getIdentityInfo(PATIENT_ST_IDENTITY);
	}

	private String getIdentityInfo(String identityId) {
		PatientIdentity identity = patientIdentityDAO.getPatitentIdentityForPatientAndType(patient.getId(), identityId);
		
		if( identity != null){
			return identity.getIdentityData();
		}else{
			return "";
		}
	}

	public String getFirstName(){
		return patient.getPerson().getFirstName();
	}
	
	public String getLastName(){
		return patient.getPerson().getLastName();
	}
	
	public String getGender(){
		return patient.getGender();
	}
	
	public Map<String, String> getAddressComponents(){
		String value;
		Map<String, String> addressMap = new HashMap<String, String>();
		
		List<PersonAddress> addressParts = new PersonAddressDAOImpl().getAddressPartsByPersonId(patient.getPerson().getId());
		
		for( PersonAddress parts : addressParts){
			if( "D".equals(parts.getType()) && !GenericValidator.isBlankOrNull(parts.getValue())){
				addressMap.put(addressPartIdToNameMap.get(parts.getAddressPartId()), dictionaryDAO.getDataForId(parts.getValue()).getDictEntry());
			}else{
				value = parts.getValue();
				addressMap.put(addressPartIdToNameMap.get(parts.getAddressPartId()), value == null ? "" : value.trim());
			}
		}
		
		value = patient.getPerson().getCity();
		addressMap.put("City", value == null ? "" : value.trim());
		value = patient.getPerson().getCountry();
		addressMap.put("Country", value == null ? "" : value.trim());
		value = patient.getPerson().getState();
		addressMap.put("State", value == null ? "" : value.trim());
		value = patient.getPerson().getStreetAddress();
		addressMap.put("Street", value == null ? "" : value.trim());
		value = patient.getPerson().getZipCode();
		addressMap.put("Zip", value == null ? "" : value.trim());
		
		return addressMap;
	}
	
	public String getDOB(){
			return patient.getBirthDateForDisplay();
	}
	
	public String getPhone(){
		String phone = patient.getPerson().getHomePhone();
		
		if(GenericValidator.isBlankOrNull(phone)){
			phone = patient.getPerson().getCellPhone();
		}
		
		return phone;
	}
}

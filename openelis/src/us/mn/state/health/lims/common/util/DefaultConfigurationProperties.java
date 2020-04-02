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
* Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
*
*/
package us.mn.state.health.lims.common.util;

import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultConfigurationProperties extends ConfigurationProperties {

	private static String propertyFile = "/SystemConfiguration.properties";
	private java.util.Properties properties = null;
	protected static Map<ConfigurationProperties.Property, KeyDefaultPair> propertiesFileMap  = new HashMap<ConfigurationProperties.Property, KeyDefaultPair>();
	protected static Map<String, ConfigurationProperties.Property> dbNamePropertiesMap  = new HashMap<String, ConfigurationProperties.Property>();
	private boolean databaseLoaded = false;

	{
		//config from SystemConfiguration.properties
		propertiesFileMap.put(Property.AmbiguousDateValue, new KeyDefaultPair("date.ambiguous.date.value", "01") );
		propertiesFileMap.put(Property.AmbiguousDateHolder , new KeyDefaultPair("date.ambiguous.date.holder", "X") );
		propertiesFileMap.put(Property.ReferingLabParentOrg , new KeyDefaultPair("organization.reference.lab.parent", null) );
		propertiesFileMap.put(Property.MenuName , new KeyDefaultPair("menu.name", "TabbedMenu") );
		propertiesFileMap.put(Property.MenuPermissions , new KeyDefaultPair("menu.permissions","") );
		propertiesFileMap.put(Property.MenuTopItems , new KeyDefaultPair("menu.topItems", "") );
		propertiesFileMap.put(Property.resultsResendTime , new KeyDefaultPair("results.send.retry.time", "30") );
/*		propertiesFileMap.put(Property. , new KeyDefaultPair() );

	*/
		//config from site_information table
		setDBPropertyMappingAndDefault(Property.SiteCode, "siteNumber", "" );
		setDBPropertyMappingAndDefault(Property.TrainingInstallation, "TrainingInstallation", "false");
		setDBPropertyMappingAndDefault(Property.PatientSearchURL, "patientSearchURL" , "");
		setDBPropertyMappingAndDefault(Property.PatientSearchUserName, "patientSearchLogOnUser" , "" );
		setDBPropertyMappingAndDefault(Property.PatientSearchPassword, "patientSearchPassword", "" );
		setDBPropertyMappingAndDefault(Property.UseExternalPatientInfo, "useExternalPatientSource" , "false");
		setDBPropertyMappingAndDefault(Property.labDirectorName, "lab director" , "");
		setDBPropertyMappingAndDefault(Property.allowLanguageChange, "allowLanguageChange", "true" );
		setDBPropertyMappingAndDefault(Property.resultReportingURL, "resultReportingURL", "");
		setDBPropertyMappingAndDefault(Property.reportResults, "resultReporting", "false");
		setDBPropertyMappingAndDefault(Property.malariaSurveillanceReportURL, "malariaSurURL", "");
		setDBPropertyMappingAndDefault(Property.malariaSurveillanceReport, "malariaSurReport", "false");
		setDBPropertyMappingAndDefault(Property.malariaCaseReport, "malariaCaseReport", "false");
		setDBPropertyMappingAndDefault(Property.malariaCaseReportURL, "malariaCaseURL", "");
		setDBPropertyMappingAndDefault(Property.testUsageReportingURL, "testUsageAggregationUrl", "");
		setDBPropertyMappingAndDefault(Property.testUsageReporting, "testUsageReporting", "false");
		setDBPropertyMappingAndDefault(Property.roleRequiredForModifyResults, "modify results role" , "false");
		setDBPropertyMappingAndDefault(Property.notesRequiredForModifyResults, "modify results note required", "false" );
		setDBPropertyMappingAndDefault(Property.resultTechnicianName, "ResultTechnicianName", "false");
		setDBPropertyMappingAndDefault(Property.autoFillTechNameBox, "autoFillTechNameBox", "false");
		setDBPropertyMappingAndDefault(Property.autoFillTechNameUser, "autoFillTechNameUser", "false");
		setDBPropertyMappingAndDefault(Property.failedValidationMarker, "showValidationFailureIcon", "true");
		setDBPropertyMappingAndDefault(Property.SiteName, "SiteName", "");
		setDBPropertyMappingAndDefault(Property.useLogoInReport, "useLogoInReport", "true");
		setDBPropertyMappingAndDefault(Property.PasswordRequirments , "passwordRequirements", "MINN");
		setDBPropertyMappingAndDefault(Property.FormFieldSet , "setFieldForm", IActionConstants.FORM_FIELD_SET_HAITI);
		setDBPropertyMappingAndDefault(Property.StringContext , "stringContext","");
		setDBPropertyMappingAndDefault(Property.StatusRules , "statusRules", "CI");
		setDBPropertyMappingAndDefault(Property.ReflexAction , "reflexAction", "Haiti");
		setDBPropertyMappingAndDefault(Property.AccessionFormat , "acessionFormat", "SITEYEARNUM"); //spelled wrong in DB
		setDBPropertyMappingAndDefault(Property.trackPatientPayment, "trackPayment", "false");
		setDBPropertyMappingAndDefault(Property.alertForInvalidResults, "alertWhenInvalidResult", "false");
		setDBPropertyMappingAndDefault(Property.defaultDateLocale, "default date locale", "fr-FR");
		setDBPropertyMappingAndDefault(Property.defaultLangLocale, "default language locale", "fr-FR");
		setDBPropertyMappingAndDefault(Property.configurationName, "configuration name", "not set");
		setDBPropertyMappingAndDefault(Property.CONDENSE_NFS_PANEL, "condenseNFS", "false");
		setDBPropertyMappingAndDefault(Property.patientDataOnResultsByRole, "roleForPatientOnResults", "false");
		setDBPropertyMappingAndDefault(Property.USE_PAGE_NUMBERS_ON_REPORTS, "reportPageNumbers", "true");
		setDBPropertyMappingAndDefault(Property.QA_SORT_EVENT_LIST, "sortQaEvents", "true");
		setDBPropertyMappingAndDefault(Property.ALWAYS_VALIDATE_RESULTS, "validate all results", "true");
		setDBPropertyMappingAndDefault(Property.ADDITIONAL_SITE_INFO, "additional site info", "");
		setDBPropertyMappingAndDefault(Property.RESULTS_PAGE_SIZE, "resultsPageSize", "10");
		setDBPropertyMappingAndDefault(Property.RESULTS_VALIDATION_PAGE_SIZE, "resultsValidationPageSize", "10");
        setDBPropertyMappingAndDefault(Property.ST_NUMBER_FORMAT, "stNumberFormat", "/([a-zA-Z].{2})(.*)/");
        setDBPropertyMappingAndDefault(Property.flagForShowingTestStatus, "flagForShowingTestStatus","false");
	}

	private void setDBPropertyMappingAndDefault(Property property, String dbName, String defaultValue) {
		dbNamePropertiesMap.put(dbName, property);
		propertiesValueMap.put(property, defaultValue);
	}

	protected DefaultConfigurationProperties(){
		loadFromPropertiesFile();
		loadSpecial();
	}


	protected void loadIfPropertyValueNeeded(Property property){
		if( !databaseLoaded && dbNamePropertiesMap.containsValue(property)){
			loadFromDatabase();
		}
	}

	protected void loadFromDatabase() {
		SiteInformationDAO siteInformationDAO = new SiteInformationDAOImpl();
		List<SiteInformation> siteInformationList = siteInformationDAO.getAllSiteInformation();

		for( SiteInformation siteInformation : siteInformationList){
			Property property = dbNamePropertiesMap.get(siteInformation.getName());
			if( property != null){
				propertiesValueMap.put(property, siteInformation.getValue());
			}
		}

		databaseLoaded = true;
	}

	protected void loadFromPropertiesFile() {
		InputStream propertyStream = null;

		try {
			propertyStream = this.getClass().getResourceAsStream(propertyFile);

			// Now load a java.util.Properties object with the properties
			properties = new java.util.Properties();

			properties.load(propertyStream);

		} catch (Exception e) {
			LogEvent.logError("DefaultConfigurationProperties","",e.toString());
		} finally {
			if (null != propertyStream) {
				try {
					propertyStream.close();
					propertyStream = null;
				} catch (Exception e) {
			        LogEvent.logError("DefaultConfigurationProperties","",e.toString());
				}
			}

		}

		for( Property property : propertiesFileMap.keySet()){
			KeyDefaultPair pair = propertiesFileMap.get(property);
			String value = properties.getProperty( pair.key, pair.defaultValue);
			propertiesValueMap.put(property, value);
		}
	}
	
	private void loadSpecial() {
		propertiesValueMap.put(Property.releaseNumber, Versioning.getReleaseNumber());
		propertiesValueMap.put(Property.buildNumber, Versioning.getBuildNumber());
	}

	protected class KeyDefaultPair{
		public final String key;
		public final String defaultValue;

		public KeyDefaultPair( String key, String defaultValue){
			this.key = key;
			this.defaultValue = defaultValue;
		}
	}
}

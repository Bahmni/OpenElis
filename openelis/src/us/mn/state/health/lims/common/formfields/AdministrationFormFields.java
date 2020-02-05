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
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.common.formfields;

import java.util.Map;

public class AdministrationFormFields {

	public static enum Field {
		ActionMenu,
		AnalyteMenu,
		AnalyzerTestNameMenu,
		CodeElementXref,
		CodeElementTypeMenu,
		CountyMenu,
		DictionaryMenu,
		DictionaryCategoryMenu,
		GenderMenu,
		LabelMenu,
		MethodMenu,
		OrganizationMenu,
		PanelMenu,
		PanelItemMenu,
		PatientTypeMenu,
		ProgramMenu,
		ProjectMenu,
		ProviderMenu,
		QaEventMenu,
		ReceiverCodeElementMenu,
		RegionMenu,
		ResultLimitsMenu,
		RoleMenu,
		SiteInformationMenu,
		SampleEntryMenu,
		ResultInformationMenu,
		SampleDomainMenu,
		ScriptletMenu,
		SourceOfSampleMenu,
		StatusOfSampleMenu,
		TestMenu,
		TestAnalyteMenu,
		TestReflexMenu,
		TestResultMenu,
		TestSectionMenu,
		TestTrailerMenu,
		TypeOfSampleMenu,
		TypeOfSamplePanelMenu,
		TypeOfSampleTestMenu,
		TypeOfTestResultMenu,
		UnitOfMeasureMenu,
		TestAnalyteTestResult,
		LoginUserMenu,
		SystemUserMenu,
		UserRoleMenu,
		SystemModuleMenu,
		SystemUserSectionMenu,
		SystemUserModuleMenu,
		UnifiedSystemUserMenu,
		OpenReports,
		TestUsageAggregatation,
		ResultReporting,
		PrintedReportsConfiguration,
		TypeOfTestStatusMenu
	}

	private static AdministrationFormFields instance = null;

	private Map<AdministrationFormFields.Field, Boolean> fields;

	private AdministrationFormFields() {
		fields = new DefaultAdministrationFormFields().getFieldFormSet();
	}

	public static AdministrationFormFields getInstance() {
		if (instance == null) {
			instance = new AdministrationFormFields();
		}

		return instance;
	}

	public boolean useField(AdministrationFormFields.Field field) {
		return fields.get(field);
	}
}

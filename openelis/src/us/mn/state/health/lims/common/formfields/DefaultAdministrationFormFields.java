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

import java.util.HashMap;

import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.formfields.AdministrationFormFields.Field;

public class DefaultAdministrationFormFields extends AAdministrationFormFields {
	private HashMap<AdministrationFormFields.Field, Boolean> defaultAttributes = new HashMap<AdministrationFormFields.Field, Boolean>();

	{
		defaultAttributes.put(Field.ActionMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.AnalyteMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.AnalyzerTestNameMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.CodeElementXref,  Boolean.FALSE);
		defaultAttributes.put(Field.CodeElementTypeMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.CountyMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.DictionaryMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.DictionaryCategoryMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.GenderMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.LabelMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.MethodMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.OrganizationMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.PanelMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.PanelItemMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.PatientTypeMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.ProgramMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.ProjectMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.ProviderMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.QaEventMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.ReceiverCodeElementMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.RegionMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.ResultLimitsMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.RoleMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.SiteInformationMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.SampleEntryMenu, Boolean.TRUE);
		defaultAttributes.put(Field.ResultInformationMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.SampleDomainMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.ScriptletMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.SourceOfSampleMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.StatusOfSampleMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TestMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TestAnalyteMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TestReflexMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TestResultMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TestSectionMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TestTrailerMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TypeOfSampleMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TypeOfSamplePanelMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TypeOfSampleTestMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TypeOfTestResultMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.UnitOfMeasureMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.TestAnalyteTestResult,  Boolean.FALSE);
		defaultAttributes.put(Field.LoginUserMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.SystemUserMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.UserRoleMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.SystemModuleMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.SystemUserSectionMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.SystemUserModuleMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.UnifiedSystemUserMenu,  Boolean.FALSE);
		defaultAttributes.put(Field.OpenReports, Boolean.FALSE);
		defaultAttributes.put(Field.TestUsageAggregatation, Boolean.FALSE);
		defaultAttributes.put(Field.ResultReporting, Boolean.FALSE);
		defaultAttributes.put(Field.PrintedReportsConfiguration, Boolean.TRUE);
		defaultAttributes.put(Field.TypeOfTestStatusMenu, Boolean.FALSE);
		
	}

	@Override
	protected HashMap<Field, Boolean> getDefaultAttributes() {
		return defaultAttributes;
	}

	@Override
	protected HashMap<Field, Boolean> getSetAttributes() {
		String fieldSet = ConfigurationProperties.getInstance().getPropertyValueUpperCase(Property.FormFieldSet);

		if(IActionConstants.FORM_FIELD_SET_LNSP_HAITI.equals(fieldSet)){
			return new LNSPHaitiAdministrationFormFields().getImplementationAttributes();
		}else if(IActionConstants.FORM_FIELD_SET_HAITI.equals(fieldSet)){
			return new HaitiAdministrationFormFields().getImplementationAttributes();
		}else if(IActionConstants.FORM_FIELD_SET_LNSP_CI.equals(fieldSet)){
			return new LNSP_CIAdministrationFormFields().getImplementationAttributes();
		}else if(IActionConstants.FORM_FIELD_SET_CDI.equals(fieldSet)){
			return new RETROCIAdministrationFormFields().getImplementationAttributes();
		}else if(IActionConstants.FORM_FIELD_SET_CI_IPCI.equals(fieldSet)){
			return new CI_IPCIAdministrationFormFields().getImplementationAttributes();
		} else if(IActionConstants.FORM_FIELD_SET_BAHMNI.equals(fieldSet)) {
            return new BahmniAdministrativeFormFields().getImplementationAttributes();
        }

		return null;
	}
}

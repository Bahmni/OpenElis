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

import us.mn.state.health.lims.common.formfields.FormFields.Field;

import java.util.HashMap;

public class BahmniFormFields implements IFormFieldsForImplementation {

	public HashMap<Field, Boolean> getImplementationAttributes() {
		HashMap<Field, Boolean> settings = new HashMap<Field, Boolean>();
		settings.put(Field.OrgState, Boolean.FALSE);
		settings.put(Field.ZipCode, Boolean.FALSE);
		settings.put(Field.MLS, Boolean.FALSE);
		settings.put(Field.OrganizationCLIA, Boolean.FALSE);
		settings.put(Field.OrganizationParent, Boolean.FALSE);
		settings.put(Field.InlineOrganizationTypes, Boolean.TRUE);
		settings.put(Field.DepersonalizedResults, Boolean.FALSE);
		settings.put(Field.OrgLocalAbrev, Boolean.FALSE);
		settings.put(Field.OrganizationShortName, Boolean.TRUE);
		settings.put(Field.OrganizationMultiUnit, Boolean.FALSE);
		settings.put(Field.OrganizationOrgId, Boolean.FALSE);
		settings.put(Field.AddressCity, Boolean.FALSE);
		settings.put(Field.AddressCommune, Boolean.FALSE);
		settings.put(Field.AddressDepartment, Boolean.FALSE);
		settings.put(Field.AddressVillage, Boolean.FALSE);
		settings.put(Field.PatientRequired, Boolean.TRUE);
		settings.put(Field.DynamicAddress, Boolean.TRUE);
        settings.put(Field.SampleCondition, Boolean.TRUE);
        settings.put(Field.NON_CONFORMITY_SITE_LIST, Boolean.TRUE);
        settings.put(Field.ValueHozSpaceOnResults, Boolean.TRUE);
        settings.put(Field.FirstNameFirst,Boolean.TRUE);
        settings.put(Field.NationalID,Boolean.FALSE);
        settings.put(Field.ResultsReferral, Boolean.TRUE);
        settings.put(Field.PatientType, Boolean.FALSE);
        settings.put(Field.UseSampleSource, Boolean.TRUE);
        settings.put(Field.AllowEditOrRemoveTests, Boolean.TRUE);
        settings.put(Field.MothersName, Boolean.FALSE);
        settings.put(Field.SupportPrimaryRelative, Boolean.TRUE);
        settings.put(Field.StNumber, Boolean.TRUE);
        settings.put(Field.CollectionDate, Boolean.FALSE);
		return settings;
	}



}

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

import us.mn.state.health.lims.common.formfields.AdministrationFormFields.Field;

public class HaitiAdministrationFormFields implements IAdministrationFormFieldsForImplementation {

	public HashMap<AdministrationFormFields.Field, Boolean> getImplementationAttributes() {
		HashMap<AdministrationFormFields.Field, Boolean> settings = new HashMap<AdministrationFormFields.Field, Boolean>();

		settings.put(Field.OrganizationMenu,  Boolean.TRUE);
		settings.put(Field.SiteInformationMenu,  Boolean.TRUE);
		settings.put(Field.ResultInformationMenu,  Boolean.TRUE);
		settings.put(Field.TestUsageAggregatation, Boolean.TRUE);
		settings.put(Field.ResultReporting, Boolean.TRUE);
		

		return settings;
	}

}

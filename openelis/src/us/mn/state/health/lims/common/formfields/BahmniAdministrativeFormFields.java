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

package us.mn.state.health.lims.common.formfields;

import java.util.HashMap;

public class BahmniAdministrativeFormFields implements IAdministrationFormFieldsForImplementation {

    @Override
    public HashMap<AdministrationFormFields.Field, Boolean> getImplementationAttributes() {
        HashMap<AdministrationFormFields.Field, Boolean> settings = new HashMap<AdministrationFormFields.Field, Boolean>();
        settings.put(AdministrationFormFields.Field.TestMenu, Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.PanelMenu, Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.PanelItemMenu, Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.UnitOfMeasureMenu, Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.TypeOfSampleMenu, Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.TypeOfSampleTestMenu, Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.TypeOfSamplePanelMenu, Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.ResultLimitsMenu, Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.OrganizationMenu,  Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.TestResultMenu,  Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.DictionaryMenu,  Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.TestSectionMenu,  Boolean.TRUE);
        settings.put(AdministrationFormFields.Field.SiteInformationMenu,  Boolean.TRUE);
        return settings;
    }
}

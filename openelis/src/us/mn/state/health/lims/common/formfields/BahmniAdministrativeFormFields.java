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
        return settings;

    }
}

package us.mn.state.health.lims.patient.action.bean;

import java.util.ArrayList;
import java.util.List;

public class AddressParts {

    private List<AddressPartForm> addressPartForms;

    public AddressParts() {
        this.addressPartForms = new ArrayList<AddressPartForm>();
    }

    public List<AddressPartForm> getAddressPartForms() {
        return addressPartForms;
    }

    public AddressPartForm getAddressPartForm(int index) {
        return addressPartForms.get(index);
    }

    public void setAddressPartForms(List<AddressPartForm> addressPartForms) {
        this.addressPartForms = addressPartForms;
    }

    public void addAddressPart(AddressPartForm addressPart){
        addressPartForms.add(addressPart);
    }
}

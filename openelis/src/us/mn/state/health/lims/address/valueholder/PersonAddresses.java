package us.mn.state.health.lims.address.valueholder;

import java.util.ArrayList;
import java.util.List;

public class PersonAddresses extends ArrayList<PersonAddress> {
    public PersonAddresses(List<PersonAddress> personAddresses) {
        super(personAddresses);
    }

    public PersonAddress findByPartName(String partName, AddressParts addressParts) {
        AddressPart addressPart = addressParts.find(partName);
        for (PersonAddress personAddress : this) {
            if (addressPart.getId().equals(personAddress.getAddressPartId())) {
                return personAddress;
            }
        }
        return null;
    }
}
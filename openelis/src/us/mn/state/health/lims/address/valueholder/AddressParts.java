package us.mn.state.health.lims.address.valueholder;

import java.util.ArrayList;
import java.util.Collection;

public class AddressParts extends ArrayList<AddressPart> {
    public AddressParts(Collection<? extends AddressPart> c) {
        super(c);
    }

    public AddressPart findByName(String partName) {
        for (AddressPart addressPart : this) {
            if (addressPart.getPartName().equals(partName))
                return addressPart;
        }
        return null;
    }

    public AddressPart findById(String id) {
        for (AddressPart addressPart : this) {
            if (addressPart.getId().equals(id))
                return addressPart;
        }
        return null;
    }
}
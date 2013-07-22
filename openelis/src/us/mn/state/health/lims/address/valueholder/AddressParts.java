package us.mn.state.health.lims.address.valueholder;

import java.util.ArrayList;
import java.util.Collection;

public class AddressParts extends ArrayList<AddressPart> {
    public AddressParts(Collection<? extends AddressPart> c) {
        super(c);
    }

    public AddressPart find(String partName) {
        for (AddressPart addressPart : this) {
            if (addressPart.getPartName().equals(partName))
                return addressPart;
        }
        return null;
    }
}
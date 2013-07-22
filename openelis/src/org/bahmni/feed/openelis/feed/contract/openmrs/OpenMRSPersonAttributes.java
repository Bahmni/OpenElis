package org.bahmni.feed.openelis.feed.contract.openmrs;

import java.util.ArrayList;
import java.util.Collection;

public class OpenMRSPersonAttributes extends ArrayList<OpenMRSPersonAttribute> {
    public OpenMRSPersonAttributes() {
    }

    public OpenMRSPersonAttributes(Collection<? extends OpenMRSPersonAttribute> c) {
        super(c);
    }
}
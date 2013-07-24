package org.bahmni.feed.openelis.feed.contract.openmrs;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPersonAttributes extends ArrayList<OpenMRSPersonAttribute> {
    public OpenMRSPersonAttributes() {
    }

    public OpenMRSPersonAttributes(Collection<? extends OpenMRSPersonAttribute> c) {
        super(c);
    }
}
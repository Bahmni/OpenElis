package org.bahmni.feed.openelis.feed.contract.openmrs.encounter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSOrderType {
    private String uuid;
    private String name;
    private boolean isRetired;

    public String getName() {
        return name;
    }

    public boolean isRetired() {
        return isRetired;
    }
}

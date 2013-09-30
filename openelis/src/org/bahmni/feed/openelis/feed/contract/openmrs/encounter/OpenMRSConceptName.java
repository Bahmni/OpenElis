package org.bahmni.feed.openelis.feed.contract.openmrs.encounter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSConceptName {
    private String name;

    public String getName() {
        return name;
    }
}

package org.bahmni.feed.openelis.feed.contract.openmrs.encounter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSOrder {
    private String uuid;
    private OpenMRSOrderType orderType;
    private OpenMRSConcept concept;

    public String getUuid() {
        return uuid;
    }

    public OpenMRSOrderType getOrderType() {
        return orderType;
    }

    public OpenMRSConcept getConcept() {
        return concept;
    }
}

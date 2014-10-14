package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceDataAllSamples {
    private String id;
    private Date dateCreated;
    private Date lastUpdated;
    private String shortName;
    private String description;
    private String name;
    private Boolean isActive;
    private List<ReferenceDataSample> samples= new ArrayList<>();

    public String getId() {
        return id;
    }

    public List<ReferenceDataSample> getSamples() {
        return samples;
    }
}

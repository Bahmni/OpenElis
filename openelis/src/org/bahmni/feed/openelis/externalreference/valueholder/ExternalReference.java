package org.bahmni.feed.openelis.externalreference.valueholder;


import us.mn.state.health.lims.common.valueholder.BaseObject;

public class ExternalReference extends BaseObject{

    private String id;
    private String itemId;
    private String externalId;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package org.bahmni.feed.openelis.feed.contract.bahmnireferencedata;

public class MinimalResource {
    private String uuid;
    private String name;

    public MinimalResource(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public MinimalResource() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

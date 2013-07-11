package org.bahmni.feed.openelis.event.object;

/**
 * Created with IntelliJ IDEA.
 * User: indraneel
 * Date: 11/7/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class LabObject {
    private String externalId;
    private String name;
    private String description;
    private String sysUserId;

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public LabObject (){}

    public LabObject(String externalId, String name, String description, String sysUserId) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
        this.sysUserId = sysUserId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

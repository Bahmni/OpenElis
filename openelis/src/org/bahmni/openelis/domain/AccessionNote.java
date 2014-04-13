package org.bahmni.openelis.domain;

public class AccessionNote {
    private String note;
    private String providerUuid;
    private String dateTime;

    public AccessionNote() {
    }

    public AccessionNote(String note, String providerUuid, String dateTime) {
        this.note = note;
        this.providerUuid = providerUuid;
        this.dateTime = dateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getProviderUuid() {
        return providerUuid;
    }

    public void setProviderUuid(String providerUuid) {
        this.providerUuid = providerUuid;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

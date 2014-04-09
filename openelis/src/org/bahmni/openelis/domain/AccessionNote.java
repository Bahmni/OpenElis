package org.bahmni.openelis.domain;

public class AccessionNote {
    private String note;
    private String providerUuid;

    public AccessionNote() {
    }

    public AccessionNote(String note, String providerUuid) {
        this.note = note;
        this.providerUuid = providerUuid;
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
}

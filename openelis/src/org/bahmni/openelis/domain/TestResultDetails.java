package org.bahmni.openelis.domain;

import us.mn.state.health.lims.note.valueholder.Note;

import java.util.ArrayList;
import java.util.List;

public class TestResultDetails {
    private String orderId;
    private String accessionNumber;
    private String patientExternalId;
    private String patientFirstName;
    private String patientLastName;
    private String testName;
    private String testUnitOfMeasurement;
    private String testExternalId;
    private String resultId;
    private Double minNormal;
    private Double maxNormal;
    private String result;
    private String alerts;
    private List<String> notes = new ArrayList<>();
    private String resultType;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getPatientExternalId() {
        return patientExternalId;
    }

    public void setPatientExternalId(String patientExternalId) {
        this.patientExternalId = patientExternalId;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestUnitOfMeasurement() {
        return testUnitOfMeasurement;
    }

    public void setTestUnitOfMeasurement(String testUnitOfMeasurement) {
        this.testUnitOfMeasurement = testUnitOfMeasurement;
    }

    public String getTestExternalId() {
        return testExternalId;
    }

    public void setTestExternalId(String testExternalId) {
        this.testExternalId = testExternalId;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAlerts() {
        return alerts;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        if(notes != null) {
            this.notes = notes;
        }
    }

    public void addNotes(String note) {
        this.notes.add(note);
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultType() {
        return resultType;
    }

    public Double getMinNormal() {
        return minNormal;
    }

    public void setMinNormal(Double minNormal) {
        this.minNormal = minNormal;
    }

    public Double getMaxNormal() {
        return maxNormal;
    }

    public void setMaxNormal(Double maxNormal) {
        this.maxNormal = maxNormal;
    }
}

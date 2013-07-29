package org.bahmni.feed.openelis.feed.service;

public interface PatientPublisherService {
    void publish(String patientIdentity, String contextPath);
}

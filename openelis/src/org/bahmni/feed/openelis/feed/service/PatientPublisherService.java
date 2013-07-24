package org.bahmni.feed.openelis.feed.service;

import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;

public interface PatientPublisherService {
    void publish(PatientIdentity patient);
}

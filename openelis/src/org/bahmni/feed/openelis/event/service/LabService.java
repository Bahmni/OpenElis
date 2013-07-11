package org.bahmni.feed.openelis.event.service;

import org.bahmni.feed.openelis.event.object.LabObject;
import org.ict4h.atomfeed.client.domain.Event;

public interface LabService {

    void save(LabObject object);

}

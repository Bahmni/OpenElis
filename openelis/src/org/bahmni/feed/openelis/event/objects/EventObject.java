package org.bahmni.feed.openelis.event.objects;

import org.ict4h.atomfeed.client.domain.Event;

public interface EventObject {

    void save(Event event);

}

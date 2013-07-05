package org.bahmni.feed.openelis.event.objects.impl;

import org.bahmni.feed.openelis.event.objects.EventObject;
import org.ict4h.atomfeed.client.domain.Event;

public class EmptyObject implements EventObject {
    @Override
    public void save(Event event) {

    }
}

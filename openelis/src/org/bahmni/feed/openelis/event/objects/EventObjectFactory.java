package org.bahmni.feed.openelis.event.objects;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.event.objects.impl.EmptyObject;
import org.bahmni.feed.openelis.event.objects.impl.LabPanel;
import org.bahmni.feed.openelis.event.objects.impl.LabTest;


public class EventObjectFactory {

    public static EventObject getEventObjectInstance(String type,AtomFeedProperties properties){

        if(properties.getProductTypeLabTest().equals(type)){
            return  new LabTest();
        }
        else if(properties.getProductTypePanel().equals(type)){
            return  new LabPanel();
        }
        return  new EmptyObject();
    }
}

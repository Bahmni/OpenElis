package org.bahmni.feed.openelis.event.objects;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.event.objects.impl.EmptyObject;
import org.bahmni.feed.openelis.event.objects.impl.LabPanel;
import org.bahmni.feed.openelis.event.objects.impl.LabTest;
import org.bahmni.feed.openelis.utils.AtomfeedClientUtils;


public class EventObjectFactory {

    public static EventObject getEventObjectInstance(String type,AtomFeedProperties properties){

        if(properties.getProductTypeLabTest().equals(type)){
            return  new LabTest(AtomfeedClientUtils.getSysUserId());
        }
        else if(properties.getProductTypePanel().equals(type)){
            return  new LabPanel(AtomfeedClientUtils.getSysUserId());
        }
        return  new EmptyObject();
    }

}

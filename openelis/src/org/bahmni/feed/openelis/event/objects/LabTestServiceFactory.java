package org.bahmni.feed.openelis.event.objects;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.event.objects.impl.EmptyObject;
import org.bahmni.feed.openelis.event.objects.impl.LabPanelService;
import org.bahmni.feed.openelis.event.objects.impl.LabTestService;
import org.bahmni.feed.openelis.utils.AtomfeedClientUtils;


public class LabTestServiceFactory {

    public static EventObject getLabTestService(String type, AtomFeedProperties properties){

        if(properties.getProductTypeLabTest().equals(type)){
            return  new LabTestService(AtomfeedClientUtils.getSysUserId());
        }
        else if(properties.getProductTypePanel().equals(type)){
            return  new LabPanelService(AtomfeedClientUtils.getSysUserId());
        }
        return  new EmptyObject();
    }

}

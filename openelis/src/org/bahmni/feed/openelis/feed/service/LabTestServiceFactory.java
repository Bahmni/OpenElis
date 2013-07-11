package org.bahmni.feed.openelis.feed.service;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.service.impl.EmptyService;
import org.bahmni.feed.openelis.feed.service.impl.LabPanelService;
import org.bahmni.feed.openelis.feed.service.impl.LabTestService;

import java.io.IOException;


public class LabTestServiceFactory {

    public static LabService getLabTestService(String type, AtomFeedProperties properties) throws IOException {
        if(properties.getProductTypeLabTest().equals(type)){
            return  new LabTestService();
        }
        else if(properties.getProductTypePanel().equals(type)){
            return  new LabPanelService();
        }
        return  new EmptyService();
    }
}

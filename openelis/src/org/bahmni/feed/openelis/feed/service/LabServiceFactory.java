/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package org.bahmni.feed.openelis.feed.service;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.service.impl.EmptyService;
import org.bahmni.feed.openelis.feed.service.impl.LabPanelService;
import org.bahmni.feed.openelis.feed.service.impl.LabTestService;

import java.io.IOException;


public class LabServiceFactory {

    public static LabService getLabService(String type, AtomFeedProperties properties) throws IOException {
        if(properties.getProductTypeLabTest().equals(type)){
            return  new LabTestService();
        }
        else if(properties.getProductTypePanel().equals(type)){
            return  new LabPanelService();
        }
        return  new EmptyService();
    }
}

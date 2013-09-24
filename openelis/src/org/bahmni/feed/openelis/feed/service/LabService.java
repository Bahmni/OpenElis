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

import org.bahmni.feed.openelis.feed.domain.LabObject;

import java.io.IOException;

public abstract class LabService {

    public void process(LabObject labObject) throws Exception {
        if (labObject.getStatus().equals("deleted"))
            delete(labObject);
        else
            save(labObject);
    }

    protected abstract void delete(LabObject labObject) throws IOException;

    protected abstract void save(LabObject labObject) throws Exception;
}

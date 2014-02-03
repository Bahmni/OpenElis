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

import org.bahmni.feed.openelis.feed.contract.openerp.OpenERPLab;

import java.io.IOException;

public abstract class LabService {

    public void process(OpenERPLab openERPLab) throws Exception {
        if (openERPLab.getStatus().equals("deleted"))
            delete(openERPLab);
        else
            save(openERPLab);
    }

    protected abstract void delete(OpenERPLab openERPLab) throws IOException;

    protected abstract void save(OpenERPLab openERPLab) throws Exception;
}

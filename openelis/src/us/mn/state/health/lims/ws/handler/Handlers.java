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

package us.mn.state.health.lims.ws.handler;

import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;

import java.util.Arrays;
import java.util.List;

public class Handlers {
    private List<Handler> handlers;

    public Handlers() {
        this(Arrays.<Handler>asList(
            new PatientHandler(new BahmniPatientService()),
            new AccessionHandler()
        ));
    }

    public Handlers(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public Handler getHandler(String resourceName) {
        if (resourceName == null) return null;

        for (Handler handler : handlers) {
            if (handler.canHandle(resourceName)) return handler;
        }

        return null;
    }
}

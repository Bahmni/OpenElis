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

package us.mn.state.health.lims.ws;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import us.mn.state.health.lims.ws.handler.Handler;
import us.mn.state.health.lims.ws.handler.Handlers;
import us.mn.state.health.lims.ws.handler.WebServicesInput;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResourceRetrieveAction extends WebServiceAction {

    private final String APPLICATION_JSON = "application/json";
    private Handlers handlers;
    private final Logger logger = Logger.getLogger(this.getClass());

    public ResourceRetrieveAction() {
        this(new Handlers());
    }

    public ResourceRetrieveAction(Handlers handlers) {
        this.handlers = handlers;
    }

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        WebServicesInput input = new WebServicesInput(urlOf(request));
        Handler handler = handlers.getHandler(input.getResourceName());

        if (anyAreNull(handler, input.getResourceName(), input.getResourceUuid())) {
            logger.error("Could not retrieve handler for url " + urlOf(request));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        Object result = handler.handle(input.getResourceUuid());

        if (result == null) {
            logger.error("Could not find " + input.getResourceName() + " with id " + input.getResourceUuid());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        response.setContentType(APPLICATION_JSON);
        ObjectMapperRepository.objectMapper.writeValue(response.getWriter(), result);

        return null;
    }

    private String urlOf(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    private boolean anyAreNull(Object... objects) {
        boolean isNull = false;
        for (Object object : objects) {
            isNull = isNull || (object == null);
        }
        return isNull;
    }
}

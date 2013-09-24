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

public class WebServicesInput {
    private final String url;
    private final String URL_PATTERN = ".*/ws/rest/([^/]*)/([^/]*).*";
    private final String RESOURCE_NAME_LOCATION = "$1";
    private final String RESOURCE_UUID_LOCATION = "$2";

    public WebServicesInput(String url) {
        this.url = trimSpacesAndTrailingSlash(url);
    }

    public String getResourceName() {
        return matchingValueIfUrlIsGood(RESOURCE_NAME_LOCATION);
    }

    public String getResourceUuid() {
        return matchingValueIfUrlIsGood(RESOURCE_UUID_LOCATION);
    }

    private String matchingValueIfUrlIsGood(String location) {
        return urlMatchesPattern() ? matchingValueAt(location) : null;
    }

    private String matchingValueAt(String location) {
        return url.replaceAll(URL_PATTERN, location);
    }

    private boolean urlMatchesPattern() {
        return url.matches(URL_PATTERN);
    }

    private String trimSpacesAndTrailingSlash(String request) {
        request = request.trim();
        if (request.endsWith("/")) request = request.substring(0, request.length() -1);
        return request;
    }
}

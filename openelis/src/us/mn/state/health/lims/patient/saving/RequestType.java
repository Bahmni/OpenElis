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

package us.mn.state.health.lims.patient.saving;

/**
 * In the context of saving & editing by project, a request type is not the type of the HttpServletRequest, but the reason the user is using the form.
 **/   
public enum RequestType {
	UNKNOWN, INITIAL, VERIFY,
    READWRITE, READONLY;
    
    /**
     * Convert a possible improperly cased string to one of the RequestTypes
     * @param anyCase
     * @return never null; something UNKNOWN
     */
	public static RequestType valueOfAsUpperCase(String anyCase) {
        RequestType rt = null;
        if (anyCase != null) {
            rt = RequestType.valueOf(anyCase.toUpperCase());
        }
        return (rt == null)?UNKNOWN:rt;
	}
}

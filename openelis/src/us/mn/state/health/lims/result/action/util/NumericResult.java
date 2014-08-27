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
package us.mn.state.health.lims.result.action.util;

import org.apache.struts.action.ActionErrors;
import us.mn.state.health.lims.common.util.validator.ActionError;

import java.util.ArrayList;
import java.util.List;

public class NumericResult {
    private static final String SPECIAL_CASE = "XXXX";
    private String value;

    public NumericResult(String value) {
        this.value = value;
    }

    public List<ActionError> validate() {
        List<ActionError> errors = new ArrayList<>();
        if( value.equals(SPECIAL_CASE)){
            return errors;
        }
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            errors.add(new ActionError("errors.number.format", new StringBuilder("Result")));
        }
        return errors;
    }
}

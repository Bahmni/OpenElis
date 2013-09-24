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

package us.mn.state.health.lims.common.action;

import us.mn.state.health.lims.common.exception.LIMSRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ActionFactory {
    public static BaseAction newAction(BaseAction baseAction) {
        try {
            Class<? extends BaseAction> aClass = baseAction.getClass();
            Constructor<? extends BaseAction> constructor = aClass.getConstructor();
            BaseAction newBaseAction = constructor.newInstance();
            newBaseAction.setState(baseAction);
            return newBaseAction;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new LIMSRuntimeException(e);
        }
    }
}

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

import junit.framework.Assert;
import org.apache.struts.action.ActionServlet;
import org.junit.Test;

public class ActionFactoryTest {
    @Test
    public void newAction() {
        ActionServlet actionServlet = new ActionServlet();
        String currentUserId = "foo";

        TestBaseAction testBaseAction = new TestBaseAction(currentUserId, actionServlet);
        BaseAction baseAction = ActionFactory.newAction(testBaseAction);
        Assert.assertNotNull(baseAction);

        Assert.assertEquals(currentUserId, testBaseAction.currentUserId);
        Assert.assertEquals(actionServlet, testBaseAction.getServlet());
    }
}

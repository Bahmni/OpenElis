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
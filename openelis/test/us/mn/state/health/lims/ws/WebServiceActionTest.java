package us.mn.state.health.lims.ws;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Ignore
public class WebServiceActionTest {

    @Mock
    private ActionMapping actionMapping;

    @Mock
    private ActionForm actionForm;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    public void shouldNotAuthorizeRequestsNotHavingAuthorizationHeader() throws Exception {
        WebServiceAction webServiceAction = new WebServiceAction();
        webServiceAction.execute(actionMapping, actionForm, httpServletRequest, httpServletResponse);
    }
}
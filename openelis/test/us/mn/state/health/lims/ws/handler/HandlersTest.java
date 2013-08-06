package us.mn.state.health.lims.ws.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HandlersTest {

    @Mock
    private Handler mockHandler;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void shouldReturnHandlerIfPresent() {
        when(mockHandler.canHandle("resource")).thenReturn(true);

        Handlers handlers = new Handlers(Arrays.asList(mockHandler));
        Handler resource = handlers.getHandler("resource");

        assertEquals(mockHandler, resource);
    }

    @Test
    public void shouldReturnNullIfNoMatchingHandlerAvailable() {
        when(mockHandler.canHandle("resource")).thenReturn(true);

        Handlers handlers = new Handlers(Arrays.asList(mockHandler));
        Handler resource = handlers.getHandler("someRandomString");

        assertNull(resource);
    }

    @Test
    public void shouldReturnNullIfNullPassedIn() {
        when(mockHandler.canHandle(null)).thenThrow(new RuntimeException("Should not reach here. Test fail"));

        Handlers handlers = new Handlers(Arrays.asList(mockHandler));
        Handler resource = handlers.getHandler(null);

        assertNull(resource);
    }
}

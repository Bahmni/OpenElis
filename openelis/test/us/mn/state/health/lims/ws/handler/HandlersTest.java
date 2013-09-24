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

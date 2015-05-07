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

import org.bahmni.feed.openelis.feed.service.impl.AccessionService;
import org.bahmni.openelis.domain.AccessionDetail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class AccessionHandlerTest {

    @Mock
    private AccessionService accessionService;

    @Before
    public void before() {
       initMocks(this);
    }

    @Test
    public void shouldHandleAccessionResource(){
        assertTrue(new AccessionHandler().canHandle("accession"));
        assertFalse(new AccessionHandler().canHandle("something"));
    }

    @Test(expected = Exception.class)
    public void shouldFailIfResourceNameIsNull(){
        new AccessionHandler().canHandle(null);
    }

    @Test
    public void shouldDelegateCallToAccessionService(){
        AccessionDetail expectedAccessionDetail = new AccessionDetail();
        String uuid = "uuid";
        when(accessionService.getAccessionDetailFor(uuid)).thenReturn(expectedAccessionDetail);
        AccessionDetail accessionDetails = new AccessionHandler(accessionService).handle(uuid);
        assertEquals(expectedAccessionDetail, accessionDetails);
    }
}

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

package us.mn.state.health.lims.feed;

import org.junit.Test;

import static junit.framework.Assert.*;

public class AtomFeedUrlParserTest {

    @Test
    public void shouldParseUrlToGetCategory() {
        assertEquals("patient", new AtomFeedUrlParser("http://host/openelis/ws/feed/patient/recent").getCategory());
        assertEquals("foo", new AtomFeedUrlParser("http://host/openelis/ws/feed/foo/1").getCategory());
        assertEquals("labtest", new AtomFeedUrlParser("http://host/openelis/ws/feed/labtest/recent").getCategory());
        assertNull(new AtomFeedUrlParser("http://host/openelis/ws/feed/recent").getCategory());
        assertNull(new AtomFeedUrlParser("http://host/openelis/ws/feed/1").getCategory());
    }

    @Test
    public void shouldGetFeedMarkerIfPresent() {
        assertEquals(2, new AtomFeedUrlParser("http://host/openelis/ws/feed/patient/2").getFeedMarker());
        assertEquals(0, new AtomFeedUrlParser("http://host/openelis/ws/feed/patient/recent").getFeedMarker());
        assertEquals(2, new AtomFeedUrlParser("http://host/openelis/ws/feed/2").getFeedMarker());
        assertEquals(0, new AtomFeedUrlParser("http://host/openelis/ws/feed/recent").getFeedMarker());
        assertEquals(2, new AtomFeedUrlParser("http://host/openelis/ws/feed/2/").getFeedMarker());
    }

    @Test
    public void shouldReturnTrueIfUrlIsForRecent() {
        assertFalse(new AtomFeedUrlParser("http://host/openelis/ws/feed/patient/2").isForRecentFeed());
        assertTrue(new AtomFeedUrlParser("http://host/openelis/ws/feed/patient/recent").isForRecentFeed());
        assertTrue(new AtomFeedUrlParser("http://host/openelis/ws/feed/patient/recent/").isForRecentFeed());
        assertFalse(new AtomFeedUrlParser("http://host/openelis/ws/feed/2").isForRecentFeed());
        assertFalse(new AtomFeedUrlParser("http://host/openelis/ws/feed/2/").isForRecentFeed());
        assertTrue(new AtomFeedUrlParser("http://host/openelis/ws/feed/recent").isForRecentFeed());
        assertTrue(new AtomFeedUrlParser("http://host/openelis/ws/feed/recent/").isForRecentFeed());
    }
}

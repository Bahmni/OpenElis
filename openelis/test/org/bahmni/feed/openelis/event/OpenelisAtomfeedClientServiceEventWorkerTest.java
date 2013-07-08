package org.bahmni.feed.openelis.event;

import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
//import org.springframework.test.context.ContextConfiguration;

//@ContextConfiguration(locations = {"classpath*:applicationContext-openelisTest.xml"})
public class OpenelisAtomfeedClientServiceEventWorkerTest {
    static final String EVENT_CONTENT = " {\"category\": \"panel\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";

    OpenelisAtomfeedClientServiceEventWorker eventWorker;
    @Before
    public void setUp(){
   //     eventWorker = new OpenelisAtomfeedClientServiceEventWorker("hosts/feed/recent");
    }

    @Test
    public void testProcess() throws Exception {
        eventWorker.process(createEvent());
    }

    private Event createEvent(){
        Event event = new Event("4234332",EVENT_CONTENT);
        return event;
    }
}

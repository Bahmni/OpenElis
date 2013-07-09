package org.bahmni.feed.openelis.event;


import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.event.objects.EventObject;
import org.bahmni.feed.openelis.event.objects.EventObjectFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import us.mn.state.health.lims.panel.valueholder.Panel;

import java.io.IOException;
import java.util.HashMap;

public class OpenelisAtomfeedClientServiceEventWorker implements EventWorker {

    private String feedUrl;

    public OpenelisAtomfeedClientServiceEventWorker(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public void process(Event event) {
        try {
            EventObject eventObject =  EventObjectFactory.getEventObjectInstance(getCategory(event), new AtomFeedProperties());;
            eventObject.save(event);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getCategory(Event event) throws IOException {
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;
        return (String) paramMap.get("category");
    }
    /*private OpenERPRequest mapRequest(Event event) throws IOException {
        List<Parameter> parameters = new ArrayList<Parameter>();

        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;

        parameters.add(createParameter("name",(String)paramMap.get("name"),"string"));
        parameters.add(createParameter("ref",(String)paramMap.get("ref"),"string"));
        parameters.add(createParameter("village", (String)paramMap.get("village"), "string"));

        parameters.add(createParameter("category", "create.customer", "string"));
        parameters.add(createParameter("feed_uri", feedUrl, "string"));
        parameters.add(createParameter("last_read_entry_id", event.getId(), "string"));
        parameters.add(createParameter("feed_uri_for_last_read_entry", event.getFeedUri(), "string"));

        return new OpenERPRequest("atom.event.worker","process_event",parameters);
    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }*/

}

package org.bahmni.feed.openelis.event.objects.impl;

import org.bahmni.feed.openelis.event.objects.EventObject;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;

import java.io.IOException;
import java.util.HashMap;


public class LabPanel implements EventObject {

    private PanelDAO panel = new PanelDAOImpl();

    @Override
    public void save(Event event) {
        try {
            panel.insertData(mapToPanel(event));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private Panel mapToPanel(Event event) throws IOException {
        Panel panel = new us.mn.state.health.lims.panel.valueholder.Panel();
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;
        panel.setDescription((String) paramMap.get("description"));
        panel.setPanelName((String) paramMap.get("name"));
        panel.setId(String.valueOf( paramMap.get("id")));

        return panel;
    }
}

package org.bahmni.feed.openelis.event.objects.impl;

import org.bahmni.feed.openelis.event.objects.EventObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.HashMap;


public class LabPanel extends TransactionalEventObject  implements EventObject {

    private PanelDAO panel = new PanelDAOImpl();

    private String sysUserId;

    public LabPanel(String sysUserId){
        this.sysUserId = sysUserId;
    }

    protected void saveEvent(Event event) throws IOException {
        panel.insertData(mapToPanel(event));
    }


    private Panel mapToPanel(Event event) throws IOException {
        Panel panel = new us.mn.state.health.lims.panel.valueholder.Panel();
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;

        panel.setPanelName((String) paramMap.get("name"));
        String desc = (String) paramMap.get("description");
        if(desc == null || desc.isEmpty()){
            desc = (String) paramMap.get("name");
        }
        panel.setDescription(desc);
        panel.setId(String.valueOf(paramMap.get("id")));
        panel.setSysUserId(sysUserId);

        return panel;
    }

    void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }
}

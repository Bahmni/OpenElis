package org.bahmni.feed.openelis.event.objects.impl;


import org.bahmni.feed.openelis.event.objects.EventObject;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import java.io.IOException;
import java.util.HashMap;

public class LabTest implements EventObject{
    private TestDAO testDAO = new TestDAOImpl() ;

    @Override
    public void save(Event event) {
        try {
            testDAO.insertData(mapToTest(event));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private Test mapToTest(Event event) throws IOException {
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;
        Test test = new Test();
        test.setName((String) paramMap.get("name"));
        test.setDescription((String) paramMap.get("description"));
        test.setId(String.valueOf( paramMap.get("id")));
        return test;
    }
}

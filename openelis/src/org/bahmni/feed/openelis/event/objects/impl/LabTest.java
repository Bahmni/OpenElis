package org.bahmni.feed.openelis.event.objects.impl;


import org.bahmni.feed.openelis.event.objects.EventObject;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import java.io.IOException;
import java.util.HashMap;

public class LabTest extends TransactionalEventObject {
    private String sysUserId;
    private String externalId;
    private TestDAO testDAO = new TestDAOImpl() ;
    private ExternalReferenceDao externalReferenceDao = new ExternalReferenceDaoImpl();


    public LabTest(String sysUserId){
        this.sysUserId = sysUserId;
    }

    @Override
    protected void saveEvent(Event event) throws IOException {
        Test test = mapToTest(event);
        ExternalReference data = externalReferenceDao.getData(externalId);
        if(data ==null)
            testDAO.insertData(test);
        if(data !=null) {
            Test activeTestById = testDAO.getActiveTestById(new Integer(data.getItemId()));
            test.setId(activeTestById.getId());
            testDAO.updateData(test);
        }
    }

    private Test mapToTest(Event event) throws IOException {
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;
        Test test = new Test();
        test.setName((String) paramMap.get("name"));
        String desc = (String) paramMap.get("description");
        if(desc == null || desc.isEmpty()){
            desc = (String) paramMap.get("name");
        }
        test.setDescription(desc);
        test.setId(String.valueOf( paramMap.get("id")));
        test.setSysUserId(sysUserId);
        return test;
    }

    void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }
}

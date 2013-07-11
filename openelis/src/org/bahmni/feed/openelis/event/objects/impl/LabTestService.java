package org.bahmni.feed.openelis.event.objects.impl;


import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

import java.io.IOException;
import java.util.HashMap;

public class LabTestService extends TransactionalEventObject {
    private String sysUserId;
    private String externalId;
    private TestDAO testDAO = new TestDAOImpl() ;
    private ExternalReferenceDao externalReferenceDao = new ExternalReferenceDaoImpl();


    public LabTestService(String sysUserId){
        this.sysUserId = sysUserId;
    }

    LabTestService(TestDAO testDao, ExternalReferenceDao externalReferenceDao) {
        testDAO = testDao;
        this.externalReferenceDao = externalReferenceDao;
    }

    @Override
    protected void saveEvent(Event event) throws IOException {
        Test test = mapToTest(event);
        ExternalReference data = externalReferenceDao.getData(externalId);
        if(data ==null) {
            testDAO.insertData(test);
            data = new ExternalReference(Long.parseLong(test.getId()),externalId,"panel");
            externalReferenceDao.insertData(data)  ;
        }
        else {
            Test activeTestById = testDAO.getActiveTestById((int)data.getItemId());
            test.setId(activeTestById.getId());
            updateTestFieldsIfNotEmpty(test, activeTestById);
            testDAO.updateData(test);
        }
    }

    private Test mapToTest(Event event) throws IOException {
        HashMap<String,Object> paramMap = new ObjectMapper().readValue(event.getContent(), HashMap.class) ;
        Test test = new Test();
        test.setTestName((String) paramMap.get("name"));
        String desc = (String) paramMap.get("description");
        if(desc == null || desc.isEmpty()){
            desc = (String) paramMap.get("name");
        }
        test.setDescription(desc);
        externalId = String.valueOf( paramMap.get("id"));
        test.setSysUserId(sysUserId);
        return test;
    }

    private void updateTestFieldsIfNotEmpty(Test test, Test testById) {
        if(isSet(test.getName())){
            testById.setName(test.getName());
        }
        if(isSet(test.getDescription())){
            testById.setDescription(test.getDescription());
        }
        if(isSet(test.getSysUserId())){
            testById.setSysUserId(test.getSysUserId());
        }
    }

    private boolean isSet(String value){
        return value != null && !value.isEmpty();
    }
    void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }
}

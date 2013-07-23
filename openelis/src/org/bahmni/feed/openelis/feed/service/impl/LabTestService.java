package org.bahmni.feed.openelis.feed.service.impl;


import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.bahmni.feed.openelis.feed.service.LabService;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;

import java.io.IOException;

public class LabTestService implements LabService {

    public static final String SECTION_NEW = "New";
    private TestDAO testDAO = new TestDAOImpl() ;
    private ExternalReferenceDao externalReferenceDao = new ExternalReferenceDaoImpl();
    private String labProductType;
    private TestSectionDAO sectionDAO = new TestSectionDAOImpl();

    public LabTestService(){
        labProductType = AtomFeedProperties.getInstance().getProductTypeLabTest();
    }

    LabTestService(TestDAO testDao, ExternalReferenceDao externalReferenceDao, TestSectionDAO testSectionDAO) {
        testDAO = testDao;
        this.externalReferenceDao = externalReferenceDao;
    }

    @Override
    public void save(LabObject labObject) throws IOException {
        Test test = mapToTest(labObject);
        ExternalReference data = externalReferenceDao.getData(labObject.getExternalId(),labObject.getCategory());
        if(data ==null) {
            testDAO.insertData(test);
            updateSection(test);
            if(isNotEmpty(test)){
                data = new ExternalReference(Long.parseLong(test.getId()),labObject.getExternalId(),labProductType);
            }
            externalReferenceDao.insertData(data)  ;
        }
        else {
            Test activeTestById = testDAO.getActiveTestById((int)data.getItemId());
            test.setId(activeTestById.getId());
            updateTestFieldsIfNotEmpty(test, activeTestById);
            testDAO.updateData(activeTestById);
        }
    }

    private void updateSection(Test test) {
        String sysUserId = test.getSysUserId();
        test = testDAO.getTestByName(test.getTestName());
        test.setSysUserId(sysUserId);

        TestSection section = sectionDAO.getTestSectionByName(SECTION_NEW);
        if(section != null){
            test.setTestSection(section);
            test.setTestSectionName(section.getTestSectionName());
        }
        testDAO.updateData(test);
    }

    private boolean isNotEmpty(Test test) {
        return test.getId() != null && !test.getId().isEmpty();
    }

    private Test mapToTest(LabObject labObject) throws IOException {
        Test test = new Test();
        test.setTestName(labObject.getName());
        String desc = labObject.getDescription();
        if(desc == null || desc.isEmpty()){
            desc = labObject.getDescription();
        }
        test.setDescription(desc);
        test.setSysUserId(labObject.getSysUserId());

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

}



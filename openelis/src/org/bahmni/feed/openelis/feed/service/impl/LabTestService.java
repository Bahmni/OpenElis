package org.bahmni.feed.openelis.feed.service.impl;


import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.bahmni.feed.openelis.feed.service.LabService;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;

import java.io.IOException;

public class LabTestService extends LabService {

    public static final String SECTION_NEW = "New";
    private TestDAO testDAO = new TestDAOImpl() ;
    private ExternalReferenceDao externalReferenceDao;
    private TestSectionDAO testSectionDAO;
    private String labProductType;

    public LabTestService(){
        labProductType = AtomFeedProperties.getInstance().getProductTypeLabTest();
        externalReferenceDao = new ExternalReferenceDaoImpl();
        testSectionDAO = new TestSectionDAOImpl();

    }

    LabTestService(TestDAO testDao, ExternalReferenceDao externalReferenceDao, TestSectionDAO testSectionDAO) {
        testDAO = testDao;
        this.externalReferenceDao = externalReferenceDao;
        this.testSectionDAO = testSectionDAO;
    }

    @Override
    public void save(LabObject labObject) throws IOException {
        Test test = mapToTest(labObject);
        ExternalReference data = externalReferenceDao.getData(labObject.getExternalId(),labObject.getCategory());
        if(data ==null) {
            testDAO.insertData(test);
            if(hasBeenSaved(test)){
                data = new ExternalReference(Long.parseLong(test.getId()),labObject.getExternalId(),labProductType);
            }
            externalReferenceDao.insertData(data)  ;
        }
        else {
            Test activeTestById = testDAO.getTestById(String.valueOf(data.getItemId()));
            updateTestFieldsIfNotEmpty(test, activeTestById);
            testDAO.updateData(activeTestById);
        }
    }

    @Override
    protected void delete(LabObject labObject) {
        ExternalReference externalReference = getexternalReference(labObject);
        if(externalReference != null){
            org.hibernate.Transaction tx = HibernateUtil.getSession().beginTransaction();
            externalReferenceDao.deleteData(externalReference);
            String testId = String.valueOf(externalReference.getItemId());
            testDAO.deleteTestById(testId, labObject.getSysUserId());
            tx.commit();
        }
    }

    private ExternalReference getexternalReference(LabObject labObject) {
        return externalReferenceDao.getData(labObject.getExternalId(),labObject.getCategory());
    }

    private boolean hasBeenSaved(Test test) {
        String testId = test.getId();
        return test != null && testId != null && !testId.isEmpty();
    }

    private Test mapToTest(LabObject labObject) throws IOException {
        Test test = new Test();
        test.setTestName(labObject.getName());
        String description = labObject.getDescription();
        if(description == null || description.isEmpty()){
            description = labObject.getName();
        }
        test.setDescription(description);
        test.setSysUserId(labObject.getSysUserId());
        setActiveStatus(test, labObject.getStatus());
        updateSection(test);
        return test;
    }

    private void setActiveStatus(Test test, String status) {
        if(status == null || status.isEmpty() )
            return;
        test.setIsActive(status.equalsIgnoreCase("active") ? IActionConstants.YES : IActionConstants.NO);
    }

    private void updateSection(Test test) {
        TestSection section = testSectionDAO.getTestSectionByName(SECTION_NEW);
        test.setTestSection(section);
    }

    private void updateTestFieldsIfNotEmpty(Test test, Test testById) {
        if(isSet(test.getTestName())){
            testById.setTestName(test.getTestName());
        }
        if(isSet(test.getDescription())){
            testById.setDescription(test.getDescription());
        }
        if(isSet(test.getSysUserId())){
            testById.setSysUserId(test.getSysUserId());
        }
        if(isSet(test.getIsActive())){
            testById.setIsActive(test.getIsActive());
        }
    }

    private boolean isSet(String value){
        return value != null && !value.isEmpty();
    }
}



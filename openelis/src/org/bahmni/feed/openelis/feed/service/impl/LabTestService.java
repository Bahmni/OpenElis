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

package org.bahmni.feed.openelis.feed.service.impl;


import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.bahmni.feed.openelis.feed.service.LabService;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;

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
        TypeOfSampleUtil.clearTestCache();
    }

    @Override
    protected void delete(LabObject labObject) {
        ExternalReference externalReference = getExternalReference(labObject);
        if(externalReference != null){
            externalReferenceDao.deleteData(externalReference);
            String testId = String.valueOf(externalReference.getItemId());
            testDAO.deleteById(testId, labObject.getSysUserId());
        }
        TypeOfSampleUtil.clearTestCache();
    }

    private ExternalReference getExternalReference(LabObject labObject) {
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
        test.setOrderable(true);
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



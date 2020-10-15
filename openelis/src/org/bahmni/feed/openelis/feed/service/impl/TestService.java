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


import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.MinimalResource;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTest;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.hibernate.Transaction;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.exception.LIMSException;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.test.valueholder.TestSection;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.util.TypeOfSampleUtil;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.CodedTestAnswer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

public class TestService {

    public static final String CATEGORY_TEST = "Test";
    public static final String CATEGORY_TEST_CODED_ANS = "CodedAns";
    public static final String DUMMY_TEST_SECTION_NAME = "New";
    private DictionaryDAO dictionaryDao;
    private AuditingService auditingService;
    private TestDAO testDAO;
    private TestResultService testResultService;
    private ExternalReferenceDao externalReferenceDao;
    private TestSectionDAO testSectionDAO;
    private UnitOfMeasureService unitOfMeasureService;

    public TestService() {
        this.testDAO = new TestDAOImpl();
        this.testResultService = new TestResultService();
        this.externalReferenceDao = new ExternalReferenceDaoImpl();
        this.testSectionDAO = new TestSectionDAOImpl();
        this.auditingService = new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl());
        this.unitOfMeasureService = new UnitOfMeasureService();
        this.dictionaryDao = new DictionaryDAOImpl();
    }

    /**
     * Exposed a constructor which takes all params for unit-testing purposes.
     */
    public TestService(ExternalReferenceDao externalReferenceDao,
                       TestDAO testDAO,
                       TestResultService testResultService,
                       TestSectionDAO testSectionDAO,
                       AuditingService auditingService,
                       TypeOfSampleDAO typeOfSampleDAO,
                       TypeOfSampleTestDAO typeOfSampleTestDAO,
                       DictionaryDAO dictionaryDao) {

        this.externalReferenceDao = externalReferenceDao;
        this.testDAO = testDAO;
        this.testResultService = testResultService;
        this.testSectionDAO = testSectionDAO;
        this.auditingService = auditingService;
        this.dictionaryDao = dictionaryDao;

    }

    public void createOrUpdate(ReferenceDataTest referenceDataTest) throws IOException, LIMSException {
        try {
            String sysUserId = auditingService.getSysUserId();
            ExternalReference data = externalReferenceDao.getData(referenceDataTest.getId(), CATEGORY_TEST);
            Test test = new Test();

            if (data == null) {
                test = populateTest(test, referenceDataTest, sysUserId, null);
                testDAO.insertData(test);
                saveExternalReference(referenceDataTest, test);
            } else {
                test = testDAO.getTestById(String.valueOf(data.getItemId()));
                String uuid = test.getTestSection() != null ? test.getTestSection().getUUID() : null;
                populateTest(test, referenceDataTest, sysUserId, uuid);
                testDAO.updateData(test);
            }
            if (referenceDataTest.getResultType().equals("Text")) {
                testResultService.createOrUpdate(test, "R", null);
            }
            if (referenceDataTest.getResultType().equals("Coded")) {
                Collection<CodedTestAnswer> codedTestAnswer = (Collection<CodedTestAnswer>) referenceDataTest.getCodedTestAnswer();
                Dictionary dict = null;
                //Mark all coded Ans inactive
                testResultService.makeCodedAnswersInactive(test.getId());
                for (CodedTestAnswer testAnswer : codedTestAnswer) {
                    ExternalReference dictReference = externalReferenceDao.getData(testAnswer.getUuid(), CATEGORY_TEST_CODED_ANS);
                    if(dictReference==null){
                        Dictionary existingDictionary = dictionaryDao.getDictionaryByDictEntry(testAnswer.getName());
                        if(existingDictionary == null) {
                            dict = new Dictionary();
                            dict.setDictEntry(testAnswer.getName());
                            dict.setLastupdated(new Timestamp(new Date().getTime()));
                            dict.setSysUserId(sysUserId);
                            dictionaryDao.insertData(dict);
                        }else{
                            dict = existingDictionary;
                        }
                        saveExternalReference(testAnswer, dict);
                    }else{
                        dict = dictionaryDao.getDictionaryById(String.valueOf(dictReference.getItemId()));
                        dict.setDictEntry(testAnswer.getName());
                        dict.setLastupdated(new Timestamp(new Date().getTime()));
                        dict.setSysUserId(sysUserId);
                        dictionaryDao.updateData(dict, false);
                    }
                    //If there is existing rln make it active
                    testResultService.createOrUpdate(test, "D", dict.getId());
                }
            }
            TypeOfSampleUtil.clearTestCache();
        } catch (Exception e) {
            throw new LIMSException(String.format("Error while saving test - %s", referenceDataTest.getName()), e);
        }
    }

    private void saveExternalReference(CodedTestAnswer codedTestAnswer, Dictionary dict) {
        ExternalReference data;
        data = new ExternalReference(Long.parseLong(dict.getId()), codedTestAnswer.getUuid(), CATEGORY_TEST_CODED_ANS);
        externalReferenceDao.insertData(data);
    }

    private void saveExternalReference(ReferenceDataTest referenceDataTest, Test test) {
        ExternalReference data;
        data = new ExternalReference(Long.parseLong(test.getId()), referenceDataTest.getId(), CATEGORY_TEST);
        externalReferenceDao.insertData(data);
    }

    private Test populateTest(Test test, ReferenceDataTest referenceDataTest, String sysUserId, String testSectionUuid) throws IOException {
        test.setTestName(referenceDataTest.getName());
        //Assign to dummy test section
        TestSection section = getTestSection(testSectionUuid);
        if (referenceDataTest.getTestUnitOfMeasure() != null) {
            test.setUnitOfMeasure(unitOfMeasureService.create(referenceDataTest.getTestUnitOfMeasure()));
        }
        test.setTestSection(section);
        test.setDescription(referenceDataTest.getName());
        test.setIsActive(referenceDataTest.getIsActive() ? IActionConstants.YES : IActionConstants.NO);
        test.setLastupdated(new Timestamp(new Date().getTime()));
        test.setName(referenceDataTest.getName());
        test.setReferenceInfo(referenceDataTest.getReferenceInfo());
        test.setSysUserId(sysUserId);
        test.setOrderable(true);
        test.setSortOrder(String.valueOf(referenceDataTest.getSortOrder()));
        return test;
    }

    private TestSection getTestSection(String uuid) {
        if (uuid == null) {
            return testSectionDAO.getTestSectionByName(DUMMY_TEST_SECTION_NAME);
        } else {
            return testSectionDAO.getTestSectionByUUID(uuid);
        }
    }

    public Test updateTestSection(String testName, String testSectionUuid, String sysUserId) throws LIMSException {
        Test test = testDAO.getTestByName(testName);
        if (test == null) {
            throw new LIMSException(String.format("%s test does not exist", testName));
        }
        test.setSysUserId(sysUserId);
        TestSection testSection = getTestSection(testSectionUuid);
        if (!test.getTestSection().equals(testSection)) {
            test.setTestSection(testSection);
            testDAO.updateData(test);
        }
        return test;
    }

    public Test getTest(MinimalResource test) {
        return testDAO.getTestByName(test.getName());
    }

}



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
 * Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.CodedTestAnswer;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataDepartment;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataSample;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTest;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.common.exception.LIMSException;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;
import us.mn.state.health.lims.testresult.valueholder.TestResult;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestServiceTest {

    @Mock
    private AuditingService auditingServiceMock;
    @Mock
    private TestDAO testDAOMock;
    @Mock
    private ExternalReferenceDao externalReferenceDaoMock;
    @Mock
    private TestSectionDAO testSectionDAOMock;
    @Mock
    private TestResultService testResultServiceMock;
    @Mock
    private TypeOfSampleDAO typeOfSampleDAOMock;
    @Mock
    private TypeOfSampleTestDAO typeOfSampleTestDAOMock;
    @Mock
    private DictionaryDAO dictionaryDAOMock;

    private TestService testService;

    @Before
    public void setUp() {
        initMocks(this);

         testService = new TestService(externalReferenceDaoMock, testDAOMock, testResultServiceMock, testSectionDAOMock,
                auditingServiceMock, typeOfSampleDAOMock, typeOfSampleTestDAOMock,dictionaryDAOMock);

    }


    @org.junit.Test
    public void savingATestWithDummyDepartmentIfTestSectionDoesNotExist() throws IOException, LIMSException {

        ReferenceDataTest referenceDataTest = createReferenceDataTest();

        when(testSectionDAOMock.getTestSectionByName(anyString())).thenReturn(null);
        when(typeOfSampleDAOMock.getTypeOfSampleByUUID(anyString())).thenReturn(createDummyTypeOfSample());
        when(externalReferenceDaoMock.getData(referenceDataTest.getId(), TestService.CATEGORY_TEST)).thenReturn(createDummyReferenceData(referenceDataTest.getId()));
        Test dummyTest = createDummyTest();
        when(testDAOMock.getTestById(anyString())).thenReturn(dummyTest);

        testService.createOrUpdate(referenceDataTest);
        verify(testDAOMock).updateData(dummyTest);
    }

    @org.junit.Test
    public void savingATestWithCodedAnswer() throws IOException, LIMSException {

        ReferenceDataTest referenceDataTest = createReferenceDataTest();
        referenceDataTest.setResultType("Coded");
        CodedTestAnswer codedTestAnswer1 = new CodedTestAnswer();
        codedTestAnswer1.setUuid("uuid1");
        codedTestAnswer1.setName("A+ve");
        referenceDataTest.setCodedTestAnswer(Arrays.asList(codedTestAnswer1));

        when(externalReferenceDaoMock.getData(referenceDataTest.getId(), TestService.CATEGORY_TEST)).thenReturn(createDummyReferenceData(referenceDataTest.getId()));
        when(externalReferenceDaoMock.getData("uuid1", TestService.CATEGORY_TEST_CODED_ANS)).thenReturn(null);
        Dictionary existingDictionary = new Dictionary();
        existingDictionary.setId("123");
        existingDictionary.setDictEntry("A+ve");
        when(dictionaryDAOMock.getDictionaryByDictEntry("A+ve")).thenReturn(existingDictionary);
        Test dummyTest = createDummyTest();
        when(testDAOMock.getTestById(anyString())).thenReturn(dummyTest);

        testService.createOrUpdate(referenceDataTest);

        verify(testResultServiceMock).makeCodedAnswersInactive("1");
        verify(testDAOMock).updateData(dummyTest);
        assertEquals("A+ve", existingDictionary.getDictEntry());
        verify(dictionaryDAOMock, never()).insertData(existingDictionary);
        ArgumentCaptor<ExternalReference> externalReferenceArgumentCaptor = ArgumentCaptor.forClass(ExternalReference.class);
        verify(externalReferenceDaoMock).insertData(externalReferenceArgumentCaptor.capture());
        ExternalReference actualRef = externalReferenceArgumentCaptor.getValue();
        assertEquals(123, actualRef.getItemId());
        assertEquals("uuid1", actualRef.getExternalId());
        assertEquals(TestService.CATEGORY_TEST_CODED_ANS, actualRef.getType());
    }

    //------- Private methods --------------

    private ReferenceDataTest createReferenceDataTest() {
        ReferenceDataTest referenceDataTest = new ReferenceDataTest();
        referenceDataTest.setId("1");
        referenceDataTest.setIsActive(Boolean.TRUE);
        referenceDataTest.setLastUpdated(new Date());
        referenceDataTest.setResultType("N/A");
        return referenceDataTest;
    }

    private TypeOfSample createDummyTypeOfSample() {
        TypeOfSample typeOfSample = new TypeOfSample();
        typeOfSample.setId("dummyTypeOfSampleID");
        return typeOfSample;
    }

    private ExternalReference createDummyReferenceData(String itemId) {
        ExternalReference externalReference = new ExternalReference();
        externalReference.setItemId(new Long(itemId));
        return externalReference;
    }

    private Test createDummyTest() {
        Test test = new Test();
        test.setId("1");
        return test;
    }

    private ReferenceDataDepartment createDummyDepartment(String deptID) {
        ReferenceDataDepartment referenceDataDepartment = new ReferenceDataDepartment();
        referenceDataDepartment.setId(deptID);
        return referenceDataDepartment;
    }

    private ReferenceDataSample createDummySample() {
        ReferenceDataSample referenceDataSample = new ReferenceDataSample();
        referenceDataSample.setId("dummySampleID");
        return referenceDataSample;
    }
}

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
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.feed.openelis.feed.contract.openerp.OpenERPLab;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.test.dao.TestDAO;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.test.valueholder.TestSection;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LabTestServiceTest {

    @Mock
    TestDAO testDao;
    @Mock
    ExternalReferenceDao externalReferenceDao;
    @Mock
    TestSectionDAO testSectionDAO;
    static final String EVENT_CONTENT = " {\"category\": \"Test\",\"description\": \"Test Panel\", \"list_price\": \"0.0\", \"name\": \"ECHO\", \"type\": \"service\", \"standard_price\": \"0.0\", \"uom_id\": 1, \"uom_po_id\": 1, \"categ_id\": 33, \"id\": 193}";
    LabTestService labTestService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        labTestService = new LabTestService(testDao,externalReferenceDao, testSectionDAO);

    }

    @org.junit.Test
    public void shouldInsertNewIfExternalReferenceNotFound() throws IOException {
        TestSection section = new TestSection();
        section.setTestSectionName("New");
        OpenERPLab openERPLab = new OpenERPLab("193","Lab Test","lab test desc","1", "Test","active");

        when(externalReferenceDao.getData("193", "Test")).thenReturn(null);
        when(testSectionDAO.getTestSectionByName("New")).thenReturn(section);

        labTestService.save(openERPLab);

        ArgumentCaptor<Test> testCaptor = ArgumentCaptor.forClass(Test.class);

        verify(testDao).insertData(testCaptor.capture());

        Test testCaptorValue = testCaptor.getValue();
        assertEquals(openERPLab.getName(), testCaptorValue.getTestName());
    }

    @org.junit.Test
    public void shouldUpdateIfExternalReferenceFound() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        OpenERPLab openERPLab = new OpenERPLab("193","Lab Test","lab test desc new","1", "Test","active");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc old");
        test.setId("293");

        when(externalReferenceDao.getData("193", "Test")).thenReturn(reference);
        when(testDao.getTestById("293")).thenReturn(test);

        labTestService.save(openERPLab);

        ArgumentCaptor<Test> testArgumentCaptor = ArgumentCaptor.forClass(Test.class);
        verify(testDao).updateData(testArgumentCaptor.capture());
        Test savedTest = testArgumentCaptor.getValue();
        assertEquals(openERPLab.getDescription(), savedTest.getDescription());
        assertEquals("293", savedTest.getId());
    }

    @org.junit.Test
    public void shouldInActivateExistingActiveTest() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        OpenERPLab openERPLab = new OpenERPLab("193","Lab Test","lab test desc new","1", "Test","inactive");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc old");
        test.setId("293");
        test.setIsActive("Y");


        when(externalReferenceDao.getData("193", "Test")).thenReturn(reference);
        when(testDao.getTestById("293")).thenReturn(test);

        labTestService.save(openERPLab);

        ArgumentCaptor<Test> testArgumentCaptor = ArgumentCaptor.forClass(Test.class);
        verify(testDao).updateData(testArgumentCaptor.capture());
        Test savedTest = testArgumentCaptor.getValue();
        assertEquals(openERPLab.getDescription(), savedTest.getDescription());
        assertEquals("293", savedTest.getId());
        assertEquals("N", savedTest.getIsActive());
    }

    @org.junit.Test
    public void shouldActivateExistingInactiveTest() throws IOException {
        ExternalReference reference = new ExternalReference();
        reference.setItemId(293);
        reference.setExternalId("193");

        OpenERPLab openERPLab = new OpenERPLab("193","Lab Test","lab test desc new","1", "Test","active");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc old");
        test.setId("293");
        test.setIsActive("N");


        when(externalReferenceDao.getData("193", "Test")).thenReturn(reference);
        when(testDao.getTestById("293")).thenReturn(test);

        labTestService.save(openERPLab);

        ArgumentCaptor<Test> testArgumentCaptor = ArgumentCaptor.forClass(Test.class);
        verify(testDao).updateData(testArgumentCaptor.capture());
        Test savedTest = testArgumentCaptor.getValue();
        assertEquals(openERPLab.getDescription(), savedTest.getDescription());
        assertEquals("293", savedTest.getId());
        assertEquals("Y", savedTest.getIsActive());
    }

    @org.junit.Test
    public void shouldSaveNewInactivateTest() throws IOException {
        TestSection section = new TestSection();
        section.setTestSectionName("New");

        OpenERPLab openERPLab = new OpenERPLab("193","Lab Test","lab test desc new","1", "Test","inactive");

        when(externalReferenceDao.getData("193", "Test")).thenReturn(null);
        when(testSectionDAO.getTestSectionByName("New")).thenReturn(section);

        labTestService.save(openERPLab);

        ArgumentCaptor<Test> testCaptor = ArgumentCaptor.forClass(Test.class);

        verify(testDao).insertData(testCaptor.capture());

        Test testCaptorValue = testCaptor.getValue();
        assertEquals(openERPLab.getName(), testCaptorValue.getTestName());
        assertEquals("N", testCaptorValue.getIsActive());
    }

    @org.junit.Test
    public void shouldDeleteTestIfNoReferencesExistToTest() throws IOException {
        ExternalReference reference = new ExternalReference(293, "193", "type");
        OpenERPLab openERPLab = new OpenERPLab("193","Lab Test","lab test desc new","1", "Test","deleted");
        Test test = new Test();
        test.setTestName("Lab Test");
        test.setDescription("lab test desc old");
        test.setId("293");
        test.setIsActive("N");

        when(externalReferenceDao.getData(openERPLab.getExternalId(), openERPLab.getCategory())).thenReturn(reference);
        labTestService.delete(openERPLab);

        verify(externalReferenceDao).deleteData(reference);
        verify(testDao).deleteById("293", openERPLab.getSysUserId());
    }
}

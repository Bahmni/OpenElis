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

package us.mn.state.health.lims.upload.service;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.dao.SampleItemDAO;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleTestDAO;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSampleTest;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SampleItemPersisterServiceTest {
    private SampleItemPersisterService sampleItemPersisterService;
    @Mock
    private TypeOfSampleTestDAO typeOfSampleTestDAO;
    @Mock
    private TypeOfSampleDAO typeOfSampleDAO;
    @Mock
    private SampleItemDAO sampleItemDAO;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        sampleItemPersisterService = new SampleItemPersisterService(sampleItemDAO, typeOfSampleTestDAO, typeOfSampleDAO);
    }

    @org.junit.Test
    public void testPersistSampleItems() {
        String testId1 = "1";
        String testId2 = "2";
        Test test1 = new Test();
        test1.setId(testId1);
        Test test2 = new Test();
        test2.setId(testId2);
        String typeOfSampleId1 = "1";
        String typeOfSampleId2 = "2";
        String statusID = StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.SampleStatus.Entered);
        TypeOfSampleTest typeOfSampleTest1 = new TypeOfSampleTest();
        typeOfSampleTest1.setTypeOfSampleId(typeOfSampleId1);
        typeOfSampleTest1.setTestId(testId1);
        TypeOfSampleTest typeOfSampleTest2 = new TypeOfSampleTest();
        typeOfSampleTest2.setTypeOfSampleId(typeOfSampleId1);
        typeOfSampleTest2.setTestId(testId2);
        Sample sample = new Sample();
        sample.setId("12");
        TypeOfSample typeOfSample1 = new TypeOfSample();
        typeOfSample1.setId(typeOfSampleId1);
        TypeOfSample typeOfSample2 = new TypeOfSample();
        typeOfSample2.setId(typeOfSampleId2);
        String sysUserId = "123";
        when(typeOfSampleTestDAO.getTypeOfSampleTestsForTest(test1.getId())).thenReturn(Arrays.asList(typeOfSampleTest1));
        when(typeOfSampleTestDAO.getTypeOfSampleTestsForTest(test2.getId())).thenReturn(Arrays.asList(typeOfSampleTest2));
        when(typeOfSampleDAO.getTypeOfSampleById(typeOfSample1.getId())).thenReturn(typeOfSample1);
        when(sampleItemDAO.getSampleItemsBySampleId(sample.getId())).thenReturn(null);

        SampleItem sampleItem1 = sampleItemPersisterService.save(sample, test1, sysUserId);

        when(sampleItemDAO.getSampleItemsBySampleId(sample.getId())).thenReturn(Arrays.asList(sampleItem1));

        ArgumentCaptor<SampleItem> captor = ArgumentCaptor.forClass(SampleItem.class);
        verify(sampleItemDAO).insertData(captor.capture());
        SampleItem item1 = captor.getValue();
        assertEquals(sample.getId(), item1.getSample().getId());
        assertEquals(typeOfSample1.getId(), item1.getTypeOfSample().getId());
        assertEquals(sysUserId, item1.getSysUserId());
        assertEquals("1", item1.getSortOrder());
        assertEquals(statusID, item1.getStatusId());

        SampleItem sampleItem2 = sampleItemPersisterService.save(sample, test2, sysUserId);

        verify(sampleItemDAO, times(1)).insertData(any(SampleItem.class));
        assertEquals(sampleItem1.getId(), sampleItem2.getId());
    }

}

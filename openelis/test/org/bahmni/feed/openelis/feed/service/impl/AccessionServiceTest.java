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

import junit.framework.Assert;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.openelis.domain.AccessionDetail;
import org.bahmni.openelis.domain.TestDetail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.dbhelper.DBHelper;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.result.dao.ResultSignatureDAO;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.samplesource.valueholder.SampleSource;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;
import us.mn.state.health.lims.systemuser.dao.SystemUserDAO;
import us.mn.state.health.lims.testresult.valueholder.TestResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccessionServiceTest {
    @Mock
    private SampleDAO sampleDao;
    @Mock
    private SampleHumanDAO sampleHumanDAO;
    @Mock
    private ExternalReferenceDao externalReferenceDao;
    @Mock
    private NoteDAO noteDao;
    @Mock
    private SystemUserDAO systemUserDao;
    @Mock
    private DictionaryDAO dictionaryDao;
    @Mock
    private ResultSignatureDAO resultSignatureDao;
    @Mock
    private PatientIdentityDAO patientIdentityDAO;
    @Mock
    private PatientIdentityTypeDAO patientIdentityTypeDAO;
    @Mock
    private SiteInformationDAO siteInformationDAO;


    private Sample sample;
    private Patient patient;
    private PatientIdentityType patienIdentityType;

    @Before
    public void setUp() {
        initMocks(this);
        SampleSource sampleSource = new SampleSource();
        sampleSource.setId("12");
        sample = DBHelper.createEntireSampleTreeWithResults();
        patient = DBHelper.createPatient();
        patienIdentityType = new PatientIdentityType();
        sample.setSampleSource(sampleSource);
    }

    @Test
    public void shouldReturnAccessionDetails() {
        AccessionService accessionService = new TestableAccessionService(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO, siteInformationDAO);
        when(sampleDao.getSamplesByEncounterUuid(sample.getUUID())).thenReturn(Arrays.asList(sample));
        when(sampleDao.getSampleByAccessionNumber(anyString())).thenReturn(sample);
        when(sampleHumanDAO.getPatientForSample(sample)).thenReturn(patient);
        when(externalReferenceDao.getDataByItemId(anyString(), anyString())).thenReturn(new ExternalReference(456789, "Ex Id", "type"));
        when(patientIdentityTypeDAO.getNamedIdentityType("ST")).thenReturn(patienIdentityType);
        when(noteDao.getNoteByRefIAndRefTableAndSubject(anyString(),anyString(),anyString())).thenReturn(Collections.EMPTY_LIST);

        AccessionDetail accessionDetail = accessionService.getAccessionDetailFor(sample.getUUID());
        assertNotNull(accessionDetail);
        assertEquals(accessionDetail.getLabLocationUuid(), "Ex Id");
    }

    @Test
    public void shouldReturnMergedAccessionDetailsForTwoSamples() {
        AccessionService accessionService = new TestableAccessionService(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO, siteInformationDAO);
        Sample sample2 = DBHelper.createSample("ijkl");
        sample2.setUUID(sample.getUUID());
        SampleItem sampleItem = DBHelper.createSampleItem(sample2);
        Panel panel = DBHelper.createPanel();
        us.mn.state.health.lims.test.valueholder.Test test = DBHelper.createTest();
        Analysis analysis = DBHelper.createAnalysis(sampleItem, panel, test);
        TestResult testResult = DBHelper.createTestResult(test);
        DBHelper.createResult(analysis, testResult);

        when(externalReferenceDao.getDataByItemId(anyString(), anyString())).thenReturn(new ExternalReference(456789, "Ex Id", "type"));
        when(patientIdentityTypeDAO.getNamedIdentityType("ST")).thenReturn(patienIdentityType);
        when(noteDao.getNoteByRefIAndRefTableAndSubject(anyString(), anyString(), anyString())).thenReturn(Collections.EMPTY_LIST);

        when(sampleDao.getSamplesByEncounterUuid(sample.getUUID())).thenReturn(Arrays.asList(sample,sample2));
        when(sampleDao.getSampleByAccessionNumber(sample.getAccessionNumber())).thenReturn(sample);
        when(sampleDao.getSampleByAccessionNumber(sample2.getAccessionNumber())).thenReturn(sample2);

        when(sampleHumanDAO.getPatientForSample(sample)).thenReturn(patient);
        when(sampleHumanDAO.getPatientForSample(sample2)).thenReturn(patient);

        AccessionDetail accessionDetail = accessionService.getAccessionDetailFor(sample.getUUID());
        assertNotNull(accessionDetail);
        assertEquals(accessionDetail.getTestDetails().size(), 2);
        assertEquals(accessionDetail.getTestDetails().get(0).getTestName(), sample.getSampleItems().iterator().next().getAnalyses().iterator().next().getTest().getTestName());
        assertEquals(accessionDetail.getTestDetails().get(1).getTestName(), sample.getSampleItems().iterator().next().getAnalyses().iterator().next().getTest().getTestName());
    }

    @Test
    public void shouldGetAccessionDetailsForUuid() {
        AccessionService accessionService = new TestableAccessionService(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO, siteInformationDAO);
        ExternalReference externalReferences = new ExternalReference(98743123, "ExternalId", "Type");
        Note oldNote = new Note();
        oldNote.setText("note 1");
        Note latestNote = new Note();
        latestNote.setText("note latest");

        when(sampleDao.getSamplesByEncounterUuid(sample.getUUID())).thenReturn(Arrays.asList(sample));
        when(sampleHumanDAO.getPatientForSample(sample)).thenReturn(patient);
        Analysis analysis = (Analysis) ((SampleItem) sample.getSampleItems().toArray()[0]).getAnalyses().toArray()[0];
        when(externalReferenceDao.getDataByItemId(analysis.getTest().getId(), "Test")).thenReturn(externalReferences);
        when(externalReferenceDao.getDataByItemId(analysis.getPanel().getId(), "Panel")).thenReturn(externalReferences);
        when(noteDao.getNoteByRefIAndRefTableAndSubject(anyString(), anyString(), anyString())).thenReturn(Arrays.asList(latestNote, oldNote));
        when(patientIdentityTypeDAO.getNamedIdentityType("ST")).thenReturn(patienIdentityType);


        AccessionDetail accessionDetail = accessionService.getAccessionDetailFor(sample.getUUID());

        Assert.assertEquals(accessionDetail.getAccessionUuid(), sample.getUUID());
        List<TestDetail> testDetails = accessionDetail.getTestDetails();
        Assert.assertNotNull(testDetails);
        Assert.assertNotNull(accessionDetail.getPatientUuid());
        Assert.assertEquals(accessionDetail.getPatientUuid(), patient.getUuid());
        assertEquals(latestNote.getText(), testDetails.get(0).getNotes());
    }


    @Test
    public void shouldSetTheResultUuidIfCodedAnswer() {
        AccessionService accessionService = new TestableAccessionService(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO, siteInformationDAO);
        TestDetail testDetail = new TestDetail();
        Result result = new Result();
        result.setValue("result");
        result.setResultType("D");

        Dictionary dictionary = new Dictionary();
        dictionary.setId("dictId");
        dictionary.setDictEntry("dictEntry");
        when(dictionaryDao.getDataForId(result.getValue())).thenReturn(dictionary);
        when(externalReferenceDao.getDataByItemId("dictId", "CodedAns")).thenReturn(new ExternalReference(1, "resultUuid", "ctype"));

        accessionService.setResultValue(testDetail, result);

        assertEquals("resultUuid", testDetail.getResultUuid());
        assertEquals("dictEntry", testDetail.getResult());

    }

    @Test
    public void shouldNotSetTheResultUuidIfNotCodedAnswer() {
        AccessionService accessionService = new TestableAccessionService(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO, siteInformationDAO);
        TestDetail testDetail = new TestDetail();
        Result result = new Result();
        result.setValue("result");
        result.setResultType("D");

        Dictionary dictionary = new Dictionary();
        dictionary.setId("dictId");
        dictionary.setDictEntry("dictEntry");
        when(dictionaryDao.getDataForId(result.getValue())).thenReturn(dictionary);
        when(externalReferenceDao.getDataByItemId("dictId", "CodedAns")).thenReturn(null);

        accessionService.setResultValue(testDetail, result);

        assertNull(testDetail.getResultUuid());
        assertEquals("dictEntry", testDetail.getResult());

    }

    @Test
    public void shouldSetResultValueForNumeric() {
        AccessionService accessionService = new TestableAccessionService(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO, siteInformationDAO);
        TestDetail testDetail = new TestDetail();
        Result result = new Result();
        result.setValue("10");
        result.setResultType("N");

        accessionService.setResultValue(testDetail, result);

        assertNull(testDetail.getResultUuid());
        assertEquals("10", testDetail.getResult());
    }

    private class TestableAccessionService extends AccessionService {

        public TestableAccessionService(SampleDAO sampleDao, SampleHumanDAO sampleHumanDAO,
                                        ExternalReferenceDao externalReferenceDao, NoteDAO noteDao, DictionaryDAO dictionaryDao,
                                        PatientIdentityDAO patientIdentityDAO, PatientIdentityTypeDAO patientIdentityTypeDAO, SiteInformationDAO siteInformationDAO) {
            super(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO, siteInformationDAO);
        }

        @Override
        protected String getResultReferenceTableId() {
            return "1";
        }

        @Override
        protected String getFinalizedStatus() {
            return "6";
        }

        @Override
        protected String getStatus(String statusId) {
            return "Status";
        }
    }

}

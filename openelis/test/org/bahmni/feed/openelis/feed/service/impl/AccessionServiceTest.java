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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.dbhelper.DBHelper;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.result.dao.ResultSignatureDAO;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.systemuser.dao.SystemUserDAO;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
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


    private Sample sample;
    private Patient patient;
    private PatientIdentityType patienIdentityType;

    @Before
    public void setUp() {
        initMocks(this);
        sample = DBHelper.createEntireSampleTreeWithResults();
        patient = DBHelper.createPatient();
        patienIdentityType = new PatientIdentityType();
    }

    @Test
    public void shouldReturnAccessionDetails() {
        AccessionService accessionService = new TestableAccessionService(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO);
        when(sampleDao.getSampleByUUID(sample.getUUID())).thenReturn(sample);
        when(sampleDao.getSampleByAccessionNumber(anyString())).thenReturn(sample);
        when(sampleHumanDAO.getPatientForSample(sample)).thenReturn(patient);
        when(externalReferenceDao.getDataByItemId(anyString(), anyString())).thenReturn(new ExternalReference(456789, "Ex Id", "type"));
        when(patientIdentityTypeDAO.getNamedIdentityType("ST")).thenReturn(patienIdentityType);
        when(noteDao.getNoteByRefIAndRefTableAndSubject(anyString(),anyString(),anyString())).thenReturn(Collections.EMPTY_LIST);
        AccessionDetail accessionDetail = accessionService.getAccessionDetailFor(sample.getUUID());
        assertNotNull(accessionDetail);
    }

    @Test
    public void shouldGetAccessionDetailsForUuid() {
        AccessionService accessionService = new TestableAccessionService(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO);
        ExternalReference externalReferences = new ExternalReference(98743123, "ExternalId", "Type");

        when(sampleDao.getSampleByUUID(sample.getUUID())).thenReturn(sample);
        when(sampleHumanDAO.getPatientForSample(sample)).thenReturn(patient);
        Analysis analysis = (Analysis) ((SampleItem) sample.getSampleItems().toArray()[0]).getAnalyses().toArray()[0];
        when(externalReferenceDao.getDataByItemId(analysis.getTest().getId(), "Test")).thenReturn(externalReferences);
        when(externalReferenceDao.getDataByItemId(analysis.getPanel().getId(), "Panel")).thenReturn(externalReferences);
        when(noteDao.getNoteByRefIAndRefTableAndSubject(anyString(), anyString(), anyString())).thenReturn(new ArrayList<Note>());
        when(patientIdentityTypeDAO.getNamedIdentityType("ST")).thenReturn(patienIdentityType);

        AccessionDetail accessionDetail = accessionService.getAccessionDetailFor(sample.getUUID());

        Assert.assertEquals(accessionDetail.getAccessionUuid(), sample.getUUID());
        Assert.assertNotNull(accessionDetail.getTestDetails());
        Assert.assertNotNull(accessionDetail.getPatientUuid());
        Assert.assertEquals(accessionDetail.getPatientUuid(), patient.getUuid());
    }

    private class TestableAccessionService extends AccessionService {

        public TestableAccessionService(SampleDAO sampleDao, SampleHumanDAO sampleHumanDAO,
                                        ExternalReferenceDao externalReferenceDao, NoteDAO noteDao, DictionaryDAO dictionaryDao,
                                        PatientIdentityDAO patientIdentityDAO, PatientIdentityTypeDAO patientIdentityTypeDAO) {
            super(sampleDao, sampleHumanDAO, externalReferenceDao, noteDao, dictionaryDao, patientIdentityDAO, patientIdentityTypeDAO);
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

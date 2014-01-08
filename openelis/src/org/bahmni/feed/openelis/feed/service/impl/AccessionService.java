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
import org.bahmni.openelis.domain.AccessionDetails;
import org.bahmni.openelis.domain.TestResult;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.dao.ResultSignatureDAO;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.systemuser.dao.SystemUserDAO;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.unitofmeasure.valueholder.UnitOfMeasure;

import java.util.ArrayList;
import java.util.List;

public class AccessionService {
    private SampleDAO sampleDao;
    private SampleHumanDAO sampleHumanDAO;
    private ExternalReferenceDao externalReferenceDao;
    private NoteDAO noteDao;
    private DictionaryDAO dictionaryDAO;
    private ResultSignatureDAO resultSignatureDAO;
    private SystemUserDAO systemUserDAO;

    public AccessionService(SampleDAO sampleDao, SampleHumanDAO sampleHumanDAO, ExternalReferenceDao externalReferenceDao, NoteDAO noteDao, DictionaryDAO dictionaryDAO, ResultSignatureDAO resultSignatureDAO, SystemUserDAO systemUserDAO) {

        this.sampleDao = sampleDao;
        this.sampleHumanDAO = sampleHumanDAO;
        this.externalReferenceDao = externalReferenceDao;
        this.noteDao = noteDao;
        this.dictionaryDAO = dictionaryDAO;
        this.resultSignatureDAO = resultSignatureDAO;
        this.systemUserDAO = systemUserDAO;
    }

    public AccessionDetails getAccessionDetailsFor(String sampleUuid) {
        Sample sample = sampleDao.getSampleByUUID(sampleUuid);
        Patient patient = sampleHumanDAO.getPatientForSample(sample);

        return mapSampleToAccessionDetails(sample, patient);
    }

    private AccessionDetails mapSampleToAccessionDetails(Sample sample, Patient patient) {
        Person person = patient.getPerson();

        AccessionDetails accessionDetails = new AccessionDetails();
        accessionDetails.setAccessionUuid(sample.getUUID());
        accessionDetails.setPatientUuid(patient.getUuid());
        accessionDetails.setPatientFirstName(person.getFirstName());
        accessionDetails.setPatientLastName(person.getLastName());

        List<TestResult> testResults = new ArrayList<>();

        for (SampleItem sampleItem : sample.getSampleItems()) {
            for (Analysis analysis : sampleItem.getAnalyses()) {
                TestResult tr = new TestResult();
                testResults.add(tr);
                Test test = analysis.getTest();
                tr.setTestName(test.getTestName());
                UnitOfMeasure unitOfMeasure = test.getUnitOfMeasure();
                if (unitOfMeasure != null) tr.setTestUnitOfMeasurement(unitOfMeasure.getUnitOfMeasureName());

                setExternalIds(analysis, tr);

                for (Result result : analysis.getResults()) {
                    String systemUserId = resultSignatureDAO.getResultSignaturesByResult(result).get(0).getSystemUserId();
                    accessionDetails.setProviderUuid(systemUserDAO.getUserById(systemUserId).getExternalId());

                    addNotes(result.getId(), tr);
                    tr.setMinNormal(result.getMinNormal());
                    tr.setMaxNormal(result.getMaxNormal());
                    tr.setResult(getResultValue(result));
                    tr.setResultType(result.getResultType());
                }
            }
        }

        accessionDetails.setTestResults(testResults);
        return accessionDetails;
    }

    private String getResultValue(Result result) {
        if (result.getResultType().equals("D"))
            return dictionaryDAO.getDataForId(result.getValue()).getDictEntry();
        else return result.getValue();
    }

    private void setExternalIds(Analysis analysis, TestResult tr) {
        ExternalReference externalReferenceForTest = externalReferenceDao.getDataByItemId(analysis.getTest().getId(), "Test");
        tr.setTestUuid(externalReferenceForTest.getExternalId());

        Panel panel = analysis.getPanel();
        if (panel != null) {
            ExternalReference externalReferenceForPanel = externalReferenceDao.getDataByItemId(panel.getId(), "Panel");
            tr.setPanelUuid(externalReferenceForPanel.getExternalId());
        }
    }

    private void addNotes(String resultId, TestResult testResult) {
        List<Note> notes = noteDao.getNoteByRefIAndRefTableAndSubject(resultId, getResultReferenceTableId(), "Result Note");
        for (Note note : notes) {
            testResult.addNotes(note.getText());
        }
    }

    protected String getResultReferenceTableId() {
        return ResultsLoadUtility.getResultReferenceTableId();
    }
}

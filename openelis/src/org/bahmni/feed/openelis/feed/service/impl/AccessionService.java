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

import org.apache.commons.lang3.time.DateFormatUtils;
import org.bahmni.feed.openelis.externalreference.dao.ExternalReferenceDao;
import org.bahmni.feed.openelis.externalreference.valueholder.ExternalReference;
import org.bahmni.openelis.domain.AccessionDetail;
import org.bahmni.openelis.domain.AccessionNote;
import org.bahmni.openelis.domain.TestDetail;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.patientidentity.dao.PatientIdentityDAO;
import us.mn.state.health.lims.patientidentity.valueholder.PatientIdentity;
import us.mn.state.health.lims.patientidentitytype.dao.PatientIdentityTypeDAO;
import us.mn.state.health.lims.patientidentitytype.valueholder.PatientIdentityType;
import us.mn.state.health.lims.person.valueholder.Person;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.result.valueholder.ResultSignature;
import us.mn.state.health.lims.resultvalidation.util.ResultsValidationUtility;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.unitofmeasure.valueholder.UnitOfMeasure;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AccessionService {
    private String[] finalizedStatusIds;
    private SampleDAO sampleDao;
    private SampleHumanDAO sampleHumanDAO;
    private ExternalReferenceDao externalReferenceDao;
    private NoteDAO noteDao;
    private DictionaryDAO dictionaryDAO;
    private PatientIdentityDAO patientIdentityDAO;
    private PatientIdentityTypeDAO patientIdentityTypeDAO;
    private ResultsValidationUtility resultsValidationUtility;
    private SiteInformationDAO siteInformationDAO;

    public AccessionService(SampleDAO sampleDao, SampleHumanDAO sampleHumanDAO, ExternalReferenceDao externalReferenceDao,
                            NoteDAO noteDao, DictionaryDAO dictionaryDAO, PatientIdentityDAO patientIdentityDAO, PatientIdentityTypeDAO patientIdentityTypeDAO,SiteInformationDAO siteInformationDAO) {

        this.sampleDao = sampleDao;
        this.sampleHumanDAO = sampleHumanDAO;
        this.externalReferenceDao = externalReferenceDao;
        this.noteDao = noteDao;
        this.dictionaryDAO = dictionaryDAO;
        this.patientIdentityDAO = patientIdentityDAO;
        this.patientIdentityTypeDAO = patientIdentityTypeDAO;
        this.finalizedStatusIds = new String[]{getFinalizedStatus(), getFinalizedROStatus()};
        this.resultsValidationUtility = new ResultsValidationUtility();
        this.siteInformationDAO = siteInformationDAO;
    }

    public AccessionDetail getAccessionDetailFor(String sampleUuid) {
        List<Sample> samples = sampleDao.getSamplesByEncounterUuid(sampleUuid);
        if(samples.size()==0){
            throw new LIMSRuntimeException("The sample with uuid ["+sampleUuid+"] does not exist");
        }
        Patient patient = sampleHumanDAO.getPatientForSample(samples.get(0));
        List<AccessionDetail> accessionDetails = mapToAccessionDetail(samples, patient);
        AccessionDetail accessionDetail = mergeAccessionDetailsForEncounter(accessionDetails);
        ExternalReference externalReference = externalReferenceDao.getDataByItemId(samples.get(0).getSampleSource().getId(),"SampleSource");
        if(externalReference != null)
            accessionDetail.setLabLocationUuid(externalReference.getExternalId());

        return accessionDetail;
    }

    protected AccessionDetail mergeAccessionDetailsForEncounter(List<AccessionDetail> accessionDetails) {
        if(accessionDetails.size() == 1){
            return accessionDetails.get(0);
        }
        Iterator<AccessionDetail> iterator = accessionDetails.iterator();
        AccessionDetail accessionDetail = iterator.hasNext() ? iterator.next() : null;
        while (iterator.hasNext()){
            accessionDetail.getTestDetails().addAll(iterator.next().getTestDetails());
        }

        return accessionDetail;
    }

    private List<AccessionDetail> mapToAccessionDetail(List<Sample> samples, Patient patient) {

        List<AccessionDetail> accessionDetails = new ArrayList<>();
        for(Sample sample: samples){
            AccessionDetail accessionDetail = new AccessionDetail();
            mapSample(sample, accessionDetail);
            mapPatient(patient, accessionDetail);
            accessionDetails.add(accessionDetail);
        }

        return accessionDetails;
    }

    private void mapPatient(Patient patient, AccessionDetail accessionDetail) {
        Person person = patient.getPerson();
        accessionDetail.setPatientUuid(patient.getUuid());
        accessionDetail.setPatientFirstName(person.getFirstName());
        accessionDetail.setPatientLastName(person.getLastName());
        accessionDetail.setPatientIdentifier(findPatientIdentity(patient));
    }

    private String findPatientIdentity(Patient patient) {
        PatientIdentity patientIdentity = patientIdentityDAO.getPatitentIdentityForPatientAndType(patient.getId(), primaryIdentityType().getId());
        return patientIdentity != null ? patientIdentity.getIdentityData() : "";
    }

    private void mapSample(Sample sample, AccessionDetail accessionDetail) {
        accessionDetail.setAccessionUuid(sample.getUUID());
        List<Note> accessionNotes = resultsValidationUtility.getAccessionNotes(sample.getAccessionNumber());
        accessionDetail.setAccessionNotes(mapToAccessionNotes(accessionNotes));
        //too many dates in sample table. We just picked the one that can be closest to what we need.
        accessionDetail.setDateTime(new java.sql.Timestamp(sample.getEnteredDate().getTime()));
        List<TestDetail> testDetails = new ArrayList<>();
        for (SampleItem sampleItem : sample.getSampleItems()) {
            mapSampleItem(testDetails, sampleItem);
        }

        accessionDetail.setTestDetails(testDetails);
    }

    private List<AccessionNote> mapToAccessionNotes(List<Note> accessionNotes) {
        List<AccessionNote> accessionNotesToPublish = new ArrayList<>();
        for (Note accessionNote : accessionNotes) {
            accessionNotesToPublish.add(new AccessionNote(accessionNote.getText(),accessionNote.getSystemUser().getExternalId(), toISODateFormat(accessionNote.getLastupdated())));
        }
        return accessionNotesToPublish;
    }

    private String toISODateFormat(Timestamp timestamp){
        return DateFormatUtils.format(timestamp.getTime(),DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());
    }

    private void mapSampleItem(List<TestDetail> testDetails, SampleItem sampleItem) {
        for (Analysis analysis : sampleItem.getAnalyses()) {
            mapAnalysis(testDetails, analysis);
        }
    }

    private void mapAnalysis(List<TestDetail> testDetails, Analysis analysis) {
        TestDetail testDetail = new TestDetail();
        testDetails.add(testDetail);
        Test test = analysis.getTest();
        testDetail.setTestName(test.getTestName());
        UnitOfMeasure unitOfMeasure = test.getUnitOfMeasure();
        if (unitOfMeasure != null) testDetail.setTestUnitOfMeasurement(unitOfMeasure.getUnitOfMeasureName());

        setExternalIds(analysis, testDetail);

        mapResults(analysis, testDetail);
    }

    private void mapResults(Analysis analysis, TestDetail testDetail) {
        testDetail.setStatus(getStatus(analysis.getStatusId()));
        for (Result result : analysis.getResults()) {
            mapResult(analysis, testDetail, result);
        }
    }

    private void mapResult(Analysis analysis, TestDetail testDetail, Result result) {
        for (String statusId : finalizedStatusIds) {
            if (statusId.equals(analysis.getStatusId())) {
                setResultDetail(testDetail, result);
                break;
            }
        }
    }

    private void setResultDetail(TestDetail testDetail, Result result) {
        ResultSignature resultSignature = (ResultSignature) result.getResultSignatures().toArray()[0];
        testDetail.setProviderUuid(resultSignature.getSystemUser().getExternalId());
        addRecentNotes(result.getId(), testDetail);
        testDetail.setMinNormal(result.getMinNormal() != null && result.getMinNormal() != Double.NEGATIVE_INFINITY ? result.getMinNormal() : null);
        testDetail.setMaxNormal(result.getMaxNormal() != null && result.getMaxNormal() != Double.POSITIVE_INFINITY ? result.getMaxNormal() : null);
        setResultValue(testDetail, result);
        testDetail.setResultType(result.getResultType());
        testDetail.setIsAbnormal(result.getAbnormal());
        testDetail.setDateTime(result.getAnalysis().getEnteredDate());
        testDetail.setUploadedFileName(result.getUploadedFileName());
    }

    protected void setResultValue(TestDetail testDetail, Result result) {
        if (result.isValid()) {
            if (result.isDictionary()) {
                Dictionary dictionary = dictionaryDAO.getDataForId(result.getValue());
                testDetail.setResult(dictionary.getDictEntry());
                ExternalReference externalReference = externalReferenceDao.getDataByItemId(dictionary.getId(), TestService.CATEGORY_TEST_CODED_ANS);
                if(externalReference!= null){
                    testDetail.setResultUuid(externalReference.getExternalId());
                }
            }else{
                testDetail.setResult(result.getValue());
            }
        }

    }

    private void setExternalIds(Analysis analysis, TestDetail tr) {
        ExternalReference externalReferenceForTest = externalReferenceDao.getDataByItemId(analysis.getTest().getId(), "Test");
        if(externalReferenceForTest != null) {
            tr.setTestUuid(externalReferenceForTest.getExternalId());
        }

        Panel panel = analysis.getPanel();
        if (panel != null) {
            ExternalReference externalReferenceForPanel = externalReferenceDao.getDataByItemId(panel.getId(), "Panel");
            if(externalReferenceForPanel != null) {
                tr.setPanelUuid(externalReferenceForPanel.getExternalId());
            }
        }
    }

    private void addRecentNotes(String resultId, TestDetail testResult) {
        List<Note> notes = noteDao.getNoteByRefIAndRefTableAndSubject(resultId, getResultReferenceTableId(), "Result Note");
        if(!notes.isEmpty()){
            Note latestNote = notes.get(0);
            testResult.setNotes(latestNote.getText());
        }
    }

    private PatientIdentityType primaryIdentityType() {
        return patientIdentityTypeDAO.getNamedIdentityType("ST");
    }

    protected String getResultReferenceTableId() {
        return ResultsLoadUtility.getResultReferenceTableId();
    }

    protected String getFinalizedStatus() {
        return StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.Finalized);
    }

    private String getFinalizedROStatus() {
        return StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.FinalizedRO);
    }

    protected String getStatus(String statusId) {
        return StatusOfSampleUtil.getStatusNameFromId(statusId);
    }
}

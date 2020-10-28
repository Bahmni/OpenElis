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
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.statusofsample.util;

import org.apache.commons.validator.GenericValidator;
import us.mn.state.health.lims.analysis.dao.AnalysisDAO;
import us.mn.state.health.lims.analysis.daoimpl.AnalysisDAOImpl;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.observationhistory.dao.ObservationHistoryDAO;
import us.mn.state.health.lims.observationhistory.daoimpl.ObservationHistoryDAOImpl;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory.ValueType;
import us.mn.state.health.lims.observationhistorytype.dao.ObservationHistoryTypeDAO;
import us.mn.state.health.lims.observationhistorytype.daoImpl.ObservationHistoryTypeDAOImpl;
import us.mn.state.health.lims.observationhistorytype.valueholder.ObservationHistoryType;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sample.dao.SampleDAO;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.samplehuman.dao.SampleHumanDAO;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.samplehuman.valueholder.SampleHuman;
import us.mn.state.health.lims.statusofsample.dao.StatusOfSampleDAO;
import us.mn.state.health.lims.statusofsample.daoimpl.StatusOfSampleDAOImpl;
import us.mn.state.health.lims.statusofsample.valueholder.StatusOfSample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * This is the union of all the status of all instances of global OpenELIS.  Please do not remove a
 * status because it is not in the database which you are using.
 *
 * These are the know status per implementation
 *
 *  ORDER
 *  Test Entered "No tests have been run for this sample"
 *  Testing Started "Some tests have been run on this sample"
 *  Testing finished "All tests have been run on this sample"
 *  Non-conforming "The sample is non-conforming"
 *  ANALYSIS
 *  Not Tested "This test has not yet been done"
 *  ReferredOut "Test has been referred to an outside lab and the results have not been returned"
 *  Finalized "The results of the test were accepted"
 *  Biologist Rejection "The result was not valid and not signed by the supervisor"
 *	ReferredIn "The analysis and result were done in a different lab"
 *  NonConforming "Test is for a non-conforming sample-item"
 *  
 * RECORD
 * Not Started "The data entry has not yet started"
 * Initial Entry "The initial entry has been done"
 * Validation Entry "The second, validation, entry has been done"
 * 
 *
 */
public class StatusOfSampleUtil {

    public enum OrderStatus {
        Entered,
        Started,
        Finished,
        NonConforming_depricated
    }

    public enum AnalysisStatus {
        NotTested,
        ReferedOut,
        Canceled,
        TechnicalAcceptance,
        TechnicalAcceptanceRO,
        TechnicalRejected,
        BiologistRejected,
        BiologistRejectedRO,
        NonConforming_depricated,
        ReferredIn,
        Finalized,
        FinalizedRO,
        MarkedAsDone
    }

    public enum RecordStatus {
        NotRegistered,
        InitialRegistration,
        ValidationRegistration
    }

    public enum SampleStatus {
        Entered,
        Canceled
    }


    public static enum StatusType {
        Analysis,
        Sample,
        Order,
        SampleEntry,
        PatientEntry
    }

    private static Map<String, OrderStatus> idToOrderStatusMap = null;
    private static Map<String, SampleStatus> idToSampleStatusMap = null;
    private static Map<String, AnalysisStatus> idToAnalysisStatusMap = null;
    private static Map<String, RecordStatus> idToRecordStatusMap = null;
    private static Map<OrderStatus, StatusOfSample> orderStatusToObjectMap = null;
    private static Map<SampleStatus, StatusOfSample> sampleStatusToObjectMap = null;
    private static Map<AnalysisStatus, StatusOfSample> analysisStatusToObjectMap = null;
    private static Map<RecordStatus, Dictionary> recordStatusToObjectMap = null;

    private static String orderRecordStatusID;
    private static String patientRecordStatusID;
    private static ObservationHistoryDAO observationHistoryDAO = new ObservationHistoryDAOImpl();

    private static boolean mapsSet = false;

    public static String getStatusID(OrderStatus statusType) {
        insureMapsAreBuilt();
        StatusOfSample status = orderStatusToObjectMap.get(statusType);
        return status == null ? "-1" : status.getId();
    }

    public static String getStatusID(SampleStatus statusType) {
        insureMapsAreBuilt();
        StatusOfSample status = sampleStatusToObjectMap.get(statusType);
        return status == null ? "-1" : status.getId();
    }

    public static String getStatusID(AnalysisStatus statusType) {
        insureMapsAreBuilt();
        StatusOfSample status = analysisStatusToObjectMap.get(statusType);
        return status == null ? "-1" : status.getId();
    }

    public static String getStatusName(RecordStatus statusType) {
        insureMapsAreBuilt();
        Dictionary dictionary = recordStatusToObjectMap.get(statusType);
        return dictionary == null ? "unknown" : dictionary.getLocalizedName();
    }

    public static String getStatusName(OrderStatus statusType) {
        insureMapsAreBuilt();
        StatusOfSample status = orderStatusToObjectMap.get(statusType);

        return status == null ? "unknown" : status.getLocalizedName();
    }

    public static String getStatusName(SampleStatus statusType) {
        insureMapsAreBuilt();
        StatusOfSample status = sampleStatusToObjectMap.get(statusType);
        return status == null ? "unknown" : status.getLocalizedName();
    }

    public static String getStatusName(AnalysisStatus statusType) {
        insureMapsAreBuilt();
        StatusOfSample status = analysisStatusToObjectMap.get(statusType);
        return status == null ? "unknown" : status.getLocalizedName();
    }

    public static String getDictionaryID(RecordStatus statusType) {
        insureMapsAreBuilt();
        Dictionary dictionary = recordStatusToObjectMap.get(statusType);
        return dictionary == null ? "-1" : dictionary.getId();
    }

    public static OrderStatus getOrderStatusForID(String id) {
        insureMapsAreBuilt();
        return idToOrderStatusMap.get(id);
    }

    public static SampleStatus getSampleStatusForID(String id) {
        insureMapsAreBuilt();
        return idToSampleStatusMap.get(id);
    }

    public static AnalysisStatus getAnalysisStatusForID(String id) {
        insureMapsAreBuilt();
        return idToAnalysisStatusMap.get(id);
    }

    public static RecordStatus getRecordStatusForID(String id) {
        insureMapsAreBuilt();
        return idToRecordStatusMap.get(id);
    }

    /**
     * @param sampleId
     * @return
     */
    public static StatusSet getStatusSetForSampleId(String sampleId) {
        Sample sample = new Sample();
        sample.setId(sampleId);

        SampleDAO sampleDAO = new SampleDAOImpl();
        sampleDAO.getData(sample);

        return buildStatusSet(sample);
    }

    public static StatusSet getStatusSetForAccessionNumber(String accessionNumber) {

        if (GenericValidator.isBlankOrNull(accessionNumber)) {
            return new StatusSet();
        }

        SampleDAO sampleDAO = new SampleDAOImpl();

        Sample sample = sampleDAO.getSampleByAccessionNumber(accessionNumber);

        return buildStatusSet(sample);
    }

    public static List<Integer> analysisFinishedStatusIds() {
        List<Integer> sampleFinishedStatus = new ArrayList<>();
        sampleFinishedStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.Finalized)));
        sampleFinishedStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.FinalizedRO)));
        sampleFinishedStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.Canceled)));
        sampleFinishedStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(AnalysisStatus.NonConforming_depricated)));
        return sampleFinishedStatus;
    }

    private static StatusSet buildStatusSet(Sample sample) {
        StatusSet statusSet = new StatusSet();
        if (sample == null || sample.getId() == null) {
            statusSet.setPatientRecordStatus(null);
            statusSet.setSampleRecordStatus(null);
        } else {

            statusSet.setSampleStatus(getOrderStatusForID(sample.getStatusId()));

            setAnalysisStatus(statusSet, sample);

            setRecordStatus(statusSet, sample);
        }

        return statusSet;
    }

    /*
     * Preconditions: It is called within a transaction
     *                Both the patient and sample ids are valid
     *
     * For now it will fail silently
     * Either sampleStatus or patient status may be null
     */
    public static void persistRecordStatusForSample(Sample sample, RecordStatus recordStatus, Patient patient, RecordStatus patientStatus, String sysUserId) {
        insureMapsAreBuilt();

        if (sample == null || patient == null) {
            return;
        }

        List<ObservationHistory> observationList = observationHistoryDAO.getAll(patient, sample);

        ObservationHistory sampleRecord = null;
        ObservationHistory patientRecord = null;

        for (ObservationHistory currentHistory : observationList) {
            if (currentHistory.getObservationHistoryTypeId().equals(orderRecordStatusID)) {
                sampleRecord = currentHistory;
            } else if (currentHistory.getObservationHistoryTypeId().equals(patientRecordStatusID)) {
                patientRecord = currentHistory;
            }
        }

        if (recordStatus != null) {
            insertOrUpdateStatus(sample, patient, recordStatus, sysUserId, sampleRecord, orderRecordStatusID);
        }

        if (patientStatus != null) {
            insertOrUpdateStatus(sample, patient, patientStatus, sysUserId, patientRecord, patientRecordStatusID);
        }
    }

    public static void deleteRecordStatus(Sample sample, Patient patient, String sysUserId) {
        insureMapsAreBuilt();

        if (sample == null || patient == null) {
            return;
        }

        List<ObservationHistory> observations = observationHistoryDAO.getAll(patient, sample);

        List<ObservationHistory> records = new ArrayList<>();

        for (ObservationHistory observation : observations) {
            if (observation.getObservationHistoryTypeId().equals(orderRecordStatusID) ||
                    observation.getObservationHistoryTypeId().equals(patientRecordStatusID)) {
                observation.setSysUserId(sysUserId);
                records.add(observation);
            }
        }

        observationHistoryDAO.delete(records);
    }

    public static boolean isPublishableAnalysis(String statusId) {
        return statusId.equals(getStatusID(AnalysisStatus.Finalized))
                || statusId.equals(getStatusID(AnalysisStatus.FinalizedRO))
                || statusId.equals(getStatusID(AnalysisStatus.ReferedOut));
    }

    private static void insertOrUpdateStatus(Sample sample, Patient patient, RecordStatus status, String sysUserId, ObservationHistory record, String historyTypeId) {

        if (record == null) {
            record = new ObservationHistory();
            record.setObservationHistoryTypeId(historyTypeId);
            record.setPatientId(patient.getId());
            record.setSampleId(sample.getId());
            record.setSysUserId(sysUserId);
            record.setValue(getDictionaryID(status));
            record.setValueType(ValueType.DICTIONARY);
            observationHistoryDAO.insertData(record);
        } else {
            record.setSysUserId(sysUserId);
            record.setValue(getDictionaryID(status));
            observationHistoryDAO.updateData(record);
        }
    }

    private static void setAnalysisStatus(StatusSet statusSet, Sample sample) {
        AnalysisDAO analysisDAO = new AnalysisDAOImpl();
        List<Analysis> analysisList = analysisDAO.getAnalysesBySampleId(sample.getId());

        Map<Analysis, AnalysisStatus> analysisStatusMap = new HashMap<Analysis, AnalysisStatus>();

        for (Analysis analysis : analysisList) {
            analysisStatusMap.put(analysis, getAnalysisStatusForID(analysis.getStatusId()));
        }

        statusSet.setAnalysisStatus(analysisStatusMap);
    }

    /**
     * Fills in the StatusSet with
     *
     * @param statusSet
     * @param sample
     */
    private static void setRecordStatus(StatusSet statusSet, Sample sample) {
        if ("H".equals(sample.getDomain())) {
            SampleHuman sampleHuman = new SampleHuman();
            sampleHuman.setSampleId(sample.getId());
            SampleHumanDAO sampleHumanDAO = new SampleHumanDAOImpl();
            sampleHumanDAO.getDataBySample(sampleHuman);

            String patientId = sampleHuman.getPatientId();

            statusSet.setSampleId(sample.getId());
            statusSet.setPatientId(patientId);

            if (patientId != null) {
                Patient patient = new Patient();
                patient.setId(patientId);

                List<ObservationHistory> observations = observationHistoryDAO.getAll(patient, sample);

                for (ObservationHistory observation : observations) {
                    if (observation.getObservationHistoryTypeId().equals(orderRecordStatusID)) {
                        statusSet.setSampleRecordStatus(getRecordStatusForID(observation.getValue()));
                    } else if (observation.getObservationHistoryTypeId().equals(patientRecordStatusID)) {
                        statusSet.setPatientRecordStatus(getRecordStatusForID(observation.getValue()));
                    }
                }
            }
        }
    }

    private static void insureMapsAreBuilt() {
        synchronized (StatusOfSampleUtil.class) {
            if (!mapsSet) {
                buildMap();
                mapsSet = true;
            }
        }
    }

    private static void buildMap() {
        orderStatusToObjectMap = new HashMap<OrderStatus, StatusOfSample>();
        sampleStatusToObjectMap = new HashMap<SampleStatus, StatusOfSample>();
        analysisStatusToObjectMap = new HashMap<AnalysisStatus, StatusOfSample>();
        recordStatusToObjectMap = new HashMap<RecordStatus, Dictionary>();
        idToOrderStatusMap = new HashMap<String, OrderStatus>();
        idToSampleStatusMap = new HashMap<String, SampleStatus>();
        idToAnalysisStatusMap = new HashMap<String, AnalysisStatus>();
        idToRecordStatusMap = new HashMap<String, RecordStatus>();

        buildStatusToIdMaps();

        //now put everything in the reverse map
        buildIdToStatusMapsFromStatusToIdMaps();

        getObservationHistoryTypeIDs();
    }

    @SuppressWarnings("unchecked")
    private static void buildStatusToIdMaps() {
        StatusOfSampleDAO statusOfSampleDAO = new StatusOfSampleDAOImpl();

        List<StatusOfSample> statusList = statusOfSampleDAO.getAllStatusOfSamples();

        //sorry about this but it is only done once and until Java 7 we have to use if..else
        for (StatusOfSample status : statusList) {
            if (status.getStatusType().equals("ORDER")) {
                addToOrderMap(status);
            } else if (status.getStatusType().equals("ANALYSIS")) {
                addToAnalysisMap(status);
            } else if (status.getStatusType().equals("SAMPLE")) {
                addToSampleMap(status);
            }
        }

        DictionaryDAO dictionaryDAO = new DictionaryDAOImpl();
        List<Dictionary> dictionaryList = dictionaryDAO.getDictionaryEntrysByCategoryName("REC_STATUS");

        for (Dictionary dictionary : dictionaryList) {
            addToRecordMap(dictionary);
        }
    }

    private static void buildIdToStatusMapsFromStatusToIdMaps() {
        for (Entry<OrderStatus, StatusOfSample> status : orderStatusToObjectMap.entrySet()) {
            idToOrderStatusMap.put(status.getValue().getId(), status.getKey());
        }
        for (Entry<SampleStatus, StatusOfSample> status : sampleStatusToObjectMap.entrySet()) {
            idToSampleStatusMap.put(status.getValue().getId(), status.getKey());
        }

        for (Entry<AnalysisStatus, StatusOfSample> status : analysisStatusToObjectMap.entrySet()) {
            idToAnalysisStatusMap.put(status.getValue().getId(), status.getKey());
        }
        for (Entry<RecordStatus, Dictionary> status : recordStatusToObjectMap.entrySet()) {
            idToRecordStatusMap.put(status.getValue().getId(), status.getKey());
        }
    }

    private static void getObservationHistoryTypeIDs() {
        ObservationHistoryTypeDAO observationTypeDAO = new ObservationHistoryTypeDAOImpl();
        List<ObservationHistoryType> obsrvationTypeList = observationTypeDAO.getAll();

        for (ObservationHistoryType observationType : obsrvationTypeList) {
            if ("SampleRecordStatus".equals(observationType.getTypeName())) {
                orderRecordStatusID = observationType.getId();
            } else if ("PatientRecordStatus".equals(observationType.getTypeName())) {
                patientRecordStatusID = observationType.getId();
            }
        }
    }

    private static void addToOrderMap(StatusOfSample status) {
        switch (status.getStatusOfSampleName()) {
            case "Test Entered":
                orderStatusToObjectMap.put(OrderStatus.Entered, status);
                break;
            case "Testing Started":
                orderStatusToObjectMap.put(OrderStatus.Started, status);
                break;
            case "Testing finished":
                orderStatusToObjectMap.put(OrderStatus.Finished, status);
                break;
            case "NonConforming":
                orderStatusToObjectMap.put(OrderStatus.NonConforming_depricated, status);
                break;
        }
    }

    private static void addToSampleMap(StatusOfSample status) {
        String name = status.getStatusOfSampleName();

        if (name.equals("SampleEntered")) {
            sampleStatusToObjectMap.put(SampleStatus.Entered, status);
        } else if (name.equals("SampleCanceled")) {
            sampleStatusToObjectMap.put(SampleStatus.Canceled, status);
        }
    }

    private static void addToAnalysisMap(StatusOfSample status) {
        switch (status.getStatusOfSampleName()) {
            case "Not Tested":
                analysisStatusToObjectMap.put(AnalysisStatus.NotTested, status);
                break;
            case "Test Canceled":
                analysisStatusToObjectMap.put(AnalysisStatus.Canceled, status);
                break;
            case "Technical Acceptance":
                analysisStatusToObjectMap.put(AnalysisStatus.TechnicalAcceptance, status);
                break;
            case "Technical Acceptance RO":
                analysisStatusToObjectMap.put(AnalysisStatus.TechnicalAcceptanceRO, status);
                break;
            case "Technical Rejected":
                analysisStatusToObjectMap.put(AnalysisStatus.TechnicalRejected, status);
                break;
            case "Biologist Rejection":
                analysisStatusToObjectMap.put(AnalysisStatus.BiologistRejected, status);
                break;
            case "Biologist Rejection RO":
                analysisStatusToObjectMap.put(AnalysisStatus.BiologistRejectedRO, status);
                break;
            case "referred out":
                analysisStatusToObjectMap.put(AnalysisStatus.ReferedOut, status);
                break;
            case "Finalized":
                analysisStatusToObjectMap.put(AnalysisStatus.Finalized, status);
                break;
            case "Finalized RO":
                analysisStatusToObjectMap.put(AnalysisStatus.FinalizedRO, status);
                break;
            case "Marked As Done":
                analysisStatusToObjectMap.put(AnalysisStatus.MarkedAsDone,status);
            case "NonConforming":
                analysisStatusToObjectMap.put(AnalysisStatus.NonConforming_depricated, status);
                break;
            case "referred in":
                analysisStatusToObjectMap.put(AnalysisStatus.ReferredIn, status);
                break;
        }
    }

    private static void addToRecordMap(Dictionary dictionary) {
        switch (dictionary.getLocalAbbreviation()) {
            case "Not Start":
                recordStatusToObjectMap.put(RecordStatus.NotRegistered, dictionary);
                break;
            case "Init Ent":
                recordStatusToObjectMap.put(RecordStatus.InitialRegistration, dictionary);
                break;
            case "Valid Ent":
                recordStatusToObjectMap.put(RecordStatus.ValidationRegistration, dictionary);
                break;
        }
    }

    public static String getStatusNameFromId(String id) {
        insureMapsAreBuilt();
        if (idToAnalysisStatusMap.get(id) != null) {
            return getStatusName(idToAnalysisStatusMap.get(id));
        } else if (idToOrderStatusMap.get(id) != null) {
            return getStatusName(idToOrderStatusMap.get(id));
        } else if (idToSampleStatusMap.get(id) != null) {
            return getStatusName(idToSampleStatusMap.get(id));
        } else if (idToRecordStatusMap.get(id) != null) {
            return getStatusName(idToRecordStatusMap.get(id));
        }

        return null;
    }
}

package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.openelis.domain.TestResultDetails;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.test.valueholder.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestResultService {

    public TestResultDetails detailsFor(String resultId) {
        TestResultDetails testResultDetails = new TestResultDetails();
        Map<String, Object> extraDetails = new HashMap<>();
        try {
            Connection connection = HibernateUtil.getSession().connection();
            PreparedStatement query = connection.prepareStatement(getSqlForResultDetails(resultId));
            ResultSet resultSet = query.executeQuery();
            mapDetailsFromResultSet(resultSet, testResultDetails, extraDetails);

            addDictionaryValueIfRequired(testResultDetails);
            addNotes(resultId, testResultDetails);

            if (testResultDetails.getResultType().equals("N")) {
                addResultLimitsAlert(testResultDetails, extraDetails);
            }

            return testResultDetails;
        } catch (Exception e) {
            throw new LIMSRuntimeException("Failed in querying details about result: " + resultId, e);
        }
    }

    private void addResultLimitsAlert(TestResultDetails testResultDetails, Map<String, Object> extraDetails) {
        Test test = new Test();
        test.setId((String) extraDetails.get("testId"));

        Patient patient = new Patient();
        patient.setBirthDate((Timestamp) extraDetails.get("patientBirthDate"));
        patient.setGender((String) extraDetails.get("patientGender"));
        ResultLimit resultLimit = new ResultsLoadUtility().getResultLimitForTestAndPatient(test, patient);

        double result = Double.parseDouble(testResultDetails.getResult());
        if (result > resultLimit.getHighNormal()) {
            testResultDetails.setAlerts("A");
        } else if(result < resultLimit.getLowNormal()) {
            testResultDetails.setAlerts("B");
        }
    }

    private String getSqlForResultDetails(String resultId) {
        return "select \n" +
                "    sample.uuid as orderId,\n" +
                "    sample.accession_number as accessionNumber,\n" +
                "    patient.uuid as patientExternalId,\n" +
                "    person.first_name as patientFirstName,\n" +
                "    person.last_name as patientLastName,\n" +
                "    test.name as testName,\n" +
                "    unit_of_measure.name as testUnitOfMeasurement,\n" +
                "    external_reference.external_id as testExternalId, \n" +
                "    result.id as resultId,\n" +
                "    result.value as result,\n" +
                "    result.result_type as resultType,\n" +
                "    test.id as testId,\n" +
                "    patient.gender as patientGender,\n" +
                "    patient.birth_date as patientBirthDate " +
                "from result\n" +
                "left join analysis on analysis.id = result.analysis_id\n" +
                "left join sample_item on sample_item.id = analysis.sampitem_id\n" +
                "left join test on test.id = analysis.test_id\n" +
                "left join sample on sample.id = sample_item.samp_id\n" +
                "left join unit_of_measure on unit_of_measure.id = test.uom_id\n" +
                "left join sample_human on sample_human.samp_id = sample.id\n" +
                "left join patient on patient.id = sample_human.patient_id\n" +
                "left join person on person.id = patient.person_id\n" +
                "left join external_reference on external_reference.item_id = test.id and external_reference.type='Test'" +
                "where result.id = " + resultId + ";\n";
    }

    private void addNotes(String resultId, TestResultDetails testResultDetails) {
        List<Note> notes = new NoteDAOImpl().getNoteByRefIAndRefTableAndSubject(resultId, ResultsLoadUtility.getResultReferenceTableId(), "Result Note");
        for (Note note : notes) {
            testResultDetails.addNotes(note.getText());
        }
    }

    private void addDictionaryValueIfRequired(TestResultDetails testResultDetails) {
        if (testResultDetails.getResultType() != null && testResultDetails.getResultType().equals("D")) {
            Dictionary dictionary = new DictionaryDAOImpl().getDataForId(testResultDetails.getResult());
            testResultDetails.setResult(dictionary.getDictEntry());
        }
    }

    private void mapDetailsFromResultSet(ResultSet resultSet, TestResultDetails testResultDetails, Map<String, Object> extraDetails) throws SQLException {
        while (resultSet.next()) {
            testResultDetails.setAccessionNumber(resultSet.getString("accessionNumber"));
            testResultDetails.setOrderId(resultSet.getString("orderId"));
            testResultDetails.setResult(resultSet.getString("result"));
            testResultDetails.setTestName(resultSet.getString("testName"));
            testResultDetails.setPatientExternalId(resultSet.getString("patientExternalId"));
            testResultDetails.setPatientFirstName(resultSet.getString("patientFirstName"));
            testResultDetails.setPatientLastName(resultSet.getString("patientLastName"));
            testResultDetails.setTestUnitOfMeasurement(resultSet.getString("testUnitOfMeasurement"));
            testResultDetails.setTestExternalId(resultSet.getString("testExternalId"));
            testResultDetails.setResultId(resultSet.getString("resultId"));
            testResultDetails.setResultType(resultSet.getString("resultType"));

            extraDetails.put("testId", resultSet.getString("testId"));
            extraDetails.put("patientGender", resultSet.getString("patientGender"));
            extraDetails.put("patientBirthDate", resultSet.getTimestamp("patientBirthDate"));
        }
    }
}

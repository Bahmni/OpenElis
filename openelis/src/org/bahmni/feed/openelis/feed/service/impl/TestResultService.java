package org.bahmni.feed.openelis.feed.service.impl;

import org.bahmni.openelis.domain.TestResultDetails;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TestResultService {

    public TestResultDetails detailsFor(String resultId) throws SQLException {
        TestResultDetails testResultDetails = new TestResultDetails();
        PreparedStatement query = null;
        ResultSet resultSet = null;
        try {
            Connection connection = HibernateUtil.getSession().connection();
            query = connection.prepareStatement(getSqlForResultDetails(resultId));
            resultSet = query.executeQuery();
            mapDetailsFromResultSet(resultSet, testResultDetails);

            addDictionaryValueIfRequired(testResultDetails);
            addNotes(resultId, testResultDetails);

            if (testResultDetails.getResultType().equals("N")) {
                addResultLimitsAlert(testResultDetails);
            }

            query.close();
            return testResultDetails;
        } catch (Exception e) {
            throw new LIMSRuntimeException("Failed in querying details about result: " + resultId, e);
        } finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if (query != null) {
                query.close();
            }
        }
    }

    private void addResultLimitsAlert(TestResultDetails testResultDetails) {
        double result = Double.parseDouble(testResultDetails.getResult());
        if(hasNoResultLimits(testResultDetails)) {
            return;
        }
        if (result > testResultDetails.getMaxNormal()) {
            testResultDetails.setAlerts("A");
        } else if(result < testResultDetails.getMinNormal()) {
            testResultDetails.setAlerts("B");
        }
    }

    private boolean hasNoResultLimits(TestResultDetails testResultDetails) {
        return testResultDetails.getMinNormal() == 0 && testResultDetails.getMaxNormal() == 0;
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
                "    exref_test.external_id as testExternalId, \n" +
                "    exref_panel.external_id as panelExternalId, \n" +
                "    result.id as resultId,\n" +
                "    result.value as result,\n" +
                "    result.result_type as resultType,\n" +
                "    result.min_normal as minNormal,\n" +
                "    result.max_normal as maxNormal,\n" +
                "    test.id as testId,\n" +
                "    patient.gender as patientGender,\n" +
                "    patient.birth_date as patientBirthDate " +
                "from result\n" +
                "left join analysis on analysis.id = result.analysis_id\n" +
                "left join sample_item on sample_item.id = analysis.sampitem_id\n" +
                "left join test on test.id = analysis.test_id\n" +
                "left join panel on panel.id = analysis.panel_id\n" +
                "left join sample on sample.id = sample_item.samp_id\n" +
                "left join unit_of_measure on unit_of_measure.id = test.uom_id\n" +
                "left join sample_human on sample_human.samp_id = sample.id\n" +
                "left join patient on patient.id = sample_human.patient_id\n" +
                "left join person on person.id = patient.person_id\n" +
                "left join external_reference exref_test on exref_test.item_id = test.id and exref_test.type='Test'\n" +
                "left join external_reference exref_panel on exref_panel.item_id = panel.id and exref_panel.type='Panel'\n" +
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

    private void mapDetailsFromResultSet(ResultSet resultSet, TestResultDetails testResultDetails) throws SQLException {
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
            testResultDetails.setPanelExternalId(resultSet.getString("panelExternalId"));
            testResultDetails.setResultId(resultSet.getString("resultId"));
            testResultDetails.setResultType(resultSet.getString("resultType"));
            testResultDetails.setMinNormal(resultSet.getDouble("minNormal"));
            testResultDetails.setMaxNormal(resultSet.getDouble("maxNormal"));
        }
    }
}

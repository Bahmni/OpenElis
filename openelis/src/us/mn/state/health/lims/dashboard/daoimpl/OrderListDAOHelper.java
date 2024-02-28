package us.mn.state.health.lims.dashboard.daoimpl;

import org.apache.commons.lang3.StringUtils;
import us.mn.state.health.lims.dashboard.valueholder.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderListDAOHelper {
    private static String COMMENT_SEPARATOR = "<~~>";
    private static final String LOW = "Low";
    private static final String HIGH = "High";
    private Boolean isGroupBySample;

    public OrderListDAOHelper(Boolean groupBySampleEnabled) {
        this.isGroupBySample = groupBySampleEnabled;
    }

    String createSqlForToday(String condition, String orderBy, String pendingAnalysisStatus,
                             String pendingValidationAnalysisStatus, String referredAnalysisStatus, String completedStatus) {
        if (isGroupBySample) {
            return createSqlStringForTodayOrdersANDGroupBySampleEnabled(condition, orderBy, pendingAnalysisStatus,
                    pendingValidationAnalysisStatus,referredAnalysisStatus, completedStatus);
        }
        return createSqlStringForTodayOrders(condition, orderBy, pendingAnalysisStatus,
                pendingValidationAnalysisStatus, referredAnalysisStatus,completedStatus);
    }

    String createSqlForPendingBeforeToday(String condition, String orderBy, String pendingAnalysisStatus,
                                          String pendingValidationAnalysisStatus, String referredAnalysisStatus,String analysesReferredOrInFinalStatus) {
        if (isGroupBySample) {
            return createSqlStringForPendingOrdersANDGroupBySampleEnabled(condition, orderBy, pendingAnalysisStatus,
                    pendingValidationAnalysisStatus, referredAnalysisStatus,analysesReferredOrInFinalStatus);
        }
        return createSqlStringForPendingOrders(condition, orderBy, pendingAnalysisStatus, pendingValidationAnalysisStatus,referredAnalysisStatus,
                analysesReferredOrInFinalStatus);
    }

    private String createSqlStringForPendingOrders(String condition, String OrderBy, String pendingAnalysisStatus,
                                                   String pendingValidationAnalysisStatus,String referredAnalysisStatus, String analysesReferredOrInFinalStatus) {
        return "SELECT \n" +
                "sample.accession_number AS accession_number, \n" +
                "string_agg(test_section.name, '" + COMMENT_SEPARATOR + "') AS section_names, \n" +
                "sample.uuid AS uuid, \n" +
                "sample.id AS id, \n" +
                "sample.collection_date AS collection_date, \n" +
                "sample.entered_date AS entered_date, \n" +
                "person.first_name AS first_name, \n" +
                "person.middle_name AS middle_name, \n" +
                "person.last_name AS last_name, \n" +
                "patient_identity.identity_data AS st_number, \n" +
                "sample_source.name AS sample_source, \n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + pendingAnalysisStatus + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + pendingValidationAnalysisStatus + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + referredAnalysisStatus + ") THEN 1 ELSE 0 END) as referred_tests_count,\n" +
                "string_agg(analysis.comment, '" + COMMENT_SEPARATOR + "') AS analysis_comments,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN document_track.report_generation_time is null THEN false ELSE true END as is_printed\n" +
                "FROM Sample AS sample\n" +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id \n" +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id \n" +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id \n" +
                "INNER JOIN Person AS person ON patient.person_id = person.id \n" +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id \n" +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' \n" +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id \n" +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id and analysis.status_id not in (" + analysesReferredOrInFinalStatus + ") and analysis.lastupdated < ?\n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "INNER JOIN test_section ON test.test_section_id = test_section.id \n" +
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' and document_track.parent_id is null\n" +
                "WHERE " + condition + "\n" +
                "GROUP BY sample.accession_number, sample.uuid,sample.id, sample.collection_date, person.first_name, person.middle_name, person.last_name, sample_source.name, patient_identity.identity_data, document_track.report_generation_time\n" +
                "ORDER BY " + OrderBy + " DESC\n" +
                "LIMIT 1000;";
    }

    private String createSqlStringForTodayOrders(String condition, String OrderBy, String pendingAnalysisStatus,
                                                 String pendingValidationAnalysisStatus, String referredAnalysisStatus,String completedStatus) {
        return "SELECT \n" +
                "sample.accession_number AS accession_number, \n" +
                "sample.uuid AS uuid, \n" +
                "string_agg(test_section.name, '" + COMMENT_SEPARATOR + "') AS section_names, \n" +
                "sample.id AS id, \n" +
                "sample.collection_date AS collection_date, \n" +
                "sample.entered_date AS entered_date, \n" +
                "person.first_name AS first_name, \n" +
                "person.middle_name AS middle_name, \n" +
                "person.last_name AS last_name, \n" +
                "patient_identity.identity_data AS st_number, \n" +
                "sample_source.name AS sample_source, \n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + pendingAnalysisStatus + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + pendingValidationAnalysisStatus + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + referredAnalysisStatus + ") THEN 1 ELSE 0 END) as referred_tests_count,\n" +
                "string_agg(analysis.comment, '" + COMMENT_SEPARATOR + "') AS analysis_comments,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN COUNT(analysis.id) = SUM(CASE WHEN  analysis.status_id IN (" + completedStatus + ") THEN 1 ELSE 0 END) THEN true ELSE false END as is_completed,\n" +
                "CASE WHEN document_track.report_generation_time is null THEN false ELSE true END as is_printed\n" +
                "FROM Sample AS sample\n" +
                "INNER JOIN (\n" +
                "SELECT DISTINCT si.samp_id as id\n" +
                "FROM analysis a INNER JOIN sample_item si ON a.sampitem_id = si.id\n" +
                "WHERE a.lastupdated >= ?\n" +
                "UNION\n" +
                "SELECT DISTINCT s.id as id\n" +
                "FROM sample s\n" +
                "WHERE s.lastupdated >= ?\n" +
                ") list on list.id = sample.id\n" +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id \n" +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id \n" +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id \n" +
                "INNER JOIN Person AS person ON patient.person_id = person.id \n" +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id \n" +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' \n" +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id \n" +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id \n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "INNER JOIN test_section ON test.test_section_id = test_section.id \n" +
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' and document_track.parent_id is null \n" +
                "WHERE " + condition + "\n" +
                "GROUP BY sample.accession_number, sample.uuid,sample.id, sample.collection_date, sample.lastupdated, person.first_name, person.middle_name, person.last_name, sample_source.name, patient_identity.identity_data, document_track.report_generation_time \n" +
                "ORDER BY " + OrderBy + " DESC\n" +
                "LIMIT 1000;";
    }

    private String createSqlStringForPendingOrdersANDGroupBySampleEnabled(String condition, String OrderBy, String pendingAnalysisStatus,
                                                                          String pendingValidationAnalysisStatus, String referredAnalysisStatus,String analysesReferredOrInFinalStatus) {
        return "SELECT \n" +
                "sample.accession_number AS accession_number, \n" +
                "string_agg(test_section.name, '" + COMMENT_SEPARATOR + "') AS section_names, \n" +
                "sample.uuid AS uuid, \n" +
                "sample.id AS id, \n" +
                "sample.collection_date AS collection_date, \n" +
                "sample.entered_date AS entered_date, \n" +
                "person.first_name AS first_name, \n" +
                "person.middle_name AS middle_name, \n" +
                "person.last_name AS last_name, \n" +
                "patient_identity.identity_data AS st_number, \n" +
                "sample_source.name AS sample_source, \n" +
                "sample.priority AS priority,\n" +
                "type_of_sample.description AS sample_type, \n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + pendingAnalysisStatus + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + pendingValidationAnalysisStatus + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + referredAnalysisStatus + ") THEN 1 ELSE 0 END) as referred_tests_count,\n" +
                "string_agg(nullif(analysis.comment, ''), '" + COMMENT_SEPARATOR + "') AS analysis_comments,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN document_track.report_generation_time is null THEN false ELSE true END as is_printed\n" +
                "FROM Sample AS sample\n" +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id \n" +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id \n" +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id \n" +
                "INNER JOIN Person AS person ON patient.person_id = person.id \n" +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id \n" +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' \n" +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id \n" +
                "INNER JOIN type_of_sample on sample_item.typeosamp_id = type_of_sample.id \n" +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id and analysis.status_id not in (" + analysesReferredOrInFinalStatus + ") and analysis.lastupdated < ?\n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "INNER JOIN test_section ON test.test_section_id = test_section.id \n" +
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' and document_track.parent_id is null\n" +
                "WHERE " + condition + "\n" +
                "GROUP BY sample.accession_number, sample.uuid,sample.id, sample.collection_date, person.first_name,sample.lastupdated, person.middle_name, person.last_name, sample_source.name, patient_identity.identity_data, document_track.report_generation_time, type_of_sample.description\n" +
                "ORDER BY " + OrderBy + "\n" +
                "LIMIT 1000;";
    }

    private String createSqlStringForTodayOrdersANDGroupBySampleEnabled(String condition, String OrderBy, String pendingAnalysisStatus,
                                                                        String pendingValidationAnalysisStatus, String referredAnalysisStatus,String completedStatus) {
        return "SELECT \n" +
                "sample.accession_number AS accession_number, \n" +
                "sample.uuid AS uuid, \n" +
                "string_agg(test_section.name, '" + COMMENT_SEPARATOR + "') AS section_names, \n" +
                "sample.id AS id, \n" +
                "sample.collection_date AS collection_date, \n" +
                "sample.entered_date AS entered_date, \n" +
                "person.first_name AS first_name, \n" +
                "person.middle_name AS middle_name, \n" +
                "person.last_name AS last_name, \n" +
                "patient_identity.identity_data AS st_number, \n" +
                "sample_source.name AS sample_source, \n" +
                "sample.priority AS priority,\n" +
                "type_of_sample.description AS sample_type, \n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + pendingAnalysisStatus + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + pendingValidationAnalysisStatus + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + referredAnalysisStatus + ") THEN 1 ELSE 0 END) as referred_tests_count,\n" +
                "string_agg(nullif(analysis.comment, ''), '" + COMMENT_SEPARATOR + "') AS analysis_comments,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN COUNT(analysis.id) = SUM(CASE WHEN  analysis.status_id IN (" + completedStatus + ") THEN 1 ELSE 0 END) THEN true ELSE false END as is_completed,\n" +
                "CASE WHEN document_track.report_generation_time is null THEN false ELSE true END as is_printed\n" +
                "FROM Sample AS sample\n" +
                "INNER JOIN (\n" +
                "SELECT DISTINCT si.samp_id as id\n" +
                "FROM analysis a INNER JOIN sample_item si ON a.sampitem_id = si.id\n" +
                "WHERE a.lastupdated >= ?\n" +
                "UNION\n" +
                "SELECT DISTINCT s.id as id\n" +
                "FROM sample s\n" +
                "WHERE s.lastupdated >= ?\n" +
                ") list on list.id = sample.id\n" +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id \n" +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id \n" +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id \n" +
                "INNER JOIN Person AS person ON patient.person_id = person.id \n" +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id \n" +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' \n" +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id \n" +
                "INNER JOIN type_of_sample on sample_item.typeosamp_id = type_of_sample.id \n" +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id \n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "INNER JOIN test_section ON test.test_section_id = test_section.id \n" +
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' and document_track.parent_id is null \n" +
                "WHERE " + condition + "\n" +
                "GROUP BY sample.accession_number, sample.uuid,sample.id, sample.collection_date, sample.lastupdated, person.first_name, person.middle_name, person.last_name, sample_source.name, patient_identity.identity_data, document_track.report_generation_time, type_of_sample.description \n" +
                "ORDER BY " + OrderBy + " DESC\n" +
                "LIMIT 1000;";
    }

    Order getOrder(ResultSet accessionResultSet, String comments, String sectionNames, boolean completed) throws SQLException {
        if (isGroupBySample) {
            return getOrderWithPriority(accessionResultSet, comments, sectionNames, getPriority(accessionResultSet), completed);
        }
        return getOrderWithoutPriority(accessionResultSet, comments, sectionNames, completed);
    }

    private String getPriority(ResultSet accessionResultSet) throws SQLException {
        String priority = accessionResultSet.getString("priority");

        if(StringUtils.isEmpty(priority)) {
            return "";
        }

        return priority.equals("1") ? HIGH : LOW;
    }

    private Order getOrderWithPriority(ResultSet accessionResultSet, String comments, String sectionNames,
                                       String priority, boolean completed) throws SQLException {
        return new Order(accessionResultSet.getString("accession_number"),
                accessionResultSet.getString("uuid"),
                accessionResultSet.getString("id"),
                accessionResultSet.getString("st_number"),
                accessionResultSet.getString("first_name"),
                accessionResultSet.getString("middle_name"),
                accessionResultSet.getString("last_name"),
                accessionResultSet.getString("sample_source"),
                completed,
                accessionResultSet.getBoolean("is_printed"),
                accessionResultSet.getInt("pending_tests_count"),
                accessionResultSet.getInt("pending_validation_count"),
                accessionResultSet.getInt("referred_tests_count"),
                accessionResultSet.getInt("total_test_count"),
                accessionResultSet.getDate("collection_date"),
                accessionResultSet.getDate("entered_date"),
                comments,
                sectionNames,
                accessionResultSet.getString("sample_type"),
                priority
        );
    }

    private Order getOrderWithoutPriority(ResultSet accessionResultSet, String comments,
                                          String sectionNames, boolean completed) throws SQLException {
        return new Order(accessionResultSet.getString("accession_number"),
                accessionResultSet.getString("uuid"),
                accessionResultSet.getString("id"),
                accessionResultSet.getString("st_number"),
                accessionResultSet.getString("first_name"),
                accessionResultSet.getString("middle_name"),
                accessionResultSet.getString("last_name"),
                accessionResultSet.getString("sample_source"),
                completed,
                accessionResultSet.getBoolean("is_printed"),
                accessionResultSet.getInt("pending_tests_count"),
                accessionResultSet.getInt("pending_validation_count"),
                accessionResultSet.getInt("referred_tests_count"),
                accessionResultSet.getInt("total_test_count"),
                accessionResultSet.getDate("collection_date"),
                accessionResultSet.getDate("entered_date"),
                comments,
                sectionNames
        );
    }
}

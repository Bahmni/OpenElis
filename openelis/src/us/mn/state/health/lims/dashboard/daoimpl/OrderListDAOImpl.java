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

package us.mn.state.health.lims.dashboard.daoimpl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.dashboard.dao.OrderListDAO;
import us.mn.state.health.lims.dashboard.valueholder.Order;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.lang.Integer.parseInt;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.BiologistRejected;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.Finalized;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.FinalizedRO;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.NotTested;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.ReferedOut;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.ReferredIn;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.TechnicalRejected;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.getStatusID;

public class OrderListDAOImpl implements OrderListDAO {

    private Logger logger = LogManager.getLogger(OrderListDAOImpl.class);

    private static String COMMENT_SEPARATOR = "<~~>";

    public OrderListDAOImpl() {
    }

    @Override
    public List<Order> getAllToday() {
        List<Order> orderList = new ArrayList<>();
        String condition = "sample.accession_number is not null and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ") ";
        String sqlForAllTestsToday = createSqlStringForTodayOrders(condition, "sample.accession_number");
        PreparedStatement preparedStatement = null;
        ResultSet todayAccessions = null;
        try {
            preparedStatement = getPreparedStatement(sqlForAllTestsToday);
            //Dont'use current_date in prepared_statement. I know its weird, but
            //The session fires query with current_date = date_on_which_session_was_created and gives wron result on next daypreparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            preparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            preparedStatement.setTimestamp(2, DateUtil.getTodayAsTimestamp());
            todayAccessions = preparedStatement.executeQuery();
            while (todayAccessions.next()) {
                orderList.add(createOrder(todayAccessions, todayAccessions.getBoolean("is_completed")));
            }
            return orderList;
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        } finally {
            closeResultSet(todayAccessions);
            closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public List<Order> getAllPendingBeforeToday() {
        String condition = "sample.accession_number is not null and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ")";
        return getOrders(createSqlStringForPendingOrders(condition, "sample.accession_number"));
    }

    @Override
    public List<Order> getAllSampleNotCollectedToday() {
        List<Order> orderList = new ArrayList<>();
        String sqlForAllSampleNotCollectedToday = createSqlStringForTodayOrders("sample.accession_number is null and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ")", "sample.lastupdated");

        ResultSet sampleNotCollectedToday = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = getPreparedStatement(sqlForAllSampleNotCollectedToday);
            preparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            preparedStatement.setTimestamp(2, DateUtil.getTodayAsTimestamp());
            sampleNotCollectedToday = preparedStatement.executeQuery();
            while (sampleNotCollectedToday.next()) {
                Order order = createOrder(sampleNotCollectedToday, false);
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            logger.error("Error closing resultSet", e);
            throw new LIMSRuntimeException(e);
        } finally {
            closeResultSet(sampleNotCollectedToday);
            closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public List<Order> getAllSampleNotCollectedPendingBeforeToday() {
        String sqlForAllSampleNotCollectedPendingBeforeToday = createSqlStringForPendingOrders("sample.accession_number is null and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ")", "sample.lastupdated");
        return getOrders(sqlForAllSampleNotCollectedPendingBeforeToday);
    }

    private List<Order> getOrders( String sql) {
        List<Order> orderList = new ArrayList<>();
        ResultSet pendingAccessions = null;
        PreparedStatement preparedStatement = null;

        try {
            //Dont'use current_date in prepared_statement. I know its weird, but
            //The session fires query with current_date = date_on_which_session_was_created and gives wron result on next daypreparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            preparedStatement = getPreparedStatement(sql);
            preparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            pendingAccessions = preparedStatement.executeQuery();
            while (pendingAccessions.next()) {
                Order order = createOrder(pendingAccessions, false);
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            logger.error("Error closing resultSet", e);
            throw new LIMSRuntimeException(e);
        } finally {
            closeResultSet(pendingAccessions);
            closePreparedStatement(preparedStatement);
        }
    }

    private void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error("Error closing statement", e);
            }
        }
    }

    private void closeResultSet(ResultSet pendingAccessions) {
        if (pendingAccessions != null) {
            try {
                pendingAccessions.close();
            } catch (SQLException e) {
                logger.error("Error closing resultSet", e);
            }
        }
    }

    private PreparedStatement getPreparedStatement(String sqlForAllTestsToday) throws SQLException {
        Connection connection = HibernateUtil.getSession().connection();
        return connection.prepareStatement(sqlForAllTestsToday);
    }

    private Order createOrder(ResultSet accessionResultSet, boolean completed) throws SQLException {
        String comments = getUniqueComments(accessionResultSet);
        String sectionNames = getUniqueSectionNames(accessionResultSet);
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
                            accessionResultSet.getInt("total_test_count"),
                            accessionResultSet.getDate("collection_date"),
                            accessionResultSet.getDate("entered_date"),
                            comments,
                            sectionNames
        );
    }

    private String getUniqueSectionNames(ResultSet accessionResultSet) throws SQLException {
        String sectionNames = accessionResultSet.getString("section_names");
        return StringUtils.isNotBlank(sectionNames) ? getUniqueValueAsCSV(sectionNames.split(COMMENT_SEPARATOR)) : "";
    }

    private String getUniqueValueAsCSV(String[] holders) {
        String[] unique = new HashSet<String>(Arrays.asList(holders)).toArray(new String[0]);
        return StringUtils.join(unique, ",");
    }

    private String getUniqueComments(ResultSet accessionResultSet) throws SQLException {
        String analysis_comments = accessionResultSet.getString("analysis_comments");
        if(StringUtils.isNotBlank(analysis_comments)) {
            String[] comments = analysis_comments.split(COMMENT_SEPARATOR);
            String[] unique = new HashSet<String>(Arrays.asList(comments)).toArray(new String[0]);
            return StringUtils.join(unique, ",");
        }else{
            return "";
        }
    }

    private String getCompletedStatus() {
        return getStatusID(Finalized); //6
    }

    private String getAllNonReferredAnalysisStatus() {
        return getAnalysisStatus(BiologistRejected, getStatusID(NotTested), TechnicalAcceptance, TechnicalRejected, Finalized);
    }

    private String getPendingValidationAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalAcceptance)));//16
        return StringUtils.join(inProgressAnalysisStatus.iterator(), ',');
    }

    private String getPendingAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(NotTested)));//4
        inProgressAnalysisStatus.add(parseInt(getStatusID(BiologistRejected)));//7
        return StringUtils.join(inProgressAnalysisStatus.iterator(), ',');
    }

    private String analysesReferredOrInFinalStatus() {
        return getAnalysisStatus(ReferedOut, getStatusID(ReferedOut), ReferredIn, Finalized, FinalizedRO);
    }

    private String getAnalysisStatus(StatusOfSampleUtil.AnalysisStatus referedOut, String statusID, StatusOfSampleUtil.AnalysisStatus referredIn, StatusOfSampleUtil.AnalysisStatus finalized, StatusOfSampleUtil.AnalysisStatus finalizedRO) {
        List<Object> analysisStatuses = new ArrayList<>();
        analysisStatuses.add(parseInt(getStatusID(referedOut)));
        analysisStatuses.add(parseInt(statusID));
        analysisStatuses.add(parseInt(getStatusID(referredIn)));
        analysisStatuses.add(parseInt(getStatusID(finalized)));
        analysisStatuses.add(parseInt(getStatusID(finalizedRO)));
        return StringUtils.join(analysisStatuses.iterator(), ',');
    }

    private String createSqlStringForPendingOrders(String condition, String OrderBy) {
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
                "SUM(CASE WHEN  analysis.status_id IN (" + getPendingAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN ("+ getPendingValidationAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
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
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id and analysis.status_id not in (" +  analysesReferredOrInFinalStatus() + ") and analysis.lastupdated < ?\n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "INNER JOIN test_section ON test.test_section_id = test_section.id \n"+
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' and document_track.parent_id is null\n" +
                "WHERE "+condition+"\n" +
                "GROUP BY sample.accession_number, sample.uuid,sample.id, sample.collection_date, person.first_name, person.middle_name, person.last_name, sample_source.name, patient_identity.identity_data, document_track.report_generation_time\n" +
                "ORDER BY "+ OrderBy +" DESC\n" +
                "LIMIT 1000;";
    }

    private String createSqlStringForTodayOrders(String condition, String OrderBy) {
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
                "SUM(CASE WHEN  analysis.status_id IN (" + getPendingAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN ("+ getPendingValidationAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "string_agg(analysis.comment, '" + COMMENT_SEPARATOR + "') AS analysis_comments,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN COUNT(analysis.id) = SUM(CASE WHEN  analysis.status_id IN (" +getCompletedStatus()+ ") THEN 1 ELSE 0 END) THEN true ELSE false END as is_completed,\n" +
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
                "INNER JOIN test_section ON test.test_section_id = test_section.id \n"+
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' and document_track.parent_id is null \n" +
                "WHERE "+condition+"\n" +
                "GROUP BY sample.accession_number, sample.uuid,sample.id, sample.collection_date, sample.lastupdated, person.first_name, person.middle_name, person.last_name, sample_source.name, patient_identity.identity_data, document_track.report_generation_time \n" +
                "ORDER BY "+ OrderBy +" DESC\n" +
                "LIMIT 1000;";
    }
}

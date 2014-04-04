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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.*;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.getStatusID;

public class OrderListDAOImpl implements OrderListDAO {

    private Logger logger = LogManager.getLogger(OrderListDAOImpl.class);

    public OrderListDAOImpl() {
    }

    @Override
    public List<Order> getAllToday() {
        List<Order> orderList = new ArrayList<>();
        String sqlForAllTestsToday = "SELECT \n" +
                "sample.accession_number AS accession_number, \n" +
                "sample.collection_date AS collection_date, \n" +
                "person.first_name AS first_name, \n" +
                "person.middle_name AS middle_name, \n" +
                "person.last_name AS last_name, \n" +
                "patient_identity.identity_data AS st_number, \n" +
                "sample_source.name AS sample_source, \n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + getPendingAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN ("+ getPendingValidationAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN COUNT(analysis.id) = SUM(CASE WHEN  analysis.status_id IN (" +getCompletedStatus()+ ") THEN 1 ELSE 0 END) THEN true ELSE false END as is_completed,\n" +
                "CASE WHEN document_track.report_generation_time is null THEN false ELSE true END as is_printed\n" +
                "FROM Sample AS sample\n" +
                "INNER JOIN (select distinct s.id from analysis a inner join sample_item si on a.sampitem_id = si.id inner join sample s on si.samp_id = s.id where a.lastupdated >= ?) list on list.id = sample.id\n" +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id \n" +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id \n" +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id \n" +
                "INNER JOIN Person AS person ON patient.person_id = person.id \n" +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id \n" +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' \n" +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id \n" +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id \n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' and document_track.parent_id is null \n" +
                "WHERE analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ") \n" +
                "GROUP BY sample.accession_number, sample.collection_date, sample.lastupdated, person.first_name, person.middle_name, person.last_name, sample_source.name, patient_identity.identity_data, document_track.report_generation_time \n" +
                "ORDER BY sample.accession_number DESC\n" +
                "LIMIT 1000;";
        PreparedStatement preparedStatement = null;
        ResultSet todayAccessions = null;
        try {
            preparedStatement = getPreparedStatement(sqlForAllTestsToday);
            //Dont'use current_date in prepared_statement. I know its weird, but
            //The session fires query with current_date = date_on_which_session_was_created and gives wron result on next daypreparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            preparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
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
        List<Order> orderList = new ArrayList<>();
        String sqlForAllTestsToday = "SELECT \n" +
                "sample.accession_number AS accession_number, \n" +
                "sample.collection_date AS collection_date, \n" +
                "person.first_name AS first_name, \n" +
                "person.middle_name AS middle_name, \n" +
                "person.last_name AS last_name, \n" +
                "patient_identity.identity_data AS st_number, \n" +
                "sample_source.name AS sample_source, \n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + getPendingAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN ("+ getPendingValidationAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN document_track.report_generation_time is null THEN false ELSE true END as is_printed\n" +
                "FROM Sample AS sample\n" +
                "inner join (select distinct s.id from analysis a inner join sample_item si on a.sampitem_id = si.id inner join sample s on si.samp_id = s.id where a.status_id not in (" +  analysesReferredOrInFinalStatus() + ") and s.lastupdated < ?) x on x.id = sample.id \n" +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id \n" +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id \n" +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id \n" +
                "INNER JOIN Person AS person ON patient.person_id = person.id \n" +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id \n" +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' \n" +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id \n" +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id \n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' and document_track.parent_id is null\n" +
                "WHERE analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ") \n" +
                "GROUP BY sample.accession_number, sample.collection_date, person.first_name, person.middle_name, person.last_name, sample_source.name, patient_identity.identity_data, document_track.report_generation_time\n" +
                "ORDER BY sample.accession_number DESC\n" +
                "LIMIT 1000;";

        ResultSet pendingAccessions = null;
        PreparedStatement preparedStatement = null;
        try {
            //Dont'use current_date in prepared_statement. I know its weird, but
            //The session fires query with current_date = date_on_which_session_was_created and gives wron result on next daypreparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            preparedStatement = getPreparedStatement(sqlForAllTestsToday);
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
        return new Order(accessionResultSet.getString("accession_number"),
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
                            accessionResultSet.getDate("collection_date"));
    }

    private String getCompletedStatus() {
        return getStatusID(Finalized); //6
    }

    private String getAllNonReferredAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(BiologistRejected)));//7
        inProgressAnalysisStatus.add(parseInt(getStatusID(NotTested)));//4
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalAcceptance)));//16
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalRejected)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(Finalized)));//6
        return StringUtils.join(inProgressAnalysisStatus.iterator(), ',');
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
        List<Object> analysisStatuses = new ArrayList<>();
        analysisStatuses.add(parseInt(getStatusID(ReferedOut)));
        analysisStatuses.add(parseInt(getStatusID(Canceled)));
        analysisStatuses.add(parseInt(getStatusID(ReferedOut)));
        analysisStatuses.add(parseInt(getStatusID(ReferredIn)));
        analysisStatuses.add(parseInt(getStatusID(Finalized)));
        analysisStatuses.add(parseInt(getStatusID(FinalizedRO)));
        return StringUtils.join(analysisStatuses.iterator(), ',');
    }
}

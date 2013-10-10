package us.mn.state.health.lims.dashboard.daoimpl;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.Date;
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
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.BiologistRejected;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.Finalized;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.NotTested;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.TechnicalRejected;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.getStatusID;

public class OrderListDAOImpl implements OrderListDAO {

    public OrderListDAOImpl() {
    }

    @Override
    public List<Order> getAllToday() {
        List<Order> orderList = new ArrayList<>();
        String sqlForAllTestsToday = "SELECT \n" +
                "sample.accession_number AS accession_number, \n" +
                "sample.collection_date AS collection_date, \n" +
                "person.first_name AS first_name, \n" +
                "person.last_name AS last_name, \n" +
                "patient_identity.identity_data AS st_number, \n" +
                "sample_source.name AS sample_source, \n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + getPendingAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN ("+ getPendingValidationAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN COUNT(analysis.id) = SUM(CASE WHEN  analysis.status_id IN (" +getCompletedStatus()+ ") THEN 1 ELSE 0 END) THEN true ELSE false END as is_completed,\n" +
                "CASE WHEN MAX(document_track.report_generation_time) is null THEN false ELSE true END as is_printed\n" +
                "FROM Sample AS sample\n" +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id \n" +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id \n" +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id \n" +
                "INNER JOIN Person AS person ON patient.person_id = person.id \n" +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id \n" +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' \n" +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id \n" +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id \n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' \n" +
                "WHERE date(sample.lastupdated) = ? and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ") \n" +
                "GROUP BY sample.accession_number, sample.collection_date, sample.lastupdated, person.first_name, person.last_name, sample_source.name, patient_identity.identity_data\n" +
                "ORDER BY sample.accession_number DESC\n" +
                "LIMIT 1000;";
        try {
            PreparedStatement preparedStatement = getPreparedStatement(sqlForAllTestsToday);
            //Dont'use current_date in prepared_statement. I know its weird, but
            //The session fires query with current_date = date_on_which_session_was_created and gives wron result on next daypreparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            preparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            ResultSet todayAccessions = preparedStatement.executeQuery();
            while (todayAccessions.next()) {
                orderList.add(createOrder(todayAccessions, todayAccessions.getBoolean("is_completed")));
            }
            return orderList;
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        }
    }

    @Override
    public List<Order> getAllPendingBeforeToday() {
        List<Order> orderList = new ArrayList<>();
        String sqlForAllTestsToday = "SELECT \n" +
                "sample.accession_number AS accession_number, \n" +
                "sample.collection_date AS collection_date, \n" +
                "person.first_name AS first_name, \n" +
                "person.last_name AS last_name, \n" +
                "patient_identity.identity_data AS st_number, \n" +
                "sample_source.name AS sample_source, \n" +
                "SUM(CASE WHEN  analysis.status_id IN (" + getPendingAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_tests_count,\n" +
                "SUM(CASE WHEN  analysis.status_id IN ("+ getPendingValidationAnalysisStatus() + ") THEN 1 ELSE 0 END) as pending_validation_count,\n" +
                "COUNT(test.id) AS total_test_count,\n" +
                "CASE WHEN MAX(document_track.report_generation_time) is null THEN false ELSE true END as is_printed\n" +
                "FROM Sample AS sample\n" +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id \n" +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id \n" +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id \n" +
                "INNER JOIN Person AS person ON patient.person_id = person.id \n" +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id \n" +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' \n" +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id \n" +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id \n" +
                "INNER JOIN test ON test.id = analysis.test_id\n" +
                "LEFT OUTER JOIN document_track as document_track ON sample.id = document_track.row_id AND document_track.name = 'patientHaitiClinical' \n" +
                "WHERE sample.lastupdated < ? and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ") \n" +
                "GROUP BY sample.accession_number, sample.collection_date, person.first_name, person.last_name, sample_source.name, patient_identity.identity_data\n" +
                "HAVING COUNT(analysis.id) > SUM(CASE WHEN  analysis.status_id IN (" +getCompletedStatus()+ ") THEN 1 ELSE 0 END)\n" +
                "ORDER BY sample.accession_number DESC\n" +
                "LIMIT 1000;";
        try {
            PreparedStatement preparedStatement = getPreparedStatement(sqlForAllTestsToday);
            //Dont'use current_date in prepared_statement. I know its weird, but
            //The session fires query with current_date = date_on_which_session_was_created and gives wron result on next daypreparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            preparedStatement.setTimestamp(1, DateUtil.getTodayAsTimestamp());
            ResultSet pendingAccessions = preparedStatement.executeQuery();
            while (pendingAccessions.next()) {
                Order order = createOrder(pendingAccessions, false);
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
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
}

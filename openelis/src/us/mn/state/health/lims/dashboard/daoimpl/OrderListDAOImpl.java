package us.mn.state.health.lims.dashboard.daoimpl;

import org.apache.commons.lang3.StringUtils;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.dashboard.dao.OrderListDAO;
import us.mn.state.health.lims.dashboard.valueholder.Order;
import us.mn.state.health.lims.dashboard.valueholder.TodayStat;
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
                "WHERE date(sample.lastupdated) = current_date and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ") \n" +
                "GROUP BY sample.accession_number, sample.lastupdated, person.first_name, person.last_name, sample_source.name, patient_identity.identity_data\n" +
                "ORDER BY sample.accession_number asc\n" +
                "LIMIT 1000;";
        try {
            Connection connection = HibernateUtil.getSession().connection();

            ResultSet todayAccessions = connection.prepareStatement(sqlForAllTestsToday).executeQuery();

            while (todayAccessions.next()) {
                orderList.add(new Order(todayAccessions.getString("accession_number"),
                        todayAccessions.getString("st_number"),
                        todayAccessions.getString("first_name"),
                        todayAccessions.getString("last_name"),
                        todayAccessions.getString("sample_source"),
                        todayAccessions.getBoolean("is_completed"),
                        todayAccessions.getBoolean("is_printed"),
                        todayAccessions.getInt("pending_tests_count"),
                        todayAccessions.getInt("pending_validation_count"),
                        todayAccessions.getInt("total_test_count")));
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
                "WHERE sample.lastupdated < current_date and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ") \n" +
                "GROUP BY sample.accession_number, sample.lastupdated, person.first_name, person.last_name, sample_source.name, patient_identity.identity_data\n" +
                "HAVING COUNT(analysis.id) > SUM(CASE WHEN  analysis.status_id IN (" +getCompletedStatus()+ ") THEN 1 ELSE 0 END)\n" +
                "ORDER BY sample.accession_number asc\n" +
                "LIMIT 1000;";
        try {
            Connection connection = HibernateUtil.getSession().connection();

            ResultSet todayAccessions = connection.prepareStatement(sqlForAllTestsToday).executeQuery();

            while (todayAccessions.next()) {
                orderList.add(new Order(todayAccessions.getString("accession_number"),
                        todayAccessions.getString("st_number"),
                        todayAccessions.getString("first_name"),
                        todayAccessions.getString("last_name"),
                        todayAccessions.getString("sample_source"),
                        false,
                        todayAccessions.getBoolean("is_printed"),
                        todayAccessions.getInt("pending_tests_count"),
                        todayAccessions.getInt("pending_validation_count"),
                        todayAccessions.getInt("total_test_count")));
            }
            return orderList;
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        }
    }

    @Override
    public TodayStat getTodayStats() {
        String sql = "select accession_number,  " +
                "                    sum(case when status='pendingTest' then 1 else 0 end) as pending_tests,  " +
                "                    sum(case when status='pendingValidation' then 1 else 0 end) as pending_validation,  " +
                "                    sum(case when status='completed' then 1 else 0 end) as completed  " +
                "                    from " +
                "                    (" +
                "                        select accession_number,  " +
                "                        CASE  " +
                "                            when analysis.status_id  in (" + getPendingAnalysisStatus() + ") then 'pendingTest'  " +
                "                            when analysis.status_id  in (" + getPendingValidationAnalysisStatus() + " ) then 'pendingValidation'  " +
                "                            when analysis.status_id  in (" + getCompletedStatus() + " ) then 'completed'  " +
                "                            else 'other' " +
                "                        END as status from sample" +
                "                        join sample_item on sample_item.samp_id = sample.id" +
                "                        join analysis on analysis.sampitem_id = sample_item.id" +
                "                        where date(sample.lastupdated) = current_date" +
                "                    ) as alias " +
                "                group by accession_number";

        try {
            Connection connection = HibernateUtil.getSession().connection();
            PreparedStatement query = connection.prepareStatement(sql);
            ResultSet resultSet = query.executeQuery();
            TodayStat todayStat = new TodayStat();
            while (resultSet.next()) {
                int pendingTestsCount = Integer.parseInt(resultSet.getString("pending_tests"));
                int pendingValidationCount = Integer.parseInt(resultSet.getString("pending_validation"));
                int completedTestsCount = Integer.parseInt(resultSet.getString("completed"));
                todayStat.incrementSamplesCount();
                if (pendingTestsCount != 0) {
                    todayStat.incrementPendingTestsCount();
                    continue;
                }
                if (pendingValidationCount != 0) {
                    todayStat.incrementPendingValidationCount();
                    continue;
                }
                if (completedTestsCount != 0) {
                    todayStat.incrementCompletedTestsCount();
                    continue;
                }
            }
            return todayStat;
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        }
    }

    private String getCompletedStatus() {
        return getStatusID(Finalized);
    }

    private String getAllNonReferredAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(BiologistRejected)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(NotTested)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalAcceptance)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalRejected)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(Finalized)));
        return StringUtils.join(inProgressAnalysisStatus.iterator(), ',');
    }

    private String getPendingValidationAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalAcceptance)));
        return StringUtils.join(inProgressAnalysisStatus.iterator(), ',');
    }

    private String getPendingAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(NotTested)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(BiologistRejected)));
        return StringUtils.join(inProgressAnalysisStatus.iterator(), ',');
    }
}

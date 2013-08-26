package us.mn.state.health.lims.dashboard.daoimpl;

import org.apache.commons.lang.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.*;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.getStatusID;

public class OrderListDAOImpl implements OrderListDAO {



    @Override
    public List<Order> getAllInProgress() {
        String pendingStatus = getPendingAnalysisStatus();
        String pendingValidationStatus = getPendingValidationAnalysisStatus();
        String nonReferredStatus = getAllNonReferredAnalysisStatus();
        List<Order> orderList = new ArrayList<>();

        String sqlForPendingTests = "select " +
                "sample.accession_number as accession_number, " +
                "count(analysis.test_id) as count " +
                "from Sample as sample " +
                "inner join sample_item on sample_item.samp_id = sample.id " +
                "inner join analysis on analysis.sampitem_id = sample_item.id " +
                "where analysis.status_id in (" + pendingStatus + ") " +
                "group by sample.accession_number";

        String sqlForPendingValidationTests = "select " +
                "sample.accession_number as accession_number, " +
                "count(analysis.test_id) as count " +
                "from Sample as sample " +
                "inner join sample_item on sample_item.samp_id = sample.id " +
                "inner join analysis on analysis.sampitem_id = sample_item.id " +
                "where analysis.status_id in (" + pendingValidationStatus + ") " +
                "group by sample.accession_number";

        String sqlForTotalTests = "select " +
                "sample.accession_number as accession_number, " +
                "person.first_name as first_name, " +
                "person.last_name as last_name, " +
                "patient_identity.identity_data as st_number, " +
                "sample_source.name as sample_source, " +
                "count(test.id) as total_test_count " +
                "from Sample as sample " +
                "left outer join Sample_Human as sampleHuman on sampleHuman.samp_Id = sample.id " +
                "left  join sample_source on sample_source.id = sample.sample_source_id " +
                "inner join Patient as patient on sampleHuman.patient_id = patient.id " +
                "inner join Person as person on patient.person_id = person.id " +
                "inner join patient_identity on patient_identity.patient_id = patient.id " +
                "inner join patient_identity_type on patient_identity.identity_type_id = patient_identity_type.id and patient_identity_type.identity_type='ST' " +
                "inner join sample_item on sample_item.samp_id = sample.id " +
                "inner join analysis on analysis.sampitem_id = sample_item.id " +
                "inner join test on test.id = analysis.test_id " +
                "where analysis.status_id in (" + nonReferredStatus + ") " +
                "group by sample.accession_number, person.first_name, person.last_name, sample_source.name, patient_identity.identity_data";
        try {
            Connection connection = HibernateUtil.getSession().connection();
            PreparedStatement queryForPendingTest = connection.prepareStatement(sqlForPendingTests);
            PreparedStatement queryForPendingValidationTest = connection.prepareStatement(sqlForPendingValidationTests);
            PreparedStatement queryForTotalTest = connection.prepareStatement(sqlForTotalTests);

            ResultSet resultSetForPendingTest = queryForPendingTest.executeQuery();
            ResultSet resultSetForPendingValidation = queryForPendingValidationTest.executeQuery();
            ResultSet resultSetForTotalTest = queryForTotalTest.executeQuery();

            Map<String, Integer> pendingTestsCountMap = createMap(resultSetForPendingTest);
            Map<String, Integer> pendingValidationTestsCountMap = createMap(resultSetForPendingValidation);

            while (resultSetForTotalTest.next()) {
                String accession_number = resultSetForTotalTest.getString("accession_number");
                Integer pendingTestCount = pendingTestsCountMap.get(accession_number) == null ? 0 : pendingTestsCountMap.get(accession_number);
                Integer pendingValidationCount = pendingValidationTestsCountMap.get(accession_number) == null ? 0 : pendingValidationTestsCountMap.get(accession_number);
                if(pendingTestCount == 0 && pendingValidationCount == 0)
                    continue;
                orderList.add(new Order(accession_number,
                        resultSetForTotalTest.getString("st_number"),
                        resultSetForTotalTest.getString("first_name"),
                        resultSetForTotalTest.getString("last_name"),
                        resultSetForTotalTest.getString("sample_source"),
                        pendingTestCount,
                        pendingValidationCount,
                        resultSetForTotalTest.getInt("total_test_count")));
            }

        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        }

        return orderList;
    }

    @Override
    public List<Order> getAllCompletedBefore24Hours() {
        List<Order> orderList = new ArrayList<>();
        String allPendingAnalysisStatus = getAllPendingAnalysisStatus();

        String sql =
                "select " +
                        "sample.accession_number as accession_number, " +
                        "person.first_name as first_name, " +
                        "person.last_name as last_name, " +
                        "patient_identity.identity_data as st_number, " +
                        "sample_source.name as sample_source, " +
                        "count(pending_analysis.id) " +
                        "from clinlims.sample as sample " +
                        "left outer join clinlims.sample_Human as sampleHuman on sampleHuman.samp_Id = sample.id " +
                        "left  join clinlims.sample_source on sample_source.id = sample.sample_source_id " +
                        "inner join clinlims.patient as patient on sampleHuman.patient_id = patient.id " +
                        "inner join clinlims.person as person on patient.person_id = person.id " +
                        "inner join clinlims.patient_identity on patient_identity.patient_id = patient.id " +
                        "inner join clinlims.patient_identity_type on patient_identity.identity_type_id = patient_identity_type.id and patient_identity_type.identity_type='ST' " +
                        "inner join clinlims.sample_item on sample_item.samp_id = sample.id " +
                        "left join clinlims.analysis as pending_analysis on pending_analysis.sampitem_id = sample_item.id and pending_analysis.status_id in (" + allPendingAnalysisStatus + ")" +
                        "where age(sample.lastupdated) <= '1 day' " +
                        "group by sample.accession_number, person.first_name, person.last_name, patient_identity.identity_data, sample_source.name ";

        try {
            Connection connection = HibernateUtil.getSession().connection();
            PreparedStatement query = connection.prepareStatement(sql);

            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                orderList.add(new Order(resultSet.getString("accession_number"),
                        resultSet.getString("st_number"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("sample_source")));
            }
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        }

        return orderList;
    }

    public TodayStat getTodayStats() {
        String sql = "select accession_number,  \n" +
                "                    sum(case when status='pendingTest' then 1 else 0 end) as pending_tests,  \n" +
                "                    sum(case when status='pendingValidation' then 1 else 0 end) as pending_validation,  \n" +
                "                    sum(case when status='completed' then 1 else 0 end) as completed  \n" +
                "                    from \n" +
                "                    (\n" +
                "                        select accession_number,  \n" +
                "                        CASE  \n" +
                "                            when analysis.status_id  in (" + getPendingAnalysisStatus() + ") then 'pendingTest'  \n" +
                "                            when analysis.status_id  in ("+ getPendingValidationAnalysisStatus()+" ) then 'pendingValidation'  \n" +
                "                            when analysis.status_id  in (" + getCompletedStatus() +" ) then 'completed'  \n" +
                "                            else 'other' \n" +
                "                        END as status from sample\n" +
                "                        join sample_item on sample_item.samp_id = sample.id\n" +
                "                        join analysis on analysis.sampitem_id = sample_item.id\n" +
                "                        where date(sample.lastupdated) = current_date\n" +
                "                    ) as alias \n" +
                "                group by accession_number";

        try {
            Connection connection = HibernateUtil.getSession().connection();
            PreparedStatement query = connection.prepareStatement(sql);
            ResultSet resultSet = query.executeQuery();
            TodayStat todayStat = new TodayStat();
            while(resultSet.next()){
                int pendingTestsCount = Integer.parseInt(resultSet.getString("pending_tests"));
                int pendingValidationCount = Integer.parseInt(resultSet.getString("pending_validation"));
                int completedTestsCount = Integer.parseInt(resultSet.getString("completed"));
                todayStat.incrementSamplesCount();
                if(pendingTestsCount != 0){
                    todayStat.incrementPendingTestsCount();
                    continue;
                }
                if(pendingValidationCount != 0){
                    todayStat.incrementPendingValidationCount();
                    continue;
                }
                if(completedTestsCount != 0){
                    todayStat.incrementCompletedTestsCount();
                    continue;
                }
            }
            return todayStat;
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        }
    }

    private String getAllPendingAnalysisStatus() {
        return getNonReferredInProgressAnalysisStatus().concat(",").concat(getReferredOutInProgessAnalysisStatus());
    }

    private String getCompletedStatus() {
        return getStatusID(Finalized);
    }

    private Map createMap(ResultSet resultSet) throws SQLException {
        HashMap<String, Integer> map = new HashMap<>();
        while (resultSet.next()) {
            map.put(resultSet.getString("accession_number"), Integer.parseInt(resultSet.getString("count")));
        }
        return map;
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

    private String getReferredOutInProgessAnalysisStatus() {
        List<Object> referredStatus = new ArrayList<>();
        referredStatus.add(parseInt(getStatusID(ReferedOut)));
        referredStatus.add(parseInt(getStatusID(ReferredIn)));
        referredStatus.add(parseInt(getStatusID(TechnicalAcceptanceRO)));
        return StringUtils.join(referredStatus.iterator(), ',');
    }

    private String getNonReferredInProgressAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(BiologistRejected)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(NotTested)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalAcceptance)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalRejected)));
        return StringUtils.join(inProgressAnalysisStatus.iterator(), ',');
    }

}

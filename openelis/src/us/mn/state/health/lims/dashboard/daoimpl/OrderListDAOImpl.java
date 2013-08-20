package us.mn.state.health.lims.dashboard.daoimpl;

import org.apache.commons.lang.StringUtils;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.dashboard.dao.OrderListDAO;
import us.mn.state.health.lims.dashboard.valueholder.Order;
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
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.BiologistRejected;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.Canceled;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.Finalized;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.NotTested;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.ReferedOut;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.ReferredIn;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.TechnicalRejected;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.getStatusID;

public class OrderListDAOImpl implements OrderListDAO {

    @Override
    public List<Order> getAllInProgress() {
        String pendingStatus = getPendingAnalysisStatus();
        String pendingValidationStatus = getPendingValidationAnalysisStatus();
        String nonReferredStatus = getNonReferredAnalysisStatus();
        List<Order> orderList = new ArrayList<>();

        String sqlForPendingTests="select " +
                "sample.accession_number as accession_number, " +
                "count(analysis.test_id) as count " +
                "from Sample as sample " +
                "inner join sample_item on sample_item.samp_id = sample.id " +
                "inner join analysis on analysis.sampitem_id = sample_item.id " +
                "where analysis.status_id in (" + pendingStatus + ") " +
                "group by sample.accession_number";

        String sqlForPendingValidationTests="select " +
                "sample.accession_number as accession_number, " +
                "count(analysis.test_id) as count " +
                "from Sample as sample " +
                "inner join sample_item on sample_item.samp_id = sample.id " +
                "inner join analysis on analysis.sampitem_id = sample_item.id " +
                "where analysis.status_id in (" + pendingValidationStatus + ") " +
                "group by sample.accession_number";

        String sqlForTotalTests="select " +
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

            while(resultSetForTotalTest.next()) {
                String accession_number = resultSetForTotalTest.getString("accession_number");
                Integer pendingTestCount = pendingTestsCountMap.get(accession_number) == null ? 0 : pendingTestsCountMap.get(accession_number);
                Integer pendingValidationCount = pendingValidationTestsCountMap.get(accession_number) == null ? 0 : pendingValidationTestsCountMap.get(accession_number);
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
        String inProgressAnalysisStatus = getInProgressAndReferredAnalysisStatus();

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
                 "left join clinlims.analysis as pending_analysis on pending_analysis.sampitem_id = sample_item.id and pending_analysis.status_id in ("+ inProgressAnalysisStatus +")"+
                 "where age(sample.lastupdated) <= '1 day' " +
                 "group by sample.accession_number, person.first_name, person.last_name, patient_identity.identity_data, sample_source.name " +
                 "having count(pending_analysis.id) = 0 ";

        try {
            Connection connection = HibernateUtil.getSession().connection();
            PreparedStatement query = connection.prepareStatement(sql);
            System.out.println(query);

            ResultSet resultSet = query.executeQuery();
            while(resultSet.next()) {
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

    private Map createMap(ResultSet resultSet) throws SQLException {
        HashMap<String, Integer> map = new HashMap<>();
        while(resultSet.next()){
            map.put(resultSet.getString("accession_number"), Integer.parseInt(resultSet.getString("count")));
        }
        return map;
    }

    private String getNonReferredAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(BiologistRejected)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(NotTested)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalAcceptance)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalRejected)));
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

    private String getInProgressAndReferredAnalysisStatus() {
        String inProgressAnalysisStatus = getNonReferredAnalysisStatus();
        String referredStatus = getReferredAnalysisStatus();
        return inProgressAnalysisStatus.concat(",").concat(referredStatus);
    }

    private String getReferredAnalysisStatus() {
        List<Object> referredStatus = new ArrayList<>();
        referredStatus.add(parseInt(getStatusID(ReferedOut)));
        referredStatus.add(parseInt(getStatusID(ReferredIn)));
        return StringUtils.join(referredStatus.iterator(), ',');
    }
}

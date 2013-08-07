package us.mn.state.health.lims.dashboard.daoimpl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.dashboard.dao.OrderListDAO;
import us.mn.state.health.lims.dashboard.valueholder.Order;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrderListDAOImpl implements OrderListDAO {
    @Override
    public List<Order> getAllInProgress() {
        List<Integer> inProgressAnalysisStatus = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();
        inProgressAnalysisStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance)));
        inProgressAnalysisStatus.add(Integer.parseInt(StatusOfSampleUtil.getStatusID(StatusOfSampleUtil.AnalysisStatus.NotStarted)));
        String join = StringUtils.join(inProgressAnalysisStatus.iterator(), ',');

        String sql =
            "select " +
                "sample.accession_number as accession_number, " +
                "person.first_name as first_name, " +
                "person.last_name as last_name, " +
                "patient_identity.identity_data as st_number, " +
                "sample_source.name as sample_source, " +
                "count(test.id) as total_test_count, " +
                "count(result.id) as completed_test_count " +
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
            "left join result on result.analysis_id = analysis.id " +
            "where analysis.status_id in (" + join + ") " +
            "group by sample.accession_number, person.first_name, person.last_name, sample_source.name, patient_identity.identity_data";
        try {
            Connection connection = HibernateUtil.getSession().connection();
            PreparedStatement query = connection.prepareStatement(sql);
            System.out.println(query);

            ResultSet resultSet = query.executeQuery();
            while(resultSet.next()) {
                int total_test_count = resultSet.getInt("total_test_count");
                int completed_test_count = resultSet.getInt("completed_test_count");
                orderList.add(new Order(resultSet.getString("accession_number"),
                        resultSet.getString("st_number"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("sample_source"),
                        (total_test_count - completed_test_count),
                        total_test_count));
            }
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        }

        return orderList;
    }
}

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

    public OrderListDAOImpl() {
    }

    @Override
    public List<Order> getAllInProgress() {
        List<Order> orderList = new ArrayList<>();
        String pendingStatus = getPendingAnalysisStatus();
        String pendingValidationStatus = getPendingValidationAnalysisStatus();
        String nonReferredStatus = getAllNonReferredAnalysisStatus();

        String sqlForPendingTests = "SELECT " +
                "sample.accession_number AS accession_number, " +
                "COUNT(analysis.test_id) AS COUNT " +
                "FROM Sample AS sample " +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id " +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id " +
                "WHERE analysis.status_id IN (" + pendingStatus + ") " +
                "GROUP BY sample.accession_number";

        String sqlForPendingValidationTests = "SELECT " +
                "sample.accession_number AS accession_number, " +
                "COUNT(analysis.test_id) AS COUNT " +
                "FROM Sample AS sample " +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id " +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id " +
                "WHERE analysis.status_id IN (" + pendingValidationStatus + ") " +
                "GROUP BY sample.accession_number";

        String sqlForTotalTests = "SELECT " +
                "sample.accession_number AS accession_number, " +
                "person.first_name AS first_name, " +
                "person.last_name AS last_name, " +
                "patient_identity.identity_data AS st_number, " +
                "sample_source.name AS sample_source, " +
                "COUNT(test.id) AS total_test_count " +
                "FROM Sample AS sample " +
                "LEFT OUTER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id " +
                "LEFT  JOIN sample_source ON sample_source.id = sample.sample_source_id " +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id " +
                "INNER JOIN Person AS person ON patient.person_id = person.id " +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id " +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' " +
                "INNER JOIN sample_item ON sample_item.samp_id = sample.id " +
                "INNER JOIN analysis ON analysis.sampitem_id = sample_item.id " +
                "INNER JOIN test ON test.id = analysis.test_id " +
                "WHERE analysis.status_id IN (" + nonReferredStatus + ") " +
                "GROUP BY sample.accession_number, person.first_name, person.last_name, sample_source.name, patient_identity.identity_data";

        try {
            Connection connection = HibernateUtil.getSession().connection();
            PreparedStatement queryForPendingTest = connection.prepareStatement(sqlForPendingTests);
            PreparedStatement queryForPendingValidationTest = connection.prepareStatement(sqlForPendingValidationTests);
            PreparedStatement queryForTotalTest = connection.prepareStatement(sqlForTotalTests);

            ResultSet resultSetForPendingTest = queryForPendingTest.executeQuery();
            ResultSet resultSetForPendingValidation = queryForPendingValidationTest.executeQuery();
            ResultSet resultSetForTotalTest = queryForTotalTest.executeQuery();

            Map pendingTestsCountMap = createMap(resultSetForPendingTest);
            Map pendingValidationTestsCountMap = createMap(resultSetForPendingValidation);

            while (resultSetForTotalTest.next()) {
                String accession_number = resultSetForTotalTest.getString("accession_number");
                Integer pendingTestCount = pendingTestsCountMap.get(accession_number) == null ? 0 : (Integer) pendingTestsCountMap.get(accession_number);
                Integer pendingValidationCount = pendingValidationTestsCountMap.get(accession_number) == null ? 0 : (Integer) pendingValidationTestsCountMap.get(accession_number);
                if (pendingTestCount == 0 && pendingValidationCount == 0)
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
            return orderList;
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        }
    }

    public List<Order> getAllCompletedBefore24Hours() {
        List<Order> orderList = new ArrayList<>();

        String sqlForTotalTests = "SELECT sample.accession_number AS accession_number, " +
                "sample.lastupdated AS completed_date, " +
                "person.first_name AS first_name, " +
                "person.last_name AS last_name, " +
                "patient_identity.identity_data AS st_number, " +
                "sample_source.name AS sample_source, " +
                "COUNT(analysis.id) AS total_test_count, " +
                "SUM(CASE WHEN  analysis.status_id IN (" + getCompletedStatus() + ") THEN 1 ELSE 0 END) AS completed_test_count " +
                "FROM Sample AS sample " +
                "INNER JOIN sample_item AS sample_item ON sample_item.samp_id = sample.id " +
                "INNER JOIN analysis AS analysis ON analysis.sampitem_id = sample_item.id " +
                "INNER JOIN Sample_Human AS sampleHuman ON sampleHuman.samp_Id = sample.id " +
                "INNER JOIN sample_source ON sample_source.id = sample.sample_source_id " +
                "INNER JOIN Patient AS patient ON sampleHuman.patient_id = patient.id " +
                "INNER JOIN Person AS person ON patient.person_id = person.id " +
                "INNER JOIN patient_identity ON patient_identity.patient_id = patient.id " +
                "INNER JOIN patient_identity_type ON patient_identity.identity_type_id = patient_identity_type.id AND patient_identity_type.identity_type='ST' " +
                "WHERE age(sample.lastupdated) <= '1 day' " +
                "AND analysis.status_id NOT IN ("+ getAllReferredAnalysisStatus() +") " +
                "GROUP BY sample.accession_number, sample.lastupdated, person.first_name, person.last_name, sample_source.name, patient_identity.identity_data " +
                "HAVING COUNT(analysis.id) = SUM(CASE WHEN  analysis.status_id IN (" +getCompletedStatus()+ " ) THEN 1 ELSE 0 END);";

        try {
            Connection connection = HibernateUtil.getSession().connection();
            PreparedStatement queryForTotalTest = connection.prepareStatement(sqlForTotalTests);

            ResultSet completedAccessions = queryForTotalTest.executeQuery();

            while (completedAccessions.next()) {
                orderList.add(new Order(completedAccessions.getString("accession_number"),
                        completedAccessions.getString("st_number"),
                        completedAccessions.getString("first_name"),
                        completedAccessions.getString("last_name"),
                        completedAccessions.getString("sample_source"),
                        completedAccessions.getTimestamp("completed_date")));
            }
            return orderList;
        } catch (SQLException e) {
            throw new LIMSRuntimeException(e);
        }
    }

    @Override
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
                "                            when analysis.status_id  in (" + getPendingValidationAnalysisStatus() + " ) then 'pendingValidation'  \n" +
                "                            when analysis.status_id  in (" + getCompletedStatus() + " ) then 'completed'  \n" +
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

    private String getAllReferredAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(BiologistRejectedRO)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalAcceptanceRO)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(FinalizedRO)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(ReferedOut)));
        inProgressAnalysisStatus.add(parseInt(getStatusID(ReferredIn)));
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

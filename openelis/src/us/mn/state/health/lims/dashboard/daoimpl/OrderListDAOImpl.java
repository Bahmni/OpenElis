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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.*;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.AnalysisStatus.BiologistRejectedRO;
import static us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil.getStatusID;

public class OrderListDAOImpl implements OrderListDAO {

    private Logger logger = LogManager.getLogger(OrderListDAOImpl.class);

    private static String COMMENT_SEPARATOR = "<~~>";
    private OrderListDAOHelper orderListDAOHelper;

    public OrderListDAOImpl(Boolean isGroupBySampleEnabled) {
        orderListDAOHelper = new OrderListDAOHelper(isGroupBySampleEnabled);
    }

    @Override
    public List<Order> getAllToday() {
        List<Order> orderList = new ArrayList<>();
        String condition = "sample.accession_number is not null and analysis.status_id IN (" + getAllAnalysisStatus() + ") ";
        String sqlForAllTestsToday = orderListDAOHelper.createSqlForToday(condition, "sample.accession_number",
                getPendingAnalysisStatus(), getPendingValidationAnalysisStatus(),getReferredAnalysisStatus(), getCompletedStatus());
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
        String condition = "sample.accession_number is not null and analysis.status_id IN (" + getAllAnalysisStatus() + ")";
        return getOrders(orderListDAOHelper.createSqlForPendingBeforeToday(condition, "sample.accession_number",
                getPendingAnalysisStatus(), getPendingValidationAnalysisStatus(), getReferredAnalysisStatus(),analysesInFinalStatus()));
    }

    @Override
    public List<Order> getAllSampleNotCollectedToday() {
        List<Order> orderList = new ArrayList<>();
        String sqlForAllSampleNotCollectedToday = orderListDAOHelper.createSqlForToday("sample.accession_number is null and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ")",
                "sample.lastupdated", getPendingAnalysisStatus(), getPendingValidationAnalysisStatus(),getReferredAnalysisStatus(), getCompletedStatus());

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
        String condition = "sample.accession_number is null and analysis.status_id IN (" + getAllNonReferredAnalysisStatus() + ")";
        String sqlForAllSampleNotCollectedPendingBeforeToday = orderListDAOHelper.createSqlForPendingBeforeToday(condition,
                        "sample.lastupdated", getPendingAnalysisStatus(), getPendingValidationAnalysisStatus(),getReferredAnalysisStatus(),
                analysesInFinalStatus());
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

        return orderListDAOHelper.getOrder(accessionResultSet, comments, sectionNames, completed);
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
        inProgressAnalysisStatus.add(parseInt(getStatusID(TechnicalAcceptanceRO)));
        return StringUtils.join(inProgressAnalysisStatus.iterator(), ',');
    }

    private String getPendingAnalysisStatus() {
        List<Object> inProgressAnalysisStatus = new ArrayList<>();
        inProgressAnalysisStatus.add(parseInt(getStatusID(NotTested)));//4
        inProgressAnalysisStatus.add(parseInt(getStatusID(BiologistRejected)));//7
        return StringUtils.join(inProgressAnalysisStatus.iterator(), ',');
    }

    private String analysesInFinalStatus() {
        List<Object> analysisStatuses = new ArrayList<>();
        analysisStatuses.add(parseInt(getStatusID(FinalizedRO)));
        analysisStatuses.add(parseInt(getStatusID(Finalized)));
        analysisStatuses.add(parseInt(getStatusID(ReferredIn)));
        analysisStatuses.add(parseInt(getStatusID(MarkedAsDone)));
        return StringUtils.join(analysisStatuses.iterator(), ',');
    }

    private String getAllAnalysisStatus() {
        List<Object> analysisStatuses = new ArrayList<>();
        analysisStatuses.add(parseInt(getStatusID(ReferedOut)));
        analysisStatuses.add(parseInt(getStatusID(MarkedAsDone)));
        analysisStatuses.add(parseInt(getStatusID(ReferredIn)));
        analysisStatuses.add(parseInt(getStatusID(TechnicalAcceptanceRO)));
        analysisStatuses.add(parseInt(getStatusID(BiologistRejectedRO)));
        analysisStatuses.add(parseInt(getStatusID(FinalizedRO)));
        analysisStatuses.add(getAnalysisStatus(BiologistRejected, getStatusID(NotTested), TechnicalAcceptance, TechnicalRejected, Finalized));
        return StringUtils.join(analysisStatuses.iterator(), ',');
    }
    private String getReferredAnalysisStatus() {
        List<Object> analysisStatuses = new ArrayList<>();
        analysisStatuses.add(parseInt(getStatusID(ReferedOut)));
        analysisStatuses.add(parseInt(getStatusID(BiologistRejectedRO)));
        return StringUtils.join(analysisStatuses.iterator(), ',');
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
}

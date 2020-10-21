package us.mn.state.health.lims.dashboard.daoimpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import us.mn.state.health.lims.dashboard.valueholder.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class OrderListDAOHelperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    public void shouldReturnPriorityAndSampleTypeInSelectClauseWhenConfigIsEnabled() {
        OrderListDAOHelper orderListDAOHelper = new OrderListDAOHelper(true);
        String sqlForToday = orderListDAOHelper.createSqlForToday("condition", "orderBy", "analysisStatus",
                "validationAnalysisStatus", "referredAnalysisStatus","completed");

        assertTrue(sqlForToday.contains("sample.priority AS priority,\n" + "type_of_sample.description AS sample_type, \n"));
    }

    @Test
    public void shouldNotHavePriorityAndSampleTypeInSelectClauseWhenConfigIsNotEnabled() {
        OrderListDAOHelper orderListDAOHelper = new OrderListDAOHelper(false);
        String sqlForToday = orderListDAOHelper.createSqlForToday("condition", "orderBy", "analysisStatus",
                "validationAnalysisStatus", "referredAnalysisStatus","completed");

        assertFalse(sqlForToday.contains("sample.priority AS priority,\n" + "type_of_sample.description AS sample_type, \n"));
    }

    @Test
    public void shouldReturnHighForPriorityOne() throws SQLException {
        when(resultSet.getString("priority")).thenReturn("1");

        Order order = new OrderListDAOHelper(true).getOrder(resultSet, "comments", "JSS", false);

        assertEquals("High", order.getPriority());

        verify(resultSet, times(1)).getString("priority");
        verify(resultSet, times(1)).getString("sample_type");
    }

    @Test
    public void shouldReturnLowForPriorityZero() throws SQLException {
        when(resultSet.getString("priority")).thenReturn("0");

        Order order = new OrderListDAOHelper(true).getOrder(resultSet, "comments", "JSS", false);

        assertEquals("Low", order.getPriority());

        verify(resultSet, times(1)).getString("priority");
        verify(resultSet, times(1)).getString("sample_type");
    }

    @Test
    public void shouldCallGetStringOfPriorityAndSampleTypeWhenConfigIsTrue() throws SQLException {
        Order order = new OrderListDAOHelper(true).getOrder(resultSet, "comments", "JSS", false);

        verify(resultSet, times(1)).getString("priority");
        verify(resultSet, times(1)).getString("sample_type");
    }

    @Test
    public void shouldNotCallGetStringOfPriorityAndSampleTypeWhenConfigIsNotEnabled() throws SQLException {
        Order order = new OrderListDAOHelper(false).getOrder(resultSet, "comments", "JSS", false);

        verify(resultSet, times(0)).getString("priority");
        verify(resultSet, times(0)).getString("sample_type");
    }

}
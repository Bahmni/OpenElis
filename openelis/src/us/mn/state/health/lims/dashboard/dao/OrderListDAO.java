package us.mn.state.health.lims.dashboard.dao;

import us.mn.state.health.lims.dashboard.valueholder.Order;
import us.mn.state.health.lims.dashboard.valueholder.TodayStat;

import java.util.List;

public interface OrderListDAO {
    public List<Order> getAllToday();
    List<Order> getAllPendingBeforeToday();
    TodayStat getTodayStats();
}

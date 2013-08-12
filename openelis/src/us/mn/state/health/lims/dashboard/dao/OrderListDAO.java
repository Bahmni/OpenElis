package us.mn.state.health.lims.dashboard.dao;

import us.mn.state.health.lims.dashboard.valueholder.Order;
import java.util.List;

public interface OrderListDAO {
    public List<Order> getAllInProgress();

    List<Order> getAllCompletedBefore24Hours();
}

package us.mn.state.health.lims.healthcenter.dao;

import java.util.List;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;

public interface HealthCenterDAO {
    HealthCenter get(String id);
    public List<HealthCenter> getAll();
    public HealthCenter getByName(String name);
    public void add(HealthCenter healthCenter);
    public void update(HealthCenter healthCenter);

    void deactivate(String centerName);
}

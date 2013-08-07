package us.mn.state.health.lims.healthcenter.dao;

import java.util.List;

import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.healthcenter.valueholder.HealthCenter;

public interface HealthCenterDAO {
    HealthCenter get(String id);
    public List<HealthCenter> getAll() throws LIMSRuntimeException;
    public HealthCenter getByName(String name) throws LIMSRuntimeException;
    public void add(HealthCenter healthCenter) throws LIMSRuntimeException;
    public void update(HealthCenter healthCenter) throws LIMSRuntimeException;

    void deactivate(String centerName);
}

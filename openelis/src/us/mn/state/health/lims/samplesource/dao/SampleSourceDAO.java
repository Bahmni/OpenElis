package us.mn.state.health.lims.samplesource.dao;

import us.mn.state.health.lims.samplesource.valueholder.SampleSource;

import java.util.List;

public interface SampleSourceDAO {

    public List<SampleSource> getAll();
    public SampleSource getByName(String name);
    public SampleSource get(String id);
    public void add(SampleSource sampleSource);
    public void update(SampleSource sampleSource);

    void deactivate(String sampleSourceName);

}

package us.mn.state.health.lims.sample.bean;

import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.test.valueholder.Test;

import java.util.List;

public class SampleTestCollection {
    public SampleItem item;
    public List<Test> tests;
    public String collectionDate;
    public List<ObservationHistory> initialSampleConditionIdList;

    public SampleTestCollection(SampleItem item, List<Test> tests, String collectionDate, List<ObservationHistory> initialConditionList) {
        this.item = item;
        this.tests = tests;
        this.collectionDate = collectionDate;
        initialSampleConditionIdList = initialConditionList;
    }
}

package org.bahmni.feed.openelis.feed.contract;

import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.observationhistory.valueholder.ObservationHistory;
import us.mn.state.health.lims.sample.bean.SampleTestCollection;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.test.valueholder.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class SampleTestOrderCollection {

    public List<TestOrder> tests;
    public SampleItem item;
    public String collectionDate;
    public List<ObservationHistory> initialSampleConditionIdList;

    public SampleTestOrderCollection(SampleItem item, List<TestOrder> testOrders, String collectionDate, List<ObservationHistory> initialConditionList) {
        this.item = item;
        this.tests = testOrders;
        this.collectionDate = collectionDate;
        initialSampleConditionIdList = initialConditionList;
    }

    public SampleTestOrderCollection(SampleItem item, List<TestOrder> testOrders, Date collectionDate) {
        // TODO : Mujir - is DateUtil.convertSqlDateToStringDate(nowAsSqlDate) ok?
        this(item, testOrders, DateUtil.formatDateTimeAsText(collectionDate), new ArrayList<ObservationHistory>());
    }

    public static SampleTestOrderCollection getTestOrderCollectionFrom(SampleTestCollection sampleItemsTest) {

        List<TestOrder> testOrders = new ArrayList<>();
        for (Test test : sampleItemsTest.tests) {
            testOrders.add(new TestOrder(test,null));
        }
        return new SampleTestOrderCollection(sampleItemsTest.item,testOrders,sampleItemsTest.collectionDate,sampleItemsTest.initialSampleConditionIdList);
    }
}

package org.bahmni.feed.openelis.feed.contract;

import us.mn.state.health.lims.test.valueholder.Test;

public class TestOrder {

    private Test test;

    private String comment;

    public TestOrder(Test test, String commentToFulfiller) {
        this.test = test;
        this.comment = commentToFulfiller;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

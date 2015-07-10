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

    public int hashCode() {
        int result = this.test.getId() != null ? this.test.getId().hashCode() : 0;
        result = 31 * result + this.test.getTestName().hashCode();
        return result;
    }

    public boolean equals(Object o) {
        TestOrder testOrder = (TestOrder) o;
        Test newTest = testOrder.getTest();
        if (o == null || getClass() != o.getClass()) return false;
//        if (getComment() != null ? !getComment().equals(testOrder.getComment()) : testOrder.getComment() != null)
//            return false;
        if (this.test.getId() != null ? !this.test.getId().equals(newTest.getId()) : newTest.getId() != null) return false;
        if (this.test.getTestName() != null ? !this.test.getTestName().equals(newTest.getTestName()) : newTest.getTestName() != null) return false;
        return true;
    }
}

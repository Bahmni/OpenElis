package us.mn.state.health.lims.test.valueholder;

import org.junit.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestComparatorTest {

    @org.junit.Test
    public void SortOrderComparatorShouldKeepTheTestsWithoutSortOrderAtTheEnd() {
        Test test1 = new Test();
        test1.setSortOrder("");
        test1.setTestName("B");

        Test test2 = new Test();
        test2.setSortOrder("1");
        test2.setTestName("C");

        Test test3 = new Test();
        test3.setSortOrder("2");
        test3.setTestName("A");

        List<Test> tests = Arrays.asList(test2, test3, test1);

        Collections.sort(tests, TestComparator.SORT_ORDER_COMPARATOR);

        Assert.assertArrayEquals(new Test[] {test2, test3, test1}, tests.toArray());
    }
}

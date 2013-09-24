/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

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

        List<Test> tests = Arrays.asList(test1, test2, test3);

        Collections.sort(tests, TestComparator.SORT_ORDER_COMPARATOR);

        Assert.assertArrayEquals(new Test[] {test2, test3, test1}, tests.toArray());
    }
}

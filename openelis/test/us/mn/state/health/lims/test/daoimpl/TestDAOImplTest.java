package us.mn.state.health.lims.test.daoimpl;

import org.bahmni.feed.openelis.IT;
import org.bahmni.openelis.builder.TestSetup;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.test.valueholder.NonNumericTests;
import us.mn.state.health.lims.test.valueholder.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestDAOImplTest extends IT {

    @org.junit.Test
    public void shouldGetAllNonNumericTests() {
        Test malariaTest = TestSetup.createTest("Malaria", "");
        Test rbcTest = TestSetup.createTest("RBC", "");

        Dictionary positive = TestSetup.createDictionary("Positiive", "+");
        Dictionary negative = TestSetup.createDictionary("negatiive", "-");
        TestSetup.createTestResult(malariaTest, "D", positive);
        TestSetup.createTestResult(malariaTest, "D", negative);

        TestSetup.createTestResult(rbcTest, "N", "");

        List<NonNumericTests> nonNumericTests = new TestDAOImpl().getAllNonNumericTests(Arrays.asList(Integer.valueOf(malariaTest.getId()), Integer.valueOf(rbcTest.getId())));
        assertEquals(1, nonNumericTests.size());
        assertEquals(2, nonNumericTests.get(0).dictionaryValues.size());

        assertEquals(0, new TestDAOImpl().getAllNonNumericTests(null).size());
        assertEquals(0, new TestDAOImpl().getAllNonNumericTests(new ArrayList<Integer>()).size());
    }
}

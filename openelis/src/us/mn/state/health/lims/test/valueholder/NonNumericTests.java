package us.mn.state.health.lims.test.valueholder;

import us.mn.state.health.lims.common.util.IdValuePair;

import java.util.ArrayList;
import java.util.List;

public class NonNumericTests {
    public String testId;
    public String testType;
    public List<IdValuePair> dictionaryValues = new ArrayList<>();

    public NonNumericTests(String testId, String testType, IdValuePair dictionaryValues) {
        this.testId = testId;
        this.testType = testType;
        this.dictionaryValues.add(dictionaryValues);
    }
}

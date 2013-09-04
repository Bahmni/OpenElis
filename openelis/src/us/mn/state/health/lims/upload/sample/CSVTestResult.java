package us.mn.state.health.lims.upload.sample;

public class CSVTestResult {
    public String test;
    public String result;

    public CSVTestResult() {
    }

    public CSVTestResult(String test, String result) {
        this.test = test;
        this.result = result;
    }

    public boolean isEmpty() {
        return test.isEmpty() && result.isEmpty();
    }

    public boolean isValid() {
        boolean bothTestAndResultHasSomeValue = !test.isEmpty() && !result.isEmpty();
        return isEmpty() || bothTestAndResultHasSomeValue;
    }
}

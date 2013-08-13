package us.mn.state.health.lims.result.valueholder;

public enum ResultType {

    Numeric("N"), MultiSelect("M"), Dictionary("D"), Remark("R");

    private String code;

    private ResultType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}

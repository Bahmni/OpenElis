package us.mn.state.health.lims.ws.handler;

public class TestResultHandler  implements Handler {

    private final String RESULT = "RESULT";

    @Override
    public boolean canHandle(String resourceName) {
        return resourceName.equalsIgnoreCase(RESULT);
    }

    @Override
    public Object handle(String uuid) {
        return "NOT IMPLEMENTED";
    }
}

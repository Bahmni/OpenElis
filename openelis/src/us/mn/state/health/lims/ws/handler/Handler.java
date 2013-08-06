package us.mn.state.health.lims.ws.handler;

public interface Handler<T> {
    public abstract boolean canHandle(String resourceName);

    public abstract T handle(String uuid);
}




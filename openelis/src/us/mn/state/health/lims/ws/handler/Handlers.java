package us.mn.state.health.lims.ws.handler;

import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;

import java.util.Arrays;
import java.util.List;

public class Handlers {
    private List<Handler> handlers;

    public Handlers() {
        this(Arrays.<Handler>asList(new PatientHandler(new BahmniPatientService()), new TestResultHandler()));
    }

    public Handlers(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public Handler getHandler(String resourceName) {
        if (resourceName == null) return null;

        for (Handler handler : handlers) {
            if (handler.canHandle(resourceName)) return handler;
        }

        return null;
    }
}
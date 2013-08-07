package us.mn.state.health.lims.ws.handler;

import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;
import org.bahmni.openelis.domain.CompletePatientDetails;

public class PatientHandler implements Handler<CompletePatientDetails> {

    private final String PATIENT = "patient";
    private BahmniPatientService bahmniPatientService;

    public PatientHandler() {
        this(new BahmniPatientService());
    }

    public PatientHandler(BahmniPatientService bahmniPatientService) {
        this.bahmniPatientService = bahmniPatientService;
    }

    @Override
    public boolean canHandle(String resourceName) {
        return resourceName.equalsIgnoreCase(PATIENT);
    }

    @Override
    public CompletePatientDetails handle(String pathVariable) {
        return bahmniPatientService.getCompletePatientDetails(pathVariable);
    }
}

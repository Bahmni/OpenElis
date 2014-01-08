package us.mn.state.health.lims.ws.handler;

import org.bahmni.feed.openelis.IT;
import org.junit.Test;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;

import java.util.Date;

import static org.bahmni.openelis.builder.TestSetup.*;

public class AccessionHandlerIT extends IT{

    @Test
    public void shouldDelegateCallToAccessionService(){

        Patient patient = createPatient("First", "Last", "GAN9897889009", null);
        Sample sample = createSample("10102013-001", new Date());
        createSampleHuman(sample, patient);
        SampleItem sampleItem = createSampleItem(sample);
        Panel panel = createPanel("Test Blood Panel");
        us.mn.state.health.lims.test.valueholder.Test test = createTest("Test Platelet Count", "some unit", panel);
        createExternalReference(test.getId(), "Test", null);
        createExternalReference(panel.getId(), "Panel", null);
        ResultLimit resultLimit = createResultLimit(test, 100.0, 200.0, 10.0, 1000.0, null, null, null);
        Analysis analysis = createAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance, "New", test, panel);
        Result result = createResult(analysis, "10000", resultLimit);
        createResultNote(result, "Some note 1");
        createResultNote(result, "Some note 2");

        new AccessionHandler().handle(sample.getUUID());
    }

}

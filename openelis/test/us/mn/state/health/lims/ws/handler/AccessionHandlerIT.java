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
 * Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package us.mn.state.health.lims.ws.handler;

import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.openelis.domain.AccessionDetail;
import org.junit.Ignore;
import org.junit.Test;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.patient.valueholder.Patient;
import us.mn.state.health.lims.result.valueholder.Result;
import us.mn.state.health.lims.resultlimits.valueholder.ResultLimit;
import us.mn.state.health.lims.sample.valueholder.Sample;
import us.mn.state.health.lims.sampleitem.valueholder.SampleItem;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.bahmni.openelis.builder.TestSetup.*;

public class AccessionHandlerIT extends IT{

    @Test @Ignore
    public void shouldRetrieveAccessionBasedOnUuid() throws IOException {

        Patient patient = createPatient("First", "Middle", "Last", "GAN9897889009", null);
        Sample sample = createSample("10102013-0012", new Date());
        createSampleHuman(sample, patient);
        SampleItem sampleItem = createSampleItem(sample);
        Panel panel = createPanel("Test Blood Panel");
        us.mn.state.health.lims.test.valueholder.Test test = createTest("Test Platelet Count", "some unit", panel);
        createExternalReference(test.getId(), "Test", null);
        createExternalReference(panel.getId(), "Panel", null);
        ResultLimit resultLimit = createResultLimit(test, 100.0, 200.0, 10.0, 1000.0, null, null, null);
        Analysis analysis = createAnalysis(sampleItem, StatusOfSampleUtil.AnalysisStatus.Finalized, "New", test, panel);
        Result result = createResult(analysis, "10000", resultLimit);
        createResultNote(result, "Some note 1");
        createResultNote(result, "Some note 2");

        AccessionDetail accessionDetails = new AccessionHandler().handle(sample.getUUID());
        ObjectMapperRepository.objectMapper.writeValue(System.out, accessionDetails);
    }

}

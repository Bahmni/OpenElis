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
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

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
        return bahmniPatientService.getPatientByUUID(pathVariable);
    }
}

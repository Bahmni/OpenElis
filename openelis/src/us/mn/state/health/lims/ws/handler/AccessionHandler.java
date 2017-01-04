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

import org.bahmni.feed.openelis.externalreference.daoimpl.ExternalReferenceDaoImpl;
import org.bahmni.feed.openelis.feed.service.impl.AccessionService;
import org.bahmni.openelis.domain.AccessionDetail;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.note.daoimpl.NoteDAOImpl;
import us.mn.state.health.lims.patientidentity.daoimpl.PatientIdentityDAOImpl;
import us.mn.state.health.lims.patientidentitytype.daoimpl.PatientIdentityTypeDAOImpl;
import us.mn.state.health.lims.sample.daoimpl.SampleDAOImpl;
import us.mn.state.health.lims.samplehuman.daoimpl.SampleHumanDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

public class AccessionHandler implements Handler<AccessionDetail>{
    private static final String ACCESSION = "accession";
    private AccessionService accessionService;

    public AccessionHandler() {
        this(new AccessionService(new SampleDAOImpl(),
                new SampleHumanDAOImpl(),
                new ExternalReferenceDaoImpl(),
                new NoteDAOImpl(),
                new DictionaryDAOImpl(),
                new PatientIdentityDAOImpl(),
                new PatientIdentityTypeDAOImpl(),
                new SiteInformationDAOImpl()
        ));
    }

    public AccessionHandler(AccessionService accessionService) {
        this.accessionService = accessionService;
    }

    @Override
    public boolean canHandle(String resourceName) {
        return resourceName.equalsIgnoreCase(ACCESSION);
    }

    @Override
    public AccessionDetail handle(String uuid) {
        return accessionService.getAccessionDetailFor(uuid);
    }
}

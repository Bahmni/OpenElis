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

package org.bahmni.feed.openelis.feed.event;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.contract.openerp.OpenERPLab;
import org.bahmni.feed.openelis.feed.mapper.OpenERPLabTestMapper;
import org.bahmni.feed.openelis.feed.service.LabService;
import org.bahmni.feed.openelis.feed.service.LabServiceFactory;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

public class LabTestFeedEventWorker extends OpenElisEventWorker {
    private AuditingService auditingService;
    private OpenERPLabTestMapper labTestMapper;

    LabTestFeedEventWorker(AuditingService auditingService) {
        this.auditingService = auditingService;
        this.labTestMapper = new OpenERPLabTestMapper();
    }

    public LabTestFeedEventWorker() {
        this(new AuditingService(new LoginDAOImpl(), new SiteInformationDAOImpl()));
    }

    public void process(Event event) {
        try {
            OpenERPLab openERPLab = labTestMapper.getLabObject(event,auditingService.getSysUserId());
            LabService labService = LabServiceFactory.getLabService(openERPLab.getCategory(), AtomFeedProperties.getInstance());
            labService.process(openERPLab);
        } catch (Exception e) {
            throw new LIMSRuntimeException(e);
        }
    }
}

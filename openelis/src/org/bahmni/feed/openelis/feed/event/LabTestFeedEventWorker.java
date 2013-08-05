package org.bahmni.feed.openelis.feed.event;

import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.domain.LabObject;
import org.bahmni.feed.openelis.feed.mapper.OpenERPLabTestMapper;
import org.bahmni.feed.openelis.feed.service.LabService;
import org.bahmni.feed.openelis.feed.service.LabTestServiceFactory;
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
            LabObject labObject = labTestMapper.getLabObject(event,auditingService.getSysUserId());
            LabService labService = LabTestServiceFactory.getLabTestService(labObject.getCategory(), AtomFeedProperties.getInstance());
            labService.process(labObject);
        } catch (Exception e) {
            throw new LIMSRuntimeException(e);
        }
    }
}

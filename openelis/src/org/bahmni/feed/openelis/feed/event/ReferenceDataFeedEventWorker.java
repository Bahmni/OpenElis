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

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataDepartment;
import org.bahmni.feed.openelis.feed.contract.openerp.OpenERPLab;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.bahmni.feed.openelis.feed.mapper.OpenERPLabTestMapper;
import org.bahmni.feed.openelis.feed.mapper.OpenMRSPatientMapper;
import org.bahmni.feed.openelis.feed.mapper.ReferenceDataDepartmentMapper;
import org.bahmni.feed.openelis.feed.service.LabService;
import org.bahmni.feed.openelis.feed.service.LabServiceFactory;
import org.bahmni.feed.openelis.feed.service.impl.BahmniPatientService;
import org.bahmni.feed.openelis.feed.service.impl.TestSectionService;
import org.bahmni.feed.openelis.utils.AuditingService;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.login.daoimpl.LoginDAOImpl;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

import java.io.IOException;
import java.net.URI;

public class ReferenceDataFeedEventWorker extends OpenElisEventWorker {

    public static final String REFERENCE_DATA_DEFAULT_ORGANIZATION = "reference.data.default.organization";
    private HttpClient webClient;
    private String urlPrefix;

    private static Logger logger = Logger.getLogger(PatientFeedEventWorker.class);

    protected enum title {
        department,
        panel,
        drug,
        drug_form
    }

    public ReferenceDataFeedEventWorker(HttpClient webClient, String urlPrefix) {
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            String content = event.getContent();
            
            
            AtomFeedProperties atomFeedProperties = AtomFeedProperties.getInstance();

            if (title.department.name().equals(event.getTitle())) {
                ReferenceDataDepartment department = webClient.get(urlPrefix + content, ReferenceDataDepartment.class);
                logger.info(String.format("Processing department with UUID=%s", department.getId()));

                TestSectionService testSectionService = new TestSectionService();
                testSectionService.createOrUpdate(department, atomFeedProperties.getProperty(REFERENCE_DATA_DEFAULT_ORGANIZATION));
            }
        } catch (Exception e) {
            throw new LIMSRuntimeException(e);
        } finally {
            ElisHibernateSession session = (ElisHibernateSession) HibernateUtil.getSession();
            session.clearSession();
        }
    }

}

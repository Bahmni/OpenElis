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
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataDepartment;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataSample;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTest;
import org.bahmni.feed.openelis.feed.service.impl.TestSectionService;
import org.bahmni.feed.openelis.feed.service.impl.TestService;
import org.bahmni.feed.openelis.feed.service.impl.TypeOfSampleService;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;

public class ReferenceDataFeedEventWorker extends OpenElisEventWorker {

    public static final String REFERENCE_DATA_DEFAULT_ORGANIZATION = "reference.data.default.organization";
    private HttpClient webClient;
    private String urlPrefix;

    private static Logger logger = Logger.getLogger(PatientFeedEventWorker.class);
    private TestSectionService testSectionService;
    private TypeOfSampleService typeOfSampleService;
    private TestService testService;

    protected enum title {
        department,
        sample,
        test,
        panel,
        drug,
        drug_form
    }

    public ReferenceDataFeedEventWorker(HttpClient webClient, String urlPrefix) {
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
        this.testSectionService = new TestSectionService();
        this.typeOfSampleService = new TypeOfSampleService();
        this.testService = new TestService();
    }

    @Override
    public void process(Event event) {
        try {
            String content = event.getContent();
            AtomFeedProperties atomFeedProperties = AtomFeedProperties.getInstance();

            if (title.department.name().equals(event.getTitle())) {
                ReferenceDataDepartment department = webClient.get(urlPrefix + content, ReferenceDataDepartment.class);
                logger.info(String.format("Processing department with UUID=%s", department.getId()));
                testSectionService.createOrUpdate(department, atomFeedProperties.getProperty(REFERENCE_DATA_DEFAULT_ORGANIZATION));
            } else if (title.sample.name().equals(event.getTitle())) {
                ReferenceDataSample sample = webClient.get(urlPrefix + content, ReferenceDataSample.class);
                logger.info(String.format("Processing sample with UUID=%s", sample.getId()));
                typeOfSampleService.createOrUpdate(sample);
            } else if (title.test.name().equals(event.getTitle())) {
                ReferenceDataTest test = webClient.get(urlPrefix + content, ReferenceDataTest.class);
                logger.info(String.format("Processing test with UUID=%s", test.getId()));
                testService.createOrUpdate(test);
            }
        } catch (Exception e) {
            throw new LIMSRuntimeException(e);
        } finally {
            ElisHibernateSession session = (ElisHibernateSession) HibernateUtil.getSession();
            session.clearSession();
        }
    }

}

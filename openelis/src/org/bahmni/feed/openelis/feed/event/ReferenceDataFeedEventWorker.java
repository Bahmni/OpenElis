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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataDepartment;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataPanel;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTest;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataTestUnitOfMeasure;
import org.bahmni.feed.openelis.feed.service.impl.*;
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
    private PanelService panelService;
    private UnitOfMeasureService unitOfMeasureService;


    protected enum title {
        department,
        test,
        panel,
        drug,
        drug_form,
        test_unit_of_measure
    }

    public ReferenceDataFeedEventWorker(HttpClient webClient, String urlPrefix) {
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
        this.testSectionService = new TestSectionService();
        this.typeOfSampleService = new TypeOfSampleService();
        this.testService = new TestService();
        this.panelService = new PanelService();
        this.unitOfMeasureService = new UnitOfMeasureService();
    }

    @Override
    public void process(Event event) {
        try {
            String content = event.getContent();
            AtomFeedProperties atomFeedProperties = AtomFeedProperties.getInstance();

            if (title.department.name().equals(event.getTitle())) {
                ReferenceDataDepartment department = webClient.get(urlPrefix + content, ReferenceDataDepartment.class);
                logger.info(String.format("Processing department with UUID=%s", department.getId()));
                testSectionService.createOrUpdate(department, getDefaultOrganization(atomFeedProperties));
            } else if (title.test.name().equals(event.getTitle())) {
                ReferenceDataTest test = webClient.get(urlPrefix + content, ReferenceDataTest.class);
                logger.info(String.format("Processing test with UUID=%s", test.getId()));
                testService.createOrUpdate(test);
            } else if (title.panel.name().equals(event.getTitle())) {
                ReferenceDataPanel panel = webClient.get(urlPrefix + content, ReferenceDataPanel.class);
                logger.info(String.format("Processing panel with UUID=%s", panel.getId()));
                panelService.createOrUpdate(panel);
            } else if (title.test_unit_of_measure.name().equals(event.getTitle())) {
                ReferenceDataTestUnitOfMeasure unitOfMeasure = webClient.get(urlPrefix + content, ReferenceDataTestUnitOfMeasure.class);
                logger.info(String.format("Processing test unit if measure with UUID=%s", unitOfMeasure.getId()));
                unitOfMeasureService.createOrUpdate(unitOfMeasure);
            }
        } catch (Exception e) {
            throw new LIMSRuntimeException(e);
        } finally {
            ElisHibernateSession session = (ElisHibernateSession) HibernateUtil.getSession();
            session.clearSession();
        }
    }

    /**
     * Provide the ability to override the property via environment variable, so that different
     * implementations can specify their own value.
     *
     * @param atomFeedProperties
     * @return The default organization value (from env variable, else from atomfeed.properties)
     */
    private String getDefaultOrganization(AtomFeedProperties atomFeedProperties) {
        final String ELIS_DEF_ORG_ENVIRONMENT_VARIABLE = "ELIS_DEFAULT_ORGANIZATION_NAME";
        String envValue = System.getenv(ELIS_DEF_ORG_ENVIRONMENT_VARIABLE);
        if (StringUtils.isNotEmpty(envValue)) {
            logger.info("Environment variable " + ELIS_DEF_ORG_ENVIRONMENT_VARIABLE + " found with value: " + envValue);
            return envValue;
        } else {
            return atomFeedProperties.getProperty(REFERENCE_DATA_DEFAULT_ORGANIZATION);
        }
    }

}

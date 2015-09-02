package org.bahmni.feed.openelis.feed.event;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.AtomFeedProperties;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.*;
import org.bahmni.feed.openelis.feed.service.impl.PanelService;
import org.bahmni.feed.openelis.feed.service.impl.TestSectionService;
import org.bahmni.feed.openelis.feed.service.impl.TestService;
import org.bahmni.feed.openelis.feed.service.impl.TypeOfSampleService;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.common.exception.LIMSException;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.io.IOException;

public class LabFeedEventWorker extends OpenElisEventWorker {
    private final TypeOfSampleService typeOfSampleService;
    private HttpClient webClient;
    private String urlPrefix;
    private static Logger logger = Logger.getLogger(LabFeedEventWorker.class);
    private TestSectionService testSectionService;
    private TestService testService;
    private PanelService panelService;

    public LabFeedEventWorker(HttpClient webClient, String urlPrefix) {
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
        typeOfSampleService = new TypeOfSampleService();
        testSectionService = new TestSectionService();
        testService = new TestService();
        panelService = new PanelService();
    }

    protected enum title {
        sample("sample"),
        panel("panel"),
        test("test"),
        department("department"),
        all_tests_and_panels("all-tests-and-panels"),
        all_samples("all-samples");

        private String value;

        private title(String value) {
            this.value = value;
        }

        public String toUrlString() {
            return this.value; //This will return hyphen separated values
        }
    }

    @Override
    public void process(Event event) {
        try {
            String content = event.getContent();
            if (title.sample.toUrlString().equals(event.getTitle())) {
                ReferenceDataSample sample = webClient.get(urlPrefix + content, ReferenceDataSample.class);
                logger.info(String.format("Processing sample with UUID=%s", sample.getId()));
                typeOfSampleService.createOrUpdate(sample);
            } else if (title.panel.toUrlString().equals(event.getTitle())) {
                ReferenceDataPanel panel = webClient.get(urlPrefix + content, ReferenceDataPanel.class);
                logger.info(String.format("Processing panel with UUID=%s", panel.getId()));
                panelService.createOrUpdate(panel);
            } else if (title.test.toUrlString().equals(event.getTitle())) {
                ReferenceDataTest test = webClient.get(urlPrefix + content, ReferenceDataTest.class);
                logger.info(String.format("Processing test with UUID=%s", test.getId()));
                testService.createOrUpdate(test);
            } else if (title.department.toUrlString().equals(event.getTitle())) {
                ReferenceDataDepartment department = webClient.get(urlPrefix + content, ReferenceDataDepartment.class);
                logger.info(String.format("Processing department with UUID=%s", department.getId()));
                testSectionService.createOrUpdate(department);
            } else if (title.all_tests_and_panels.toUrlString().equals(event.getTitle())) {
                ReferenceDataAllTestsAndPanels allTestsAndPanels = webClient.get(urlPrefix + content, ReferenceDataAllTestsAndPanels.class);
                logger.info(String.format("Processing all tests and panels with UUID=%s", allTestsAndPanels.getId()));
                for (ReferenceDataTest test : allTestsAndPanels.getTestsAndPanels().getTests()) {
                    testService.createOrUpdate(test);
                }
                for (ReferenceDataPanel panel : allTestsAndPanels.getTestsAndPanels().getPanels()) {
                    panelService.createOrUpdate(panel);
                }
            } else if (title.all_samples.toUrlString().equals(event.getTitle())) {
                Object a = webClient.get(urlPrefix + content, ReferenceDataDepartment.class);
                ReferenceDataAllSamples allSamples = webClient.get(urlPrefix + content, ReferenceDataAllSamples.class);
                logger.info(String.format("Processing all samples with UUID=%s", allSamples.getId()));
                for (ReferenceDataSample sample : allSamples.getSamples()) {
                    typeOfSampleService.createOrUpdate(sample);
                }
            }
        } catch (IOException | LIMSException e) {
            throw new LIMSRuntimeException(e);
        } finally {
            ElisHibernateSession session = (ElisHibernateSession) HibernateUtil.getSession();
            session.clearSession();
        }
    }

}
package org.bahmni.feed.openelis.feed.event;

import org.apache.log4j.Logger;
import org.bahmni.feed.openelis.feed.contract.bahmnireferencedata.ReferenceDataSample;
import org.bahmni.feed.openelis.feed.service.impl.TypeOfSampleService;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.domain.Event;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.hibernate.ElisHibernateSession;
import us.mn.state.health.lims.hibernate.HibernateUtil;

import java.io.IOException;

public class LabFeedEventWorker extends OpenElisEventWorker {
    private final TypeOfSampleService typeOfSampleService;
    private HttpClient webClient;
    private String urlPrefix;
    private static Logger logger = Logger.getLogger(ReferenceDataFeedEventWorker.class);

    public LabFeedEventWorker(HttpClient webClient, String urlPrefix) {
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
        typeOfSampleService = new TypeOfSampleService();
    }

    protected enum title {
        sample
    }

    @Override
    public void process(Event event) {
        try {
            String content = event.getContent();
            if (title.sample.name().equals(event.getTitle())) {
                ReferenceDataSample sample = webClient.get(urlPrefix + content, ReferenceDataSample.class);
                logger.info(String.format("Processing sample with UUID=%s", sample.getId()));
                typeOfSampleService.createOrUpdate(sample);
            }
        } catch (IOException e) {
            throw new LIMSRuntimeException(e);
        } finally {
            ElisHibernateSession session = (ElisHibernateSession) HibernateUtil.getSession();
            session.clearSession();
        }
    }
}

package org.bahmni.feed.openelis.feed.client;

import org.apache.commons.codec.binary.Base64;
import org.bahmni.feed.openelis.OpenMRSConfiguration;
import org.bahmni.feed.openelis.webclient.WebClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

public class OpenMRSPatientFeedReaderJob implements Job {
    private OpenMRSConfiguration openMRSConfiguration;
    private AtomFeedClient atomFeedClient;

    public OpenMRSPatientFeedReaderJob() {
        this(new OpenMRSConfiguration(), AtomFeedClientFactory.getFeedClient());
    }

    public OpenMRSPatientFeedReaderJob(OpenMRSConfiguration openMRSConfiguration, AtomFeedClient atomFeedClient) {
        this.openMRSConfiguration = openMRSConfiguration;
        this.atomFeedClient = atomFeedClient;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        WebClient webClient = new WebClient();
        URI uri = URI.create(openMRSConfiguration.getPatientFeedURL());
        HashMap<String, String> headers = new HashMap<String, String>();
        String authorizationHeaderValue = String.format("Basic %s:%s", openMRSConfiguration.getUser(), openMRSConfiguration.getPassword());
        headers.put("Authorization", new String(Base64.encodeBase64(authorizationHeaderValue.getBytes())));
        webClient.get(uri, headers);
    }
}
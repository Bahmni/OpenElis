package org.bahmni.feed.openelis.feed.mapper.encounter;

import org.apache.commons.io.IOUtils;
import org.bahmni.feed.openelis.IT;
import org.bahmni.feed.openelis.feed.event.EncounterFeedWorker;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import static org.mockito.MockitoAnnotations.initMocks;

public class EncounterFeedWorkerIT extends IT {

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    @Ignore("Mujir - need to setup all data needed by elis. Using this more ")
    public void shouldProcessEvent() throws IOException {
        String json = deserialize("sampleOpenMRSEncounter.json");

        EncounterFeedWorker encounterFeedWorker = new EncounterFeedWorker(null, null);
        encounterFeedWorker.process(json);
    }

    private String deserialize(String fileName) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(fileName);
        org.junit.Assert.assertNotNull(inputStream);
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        return writer.toString();
    }
}

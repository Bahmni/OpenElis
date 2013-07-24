package org.bahmni.feed.openelis.feed.mapper;

import org.apache.commons.io.IOUtils;
import org.bahmni.feed.openelis.feed.contract.openmrs.OpenMRSPatient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class OpenMRSPatientMapperTest {
    @Test
    public void map() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("sampleOpenMRSPatient.json");
        Assert.assertNotNull(inputStream);
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        String json = writer.toString();
        OpenMRSPatientMapper openMRSPatientMapper = new OpenMRSPatientMapper(ObjectMapperForTest.MAPPER);
        OpenMRSPatient openMRSPatient = openMRSPatientMapper.map(json);
        Assert.assertNotNull(openMRSPatient);
    }
}
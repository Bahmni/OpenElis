package org.bahmni.feed.openelis.feed.mapper;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;


public abstract class OpenMRSMapperBaseTest {
    protected String deserialize(String fileName) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(fileName);
        Assert.assertNotNull(inputStream);
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        return writer.toString();
    }
}
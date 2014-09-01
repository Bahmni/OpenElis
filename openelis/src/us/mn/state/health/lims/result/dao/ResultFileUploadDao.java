package us.mn.state.health.lims.result.dao;

import org.apache.struts.upload.FormFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ResultFileUploadDao {
    String upload(FormFile file) throws IOException, URISyntaxException;
}

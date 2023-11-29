package us.mn.state.health.lims.result.daoimpl;

import org.apache.struts.upload.FormFile;
import us.mn.state.health.lims.result.dao.ResultFileUploadDao;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultFileUploadDaoImpl implements ResultFileUploadDao {
    public static final String SEPARATOR = "_";
    public static final String PARENT_OF_UPLOADED_FILES_DIRECTORY = "parentOfUploadedFilesDirectory";
    public static final String UPLOADED_RESULTS_DIRECTORY = "uploadedResultsDirectory";


    @Override
    public String upload(FormFile file) throws Exception {
        String encodedFileName = null;
        if(file != null && file.getFileName() != ""){
            if(!validateFile(file.getFileName())) {
                throw new Exception("file format not matching");
            }
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + SEPARATOR + file.getFileName();
            encodedFileName = new URI(null, null, fileName, null).toString();
            File downloadedFile = getFile(fileName);
            writeToFileSystem(file, downloadedFile);
        }
        return encodedFileName;
    }

    private boolean validateFile(String fileName){
        Pattern fileExtnPtrn = Pattern.compile("(^.((?!exe).)*\\.(jpg|JPG|jpeg|JPEG|doc|DOC|docx|DOCX|pdf|PDF|png|PNG)$)");
        Matcher matcher = fileExtnPtrn.matcher(fileName);
        if(matcher.matches()){
            return true;
        }
        return false;
    }

    private File getFile(String fileName) {
        String parentForUploadedFilesDirectory = new SiteInformationDAOImpl().getSiteInformationByName(PARENT_OF_UPLOADED_FILES_DIRECTORY).getValue();
        String uploadedFilesDirectory = new SiteInformationDAOImpl().getSiteInformationByName(UPLOADED_RESULTS_DIRECTORY).getValue();

        String filePath = parentForUploadedFilesDirectory + uploadedFilesDirectory + fileName;
        return new File(filePath);
    }

    private void writeToFileSystem(FormFile file, File aFile) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(aFile);
        fileOutputStream.write(file.getFileData());
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}


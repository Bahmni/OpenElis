package us.mn.state.health.lims.upload.action;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.fileimport.FileImporter;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.login.valueholder.UserSessionData;
import us.mn.state.health.lims.upload.patient.CSVPatient;
import us.mn.state.health.lims.upload.patient.PatientPersister;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class UploadAction extends BaseAction {

    // TODO : Mujir, Shruthi - finalize these constants
    public static final String TEMPORARY_FILE_LOCATION = "/tmp";

    public static final String ELIS_IMPORT_FILES_FOLDER = "elisImportFiles";
    public static final String filePath = "/home/jss/" + ELIS_IMPORT_FILES_FOLDER + "/";

    public static int maxFileSize = 50 * 1024 * 1024;
    public static int maxMemSize = 4 * 1024 * 1024;

    private static ExecutorService fileImportExecutorService;
    private static Logger logger = Logger.getLogger(UploadAction.class);

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(maxMemSize);
        factory.setRepository(new File(TEMPORARY_FILE_LOCATION));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxFileSize);

        try {
            List fileItems = upload.parseRequest(request);
            for (Object fileItem : fileItems) {
                FileItem fi = (FileItem) fileItem;
                if (!fi.isFormField()) {
                    String fileName = fi.getName();
                    fi.write(getFile(fileName));

                    UserSessionData userSessionData = (UserSessionData)request.getSession().getAttribute(USER_SESSION_DATA);

                    PatientPersister patientPersister = new PatientPersister();
                    FileImporter<CSVPatient> csvPatientFileImporter = new FileImporter<>();
                    boolean hasStartedUpload = csvPatientFileImporter.importCSV(fileName, getFile(fileName),
                            patientPersister, CSVPatient.class, new ELISJDBCConnectionProvider(), userSessionData.getLoginName());
                    if (!hasStartedUpload)
                        return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
            return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
        }
        return mapping.findForward(IActionConstants.FWD_SUCCESS);
    }

    private File getFile(String fileName) {
        int indexForSlash = fileName.lastIndexOf("\\");
        String substring = null;
        if (indexForSlash >= 0) {
            substring = fileName.substring(indexForSlash);
        } else {
            substring = fileName.substring(indexForSlash + 1);
        }
        String fileNameWithoutExtension = substring.substring(0, substring.lastIndexOf("."));
        String fileExtension = substring.substring(substring.lastIndexOf("."));

        String timestampForFile = new SimpleDateFormat("_yyyy-MM-dd_hh:mm:ss").format(new Date());
        return new File(filePath + fileNameWithoutExtension + timestampForFile + fileExtension);
    }

    @Override
    protected String getPageTitleKey() {
        return "action.upload";
    }

    @Override
    protected String getPageSubtitleKey() {
        return "action.upload";
    }

}


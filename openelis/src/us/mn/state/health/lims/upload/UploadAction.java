package us.mn.state.health.lims.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.fileimport.FileImporter;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.upload.patient.CSVPatient;
import us.mn.state.health.lims.upload.patient.PatientPersister;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class UploadAction extends BaseAction {

    // TODO : Mujir, Shruthi - finalize these constants
    public static final String TEMPORARY_FILE_LOCATION = "/tmp";
    public static final String filePath = "/home/jss/open-elis-upload/";

    public static int maxFileSize = 50 * 1024;
    public static int maxMemSize = 4 * 1024;

    private static ExecutorService fileImportExecutorService;

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
                    File file;
                    if (fileName.lastIndexOf("\\") >= 0) {
                        file = new File(filePath +
                                fileName.substring(fileName.lastIndexOf("\\")));
                    } else {
                        file = new File(filePath +
                                fileName.substring(fileName.lastIndexOf("\\") + 1));
                    }
                    fi.write(file);

                    PatientPersister patientPersister = new PatientPersister();
                    FileImporter<CSVPatient> csvPatientFileImporter = new FileImporter<>();
                    boolean hasStartedUpload = csvPatientFileImporter.importCSV(file, patientPersister, CSVPatient.class);
                    if (!hasStartedUpload)
                        return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
                }
            }
        } catch (Exception ex) {
            return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
        }
        return mapping.findForward(IActionConstants.FWD_SUCCESS);
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

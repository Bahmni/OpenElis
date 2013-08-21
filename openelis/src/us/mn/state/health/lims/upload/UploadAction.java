package us.mn.state.health.lims.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.IActionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

public class UploadAction extends BaseAction {

    public static int maxFileSize = 50 * 1024;
    public static int maxMemSize = 4 * 1024;
    public static String filePath = "/home/jss/open-elis-upload/";

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("/tmp"));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax(maxFileSize);

        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            for (Object fileItem : fileItems) {
                FileItem fi = (FileItem) fileItem;
                if (!fi.isFormField()) {
                    // Get the uploaded file parameters
//                    String String = fi.getFieldName();
                    String fileName = fi.getName();
//                    String contentType = fi.getContentType();
//                    boolean isInMemory = fi.isInMemory();
//                    long sizeInBytes = fi.getSize();
//                    Write the file
                    File file;
                    if (fileName.lastIndexOf("\\") >= 0) {
                        file = new File(filePath +
                                fileName.substring(fileName.lastIndexOf("\\")));
                    } else {
                        file = new File(filePath +
                                fileName.substring(fileName.lastIndexOf("\\") + 1));
                    }
                    fi.write(file);
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

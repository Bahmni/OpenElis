/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package us.mn.state.health.lims.upload.action;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.bahmni.csv.CSVFile;
import org.bahmni.fileimport.FileImporter;
import org.jfree.data.io.CSV;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.login.valueholder.UserSessionData;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.upload.patient.CSVPatient;
import us.mn.state.health.lims.upload.patient.PatientPersister;
import us.mn.state.health.lims.upload.sample.CSVSample;
import us.mn.state.health.lims.upload.sample.TestResultPersister;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadAction extends BaseAction {
    public static final String YYYY_MM_DD_HH_MM_SS = "_yyyy-MM-dd_HH:mm:ss";
    public static final String PARENT_OF_UPLOADED_FILES_DIRECTORY = "parentOfUploadedFilesDirectory";
    public static final String UPLOADED_FILES_DIRECTORY = "uploadedFilesDirectory";
    public static final String DURATION_IN_DAYS_FOR_UPLOAD_STATUSES = "durationInDaysForUploadStatuses";

    private static Logger logger = LogManager.getLogger(UploadAction.class);

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
        }
        DynaActionForm dynaForm = (DynaActionForm) form;
        String importType = dynaForm.getString("importType");
        FormFile file = (FormFile) dynaForm.get("file");

        try {
            String fileName = file.getFileName();
            CSVFile downloadedFile = getFile(fileName);
            writeToFileSystem(file, new File(downloadedFile.getAbsolutePath()));

            UserSessionData userSessionData = (UserSessionData) request.getSession().getAttribute(USER_SESSION_DATA);

            if (importType.equals("patient")) {
                PatientPersister patientPersister = new PatientPersister(request.getContextPath());
                FileImporter<CSVPatient> csvPatientFileImporter = new FileImporter<>();
                boolean hasStartedUpload = csvPatientFileImporter.importCSV(fileName, downloadedFile, patientPersister, CSVPatient.class, new ELISJDBCConnectionProvider(), userSessionData.getLoginName());
                if (!hasStartedUpload) {
                    return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
                }
            } else if (importType.equals("sample")) {
                TestResultPersister testResultPersister = new TestResultPersister(request.getContextPath());
                FileImporter <CSVSample> fileImporter = new FileImporter<>();
                boolean hasStartedUpload = fileImporter.importCSV(fileName, downloadedFile, testResultPersister, CSVSample.class, new ELISJDBCConnectionProvider(), userSessionData.getLoginName());
                if (!hasStartedUpload) {
                    return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
            return mapping.findForward(IActionConstants.FWD_VALIDATION_ERROR);
        }
        return mapping.findForward(IActionConstants.FWD_SUCCESS);
    }

    private void writeToFileSystem(FormFile file, File aFile) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(aFile);
        fileOutputStream.write(file.getFileData());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private CSVFile getFile(String fileName) {
        int indexForSlash = fileName.lastIndexOf("\\");
        String substring;
        if (indexForSlash >= 0) {
            substring = fileName.substring(indexForSlash);
        } else {
            substring = fileName.substring(indexForSlash + 1);
        }
        String fileNameWithoutExtension = substring.substring(0, substring.lastIndexOf("."));
        String fileExtension = substring.substring(substring.lastIndexOf("."));

        String timestampForFile = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(new Date());

        String parentForUploadedFilesDirectory = new SiteInformationDAOImpl().getSiteInformationByName(PARENT_OF_UPLOADED_FILES_DIRECTORY).getValue();
        String uploadedFilesDirectory = new SiteInformationDAOImpl().getSiteInformationByName(UPLOADED_FILES_DIRECTORY).getValue();

        String relativeFilePath = fileNameWithoutExtension + timestampForFile + fileExtension;
        String uploadedFilesBasePath = parentForUploadedFilesDirectory + uploadedFilesDirectory;
        return new CSVFile(uploadedFilesBasePath, relativeFilePath);
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


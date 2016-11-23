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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.fileimport.ImportStatus;
import org.bahmni.fileimport.dao.ImportStatusDao;
import org.joda.time.LocalDate;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class UploadDashboardAction extends BaseAction {
    private SiteInformationDAO siteInformationDAO;
    private ImportStatusDao importStatusDao;
    private ObjectMapper objectMapper;

    public UploadDashboardAction() {
        this(new SiteInformationDAOImpl(), new ImportStatusDao(new ELISJDBCConnectionProvider()), ObjectMapperRepository.objectMapper);
    }

    public UploadDashboardAction(SiteInformationDAO siteInformationDAO, ImportStatusDao importStatusDao, ObjectMapper objectMapper) {
        this.siteInformationDAO = siteInformationDAO;
        this.importStatusDao = importStatusDao;
        this.objectMapper = objectMapper;
    }

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uploadedFilesDirectory = siteInformationDAO.getSiteInformationByName(UploadAction.UPLOADED_FILES_DIRECTORY).getValue();
        int durationInDaysForDisplayingUploadStatuses = Integer.parseInt(siteInformationDAO.getSiteInformationByName(UploadAction.DURATION_IN_DAYS_FOR_UPLOAD_STATUSES).getValue());

        LocalDate thirtyDaysBefore = new LocalDate(new Date()).minusDays(durationInDaysForDisplayingUploadStatuses);
        List<ImportStatus> uploads = importStatusDao.getImportStatusFromDate(thirtyDaysBefore.toDate());

        for (ImportStatus uploadStatus : uploads) {
            createHTMLLinkForErrorFile(uploadedFilesDirectory, uploadStatus);
            retainErrorMessageOnlyFromStackTrace(uploadStatus);
        }

        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), uploads);

        return null;
    }

    private void retainErrorMessageOnlyFromStackTrace(ImportStatus uploadStatus) {
        String stackTrace = uploadStatus.getStackTrace();
        if (stackTrace != null && stackTrace.trim().length() > 0) {
            uploadStatus.setStackTrace(stackTrace.substring(stackTrace.indexOf(":") + 1, stackTrace.indexOf("\n")));
        }
    }

    private void createHTMLLinkForErrorFile(String uploadedFilesDirectory, ImportStatus uploadStatus) {
        String errorFileName = uploadStatus.getErrorFileName();
        if (!StringUtils.isEmpty(errorFileName)) {
            String name = FilenameUtils.getName(errorFileName);
            String urlForErrorFile = uploadedFilesDirectory + name;
            uploadStatus.setErrorFileName(urlForErrorFile);
        }
    }

    @Override
    protected String getPageTitleKey() {
        return "Upload Dashboard";
    }

    @Override
    protected String getPageSubtitleKey() {
        return "Upload Dashboard";
    }
}

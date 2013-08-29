package us.mn.state.health.lims.upload.action;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.fileimport.ImportStatus;
import org.bahmni.fileimport.dao.ImportStatusDao;
import org.codehaus.jackson.map.ObjectMapper;
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
            String errorFileName = uploadStatus.getErrorFileName();
            if (!StringUtils.isEmpty(errorFileName)) {
                String name = FilenameUtils.getName(errorFileName);
                String urlForErrorFile = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + uploadedFilesDirectory + name;
                uploadStatus.setErrorFileName(urlForErrorFile);
            }
        }

        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), uploads);

        return null;
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

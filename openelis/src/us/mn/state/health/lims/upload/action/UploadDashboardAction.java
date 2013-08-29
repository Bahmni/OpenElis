package us.mn.state.health.lims.upload.action;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.fileimport.ImportStatus;
import org.bahmni.fileimport.dao.ImportStatusDao;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDate;
import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UploadDashboardAction extends BaseAction {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LocalDate thirtyDaysBefore = new LocalDate(new Date()).minusDays(30);
        List<ImportStatus> uploads = new ImportStatusDao(new ELISJDBCConnectionProvider()).getImportStatusFromDate(thirtyDaysBefore.toDate());

        String uploadedFilesDirectory = new SiteInformationDAOImpl().getSiteInformationByName(UploadAction.UPLOADED_FILES_DIRECTORY).getValue();

        for (ImportStatus uploadStatus : uploads) {
            String errorFileName = uploadStatus.getErrorFileName();
            if (!StringUtils.isEmpty(errorFileName)) {
                String name = FilenameUtils.getName(errorFileName);
                String urlForErrorFile = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + uploadedFilesDirectory + name;
                uploadStatus.setErrorFileName(urlForErrorFile);
            }
        }

        response.setContentType("application/json");
        ObjectMapper objectMapper = ObjectMapperRepository.objectMapper;
        objectMapper.setDateFormat(new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS));
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

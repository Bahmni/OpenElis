package us.mn.state.health.lims.upload.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import org.bahmni.fileimport.ImportStatus;
import org.bahmni.fileimport.dao.ImportStatusDao;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDate;
import us.mn.state.health.lims.common.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UploadDashboardAction extends BaseAction {
    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LocalDate thirtyDaysBefore = new LocalDate(new Date()).minusDays(30);
        List<ImportStatus> uploads = new ImportStatusDao(new ELISJDBCConnectionProvider()).getImportStatusFromDate(thirtyDaysBefore.toDate());

        response.setContentType("application/json");
        ObjectMapper objectMapper = ObjectMapperRepository.objectMapper;
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
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

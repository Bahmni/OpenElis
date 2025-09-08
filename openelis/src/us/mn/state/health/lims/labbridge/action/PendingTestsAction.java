package us.mn.state.health.lims.labbridge.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import org.bahmni.feed.openelis.ObjectMapperRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class PendingTestsAction extends Action {

    private final String APPLICATION_JSON = "application/json";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        String assignedStatusId = getStatusIdBySysName("analysis_status_assigned");
        String resultCompletedStatusId = getStatusIdBySysName("analysis_status_result_completed");
        String notTestedStatusId = getStatusIdBySysName("analysis_status_not_tested");
        String canceledStatusId = getStatusIdBySysName("analysis_status_canceled");

        String sql =
            "select a.id as analysis_id, t.id as test_id, t.name as test_name, t.description, a.status_id, " +
            " s.accession_number, p.id as patient_id, per.first_name, per.last_name, per.middle_name, " +
            " s.collection_date, s.entered_date, a.started_date, a.comment " +
            "from analysis a " +
            "join sample_item si on si.id = a.sampitem_id " +
            "join sample s on s.id = si.samp_id " +
            "left join sample_human sh on sh.samp_id = s.id " +
            "left join patient p on p.id = sh.patient_id " +
            "left join person per on per.id = p.person_id " +
            "join test t on t.id = a.test_id " +
            "where a.status_id in (:assignedStatusId, :resultCompletedStatusId, :notTestedStatusId) " +
            "and a.status_id <> :canceledStatusId " +
            "and a.revision = (select max(a2.revision) from analysis a2 where a2.sampitem_id = a.sampitem_id and a2.test_id = a.test_id)";

        Query q = HibernateUtil.getSession().createSQLQuery(sql);
        q.setInteger("assignedStatusId", Integer.parseInt(assignedStatusId));
        q.setInteger("resultCompletedStatusId", Integer.parseInt(resultCompletedStatusId));
        q.setInteger("notTestedStatusId", Integer.parseInt(notTestedStatusId));
        q.setInteger("canceledStatusId", Integer.parseInt(canceledStatusId));

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.list();
        for (Object[] r : rows) {
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("analysisId", toStringSafe(r[0]));
            row.put("testId", toStringSafe(r[1]));
            row.put("testName", toStringSafe(r[2]));
            row.put("testDescription", toStringSafe(r[3]));
            row.put("status", toStringSafe(r[4]));
            row.put("accessionNumber", toStringSafe(r[5]));
            row.put("patientId", toStringSafe(r[6]));
            row.put("patientFirstName", toStringSafe(r[7]));
            row.put("patientLastName", toStringSafe(r[8]));
            row.put("patientMiddleName", toStringSafe(r[9]));
            row.put("collectionDate", toStringSafe(r[10]));
            row.put("enteredDate", toStringSafe(r[11]));
            row.put("startedDate", toStringSafe(r[12]));
            row.put("comment", toStringSafe(r[13]));
            data.add(row);
        }

        Map<String, Object> payload = new LinkedHashMap<String, Object>();
        payload.put("status", "success");
        payload.put("message", "");
        payload.put("data", data);

        response.setContentType(APPLICATION_JSON);
        ObjectMapperRepository.objectMapper.writeValue(response.getWriter(), payload);
        return null;
    }

    private String toStringSafe(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    private String getStatusIdBySysName(String sysName) {
        if ("analysis_status_assigned".equals(sysName)) return "1";  // Test Entered
        if ("analysis_status_result_completed".equals(sysName)) return "16"; // Technical Acceptance  
        if ("analysis_status_not_tested".equals(sysName)) return "4"; // Not Tested
        if ("analysis_status_canceled".equals(sysName)) return "15"; // Test Canceled
        return "1";
    }
}



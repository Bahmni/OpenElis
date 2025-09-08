package us.mn.state.health.lims.labbridge.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateTestResultAction extends Action {
    private final String APPLICATION_JSON = "application/json";

    private static class UpdatePayload {
        public String analysisId;
        public String testId;
        public String resultValue;
        public String resultType;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UpdatePayload payload = parsePayload(request);
        if (payload == null || payload.analysisId == null || payload.testId == null || payload.resultValue == null) {
            return writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters: analysisId, testId, resultValue");
        }

        try {
            // Get database connection using OpenELIS existing infrastructure
            Connection conn = us.mn.state.health.lims.hibernate.HibernateUtil.getSession().connection();
            
            // First, check if the analysis exists and is in pending status
            String checkSql = "SELECT a.id, a.status_id, t.description as test_name, s.accession_number " +
                            "FROM analysis a " +
                            "JOIN test t ON a.test_id = t.id " +
                            "JOIN sample_item si ON a.sampitem_id = si.id " +
                            "JOIN sample s ON si.samp_id = s.id " +
                            "WHERE a.id = ?::numeric AND a.test_id = ?::numeric";
            
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, payload.analysisId);
            checkStmt.setString(2, payload.testId);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (!checkRs.next()) {
                checkRs.close();
                checkStmt.close();
                return writeError(response, HttpServletResponse.SC_NOT_FOUND, "Analysis not found or test ID mismatch");
            }
            
            String currentStatus = checkRs.getString("status_id");
            String testName = checkRs.getString("test_name");
            String accessionNumber = checkRs.getString("accession_number");
            
            // Check if analysis is in pending status (status_id = 4 for "Not Tested")
            if (!"4".equals(currentStatus)) {
                checkRs.close();
                checkStmt.close();
                return writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Analysis is not in pending status. Current status: " + currentStatus);
            }
            
            checkRs.close();
            checkStmt.close();
            
            // Find or create a test_result entry for this test and value
            String testResultSql = "SELECT id FROM test_result WHERE test_id = ?::numeric AND value = ? LIMIT 1";
            PreparedStatement testResultStmt = conn.prepareStatement(testResultSql);
            testResultStmt.setString(1, payload.testId);
            testResultStmt.setString(2, payload.resultValue);
            ResultSet testResultRs = testResultStmt.executeQuery();
            
            String testResultId = null;
            if (testResultRs.next()) {
                testResultId = testResultRs.getString("id");
            } else {
                // Get the next available ID for test_result
                String getNextIdSql = "SELECT COALESCE(MAX(id), 0) + 1 FROM test_result";
                PreparedStatement nextIdStmt = conn.prepareStatement(getNextIdSql);
                ResultSet nextIdRs = nextIdStmt.executeQuery();
                String nextId = "1";
                if (nextIdRs.next()) {
                    nextId = nextIdRs.getString(1);
                }
                nextIdRs.close();
                nextIdStmt.close();
                
                // Create a new test_result entry
                String insertTestResultSql = "INSERT INTO test_result (id, test_id, value, tst_rslt_type, lastupdated) VALUES (?::numeric, ?::numeric, ?, ?, NOW()) RETURNING id";
                PreparedStatement insertTestResultStmt = conn.prepareStatement(insertTestResultSql);
                insertTestResultStmt.setString(1, nextId);
                insertTestResultStmt.setString(2, payload.testId);
                insertTestResultStmt.setString(3, payload.resultValue);
                insertTestResultStmt.setString(4, payload.resultType != null ? payload.resultType : "R");
                ResultSet insertRs = insertTestResultStmt.executeQuery();
                if (insertRs.next()) {
                    testResultId = insertRs.getString("id");
                }
                insertRs.close();
                insertTestResultStmt.close();
            }
            testResultRs.close();
            testResultStmt.close();
            
            if (testResultId == null) {
                return writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create or find test_result entry");
            }
            
            // Check if a result already exists for this analysis and test_result
            String checkResultSql = "SELECT id FROM result WHERE analysis_id = ?::numeric AND test_result_id = ?::numeric LIMIT 1";
            PreparedStatement checkResultStmt = conn.prepareStatement(checkResultSql);
            checkResultStmt.setString(1, payload.analysisId);
            checkResultStmt.setString(2, testResultId);
            ResultSet checkResultRs = checkResultStmt.executeQuery();
            
            int resultRows = 0;
            if (checkResultRs.next()) {
                // Update existing result
                String updateResultSql = "UPDATE result SET value = ?, result_type = ?, lastupdated = NOW() WHERE analysis_id = ?::numeric AND test_result_id = ?::numeric";
                PreparedStatement updateResultStmt = conn.prepareStatement(updateResultSql);
                updateResultStmt.setString(1, payload.resultValue);
                updateResultStmt.setString(2, payload.resultType != null ? payload.resultType : "R");
                updateResultStmt.setString(3, payload.analysisId);
                updateResultStmt.setString(4, testResultId);
                resultRows = updateResultStmt.executeUpdate();
                updateResultStmt.close();
            } else {
                // Get the next available ID for result
                String getNextResultIdSql = "SELECT COALESCE(MAX(id), 0) + 1 FROM result";
                PreparedStatement nextResultIdStmt = conn.prepareStatement(getNextResultIdSql);
                ResultSet nextResultIdRs = nextResultIdStmt.executeQuery();
                String nextResultId = "1";
                if (nextResultIdRs.next()) {
                    nextResultId = nextResultIdRs.getString(1);
                }
                nextResultIdRs.close();
                nextResultIdStmt.close();
                
                // Insert new result
                String insertResultSql = "INSERT INTO result (id, analysis_id, test_result_id, value, result_type, lastupdated) VALUES (?::numeric, ?::numeric, ?::numeric, ?, ?, NOW())";
                PreparedStatement insertResultStmt = conn.prepareStatement(insertResultSql);
                insertResultStmt.setString(1, nextResultId);
                insertResultStmt.setString(2, payload.analysisId);
                insertResultStmt.setString(3, testResultId);
                insertResultStmt.setString(4, payload.resultValue);
                insertResultStmt.setString(5, payload.resultType != null ? payload.resultType : "R");
                resultRows = insertResultStmt.executeUpdate();
                insertResultStmt.close();
            }
            checkResultRs.close();
            checkResultStmt.close();
            
            // Update analysis status to "Finalized" (status_id = 6)
            String updateStatusSql = "UPDATE analysis SET status_id = 6::numeric, completed_date = NOW() WHERE id = ?::numeric";
            PreparedStatement statusStmt = conn.prepareStatement(updateStatusSql);
            statusStmt.setString(1, payload.analysisId);
            int statusRows = statusStmt.executeUpdate();
            statusStmt.close();
            
            // Create success response
            Map<String, Object> resp = new LinkedHashMap<String, Object>();
            resp.put("status", "success");
            resp.put("message", "Test result updated successfully");
            
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("analysisId", payload.analysisId);
            data.put("testId", payload.testId);
            data.put("testName", testName);
            data.put("accessionNumber", accessionNumber);
            data.put("resultValue", payload.resultValue);
            data.put("resultType", payload.resultType != null ? payload.resultType : "R");
            data.put("testResultId", testResultId);
            data.put("statusUpdated", statusRows > 0);
            data.put("rowsAffected", resultRows);
            resp.put("data", data);
            
            response.setContentType(APPLICATION_JSON);
            ObjectMapperRepository.objectMapper.writeValue(response.getWriter(), resp);
            return null;
            
        } catch (Exception e) {
            return writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating test result: " + e.getMessage());
        }
    }

    private UpdatePayload parsePayload(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = ObjectMapperRepository.objectMapper;
        return mapper.readValue(request.getInputStream(), UpdatePayload.class);
    }

    private ActionForward writeError(HttpServletResponse response, int status, String message) throws IOException {
        Map<String, Object> resp = new LinkedHashMap<String, Object>();
        resp.put("status", "error");
        resp.put("message", message);
        response.setStatus(status);
        response.setContentType(APPLICATION_JSON);
        ObjectMapperRepository.objectMapper.writeValue(response.getWriter(), resp);
        return null;
    }
}



package us.mn.state.health.lims.resultvalidation.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.resultvalidation.action.util.ResultValidationPaging;
import us.mn.state.health.lims.resultvalidation.bean.AnalysisItem;
import us.mn.state.health.lims.resultvalidation.util.ResultsValidationUtility;
import us.mn.state.health.lims.statusofsample.util.StatusOfSampleUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ResultValidationForAccessionNumberAction extends BaseResultValidationAction {

    private static Logger logger = Logger.getLogger(ResultValidationForAccessionNumberAction.class);


    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BaseActionForm dynaForm = (BaseActionForm) form;
        String accessionNumber = request.getParameter("accessionNumber");
        ResultValidationPaging paging = new ResultValidationPaging();
        // Initialize the form.
        dynaForm.initialize(mapping);

        try {
            setRequestType(BaseResultValidationAction.VALIDATION_BY_ACCESSION_NUMBER); // this sets the page title/subtitle

            ResultsValidationUtility resultsValidationUtility = new ResultsValidationUtility();
            List<AnalysisItem> resultList = resultsValidationUtility.getResultValidationListByAccessionNumber(
                    getToBeValidatedStatuses(), accessionNumber);

            paging.setDatabaseResults(request, dynaForm, resultList);
        } catch (Throwable e) {
            logger.error(e);
        }

        return mapping.findForward(FWD_SUCCESS);
    }


  /*  @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        BaseActionForm dynaForm = (BaseActionForm) form;

        request.getSession().setAttribute(SAVE_DISABLED, "true");
        String testSectionName = "hematology";
        String testName = (request.getParameter("test"));
        String accessionNumber = request.getParameter("accessionNumber");


        ResultValidationPaging paging = new ResultValidationPaging();
        String newPage = request.getParameter("page");

        if (GenericValidator.isBlankOrNull(newPage)) {

            // Initialize the form.
            dynaForm.initialize(mapping);

            List<AnalysisItem> resultList = new ArrayList<AnalysisItem>();
            ResultsValidationUtility resultsValidationUtility = new ResultsValidationUtility();
            setRequestType(testSectionName);

//            if (!GenericValidator.isBlankOrNull(testSectionName)) {
//                String sectionName = Character.toUpperCase(testSectionName.charAt(0)) + testSectionName.substring(1);
//                sectionName = getDBSectionName(sectionName);
            resultList = resultsValidationUtility.getResultValidationListByAccessionNumber(getToBeValidatedStatuses(), accessionNumber);
            paging.setDatabaseResults(request, dynaForm, resultList);
//            }

        } else {
            paging.page(request, dynaForm, newPage);
        }

        if (testSectionName.equals("serology")) {
            return mapping.findForward("elisaSuccess");
        } else {
            return mapping.findForward(FWD_SUCCESS);
        }
    }
*/
    private List<StatusOfSampleUtil.AnalysisStatus> getToBeValidatedStatuses() {
        List<StatusOfSampleUtil.AnalysisStatus> validationStatus = new ArrayList<>();
        validationStatus.add(StatusOfSampleUtil.AnalysisStatus.TechnicalAcceptance);
        validationStatus.add(StatusOfSampleUtil.AnalysisStatus.Canceled);
        return validationStatus;
    }

}

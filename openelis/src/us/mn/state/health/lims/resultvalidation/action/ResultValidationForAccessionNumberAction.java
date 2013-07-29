package us.mn.state.health.lims.resultvalidation.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.resultvalidation.action.util.ResultValidationPaging;
import us.mn.state.health.lims.resultvalidation.bean.AnalysisItem;
import us.mn.state.health.lims.resultvalidation.util.ResultsValidationUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ResultValidationForAccessionNumberAction extends BaseResultValidationAction {
    private static Logger logger = Logger.getLogger(ResultValidationForAccessionNumberAction.class);

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        BaseActionForm dynaForm = (BaseActionForm) form;
        String accessionNumber = request.getParameter("accessionNumber");
        ResultValidationPaging paging = new ResultValidationPaging();
        // Initialize the form.
        dynaForm.initialize(mapping);

        setRequestType(BaseResultValidationAction.VALIDATION_BY_ACCESSION_NUMBER); // this sets the page title/subtitle

        List<AnalysisItem> resultList = new ResultsValidationUtility().getResultValidationListByAccessionNumber(
                getToBeValidatedStatuses(), accessionNumber);

        paging.setDatabaseResults(request, dynaForm, resultList);

        return mapping.findForward(FWD_SUCCESS);
    }

}

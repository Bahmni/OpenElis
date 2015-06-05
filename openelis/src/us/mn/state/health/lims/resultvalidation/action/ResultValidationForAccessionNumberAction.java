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

package us.mn.state.health.lims.resultvalidation.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.note.valueholder.Note;
import us.mn.state.health.lims.resultvalidation.action.util.ResultValidationPaging;
import us.mn.state.health.lims.resultvalidation.bean.AnalysisItem;
import us.mn.state.health.lims.resultvalidation.util.ResultsValidationUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ResultValidationForAccessionNumberAction extends BaseResultValidationAction {

    @Override
    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        ResultsValidationUtility resultsValidationUtility = new ResultsValidationUtility();
        BaseActionForm dynaForm = (BaseActionForm) form;
        String accessionNumber = request.getParameter("accessionNumber");
        ResultValidationPaging paging = new ResultValidationPaging();
        // Initialize the form.
        dynaForm.initialize(mapping);

        setRequestType(BaseResultValidationAction.VALIDATION_BY_ACCESSION_NUMBER); // this sets the page title/subtitle
        List<AnalysisItem> resultList = resultsValidationUtility.getResultValidationListByAccessionNumber(getToBeValidatedStatuses(), accessionNumber);
        paging.setDatabaseResults(request, dynaForm, resultList);
        List<Note> accessionNotes = resultsValidationUtility.getAccessionNotes(accessionNumber);

        PropertyUtils.setProperty(dynaForm, "savedAccessionNotes", accessionNotes);
        PropertyUtils.setProperty(dynaForm, "canCaptureAccessionNotes", Boolean.TRUE);

        return mapping.findForward(FWD_SUCCESS);


    }

}

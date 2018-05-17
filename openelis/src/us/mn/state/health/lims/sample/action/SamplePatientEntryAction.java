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
* Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
*
*/
package us.mn.state.health.lims.sample.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.lims.common.action.BaseActionForm;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.formfields.FormFields;
import us.mn.state.health.lims.common.services.DisplayListService;
import us.mn.state.health.lims.common.services.DisplayListService.ListType;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.patient.action.bean.PatientManagmentInfo;
import us.mn.state.health.lims.provider.dao.ProviderDAO;
import us.mn.state.health.lims.provider.daoimpl.ProviderDAOImpl;
import us.mn.state.health.lims.samplesource.dao.SampleSourceDAO;
import us.mn.state.health.lims.samplesource.daoimpl.SampleSourceDAOImpl;
import us.mn.state.health.lims.siteinformation.dao.SiteInformationDAO;
import us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl;
import us.mn.state.health.lims.siteinformation.valueholder.SiteInformation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * The SampleEntryAction class represents the initial Action for the SampleEntry
 * form of the application
 *
 */
public class SamplePatientEntryAction extends BaseSampleEntryAction {

    private SampleSourceDAO sampleSourceDAO;
    private ProviderDAO providerDAO;

	public SamplePatientEntryAction() {
        this.sampleSourceDAO = new SampleSourceDAOImpl();
        this.providerDAO = new ProviderDAOImpl();
    }

    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

        String[] fieldsetOrder  ={"order","samples","patient"};
		String forward = "success";

		request.getSession().setAttribute(IActionConstants.SAVE_DISABLED, IActionConstants.TRUE);

		BaseActionForm dynaForm = (BaseActionForm) form;

		// Initialize the form.
		dynaForm.initialize(mapping);

		// Set received date and entered date to today's date
		//Date today = Calendar.getInstance().getTime();
		String dateAsText = DateUtil.getCurrentDateAsText();
		PropertyUtils.setProperty(form, "currentDate", dateAsText);

		boolean needRequesterList = FormFields.getInstance().useField(FormFields.Field.RequesterSiteList);
		boolean needSampleInitialConditionList = FormFields.getInstance().useField(FormFields.Field.InitialSampleCondition);
		boolean needPaymentOptions = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.trackPatientPayment, "true");

        SiteInformationDAO siteInfo = new SiteInformationDAOImpl();
        SiteInformation sampleEntryFieldsetOrder = siteInfo.getSiteInformationByName("SampleEntryFieldsetOrder");
        if(sampleEntryFieldsetOrder != null &&
                sampleEntryFieldsetOrder.getValue() != null &&
                !sampleEntryFieldsetOrder.getValue().isEmpty() ){
            fieldsetOrder = sampleEntryFieldsetOrder.getValue().split("\\|");
        }



        PropertyUtils.setProperty(dynaForm, "receivedDateForDisplay", dateAsText);
		PropertyUtils.setProperty(dynaForm, "requestDate", dateAsText);
        PropertyUtils.setProperty(dynaForm, "collapsePatientInfo", true);
		PropertyUtils.setProperty(dynaForm, "patientProperties", new PatientManagmentInfo());
		PropertyUtils.setProperty(dynaForm, "sampleTypes", DisplayListService.getList(ListType.SAMPLE_TYPE));
		PropertyUtils.setProperty(dynaForm, "orderTypes", DisplayListService.getList(ListType.SAMPLE_PATIENT_PRIMARY_ORDER_TYPE));
		PropertyUtils.setProperty(dynaForm, "followupPeriodOrderTypes", DisplayListService.getList(ListType.SAMPLE_PATIENT_FOLLOW_UP_PERIOD_ORDER_TYPE));
		PropertyUtils.setProperty(dynaForm, "initialPeriodOrderTypes", DisplayListService.getList(ListType.SAMPLE_PATIENT_INITIAL_PERIOD_ORDER_TYPE));
		PropertyUtils.setProperty(dynaForm, "testSectionList", DisplayListService.getList(ListType.TEST_SECTION));
		PropertyUtils.setProperty(dynaForm, "labNo", "");
		PropertyUtils.setProperty(dynaForm, "sampleEntryFieldsetOrder", Arrays.asList(fieldsetOrder));
        PropertyUtils.setProperty(dynaForm, "sampleSourceList", sampleSourceDAO.getAllActive());
        PropertyUtils.setProperty(dynaForm, "providerList", providerDAO.getAllActiveProviders());

		addProjectList(dynaForm);

		if (needRequesterList) {
			PropertyUtils.setProperty(dynaForm, "referringSiteList", DisplayListService.getFreshList(ListType.SAMPLE_PATIENT_REFERRING_CLINIC));
		}

		if (needSampleInitialConditionList) {
			PropertyUtils.setProperty(dynaForm, "initialSampleConditionList", DisplayListService.getList(ListType.INITIAL_SAMPLE_CONDITION));
		}

		if (needPaymentOptions) {
			setDictionaryList(dynaForm, "paymentOptions", "PP", true);
		}
		return mapping.findForward(forward);
	}

}

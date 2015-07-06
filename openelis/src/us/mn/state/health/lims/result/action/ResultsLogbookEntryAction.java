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
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.result.action;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import us.mn.state.health.lims.common.action.BaseAction;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.common.util.ConfigurationProperties.Property;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.inventory.action.InventoryUtility;
import us.mn.state.health.lims.inventory.form.InventoryKitItem;
import us.mn.state.health.lims.organization.util.OrganizationUtils;
import us.mn.state.health.lims.referral.util.ReferralUtil;
import us.mn.state.health.lims.result.action.util.ResultsLoadUtility;
import us.mn.state.health.lims.result.action.util.ResultsPaging;
import us.mn.state.health.lims.statusofsample.util.StatusRules;
import us.mn.state.health.lims.test.beanItems.TestResultItem;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.TestSection;

public class ResultsLogbookEntryAction extends BaseAction {

	private InventoryUtility inventoryUtility = new InventoryUtility();
    private String testSectionName;

    protected ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

        request.getSession().setAttribute(SAVE_DISABLED, TRUE);

        String forward = FWD_SUCCESS;
        String requestedPage = request.getParameter("page");
        String type = request.getParameter("type");
        String currentDate = getCurrentDate(request);

        DynaActionForm dynaForm = (DynaActionForm) form;

        PropertyUtils.setProperty(dynaForm, "currentDate", currentDate);
        PropertyUtils.setProperty(dynaForm, "logbookType", type);
        PropertyUtils.setProperty(dynaForm, "referralReasons", ReferralUtil.getReferralReasons());
        PropertyUtils.setProperty(dynaForm, "displayTestMethod", true);
        PropertyUtils.setProperty(dynaForm, "referralOrganizations", OrganizationUtils.getReferralOrganizations());

        testSectionName = getDecoded(type);

        String testSectionId = getTestSelectId(testSectionName);
        List<TestResultItem> tests = null;

        ResultsPaging paging = new ResultsPaging();
        List<InventoryKitItem> inventoryList = new ArrayList<InventoryKitItem>();

        ResultsLoadUtility resultsLoadUtility = new StatusRules().setAllowableStatusForLoadingResults(currentUserId);

        if (GenericValidator.isBlankOrNull(requestedPage)) {

            if (testSectionId != null) {
                tests = resultsLoadUtility.getUnfinishedTestResultItemsInTestSection(testSectionId);
            } else {
                tests = new ArrayList<TestResultItem>();
            }

            if( ConfigurationProperties.getInstance().isPropertyValueEqual(Property.patientDataOnResultsByRole, "true") &&
					!userHasPermissionForModule(request, "PatientResults") ){
                for( TestResultItem resultItem : tests){
                    resultItem.setPatientInfo("---");
                }
            }

            paging.setDatabaseResults(request, dynaForm, tests);
        } else {
            paging.page(request, dynaForm, requestedPage);
        }

        //this does not look right what happens after a new page!!!
        if (resultsLoadUtility.inventoryNeeded()) {
            inventoryList = inventoryUtility.getExistingActiveInventory();
            PropertyUtils.setProperty(dynaForm, "displayTestKit", true);
        } else {
            PropertyUtils.setProperty(dynaForm, "displayTestKit", false);
        }

        PropertyUtils.setProperty(dynaForm, "inventoryItems", inventoryList);

		return mapping.findForward(forward);
	}

    private String getDecoded(String type) {
        try {
            return URLDecoder.decode(type, Charset.defaultCharset().displayName());
        } catch (UnsupportedEncodingException e) {
            return "UTF-8";
        }
    }

    @Override
    public String getPageSubtitleKey() {
        return testSectionName;
    }

    @Override
    public String getPageTitleKey() {
        return "banner.menu.results";
    }

	private String getTestSelectId(String testSectionName) {

		TestSection testSection = new TestSection();
		testSection.setTestSectionName(testSectionName);

		TestSectionDAO testSectionDAO = new TestSectionDAOImpl();
		testSection = testSectionDAO.getTestSectionByName(testSection);

		return testSection == null ? null : testSection.getId();
	}

	private String getCurrentDate(HttpServletRequest request) {
		Date today = Calendar.getInstance().getTime();
		Locale locale = (Locale) request.getSession().getAttribute("org.apache.struts.action.LOCALE");
		return DateUtil.formatDateAsText(today, locale);

	}

}

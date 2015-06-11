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
package us.mn.state.health.lims.common.action;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.*;
import us.mn.state.health.lims.analysis.valueholder.Analysis;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.provider.validation.AccessionNumberValidationProvider;
import us.mn.state.health.lims.common.security.PageIdentityUtil;
import us.mn.state.health.lims.common.util.IdValuePair;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.common.util.resources.ResourceLocator;
import us.mn.state.health.lims.common.util.validator.ActionError;
import us.mn.state.health.lims.common.valueholder.BaseTestComparator;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.login.dao.UserModuleDAO;
import us.mn.state.health.lims.login.daoimpl.UserModuleDAOImpl;
import us.mn.state.health.lims.login.valueholder.UserSessionData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class BaseAction extends Action implements IActionConstants {
    private static final boolean USE_PARAMETERS = true;
    protected String currentUserId;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // return to login page if user session is not found
        BaseAction perRequestAction = ActionFactory.newAction(this);

        UserModuleDAO userModuleDAO = new UserModuleDAOImpl();
        if (userModuleDAO.isSessionExpired(request)) {
            ActionMessages errors = new ActionMessages();
            ActionError error = new ActionError("login.error.session.message", null, null);
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            perRequestAction.saveErrors(request, errors);
            return mapping.findForward(LOGIN_PAGE);
        }
        // 'Save successful' set from action forwarding to this action
        if (FWD_SUCCESS.equals(request.getParameter("forward"))) {
            perRequestAction.setSuccessFlag(request);
        }
        perRequestAction.currentUserId = perRequestAction.getSysUserId(request);

        ActionForward forward = perRequestAction.performAction(mapping, form, request, response);
//        forward = performAction(mapping, form, request, response);
        return perRequestAction.postAction(mapping, form, request, userModuleDAO, forward);
    }

    private ActionForward postAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, UserModuleDAO userModuleDAO, ActionForward forward) throws Exception {
        String pageTitleKey;
        String pageSubtitleKey;
        String pageTitleKeyParameter;
        String pageSubtitleKeyParameter;
        String pageSubtitle;
        String pageTitle;
        pageTitleKey = getPageTitleKey(request, form);
        pageSubtitleKey = getPageSubtitleKey(request, form);

        pageTitleKeyParameter = getPageTitleKeyParameter(request, form);
        pageSubtitleKeyParameter = getPageSubtitleKeyParameter(request, form);

        // bugzilla 1512 internationalization
        request.getSession().setAttribute(Globals.LOCALE_KEY, SystemConfiguration.getInstance().getDefaultLocale());

        // bugzilla 1348
        if (StringUtil.isNullorNill(pageTitleKeyParameter)) {
            pageTitle = getMessageForKey(pageTitleKey);
        } else {
            pageTitle = getMessageForKey(request, pageTitleKey, pageTitleKeyParameter);
        }

        // bugzilla 1348
        if (StringUtil.isNullorNill(pageSubtitleKeyParameter)) {
            String messageForKey = getMessageForKey(pageSubtitleKey);
            pageSubtitle = (messageForKey == null) ? pageSubtitleKey : messageForKey;
        } else {
            pageSubtitle = getMessageForKey(request, pageSubtitleKey, pageSubtitleKeyParameter);
        }

        if (null != pageTitle)
            request.setAttribute(PAGE_TITLE_KEY, pageTitle);
        if (null != pageSubtitle)
            request.setAttribute(PAGE_SUBTITLE_KEY, pageSubtitle);

        // Set the form attributes
        setFormAttributes(form, request, mapping);

        // check for account disabled
        // bugzilla 2160
        if (userModuleDAO.isAccountDisabled(request)) {
            ActionMessages errors = new ActionMessages();
            ActionError error = new ActionError("login.error.account.disable", null, null);
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(request, errors);
            return mapping.findForward(LOGIN_PAGE);
        }

        // check for account locked
        // bugzilla 2160
        if (userModuleDAO.isAccountLocked(request)) {
            ActionMessages errors = new ActionMessages();
            ActionError error = new ActionError("login.error.account.lock", null, null);
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(request, errors);
            return mapping.findForward(LOGIN_PAGE);
        }

        // check for password expired
        // bugzilla 2160
        if (userModuleDAO.isPasswordExpired(request)) {
            ActionMessages errors = new ActionMessages();
            ActionError error = new ActionError("login.error.password.expired", null, null);
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(request, errors);
            return mapping.findForward(LOGIN_PAGE);
        }

        // uncomment to collect the actionNames
        // System.out.println("actionName: " + PageIdentityUtil.getActionName(request, USE_PARAMETERS));

        // check for user type (admin or non-admin)

        if (!userModuleDAO.isUserAdmin(request)) {
            if (SystemConfiguration.getInstance().getPermissionAgent().equals("ROLE")) {
                if (!PageIdentityUtil.isMainPage(request)) {

                    @SuppressWarnings("rawtypes")
                    HashSet accessMap = (HashSet) request.getSession().getAttribute(IActionConstants.PERMITTED_ACTIONS_MAP);

                    if (!accessMap.contains(PageIdentityUtil.getActionName(request))) {
                        return handlePermissionDenied(mapping, request, userModuleDAO.isSessionExpired(request));
                    }
                }
            } else {
                if (!userModuleDAO.isVerifyUserModule(request)) {
                    return handlePermissionDenied(mapping, request, userModuleDAO.isSessionExpired(request));
                }
            }
        }
        userModuleDAO.setupUserSessionTimeOut(request);

        return forward;
    }

    protected boolean userHasPermissionForModule(HttpServletRequest request, String module) {
        UserModuleDAO userModuleDAO = new UserModuleDAOImpl();
        if (!userModuleDAO.isUserAdmin(request) && SystemConfiguration.getInstance().getPermissionAgent().equals("ROLE")) {
            @SuppressWarnings("rawtypes")
            HashSet accessMap = (HashSet) request.getSession().getAttribute(IActionConstants.PERMITTED_ACTIONS_MAP);
            return accessMap.contains(module);
        }

        return true;
    }

    private ActionForward handlePermissionDenied(ActionMapping mapping, HttpServletRequest request, boolean sessionExpired) {
        ActionMessages errors = new ActionMessages();
        ActionError error = new ActionError("login.error.module.not.allow", null, null);
        errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        saveErrors(request, errors);
        // bugzilla 2154
        LogEvent.logInfo("BaseAction", "execute()", "======> NOT ALLOWED ACCESS TO THIS MODULE");

        return sessionExpired ? mapping.findForward(LOGIN_PAGE) : mapping.findForward(HOME_PAGE);
    }

    /**
     * Abstract method that sub classes must implement to perform the desired
     * action
     */
    protected abstract ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception;

    /**
     * Must be implemented by subclasses to set the title for the requested
     * page. The value returned should be a key String from the
     * ApplicationResources.properties file.
     *
     * @return the title key for this page.
     */
    protected abstract String getPageTitleKey();

    /**
     * Must be implemented by subclasses to set the subtitle for the requested
     * page. The value returned should be a key String from the
     * ApplicationResources.properties file.
     *
     * @return the subtitle key this page.
     */
    protected abstract String getPageSubtitleKey();

    /**
     * This getPageTitleKey method accepts a request and form parameter so that
     * a subclass can override the method and conditionally return different
     * titles.
     *
     * @param request the request
     * @param form    the form associated with this request.
     * @return the title key for this page.
     */
    protected String getPageTitleKey(HttpServletRequest request, ActionForm form) {
        return getPageTitleKey();
    }

    protected String getPageTitleKeyParameter(HttpServletRequest request, ActionForm form) {
        return null;
    }

    /**
     * This getSubtitleKey method accepts a request and form parameter so that a
     * subclass can override the method and conditionally return different
     * subtitles.
     *
     * @param request the request
     * @param form    the form associated with this request.
     * @return the subtitle key this page.
     */
    protected String getPageSubtitleKey(HttpServletRequest request, ActionForm form) {
        return getPageSubtitleKey();
    }

    protected String getPageSubtitleKeyParameter(HttpServletRequest request, ActionForm form) {
        return null;
    }

    /**
     * Utility method to simplify the lookup of MessageResource Strings in the
     * ApplicationResources.properties file for this application.
     *
     * @param messageKey the message key to look up
     */
    protected String getMessageForKey(String messageKey) throws Exception {
        return StringUtil.getContextualMessageForKey(messageKey);
    }

    protected String getMessageForKey(HttpServletRequest request, String messageKey, String arg0) throws Exception {
        if (null == messageKey)
            return null;
        java.util.Locale locale = (java.util.Locale) request.getSession().getAttribute("org.apache.struts.action.LOCALE");
        // Return the message for the user's locale.
        return ResourceLocator.getInstance().getMessageResources().getMessage(locale, messageKey, arg0);
    }

    protected void setFormAttributes(ActionForm form, HttpServletRequest request, ActionMapping mapping) throws Exception {
        try {
            if (null != form) {
                DynaActionForm theForm = (DynaActionForm) form;
                theForm.getDynaClass().getName();
                String name = theForm.getDynaClass().getName().toString();
                // use IActionConstants!
                request.setAttribute(FORM_NAME, name);
                request.setAttribute("formType", theForm.getClass().toString());
                String actionName = name.substring(1, name.length() - 4);
                actionName = name.substring(0, 1).toUpperCase() + actionName;
                request.setAttribute(ACTION_KEY, actionName);
                // bugzilla 2154
                LogEvent.logInfo("BaseAction", "setFormAttributes()", actionName);
            } else {
                //In case of cancel action, there is no form assosiated.. so action will be null
                request.setAttribute(ACTION_KEY, getActionName(mapping));
            }
        } catch (ClassCastException e) {
            // bugzilla 2154
            LogEvent.logError("BaseAction", "setFormAttributes()", e.toString());
            throw new ClassCastException("Error Casting form into DynaForm");
        }
    }

    protected String getActionName(ActionMapping mapping) {
        if (mapping.getPath().startsWith("/")) {
            return mapping.getPath().substring(1);
        } else {
            return mapping.getPath();
        }
    }

    protected ActionForward getForward(ActionForward forward, String id, String startingRecNo) {
        ActionRedirect redirect = new ActionRedirect(forward);

        if (id != null)
            redirect.addParameter(ID, id);
        if (startingRecNo != null)
            redirect.addParameter("startingRecNo", startingRecNo);
        return redirect;
    }

    protected ActionForward getForwardWithParameters(ActionForward forward, Map<String, String> params) {
        ActionRedirect redirect = new ActionRedirect(forward);

        for (String param : params.keySet()) {
            redirect.addParameter(param, params.get(param));
        }
        return redirect;
    }

    // added for bugzilla 1467
    protected ActionForward getForward(ActionForward forward, String id, String startingRecNo, String direction) {
        ActionRedirect redirect = new ActionRedirect(forward);
        // bugzilla 2154
        LogEvent.logInfo("BaseAction", "getForward()", "This is forward " + forward.getRedirect() + " " + forward.getPath());

        if (id != null)
            redirect.addParameter(ID, id);
        if (startingRecNo != null)
            redirect.addParameter("startingRecNo", startingRecNo);
        if (direction != null)
            redirect.addParameter("direction", direction);
        // bugzilla 2154
        LogEvent.logInfo("BaseAction", "getForward()", "This is redirect " + redirect.getPath());

        return redirect;
    }

    // N.B. Unless validating accession numbers is part of the concept of
    // actions this should be moved to
    // a utility
    protected ActionMessages validateAccessionNumber(HttpServletRequest request, ActionMessages errors, BaseActionForm dynaForm)
            throws Exception {

        String formName = dynaForm.getDynaClass().getName().toString();

        // accession number validation against database (reusing ajax
        // validation logic)
        AccessionNumberValidationProvider accessionNumberValidator = new AccessionNumberValidationProvider();

        // this was not validating before...
        String accessionNumber = "";
        String result = "";
        // if routing from another module accessionNumber is not a form variable
        // but a request parameter
        if (!StringUtil.isNullorNill((String) request.getParameter(ACCESSION_NUMBER))) {
            accessionNumber = (String) request.getParameter(ACCESSION_NUMBER);
        } else {
            accessionNumber = (String) dynaForm.get(ACCESSION_NUMBER);
        }
        result = accessionNumberValidator.validate(accessionNumber, formName);

        String messageKey = "sample.accessionNumber";
        if (result.equals(INVALID)) {
            ActionError error = new ActionError("errors.invalid", getMessageForKey(messageKey), null);
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        if (result.equals(INVALIDSTATUS)) {
            ActionError error = new ActionError("error.invalid.sample.status", getMessageForKey(messageKey), null);
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        return errors;
    }

    // N.B. Unless sorting tests is part of the concept of actions this should
    // be moved to a utility
    protected List sortTests(List analyses) {

        // find root level nodes and fill in children for each Test_TestAnalyte
        List rootLevelNodes = new ArrayList();
        for (int i = 0; i < analyses.size(); i++) {
            Analysis analysis = (Analysis) analyses.get(i);
            String analysisId = analysis.getId();

            List children = new ArrayList();
            for (int j = 0; j < analyses.size(); j++) {
                Analysis anal = (Analysis) analyses.get(j);
                if (anal.getParentAnalysis() != null && anal.getParentAnalysis().getId().equals(analysisId)) {
                    children.add(anal);
                }
            }
            analysis.setChildren(children);

            if (analysis.getParentAnalysis() == null) {
                rootLevelNodes.add(analysis);
            }
        }

        // sort rootLevelNodes
        Collections.sort(rootLevelNodes, BaseTestComparator.SORT_ORDER_COMPARATOR);

        analyses = new ArrayList();
        for (int i = 0; i < rootLevelNodes.size(); i++) {
            Analysis analysis = (Analysis) rootLevelNodes.get(i);
            analyses.add(analysis);
            recursiveSort(analysis, analyses);
        }

        return analyses;
    }

    private void recursiveSort(Analysis element, List analyses) {
        List<Analysis> children = element.getChildren();
        // sort children
        if (children != null && children.size() > 0) {
            Collections.sort(children, BaseTestComparator.SORT_ORDER_COMPARATOR);
        }
        for (Iterator<Analysis> it = children.iterator(); it.hasNext(); ) {
            Analysis childElement = it.next();
            analyses.add(childElement);
            recursiveSort(childElement, analyses);
        }
    }

    protected String getSysUserId(HttpServletRequest request) {
        UserSessionData usd = (UserSessionData) request.getSession().getAttribute(USER_SESSION_DATA);
        return String.valueOf(usd.getSystemUserId());
    }

    protected void setSuccessFlag(HttpServletRequest request, String forwardFlag) {
        request.setAttribute(FWD_SUCCESS, FWD_SUCCESS.equals(forwardFlag));
    }

    protected void setSuccessFlag(HttpServletRequest request) {
        request.setAttribute(FWD_SUCCESS, Boolean.TRUE);
    }

    protected void setDictionaryList(BaseActionForm dynaForm, String propertyName, String category, boolean sortById) throws LIMSRuntimeException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        List<IdValuePair> conditionList = new ArrayList<IdValuePair>();
        DictionaryDAO dictionaryDAO = new DictionaryDAOImpl();
        // The category is by local_abbrev
        List<Dictionary> conditionDictionaryList = dictionaryDAO.getDictionaryEntrysByCategory(category);

        Collections.sort(conditionDictionaryList, new Comparator<Dictionary>() {
            @Override
            public int compare(Dictionary o1, Dictionary o2) {
                return (int) (Long.parseLong(o1.getId()) - Long.parseLong(o2.getId()));
            }
        });

        for (Dictionary dictionary : conditionDictionaryList) {
            conditionList.add(new IdValuePair(dictionary.getId(), dictionary.getLocalizedName()));
        }

        PropertyUtils.setProperty(dynaForm, propertyName, conditionList);
    }

    public void setState(BaseAction other) {
        this.servlet = other.servlet;
    }
}

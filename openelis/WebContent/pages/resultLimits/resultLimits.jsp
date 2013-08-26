<%@ page language="java" contentType="text/html; charset=utf-8"
         import="java.util.Date,us.mn.state.health.lims.common.action.IActionConstants" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.apache.struts.util.MessageResources" %>
<%@ page import="us.mn.state.health.lims.common.util.resources.ResourceLocator" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<bean:define id="formName" value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>'/>

<%!
    String allowEdits = "true";
    String errorRequired = "";
    String test = "";
    String errorRangeInputForNumeric = "";
    String errorAgeInputType = "";
    String errorInvalidHighLowForAge = "";
    String errorInvalidHighLowForRange = "";
    String errorInvalidLowNormalValidRange = "";
    String errorInvalidHighNormalValidRange = "";
%>

<%

    if (request.getAttribute(IActionConstants.ALLOW_EDITS_KEY) != null) {
        allowEdits = (String) request.getAttribute(IActionConstants.ALLOW_EDITS_KEY);
    }

    Locale locale = (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY);
    MessageResources messageResources = ResourceLocator.getInstance().getMessageResources();
    test = messageResources.getMessage(locale, "resultlimits.test");
    errorRequired = messageResources.getMessage(locale, "errors.required", test);
    errorRangeInputForNumeric = messageResources.getMessage(locale, "resultLimits.invalid.input.number", "range");
    errorAgeInputType = messageResources.getMessage(locale, "resultLimits.invalid.input.number", "age");
    errorInvalidHighLowForAge = messageResources.getMessage(locale, "resultlimits.invalid.high-low-value", "age");
    errorInvalidHighLowForRange = messageResources.getMessage(locale, "resultlimits.invalid.high-low-value", "range");
    errorInvalidLowNormalValidRange = messageResources.getMessage(locale, "resultlimits.invalid.low-normal-valid-range");
    errorInvalidHighNormalValidRange = messageResources.getMessage(locale, "resultlimits.invalid.high-normal-valid-range");
%>

<script language="JavaScript1.2">

    var floatRegEx = new RegExp("^\\d{1,}\\.?\\d?$");
    var intRegEx = new RegExp("^\\d*$");
    var dayToYear = 0.00273790926;

    function validateForm(form) {
        if ($("testId").value.blank()) {
            alert('<%=errorRequired %>');
            return false;
        }
        var lowNormal = $("lowNormalId").value != "" ? parseFloat($("lowNormalId").value) : "";
        var highNormal = $("highNormalId").value != "" ? parseFloat($("highNormalId").value) : "";
        var lowValid =  $("lowValidId").value != "" ?  parseFloat($("lowValidId").value) : "";
        var highValid = $("highValidId").value != "" ? parseFloat($("highValidId").value) : "";
        var minDaysAge = $("minDayAge").value != "" ? parseFloat($("minDayAge").value) : "";
        var maxDaysAge = $("maxDayAge").value != "" ? parseFloat($("maxDayAge").value) : "";
        var minYearAge = $("minYearAge").value != "" ? parseFloat($("minYearAge").value) : "";
        var maxYearAge = $("maxYearAge").value != "" ? parseFloat($("maxYearAge").value) : "";
        var resultTypeName = $("resultTypeId").firstChild.text;

        function notNullAndNotANumber(field) {
            return field != "" && isNaN(field);
        }

        if (resultTypeName === "Numeric") {
            if (notNullAndNotANumber(lowNormal) || notNullAndNotANumber(highNormal) ||
                    notNullAndNotANumber(lowValid) || notNullAndNotANumber(highValid)) {
                alert('<%=errorRangeInputForNumeric %>');
                return false;
            }


            if (lowNormal > highNormal || lowValid > highValid){
                alert('<%=errorInvalidHighLowForRange %>');
                return false;
            }

            if(lowValid != '' && (lowNormal < lowValid)){
                alert('<%=errorInvalidLowNormalValidRange %>');
                return false;
            }

            if(highValid != '' && (highNormal > highValid)){
                alert('<%=errorInvalidHighNormalValidRange %>');
                return false;
            }
        }

        if (notNullAndNotANumber(minDaysAge) || notNullAndNotANumber(maxDaysAge) ||
                notNullAndNotANumber(minYearAge) || notNullAndNotANumber(maxYearAge)){
            alert('<%=errorAgeInputType %>');
            return false;
        }

        if (minDaysAge != "0" && maxDaysAge != "" && ( minDaysAge > maxDaysAge) || minYearAge > maxYearAge) {
            alert('<%=errorInvalidHighLowForAge %>');
            return false;
        }

        return true;
    }

    function /*void*/ updateYear(dayElement, yearId) {
        dayElement.value = Math.floor(dayElement.value);
        var value = dayElement.value;
        $(yearId).value = intRegEx.test(value) ? Math.round(10000 * value * dayToYear) / 10000 : 0;
    }

    function /*void*/ updateDay(yearElement, dayId) {
        var value = yearElement.value;
        $(dayId).value = floatRegEx(value) ? Math.floor(value * 365.0 + (value / 4.0)) : 0;
    }

</script>

<table>
    <tr>
        <td width="10%">&nbsp;</td>
        <td class="label" width="10%">
            <bean:message key="resultlimits.test"/>
            :
            <span class="requiredlabel">*</span>
        </td>
        <td width="15%">
            <html:select name="<%=formName%>" property="limit.testId" styleId="testId">
                <app:optionsCollection name="<%=formName%>" property="tests" label="description" value="id"
                                       allowEdits="true"/>

            </html:select>
        </td>
        <td class="label" width="10%">
            <bean:message key="resultlimits.resulttype"/>
            :
            <span class="requiredlabel">*</span>
        </td>
        <td width="15%">
            <html:select name="<%=formName%>" property="limit.resultTypeId" styleId="resultTypeId">
                <app:optionsCollection name="<%=formName%>" property="resultTypes" label="description" value="id"
                                       allowEdits="true" showDefault="false"/>
            </html:select>
        </td>
        <td width="50%">&nbsp;</td>
    </tr>
    <tr>
        <td></td>
        <td class="label">
            <bean:message key="resultlimits.gender"/>
            :
        </td>
        <td>
            <html:select name="<%=formName%>" property="limit.gender">
                <app:optionsCollection name="<%=formName%>" property="genders" label="description" value="genderType"
                                       allowEdits="true"/>
            </html:select>
        </td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>
            <bean:message key="resultLimits.gender.instrutions"/>
        </td>
    </tr>
    <tr>
        <td><bean:message key="resultlimits.age"/></td>
        <td class="label">
            <bean:message key="resultlimits.min"/>
            :
        </td>
        <td>
            <html:text name="<%=formName%>"
                       property="limit.minDayAgeDisplay"
                       size="4"
                       styleId="minDayAge"
                       onchange="updateYear( this, 'minYearAge');"/>
            &nbsp;/&nbsp;
            <html:text name="<%=formName%>"
                       property="limit.minAgeDisplay"
                       size="10"
                       styleId="minYearAge"
                       onchange="updateDay(this, 'minDayAge');"/>
        </td>
        <td class="label">
            <bean:message key="resultlimits.max"/>
            :
        </td>
        <td>
            <html:text name="<%=formName%>"
                       property="limit.maxDayAgeDisplay"
                       size="4"
                       styleId="maxDayAge"
                       onchange="updateYear( this, 'maxYearAge');"/>
            &nbsp;/&nbsp;
            <html:text name="<%=formName%>"
                       property="limit.maxAgeDisplay"
                       size="10"
                       styleId="maxYearAge"
                       onchange="updateDay(this, 'maxDayAge');"/>
        </td>
        <td>
            <bean:message key="resultLimits.age.instrutions"/>
        </td>

    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td><bean:message key="resultlimits.normal"/></td>
        <td class="label">
            <bean:message key="resultlimits.low"/>
            :
        </td>
        <td>
            <html:text name="<%=formName%>" property="limit.lowNormalDisplay" size="10" styleId="lowNormalId"/>
        </td>
        <td class="label">
            <bean:message key="resultlimits.high"/>
            :
        </td>
        <td>
            <html:text name="<%=formName%>" property="limit.highNormalDisplay" size="10" styleId="highNormalId"/>
        </td>
        <td>
            <bean:message key="resultLimits.normal.instrutions"/>
        </td>

    </tr>
    <tr>
        <td><bean:message key="resultlimits.valid"/></td>
        <td class="label">
            <bean:message key="resultlimits.low"/>
            :
        </td>
        <td>
            <html:text name="<%=formName%>" property="limit.lowValidDisplay" size="10" styleId="lowValidId"/>
        </td>
        <td class="label">
            <bean:message key="resultlimits.high"/>
            :
        </td>
        <td>
            <html:text name="<%=formName%>" property="limit.highValidDisplay" size="10" styleId="highValidId"/>
        </td>
        <td>
            <bean:message key="resultLimits.valid.instrutions"/>
        </td>

    </tr>
    <tr>
        <td>
            &nbsp;
        </td>
    </tr>
</table>



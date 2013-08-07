<%@page import="us.mn.state.health.lims.common.formfields.FormFields.Field"%>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="us.mn.state.health.lims.common.action.IActionConstants,
			     us.mn.state.health.lims.common.formfields.FormFields,
			     us.mn.state.health.lims.common.util.StringUtil" %>



<%@ taglib uri="/tags/struts-bean"		prefix="bean" %>
<%@ taglib uri="/tags/struts-html"		prefix="html" %>
<%@ taglib uri="/tags/struts-logic"		prefix="logic" %>
<%@ taglib uri="/tags/labdev-view"		prefix="app" %>
<%@ taglib uri="/tags/struts-tiles"     prefix="tiles" %>
<%@ taglib uri="/tags/sourceforge-ajax" prefix="ajax"%>

<bean:define id="formName"		value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>' />


<%!
    boolean useSTNumber = true;
    boolean useMothersName = true;
    boolean patientRequired = false;
%>

<%
    String path = request.getContextPath();
    useSTNumber =  FormFields.getInstance().useField(FormFields.Field.StNumber);
    useMothersName = FormFields.getInstance().useField(FormFields.Field.MothersName);
    patientRequired = FormFields.getInstance().useField(FormFields.Field.PatientRequired);
%>

<html:hidden name="<%=formName%>" property="patientPK" styleId="patientPK"/>



<div id="patientDisplay"  style="display:none;" >
    <tiles:insert attribute="patientInfo" />
    <tiles:insert attribute="patientClinicalInfo" />
</div><!-- 
<table style="width:100%" style="display:none;">
    <tr>
        <td width="10%" align="left">
            <html:button styleId="patientDisplayButton" property="showPatient" onclick="showHideSection(this, 'patientDisplay');" value="+">+</html:button>
            <bean:message key="sample.entry.patient" />:
            <% if ( patientRequired ) { %><span class="requiredlabel">*</span><% } %>
        </td>
        <td width="20%" id="firstName"><b>&nbsp;</b></td>
        <td width="10%">
            <% if(useMothersName){ %><bean:message key="patient.mother.name"/>:<% } %>
        </td>
        <td width="20%" id="mother"><b>&nbsp;</b></td>
        <td width="10%">
            <% if( useSTNumber){ %><bean:message key="patient.ST.number"/>:<% } %>
        </td>
        <td width="15%" id="st"><b>&nbsp;</b></td>
        <td width="5%">&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td id="lastName"><b>&nbsp;</b></td>
        <td>
            <bean:message key="patient.birthDate"/>:
        </td>
        <td id="dob"><b>&nbsp;</b></td>
        <td class="nationalID">
            <%=StringUtil.getContextualMessageForKey("patient.NationalID") %>:
        </td>
        <td class="nationalID" id="national"><b>&nbsp;</b></td>
        <td>
            <bean:message key="patient.gender"/>:
        </td>
        <td id="gender"><b>&nbsp;</b></td>
    </tr>
</table> -->
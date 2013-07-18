<%@ page language="java" contentType="text/html; charset=utf-8" import="us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<%!
    String currentAction = "list";
%>
<%
    currentAction = "default";
    if (request.getAttribute("currentAction") != null) {
        currentAction = (String)request.getAttribute("currentAction");
    }
//System.out.println("menuDef " + menuDef);
%>

<bean:define id="currentAction" value="<%=currentAction%>" />
<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />

<logic:equal name="currentAction" value="list">

    <div>
        <html:link action="/AddHealthCenter">
            <button><bean:message key="healthcenter.add.link"/></button>
        </html:link>
    </div>
    <table width="100%" border="2">
        <tr>
            <th><bean:message key="healthcenter.add.name" /></th>
            <th><bean:message key="healthcenter.add.description" /></th>
            <th><bean:message key="healthcenter.add.active" /></th>
        </tr>

        <logic:iterate id="healthCenter" name="<%=formName%>" property="healthCenters" type="us.mn.state.health.lims.healthcenter.valueholder.HealthCenter">
            <tr>
                <td>
                    <html:link action="/EditHealthCenter" paramId="name" paramName="healthCenter" paramProperty="name">
                        <bean:write name="healthCenter" property="name" />
                    </html:link>

                </td>
                <td><bean:write name="healthCenter" property="description" /></td>
                <td><bean:write name="healthCenter" property="active" /></td>
            </tr>
        </logic:iterate>
    </table>
</logic:equal>

<logic:equal name="currentAction" value="addNew">
    <html:form action="/AddHealthCenter">
        <table>
            <tr>
                <td><bean:message key="healthcenter.add.name" /></td>
                <td><html:text name="<%=formName%>" property="name" size="30"/></td>
            </tr>
            <tr>
                <td><bean:message key="healthcenter.add.description" /></td>
                <td><html:text name="<%=formName%>" property="description" size="30"/></td>
            </tr>
        </table>
        <html:submit>Submit</html:submit>
    </html:form>
</logic:equal>

<logic:equal name="currentAction" value="edit">
    <html:form action="/EditHealthCenter">
        <table>
            <tr>
                <td><bean:message key="healthcenter.add.name" /></td>
                <td><html:text name="<%=formName%>" property="name" size="30"/></td>
            </tr>
            <tr>
                <td><bean:message key="healthcenter.add.description" /></td>
                <td><html:text name="<%=formName%>" property="description" size="30"/></td>
            </tr>
            <tr>
                <td><bean:message key="healthcenter.add.active" /></td>
                <td><html:checkbox name="<%=formName%>" property="active" value="true" /></td>
            </tr>
        </table>
        <html:hidden property="active" value="false"/> <!-- To submit checkbox value when unchecked -->
        <html:submit>Submit</html:submit>
    </html:form>
</logic:equal>


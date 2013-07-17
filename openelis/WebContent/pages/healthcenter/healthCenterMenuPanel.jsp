<%@ page language="java" contentType="text/html; charset=utf-8" import="us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />


<table width="100%" border="2">
    <tr>
        <th>Name</th>
        <th>Description</th>
        <th>IsActive</th>
    </tr>

    <logic:iterate id="healthCenter" name="<%=formName%>" property="healthCenters" type="us.mn.state.health.lims.healthcenter.valueholder.HealthCenter">
        <tr>
            <td>
                <bean:write name="healthCenter" property="name" />
            </td>
            <td>
                <bean:write name="healthCenter" property="description" />
            </td>
            <td>
                <bean:write name="healthCenter" property="active" />
            </td>
        </tr>
    </logic:iterate>
</table>

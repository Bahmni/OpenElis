<%@ page language="java" contentType="text/html; charset=utf-8" import="us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<%!
    String currentAction = "list";
    int i;
%>
<%
    currentAction = "default";
    if (request.getAttribute("currentAction") != null) {
        currentAction = (String)request.getAttribute("currentAction");
    }
//System.out.println("menuDef " + menuDef);
    i = 0;
%>

<script type="text/javascript" >
    function toUppercase(){
        var x=document.getElementById("healthCenterName");
        x.value=x.value.toUpperCase();
    }

    function toEditHealthCenter(){
        var checked = jQuery("input:checked")[0];
        var url = "<%= request.getContextPath()%>" + "/EditHealthCenter.do?name=" +checked.value;
        window.location = url;
    }

    function deactivateHealthCenters(){
        document.getElementById("HealthCenterDeactivateForm").submit();
    }


</script>





<bean:define id="currentAction" value="<%=currentAction%>" />
<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />

<logic:equal name="currentAction" value="list">
    <html:form styleId="HealthCenterDeactivateForm" action="HealthCenterDeactivate">
        <table>
            <tr>
                <td>
                    <html:link action="/AddHealthCenter">
                        <button type="button"><bean:message key="healthcenter.add.link"/></button>
                    </html:link>
                </td>
                <td>
                    <button type="button" onclick="toEditHealthCenter()" ><bean:message key="healthcenter.edit.link"/></button>
                </td>
                <td>
                    <button type="button" onclick="deactivateHealthCenters()" ><bean:message key="healthcenter.deactivate.link"/></button>
                </td>

            </tr>
        </table>


    <table width="100%" border="2">
        <tr>
            <th>Select</th>
            <th><bean:message key="healthcenter.add.name" /></th>
            <th><bean:message key="healthcenter.add.description" /></th>
            <th><bean:message key="healthcenter.add.active" /></th>
        </tr>

        <logic:iterate id="healthCenter" name="<%=formName%>" property="healthCenters" type="us.mn.state.health.lims.healthcenter.valueholder.HealthCenter">
            <tr>
                <td>
               <%-- <html:checkbox name="<%=formName%>" property="healthCenterSelect" indexed="true" value="<%= healthCenter.getName() %>" />--%>
                   <input id="hcname" type="checkbox" name="hcname" value="<%= healthCenter.getName() %>"></input>
                </td>
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

    </html:form>
</logic:equal>

<logic:equal name="currentAction" value="addNew">
    <html:form styleId="addHealthCenterForm" action="/AddHealthCenter">
        <table>
            <tr>
                <td><bean:message key="healthcenter.add.name" /></td>
                <td><html:text name="<%=formName%>" property="name" size="30" styleId="healthCenterName" onchange="toUppercase()" value="" /></td>
            </tr>
            <tr>
                <td><bean:message key="healthcenter.add.description" /></td>
                <td><html:text name="<%=formName%>" property="description" size="30" value=""/></td>
            </tr>
        </table>
        <html:submit><bean:message key="healthcenter.save" /></html:submit>
        <a href="<%= request.getContextPath()%>/HealthCenterMenu.do"><button type="button">exit</button></a>
    </html:form>
</logic:equal>

<logic:equal name="currentAction" value="edit">
    <html:form action="/EditHealthCenter">
        <table>
            <tr>
                <td><bean:message key="healthcenter.add.name" /></td>
                <td><html:text name="<%=formName%>" property="name" size="30" styleId="healthCenterName" onchange="toUppercase()" readonly="true"/></td>
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
        <html:submit><bean:message key="healthcenter.save" /></html:submit>
        <a href="<%= request.getContextPath()%>/HealthCenterMenu.do"><button type="button">exit</button></a>
    </html:form>
</logic:equal>


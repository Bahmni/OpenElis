<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date,
	java.util.Hashtable,
	us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />


<table width="100%" border=2">
	<tr>
	   <th>
	     <bean:message key="label.form.select"/>
	   </th>
	   
	   <th> 
	   	  <bean:message key="typeofteststatus.name"/>
	   </th>
	   <th>
	   	  <bean:message key="typeofteststatus.description"/>
	   </th>
	   <th>
	   	  <bean:message key="typeofteststatus.statusType"/>
	   </th>
	   <th>
	   	  <bean:message key="typeofteststatus.isActive"/>
	   </th>
	   <th>
	   	  <bean:message key="typeofteststatus.isResultRequired"/>
	   </th>
	   <th>
	   	  <bean:message key="typeofteststatus.isApprovalRequired"/>
	   </th>
  
	</tr>
	<logic:iterate id="tors" indexId="ctr" name="<%=formName%>" property="menuList" type="us.mn.state.health.lims.typeofteststatus.valueholder.TypeOfTestStatus">
	<bean:define id="torsID" name="tors" property="id"/>
	  <tr>
	   <td class="textcontent">
	      <html:multibox name="<%=formName%>" property="selectedIDs">
	         <bean:write name="torsID" />
	      </html:multibox>
     
   	   </td>
   	    
	   <td class="textcontent" style="max-width: 100%;word-break: break-word;">
	   	  <bean:write name="tors" property="statusName"/>
	   </td>
	   <td class="textcontent" style="max-width: 100%;word-break: break-word;">
	   	  <bean:write name="tors" property="description"/>
	   </td>
	   <td class="textcontent">
	   	  <bean:write name="tors" property="statusType"/>
	   </td>
	    <td class="textcontent">
	   	  <bean:write name="tors" property="isActive"/>
	   </td>
	    <td class="textcontent">
	   	  <bean:write name="tors" property="isResultRequired"/>
	   </td>
	    <td class="textcontent">
	   	  <bean:write name="tors" property="isApprovalRequired"/>
	   </td>
       </tr>
	</logic:iterate>
</table>

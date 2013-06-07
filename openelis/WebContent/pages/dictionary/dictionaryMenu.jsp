<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date,
	java.util.Hashtable,
	us.mn.state.health.lims.dictionary.valueholder.Dictionary,
	us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />
<%--bugzilla 2061-2063--%>

<table width="100%" border=2">
	<tr>
	   <th>
	     <bean:message key="label.form.select"/>
	   </th>
	   <%--remove the following 09/12/2006 bugzilla 1399--%>
	   <th>
	      <bean:message key="dictionary.id"/>
	   </th>
	   <th>
	   	  <bean:message key="dictionary.dictionarycategory"/>
	   </th>
	   <%-- added dict entry as we are sorting on this column--%>
	   <th>
	      <bean:message key="dictionary.dictEntry"/>
	   </th>
       <%--bugzilla 1847--%>
	   <th>
	      <bean:message key="dictionary.localAbbreviation"/>
	   </th>
	   <th>
	      <bean:message key="dictionary.isActive"/>
	   </th>
   
	</tr>
	<logic:iterate id="dict" indexId="ctr" name="<%=formName%>" property="menuList" type="us.mn.state.health.lims.dictionary.valueholder.Dictionary">
	<bean:define id="dictID" name="dict" property="id"/>
	  <tr>	
	   <td class="textcontent">
	      <html:multibox name="<%=formName%>" property="selectedIDs">
	         <bean:write name="dictID" />
	      </html:multibox>
     
   	   </td>
   	   <%--remove the following 09/12/2006 bugzilla 1399--%>
	 
	   <td class="textcontent">
	      <bean:write name="dict" property="id"/>
	   </td>
	 
	   <td class="textcontent">
	        <bean:write name="dict" property="dictionaryCategory.categoryName"/>
	      &nbsp;
       </td>
	   <%-- added dict entry as we are sorting on this column--%>
	   <td class="textcontent">
          <%--bugzilla 1405 increase width--%>
          <%--bugzilla 2368 drop length contraint, allow automatic wrapping --%>
   	      <app:write name="dict" property="dictEntry" />
	      &nbsp;
       </td>
	   <td class="textcontent">
   	      <app:write name="dict" property="localAbbreviation" maxLength="10" />
	      &nbsp;
       </td>
	   <td class="textcontent">
	   	  <bean:write name="dict" property="isActive"/>
	   </td>
     </tr>
	</logic:iterate>
</table>

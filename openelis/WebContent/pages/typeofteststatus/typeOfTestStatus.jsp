<%@ page language="java"
		 contentType="text/html; charset=utf-8"
		 import="java.util.Date,
	us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />

<%!
	String allowEdits = "true";
%>

<%
	if (request.getAttribute(IActionConstants.ALLOW_EDITS_KEY) != null) {
		allowEdits = (String)request.getAttribute(IActionConstants.ALLOW_EDITS_KEY);
	}

%>

<script language="JavaScript1.2">
	function validateForm(form) {
		return validateTypeOfTestStatusForm(form);
	}
</script>

<table>

	<tr>
		<td class="label">
			<bean:message key="typeofteststatus.id"/>:
		</td>
		<td>
			<app:text name="<%=formName%>" property="id" allowEdits="false"/>
		</td>
	</tr>

	<tr>
		<td class="label">
			<bean:message key="typeofteststatus.statusType"/>:<span class="requiredlabel">*</span>
		</td>
		<td>
			<html:select name="<%=formName%>" property="statusType" style="width: 150px;">
				<logic:iterate name="allowedStatusTypes" id="allowedStatusType" type="java.lang.String">
					<html:option value='<%=allowedStatusType%>'>
						<bean:write name="allowedStatusType"/>
					</html:option>
				</logic:iterate>


				</select>

			</html:select>
		</td>
	</tr>

	<tr>
		<td class="label">
			<bean:message key="typeofteststatus.name"/>:<span class="requiredlabel">*</span>
		</td>
		<td>
			<html:text name="<%=formName%>" property="statusName"/>
		</td>
	</tr>

	<tr>
		<td class="label">
			<bean:message key="typeofteststatus.description"/>:
		</td>
		<td>
			<html:text name="<%=formName%>" property="description"/>
		</td>
	</tr>

	<tr>
		<td class="label">
			<bean:message key="typeofteststatus.isActive"/>:<span class="requiredlabel">*</span>
		</td>
		<td>
			<html:radio styleId="isActiveTrue" name='<%= formName %>'
						property="isActive"
						value="Y" ><bean:message key="label.button.yes"/></html:radio>
			<html:radio styleId="isActiveFalse" name='<%= formName %>'
						property="isActive"
						value="N" ><bean:message key="label.button.no"/></html:radio>
		</td>
	</tr>

	<tr>
		<td class="label">
			<bean:message key="typeofteststatus.isResultRequired"/>:<span class="requiredlabel">*</span>
		</td>
		<td>
			<html:radio name='<%= formName %>'
						property="isResultRequired"
						value="Y" ><bean:message key="label.button.yes"/></html:radio>
			<html:radio name='<%= formName %>'
						property="isResultRequired"
						value="N" ><bean:message key="label.button.no"/></html:radio>
		</td>
	</tr>

	<tr>
		<td class="label">
			<bean:message key="typeofteststatus.isApprovalRequired"/>:<span class="requiredlabel">*</span>
		</td>
		<td>
			<html:radio name='<%= formName %>'
						property="isApprovalRequired"
						value="Y" ><bean:message key="label.button.yes"/></html:radio>
			<html:radio name='<%= formName %>'
						property="isApprovalRequired"
						value="N" ><bean:message key="label.button.no"/></html:radio>
		</td>
	</tr>

</table>

<html:javascript formName="typeOfTestStatusForm"/>

<%@ page language="java" contentType="text/html; charset=utf-8"
         import="us.mn.state.health.lims.common.action.IActionConstants,
			us.mn.state.health.lims.common.util.SystemConfiguration,
			us.mn.state.health.lims.common.util.ConfigurationProperties,
			us.mn.state.health.lims.common.util.ConfigurationProperties.Property,
	        us.mn.state.health.lims.common.formfields.FormFields,
	        us.mn.state.health.lims.common.util.StringUtil,
            us.mn.state.health.lims.common.util.Versioning,
	        us.mn.state.health.lims.sample.bean.SampleEditItem,
	        us.mn.state.health.lims.sample.util.AccessionNumberUtil,
	        us.mn.state.health.lims.common.util.IdValuePair" %>

<%@ taglib uri="/tags/struts-bean"		prefix="bean" %>
<%@ taglib uri="/tags/struts-html"		prefix="html" %>
<%@ taglib uri="/tags/struts-logic"		prefix="logic" %>
<%@ taglib uri="/tags/labdev-view"		prefix="app" %>
<%@ taglib uri="/tags/struts-tiles"     prefix="tiles" %>
<%@ taglib uri="/tags/sourceforge-ajax" prefix="ajax"%>
<%@ taglib uri="/tags/globalOpenELIS" 	prefix="global"%>

<bean:define id="formName"		value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>' />
<bean:define id="idSeparator"	value='<%=SystemConfiguration.getInstance().getDefaultIdSeparator()%>' />
<bean:define id="accessionFormat" value='<%=ConfigurationProperties.getInstance().getPropertyValue(Property.AccessionFormat)%>' />
<bean:define id="genericDomain" value='' />
<bean:define id="accessionNumber " name="<%=formName %>" property="accessionNumber"/>
<bean:define id="newAccessionNumber " name="<%=formName %>" property="newAccessionNumber"/>

<%!
	String basePath = "";
	int editableAccession = 0;
	int nonEditableAccession = 0;
	int maxAccessionLength = 0;
	boolean trackPayments = false;
	boolean allowEditOrRemoveTests = FormFields.getInstance().useField(FormFields.Field.AllowEditOrRemoveTests);
%>
<%
	String path = request.getContextPath();
	basePath = request.getScheme() + "://" + request.getServerName() + ":"	+ request.getServerPort() + path + "/";
	editableAccession = AccessionNumberUtil.getChangeableLength();
	nonEditableAccession = AccessionNumberUtil.getInvarientLength();
	maxAccessionLength = editableAccession + nonEditableAccession;
	trackPayments = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.trackPatientPayment, "true");
%>

<script type="text/javascript" src="<%=basePath%>scripts/utilities.js?ver=<%= Versioning.getBuildNumber() %>" ></script>
<script type="text/javascript" src="<%=basePath%>scripts/ajaxCalls.js?ver=<%= Versioning.getBuildNumber() %>" ></script>

<script type="text/javascript" >

var checkedCount = 0;
var paymentChanged = false;
var currentSampleType;
var sampleIdStart = 0;

$jq(function() {
   	var maxAccessionNumber = $("maxAccessionNumber").value;
	var lastDash = maxAccessionNumber.lastIndexOf('-');
   	sampleIdStart = maxAccessionNumber.substring(lastDash + 1);
});

function  /*void*/ setMyCancelAction(form, action, validate, parameters)
{
	//first turn off any further validation
	setAction(window.document.forms[0], 'Cancel', 'no', '');
}

function setPaymentChanged(){

	paymentChanged = true;
	
	setSaveButton();
}

function /*void*/ addRemoveRequest( checkbox ){
	checkedCount = Math.max(checkedCount + (checkbox.checked ? 1 : -1), 0 );

	if( typeof(showSuccessMessage) != 'undefinded' ){
		showSuccessMessage(false); //refers to last save
	}

	setSaveButton();

}

function setSaveButton(){
	var newAccession = $("newAccessionNumber").value;
	var accessionChanged = newAccession.length > 1 && newAccession != "<%=accessionNumber%>"; 

    $("saveButtonId").disabled = errorsOnForm() || (checkedCount == 0  && !accessionChanged && !paymentChanged && !samplesHaveBeenAdded() );
}

// Adds warning when leaving page if tests are checked
function formWarning(){
	var newAccession = $("newAccessionNumber").value;
	var accessionChanged = newAccession.length > 1 && newAccession != "<%=accessionNumber%>"; 

  	if ( checkedCount > 0 || accessionChanged || paymentChanged || samplesHaveBeenAdded()) {
    	return "<bean:message key="banner.menu.dataLossWarning"/>";
	}
}
window.onbeforeunload = formWarning;

function /*void*/ savePage(){
    jQuery("#saveButtonId").attr("disabled", "disabled");
	if( samplesHaveBeenAdded() && !allSamplesHaveTests()){
		alert('<%= StringUtil.getMessageForKey("warning.sample.missing.test")%>');
		return;
	}
	window.onbeforeunload = null; // Added to flag that formWarning alert isn't needed.
	loadSamples();
	
	var form = document.forms[0];
	form.action = "SampleEditUpdate.do";
	form.submit();
}

function checkAccessionNumber(changeElement){
	var accessionNumber;
	clearFieldErrorDisplay( changeElement );

	$("newAccessionNumber").value = "";
	
	if( changeElement.value.length == 0){
		updateSampleItemNumbers( "<%=accessionNumber%>" );
		setSaveButton();
		return;
	}
	
	if( changeElement.value.length != <%= editableAccession%>){
		setFieldErrorDisplay( changeElement );
		setSaveButton();
		alert("<%=StringUtil.getMessageForKey("sample.entry.invalid.accession.number.length")%>");
		return;
	}
	
	accessionNumber = "<%=((String)accessionNumber).substring(0, nonEditableAccession)%>" + changeElement.value;
	
	if( accessionNumber == "<%=accessionNumber%>"){
		updateSampleItemNumbers( accessionNumber );
		setSaveButton();
		return;
	}
	
	validateAccessionNumberOnServer(true, changeElement.id, accessionNumber, processAccessionSuccess, processAccessionFailure);
}

function processAccessionSuccess(xhr)
{

	//alert( xhr.responseText );
	var accessionNumberUpdate;
	var formField = xhr.responseXML.getElementsByTagName("formfield").item(0).firstChild.nodeValue;
	var message = xhr.responseXML.getElementsByTagName("message").item(0).firstChild.nodeValue;

	if (message == "SAMPLE_FOUND"){
		//$(formField).style.borderColor = "red";
		setFieldErrorDisplay( $(formField) );
		setSaveButton();
		alert('<%=StringUtil.getMessageForKey("errors.may_not_reuse_accession_number")%>');
		return;
	}
	
	if( message == "SAMPLE_NOT_FOUND"){
		accessionNumberUpdate = "<%=((String)accessionNumber).substring(0, nonEditableAccession)%>" + $(formField).value;
		updateSampleItemNumbers( accessionNumberUpdate );
		$("newAccessionNumber").value = accessionNumberUpdate;
		setSaveButton();
		return;
	}
	
	setFieldErrorDisplay( $(formField) );
	setSaveButton();
	alert(message);
}

function updateSampleItemNumbers(newAccessionNumber){
		var i, itemNumbers, currentValue, lastDash = 0;
		itemNumbers = $$('span.itemNumber');
		
		for( i = 0; i < itemNumbers.length; i++){
			if(itemNumbers[i].firstChild != undefined){
				currentValue = itemNumbers[i].firstChild.nodeValue;
				lastDash = currentValue.lastIndexOf('-');
				itemNumbers[i].firstChild.nodeValue = newAccessionNumber + currentValue.substring(lastDash);
			}
		}
}

function processAccessionFailure(xhr)
{
	//unhandled error: someday we should be nicer to the user
}


</script>


<logic:equal name="<%=formName%>" property="noSampleFound" value="false">
<br/>
<DIV  id="patientInfo" class='textcontent'>
<bean:message key="sample.entry.patient"/>:&nbsp;
<bean:write name="<%=formName%>" property="firstName"/>&nbsp;
<bean:write name="<%=formName%>" property="lastName"/>&nbsp;
<bean:write name="<%=formName%>" property="dob"/>&nbsp;
<bean:write name="<%=formName%>" property="gender"/>&nbsp;
<bean:write name="<%=formName%>" property="nationalId"/>
</DIV>
<br/>
<html:hidden name="<%=formName%>" property="accessionNumber"/>
<html:hidden name="<%=formName%>" property="newAccessionNumber" styleId="newAccessionNumber"/>
<html:hidden name="<%=formName%>" property="isEditable"/>
<html:hidden name="<%=formName%>" property="maxAccessionNumber" styleId="maxAccessionNumber"/>
<bean:define id="paymentSelection" name="<%=formName %>"  property="paymentOptionSelection"/>

<% if( trackPayments){ %>
	<h2><%=StringUtil.getContextualMessageForKey("sample.edit.patientPayment") %></h2>  
	<bean:message key="sample.entry.patientPayment"/>: 
		<html:select name="<%=formName %>" 
		             property="paymentOptionSelection" 
		             onchange="setPaymentChanged();"
		             >
					<option value='' ></option>
		<logic:iterate id="optionValue" name='<%=formName%>' property="paymentOptions" type="IdValuePair" >
						<% if( optionValue.getId().equals(paymentSelection) ){ %>
							   	<option value='<%=optionValue.getId()%>' selected="selected" >	
						<% }else {%>
								<option value='<%=optionValue.getId()%>' >	
						<% } %>
						<bean:write name="optionValue" property="value"/>
					</option>
		</logic:iterate>
		</html:select>
<% } %>
<logic:equal name='<%=formName%>' property="isEditable" value="true" >
	<h2><%=StringUtil.getContextualMessageForKey("sample.edit.accessionNumber") %></h2>  
	<div id="accessionEditDiv" class="TableMatch">
		<b><%=StringUtil.getContextualMessageForKey("sample.edit.change.from") %>:</b> <bean:write name="<%=formName%>" property="accessionNumber"/>  
		<b><%=StringUtil.getContextualMessageForKey("sample.edit.change.to") %>:</b> <%= ((String)accessionNumber).substring(0, nonEditableAccession) %>
		<input type="text"
		       value='<%= ((String)newAccessionNumber).length() == maxAccessionLength ? ((String)newAccessionNumber).substring(nonEditableAccession, maxAccessionLength) : "" %>'
		       maxlength="<%= editableAccession%>"
		       onchange="checkAccessionNumber(this);"
		       id="accessionEdit">
		       
	<br/><br/>
	</div>
</logic:equal>

<logic:equal name='<%=formName%>' property="isEditable" value="true" >
	<h2><%=StringUtil.getContextualMessageForKey("sample.edit.tests") %></h2>
</logic:equal>
<table width="100%" class="existingTests">
<caption><bean:message key="sample.edit.existing.tests"/></caption>
<tr>
<th><%= StringUtil.getContextualMessageForKey("quick.entry.accession.number") %></th>
<th><bean:message key="sample.entry.sample.type"/></th>
<logic:equal name='<%=formName%>' property="isEditable" value="true" >
	<th ><bean:message key="sample.edit.remove.sample" /></th>
</logic:equal>
<th><bean:message key="test.testName"/></th>
<% if(allowEditOrRemoveTests){ %>
<logic:equal name='<%=formName%>' property="isEditable" value="true" >
	<th ><bean:message key="sample.edit.remove.tests" /></th>
</logic:equal>
<% } %>
<logic:equal name='<%=formName%>' property="isEditable" value="false" >
	<th><bean:message key="analysis.status" /></th>
</logic:equal>

</tr>
	<logic:iterate id="existingTests" name="<%=formName%>"  property="existingTests" indexId="index" type="SampleEditItem">
	<tr>
		<td>
			<html:hidden name="existingTests" property="analysisId" indexed="true"/>
			<span class="itemNumber" ><bean:write name="existingTests" property="accessionNumber"/></span>
		</td>
		<td>
            <% if(existingTests.shouldDisplaySampleTypeInformation()){ %>
			    <bean:write name="existingTests" property="sampleType"/>
            <% } %>
		</td>
		<logic:equal name='<%=formName%>' property="isEditable" value="true" >
		    <td>
				<% if( existingTests.getAccessionNumber() != null ){
				         if( existingTests.isCanRemoveSample()){ %>
				<html:checkbox name='existingTests' property='removeSample' indexed='true' onchange="addRemoveRequest(this);" />
				<% }else{ %>
				<html:checkbox name='existingTests' property='removeSample' indexed='true' disabled="true" />
				<% }} %>
			</td>
		</logic:equal>
		<td>
            <% if( existingTests.isPanel()){%>
                <strong><bean:write name="existingTests" property="panelName"/></strong>
                    <%for (SampleEditItem panelItem : existingTests.getPanelTests()) { %>
                        <br> &nbsp;
                        <%=panelItem.getTestName()%>
                    <%}%>
            <% }else{ %>
                <bean:write name="existingTests" property="testName"/>
            <%}%>
		</td>
        <% if(allowEditOrRemoveTests){ %>
		<logic:equal name='<%=formName%>' property="isEditable" value="true" >
			<td>
				<% if( existingTests.isCanCancel()){ %>
				<html:checkbox name='existingTests' property='canceled' indexed='true' onchange="addRemoveRequest(this);" />
				<% }else{ %>
				<html:checkbox name='existingTests' property='canceled' indexed='true' disabled="true" />
				<% } %>
			</td>
		</logic:equal>
        <% } %>
		<logic:equal name='<%=formName%>' property="isEditable" value="false" >
			<td>
				<bean:write name='existingTests' property="status" />
			</td>
		</logic:equal>
	</tr>
	</logic:iterate>
</table>
<br/>
<logic:equal name='<%=formName%>' property="isEditable" value="true" >
<% if(allowEditOrRemoveTests){ %>
<table id="availableTestTable" width="100%">
<caption><bean:message key="sample.edit.available.tests"/></caption>
<tr>
<th><%= StringUtil.getContextualMessageForKey("quick.entry.accession.number") %></th>
<th><bean:message key="sample.entry.sample.type"/></th>
<th><bean:message key="sample.entry.assignTests"/></th>
<th><bean:message key="test.testName"/></th>
</tr>
<logic:iterate id="possibleTests" name="<%=formName%>"  property="possibleTests" indexId="index" type="SampleEditItem">
<tr>
    <td>
        <html:hidden name="possibleTests" property="testId" indexed="true"/>
        <html:hidden name="possibleTests" property="sampleItemId" indexed="true"/>
        <span class="itemNumber" ><bean:write name="possibleTests" property="accessionNumber"/></span>
    </td>
    <td>
        <bean:write name="possibleTests" property="sampleType"/>
    </td>
    <td>
        <html:checkbox name="possibleTests" property="add" indexed="true" onchange="addRemoveRequest(this);" />
    </td>
    <td>&nbsp;
        <bean:write name="possibleTests" property="testName"/>
    </td>
</tr>
</logic:iterate>
</table>
<% } %>
<h2><bean:message key="sample.entry.addSample" /></h2>

<div id="samplesDisplay" class="colorFill" >
	<tiles:insert attribute="addSample"/>
</div>
</logic:equal>
</logic:equal>
<logic:equal name="<%=formName%>" property="noSampleFound" value="true">
	<bean:message key="sample.edit.sample.notFound"/>
</logic:equal>
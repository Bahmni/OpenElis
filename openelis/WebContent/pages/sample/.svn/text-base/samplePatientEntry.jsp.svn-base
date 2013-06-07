
<%@page import="us.mn.state.health.lims.common.formfields.FormFields.Field"%>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="us.mn.state.health.lims.common.action.IActionConstants,
			     us.mn.state.health.lims.common.util.SystemConfiguration,
			     us.mn.state.health.lims.common.util.ConfigurationProperties,
			     us.mn.state.health.lims.common.util.ConfigurationProperties.Property,
			     us.mn.state.health.lims.common.provider.validation.AccessionNumberValidatorFactory,
			     us.mn.state.health.lims.common.provider.validation.IAccessionNumberValidator,
			     us.mn.state.health.lims.common.formfields.FormFields,
                 us.mn.state.health.lims.common.util.Versioning,
			     us.mn.state.health.lims.common.util.StringUtil,
			     us.mn.state.health.lims.common.util.IdValuePair" %>


<%@ taglib uri="/tags/struts-bean"		prefix="bean" %>
<%@ taglib uri="/tags/struts-html"		prefix="html" %>
<%@ taglib uri="/tags/struts-logic"		prefix="logic" %>
<%@ taglib uri="/tags/labdev-view"		prefix="app" %>
<%@ taglib uri="/tags/struts-tiles"     prefix="tiles" %>
<%@ taglib uri="/tags/sourceforge-ajax" prefix="ajax"%>

<bean:define id="formName"		value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>' />
<bean:define id="idSeparator"	value='<%=SystemConfiguration.getInstance().getDefaultIdSeparator()%>' />
<bean:define id="accessionFormat" value='<%= ConfigurationProperties.getInstance().getPropertyValue(Property.AccessionFormat)%>' />
<bean:define id="genericDomain" value='' />

<%!
	String basePath = "";
	boolean useSTNumber = true;
	boolean useMothersName = true;
	boolean useReferralSiteList = false;
	boolean useProviderInfo = false;
	boolean patientRequired = false;
	boolean trackPayment = false;
	boolean requesterLastNameRequired = false;
	IAccessionNumberValidator accessionNumberValidator;

%>
<%
	String path = request.getContextPath();
	basePath = request.getScheme() + "://" + request.getServerName() + ":"	+ request.getServerPort() + path + "/";
	useSTNumber =  FormFields.getInstance().useField(FormFields.Field.StNumber);
	useMothersName = FormFields.getInstance().useField(FormFields.Field.MothersName);
	useReferralSiteList = FormFields.getInstance().useField(FormFields.Field.RequesterSiteList);
	useProviderInfo = FormFields.getInstance().useField(FormFields.Field.ProviderInfo);
	patientRequired = FormFields.getInstance().useField(FormFields.Field.PatientRequired);
	trackPayment = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.trackPatientPayment, "true");
	accessionNumberValidator = new AccessionNumberValidatorFactory().getValidator();
	requesterLastNameRequired = FormFields.getInstance().useField(Field.SampleEntryRequesterLastNameRequired);
%>

<script type="text/javascript" src="<%=basePath%>scripts/utilities.js?ver=<%= Versioning.getBuildNumber() %>" ></script>




<link rel="stylesheet" href="css/jquery_ui/jquery.ui.all.css?ver=<%= Versioning.getBuildNumber() %>">
<link rel="stylesheet" href="css/customAutocomplete.css?ver=<%= Versioning.getBuildNumber() %>">

<script src="scripts/ui/jquery.ui.core.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/ui/jquery.ui.widget.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/ui/jquery.ui.button.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/ui/jquery.ui.position.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/ui/jquery.ui.autocomplete.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/customAutocomplete.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="scripts/ajaxCalls.js?ver=<%= Versioning.getBuildNumber() %>"></script>


<script type="text/javascript" >

var useSTNumber = <%= useSTNumber %>;
var useMothersName = <%= useMothersName %>;
var useReferralSiteList = <%= useReferralSiteList%>;
var requesterLastNameRequired = <%= requesterLastNameRequired %>
var dirty = false;
var invalidSampleElements = new Array();
var requiredFields = new Array("labNo", "receivedDateForDisplay" );

if( requesterLastNameRequired ){
	requiredFields.push("providerLastNameID");
}
<% if( FormFields.getInstance().useField(Field.SampleEntryUseRequestDate)){ %>
	requiredFields.push("requestDate");
<% } %>
<%  if (requesterLastNameRequired) { %>
	requiredFields.push("providerLastNameID");
<% } %>


$jq(function() {
     	var dropdown = $jq( "select#requesterId" );
        autoCompleteWidth = dropdown.width() + 66 + 'px';
        clearNonMatching = false;
		capitialize = true;
		dropdown.combobox();
       // invalidLabID = '<bean:message key="error.site.invalid"/>'; // Alert if value is typed that's not on list. FIX - add badmessage icon
        maxRepMsg = '<bean:message key="sample.entry.project.siteMaxMsg"/>'; 
        
        resultCallBack = function( textValue) {
  				siteListChanged(textValue);
  				makeDirty();
  				setSave();
				};
});

function isFieldValid(fieldname)
{
	return invalidSampleElements.indexOf(fieldname) == -1;
}

function setSampleFieldInvalid(field)
{
	if( invalidSampleElements.indexOf(field) == -1 )
	{
		invalidSampleElements.push(field);
	}
}

function setSampleFieldValid(field)
{
	var removeIndex = invalidSampleElements.indexOf( field );
	if( removeIndex != -1 )
	{
		for( var i = removeIndex + 1; i < invalidSampleElements.length; i++ )
		{
			invalidSampleElements[i - 1] = invalidSampleElements[i];
		}

		invalidSampleElements.length--;
	}
}

function isSaveEnabled()
{
	return invalidSampleElements.length == 0;
}

function submitTheForm(form)
{
	setAction(form, 'Update', 'yes', '?ID=');
}

function  /*void*/ processValidateEntryDateSuccess(xhr){

    //alert(xhr.responseText);
	
	var message = xhr.responseXML.getElementsByTagName("message").item(0).firstChild.nodeValue;
	var formField = xhr.responseXML.getElementsByTagName("formfield").item(0).firstChild.nodeValue;

	var isValid = message == "<%=IActionConstants.VALID%>";

	//utilites.js
	selectFieldErrorDisplay( isValid, $(formField));
	setSampleFieldValidity( isValid, formField );
	setSave();

	if( message == '<%=IActionConstants.INVALID_TO_LARGE%>' ){
		alert( '<bean:message key="error.date.inFuture"/>' );
	}else if( message == '<%=IActionConstants.INVALID_TO_SMALL%>' ){
		alert( '<bean:message key="error.date.inPast"/>' );
	}
}


function checkValidEntryDate(date, dateRange)
{	
	if(!date.value || date.value == ""){
		setSave();
		return;
	}

	if( !dateRange || dateRange == ""){
		dateRange = 'past';
	}
	//ajax call from utilites.js
	isValidDate( date.value, processValidateEntryDateSuccess, date.name, dateRange );
}


function processAccessionSuccess(xhr)
{
	//alert(xhr.responseText);
	var formField = xhr.responseXML.getElementsByTagName("formfield").item(0);
	var message = xhr.responseXML.getElementsByTagName("message").item(0);
	var success = false;

	if (message.firstChild.nodeValue == "valid"){
		success = true;
	}
	var labElement = formField.firstChild.nodeValue;
	selectFieldErrorDisplay( success, $(labElement));
	setSampleFieldValidity( success, labElement);

	if( !success ){
		alert( message.firstChild.nodeValue );
	}

	setSave();
}

function processAccessionFailure(xhr)
{
	//unhandled error: someday we should be nicer to the user
}


function checkAccessionNumber( accessionNumber )
{
	//check if empty
	if ( !fieldIsEmptyById( "labNo" ) )
	{
		validateAccessionNumberOnServer(false, accessionNumber.id, accessionNumber.value, processAccessionSuccess, processAccessionFailure );
	}
	else
	{
		setSampleFieldInvalid(accessionNumber.name );
		setValidIndicaterOnField(false, accessionNumber.name);
	}

	setSave();
}

//Note this is hard wired for Haiti -- do not use
function checkEntryPhoneNumber( phone )
{

	var regEx = new RegExp("^\\(?\\d{3}\\)?\\s?\\d{4}[- ]?\\d{4}\\s*$");

	var valid = regEx.test(phone.value);

	selectFieldErrorDisplay( valid, phone );
	setValidIndicaterOnField(valid, phone.name);

	setSave();
}

function setSampleFieldValidity( valid, fieldName ){

	if( valid )
	{
		setSampleFieldValid(fieldName);
	}
	else
	{
		setSampleFieldInvalid(fieldName);
	}
}


function checkValidTime(time)
{
	var lowRangeRegEx = new RegExp("^[0-1]{0,1}\\d:[0-5]\\d$");
	var highRangeRegEx = new RegExp("^2[0-3]:[0-5]\\d$");

	if( lowRangeRegEx.test(time.value) ||
	    highRangeRegEx.test(time.value) )
	{
		if( time.value.length == 4 )
		{
			time.value = "0" + time.value;
		}
		clearFieldErrorDisplay(time);
		setSampleFieldValid(time.name);
	}
	else
	{
		setFieldErrorDisplay(time);
		setSampleFieldInvalid(time.name);
	}

	setSave();
}

function setMyCancelAction(form, action, validate, parameters)
{
	//first turn off any further validation
	setAction(window.document.forms[0], 'Cancel', 'no', '');
}


function patientInfoValid()
{
	var hasError = false;
	var returnMessage = "";

	if( fieldIsEmptyById("patientID") )
	{
		hasError = true;
		returnMessage += ": patient ID";
	}

	if( fieldIsEmptyById("dossierID") )
	{
		hasError = true;
		returnMessage += ": dossier ID";
	}

	if( fieldIsEmptyById("firstNameID") )
	{
		hasError = true;
		returnMessage += ": first Name";
	}
	if( fieldIsEmptyById("lastNameID") )
	{
		hasError = true;
		returnMessage += ": last Name";
	}


	if( hasError )
	{
		returnMessage = "Please enter the following patient values  " + returnMessage;
	}else
	{
		returnMessage = "valid";
	}

	return returnMessage;
}



function saveItToParentForm(form) {
 submitTheForm(form);
}

function getNextAccessionNumber() {
	generateNextScanNumber();
}

function generateNextScanNumber(){

	var selected = "";

	new Ajax.Request (
                          'ajaxQueryXML',  //url
                           {//options
                             method: 'get', //http method
                             parameters: "provider=SampleEntryGenerateScanProvider&programCode=" + selected,
                             //indicator: 'throbbing'
                             onSuccess:  processScanSuccess,
                             onFailure:  processScanFailure
                           }
                          );
}

function processScanSuccess(xhr){
	//alert(xhr.responseText);
	var formField = xhr.responseXML.getElementsByTagName("formfield").item(0);
	var returnedData = formField.firstChild.nodeValue;

	var message = xhr.responseXML.getElementsByTagName("message").item(0);

	var success = message.firstChild.nodeValue == "valid";

	if( success ){
		$("labNo").value = returnedData;

	}else{
		alert( "<%= StringUtil.getMessageForKey("error.accession.no.next") %>");
		$("labNo").value = "";
	}

	var targetName = $("labNo").name;
	selectFieldErrorDisplay(success, $(targetName));
	setValidIndicaterOnField( success, targetName );

	setSave();
}

function processScanFailure(xhr){
	//some user friendly response needs to be given to the user
}

function addPatientInfo(  ){
	$("patientInfo").show();
}

function showHideSection(button, targetId){
	if( button.value == "+" ){
		$(targetId).show();
		button.value = "-";
	}else{
		$(targetId).hide();
		button.value = "+";
	}
}

function /*bool*/ requiredSampleEntryFieldsValid(){
	for( var i = 0; i < requiredFields.length; ++i ){
		if( $(requiredFields[i]).value.blank() ){
			//special casing
			if( requiredFields[i] == "requesterId" && 
			   !( ($("requesterId").selectedIndex == 0)  &&  $("newRequesterName").value.blank())){
				continue;
			}
		return false;
		}
	}


	return allSamplesHaveTests();
}

function /*bool*/ sampleEntryTopValid(){
	return invalidSampleElements.length == 0 && requiredSampleEntryFieldsValid();
}

function /*void*/ loadSamples(){
	alert( "Implementation error:  loadSamples not found in addSample tile");
}

function show(id){
	document.getElementById(id).style.visibility="visible";
}

function hide(id){
	document.getElementById(id).style.visibility="hidden";
}

function orderTypeSelected( radioElement){
	labOrderType = radioElement.value; //labOrderType is in sampleAdd.jsp
	if( removeAllRows){
		removeAllRows();
	}
	//this is bogus, we should go back to the server to load the dropdown
	if( radioElement.value == 2){
		$("followupLabOrderPeriodId").show();
		$("initialLabOrderPeriodId").hide();
	}else{
		$("initialLabOrderPeriodId").show();
		$("followupLabOrderPeriodId").hide();
	}
	
	$("sampleEntryPage").show();
}
function labPeriodChanged( labOrderPeriodElement){
	if( labOrderPeriodElement.length - 1 ==  labOrderPeriodElement.selectedIndex  ){
		$("labOrderPeriodOtherId").show();
	}else{
		$("labOrderPeriodOtherId").hide();
		$("labOrderPeriodOtherId").value = "";
	}
	
}

function siteListChanged(textValue){
	var siteList = $("requesterId");
	
	//if the index is 0 it is a new entry, if it is not then the textValue may include the index value
	if( siteList.selectedIndex == 0 || siteList.options[siteList.selectedIndex].label != textValue){
		  $("newRequesterName").value = textValue;
	}else{
		//do auto fill stuff
	}
}

function capitalizeValue( text){
	$("requesterId").value = text.toUpperCase();
}
</script>

<bean:define id="orderTypeList"  name='<%=formName%>' property="orderTypes" type="java.util.Collection"/>
<html:hidden property="currentDate" name="<%=formName%>" styleId="currentDate"/>
<html:hidden property="domain" name="<%=formName%>" value="<%=genericDomain%>" styleId="domain"/>
<html:hidden property="removedSampleItem" value="" styleId="removedSampleItem"/>
<html:hidden property="newRequesterName" name='<%=formName %>' styleId="newRequesterName" />

<% if( FormFields.getInstance().useField(Field.SampleEntryLabOrderTypes)) {%>
	<logic:iterate indexId="index" id="orderTypes"  type="IdValuePair" name='<%=formName%>' property="orderTypes">
		<input id='<%="orderType_" + index %>' 
		       type="radio" 
		       name="orderType" 
		       onchange='orderTypeSelected(this);'
		       value='<%=orderTypes.getId() %>' />
		<label for='<%="orderType_" + index %>' ><%=orderTypes.getValue() %></label>
	</logic:iterate>
	<hr/>
<% } %>

<div id=sampleEntryPage <%= (orderTypeList == null || orderTypeList.size() == 0)? "" : "style='display:none'"  %>>

<html:button property="showHide" value="-" onclick="showHideSection(this, 'orderDisplay');" />
<%= StringUtil.getContextualMessageForKey("sample.entry.order.label") %>
<span class="requiredlabel">*</span>

<div id=orderDisplay >
<table  style="width:90%" >

	<tr>
		<td>
			<table >
					<tr>
					<td width="35%">
						<%=StringUtil.getContextualMessageForKey("quick.entry.accession.number")%>
						:
						<span class="requiredlabel">*</span>
					</td>
					<td width="65%">
						<app:text name="<%=formName%>" property="labNo"
							maxlength='<%= Integer.toString(accessionNumberValidator.getMaxAccessionLength())%>'
							onchange="checkAccessionNumber(this);makeDirty();"
							styleClass="text"
							styleId="labNo" />
				
						<bean:message key="sample.entry.scanner.instructions"/>
						<html:button property="generate"
									 styleClass="textButton"
									 onclick="getNextAccessionNumber(); makeDirty();" >
						<bean:message key="sample.entry.scanner.generate"/>
						</html:button>
					</td>
					</tr>
					<% if( FormFields.getInstance().useField(Field.SampleEntryUseRequestDate)){ %>
					<tr>
						<td><bean:message key="sample.entry.requestDate" />:
						<span class="requiredlabel">*</span><font size="1"><bean:message key="sample.date.format" /></font></td>
						<td><html:text name='<%=formName %>' 
						               property="requestDate" 
						               styleId="requestDate"
						               onchange="makeDirty();checkValidEntryDate(this, 'past')" 
						               onkeyup="addDateSlashes(this, event);" 
						               maxlength="10"/>
					</tr>
					<% } %>
					<tr>
					<td >
					    <%= StringUtil.getContextualMessageForKey("quick.entry.received.date") %>
						:
						<span class="requiredlabel">*</span>
						<font size="1"><bean:message key="sample.date.format" />
						</font>
					</td>
				    <td colspan="2">
						<app:text name="<%=formName%>" 
						    property="receivedDateForDisplay"
							onchange="checkValidEntryDate(this, 'past');makeDirty();"
							onkeyup="addDateSlashes(this, event);" 
							maxlength="10"
							styleClass="text"
							styleId="receivedDateForDisplay" />
					
					<% if( FormFields.getInstance().useField(Field.SampleEntryUseReceptionHour)){ %>
					    <bean:message key="sample.receptionTime" />:
							<html:text name="<%=formName %>" property="recievedTime" onchange="makeDirty(); checkValidTime(this)"/>
					
					<% } %>
						</td>
				</tr>
				<% if( FormFields.getInstance().useField(Field.SampleEntryNextVisitDate)){ %>
				<tr>
					<td><bean:message key="sample.entry.nextVisit.date" />&nbsp;<font size="1"><bean:message key="sample.date.format" /></font>:</td>
					<td>
						<html:text name='<%= formName %>'
						           property="nextVisitDate" 
						           onchange="makeDirty();checkValidEntryDate(this, 'future')"
						           onkeyup="addDateSlashes(this, event);" 
						           styleId="nextVisitDate"
						           maxlength="10"/>
					</td>
				</tr>
				<% } %>
				<tr>
				<td>&nbsp;</td>
				</tr>
				<% if( FormFields.getInstance().useField(Field.SampleEntryRequestingSiteSampleId)) {%>
				<tr>
					<td >
						<bean:message key="sample.clientReference" />
						:
					</td>
					<td >
						<app:text name="<%=formName%>"
								  property="requesterSampleID"
								  styleId="requestingFacilityID"
								  size="50"
								  maxlength="50"
								  onchange="makeDirty();"/>
					</td>
					<td width="10%" >&nbsp;</td>
					<td width="45%" >&nbsp;</td>
				</tr>
				<% } %>
				<% if( useReferralSiteList){ %>
				<tr>
					<td >
						<%= StringUtil.getContextualMessageForKey("sample.entry.project.siteName") %>:
						<% if( FormFields.getInstance().useField(Field.SampleEntryReferralSiteNameRequired)) {%>
						<span class="requiredlabel">*</span>
						<% } %>
                    </td>
                    <td colspan="3">
						<html:select styleId="requesterId"
								     name="<%=formName%>"
								     property="referringSiteId"
								     onchange="makeDirty();siteListChanged(this);setSave();" 
								     onkeyup="capitalizeValue( this.value );"
								     >
							<option value=""></option>
							<html:optionsCollection name="<%=formName%>" property="referringSiteList" label="value" value="id" />
					   	</html:select>
					</td>
				</tr>
				<% } %>
				<% if( FormFields.getInstance().useField(Field.SampleEntryReferralSiteCode)){ %>
				<tr>
					<td >
					    <bean:message key="sample.entry.referringSite.code" />
					</td>
					<td>    
						<html:text styleId="requesterCodeId"
								     name="<%=formName%>"
								     property="referringSiteCode"
								     onchange="makeDirty();setSave();">
					   	</html:text>
					</td>
				</tr>
				<% } %>
				<tr>
				<td>&nbsp;</td>
				</tr>
				<%  if (useProviderInfo) { %>
				<tr>
					<td >
						<%= StringUtil.getContextualMessageForKey("sample.entry.provider.name") %>:
						<% if(requesterLastNameRequired ){ %>
						<span class="requiredlabel">*</span>
						<% } %>
					</td>
					<td >
						<html:text name="<%=formName%>"
								  property="providerLastName"
							      styleId="providerLastNameID"
							      onchange="makeDirty();setSave()"
							      size="30" />
						<bean:message key="humansampleone.provider.firstName.short"/>:
						<html:text name="<%=formName%>"
								  property="providerFirstName"
							      styleId="providerFirstNameID"
							      onchange="makeDirty();"
							      size="30" />
					</td>
				</tr>
				<tr>
					<td>
					    <%= StringUtil.getContextualMessageForKey("humansampleone.provider.workPhone") + ": " +  StringUtil.getContextualMessageForKey("humansampleone.phone.additionalFormat")%>
					</td>
					<td>
						<app:text name="<%=formName%>"
						          property="providerWorkPhone"
							      styleId="providerWorkPhoneID"
							      size="20"
							      styleClass="text"
							      onchange="makeDirty()" />
					</td>
				</tr>
				<% } %>
				<% if( FormFields.getInstance().useField(Field.SampleEntryProviderFax)){ %>
					<tr>
					<td>
					    <%= StringUtil.getContextualMessageForKey("sample.entry.project.faxNumber")%>:
					</td>
					<td>
						<app:text name="<%=formName%>"
						          property="providerFax"
							      styleId="providerFaxID"
							      size="20"
							      styleClass="text"
							      onchange="makeDirty()" />
					</td>
				</tr>
				<% } %>
				<% if( FormFields.getInstance().useField(Field.SampleEntryProviderEmail)){ %>
					<tr>
					<td>
					    <%= StringUtil.getContextualMessageForKey("sample.entry.project.email")%>:
					</td>
					<td>
						<app:text name="<%=formName%>"
						          property="providerEmail"
							      styleId="providerEmailID"
							      size="20"
							      styleClass="text"
							      onchange="makeDirty()" />
					</td>
				</tr>
				<% } %>
				<% if( FormFields.getInstance().useField(Field.SampleEntryHealthFacilityAddress)) {%>
				<tr>
					<td><bean:message key="sample.entry.facility.address"/>:</td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;<bean:message key="sample.entry.facility.street"/>
					<td>	
					<html:text name='<%=formName %>'
							   property="facilityAddressStreet" 
							   styleClass="text"
							   onchange="makeDirty()"/>
					</td>		     
				</tr>
				<tr>
					<td>&nbsp;&nbsp;<bean:message key="sample.entry.facility.commune"/>:<td>	
					<html:text name='<%=formName %>'
							   property="facilityAddressCommune" 
							   styleClass="text"
							   onchange="makeDirty()"/>
					</td>  
				</tr>
				<tr>
					<td><bean:message key="sample.entry.facility.phone"/>:<td>	
					<html:text name='<%=formName %>'
							   property="facilityPhone" 
							   styleClass="text"
							   onchange="makeDirty()"/>
					</td>  
				</tr>
				<tr>
					<td><bean:message key="sample.entry.facility.fax"/>:<td>	
					<html:text name='<%=formName %>'
							   property="facilityFax" 
							   styleClass="text"
							   onchange="makeDirty()"/>
					</td>  
				</tr>
				<% } %>
				<tr><td>&nbsp;</td></tr>
				<% if( trackPayment){ %>
				<tr>
					<td><bean:message key="sample.entry.patientPayment"/>: </td>
					<td>
						
					<html:select name="<%=formName %>" property="paymentOptionSelection" >
								<option value='' ></option>
					<logic:iterate id="optionValue" name='<%=formName%>' property="paymentOptions" type="IdValuePair" >
								<option value='<%=optionValue.getId()%>' >
									<bean:write name="optionValue" property="value"/>
								</option>
					</logic:iterate>
					</html:select>
					</td>
				</tr>
				<% } %>
				<% if( FormFields.getInstance().useField(Field.SampleEntryLabOrderTypes)) {%>
				<tr >
					<td><bean:message key="sample.entry.sample.period"/>:</td>
					<td>
						<html:select name="<%=formName %>" 
						             property="followupPeriodOrderType" 
						             onchange="makeDirty(); labPeriodChanged( this, '8' )" 
						             styleId="followupLabOrderPeriodId" 
						             style="display:none">
						<option value='' ></option>
						<logic:iterate id="optionValue" name='<%=formName%>' property="followupPeriodOrderTypes" type="IdValuePair" >
						<option value='<%=optionValue.getId()%>' >
							<bean:write name="optionValue" property="value"/>
						</option>
						</logic:iterate>
						</html:select>
						<html:select name="<%=formName %>" 
						             property="initialPeriodOrderType" 
						             onchange="makeDirty(); labPeriodChanged( this, '2' )" 
						             styleId="initialLabOrderPeriodId" 
						             style="display:none">
						<option value='' ></option>
						<logic:iterate id="optionValue" name='<%=formName%>' property="initialPeriodOrderTypes" type="IdValuePair" >
						<option value='<%=optionValue.getId()%>' >
							<bean:write name="optionValue" property="value"/>
						</option>
						</logic:iterate>
						</html:select>
						&nbsp;
						<html:text name='<%= formName %>' 
						           property="otherPeriodOrder" 
						           styleId="labOrderPeriodOtherId" 
						           style="display:none" />
					</td>							
				</tr>
				<% } %>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>		
<hr style="width: 100%; height: 5px" />
<html:button property="showHide" value="+" onclick="showHideSection(this, 'samplesDisplay');" />
<%= StringUtil.getContextualMessageForKey("sample.entry.sampleList.label") %>
<span class="requiredlabel">*</span>

<div id="samplesDisplay" class="colorFill" style="display:none;" >
	<tiles:insert attribute="addSample"/>
</div>

<br />
<hr style="width: 100%; height: 5px" />
<html:hidden name="<%=formName%>" property="patientPK" styleId="patientPK"/>

<table style="width:100%">
	<tr>
		<td width="15%" align="left">
			<html:button property="showPatient" onclick="showHideSection(this, 'patientInfo');" >+</html:button>
			<bean:message key="sample.entry.patient" />:
			<% if ( patientRequired ) { %><span class="requiredlabel">*</span><% } %>
		</td>
		<td width="15%" id="firstName"><b>&nbsp;</b></td>
		<td width="15%">
			<% if(useMothersName){ %><bean:message key="patient.mother.name"/>:<% } %>
		</td>
		<td width="15%" id="mother"><b>&nbsp;</b></td>
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
		<td>
			<%=StringUtil.getContextualMessageForKey("patient.NationalID") %>:
		</td>
		<td id="national"><b>&nbsp;</b></td>
		<td>
			<bean:message key="patient.gender"/>:
		</td>
		<td id="gender"><b>&nbsp;</b></td>
	</tr>
</table>

<div id="patientInfo"  style="display:none;" >
	<tiles:insert attribute="patientInfo" />
	<tiles:insert attribute="patientClinicalInfo" />
</div>
</div>
<script type="text/javascript" >

//all methods here either overwrite methods in tiles or all called after they are loaded

function /*void*/ makeDirty(){
	dirty=true;
	if( typeof(showSuccessMessage) != 'undefinded' ){
		showSuccessMessage(false); //refers to last save
	}
	// Adds warning when leaving page if content has been entered into makeDirty form fields
	function formWarning(){ 
    return "<bean:message key="banner.menu.dataLossWarning"/>";
	}
	window.onbeforeunload = formWarning;
}

function  /*void*/ savePage()
{
	loadSamples(); //in addSample tile

  window.onbeforeunload = null; // Added to flag that formWarning alert isn't needed.
	var form = window.document.forms[0];
	form.action = "SamplePatientEntrySave.do";
	form.submit();
}


function /*void*/ setSave()
{
	var validToSave =  patientFormValid() && sampleEntryTopValid();
	$("saveButtonId").disabled = !validToSave;
}

//called from patientSearch.jsp
function /*void*/ selectedPatientChangedForSample(firstName, lastName, gender, DOB, stNumber, subjectNumb, nationalID, mother, pk ){
	patientInfoChangedForSample( firstName, lastName, gender, DOB, stNumber, subjectNumb, nationalID, mother, pk );
	$("patientPK").value = pk;

	setSave();
}

//called from patientManagment.jsp
function /*void*/ patientInfoChangedForSample( firstName, lastName, gender, DOB, stNumber, subjectNum, nationalID, mother, pk ){
	setPatientSummary( "firstName", firstName );
	setPatientSummary( "lastName", lastName );
	setPatientSummary( "gender", gender );
	setPatientSummary( "dob", DOB );
	if( useSTNumber){setPatientSummary( "st", stNumber );}
	setPatientSummary( "national", nationalID );
	if( useMothersName){setPatientSummary( "mother", mother );}
	$("patientPK").value = pk;

	makeDirty();
	setSave();
}

function /*voiid*/ setPatientSummary( name, value ){
	$(name).firstChild.firstChild.nodeValue = value;
}

//overwrites function from patient search
function /*void*/ doSelectPatient(){
/*	$("firstName").firstChild.firstChild.nodeValue = currentPatient["first"];
	$("mother").firstChild.firstChild.nodeValue = currentPatient["mother"];
	$("st").firstChild.firstChild.nodeValue = currentPatient["st"];
	$("lastName").firstChild.firstChild.nodeValue = currentPatient["last"];
	$("dob").firstChild.firstChild.nodeValue = currentPatient["DOB"];
	$("national").firstChild.firstChild.nodeValue = currentPatient["national"];
	$("gender").firstChild.firstChild.nodeValue = currentPatient["gender"];
	$("patientPK").value = currentPatient["pk"];

	setSave();

*/
}

var patientRegistered = false;
var sampleRegistered = false;

/* is registered in patientManagement.jsp */
function /*void*/ registerPatientChangedForSampleEntry(){
	if( !patientRegistered ){
		addPatientInfoChangedListener( patientInfoChangedForSample );
		patientRegistered = true;
	}
}

/* is registered in sampleAdd.jsp */
function /*void*/ registerSampleChangedForSampleEntry(){
	if( !sampleRegistered ){
		addSampleChangedListener( makeDirty );
		sampleRegistered = true;
	}
}

registerPatientChangedForSampleEntry();
registerSampleChangedForSampleEntry();

</script>

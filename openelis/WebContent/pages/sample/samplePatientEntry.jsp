
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
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

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
<bean:define id="fieldsetOrder" name='<%=formName%>' property='sampleEntryFieldsetOrder' type="java.util.List" />

<%!
	String basePath = "";
	boolean useSTNumber = true;
	boolean useMothersName = true;
	boolean useReferralSiteList = false;
	boolean useProviderInfo = false;
	boolean patientRequired = false;
	boolean trackPayment = false;
	boolean requesterLastNameRequired = false;
    boolean useSampleSource = false;
	IAccessionNumberValidator accessionNumberValidator;
    Map<String,String> fieldsetToJspMap = new HashMap<String, String>() ;
	String sampleId = "";

%>
<%
	String path = request.getContextPath();
	basePath = path + "/";
	useSTNumber =  FormFields.getInstance().useField(FormFields.Field.StNumber);
	useMothersName = FormFields.getInstance().useField(FormFields.Field.MothersName);
	useReferralSiteList = FormFields.getInstance().useField(FormFields.Field.RequesterSiteList);
	useProviderInfo = FormFields.getInstance().useField(FormFields.Field.ProviderInfo);
	patientRequired = FormFields.getInstance().useField(FormFields.Field.PatientRequired);
	trackPayment = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.trackPatientPayment, "true");
	accessionNumberValidator = new AccessionNumberValidatorFactory().getValidator();
	requesterLastNameRequired = FormFields.getInstance().useField(Field.SampleEntryRequesterLastNameRequired);
    useSampleSource = FormFields.getInstance().useField(Field.UseSampleSource);
    fieldsetToJspMap.put("patient","SamplePatientInfoSection.jsp");
    fieldsetToJspMap.put("samples","SamplePatientSampleSection.jsp");
    fieldsetToJspMap.put("order","SampleOrderInfoSection.jsp");
	sampleId = request.getParameter("id");
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
var useSampleSource = <%= useSampleSource%>
var dirty = false;
var invalidSampleElements = new Array();
var requiredFields = new Array("labNo", "receivedDateForDisplay" );
var sampleId = "<%= sampleId %>";

if( requesterLastNameRequired ){
	requiredFields.push("providerLastNameID");
}
<% if( FormFields.getInstance().useField(Field.SampleEntryUseRequestDate)){ %>
	requiredFields.push("requestDate");
<% } %>
<%  if (requesterLastNameRequired) { %>
	requiredFields.push("providerLastNameID");
<% } %>
<%  if (useSampleSource) { %>
requiredFields.push("sampleSourceID");
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


        $('<%= fieldsetOrder.get(0) +"Display" %>').show();

        $('addEditPatient').hide();
	if(sampleId != "null") {
		getSampleOrderDetailsFromSampleId(sampleId, processSampleOrderDetailsSuccess, processSampleOrderDetailsFailure);
	}
	else{
		getDefaultSampleSource(processDefaultSampleSourceSuccess)
	}

});

function processDefaultSampleSourceSuccess(xhr) {
	var defaultSampleSource = xhr.responseXML.getElementsByTagName("defaultSampleSourceID");
	$jq("#sampleSourceID").val(defaultSampleSource[0].innerHTML);
}

function processSampleOrderDetailsSuccess(xhr){
	var sampleSource = xhr.responseXML.getElementsByTagName("sampleSource");
	var sampleRequester = xhr.responseXML.getElementsByTagName("sampleRequester");
	var sampleReceivedDateForDisplay = xhr.responseXML.getElementsByTagName("sampleReceivedDateForDisplay");
	$jq("#sampleSourceID").val(sampleSource[0].innerHTML);
	$jq("#sampleSourceID").attr("disabled", "disabled");
	$jq("#receivedDateForDisplay").val(sampleReceivedDateForDisplay[0].innerHTML);
	if(sampleRequester.length > 0)
	{
		$jq("#providerId").val(sampleRequester[0].innerHTML);
	}
}
function processSampleOrderDetailsFailure(xhr){
}

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

function successUpdateAccession(xhr)
{

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
    if ( !new RegExp("^([0-9]+)([-]*)([0-9]*)$").test(accessionNumber.value) ) {
        setSampleFieldInvalid(accessionNumber.name );
       	setValidIndicaterOnField(false, accessionNumber.name);
    }
	//check if empty
	else if ( !fieldIsEmptyById( "labNo" ) )
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
5
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
	$("patientDisplay").show();
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
	//$("sampleEntryPage").show();
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
<div id=sampleEntryPage <%= (orderTypeList == null || orderTypeList.size() == 0)? "" : "style='display:none'"  %>>
    <jsp:include page="<%=fieldsetToJspMap.get(fieldsetOrder.get(0))%>" />
    <jsp:include page="<%=fieldsetToJspMap.get(fieldsetOrder.get(1))%>" />

    <jsp:include page="<%=fieldsetToJspMap.get(fieldsetOrder.get(2))%>" />
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
    jQuery("#saveButtonId").attr("disabled", "disabled");
	loadSamples(); //in addSample tile
    window.onbeforeunload = null; // Added to flag that formWarning alert isn't needed.
    var form = window.document.forms[0];
	if (supportSTNumber) {
		form.elements.namedItem("patientProperties.STnumber").value = $('ST_ID').value;
	}
	if(sampleId != "null"){
		var accessionNumber = $("labNo").value
		var rows = $jq('#samplesAddedTable tr');
		var testIdsSelected = new Object();
		var typeIdsSelected = new Object();
		for (var i=1; i<rows.length; i++){
			testIdsSelected[i-1]=$jq('#testIds'+rows[i].id).val();
			typeIdsSelected[i-1] =$jq('#typeId'+rows[i].id).val();
		}
		var testsAndTypes = new Object();
		testsAndTypes.tests = testIdsSelected;
		testsAndTypes.types = typeIdsSelected;
		var typeAndTestIdsJson = JSON.stringify(testsAndTypes);

		updateTestsWithAccessionNumber(accessionNumber, sampleId, typeAndTestIdsJson, successUpdateAccession, processScanFailure)
		form.action = "LabDashboard.do?";
	}else {
		form.action = "SamplePatientEntrySave.do";
	}
	form.submit();
}


function /*void*/ setSave()
{
	var validToSave =  patientFormValid() && sampleEntryTopValid();
    if (validToSave) {
        jQuery("#saveButtonId").removeAttr("disabled", "disabled");
    } else {
        jQuery("#saveButtonId").attr("disabled", "disabled");
    }
}

//called from patientSearch.jsp
function /*void*/ selectedPatientChangedForSample(firstName, lastName, gender, DOB, stNumber, subjectNumb, nationalID, mother, pk ){
	patientInfoChangedForSample( firstName, lastName, gender, DOB, stNumber, subjectNumb, nationalID, mother, pk );
	$("patientPK").value = pk;

	setSave();
}

//called from patientManagment.jsp
function /*void*/ patientInfoChangedForSample( firstName, lastName, gender, DOB, stNumber, subjectNum, nationalID, mother, pk ){
	$("patientPK").value = pk;

	makeDirty();
	setSave();
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

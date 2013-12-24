<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="us.mn.state.health.lims.common.action.IActionConstants,
                 us.mn.state.health.lims.common.util.SystemConfiguration,
                 us.mn.state.health.lims.common.formfields.FormFields,
                 us.mn.state.health.lims.common.formfields.FormFields.Field,
                 us.mn.state.health.lims.common.util.StringUtil,
                 us.mn.state.health.lims.common.util.Versioning,
                 us.mn.state.health.lims.patient.action.bean.PatientManagmentInfo,
                 us.mn.state.health.lims.patient.action.bean.AddressPartForm" %>


<%@ taglib uri="/tags/struts-bean"		prefix="bean" %>
<%@ taglib uri="/tags/struts-html"		prefix="html" %>
<%@ taglib uri="/tags/struts-logic"		prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles"		prefix="tiles" %>
<%@ taglib uri="/tags/struts-nested"	prefix="nested" %>
<%@ taglib uri="/tags/labdev-view"		prefix="app" %>
<%@ taglib uri="/tags/sourceforge-ajax" prefix="ajax"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/xml_rt" %>
<%@ taglib prefix="p" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<bean:define id="formName"		value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>' />
<bean:define id="patientProperties" name='<%=formName%>' property='patientProperties' type="PatientManagmentInfo" />
<bean:define id="collapse" name='<%=formName%>' property='collapsePatientInfo' type="Boolean" />


<%!
	String basePath = "";
	boolean supportSTNumber = true;
	boolean supportAKA = true;
	boolean supportMothersName = true;
	boolean supportPatientType = true;
	boolean supportInsurance = true;
	boolean supportSubjectNumber = true;
	boolean supportNationalID = false;
	boolean supportOccupation = true;
	boolean supportCommune = true;
	boolean supportAddressDepartment = false;
	boolean supportMothersInitial = true;
	boolean patientRequired = true;
	boolean patientIDRequired = true;
	boolean patientNamesRequired = true;
	boolean patientAgeRequired = true;
	boolean patientGenderRequired = true;
    boolean supportDynamicAddresses = false;
    boolean supportfirstNameFirst;
    boolean supportHealthCenters = false;
    boolean supportPrimaryRelative = false;
    boolean allHealthCenter = false;
 %>
<%
    allHealthCenter = false;
    if (request.getAttribute("allHealthCenter") != null) {
        allHealthCenter = (Boolean) request.getAttribute("allHealthCenter");
    }

    String path = request.getContextPath();
	basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
	supportSTNumber = FormFields.getInstance().useField(Field.StNumber);
	supportAKA = FormFields.getInstance().useField(Field.AKA);
	supportMothersName = FormFields.getInstance().useField(Field.MothersName);
	supportPatientType = FormFields.getInstance().useField(Field.PatientType);
	supportInsurance = FormFields.getInstance().useField(Field.InsuranceNumber);
	supportSubjectNumber = FormFields.getInstance().useField(Field.SubjectNumber);
	supportNationalID = FormFields.getInstance().useField(Field.NationalID);
	supportOccupation = FormFields.getInstance().useField(Field.Occupation);
	supportCommune = FormFields.getInstance().useField(Field.Commune);
	supportMothersInitial = FormFields.getInstance().useField(Field.MotherInitial);
	supportAddressDepartment = FormFields.getInstance().useField(Field.AddressDepartment );
    supportDynamicAddresses = FormFields.getInstance().useField(Field.DynamicAddress);
    supportfirstNameFirst = FormFields.getInstance().useField(Field.FirstNameFirst);
    supportHealthCenters = FormFields.getInstance().useField(Field.HealthCenter);
    supportPrimaryRelative = FormFields.getInstance().useField(Field.SupportPrimaryRelative);

	if("SampleConfirmationEntryForm".equals( formName )){
		patientIDRequired = FormFields.getInstance().useField(Field.PatientIDRequired_SampleConfirmation);
		patientRequired = FormFields.getInstance().useField(Field.PatientRequired_SampleConfirmation );
		patientAgeRequired = FormFields.getInstance().useField(Field.PatientAgeRequired_SampleConfirmation );
		patientGenderRequired = FormFields.getInstance().useField(Field.PatientGenderRequired_SampleConfirmation );
	}else{
		patientIDRequired = FormFields.getInstance().useField(Field.PatientIDRequired);
	    patientRequired = FormFields.getInstance().useField(Field.PatientRequired );
	    patientAgeRequired = FormFields.getInstance().useField(Field.PatientAgeRequired_SampleEntry );
		patientGenderRequired = FormFields.getInstance().useField(Field.PatientGenderRequired_SampleEntry);
	}
	
	patientNamesRequired = FormFields.getInstance().useField(Field.PatientNameRequired);
%>


<script type="text/javascript" src="<%=basePath%>scripts/utilities.js?ver=<%= Versioning.getBuildNumber() %>" ></script>

<script type="text/javascript" >

/*the prefix pt_ is being used for scoping.  Since this is being used as a tile there may be collisions with other
  tiles with simular names.  Only those elements that may cause confusion are being tagged, and we know which ones will collide
  because we can predicte the future */

var supportSTNumber = <%= supportSTNumber %>;
var supportAKA = <%= supportAKA %>;
var supportMothersName = <%= supportMothersName %>;
var supportPatientType = <%= supportPatientType %>;
var supportInsurance = <%= supportInsurance %>;
var supportSubjectNumber = <%= supportSubjectNumber %>;
var supportNationalID = <%= supportNationalID %>;
var supportMothersInitial = <%= supportMothersInitial %>;
var supportCommune = <%= supportCommune %>;
var supportCity = <%= FormFields.getInstance().useField(Field.AddressVillage) %>;
var supportOccupation = <%= supportOccupation %>;
var supportAddressDepartment = <%= supportAddressDepartment %>;
var patientRequired = <%= patientRequired %>;
var patientIDRequired = <%= patientIDRequired %>;
var patientNamesRequired = <%= patientNamesRequired %>;
var patientAgeRequired = <%= patientAgeRequired %>;
var patientGenderRequired = <%= patientGenderRequired %>;
var supportEducation = <%= FormFields.getInstance().useField(Field.PatientEducation) %>;
var supportPatientNationality = <%= FormFields.getInstance().useField(Field.PatientNationality) %>;
var supportMaritialStatus = <%= FormFields.getInstance().useField(Field.PatientMarriageStatus) %>;
var supportHealthRegion = <%= FormFields.getInstance().useField(Field.PatientHealthRegion) %>;
var supportHealthDistrict = <%= FormFields.getInstance().useField(Field.PatientHealthDistrict) %>;
var supportHealthCenters = <%= supportHealthCenters %>;
var supportPrimaryRelative = <%= supportPrimaryRelative %>;

var pt_invalidElements = [];
var pt_requiredFields = new Array( );
if( patientAgeRequired){
	pt_requiredFields.push("dateOfBirthID");
}
if( patientGenderRequired){
	pt_requiredFields.push("genderID");
}
if( patientNamesRequired){
	pt_requiredFields.push("firstNameID"); 
	pt_requiredFields.push("lastNameID"); 
}

var pt_requiredOneOfFields = new Array();

if( patientIDRequired){
    if(supportNationalID){
	    pt_requiredOneOfFields.push("nationalID") ;
    }
	if (supportSTNumber) {
		pt_requiredOneOfFields.push("ST_ID");
	} else if (supportSubjectNumber){
		pt_requiredOneOfFields = new Array("subjectNumberID");
	}
    if(supportHealthCenters) {
        pt_requiredFields.push("healthCenterName");
    }
}

var updateStatus = "add";
var patientInfoChangeListeners = new Array();
var dirty = false;

function  /*bool*/ pt_isFieldValid(fieldname)
{
	return pt_invalidElements.indexOf(fieldname) == -1;
}


function  /*void*/ pt_setFieldInvalid(field)
{
	if( pt_invalidElements.indexOf(field) == -1 )
	{
		pt_invalidElements.push(field);
	}
}

function  /*void*/ pt_setFieldValid(field)
{
	var removeIndex = pt_invalidElements.indexOf( field );
	if( removeIndex != -1 )
	{
		for( var i = removeIndex + 1; i < pt_invalidElements.length; i++ )
		{
			pt_invalidElements[i - 1] = pt_invalidElements[i];
		}

		pt_invalidElements.length--;
	}
}

function  /*void*/ pt_setFieldValidity( valid, fieldName ){
	if( valid ){
		pt_setFieldValid( fieldName );
	}else{
		pt_setFieldInvalid( fieldName );
	}
}

function /*boolean*/ patientFormValid(){
	if ( patientRequired || !pt_patientRequiredFieldsAllEmpty()) {
		return pt_invalidElements.length == 0 && pt_requiredFieldsValid();
	} else {
		return true;
	}
}

function pt_patientRequiredFieldsAllEmpty() {
	
	for( var i = 0; i < pt_requiredFields.length; ++i ){
		if( !$(pt_requiredFields[i]).value.blank() ){
			return false;
		}
	}
	
	for( var i = 0; i < pt_requiredOneOfFields.length; ++i ){
		if( !($(pt_requiredOneOfFields[i]).value.blank()) ){
			return false;
		}
	}
	return true;
}

function /*void*/ pt_setSave()
{
	if( window.setSave ){
		setSave();
	}else{
		$("saveButtonId").disabled = !patientFormValid();
	}
}

function /*boolean*/ pt_isSaveEnabled()
{
	return !$("saveButtonId").disabled;
}

function  /*void*/ checkPhoneNumber( phone )
{
	var regEx = new RegExp("^\\(?\\d{3}\\)?\\s?\\d{4}-?\\d{4}$");
	var valid = regEx.test(phone.value);

	setValidIndicaterOnField( valid, phone.name );
	pt_setFieldValidity( valid, phone.name );
	pt_setSave();
}


function  /*void*/ setMyCancelAction(form, action, validate, parameters)
{

	//first turn off any further validation
	setAction(window.document.forms[0], 'Cancel', 'no', '');
}

function  /*void*/ pt_requiredFieldsValid(){

	for( var i = 0; i < pt_requiredFields.length; ++i ){
		if( $(pt_requiredFields[i]).value.blank() ){
			return false;
		}
	}

	if( pt_requiredOneOfFields.length == 0){
		return true;
	}

	for( var i = 0; i < pt_requiredOneOfFields.length; ++i ){
		if( !($(pt_requiredOneOfFields[i]).value.blank()) ){
			return true;
		}
	}

	return false;
}

function  /*string*/ pt_requiredFieldsValidMessage()
{
	var hasError = false;
	var returnMessage = "";
	var oneOfMembers = "";
	var requiredField = "";

	for( var i = 0; i < pt_requiredFields.length; ++i ){
		if( $(pt_requiredFields[i]).value.blank() ){
			hasError = true;
			requiredField += " : " + pt_requiredFields[i];
		}
	}

	for( var i = 0; i < pt_requiredOneOfFields.length; ++i ){
		if( !pt_requiredOneOfFields[i].value.blank() ){
			oneOfFound = true;
			break;
		}

		oneOfMemebers += " : " + pt_requiredOneOfFields[i];
	}

	if( !oneOFound ){
		hasError = true;
	}

	if( hasError )
	{
		if( !requiredField.blank() ){
			returnMessage = "Please enter the following patient values  " + requiredField;
		}
		if( !oneOfMembers.blank() ){
			returnMessage = "One of the following must have a value " + onOfMemebers;
		}
	}else{
		returnMessage = "valid";
	}

	return returnMessage;
}


function  /*void*/ handleBirthDateChange( dateElement, checkForValidDate )
{

	if(!checkForValidDate ){
		updatePatientAge( dateElement );
	}else{
		 checkValidAgeDate( dateElement );
	}
}

function  /*void*/ processValidateDateSuccess(xhr){

    //alert(xhr.responseText);
	var message = xhr.responseXML.getElementsByTagName("message").item(0).firstChild.nodeValue;
	var formField = xhr.responseXML.getElementsByTagName("formfield").item(0).firstChild.nodeValue;

	var isValid = message == "<%=IActionConstants.VALID%>";

	setValidIndicaterOnField(isValid, formField);
	pt_setFieldValidity( isValid, formField );


	if( isValid ){
		updatePatientAge( $("dateOfBirthID") );
	}else if( message == "<%=IActionConstants.INVALID_TO_LARGE%>" ){
		alert( '<bean:message key="error.date.birthInPast" />' );
	}
	
	pt_setSave();
}

function  /*void*/ checkValidAgeDate(dateElement)
{
	if( dateElement && !dateElement.value.blank() ){
		isValidDate( dateElement.value, processValidateDateSuccess, dateElement.name, "past" );
	}else{
		setValidIndicaterOnField(dateElement.value.blank(), dateElement.name);
	    pt_setFieldValidity( dateElement.value.blank(),  dateElement.name);
		pt_setSave();
		$("age").value = null;
	}
}


function  /*void*/ updatePatientAge( DOB )
{
	var date = new String( DOB.value );

	var datePattern = '<%=SystemConfiguration.getInstance().getPatternForDateLocale() %>';
	var splitPattern = datePattern.split("/");
	var dayIndex = 0;
	var monthIndex = 1;
	var yearIndex = 2;

	for( var i = 0; i < 3; i++ ){
		if(splitPattern[i] == "DD"){
			dayIndex = i;
		}else if(splitPattern[i] == "MM" ){
			monthIndex = i;
		}else if(splitPattern[i] == "YYYY" ){
			yearIndex = i;
		}
	}


	var splitDOB = date.split("/");
	var monthDOB = splitDOB[monthIndex];
	var dayDOB = splitDOB[dayIndex];
	var yearDOB = splitDOB[yearIndex];

	var today = new Date();

	var adjustment = 0;

	if( !monthDOB.match( /^\d+$/ ) ){
		monthDOB = "01";
	}

	if( !dayDOB.match( /^\d+$/ ) ){
		dayDOB = "01";
	}

	//months start at 0, January is month 0
	var monthToday = today.getMonth() + 1;

	if( monthToday < monthDOB ||
	    (monthToday == monthDOB && today.getDate() < dayDOB  ))
	    {
	    	adjustment = -1;
	    }

	var calculatedAge = today.getFullYear() - yearDOB + adjustment;

	var age = document.getElementById("age");
	age.value = calculatedAge;
}

function /*void*/ handleAgeChange( age )
{
	if( pt_checkValidAge( age ) )
	{
		pt_updateDOB( age );
		setValidIndicaterOnField( true, $("dateOfBirthID").name);
		pt_setFieldValid( $("dateOfBirthID").name );
	}

	pt_setSave();
}

function  /*bool*/ pt_checkValidAge( age )
{
	var valid = age.value.blank();

	if( !valid ){
		var regEx = new RegExp("^\\s*\\d{1,2}\\s*$");
	 	valid =  regEx.test(age.value);
	}

	setValidIndicaterOnField(  valid , age.name );
	pt_setFieldValidity( valid, age.name );

	return valid;
}

function  /*void*/ pt_updateDOB( age )
{
	if( age.value.blank() ){
		$("dateOfBirthID").value = null;
	}else{
		var today = new Date();

		var day = "xx";
		var month = "xx";
		var year = today.getFullYear() - age.value;

		var datePattern = '<%=SystemConfiguration.getInstance().getPatternForDateLocale() %>';
		var splitPattern = datePattern.split("/");

		var DOB = "";

		for( var i = 0; i < 3; i++ ){
			if(splitPattern[i] == "DD"){
				DOB = DOB + day + "/";
			}else if(splitPattern[i] == "MM" ){
				DOB = DOB + month + "/";
			}else if(splitPattern[i] == "YYYY" ){
				DOB = DOB + year + "/";
			}
		}

		$("dateOfBirthID").value = DOB.substring(0, DOB.length - 1 );
	}
}

function  /*void*/ getDetailedPatientInfo()
{
	$("patientPK_ID").value = patientSelectID;
    jQuery(".required-message").addClass("hide");

	new Ajax.Request (
                       'ajaxQueryXML',  //url
                        {//options
                          method: 'get', //http method
                          parameters: "provider=PatientSearchPopulateProvider&personKey=" + patientSelectID,
                          onSuccess:  processSearchPopulateSuccess,
                          onFailure:  processSearchPopulateFailure
                         }
                          );
}

function  /*void*/ setUpdateStatus( newStatus )
{
	if( updateStatus != newStatus )
	{
		updateStatus = newStatus;
		document.getElementById("processingStatus").value = newStatus;
	}
}

function  /*void*/ processSearchPopulateSuccess(xhr)
{

	setUpdateStatus("noAction");
    //alert(xhr.responseText);
	var response = xhr.responseXML.getElementsByTagName("formfield").item(0);

	var nationalIDValue = getXMLValue(response, "nationalID");
	var STValue = getXMLValue(response, "ST_ID");
	var subjectNumberValue = getXMLValue(response, "subjectNumber");
	var lastNameValue = getXMLValue(response, "lastName");
	var firstNameValue = getXMLValue(response, "firstName");
	var akaValue = getXMLValue(response, "aka");
	var motherValue = getXMLValue(response, "mother");
	var motherInitialValue = getXMLValue(response, "motherInitial");
	var streetValue = getXMLValue(response, "street");
	var cityValue = getXMLValue(response, "city");
	var communeValue = getXMLValue(response, "commune");
	var dobValue = getXMLValue(response, "dob");
	var genderValue = getSelectIndexFor( "genderID", getXMLValue(response, "gender"));
	var patientTypeValue = getSelectIndexFor( "patientTypeID", getXMLValue(response, "patientType"));
	var insuranceValue = getXMLValue(response, "insurance");
	var occupationValue = getXMLValue(response, "occupation");
	var patientUpdatedValue = getXMLValue(response, "patientUpdated");
	var personUpdatedValue = getXMLValue(response, "personUpdated");
	var addressDepartment = getXMLValue( response, "addressDept" );
	var education = getSelectIndexFor( "educationID", getXMLValue(response, "education"));
	var nationality = getSelectIndexFor( "nationalityID", getXMLValue(response, "nationality"));
	var otherNationality = getXMLValue( response, "otherNationality");
	var maritialStatus = getSelectIndexFor( "maritialStatusID", getXMLValue(response, "maritialStatus"));
	var healthRegion = getSelectIndexByTextFor( "healthRegionID", getXMLValue(response, "healthRegion"));
	var healthDistrict = getXMLValue(response, "healthDistrict");
	var primaryRelative = getXMLValue(response, "primaryRelative");

    jQuery("#PatientDetail").show();

	setPatientInfo( nationalIDValue,
					STValue,
					subjectNumberValue,
					lastNameValue,
					firstNameValue,
					akaValue,
					motherValue,
					streetValue,
					cityValue,
					dobValue,
					genderValue,
					patientTypeValue,
					insuranceValue,
					occupationValue,
					patientUpdatedValue,
					personUpdatedValue,
					motherInitialValue,
					communeValue,
					addressDepartment,
					education,
					nationality,
					otherNationality,
					maritialStatus,
					healthRegion,
					healthDistrict,
                    primaryRelative);

    setPatientAddress(response);

}

function setPatientAddress(response) {
    clearDynamicAddresses();

    var forEach = Array.prototype.forEach;
    var addressLines = response.getElementsByTagName("addressline");
    function getValue(node, key) {
        var matchingNodes = node.getElementsByTagName(key);
        if(matchingNodes.length == 0) return null;
        var firstChild = matchingNodes[0].firstChild;
        return firstChild ? firstChild.nodeValue : null;
    }
    forEach.call(addressLines, function(line){
        var value = getValue(line, 'value');
        var index = getValue(line, 'index');
        if (value != null && index != null)
            jQuery('input[name="patientProperties.addressParts.addressPartForms[' + index + '].value"]')[0].value = value;
    });
}

function /*string*/ getXMLValue( response, key )
{
	var field = response.getElementsByTagName(key).item(0);

	if( field != null )
	{
		 return field.firstChild.nodeValue;
	}
	else
	{
		return undefined;
	}
}

function  /*void*/ processSearchPopulateFailure(xhr) {
		//alert(xhr.responseText); // do something nice for the user
    jQuery(".required-message").addClass("hide");

}

function  /*void*/ clearPatientInfo(){
	setPatientInfo();
    if(supportHealthCenters) {
        jQuery('#healthCenterName').children()[0].selected=true;
    }
}

function /*void*/ clearErrors(){

	for( var i = 0; i < pt_invalidElements.length; ++i ){
		setValidIndicaterOnField( true, $(pt_invalidElements[i]).name );
	}

	pt_invalidElements = [];

}

function  /*void*/ setPatientInfo(nationalID, ST_ID, subjectNumber, lastName, firstName, aka, mother, street, city, dob, gender,
		patientType, insurance, occupation, patientUpdated, personUpdated, motherInitial, commune, addressDept, educationId, nationalId, nationalOther,
		maritialStatusId, healthRegionId, healthDistrictId, primaryRelative ) {

	clearErrors();

	if ( supportNationalID) { $("nationalID").value = nationalID == undefined ? "" : nationalID; }
	if(supportSTNumber){ $("ST_ID").value = ST_ID == undefined ? "" : ST_ID; }
	if(supportSubjectNumber){ $("subjectNumberID").value = subjectNumber == undefined ? "" : subjectNumber; }
	$("lastNameID").value = lastName == undefined ? "" : lastName;
	$("firstNameID").value = firstName == undefined ? "" : firstName;
	if(supportAKA){$("akaID").value = aka == undefined ? "" : aka; }
	if(supportMothersName){$("motherID").value = mother == undefined ? "" : mother; }
	if(supportMothersInitial){$("motherInitialID").value = (motherInitial == undefined ? "" : motherInitial); }
    $("streetID").value = street == undefined ? "" : street;
	if(supportCity){$("cityID").value = city == undefined ? "" : city; }
	if(supportCommune){$("communeID").value = commune == undefined ? "" : commune; }
	if(supportInsurance){$("insuranceID").value = insurance == undefined ? "" : insurance; }
	if(supportOccupation){$("occupationID").value = occupation == undefined ? "" : occupation; }
	$("patientLastUpdated").value = patientUpdated == undefined ? "" : patientUpdated;
	$("personLastUpdated").value = personUpdated == undefined ? "" : personUpdated;
	if(supportPatientNationality){
		$("nationalityID").selectedIndex = nationalId == undefined ? 0 : nationalId; 
		$("nationalityOtherId").value = nationalOther == undefined ? "" : nationalOther;}
	if( supportEducation){ $("educationID").selectedIndex =  educationId == undefined ? 0 : educationId;}
	if( supportMaritialStatus){ $("maritialStatusID").selectedIndex = maritialStatusId == undefined ? 0 : maritialStatusId;}
	if( supportHealthRegion){ 
		var healthRegion = $("healthRegionID"); 
		healthRegion.selectedIndex = healthRegionId == undefined ? 0 : healthRegionId;
		$("shadowHealthRegion").value = healthRegion.options[healthRegion.selectedIndex].label;}
	if( supportHealthDistrict){
		if($("healthRegionID").selectedIndex != 0){
			getDistrictsForRegion( $("healthRegionID").value, healthDistrictId, healthDistrictSuccess, null);
		} 
	}
    if(supportPrimaryRelative) {
        $("primaryRelativeID").value = primaryRelative == undefined ? "" : primaryRelative;
    }

	if(supportAddressDepartment){
		var deptMessage = $("deptMessage");
		deptMessage.innerText = deptMessage.textContent = "";
		//for historic reasons, we switched from text to dropdown
		if( addressDept == undefined ){
			$("departmentID").value = 0;
		}else if( isNaN( addressDept) ){
			$("departmentID").value = 0;
			deptMessage.textContent = "<%= StringUtil.getMessageForKey("patient.address.dept.entry.msg") %>" + " " + addressDept;
		}else{
			$("departmentID").value = addressDept;
		}

	}

	if (dob == undefined) {
		$("dateOfBirthID").value = "";
		$("age").value = "";
	} else {
		var dobElement = $("dateOfBirthID");
		dobElement.value = dob;
		handleBirthDateChange(dobElement, false);
	}

	document.getElementById("genderID").selectedIndex = gender == undefined ? 0 : gender;
	if(supportPatientType){document.getElementById("patientTypeID").selectedIndex = patientType == undefined ? 0 : patientType; }

    if(supportHealthCenters) {
        splitSTNumber();
        setSTNumberFieldState(false);
    }
	// run this b/c dynamically populating the fields does not constitute an onchange event to populate the patmgmt tile
	// this is the fx called by the onchange event if manually changing the fields
	updatePatientEditStatus();

}

function patientIdChanged(){
    if(supportHealthCenters){
        validatePatientId();
    }
    updatePatientEditStatus();
}

function onHealthCenterChange() {
    $('hiddenhealthCenterName').value =  $('healthCenterName').value;
    updatePatientEditStatus();
}

function  /*void*/ updatePatientEditStatus() {
	if (updateStatus == "noAction") {
		setUpdateStatus("update");
	}

    var stNumber =  supportSTNumber ? getPatientId() : "";
	for(var i = 0; i < patientInfoChangeListeners.length; i++){
			patientInfoChangeListeners[i]($("firstNameID").value,
										  $("lastNameID").value,
										  $("genderID").value,
										  $("dateOfBirthID").value,
                                          stNumber,
										  supportSubjectNumber ? $("subjectNumberID").value : "",
										  supportNationalID ? $("nationalID").value : "",
										  supportMothersName ? $("motherID").value : null,
										  $("patientPK_ID").value);

		}

	makeDirty();

	pt_setSave();
}

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

function addPatient(){

	$("PatientDetail").show();
    jQuery(".required-message").removeClass("hide");
	clearPatientInfo();
    clearDynamicAddresses();
	clearErrors();
	if(supportSTNumber){$("ST_ID").disabled = false;}
	if(supportSubjectNumber){$("subjectNumberID").disabled = false;}
	if(supportNationalID){$("nationalID").disabled = false;}
	setUpdateStatus( "add" );

    if(supportHealthCenters) {
        setSTNumberFieldState(true);
    }

	for(var i = 0; i < patientInfoChangeListeners.length; i++){
        patientInfoChangeListeners[i]("", "", "", "", "", "", "", "", "");
    }
}

function clearDynamicAddresses(){
    jQuery('input[name^="patientProperties.addressParts"]').val("");
}

function  /*void*/ savePage()
{
    window.onbeforeunload = null; // Added to flag that formWarning alert isn't needed.
    var form = window.document.forms[0];
    if(supportHealthCenters && supportSTNumber) {
        var val = concatenateSTNumberAndHealthCenter();
        form.elements.namedItem("patientProperties.STnumber").value = val;
    }
	form.action = "PatientManagementUpdate.do";
	form.submit();
}

function /*void*/ addPatientInfoChangedListener( listener ){
	patientInfoChangeListeners.push( listener );
}

function clearDeptMessage(){
	$("deptMessage").innerText = deptMessage.textContent = "";
}

function updateHealthDistrict( regionElement){
	$("shadowHealthRegion").value = regionElement.options[regionElement.selectedIndex].text;
	getDistrictsForRegion( regionElement.value, "", healthDistrictSuccess, null);
}

function healthDistrictSuccess( xhr ){
  	//alert(xhr.responseText);

	var message = xhr.responseXML.getElementsByTagName("message").item(0).firstChild.nodeValue;
	var districts = xhr.responseXML.getElementsByTagName("formfield").item(0).childNodes[0].childNodes;
	var selected = xhr.responseXML.getElementsByTagName("formfield").item(0).childNodes[1];
	var isValid = message == "<%=IActionConstants.VALID%>";
	var healthDistrict = $("healthDistrictID");
	var i = 0;

	healthDistrict.disabled = "";
	if( isValid ){
		healthDistrict.options.length = 0;
		healthDistrict.options[0] = new Option('', '');
		for( ;i < districts.length; ++i){
			healthDistrict.options[i + 1] = new Option(districts[i].attributes.getNamedItem("value").value, districts[i].attributes.getNamedItem("value").value);
		}
	}
	
	if( selected){
		healthDistrict.selectedIndex = getSelectIndexFor( "healthDistrictID", selected.childNodes[0].nodeValue);
	}
}

jQuery(function(){
    if (<%=collapse%>)
        jQuery("#PatientDetail").hide();

});

</script>
<nested:hidden name='<%=formName%>' property="patientProperties.currentDate" styleId="currentDate"/>

<div id="PatientPage" style="display:inline"  >
	<nested:hidden property="patientProperties.patientLastUpdated" name='<%=formName%>' styleId="patientLastUpdated" />
	<nested:hidden property="patientProperties.personLastUpdated" name='<%=formName%>'  styleId="personLastUpdated"/>

	<tiles:insert attribute="patientSearch" />

	<nested:hidden name='<%=formName%>' property="patientProperties.patientProcessingStatus" styleId="processingStatus" value="add" />
	<nested:hidden name='<%=formName%>' property="patientProperties.patientPK" styleId="patientPK_ID" />
	<br/>

	<div id="addEditPatient">
	<div class="form-item">
	<html:button property="newPatient"  onclick="addPatient()" >
		<bean:message key="patient.new" />
	</html:button>
    <span class="required-message hide"><bean:message key="patient.message.newPatient"/></span>
	</div>

	<div id="PatientDetail">
	<h2><bean:message key="patient.information"/></h2>
	<table width="100%" border="0">
	<tr>
        <% if( !supportSubjectNumber){ %>
        <td>
            <bean:message key="patient.externalId"/>
            <% if( patientIDRequired){ %>
                <span class="requiredlabel">*</span>
            <% } %>
        </td>
        <%} %>
        <% if( supportSTNumber){ %>
        <td align="right">
            <bean:message key="patient.ST.number"/>:
        </td>
        <td>
            <div>
                <% if (supportHealthCenters) { %>
                    <input id="hiddenhealthCenterName" type="hidden" name="patientProperties.healthCenterName">
                    <select id="healthCenterName" onchange="onHealthCenterChange()" name="healthCenterName">
                        <option value="">Select One</option>
                    </select>
                <% } %>
                <input id="hiddenPatientProperties" name="patientProperties.STnumber" type="hidden">
                <input type="text" id="ST_ID" onchange="patientIdChanged();">
            </div>
        </td>
        <%} %>
    </tr>
    <% if( supportSubjectNumber){ %>
    <tr >
        <td width="5%">&nbsp;

        </td>
        <td>&nbsp;

        </td>
        <td align="right">
            <bean:message key="patient.subject.number"/>:
            <span class="requiredlabel">*</span>
        </td>
        <td>
            <nested:text name='<%=formName%>'
                      property="patientProperties.subjectNumber"
                      onchange="updatePatientEditStatus();"
                      styleId="subjectNumberID"
                      styleClass="text"
                      size="60" />
        </td>
    </tr>
    <% } %>

    <% if( supportNationalID ){ %>
    <tr class="nationalID">
        <td width="5%">&nbsp;

        </td>


        <td  width="25%" align="right">
            <%=StringUtil.getContextualMessageForKey("patient.NationalID") %>:

        </td>
        <td width="25%">
            <nested:text name='<%=formName%>'
                      property="patientProperties.nationalId"
                      onchange="updatePatientEditStatus();"
                      styleId="nationalID"
                      styleClass="text"
                      size="60"/>
        </td>
        <td width="10%">&nbsp;

        </td>
        <td width="15%">&nbsp;

        </td>
    </tr>

	<tr class="empty-row"><td colspan="2">&nbsp;</td></tr>

    <%} %>
	<tr>
		<td width="15%">
			<bean:message key="patient.name" />
		</td>
		 <% if(supportfirstNameFirst) { %>

		<td class="firstNameLabel" width="10%" align="right">
			<bean:message key="patient.epiFirstName" />
			:
			<% if( patientNamesRequired){ %>
				<span class="requiredlabel">*</span>
			<% } %>	
		</td>
		<td class="firstName" width="20%">
			<nested:text name='<%=formName%>'
					  property="patientProperties.firstName"
					  styleClass="text"
					  size="60"
					  onchange="updatePatientEditStatus();"
					  styleId="firstNameID"/>
		</td>
		<td class="lastNameLabel" align="right" width="15%">
        			<bean:message key="patient.epiLastName" />
        			:
        			<% if( patientNamesRequired){ %>
        				<span class="requiredlabel">*</span>
        			<% } %>
        		</td>
        		<td class="lastName">
        			<nested:text name='<%=formName%>'
        					  property="patientProperties.lastName"
        					  styleClass="text"
        				      size="40"
        				      onchange="updatePatientEditStatus();"
        				      styleId="lastNameID"/>
        		</td>
		 <% } else { %>
		  <td class="lastNameLabel" align="right" width="15%">
          			<bean:message key="patient.epiLastName" />
          			:
          			<% if( patientNamesRequired){ %>
          				<span class="requiredlabel">*</span>
          			<% } %>
          		</td>
          		<td class="lastName" width="20%">
          			<nested:text name='<%=formName%>'
          					  property="patientProperties.lastName"
          					  styleClass="text"
          				      size="60"
          				      onchange="updatePatientEditStatus();"
          				      styleId="lastNameID"/>
          		</td>
          		<td class="firstNameLabel" width="10%" align="right">
          			<bean:message key="patient.epiFirstName" />
          			:
          			<% if( patientNamesRequired){ %>
          				<span class="requiredlabel">*</span>
          			<% } %>
          		</td>
          		<td class="firstName">
          			<nested:text name='<%=formName%>'
          					  property="patientProperties.firstName"
          					  styleClass="text"
          					  size="40"
          					  onchange="updatePatientEditStatus();"
          					  styleId="firstNameID"/>
          		</td>
           <% } %>

	</tr>
	<% if(supportAKA){ %>
	<tr class="akaID">
	<td></td>
	<td  align="right">
		<bean:message key="patient.aka"/>
	</td>
	<td>
		<nested:text name='<%=formName%>'
				  property="patientProperties.aka"
				  onchange="updatePatientEditStatus();"
				  styleId="akaID"
				  styleClass="text"
				  size="60" />
	</td>
	</tr>
	<% } %>
	<% if( supportMothersName ){ %>
	<tr>
		<td></td>
		<td align="right">
			<bean:message key="patient.mother.name"/>
		</td>
		<td>
			<nested:text name='<%=formName%>'
					  property="patientProperties.mothersName"
					  onchange="updatePatientEditStatus();"
				  	  styleId="motherID"
				  	  styleClass="text"
				      size="60" />
		</td>
	</tr>
    <% } if( supportPrimaryRelative ){ %>
    <tr>
        <td></td>
        <td align="right">
            <bean:message key="patient.primaryRelative"/>:
        </td>
        <td>
            <nested:text name='<%=formName%>'
                         property="patientProperties.primaryRelative"
                         onchange="updatePatientEditStatus();"
                         styleId="primaryRelativeID"
                         styleClass="text"
                         size="60" />
        </td>
    </tr>
	<% } if(supportMothersInitial){ %>
	<tr>
		<td></td>
		<td align="right">
			<bean:message key="patient.mother.initial"/>
		</td>
		<td>
			<nested:text name='<%=formName%>'
					  property="patientProperties.mothersInitial"
					  onchange="updatePatientEditStatus();"
				  	  styleId="motherInitialID"
				  	  styleClass="text"
				      size="1"
				      maxlength="1" />
		</td>
	</tr>
	<%} %>

    <%  if(!supportDynamicAddresses){ %>
    <tr ><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td >
			<bean:message  key="person.streetAddress" />
		</td>
		<td align="right">
			<bean:message  key="person.streetAddress.street" />:
		</td>
		<td>
			<nested:text name='<%=formName%>'
					  property="patientProperties.streetAddress"
					  onchange="updatePatientEditStatus();"
					  styleId="streetID"
					  styleClass="text"
					  size="70" />
		</td>
	</tr>
    <%} else {%>
        <input type="hidden" name="patientProperties.streetAddress" id="streetID"  value=""/>
    <% } %>

    <tr ><td colspan="2">&nbsp;</td></tr>


    <%  if(supportDynamicAddresses){ %>
    <tr>
        <td>
            <bean:message  key="person.streetAddress" />
        </td>
    	<logic:iterate name="<%=formName%>" property="patientProperties.addressParts.addressPartForms"
                   id="addressPart"
                   indexId="index">
            <td  align="right">
                <bean:message  name="addressPart" property="nameKey"/>
            </td>
            <td>
                <input name="patientProperties.addressParts.addressPartForms[<bean:write name='index'/>].value" value="<%= ((AddressPartForm)addressPart).getValue()  %>" size="60"/>
            </td>
        </tr>
        <tr>
            <td></td>
    </logic:iterate>
        </tr>
    <% } %>

	<% if( FormFields.getInstance().useField(Field.AddressVillage)) { %>
	<tr>
		<td></td>
		<td align="right">
		    <%= StringUtil.getContextualMessageForKey("person.town") %>:
		</td>
		<td>
			<nested:text name='<%=formName%>'
					  property="patientProperties.city"
					  onchange="updatePatientEditStatus();"
					  styleId="cityID"
					  styleClass="text"
					  size="30" />
		</td>
	</tr>
	<% } %>
	<% if( supportCommune){ %>
	<tr>
		<td></td>
		<td align="right">
			<bean:message  key="person.commune" />:
		</td>
		<td>
			<nested:text name='<%=formName%>'
					  property="patientProperties.commune"
					  onchange="updatePatientEditStatus();"
					  styleId="communeID"
					  styleClass="text"
					  size="30" />
		</td>
	</tr>
	<% } %>
	<% if( supportAddressDepartment){ %>
	<tr>
		<td></td>
		<td align="right">
			<bean:message  key="person.department" />:
		</td>
		<td>
			<html:select name='<%=formName%>'
						 property="patientProperties.addressDepartment"
						 onchange="updatePatientEditStatus();clearDeptMessage();"
					     styleId="departmentID" >
			<option value="0" ></option>
			<html:optionsCollection name="<%=formName %>" property="patientProperties.addressDepartments" label="dictEntry" value="id" />
			</html:select><br>
			<span id="deptMessage"></span>
		</td>
	</tr>
	<% } %>
	<% if( FormFields.getInstance().useField(Field.PatientPhone)){ %>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><%= StringUtil.getContextualMessageForKey("person.phone") %>:</td>
			<td>
				<html:text name='<%= formName %>' property="patientProperties.phone" />
			</td>
		</tr>
	<% } %>


	<tr ><td >&nbsp;</td></tr>
	<% if( FormFields.getInstance().useField(Field.PatientHealthRegion)){ %>
	<tr>
	<td>&nbsp;</td>
	<td align="right"><bean:message key="person.health.region"/>: </td>
		<td>
			<nested:hidden name='<%=formName%>' property="patientProperties.healthRegion" styleId="shadowHealthRegion" />
			<html:select name='<%=formName%>'
						 property="patientProperties.healthRegion"
						 onchange="updateHealthDistrict( this );"
					     styleId="healthRegionID" >
			<option value="0" ></option>
			<html:optionsCollection name="<%=formName %>" property="patientProperties.healthRegions" label="value" value="id" />
			</html:select>
		</td>	
	</tr>		
	<% } %>
	<% if( FormFields.getInstance().useField(Field.PatientHealthDistrict)){ %>
	<tr>
	<td>&nbsp;</td>
	<td align="right"><bean:message key="person.health.district"/>: </td>
		<td>
			<html:select name='<%=formName%>'
						 property="patientProperties.healthDistrict"
					     styleId="healthDistrictID"
					     disabled="true" >
			<option value="0" ></option>

			</html:select>
		</td>	
	</tr>		
	<% } %>
	</table>

	<table>
	<tr>
		<td align="right">
			<bean:message key="patient.birthDate" />&nbsp;<bean:message key="sample.date.format"/>:
			<% if(patientAgeRequired){ %>
				<span class="requiredlabel">*</span>
			<% } %>
		</td>
		<td>
			<nested:text name='<%=formName%>'
					  property="patientProperties.birthDateForDisplay"
					  styleClass="text"
					  size="20"
					  onchange="handleBirthDateChange( this, true ); updatePatientEditStatus();"
					  styleId="dateOfBirthID" />
			<div id="patientProperties.birthDateForDisplayMessage" class="blank" ></div>
		</td>
		<td align="right">
			<bean:message  key="patient.age" />:
		</td>
		<td >
			<INPUT type=text name=age id=age size="3" class="text"
				   onchange="handleAgeChange( this ); updatePatientEditStatus();"
				   maxlength="3" />
			<div id="ageMessage" class="blank" ></div>
		</td>
		<td align="right">
			<bean:message  key="patient.gender" />:
			<% if(patientGenderRequired){ %>
				<span class="requiredlabel">*</span>
			<% } %>
		</td>
		<td>
			<nested:select name='<%=formName%>'
						 property="patientProperties.gender"
						 onchange="updatePatientEditStatus();"
						 styleId="genderID">
				<option value=" " ></option>
				<nested:optionsCollection name='<%=formName%>' property="patientProperties.genders"   label="value" value="id" />
			</nested:select>
		</td>
	</tr>
	<% if( supportInsurance || supportPatientType){ %>
	<tr>
		<% if( supportPatientType ){ %>
		<td align="right">
			<bean:message  key="patienttype.type" />:
		</td>
		<td>
			<nested:select name='<%=formName%>'
						 property="patientProperties.patientType"
						 onchange="updatePatientEditStatus();"
						 styleId="patientTypeID"  >
				<option value="0" ></option>
				<nested:optionsCollection name='<%=formName%>' property="patientProperties.patientTypes" label="description" value="type" />
			</nested:select>
		</td>
		<% } if( supportInsurance ){ %>
		<td class="insuranceID" align="right">
			<bean:message  key="patient.insuranceNumber" />:
		</td>
		<td>
			<nested:text name='<%=formName%>'
					  property="patientProperties.insuranceNumber"
					  onchange="updatePatientEditStatus();"
					  styleId="insuranceID"
					  styleClass="text"
					  size="20" />
		</td>
		<td align="right">
		</td>
		<% } %>

	</tr>
	<% } if( supportOccupation ){ %>
	<tr>
	<td align="right">
		<bean:message  key="patient.occupation" />:
	</td>
	<td>
		<nested:text name='<%=formName%>'
				  property="patientProperties.occupation"
				  onchange="updatePatientEditStatus();"
				  styleId="occupationID"
				  styleClass="text"
				  size="20" />
	</td>
	</tr>
	<% } %>
	<% if( FormFields.getInstance().useField(Field.PatientEducation)){ %>
		<tr>
			<td align="right"><bean:message key="patient.education"/>: </td>
				<td>
					<html:select name='<%=formName%>'
								 property="patientProperties.education"
							     styleId="educationID" >
					<option value="0" ></option>
					<html:optionsCollection name="<%=formName %>" property="patientProperties.educationList" label="value" value="value" />
					</html:select>
				</td>	
			</tr>	
	<% } %>
	<% if( FormFields.getInstance().useField(Field.PatientMarriageStatus)){ %>
		<tr>
			<td align="right"><bean:message key="patient.maritialStatus"/>: </td>
				<td>
					<html:select name='<%=formName%>'
								 property="patientProperties.maritialStatus"
							     styleId="maritialStatusID" >
					<option value="0" ></option>
					<html:optionsCollection name="<%=formName %>" property="patientProperties.maritialList" label="value" value="value" />
					</html:select>
				</td>	
			</tr>	
	<% } %>
	<% if( FormFields.getInstance().useField(Field.PatientNationality)){ %>
		<tr>
			<td align="right"><bean:message key="patient.nationality"/>: </td>
				<td>
					<html:select name='<%=formName%>'
								 property="patientProperties.nationality"
							     styleId="nationalityID" >
					<option value="0" ></option>
					<html:optionsCollection name="<%=formName %>" property="patientProperties.nationalityList" label="value" value="value" />
					</html:select>
				</td>
				<td><bean:message key="specify"/>:</td>
				<td>
					<html:text name='<%=formName %>'  property="patientProperties.otherNationality" styleId="nationalityOtherId" />
				</td>	
			</tr>	
	<% } %>
	</table>
	</div>
	</div>
</div>

<script type="text/javascript" >

//overrides method of same name in patientSearch
function selectedPatientChangedForManagement(firstName, lastName, gender, DOB, stNumber, subjectNumber, nationalID, mother, pk ){
	if( pk ){
		getDetailedPatientInfo();
		$("patientPK_ID").value = pk;
	}else{
		clearPatientInfo();
		setUpdateStatus("add");
	}
}

var registered = false;

function registerPatientChangedForManagement(){
	if( !registered ){
		addPatientChangedListener( selectedPatientChangedForManagement );
		registered = true;
	}
}

function pageOnLoad() {
    if(supportHealthCenters) {
        populateHealthCenter();
        $("ST_ID").value = "";
    }
}

function validatePatientId(){
    var patientId = $("ST_ID").value;
    if(patientId){
        if(patientId.search(/\D/) !== -1 ){
            alert("Only numbers allowed in the PatientID");
            $("ST_ID").value = "";
            makeDirty();
        }
    }
}

function populateHealthCenter() {

    var url = "/HealthCenterList.do?allHealthCenter=false";
    if(<%= allHealthCenter %>){
        url = "/HealthCenterList.do?allHealthCenter=true";
    }
    new Ajax.Request('<%= request.getContextPath() %>' + url, {
        method: 'get',
        onSuccess:  function(object) {
            var selectObj = jQuery("#healthCenterName")[0];
            JSON.parse(object.responseText).forEach(function(healthCenter){
                selectObj.add(new Option(healthCenter, healthCenter));
            });
        },
        onFailure:  function(data) {console.log(data)}
    });
}

function splitSTNumber() {
    var stNumber = $('ST_ID').value;
    if(stNumber && !stNumber.blank()) {
        var match = stNumber.match(/([a-zA-Z]*)(\d+)/);
        $('healthCenterName').value = match[1];
        $('hiddenhealthCenterName').value =  $('healthCenterName').value;
        $('ST_ID').value = match[2];
    }
}

function setSTNumberFieldState(state){
    if(state){
        $('ST_ID').removeAttribute('readonly');
        $('healthCenterName').disabled = false;
    }
    else{
        jQuery('#ST_ID').attr('readonly', 'true');
        jQuery('#healthCenterName').attr('disabled', 'true');
    }
}

function getPatientId(){
    if(supportHealthCenters){
        return concatenateSTNumberAndHealthCenter();
    }
    return $("ST_ID").value ;
}

function concatenateSTNumberAndHealthCenter() {
    return  $('healthCenterName').value + $('ST_ID').value;
}
registerPatientChangedForManagement();
</script>

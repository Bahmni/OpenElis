<%@ page language="java" contentType="text/html; charset=utf-8"
	import="us.mn.state.health.lims.common.action.IActionConstants,
			us.mn.state.health.lims.common.formfields.FormFields,
			us.mn.state.health.lims.common.formfields.FormFields.Field,
			us.mn.state.health.lims.common.provider.validation.AccessionNumberValidatorFactory,
			us.mn.state.health.lims.common.provider.validation.IAccessionNumberValidator,
			us.mn.state.health.lims.sample.bean.SampleConfirmationItem,
			us.mn.state.health.lims.sample.bean.SampleConfirmationTest,
			us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample,
			us.mn.state.health.lims.common.util.IdValuePair,
            us.mn.state.health.lims.common.util.Versioning,
			us.mn.state.health.lims.common.util.StringUtil"%>

<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/labdev-view" prefix="app"%>
<%@ taglib uri="/tags/struts-tiles"     prefix="tiles" %>
<%@ taglib uri="/tags/sourceforge-ajax" prefix="ajax"%>


<bean:define id="formName" 	value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>' />



<%!String path = "";
	String basePath = "";
	IAccessionNumberValidator accessionNumberValidator;
	boolean useSTNumber = true;
	boolean useMothersName = true;
	boolean useRequesterSiteList = false;
	boolean useProviderInfo = false;
	boolean useInitialSampleCondition = false;
	boolean patientRequired = true;
%>
<%
	path = request.getContextPath();
	basePath = path + "/";

	accessionNumberValidator = new AccessionNumberValidatorFactory().getValidator();

	useSTNumber =  FormFields.getInstance().useField(Field.StNumber);
	useMothersName = FormFields.getInstance().useField(Field.MothersName);
	useRequesterSiteList = FormFields.getInstance().useField(Field.RequesterSiteList);
	useProviderInfo = FormFields.getInstance().useField(Field.ProviderInfo);
	useInitialSampleCondition = FormFields.getInstance().useField(Field.InitialSampleCondition);
	patientRequired = FormFields.getInstance().useField(Field.PatientRequired_SampleConfirmation);
%>

<link rel="stylesheet" type="text/css" href="css/jquery.asmselect.css?ver=<%= Versioning.getBuildNumber() %>" />
<link rel="stylesheet" href="css/jquery_ui/jquery.ui.all.css?ver=<%= Versioning.getBuildNumber() %>">
<link rel="stylesheet" href="css/customAutocomplete.css?ver=<%= Versioning.getBuildNumber() %>">

<script type="text/javascript" src="<%=basePath%>scripts/utilities.jsp"></script>
<script type="text/javascript" src="<%=basePath%>scripts/utilities.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="scripts/jquery.asmselect.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="scripts/ajaxCalls.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/jquery.asmselect.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/ui/jquery.ui.core.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/ui/jquery.ui.widget.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/ui/jquery.ui.button.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/ui/jquery.ui.position.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/ui/jquery.ui.autocomplete.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script src="scripts/customAutocomplete.js?ver=<%= Versioning.getBuildNumber() %>"></script>

<script type="text/javascript" >


var useSTNumber = <%= useSTNumber %>;
var useMothersName = <%= useMothersName %>;
var useInitialSampleCondition = <%= useInitialSampleCondition %>;
var dirty = false;

var currentRequestSampleIndex;


$jq(document).ready( function() {
			//fieldValidator declared in utilities.js
			fieldValidator.addRequiredField( "labNo" );
			fieldValidator.addRequiredField( "receivedDate" );
			fieldValidator.setFieldValidity(false, "requestedTests_0");

			$jq("select[multiple]").asmSelect({
					removeLabel: "X"
				});

			$jq("select[multiple]").change(function(e, data) {
		//		handleMultiSelectChange( e, data );
				});

            //dropdown defined in customAutocomplete.js
			autoCompId = 'siteId'; //needs to be set before the dropdown is created N.B. shouuld be passed in as arg
			var dropdown = $jq( "select#orgRequesterId" );
            autoCompleteWidth = dropdown.width() + 66 + 'px';
            clearNonMatching = false;
			capitialize = true;
            dropdown.combobox();
            invalidLabID = '<bean:message key="error.site.invalid"/>'; // Alert if value is typed that's not on list. FIXME - add badmessage icon
            maxRepMsg = '<bean:message key="sample.entry.project.siteMaxMsg"/>'; 
          

			//$jq( "select#orgRequesterId" ).show();

			//resultCallBack defined in customAutocomplete.js
			resultCallBack = function( ) {
  				getRequestersForOrg( );
  				makeDirty();
  				setSaveButton();
				};

			});

function handleMultiSelectChange( e, data ){
	var id = "#multi" + e.target.id;
	var selection = $jq(id)[0];

	if( data.type == "add"){
		appendValueToElementValue( selection, data.value );
	}else{
		var splitValues =  selection.value.split(",");
		selection.value = "";

		for( var i = 0; i < splitValues.length; i++ ){
			if( splitValues[i] != data.value ){
				appendValueToElementValue( selection, splitValues[i] );
			}
		}
	}
}

function appendValueToElementValue( elem, addString ){
	if( elem.val() && elem.val().length > 1 ){
			elem.val( elem.val() + ',');
		}

	elem.val( elem.val() + addString);
}

function checkAccessionNumber( accessionNumber )
{

	//check if empty
	if ( !fieldIsEmptyById( accessionNumber.id ) )
	{
		validateAccessionNumberOnServer(true, accessionNumber.id, accessionNumber.value, processAccessionSuccess, processAccessionFailure);
	}
	else
	{
		fieldValidator.setFieldValidity( false, accessionNumber.id);
		setFieldErrorDisplay( accessionNumber);
		setSaveButton();
	}
}

function processAccessionSuccess(xhr)
{
	//alert(xhr.responseText);
	var formField = xhr.responseXML.getElementsByTagName("formfield").item(0);
	var message = xhr.responseXML.getElementsByTagName("message").item(0);
	var success = false;
	var labElement;
	var messageValue = message.firstChild.nodeValue;
	var	success = messageValue == "SAMPLE_NOT_FOUND" || messageValue == "valid";  
	var labElement = $(formField.firstChild.nodeValue);
	
	selectFieldErrorDisplay( success, labElement);
	
	if( !success ){
		if(messageValue == "SAMPLE_FOUND"){
			alert("<%= StringUtil.getMessageForKey("sample.entry.invalid.accession.number.used") %>");
		}else{
			alert( message.firstChild.nodeValue );
		}
	}

	setSaveButton();
}

function processAccessionFailure(xhr)
{
	//unhandled error: someday we should be nicer to the user
}


function getNextAccessionNumber() {
	generateNextScanNumber();
}

function generateNextScanNumber(){
	new Ajax.Request (
                          'ajaxQueryXML',  //url
                           {//options
                             method: 'get', //http method
                             parameters: "provider=SampleEntryGenerateScanProvider",
                             //indicator: 'throbbing'
                             onSuccess:  processScanSuccess,
                             onFailure:  processScanFailure
                           }
                          );
}

function processScanSuccess(xhr){
	var formField = xhr.responseXML.getElementsByTagName("formfield").item(0);
	var returnedData = formField.firstChild.nodeValue;

	var message = xhr.responseXML.getElementsByTagName("message").item(0);
	var success = message.firstChild.nodeValue == "valid";
	var target = $("labNo");
	
	$("labNo").value = success ? returnedData : "";

	selectFieldErrorDisplay( success, target);
	fieldValidator.setFieldValidity( success, target.id);
	
	setSaveButton();
}

function processScanFailure(xhr){
	//some user friendly response needs to be given to the user
}

var requesterInfoHash = {};

function getRequestersForOrg( ){
    var orgEnteredValue = $("siteId").value,
    	orgSelectList = $("orgRequesterId").options,
    	orgKey = 0,
    	searchCount = 0;
    	
	for( searchCount; searchCount < orgSelectList.length; searchCount++){
		if( orgSelectList[searchCount].text == orgEnteredValue){
			orgKey = orgSelectList[searchCount].value;
			break;
		}
	}    

	if( orgKey == 0 ){ //if no match with site list
		var requesterList = $("personRequesterId");
		requesterList.options.length = 0;
		addOptionToSelect( requesterList, '<%=StringUtil.getMessageForKey("sample.entry.requester.new")%>' , "0" );
	}else{
		new Ajax.Request (
                          'ajaxQueryXML',  //url
                           {//options
                             method: 'get', //http method
                             parameters: "provider=RequestersForOrganizationProvider&orgId=" + orgKey,
                             //indicator: 'throbbing'
                             onSuccess:  processRequestersSuccess,
                             onFailure:  null
                           }
                          );
	}
}

function processRequestersSuccess(xhr){
	//alert(xhr.responseText);

	var requesters = xhr.responseXML.getElementsByTagName("requester");
	var requesterList = $("personRequesterId");
	requesterList.options.length = 0;

	if( requesters.length == 0 ){
		addOptionToSelect( requesterList, '<%=StringUtil.getMessageForKey("sample.entry.requester.new")%>' , "0" );
	}else{
		requesterInfoHash = {};
		addOptionToSelect( requesterList, '' , '' );
		for(var i = 0; i < requesters.length; ++i ){
			addRequester(requesterList, requesters[i] );
		}
		addOptionToSelect( requesterList, '<%=StringUtil.getMessageForKey("sample.entry.requester.new")%>' , "0" );
	}
}

function addRequester(requesterList, requesterXml ){
	addOptionToSelect( requesterList,
	                   requesterXml.getAttribute("lastName") + ', ' + requesterXml.getAttribute("firstName") ,
	                   requesterXml.getAttribute("id") );

	requesterInfoHash[requesterXml.getAttribute("id")] = {'firstName': requesterXml.getAttribute("firstName"),
														  'lastName': requesterXml.getAttribute("lastName"),
														  'phone': requesterXml.getAttribute("phone"),
														  'fax': requesterXml.getAttribute("fax"),
														  'email': requesterXml.getAttribute("email")};
}

function populateRequesterDetail(requesterSelector){
	var requesterId = requesterSelector[requesterSelector.selectedIndex].value;

	var details = requesterInfoHash[requesterId];

	if( details ){
		setRequesterDetails( details );
	}else{
		setRequesterDetails( {'firstName': '', 'lastName': '', 'phone': '', 'fax': '', 'email': ''}  );
	}
}

function setRequesterDetails( details ){
	$("requesterFirstName").value = details["firstName"];
	$("requesterLastName").value = details["lastName"];
	$("requesterPhone").value = details["phone"];
	$("requesterFax").value = details["fax"];
	$("requesterEMail").value = details["email"];
}

function populateRequestForSampleType( selector, sampleIndex){
	var selectIndex = selector.selectedIndex;
	var selection;

	fieldValidator.setFieldValidity( false, "requestedTests_" + sampleIndex );

	if( selectIndex == 0 ){
		var requestingTests = $$(".requestingTests_" + sampleIndex );
		var requestedTestTable = $("requestedTests_" + sampleIndex  );
		enableDisableAndClearRequestingTests(requestedTestTable, sampleIndex, requestingTests, true );
	}else{
		selection = selector.options[selectIndex];
		currentRequestSampleIndex = sampleIndex;
		getTestsForSampleType(selection.value, "none", processGetTestSuccess, processGetTestFailure);
	}

	setSaveButton();
}


function processGetTestSuccess(xhr){
   	//alert(xhr.responseText);
   	var response = xhr.responseXML.getElementsByTagName("formfield").item(0);
   	var tests = response.getElementsByTagName("test");
   	var test;
   	var length = tests.length;

   	var requestingTests = $jq(".requestingTests_" + currentRequestSampleIndex );
   	var requestedTestTable = $("requestedTests_" + currentRequestSampleIndex  );

	enableDisableAndClearRequestingTests(requestedTestTable, currentRequestSampleIndex, requestingTests, length == 0 );


   	for( var i = 0; i < length; ++i){
   		test = tests[i];
		var name = getValueFromXmlElement( test, "name" );
		var id = getValueFromXmlElement( test, "id" );
		insertIntoRequestingList(requestingTests, name, id );
		insertIntoRequestedList(requestedTestTable, name, id, i, currentRequestSampleIndex);
   }

	fieldValidator.setFieldValidity( false, "requestedTests_" + currentRequestSampleIndex );
	$("requestedTests_" + currentRequestSampleIndex).style.border = "1px solid black";
	setSaveButton();
}

function enableDisableAndClearRequestingTests(requestedTestTable, sampleIndex, requestingTests, disable ){
	var checkBoxRows = $$(".selectionRow_" + sampleIndex);

	for( var i = 0; i < requestingTests.length; ++i){
		requestingTests[i].disabled = disable;
		removeOptions( requestingTests[i] );
	}

	for( i = checkBoxRows.length - 1; i >= 0; --i){
		 requestedTestTable.deleteRow(checkBoxRows[i].rowIndex);
	}
	
	$jq(".referralTestResult_" + sampleIndex ).hide();
	$jq(".referralTestResult_" + sampleIndex ).val( "" );
}

function removeOptions(selectbox){
	for(var i=selectbox.options.length-1; i>0 ; --i){
		selectbox.remove(i);
	}
}

function insertIntoRequestingList(testSelections, name, id ){
	for(var i = 0; i < testSelections.length; ++i ){
		addOptionToSelect(testSelections[i],name,id );
	}
}

function insertIntoRequestedList(requestedTestsTable, name, id, i, sampleIndex){
	var newRow = requestedTestsTable.insertRow(  i);
	newRow.id = "availRow_" + i;
	newRow.className = "selectionRow_" + sampleIndex;

	newRow.insertCell(0);
	var selectionCell = newRow.insertCell(1);

	selectionCell.innerHTML = getCheckBoxHtml(id, i, sampleIndex ) + getTestDisplayRowHtml( name, i );
}

function setPossibleResultsForTest( testSelection, compoundIndex ){
	new Ajax.Request (  'ajaxQueryXML',
            	         {
                	         method: 'get',
                    	     parameters: "provider=SampleEntryPossibleResultsForTest&testId=" + testSelection.value + "&index=" + compoundIndex,
                          	 onSuccess:  processGetPossibleResultsSuccess,
                          	 onFailure:  null
                         	}
                          );

}

function processGetPossibleResultsSuccess(xhr){
   	//alert(xhr.responseText);

  	var response = xhr.responseXML.getElementsByTagName("formfield").item(0);
   	var resultTypeElement = response.getElementsByTagName("resultType")[0];
	var indexElement = response.getElementsByTagName("callerIndex")[0];

   	var resultType =  resultTypeElement.attributes.getNamedItem("value").value;
   	var sourceIndex = indexElement.attributes.getNamedItem("value").value;

   	if('N' == resultType){
   		$("textResult_" + sourceIndex).style.display = "inline";
   		$("dictionaryResult_" + sourceIndex).style.display = "none";
   		$("freeTextResult_" + sourceIndex).style.display = "none";
   	}else if('R' == resultType){
   		$("textResult_" + sourceIndex).style.display = "none";
   		$("dictionaryResult_" + sourceIndex).style.display = "none";
   		$("freeTextResult_" + sourceIndex).style.display = "inline";
   	}else{
   	    var dictionarySelection = $("dictionaryResult_" + sourceIndex);
   	    var dictionaryValues = response.getElementsByTagName("value");

   	    removeOptions( dictionarySelection );

   	    addOptionToSelect( dictionarySelection, "", "0" );
   	    for(var i = 0; i < dictionaryValues.length; ++i ){
   	    	addOptionToSelect( dictionarySelection, dictionaryValues[i].attributes.getNamedItem("name").value, dictionaryValues[i].attributes.getNamedItem("id").value );
   	    }
   	    addOptionToSelect( dictionarySelection, '<%= StringUtil.getMessageForKey("option.notListed")%>', "UseText" );

   		dictionarySelection.style.display = "inline";
   		$("textResult_" + sourceIndex).style.display = "none";
   		$("freeTextResult_" + sourceIndex).style.display = "none";
   	}
}

function addOptionToSelect( selectElement, text, value ){
  	    var option = document.createElement("OPTION");
		option.text = text;
		option.value = value;
		selectElement.options.add(option);
}

function checkDictionaryForUseText(dictionaryElement , sourceIndex){
	var selected = dictionaryElement.options[dictionaryElement.selectedIndex].value;
	var textResult = $("textResult_" + sourceIndex);

	if( "UseText" == selected ){
		textResult.style.display = "inline";
	}else{
		textResult.style.display = "none";
		textResult.value = "";
	}

}

function addNewRequesterTestResult(addButtonElement, sampleIndex){  //request for another test in this lab

	var testRequestTable = $("testRequestTable_" + sampleIndex );
	var testResultRow = $("referralTestId_" + sampleIndex); 
	var maxReferralElement = $("maxReferralTestIndex_" + sampleIndex );
	var newTestIndex = parseInt(maxReferralElement.value) + 1;
	var newRow = testRequestTable.insertRow( addButtonElement.parentNode.parentNode.rowIndex  );

	var protoIDPattern = /[0-9]_0/g;
	var selectedPattern = /selected/i;
	var compoundIndex = sampleIndex + "_" + newTestIndex;

	//this crap is brought to you because of the good folks who brought you IE
	var clonedCells = testResultRow.getElementsByTagName('td');
	var cell;
	for (var i = 0; i < clonedCells.length; ++i ){
		cell = newRow.insertCell(i);
		cell.innerHTML = clonedCells[i].innerHTML.replace(protoIDPattern, compoundIndex).
		                                          replace("Tests_0", "Tests_" + sampleIndex).
		                                          replace("inline", "none").
		                                          replace(selectedPattern, "");
		//see note about crap
		if( i == 0 ){ cell.align = "right";	}
	}

	cell = newRow.insertCell( clonedCells.length );
	cell.innerHTML = "<input type=\'button\' value=\'" +
	                 "<%= StringUtil.getMessageForKey("label.button.remove") %>" +
	                 "\' class=\'textButton\'  onclick=\'removeRequestedTest( this, \"" + sampleIndex + "\" );\' >";

	newRow.className = "extraTest_" + sampleIndex;
	maxReferralElement.value = newTestIndex;
}

function addNewRequesterSample( newSampleButton ){ // a new sample which came in with the request
	var cell;
	var maxSampleElement = $("maxSampleIndex");
	var sampleIndex = parseInt(maxSampleElement.value) + 1;
	var requestedTestRow = $("requestedTestRow_0" );
	var protoSampleIDPattern = /_0/g;
	var protoFunction = /this, '0'\)/g;
	var protoMaxValue = /input value=\".\"/;
	var protoIDPattern = /[0-9]_[0-9]/g; //this has to do with the order of replacements
	var optionPattern = /<option .*<\/option>/gi;
	var textAreaPattern = />.*<\/textarea>/i;
	var selectPrototype;

 	var clone = $jq("#div_0" ).clone(true, true);
	clone.attr("id", "div_" + sampleIndex);
	clone.find(".sampleIndex").val( sampleIndex );
	clone.find("#requesterSampleId_0").val("");
	clone.find("#collectionDate_0").val("");
	clone.find("#maxReferralTestIndex_0").val(0);
	clone.find("#hideShow_0").val("hidden");
	clone.find("#showHideButton_0").attr("src", "./images/note-add.gif");
	clone.find("tr:first").append("<td><input type=\"button\" value=\"" +
				                  "<%= StringUtil.getMessageForKey("label.button.remove") %>" +
	                              "\" class=\"textButton\"  onclick=\"removeRequesterTest( this, \'" + sampleIndex +  "\' );\" ></td>");
	
	if( clone.find("div")){
		selectPrototype = $jq("#prototypeID").clone(true, true);
		selectPrototype.attr("id", "initialCondition_" + sampleIndex);
		selectPrototype.attr("multiple", "multiple");
		clone.find("div").replaceWith( selectPrototype );
	}else{
		clone.find("requesterSampleRowTwo").replace(protoMaxPattern, "input value=\"0\"");
	}
	             
	clone.find(".requestingTests_0 option:gt(0)").remove();
	clone.find("#requestedTests_0 tbody").remove();
	clone.find("#note_0").val("");
	clone.find("#noteRow_0").hide();
	clone.find("#dictionaryResult_0_0").hide();
	clone.find("#textResult_0_0").hide();
	clone.find("#freeTextResult_0_0").hide();
	
	clone.html(clone.html().replace(protoSampleIDPattern, "_" + sampleIndex));
	clone.html(clone.html().replace(protoFunction, "this, '" + sampleIndex + "')"));
	clone.html(clone.html().replace("selected", ""));
	clone.html(clone.html().replace( protoIDPattern, sampleIndex + "_0") );
	
	clone.find(".extraTest_" + sampleIndex).remove();
	

	$jq("#div_" + (sampleIndex - 1) ).after(clone);
	
	if(selectPrototype){
		$jq("#initialCondition_" + sampleIndex).asmSelect({removeLable: "X"});
		selectPrototype.attr("selectedIndex", -1);
	}	
	
	maxSampleElement.value = sampleIndex;

    fieldValidator.setFieldValidity(false, "requestedTests_" + sampleIndex);
	setValidIndicaterOnField( true, "requestedTests_" + sampleIndex);
	setValidIndicaterOnField( true, "collectionDate_" + sampleIndex);
	
	setSaveButton();
}

function removeRequestedTest( buttonElement, sampleIndex ){
	$("testRequestTable_" + sampleIndex).deleteRow( buttonElement.parentNode.parentNode.rowIndex );
}

function removeRequesterTest( buttonElement, sampleIndex ){
	var highRowRange = $("requestedTestRow_" + sampleIndex).rowIndex;
	var lowRowRange = buttonElement.parentNode.parentNode.rowIndex;

	for( var i = highRowRange; i >= lowRowRange; --i ){
		$("testRequestTable_" + sampleIndex).deleteRow( i );
	}

	fieldValidator.setFieldValidity(true, "requestedTests_" + sampleIndex  );
	setSaveButton();
}


function getCheckBoxHtml(id, row, sampleIndex ){
	return "<input type='checkbox' id='select_" + row + "' value='" + id + "' onclick='requestedTestChanged(" + sampleIndex + " );'>";
}

function getTestDisplayRowHtml( name, i ){
	return "<label for='select_" + i + "'>" + name + "</label>";
}

function requestedTestChanged( sampleIndex ){
	var requestedTests = $("requestedTests_" + sampleIndex ).getElementsByTagName("input");
	var somethingChecked = false;

	for(var i = 0; i < requestedTests.length; ++i ){
		if( requestedTests[i].checked ){
			somethingChecked = true;
			break;
		}
	}

	$("requestedTests_" + sampleIndex ).style.border = somethingChecked ? "1px solid black" : "1px solid red";
	fieldValidator.setFieldValidity( somethingChecked, "requestedTests_" + sampleIndex );

	setSaveButton();
}

function processGetTestFailure(xhr){
	//alert(xhr.responseText);
}

function getValueFromXmlElement( parent, tag ){
	var element = parent.getElementsByTagName( tag );
	return element ? element[0].childNodes[0].nodeValue : "";
}

function showHideSamples(button, targetId){
	if( button.value == "+" ){
		$(targetId).show();
		button.value = "-";
	}else{
		$(targetId).hide();
		button.value = "+";
	}
}

function loadDynamicData(){
	var xml = "<requestedTests>";
	xml += addSamples( );
	xml += "</requestedTests>";

	$("xmlWad").value = xml;
}

function /*string*/ addSamples( xml ){
	var samplesXml = "<samples>";
	var sampleIndexs = $$(".sampleIndex");

	for( var i = 0; i < sampleIndexs.length; ++ i){
		samplesXml += addSample( 	"zero" == sampleIndexs[i].value ? "0" : sampleIndexs[i].value);
	}

	samplesXml += "</samples>";

	return samplesXml;
}

function /*string*/ addSample( sampleIndex ){

	var sampleXml = "<sample ";
	sampleXml += "requesterSampleId='" + $("requesterSampleId_" + sampleIndex).value + "' ";
	sampleXml += "sampleType='" + ($("sampleType_" + sampleIndex).value) + "' ";
	sampleXml += "collectionDate='" + ($("collectionDate_" + sampleIndex).value) + "' ";
	sampleXml += "note ='" + getNote( sampleIndex ) + "' ";
	if( useInitialSampleCondition ){
		var initialConditions = $("initialCondition_" + sampleIndex);
		var optionLength = initialConditions.options.length;
		xml = " initialConditionIds=' ";
		for( var i = 0; i < optionLength; ++i ){
			if( initialConditions.options[i].selected ){
				xml += initialConditions.options[i].value + ",";
			}
		}

		xml =  xml.substring(0,xml.length - 1);
		xml += "'";
		sampleXml += xml;
	}
	sampleXml += " requestedTests='" + getRequestedTests( sampleIndex ) + "' >";
	sampleXml += "<tests>";
	sampleXml += getTests( sampleIndex );
	sampleXml += "</tests>";
	sampleXml += "</sample>";

	return sampleXml;
}

function /*string*/ getTests( sampleIndex ){
	var tests = "";
	var maxTestIndex = $("maxReferralTestIndex_" + sampleIndex).value;

	for( var i = 0; i <= maxTestIndex; ++i ){
		tests += getTest( sampleIndex, i );
	}

	return tests;
}

function /*string*/ getTest( sampleIndex, testIndex ){
	var compoundIndex = sampleIndex + "_" + testIndex;
	var requestedTest = $("requestedTests_" + compoundIndex);

	if( requestedTest ){
		var id = requestedTest.options[requestedTest.selectedIndex].value;
		var resultType;
		var value;


		if( $("textResult_" + compoundIndex).visible() ){
			resultType = "A";
			value = $jq("#textResult_" + compoundIndex).val();
		}else if( $("freeTextResult_" + compoundIndex).visible() ){
			resultType = "R";
			value = $jq("#freeTextResult_" + compoundIndex).val();
		}else{
			resultType = "D";
			value = $jq("#dictionaryResult_" + compoundIndex).val();
		}

		return "<test id='" +id + "' resultType='" + resultType + "' value='" + value +  "' />";
	}

    return "";
}

function /*string*/ getRequestedTests( sampleIndex ){
	var requestList = "";
	var requestedTests = $("requestedTests_" + sampleIndex ).getElementsByTagName("input");

	for(var i = 0; i < requestedTests.length; ++i ){
		if( requestedTests[i].checked ){
			requestList += requestedTests[i].value + ",";
		}
	}

	return requestList;
}

function /*string*/ getNote( sampleIndex ){
	var singleQuote = /\'/g;
	var doubleQuote = /\"/g;
	return ($("note_" + sampleIndex).value.replace(singleQuote, "\\'" ).replace(doubleQuote, '\\"'));
}

</script>
<% if(useInitialSampleCondition){ %>
<div id="sampleConditionPrototype" style="display: none" >
			<select id="prototypeID" title='<%= StringUtil.getMessageForKey("result.multiple_select")%>' > 
			<logic:iterate id="optionValue" name='<%=formName%>' property="initialSampleConditionList" type="IdValuePair" >
						<option value='<%=optionValue.getId()%>' >
							<bean:write name="optionValue" property="value"/>
						</option>
					</logic:iterate>
			</select>
</div>
<% } %>

<input type="hidden" id="maxSampleIndex" value="0" />
<html:hidden name='<%=formName %>' property="requestAsXML" styleId="xmlWad" />
<table width="70%" border="0">
	<tr>
		<td>
			<%=StringUtil.getContextualMessageForKey("quick.entry.accession.number")%>:
			<span class="requiredlabel">*</span>
		</td>
		<td width="15%">
			<app:text name="<%=formName%>" property="labno"
				maxlength='<%= Integer.toString(accessionNumberValidator.getMaxAccessionLength())%>'
				onchange="checkAccessionNumber(this);makeDirty();" styleClass="text"
				styleId="labNo" />
		</td>
		<td id="generate">
			<bean:message key="sample.entry.scanner.instructions" />
			<html:button property="generate" styleClass="textButton"
				onclick="getNextAccessionNumber(); makeDirty();">
				<bean:message key="sample.entry.scanner.generate" />
			</html:button>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="quick.entry.received.date" />
			:
			<span class="requiredlabel">*</span>
			<font size="1"><bean:message key="sample.date.format" />
			</font>
		</td>
		<td>
			<app:text name="<%=formName%>" property="receivedDate"
				onchange="checkValidDate(this);makeDirty();"
				onkeyup="addDateSlashes(this,event);"
				styleClass="text"
				maxlength="10"
				styleId="receivedDate" />
		</td>
	</tr>
</table>
<hr />
<h3>
	<bean:message key="sample.entry.requester"/>
</h3>
<table>
	<tr>
		<td>
			<bean:message key="organization.site" />
		</td>
		<td colspan="5">
		<!-- N.B. this is replaced by auto repeate -->
		<html:select styleId="orgRequesterId" 
								     name="<%=formName%>"
								     property="requestingOrganization"
								     onchange="getRequestersForOrg();makeDirty();setSaveButton();"
								     >
							<option value=""></option>
							<logic:iterate name="<%=formName %>" property="requestingOrganizationList" id="orgIdx" type="us.mn.state.health.lims.common.util.IdValuePair">
							    <option value="<%=orgIdx.getId() %>" ><%= orgIdx.getValue() %></option>
							</logic:iterate>
		</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="sample.entry.contact" />
		</td>
		<td colspan="5">
			<html:select name='<%= formName %>'
			             property="personRequesterId"
			             styleId="personRequesterId"
			             onchange="populateRequesterDetail(this); makeDirty();">
				<option value="0"><bean:message key="sample.entry.requester.new" /></option>
			</html:select>
		</td>
	</tr>
	<tr><td>&nbsp;</td>
		<td>
			<bean:message key="person.firstName" />
		</td>
		<td>
			<bean:message key="person.lastName" />
		</td>
		<td>
			<bean:message key="person.phone"/>
		</td>
		<td>
			<bean:message key="person.fax"/>
		</td>
		<td>
			<bean:message key="person.email"/>
		</td>

	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>
			<html:text name='<%=formName%>' property="firstName" styleId="requesterFirstName" />
		</td>
		<td>
			<html:text name='<%=formName%>' property="lastName"  styleId="requesterLastName" />
		</td>
		<td>
			<html:text name='<%=formName%>' property="phone"  styleId="requesterPhone" />
		</td>
		<td>
			<html:text name='<%=formName%>' property="fax"  styleId="requesterFax" />
		</td>
		<td>
			<html:text name='<%=formName%>' property="e-mail"  styleId="requesterEMail" />
		</td>

	</tr>
</table>
<hr/>

<h3><bean:message key="sample.entry.confirmation.test.request"/></h3>
<input type="hidden" value="0" id="maxReferralSampleIndex_0" />
<div id=div_0>
<table  id="testRequestTable_0">
	<tr id="requesterSampleRowOne">
			<td><bean:message key="sample.entry.test.confirmation.requester.id"/></td>
			<td>
				<input type="hidden" value="zero" class="sampleIndex" />
				<input type="hidden" value="0" id="maxReferralTestIndex_0" />
				<input type="text" id="requesterSampleId_0" onchange=" makeDirty();">
			</td>
			<td><bean:message key="sample.collectionDate"/></td>
			<td><input type="text"
			           id="collectionDate_0"
			           name="collectionDate_0"
			           maxlength="10"
			           onkeyup="addDateSlashes(this,event);"
			           onchange="checkValidDate(this)"/>
			</td>
			<td>
				<img src="./images/note-add.gif"
						 	     onclick="showHideNotes(this, '0');"
						 	     id="showHideButton_0"
						    />
				<html:hidden property="hideShowFlag"  styleId="hideShow_0" value="hidden" />
			</td>
	</tr>
	<tr id="requesterSampleRowTwo">	
	        <% if(useInitialSampleCondition){ %>
				<td >
					<bean:message key="sample.entry.sample.condition"/>
				</td>
				<td>
				<select id="initialCondition_0"  multiple="multiple" title='<%= StringUtil.getMessageForKey("result.multiple_select")%>' >
					<logic:iterate id="optionValue" name='<%=formName%>' property="initialSampleConditionList" type="IdValuePair" >
								<option value='<%=optionValue.getId()%>' >
									<bean:write name="optionValue" property="value"/>
								</option>
							</logic:iterate>
						</select>
			</td>
			<% } %>
					
			<td><bean:message key="sample.entry.sample.type"/>&nbsp;<span class="requiredlabel">*</span></td>
			<td>
			<select id="sampleType_0"
				    onchange="makeDirty(); populateRequestForSampleType(this, '0');" >
				<option value="0"></option>
				<logic:iterate id="sampleTypes"
							   name='<%=formName %>'
							   property="sampleTypes"
							   type="TypeOfSample">
					<option value='<%= sampleTypes.getId() %>'>
						<%=sampleTypes.getLocalizedName() %>
					</option>
				</logic:iterate>
			</select>
			</td>
		</tr>
		<tr id="noteRow_0"	style="display: none;">
			<td valign="top" align="right"><bean:message key="note.note"/>:</td>
			<td colspan="6" align="left" >
				<textarea id="note_0"
						   onchange="makeDirty();"
			           	   cols="100"
			           	   rows="3" ></textarea>
			</td>
		</tr>
		<tr id="referralTestId_0" >
			<td align="right"><bean:message key="sample.entry.test.confirmation.site.test" /></td>
			<td>
				<select name="requesterTests"
							id="requestedTests_0_0"
							class="requestingTests_0"
							onchange="setPossibleResultsForTest(this, '0_0');"
							 >
					<option value="0" >&nbsp;</option>
				</select>
			</td>
			<td><bean:message key="sample.entry.test.confirmation.site.result"/></td>
			<td>
				<select name="result"
				        style="display: none"
				        class="referralTestResult_0"
				        id="dictionaryResult_0_0"
		        		onchange="checkDictionaryForUseText(this, '0_0');"
				        >
				</select>

				<input type="text" class="referralTestResult_0" name="result" style="display: none" id="textResult_0_0" />
				
				<textarea rows="2" class="referralTestResult_0" name=result style="display: none" id="freeTextResult_0_0" ></textarea>
			</td>
		</tr>
		<tr id="addButtonRow">
			<td>&nbsp;</td>
			<td>
				<input type="button"
				       class=textButton
				       value='<%= StringUtil.getMessageForKey("sampletracking.requester.test.result.add") %>'
				       onclick="addNewRequesterTestResult(this, '0');" />
			</td>
		</tr>
		<tr id="requestedTestRow_0">
			<td valign="top"><bean:message key="sample.entry.confirmation.requested.tests"/> : <span class="requiredlabel">*</span></td>

			<td colspan="5">
			<table style="background-color:#EEEEEE; color: black; border : 1px solid red" id="requestedTests_0" >
			</table>
			</td>
		</tr>
</table>
<hr/>
</div>		

<input type="button"
	   class=textButton
	   value="<%= StringUtil.getMessageForKey("sampletracking.requester.sample.add") %>"
	   onclick="addNewRequesterSample( this )" />

<hr/>
<hr style="width: 100%;" class="hr-style-1" />
<html:hidden name="<%=formName%>" property="patientPK" styleId="patientPK"/>
<table style="width:100%">
	<tr>
		<td width="15%" align="left">
			<html:button property="showPatient" onclick="showHideSamples(this, 'patientInfo');" >+</html:button>
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

<div id="patientInfo" class="colorFill" style="display:none;" >
	<tiles:insert attribute="patientInfo" />
</div>

<script type="text/javascript" >

function /*void*/ setSave(){
	if(patientFormValid()){
		setSaveButton();
	}else{
		$("saveButtonId").disabled = true;
	}
}

//all methods here either overwrite methods in tiles or all called after they are loaded
var dirty=false;
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

function savePage(){
    jQuery("#saveButtonId").attr("disabled", "disabled");
	loadDynamicData();
	//alert( $("xmlWad").value );
	
	window.onbeforeunload = null; // Added to flag that formWarning alert isn't needed.
	var form = window.document.forms[0];
	form.action = "SampleConfirmationUpdate.do";
	form.submit();
}

function setValidIndicaterOnField( success, element){
	//Note the method this is overriding uses the name of the element, which does not have to be unique, set the id == name
	if( !element.id ){ //element is id
		element = $(element);
	}

	element.style.borderColor = success ? "" : "red";
	element.style.borderWidth = success ? "" : "2";
}

</script>
<!--
 <ajax:autocomplete
  source="requestingOrg"
  target="selectedOrganizationId"
  baseUrl="ajaxAutocompleteXML"
  className="autocomplete"
  parameters="organizationName={requestingOrg},orgType=Refering lab,provider=OrganizationAutocompleteProvider,fieldName=organizationName,idName=id"
  indicator="indicator1"
  minimumCharacters="1" />
 -->
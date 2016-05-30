<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date, java.util.List,
	org.apache.struts.Globals,
	us.mn.state.health.lims.common.util.SystemConfiguration,
	us.mn.state.health.lims.common.action.IActionConstants,
	java.util.Collection,
	java.util.ArrayList,
	java.text.DecimalFormat,
	org.apache.commons.validator.GenericValidator,
	us.mn.state.health.lims.inventory.form.InventoryKitItem,
	us.mn.state.health.lims.test.beanItems.TestResultItem,
	us.mn.state.health.lims.common.util.IdValuePair,
	us.mn.state.health.lims.common.formfields.FormFields,
	us.mn.state.health.lims.common.formfields.FormFields.Field,
	us.mn.state.health.lims.common.provider.validation.AccessionNumberValidatorFactory,
	us.mn.state.health.lims.common.provider.validation.IAccessionNumberValidator,
	us.mn.state.health.lims.common.util.ConfigurationProperties,
	us.mn.state.health.lims.common.util.ConfigurationProperties.Property,
	us.mn.state.health.lims.common.util.StringUtil,
    us.mn.state.health.lims.common.util.Versioning,
	us.mn.state.health.lims.testreflex.action.util.TestReflexResolver,
	java.net.URLDecoder,
    us.mn.state.health.lims.siteinformation.daoimpl.SiteInformationDAOImpl"%>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>
<%@ taglib uri="/tags/sourceforge-ajax" prefix="ajax"%>

<bean:define id='formName' value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />

<bean:define id="tests" name='<%=formName%>' property="testResult" />
<bean:size id="testCount" name="tests"/>
<bean:define id="inventory" name="<%=formName%>" property="inventoryItems" />

<bean:define id="pagingSearch" name='<%=formName%>' property="paging.searchTermToPage"  />

<bean:define id="logbookType" name="<%=formName%>" property="logbookType" />
<bean:define id="referer" value='<%= request.getParameter("referer") == null || request.getParameter("referer").isEmpty() ? "" : request.getParameter("referer")%>' />

<%!
    private static final String UPLOADED_RESULTS_DIRECTORY = "uploadedResultsDirectory";
    List<String> hivKits;
	List<String> syphilisKits;
	String basePath = "";
	String searchTerm = null;
	IAccessionNumberValidator accessionNumberValidator;
	boolean useSTNumber = true;
	boolean useNationalID = true;
	boolean useSubjectNumber = true;
	boolean useTechnicianName = true;
	boolean depersonalize = false;
	boolean ableToRefer = false;
	boolean compactHozSpace = false;
	boolean useInitialCondition = false;
	boolean failedValidationMarks = false;
	boolean noteRequired = false;
	boolean autofillTechBox = false;

 %>
<%
	hivKits = new ArrayList<String>();
	syphilisKits = new ArrayList<String>();

	for( InventoryKitItem item : ((List<InventoryKitItem>)inventory) ){
		if( item.getType().equals("HIV") ){
	hivKits.add(item.getInventoryLocationId());
		}else{
	syphilisKits.add( item.getInventoryLocationId());
		}
	}

	String path = request.getContextPath();
	basePath = path + "/";

	searchTerm = request.getParameter("searchTerm");

	accessionNumberValidator = new AccessionNumberValidatorFactory().getValidator();
	useSTNumber = FormFields.getInstance().useField(Field.StNumber);
	useNationalID = FormFields.getInstance().useField(Field.NationalID);
	useSubjectNumber = FormFields.getInstance().useField(Field.SubjectNumber);
	useTechnicianName =  ConfigurationProperties.getInstance().isPropertyValueEqual(Property.resultTechnicianName, "true");

	depersonalize = FormFields.getInstance().useField(Field.DepersonalizedResults);
	ableToRefer = FormFields.getInstance().useField(Field.ResultsReferral);
	compactHozSpace = FormFields.getInstance().useField(Field.ValueHozSpaceOnResults);
	useInitialCondition = FormFields.getInstance().useField(Field.InitialSampleCondition);
	failedValidationMarks = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.failedValidationMarker, "true");
	noteRequired =  ConfigurationProperties.getInstance().isPropertyValueEqual(Property.notesRequiredForModifyResults, "true");
	autofillTechBox = ConfigurationProperties.getInstance().isPropertyValueEqual(Property.autoFillTechNameBox, "true");
%>

<!-- N.B. testReflex.js is dependent on utilities.js so order is important  -->
<script type="text/javascript" src="<%=basePath%>scripts/utilities.js?ver=<%= Versioning.getBuildNumber() %>" ></script>
<script type="text/javascript" src="<%=basePath%>scripts/ajaxCalls.js?ver=<%= Versioning.getBuildNumber() %>" ></script>
<script type="text/javascript" src="<%=basePath%>scripts/testResults.js?ver=<%= Versioning.getBuildNumber() %>" ></script>
<script type="text/javascript" src="<%=basePath%>scripts/testReflex.js?ver=<%= Versioning.getBuildNumber() %>" ></script>
<script type="text/javascript" src="scripts/overlibmws.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="scripts/jquery.ui.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="scripts/jquery.asmselect.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="scripts/OEPaging.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="<%=basePath%>scripts/math-extend.js?ver=<%= Versioning.getBuildNumber() %>" ></script>

<link rel="stylesheet" type="text/css" href="css/jquery.asmselect.css?ver=<%= Versioning.getBuildNumber() %>" />


<script type="text/javascript" >

<% if( ConfigurationProperties.getInstance().isPropertyValueEqual(Property.alertForInvalidResults, "true")){%>
       outOfValidRangeMsg = '<%= StringUtil.getMessageForKey("result.outOfValidRange.msg") %>';
<% }else{ %>
       outOfValidRangeMsg = null;
<% } %>

var compactHozSpace = '<%=compactHozSpace%>';
var dirty = false;

var pager = new OEPager('<%=formName%>', '<%= logbookType == "" ? "" : "&type=" + logbookType  %>');
pager.setCurrentPageNumber('<bean:write name="<%=formName%>" property="paging.currentPage"/>');

var pageSearch; //assigned in post load function

var pagingSearch = new Object();

<%
	for( IdValuePair pair : ((List<IdValuePair>)pagingSearch)){
		out.print( "pagingSearch[\'" + pair.getId()+ "\'] = \'" + pair.getValue() +"\';\n");
	}
%>


$jq(document).ready( function() {
    var searchTerm = '<%=searchTerm%>';
    $jq("select[multiple]").asmSelect({
            removeLabel: "X"
        });

    $jq("select[multiple]").change(function(e, data) {
        handleMultiSelectChange( e, data );
        });

    pageSearch = new OEPageSearch( $("searchNotFound"), compactHozSpace == "true" ? "tr" : "td", pager );

    if( searchTerm != "null" ){
         pageSearch.highlightSearch( searchTerm, false );
    }

    if (jQuery(".alert-error").length == 0) {
        $jq("html, body").animate({ scrollTop: $jq("#resultsDisplayBlock").offset().top }, 500);
    }

	$jq("input[type=checkbox][class=referralCheckBox]").each(
			function(){
				var index = this.id.slice(this.id.length-1,this.id.length);
				var checked = this.checked;
				var disabled = this.disabled;
				if(checked  && !disabled) {
					markUpdated(index);
					handleReferralCheckChange(this);
				}
			}
	);
});

function handleMultiSelectChange( e, data ){
	var id = "#multi" + e.target.id;
	var selection = $jq(id)[0];

	if( data.type == "add"){
		appendValueToElementValue( selection, data.value );
	}else{ //drop
		var splitValues =  selection.value.split(",");
		selection.value = "";

		for( var i = 0; i < splitValues.length; i++ ){
			if( splitValues[i] != data.value ){
				appendValueToElementValue( selection, splitValues[i] );
			}
		}
	}
}

function /*boolean*/ handleEnterEvent(){
    return true;
}

function appendValueToElementValue( e, addString ){
	if( e.value && e.value.length > 1 ){
			e.value += ',';
		}

		e.value += addString;
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

function toggleKitDisplay( button ){
	if( button.value == "+" ){
		$(kitView).show();
		button.value = "-";
	}else{
		$(kitView).hide();
		button.value = "+";
	}
}


function /*void*/ markUpdated( index, userChoiceReflex, siblingReflexKey ){

	if( userChoiceReflex ){
		var siblingId = siblingReflexKey != 'null' ? $(siblingReflexKey).value : null;
		showUserReflexChoices( index, siblingId );
	}

	$("modified_" + index).value = "true";

	makeDirty();

	$("saveButtonId").disabled = !unSelectedReflexs.isEmpty();
}

function updateLogValue(element, index ){
	var logField = $("log_" + index );

	if( logField ){
		var logValue = Math.baseLog(element.value).toFixed(2);

		if( isNaN(logValue) ){
			logField.value = "--";
		}else{
			logField.value = logValue;
		}
	}
}


function /*void*/ setRadioValue( index, value){
	$("results_" + index).value = value;
}

function  /*void*/ setMyCancelAction(form, action, validate, parameters)
{
	//first turn off any further validation
	setAction(window.document.forms[0], 'Cancel', 'no', '');
}

function /*void*/ autofill( sourceElement ){
	var techBoxes = $$(".techName"),
	    boxCount = techBoxes.length,
	    value = sourceElement.value;
	    i = 0;

	for( ; i < boxCount; ++i){
		techBoxes[i].value = value;
	}
}
function validateForm(){
	return true;
}

function /*void*/ handleReferralCheckChange(checkbox){
    var testResult = $(checkbox).up('.testResult');
    var referralReason = $(testResult).down('.referralReason');
	referralReason.value = (referralReason.value != "0") ? referralReason.value : 0;
    var isTestReferredOut = checkbox.checked;
    referralReason.disabled = !isTestReferredOut;

	var referralOrganization = $(testResult).down('.referralOrganization');
	referralOrganization.value = (referralOrganization.value != "0") ? referralOrganization.value : 0;
	referralOrganization.disabled = !isTestReferredOut;

	var isReferredOutValueChanged = $(testResult).down('#isReferredOutValueChanged');

    var result = $(testResult).down('.testResultValue');
    result.style.background = "#ffffff";
    result.disabled = isTestReferredOut;
    var abnormal = $(testResult).down('.testValueAbnormal');
    if(isTestReferredOut) {
        result.value = "";
        abnormal.checked = false;
		isReferredOutValueChanged.value = false;
    }else{
		referralReason.value = 0;
		referralOrganization.value = 0;
		isReferredOutValueChanged.value = true;
	}
}

function /*void*/ handleReferralReasonAndInstituteChange(index ){
    var organizationSelect = $( "referralOrganizationId_" + index );
    var reasonSelectSelect = $( "referralReasonId_" + index );

	if( reasonSelectSelect.value == 0 && organizationSelect.value == 0){
		$( "referralId_" + index ).checked = false;
		organizationSelect.disabled = true;
		reasonSelectSelect.disabled = true;
	}
}

function updateAbnormalCheck(isNormal, index) {
    if(isNormal == true){
        $("abnormalId_" + index).checked = false;
    } else {
        $("abnormalId_" + index).checked = true;
    }
}
function isNormalForNumeric(value, lowerBound, upperBound) {
    if(value && upperBound !== lowerBound) {
        return value <= upperBound && value >= lowerBound;
    } else {
        return true;
    }
}

function isNormalForDropDown(id, idValuePair) {
    if(id === "0") {
        return true;
    }
var retVal;
    idValuePair.forEach(function(idValue){
        if(idValue.id === id){
            if(idValue.value === "true"){
                retVal = false;
            } else {
                retVal = true;
            }
        }
    })
    return retVal;
}

function enableOnlyForRemark(index, resultType) {
    if(resultType === 'R') {
        $("abnormalId_" + index).disabled = false;
    } else {
        $("abnormalId_" + index).disabled = true;
    }
}

function initializeAbnormalValue(index, resultType) {
    $("realAbnormalId_" + index).value = $("abnormalId_" + index).value;
}

function setAbnormalValues() {
    $$('.testValueAbnormal').forEach(function(element) {
        var hiddenElementId = element.id.replace('abnormalId', 'realAbnormalId');
        $(hiddenElementId).value = element.checked;
    });
}

//this overrides the form in utilities.jsp
function  /*void*/ savePage()
{
    setAbnormalValues();

    jQuery("#saveButtonId").attr("disabled", "disabled");
	window.onbeforeunload = null; // Added to flag that formWarning alert isn't needed.
	var form = window.document.forms[0];
    form.enctype = "multipart/form-data";
	form.action = '<%=formName%>'.sub('Form','') + "Update.do?referer=" + '<%= referer %>'  + '<%= logbookType == "" ? "" : "&type=" + logbookType  %>';
	form.submit();
}

function updateReflexChild( group){

 	var reflexGroup = $$(".reflexGroup_" + group);
	var childReflex = $$(".childReflex_" + group);
 	var i, childId, rowId, resultIds = "", values="", requestString = "";

 	if( childReflex ){
 		childId = childReflex[0].id.split("_")[1];

		for( i = 0; i < reflexGroup.length; i++ ){
			if( childReflex[0] != reflexGroup[i]){
				rowId = reflexGroup[i].id.split("_")[1];
				resultIds += "," + $("hiddenResultId_" + rowId).value;
				values += "," + reflexGroup[i].value;
			}
		}

		requestString +=   "results=" +resultIds.slice(1) + "&values=" + values.slice(1) + "&childRow=" + childId;

		new Ajax.Request (
                      'ajaxQueryXML',  //url
                      {//options
                      method: 'get', //http method
                      parameters: 'provider=TestReflexCD4Provider&' + requestString,
                      indicator: 'throbbing',
                      onSuccess:  processTestReflexCD4Success,
                      onFailure:  processTestReflexCD4Failure
                           }
                          );
 	}

}

function /*void*/ processTestReflexCD4Failure(xhr){
	alert("failed");
}

function /*void*/ processTestReflexCD4Success(xhr)
{
	//alert( xhr.responseText );
	var formField = xhr.responseXML.getElementsByTagName("formfield").item(0);
	var message = xhr.responseXML.getElementsByTagName("message").item(0);
	var success = false, childRow, value;


	if (message.firstChild.nodeValue == "valid"){
		success = true;
		childRow = formField.getElementsByTagName("childRow").item(0).childNodes[0].nodeValue;
		value = formField.getElementsByTagName("value").item(0).childNodes[0].nodeValue;

		if( value && value.length > 0){
			$("results_" + childRow).value = value;
		}

	}

}


</script>

<logic:notEmpty name="<%=formName%>" property="logbookType" >
	<html:hidden name="<%=formName%>" property="logbookType" />
</logic:notEmpty>

<logic:notEqual name="testCount" value="0">
<logic:equal name="<%=formName%>" property="displayTestKit" value="true">
	<html:button property="showKit" onclick="toggleKitDisplay(this)" >+</html:button>
	<bean:message key="inventory.testKits"/>
	<div id="kitView" style="display: none;" class="colorFill" >
		<tiles:insert attribute="testKitInfo" />
		<br/>
	</div>
</logic:equal>

<div id="resultsDisplayBlock">
        <logic:equal  name='<%=formName%>' property="singlePatient" value="true">
<% if(!depersonalize){ %>
<table width="100%" >
	<tr>

		<% if(useSTNumber){ %>
		<th width="15%">
			<bean:message key="patient.ST.number" />
		</th>
		<% } %>
		<th width="20%">
			<bean:message key="person.firstName" />
		</th>
		<th width="20%">
			<bean:message key="person.lastName" />
		</th>
		<th width="10%">
			<bean:message key="patient.gender" />
		</th>
		<th width="15%">
			<bean:message key="patient.birthDate" />
		</th>
		<% if(useNationalID){ %>
		<th width="20%">
			<%= StringUtil.getContextualMessageForKey("patient.NationalID") %>
		</th>
		<% } %>
		<% if(useSubjectNumber){ %>
		<th width="20%">
			<bean:message key="patient.subject.number" />
		</th>
		<% } %>
	</tr>
	<tr>
		<% if(useSTNumber){ %>
		<td>
			<bean:write name="<%=formName%>" property="st" />
		</td>
		<% } %>
		<td>
			<bean:write name="<%=formName%>" property="firstName" />
		</td>
		<td>
			<bean:write name="<%=formName%>" property="lastName" />
		</td>
		<td>
			<bean:write name="<%=formName%>" property="gender" />
		</td>
		<td>
			<bean:write name="<%=formName%>" property="dob" />
		</td>
		<% if(useNationalID){ %>
		<td>
			<bean:write name="<%=formName%>" property="nationalId" />
		</td>
		<% } %>
		<% if(useSubjectNumber){ %>
		<td>
			<bean:write name="<%=formName%>" property="subjectNumber" />
		</td>
		<% } %>
	</tr>
</table>
<% } %>
</logic:equal>

<div class="results-page-block">

<logic:notEqual name="<%=formName%>" property="paging.totalPages" value="0">
	<html:hidden styleId="currentPageID" name="<%=formName%>" property="paging.currentPage"/>
	<bean:define id="total" name="<%=formName%>" property="paging.totalPages"/>
	<bean:define id="currentPage" name="<%=formName%>" property="paging.currentPage"/>

	<%if( "1".equals(currentPage)) {%>
		<input type="button" value='<%=StringUtil.getMessageForKey("label.button.previous") %>' style="width:100px;" disabled="disabled" >
	<% } else { %>
		<input type="button" value='<%=StringUtil.getMessageForKey("label.button.previous") %>' style="width:100px;" onclick="pager.pageBack();" />
	<% } %>
	<%if( total.equals(currentPage)) {%>
		<input type="button" value='<%=StringUtil.getMessageForKey("label.button.next") %>' style="width:100px;" disabled="disabled" />
	<% }else{ %>
		<input type="button" value='<%=StringUtil.getMessageForKey("label.button.next") %>' style="width:100px;" onclick="pager.pageFoward();"   />
	<% } %>

	&nbsp;
	<bean:write name="<%=formName%>" property="paging.currentPage"/> of
	<bean:write name="<%=formName%>" property="paging.totalPages"/>
	<div class='textcontent' style="float: right" >
	<span style="visibility: hidden" id="searchNotFound"><em><%= StringUtil.getMessageForKey("search.term.notFound") %></em></span>
	<%=StringUtil.getContextualMessageForKey("result.sample.id")%> : &nbsp;
	<input type="text"
	       id="labnoSearch"
	       maxlength='<%= Integer.toString(accessionNumberValidator.getMaxAccessionLength())%>' />
	<input type="button" onclick="pageSearch.doLabNoSearch($(labnoSearch))" value='<%= StringUtil.getMessageForKey("label.button.search") %>'>
	</div>
</logic:notEqual>

<div style="float: right" >
	<div><img src="./images/nonconforming.gif" /> = <bean:message key="result.nonconforming.item"/></div>
<% if(failedValidationMarks){ %>
<img src="./images/validation-rejected.gif" /> = <bean:message key="result.validation.failed"/>&nbsp;&nbsp;&nbsp;&nbsp;
<% } %>
</div>

</div>

<Table width="100%" border="0" cellspacing="0" class="results-table">
	<tr>
		<% if( !compactHozSpace ){ %>
		<th>
			<%=StringUtil.getContextualMessageForKey("result.sample.id")%>
		</th>
		<logic:equal name="<%=formName %>" property="singlePatient" value="false">
			<th style="text-align: left">
                <bean:message key="result.sample.patient.summary"/>
			</th>
		</logic:equal>
		<% } %>

		<%--<th>--%>
			<%--<bean:message key="result.test.date"/><br/>--%>
			<%--<bean:message key="sample.date.format"/>--%>
		<%--</th>--%>
		<%--<logic:equal  name="<%=formName%>" property="displayTestMethod" value="true">--%>
			<%--<th width="5%" style="text-align: left">--%>
				<%--<bean:message key="result.method.auto"/>--%>
			<%--</th>--%>
		<%--</logic:equal>--%>
		<th>
			<bean:message key="result.test"/>
		</th>
		<th width="16px">&nbsp;</th>
		<th width="165px" style="text-align: left">
			<bean:message key="result.result"/>
		</th>
        <th width="165px" style="text-align: left">
			<bean:message key="result.abnormal"/>
		</th>
		<% if( ableToRefer ){ %>
		<th>
			<bean:message key="referral.referandreason"/>
		</th>
		<% } %>
		<th>
			<bean:message key="referral.institute"/>
		</th>
		<th width="5%">
			<bean:message key="result.notes"/>
		</th>
        <th width="5%">
            <bean:message key="result.files"/>
        </th>
	</tr>
	<logic:iterate id="testResult" name="<%=formName%>"  property="testResult" indexId="index" type="TestResultItem">
	<logic:equal name="testResult" property="isGroupSeparator" value="true">
	<tr>
		<td colspan="10"><hr/></td>
	</tr>
	<tr>
		<th>
			<bean:message key="sample.receivedDate"/> <br/>
			<bean:write name="testResult" property="receivedDate"/>
		</th>
		<th >
			<%=StringUtil.getContextualMessageForKey("resultsentry.accessionNumber")%><br/>
			<bean:write name="testResult" property="accessionNumber"/>
		</th>
		<th colspan="8" />
	</tr>
	</logic:equal>
	<logic:equal name="testResult" property="isGroupSeparator" value="false">
		<bean:define id="abnormalDictValuePair" name="testResult" property="abnormalTestResult"/>
		<bean:define id="lowerBound" name="testResult" property="lowerNormalRange" />
		<bean:define id="upperBound" name="testResult" property="upperNormalRange" />
		<bean:define id="lowerAbnormalBound" name="testResult" property="lowerAbnormalRange" />
		<bean:define id="upperAbnormalBound" name="testResult" property="upperAbnormalRange" />
		<bean:define id="bound" value="<%=String.valueOf(!lowerBound.equals(upperBound))%>" />
		<bean:define id="rowColor" value='<%=(testResult.getSampleGroupingNumber() % 2 == 0) ? "evenRow" : "oddRow" %>' />
		<bean:define id="readOnly" value='<%=testResult.isReadOnly() ? "disabled=\'true\'" : "" %>' />
		<bean:define id="accessionNumber" name="testResult" property="accessionNumber"/>
   <% if( compactHozSpace ){ %>
   <logic:equal  name="testResult" property="showSampleDetails" value="true">
		<tr class='<%= rowColor %>Head <%= accessionNumber%>' >
			<td colspan="10" class='InterstitialHead' >
                <%=StringUtil.getContextualMessageForKey("result.sample.id")%> : &nbsp;
				<b><bean:write name="testResult" property="accessionNumber"/> -
				<bean:write name="testResult" property="sequenceNumber"/></b>
				<% if(useInitialCondition){ %>
					&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="sample.entry.sample.condition" />:
					<b><bean:write name="testResult" property="initialSampleCondition" /></b>
				<% } %>
				&nbsp;&nbsp;&nbsp;&nbsp;
                <bean:message  key="sample.entry.sample.type"/>:
                <bean:write name="testResult" property="sampleType"/> &nbsp;
				<logic:equal name="<%=formName %>" property="singlePatient" value="false">
                    <% if (!depersonalize) { %>
                    <logic:equal name="testResult" property="showSampleDetails" value="true">
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <bean:message key="result.sample.patient.summary"/> : &nbsp;
                        <b>
                            <bean:write name="testResult" property="patientIdentity"/> &nbsp;,
                            <bean:write name="testResult" property="patientName"/> &nbsp;
                        </b>
                    </logic:equal>
                    <% } %>
                </logic:equal>
            </td>
        </tr>
	</logic:equal>
    <% } %>
	<tr class='<%= rowColor %> testResult'  >
			<html:hidden name="testResult" property="isModified"  indexed="true" styleId='<%="modified_" + index%>' />
			<html:hidden name="testResult" property="analysisId"  indexed="true" styleId='<%="analysisId_" + index%>' />
			<html:hidden name="testResult" property="resultId"  indexed="true" styleId='<%="hiddenResultId_" + index%>'/>
			<html:hidden name="testResult" property="testId"  indexed="true" styleId='<%="testId_" + index%>'/>
			<html:hidden name="testResult" property="technicianSignatureId" indexed="true" />
			<html:hidden name="testResult" property="testKitId" indexed="true" />
			<html:hidden name="testResult" property="resultLimitId" indexed="true" />
			<html:hidden name="testResult" property="resultType" indexed="true" styleId='<%="resultType_" + index%>' />
			<html:hidden name="testResult" property="valid" indexed="true"  styleId='<%="valid_" + index %>'/>
			<html:hidden name="testResult" property="referralId" indexed="true" />
			<html:hidden name="testResult" property="referralCanceled" indexed="true" />
			<html:hidden name="testResult" property="userChoicePending"  styleId='<%="userChoicePendingId_" + index%>' indexed="true"/>
			<html:hidden name="testResult" property="isReferredOutValueChanged" indexed="true" styleId='<%="isReferredOutValueChanged"%>'/>
		    <logic:notEmpty name="testResult" property="thisReflexKey">
					<input type="hidden" id='<%= testResult.getThisReflexKey() %>' value='<%= index %>' />
			</logic:notEmpty>
		 <% if( !compactHozSpace ){ %>
	     <td class='<%= accessionNumber%>'>
			<logic:equal  name="testResult" property="showSampleDetails" value="true">
				<bean:write name="testResult" property="accessionNumber"/> -
				<bean:write name="testResult" property="sequenceNumber"/>
			</logic:equal>
		</td>
		<logic:equal  name="<%=formName %>" property="singlePatient" value="false">
			<td >
				<logic:equal  name="testResult" property="showSampleDetails" value="true">
					<bean:write name="testResult" property="patientName"/><br/>
					<bean:write name="testResult" property="patientInfo"/>
				</logic:equal>
			</td>
		</logic:equal>
		<% } %>
		<!-- results -->
		<logic:equal name="testResult" property="resultDisplayType" value="HIV">
			<td valign="top" class="ruled">
				<html:hidden name="testResult" property="testMethod" indexed="true"/>
				<bean:write name="testResult" property="testName"/>
				<logic:greaterThan name="testResult" property="reflexStep" value="0">
				&nbsp;--&nbsp;
				<bean:message key="reflexTest.step" />&nbsp;<bean:write name="testResult" property="reflexStep"/>
				</logic:greaterThan>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="inventory.testKit"/>
				<html:select name="testResult"
							 property="testKitInventoryId"
							 value='<%=testResult.getTestKitInventoryId()%>'
							 indexed="true"
							 tabindex='-1'
							 onchange='<%="markUpdated(" + index + ");"%>' >
					<logic:iterate id="id" indexId="index" collection="<%=hivKits%>">
						<option value='<%=id%>'  <%if(id.equals(testResult.getTestKitInventoryId())) out.print("selected");%>  >
								<%=id%>
							</option>
					</logic:iterate>
					<logic:equal name="testResult" property="testKitInactive" value="true">
						<option value='<%=testResult.getTestKitInventoryId()%>' selected ><%=testResult.getTestKitInventoryId()%></option>
					</logic:equal>
				</html:select>
			</td>
		</logic:equal>
		<logic:equal name="testResult" property="resultDisplayType" value="SYPHILIS">
			<td valign="middle" class="ruled">
				<html:hidden name="testResult" property="testMethod" indexed="true"/>
				<bean:write name="testResult" property="testName"/>
				<logic:greaterThan name="testResult" property="reflexStep" value="0">
				&nbsp;--&nbsp;
				<bean:message key="reflexTest.step" />&nbsp;<bean:write name="testResult" property="reflexStep"/>
				</logic:greaterThan>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<bean:message key="inventory.testKit"/>
				<html:select name="testResult"
							 indexed="true"
							 tabindex='-1'
							 property="testKitInventoryId"
							 value='<%=testResult.getTestKitInventoryId()%>'
							 onchange='<%="markUpdated(" + index + ");"%>' >
					<logic:iterate id="id" indexId="index" collection="<%=syphilisKits%>">
							<option value='<%=id%>'  <%if(id.equals(testResult.getTestKitInventoryId())) out.print("selected");%>  >
								<%=id%>
							</option>
					</logic:iterate>
					<logic:equal name="testResult" property="testKitInactive" value="true">
						<option value='<%=testResult.getTestKitInventoryId()%>' selected ><%=testResult.getTestKitInventoryId()%></option>
					</logic:equal>
				</html:select>
			</td>
		</logic:equal>
		<logic:notEqual name="testResult" property="resultDisplayType" value="HIV"><logic:notEqual name="testResult" property="resultDisplayType" value="SYPHILIS">
			<td valign="middle" class="ruled">
				<bean:write name="testResult" property="testName"/>
				<logic:equal  name="bound"  value="true" >
					<br/><bean:write name="testResult" property="normalRange"/>&nbsp;
					<bean:write name="testResult" property="unitsOfMeasure"/>
				</logic:equal>
								<logic:greaterThan name="testResult" property="reflexStep" value="0">
				&nbsp;--&nbsp;
				<bean:message key="reflexTest.step" />&nbsp;<bean:write name="testResult" property="reflexStep"/>
				</logic:greaterThan>
			</td>
		</logic:notEqual></logic:notEqual>

		<td class="ruled" style='vertical-align: middle'>
		<% if( failedValidationMarks){ %>
		<logic:equal name="testResult" property="failedValidation" value="true">
			<img src="./images/validation-rejected.gif" />
		</logic:equal>
		<% } %>
		<logic:equal name="testResult" property="nonconforming" value="true">
			<img src="./images/nonconforming.gif" />
		</logic:equal>
		</td>
		<!-- result cell -->
		<td id='<%="cell_" + index %>' class="ruled">
			<logic:equal name="testResult" property="resultType" value="N">
			    <input type="text"
			           name='<%="testResult[" + index + "].resultValue" %>'
			           size="6"
			           value='<%= testResult.getResultValue() %>'
			           id='<%= "results_" + index %>'
                       class="testResultValue"
			           style='<%="background: " + (testResult.isValid() ? testResult.isNormal() ? "#ffffff" : "#ffffa0" : "#ffa0a0") %>'
			           title='<%= (testResult.isValid() ? testResult.isNormal() ? "" : StringUtil.getMessageForKey("result.value.abnormal") : StringUtil.getMessageForKey("result.value.invalid")) %>'
					   <%= testResult.isReadOnly() || testResult.isReferredOut() ? "disabled='disabled'" : ""%>
					   class='<%= (testResult.isReflexGroup() ? "reflexGroup_" + testResult.getReflexParentGroup()  : "")  +  (testResult.isChildReflex() ? " childReflex_" + testResult.getReflexParentGroup() : "") %> '
					   onchange='<%="validateResults( this," + index + "," + lowerBound + "," + upperBound + "," + lowerAbnormalBound + "," + upperAbnormalBound + ", \"XXXX\" );" +
						               "markUpdated(" + index + "); " +
						                (testResult.isReflexGroup() && !testResult.isChildReflex() ? "updateReflexChild(" + testResult.getReflexParentGroup()  +  " ); " : "") +
						                ( noteRequired && !"".equals(testResult.getResultValue())  ? "showNote( " + index + ");" : ""  ) +
						                ( testResult.isDisplayResultAsLog() ? " updateLogValue(this, " + index + ");" : "" ) +
						                 "; updateAbnormalCheck( isNormalForNumeric(this.value,"+lowerBound+","+upperBound+")," + index + ")" %>'/>

				<bean:write name="testResult" property="unitsOfMeasure"/>
			</logic:equal><logic:equal name="testResult" property="resultType" value="A">
				<app:text name="testResult"
						  indexed="true"
						  property="resultValue"
						  size="20"
						  disabled='<%= testResult.isReadOnly() || testResult.isReferredOut() %>'
                          style='<%="background: " + (testResult.isValid() ? testResult.isNormal() ? "#ffffff" : "#ffffa0" : "#ffa0a0") %>'
						  title='<%= (testResult.isValid() ? testResult.isNormal() ? "" : StringUtil.getMessageForKey("result.value.abnormal") : StringUtil.getMessageForKey("result.value.invalid")) %>'
						  styleId='<%="results_" + index %>'
                          styleClass="testResultValue"
						  onchange='<%="markUpdated(" + index + ");"  +
						               ((noteRequired && !"".equals(testResult.getResultValue()) ) ? "showNote( " + index + ");" : "")%>'/>
				<bean:write name="testResult" property="unitsOfMeasure"/>
			</logic:equal><logic:equal name="testResult" property="resultType" value="R">
				<!-- text results -->
				<app:textarea name="testResult"
						  indexed="true"
						  property="resultValue"
						  rows="2"
						  disabled='<%= testResult.isReadOnly() || testResult.isReferredOut() %>'
						  style='<%="background: " + (testResult.isValid() ? testResult.isNormal() ? "#ffffff" : "#ffffa0" : "#ffa0a0") %>'
						  title='<%= (testResult.isValid() ? testResult.isNormal() ? "" : StringUtil.getMessageForKey("result.value.abnormal") : StringUtil.getMessageForKey("result.value.invalid")) %>'
						  styleId='<%="results_" + index %>'
                          styleClass="testResultValue"
						  onkeyup='<%="markUpdated(" + index + ");"  +
						               ((noteRequired && !"".equals(testResult.getResultValue()) ) ? "showNote( " + index + ");" : "")%>'
						  />
			</logic:equal>
			<% if( "D".equals(testResult.getResultType()) || "Q".equals(testResult.getResultType()) ){ %>
			<!-- dictionary results -->
			<select name="<%="testResult[" + index + "].resultValue" %>"
			        onchange="<%= "updateAbnormalCheck(isNormalForDropDown(this.value, ["+ testResult.getAbnormalTestResultMap()+" ]),"+  index+" );" +
            "markUpdated(" + index + ", " + testResult.isUserChoiceReflex() +  ", \'" + testResult.getSiblingReflexKey() + "\') ; "   +
						               ((noteRequired && !"".equals(testResult.getResultValue()) )? "showNote( " + index + ");" : "") +
						               (testResult.getQualifiedDictonaryId() != null ? "showQuanitiy( this, "+ index + ", " + testResult.getQualifiedDictonaryId() + ");" :"") %>"
			        id='<%="results_" + index%>'
                    class="testResultValue"

            <%=testResult.isReadOnly() || testResult.isReferredOut() ? "disabled=\'true\'" : "" %> >
					<option value="0"></option>
					<logic:iterate id="optionValue" name="testResult" property="dictionaryResults" type="IdValuePair" >
						<option value='<%=optionValue.getId()%>'  <%if(optionValue.getId().equals(testResult.getResultValue())) out.print("selected"); %>  >
							<bean:write name="optionValue" property="value"/>
						</option>
					</logic:iterate>
			</select><br/>
			<input type="text"
			           name='<%="testResult[" + index + "].qualifiedResultValue" %>'
			           value='<%= testResult.getQualifiedResultValue() %>'
			           id='<%= "qualifiedDict_" + index %>'
			           style = '<%= "display:" + ("".equals(testResult.getQualifiedResultValue()) ? "none" : "inline") %>'
					   <%= (testResult.isReadOnly() || testResult.isReferredOut()) ? "disabled='disabled'" : ""%> />
			<% } %><logic:equal name="testResult" property="resultType" value="M">
			<!-- multiple results -->
			<select name="<%="testResult[" + index + "].multiSelectResultValues" %>"
					id='<%="results_" + index%>'
                    class="testResultValue"
					multiple="multiple"
					<%= testResult.isReadOnly() || testResult.isReferredOut()? "disabled=\'disabled\'" : "" %>
						 title='<%= StringUtil.getMessageForKey("result.multiple_select")%>'
						 onchange='<%="markUpdated(" + index + ");"  +
						               ((noteRequired && !GenericValidator.isBlankOrNull(testResult.getMultiSelectResultValues())) ? "showNote( " + index + ");" : "") %>' >
						<logic:iterate id="optionValue" name="testResult" property="dictionaryResults" type="IdValuePair" >
						<option value='<%=optionValue.getId()%>'
								<%if(StringUtil.textInCommaSeperatedValues(optionValue.getId(), testResult.getMultiSelectResultValues())) out.print("selected"); %>  >
							<bean:write name="optionValue" property="value"/>
						</option>
					</logic:iterate>
				</select>
				<html:hidden name="testResult" property="multiSelectResultValues" indexed="true" styleId='<%="multiresultId_" + index%>' />
			</logic:equal>
			<% if( testResult.isDisplayResultAsLog()){ %>
						<br/><input type='text'
								    id='<%= "log_" + index %>'
									disabled='disabled'
									style="color:black"
									value='<% try{
												Double value = Math.log10(Double.parseDouble(testResult.getResultValue()));
												DecimalFormat twoDForm = new DecimalFormat("##.##");
												out.print(Double.valueOf(twoDForm.format(value)));
												}catch(Exception e){
													out.print("--");} %>'
									size='6' /> log
					<% } %>
		</td>
        <td>
            <html:checkbox name='testResult'
                           styleClass="testValueAbnormal"
                           property="abnormal"
                           indexed="true"
                           styleId='<%="abnormalId_" + index %>'
            onchange='<%="markUpdated(" + index + ");"%>'/>
            <html:hidden property='<%="testResult["+index+"].abnormal"%>' value="false" styleId='<%="realAbnormalId_" + index %>'/> <!-- To submit checkbox value when unchecked -->
            <script language="JavaScript">
                enableOnlyForRemark(<%=index%>,'<%=testResult.getResultType()%>');
                initializeAbnormalValue(<%=index%>,'<%=testResult.getResultType()%>');
            </script>

        </td>
		<% if( ableToRefer ){ %>
		<td style="white-space: nowrap" class="ruled">
		<html:hidden name="testResult" property="referralId" indexed='true'/>
            <% if ((GenericValidator.isBlankOrNull(testResult.getReferralId()) || testResult.isReferralCanceled()) && (testResult.getResult() == null || testResult.isResultValueBlankOrNull())) {%>
            <html:checkbox name="testResult"
                           property="referredOut"
                           indexed="true"
                           styleId='<%="referralId_" + index %>'
                           styleClass="referralCheckBox"
                           onchange='<%="markUpdated(" + index + "); handleReferralCheckChange(this)" %>'/>
            <% } else {%>
            <html:checkbox name="testResult"
                           property="referredOut"
                           styleClass="referralCheckBox"
                           indexed="true"
                           disabled="true"/>
            <% } %>
			<select name="<%="testResult[" + index + "].referralReasonId" %>"
			        id='<%="referralReasonId_" + index%>'
                    class="referralReason"
					onchange='<%="markUpdated(" + index + "); handleReferralReasonAndInstituteChange(" + index + ")" %>'
                    <%= (testResult.isReferredOut() && ("0".equals(testResult.getReferralReasonId()) || "0".equals(testResult.getReferralOrganizationId()))) ? "" : "disabled='disabled'" %> >
                <option value='0' >
                    <logic:equal name="testResult" property="referralCanceled" value="true"  >
                        <bean:message key="referral.canceled" />
                    </logic:equal>
                </option>
			<logic:iterate id="optionValue" name='<%=formName %>' property="referralReasons" type="IdValuePair" >
					<option value='<%=optionValue.getId()%>'  <%if(optionValue.getId().equals(testResult.getReferralReasonId())) out.print("selected='selected'"); %>  >
							<bean:write name="optionValue" property="value"/>
					</option>
			</logic:iterate>
			</select>
		</td>
		<% } %>

		<td style="white-space: nowrap" class="ruled">
			<select name="<%="testResult[" + index + "].referralOrganizationId" %>"
					id='<%="referralOrganizationId_" + index%>'
					class="referralOrganization"
					onchange='<%="markUpdated(" + index + "); handleReferralReasonAndInstituteChange(" + index + ")" %>'
					<%= (testResult.isReferredOut() && ("0".equals(testResult.getReferralReasonId()) || "0".equals(testResult.getReferralOrganizationId()))) ? "" : "disabled='disabled'" %> >
                    <logic:equal name="testResult" property="referralCanceled" value="true"  >
                        <bean:message key="referral.canceled" />
                    </logic:equal>
				<logic:iterate id="optionValue" name='<%=formName %>' property="referralOrganizations" type="IdValuePair" >
					<option value='<%=optionValue.getId()%>'  <%if(optionValue.getId().equals(testResult.getReferralOrganizationId())) out.print("selected='selected'"); %>  >
						<bean:write name="optionValue" property="value"/>
					</option>
				</logic:iterate>
			</select>
		</td>

		<td align="left" class="ruled">
						 	<img src="./images/note-add.gif"
						 	     onclick='<%= "showHideNotes(this, " + index + ");" %>'
						 	     id='<%="showHideButton_" + index %>'
						    />
			<html:hidden property="hideShowFlag"  styleId='<%="hideShow_" + index %>' value="hidden" />
		</td>
        <td>
            <input type="file" name='<%="testResult["+index+"].uploadedFile"%>' onchange='<%="markUpdated(" + index + ");"%>'>
            <% if(testResult.getUploadedFileName() != null){ %>
                    <%  String filePath = testResult.getUploadedFileName();
                        String fileNameWithUUID = filePath.substring(filePath.lastIndexOf("/") + 1);
                        String fileName = fileNameWithUUID.substring(fileNameWithUUID.indexOf("_")+1);
                        fileName = new URLDecoder().decode(fileName, "UTF-8");
                        String uploadedFilesDirectory = new SiteInformationDAOImpl().getSiteInformationByName(UPLOADED_RESULTS_DIRECTORY).getValue();
                    %>
                <label><%= fileName %> </label>
                <a href='<%= uploadedFilesDirectory + testResult.getUploadedFileName()%>' target="_blank">Download</a>
            <% }%>

        </td>
	</tr>
	<logic:notEmpty name="testResult" property="pastNotes">
		<tr class='<%= rowColor %>' >
			<td colspan="2" align="right" valign="top"><bean:message key="label.prior.note" />: </td>
			<td colspan="6" align="left">
                <pre><%= testResult.getPastNotes() %></pre>
			</td>
		</tr>
	</logic:notEmpty>
	<tr id='<%="noteRow_" + index %>'
		class='<%= rowColor %>'
		style="display: none;">
		<td colspan="4" valign="top" align="right"><% if(noteRequired &&
														 !(GenericValidator.isBlankOrNull(testResult.getMultiSelectResultValues()) &&
														   GenericValidator.isBlankOrNull(testResult.getResultValue()))){ %>
													  <bean:message key="note.required.result.change"/>
													<% } else {%>
													<bean:message key="note.note"/>
													<% } %>
													:</td>
		<td colspan="6" align="left" >
			<html:textarea styleId='<%="note_" + index %>'
						   onchange='<%="markUpdated(" + index + ");"%>'
					   	   name="testResult"
			           	   property="note"
			           	   indexed="true"
			           	   rows="3" />
		</td>
	</tr>

	<logic:match name="testResult" property="userChoiceReflex"  value="true">
		<tr id='<%="reflexInstruction_" + index %>' class='<%= rowColor %>' style="display: none;">
            <td colspan="4" >&nbsp;</td>
			<td colspan="4" valign="top"  ><bean:message key="testreflex.actionselection.instructions" /></td>
		</tr>
		<tr id='<%="reflexSelection_" + index %>' class='<%= rowColor %>' style="display: none;">
		<td colspan="4" >&nbsp;</td>
		<td colspan="4" >
				<html:radio name="testResult"
						    styleId = '<%="selectionOne_" + index %>'
							property="reflexSelectionId"
							indexed="true"
							value=''
							onchange='<%="reflexChoosen(" + index + ", \'" + rowColor + "\', \'" + testResult.getSiblingReflexKey() +  "\');"%>' />
							<label id='<%="selectionOneLabel_" + index %>'  for='<%="selectionOne_" + index %>' > </label>
				<br/>
				<html:radio name="testResult"
							styleId = '<%="selectionTwo_" + index %>'
							property="reflexSelectionId"
							indexed="true"
							value=''
							onchange='<%="reflexChoosen(" + index + ", \'" + rowColor + "\', \'" + testResult.getSiblingReflexKey() +  "\');"%>' />
							<label id='<%="selectionTwoLabel_" + index %>'  for='<%="selectionTwo_" + index %>' > </label>
			</td>
		</tr>

	</logic:match>

	</logic:equal>
	</logic:iterate>
</Table>

<div class="results-page-block">
<logic:notEqual name="<%=formName%>" property="paging.totalPages" value="0">
	<html:hidden styleId="currentPageID" name="<%=formName%>" property="paging.currentPage"/>
	<bean:define id="total" name="<%=formName%>" property="paging.totalPages"/>
	<bean:define id="currentPage" name="<%=formName%>" property="paging.currentPage"/>

	<%if( "1".equals(currentPage)) {%>
		<input type="button" value='<%=StringUtil.getMessageForKey("label.button.previous") %>' style="width:100px;" disabled="disabled" >
	<% } else { %>
		<input type="button" value='<%=StringUtil.getMessageForKey("label.button.previous") %>' style="width:100px;" onclick="pager.pageBack();" />
	<% } %>
	<%if( total.equals(currentPage)) {%>
		<input type="button" value='<%=StringUtil.getMessageForKey("label.button.next") %>' style="width:100px;" disabled="disabled" />
	<% }else{ %>
		<input type="button" value='<%=StringUtil.getMessageForKey("label.button.next") %>' style="width:100px;" onclick="pager.pageFoward();"   />
	<% } %>

	&nbsp;
	<bean:write name="<%=formName%>" property="paging.currentPage"/> of
	<bean:write name="<%=formName%>" property="paging.totalPages"/>
</logic:notEqual>
</div>
</logic:notEqual>
<logic:equal name="testCount"  value="0">
<h2><bean:message key="result.noTestsFound"/></h2>
</logic:equal>


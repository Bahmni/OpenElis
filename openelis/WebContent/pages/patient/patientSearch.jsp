<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="us.mn.state.health.lims.common.action.IActionConstants,
			     us.mn.state.health.lims.common.util.SystemConfiguration,
			     us.mn.state.health.lims.common.formfields.FormFields,
			     us.mn.state.health.lims.common.formfields.FormFields.Field,
			     us.mn.state.health.lims.common.provider.validation.AccessionNumberValidatorFactory,
			     us.mn.state.health.lims.common.provider.validation.IAccessionNumberValidator,
			     us.mn.state.health.lims.common.util.StringUtil,
			     us.mn.state.health.lims.common.util.Versioning,
			     us.mn.state.health.lims.common.util.ConfigurationProperties,
			     us.mn.state.health.lims.common.util.ConfigurationProperties.Property" %>


<%@ taglib uri="/tags/struts-bean"		prefix="bean" %>
<%@ taglib uri="/tags/struts-html"		prefix="html" %>
<%@ taglib uri="/tags/struts-logic"		prefix="logic" %>
<%@ taglib uri="/tags/labdev-view"		prefix="app" %>
<%@ taglib uri="/tags/sourceforge-ajax" prefix="ajax"%>

<bean:define id="formName"	value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>' />
<bean:define id="localDBOnly" value='<%=Boolean.toString(ConfigurationProperties.getInstance().getPropertyValueLowerCase(Property.UseExternalPatientInfo).equals("false"))%>' />

<%!
	IAccessionNumberValidator accessionNumberValidator;
	boolean supportSTNumber = true;
	boolean supportMothersName = true;
	boolean supportSubjectNumber = true;
	boolean supportNationalID = true;
	boolean supportLabNumber = false;
	String basePath = "";
	boolean supportfirstNameFirst;
 %>

 <%
	supportSTNumber = FormFields.getInstance().useField(Field.StNumber);
 	supportMothersName = FormFields.getInstance().useField(Field.MothersName);
 	supportSubjectNumber = FormFields.getInstance().useField(Field.SubjectNumber);
 	supportNationalID = FormFields.getInstance().useField(Field.NationalID);
 	//The logic is like this because the special case is to override the form.  If the action doesn't set the attribute then the form wins
 	supportLabNumber = FormFields.getInstance().useField(Field.SearchPatientWithLabNo) &&
 						(request.getAttribute(IActionConstants.ACCESSION_SEARCH_WITH_PATIENT) == null ||
 						request.getAttribute(IActionConstants.ACCESSION_SEARCH_WITH_PATIENT) == IActionConstants.TRUE);
	accessionNumberValidator = new AccessionNumberValidatorFactory().getValidator();
	String path = request.getContextPath();
    basePath = path + "/";
	supportfirstNameFirst = FormFields.getInstance().useField(Field.FirstNameFirst);
  %>

<link rel="stylesheet" href="css/bahmni.css?ver=<%= Versioning.getBuildNumber() %>">

<script type="text/javascript" src="<%=basePath%>scripts/ajaxCalls.js?ver=<%= Versioning.getBuildNumber() %>" ></script>
<script type="text/javascript" language="JavaScript1.2">

var supportSTNumber = <%= supportSTNumber %>;
var supportMothersName = <%= supportMothersName %>;
var supportSubjectNumber = <%= supportSubjectNumber %>;
var supportNationalID = <%= supportNationalID %>;
var supportLabNumber = <%= supportLabNumber %>;
var patientSelectID;
var patientInfoHash = new Array();
var patientChangeListeners = new Array();
var localDB = '<%=localDBOnly%>' == "true";
var newSearchInfo = false;
var supportfirstNameFirst = <%=supportfirstNameFirst %>;

function searchPatients()
{
	newSearchInfo = false;

	var results = $("resultsDiv");
	if( results ){
		results.hide();
	}

	var lastName = $("searchLastNameID").value;
	var firstName = $("searchFirstNameID").value;
	var middleName = $("searchMiddleNameID").value;
    var STNumber = supportSTNumber ? $("searchSTID").value : "";
    var subjectNumber = supportSubjectNumber ? $("searchSubjectNumberID").value : "";
    var nationalID = supportNationalID ? $("searchNationalID").value : "";
    var labNumber = supportLabNumber ? $("searchLabNumber").value : "";
	patientSearch(lastName, firstName, middleName, STNumber, subjectNumber, nationalID, labNumber, processSearchSuccess, processSearchFailure);
}

function processSearchSuccess(xhr)
{
	//alert( xhr.responseText );
	var formField = xhr.responseXML.getElementsByTagName("formfield").item(0);
	var message = xhr.responseXML.getElementsByTagName("message").item(0);
	var table = $("searchResultTable");

	clearTable(table);
	clearPatientInfoCache();

	if( message.firstChild.nodeValue == "valid" )
	{
		$("noPatientFound").hide();
		$("searchResultsDiv").show();

		var resultNodes = formField.getElementsByTagName("result");

		for( var i = 0; i < resultNodes.length; i++ )
		{
			addPatientToSearch( table, resultNodes.item(i) );
//		    if (resultNodes.length === 1) {
//		        doSelectPatient();
//		    }
		}
	}else
	{
		hideIfExists("searchResultsDiv");
		hideIfExists('PatientDetail');
		showIfExists('noPatientFound');
		selectPatient( null );
	}
}

function hideIfExists(id){
	$(id)&&$(id).hide();
}
function showIfExists(id){
	$(id)&&$(id).show();
}

function processSearchFailure(xhr)
{
	//alert( xhr.responseText );
	alert("<bean:message key="error.system"/>");
}

function clearTable(table){
	var rows = table.rows.length - 1;
	while( rows > 0 ){
		table.deleteRow( rows-- );
	}
}

function clearPatientInfoCache(){
	patientInfoHash = new Array();
}

function addPatientToSearch(table, result ){
	var patient = result.getElementsByTagName("patient")[0];

	var firstName = getValueFromXmlElement( patient, "first");
	var middleName = getValueFromXmlElement( patient, "middle");
	var lastName = getValueFromXmlElement( patient, "last");
	var gender = getValueFromXmlElement( patient, "gender");
	var DOB = getValueFromXmlElement( patient, "dob");
	var stNumber = getValueFromXmlElement( patient, "ST");
	var subjectNumber = getValueFromXmlElement( patient, "subjectNumber");
	var nationalID = getValueFromXmlElement( patient, "nationalID");
	var mother = getValueFromXmlElement( patient, "mother");
	var pk = getValueFromXmlElement( result, "id");
	var dataSourceName = getValueFromXmlElement( result, "dataSourceName");


	var row = createRow( table, firstName, middleName, lastName, gender, DOB, stNumber, subjectNumber, nationalID, mother, pk, dataSourceName );
	addToPatientInfo( firstName, lastName, gender, DOB, stNumber, subjectNumber, nationalID, mother, pk );

	if( row == 1 ){
		patientSelectID = pk;
		$("sel_1").checked = "true";
		selectPatient( pk );
	}
}

function getValueFromXmlElement( parent, tag ){
	var element = parent.getElementsByTagName( tag ).item(0);

	return element ? element.firstChild.nodeValue : "";
}

function createRow(table, firstName, middleName, lastName, gender, DOB, stNumber, subjectNumber, nationalID, mother, pk,  dataSourceName){

		var row = table.rows.length;

		var newRow = table.insertRow(row);

		newRow.id = "_" + row;

		var cellCounter = -1;

		var selectionCell = newRow.insertCell(++cellCounter);
        var stCell = supportSTNumber ? newRow.insertCell(++cellCounter) : null;
        if (supportfirstNameFirst) {
            var firstNameCell = newRow.insertCell(++cellCounter);
            var middleNameCell = newRow.insertCell(++cellCounter);
            var lastNameCell = newRow.insertCell(++cellCounter);
        } else {
            var lastNameCell = newRow.insertCell(++cellCounter);
            var firstNameCell = newRow.insertCell(++cellCounter);
            var middleNameCell = newRow.insertCell(++cellCounter);
        }
		var genderCell = newRow.insertCell(++cellCounter);
		var dobCell = newRow.insertCell(++cellCounter);
		var motherCell = supportMothersName ? newRow.insertCell(++cellCounter) : null;
		var subjectNumberCell = supportSubjectNumber ? newRow.insertCell(++cellCounter) : null;
		var nationalCell = supportNationalID ? newRow.insertCell(++cellCounter) : null;

		selectionCell.innerHTML = getSelectionHtml( row, pk );
		lastNameCell.innerHTML = nonNullString( lastName );
		firstNameCell.innerHTML = nonNullString( firstName );
		middleNameCell.innerHTML = nonNullString(middleName);
		genderCell.innerHTML = nonNullString( gender );
		if( supportSTNumber){stCell.innerHTML = nonNullString( stNumber );}
		if( supportSubjectNumber){subjectNumberCell.innerHTML = nonNullString( subjectNumber );}
		if( supportNationalID){nationalCell.innerHTML = nonNullString( nationalID );}

		dobCell.innerHTML = nonNullString( DOB );
		if(supportMothersName){motherCell.innerHTML = nonNullString( mother );}

		return row;
}

function getSelectionHtml( row, key){
	return "<input name='selPatient' id='sel_" + row + "' value='" + key + "' onclick='selectPatient(this.value)' type='radio'>";
}

function /*String*/ nonNullString( target ){
	return target == "null" ? "" : target;
}

function addToPatientInfo( firstName, lastName, gender, DOB, stNumber, subjectNumber, nationalID, mother, pk ){
	var info = new Array();
	info["first"] = nonNullString( firstName );
	info["last"] = nonNullString( lastName );
	info["gender"] = nonNullString( gender );
	info["DOB"] = nonNullString( DOB );
	info["ST"] = nonNullString( stNumber );
	info["subjectNumber"] = nonNullString( subjectNumber );
	info["national"] = nonNullString( nationalID );
	info["mother"] = nonNullString( mother );

	patientInfoHash[pk] = info;
}


function selectPatient( patientID ){

	if( patientID ){
		patientSelectID = patientID;

		var info = patientInfoHash[patientID];

		for(var i = 0; i < patientChangeListeners.length; i++){
			patientChangeListeners[i](info["first"],info["last"],info["gender"],info["DOB"],info["ST"],info["subjectNumber"],info["national"],info["mother"], patientID);
		}

	}else{
		for(var i = 0; i < patientChangeListeners.length; i++){
			patientChangeListeners[i]("","","","","","","","", null);
		}
	}
}

function /*void*/ addPatientChangedListener( listener ){
	patientChangeListeners.push( listener );
}


function doSelectPatient(){	
	var form = document.forms[0];

	form.action = "PatientResults.do?patientID="  + patientSelectID;

	form.submit();
}

function /*void*/ handleEnterEvent( ){
		
		if( newSearchInfo ){
			searchPatients();
		}
		return false;
}

function /*void*/ dirtySearchInfo(e){ 
	var code = e ? e.which : window.event.keyCode;
	if( code != 13 ){
		newSearchInfo = true; 
	}
}			
</script>

<div id="PatientPage" class="colorFill" style="display:inline" >

	<h2><bean:message key="sample.entry.search"/></h2>

	<table width="70%">
	<tr >
        <% if(supportfirstNameFirst) { %>
            <td class="searchFirstName" width="25%">
                <bean:message key="patient.epiFirstName"/>
            </td>
            <td class="searchMiddleName" width="25%">
                <bean:message key="patient.epiMiddleName"/>
            </td>
            <td class="searchLastName" width="30%">
                <bean:message key="patient.epiLastName"/>
            </td>
        <% } else { %>
            <td class="searchLastName" width="30%">
                <bean:message key="patient.epiLastName"/>
            </td>
            <td class="searchFirstName" width="25%">
                <bean:message key="patient.epiFirstName"/>
            </td>
            <td class="searchMiddleName" width="25%">
                <bean:message key="patient.epiMiddleName"/>
            </td>
        <% } %>
		<% if( supportSTNumber ){ %>
            <td class="searchST" width="20%">
                <bean:message key="patient.ST.number"/>
            </td>
        <%} %>
		<% if( supportSubjectNumber ){ %>
            <td width="20%">
                <%=StringUtil.getContextualMessageForKey("patient.subject.number") %>
            </td>
		<%} %>
		<% if( supportNationalID ){ %>
            <td class="nationalID" width="20%">
                <%=StringUtil.getContextualMessageForKey("patient.NationalID") %>
            </td>
		<%} %>
		<% if( supportLabNumber ){ %>
            <td width="20%">
                <%=StringUtil.getContextualMessageForKey("resultsentry.accessionNumber") %>
            </td>
		<%} %>
		<td></td>
	</tr>

	<tr>
	<% if(supportfirstNameFirst) { %>
        <td class="searchFirstName">
            <input name="searchFirstName" size="30" value="" id="searchFirstNameID" class="text" type="text" onkeyup="dirtySearchInfo( event )" >
        </td>
        <td class="searchMiddleName">
            <input name="searchMiddleName" size="30" value="" id="searchMiddleNameID" class="text" type="text" onkeyup="dirtySearchInfo( event )" >
        </td>
        <td class="searchLastName" >
            <input name="searchLastName" size="30" value="" id="searchLastNameID" class="text" type="text" onkeyup="dirtySearchInfo( event )">
        </td>
    <%} else {%>
        <td class="searchLastName" >
          <input name="searchLastName" size="30" value="" id="searchLastNameID" class="text" type="text" onkeyup="dirtySearchInfo( event )">
        </td>
        <td class="searchFirstName">
          <input name="searchFirstName" size="30" value="" id="searchFirstNameID" class="text" type="text" onkeyup="dirtySearchInfo( event )" >
        </td>
        <td class="searchMiddleName">
          <input name="searchMiddleName" size="30" value="" id="searchMiddleNameID" class="text" type="text" onkeyup="dirtySearchInfo( event )" >
        </td>
    <% } %>

	<% if(supportSTNumber){ %>
	<td class="searchST">
		<input name="searchST" size="15" value="" id="searchSTID" class="text" type="text" onkeyup="dirtySearchInfo( event )" autofocus="autofocus">
	</td>
	<% } %>
	<% if(supportSubjectNumber){ %>
	<td>
		<input name="searchSubjectNumber" size="15" value="" id="searchSubjectNumberID" class="text" type="text" onkeyup="dirtySearchInfo( event )" >
	</td>
	<% } %>
	<% if(supportNationalID){ %>
	<td>
		<input name="searchNationalID" size="15" value="" id="searchNationalID" class="text nationalID" type="text" onkeyup="dirtySearchInfo( event )" >
	</td>
	<% } %>
	<% if(supportLabNumber){ %>
	<td>
		<input name="searchLabNumber"
			   size="15"
			   value=""
			   id="searchLabNumber"
			   class="text"
			   maxlength="<%= Integer.toString(accessionNumberValidator.getMaxAccessionLength()) %>"
			   type="text"  
			   onkeyup="dirtySearchInfo( event )" >
	</td>
	<% } %>
	
	<td class="searchButton" ><html:button property="searchButton" onclick="searchPatients()" styleId="searchButton" >
			<bean:message key="sample.entry.runSearch"/>
		</html:button>
	</td>
	</tr>
</table>
<div id="noPatientFound" align="center" style="display: none" >
	<p class="results-message"><bean:message key="patient.search.not.found"/></p>
</div>
	<div id="searchResultsDiv" class="colorFill" style="display: none;" >
                <table id="searchResultTable" style="width:100%">
                <tr>
                <th width="2%"></th>
                <% if(supportSTNumber){ %>
                <th width="12%">
                    <bean:message key="patient.ST.number"/>
                </th>
                <% } %>
                <% if (supportfirstNameFirst) { %>
                <th class="firstName" width="15%">
                    <bean:message key="patient.epiFirstName"/>
                </th>
                <th class="middleName" width="15%">
                    <bean:message key="patient.epiMiddleName"/>
                </th>
                <th class="lastName" width="18%">
                    <bean:message key="patient.epiLastName"/>
                </th>
                <% } else {%>
                <th class="lastName" width="18%">
                    <bean:message key="patient.epiLastName"/>
                </th>
                <th class="firstName" width="15%">
                    <bean:message key="patient.epiFirstName"/>
                </th>
                <th class="middleName" width="15%">
                    <bean:message key="patient.epiMiddleName"/>
                </th>
                <% } %>
				<th width="5%">
					<bean:message key="patient.gender"/>
				</th>
				<th width="11%">
					<bean:message key="patient.birthDate"/>
				</th>
				<% if( supportMothersName ){ %>
				<th width="20%">
					<bean:message key="patient.mother.name"/>
				</th>
                <% } %>
                <% if(supportSubjectNumber){ %>
                <th width="12%">
                <bean:message key="patient.subject.number"/>
				</th>
				<% } %>
				<% if(supportNationalID){ %>
				<th width="12%">
					<%=StringUtil.getContextualMessageForKey("patient.NationalID") %>
				</th>
				<% } %>
			</tr>
		</table>
		<br/>
		<logic:notEmpty name="<%=formName%>" property="buttonText" >
			<html:button property="retrieveTestsButton" onclick="doSelectPatient();" styleId="selectPatientButtonID">
				<bean:write name="<%=formName%>" property="buttonText" />
			</html:button>
		</logic:notEmpty>
	</div>
</div>




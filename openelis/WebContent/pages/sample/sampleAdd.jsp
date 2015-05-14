<%@ page language="java" contentType="text/html; charset=utf-8"
	import="us.mn.state.health.lims.common.action.IActionConstants,
	        us.mn.state.health.lims.common.formfields.FormFields,
	        us.mn.state.health.lims.common.formfields.FormFields.Field,
	        us.mn.state.health.lims.common.util.IdValuePair,
            us.mn.state.health.lims.common.util.Versioning,
	        us.mn.state.health.lims.common.util.StringUtil"%>

<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/labdev-view" prefix="app"%>

<bean:define id="formName" value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>' />
<bean:define id="entryDate" name="<%=formName%>" property="currentDate" />
<bean:define id="patientId" value='<%= request.getParameter("patientId") == null || request.getParameter("patientId").isEmpty() ? "" : request.getParameter("patientId")%>' />




<%!String path = "";
	String basePath = "";
	String sampleId = "";
	boolean useCollectionDate = true;
	boolean useInitialSampleCondition = false;
	boolean useCollector = false;
%>
<%
	path = request.getContextPath();
	basePath = path + "/";
	useCollectionDate = FormFields.getInstance().useField(Field.CollectionDate);
	useInitialSampleCondition = FormFields.getInstance().useField(Field.InitialSampleCondition);
	useCollector = FormFields.getInstance().useField(Field.SampleEntrySampleCollector);
	sampleId = request.getParameter("id");
%>

<script type="text/javascript" src="<%=basePath%>scripts/utilities.jsp"></script>
<script type="text/javascript" src="scripts/jquery.asmselect.js?ver=<%= Versioning.getBuildNumber() %>"></script>
<script type="text/javascript" src="scripts/ajaxCalls.js?ver=<%= Versioning.getBuildNumber() %>"></script>

<link rel="stylesheet" type="text/css" href="css/jquery.asmselect.css?ver=<%= Versioning.getBuildNumber() %>" />



<script type="text/javascript" >

var useCollectionDate = <%= useCollectionDate %>;
var useInitialSampleCondition = <%= useInitialSampleCondition  %>;
var useCollector = <%= useCollector %>;
var currentCheckedType = -1;
var currentTypeForTests = -1;
var selectedRowId = -1;
var sampleChangeListeners = new Array();
var sampleIdStart = 0;
var labOrderType = "none"; //if set will be done by other tiles
var sampleTypesForSample = new Array() ;
var samplesTags;
var sampleId = "<%= sampleId %>";
var sampleTypeSelectedIds= new Array();


		function /*void*/ addSampleChangedListener( listener ){
	sampleChangeListeners.push( listener );
}

function /*void*/ notifyChangeListeners(){
	for(var i = 0; i < sampleChangeListeners.length; i++){
			sampleChangeListeners[i]();
	}
}

$jq(document).ready(function() {
	if(sampleId != "null"){
		$jq("#searchSTID").val('<%=patientId%>');
		$jq("#searchButton").click();
		getSampleTypesAndTestsForSample(sampleId, processGetSampleTypesAndTestsSuccess, processGetTestFailure);
		$jq("#select_1").attr("checked","true");
		sampleClicked("1");
	}
});



function addNewSamples(){

	$("samplesAdded").show();

	var addTable = $("samplesAddedTable");
	var typeElement = $("sampleTypeSelect");
	var typeIndex = typeElement.selectedIndex;
	var sampleDescription = typeElement.options[typeIndex].text;
	var sampleTypeValue = typeElement.options[typeIndex].value;
	var currentTime = getCurrentTime();

	addTypeToTable(addTable, sampleDescription, sampleTypeValue, currentTime, '<%=entryDate%>' );

	notifyChangeListeners();
	
	testAndSetSave();
	$jq("html, body").animate({ scrollTop: $jq("#samplesDisplay").offset().top }, 500);
}

function testAndSetSave(){
	//N.B. This is bogus and the saving issues need to be sorted out.
	// Story https://www.pivotaltracker.com/story/show/31485441 
	if(window.setSave){
		setSave();
	}else if(window.setSaveButton){
		setSaveButton();
	}
}

function addTypeToTable(table, sampleDescription, sampleType, currentTime, currentDate ) {
		var rowLength = table.rows.length;
		var selectRow = true;
		var rowLabel = rowLength == 1 ? 1 : parseInt(table.rows[rowLength - 1].id.substr(1)) + 1;
		var newRow = table.insertRow(rowLength);

		var cellCount = 0;
		newRow.id = "_" + rowLabel;


		var selectionBox = newRow.insertCell(cellCount);
		var sampleId = newRow.insertCell(++cellCount);
		var type = newRow.insertCell(++cellCount);

		if( useInitialSampleCondition ){
			var newMulti = $("prototypeID").parentNode.cloneNode(true);
			var selection = newMulti.getElementsByTagName("select")[0];
			selection.id = "initialCondition_" + rowLabel;

			var initialConditionCell = newRow.insertCell(++cellCount);
			initialConditionCell.innerHTML = newMulti.innerHTML.replace("initialSampleConditionList", "formBreaker");

			$jq("#initialCondition_" + rowLabel).asmSelect({	removeLabel: "X"});
		}

		if( useCollectionDate ){
			var collectionDate = newRow.insertCell(++cellCount);
			var collectionTime = newRow.insertCell(++cellCount);
		}
		if( useCollector ){
			var collector = newRow.insertCell(++cellCount);
		}
		var tests = newRow.insertCell(++cellCount);
		var remove = newRow.insertCell(++cellCount);

		selectionBox.innerHTML = getCheckBoxHtml( rowLabel, selectRow );
		sampleId.innerHTML = getSampleIdHtml(rowLabel);
		type.innerHTML = getSampleTypeHtml( rowLabel, sampleDescription, sampleType );
		if( useCollectionDate ){
			collectionDate.innerHTML = getCollectionDateHtml( rowLabel, currentDate );
			collectionTime.innerHTML = getCollectionTimeHtml( rowLabel, currentTime );
		}
		if( useCollector ){
			collector.innerHTML = getCollectorHtml( rowLabel);
		}
		tests.innerHTML = getTestsHtml( rowLabel );
		remove.innerHTML = getRemoveButtonHtml( rowLabel );

		if( selectRow ){
			sampleClicked( rowLabel );
		}
	}


function getCheckBoxHtml( row, selectRow ){
	return "<input type='radio' name='sampleSelect' id='select_" + row + "' onclick='sampleClicked(" + row + " )' " +
			(selectRow ? "CHECKED" : " ") + " >";
}

function getSampleIdHtml(row){
	return "<input name='sequence' size ='4' value='" + (parseInt(row) + parseInt(sampleIdStart) ) + "' id='sequence_" + row + "' class='text' type='text'  disabled='disabled' >";
}

function getSampleTypeHtml(  row, sampleDescription, sampleType ){
	return   sampleDescription + "<input name='sampleType' id='typeId_" + row + "'value='" + sampleType + "' type='hidden'>";
}

function getCollectionDateHtml( row, date ){
	return "<input name='collectionDate' maxlength='10' size ='12' value='" + date + "'onchange='checkLabID(this);' id='collectionDate_" + row + "' class='text' type='text'>";
}

function getCollectionTimeHtml( row, time ){
	return "<input name='collectionTime' maxlength='10' size ='12' value='" + time + "' onchange='checkLabID(this);' id='collectionTime_" + row + "' class='text' type='text'>";
}

function getTestsHtml(row){
	return "<input id='testIds_" + row + "' type='hidden'>" +
	       "<input id='panelIds_" + row + "' type='hidden'>" +
	       "<textarea name='tests' id='tests_" + row + "' cols='65' class='text' readonly='true'  />";

}

function getCollectorHtml(row){
	return "<input name='collector'  value='' id='collector_" + row + "' class='text' type='text'>";

}
function getRemoveButtonHtml( row ){
	return "<input name='remove' value='" + "<bean:message key="sample.entry.remove.sample"/>" + "' class='textButton' onclick='removeRow(" + row + ");setSave();' id='removeButton_" + row +"' type='button' >";
}

function getCurrentTime(){
	var date = new Date();

	return (formatToTwoDigits(date.getHours()) + ":"  + formatToTwoDigits(date.getMinutes()));
}

function formatToTwoDigits( number ){
	return number > 9 ? number : "0" + number;
}

function removeAllRows(){
	var table = $("samplesAddedTable");
	var rows = table.rows.length;

	for( var i = rows - 1; i > 0; i--){
		table.deleteRow( i );
	}

	$("samplesAdded").hide();
}

function removeRow( row ){
	var checkedRowRemoved = false;
	var table = $("samplesAddedTable");
	var rowID = "_" + row;
	var rows = table.rows;


	for( var i = rows.length - 1; i > 0; i--){
		if( rows[i].id == rowID ){
			checkedRowRemoved = $("select" + rowID).checked;
			table.deleteRow( i );
			break;
		}
	}

	if( rows.length == 1 ){
		$("samplesAdded").hide();
	}else if( checkedRowRemoved){
		$("select" + rows[1].id).checked = true;
		sampleClicked( rows[1].id.sub('_', '') );
	}
	
	testAndSetSave();
}

function loadSamples(){
	var xml = convertSamplesToXml();
	$("sampleXML").value = xml;
}

function convertSamplesToXml(){
	var rows = $("samplesAddedTable").rows;

	var xml = "<?xml version='1.0' encoding='utf-8'?><samples>";

	for( var i = 1; i < rows.length; i++ ){
		xml = xml + convertSampleToXml( rows[i].id );
	}

	xml = xml + "</samples>";

	return xml;
}

function convertSampleToXml( id ){
	var dateID = "collectionDate" + id;
	var timeID = "collectionTime" + id;
	var typeID = "typeId" + id;
	var panelID = "panelIds" + id;
	var testID = "testIds" + id;
	var collectorID = "collector" + id;

	var xml = "<sample sampleID='" + $(typeID).value +
			  "' date='" + (useCollectionDate ? $(dateID).value : '') +
			  "' time='" + (useCollectionDate ? $(timeID).value : '') +
			  "' collector='" + (useCollector ? $(collectorID).value : '') +
			  "' tests='" + $(testID).value +
			  "' panels='" + $(panelID).value + "'";

	if( useInitialSampleCondition ){
		var initialConditions = $("initialCondition" + id);
		var optionLength = initialConditions.options.length;
		xml += " initialConditionIds=' ";
		for( var i = 0; i < optionLength; ++i ){
			if( initialConditions.options[i].selected ){
				xml += initialConditions.options[i].value + ",";
			}
		}

		xml =  xml.substring(0,xml.length - 1);
		xml += "'";
	}

	xml +=  " />";

	return xml;
}


function sampleTypeSelected( element ){
	if(sampleTypeSelectedIds.contains($jq("#sampleTypeSelect").val())){
		$("addSampleButton").disabled = true;
		return;
	}
	currentTypeIndex = element.selectedIndex;
	$("addSampleButton").disabled = currentTypeIndex == 0;
}

function sampleClicked( id ){
	selectedRowId = id;
	var checkedTypeValue = $("typeId_" + id).value;
	currentCheckedType = checkedTypeValue;

	editSelectedTest();
}


function processGetTestSuccess(xhr){
   //alert(xhr.responseText);
   var response = xhr.responseXML.getElementsByTagName("formfield").item(0);

   var testTable = $("addTestTable");
   clearTable( testTable );
   var tests = response.getElementsByTagName("test");
   for( var i = 0; i < tests.length; i++ ){
   		insertTestIntoTestTable( tests[i], testTable );
   }


   var panelTable = $("addPanelTable");
   clearTable( panelTable );
   var panels = response.getElementsByTagName("panel");
   for( var i = 0; i < panels.length; i++ ){
   		insertPanelIntoPanelTable( panels[i], panelTable );
   }

	$("testSelections").show();

	setSampleTests();
}

function populateTheSelectedTests(i) {
		var testsBySample = samplesTags[i].getElementsByTagName("test");
		var inputs = $("addTestTable").getElementsByTagName("input");
		for (var j = 0; j < testsBySample.length; j++) {
			var testId = testsBySample[j].innerHTML;
			for (var k = 0; k < inputs.length; k++) {
				var input = inputs[k];
				if (input.value.toString() === testId) {
					if (!input.checked){
						var checkbox_id = "test_"+input.id.split("_")[1];
						inputs[checkbox_id].checked = true;
						break;
					}
				}
			}
		}
		assignTestsToSelected();
}

function populateSelectedPanels(i) {
	var panels = samplesTags[i].getElementsByTagName("panel");
	for (var i=0; i< panels.length;i++){
		$jq('input[value='+panels[i].innerHTML+']')[0].click();
	}
}

function processGetSampleTypesAndTestsSuccess(xhr){
	samplesTags = xhr.responseXML.getElementsByTagName("sample");
	for(var i=0; i< samplesTags.length; i++){
		var sampleType = samplesTags[i].getElementsByTagName("sampleType")[0].innerHTML;
		sampleTypeSelectedIds.push(sampleType);
		$jq("select#sampleTypeSelect").val(parseInt(sampleType));
		addNewSamples();
		sampleClicked(i+1)
		populateTheSelectedTests(i);
		populateSelectedPanels(i);
	}
}

function insertTestIntoTestTable( test, testTable ){
	var name = getValueFromXmlElement( test, "name" );
	var id = getValueFromXmlElement( test, "id" );
	var userBench = "true" == getValueFromXmlElement( test, "userBenchChoice" );
	var row = testTable.rows.length;
	//var newrow= testTable.insertRow(row).incertCell(0);

	if($jq("#addTestTable ul").length ==0){
		$jq("#addTestTable").append("<tr><td colspan='3'><ul id='addsample-list'></td></tr>")
		//var newrow= testTable.insertRow(row).incertCell(0);
	}
	var row = $jq("#addsample-list li").length;
	//var nominalRow = row - 1;
	var nominalRow =row;
	var newRow=$jq("<li></li>");
	$jq("#addsample-list").append(newRow);
	//var newRow = testTable.insertRow(row);
	//var selectionCell = newRow.insertCell(0);
	//var nameCell = newRow.insertCell(1);
	//var selectionCell;

	var selectionCell =$jq("<span class='checkbox'></span>");
	newRow.append(selectionCell);

	var nameCell = $jq("<span class='checkbox-label'></span>");
	newRow.append(nameCell);

	var idli="availRow_" + nominalRow;
	newRow.attr('id',idli); 

	//selectionCell.innerHTML = getCheckBoxesHtml( nominalRow, userBench );
	//nameCell.innerHTML = getTestDisplayRowHtml( name, id, nominalRow );
selectionCell.html(getCheckBoxesHtml( nominalRow, userBench ));
nameCell.html(getTestDisplayRowHtml( name, id, nominalRow ));

	/*if( userBench ){
		$("sectionHead").show();
		selectionCell = newRow.insertCell(2);
		selectionCell.id = "testSection_" + nominalRow;
		var selectionClone = $jq("#sectionPrototype").clone().show();
		selectionCell.innerHTML = selectionClone.html();
	}*/
}

function insertPanelIntoPanelTable( panel, panelTable ){

	var name = getValueFromXmlElement( panel, "name" );
	var id = getValueFromXmlElement( panel, "id");
	var testMap = getValueFromXmlElement( panel, "testMap" );


	//This sillyness is because a single value will be interperted as an array size rather than a member
	if( testMap.indexOf( "," ) == -1 ){
		testMap += ", " + testMap;
	}

	var row = panelTable.rows.length;
	var nominalRow = row - 1;
	var newRow = panelTable.insertRow(row);

	var selectionCell = newRow.insertCell(0);
	var nameCell = newRow.insertCell(1);

	selectionCell.innerHTML = getPanelCheckBoxesHtml(testMap, nominalRow, id );
	nameCell.innerHTML = "<label for='panel_" + nominalRow + "'> " + name + "</label>";
}

function getCheckBoxesHtml( row, userBench){
	var benchUpdate = userBench ? "setUserSectionSelection(this, \'" + row + "\');" : " ";
	return "<input name='testSelect' id='test_" + row + "' type='checkbox' onclick=\"assignTestsToSelected();" + benchUpdate + "\" >";
}

function getPanelCheckBoxesHtml(map, row, id ){
	return "<input name='panelSelect' id='panel_" + row + "' onclick='panelSelected(this, new Array(" + map + "),\"" + id + "\" );assignTestsToSelected( this, \"" + id + "\")' type='checkbox' value='"+ id + "'>";
}

function getTestDisplayRowHtml( name, id, row ){
	return "<label for='test_" + row + "'>" + name + "</label>" + "<input name='testName' value='" + id + "' id='testName_" + row  + "' type='hidden' style='display:none'>";
}



function processGetTestFailure(xhr){
  // alert(xhr.responseText);
}

function getValueFromXmlElement( parent, tag ){
	var element = parent.getElementsByTagName( tag );

	return (element && element.length > 0 )  ? element[0].childNodes[0].nodeValue : "";
}

function clearTable(table){
    $("sectionHead").hide();
	var rows = table.rows.length - 1;
	while( rows > 0 ){
		table.deleteRow( rows-- );
	}
}

function deselectAllTests(){
	deselecAllTestsForList( "addTestTable" );
	deselecAllTestsForList( "addPanelTable" );
}

function deselecAllTestsForList( table ){
	var testTable = $(table);
	var inputs = testTable.getElementsByTagName( "input" );

	for( var i = 0; i < inputs.length; i = i + 2 ){
		 inputs[i].checked = false;
	}
}

function setUserSectionSelection(testCheckbox, row){
	if( testCheckbox.checked){
		$jq("#testSection_" + row + " select").removeAttr("disabled");
		$jq("#testSection_" + row + " span").css("visibility", "visible");
	}else{
		$jq("#testSection_" + row + " select").attr("disabled", "disabled");
		$jq("#testSection_" + row + " span").css("visibility", "hidden");
	}	
}

function assignTestsToSelected(checkbox, panelId){
	var testTable = $("addTestTable");
	var panelTable = $("addPanelTable");
	var choosenTests = new Array();
	var choosenIds = new Array();
	var choosenPanelIds = new Array();
	var i;
	var displayTests = "";
	var testIds = "";
	var panelIds = "";

	var inputs = testTable.getElementsByTagName( "input" );

	for( i = 0; i < inputs.length; i = i + 2 ){
		if( inputs[i].checked ){
			//this is fragile.  It depends on the code in getTestDisplayRowHtml()
			choosenTests.push( inputs[i+1].parentNode.firstChild.firstChild.wholeText );
			choosenIds.push( inputs[i+1].value );
		}
	}

	if( choosenTests.length > 0 ){
		displayTests = choosenTests[0]; //this is done to get the ',' right
		testIds = choosenIds[0];
		for( var i = 1; i < choosenTests.length; i++ ){
			displayTests += ", " + choosenTests[i];
			testIds += ", " + choosenIds[i];
		}
	}

	$("tests_" + selectedRowId).value = choosenTests;
	$("testIds_" + selectedRowId).value = choosenIds;

	if( checkbox){
		var panelIdElement = $("panelIds_" + selectedRowId);
		
		if( checkbox.checked ){
			if(panelIdElement.value.indexOf(panelId) === -1)
                panelIdElement.value = panelIdElement.value + panelId + ",";
		}else{
			panelIdElement.value = panelIdElement.value.replace( panelId + ",", "");
		}
	}
	testAndSetSave();
}

function editSelectedTest( ){
	if( currentCheckedType == -1 || currentTypeForTests != currentCheckedType  ){
    	getTestsForSampleType(currentCheckedType, labOrderType, processGetTestSuccess, processGetTestFailure); //this is an asychronise call and setSampleType will be called on the return of the call
    }else{
    	setSampleTests();
    }
}

function setSampleTests(){
	deselectAllTests();

	var id = selectedRowId;

	var allTests = $("testIds_" + id ).value;
    var allPanels = $("panelIds_" + id ).value;

    if(allPanels.length > 0) {
        var panels = allPanels.split(",");
        var inputs = $("addPanelTable").getElementsByTagName("input");
        for( var i = 0; i < panels.length; i++ ){
            for( var j = 0; j < inputs.length; j++ ){
                if( inputs[j].value == panels[i] ){
                    inputs[j].click();
                    break;
                }
            }
        }
    }

	if( allTests.length > 0 ){
		var tests = allTests.split(",");

		var inputs = $("addTestTable").getElementsByTagName("input");

		for( var i = 0; i < tests.length; i++ ){
			for( var j = 1; j < inputs.length; j = j + 2 ){
				if( inputs[j].value == tests[i] ){
                    var input = inputs[ j - 1];
                    if(!input.checked)
                        input.click();
					break;
				}
			}
		}
	}

	$("testSelections").show();
}

function panelSelected(checkBox, tests, panelId ){
	for( var i = 0; i < tests.length; i++ ){
        var test = $jq("#test_" + tests[i]);

        var selectedPanels = test.data('panels') || [];
        if(checkBox.checked){
            selectedPanels.push(panelId);
        } else {
            var indexOfPanel = selectedPanels.indexOf(panelId);
            selectedPanels.splice(indexOfPanel, 1);
        }
        test.data('panels', selectedPanels);
        test.prop('checked', selectedPanels.length > 0);
        test.prop('disabled', selectedPanels.length > 0);
    }
}

function /*boolean*/ allSamplesHaveTests(){
	var testBoxes = document.getElementsByName("tests");

	for(var i = 0; i < testBoxes.length; i++ ){
		if( testBoxes[i].value.blank() ){
			return false;
		}
	}

	//ensure that at least 1 sample exists
	var table = $("samplesAddedTable");
	var rows = table.rows;
	//if length is 1, then no sample exists
	if( rows.length == 1 ){
		return false;
	}

	return true;
}

function samplesHaveBeenAdded(){
	return $("samplesAddedTable").rows.length > 1;
}
</script>
<% if(useInitialSampleCondition){ %>
<div id="sampleConditionPrototype" style="display: none" >
<html:select name='<%=formName%>'
			 property="initialSampleConditionList"
			 multiple="true"
			 title='<%= StringUtil.getMessageForKey("result.multiple_select")%>'
			 styleId = 'prototypeID'>
			<logic:iterate id="optionValue" name='<%=formName%>' property="initialSampleConditionList" type="IdValuePair" >
						<option value='<%=optionValue.getId()%>' >
							<bean:write name="optionValue" property="value"/>
						</option>
					</logic:iterate>
				</html:select>
</div>
<% } %>
<div id="sectionPrototype" style="display:none;" >
	<span class="requiredlabel" style="visibility:hidden;">*</span>
	<select id="testSectionPrototypeID" disabled>
				<option value='0'></option>
			<logic:iterate id="optionValue" name='<%=formName%>' property="testSectionList" type="IdValuePair" >
				<option value='<%=optionValue.getId()%>' >
					<bean:write name="optionValue" property="value"/>
				</option>
			</logic:iterate>
	</select>
</div>

<html:hidden name="<%=formName%>" property="sampleXML" styleId="sampleXML"/>
	<Table width="100%">
		<tr>
			<td>
				<bean:message  key="sample.entry.sample.type"/>
				<html:select name="<%=formName%>" property="sampleTypeSelect" onchange="sampleTypeSelected(this);" styleId="sampleTypeSelect"
					value="0">
					<app:optionsCollection name="<%=formName%>" property="sampleTypes" label="value" value="id" />
				</html:select>
				<html:button property="addSamples" styleId="addSampleButton" onclick="addNewSamples()" disabled="true">
					<bean:message key="sample.entry.addSample" />
				</html:button>
			</td>
		</tr>
	</Table>

	<div id="samplesAdded" class="colorFill" style="display: none; ">

		<table id="samplesAddedTable" width=<%=useCollectionDate ? "100%" : "80%" %>>
			<tr>
				<th width="5%"></th>
				<th width="10%">
					<bean:message key="sample.entry.id"/>
				</th>
				<th width="10%">
					<bean:message key="sample.entry.sample.type"/>
				</th>
				<% if(useInitialSampleCondition){ %>
				<th width="15%">
					<bean:message key="sample.entry.sample.condition"/>
				</th>
				<% } %>
				<% if( useCollectionDate ){ %>
				<th >
					<bean:message key="sample.collectionDate"/>
				</th>
				<th >
					<bean:message key="sample.collectionTime"/>
				</th>
				<% } %>
				<% if( useCollector ){ %>
				<th>
					<bean:message key="sample.entry.collector" />
				</th>	
				<% } %>
				<th width="35%">
					<span class='requiredlabel'>*</span>&nbsp;<bean:message key="sample.entry.sample.tests"/>
				</th>
				<th width="10%"></th>
			</tr>
		</table >
		<table width=<%=useCollectionDate ? "100%" : "80%" %>>
			<tr>
				<td width=<%=useCollectionDate ? "90%" : "90%" %>>&nbsp;</td>
				<td width="10%">
					<html:button property="removeAll" styleClass="textButton"  onclick="removeAllRows();">
						<bean:message key="sample.entry.removeAllSamples"/>
					</html:button>
				</td>
			</tr>
		</table>
		<div id="testSelections" class="colorFill" style="display:none;" >
		<table width="99%" style="margin-left: 1%" id="addTables">
		<tr>
			<td valign="top" width="30%">
				<table width="97%" id="addPanelTable" >
					<caption>
						<bean:message key="sample.entry.panels"/>
					</caption>
					<tr>
						<th width="20%">&nbsp;
							
						</th>
						<th width="80%">
							<bean:message key="sample.entry.panel.name"/>
						</th>
					</tr>

				</table>
			</td>
			<td valign="top" width="70%">
				<table width="97%" style="margin-left: 3%" id="addTestTable">
					<caption>
						<bean:message key="sample.entry.available.tests"/>
					</caption>
					<tr>
						<th width="10%">&nbsp;
							
						</th>
						<th width="90%" class="last-child">
							<bean:message key="sample.entry.available.test.names"/>
						</th>
						<th width="0%" style="display:none;" id="sectionHead">
							Section
						</th>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</div>
	</div>

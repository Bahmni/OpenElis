<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date, java.util.List,
	org.apache.struts.Globals,
	us.mn.state.health.lims.common.util.SystemConfiguration,
	us.mn.state.health.lims.common.action.IActionConstants,
	java.util.Collection,
	java.util.ArrayList,
	us.mn.state.health.lims.common.provider.validation.AccessionNumberValidatorFactory,
	us.mn.state.health.lims.common.provider.validation.IAccessionNumberValidator,
	us.mn.state.health.lims.resultvalidation.bean.AnalysisItem,
	us.mn.state.health.lims.note.valueholder.Note,
	us.mn.state.health.lims.common.util.IdValuePair,
	us.mn.state.health.lims.common.util.StringUtil,
    us.mn.state.health.lims.common.util.Versioning,
    us.mn.state.health.lims.common.util.validator.GenericValidator,
	java.text.DecimalFormat" %>

<%@ taglib uri="/tags/struts-bean"		prefix="bean" %>
<%@ taglib uri="/tags/struts-html"		prefix="html" %>
<%@ taglib uri="/tags/struts-logic"		prefix="logic" %>
<%@ taglib uri="/tags/labdev-view"		prefix="app" %>
<%@ taglib uri="/tags/sourceforge-ajax" prefix="ajax" %>

<bean:define id="formName"	value='<%=(String) request.getAttribute(IActionConstants.FORM_NAME)%>' />

<bean:define id="testSection"	value='<%=request.getParameter("type") == null || request.getParameter("type").isEmpty() ? "" : request.getParameter("type")%>' />
<bean:define id="testName"	value='<%=request.getParameter("test") == null || request.getParameter("test").isEmpty() ? "" : request.getParameter("test")%>' />
<bean:define id="patientId"	value='<%=request.getParameter("patientId") == null || request.getParameter("patientId").isEmpty() ? "" : request.getParameter("patientId")%>' />
<bean:define id="referer"	value='<%=request.getParameter("referer") == null || request.getParameter("referer").isEmpty() ? "" : request.getParameter("referer")%>' />

<bean:define id="results" name="<%=formName%>" property="resultList" />
<bean:define id="pagingSearch" name='<%=formName%>' property="paging.searchTermToPage" type="List<IdValuePair>" />

<bean:size id="resultCount" name="results" />
<%!
	boolean showAccessionNumber = false;
	String currentAccessionNumber = "";
	int rowColorIndex = 2;
	IAccessionNumberValidator accessionNumberValidator;
	String searchTerm = null;
%>
<%

	String basePath = "";
	String path = request.getContextPath();
	basePath = path + "/";
	currentAccessionNumber="";
	accessionNumberValidator = new AccessionNumberValidatorFactory().getValidator();
	searchTerm = request.getParameter("searchTerm");

%>
<script type="text/javascript" src="<%=basePath%>scripts/math-extend.js?ver=<%= Versioning.getBuildNumber() %>" ></script>
<script type="text/javascript" src="<%=basePath%>scripts/utilities.js?ver=<%= Versioning.getBuildNumber() %>" ></script>
<script type="text/javascript" src="scripts/OEPaging.js?ver=<%= Versioning.getBuildNumber() %>"></script>

<script type="text/javascript" language="JavaScript1.2">
var dirty = false;
var pager = new OEPager('<%=formName%>', '<%= testSection == "" ? "" : "&type=" + testSection  %>' + '<%= "&test=" + testName  %>');
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
			pageSearch = new OEPageSearch( $("searchNotFound"), "td", pager );

			if( searchTerm != "null" ){
				 pageSearch.highlightSearch( searchTerm, false );
			}
			});

function  /*void*/ setMyCancelAction(form, action, validate, parameters)
{
	//first turn off any further validation
	setAction(window.document.forms[0], 'Cancel', 'no', '');
}

function /*void*/ enableDisableCheckboxes( matchedElement, groupingNumber ){
	$(matchedElement).checked = false;

	$("sampleRejected_" + groupingNumber).checked = false;
	$("sampleAccepted_" + groupingNumber).checked = false;
	$("selectAllReject").checked = false;
	$("selectAllAccept").checked = false;
}

function updateAbnormalCheck(isNormal, index) {
    if(isNormal == true){
        $("abnormalId_" + index).checked = false;
    } else {
        $("abnormalId_" + index).checked = true;
    }
}
function isNormalForNumeric(value, lowerBound, upperBound) {
    return value <= upperBound && value >= lowerBound;
}

function isNormalForDropDown(id, idValuePair) {
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

function /*void*/ acceptSample(element, groupingNumber ){
	$$(".accepted_" + groupingNumber).each( function(item){
		item.checked = element.checked;
		}
	);

	$$(".rejected_" + groupingNumber).each( function(item){
		item.checked = false;
		}
	);

	$("sampleRejected_" + groupingNumber).checked = false;
	$("selectAllReject").checked = false;
	$("selectAllAccept").checked = false;
}

function /*void*/ rejectSample(element, groupingNumber ){
	$$(".accepted_" + groupingNumber).each( function(item){
		item.checked = false;
		}
	);

	$$(".rejected_" + groupingNumber).each( function(item){
		item.checked = element.checked;
		}
	);

	$("sampleAccepted_" + groupingNumber).checked = false;
	$("selectAllAccept").checked = false;
	$("selectAllReject").checked = false;
}

function /*void*/ markUpdated(){
	$("saveButtonId").disabled = false;
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

function savePage() {
    for(i=0; i< <%= resultCount %>; i++) {
        $("abnormalId_" + i).disabled = false;
    }

    jQuery("#saveButtonId").attr("disabled", "disabled");
  window.onbeforeunload = null; // Added to flag that formWarning alert isn't needed.
	var form = window.document.forms[0];
	form.action = "ResultValidationSave.do?referer=<%=referer%>";
	form.submit();
}

function toggleSelectAll( element ) {

	if (element.id == "selectAllAccept" ) {
		var checkboxes = $$(".accepted");
		var matchedCheckboxes = $$(".rejected");
	} else if (element.id == "selectAllReject" ) {
		var checkboxes = $$(".rejected");
		var matchedCheckboxes = $$(".accepted");
	}

	if (element.checked == true ) {
		for (var index = 0; index < checkboxes.length; ++index) {
			  var item = checkboxes[index];
			  item.checked = true;
		}
		for (var index = 0; index < matchedCheckboxes.length; ++index) {
			  var item = matchedCheckboxes[index];
			  item.checked = false;
		}
	} else if (element.checked == false ) {
		for (var index = 0; index < checkboxes.length; ++index) {
			  var item = checkboxes[index];
			  item.checked = false;
		}
	}

}

function updateLogValue(element, index ){
	var logField = $("log_" + index );

	if( logField ){
		var logValue = Math.baseLog(element.value).toFixed(2);

		if( isNaN(logValue) ){
			jQuery(logField).html("--");
		}else{
			jQuery(logField).html(logValue);
		}
	}
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
				resultIds += "," + $("resultIdValue_" + rowId).value;
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

/*It is not clear why this is needed but what it prevents is the browser trying to do a submit on 'enter'  Other pages handle this
differently.  Overrides formTemplate.jsp handleEnterEvent
*/
function /*boolean*/ handleEnterEvent(){
	return true;
}

function enableOnlyForRemark(index, resultType, isReferredOut) {
    if(resultType !== 'R' && !isReferredOut){
        $("abnormalId_" + index).disabled = true;
    }
    else{
        $("abnormalId_" + index).disabled = false;
    }
}

</script>
<logic:notEmpty name="patientId">
    <h3><span>PatientID: </span><span><%=patientId%></span></h3>
</logic:notEmpty>

<div class="btn-block">

<logic:notEqual name="resultCount" value="0">
<div  style="width:100%" >
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
	<span style="float : right" >
	<span style="visibility: hidden" id="searchNotFound"><em><%= StringUtil.getMessageForKey("search.term.notFound") %></em></span>
	<%=StringUtil.getContextualMessageForKey("result.sample.id")%> : &nbsp;
	<input type="text"
	       id="labnoSearch"
	       maxlength='<%= Integer.toString(accessionNumberValidator.getMaxAccessionLength())%>' />
	<input type="button" onclick="pageSearch.doLabNoSearch($(labnoSearch))" value='<%= StringUtil.getMessageForKey("label.button.search") %>'>
	</span>
</div>
</logic:notEqual>
</div>
<html:hidden name="<%=formName%>"  property="testSection" value="<%=testSection%>" />
<html:hidden name="<%=formName%>"  property="testName" value="<%=testName%>" />
<%
    if (request.getParameter("accessionNumber") != null) {
%>
        <html:hidden name="<%=formName%>"  property="accessionNumber" value='<%=(String) request.getParameter("accessionNumber") %>' />
<%
    }
%>


<logic:notEqual name="resultCount" value="0">
<Table width="100%" >
    <tr>
		<th width="77%" colspan="3" style="background-color: white">
			<img src="./images/nonconforming.gif" /> = <bean:message key="result.nonconforming.item"/>
		</th>
		<th width="10%" align="center" style="background-color: white">&nbsp;
				<bean:message key="validation.accept.all" />
			<input type="checkbox"
				name="selectAllAccept"
				value="on"
				onclick="toggleSelectAll(this);"
				onchange="markUpdated(); makeDirty();"
				id="selectAllAccept"
				class="accepted acceptAll">
		</th>
		<th width="12%" align="center" style="background-color: white">&nbsp;
		<bean:message key="validation.reject.all" />
			<input type="checkbox"
					name="selectAllReject"
					value="on"
					onclick="toggleSelectAll(this);"
					onchange="markUpdated(); makeDirty();"
					id="selectAllReject"
					class="rejected rejectAll">
		</th>
		<th width="3%" style="background-color: white">&nbsp;</th>
  	</tr>
</Table>
</logic:notEqual>
<Table width="100%" >
	<logic:notEqual name="resultCount" value="0">
	<tr>
    	<th>
	  		<bean:message key="quick.entry.accession.number.CI"/>
		</th>
		<th>
	  		<bean:message key="sample.entry.project.testName"/>
		</th>
		<th>
			<bean:message key="analyzer.results.result"/>
		</th>
        <th align="center">
            <bean:message key="result.abnormal" />
        </th>
        <th align="center">
			<bean:message key="validation.accept" />
		</th>
		<th align="center">
			<bean:message key="validation.reject" />
		</th>
		<th>
            <bean:message key="result.uploadedFile"/>
		</th>
        <th>
            <bean:message key="result.notes"/>
        </th>
  	</tr>

	<logic:iterate id="resultList" name="<%=formName%>"  property="resultList" indexId="index" type="AnalysisItem">
			 <% showAccessionNumber = !resultList.getAccessionNumber().equals(currentAccessionNumber);
				   if( showAccessionNumber ){
					currentAccessionNumber = resultList.getAccessionNumber();
					rowColorIndex++;}  %>
			<html:hidden name="resultList" property="sampleGroupingNumber" indexed="true" />
			<html:hidden name="resultList" property="noteId" indexed="true" />
			<html:hidden name="resultList" property="resultId"  indexed="true" styleId='<%="resultIdValue_" + index%>'/>

			<%if( resultList.isMultipleResultForSample() && showAccessionNumber ){
			     showAccessionNumber = false; %>
			<tr  class='<%=(rowColorIndex % 2 == 0) ? "evenRow" : "oddRow" %>'  >
				<td colspan="3" class='<%= currentAccessionNumber %>'>
	      			<bean:write name="resultList" property="accessionNumber"/>
	    		</td>
	    		<td align="center">
					<html:checkbox styleId='<%="sampleAccepted_" + resultList.getSampleGroupingNumber() %>'
								   name="resultList"
								   property="isAccepted"
								   styleClass="accepted"
								   indexed="true"
								   onchange="markUpdated(); makeDirty();"
								   onclick='<%="acceptSample( this, \'" + resultList.getSampleGroupingNumber() + "\');" %>' />
				</td>
				<td align="center">
					<html:checkbox styleId='<%="sampleRejected_" + resultList.getSampleGroupingNumber() %>'
									   name="resultList"
									   property="isRejected"
									   styleClass="rejected"
									   indexed="true"
									   onchange="markUpdated(); makeDirty();"
									   onclick='<%="rejectSample( this, \'" + resultList.getSampleGroupingNumber() + "\');" %>' />
				</td>
				<td>&nbsp;</td>
			</tr>
			<% } %>
     		<tr id='<%="row_" + index %>' class='<%=(rowColorIndex % 2 == 0) ? "evenRow" : "oddRow" %>'  >
				<% if( showAccessionNumber ){%>
	    		<td class='<%= currentAccessionNumber %>' >
	      			<bean:write name="resultList" property="accessionNumber"/>
	    		</td>
	    		<% }else{ %>
	    			<td></td>
	    		<% } %>
				<td>
                    <bean:write name="resultList" property="testName"/>
                    <logic:equal name="resultList" property="referredOut" value="true">
                        <span class="referredout-highlight">R</span>
                    </logic:equal>
					<% if( resultList.isNonconforming()){ %>
						<img src="./images/nonconforming.gif" />
					<% } %>
				</td>
				<td>
					<logic:equal name="resultList" property="resultType" value="N">
						<% if( resultList.isReadOnly() ){%>
							<div
								class='results-readonly <%= (resultList.getIsHighlighted() ? "invalidHighlight " : " ") + (resultList.isReflexGroup() ? "reflexGroup_" + resultList.getSampleGroupingNumber()  : "")  +
							              (resultList.isChildReflex() ? " childReflex_" + resultList.getSampleGroupingNumber(): "") %> '
							    id='<%= "results_" + index %>'
								name='<%="resultList[" + index + "].result" %>' >
							<%= resultList.getResult() %>
							</div>
						<% }else{ %>
	    					<input type="text"
					           name='<%="resultList[" + index + "].result" %>'
					           size="6"
					           value='<%= resultList.getResult() %>'
					           id='<%= "results_" + index %>'
							   class='<%= (resultList.getIsHighlighted() ? "invalidHighlight " : " ") + (resultList.isReflexGroup() ? "reflexGroup_" + resultList.getSampleGroupingNumber()  : "")  +
							              (resultList.isChildReflex() ? " childReflex_" + resultList.getSampleGroupingNumber(): "") %> '
							   onchange='<%=  "markUpdated(); makeDirty(); updateLogValue(this, " + index + "); " +
								                (resultList.isReflexGroup() && !resultList.isChildReflex() ? "updateReflexChild(" + resultList.getSampleGroupingNumber()  +  " ); " : "") +
								                 "; updateAbnormalCheck( isNormalForNumeric(this.value,"+resultList.getMinNormal()+","+resultList.getMaxNormal()+")," + index + ")"%>'/>
	    				<% } %>
						<bean:write name="resultList" property="units"/>
					</logic:equal>
					<logic:equal name="resultList" property="resultType" value="D">
						<select name="<%="resultList[" + index + "].result" %>"
						        id='<%="resultId_" + index%>'
						        onchange="markUpdated(); makeDirty(); updateAbnormalCheck(isNormalForDropDown(this.value, [<%= resultList.getAbnormalTestResultMap() %>]), <%= index %>);" >
								<logic:iterate id="optionValue" name="resultList" property="dictionaryResults" type="IdValuePair" >
									<option value='<%=optionValue.getId()%>'  <%if(optionValue.getId().equals(resultList.getResult())) out.print("selected"); %>  >
										<bean:write name="optionValue" property="value"/>
									</option>
								</logic:iterate>
						</select>
					</logic:equal>
					<logic:equal name="resultList" property="resultType" value="A">
						<app:text name="resultList"
								  indexed="true"
								  property="result"
								  size="6"
								  allowEdits='<%= !resultList.isReadOnly() %>'
								  styleId='<%="results_" + index %>'
								  onchange='<%="markUpdated(); makeDirty(); updateLogValue(this, " + index + ");" %>'/>
						<bean:write name="resultList" property="units"/>

					</logic:equal>
					<logic:equal name="resultList" property="resultType" value="R">
						<app:textarea name="resultList"
								  indexed="true"
								  property="result"
								  rows="2"
								  allowEdits='<%= !resultList.isReadOnly() %>'
								  styleId='<%="results_" + index %>'
								  onkeyup='markUpdated();'
								  />
					</logic:equal>
					<% if(resultList.isDisplayResultAsLog()){ %>
						<br/>
						<div id='<%= "log_" + index %>'
								class='results-readonly'>
								<% try{
												Double value = Math.log10(Double.parseDouble(resultList.getResult()));
												DecimalFormat twoDForm = new DecimalFormat("##.##");
												out.print(Double.valueOf(twoDForm.format(value)));
												}catch(Exception e){
													out.print("--");} %>
						</div> log
					<% } %>
				</td>
                <td>
                    <html:checkbox name='resultList'
                                   property="abnormal"
                                   indexed="true"
                                   styleId='<%="abnormalId_" + index %>'
                                   onchange='<%="markUpdated();" %>' />

                    <html:hidden property='<%="resultList["+index+"].abnormal"%>' value="false"/> <!-- To submit checkbox value when unchecked -->
                    <script language="JavaScript">
                        enableOnlyForRemark(<%=index%>,'<%=resultList.getResultType()%>',<%= resultList.isReferredOut() %>);
                    </script>

                </td>

				<% if(resultList.isShowAcceptReject()){ %>
				<td>
					<html:checkbox styleId='<%="accepted_" + index %>'
								   name="resultList"
								   property="isAccepted"
								   styleClass='<%= "accepted accepted_" + resultList.getSampleGroupingNumber() %>'
								   indexed="true"
								   onchange="markUpdated(); makeDirty();"
								   onclick='<%="enableDisableCheckboxes(\'rejected_" + index + "\', \'" + resultList.getSampleGroupingNumber() + "\');" %>' />
				</td>
				<td>
					<html:checkbox styleId='<%="rejected_" + index %>'
									   name="resultList"
									   property="isRejected"
									   styleClass='<%= "rejected rejected_" + resultList.getSampleGroupingNumber() %>'
									   indexed="true"
									   onchange="markUpdated(); makeDirty();"
									   onclick='<%="enableDisableCheckboxes(\'accepted_" + index + "\', \'" + resultList.getSampleGroupingNumber() + "\');" %>' />
				</td>
				<% }else{ %>
				<td><bean:message key="label.computed"/></td><td><bean:message key="label.computed"/></td>
				<% } %>
                <td>
                    <% if (resultList.getUploadedFilePath() != null) {%>
                        <a href="<bean:write name="resultList" property="uploadedFilePath"/>" target="_blank">Download file</a>
                    <% }%>
                </td>
				<td>
					<% if( !resultList.isReadOnly()){ %>
				    	<logic:empty name="resultList" property="note">
						 	<img src="./images/note-add.gif"
						 	     onclick='<%= "showHideNotes(this, " + index + ");" %>'
						 	     id='<%="showHideButton_" + index %>'
						    />
						 </logic:empty>
						 <logic:notEmpty name="resultList" property="note">
						 	<img src="./images/note-edit.gif"
						 	     onclick='<%= "showHideNotes(this, " + index + ");" %>'
						 	     id='<%="showHideButton_" + index %>'
						    />
						 </logic:notEmpty>
					<html:hidden property="hideShowFlag"  styleId='<%="hideShow_" + index %>' value="hidden" />
					<% } %>
				</td>
      		</tr>
      		<tr id='<%="noteRow_" + index %>' style="display: none;">
				<td colspan="2" valign="top" align="right"><bean:message key="note.note"/>:</td>
				<td colspan="4" align="left" >
					<html:textarea styleId='<%="note_" + index %>'
								   onchange='<%="markUpdated(" + index + ");"%>'
							   	   name="resultList"
					           	   property="note"
					           	   indexed="true"
					           	   cols=""
					           	   rows="3" />
				</td>
			</tr>
  	</logic:iterate>



  	</logic:notEqual>
  	<logic:equal name="resultCount"  value="0">
		<h2><bean:message key="result.noTestsFound"/></h2>
	</logic:equal>
</Table>
<div class="btn-block">
    <logic:notEqual name="resultCount" value="0">
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
<div>
    <bean:define id="capture" name="<%=formName%>" property="canCaptureAccessionNotes"/>
    <logic:notEqual name="resultCount" value="0">
        <%if (Boolean.TRUE.equals(capture)) {%>
            <div class="lab-results-notes">
                <bean:message key="note.accession"/>:
                <html:textarea name="<%=formName%>" property="accessionNotes" onchange="markUpdated(); makeDirty();"/>
            </div>

            <logic:iterate id="savedAccessionNotes" name="<%=formName%>"  property="savedAccessionNotes" type="Note" indexId="foo">
                <div class="notes-block">
                    <pre><bean:write name="savedAccessionNotes" property="text"/></pre>
                </div>
            </logic:iterate>
        <% } %>
    </logic:notEqual>
</div>

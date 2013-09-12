<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="us.mn.state.health.lims.common.action.IActionConstants,
			us.mn.state.health.lims.common.util.resources.ResourceLocator,
			java.util.Locale"
%>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />


<%!
  String paginationMessage = "";
  String totalCount = "0";
  String fromCount = "0";
  String toCount = "0";
%>

<%
	   String deactivateDisabled = "true";
       if (request.getAttribute(IActionConstants.DEACTIVATE_DISABLED) != null) {
            deactivateDisabled = (String)request.getAttribute(IActionConstants.DEACTIVATE_DISABLED);
       }

       String addDisabled = "true";
       if (request.getAttribute(IActionConstants.ADD_DISABLED) != null) {
            addDisabled = (String)request.getAttribute(IActionConstants.ADD_DISABLED);
       }

       //This is added for testAnalyteTestResult (we need to disable ADD until test is selected
       String allowEdits = "true";
       if (request.getAttribute(IActionConstants.ALLOW_EDITS_KEY) != null) {
            allowEdits = (String)request.getAttribute(IActionConstants.ALLOW_EDITS_KEY);
       }

       String editDisabled = "false"; 
       if (request.getAttribute(IActionConstants.EDIT_DISABLED) != null) {
            editDisabled = (String)request.getAttribute(IActionConstants.EDIT_DISABLED);
       }

       boolean disableEdit = !Boolean.valueOf(allowEdits).booleanValue() && Boolean.valueOf(editDisabled).booleanValue();

      String notAllowSearching="true";
      if (request.getAttribute(IActionConstants.MENU_SEARCH_BY_TABLE_COLUMN) != null) {
          notAllowSearching = "false";

       }

       String searchStr="";
       if (request.getAttribute(IActionConstants.MENU_SELECT_LIST_HEADER_SEARCH_STRING) != null ) {
          {
             searchStr = (String) request.getAttribute(IActionConstants.MENU_SELECT_LIST_HEADER_SEARCH_STRING);
           }
       }

       String searchColumn="";
       if (request.getAttribute(IActionConstants.MENU_SEARCH_BY_TABLE_COLUMN) != null )  {
          {
             searchColumn = (String) request.getAttribute(IActionConstants.MENU_SEARCH_BY_TABLE_COLUMN);
          }
       }
%>
<%
	if(null != request.getAttribute(IActionConstants.FORM_NAME))
	{
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	<tr>
		<td class="pageTitle" align="center">
				<logic:notEmpty
					name="<%=IActionConstants.PAGE_SUBTITLE_KEY%>">
					<bean:write name="<%=IActionConstants.PAGE_SUBTITLE_KEY%>" />
				</logic:notEmpty>
		</td>
	</tr>
</table>
<%
	}
%>


<script language="JavaScript1.2">


function submitSearchForEnter(e){
    if (enterKeyPressed(e)) {
       var button = document.getElementById("searchButton");
       e.returnValue=false;
       e.cancel = true;
       button.click();
    }
}

function submitSearchForClick(button){
     setMenuAction( button, window.document.forms[0], 'Search', 'yes', '?search=Y');
}
</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%" class="menu-select-header-btns">
  <tbody >
    <tr>
     <td>
        <ul>
          <li class="menu-select-header-seperator">
            <logic:notEmpty name="<%=IActionConstants.MENU_SEARCH_BY_TABLE_COLUMN%>">
                  <ul>
                    <li>
                      <span class="menu-select-header-btns-text">
                       <bean:message key="label.form.searchby"/></span>
                       <span class="menu-select-header-btns-text">
                       <bean:message key="<%=searchColumn%>"/>
                     </span>
                   </li>
                    <li>   
                        <html:text name="<%=formName%>" property = "searchString" onkeypress="submitSearchForEnter(event);" size = "20" maxlength= "20" value="<%=searchStr%>" disabled="<%=Boolean.valueOf(notAllowSearching).booleanValue()%>" />
                    </li>
                    <li>
                        <html:button property="search" styleId="searchButton" onclick="submitSearchForClick(this);return false;" disabled="<%=Boolean.valueOf(notAllowSearching).booleanValue()%>">
                         <bean:message key="label.button.search"/>
                        </html:button>
                    </li>
                    
                  </ul>
             </logic:notEmpty>
          </li>
        <li>
             <html:button onclick="setMenuAction(this, window.document.forms[0], '', 'yes', '?ID=0');return false;" property="add" disabled="<%=Boolean.valueOf(addDisabled).booleanValue()%>" >
               <bean:message key="label.button.add"/>
             </html:button>
        </li>
        <!-- <li>
            <span class="menu-select-header-btns-text">
              <bean:message key="label.form.selectand"/>&nbsp;
            </span>
       </li> -->
        <li>
             <html:button onclick="setMenuAction(this, window.document.forms[0], '', 'yes', '?ID=');return false;" property="edit" disabled="<%= disableEdit %>">
                  <bean:message key="label.button.edit"/>
             </html:button>
        </li>
        <li class="menu-select-header-seperator">
             <html:button onclick="setMenuAction(this, window.document.forms[0], 'Delete', 'yes', '?ID=');return false;" property="deactivate" disabled="<%=Boolean.valueOf(deactivateDisabled).booleanValue()%>">
               <bean:message key="label.button.deactivate"/>
             </html:button>
       </li>
     </ul>
     <ul class="paginator">

        <%
          String previousDisabled = "false";
              String nextDisabled = "false";
              if (request.getAttribute(IActionConstants.PREVIOUS_DISABLED) != null) {
                 previousDisabled = (String)request.getAttribute(IActionConstants.PREVIOUS_DISABLED);
              }
              if (request.getAttribute(IActionConstants.NEXT_DISABLED) != null) {
                 nextDisabled = (String)request.getAttribute(IActionConstants.NEXT_DISABLED);
              }
        %>
          <li class="paginator-previous">
            <html:button onclick="setMenuAction(this, window.document.forms[0], '', 'yes', '?paging=1');return false;" property="previous" disabled="<%=Boolean.valueOf(previousDisabled).booleanValue()%>">
               <bean:message key="label.button.previous"/>
            </html:button>
          </li>
           <li>
            <span class="menu-select-header-btns-text">
             <%
                 if (request.getAttribute(IActionConstants.MENU_TOTAL_RECORDS) != null) {
                     totalCount = (String)request.getAttribute(IActionConstants.MENU_TOTAL_RECORDS);
                 }
                 if (request.getAttribute(IActionConstants.MENU_FROM_RECORD) != null) {
                     fromCount = (String)request.getAttribute(IActionConstants.MENU_FROM_RECORD);
                 }
                 if (request.getAttribute(IActionConstants.MENU_TO_RECORD) != null) {
                    toCount = (String)request.getAttribute(IActionConstants.MENU_TO_RECORD);
                 }

                  java.util.Locale locale = (Locale)request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY);
                  String msgResults = ResourceLocator.getInstance().getMessageResources().getMessage(locale,"list.showing");
                  String msgOf = ResourceLocator.getInstance().getMessageResources().getMessage(locale,"list.of");

                  paginationMessage = msgResults + " " + fromCount + " - " + toCount + " " + msgOf + " " + totalCount;
             %>
      
              <%=paginationMessage%>
            </span>
          </li>
          <li>
            <html:button onclick="setMenuAction(this, window.document.forms[0], '', 'yes', '?paging=2');return false;" property="next" disabled="<%=Boolean.valueOf(nextDisabled).booleanValue()%>">
               <bean:message key="label.button.next"/>
            </html:button>
          </li>
         
          
        </ul>
       </td>
    </tr>
   </tbody>
</table>

<script language="JavaScript1.2">
   var textName = document.getElementById ("searchString");
 
   if (textName != null && textName.value != null)
   {  textName.focus();
      textName.value+='';
   }
</script>

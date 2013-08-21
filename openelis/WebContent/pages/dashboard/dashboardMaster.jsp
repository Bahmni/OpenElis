<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date,
	us.mn.state.health.lims.common.util.SystemConfiguration,
	us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>
<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />
<bean:define id="inProgressOrderListJson" name="<%=formName%>" property="inProgressOrderList" />
<bean:define id="completedOrderListJson" name="<%=formName%>" property="completedOrderList" />

<%!
String path = "";
String basePath = "";
%>

<%
path = request.getContextPath();
basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<link rel="stylesheet" type="text/css" href="<%=basePath%>css/slickgrid/slick.grid.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/slickgrid/examples.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/jquery_ui/jquery-ui-1.8.16.custom.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/jquery_ui/jquery.ui.tabs.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/slickgrid/dashboard.css" />

<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.event.drag-2.2.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.event.drop-2.2.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.core.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.formatters.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.cellrangedecorator.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.cellrangeselector.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.cellexternalcopymanager.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.cellselectionmodel.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.rowselectionmodel.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.dataview.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.grid.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.editors.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/merge-sort.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/dashBoard/orders.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/dashBoard/createGrid.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/utils.js"></script>

<input id="refreshButton" type="button" value="Refresh">


<div>
    <div id="tabs">
         <ul>
            <li><a href="#inProgressListContainer">In Progress</a></li>
            <li><a href="#completedListContainer">Completed</a></li>
         </ul>
        <div id="inProgressListContainer" style="width:750px"></div>
        <div id="completedListContainer" style="width:520px"></div>
    </div>

    <div id="patientDetails">
        <div><span>Patient ID: </span><span id="patientId"></span></div>
        <div><span>Name : </span><span id="name"></span></div>
        <div><span>Father/Husband's Name : </span><span id="primaryRelative"></span></div>
        <div><span>Village : </span><span id="village"></span></div>
        <div><span>Gender : </span><span id="gender"></span></div>
        <div><span>Age : </span><span id="age"></span></div>
    </div>
</div>

<script type="text/javascript">

    var gridForInProgressOrder;
    var gridForCompletedOrder;
    var dataViewForInProgressTab;
    var dataViewForCompletedTab;
    var columnFilters = {};
    var inProgressObject;
    var completedOrderObject;

    var options = {
        enableColumnReorder: false,
        autoHeight:true,
        enableCellNavigation: true,
        showHeaderRow: true,
        headerRowHeight: 30,
        explicitInitialization: true
    };

    jQuery(document).ready(function() {

        inProgressObject = new order("#inProgressListContainer", "<%= inProgressOrderListJson %>", generateLinkForInProgressOrder, getColumnsForInProgressOrder);
        dataViewForInProgressTab = new Slick.Data.DataView({ inlineFilters: true });
        gridForInProgressOrder = new Slick.Grid(inProgressObject.div, dataViewForInProgressTab, inProgressObject.columns,options);
        createGrid(gridForInProgressOrder, dataViewForInProgressTab, inProgressObject, onRowSelection);

        completedOrderObject = new order("#completedListContainer", "<%= completedOrderListJson %>", generateLinkForCompletedOrder, getColumnsForCompletedOrder);
        dataViewForCompletedTab = new Slick.Data.DataView();
        gridForCompletedOrder = new Slick.Grid(completedOrderObject.div, dataViewForCompletedTab, completedOrderObject.columns, options);
        createGrid(gridForCompletedOrder, dataViewForCompletedTab, completedOrderObject, onRowSelection);

        var activeTab = <%= request.getParameter("activeTab") %>;

        var tabOptions = {
            collapsible: true
        };

        if (activeTab){
            tabOptions.selected = activeTab;
        }

        jQuery("#tabs").tabs(tabOptions);
        jQuery("#patientDetails").hide();

        jQuery("#refreshButton").on("click",function(){
            var index = jQuery( "#tabs" ).tabs( "option", "selected" );
            //making sure to delete all the parameters on location href
            var link =location.href.split("?")[0];
            location.href = link + "?activeTab=" + index;
            return false;
        });

    });

    var showPatientDetails = function(stNumber, firstName, lastName, primaryRelative, village, gender, age) {
        jQuery("#patientDetails").show();
        jQuery("#patientId").text(stNumber);
        jQuery("#name").text(firstName + " " + lastName);
        jQuery("#primaryRelative").text(primaryRelative);
        jQuery("#village").text(village);
        jQuery("#gender").text(gender);
        jQuery("#age").text(age);
    }

    function onRowSelection(row) {
        new Ajax.Request ('ajaxQueryXML', {
            method: 'get',
            parameters: "provider=PatientSearchPopulateProvider&stNumber=" + row.stNumber,
            onSuccess:  onRowSelectionSuccess
        });
    }

    function onRowSelectionSuccess(xhr) {
        var villageIndex = 1;
        var datePattern = '<%=SystemConfiguration.getInstance().getPatternForDateLocale() %>';
        showPatientDetails(
            OpenElis.Utils.getXMLValue(xhr.responseXML, 'ST_ID'),
            OpenElis.Utils.getXMLValue(xhr.responseXML, 'firstName'),
            OpenElis.Utils.getXMLValue(xhr.responseXML, 'lastName'),
            OpenElis.Utils.getXMLValue(xhr.responseXML, 'primaryRelative'),
            getAddressValue(xhr.responseXML, villageIndex),
            OpenElis.Utils.getXMLValue(xhr.responseXML, 'gender'),
            OpenElis.Utils.calculateAge(OpenElis.Utils.getXMLValue(xhr.responseXML, 'dob'), datePattern)
        );
    }

    function getAddressValue(response, index) {
        var field = response.getElementsByTagName('addressline').item(index);
        var value = field && field.getElementsByTagName('value').item(0);

        return value != null ? value.firstChild.nodeValue : "";
    }
</script>

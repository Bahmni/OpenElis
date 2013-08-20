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

    <div id="patientDetails"></div>
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
        createGrid(gridForInProgressOrder, dataViewForInProgressTab, inProgressObject);


        completedOrderObject = new order("#completedListContainer", "<%= completedOrderListJson %>", generateLinkForCompletedOrder, getColumnsForCompletedOrder);
        dataViewForCompletedTab = new Slick.Data.DataView();
        gridForCompletedOrder = new Slick.Grid(completedOrderObject.div, dataViewForCompletedTab, completedOrderObject.columns, options);
        createGrid(gridForCompletedOrder, dataViewForCompletedTab, completedOrderObject);

        var activeTab = <%= request.getParameter("activeTab") %>;

        var tabOptions = {
                        collapsible: true
                    };

        if (activeTab){
            tabOptions.selected = activeTab;
        }

         jQuery("#tabs").tabs(tabOptions);

         jQuery("#refreshButton").on("click",function(){
            var index = jQuery( "#tabs" ).tabs( "option", "selected" );
            //making sure to delete all the parameters on location href
            var link =location.href.split("?")[0];
            location.href = link + "?activeTab=" + index;
            return false;
         })

    });


    function  /*void*/ updatePatientAge( DOB )
    {
        var date = new String( DOB );

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

        return today.getFullYear() - yearDOB + adjustment;

    }

    var showPatientDetails = function(stNumber, firstName, lastName, primaryRelative, village, gender, age) {
        var patientDetails = jQuery("#patientDetails");
        patientDetails.empty();
        patientDetails.show()
        patientDetails.append("<div><span>PatientID : </span><span>" + stNumber + "</span></div>")
        patientDetails.append("<div><span>Name : </span><span>" + firstName + " " + lastName + "</span></div>")
        patientDetails.append("<div><span>Father/Husband's Name : </span><span>" + primaryRelative + "</span></div>")
        patientDetails.append("<div><span>Village : </span><span>" + village + "</span></div>")
        patientDetails.append("<div><span>Gender : </span><span>" + gender + "</span></div>")
        patientDetails.append("<div><span>Age : </span><span>" + age + "</span></div>")
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
        showPatientDetails(
                getXMLValue(xhr.responseXML, 'ST_ID'),
                getXMLValue(xhr.responseXML, 'firstName'),
                getXMLValue(xhr.responseXML, 'lastName'),
                getXMLValue(xhr.responseXML, 'primaryRelative'),
                getAddressValue(xhr.responseXML, villageIndex),
                getXMLValue(xhr.responseXML, 'gender'),
                updatePatientAge(getXMLValue(xhr.responseXML, 'dob'))
        );
    }

    function getXMLValue(response, key){
        var field = response.getElementsByTagName(key).item(0);
        return field != null ? field.firstChild.nodeValue : "";
    }

    function getAddressValue(response, index) {
        var field = response.getElementsByTagName('addressline').item(index);
        var value = field && field.getElementsByTagName('value').item(0);

        return value != null ? value.firstChild.nodeValue : "";
    }
</script>

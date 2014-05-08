<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date,
	java.text.SimpleDateFormat,
	us.mn.state.health.lims.common.util.SystemConfiguration,
	us.mn.state.health.lims.common.action.IActionConstants" %>
<%@ page import="us.mn.state.health.lims.common.util.ConfigurationProperties" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>
<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />
<bean:define id="todayOrderListJson" name="<%=formName%>" property="todayOrderList" />
<bean:define id="backlogOrderListJson" name="<%=formName%>" property="backlogOrderList" />

<%!
String path = "";
String basePath = "";
String serverNow = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
Boolean alwaysValidate = ConfigurationProperties.getInstance().isPropertyValueEqual(ConfigurationProperties.Property.ALWAYS_VALIDATE_RESULTS, "true");
%>

<%
path = request.getContextPath();
basePath = path + "/";
%>

<link rel="stylesheet" type="text/css" href="<%=basePath%>css/slickgrid/examples.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/jquery_ui/jquery-ui-1.8.16.custom.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/jquery_ui/jquery.ui.tabs.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/slickgrid/slick.grid.css" />
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

  

<div>
    <div id="todayStat">
        <table>
            <tr>
                <td>
                    <input id="refreshButton" type="button" value="Refresh">
                </td>
                <td>
                    <div class="arrow-text"> Today&#39;s Stats</div><div class="arrow-right"></div>
                </td>
                <td>
                    <span>Awaiting Testing : </span>
                    <span id="awaitingTestCount"></span>
                </td>
                <td>
                    <span>Awaiting Validation : </span>
                    <span id="awaitingValidationCount"></span>
                </td>
                <td>
                    <span>Completed : </span>
                    <span id="completedTestCount"></span>
                </td>
                <td>
                    <span>Total : </span>
                    <span id="totalSamplesCount"></span>
                </td>
                <td>
                    <span class="stats-header"> Total Patients Today</span><span> : </span>
                    <span id="totalCollectedTodayCount"></span>
                </td>
            </tr>
        </table>
    </div>

    <div id="tabs">
        <ul>
            <li><a href="#todayListContainer">Today</a></li>
            <li><a href="#backlogListContainer">Backlog</a></li>
         </ul>
        <div id="todayListContainer"><div id="todayListContainer-slick-grid"></div></div>
        <div id="backlogListContainer"><div id="backlogListContainer-slick-grid"></div></div>
    </div>

    <div id="patientDetails" class="hide details">
        <div class='details-more-info'><span class='details-key'>Patient ID : </span><span class='details-value' id="patientId"></span></div>
        <div class='details-more-info'><span class='details-key'>Name : </span><span class='details-value' id="name"></span></div>
        <div class='details-more-info'><span class='details-key'>Father/Husband's Name : </span><span class='details-value' id="primaryRelative"></span></div>
        <div class='details-more-info'><span class='details-key'>Village : </span><span class='details-value' id="village"></span></div>
        <div class='details-more-info'><span class='details-key'>Gender : </span><span  class='details-value' id="gender"></span></div>
        <div class='details-more-info'><span class='details-key'>Age : </span><span class='details-value' id="age"></span></div>
    </div>

</div>

<script type="text/javascript">

    var columnFilters = {};

    var options = {
        enableColumnReorder: false,
        autoHeight:true,
        enableCellNavigation: true,
        showHeaderRow: true,
        headerRowHeight: 35,
        explicitInitialization: true
    };

    var showStats = function(stats){
        jQuery("#todayStat").show();
        jQuery("#awaitingTestCount").text(stats.awaitingTestCount);
        jQuery("#awaitingValidationCount").text(stats.awaitingValidationCount);
        jQuery("#completedTestCount").text(stats.completedTestCount);
        jQuery("#totalSamplesCount").text(stats.totalSamplesCount);
        jQuery("#totalCollectedTodayCount").text(stats.totalCollectedTodayCount);
    }

    jQuery(document).ready(function() {
        var todayOrderList = JSON.parse('<%=todayOrderListJson%>');
        var backlogOrderList = JSON.parse('<%=backlogOrderListJson%>');

        var isToday = function(date) {
            // ISO date format
            var clientNow = new Date('<%= serverNow %>');
            return clientNow.getDate() === date.getDate() && clientNow.getMonth() == date.getMonth() && clientNow.getFullYear() === date.getFullYear();
        };

        var todayStats = {
            awaitingTestCount: todayOrderList.filter(function(order){ return order.pendingTestCount > 0 }).length,
            awaitingValidationCount: todayOrderList.filter(function(order){ return order.pendingTestCount == 0 && order.pendingValidationCount > 0 }).length,
            completedTestCount: todayOrderList.filter(function(order){ return order.isCompleted }).length,
            totalSamplesCount: todayOrderList.length,
            totalCollectedTodayCount: todayOrderList.filter(function(order){ return isToday(new Date(order.collectionDate)) }).length,
        }

        showStats(todayStats)

        var todayOrdersObject = new order("#todayListContainer-slick-grid", todayOrderList, generateAllLinksForOrder, getColumnsForTodayOrder, <%= alwaysValidate%>);
        var dataViewForTodayTab = new Slick.Data.DataView({ inlineFilters: true });
        var gridForTodayOrder = new Slick.Grid(todayOrdersObject.div, dataViewForTodayTab, todayOrdersObject.columns,options);
        createGrid(gridForTodayOrder, dataViewForTodayTab, todayOrdersObject, onRowSelection);

        var backlogOrdersObject = new order("#backlogListContainer-slick-grid", backlogOrderList, generateAllLinksForOrder, getColumnsForBacklogOrder, <%= alwaysValidate%>);
        var dataViewForBacklogTab = new Slick.Data.DataView({ inlineFilters: true });
        var gridForBacklogOrder = new Slick.Grid(backlogOrdersObject.div, dataViewForBacklogTab, backlogOrdersObject.columns,options);
        createGrid(gridForBacklogOrder, dataViewForBacklogTab, backlogOrdersObject, onRowSelection);

        var activeTab = <%= request.getParameter("activeTab") %>;

        var tabOptions = {
            select: function(event, ui) {
                jQuery("#patientDetails").hide();
            }
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

    var showPatientDetails = function(stNumber, firstName, middleName, lastName, primaryRelative, village, gender, age) {
        jQuery("#patientDetails").show();
        jQuery("#patientId").text(stNumber);
        jQuery("#name").text(firstName + " " + (middleName ? middleName + " " : "") + lastName);
        jQuery("#primaryRelative").text(primaryRelative?primaryRelative:"N/A");
        jQuery("#village").text(village?village:"N/A");
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
            OpenElis.Utils.getXMLValue(xhr.responseXML, 'middleName'),
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

        return value != null && value.firstChild != null ? value.firstChild.nodeValue : "";
    }
</script>

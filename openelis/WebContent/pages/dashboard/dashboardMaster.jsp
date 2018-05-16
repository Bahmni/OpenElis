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
<bean:define id="todaySampleNotCollectedListJson" name="<%=formName%>" property="todaySampleNotCollectedList" />
<bean:define id="backlogSampleNotCollectedListJson" name="<%=formName%>" property="backlogSampleNotCollectedList" />
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
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.autotooltips.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/utils.js"></script>

  

<div>
    <div id="todayStat">
        <table>
            <tr>
                <td>
                    <input id="refreshButton" type="button" value='<bean:message key="dashboard.msg.refresh"/>'>
                </td>
                <td>
                    <div class="arrow-text"><bean:message key="dashboard.todayStatus.todayStatus"/></div><div class="arrow-right"></div>
                </td>
                <td>
                    <span><bean:message key="dashboard.todayStatus.samplesToCollect"/></span>
                    <span id="samplesToCollect"></span>
                </td>
                <td>
                    <span><bean:message key="dashboard.todayStatus.samplesCollected"/></span>
                    <span id="samplesCollected"></span>
                </td>
                <td>
                    <span><bean:message key="dashboard.todayStatus.total"/></span>
                    <span id="totalSamplesCount"></span>
                </td>
                <td>
                    <span><bean:message key="dashboard.todayStatus.awaitingTesting"/></span>
                    <span id="awaitingTestCount"></span>
                </td>
                <td style="<% if (!alwaysValidate) { %> display:none <% } %>">
                    <span><bean:message key="dashboard.todayStatus.awaitingValidation"/></span>
                    <span id="awaitingValidationCount"></span>
                </td>
                <td>
                    <span><bean:message key="dashboard.todayStatus.Completed"/></span>
                    <span id="completedTestCount"></span>
                </td>
                <td>
                    <span class="stats-header"><bean:message key="dashboard.todayStatus.totalPatientsToday"/></span><span> : </span>
                    <span id="totalCollectedTodayCount"></span>
                </td>
            </tr>
        </table>
    </div>
    <span id = "translatedColumnNames"
        data-patientID = '<bean:message key="dashboard.sample.column.patientID"/>'
        data-patientName = '<bean:message key="dashboard.sample.column.patientName"/>'
        data-source = '<bean:message key="dashboard.sample.column.source"/>'
        data-sectionNames = '<bean:message key="dashboard.sample.column.sectionNames"/>'
        data-total = '<bean:message key="dashboard.sample.column.total"/>'
        data-notes = '<bean:message key="dashboard.sample.column.notes"/>'
        data-action = '<bean:message key="dashboard.sample.column.action"/>'

        data-accessionNumber = '<bean:message key="dashboard.sample.column.accessionNumber"/>'
        data-pendingTests = '<bean:message key="dashboard.sample.column.pendingTests"/>'
        data-pendingValidation = '<bean:message key="dashboard.sample.column.pendingValidation"/>'
        data-completed = '<bean:message key="dashboard.sample.column.completed"/>'
        data-printed = '<bean:message key="dashboard.sample.column.printed"/>'
        data-link-collectSample = '<bean:message key="dashboard.sample.column.link.collectSample"/>'
        data-link-result = '<bean:message key="dashboard.sample.column.link.result"/>'
        data-link-validate = '<bean:message key="dashboard.sample.column.link.validate"/>'
        data-link-print = '<bean:message key="dashboard.sample.column.link.print"/>'
    ></span>
    <div id="tabs">
        <span class="samplesToCollect sample_title_dashboard"><bean:message key="dashboard.msg.samplesToCollect"/></span>
        <span class="samplesCollected sample_title_dashboard"><bean:message key="dashboard.msg.samplesCollected"/></span>
        <ul>
            <li><a href="#todaySamplesToCollectListContainer" id="todaySamplesToCollectListContainerId" ><bean:message key="dashboard.table.today"/></a></li>
            <li class="backLog_first"><a href="#backlogSamplesToCollectListContainer" id="backlogSamplesToCollectListContainerId"><bean:message key="dashboard.table.backlog"/></a></li>
            <li><a href="#todaySamplesCollectedListContainer" id="todaySamplesCollectedListContainerId" ><bean:message key="dashboard.table.today"/></a></li>
            <li><a href="#backlogSamplesCollectedListContainer" id="backlogSamplesCollectedListContainerId" ><bean:message key="dashboard.table.backlog"/></a></li>
         </ul>
        <div id="todaySamplesToCollectListContainer">
                <div id="todaySamplesToCollectListContainer-slick-grid"></div>
        </div>
        <div id="backlogSamplesToCollectListContainer">
                <div id="backlogSamplesToCollectListContainer-slick-grid"></div>
        </div>
        <div id="todaySamplesCollectedListContainer">
                <div id="todaySamplesCollectedListContainer-slick-grid"></div>
        </div>
        <div id="backlogSamplesCollectedListContainer">
                <div id="backlogSamplesCollectedListContainer-slick-grid"></div>
        </div>
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
        jQuery("#samplesToCollect").text(stats.samplesToCollect);
        jQuery("#samplesCollected").text(stats.samplesCollected);
    }

    jQuery(document).ready(function() {
        var todayOrderList = JSON.parse('<%=todayOrderListJson%>');
        var todaySampleNotCollectedList = JSON.parse('<%=todaySampleNotCollectedListJson%>');
        var backlogSampleNotCollectedList = JSON.parse('<%=backlogSampleNotCollectedListJson%>');
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
            totalSamplesCount: todayOrderList.length + todaySampleNotCollectedList.length,
            totalCollectedTodayCount: todayOrderList.filter(function(order){ return isToday(new Date(order.enteredDate)) }).length,
            samplesToCollect: todaySampleNotCollectedList.length,
            samplesCollected: todayOrderList.length
        }


       showStats(todayStats)

        var todaySamplesToCollectObject = new order("#todaySamplesToCollectListContainer-slick-grid", todaySampleNotCollectedList, generateAllLinksForOrder, getColumnsForSampleNotCollected, false);
        var dataViewForTodaySamplesToCollect = new Slick.Data.DataView();
        var gridForTodaySamplesToCollect = new Slick.Grid(todaySamplesToCollectObject.div, dataViewForTodaySamplesToCollect, todaySamplesToCollectObject.columns,options);
        createGrid(gridForTodaySamplesToCollect, dataViewForTodaySamplesToCollect, todaySamplesToCollectObject, onRowSelection);

        var backlogSamplesToCollectObject = new order("#backlogSamplesToCollectListContainer-slick-grid", backlogSampleNotCollectedList, generateAllLinksForOrder, getColumnsForSampleNotCollected, false);
        var dataViewForBacklogSamplesToCollect = new Slick.Data.DataView();
        var gridForBacklogSamplesToCollect = new Slick.Grid(backlogSamplesToCollectObject.div, dataViewForBacklogSamplesToCollect, backlogSamplesToCollectObject.columns,options);
        createGrid(gridForBacklogSamplesToCollect, dataViewForBacklogSamplesToCollect, backlogSamplesToCollectObject, onRowSelection);

        var todayOrdersObject = new order("#todaySamplesCollectedListContainer-slick-grid", todayOrderList, generateAllLinksForOrder, getColumnsForTodayOrder, <%= alwaysValidate%>);
        var dataViewForTodayTab = new Slick.Data.DataView();
        var gridForTodayOrder = new Slick.Grid(todayOrdersObject.div, dataViewForTodayTab, todayOrdersObject.columns,options);
        createGrid(gridForTodayOrder, dataViewForTodayTab, todayOrdersObject, onRowSelection);

        var backlogOrdersObject = new order("#backlogSamplesCollectedListContainer-slick-grid", backlogOrderList, generateAllLinksForOrder, getColumnsForBacklogOrder, <%= alwaysValidate%>);
        var dataViewForBacklogTab = new Slick.Data.DataView();
        var gridForBacklogOrder = new Slick.Grid(backlogOrdersObject.div, dataViewForBacklogTab, backlogOrdersObject.columns,options);
        createGrid(gridForBacklogOrder, dataViewForBacklogTab, backlogOrdersObject, onRowSelection);

        var activeTab = <%= (String)request.getSession().getAttribute("activeTab") %>;
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

        function saveActiveTab(tabPosition) {
            new Ajax.Request(
                    'tabSelectServlet',  //url
                    {//options
                        method: 'get', //http method
                        parameters: 'activeTab=' + tabPosition
                    }
            );
        }

        jQuery("#todaySamplesToCollectListContainerId").on("click", function(){
            saveActiveTab(0);
        });
        jQuery("#backlogSamplesToCollectListContainerId").on("click", function(){
            saveActiveTab(1);
        });
        jQuery("#todaySamplesCollectedListContainerId").on("click", function(){
            saveActiveTab(2);
        });
        jQuery("#backlogSamplesCollectedListContainerId").on("click", function(){
            saveActiveTab(3);
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

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
<bean:define id="todayStats" name="<%=formName%>" property="todayStats" />

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

  

<div>
    <div id="todayStat">
        <table ">
            <tr>
                <td>
                    <input id="refreshButton" type="button" value="Refresh">
                </td>
                <td>
                    <div class="arrow-text"> Today's Stats</div><div class="arrow-right"></div>
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
                    <span>Total Samples Collected: </span>
                    <span id="totalSamplesCount"></span>
                </td>
            </tr>
        </table>
    </div>

    <div id="tabs">
        <ul>
            <li><a href="#inProgressListContainer">In Progress</a></li>
            <li><a href="#completedListContainer">Completed</a></li>
         </ul>
        <div id="inProgressListContainer"></div>
        <div id="completedListContainer"></div>
    </div>

    <div id="patientDetails" class="hide">
        <div class='pd-more-info'><span class='pd-key'>Patient ID: </span><span class='pd-value' id="patientId"></span></div>
        <div class='pd-more-info'><span class='pd-key'>Name : </span><span class='pd-value' id="name"></span></div>
        <div class='pd-more-info'><span class='pd-key'>Father/Husband's Name : </span><span class='pd-value' id="primaryRelative"></span></div>
        <div class='pd-more-info'><span class='pd-key'>Village : </span><span class='pd-value' id="village"></span></div>
        <div class='pd-more-info'><span class='pd-key'>Gender : </span><span  class='pd-value' id="gender"></span></div>
        <div class='pd-more-info'><span class='pd-key'>Age : </span><span class='pd-value' id="age"></span></div>
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

    var showStats = function(){
        var stats = JSON.parse('<%=todayStats%>');
        jQuery("#todayStat").show();
        jQuery("#awaitingTestCount").text(stats.awaitingTestCount);
        jQuery("#awaitingValidationCount").text(stats.awaitingValidationCount);
        jQuery("#completedTestCount").text(stats.completedTestCount);
        jQuery("#totalSamplesCount").text(stats.totalSamplesCount);
    }

    jQuery(document).ready(function() {
        showStats()
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
            collapsible: true,
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

    var showPatientDetails = function(stNumber, firstName, lastName, primaryRelative, village, gender, age) {
        jQuery("#patientDetails").show();
        jQuery("#patientId").text(stNumber);
        jQuery("#name").text(firstName + " " + lastName);
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

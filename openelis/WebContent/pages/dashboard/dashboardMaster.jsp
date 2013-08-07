<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date,
	us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>
<bean:define id="formName" value='<%= (String)request.getAttribute(IActionConstants.FORM_NAME) %>' />
<bean:define id="orderList" name="<%=formName%>" property="inProgressOrderList" />

<%!
String path = "";
String basePath = "";
%>

<%
path = request.getContextPath();
basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<link rel="stylesheet" type="text/css" href="<%=basePath%>css/slickgrid/slick.grid.css" />

<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.event.drag-2.2.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.event.drop-2.2.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.core.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.grid.js"></script>


<div id="inProgressListContainer" style="width:900px;height:400px;"></div>

<script type="text/javascript">
    var grid;
    var columns = [
        {id: "accessionNumber", name: "Accession Number", field: "accessionNumber", sortable: true, selectable: true, focusable: true},
        {id: "stNumber", name: "PatientID", field: "stNumber", sortable: true, selectable: true, focusable: true},
        {id: "firstName", name: "PatientName", field: "firstName", sortable: true, selectable: true, focusable: true},
        {id: "pendingTestCount", name: "Pending Tests", field: "pendingTestCount", sortable: true, selectable: true, focusable: true},
        {id: "totalTestCount", name: "Total Tests", field: "totalTestCount", sortable: true, selectable: true, focusable: true},
        {id: "source", name: "Source", field: "source", sortable: true, selectable: true, focusable: true},
    ];

    var options = {
        enableColumnReorder: false
    };

    jQuery(document).ready(function() {
        var orderList = "<%= orderList %>";
        var orderListParsed = JSON.parse(orderList);
        grid = new Slick.Grid("#inProgressListContainer", orderListParsed, columns, options);

        grid.onSort.subscribe(function(e, args){ // args: sort information.
            var field = args.sortCol.field;
            orderListParsed.sort(function(a, b){
                var result = a[field] > b[field] ? 1 : a[field] < b[field] ? -1 : 0;
                return args.sortAsc ? result : -result;
            });
            grid.invalidate();
        });
    });
</script>

<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date,
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

<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.event.drag-2.2.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.event.drop-2.2.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.core.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.formatters.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.grid.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/dashBoard/inProgressOrder.js"></script>



<div id="tabs">
     <ul>
        <li><a href="#inProgressListContainer">In Progress</a></li>
        <li><a href="#completedListContainer">Completed</a></li>
     </ul>
    <div id="inProgressListContainer" style="width:600px"></div>
    <div id="completedListContainer" style="width:600px"></div>

</div>

<script type="text/javascript">

    function formatter(row, cell, value, columnDef, dataContext) {
        return value;
    }

    var grid;

    var options = {
        enableColumnReorder: false,
        autoHeight:true,
        autoWidth: true
    };

    jQuery(document).ready(function() {
        var inProgressObject = new inProgressOrder("#inProgressListContainer", formatter, "<%= inProgressOrderListJson %>");

        grid = new Slick.Grid(inProgressObject.div, inProgressObject.orderList(), inProgressObject.columns,options);

        grid.onSort.subscribe(function(e, args){ // args: sort information.
            var field = args.sortCol.field;
            inProgressObject.orderList().sort(function(a, b){
                var result = a[field] > b[field] ? 1 : a[field] < b[field] ? -1 : 0;
                return args.sortAsc ? result : -result;
            });
            grid.invalidate();
        });

        var completedOrderObject = new completedOrder("#completedListContainer", formatter, "<%= completedOrderListJson %>" );
              var grid2 = new Slick.Grid(completedOrderObject.div, completedOrderObject.orderList(), completedOrderObject.columns, options);

                grid2.onSort.subscribe(function(e, args){ // args: sort information.
                    var field = args.sortCol.field;
                    completedOrderObject.orderList().sort(function(a, b){
                    var result = a[field] > b[field] ? 1 : a[field] < b[field] ? -1 : 0;
                    return args.sortAsc ? result : -result;
                });
                    grid2.invalidate();
                });


         jQuery("#tabs").tabs();
    });
</script>

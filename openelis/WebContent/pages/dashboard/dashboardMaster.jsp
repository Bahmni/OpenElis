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
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.dataview.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.grid.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/merge-sort.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/dashBoard/orders.js"></script>

<input id="refreshButton" type="button" value="Refresh">

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
    var dataView;
    var dataView1;
    var columnFilters = {};
    var columnFilters2 = {};

    var options = {
        enableColumnReorder: false,
        autoHeight:true,
        autoWidth: true,
        showHeaderRow: true,
        enableCellNavigation: true,
        showHeaderRow: true,
        headerRowHeight: 30,
        explicitInitialization: true
    };

    function filter(item) {
        for (var columnId in columnFilters) {
          if (columnId !== undefined && columnFilters[columnId] !== "") {
            var c = grid.getColumns()[grid.getColumnIndex(columnId)];
            if (item[c.field].indexOf(columnFilters[columnId]) == -1 ) {
              return false;
            }
          }
        }
        return true;
      }

      function filter2(item) {
              for (var columnId in columnFilters2) {
                if (columnId !== undefined && columnFilters2[columnId] !== "") {
                  var c = grid.getColumns()[grid.getColumnIndex(columnId)];
                  if (item[c.field].indexOf(columnFilters2[columnId]) == -1 ) {
                    return false;
                  }
                }
              }
              return true;
            }


    jQuery(document).ready(function() {
        var inProgressObject = new inProgressOrder("#inProgressListContainer", formatter, "<%= inProgressOrderListJson %>");

         var indexesOfNonSearchableColumns = inProgressObject.indexesOfNonSearchableColumns();



        dataView = new Slick.Data.DataView();
        grid = new Slick.Grid(inProgressObject.div, dataView, inProgressObject.columns,options);

        grid.onSort.subscribe(function(e, args){ // args: sort information.
           var field = args.sortCol.field;
               data = inProgressObject.orderList().mergeSort(function (a, b) {
                  var result =
                      a[field] > b[field] ? 1 :
                      a[field] < b[field] ? -1 :
                      0;
                  return args.sortAsc ? result : -result;
               });
           grid.invalidate();
           grid.setData(data);
           grid.render();
        });


         dataView.onRowCountChanged.subscribe(function (e, args) {
              grid.updateRowCount();
              grid.render();
            });

            dataView.onRowsChanged.subscribe(function (e, args) {
              grid.invalidateRows(args.rows);
              grid.render();
            });


            jQuery(grid.getHeaderRow()).delegate(":input", "change keyup", function (e) {
              var columnId = jQuery(this).data("columnId");
              if (columnId != null) {
                columnFilters[columnId] = jQuery.trim(jQuery(this).val());
                dataView.refresh();
              }
            });

            grid.onHeaderRowCellRendered.subscribe(function(e, args) {
                jQuery(args.node).empty();
                jQuery("<input type='text'>")
                   .data("columnId", args.column.id)
                   .val(columnFilters[args.column.id])
                   .appendTo(args.node);
            });

            grid.init();

            dataView.beginUpdate();
            dataView.setItems(inProgressObject.orderList());
            dataView.setFilter(filter);
            dataView.endUpdate();

            jQuery.each(indexesOfNonSearchableColumns, function(id, index){
                jQuery(".slick-headerrow-column.l" + index).find("input").hide();
            });



        var completedOrderObject = new completedOrder("#completedListContainer", formatter, "<%= completedOrderListJson %>" );

              var indexesOfNonSearchableColumns = completedOrderObject.indexesOfNonSearchableColumns();


              dataView1 = new Slick.Data.DataView();
              grid2 = new Slick.Grid(completedOrderObject.div, dataView1, completedOrderObject.columns, options);



            grid2.onSort.subscribe(function(e, args){ // args: sort information.
               var field = args.sortCol.field;
                 data = completedOrderObject.orderList().mergeSort(function (a, b) {
                    var result =
                        a[field] > b[field] ? 1 :
                        a[field] < b[field] ? -1 :
                        0;
                    return args.sortAsc ? result : -result;
                 });
                grid2.invalidate();
                grid2.setData(data);
                grid2.render();
            });

            dataView1.onRowCountChanged.subscribe(function (e, args) {
              grid2.updateRowCount();
              grid2.render();
            });

            dataView1.onRowsChanged.subscribe(function (e, args) {
              grid2.invalidateRows(args.rows);
              grid2.render();
            });


            jQuery(grid2.getHeaderRow()).delegate(":input", "change keyup", function (e) {
              var columnId = jQuery(this).data("columnId");
              if (columnId != null) {
                columnFilters2[columnId] = jQuery.trim(jQuery(this).val());
                dataView1.refresh();
              }
            });

            grid2.onHeaderRowCellRendered.subscribe(function(e, args) {
                jQuery(args.node).empty();
                jQuery("<input type='text'>")
                   .data("columnId", args.column.id)
                   .val(columnFilters2[args.column.id])
                   .appendTo(args.node);
            });

            grid2.init();

            dataView1.beginUpdate();
            dataView1.setItems(completedOrderObject.orderList());
            dataView1.setFilter(filter2);
            dataView1.endUpdate();

            jQuery.each(indexesOfNonSearchableColumns, function(id, index){
                jQuery(".slick-headerrow-column.l" + index).find("input").hide();
            });




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
            location.href +="?activeTab=" + index;
            return false;
         })

    });
</script>

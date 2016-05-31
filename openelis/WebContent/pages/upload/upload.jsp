<%@ page language="java"
	contentType="text/html; charset=utf-8"
	import="java.util.Date,
	us.mn.state.health.lims.common.util.SystemConfiguration,
	us.mn.state.health.lims.common.action.IActionConstants" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/labdev-view" prefix="app" %>

<%!
String path = "";
String basePath = "";
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
<script type="text/javascript" src="<%=basePath%>scripts/dashBoard/createGrid.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/utils.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/slickgrid/slick.autotooltips.js"></script>

<div>
    <h1><bean:message key="result.uploadedFile"/></h1>
    <div id="upload">
        <form method="POST" action="/openelis/DoUpload.do" enctype="multipart/form-data">
            <table>
                <tr>
                <td><button type="button" id="refresh"><bean:message key="upload.refresh"/></button></td>
                <td><input type="file" name="file" value="test_results_Upload_Template.csv"></td>
                <td><span><input type="radio" name="importType" value="patient" checked="checked"><bean:message key="upload.patient"/></span></td>
                <td><span><input type="radio" name="importType" value="sample"><bean:message key="upload.sample"/></span> </td>
                <td><input type="submit" value='<bean:message key="upload.upload"/>'>  </td>
            </table>
        </form>
    </div>

    <span id = "uploadDetail"
        data-fileName = '<bean:message key="dashboard.uploadedFile.fileName"/>'
        data-type = '<bean:message key="dashboard.uploadedFile.type"/>'
        data-dateOfUpload = '<bean:message key="dashboard.uploadedFile.dateOfUpload"/>'
        data-status = '<bean:message key="dashboard.uploadedFile.status"/>'
        data-stage = '<bean:message key="dashboard.uploadedFile.stage"/>'
        data-download = '<bean:message key="dashboard.uploadedFile.download"/>'
        >
    </span>
    <div id="importStatusGrid" style="width:1000px">
    </div>

    <div id="uploadDetails" class="hide details">
        <div class='details-more-info'><span class='details-key'>File Name: </span><span class='details-value' id="originalFileName"></span></div>
        <div class='details-more-info'><span class='details-key'>Uploaded By: </span><span class='details-value' id="uploadedBy"></span></div>
        <div class='details-more-info'><span class='details-key'>Status: </span><span class='details-value' id="uploadStatus"></span></div>
        <div class='details-more-info'><span class='details-key'>Success Count: </span><span class='details-value' id="successfulRecordsCount"></span></div>
        <div class='details-more-info'><span class='details-key'>Fail Count: </span><span class='details-value' id="failedRecordsCount"></span></div>
        <div class='details-more-info'><span class='details-key'>Error Message: </span><span id="errorMessage"></span></div>
    </div>
</div>


<style>
    .bar {
        height: 18px;
        background: green;
    }
</style>

<script type="text/javascript">
    var gridForInImportStatus;

    var columns = [
        {id: "originalFileName", name: jQuery("#uploadDetail").attr("data-
        fileName"), field: "originalFileName", sortable: true, width: 200},
        {id: "type", name:jQuery("#uploadDetail").attr("data-type"), field: "type", sortable: true, width: 100},
        {id: "startTime", name:jQuery("#uploadDetail").attr("data-dateOfUpload"), field: "startTime", sortable: true, width: 200,
            formatter: function ( row, cell, value, columnDef, dataContext ) {
                function padWithZeroes(aField) {
                    if (aField < 10)
                        return "0" + aField;
                    return aField;
                }

                if (value) {
                    var startTime = new Date(value);
                    return padWithZeroes(startTime.getDate()) + "-" + padWithZeroes(startTime.getMonth()) + "-" + padWithZeroes(startTime.getFullYear()) + " " +
                             padWithZeroes(startTime.getHours()) + ":" + padWithZeroes(startTime.getMinutes()) + ":" + padWithZeroes(startTime.getSeconds());
                }
                return "";
            }
        },
        {id: "status", name: jQuery("#uploadDetail").attr("data-status"), field: "status", sortable: true, width: 300},
        {id: "stage", name: jQuery("#uploadDetail").attr("data-stage"), field: "stageName", sortable: true, width: 100},
        {id: "errorFileName", name: jQuery("#uploadDetail").attr("data-download"), field: "errorFileName", sortable: true, width: 100,
            formatter: function ( row, cell, value, columnDef, dataContext ) {
                if (value) {
                    return '<a href="' + value + '">ErrorFile</a>';
                }
                return "";
            },
        }
    ];

    var options = {
        enableColumnReorder: false,
        autoHeight:true,
        enableCellNavigation: true,
        forceFitColumns: true,
        multiColumnSort: true,
    };

    var showUploadDetails = function(row) {
        jQuery("#uploadDetails").show();
        jQuery("#originalFileName").text(row.originalFileName);
        jQuery("#uploadedBy").text(row.uploadedBy);
        jQuery("#uploadStatus").text(row.status);
        jQuery("#successfulRecordsCount").text(row.successfulRecords);
        jQuery("#failedRecordsCount").text(row.failedRecords);
        jQuery("#errorMessage").text(row.stackTrace);
    }

    jQuery(document).ready(function() {
        jQuery("#refresh").on("click", renderGrid);

        function renderGrid() {
            jQuery.ajax('UploadDashboard.do', {
                type: 'get',
                success: function(data) {
                    gridForInImportStatus = new Slick.Grid("#importStatusGrid", data, columns, options);
                    gridForInImportStatus .setSelectionModel(new Slick.RowSelectionModel());
                    gridForInImportStatus.registerPlugin( new Slick.AutoTooltips({ enableForHeaderCells: true }) );
                    gridForInImportStatus.init();

                    gridForInImportStatus.onSort.subscribe(function (e, args) {
                        var cols = args.sortCols;
                        data.sort(function(dataRow1, dataRow2) {
                            for (var i = 0, l = cols.length; i < l; i++) {
                                var field = cols[i].sortCol.field;
                                var sign = cols[i].sortAsc ? 1 : -1;
                                var value1 = dataRow1[field], value2 = dataRow2[field];
                                var result = (value1 == value2 ? 0 : (value1 > value2 ? 1 : -1)) * sign;
                                if (result != 0) {
                                    return result;
                                }
                            }
                            return 0;
                        });
                        gridForInImportStatus.invalidate();
                        gridForInImportStatus.render();
                    });

                    gridForInImportStatus.onSelectedRowsChanged.subscribe(function(e, args) {
                        var row = args.grid.getDataItem(args.rows[0]);
                        showUploadDetails(row);
                    });
                },
            });
        }

        renderGrid();
    });
</script>




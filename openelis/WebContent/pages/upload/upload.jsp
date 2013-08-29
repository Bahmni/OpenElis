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
basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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

<script type="text/javascript" src="<%=basePath%>scripts/jquery-file-upload/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery-file-upload/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery-file-upload/jquery.fileupload.js"></script>

<div>
    <h1>Uploaded patient file</h1>
    <div>
        <form>
            <button type="button" id="refresh">Refresh</button>
            <br/>
            <p id="status"></p>

            <div id="progress">
                <div class="bar" style="width: 0%;"></div>
            </div>
            <br/>
            <input id="file" type="file" data-url="/openelis/DoUpload.do" accept="text/csv">
            <br/>
            <button type="button" id="upload">Upload</button>
        </form>
    </div>

    <div id="importStatusGrid" style="width:1000px">
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
        {id: "originalFileName", name: "Name of the File", field: "originalFileName", sortable: true, width: 200},
        {id: "type", name: "Type of File", field: "type", sortable: true, width: 75},
        {id: "startTime", name: "Date of Upload", field: "startTime", sortable: true, width: 100,
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
        {id: "status", name: "Status", field: "status", sortable: true, width: 200},
        {id: "successfulRecords", name: "Successful Records", field: "successfulRecords", sortable: true, width: 50},
        {id: "failedRecords", name: "Failed Records", field: "failedRecords", sortable: true, width: 50},
        {id: "errorFileName", name: "Download Error File", field: "errorFileName", sortable: true, width: 400,
                formatter: function ( row, cell, value, columnDef, dataContext ) {
                      if (value) {
                        return '<a href="' + value + '">' + value.substring(value.lastIndexOf("/") + 1) + '</a>';
                      }
                      return "";
                  },}
    ];

    var options = {
        enableColumnReorder: false,
        autoHeight:true,
        enableCellNavigation: true,
        forceFitColumns: true,
        multiColumnSort: true,
    };

    jQuery(document).ready(function() {
        jQuery('#file').fileupload({
            dataType: 'text',
            replaceFileInput: false,
            add: function (e, data) {
                data.context = jQuery('#upload').click(function () {
                    data.context = jQuery('#status').text('Uploading...');
                    data.submit();
                });
            },
            done: function (e, data) {
                data.context.text('Upload finished.');
                renderGrid();
            },
            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                jQuery('#progress .bar').css('width', progress + '%');
            }
        });

        jQuery("#refresh").on("click", renderGrid);

        function renderGrid() {
            jQuery.ajax('UploadDashboard.do', {
                type: 'get',
                success: function(data) {
                    gridForInImportStatus = new Slick.Grid("#importStatusGrid", data, columns, options);
                    gridForInImportStatus.init();

                    gridForInImportStatus.onSort.subscribe(function (e, args) {
                          var cols = args.sortCols;

                          data.sort(function (dataRow1, dataRow2) {
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

                },
            });
        }

        renderGrid();
    });
</script>




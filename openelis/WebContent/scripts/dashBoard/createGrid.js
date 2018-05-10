function createGrid(grid, dataView, orderObject, onRowSelection) {
        var columnFilters = {};
        grid.setSelectionModel(new Slick.CellSelectionModel());
        grid.setSelectionModel(new Slick.RowSelectionModel());
        grid.registerPlugin(new Slick.CellExternalCopyManager());
        grid.registerPlugin(new Slick.AutoTooltips({ enableForHeaderCells: true }));

        grid.onSort.subscribe(function(e, args){ // args: sort information.
           var field = args.sortCol.field;
           var asc = args.sortAsc;

           sort(field, asc, grid);
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

        grid.onCellChange.subscribe(function (e, args) {
            dataView.updateItem(args.item.id, args.item);
        });

        grid.onSelectedRowsChanged.subscribe(function(e, args) {
            var row = args.grid.getDataItem(args.rows[0]);
            if(onRowSelection != null) {
                onRowSelection(row);
            }
        });

        grid.init();

        dataView.beginUpdate();
        dataView.setItems(orderObject.orderList());
        dataView.setFilter(getFilterFor(grid, columnFilters));
        dataView.endUpdate();

        jQuery.each(orderObject.indexesOfNonSearchableColumns(), function(id, index){
            jQuery(orderObject.div + " .slick-headerrow-column.l" + index).find("input").hide();
        });
    }

function getFilterFor(grid, columnFilters) {
    return function filter (item) {
        for (var columnId in columnFilters) {
            if (columnId !== undefined && columnFilters[columnId] !== "") {
                var c = grid.getColumns()[grid.getColumnIndex(columnId)];
                if (item[c.field] == null || item[c.field].toLowerCase().indexOf(columnFilters[columnId].toLowerCase()) == -1) {
                    return false;
                }
            }
        }
        return true;
    }
}

function sort(field, isAsc, grid){
     var dataView = grid.getData();
     var comparer = function(a, b) {
         var x = a[field], y = b[field];
         if (x == y) {
             return a["accessionNumber"] > b["accessionNumber"];
         }
         return (x > y ? 1 : -1);
     }
     dataView.sort(comparer, isAsc);
}

function getFilteredItems(dataView){
    var items = [];
    var length = dataView.getLength();
    for (var i = 0 ; i < length ; i++){
        items.push(dataView.getItem(i));
    }
    return items;
}



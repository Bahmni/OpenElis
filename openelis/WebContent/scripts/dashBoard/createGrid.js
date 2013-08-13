
var currentGrid;
function createGrid(grid, dataView, orderObject) {
        currentGrid = grid;

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

        grid.init();

        dataView.beginUpdate();
        dataView.setItems(orderObject.orderList());
        dataView.setFilter(filter);
        dataView.endUpdate();

        jQuery.each(orderObject.indexesOfNonSearchableColumns(), function(id, index){
            jQuery(".slick-headerrow-column.l" + index).find("input").hide();
        });
    }

function filter(item) {
    for (var columnId in columnFilters) {
      if (columnId !== undefined && columnFilters[columnId] !== "") {
        var c = currentGrid.getColumns()[currentGrid.getColumnIndex(columnId)];
        if (item[c.field].indexOf(columnFilters[columnId]) == -1 ) {
          return false;
        }
      }
    }
    return true;
}

function sort(field, isAsc, grid){
     var dataView = grid.getData();
     var data = getFilteredItems(dataView).mergeSort(function (a, b) {
      var result =
          a[field] > b[field] ? 1 :
          a[field] < b[field] ? -1 :
          0;
      return isAsc ? result : -result;
   });
   dataView.setRows(data);

   var data = dataView.getItems().mergeSort(function (a, b) {
         var result =
             a[field] > b[field] ? 1 :
             a[field] < b[field] ? -1 :
             0;
         return isAsc ? result : -result;
      });
    dataView.setItems(data);
}

function getFilteredItems(dataView){
    var items = [];
    var length = dataView.getLength();
    for (var i = 0 ; i < length ; i++){
        items.push(dataView.getItem(i));
    }
    return items;
}



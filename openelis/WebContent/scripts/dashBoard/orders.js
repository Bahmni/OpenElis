function inProgressOrder(div, formatter, inProgressOrder) {
     this.div = div;
     this.inProgressOrder = JSON.parse(inProgressOrder);
     this.columns = [
            {id: "accessionNumber", name: "Accession Number", field: "accessionNumber", sortable: true, selectable: true, focusable: true, index:0, searchable: true },
            {id: "stNumber", name: "PatientID", field: "stNumber", sortable: true, selectable: true, focusable: true, index:1, searchable: true},
            {id: "name", name: "PatientName", field: "name", sortable: true, selectable: true, focusable: true, index:2, searchable: true},
            {id: "pendingTestCount", name: "Pending Tests", field: "pendingTestCount", sortable: true, selectable: true, focusable: true, index:3, searchable: false},
            {id: "totalTestCount", name: "Total Tests", field: "totalTestCount", sortable: true, selectable: true, focusable: true, index:4, searchable: false},
            {id: "source", name: "Source", field: "source", sortable: true, selectable: true, focusable: true, index:5, searchable: true},
            {id: "link", name: "Action", field: "link", selectable: true, focusable: true, cssClass: "cell-title", formatter: formatter, index:6, searchable: false}
     ];

     this.indexesOfNonSearchableColumns = function(){
        var indexes = [];

        jQuery.each(this.columns, function(id, column){
            if (!column.searchable){
                indexes.push(column.index);
            }
        });

        return indexes;
     }

     this.orderList = function (){

        if (this.orders) {
            return this.orders;
        }

        this.orders = jQuery.map(this.inProgressOrder, function(order, i) {
            order.id= i;
            order.link = "<a href='ResultValidationForAccessionNumber.do?accessionNumber=" + order.accessionNumber + "&type=Validation+By+Accession+Number&test='>Validate</a>";
            order.name = order.firstName + " " + order.lastName;
            return order;
        });

        return this.orders;
     };
}

function completedOrder(div, formatter, completedOrder) {
     this.div = div;
     this.completedOrder = JSON.parse(completedOrder);
     this.columns = [
            {id: "accessionNumber", name: "Accession Number", field: "accessionNumber", sortable: true, selectable: true, focusable: true, index:0, searchable: true},
            {id: "stNumber", name: "PatientID", field: "stNumber", sortable: true, selectable: true, focusable: true, index:1, searchable: true},
            {id: "name", name: "PatientName", field: "name", sortable: true, selectable: true, focusable: true, index:2, searchable: true},
            {id: "source", name: "Source", field: "source", sortable: true, selectable: true, focusable: true, index:3, searchable: true},
            {id: "link", name: "PrintReport", field: "link", selectable: true, focusable: true, cssClass: "cell-title", formatter: formatter, index:4, searchable: false}
        ];
         this.indexesOfNonSearchableColumns = function(){
                var indexes = [];

                jQuery.each(this.columns, function(id, column){
                    if (!column.searchable){
                        indexes.push(column.index);
                    }
                });

                return indexes;
             }
    this.orderList = function (){

            if (this.orders) {
                return this.orders;
            }

            this.orders = jQuery.map(this.completedOrder, function(order, i) {
                order.id= i;
                order.link = "<a href='ReportPrint.do?type=patient&report=patientHaitiClinical&accessionDirect="+ order.accessionNumber +"&patientNumberDirect=" + order.stNumber + "'>Print Report</a>";
                order.name = order.firstName + " " + order.lastName;
                return order;
            });

            return this.orders;
         };
}


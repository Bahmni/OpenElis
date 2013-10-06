
function order(div, orderJson, generateLink, getColumns) {
     this.div = div;
     this.orderArray = JSON.parse(orderJson);
     this.columns = getColumns();
         this.indexesOfNonSearchableColumns = function(){
                var indexes = [];

                jQuery.each(this.columns, function(id, column){

                    //we shouldn't add the id for the column if column.searchable is null.
                    // It assumes that we don't need to specify searchable property when the column is searchable
                    if (column.searchable == false){
                        indexes.push(column.index);
                    }
                });

                return indexes;
             }
    this.orderList = function (){

            if (this.orders) {
                return this.orders;
            }

            this.orders = jQuery.map(this.orderArray, function(order, i) {
                order.id= i;
                order.link = generateLink(order);
                order.name = order.firstName + " " + order.lastName;
                return order;
            });

            return this.orders;
         };
}

function generateLinkForCompletedOrder(order){
    return "<a target='_blank' href='ReportPrint.do?type=patient&report=patientHaitiClinical&accessionDirect="+ order.accessionNumber +"&patientNumberDirect=" + order.stNumber + "'>Print Report</a>";
}

function generateLinkForInProgressOrder(order){
    var enterResultLink = "<a href='AccessionResults.do?accessionNumber=" + order.accessionNumber + "&referer=LabDashboard'>Result</a>";
    //TODO: &type= is required in the url because of a bug I can't find the source of. The bug causes people without
    var validationLink = "<a href='ResultValidationForAccessionNumber.do?accessionNumber=" + order.accessionNumber + "&patientId=" + order.stNumber + "&referer=LabDashboard&type=&test='>Validate</a>";
    return enterResultLink + " | " + validationLink;
}

function getColumnsForInProgressOrder() {
     return [
                {id: "accessionNumber", name: "Accession Number", field: "accessionNumber", sortable: true, index:0, editor: Slick.Editors.Text,  minWidth:140},
                {id: "stNumber", name: "PatientID", field: "stNumber", sortable: true, editor: Slick.Editors.Text,  index:1 , minWidth:120},
                {id: "name", name: "PatientName", field: "name", sortable: true,  index:2, editor: Slick.Editors.Text,  minWidth:140},
                {id: "pendingTestCount", name: "Pending Tests", field: "pendingTestCount", sortable: true, editor: Slick.Editors.Text,  index:3, searchable: false, minWidth:110},
                {id: "pendingValidationCount", name: "Pending Validation", field: "pendingValidationCount", sortable: true, editor: Slick.Editors.Text,  index:4, searchable: false, minWidth:140},
                {id: "totalTestCount", name: "Total Tests", field: "totalTestCount", sortable: true, editor: Slick.Editors.Text,  index:5, searchable: false, minWidth:90},
                {id: "source", name: "Source", field: "source", sortable: true,  index:6, editor: Slick.Editors.Text, minWidth:90},
                {id: "link", name: "Action", field: "link",  cssClass: "cell-title", formatter: formatter, index:7,editor: Slick.Editors.Text, searchable: false, minWidth:130}
         ];
}

function dateFormatter(row, cell, value, columnDef, dataContext) {
    var date = new Date(value);
    return date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear() + " " + date.toLocaleTimeString();
}

function getColumnsForCompletedOrder(){
    return [
        {id: "accessionNumber", name: "Accession Number", field: "accessionNumber", sortable: true, index:0, editor: Slick.Editors.Text,  minWidth:150},
        {id: "stNumber", name: "PatientID", field: "stNumber", sortable: true,  index:1, editor: Slick.Editors.Text,minWidth:150 },
        {id: "name", name: "PatientName", field: "name", sortable: true,  index:2,  editor: Slick.Editors.Text, minWidth:150},
        {id: "source", name: "Source", field: "source", sortable: true,  index:3, editor: Slick.Editors.Text,minWidth:150 },
        {id: "completedDate", name: "Completed Date", sortable: true, field: "completedDate",  formatter: dateFormatter, cssClass: "cell-title", index:4, editor: Slick.Editors.Text, searchable: false,minWidth:180},
        {id: "link", name: "PrintReport", field: "link",  cssClass: "cell-title", formatter: formatter, index:5, editor: Slick.Editors.Text, searchable: false,minWidth:150},
        {id: "printed", name: "Printed", field: "alreadyPrinted",  sortable: true,  cssClass: "cell-title", index:6, editor: Slick.Editors.Text, searchable: false,minWidth:50}
    ];
}

function formatter(row, cell, value, columnDef, dataContext) {
    return value;
}

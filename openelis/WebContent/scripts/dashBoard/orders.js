
function order(div, orderArray, generateLink, getColumns, alwaysValidate) {
     this.div = div;
     this.orderArray = orderArray;
     this.columns = getColumns(alwaysValidate);
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
                order.link = generateLink(order, alwaysValidate);
                order.name = order.firstName + " " + (order.middleName ? order.middleName + " " : "") + order.lastName;
                return order;
            });

            return this.orders;
         };
}

function generateLinkForPrint(order){
    return "<a target='_blank' href='ReportPrint.do?type=patient&report=patientHaitiClinical&accessionDirect="+ order.accessionNumber +"&patientNumberDirect=" + order.stNumber + "'>Print</a>";
}


function generateAllLinksForOrder(order, alwaysValidate){
    if(order.accessionNumber == null){
        return  "<a target='_blank' href='SamplePatientEntry.do?id="+ order.orderId +"&patientId=" + order.stNumber +"'>Collect Sample</a>";
    }
    var enterResultLink = "<a href='AccessionResults.do?accessionNumber=" + order.accessionNumber + "&referer=LabDashboard'>Result</a>";
    if(alwaysValidate){
        //TODO: &type= is required in the url because of a bug I can't find the source of. The bug causes people without
        var validationLink = "<a href='ResultValidationForAccessionNumber.do?accessionNumber=" + order.accessionNumber + "&patientId=" + order.stNumber + "&referer=LabDashboard&type=&test='>Validate</a>";
        return enterResultLink + " | " + validationLink + " | " + generateLinkForPrint(order);
    }
    return enterResultLink + " | " + generateLinkForPrint(order);
}

function getColumnsForTodayOrder(alwaysValidate) {
    if (alwaysValidate) {
        return [
            {id:"accessionNumber", name:"Accession Number", field:"accessionNumber", sortable:true, index:0, editor:Slick.Editors.Text, minWidth:180},
            {id:"stNumber", name:"Patient ID", field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:1, minWidth:120},
            {id:"name", name:"Patient Name", field:"name", sortable:true, index:2, editor:Slick.Editors.Text, minWidth:140},
            {id:"pendingTestCount", name:"Pending Tests", field:"pendingTestCount", sortable:true, editor:Slick.Editors.Text, index:3, searchable:false, minWidth:130},
            {id:"pendingValidationCount", name:"Pending Validation", field:"pendingValidationCount", sortable:true, editor:Slick.Editors.Text, index:4, searchable:false, minWidth:160},
            {id:"totalTestCount", name:"Total", field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:5, searchable:false, minWidth:50},
            {id:"isCompleted", name:"Completed", field:"isCompleted", sortable:true, cssClass:"cell-title", index:6, formatter:Slick.Formatters.YesNo, searchable:false, minWidth:120},
            {id:"link", name:"Action", field:"link", cssClass:"cell-title", formatter:formatter, index:7, editor:Slick.Editors.Text, searchable:false, minWidth:180},
            {id:"isPrinted", name:"Printed", field:"isPrinted", sortable:true, cssClass:"cell-title", index:8, formatter:Slick.Formatters.Checkmark, searchable:false, minWidth:80},
        ];
    }
    else {
        return [
            {id:"accessionNumber", name:"Accession Number", field:"accessionNumber", sortable:true, index:0, editor:Slick.Editors.Text, minWidth:180},
            {id:"stNumber", name:"Patient ID", field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:1, minWidth:160},
            {id:"name", name:"Patient Name", field:"name", sortable:true, index:2, editor:Slick.Editors.Text, minWidth:160},
            {id:"pendingTestCount", name:"Pending Tests", field:"pendingTestCount", sortable:true, editor:Slick.Editors.Text, index:3, searchable:false, minWidth:130},
            {id:"totalTestCount", name:"Total", field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:5, searchable:false, minWidth:70},
            {id:"isCompleted", name:"Completed", field:"isCompleted", sortable:true, cssClass:"cell-title", index:6, formatter:Slick.Formatters.YesNo, searchable:false, minWidth:120},
            {id:"link", name:"Action", field:"link", cssClass:"cell-title", formatter:formatter, index:7, editor:Slick.Editors.Text, searchable:false, minWidth:180},
            {id:"isPrinted", name:"Printed", field:"isPrinted", sortable:true, cssClass:"cell-title", index:8, formatter:Slick.Formatters.Checkmark, searchable:false, minWidth:80},
        ];
    }
}

function getColumnsForBacklogOrder(alwaysValidate) {
    if (alwaysValidate) {
        return [
            {id:"accessionNumber", name:"Accession Number", field:"accessionNumber", sortable:true, index:0, editor:Slick.Editors.Text, minWidth:180},
            {id:"stNumber", name:"Patient ID", field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:1, minWidth:120},
            {id:"name", name:"Patient Name", field:"name", sortable:true, index:2, editor:Slick.Editors.Text, minWidth:140},
            {id:"pendingTestCount", name:"Pending Tests", field:"pendingTestCount", sortable:true, editor:Slick.Editors.Text, index:3, searchable:false, minWidth:130},
            {id:"pendingValidationCount", name:"Pending Validation", field:"pendingValidationCount", sortable:true, editor:Slick.Editors.Text, index:4, searchable:false, minWidth:160},
            {id:"totalTestCount", name:"Total", field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:5, searchable:false, minWidth:50},
            {id:"link", name:"Action", field:"link", cssClass:"cell-title", formatter:formatter, index:6, editor:Slick.Editors.Text, searchable:false, minWidth:180},
            {id:"isPrinted", name:"Printed", field:"isPrinted", sortable:true, cssClass:"cell-title", index:7, formatter:Slick.Formatters.Checkmark, searchable:false, minWidth:80},
        ];
    }
    else {
        return [
            {id:"accessionNumber", name:"Accession Number", field:"accessionNumber", sortable:true, index:0, editor:Slick.Editors.Text, minWidth:180},
            {id:"stNumber", name:"Patient ID", field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:1, minWidth:160},
            {id:"name", name:"Patient Name", field:"name", sortable:true, index:2, editor:Slick.Editors.Text, minWidth:160},
            {id:"pendingTestCount", name:"Pending Tests", field:"pendingTestCount", sortable:true, editor:Slick.Editors.Text, index:3, searchable:false, minWidth:130},
            {id:"totalTestCount", name:"Total", field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:5, searchable:false, minWidth:70},
            {id:"link", name:"Action", field:"link", cssClass:"cell-title", formatter:formatter, index:6, editor:Slick.Editors.Text, searchable:false, minWidth:180},
            {id:"isPrinted", name:"Printed", field:"isPrinted", sortable:true, cssClass:"cell-title", index:7, formatter:Slick.Formatters.Checkmark, searchable:false, minWidth:80},
        ];
    }
}
function getColumnsForSampleNotCollected() {
        return [
            {id:"stNumber", name:"Patient ID", field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:0, minWidth:160},
            {id:"name", name:"Patient Name", field:"name", sortable:true, index:1, editor:Slick.Editors.Text, minWidth:160},
            {id:"source", name:"Source", field:"source", sortable:false, index:2, editor:Slick.Editors.Text, minWidth:160},
            {id:"totalTestCount", name:"Total", field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:3, searchable:false, minWidth:70},
            {id:"link", name:"Action", field:"link", cssClass:"cell-title", formatter:formatter, index:4, editor:Slick.Editors.Text, searchable:false, minWidth:180}
        ];
}


function formatter(row, cell, value, columnDef, dataContext) {
    return value;
}

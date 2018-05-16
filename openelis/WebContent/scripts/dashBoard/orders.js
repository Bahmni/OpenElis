
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
                order.name = order.firstName + (order.middleName ? " " + order.middleName : "") + (order.lastName ? " " +order.lastName : "");
                return order;
            });

            return this.orders;
         };
}

function generateLinkForPrint(order){
    var printMessage = jQuery("#translatedColumnNames").attr("data-link-print");
    var printIcon = '<img id="actionIcon" src="images/print.svg" title="'+ printMessage +'">';
    return "<a id='print' target='_blank' href='ReportPrint.do?type=patient&report=patientHaitiClinical&accessionDirect="+ order.accessionNumber +"&patientNumberDirect=" + order.stNumber + "'>"+printIcon+"</a>";
}


function generateAllLinksForOrder(order, alwaysValidate){
    if(order.accessionNumber == null){
        var linkSample = jQuery("#translatedColumnNames").attr("data-link-collectSample");
        return  "<a href='SamplePatientEntry.do?id="+ order.orderId +"&patientId=" + order.stNumber + "'>"+linkSample+"</a>";
    }
    var resultMessage = jQuery("#translatedColumnNames").attr("data-link-result");
    var resultIcon = '<img id="actionIcon" src="images/result.svg" title="'+resultMessage+'">';
    var enterResultLink = "<a id='result' href='AccessionResults.do?accessionNumber=" + order.accessionNumber + "&referer=LabDashboard"+"'>"+resultIcon+"</a>";

    if(alwaysValidate){
        //TODO: &type= is required in the url because of a bug I can't find the source of. The bug causes people without
        var validateMessage = jQuery("#translatedColumnNames").attr("data-link-validate");
        var validateIcon = '<img id="actionIcon" src="images/validate.svg" title="'+validateMessage+'">';
        var validationLink = "<a id='validate' href='ResultValidationForAccessionNumber.do?accessionNumber=" + order.accessionNumber + "&patientId=" + order.stNumber + "&referer=LabDashboard&type=&test='>"+validateIcon+"</a>";
        return enterResultLink + " | " + validationLink + " | " + generateLinkForPrint(order);
    }
    return enterResultLink + " | " + generateLinkForPrint(order);
}

function getColumnsForTodayOrder(alwaysValidate) {
    if (alwaysValidate) {
        return [
            {id:"accessionNumber", name:jQuery("#translatedColumnNames").attr("data-accessionNumber"), field:"accessionNumber", sortable:true, index:0, editor:Slick.Editors.Text, minWidth:160},
            {id:"stNumber", name:jQuery("#translatedColumnNames").attr("data-patientID"), field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:1, minWidth:120},
            {id:"name", name:jQuery("#translatedColumnNames").attr("data-patientName"), field:"name", sortable:true, index:2, editor:Slick.Editors.Text, minWidth:140},
            {id:"sectionNames", name:jQuery("#translatedColumnNames").attr("data-sectionNames"), field:"sectionNames", searchable:true, sortable:true, index:3, editor:Slick.Editors.Text, minWidth:160},
            {id:"pendingTestCount", name:jQuery("#translatedColumnNames").attr("data-pendingTests"), field:"pendingTestCount", sortable:true, editor:Slick.Editors.Text, index:4, searchable:false, minWidth:100},
            {id:"pendingValidationCount", name:jQuery("#translatedColumnNames").attr("data-pendingValidation"), field:"pendingValidationCount", sortable:true, editor:Slick.Editors.Text, index:5, searchable:false, minWidth:140},
            {id:"totalTestCount", name:jQuery("#translatedColumnNames").attr("data-total"), field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:6, searchable:false, minWidth:50},
            {id:"notes", name:jQuery("#translatedColumnNames").attr("data-notes"), field:"comments", sortable:true, index:7, searchable:false, editor:Slick.Editors.Text, minWidth:170},
            {id:"isCompleted", name:jQuery("#translatedColumnNames").attr("data-completed"), field:"isCompleted", sortable:true, cssClass:"cell-title", index:8, formatter:Slick.Formatters.YesNo, searchable:false, minWidth:120},
            {id:"link", name:jQuery("#translatedColumnNames").attr("data-action"), field:"link", cssClass:"cell-title", formatter:formatter, index:9, editor:Slick.Editors.Text, searchable:false, minWidth:180},
            {id:"isPrinted", name:jQuery("#translatedColumnNames").attr("data-printed"), field:"isPrinted", sortable:true, cssClass:"cell-title", index:10, formatter:Slick.Formatters.Checkmark, searchable:false, minWidth:80},
        ];
    }
    else {
        return [
            {id:"accessionNumber", name:jQuery("#translatedColumnNames").attr("data-accessionNumber"), field:"accessionNumber", sortable:true, index:0, editor:Slick.Editors.Text, minWidth:160},
            {id:"stNumber", name:jQuery("#translatedColumnNames").attr("data-patientID"), field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:1, minWidth:170},
            {id:"name", name:jQuery("#translatedColumnNames").attr("data-patientName"), field:"name", sortable:true, index:2, editor:Slick.Editors.Text, minWidth:160},
            {id:"sectionNames", name:jQuery("#translatedColumnNames").attr("data-sectionNames"), field:"sectionNames", searchable:true, sortable:true, index:3, editor:Slick.Editors.Text, minWidth:160},
            {id:"pendingTestCount", name:jQuery("#translatedColumnNames").attr("data-pendingTests"), field:"pendingTestCount", sortable:true, editor:Slick.Editors.Text, index:5, searchable:false, minWidth:100},
            {id:"totalTestCount", name:jQuery("#translatedColumnNames").attr("data-total"), field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:6, searchable:false, minWidth:70},
            {id:"notes", name:jQuery("#translatedColumnNames").attr("data-notes"), field:"comments", sortable:true, index:7, searchable:false, editor:Slick.Editors.Text, minWidth:160},
            {id:"isCompleted", name:"Completed", field:"isCompleted", sortable:true, cssClass:"cell-title", index:8, formatter:Slick.Formatters.YesNo, searchable:false, minWidth:120},
            {id:"link", name:jQuery("#translatedColumnNames").attr("data-action"), field:"link", cssClass:"cell-title", formatter:formatter, index:9, editor:Slick.Editors.Text, searchable:false, minWidth:180},
            {id:"isPrinted", name:jQuery("#translatedColumnNames").attr("data-printed"), field:"isPrinted", sortable:true, cssClass:"cell-title", index:10, formatter:Slick.Formatters.Checkmark, searchable:false, minWidth:80},
        ];
    }
}

function getColumnsForBacklogOrder(alwaysValidate) {
    if (alwaysValidate) {
        return [
            {id:"accessionNumber", name:jQuery("#translatedColumnNames").attr("data-accessionNumber"), field:"accessionNumber", sortable:true, index:0, editor:Slick.Editors.Text, minWidth:160},
            {id:"stNumber", name:jQuery("#translatedColumnNames").attr("data-patientID"), field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:1, minWidth:120},
            {id:"name", name:jQuery("#translatedColumnNames").attr("data-patientName"), field:"name", sortable:true, index:2, editor:Slick.Editors.Text, minWidth:140},
            {id:"sectionNames", name:jQuery("#translatedColumnNames").attr("data-sectionNames"), field:"sectionNames", searchable:true, sortable:true, index:3, editor:Slick.Editors.Text, minWidth:160},
            {id:"pendingTestCount", name:jQuery("#translatedColumnNames").attr("data-pendingTests"), field:"pendingTestCount", sortable:true, editor:Slick.Editors.Text, index:4, searchable:false, minWidth:100},
            {id:"pendingValidationCount", name:jQuery("#translatedColumnNames").attr("data-pendingValidation"), field:"pendingValidationCount", sortable:true, editor:Slick.Editors.Text, index:5, searchable:false, minWidth:140},
            {id:"totalTestCount", name:jQuery("#translatedColumnNames").attr("data-total"), field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:6, searchable:false, minWidth:50},
            {id:"notes", name:jQuery("#translatedColumnNames").attr("data-notes"), field:"comments", sortable:true, index:7, searchable:false, editor:Slick.Editors.Text, minWidth:290},
            {id:"link", name:jQuery("#translatedColumnNames").attr("data-action"), field:"link", cssClass:"cell-title", formatter:formatter, index:8, editor:Slick.Editors.Text, searchable:false, minWidth:180},
            {id:"isPrinted", name:jQuery("#translatedColumnNames").attr("data-printed"), field:"isPrinted", sortable:true, cssClass:"cell-title", index:9, formatter:Slick.Formatters.Checkmark, searchable:false, minWidth:80},
        ];
    }
    else {
        return [
            {id:"accessionNumber", name:jQuery("#translatedColumnNames").attr("data-accessionNumber"), field:"accessionNumber", sortable:true, index:0, editor:Slick.Editors.Text, minWidth:160},
            {id:"stNumber", name:jQuery("#translatedColumnNames").attr("data-patientID"), field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:1, minWidth:160},
            {id:"name", name:jQuery("#translatedColumnNames").attr("data-patientName"), field:"name", sortable:true, index:2, editor:Slick.Editors.Text, minWidth:160},
            {id:"sectionNames", name:jQuery("#translatedColumnNames").attr("data-sectionNames"), field:"sectionNames", searchable:true, sortable:true, index:3, editor:Slick.Editors.Text, minWidth:160},
            {id:"pendingTestCount", name:jQuery("#translatedColumnNames").attr("data-pendingTests"), field:"pendingTestCount", sortable:true, editor:Slick.Editors.Text, index:4, searchable:false, minWidth:100},
            {id:"totalTestCount", name:jQuery("#translatedColumnNames").attr("data-total"), field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:6, searchable:false, minWidth:70},
            {id:"notes", name:jQuery("#translatedColumnNames").attr("data-notes"), field:"comments", sortable:true, index:7, searchable:false, editor:Slick.Editors.Text, minWidth:290},
            {id:"link", name:jQuery("#translatedColumnNames").attr("data-action"), field:"link", cssClass:"cell-title", formatter:formatter, index:8, editor:Slick.Editors.Text, searchable:false, minWidth:180},
            {id:"isPrinted", name:jQuery("#translatedColumnNames").attr("data-printed"), field:"isPrinted", sortable:true, cssClass:"cell-title", index:9, formatter:Slick.Formatters.Checkmark, searchable:false, minWidth:80},
        ];
    }
}
function getColumnsForSampleNotCollected() {
        return [
            {id:"stNumber", name:jQuery("#translatedColumnNames").attr("data-patientID"), field:"stNumber", sortable:true, editor:Slick.Editors.Text, index:0, minWidth:160},
            {id:"name", name:jQuery("#translatedColumnNames").attr("data-patientName"), field:"name", sortable:true, index:1, editor:Slick.Editors.Text, minWidth:160},
            {id:"source", name:jQuery("#translatedColumnNames").attr("data-source"), field:"source", sortable:false, index:2, editor:Slick.Editors.Text, minWidth:160},
            {id:"sectionNames", name:jQuery("#translatedColumnNames").attr("data-sectionNames"), field:"sectionNames", searchable:true, sortable:true, index:3, editor:Slick.Editors.Text, minWidth:160},
            {id:"totalTestCount", name:jQuery("#translatedColumnNames").attr("data-total"), field:"totalTestCount", sortable:true, editor:Slick.Editors.Text, index:4, searchable:false, minWidth:70},
            {id:"notes", name:jQuery("#translatedColumnNames").attr("data-notes"), field:"comments", sortable:true, index:5, editor:Slick.Editors.Text, minWidth:465},
            {id:"link", name:jQuery("#translatedColumnNames").attr("data-action"), field:"link", cssClass:"cell-title", formatter:formatter, index:6, editor:Slick.Editors.Text, searchable:false, minWidth:180}
        ];
}


function formatter(row, cell, value, columnDef, dataContext) {
    return value;
}

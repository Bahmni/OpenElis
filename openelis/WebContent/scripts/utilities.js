/*
 * Set the valid/invalid indicator on a field.  The field should have the form of below, the linkage being foo and fooMessage
 *
 * <app:text name="<%=formName%>"  property="..."  styleClass="text"  ....
             styleId="foo" />
   <div id="fooMessage" class="blank" />
 * Then it should be called with
 *
 *     setValidIndicatorOnField(true, field.id);
 *
 * Alternatively:
 * <app:text name="<%=formName%>"  property="foo"  styleClass="text"  ....
 *           styleId="..." />
 * <div id="fooMessage" class="blank" />
 * Should be called with.
 *     setValidIndicatorOnField(true, field.id);
 */

var datePattern = "DD/MM/YYYY";
var timeToWeeks = 1000 * 60 * 60 * 24 * 7;
var timeToYears = 1000 * 60 * 60 * 24 * 365;
var timeToMonths = timeToYears / 12;
var dayIndex = 0;
var monthIndex = 1;
var yearIndex = 2;

var setUpDateIndecies = function() {
        var splitPattern = datePattern.split("/");
        for (var i = 0; i < 3; i++) {
            if (splitPattern[i] == "DD") {
                dayIndex = i;
            } else if (splitPattern[i] == "MM") {
                monthIndex = i;
            } else if (splitPattern[i] == "YYYY") {
                yearIndex = i;
            }
        }
    };

setUpDateIndecies();

function fieldIsEmptyById(id) {
    var field = $(id);
    return (!field || field.value.blank());
}


function /*void*/
setDatePattern(pattern) {
    datePattern = pattern;
}

function /*void*/
setValidIndicaterOnField(valid, messageFieldIdPrefix) {
    var fieldMessage = messageFieldIdPrefix + "Message";

    var mdiv = $(fieldMessage);
    if (mdiv) {
        if (valid) {
            mdiv.className = "blank";
        } else {
            mdiv.className = "badmessage";
        }
    } else {
        selectFieldErrorDisplay(valid, $(messageFieldIdPrefix));
    }
}

function /*void*/
clearField(fieldId) {
    var element = $(fieldId);
    if (element) {
        element.value = "";
        updateFieldValidity(true, element.id);
    }
}

function selectFieldErrorDisplay(ok, field) {
    if (ok) {
        clearFieldErrorDisplay(field);
    } else {
        setFieldErrorDisplay(field);
    }
}

function setFieldErrorDisplay(field) {
    if (field.className.search("error") == -1) {
        field.className += " error";
    }
}

function clearFieldErrorDisplay(field) {
    field.className = field.className.replace(/(?:^|\s)error(?!\S)/, '');
}

function /*boolean*/
errorsOnForm() {
    return $$(".error").length > 0;
}

/*
 * Given the id of a selection element and the optionValue the index will be returned.  If option is not found
 * then 0 will be returned.  Note that there is no way to differentiate between the value not being found and
 * the value found being 0.
 */

function /*int*/
getSelectIndexFor(selectID, optionValue) {

    var select = $(selectID);

    if (optionValue && select) {
        for (var i = 0; i < select.length; i++) {
            if (select.options[i].value == optionValue) {
                return i;
            }
        }
    }

    return 0;
}

/*
 * Given the id of a selection element and the optionValue the index will be returned.  If option is not found
 * then 0 will be returned.  Note that there is no way to differentiate between the value not being found and
 * the value found being 0.
 */

function /*int*/
getSelectIndexByTextFor(selectID, optionValue, trace) {

    var select = $(selectID);

    if (optionValue && select) {
        for (var i = 0; i < select.length; i++) {
            if (trace) {
                alert(select.options[i].text + ":" + optionValue);
            }
            if (select.options[i].text == optionValue) {
                return i;
            }
        }
    }

    return 0;
}

/**
 * Callback function for checking a date via an AJAX call to server.
 * @param xhr the XML document with formField containing the field.id.
 * @return
 */

function /*void*/
processDateSuccessById(xhr) {

    //alert(xhr.responseText);
    var message = xhr.responseXML.getElementsByTagName("message").item(0).firstChild.nodeValue;
    var formFieldId = xhr.responseXML.getElementsByTagName("formfield").item(0).firstChild.nodeValue;

    var isValid = message == "valid";

    updateFieldValidity(isValid, formFieldId);
}

/*
 * evaluates if the given date is valid.  Delegates to us.mn.state.health.lims.common.util.validator.DateValidater
 *
 * date -- the date to be evaluated -- required
 * successCallback -- function to be called if call is successful -- required
 * field -- the field name or id being evaluated, it will be passed back unchanged in
 *          response so should be whatever is needed to process success --  required
 * relativeToNow -- flag for date range of date.  Must be one of 'past', 'future', 'DNA' (does not apply)-- default is 'DNA'
 * failCallback -- function to be called if call is not successful -- default is to do nothing
 */

function /*void*/
isValidDate(date, successCallback, field, relativeToNow, failCallback) {

    if (date && successCallback && field) {

        if (!failCallback) {
            failCallback = function() { /*no-op*/
            };
        }

        if (!relativeToNow) {
            relativeToNow = "DNA";
        }

        new Ajax.Request('ajaxXML', //url
        { //options
            method: 'get',
            //http method
            parameters: 'provider=DateValidationProvider&date=' + date + '&relativeToNow=' + relativeToNow + '&field=' + field,
            onSuccess: successCallback,
            onFailure: failCallback
        });

    } else if (failCallback) {
        failCallback(null);
    }
}

/**
 * Checks if entry is a valid time 
 */

function checkValidTimeEntry(time, blanksAllowed) {
    var lowRangeRegEx = new RegExp("^[0-1]{0,1}\\d:[0-5]\\d$");
    var highRangeRegEx = new RegExp("^2[0-3]:[0-5]\\d$");

    if (time.value.blank() && blanksAllowed == true) {
        return true;
        //updateFieldValidity(true, time.id );
    } else if (lowRangeRegEx.test(time.value) || highRangeRegEx.test(time.value)) {
        if (time.value.length == 4) {
            time.value = "0" + time.value;
        }
        return true;
    } else {
        return false;
    }

}


function checkTime(value) {
    var regEx = new RegExp("^(20|21|22|23|[01]\\d|\\d)(([:.][0-5]\\d){1,2})$");
    return regEx.test(value);
}

/**
 * call for checking a date then updating a field message based on the _ID_ Ajaxed over and back.
 * @param dateElement
 * @return
 */

function /*void*/
checkValidDate(dateElement) {
    if (dateElement) {
        if (dateElement.value.blank()) {
            updateFieldValidity(true, dateElement.id);
            return true;
        } else {
            isValidDate(dateElement.value, processDateSuccessById, dateElement.id, "past");
            return false;
        }
    }
}

//--------------- age change
/*
 * AgeType should be one of "age", "month" or "week"
 */

function /*void*/
handlePatientAgeChange(ageField, dateOfBirthID, endDateID, ageType) {
    var validAge = checkValidAge(ageField);
    if (validAge) {
        updateDOB(ageField.value, dateOfBirthID, endDateID, ageType);
    }
    updateFieldValidity(validAge, dateOfBirthID);
}


function /*bool*/
checkValidAge(age) {
    var regEx = new RegExp("^\\s*\\d{1,2}\\s*$");
    var valid = regEx.test(age.value) || age.value == "";

    updateFieldValidity(valid, age.id);

    return valid;
}

function /*void*/
updateDOB(age, dateOfBirthID, endDateFieldID, ageType) {
    var useMonth = ageType == 'month' || ageType == 'week';
    var useDays = ageType == 'week';

    var endDateStr = (endDateFieldID != null) ? $(endDateFieldID).value : "";
    var eDate = endDateStr.split("/");
    var endDate = (endDateStr == null) ? new Date() : new Date(eDate[yearIndex], eDate[monthIndex] - 1, eDate[dayIndex]);

    var dob;
    if (ageType == 'week') {
        dob = new Date(endDate.getTime() - age * timeToWeeks);
    } else if (ageType == 'month') {
        dob = new Date(endDate.getFullYear(), endDate.getMonth() - age);
    } else {
        dob = new Date(endDate.getFullYear() - age, 0);
    }



    var day = useDays ? dob.getDate() : "xx";
    var month = useMonth ? dob.getMonth() + 1 : "xx";
    var year = dob.getFullYear();

    if (day < 10) {
        day = "0" + day.toString();
    }
    if (month < 10) {
        month = "0" + month.toString();
    }

    var splitPattern = datePattern.split("/");

    var DOB = "";

    for (var i = 0; i < 3; i++) {
        if (splitPattern[i] == "DD") {
            DOB = DOB + day + "/";
        } else if (splitPattern[i] == "MM") {
            DOB = DOB + month + "/";
        } else if (splitPattern[i] == "YYYY") {
            DOB = DOB + year + "/";
        }
    }

    $(dateOfBirthID).value = DOB.substring(0, DOB.length - 1);
}

//--------- dob change
//note: currently checkForValidDate == true is not supported

function /*void*/
handlePatientBirthDateChange(dobField, endDateField, checkForValidDate, ageId, monthId, weekId, errorMsg) {
    if (!checkForValidDate) {
        var success = updateAge(dobField, endDateField, ageId, monthId, weekId, errorMsg);
        updateFieldValidity(success, dobField.id);
    } else {
        // checkValidDate( dobField );
    }
}

//note this has to be corrected to handle the callback, needs reference age and removal of jsp elements
//function  /*void*/ processValidateDateSuccess(xhr){
//
//    //alert(xhr.responseText);
//  var message = xhr.responseXML.getElementsByTagName("message").item(0).firstChild.nodeValue;
//  var formField = xhr.responseXML.getElementsByTagName("formfield").item(0).firstChild.nodeValue;
//
//  var isValid = message == "<%=IActionConstants.VALID%>";
//
//  setValidIndicaterOnField(isValid, formField);
//  pt_setFieldValidity( isValid, formField );
//  setSave();
//
//  if( isValid ){
//      updateAge( $("dateOfBirthID") );
//  }else if( message == "<%=IActionConstants.INVALID_TO_LARGE%>" ){
//      alert( '<bean:message key="error.date.birthInPast" />' );
//  }
//}
//
//function  /*void*/ checkValidDate(dateElement)
//{
//  if( dateElement && !dateElement.value.blank() ){
//      isValidDate( dateElement.value, processValidateDateSuccess, dateElement.id, "past" );
//  }else{
//      setValidIndicaterOnField(false, dateElement.id);
//      pt_setFieldInvalid( dateElement.id );
//      setSave();
//  }
//}

function /*boolean*/
updateAge(DOB, endDateField, yearId, monthId, weekId, errorMsg) {
    if (DOB.value.blank() || endDateField.value.blank()) {
        clearField(weekId);
        clearField(yearId);
        clearField(monthId);
        return true;
    }

    var regEx = new RegExp("^(\\d{1,2}|xx|XX|xX|Xx)/(\\d{1,2}|xx|XX|Xx|xX)/\\d{4}\\s*$");

    if (!regEx.test(DOB.value)) {
        clearField(weekId);
        clearField(yearId);
        clearField(monthId);
        return false;
    }

    var date = new String(DOB.value);

    var monthSpecified = true;
    var daySpecified = true;

    var splitDOB = date.split("/");
    var monthDOB = splitDOB[monthIndex];
    var dayDOB = splitDOB[dayIndex];
    var yearDOB = splitDOB[yearIndex];

    var endDateStr = (endDateField != null) ? endDateField.value : "";
    var eDate = endDateStr.split("/");
    var endDate = (endDateStr == null) ? new Date() : new Date(eDate[yearIndex], eDate[monthIndex] - 1, eDate[dayIndex]);

    if (!monthDOB.match(/^\d+$/)) {
        monthDOB = "01";
        monthSpecified = false;
    }

    if (!dayDOB.match(/^\d+$/)) {
        dayDOB = "01";
        daySpecified = false;
    }

    var dob = new Date(yearDOB, monthDOB - 1, dayDOB);
    var age = new Date(endDate.getTime() - dob.getTime());

    if (weekId) {
        var years = age.getTime() / timeToYears;

        if (years < 2) {
            if (monthSpecified && daySpecified) {
                $(weekId).value = Math.floor(age.getTime() / timeToWeeks);
                clearField(yearId);
                clearField(monthId);
            } else {
                if (errorMsg) {
                    alert(errorMsg);
                }
                clearField(weekId);
                clearField(yearId);
                clearField(monthId);
                return false;
            }
        } else {
            if (yearId) {
                $(yearId).value = Math.floor(years);
                clearField(monthId);
            }
            if (monthId) {
                $(monthId).value = Math.floor(age.getTime() / timeToMonths);
                clearField(yearId);
            }
            clearField(weekId);

        }
    } else if (monthId) {
        var years = age.getTime() / timeToYears;

        if (years < 2 || !yearId) {
            if (monthSpecified) {
                $(monthId).value = Math.floor(age.getTime() / timeToMonths);
                clearField(yearId);
                clearField(weekId);
            } else {
                if (errorMsg) {
                    alert(errorMsg);
                }
                clearField(monthId);
                clearField(yearId);
                clearField(weekId);
                return false;
            }
        } else {
            $(yearId).value = Math.floor(years);
            clearField(monthId);
            clearField(weekId);
        }
    } else if (yearId) {
        $(yearId).value = Math.floor(age.getTime() / timeToYears);
        clearField(weekId);
        clearField(monthId);
    }

    return true;
}

//----------------------------------------------------------
/**
 * Class for required and invalid field management.  No One-Of a set of fields yet... see patientManagement.jsp for that functionality.
 * I don't think this works perfectly if you switch required fields half way through validating a form, so until then be careful
 * and keep separate objects, one for each set of fields.
 * @author pahill
 * @since 2010-04-02
 */
// Class

function FieldValidator() {

    this.invalidFields = new Array();
    this.requiredFields = new Array();
    this.conflictedFields = new Array();

    /**
     * Call this to set a new set of required field Ids
     **/
    this.setRequiredFields = function /*void*/
    ( /*Array<String>[]*/ newRequiredFields) {
        this.requiredFields = newRequiredFields
    }

    this.addRequiredField = function /*void*/
    ( /*String*/ fieldId) {
        if (this.requiredFields.indexOf(fieldId) == -1) {
            this.requiredFields.push(fieldId);
        }
    }

    /**
     * drop a field from the list of required
     * @param fieldId
     * @return
     */
    this.removeRequiredField = function /*void*/
    ( /*String*/ fieldId) {
        var i = this.requiredFields.indexOf(fieldId)
        if (i != -1) {
            this.requiredFields.splice(i, 1);
        }
    }

    /**
     * @Returns true if the particular field is known to be filled in already.
     **/
    this.isFieldValid = function /*boolean*/
    ( /*String*/ fieldId) {
        return this.invalidFields.indexOf(fieldId) == -1;
    }

    /**
     * sets a particular field into a
     */
    this.addField = function /*void*/
    (array, /*String*/ fieldId) {
        if (array.indexOf(fieldId) == -1) {
            array.push(fieldId);
        }
    }

    /**
     * sets a particular field to VALID
     **/
    this.removeField = function /*void*/
    (array, fieldId) {
        var removeIndex = array.indexOf(fieldId);
        if (removeIndex != -1) {
            for (var i = removeIndex + 1; i < array.length; i++) {
                array[i - 1] = array[i];
            }
            array.length--;
        }
    }

    /**
     * set to valid or invalid depending on the 1st argument
     * @arg valid - TRUE or FALSE
     * @arg field - change validity of this field.
     */
    this.setFieldValidity = function /*void*/
    (valid, fieldId) {
        if (valid) {
            this.removeField(this.invalidFields, fieldId);
        } else {
            this.addField(this.invalidFields, fieldId);
        }
    }

    this.setFieldConflict = function(noConflict, fieldId) {
        if (noConflict) {
            this.removeField(this.conflictedFields, fieldId);
        } else {
            this.addField(this.conflictedFields, fieldId);
        }
    }

    /**
     * checks that there are no invalid fields and that all fields have been filled has been.
     */
    this.isAllValid = function /*boolean*/
    () {
        return this.invalidFields.length == 0 && this.isEachRequiredFieldEntered();
    }

    this.isAnyConflicted = function /*boolean*/
    () {
        return this.conflictedFields.length != 0;
    }

    /**
     * checks there are no blank of those fields listed by ID in the required fields.
     */
    this.isEachRequiredFieldEntered = function /*boolean*/
    () {
        for (var i = 0; i < this.requiredFields.length; ++i) {
            var field = document.getElementById(this.requiredFields[i]);
            if (field == null) {
                // console.info(this.requiredFields[i] + " is not found.");
            } else {
                if (field.value.blank() || (field.tagName == 'SELECT' && field.value == 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * If you reload a form with new value call this to set everything back to good.
     * @return
     */
    this.setAllFieldsValid = function() {
        for (var i = 0; i < this.invalidFields.length; i++) {
            updateFieldValidity(true, this.invalidFields[i]);
        }
    }
}

/**
 * The following page convenience methods use this instance
 */
fieldValidator = new FieldValidator();

/**
 * method for just checking that a field is filled in at all and updating it indicator and the values in the page fieldValidator
 * Call this right from a fields onChange="checkRequiredField(this)" for a simple required field.
 * @author pahill
 */

function checkRequiredField(field, blanksAllowed) {
    updateFieldValidity(!$(field.value.blank()) || (field.value.blank() && blanksAllowed == true), field.id)
}

/**
 * Updates (1) display of valid/invalid on the page, (2) the 1 object for tracking field validity, and (3) the active/inactivit state of the save button.
 * Calls two methods assumed to be in page scope.  setValidIndicatorOnField (see above) and setSaveButton( see below or override in page)
 *
 * Use this method in a customer field validator ... onChange="checkMyField(this)"
 * @author pahill
 */

function /*void*/
updateFieldValidity(isValid, fieldId) {
    setValidIndicaterOnField(isValid, fieldId);
    fieldValidator.setFieldValidity(isValid, fieldId);
    setSaveButton();
    return isValid;
}

function enableFields(enable, fieldsStr) {
    var fields = fieldsStr.split(',');
    for (var i = 0; i < fields.length; i++) {
        var fieldName = fields[i];
        if ($(fieldName) == null) {
            // alert( "Programmer Error: in enableFields(): field \"" + fieldName + "\" not found.");
        }
        f.disabled = !enable;
    }
}

/**
 * Page method which checks all fields are valid, then enables/disables "saveButtonId" appropriately.
 * Called by
 * Override this method on your page, if there is more to do.
 * @author pahill
 */

function /*void*/
setSaveButton() {
    var validToSave = fieldValidator.isAllValid();
    $("saveButtonId").disabled = !validToSave;
}


/*
 * credit to http://www.mojavelinux.com/articles/javascript_hashes.html
 *
 * added functionality CIRG University Of Washington
 */

function HashObj() {
    this.length = 0;
    this.items = new Array();
    for (var i = 0; i < arguments.length; i += 2) {
        if (typeof(arguments[i + 1]) != 'undefined') {
            this.items[arguments[i]] = arguments[i + 1];
            this.length++;
        }
    }

    this.removeItem = function(in_key) {
        var tmp_previous;
        if (typeof(this.items[in_key]) != 'undefined') {
            this.length--;
            tmp_previous = this.items[in_key];
            delete this.items[in_key];
        }

        return tmp_previous;
    }

    this.getItem = function(in_key) {
        return this.items[in_key];
    }

    this.setItem = function(in_key, in_value) {
        var tmp_previous;
        if (typeof(in_value) != 'undefined') {
            if (typeof(this.items[in_key]) == 'undefined') {
                this.length++;
            } else {
                tmp_previous = this.items[in_key];
            }

            this.items[in_key] = in_value;
        }

        return tmp_previous;
    }

    this.hasItem = function(in_key) {
        return typeof(this.items[in_key]) != 'undefined';
    }

    this.clear = function() {
        for (var i in this.items) {
            delete this.items[i];
        }

        this.length = 0;
    }

    this.isEmpty = function() {
        return this.length == 0;
    }
}

/**
 * Add slashes to the end of a date field as characters are typed in the 3rd and 6th position, but only when the fields are each 2 chars.
 * If the user types a delete key, do nothing.  This means that if they delete the slash they need to put it back themselves.
 * use: onkeyup="addDateSlashes(this, event);"
 * @param field - the date form field
 * @param event the onkeyup event containing the typed key (which diffs on IE and Mozilla).
 **/

function addDateSlashes(field, event) {
    var key = event.which ? event.which : event.keyCode; // browser difference
    if (key == 8) { // delete key? do nothing
        return;
    }
    var v = field.value;
    var parts = v.split("/");
    var last2 = v.substring(v.length - 2);
    if (last2 == "//") {
        v = v.substring(0, v.length - 1);
        field.value = v;
        return;
    }
    if (v.length == 2 && parts[0].length == 2) {
        field.value += "/";
    } else if (v.length == 5 && parts[1].length == 2 && parts[0].length == 2) {
        field.value += "/";
    }
}

/**
 * shows hides note field.  Naming convention needs to be followed
 * showHideButton
 **/

function /*void*/
showHideNotes(element, index) {
    var current = $("hideShow_" + index).value;

    if (current == "hidden") {
        $("showHideButton_" + index).src = "./images/note-close.gif";
        $("hideShow_" + index).value = "showing";
        $("noteRow_" + index).show();
    } else {
        $("showHideButton_" + index).src = $("note_" + index).value.blank() ? "./images/note-add.gif" : "./images/note-edit.gif";
        $("hideShow_" + index).value = "hidden";
        $("noteRow_" + index).hide();
    }
}

function /*void*/
showNote(index) {
    var noteElement = $("showHideButton_" + index);

    $("showHideButton_" + index).src = "./images/note-close.gif";
    $("hideShow_" + index).value = "showing";
    $("noteRow_" + index).show();
}

function /*void*/
hideNote(index) {
    $("showHideButton_" + index).src = $("note_" + index).value.blank() ? "./images/note-add.gif" : "./images/note-edit.gif";
    $("hideShow_" + index).value = "hidden";
    $("noteRow_" + index).hide();
}

function /*void*/
showStatusNote(index) {
    var originalTOTS = document.getElementById("tots_" + index).value
    var totsSelected = document.getElementById("testStatusId_" + index).value;
    if(!((originalTOTS == null || originalTOTS =="") && totsSelected == 0) && originalTOTS != totsSelected) {
        return showNote(index);
    } else {
        return hideNote(index);
    }
}

function showQuanitiy(selector, index, dictionaryIds) {

    if (dictionaryIds.indexOf($jq("#resultId_" + index + " option:selected").val()) != -1) {
        $jq("#qualifiedDict_" + index).show();
        $jq("#resultType_" + index).val("Q");
    } else {
        $jq("#qualifiedDict_" + index).hide();
        $jq("#qualifiedDict_" + index).val("");
        $jq("#resultType_" + index).val("D");
    }
}
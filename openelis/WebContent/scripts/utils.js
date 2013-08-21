var OpenElis = OpenElis || {}
OpenElis.Utils = {
    calculateAge: function(DOB, datePattern) {
        var date = new String(DOB);
        var splitPattern = datePattern.split("/");
        var dayIndex = 0;
        var monthIndex = 1;
        var yearIndex = 2;

        for (var i = 0; i < 3; i++) {
            if (splitPattern[i] == "DD") {
                dayIndex = i;
            } else if (splitPattern[i] == "MM") {
                monthIndex = i;
            } else if (splitPattern[i] == "YYYY") {
                yearIndex = i;
            }
        }

        var splitDOB = date.split("/");
        var monthDOB = splitDOB[monthIndex];
        var dayDOB = splitDOB[dayIndex];
        var yearDOB = splitDOB[yearIndex];

        var today = new Date();

        var adjustment = 0;

        if (!monthDOB.match(/^\d+$/)) {
            monthDOB = "01";
        }

        if (!dayDOB.match(/^\d+$/)) {
            dayDOB = "01";
        }

        //months start at 0, January is month 0
        var monthToday = today.getMonth() + 1;

        if (monthToday < monthDOB ||
            (monthToday == monthDOB && today.getDate() < dayDOB  )) {
            adjustment = -1;
        }

        return today.getFullYear() - yearDOB + adjustment;
    },

    getXMLValue: function(response, key){
        var field = response.getElementsByTagName(key).item(0);
        return field != null ? field.firstChild.nodeValue : "";
    }

}

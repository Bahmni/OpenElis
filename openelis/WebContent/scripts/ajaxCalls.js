function getDistrictsForRegion( regionId, selectedValue, success, failure){
	if( !failure ){	failure = defaultFailure;}
	
	new Ajax.Request('ajaxQueryXML',
			{
				method : 'get', 
				parameters : "provider=HealthDistrictForRegionProvider&regionId=" + regionId +"&selectedValue=" + selectedValue,
			    //indicator: 'throbbing',
				onSuccess : success,
				onFailure : failure
			});

}

function getTestsForSampleType(sampleTypeId, labOrderTypeId, success, failure) {
	var request = "&sampleType=" + sampleTypeId + "&labOrderType=" + labOrderTypeId;
	if( !failure ){	failure = defaultFailure;}
	
	new Ajax.Request('ajaxQueryXML', // url
	{// options
		method : 'get', // http method
		parameters : "provider=SampleEntryTestsForTypeProvider" + request,
		// indicator: 'throbbing'
		asynchronous: false,
		onSuccess : success,
		onFailure : failure
	});
}

function getSampleOrderDetailsFromSampleId(sampleId, success, failure) {
	var request = "&sampleId=" + sampleId;
	if( !failure ){	failure = defaultFailure;}

	new Ajax.Request('ajaxQueryXML', // url
		{// options
			method : 'get', // http method
			parameters : "provider=SampleOrderDetailsFromSampleProvider" + request,
			// indicator: 'throbbing'
			asynchronous: false,
			onSuccess : success,
			onFailure : failure
		});
}

function getDefaultSampleSource(success) {
	var failure = defaultFailure;

	new Ajax.Request('ajaxQueryXML', // url
		{// options
			method : 'get', // http method
			parameters : "provider=SiteInformationProvider",
			// indicator: 'throbbing'
			asynchronous: false,
			onSuccess : success,
			onFailure : failure
		});
}

function getSampleTypesAndTestsForSample(sampleId, success, failure) {
	var request = "&sampleId=" + sampleId;
	if( !failure ){	failure = defaultFailure;}

	new Ajax.Request('ajaxQueryXML', // url
		{// options
			method : 'get', // http method
			parameters : "provider=SampleTypeTestsForSampleProvider" + request,
			// indicator: 'throbbing'
			asynchronous: false,
			onSuccess : success,
			onFailure : failure
		});
}

function testConnectionOnServer(connectionId, url, success, failure) {
	var request = "&connectionId=" + connectionId + "&url=" + url;
	
	if( !failure ){	failure = defaultFailure;}
	
	new Ajax.Request('ajaxQueryXML', // url
	{// options
		method : 'get', // http method
		parameters : "provider=ConnectionTestProvider" + request,
		// indicator: 'throbbing'
		onSuccess : success,
		onFailure : failure
	});
}

function validateAccessionNumberOnServer(checkformatAndUsed, fieldId, accessionNumber, success, failure) {
	if( !failure ){	failure = defaultFailure;}
	new Ajax.Request(
			'ajaxXML', // url
			{// options
				method : 'get', // http method
				parameters : 'provider=SampleEntryAccessionNumberValidationProvider&checkFormatAndUsed=' + checkformatAndUsed + '&field='	+ fieldId + '&accessionNumber=' + accessionNumber,
				indicator : 'throbbing',
				onSuccess : success,
				onFailure : failure
			});
}

function validateNonConformityRecordNumberOnServer( field, success, failure){
	if( !failure){failure = defaultFailure;	}

	new Ajax.Request('ajaxXML',
			{
				method : 'get', 
				parameters : "provider=NonConformityRecordNumberValidationProvider&fieldId=" + field.id +"&value=" + field.value,
			    //indicator: 'throbbing',
				onSuccess : success,
				onFailure : failure
			});

}

function patientSearch(lastName, firstName, middleName, STNumber, subjectNumber, nationalId, labNumber, success, failure){
	if( !failure){failure = defaultFailure;	}
	
	new Ajax.Request (
            'ajaxQueryXML',  //url
             {//options
               method: 'get', //http method
               parameters: "provider=PatientSearchProvider&lastName=" + lastName +
               			  "&firstName=" + firstName +
               			  "&middleName=" + middleName +
               			  "&STNumber=" + STNumber +
               			  "&subjectNumber=" + subjectNumber +
               			  "&nationalID=" + nationalId +
               			  "&labNumber=" + labNumber,
               onSuccess:  success,
               onFailure:  failure
              }
           );	
}

function updateTestsWithAccessionNumber(accessionNumber, sampleId, typesAndTests, success, failure) {
	if( !failure){failure = defaultFailure;	}
	new Ajax.Request (
		'ajaxQueryXML',  //url
		{//options
			method: 'get', //http method
			dataType:'json',
			parameters: "provider=TestUpdateWithAccessionNumberProvider&accessionNumber=" + accessionNumber + "&typeAndTestIds="+ typesAndTests + "&sampleId="+ sampleId,
			asynchronous: false,
			onSuccess:  success,
			onFailure:  failure
		}
	);
}

function getReflexUserChoice( resultId, analysisId, testId, accessionNumber, index, success, failure){
	if( !failure){failure = defaultFailure;	}
	
	new Ajax.Request (
            'ajaxQueryXML',  //url
            {//options
            method: 'get', //http method
            parameters: 'provider=TestReflexUserChoiceProvider&resultIds=' + resultId + 
            			'&analysisIds=' +  analysisId + 
            			'&testIds=' + testId + 
            			'&rowIndex=' + index +
            			'&accessionNumber=' + accessionNumber,
            indicator: 'throbbing',
            onSuccess:  success,
            onFailure:  failure
                 }
                );
	
}

function defaultFailure(xhr){
	//no-op
}

/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
 *
 * Contributor(s): CIRG, University of Washington, Seattle WA.
 */
package us.mn.state.health.lims.common.externalLinks;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.validator.GenericValidator;
import org.dom4j.DocumentException;

import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.provider.query.PatientDemographicsSearchResults;

public class ExternalPatientSearch implements Runnable {

	private static final String GET_PARAM_PWD = "pwd";
	private static final String GET_PARAM_NAME = "name";
	private static final String GET_PARAM_NATIONAL_ID = "nationalId";
	private static final String GET_PARAM_ST = "ST";
	private static final String GET_PARAM_SUBJECT = "subjectNumber";
	private static final String GET_PARAM_LAST = "last";
	private static final String GET_PARAM_FIRST = "first";


	public static final String MALFORMED_REPLY = "Malformed reply";

	private boolean finished = false;

	private String firstName;
	private String lastName;
	private String STNumber;
	private String subjectNumber;
	private String nationalId;
	private String connectionString;
	private String connectionName;
	private String connectionPassword;
	private int timeout = 0;

	protected String resultXML;
	protected List<PatientDemographicsSearchResults> searchResults;
	protected List<String> errors;
	protected int returnStatus = HttpStatus.SC_CREATED;

	synchronized public void setConnectionCredentials(String connectionString, String name, String password,
			int timeout_Mil) {
		if (finished) {
			throw new IllegalStateException("ServiceCredentials set after ExternalPatientSearch thread was started");
		}

		this.connectionString = connectionString;
		connectionName = name;
		connectionPassword = password;
		timeout = timeout_Mil;
	}

	synchronized public void setSearchCriteria(String lastName, String firstName, String STNumber, String subjectNumber, String nationalID)
			throws IllegalStateException {

		if (finished) {
			throw new IllegalStateException("Search criteria set after ExternalPatientSearch thread was started");
		}

		this.lastName = lastName;
		this.firstName = firstName;
		this.STNumber = STNumber;
		this.subjectNumber = subjectNumber;
		this.nationalId = nationalID;
	}

	synchronized public List<PatientDemographicsSearchResults> getSearchResults() {

		if (!finished) {
			throw new IllegalStateException("Results requested before ExternalPatientSearch thread was finished");
		}
		
		if (searchResults == null) {
			searchResults = new ArrayList<PatientDemographicsSearchResults>();

			convertXMLToResults();
		}

		return searchResults;
	}

	public int getSearchResultStatus() {
		if (!finished) {
			throw new IllegalStateException("Result status requested ExternalPatientSearch before search was finished");
		}

		return returnStatus;
	}

	public void run() {
		try {
			synchronized (this) {
				if (noSearchTerms()) {
					return;
				}

				if (connectionCredentialsIncomplete()) {
					throw new IllegalStateException("Search requested before connection credentials set.");
				}
				errors = new ArrayList<String>();

				doSearch();
			}
		} finally {
			finished = true;
		}
	}

	private boolean connectionCredentialsIncomplete() {
		return GenericValidator.isBlankOrNull(connectionString) || GenericValidator.isBlankOrNull(connectionName)
				|| GenericValidator.isBlankOrNull(connectionPassword);
	}

	private boolean noSearchTerms() {
		return GenericValidator.isBlankOrNull(firstName) && GenericValidator.isBlankOrNull(lastName)
				&& GenericValidator.isBlankOrNull(nationalId) && GenericValidator.isBlankOrNull(STNumber);
	}

	// protected for unit testing called from synchronized block
	protected void doSearch() {

		HttpClient httpclient = new HttpClient();
		setTimeout(httpclient);

		String getString = buildConnectionString();
		GetMethod httpget = new GetMethod(getString.replaceAll(" ", "%20"));

		try {
			try {
				httpclient.executeMethod(httpget);
				returnStatus = httpget.getStatusCode();
				setPossibleErrors();
				setResults(httpget.getResponseBodyAsString());
			} catch (SocketTimeoutException e) {
				errors.add("Response from patient information server took too long.");
				LogEvent.logError("ExternalPatientSearch", "doSearch()", e.toString());
				// System.out.println("Tinny time out" + e);
			} catch (HttpException e) {
				errors.add("Unknown HTTP error trying to obtain patient information form service.");
				LogEvent.logError("ExternalPatientSearch", "doSearch()", e.toString());
				// System.out.println("whoa " + e);
			} catch (ConnectException e) {
				errors.add("Unable to connect to patient information form service. Service may not be running");
				LogEvent.logError("ExternalPatientSearch", "doSearch()", e.toString());
				// System.out.println("you no talks? " + e);
			} catch (IOException e) {
				errors.add("Unknown IO error trying to obtain patient information form service.");
				LogEvent.logError("ExternalPatientSearch", "doSearch()", e.toString());
				// System.out.println("all else failed " + e);
			}
		} finally {
			httpget.releaseConnection();
		}
	}
	
	private void convertXMLToResults() {
		if (!GenericValidator.isBlankOrNull(resultXML)) {
			
			ExternalPatientSearchResultsXMLConverter converter = new ExternalPatientSearchResultsXMLConverter();
			
			try {
				searchResults = converter.convertXMLToSearchResults( resultXML );
			} catch (DocumentException e) {
				errors.add(MALFORMED_REPLY);
			}
		}
	}

	
	protected void setResults(String resultsAsXml) {
		resultXML = resultsAsXml;
	}

	private void setPossibleErrors() {
		switch (returnStatus) {
		case HttpStatus.SC_UNAUTHORIZED: {
			errors.add("Access denied to patient information service.");
			break;
		}
		case HttpStatus.SC_INTERNAL_SERVER_ERROR: {
			errors.add("Internal error on patient information service.");
			break;
		}
		case HttpStatus.SC_OK:{
			break; //NO-OP
		}
		default: {
			errors.add("Unknown error trying to connect to patient information service. Resturn status was "
					+ returnStatus);
		}
		}

	}

	private void setTimeout(HttpClient httpclient) {
		HttpConnectionManager connectionManager = httpclient.getHttpConnectionManager();
		connectionManager.getParams().setSoTimeout(timeout);
	}

	private String buildConnectionString() {
		boolean previousParam = false;

		StringBuilder builder = new StringBuilder(connectionString);

		previousParam = addPossibleParam(previousParam, builder, GET_PARAM_FIRST, firstName);
		previousParam = addPossibleParam(previousParam, builder, GET_PARAM_LAST, lastName);
		previousParam = addPossibleParam(previousParam, builder, GET_PARAM_ST, STNumber);
		previousParam = addPossibleParam(previousParam, builder, GET_PARAM_SUBJECT, subjectNumber);
		previousParam = addPossibleParam(previousParam, builder, GET_PARAM_NATIONAL_ID, nationalId);
		previousParam = addPossibleParam(previousParam, builder, GET_PARAM_NAME, connectionName);
		addPossibleParam(previousParam, builder, GET_PARAM_PWD, connectionPassword);

		return builder.toString();
	}

	private boolean addPossibleParam(boolean previousParam, StringBuilder builder, String paramName, String paramValue) {
		if (!GenericValidator.isBlankOrNull(paramValue)) {
			builder.append(previousParam ? "&" : "?");
			builder.append(paramName);
			builder.append("=");
			builder.append(paramValue);
			previousParam = true;
		}

		return previousParam;
	}
}

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
*/

package us.mn.state.health.lims.dataexchange.common;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;

import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.StringUtil;

abstract public class HttpSender implements IExternalSender {

	protected String message;
	protected String url;
	private static final int timeout = 10000;
	protected int returnStatus = HttpStatus.SC_CREATED;
	String serviceTargetName = "";
	List<String> errors;
	
	abstract public boolean sendMessage();

	@Override
	public void setTargetName(String name) {
		serviceTargetName = name != null ? name : "";
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void setURI(String url) {
		this.url = url;
	}

	@Override
	public List<String> getErrors() {
		return errors;
	}

	@Override
	public int getSendResponse() {
			return returnStatus;
	}

	protected void setTimeout(HttpClient httpclient) {
		HttpConnectionManager connectionManager = httpclient.getHttpConnectionManager();
		connectionManager.getParams().setConnectionTimeout(timeout);
	}
	
	protected void setPossibleErrors() {
		switch (returnStatus) {
		case HttpStatus.SC_UNAUTHORIZED: {
			errors.add( StringUtil.getMessageForKey("http.error.authorization") + url);
			break;
		}
		case HttpStatus.SC_INTERNAL_SERVER_ERROR: {
			errors.add(StringUtil.getMessageForKey("http.error.internal") + url);
			break;
		}
		case HttpStatus.SC_CONFLICT:
		case HttpStatus.SC_OK: {
			break; // NO-OP
		}
		default: {
			errors.add(StringUtil.getMessageForKey("http:error.unknown.status") + url );
		}
		}

	}
	
	protected void sendByHttp(HttpClient httpclient, HttpMethod httpPost) {
		try {
			try {
				httpclient.executeMethod(httpPost);
				returnStatus = httpPost.getStatusCode();
				setPossibleErrors();
			} catch (SocketTimeoutException e) {
				returnStatus = HttpServletResponse.SC_REQUEST_TIMEOUT;
				LogEvent.logError("HttpSender", "sendPutMessage()", e.toString());
			} catch (ConnectTimeoutException e) {
				returnStatus = HttpServletResponse.SC_REQUEST_TIMEOUT;
				errors.add(e.getMessage() + " " + url);
				LogEvent.logError("HttpSender", "sendPutMessage()", e.toString());
			} catch (HttpException e) {
				errors.add( StringUtil.getMessageForKey("http.error.unknown") + " " + url );
				LogEvent.logError("HttpSender", "sendPutMessage()", e.toString());
			} catch (ConnectException e) {
				returnStatus = HttpServletResponse.SC_BAD_REQUEST;
				errors.add(StringUtil.getMessageForKey("http.error.noconnection") + " " + url);
				LogEvent.logError("HttpSender", "sendPutMessage()", e.toString());
			}catch (UnknownHostException e) {
				returnStatus = HttpServletResponse.SC_NOT_FOUND;
				errors.add(StringUtil.getMessageForKey("http.error.unknownhost") + " " + url);
				LogEvent.logError("HttpSender", "sendPutMessage()", e.toString());
			} 
			catch (IOException e) {
				errors.add(StringUtil.getMessageForKey("http.error.io" ) + " " + url);
				LogEvent.logError("HttpSender", "sendPutMessage()", e.toString());
			}
		} finally {
			httpPost.releaseConnection();
		}
	}
}

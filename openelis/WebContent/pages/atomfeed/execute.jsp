<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="org.bahmni.feed.openelis.feed.job.openmrs.OpenMRSPatientFeedReaderJob" %>
<%@ page import="org.apache.log4j.Logger" %>

<%
  OpenMRSPatientFeedReaderJob job = null;
  try {
    job = new OpenMRSPatientFeedReaderJob();
  } catch (Throwable e) {
    Logger.getLogger(OpenMRSPatientFeedReaderJob.class).error(e.getMessage(), e);
    return;
  }
  job.execute(null);
%>
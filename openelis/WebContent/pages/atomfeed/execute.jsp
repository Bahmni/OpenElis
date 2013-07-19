<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="org.bahmni.feed.openelis.feed.client.OpenMRSPatientFeedReaderJob" %>
<%@ page import="org.apache.log4j.Logger" %>

<%
  try {
    OpenMRSPatientFeedReaderJob job = new OpenMRSPatientFeedReaderJob();
    job.execute(null);
  } catch (Exception e) {
  }
%>
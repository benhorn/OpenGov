<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="au.gov.nsw.records.digitalarchive.base.JSONFactory"%>
<%
  JSONFactory jFactory = new JSONFactory();
  out.println(jFactory.keywordJSON());
%>
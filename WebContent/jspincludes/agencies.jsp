<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="au.gov.nsw.records.digitalarchive.base.JSONFactory"%>
<%
	String query = request.getParameter("q");
	JSONFactory jFactory = new JSONFactory();
	out.println(jFactory.AgenciesJSON(query));
%>
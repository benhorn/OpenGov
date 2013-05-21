<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%  
	Member member = (Member)request.getSession().getAttribute("member");
	if (member != null)
	{
		String whereTo = request.getParameter("page");
	    String redirectURL = "/my_publications";
	    
		if(("myPub").equalsIgnoreCase(whereTo))
	    {	redirectURL = "/my_publications";}
		response.sendRedirect(redirectURL);
%>

<%
}else {%>
	Session time out. Please <a href="<html:rewrite page='/agency_login'/>">Login</a> again.
<% } %>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.Archivist" %>
<%
	Archivist archivist = (Archivist)session.getAttribute("archivist");
	if (archivist == null) 
	{
%>
<script language="javascript">
<!--
	window.top.location="adminLogin.jsp";
//-->
</script>
<%
}
%>
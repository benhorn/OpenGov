<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="org.jabsorb.JSONRPCBridge"/>
<%@page import="au.gov.nsw.records.digitalarchive.base.AjaxBean"%> 
<%  
	Member member = (Member)request.getSession().getAttribute("member");
	if (member != null)
	{
		AjaxBean ajaxBean = new AjaxBean();
		JSONRPCBridge.registerObject("ajaxBean", ajaxBean); 
		HashMap<String, String> JSONMap = (HashMap)request.getSession().getAttribute("JSONMap");
%>	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OpenGov (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
<link rel="stylesheet" type="text/css" media="all" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/menus.css"/>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>
<body class="yui-skin-sam">
 <div class="BodyWrapper">
 <div class="Header"><a title="Home" href="https://www.opengov.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0">
 <img class="Logo" src="images/OpenGov.png" alt="logo" border="0"/>
 </a></div>
 <div class="AboveSiteContent">
 <h2 class="Cloak">Content Actions</h2>
 <ul class="ContentActions FooterMenu">
 	<li><a href="#" title="Print this Page" onclick="window.print(); return false;"><img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/></a></li>
 </ul>
 <h2 class="Cloak">NSW Sites</h2>
 <jsp:include page="../jspincludes/loggedin_menu.jsp"/>
</div>

<div class="PageTitle"><h1>My stats</h1>
</div>
<div class="ContextInformation">
 <jsp:include page="../jspincludes/left_sidebar.jsp"/>
</div>

<div class="SiteContent">
	<form>             	
	<fieldset>
		<legend><span>My stats</span></legend>
		<table>
			  <tr><td></td></tr>
		</table>
	</fieldset>
	</form>
</div>
<jsp:include page="../jspincludes/footer.jsp"/>
</body></html>
<%} else {%>
Session time out. Please <a href="<html:rewrite page='/agency_login'/>">Login</a> again.
<% } %>
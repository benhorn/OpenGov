<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="java.util.List" %>
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

<div class="PageTitle"><h1>My details</h1>
<logic:messagesPresent message="true">
		<html:messages id="msgs" message="true">
			<ul><bean:write name="msgs"/></ul>
		</html:messages>
</logic:messagesPresent></div>
<div class="ContextInformation">
 <jsp:include page="../jspincludes/left_sidebar.jsp"/>
</div>

<div class="SiteContent">
	<html:form action="/member.do?method=chgDetail" method="post">             	
	<fieldset>
		<legend><span>My agency</span></legend>
		<table>
			   <tr><td>Agency name:</td>
			       <td>	<%
			 FullAgencyListService fal = new FullAgencyListServiceImpl();
			 FullAgencyList fls = fal.loadFullAgencyList(member.getFullAgencyList().getFullAgencyListId());
			 out.println(fls.getAgencyName().trim());			
						%>
					</td>
				</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend><span>My contact details</span></legend>
		<table>
		<tr><td>First name:</td><td><html:text property="firstName" styleId="firstName" value="${member.firstname}" size="30"/></td></tr>
		<tr><td>Last name:</td><td><html:text property="lastName" styleId="lastName" value="${member.lastname}" size="30"/></td></tr>
		<tr><td>Email (work):</td><td><html:text property="email" styleId="email" value="${member.email}" size="30"/></td></tr>
		<tr><td>Phone (Office hours):</td><td><html:text property="phone" styleId="phone" value="${member.phone}" size="30"/></td></tr>
		</table>
	</fieldset>
	<fieldset>
		<legend><span>My password</span></legend>
		<table>
			<tr><td>New password:</td><td><html:password property="loginPwd" styleId="loginPwd" size="30"/></td></tr>
			<tr><td>Re-type password:</td><td><input type="password" id="reLoginPwd" size="30" onblur="checkPwd();"/></td></tr>
		</table>
	</fieldset>
		<p align="right">
		<input type="hidden" value="${member.memberId}"/>
		<input type="submit" name="update" value="Update"/></p>
	</html:form>
</div>
<jsp:include page="../jspincludes/footer.jsp"/>
<script type="text/javascript">
<!--
function validateCurrentPwd(){

	var currentPwd = '<c:out value="${member.password}"/>';
	if (currentPwd != window.document.memberForm.currentPwd.value)
	{  alert("Incorrect Current password entered!");
	   window.document.memberForm.currentPwd.value = "";
	   window.document.memberForm.currentPwd.focus();
	}
}

function checkPwd(){
	if (window.document.memberForm.loginPwd.value != window.document.memberForm.reLoginPwd.value)
	{
		alert("Passwords don\'t match, please re-enter");
		window.document.memberForm.loginPwd.value = "";
		window.document.memberForm.reLoginPwd.value = "";
		window.document.memberForm.loginPwd.focus();
	}
}
//-->
</script>
</body></html>
<%} else {%>
Session time out. Please <a href="<html:rewrite page='/agency_login'/>">Login</a> again.
<% } %>
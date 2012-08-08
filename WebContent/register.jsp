<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<jsp:useBean id="JSONRPCBridge" scope="session" class="org.jabsorb.JSONRPCBridge" />
<%@page import="au.gov.nsw.records.digitalarchive.base.AjaxBean"%>
<% 
	AjaxBean ajaxBean = new AjaxBean();
	JSONRPCBridge.registerObject("ajaxBean", ajaxBean); 
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Publications (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="http://publications.nsw.gov.au/@@/default/favicon.png"/>
<link rel="stylesheet" type="text/css" media="all" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/menus.css"/>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<link rel="stylesheet" type="text/css" href="css/token-input-ui.css" />
<link rel="stylesheet" type="text/css" href="css/Tooltip.css" />
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="js/Tooltip.js"></script>
<script type="text/javascript" src="js/jsonrpc.js"></script>
<script type="text/javascript">
<!--
function reloadAgencies(agencyNumber)
{
	$("#chooseAgency").load('jspincludes/prefillAgency.jsp?agency_number=' + agencyNumber);
}
//-->
</script>
</head>
<body class="yui-skin-sam">
<div class="BodyWrapper">
  <div class="Header"><a title="Home" href="http://publications.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0"></a>
 		 <div class="loginBox">
		     <a href="login">Agency login</a>
		   </div>
  </div>
  <div class="AboveSiteContent">
  	<h2 class="Cloak">Content Actions</h2>
  	<ul class="ContentActions FooterMenu">
    	<li>
      	<a href="#" title="Print this Page" onclick="window.print(); return false;">
        <img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0">
      </a></li>
    </ul>    
  <h2 class="Cloak">NSW Sites</h2>
    <jsp:include page="jspincludes/menu.jsp" />

  <ul class="Breadcrumbs Menu">
      <li class="Breadcrumb Start">
        <a href="http://publications.nsw.gov.au/" title="publications.nsw">
          <span>publications.nsw</span>
        </a>
      </li>
  </ul>
</div>
<div class="PageTitle"><h1>Registration</h1></div>
<div class="ContextInformation">

</div>

<div class="SiteContent">
<logic:messagesPresent message="true">
	<div class="error">
		<html:messages id="msgs" message="true">
			<ul><bean:write name="msgs"/></ul>
		</html:messages>
	</div>
</logic:messagesPresent>
<div id="flag"></div>
	<html:form action="/reg?method=register" method="post">
	<fieldset>
		<legend><span>Which NSW agency do you work for?</span></legend>
		<table>
			   <tr><td>Find agency:</td>
			       <td>
	<div id="chooseAgency">
<%
	String AgencyNo = request.getParameter("agencyNumber");
	if (AgencyNo == null)
	{
%>
	<jsp:include page="jspincludes/agency_widget.jsp" flush="true"/>	
<%
	}
	else
	{
%>
	<script type="text/javascript">
	<!--
		reloadAgencies(<%=AgencyNo%>);
	//-->
	</script>
<%
	}
%>
	</div>
	<div style="display:none;"> 
		<div id="new_agency">
		 		<p id="validateTips"><strong>Create a new agency</strong></p>
		 		<label for="nAgency">Agency name:</label>
				<input type="text" name="newAgency" id="newAgency" size="35"/>
		 		<p align="right"><html:button styleId="create_agency" property="createAgency" onclick="addAgency();" value="Create agency"/></p>
		</div>
	</div>
		</td>
		</tr>
<tr>
	<td></td>
	<td><small><span class="tooltip" onclick="this.position=1; this.sticky=true; tooltip.add(this, 'new_agency', true); return false;">Can't find your agency?</span></small>
		</td>
</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend><span>Contact details</span></legend>
		<table>
		<tr><td>First name:</td><td><html:text property="firstName" styleId="firstName" size="30"/></td></tr>
		<tr><td>Last name:</td><td><html:text property="lastName" styleId="lastName" size="30"/></td></tr>
		<tr><td>Email (work):</td><td><html:text property="email" styleId="email" size="30" onblur="checkLoginName();"/></td></tr>
		<tr><td>Phone (Office hours):</td><td><html:text property="phone" styleId="phone" size="30"/></td></tr>
		</table>
	</fieldset>
	<fieldset>
		<legend><span>Password</span></legend>
		<table>
			<tr><td>Password:</td><td><html:password property="loginPwd" styleId="loginPwd" size="30"/></td></tr>
			<tr><td>Re-type password:</td><td><input type="password" id="reLoginPwd" size="30" onblur="checkPwd();"/></td></tr>
		</table>
	</fieldset>
		<p align="right"><html:submit property="submit" value="Submit"/></p>
	</html:form>
</div>      
 <jsp:include page="jspincludes/footer.jsp"/>
 <script type="text/javascript">
<!--
var tips = $("#validateTips");
function updateTips(t) {
	tips.text(t).effect("highlight",{color:"#b1b1b1"},1500);
}
function checkPwd(){
	if (window.document.registrationForm.loginPwd.value != window.document.registrationForm.reLoginPwd.value)
	{
		alert("Passwords don\'t match, please re-enter");
		window.document.registrationForm.loginPwd.value = "";
		window.document.registrationForm.reLoginPwd.value = "";
		window.document.registrationForm.loginPwd.focus();
	}
}
function checkLoginName(){
	var	jsonrpc = new JSONRpcClient("JSON-RPC");
	var lname = window.document.registrationForm.email.value;
	//var element = window.document.createElement('<div class="error" id="errorbloc">There is already an account registered under this email, please choose another one</div>');
	if ((lname != null) && (lname.length > 0) && (!jsonrpc.ajaxBean.chkLoginName(lname))){
		alert("There is already an account registered under this email, please choose another one");
		//window.document.getElementById("flag").insertBefore(element);
		window.document.registrationForm.email.focus();
	}
}
function checkAgency()
{
	var	jsonrpc = new JSONRpcClient("JSON-RPC");
	var agencyName = window.document.registrationForm.newAgency.value;
	if ((agencyName != null) && (agencyName.length > 0) && (!jsonrpc.ajaxBean.chkAgencyName(agencyName)))
	{
		alert("This agency already exists");
		window.document.registrationForm.newAgency.focus();
		return true;		
	}else
	{
		return false;
	}
}
function addAgency()
{
	var jsonrpc = new JSONRpcClient("JSON-RPC");
	var agencyName = window.document.registrationForm.newAgency.value;
	var result = false;
	if ((agencyName == null) || (agencyName.length < 1))
	{
	   alert("Agency name is required.");
	}
	else
	{
	 if (!checkAgency())
	 {
		result = jsonrpc.ajaxBean.addAgency(agencyName);
		if (result)
		{
			alert("New agency created !");
			$("#newAgency").val("");
	  		$("#chooseAgency").load('jspincludes/prefillAgency.jsp?agencyName=' + encodeURI(agencyName));
		}else
		{
			alert("Unable to create this agency.");
			window.document.registrationForm.newAgency.focus();
		}
	 }
	}
}
//-->
</script>
</body></html>
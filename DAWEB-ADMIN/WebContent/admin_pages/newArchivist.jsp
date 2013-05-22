<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<jsp:useBean id="ajax" class="au.gov.nsw.records.digitalarchive.base.AjaxBean"/>
<%
	JSONRPCBridge.registerObject("ajax", ajax);
%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<script type="text/javascript" src="js/jsonrpc.js"></script>
<style type="text/css">
	<!--
	body {
		background-color: lightgrey;
	}
	-->
</style>
</head>
<body>
  <table width="600" border="0" align="center" cellpadding="0" cellspacing="0">
  	<tr>
		<td align="center"><h1>Create a new archivist</h1></td>
	  </tr>
      <tr>
        <td>First name:</td>
        <td><input type="text" name="firstName" size="30"/></td>
     </tr>
      <tr>
        <td>Last name:</td>
        <td><input type="text" name="lastName" size="30"/></td>
     </tr>
     <tr>
        <td>Email:</td>
        <td><input type="text" name="email" size="30"/></td>
     </tr>
      <tr>
        <td>Employee number:</td>
        <td><input type="text" name="employeeNumber" size="30"/></td>
     </tr>
     <tr>
        <td>Login name:</td>
        <td><input type="text" name="loginName" size="30"/></td>
     </tr>
     <tr>
        <td>Password:</td>
        <td><input type="text" name="password" size="30"/></td>
     </tr>
     <tr><td colspan="2"><input type="button" onClick="window.history.back(-1);" value="Back" name="Back"/>&nbsp;&nbsp;<input type="submit" name="create new archivist"/></td></tr>
    </table>
</body>
</html>
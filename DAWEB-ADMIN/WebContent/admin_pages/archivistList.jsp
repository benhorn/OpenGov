<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-bean" prefix="bean"%> 
<%@ taglib uri="/struts-html" prefix="html"%>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"> 
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<link rel="stylesheet" type="text/css" href="css/style.css"/>
		<link rel="stylesheet" type="text/css" href="css/displaytag.css"/>		
		
		<style type="text/css">
		<!--
		body {
			background-color: lightgrey;
		}
		-->
		</style>
	</head>  
  <body>
  	<table border="0" align="center" cellpadding="0" cellspacing="0" style="background-color:lightgrey; border:0px;">
	  <tr>
		<td align="center"><h1>Current users</h1></td>
	  </tr>
	  <tr>
		<td align="center">
		<html:link forward="addArchivist">
					 <h3>Add a system user</h3>			
			</html:link>
		</td>
	  </tr>
	  <tr>
	    <td align="center">		
			<display:table name="archivistList" id="archivist" pagesize="15" class="display" requestURI="/archivist.do?method=loadArchivist" >
				<display:column property="firstname" title="First name" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="lastname" title="Last name" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="email" title="Email" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="loginName" title="Login name" headerClass="sortable" style="text-align:center;"/>
				<display:column property="lastLogin" title="Last login" headerClass="sortable" style="text-align:center;"/>
			</display:table>
		</td>
      </tr>
      <tr>
	    <td height="30" align="center" class="redText"><html:errors property="delArchivistStatus"/></td>
      </tr>
  </table>
  </body>
</html>
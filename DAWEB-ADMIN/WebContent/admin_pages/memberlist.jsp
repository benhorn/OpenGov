<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-bean" prefix="bean"%> 
<%@ taglib uri="/struts-html" prefix="html"%>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"> 
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<link rel="stylesheet" type="text/css" href="css/style.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="css/global.css" />
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
  	<table border="0" align="center" cellpadding="0" class="content" cellspacing="0" style="background-color:lightgrey; border:0px;">
	  <tr>
		<td><h1>Current members</h1></td>
	  </tr>
	  <tr>
	    <td align="center">		
			<display:table name="memberList" id="member" pagesize="30" class="display" requestURI="/member.do?method=loadMember" >
				<display:column property="firstname" title="Firstname" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="lastname" title="Lastname" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="email" title="Email" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="phone" title="Phone" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="activated" title="Account activated" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="privileged" title="Privileged member" style="text-align:center;"/>
				<display:column property="registrationDate" title="Registration Date" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column title="Options" media="html">
						<html:link page="/member.do?method=viewMember" 
								   paramId="id" 
							   	   paramName="member" 
							   	   paramProperty="memberId" 
							   	   styleClass="bluebutton"><span>Detail</span>
						</html:link>
				</display:column>
			</display:table>
		</td>
      </tr>
  </table>
  </body>
</html>
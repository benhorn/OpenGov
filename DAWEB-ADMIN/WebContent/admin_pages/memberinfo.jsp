<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-bean" prefix="bean"%> 
<%@ taglib uri="/struts-html" prefix="html"%> 
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html> 
	<head>
		<title></title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
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
		<td height="40" class="itemTitle" align="center"></td>
	  </tr>
	  <tr>
		<td height="30" class="blueText" align="center">
		
		</td>
	  </tr>
	  <tr>
	    <td height="30" align="center">		
			<display:table name="memberList" id="row" pagesize="15" class="displaytag" requestURI="/member.do?method=loadMember" >
				<display:column property="firstname" title="Firstname" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="lastname" title="Lastname" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="email" title="Email" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="phone" title="Phone" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="activated" title="Account activated" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="registrationDate" title="Registration Date" sortable="true" headerClass="sortable" style="text-align:center;"/>
			</display:table>
		</td>
      </tr>
  </table>
  </body>
</html>
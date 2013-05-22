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
			<display:table name="adminList" id="row" pagesize="15" class="displaytag" requestURI="/admin.do?method=loadAdmin" >
				
			</display:table>
		</td>
      </tr>
      <tr>
	    <td height="30" align="center" class="redText"></td>
      </tr>
  </table>
  </body>
</html>
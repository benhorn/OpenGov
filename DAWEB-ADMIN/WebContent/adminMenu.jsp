<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://struts-menu.sf.net/tag" prefix="menu" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<html>
  <head>
	<title></title>
	<link rel="stylesheet" type="text/css" media="screen" href="css/global.css" />
	<script type="text/javascript" src="scripts/cookies.js"></script>
	<style type="text/css">
		<!--
		body {
			background-color:lightgrey;
		}
		-->
	</style>
  </head>  
  <body>
	<table cellpadding="5">
		<tr valign="top">
			 <td width="170" style=" width : 353px;">
				<menu:useMenuDisplayer name="DropDown" >
				    <menu:displayMenu name="AdminMenu" target="frame1"/>
				</menu:useMenuDisplayer>
			 </td>
		</tr>
	</table>	
  </body>
</html>
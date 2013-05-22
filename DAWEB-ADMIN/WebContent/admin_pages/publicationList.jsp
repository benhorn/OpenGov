<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="java.util.*"%>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-bean" prefix="bean"%> 
<%@ taglib uri="/struts-html" prefix="html"%> 
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<% 
String migrationURL= new String("window.location.href='/DAWEB-ADMIN/pub.do?method=dbUpdator'");
%>
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
		<td align="center"><h1>Current 'submitted' publications</h1></td>
	  </tr>
	  <tr>
	    <td align="center">		
			<display:table name="publicationList"  id="publication" pagesize="15" class="display" requestURI="/pub.do?method=submittedPublication" >
				<display:column property="title" title="Title" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="type" title="Type" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="language" title="language" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="datePublishedDisplay" title="Date published" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="lastUpdated" title="Last updated" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="dateRegistered" title="Registration Date" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column property="status" title="Current status" sortable="true" headerClass="sortable" style="text-align:center;"/>
				<display:column title="Options" media="html">
						<html:link page="/pub.do?method=publicationDetails" 
							   	   paramId="id"
							   	   paramName="publication" 
							   	   paramProperty="publicationId"><span>Details</span>
						</html:link>
				</display:column>
			</display:table>
		</td>
      </tr>
  </table>
    <table><tr><td><input type="button" onClick="<%=migrationURL%>" value="DB_Updater"/></td></tr></table>
  </body>
</html>
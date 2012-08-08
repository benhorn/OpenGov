<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%  
	Member member = (Member)request.getSession().getAttribute("member");
	if (member != null)
	{	
		Publication publication = (Publication)request.getSession().getAttribute("publication");
		FileService fs = new FileServiceImpl();
		List<UploadedFile> list = fs.browseFiles(publication);
		request.setAttribute("fileList", list);
		String UploadURL= new String("/DA-WEB/pub.do?method=uploadFiles&id=") + publication.getPublicationId();
		String updateURL = new String("/DA-WEB/dashboard.do?method=metaData&id=") + publication.getPublicationId();
		String submitURL = new String("/DA-WEB/pub.do?method=submitPublication&id=") + publication.getPublicationId();
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
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<link rel="stylesheet" type="text/css" href="css/displaytag.css"/>
</head>
<body class="yui-skin-sam">
  <div class="BodyWrapper">
    <div class="Header"><a title="Home" href="http://publications.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0"/></a></div>
      <div class="AboveSiteContent">
  		<h2 class="Cloak">Content Actions</h2>
  		<ul class="ContentActions FooterMenu">
    		<li><a href="#" title="Print this Page" onclick="window.print(); return false;">
    		<img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/></a></li>
    	</ul>
  <h2 class="Cloak">NSW Sites</h2>
  <jsp:include page="../jspincludes/loggedin_menu.jsp"/>

</div>

<div class="PageTitle"><h1>Displaying <%=publication.getTitle()%><small>(<%=publication.getStatus() %>)</small></h1></div>
<div class="ContextInformation">
<jsp:include page="../jspincludes/left_sidebar.jsp"/>
</div>

<div class="SiteContent">
		<table width="100%">
			<tr>
				<td>
				<display:table name="fileList" id="UploadedFile" pagesize="15" class="displaytag" requestURI="/DA-WEB/pub.do?method=publicationDetails">
					<display:column property="downloadLink" title="File name" sortable="true" headerClass="sortable" style="text-align:center;"/>
					<display:column property="dateUploaded" title="Date uploaded" sortable="true" headerClass="sortable" style="text-align:center;"/>
					<display:column property="size" title="Size" style="text-align:center;"/>
					<display:column property="contentType" title="Content type" headerClass="sortable" style="text-align:center;"/>
					<display:column property="uploadedBy" title="Uploaded by" sortable="true" headerClass="sortable" style="text-align:center;"/>
				</display:table>
			   </td>
			</tr>
		</table>
		<hr/>
		<div class="squarebuttonwrapper">
		<table width="100%">
			<tr>
				<td><a class="orangesquarebutton" href="my_publications" title="My publications"><span>My publications</span></a></td>
				<logic:equal name="publication" property="status" value="draft">
					<td><a class="bluesquarebutton" href="<%=UploadURL%>" title="Upload files"><span>Upload files</span></a></td>
					<td><a class="redsquarebutton" href="<%=updateURL%>" title="Update metadata"><span>Update metadata</span></a></td>
					<td><a class="graysquarebutton" href="<%=submitURL%>" title="Submit publication"><span>Submit</span></a></td>
				</logic:equal>
			</tr>
		</table>
		</div>
</div>
      
  <jsp:include page="../jspincludes/footer.jsp"/>
</body></html>
<%} else {%>
Session time out. Please <a href="<html:rewrite page='/login.jsp'/>">Login</a> again.
<% } %>
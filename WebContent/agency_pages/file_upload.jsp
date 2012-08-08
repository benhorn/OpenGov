<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%  
	Member member = (Member)request.getSession().getAttribute("member");
	String id = request.getParameter("id");
	if (member != null)
	{
		String RedirectURL= new String("window.location.href='/DA-WEB/pub.do?method=publicationDetails&id=") + id + "'"; 
		PublicationService ps = new PublicationServiceImpl();
		Publication publication = ps.loadPublication(Integer.parseInt(id));
		FileService fs = new FileServiceImpl();
		List<UploadedFile> list = fs.browseFiles(publication);
		String updateURL = new String("/DA-WEB/dashboard.do?method=metaData&id=") + publication.getPublicationId();
		String submitURL = new String("/DA-WEB/pub.do?method=submitPublication&id=") + publication.getPublicationId();
		request.setAttribute("fileList", list);
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
<link rel="stylesheet" type="text/css" href="css/ui-lightness/jquery-ui-1.8.14.custom.css"/>
<link rel="stylesheet" type="text/css" href="css/fileUploader.css"/>
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
<script src="jspincludes/jquery.fileUploader.jsp?id=<%=id%>"></script>
</head>
    <body class="yui-skin-sam">
      <div class="BodyWrapper">
        <div class="Header"><a title="Home" href="http://publications.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0"></a></div>
        <div class="AboveSiteContent">
  		<h2 class="Cloak">Content Actions</h2>
  		<ul class="ContentActions FooterMenu">
    		<li><a href="#" title="Print this Page" onclick="window.print(); return false;"><img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/></a></li>
    	</ul>
  <h2 class="Cloak">NSW Sites</h2>
  <jsp:include page="../jspincludes/loggedin_menu.jsp"/>
</div>
<div class="PageTitle"><h1>Displaying <%=publication.getTitle()%><small>(<%=publication.getStatus() %>)</small></h1></div>

<div class="ContextInformation">
<jsp:include page="../jspincludes/left_sidebar.jsp"/>
</div>

<div class="SiteContent">
<%
	if (("draft").equalsIgnoreCase((publication.getStatus())))
	{
%>
		<div id="main_container">
			<form action="fileUploadAction.do?id=<%=id%>" method="post" enctype="multipart/form-data">
				<input type="file" name="file" class="fileUpload" multiple/>
			</form>
			<script type="text/javascript">
				<!--
					jQuery(function($){
						$('.fileUpload').fileUploader();
					});
				//-->
			</script>
		</div>
<%
	}
%>
<hr/>
		<div id="displayTable">
		<display:table name="fileList" id="UploadedFile" pagesize="15" class="displaytag" requestURI="/DA-WEB/pub.do?method=uploadFiles">
				<display:column property="downloadLink" title="File name" headerClass="sortable" style="text-align:left;"/>
				<display:column property="dateUploaded" title="Date uploaded" headerClass="sortable" style="text-align:center;"/>
				<display:column property="size" title="Size" style="text-align:center;"/>
				<display:column property="contentType" title="Content type" headerClass="sortable" style="text-align:center;"/>
				<%
				if (("draft").equalsIgnoreCase((publication.getStatus())))
				{
				%>
				<display:column title="Option" media="html">
					<html:link page="/pub.do?method=deleteFile" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   onclick="return confirm('Are you sure you want to delete this file? (This operation cannot be undone)');"
							   paramProperty="fileId"><span>Delete</span>
					</html:link>
				</display:column>
				<%
				}
				%>
		</display:table>		
		</div>		
<hr/>
<%
	if (("draft").equalsIgnoreCase((publication.getStatus())))
	{
%>
<div class="sbuttonwrapper">
		<table>
			<tr>
				<td><a class="graysquarebutton" href="<%=updateURL%>" title="Update metadata"><span>Update metadata</span></a>&nbsp;&nbsp;
					<a class="redsquarebutton" href="<%=submitURL%>" onclick="return confirm('By submitting this publication you have agreed for it to be reviewed by our staff.');" title="Submit publication"><span>Submit</span></a></td>
			</tr>
		</table>
</div>
<%
	}
%>
</div>


 <jsp:include page="../jspincludes/footer.jsp"/>
</body>
</html>

<%} else {%>
Session time out. Please <a href="<html:rewrite page='/login.jsp'/>">Login</a> again.
<% } %>
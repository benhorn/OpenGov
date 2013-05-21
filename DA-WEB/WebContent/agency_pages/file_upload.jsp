<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%  
	Member member = (Member)request.getSession().getAttribute("member");
	String id = request.getParameter("id");
	if (member != null)
	{
		String RedirectURL= new String("window.location.href='/pub.do?method=publicationDetails&id=") + id + "'"; 
		PublicationService ps = new PublicationServiceImpl();
		Publication publication = ps.loadPublication(Integer.parseInt(id));
		FileService fs = new FileServiceImpl();
		List<UploadedFile> list = fs.browseFiles(publication);
		String updateURL = new String("/dashboard.do?method=metaData&id=") + publication.getPublicationId();
		String submitURL = new String("/pub.do?method=submitPublication&id=") + publication.getPublicationId();
		request.setAttribute("fileList", list);
		String maxOrder = Integer.toString(fs.maxFileOrder(publication.getPublicationId().toString()));
		String minOrder = Integer.toString(fs.minFileOrder(publication.getPublicationId().toString()));
%>	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OpenGov (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
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
        <div class="Header"><a title="Home" href="https://www.opengov.nsw.gov.au/">
        <img class="Logo" src="images/logo.png" alt="logo" border="0"/><img class="Logo" src="images/OpenGov.png" alt="logo" border="0"/></a></div>
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
		<display:table name="fileList" id="UploadedFile" pagesize="15" class="displaytag" requestURI="/pub.do?method=uploadFiles">
				<display:column property="downloadLink" title="File name" headerClass="sortable" style="text-align:left;"/>
				<display:column property="dateUploaded" title="Date uploaded" headerClass="sortable" style="text-align:center;"/>
				<display:column property="size" title="Size" style="text-align:center;"/>
				<display:column property="contentType" title="Content type" headerClass="sortable" style="text-align:center;"/>
				<display:column property="fileOrder" title="Order" style="text-align:center;"/>
				<% 
				if ( ("draft").equalsIgnoreCase(publication.getStatus()) && publication.getTotalFiles() !=null)
				{	
					if (Integer.parseInt(publication.getTotalFiles()) > 1)
					{
				%>
				<display:column title="  " media="html">
					<logic:equal name="UploadedFile" property="fileOrder" value="<%=minOrder%>">
						<html:link page="/file.do?method=moveDown" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   paramProperty="fileId"><img src="images/down.gif" alt="Down"/></html:link>
					</logic:equal>
					
					<logic:notEqual name="UploadedFile" property="fileOrder" value="<%=maxOrder%>">
						<logic:notEqual name="UploadedFile" property="fileOrder" value="<%=minOrder%>">
						<html:link page="/file.do?method=moveUp" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   paramProperty="fileId"><img src="images/up.gif" alt="Up"/></html:link>&nbsp;
					    <html:link page="/file.do?method=moveDown" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   paramProperty="fileId"><img src="images/down.gif" alt="Down"/></html:link>
						</logic:notEqual>
					</logic:notEqual>
					<logic:equal name="UploadedFile" property="fileOrder" value="<%=maxOrder%>">
						<html:link page="/file.do?method=moveUp" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   paramProperty="fileId"><img src="images/up.gif" alt="Down"/></html:link>
					</logic:equal>					
				</display:column>
				<%
				}
				%>
				<display:column title="Option" media="html">
					<html:link page="/pub.do?method=deleteFile" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   onclick="return confirm('Are you sure you want to delete this file? (This operation cannot be undone)');"
							   paramProperty="fileId"><span>Delete</span>
					</html:link>
				</display:column>
				<%} %>
		</display:table>		
		</div>		
<hr/>
<%
	if (("draft").equalsIgnoreCase((publication.getStatus())))
	{
%>
<table>
	<tr>
		<td><a class="greySquare" href="<%=updateURL%>" title="Update metadata"><span>Update metadata</span></a>&nbsp;&nbsp;
			<a class="greySquare" href="<%=submitURL%>" onclick="return confirm('After submitting this publication you cannot make any further changes. Are you ready to submit?');" title="Submit publication"><span>Submit publication</span></a></td>
	</tr>
</table>
<%
	}
%>
</div>
 <jsp:include page="../jspincludes/footer.jsp"/>
</body>
</html>

<%} else {%>
Session time out. Please <a href="<html:rewrite page='/agency_login'/>">Login</a> again.
<% } %>
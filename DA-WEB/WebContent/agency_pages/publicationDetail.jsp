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
		Publication currentPub = (Publication)request.getSession().getAttribute("currentPub");
		FileService fs = new FileServiceImpl();
		KeywordService ks = new KeywordServiceImpl();
		FullAgencyListService fas = new FullAgencyListServiceImpl();
		PublisherPublicationService pps = new PublisherPublicationServiceImpl();
		List<UploadedFile> list = fs.browseFiles(currentPub);
		request.setAttribute("fileList", list);	
%>	
<%
		List<Keyword> kList = ks.loadKeywordViaPublication(currentPub.getPublicationId().toString());
		List<FullAgencyList> faList = fas.loadAgencyViaPublication(currentPub.getPublicationId().toString());
		List<FullAgencyList> pList = pps.loadPublisherViaPublication(currentPub.getPublicationId().toString());
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OpenGov (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<link rel="stylesheet" type="text/css" href="css/mouseover.css" />
<link rel="stylesheet" type="text/css" media="all" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/menus.css"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>

<link rel="stylesheet" type="text/css" href="css/displaytag.css"/>
</head>
<body class="yui-skin-sam">
  <div class="BodyWrapper">
    <div class="Header"><a title="Home" href="https://www.opengov.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0"/>
    	<img class="Logo" src="images/OpenGov.png" alt="logo" border="0"/></a></div>
      <div class="AboveSiteContent">
  		<h2 class="Cloak">Content Actions</h2>
  		<ul class="ContentActions FooterMenu">
    		<li><a href="#" title="Print this Page" onclick="window.print(); return false;">
    		<img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/></a></li>
    	</ul>
  <h2 class="Cloak">NSW Sites</h2>
  <jsp:include page="../jspincludes/loggedin_menu.jsp"/>
</div>

<div class="PageTitle"><h1>${currentPub.title}&nbsp;<small>(${currentPub.status})</small></h1></div>
<div class="ContextInformation">
<jsp:include page="../jspincludes/left_sidebar.jsp"/>
</div>

<div class="SiteContent">
	<fieldset>
		<table class="publication">
		<tr>
			<td><span class="dropt" title="">Title:<span><bean:message key="metadata.title"/></span></span></td> 
			<td>${currentPub.title}</td>
		</tr>
		<tr>
			<td valign="top"><span class="dropt" title="">Description:<span><bean:message key="metadata.description"/></span></span></td>
			<td>${currentPub.description}</td>
		</tr>
		<tr>
			<td valign="top"><span class="dropt" title="">Keywords:<span><bean:message key="metadata.keywords"/></span></span></td>
			<td><% Iterator<Keyword> kIterator = kList.iterator(); 
	  	  		 while(kIterator.hasNext())
	  	  		 {
	  	  			 Keyword keyword = (Keyword)kIterator.next();
	  	  			 out.println("'" + keyword.getKeyword() + "'" + "&nbsp;");
	  	  		 }
	  	  	  %></td>
		</tr>
		<tr>
			<td valign="top"><span class="dropt" title="">Agency:<span><bean:message key="metadata.agency"/></span></span></td>
			<td><% Iterator<FullAgencyList> aIterator = faList.iterator(); 
	  	  		 while(aIterator.hasNext())
	  	  		 {
	  	  			 FullAgencyList fal = (FullAgencyList)aIterator.next();
	  	  			 out.println(fal.getAgencyName() + "<br/><br/>");
	  	  		 }
	  	  	  %>
			</td>
		</tr>
		<tr><td>Date published:</td><td>${currentPub.datePublishedDisplay}</td></tr>
	  	<tr><td>Type:</td><td>${currentPub.type}</td></tr>
	    <tr><td>Type Other:</td><td>${currentPub.typeOther}</td></tr>
	    <tr><td>Coverage:</td><td>${currentPub.coverage}</td></tr>
	    <tr><td>Language:</td><td>${currentPub.language}</td></tr>
	    <tr><td valign="top">Publisher:</td>
	  	    <td><% Iterator<FullAgencyList> pIterator = pList.iterator(); 
	  	  		 while(pIterator.hasNext())
	  	  		 {
	  	  			 FullAgencyList fal = (FullAgencyList)pIterator.next();
	  	  			 out.println(fal.getAgencyName() + "<br/><br/>");
	  	  		 }
	  	  	  %></td>
	    </tr>
	    <tr><td>Rights:</td><td>${currentPub.rights}</td></tr>
	    <tr><td>Total pages:</td><td>${currentPub.totalPages}</td></tr>
	    <tr><td>Total files:</td><td>${currentPub.totalFiles}</td></tr>
	    <tr><td>Status:</td><td>${currentPub.status}</td></tr>
		<tr><td>Date uploaded:</td><td>${currentPub.dateRegistered}</td></tr>
		</table>
		<div></div>
		</fieldset>
		
	  <table border="0" cellpadding="5" cellspacing="5">
	  <tr><td>
  		<display:table name="fileList" id="UploadedFile" pagesize="15">
				<display:column property="downloadLink" title="File name" headerClass="sortable" style="text-align:left;"/>
				<display:column property="dateUploaded" title="Date uploaded" headerClass="sortable" style="text-align:center;"/>
				<display:column property="pages" title="Pages" style="text-align:center;"/>
				<display:column property="size" title="Size" style="text-align:center;"/>
				<display:column property="fileOrder" title="Order" style="text-align:center;"/>
				<display:column property="contentType" title="Content type" headerClass="sortable" style="text-align:center;"/>
		</display:table>
		</td></tr>
	</table>
	  
</div>	  
	  
  <jsp:include page="../jspincludes/footer.jsp"/>
</body></html>
<%} else {%>
Session time out. Please <a href="<html:rewrite page='/agency_login'/>">Login</a> again.
<% } %>
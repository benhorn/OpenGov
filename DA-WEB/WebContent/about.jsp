<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OpenGov (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
<link rel="stylesheet" type="text/css" media="all" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/menus.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/style.css"/>
<link rel="stylesheet" type="text/css" href="css/dailymotion.css" />
</head>
    <body class="yui-skin-sam">
      <div class="BodyWrapper">
        <div class="Header">
		<a title="Home" href="https://www.opengov.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0"/>
				<img class="Logo" src="images/OpenGov.png" alt="logo" border="0"/>
			</a>
		   <div class="loginBox">
		     <a href="agency_login" title="login">Agency login</a>
		   </div>
		</div>
        <div class="AboveSiteContent">
  		<h2 class="Cloak">Content Actions</h2>
  		<ul class="ContentActions FooterMenu">
    		<li><a href="#" title="Print this Page" onclick="window.print(); return false;"><img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/></a></li>
    	</ul>
  <h2 class="Cloak">NSW Sites</h2>
  <jsp:include page="jspincludes/menu.jsp" />
  <ul class="Breadcrumbs Menu">
      <li class="Breadcrumb Start">
      </li>
  </ul>
</div>
<div class="PageTitle"></div>

<div class="listTable">
<p>Welcome to OpenGov NSW. Here is where you will find information published by NSW Government agencies, including Annual Reports and <a href="http://www.oic.nsw.gov.au/oic/openaccessinformation.html" target="_blank">open access information</a> released under the Government Information (Public Access) Act 2009 (GIPA Act). 
   Please note, this site is not a comprehensive collection of all NSW government published information. Other useful sources include <a href="http://data.nsw.gov.au/" target="_blank">data.nsw</a>, the <a href="http://www.nsw.gov.au/gazette" target="_blank">NSW Government Gazette</a>, the <a href="http://www.sl.nsw.gov.au/using/search/" target="_blank">State Library of NSW</a>, 
   <a href="http://www.legislation.nsw.gov.au" target="_blank">NSW Legislation</a>, the <a href="http://www.parliament.nsw.gov.au" target="_blank">Parliament of NSW</a> and the <a href="http://www.records.nsw.gov.au/state-archives/collection-search" target="_blank">State archives collection</a>.</p>
<p>Content on this site is licensed <a href="http://creativecommons.org.au/learn-more/licences" target="_blank">Attribution (CC BY)</a> under the Creative Commons licensing system, unless indicated otherwise. Copyright resides with the State of New South Wales unless otherwise indicated.</p>
<p>Instructions for NSW Government agencies (including Local Government) on registering with OpenGov and adding and managing content is available from the <a href="/faq">FAQs</a> page.</p>
<p>Comments and questions? Please contact us via <a href="mailto:opengov@records.nsw.gov.au">opengov@records.nsw.gov.au</a></p>
</div>

<jsp:include page="jspincludes/footer.jsp"/>
</body></html>
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
<jsp:include page="jspincludes/search.jsp" />
<div class="listTable">
	 <div class="publication">
	 	  <h2>Latest</h2>
	 	  	<jsp:include page="jspincludes/latest.jsp" />
	 </div>
	 <div class="popular">
	 	  <h2>Popular</h2>
	 	  	<jsp:include page="jspincludes/popular.jsp" />
	 </div>
	 <br style="clear:both;"/>
	 <hr/>
</div>

<div>
	  <jsp:include page="jspincludes/agencyCloud.jsp" />
</div>

<jsp:include page="jspincludes/footer.jsp"/>
</body></html>
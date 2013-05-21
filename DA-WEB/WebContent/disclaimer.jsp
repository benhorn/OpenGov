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
<p>The New South Wales Government makes this material available on the understanding that 
users exercise their own skill and care with respect to its use. Before relying on the 
material in any important matter, users should carefully evaluate the accuracy completeness 
and relevance of the information for their purposes and should obtain appropriate professional 
advice relevant to their particular circumstances. The material at this site may include views or recommendations 
of third parties which do not necessarily reflect the views of the New South Wales Government or indicate its commitment 
to a particular course of action. Links to other web sites are inserted for convenience and do not constitute endorsement 
of material at those sites or any associated organisation product or service. These external information sources are outside our 
control. It is the responsibility of users to make their own decisions about the accuracy, currency, reliability and correctness 
of the information at those sites.</p>
<p>By accessing information at or through this site, each user waives and releases the New South Wales Government to the 
full extent permitted by law from any and all claims relating to the usage of and/or reliance on the material made available 
through the web site. In no event shall the New South Wales Government be liable for any injury, loss or damage resulting from 
use of or reliance upon the material.</p>
</div>

<jsp:include page="jspincludes/footer.jsp"/>
</body></html>
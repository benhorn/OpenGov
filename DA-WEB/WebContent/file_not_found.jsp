<?xml version="1.0" encoding="UTF-8"?>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"
        lang="en">
    <head>
    <title>OpenGov (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
	  <link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
	  <link rel="stylesheet" type="text/css" media="all" href="https://www.opengov.nsw.gov.au/css/layout.css"/>
	  <link rel="stylesheet" type="text/css" media="all" href="https://www.opengov.nsw.gov.au/css/print.css"/>
	  <link rel="stylesheet" type="text/css" media="all" href="https://www.opengov.nsw.gov.au/css/menus.css"/>
	  <link rel="stylesheet" type="text/css" media="all" href="https://www.opengov.nsw.gov.au/css/style.css"/>
    </head>
    <body class="yui-skin-sam">
      <div class="BodyWrapper">
        <div class="Header">
        	<a title="Home" href="https://www.opengov.nsw.gov.au/">
        		<img class="Logo" src="https://www.opengov.nsw.gov.au//images/logo.png" alt="logo" border="0"/>
        	</a>
        	<div class="loginBox">
		     <a href="agency_login" title="login">Agency login</a>
		   </div>
        </div>
        <div class="AboveSiteContent">
  <h2 class="Cloak">Content Actions</h2>
  <ul class="ContentActions FooterMenu">
    <li>
      <a href="#" title="Print this Page" onclick="window.print(); return
        false;">
        <img class="Logo" border="0" src="https://www.opengov.nsw.gov.au/images/icon_print.gif" alt="Print this page" />
      </a></li>
    </ul>
    
  <h2 class="Cloak">NSW Sites</h2>
	 <%
	String selection = request.getParameter("selected");
	String selected = "class=\"selected\"";
	String main="";
	String about ="";
	String register="";
	String login = "";
	if (("main").equalsIgnoreCase(selection))
		main = selected;
	if (("about").equalsIgnoreCase(selection))
		about = selected;
	if (("login").equalsIgnoreCase(selection))
		login = selected;
%>

<ul class="Menu">
    <li <%=main%>>
      <a href="/main" title="Search OpenGov">Search</a>
    </li>
    <li <%=about%>>
      <a href="/about" title="About OpenGov">About</a>
    </li>
    <li>
      <a href="http://data.nsw.gov.au" target="_blank" title="NSW data catalogue">Data</a>
    </li>
    <li>
      <a href="http://www.sdi.nsw.gov.au/GPT9/catalog/main/home.page" target="_blank" title="NSW spatial data catalogue">Spatial</a>
    </li>
    <li>
      <a href="http://www.records.nsw.gov.au/" target="_blank" title="Search archival information">State Records</a>
    </li>
    <li>
      <a href="http://www.ipc.nsw.gov.au/" target="_blank" title="IPC">IPC</a>
    </li>
</ul> 
  <ul class="Breadcrumbs Menu">
      <li class="Breadcrumb Start">
      </li>
  </ul>
</div>
        <div class="PageTitle">
  <h1></h1>
</div>
        <div class="ContextInformation"><div class="Messages">
</div>
</div>  
        <div class="SiteContent"><div>
  <br />
  <br />
  <h3>
    The page you are trying to access is not available
  </h3>
  <br />
  <b>
    Please try the following:
  </b>
  <br />
  <ol>
    <li>
      Make sure that the Web site address is spelled correctly.
    </li>
    <li>
      <a href="javascript:history.back(1);">
        Go back and try another URL.
      </a>
    </li>
  </ol>
</div>
</div>
<div class="Footer">
<div class="FooterMenu">
<div>
<a href="/disclaimer"><span>Disclaimer</span></a>
<span>|</span>&nbsp;<a href="/privacy"><span>Privacy</span></a>
<span>|</span>&nbsp;<a href="mailto:opengov@records.nsw.gov.au"><span>Contact</span></a>
<span>|</span>&nbsp;<a href="/FAQs"><span>FAQs</span></a>
</div>
</div>
<div class="ExternalLinks">
  <a href="http://www.nsw.gov.au/">NSW Government</a> |
  <a href="http://www.jobs.nsw.gov.au/">jobs.nsw</a>
</div>

<div class="ClearFloating"></div>
</div>
</div>
<div class="VisualPageEnd"></div>
    </body>
  </html>


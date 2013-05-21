<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page contentType="text/html; charset=utf-8" %>
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
<div class="PageTitle"><h1>API</h1></div>
<div class="listTable">
<h1 id="working-with-the-opengov-nsw-api">Working with the OpenGov NSW API</h1>
<p>OpenGov NSW data and functionality are available via an open web API.
 This service provides additional options for agencies wishing to 
integrate OpenGov NSW with their own websites. It also means that 
external developers, and members of the public more broadly, can easily 
extract and use OpenGov NSW data.</p>
<p>Please note that OpenGov NSW content is licensed Attribution (CC BY) 
under the Creative Commons licensing system, unless otherwise indicated.
 Copyright resides with the State of New South Wales unless otherwise 
indicated.</p>
<p>The OpenGov NSW web API is being actively developed. Unfortunately 
this does mean that it may change over the coming months. On the 
positive side, we will be very receptive, and grateful, for any feedback
 or suggestions for improvements: opengov@records.nsw.gov.au</p>
<h2 id="search-for-publications">Search for publications</h2>
<p>https://www.opengov.nsw.gov.au/search.xml?query=<em>query term</em></p>
<p>Returns search results in the OpenSearch XML format (http://www.opensearch.org)</p>
<h2 id="list-all-publications">List all publications</h2>
<p>https://www.opengov.nsw.gov.au/publication.json</p>
<p>Returns a JSON list of all publications on the site.</p>
<p>Information about publications comprises descriptive metadata and a 
list of publication files. Publication files are the sequence of files 
that make up a publication: while many publications are associated with 
just one file, others, including annual reports published in separate 
parts, have multiple files.</p>
<p>For example:</p>
<pre><code>{
  "ID": 11305,
  "Title": "NSW Department of Commerce Annual Report 2007/08",
  "Agencies": [
    "NSW Department of Commerce"
  ],
  "Type": "Annual Report",
  "Date": "2008",
  "Publishers": [],
  "Keywords": [
    "annual reports"
  ],
  "Description": "",
  "Language": "English",
  "Coverage": "New South Wales",
  "Copyright": "State of New South Wales",
  "Files": [
    "https://www.opengov.nsw.gov.au/publication/12304",
    "https://www.opengov.nsw.gov.au/publication/12305",
    "https://www.opengov.nsw.gov.au/publication/12306",
    "https://www.opengov.nsw.gov.au/publication/12307",
    "https://www.opengov.nsw.gov.au/publication/12308",
    "https://www.opengov.nsw.gov.au/publication/12309",
    "https://www.opengov.nsw.gov.au/publication/12310",
    "https://www.opengov.nsw.gov.au/publication/12311"
  ]
}</code></pre>
<h2 id="list-all-publications-created-by-a-particular-agency">List all publications created by a particular agency</h2>
<p>https://www.opengov.nsw.gov.au/agency/Agency ID.json</p>
<p>Returns a JSON list of all publications created by a particular agency.</p>
<h2 id="list-all-agencies">List all agencies</h2>
<p>https://www.opengov.nsw.gov.au/agency.json</p>
<p>Returns a JSON list of all NSW government agencies with publications on the site.</p>
<p>Information includes agency ID, agency name, and, optionally, a link to the agency’s description in State Records NSW’s online catalogue.</p>
<h2 id="extract-metadata-for-a-particular-publication-file">Extract metadata for a particular publication file</h2>
<p>https://www.opengov.nsw.gov.au/publication/Publication File ID.json</p>
<p>Returns a JSON object describing a publication. This information comprises descriptive metadata and a list of publication files.</p>
<h2 id="extract-text-for-a-particular-publication-file">Extract text for a particular publication file</h2>
<p>https://www.opengov.nsw.gov.au/publication/Publication File ID.txt</p>
<p>Returns the extracted text contents of a publication file.</p>
<h2 id="return-the-pdf-for-a-particular-publication-file">Return the pdf for a particular publication file</h2>
<p>https://www.opengov.nsw.gov.au/publication/Publication File ID.pdf</p>
<p>Returns the publication file's pdf.</p>
</div>

<jsp:include page="jspincludes/footer.jsp"/>
</body>
</html>
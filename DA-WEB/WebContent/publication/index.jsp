<%String fileID = request.getParameter("id");
String redirectURL = "";
if (("application/json").equalsIgnoreCase(request.getHeader("accept")))
{
	if (fileID== null)
 		{redirectURL = "/pub.do?method=allPublicationJSON";}
 	else
 		{redirectURL = "/pub.do?method=publicationJSON&id="+fileID;}
 	response.sendRedirect(redirectURL);
}else if (("text/plain").equalsIgnoreCase(request.getHeader("accept")))
{
	redirectURL = "/pub.do?method=publicationText&id="+Integer.parseInt(fileID);
	response.sendRedirect(redirectURL);
}
else if (("application/pdf").equalsIgnoreCase(request.getHeader("accept")))
{
	redirectURL = "/download.do?id="+Integer.parseInt(fileID);
	response.sendRedirect(redirectURL);
}
else{
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %> 
<%@ page import="au.gov.nsw.records.digitalarchive.base.*"%>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*"%>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.awt.Desktop" %>
<%
	FileService fs = new FileServiceImpl();
	KeywordService ks = new KeywordServiceImpl();
	PublicationService ps = new PublicationServiceImpl();
	FullAgencyListService fas = new FullAgencyListServiceImpl();
	PublisherPublicationService pps = new PublisherPublicationServiceImpl();
	
	UploadedFile thisFile = fs.loadFile(Integer.parseInt(fileID));
	String filename = thisFile.getFileName().substring(0, thisFile.getFileName().length()-3).concat("js");
	String readerJSON = thisFile.getReaderJson();
	
	Publication thisPub = fs.findPubViaFile(Integer.parseInt(fileID));
	
	NYTimesJSONFileWriter NYTWriter = new NYTimesJSONFileWriter();
	String location = getServletConfig().getServletContext().getRealPath("publication") + File.separator + filename; 
	NYTWriter.createJSONFile(location, readerJSON);		

	List<Keyword> kList = ks.loadKeywordViaPublication(thisPub.getPublicationId().toString());
	List<FullAgencyList> faList = fas.loadAgencyViaPublication(thisPub.getPublicationId().toString());
	List<FullAgencyList> pList = pps.loadPublisherViaPublication(thisPub.getPublicationId().toString());
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OpenGov (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
<link rel="stylesheet" type="text/css" media="all" href="../css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="../css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="../css/menus.css"/>
<link rel="stylesheet" type="text/css" media="all" href="../css/style.css"/>

<link href="public/stylesheets/DV/components/reset.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/structure.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/ui.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/annotations.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/pages.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/ui-header.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/ui-footer.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/ui-menu.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/ui-navigation.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/ui-search.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/ui-text.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/ui-zoom.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/view-annotations.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/view-document.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/view-search.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/view-text.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/view-thumbnails.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/unsupported.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/components/minimode.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/themes/plain.css" media="screen" rel="stylesheet" type="text/css" />
<link href="public/stylesheets/DV/print.css" media="print" rel="stylesheet" type="text/css" />

<script src="public/javascripts/DV/vendor/jquery-1.5.1.js" type="text/javascript"></script>
<script src="public/javascripts/DV/vendor/jquery-ui-1.8.1.custom.min.js" type="text/javascript"></script>
<script src="public/javascripts/DV/vendor/underscore.js" type="text/javascript"></script>
<script src="public/javascripts/DV/vendor/jquery.acceptInput.js" type="text/javascript"></script>
<script src="public/javascripts/DV/vendor/jquery.placeholder.js" type="text/javascript"></script>
<script src="public/javascripts/DV/lib/initializer.js" type="text/javascript"></script>
<script src="public/javascripts/DV/lib/history.js" type="text/javascript"></script>
<script src="public/javascripts/DV/lib/annotation.js" type="text/javascript"></script>
<script src="public/javascripts/DV/lib/drag_reporter.js" type="text/javascript"></script>
<script src="public/javascripts/DV/lib/elements.js" type="text/javascript"></script>
<script src="public/javascripts/DV/lib/page.js" type="text/javascript"></script>
<script src="public/javascripts/DV/lib/page_set.js" type="text/javascript"></script>
<script src="public/javascripts/DV/lib/thumbnails.js" type="text/javascript"></script>
<script src="public/javascripts/DV/schema/schema.js" type="text/javascript"></script>
<script src="public/javascripts/DV/elements/elements.js" type="text/javascript"></script>
<script src="public/javascripts/DV/models/annotation.js" type="text/javascript"></script>
<script src="public/javascripts/DV/models/chapter.js" type="text/javascript"></script>
<script src="public/javascripts/DV/models/document.js" type="text/javascript"></script>
<script src="public/javascripts/DV/models/page.js" type="text/javascript"></script>
<script src="public/javascripts/DV/events/events.js" type="text/javascript"></script>
<script src="public/javascripts/DV/events/ViewAnnotation.js" type="text/javascript"></script>
<script src="public/javascripts/DV/events/ViewDocument.js" type="text/javascript"></script>
<script src="public/javascripts/DV/events/ViewSearch.js" type="text/javascript"></script>
<script src="public/javascripts/DV/events/ViewText.js" type="text/javascript"></script>
<script src="public/javascripts/DV/events/ViewThumbnails.js" type="text/javascript"></script>
<script src="public/javascripts/DV/events/history.js" type="text/javascript"></script>
<script src="public/javascripts/DV/events/navigation.js" type="text/javascript"></script>
<script src="public/javascripts/DV/helpers/helpers.js" type="text/javascript"></script>
<script src="public/javascripts/DV/helpers/annotations.js" type="text/javascript"></script>
<script src="public/javascripts/DV/helpers/construction.js" type="text/javascript"></script>
<script src="public/javascripts/DV/helpers/editor.js" type="text/javascript"></script>
<script src="public/javascripts/DV/helpers/navigation.js" type="text/javascript"></script>
<script src="public/javascripts/DV/helpers/search.js" type="text/javascript"></script>
<script src="public/javascripts/DV/states/states.js" type="text/javascript"></script>
<script src="public/javascripts/DV/controllers/document_viewer.js" type="text/javascript"></script>
<script src="public/javascripts/DV/controllers/api.js" type="text/javascript"></script>
<script src="public/assets/templates.js" type="text/javascript"></script>
<script type="text/javascript" language="javascript">
<!--
function displayThumb(event, UID)
{
	var evt = event ? event : (window.event ? window.event : null);
	 if (document.all) // IE
    {
        var x =   event.x ;
        var y =   event.y ;
    }
    else  // firefox
    {
        var x = evt.clientX;
        var y = evt.clientY;
    }
	window.document.getElementById("thumbDisplay").style.display = "block";
	window.document.getElementById("thumbDisplay").top = y;
	window.document.getElementById("thumbDisplay").left = x;
	window.document.getElementById("photo").src = "/thumb?uid=" + UID; 
}
function out()
{
	window.document.getElementById("thumbDisplay").style.display = "none";
}		
//-->
</script>
<style type="text/css">
<!--
.pubDwnButton {
	-webkit-box-shadow:rgba(0,0,0,0.98) 0 1px 0 0;
	-moz-box-shadow:rgba(0,0,0,0.98) 0 1px 0 0;
	box-shadow:rgba(0,0,0,0.98) 0 1px 0 0;
	background-color:#EEE;
	border-radius:0;
	-webkit-border-radius:0;
	-moz-border-radius:0;
	border:1px solid #999;
	border-radius:5px;
	color:#666;
	font-family:'Lucida Grande',Tahoma,Verdana,Arial,Sans-serif;
	font-size:11px;
	font-weight:700;
	padding:2px 6px;
	height:28px;
}
//-->
</style>
</head>

 <body class="yui-skin-sam">
      <div class="BodyWrapper">
        <div class="Header">
		<a title="Home" href="https://www.opengov.nsw.gov.au/"><img class="Logo" src="../images/logo.png" alt="logo" border="0"/>
				<img class="Logo" src="../images/OpenGov.png" alt="logo" border="0"/>
			</a>
		   <div class="loginBox">
		     <a href="../agency_login">Agency login</a>
		   </div>
		</div>
        <div class="AboveSiteContent">
  		<h2 class="Cloak">Content Actions</h2>
  		<ul class="ContentActions FooterMenu">
    		<li><a href="#" title="Print this Page" onclick="window.print(); return false;"><img class="Logo" src="../images/icon_print.gif" alt="Print this page" border="0"/></a></li>
    	</ul>
  <h2 class="Cloak">NSW Sites</h2>
  
   <%
	String selection = request.getParameter("selected");
	String selected = "class=\"selected\"";
	String search="";
	String about ="";
	String register="";
	String login = "";
	if (("search").equalsIgnoreCase(selection))
		search = selected;
	if (("about").equalsIgnoreCase(selection))
		about = selected;
	if (("login").equalsIgnoreCase(selection))
		login = selected;
%>
<ul class="Menu">
    <li <%=search%>>
      <a href="/main" title="Search">Search</a>
    </li>
    <li <%=about%>>
      <a href="/about" title="About">About</a>
    </li>
    <li>
      <a href="http://data.nsw.gov.au" target="_blank" title="Data">Data</a>
    </li>
    <li>
      <a href="http://www.sdi.nsw.gov.au/GPT9/catalog/main/home.page" target="_blank" title="Spatial">Spatial</a>
    </li>
    <li>
      <a href="http://www.records.nsw.gov.au/" target="_blank" title="State Records">State Records</a>
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
  <h1><%=thisPub.getTitle()%></h1>
</div>

 <ul class="DocumentActions"><li>
  <a href="/desktop/<%=fileID%>">
    <span><%=thisFile.getFileName()%></span>
  </a>
  <b class="DownloadMenuItemInfo">
    (size: <%=thisFile.getSize()%>)
  </b>
<a href="/download/<%=fileID%>"><span class="pubDwnButton">Download</span></a>
<%
   	if (("MB").equalsIgnoreCase(thisFile.getSize().substring(thisFile.getSize().length()-2)))
	{
   		String[] parts = thisFile.getSize().split("\\.");	   		
	   	if (Integer.parseInt(parts[0]) >= 5)
	   		out.println("Warning: File size over 5 MB");
	}
%>
</li>
</ul>

<div class="ContextInformation">

  <div class="Portlet MetadataInformation">
    <div class="Top"></div>
    <div class="Content">
      <h3>Publication Information</h3>
     <dl>
      <dt>Title</dt>
      <dd><%=thisPub.getTitle()%></dd>
      <dt>Agency</dt>
      <dd><% Iterator<FullAgencyList> aIterator = faList.iterator(); 
	  	  	 while(aIterator.hasNext())
	  	     {
	  	  		FullAgencyList fal = (FullAgencyList)aIterator.next();
			%>
	  	  		<a href="https://www.opengov.nsw.gov.au/agency/<%=fal.getFullAgencyListId()%>"><%=fal.getAgencyName()%></a><br/>
	  	  	 <%}
	  	  	%></dd>
      <dt>Type</dt>
      <dd><%=thisPub.getType()%></dd>
      <dt>Date published</dt>
      <dd><%=thisPub.getDatePublishedDisplay()%></dd>
      <dt>Published by</dt>
      <dd><% Iterator<FullAgencyList> pIterator = pList.iterator(); 
	  	  		 while(pIterator.hasNext())
	  	  		 {
	  	  			 FullAgencyList fal = (FullAgencyList)pIterator.next();
	  	  			 out.println(fal.getAgencyName() + "<br/>");
	  	  		 }
	  	  	  %></dd>
	  <dt>Keywords</dt>
      <dd><% Iterator<Keyword> kIterator = kList.iterator(); 
	  	  		 while(kIterator.hasNext())
	  	  		 {
	  	  			 Keyword keyword = (Keyword)kIterator.next();
	  	  			 out.println(keyword.getKeyword() + "&nbsp;");
	  	  		 }
	  	  	  %></dd>
      <dt>Description</dt>
      <dd><%=thisPub.getDescription()%></dd>
      <dt>Language</dt>
      <dd><%=thisPub.getLanguage()%></dd>
      <dt>Coverage</dt>
      <dd><%=thisPub.getCoverage()%></dd>
      <dt>Copyright</dt>
      <dd><%=thisPub.getRights()%></dd>
     </dl>
    </div>
    <div class="Bottom"></div>
</div>

 <div class="Portlet MetadataInformation">
    <div class="Top"></div>
    <div class="Content">
      <h3>Publication Files</h3>
     <dl> 
      <%
      	List<UploadedFile> upFileList = fs.browseFiles(thisPub);
      	Iterator<UploadedFile> upFileIterator = upFileList.iterator();
      	UploadedFile pubFile = null;
      	while(upFileIterator.hasNext())
      	{
      		pubFile = (UploadedFile)upFileIterator.next();
      		String fileOrder = pubFile.getFileOrder().toString();
      		if (fileOrder.equals(thisFile.getFileOrder().toString()))
      			fileOrder = "<span class=\"highlighter\">" + fileOrder + "</span>"; 
	 %>
      	  <a href="/publication/<%=pubFile.getFileId()%>" title="<%=pubFile.getFileName()%>" 
		     onmouseover="javascript:displayThumb(event, '<%=pubFile.getUid()%>');" onmouseout="javascript:out();" style="cursor:hand;"><%=fileOrder%></a>
		<div id="thumbDisplay" style="position:absolute;display:none;border:0px;width:82px;height:108px;">
		<table id="tipTable" border="0" bgcolor="#ffffee">
			<tr>
				<td><img id="photo" src="" width="82" height="108"/></td>
			</tr>
		</table>
		</div>
      <%
	  	}
      %>
     </dl>
    </div>
    <div class="Bottom"></div>
</div>
</div>

<div class="SiteContent">
	<div id="document-viewer"></div>
<script type="text/javascript">
<!--
  DV.load('<%=filename%>', {
    container: '#document-viewer',
    width: 640,
    height: 870,
    sidebar: false,
    search: true
  });
//-->
</script>
	<div align="right"><br/><input type="button" value="Back" onclick="history.go(-1);"/></div>
</div>
<jsp:include page="../jspincludes/footer.jsp"/>
</body>
</html>
<%
}%>
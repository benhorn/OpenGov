<%@ page import="java.util.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Publications (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="http://publications.nsw.gov.au/@@/default/favicon.png"/>
<link rel="stylesheet" type="text/css" media="all" href="../css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="../css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="../css/menus.css"/>
<link rel="stylesheet" type="text/css" media="all" href="../css/style.css"/>
</head>
    <body class="yui-skin-sam">
      <div class="BodyWrapper">
        <div class="Header">
		<a title="Home" href="http://publications.nsw.gov.au/"><img class="Logo" src="../images/logo.png" alt="logo" border="0"/></a>
		  <div class="loginBox">
		     <a href="../login">Agency login</a>
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
      <a href="../search" title="Search">Search</a>
    </li>
    <li <%=about%>>
      <a href="../about" title="About">About</a>
    </li>
    <li>
      <a href="http://data.nsw.gov.au" target="_blank" title="Data">Data</a>
    </li>
    <li>
      <a href="http://www.sdi.nsw.gov.au/GPT9/catalog/main/home.page" target="_blank" title="Spatial">Spatial</a>
    </li>
    <li>
      <a href="http://www.records.nsw.gov.au/" target="_blank "title="State Records">State Records</a>
    </li>
    <li>
      <a href="http://www.oic.nsw.gov.au/" target="_blank" title="OIC">OIC</a>
    </li>
</ul>
  <ul class="Breadcrumbs Menu">
      <li class="Breadcrumb Start">
        <a href="http://publications.nsw.gov.au/" title="publications.nsw">
          <span>publications.nsw</span>
        </a>
      </li>
  </ul>
</div>
<div class="PageTitle"></div>

<div  class="listTable">
	 <%
	 	String fullAgencyListID = request.getParameter("agency");
	 	AgencyPublicationService aps = new AgencyPublicationServiceImpl();
	 	FullAgencyList fal = new FullAgencyList();
	    fal.setFullAgencyListId(Integer.parseInt(fullAgencyListID));
	 	List<Publication> pList= aps.loadPublicationViaFullAgency(fal); 
	 	
	 	for (int i = 0; i < pList.size(); i++)
	 	{
	 %>
	  <div class="SearchResultItem">
      <a href="http://publications.nsw.gov.au/1525-office-for-children-annual-report-fy2008"
         title="<%=pList.get(i).getTitle()%>">
        <img src="/pub/561/f50/561f50caaa686b6894d56d9fa887bf1c53a14dab/thumbnail.gif" />
      </a>
      <div class="SearchResultDetail">
        <ul class="SearchResultDetailHeader">
          <li>
            <h3>
              <a href="http://publications.nsw.gov.au/1525-office-for-children-annual-report-fy2008"
                 title="<%=pList.get(i).getTitle()%>"><%=pList.get(i).getTitle()%></a>
            </h3>
          </li>
        </ul>
        <dl>
          <dt>Published:</dt>
          <dd><%=pList.get(i).getDatePublished()%></dd>
          <dt>Total files:</dt>
          <dd><%=pList.get(i).getTotalFiles()%></dd>
          <dt>Pages:</dt>
          <dd><%=pList.get(i).getTotalPages()%></dd>
          <dt>Type:</dt>
          <dd><%=pList.get(i).getType()%></dd>
        </dl>
      </div>
      <div class="ClearFloating Hline"></div>
    </div>
	 
	 <%
	 	}
	 %>
</div>

<jsp:include page="../jspincludes/footer.jsp"/>
</body></html>
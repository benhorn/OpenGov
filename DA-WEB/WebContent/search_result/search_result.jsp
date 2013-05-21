<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="au.gov.nsw.records.digitalarchive.search.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.servlet.*" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.SortedMap" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.lucene.facet.search.*" %>
<%@ page import="org.apache.lucene.facet.taxonomy.*" %>
<%@ page import="org.apache.lucene.facet.search.results.FacetResult"%>
<%@ page import="org.apache.lucene.facet.search.params.*"%>
<%@ page import="org.apache.lucene.facet.index.params.*"%>
<%@ page import="org.apache.lucene.facet.search.results.FacetResultNode"%>
<%!
public int nullIntconv(String str)
{   
    int conv=0;
    if(str==null)
    {
        str="0";
    }
    else if((str.trim()).equals("null"))
    {
        str="0";
    }
    else if(str.equals(""))
    {
        str="0";
    }
    try{
        conv=Integer.parseInt(str);
    }
    catch(Exception e)
    {
    }
    return conv;
}
public String trimDoubleQuotes(String text) {
    int textLength = text.length();

    if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
      return text.substring(1, textLength - 1);
    }

    return text;
 }
%>
<%
String text = trimDoubleQuotes(request.getParameter("query"));
text = "\""+text+"\"";
int currentYear = Calendar.getInstance().get(Calendar.YEAR);
int iTotalRows = nullIntconv(request.getParameter("iTotalRows"));
int iTotalPages = nullIntconv(request.getParameter("iTotalPages"));
int iPageNo = nullIntconv(request.getParameter("iPageNo"));
int cPageNo = nullIntconv(request.getParameter("cPageNo"));

int iStartResultNo = 0;
int iEndResultNo = 0;
int iShowRows = 25;  // Number of records show on per page
int iTotalSearchRecords = 15;  // Number of pages index shown
int pageNumber;

if(iPageNo == 0 || iPageNo == 1)
{
	iPageNo = 1;
	pageNumber = 1;
}else
{
	pageNumber = iPageNo;
	iPageNo=Math.abs((iPageNo-1) * iShowRows);
}
OpenGovLucene ogl = new OpenGovLucene();
FacetIndexingParams indexingParams = new DefaultFacetIndexingParams();
FacetSearchParams facetSearchParams = new FacetSearchParams(indexingParams);
facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("agency"), 10));
facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("type"), 10));
facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("publication_year"), currentYear-1800));
OpenGovSearchResult results = null;

String agency = request.getParameter("agency"); if (StringUtils.isEmpty(agency) || ("null").equals(agency)){agency="";}
String type =  request.getParameter("type"); if (StringUtils.isEmpty(type) || ("null").equals(type)){type="";}
String startYear = request.getParameter("from"); if (StringUtils.isEmpty(startYear) || ("null").equals(startYear)){startYear="";}
String endYear = request.getParameter("to"); if (StringUtils.isEmpty(endYear) || ("null").equals(endYear)){endYear="";}
String thisURL = request.getRequestURL().toString();
String condition = request.getParameter("condition");if (StringUtils.isEmpty(condition) || ("null").equals(condition)){condition = "relevance";}
LuceneSearchParams params = new LuceneSearchParams(text, facetSearchParams, agency, type, startYear, endYear, pageNumber, iShowRows);
results = ogl.search(params, condition);
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OpenGov (NSW)</title>
<meta http-equiv="X-UA-Compatible" content="IE=9"/>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
<link rel="stylesheet" type="text/css" media="all" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/menus.css"/>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<link rel="stylesheet" type="text/css" href="css/modal.css"/>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>  
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="js/script.js"></script> 
<script type="text/javascript">
<!--
function sortBy(condition)
{
 	var searchText = "/search?query="+encodeURIComponent("<%=trimDoubleQuotes(text)%>")+"&agency="+encodeURIComponent("<%=agency%>")+"&type="+encodeURIComponent("<%=type%>")+"&from="+encodeURIComponent("<%=startYear%>")+"&to="+encodeURIComponent("<%=endYear%>");
	if (condition == "Relevance")
	{  searchText = searchText + "&condition=relevance";
	}
	else
	{	searchText = searchText + "&condition=year";
	}
	$("body").load(searchText);
}
//-->
</script>
</head>
<body class="yui-skin-sam">
<div class="BodyWrapper" id="BWrapper">
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
      <a href="main" title="Search">Search</a>
    </li>
    <li <%=about%>>
      <a href="about" title="About">About</a>
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

<jsp:include page="../jspincludes/search.jsp" />
<div class="PageTitle">
  <h1>Search Results for <%=text%></h1>
</div>

<div class="ContextInformation">
 <div class="Portlet MetadataInformation">
    <div class="Top"></div>
    <div class="Content">
    <% 
    Iterator<FacetResultItem> facetIterator = results.getFacets().iterator();
	SortedMap yearList = new TreeMap();
	String label = "";
	while(facetIterator.hasNext())
	{
		FacetResultItem fResult = (FacetResultItem)facetIterator.next();
		if (("publication_year").equalsIgnoreCase(fResult.getLabel()))
			label = "year";
		else
			label = fResult.getLabel();
		%>
		<h3>Refine by <%=label%></h3>
		<% 
		Iterator<FacetResultItem> labelIterator = fResult.getItems().iterator();
		while(labelIterator.hasNext())
		{
			FacetResultItem lResult = (FacetResultItem)labelIterator.next();
			if (("publication_year").equalsIgnoreCase(fResult.getLabel()))
			{yearList.put(lResult.getLabel(), lResult.getCount());
			 continue;
			}
			if(StringUtils.isEmpty(agency) && StringUtils.isEmpty(type) && 
					(StringUtils.isEmpty(startYear) || ("null").equals(startYear))
					&& (StringUtils.isEmpty(endYear) || ("null").equals(endYear)))
			{
	%><a id="facetlink" href="/search?query=<%=trimDoubleQuotes(text)%>&<%=fResult.getLabel()%>=<%=lResult.getLabel()%>"><%=lResult.getLabel()%></a>&nbsp;<strong>[<%=lResult.getCount()%>]</strong><br><br>
	<%		
			}
			
			else{
			if (("agency").equalsIgnoreCase(fResult.getLabel()) && (StringUtils.isEmpty(type) || ("null").equals(type))
																&& (StringUtils.isEmpty(startYear) || ("null").equals(startYear))
																&& (StringUtils.isEmpty(endYear) || ("null").equals(endYear)))
			{
	%><a id="facetlink" href="/search?query=<%=trimDoubleQuotes(text)%>&agency=<%=agency%>"><%=lResult.getLabel()%></a>&nbsp;<strong>[<%=lResult.getCount()%>]</strong><br/><br/>
	<%
			}
			else if (("agency").equalsIgnoreCase(fResult.getLabel()) && (StringUtils.isEmpty(startYear) || ("null").equals(startYear))
																	 && (StringUtils.isEmpty(endYear) || ("null").equals(endYear)))
			{
	%><a id="facetlink" href="/search?query=<%=trimDoubleQuotes(text)%>&agency=<%=lResult.getLabel()%>&type=<%=type%>"><%=lResult.getLabel()%></a>&nbsp;<strong>[<%=lResult.getCount()%>]</strong><br/><br/>
	<%
			}else if (("agency").equalsIgnoreCase(fResult.getLabel()) && (StringUtils.isEmpty(type) || ("null").equals(type)))
			{
	%><a id="facetlink" href="/search?query=<%=trimDoubleQuotes(text)%>&agency=<%=lResult.getLabel()%>&from=<%=startYear%>&to=<%=endYear%>"><%=lResult.getLabel()%></a>&nbsp;<strong>[<%=lResult.getCount()%>]</strong><br/><br/>
	<%
			}
			if (("type").equalsIgnoreCase(fResult.getLabel()) && (StringUtils.isEmpty(agency) || ("null").equals(agency))
															  && (StringUtils.isEmpty(startYear) || ("null").equals(startYear))
															  && (StringUtils.isEmpty(endYear) || ("null").equals(endYear)))
			{
	%><a id="facetlink" href="/search?query=<%=trimDoubleQuotes(text)%>&type=<%=lResult.getLabel()%>"><%=lResult.getLabel()%></a>&nbsp;<strong>[<%=lResult.getCount()%>]</strong><br/><br/>	
	<%
			}
			else if (("type").equalsIgnoreCase(fResult.getLabel()) && (StringUtils.isEmpty(startYear) || ("null").equals(startYear))
																   && (StringUtils.isEmpty(endYear) || ("null").equals(endYear)))
			{
	%><a id="facetlink" href="/search?query=<%=trimDoubleQuotes(text)%>&type=<%=lResult.getLabel()%>&agency=<%=agency%>"><%=lResult.getLabel()%></a>&nbsp;<strong>[<%=lResult.getCount()%>]</strong><br/><br/>
	<%
			}
			else if(("type").equalsIgnoreCase(fResult.getLabel()) && (StringUtils.isEmpty(agency) || ("null").equals(agency)))
			{
	%><a id="facetlink" href="/search?query=<%=trimDoubleQuotes(text)%>&type=<%=lResult.getLabel()%>&from=<%=startYear%>&to=<%=endYear%>"><%=lResult.getLabel()%></a>&nbsp;<strong>[<%=lResult.getCount()%>]</strong><br/><br/>	
	<%		}
		}}}
	%>
	<div id="box1"></div>
<%
	if (yearList.size() != 0)
	{
%>
<div id="rangeslider">
<p id="daterange"></p><div id="slider-range"></div>
<script type="text/javascript">
<!--
$(function() {
        $( "#slider-range" ).slider({
            range: true,
            min: 1800,
            max: <%=currentYear%>,
            step: 1,
			<% if ((StringUtils.isEmpty(startYear) || 
			   	   ("null").equals(startYear)) && (StringUtils.isEmpty(endYear) || 
			   	   ("null").equals(endYear))){%>
              	   values: [ 1800, <%=currentYear%> ],
            <%}else{%>
				   values: [<%=startYear.substring(0,4)%>,<%=endYear.substring(0,4)%>],
			<%}%>
			slide: function( event, ui ) {
            	if (ui.values[1]==<%=currentYear%>){
            	   $("#daterange").text(ui.values[ 0 ] + "+ " + " (loading...)"  );
            	}else{
                $("#daterange").text(ui.values[ 0 ] + " to " + ui.values[ 1 ] + " (loading...)");
            	}
            	 delayURLNavigation('yearslider', ui.values);
            }
        });
        if($( "#slider-range" ).slider( "values", 1 )==<%=currentYear%>){
        	$( "#daterange" ).text($( "#slider-range" ).slider( "values", 0 ) + "+ " + " (" + <%=ogl.getNumTotalHits()%> + ")" );
        }else{
        	$( "#daterange" ).text($( "#slider-range" ).slider( "values", 0 ) + " to " + $( "#slider-range" ).slider( "values", 1 ) + "+ " + " (" + <%=ogl.getNumTotalHits()%> + ")" );	
        }
        
    });
    
    var timers = {};
    function delayURLNavigation(type, values) {
      clearTimeout(timers[type]);
      timers[type] = setTimeout(function() {
    	 window.location = '/search?query=' + encodeURIComponent("<%=trimDoubleQuotes(text)%>") +"&agency="+encodeURIComponent("<%=agency%>")+"&type="+encodeURIComponent("<%=type%>")+ '&from=' + values[0] + '0101&to=' + values[1] + '1230';  
      }, 2000);
    }
//-->
</script>
</div>
<%
}
%>
    </div>

	<div class="Bottom"></div>
</div>
</div>

<div class="SiteContent" id="contentArea">
<div align="center">
<%	
	iTotalRows = ogl.getNumTotalHits();
	FileService fs = new FileServiceImpl();
	MemberService ms = new MemberServiceImpl();
	FullAgencyListService fls = new FullAgencyListServiceImpl();
	
	 int i=0;
     int cPage=0;
     int currentPage=0;
	
try{
	    if( iTotalRows < (iPageNo+iShowRows))
	    {
	        iEndResultNo = iTotalRows;
	    }
	    else
	    {
	        iEndResultNo = (iPageNo+iShowRows);
	    }
	   
	    iStartResultNo=(iPageNo+1);
	    iTotalPages=((int)(Math.ceil((double)iTotalRows/iShowRows)));
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
%>
<%
Set<Map.Entry<String, String>> resultEntities = results.getResults().entrySet();
if (resultEntities == null || resultEntities.size() < 1)
{
%>
Your search returned no results.
</div>
<a name="top"></a>
<%	
}else
{   
     if(iTotalRows != 0)
     {
      cPage=((int)(Math.ceil((double)iEndResultNo/(iTotalSearchRecords*iShowRows))));
      currentPage = iPageNo/iShowRows + 1;
		
      int prePageNo=(cPage*iTotalSearchRecords)-((iTotalSearchRecords-1)+iTotalSearchRecords);
      
      if((cPage*iTotalSearchRecords)-(iTotalSearchRecords) > 0)
      {
       %>
        <a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=prePageNo%>&cPageNo=<%=prePageNo%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>">Previous 15</a>&nbsp;|
       <%
      }
		if(currentPage != 1){
		%>
		<a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=currentPage-1%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>"><<</a>&nbsp;
      <%}
		
		for(i=((cPage*iTotalSearchRecords)-(iTotalSearchRecords-1)); i<=(cPage*iTotalSearchRecords); i++)
      {
        if(i==((iPageNo/iShowRows)+1))
        {
        %>
         <a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=i%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>" style="cursor:pointer;color:white;background-color:red;border-style:double;"><b><%=i%></b></a>&nbsp;
        <%
        }
        else if(i<=iTotalPages)
        {
        %>
         <a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=i%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>"><%=i%></a>&nbsp;
        <% 
        }
      }
		if(currentPage < iTotalPages){
		%>
		<a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=currentPage+1%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>">>></a>&nbsp;
      <%}
		
		if(iTotalPages > iTotalSearchRecords && i < iTotalPages)
      {
       %>
       |&nbsp;<a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=i%>&cPageNo=<%=i%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>">Next 15</a> 
       <%
      }
     }
    %><br/>
<b>Page <%=currentPage%>, Displaying <%=iStartResultNo-1%> - <%=iEndResultNo-1%>, Total <%=iTotalRows%></b><br/>
<b>Sort by:<input type="radio" name="sortOption" value="relevance" onClick="javascript:sortBy('Relevance');" <%if (("relevance").equals(condition)){%>checked="true"<%}%>>Relevance</input><input type="radio" name="sortOption" value="year" onClick="javascript:sortBy('Year');" <%if (("year").equals(condition)){%>checked="true"<%}%>>Date</input>
</b>
</div>
<%
if (("year").equals(condition))
{
    String[] keys = new String[results.getYearToFileID().size()];
	results.getYearToFileID().keySet().toArray(keys);
	results.getExtractedParagraph().keySet().toArray(keys);
	Arrays.sort(keys, Collections.reverseOrder());
	for (String key:keys) 
	{  
		UploadedFile uFile = fs.loadFile(Integer.parseInt(results.getYearToFileID().get(key)));
		Publication pub = fs.findPubViaFile(Integer.parseInt(results.getYearToFileID().get(key)));
%>
<div class="SearchResultItem">
    	<a href="/publication/<%=uFile.getFileId()%>" title="<%=uFile.getFileName()%>">
         <img src="/thumb?uid=<%=uFile.getUid()%>"/>
        </a>
      <div class="SearchResultDetail">
        <ul class="SearchResultDetailHeader">
          <li>
            <h3>
             <a href="/publication/<%=uFile.getFileId()%>" title="<%=uFile.getFileName()%>">
             <%=pub.getTitle()%></a>&nbsp;&nbsp;
             <%List<FullAgencyList> faList = fls.loadAgencyViaPublication(pub.getPublicationId().toString());%> 
              <small>Agency: <% Iterator<FullAgencyList> aIterator = faList.iterator(); 
	  	  	 while(aIterator.hasNext())
	  	     {
	  	  		FullAgencyList fList = (FullAgencyList)aIterator.next();
	  	  	%>
	  	  	<a href="https://www.opengov.nsw.gov.au/agency/<%=fList.getFullAgencyListId()%>" target="_blank" class="resultAgency"><%=fList.getAgencyName()%></a><br/>
	  	  	<%	
	  	  	 }
	  	  	%></small>
            </h3>
          </li>
        </ul>
        <p class="pagetext"><%=results.getExtractedParagraph().get(key)%></p>
      </div>
      <div class="Hline"></div>
    </div>
<%
}}else{
for(Map.Entry<String, String> resultEntity : resultEntities ) 
{
        String key = resultEntity.getKey();
        String value = resultEntity.getValue();
        if (key != null)
        {
    		UploadedFile uFile = fs.loadFile(Integer.parseInt(key));
			Publication pub = fs.findPubViaFile(Integer.parseInt(key));		

%>
<div class="SearchResultItem">
    	<a href="/publication/<%=uFile.getFileId()%>" title="<%=uFile.getFileName()%>">
         <img src="/thumb?uid=<%=uFile.getUid()%>"/>
        </a>
      <div class="SearchResultDetail">
        <ul class="SearchResultDetailHeader">
          <li>
            <h3>
             <a href="/publication/<%=uFile.getFileId()%>" title="<%=uFile.getFileName()%>">
             <%=pub.getTitle()%></a>&nbsp;&nbsp;
             <%List<FullAgencyList> faList = fls.loadAgencyViaPublication(pub.getPublicationId().toString());%> 
              <small>Agency: <% Iterator<FullAgencyList> aIterator = faList.iterator(); 
	  	  	 while(aIterator.hasNext())
	  	     {
	  	  		FullAgencyList fList = (FullAgencyList)aIterator.next();
	  	  	%>
	  	  	<a href="https://www.opengov.nsw.gov.au/agency/<%=fList.getFullAgencyListId()%>" target="_blank" class="resultAgency"><%=fList.getAgencyName()%></a><br/>
	  	  	<%	
	  	  	 }
	  	  	%></small>
            </h3>
          </li>
        </ul>
        <p class="pagetext"><%=value%></p>
      </div>
      <div class="Hline"></div>
    </div>
<%}}}}%>
 <div align="center">
 <%
      if(iTotalRows != 0)
     {
      cPage=((int)(Math.ceil((double)iEndResultNo/(iTotalSearchRecords*iShowRows))));
      currentPage = iPageNo/iShowRows + 1;
		
      int prePageNo=(cPage*iTotalSearchRecords)-((iTotalSearchRecords-1)+iTotalSearchRecords);
      
      if((cPage*iTotalSearchRecords)-(iTotalSearchRecords) > 0)
      {
       %>
        <a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=prePageNo%>&cPageNo=<%=prePageNo%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>">Previous 15</a>&nbsp;|
       <%
      }
		if(currentPage != 1){
		%>
		<a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=currentPage-1%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>"><<</a>&nbsp;
      <%}
		
		for(i=((cPage*iTotalSearchRecords)-(iTotalSearchRecords-1)); i<=(cPage*iTotalSearchRecords); i++)
      {
        if(i==((iPageNo/iShowRows)+1))
        {
        %>
         <a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=i%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>" style="cursor:pointer;color:white;background-color:red;border-style:double;"><b><%=i%></b></a>&nbsp;
        <%
        }
        else if(i<=iTotalPages)
        {
        %>
         <a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=i%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>"><%=i%></a>&nbsp;
        <% 
        }
      }
		if(currentPage < iTotalPages){
		%>
		<a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=currentPage+1%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>">>></a>&nbsp;
      <%}
		if(iTotalPages > iTotalSearchRecords && i < iTotalPages)
      {
       %>
       |&nbsp;<a id="navlink" href="/search?query=<%=trimDoubleQuotes(text)%>&iPageNo=<%=i%>&cPageNo=<%=i%>&agency=<%=request.getParameter("agency")%>&type=<%=request.getParameter("type")%>&from=<%=request.getParameter("from")%>&to=<%=request.getParameter("to")%>&condition=<%=request.getParameter("condition")%>">Next 15</a> 
       <%
      }
     }
    %><br/>
<b>Page <%=currentPage%>, Displaying <%=iStartResultNo-1%> - <%=iEndResultNo-1%>, Total <%=iTotalRows%></b>
</div>
<div align="center">
<p>
 <a href="#top" style="cursor:pointer;color:white;background-color:red;border-style:double;">BACK TO TOP</a>
 </p>
</div>
</div>
<jsp:include page="../jspincludes/footer.jsp"/>
</body>
</html>

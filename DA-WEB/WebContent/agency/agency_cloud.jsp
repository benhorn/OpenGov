<%String fullAgencyListID = request.getParameter("agency"); %>
<%if (("application/json").equalsIgnoreCase(request.getHeader("accept")))
{String redirectURL = "/pub.do?method=agencyJSON&agency="+fullAgencyListID;
 response.sendRedirect(redirectURL);}
else{
%>
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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.Constants" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="gov.loc.repository.pairtree.Pairtree" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.File" %>
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
</head>
    <body class="yui-skin-sam">
      <div class="BodyWrapper">
        <div class="Header">
		<a title="Home" href="https://www.opengov.nsw.gov.au/"><img class="Logo" src="../images/logo.png" alt="logo" border="0"/>
		<img class="Logo" src="../images/OpenGov.png" alt="logo" border="0"/>
		</a>
		  <div class="loginBox">
		     <a href="../agency_login" title="login">Agency login</a>
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
      <a href="../main" title="Search">Search</a>
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
      <a href="http://www.ipc.nsw.gov.au/" target="_blank" title="IPC">IPC</a>
    </li>
</ul>
  <ul class="Breadcrumbs Menu">
      <li class="Breadcrumb Start">
      </li>
  </ul>
</div>
<%
	

	int iShowRows=7;  // Number of records show on per page
	int iTotalSearchRecords=15;  // Number of pages index shown
	
	int iTotalRows = nullIntconv(request.getParameter("iTotalRows"));
    int iTotalPages = nullIntconv(request.getParameter("iTotalPages"));
    int iPageNo = nullIntconv(request.getParameter("iPageNo"));
    int cPageNo = nullIntconv(request.getParameter("cPageNo"));
	
	int iStartResultNo = 0;
    int iEndResultNo = 0;
    
    if(iPageNo == 0)
    {
        iPageNo=0;
    }
    else
    {
        iPageNo=Math.abs((iPageNo-1) * iShowRows);
    }
	
	FileService fs = new FileServiceImpl();
	MemberService ms = new MemberServiceImpl();
	FullAgencyListService fas = new FullAgencyListServiceImpl();
	AgencyPublicationService aps = new AgencyPublicationServiceImpl();

	FullAgencyList thisAgency = fas.loadFullAgencyList(Integer.parseInt(fullAgencyListID));
	FullAgencyList fal = new FullAgencyList();
	fal.setFullAgencyListId(Integer.parseInt(fullAgencyListID));
	List<Publication> pList= aps.loadPublicationViaFullAgency(iPageNo, iShowRows, fal);
	iTotalRows = aps.countRow(fullAgencyListID);
	Iterator<Publication> it = pList.iterator();
%>
<%
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
<div class="PageTitle">
<h1><%=thisAgency.getAgencyName()%></h1>
</div>
<div class="listTable" align="right">
<%
       int i=0;
       int cPage=0;
       int currentPage=0;
	   
	   if(iTotalRows != 0)
       {
        cPage=((int)(Math.ceil((double)iEndResultNo/(iTotalSearchRecords*iShowRows))));
        currentPage = iPageNo/iShowRows + 1;
		
        int prePageNo=(cPage*iTotalSearchRecords)-((iTotalSearchRecords-1)+iTotalSearchRecords);
        
        if((cPage*iTotalSearchRecords)-(iTotalSearchRecords) > 0)
        {
         %>
          <a href="/agency/<%=fullAgencyListID%>?iPageNo=<%=prePageNo%>&cPageNo=<%=prePageNo%>">Previous 15</a>&nbsp;|
         <%
        }
        
		if(currentPage != 1){
		%>
		     <a href="/agency/<%=fullAgencyListID%>?iPageNo=<%=currentPage-1%>"><<</a>&nbsp;
        <%}
		
		for(i=((cPage*iTotalSearchRecords)-(iTotalSearchRecords-1)); i<=(cPage*iTotalSearchRecords); i++)
        {
          if(i==((iPageNo/iShowRows)+1))
          {
          %>
           <a href="/agency/<%=fullAgencyListID%>?iPageNo=<%=i%>" style="cursor:pointer;color:white;background-color:red;border-style:double;"><b><%=i%></b></a>&nbsp;
          <%
          }
          else if(i<=iTotalPages)
          {
          %>
           <a href="/agency/<%=fullAgencyListID%>?iPageNo=<%=i%>"><%=i%></a>&nbsp;
          <% 
          }
        }
		
		if(currentPage < iTotalPages){
		%>
		     <a href="/agency/<%=fullAgencyListID%>?iPageNo=<%=currentPage+1%>">>></a>&nbsp;
        <%}
		
		if(iTotalPages > iTotalSearchRecords && i < iTotalPages)
        {
         %>
         |&nbsp;<a href="/agency/<%=fullAgencyListID%>?iPageNo=<%=i%>&cPageNo=<%=i%>">Next 15</a> 
         <%
        }
       }
      %><br/>
<b>Page <%=currentPage%>, Displaying <%=iStartResultNo%> - <%=iEndResultNo%>, Total <%=iTotalRows%></b>

</div>
<div  class="listTable">
	 <%
	 	while(it.hasNext())
	 	{
	 		Publication pub = (Publication)it.next();
	 		UploadedFile uFile = fs.loadFileViaOrder(1, pub.getPublicationId());
			List<FullAgencyList> faList = fas.loadAgencyViaPublication(pub.getPublicationId().toString());
			Pairtree pt = new Pairtree();
			String thumbName = Constants.PAIRTREE_ROOT + pt.mapToPPath(uFile.getUid()) + File.separator + "obj" + File.separator + "thumbnail.gif";
	 %>
	  <div class="SearchResultItem">
      <a href="/publication/<%=uFile.getFileId()%>"
         title="<%=pub.getTitle()%>">
          <%if (new File(thumbName).exists()) {%>
        <img src="/thumb?uid=<%=uFile.getUid()%>" />
         <%} %>
      </a>
      <div class="SearchResultDetail">
        <ul class="SearchResultDetailHeader">
          <li>
            <h3>
              <a href="/publication/<%=uFile.getFileId()%>"
                 title="<%=pub.getTitle()%>"><%=pub.getTitle()%></a>
            </h3>
          </li>
        </ul>
        <dl>
          <dt>Agency:</dt>
          <dd><% Iterator<FullAgencyList> aIterator = faList.iterator(); 
	  	  	 while(aIterator.hasNext())
	  	     {
	  	  		FullAgencyList fList = (FullAgencyList)aIterator.next();
	  	  		out.println(fList.getAgencyName());
	  	  	 }
	  	  	%></dd>
          <dt>Published:</dt>
          <dd><%=pub.getDatePublishedDisplay()%></dd>
          <dt>Pages:</dt>
          <dd><%=pub.getTotalPages()%></dd>
          <dt>Type:</dt>
          <dd class="End"><%=pub.getType()%></dd>
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
<%}
%>
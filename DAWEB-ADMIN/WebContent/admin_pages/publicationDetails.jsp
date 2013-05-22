<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="java.util.*"%>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ taglib uri="/struts-bean" prefix="bean"%> 
<%@ taglib uri="/struts-html" prefix="html"%> 
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% 
	Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");
	if (archivist != null)
	{	
		Publication currentPub = (Publication)request.getSession().getAttribute("currentPub");
		MemberService ms = new MemberServiceImpl();
		Member member = ms.loadMember(currentPub.getMember().getMemberId());
		FileService fs = new FileServiceImpl();
		KeywordService ks = new KeywordServiceImpl();
		FullAgencyListService fas = new FullAgencyListServiceImpl();
		PublisherPublicationService pps = new PublisherPublicationServiceImpl();
		List<UploadedFile> list = fs.browseFiles(currentPub);
		String rejectURL= new String("window.location.href='pub.do?method=rejectPublication&id=") + currentPub.getPublicationId() + "'";
		String publishURL= new String("window.location.href='pub.do?method=publishPublication&id=") + currentPub.getPublicationId() + "'";
		String submittedPubList= new String("window.location.href='pub.do?method=submittedPublication'");
		String metadataURL= new String("window.location.href='pub.do?method=metadataDetails&id=") + currentPub.getPublicationId() + "'";
		request.setAttribute("fileList", list);
%>
<% 
		List<Keyword> kList = ks.loadKeywordViaPublication(currentPub.getPublicationId().toString());
		List<FullAgencyList> faList = fas.loadAgencyViaPublication(currentPub.getPublicationId().toString());
		List<FullAgencyList> pList = pps.loadPublisherViaPublication(currentPub.getPublicationId().toString());
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">	
<head>
<title></title>
<script type="text/javascript" src="scripts/jquery-1.7.1.js"></script>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
<link rel="stylesheet" type="text/css" href="css/modal.css" />
<style type="text/css">
<!--
body {
	background-color: lightgrey;
}
-->
</style>
<script type="text/javascript">
<!--
 $(document).ready(function ()
 {
     $("#btnShowSimple").click(function (e)
     {
         ShowDialog(false);
         e.preventDefault();
     });

     $("#btnShowModal").click(function (e)
     {
         ShowDialog(true);
         e.preventDefault();
     });

     $("#btnClose").click(function (e)
     {
         HideDialog();
         e.preventDefault();
     });

     
 });
function ShowDialog(modal)
 {
     $("#overlay").show();
     $("#dialog").fadeIn(300);

     if (modal)
     {
         $("#overlay").unbind("click");
     }
     else
     {
         $("#overlay").click(function (e)
         {
             HideDialog();
         });
     }
 }

function HideDialog()
 {
     $("#overlay").hide();
     $("#dialog").fadeOut(300);
 } 
//-->  
</script>
</head>  
  <body>
  <form id="rejectionform" action="pub.do?method=rejectPublication&id=<%=currentPub.getPublicationId()%>" method="post" name="rejectionform">
    <div id="output"></div>
    <div id="overlay" class="web_dialog_overlay"></div>
    <div id="dialog" class="web_dialog">
        <table cellpadding="3" cellspacing="0" width="100%">
            <tr>
                <td class="web_dialog_title">Reject a publication</td>
                <td class="web_dialog_title align_right">
                    <a href="#" id="btnClose">Close</a>
                </td>
            </tr>
            <tr>
                <td width="30%"><b>Agency contact email:</b></td>
                <td width="70%"><input type="text" name="email" value="<%=member.getEmail()%>" size="30"/></td>
            </tr>
            <tr>
                <td><b>Reason for rejecting:</b></td>
                <td>
                    <select name="reason">
                    	<option value="Does not conform OpenGov publication policy">Does not conform OpenGov publication policy</option>
                    	<option value="Not related to NSW government">Not related to NSW government</option>
                    	<option value="Is incomplete">Is incomplete</option>
                    </select>
                </td>
            </tr>
            <tr>
               <td valign="top"><b>Text:</b></td>
               <td><textarea rows="7" cols="35" name="textbody"></textarea></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: right;">
                    <input id="btnSubmit" type="submit" value="Submit" />
                </td>
            </tr>
        </table>
    </div>
    </form>

	<table border="1" align="center" cellpadding="5" cellspacing="5" style="background-color:lightgrey; border:0px;">
	  <tr>
		<td align="center" colspan="2"><h1>Displaying '${currentPub.title}'</h1></td>
	  </tr>
	  <tr><td>Title:</td><td>${currentPub.title}</td></tr>
	  <tr><td>Description:</td><td>${currentPub.description}</td></tr>
	  <tr><td>Keywords:</td>
	      <td><% Iterator<Keyword> kIterator = kList.iterator(); 
	  	  while(kIterator.hasNext())
	  	  {
	  	 	 Keyword keyword = (Keyword)kIterator.next();
	  	 	 out.println("'" + keyword.getKeyword() + "'" + "&nbsp;");
	  	  }
	      %></td>
	  </tr>
	  <tr><td>Agency:</td>
	  	  <td><% Iterator<FullAgencyList> aIterator = faList.iterator(); 
	  	  		 while(aIterator.hasNext())
	  	  		 {
	  	  			 FullAgencyList fal = (FullAgencyList)aIterator.next();
	  	  			 out.println(fal.getAgencyName() + "<br/><br/>");
	  	  		 }
	  	  	  %></td>
	  </tr>
	  <tr><td>Date published:</td><td>${currentPub.datePublishedDisplay}</td></tr>
	  <tr><td>Type:</td><td>${currentPub.type}</td></tr>
	  <tr><td>Type Other:</td><td>${currentPub.typeOther}</td></tr>
	  <tr><td>Coverage:</td><td>${currentPub.coverage}</td></tr>
	  <tr><td>Language:</td><td>${currentPub.language}</td></tr>
	  <tr><td>Publisher:</td>
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
	  <tr><td>Uploaded by:</td><td><%=member.getFirstname()%>&nbsp;<%=member.getLastname()%>&nbsp;(<%	 
	  		 FullAgencyList fls = fas.loadFullAgencyList(member.getFullAgencyList().getFullAgencyListId());
			 out.println(fls.getAgencyName().trim());
%>)</td></tr>
	  <tr><td>Date uploaded:</td><td>${currentPub.dateRegistered}</td></tr>
  	</table>
  	<hr/>
  	<table border="0" align="center" cellpadding="5" cellspacing="5" style="background-color:lightgrey; border:0px;">
	  <tr><td>
  		<display:table name="fileList" id="UploadedFile" pagesize="15" class="displaytag" requestURI="pub.do?method=uploadFiles">
		<display:column property="cmsDownloadLink" title="File name" headerClass="sortable" style="text-align:left;"/>
		<display:column property="dateUploaded" title="Date uploaded" headerClass="sortable" style="text-align:center;"/>
		<display:column property="pages" title="Pages" style="text-align:center;"/>
		<display:column property="size" title="Size" style="text-align:center;"/>
		<display:column property="uid" title="UID" style="text-align:center;"/>
		<display:column property="ipAddress" title="IP Address" style="text-align:center;"/>
		<display:column property="fileOrder" title="Order" style="text-align:center;"/>
		<display:column property="contentType" title="Content type" headerClass="sortable" style="text-align:center;"/>
		<display:column title="Option" media="html">
			<html:link page="/pub.do?method=deleteFile" 
			   	   paramId="id" 
				   paramName="UploadedFile"
				   onclick="return confirm('Are you sure you want to delete this file? (This operation cannot be undone)');"
				   paramProperty="fileId"><span>Delete</span>
			</html:link>
		</display:column>
		</display:table>
		</td></tr>
	</table>
	<hr/>
  	<table border="0" align="center" cellpadding="5" cellspacing="5" style="background-color:lightgrey; border:0px;">
	<tr>
		<td><input type="button" value="Back to Main" onClick="<%=submittedPubList%>"/></td>
		<td><input type="button" value="Edit Metadata" onClick="<%=metadataURL%>"/></td>
		<td><input type="button" id="btnShowModal" value="Reject"/></td>
		<td><input type="button" id="publish" onClick="<%=publishURL%>" value="Publish"/></td>
	</tr>	
	</table>		
  </body>
</html>
<%
}
%>
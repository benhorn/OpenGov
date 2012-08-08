<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%  
	Member member = (Member)request.getSession().getAttribute("member");
	if (member != null)
	{	
		String id = request.getParameter("id");
		PublicationService ps = new PublicationServiceImpl();
		Publication publication = ps.loadPublication(Integer.parseInt(id));
		FileService fs = new FileServiceImpl();
		List<UploadedFile> list = fs.browseFiles(publication);
		request.setAttribute("fileList", list);
%>	
<display:table name="fileList" id="UploadedFile" pagesize="15" class="displaytag" requestURI="/DA-WEB/pub.do?method=uploadFiles">
				<display:column property="downloadLink" title="File name" headerClass="sortable" style="text-align:left;"/>
				<display:column property="dateUploaded" title="Date uploaded" headerClass="sortable" style="text-align:center;"/>
				<display:column property="size" title="Size" style="text-align:center;"/>
				<display:column property="contentType" title="Content type" headerClass="sortable" style="text-align:center;"/>
				<display:column title="Options" media="html" class="buttonwrapper" style="text-align:left;">
					<html:link page="/pub.do?method=deleteFile" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   onclick="return confirm('Are you sure to delete this file? (This operation cannot be undone)');"
							   paramProperty="fileId"><span>Delete</span>
					</html:link>
				</display:column>
</display:table>			
<%
  }
%>		
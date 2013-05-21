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
		String maxOrder = Integer.toString(fs.maxFileOrder(publication.getPublicationId().toString()));
		String minOrder = Integer.toString(fs.minFileOrder(publication.getPublicationId().toString()));
%>	
<display:table name="fileList" id="UploadedFile" pagesize="10" class="displaytag" requestURI="/pub.do?method=publicationDetails">
				<display:column property="downloadLink" title="File name" headerClass="sortable" style="text-align:left;"/>
				<display:column property="dateUploaded" title="Date uploaded" headerClass="sortable" style="text-align:center;"/>
				<display:column property="size" title="Size" style="text-align:center;"/>
				<display:column property="fileOrder" title="Order" style="text-align:center;"/>
				<% 
				if ( ("draft").equalsIgnoreCase(publication.getStatus()) && publication.getTotalFiles() !=null)
				{	
					if (Integer.parseInt(publication.getTotalFiles()) > 1)
					{
				%>
				<display:column title="  " media="html">
					<logic:equal name="UploadedFile" property="fileOrder" value="<%=minOrder%>">
						<html:link page="/file.do?method=moveDown&pageType=edit" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   paramProperty="fileId"><img src="images/down.gif" alt="Down"/></html:link>
					</logic:equal>
					
					<logic:notEqual name="UploadedFile" property="fileOrder" value="<%=maxOrder%>">
						<logic:notEqual name="UploadedFile" property="fileOrder" value="<%=minOrder%>">
						<html:link page="/file.do?method=moveUp&pageType=edit" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   paramProperty="fileId"><img src="images/up.gif" alt="Up"/></html:link>&nbsp;
					    <html:link page="/file.do?method=moveDown&pageType=edit" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   paramProperty="fileId"><img src="images/down.gif" alt="Down"/></html:link>
						</logic:notEqual>
					</logic:notEqual>
					<logic:equal name="UploadedFile" property="fileOrder" value="<%=maxOrder%>">
						<html:link page="/file.do?method=moveUp&pageType=edit"
						   	   paramId="id" 
							   paramName="UploadedFile"
							   paramProperty="fileId"><img src="images/up.gif" alt="Down"/></html:link>
					</logic:equal>					
				</display:column>
				<%
				}
				%>
				<display:column title="Option" media="html">
					<html:link page="/pub.do?method=deleteFile&pageType=edit" 
						   	   paramId="id" 
							   paramName="UploadedFile"
							   onclick="return confirm('Are you sure you want to delete this file? (This operation cannot be undone)');"
							   paramProperty="fileId"><span>Delete</span>
					</html:link>
				</display:column>
				<%} %>
</display:table>
<%
  }
%>		
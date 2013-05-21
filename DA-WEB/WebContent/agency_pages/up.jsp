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
		String ID = request.getParameter("fileId");
		FileService fs = new FileServiceImpl();
		
  	}
%>		
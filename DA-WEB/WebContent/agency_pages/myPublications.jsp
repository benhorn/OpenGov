<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%  
	Member member = (Member)request.getSession().getAttribute("member");
	if (member != null)
	{
		PublicationService ps= new PublicationServiceImpl();
		FullAgencyListService fls = new FullAgencyListServiceImpl();
		List<Publication> pubList = ps.browsePublication(member); 
		request.setAttribute("pubList", pubList);
%>	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OpenGov (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
<link rel="stylesheet" type="text/css" media="all" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/menus.css"/>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>
<body class="yui-skin-sam">
 <div class="BodyWrapper">
 <div class="Header"><a title="Home" href="https://www.opengov.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0"/>
 <img class="Logo" src="images/OpenGov.png" alt="logo" border="0"/>
 </a></div>
 <div class="AboveSiteContent">
 <h2 class="Cloak">Content Actions</h2>
 <ul class="ContentActions FooterMenu">
 	<li><a href="#" title="Print this Page" onclick="window.print(); return false;"><img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/></a></li>
 </ul>
 <h2 class="Cloak">NSW Sites</h2>
 <jsp:include page="../jspincludes/loggedin_menu.jsp"/>
</div>

<div class="PageTitle"><h1>My publications</h1></div>
<div class="ContextInformation">
 <jsp:include page="../jspincludes/left_sidebar.jsp"/>
 <div class="PublicationsInfo Portlet">
  <div class="Top"></div>
  <div class="Content">
    <h3>Link to OpenGov NSW</h3>
   	  <p>Copy and paste the following code to have the OpenGov button embedded into your website, you may then click on it to access publications from your agency.</p>
   	  <p><textarea cols="25" rows="8" id="synergyButton" onclick="this.focus();this.select();" readonly="readonly"><a href="https://www.opengov.nsw.gov.au/agency/<%=fls.loadAgencyNumberViaMember(member.getMemberId().toString())%>" target="_blank"><img src="https://www.opengov.nsw.gov.au/images/OGovButton.jpg"/></a></textarea></p>
  </div>
  <div class="Bottom"></div>
</div>
 <div class="PublicationsInfo Portlet">
  <div class="Top"></div>
  <div class="Content">
    <h3>FAQ</h3>
   	  <p>If you need assistance, please check the <a href="/faq" target="_blank">frequently asked questions</a> page. 
   	     You can also <a href="mailto:opengov@records.nsw.gov.au">contact us</a></p>
   </div>
  <div class="Bottom"></div>
</div>
</div>

<div class="SiteContent">
<table width="100%">
			<tr><td>
				<html:form action="/pub.do?method=uploadFiles" method="post">
					<display:table name="pubList" id="pub" pagesize="15" class="displaytag" requestURI="/dashboard.do">
					<display:column property="title" title="Title" sortable="true" headerClass="sortable" style="text-align:left;"/>
					<display:column property="totalFiles" title="Total files" headerClass="sortable" style="text-align:center;"/>
					<display:column property="lastUpdated" title="Last update" sortable="true" headerClass="sortable" style="text-align:center;"/>
					<display:column property="status" title="Status" sortable="true" headerClass="sortable" style="text-align:center;"/>
					<display:column title="Options" media="html" style="text-align:left;">
					<html:link page="/pub.do?method=publicationDetails"
						   	   paramId="id" 
							   paramName="pub" 
							   paramProperty="publicationId"><span>Detail</span>
					</html:link>
					<logic:equal name="pub" property="status" value="draft">&nbsp;|&nbsp;
					<a href="/my_publications/delete/<jsp:getProperty property="publicationId" name="pub"/>" onclick="return confirm('Are you sure you want to delete this publication? (This operation cannot be undone)');">Delete</a>
					</logic:equal>
					</display:column>
					</display:table>		
				</html:form>
			</td></tr>
</table>
<hr size="1"/>
<table width="100%">
	<tr><td>
		<a class="greySquare" href="new_publication" title="Register new publication"><span>Register new publication</span></a>		
	</td></tr>
</table>
</div>
      <jsp:include page="../jspincludes/footer.jsp"/>
<logic:messagesPresent message="true">
	<script language="javascript" type="text/javascript">
	<!--
		alert('Thank you, your publication has been submitted and will be reviewed.');
	//-->
	</script>
</logic:messagesPresent>
</body></html>
<%} else {%>
Session time out. Please <a href="<html:rewrite page='/agency_login'/>">Login</a> again.
<% } %>
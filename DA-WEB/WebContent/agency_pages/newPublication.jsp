<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.AjaxBean"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="org.jabsorb.JSONRPCBridge" />

<%  
	Member member = (Member)request.getSession().getAttribute("member");
	if (member != null)
	{	
		AjaxBean ajaxBean = new AjaxBean();
		JSONRPCBridge.registerObject("ajaxBean", ajaxBean);
		AgencyService as = new AgencyServiceImpl();
		String pubID = request.getParameter("id");
		PublicationService ps = new PublicationServiceImpl();
		Publication publication = ps.loadPublication(Integer.parseInt(pubID));
		
%>	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OpenGov (NSW)</title>
<link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/mouseover.css" />
<link rel="stylesheet" type="text/css" href="css/token-input.css" />
<link rel="stylesheet" type="text/css" href="css/jquery.validate.css" />
<link rel="stylesheet" type="text/css" href="css/Tooltip.css" />
<link rel="stylesheet" type="text/css" media="all" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/menus.css"/>
<link rel="stylesheet" type="text/css" href="css/ui-lightness/jquery-ui-1.8.14.custom.css"/>
<link rel="stylesheet" type="text/css" href="css/fileUploader.css"/>
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="js/jsonrpc.js"></script>
<script type="text/javascript" src="js/Tooltip.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.validation.functions.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
<script src="jspincludes/jquery.fileUploader.jsp?id=<%=pubID%>"></script>
<script type="text/javascript">
/* <![CDATA[ */
   jQuery(function(){
       jQuery("#title").validate({
            expression: "if (VAL) return true; else return false;",
            message: "This field is required."
       });
       jQuery("#agencies").validate({
            expression: "if (VAL) return true; else return false;",
            message: "This field is required."
       });
       jQuery("#publication_year").validate({
            expression: "if (VAL) return true; else return false;",
            message: "This field is required."
       });
    });
/* ]]> */
</script>
<script type="text/javascript">
<!--
function verifyInput(type)
{
	var	jsonrpc = new JSONRpcClient("JSON-RPC");
	var typeValue;
	var typeID;
	var result = false;
	var warningMessage;
	if (type=="keyword")
	{
		typeValue = window.document.PublicationForm.new_keyword.value;
		result = jsonrpc.ajaxBean.chkKeyword(typeValue);
	}
	if (type=="agency")
	{
		typeValue = window.document.PublicationForm.new_agency.value;
		result = jsonrpc.ajaxBean.chkAgencyName(typeValue);
	}
	if (type=="publisher")
	{
		typeValue = window.document.PublicationForm.new_publisher.value;
		result = jsonrpc.ajaxBean.chkAgencyName(typeValue);
	}
	if ((type != null) && (type.length > 0) && !result)
	{
		warningMessage = ("This ").concat(type).concat(" already exists");
		alert(warningMessage);
		typeID = "new_".concat(type);
		document.getElementById(typeID).focus();
		return true;		
	}else
	{
		return false;
	}
	
}

function addInput(type)
{
	var	jsonrpc = new JSONRpcClient("JSON-RPC");
	var typeValue = window.document.PublicationForm.new_keyword.value;
	var typeID = "new_".concat(type);
	var result = false;
	var warningMessage = ("New ").concat(type).concat(" created");
	
	if (!verifyInput(type))
	{
		if (type=="keyword")
		{
			typeValue = window.document.PublicationForm.new_keyword.value;
			if ((typeValue == null) || (typeValue.length < 1))
			{
			   alert("Keyword is required.");
			}else
			{	
				result = jsonrpc.ajaxBean.addKeyword(typeValue);
				if (result)
				{	
					alert(warningMessage);
					$("#new_keyword").val("");
					$("#selectKeywords").load('agency_pages/search_tag.jsp?keyword=' + window.document.PublicationForm.keywords.value + '&newKeyword=' + encodeURI(typeValue));
				}else
				{
					alert("Unable to add this keyword.");
					window.document.PublicationForm.new_keyword.focus();
				}
			}
		}
		if (type=="agency")
		{
			typeValue = window.document.PublicationForm.new_agency.value;
			if ((typeValue == null) || (typeValue.length < 1))
			{
			   alert("Agency name is required.");
			}else
			{	
				result = jsonrpc.ajaxBean.addAgency(typeValue);
				if (result)
				{	
					alert(warningMessage);
					$("#new_agency").val("");
					$("#selectAgency").load('agency_pages/search_agency.jsp?agency=' + window.document.PublicationForm.agencyNumber.value + '&newAgency=' + encodeURI(typeValue));
				}else
				{
					alert("Unable to create agency.");
					window.document.PublicationForm.new_agency.focus();
				}
			}
		}
		if (type=="publisher")
		{
			typeValue = window.document.PublicationForm.new_publisher.value;
			if ((typeValue == null) || (typeValue.length < 1))
			{
			   alert("Publisher is required.");
			}else
			{
				result = jsonrpc.ajaxBean.addAgency(typeValue);
				if (result)
				{
					alert(warningMessage);
					$("#new_publisher").val("");
		  			$("#selectPublisher").load('agency_pages/search_publisher.jsp?publisher=' + window.document.PublicationForm.publisherNumber.value + '&newPublisher=' + encodeURI(typeValue));
				}else
				{
					alert("Unable to create publisher.");
					window.document.PublicationForm.new_publisher.focus();
				}
			}
		}	
		
	}
}

function addPublisher()
{
	var	jsonrpc = new JSONRpcClient("JSON-RPC");
	var publisher = window.document.PublicationForm.new_publisher.value;
	var result = false;
	if (!checkAgency())
	{
		result = jsonrpc.ajaxBean.addAgency(agency);
		if (result)
		{
			alert(agency);
			alert("New agency created !");
	  		$("#selectAgency").load('agency_pages/search_agency.jsp?agency=' + window.document.PublicationForm.agencyNumber.value);
		}else
		{
			alert("Unable to create publisher.");
			window.document.PublicationForm.new_agency.focus();
		}
	}
}
function showInput() {
	  var list = window.document.forms['PublicationForm'].type;
	  if(list.options[list.selectedIndex].value == "other")
	  {
	   	document.getElementById("type_other").style.display="block";
	  }
	  else 
	  {
	 	document.getElementById("type_other").style.display="none";
	  }
}
//-->
</script>
<script type="text/javascript">
<!--
function countFiles()
{
	var	jsonrpc = new JSONRpcClient("JSON-RPC");
	var pubID = window.document.PublicationForm.publicationId.value;
	var fileNo = jsonrpc.ajaxBean.countFiles(pubID);
	if (fileNo == 0)
	{
		alert("Please attach files.");
		return false;
	}else
		{
		return true;
		}
}
//-->
</script>
</head>

<body class="yui-skin-sam">
  <div class="BodyWrapper">
    <div class="Header"><a title="Home" href="https://www.opengov.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0"/>
    <img class="Logo" src="images/OpenGov.png" alt="logo" border="0"/>
    </a></div>
      <div class="AboveSiteContent">
  		<h2 class="Cloak">Content Actions</h2>
  		<ul class="ContentActions FooterMenu">
    		<li><a href="#" title="Print this Page" onclick="window.print(); return false;">
    		<img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/></a></li>
    	</ul>
  <h2 class="Cloak">NSW Sites</h2>
  <jsp:include page="../jspincludes/loggedin_menu.jsp"/>
</div>

<div class="PageTitle"><h1>Publication details</h1></div>
<div class="ContextInformation">
<jsp:include page="../jspincludes/left_sidebar.jsp"/>

<div class="PublicationsInfo Portlet">
  <div class="Top"></div>
  <div class="Content">
    <h3>FAQ</h3>
   	  <p>If you need assistance, please check the <a href="/faq" target="_blank">frequently asked questions</a> page. 
   	     You can also <a href="mailto:opengov@records.nsw.gov.au">contact us</a></p>
   </div>
  <div class="Bottom"></div>
</div>

<div class="rightbottom">
    <div class="Top"></div>
  <div class="Content">
      <h3></h3>
  	   <p>Submitted publications are checked before they appear on OpenGov NSW. You will 
	   be notified by email when the publication is available on the site.</p>
  </div>
  <div class="Bottom"></div>
</div>
</div>

<div class="SiteContent">
<h3>Attach file(s)</h3>
<fieldset>
		<div id="main_container">
			<form action="fileUploadAction.do?id=<%=pubID%>" method="post" enctype="multipart/form-data">
				<input type="file" name="file" class="fileUpload" multiple="multiple"/>
			</form>
			<script type="text/javascript">
				<!--
					jQuery(function($){
						$('.fileUpload').fileUploader();
					});
				//-->
			</script>
		</div>
		<div id="displayTable">
		<%
		FileService fs = new FileServiceImpl();
		List<UploadedFile> fList = fs.browseFiles(publication);
		if (!fList.isEmpty())
		{
		%>
		<script language="javascript">
		<!--
			$('#displayTable').load("agency_pages/displayTable.jsp?id=<%=pubID%>&random=" + Math.round(Math.random()*99999));
		//-->
		</script>	  
		<%
		}
		%>
		</div>
		<div></div>
</fieldset>
<html:form action="pub.do?method=addPublication" styleId="PublicationForm" method="post" onsubmit="return countFiles();">
<h3>Enter publication details</h3>
		<fieldset>
			<table class="publication">
			<tr>
				<td><span class="dropt" title=""><label for="title">Title:<font color="red">*</font></label><span><bean:message key="metadata.title"/></span></span></td> 
				<td><html:text property="title" styleId="title" value="" style="width: 280px;"/></td>
			</tr>
			<tr>
				<td valign="top"><span class="dropt" title=""><label for="description">Description:</label><span><bean:message key="metadata.description"/></span></span></td>
				<td><html:textarea property="description" rows="4" cols="34" value=""/></td>
			</tr>
			<tr>
				<td><span class="dropt" title=""><label for="keywords">Keywords:</label><span><bean:message key="metadata.keywords"/></span></span></td>
				<td><div id="selectKeywords">
						<jsp:include page="./search_tag.jsp" flush="true"/>
					</div>
					<div style="display:none;"> 
						 <div id="new_keyword">
						 		<p id="validateTips"><strong>Create a new keyword</strong></p>
						 		<label for="new_keyword">Keyword:</label>
								<input type="text" name="new_keyword" onblur="verifyInput('keyword');" id="new_keyword" />
						 		<p align="right"><html:button styleId="create_keyword" property="createKeyword" onclick="addInput('keyword');" value="Create keyword"/></p>
						 </div>
					</div>
				</td>
			</tr>
			<tr>
				<td><span class="dropt" title=""><label for="agency">Agency:<font color="red">*</font></label><span><bean:message key="metadata.agency"/></span></span></td>
				<td><div id="selectAgency">
						<jsp:include page="./search_agency.jsp" flush="true"/>
					</div>
					<div style="display:none;"> 
						 <div id="new_agency">
						 		<p id="validateTips"><strong>Create a new Agency</strong></p>
						 		<label for="new_agency">Agency name:</label>
								<input type="text" name="new_agency" size="34" onblur="verifyInput('agency');" id="new_agency" />
						 		<p align="right">
						 		<html:button styleId="create_agency" property="createAgency" onclick="addInput('agency');" value="Create Agency"/></p>
						 </div>
					</div>
				</td>
			</tr>
			<tr>
		 		<td><span class="dropt" title=""><label for="publisher">Publisher:</label><span><bean:message key="metadata.publisher"/></span></span></td>
		 		<td><div id="selectPublisher">
						<jsp:include page="./search_publisher.jsp" flush="true"/>
					</div>
					<div style="display:none;"> 
						 <div id="new_publisher">
						 		<p id="validateTips"><strong>Create a new publisher</strong></p>
						 		<label for="new_publisher">Publisher name:</label>
								<input type="text" name="new_publisher" size="34" onblur="verifyInput('publisher');" id="new_publisher" />
						 		<p align="right"><html:button styleId="create_keyword" property="createKeyword" onclick="addInput('publisher');" value="Create publisher"/></p>
						 </div>
					</div>
		 		</td>
		 	</tr>
			
			<tr>
				<td><span class="dropt" title=""><label for="published_date">Date published:<font color="red">*</font></label><span><bean:message key="metadata.date.published"/></span></span></td>
				<td>
      				<select name="publication_date"> 
	    				<option value="" selected>dd</option>
						<c:forEach var="k" begin="1" end="31" step="1">
						   <c:if test="${k<10}"><c:set var="k" value="0${k}" /></c:if>
						   <option value="${k}">${k}</option> 
						</c:forEach>
					</select>
					<select name="publication_month"> 
	  					<option value="" selected>mm</option>
	  					<c:forEach var="j" begin="1" end="12" step="1">
	   					<c:if test="${j<10}"><c:set var="j" value="0${j}" /></c:if>
							<option value="${j}">${j}</option> 
						</c:forEach>
					</select>
<%
	Calendar cal = new GregorianCalendar();
	int year = cal.get(Calendar.YEAR);             
%>
					<select name="publication_year" id="publication_year"> 
	  					<option value="" selected>yyyy</option>
						  	<c:forEach var="i" begin="1800" end="<%=year%>" step="1" varStatus ="status">
							   <option value="${i}">${i}</option> 
							</c:forEach>				
					</select>
				</td>
			</tr>
			<tr>
				<td><span class="dropt" title=""><label for="type">Type:</label><span><bean:message key="metadata.type"/></span></span></td>
				<td>
				<html:select property="type" onchange="showInput();">
				 <html:option value="Annual Report">Annual Report</html:option>
				 <html:option value="Strategic Plan">Strategic Plan</html:option>
				 <html:option value="Agency Information Guide">Agency Information Guide</html:option>
				 <html:option value="Policy Document">Policy Document</html:option>
				 <html:option value="Disclosure Log">Disclosure Log</html:option>
				 <html:option value="Register of Contracts">Register of Contracts</html:option>
				 <html:option value="Tabled in Parliament">Tabled in Parliament</html:option>
				 <html:option value="List of Assets">List of Assets</html:option>
				 <html:option value="Guarantee of Service">Guarantee of Service</html:option>
				 <html:option value="Government Gazette">Government Gazette</html:option>
				 <html:option value="Code of Conduct">Code of Conduct</html:option>
				 <html:option value="Map">Map</html:option>
				 <html:option value="Media Release">Media Release</html:option>
				 <html:option value="Overseas Travel Expense">Overseas Travel Expense</html:option>
				 <html:option value="Corporate Plan">Corporate Plan</html:option>
				 <html:option value="other">Other</html:option>
       			</html:select><div id="type_other" style="display: none"><br/><html:text property="type_other" value="" size="32"/></div>
				</td>
		 	</tr>
		 	<tr>
		 		<td><span class="dropt" title=""><label for="coverage">Geographical coverage:</label><span><bean:message key="metadata.coverage"/></span></span></td>
		 		<td><html:text property="coverage" value="New South Wales" style="width: 280px;"/></td>
		 	</tr>
		 	<tr>
		 		<td><span class="dropt" title=""><label for="language">Language:</label><span><bean:message key="metadata.language"/></span></span></td>
		 		<td><html:text property="language" value="English" style="width: 280px;"/></td>
		 	</tr>
		 	<tr>
		 		<td><span class="dropt" title=""><label for="copyright">Copyright:</label><span><bean:message key="metadata.rights"/></span></span></td>
		 		<td><html:text property="rights" value="State of New South Wales" style="width: 280px;"/></td>
		 	</tr>
		 	<tr>
		 		<td><span class="dropt" title=""><label for="commons">Creative commons:</label><span><bean:message key="metadata.creativecommons"/></span></span></td>
		 		<td><input type="checkbox" name="creativeCommons" checked/></td>
		 	</tr>
			</table>
		</fieldset>
				<div><small><font color="red">*</font> mandatory field.</small></div>
		<html:hidden property="publicationId" value="<%=pubID%>" />
		<p align="right">
		<html:submit property="action" styleId="savedraft" value="Save draft"/>&nbsp;&nbsp;
		<c:choose>
      		<c:when test="${member.privileged=='y'}">
				<html:submit property="action" onclick="return confirm('This publication will be live without being moderated. Are you ready to proceed?');" styleId="publish" value="Publish"/>
			</c:when>
			<c:otherwise>
				<html:submit property="action" onclick="return confirm('After submitting this publication you cannot make any further changes. Are you ready to submit?');" styleId="submitdraft" value="Submit publication"/>
			</c:otherwise>
		</c:choose>		
		</p>
</html:form>
</div>
  <jsp:include page="../jspincludes/footer.jsp"/>
</body></html>

<%} else {%>
Session time out. Please <a href="<html:rewrite page='/agency_login'/>">Login</a> again.
<% } %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="org.jabsorb.JSONRPCBridge" />
<%@page import="au.gov.nsw.records.digitalarchive.base.*"%>
<%@page import="java.util.*"%>
<%  
	Member member = (Member)request.getSession().getAttribute("member");
	if (member != null)
	{	
		Publication currentPub = (Publication)request.getSession().getAttribute("currentPub");
		
%>	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/mouseover.css" />
<link rel="stylesheet" type="text/css" href="css/token-input.css" />
<link rel="stylesheet" type="text/css" href="css/jquery.validate.css" />
<link rel="stylesheet" type="text/css" href="css/Tooltip.css" />
<title>Publications (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="http://publications.nsw.gov.au/@@/default/favicon.png"/>
<link rel="stylesheet" type="text/css" media="all" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/menus.css"/>
<link rel="stylesheet" href="css/jquery.ui.all.css"/>
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="js/jsonrpc.js"></script>
<script type="text/javascript" src="js/Tooltip.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.validation.functions.js"></script>
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
       jQuery("#publication_year").validate({
           expression: "if (!isNaN(VAL) && VAL) return true; else return false;",
           message: "Please enter a valid publication year."
       });
    });
/* ]]> */
</script>
<script type="text/javascript">
<!--
var tips = $("#validateTips");
function updateTips(t) {
	tips.text(t).effect("highlight",{color:"#b1b1b1"},1500);
}

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
		result = jsonrpc.ajaxBean.chkTag(typeValue);
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
				result = jsonrpc.ajaxBean.addTag(typeValue);
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
					$("#selectAgency").load('agency_pages/search_agency_edit.jsp?agency=' + window.document.PublicationForm.agencyNumber.value + '&newAgency=' + encodeURI(typeValue));
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
</head>

<body class="yui-skin-sam">
  <div class="BodyWrapper">
    <div class="Header"><a title="Home" href="http://publications.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0"/></a></div>
      <div class="AboveSiteContent">
  		<h2 class="Cloak">Content Actions</h2>
  		<ul class="ContentActions FooterMenu">
    		<li><a href="#" title="Print this Page" onclick="window.print(); return false;">
    		<img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/></a></li>
    	</ul>
  <h2 class="Cloak">NSW Sites</h2>
  <jsp:include page="../jspincludes/loggedin_menu.jsp"/>
</div>

<div class="PageTitle"><h1>Editing <%=currentPub.getTitle()%></h1></div>
<div class="ContextInformation">
<jsp:include page="../jspincludes/left_sidebar.jsp"/>
</div>
<div class="SiteContent">
<html:form action="/pub.do?method=editPublication" method="post">
		<fieldset>
			<table class="publication">
			<tr>
				<td><span class="dropt" title="">Title:<font color="red">*</font><span><bean:message key="metadata.title"/></span></span></td> 
				<td><html:text property="title" styleId="title" value="${currentPub.title}" style="width: 280px;"/></td>
			</tr>
			<tr>
				<td valign="top"><span class="dropt" title="">Description:<span><bean:message key="metadata.description"/></span></span></td>
				<td><html:textarea property="description" rows="4" cols="34" value="${currentPub.description}"/></td>
			</tr>
			<tr>
				<td><span class="dropt" title="">Keywords:<span><bean:message key="metadata.keywords"/></span></span></td>
				<td><div id="selectKeywords">
						<jsp:include page="./search_tag_edit.jsp" flush="true">
								<jsp:param name="pubID" value="<%=currentPub.getPublicationId()%>" />
						</jsp:include>
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
				<td><span class="dropt" title="">Agency:<font color="red">*</font><span><bean:message key="metadata.agency"/></span></span></td>
				<td><div id="selectAgency">
						<jsp:include page="./search_agency_edit.jsp" flush="true">
							 <jsp:param name="pubID" value="<%=currentPub.getPublicationId()%>" />
						</jsp:include>
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
				<td><span class="dropt" title="">Type:<span><bean:message key="metadata.type"/></span></span></td>
				<td>
				<select name="type" onchange="showInput();">
					<option value="Annual Report" <c:if test="${currentPub.type=='Annual Report'}">selected="selected"</c:if>>Annual Report</option>
					<option value="Strategic Plan" <c:if test="${currentPub.type=='Strategic Plan'}">selected="selected"</c:if>>Strategic Plan</option>
					<option value="Agency Information Guide" <c:if test="${currentPub.type=='Agency Information Guide'}">selected="selected"</c:if>>Agency Information Guide</option>
					<option value="Policy Document" <c:if test="${currentPub.type=='Policy Document'}">selected="selected"</c:if>>Policy Document</option>
					<option value="Disclosure Log" <c:if test="${currentPub.type=='Disclosure Log'}">selected="selected"</c:if>>Disclosure Log</option>
					<option value="Register of Contracts" <c:if test="${currentPub.type=='Register of Contracts'}">selected="selected"</c:if>>Register of Contracts</option>
					<option value="Tabled in Parliament" <c:if test="${currentPub.type=='Tabled in Parliament'}">selected="selected"</c:if>>Tabled in Parliament</option>
					<option value="List of Assets" <c:if test="${currentPub.type=='List of Assets'}">selected="selected"</c:if>>List of Assets</option>
					<option value="Guarantee of Service" <c:if test="${currentPub.type=='Guarantee of Service'}">selected="selected"</c:if>>Guarantee of Service</option>
					<option value="Code of Conduct" <c:if test="${currentPub.type=='Code of Conduct'}">selected="selected"</c:if>>Code of Conduct</option>
					<option value="Media Release" <c:if test="${currentPub.type=='Media Release'}">selected="selected"</c:if>>Media Release</option>
					<option value="Overseas Travel Expense" <c:if test="${currentPub.type=='Overseas Travel Expense'}">selected="selected"</c:if>>Overseas Travel Expense</option>
					<option value="Corporate Plan" <c:if test="${currentPub.type=='Corporate Plan'}">selected="selected"</c:if>>Corporate Plan</option>
					<option value="other" <c:if test="${currentPub.type=='other'}">selected="selected"</c:if>>Other</option>					
				</select><div id="type_other" style="display: none"><br/><input type="text" name="type_other" value="" size="32"/></div>
				</td>
		 	</tr>
		 	<tr>
		 		<td><span class="dropt" title="">Geographical coverage:<span><bean:message key="metadata.coverage"/></span></span></td>
		 		<td><html:text property="coverage" style="width: 280px;" value="${currentPub.coverage}"/></td>
		 	</tr>
		 	<tr>
		 		<td><span class="dropt" title="">Language:<span><bean:message key="metadata.language"/></span></span></td>
		 		<td><html:text property="language" style="width: 280px;" value="${currentPub.language}"/></td>
		 	</tr>
		 	<tr>
		 		<td><span class="dropt" title="">Publisher:<span><bean:message key="metadata.publisher"/></span></span></td>
		 		<td><div id="selectPublisher">
						<jsp:include page="./search_publisher_edit.jsp" flush="true">
								<jsp:param name="pubID" value="<%=currentPub.getPublicationId()%>" />
						</jsp:include>
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
		 		<td><span class="dropt" title="">Rights:<span><bean:message key="metadata.rights"/></span></span></td>
		 		<td><html:text property="rights" value="${currentPub.rights}" style="width: 280px;"/></td>
		 	</tr>
			</table>
		</fieldset>
		<div><small><font color="red">*</font> mandatory field.</small></div>
		<p align="right"><html:submit property="next" styleId="FormSubmit" value="Update"/></p>
</html:form>
</div>
  <jsp:include page="../jspincludes/footer.jsp"/>
</body></html>

<%} else {%>
Session time out. Please <a href="<html:rewrite page='/login.jsp'/>">Login</a> again.
<% } %>
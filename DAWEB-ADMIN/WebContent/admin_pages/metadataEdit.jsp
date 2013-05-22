<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="org.jabsorb.JSONRPCBridge" />
<% 
	Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");
	if (archivist != null)
	{	
		AjaxBean ajaxBean = new AjaxBean();
		JSONRPCBridge.registerObject("ajaxBean", ajaxBean);
		Publication currentPub = (Publication)request.getSession().getAttribute("currentPub");
		String pubDetailsURL= new String("window.location.href='pub.do?method=publicationDetails&id=") + currentPub.getPublicationId() + "'";
		String datePublished = currentPub.getDatePublishedDisplay();
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">	
<head>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/token-input.css" />
<link rel="stylesheet" type="text/css" href="css/jquery.validate.css" />
<link rel="stylesheet" type="text/css" href="css/Tooltip.css" />
<script type="text/javascript" src="scripts/jquery-1.7.1.js"></script>
<script type="text/javascript" src="scripts/jquery.tokeninput.js"></script>
<script type="text/javascript" src="scripts/jsonrpc.js"></script>
<script type="text/javascript" src="scripts/Tooltip.js"></script>
<script type="text/javascript" src="scripts/jquery.validate.js"></script>
<script type="text/javascript" src="scripts/jquery.validation.functions.js"></script>
<script type="text/javascript" src="scripts/jquery-ui-1.8.14.custom.min.js"></script>
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
					$("#selectKeywords").load('admin_pages/search_tag.jsp?keyword=' + window.document.PublicationForm.keywords.value + '&newKeyword=' + encodeURI(typeValue));
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
					$("#selectAgency").load('admin_pages/search_agency_edit.jsp?agency=' + window.document.PublicationForm.agencyNumber.value + '&newAgency=' + encodeURI(typeValue));
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
		  			$("#selectPublisher").load('admin_pages/search_publisher.jsp?publisher=' + window.document.PublicationForm.publisherNumber.value + '&newPublisher=' + encodeURI(typeValue));
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
	  		$("#selectAgency").load('admin_pages/search_agency.jsp?agency=' + window.document.PublicationForm.agencyNumber.value);
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
<style type="text/css">
<!--
body {
	background-color: lightgrey;
}
-->
</style>

</head>  
  <body>
 <html:form action="pub.do?method=amendPublication" styleId="PublicationForm" method="post">
	<table border="1" align="center" cellpadding="5" cellspacing="5" style="background-color:lightgrey; border:0px;">
	  <tr>
		<td align="center" colspan="2"><h1>Editing '${currentPub.title}'</h1></td>
	  </tr>
	  <tr><td>Title:</td><td><html:text property="title" styleId="title" value="${currentPub.title}" style="width: 350px;"/></td></tr>
	  <tr><td>Description:</td><td><html:textarea property="description" rows="10" cols="50" value="${currentPub.description}"/></td></tr>
	   <tr><td>Keywords:</td>
	  	  <td><div id="selectKeywords">
						<jsp:include page="./search_tag_edit.jsp" flush="true">
								<jsp:param name="pubID" value="${currentPub.publicationId}" />
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
				<td>Agency:</td>
				<td><div id="selectAgency">
						<jsp:include page="./search_agency_edit.jsp" flush="true">
							 <jsp:param name="pubID" value="${currentPub.publicationId}" />
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
		 		<td>Publisher:</td>
		 		<td><div id="selectPublisher">
						<jsp:include page="./search_publisher_edit.jsp" flush="true">
								<jsp:param name="pubID" value="${currentPub.publicationId}" />
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
		<%
	Calendar cal = new GregorianCalendar();
	int thisYear = cal.get(Calendar.YEAR); 
	
	String published_year = "";
	String published_month = "";
	String published_day = "";
	
	if (!("").equals(datePublished) || datePublished != null)
	{
	   if (StringUtils.countMatches(datePublished, "/") == 0)
	   {
		  published_year = datePublished;
	   }
	   if (StringUtils.countMatches(datePublished, "/") == 1)
	   {
		  String[] temp;
		  temp = datePublished.split("/");
		  published_month = temp[0];
		  published_year = temp[1];
	   }
	   if (StringUtils.countMatches(datePublished, "/") == 2)
	   {
		  String[] temp1;
		  temp1 = datePublished.split("/");
		  published_day = temp1[0];
		  published_month = temp1[1];
		  published_year = temp1[2];
	   }
	}
%>
			<%if (datePublished != null)
			{%>	
			<tr>
				<td>Date published:</td>
				<td><select name="publication_day"> 
	    				<option value="">dd</option>
						<%
							  for (int k = 1; k <= 31; k++)
							  {
						%>
						<option value="<%=k%>" <%if (!("").equals(published_day)){if (Integer.parseInt(published_day)==k){%>selected="selected"<%}}%>><%=k%></option>
							 <%
							 }
							 %>		
					</select>
					<select name="publication_month"> 
	  				<option value="">mm</option>
	  				<%
						  for (int j = 1; j <= 12; j++)
						  {
					%><option value="<%=j%>" <%if (!("").equals(published_month)){if (Integer.parseInt(published_month)==j){%>selected="selected"<%}}%>><%=j%></option>
					<%
						 }
					%>		
					</select>
					<select name="publication_year" id="publication_year"> 
	  				<option value="">yyyy</option>
					<%
					  for (int i = 1800; i <= thisYear; i++)
					  {
					%>
					<option value="<%=i%>"  <%if (!("").equals(published_year)){if (Integer.parseInt(published_year)==i){%>selected="selected"<%}}%>><%=i%></option>
					 <%
					  }
					 %>				
					</select>
				</td>
			</tr><%}%>
			<%if (datePublished == null)
			{%>	
			<tr>
				<td>Date published:</td>
				<td>
      				<select name="publication_day"> 
	    				<option value="" selected>dd</option>
						<c:forEach var="k" begin="01" end="31" step="1">
								<option value="${k}">${k}</option> 
						</c:forEach>
					</select>
					<select name="publication_month"> 
	  					<option value="" selected>mm</option>
	  					<c:forEach var="j" begin="01" end="12" step="1">
								<option value="${j}">${j}</option> 
						</c:forEach>
					</select>
					<select name="publication_year" id="publication_year"> 
	  					<option value="" selected>yyyy</option>
						  	<c:forEach var="i" begin="1860" end="<%=thisYear%>" step="1" varStatus ="status">
								<option value="${i}">${i}</option> 
							</c:forEach>				
					</select>
				</td>
			</tr>
			<%}%>
				
			<tr>
				<td>Type:</td>
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
		 		<td>Geographical coverage:</td>
		 		<td><html:text property="coverage" style="width: 280px;" value="${currentPub.coverage}"/></td>
		 	</tr>
		 	<tr>
		 		<td>Language:</td>
		 		<td><html:text property="language" style="width: 280px;" value="${currentPub.language}"/></td>
		 	</tr>
		 	<tr>
		 		<td>Rights:</td>
		 		<td><html:text property="rights" value="${currentPub.rights}" style="width: 280px;"/></td>
		 	</tr>
		 	<tr>
		 		<td>Creative commons:</td>
		 		<td><input type="checkbox" name="creativeCommons" <c:if test="${currentPub.creativeCommons=='on'}">checked</c:if>/></td>
		 	</tr>
			</table>
		<html:hidden property="publicationId" value="${currentPub.publicationId}" />
		<table border="0" align="center" cellpadding="5" cellspacing="5" style="background-color:lightgrey; border:0px;">
		<tr><td>
		<input type="button" value="Back" onclick="<%=pubDetailsURL%>"/>
		</td><td>
		<html:submit property="action" styleId="savedraft" value="Update Changes"/>
		</td></tr>
  		</table>
</html:form>
  </body>
</html>
<%
}
%>
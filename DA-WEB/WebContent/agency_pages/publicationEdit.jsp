<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
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
		Publication currentPub = (Publication)request.getSession().getAttribute("currentPub");
		String datePublished = currentPub.getDatePublishedDisplay();
%>	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>OpenGov (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="https://www.opengov.nsw.gov.au/images/favicon.png"/>
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
<script src="jspincludes/jquery.fileUploader.jsp?id=${currentPub.publicationId}"></script>
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
    <div class="Header"><a title="Home" href="https://www.opengov.nsw.gov.au/">
    <img class="Logo" src="images/logo.png" alt="logo" border="0"/><img class="Logo" src="images/OpenGov.png" alt="logo" border="0"/></a></div>
      <div class="AboveSiteContent">
  		<h2 class="Cloak">Content Actions</h2>
  		<ul class="ContentActions FooterMenu">
    		<li><a href="#" title="Print this Page" onclick="window.print(); return false;">
    		<img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/></a></li>
    	</ul>
  <h2 class="Cloak">NSW Sites</h2>
  <jsp:include page="../jspincludes/loggedin_menu.jsp"/>
</div>

<div class="PageTitle"><h1>Editing ${currentPub.title}&nbsp;<small>(${currentPub.status})</small></h1></div>
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
			<form action="fileUploadAction.do?id=${currentPub.publicationId}" method="post" enctype="multipart/form-data">
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
		List<UploadedFile> fList = fs.browseFiles(currentPub);
		if (!fList.isEmpty())
		{
		%>
		<script language="javascript">
		<!--
			$('#displayTable').load("agency_pages/displayTableEdit.jsp?id=${currentPub.publicationId}&random=" + Math.round(Math.random()*99999));
		//-->
		</script>	  
		<%
		}
		%>
		</div>
		<div></div>
</fieldset>
<html:form action="/pub.do?method=amendPublication" styleId="PublicationForm" method="post" onsubmit="return countFiles();">
<h3>Enter publication details</h3>
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
				<td><span class="dropt" title="">Agency:<font color="red">*</font><span><bean:message key="metadata.agency"/></span></span></td>
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
		 		<td><span class="dropt" title="">Publisher:<span><bean:message key="metadata.publisher"/></span></span></td>
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
				<td><span class="dropt" title=""><label for="published_date">Date published:<font color="red">*</font></label><span><bean:message key="metadata.date.published"/></span></span></td>
				<td><select name="publication_day"> 
	    				<option value="">dd</option>
					<%
					  String tempI = "";
					  for (int i = 1; i <= 31; i++)
					  {if (i < 10)
					   {tempI = "0".concat(Integer.toString(i)); 
					%>
					<option value="<%=tempI%>" <%if (!("").equals(published_day)){if (Integer.parseInt(published_day)==i){%>selected="selected"<%}}%>><%=tempI%></option>
					<%}else{%>
					<option value="<%=i%>" <%if (!("").equals(published_day)){if (Integer.parseInt(published_day)==i){%>selected="selected"<%}}%>><%=i%></option>
					<%
					 }}
					%>	
					</select>
					<select name="publication_month"> 
	  				<option value="">mm</option>
	  				<%
					  String tempJ = "";
					  for (int j = 1; j <= 12; j++)
					  {if (j < 10)
					    {tempJ = "0".concat(Integer.toString(j));
					%>
					<option value="<%=tempJ%>" <%if (!("").equals(published_month)){if (Integer.parseInt(published_month)==j){%>selected="selected"<%}}%>><%=tempJ%></option>
					<%}else{%>
					<option value="<%=j%>" <%if (!("").equals(published_month)){if (Integer.parseInt(published_month)==j){%>selected="selected"<%}}%>><%=j%></option>
					<%
					}}
					%>		
					</select>
					<select name="publication_year" id="publication_year"> 
	  				<option value="">yyyy</option>
					<%
					  for (int i = 1800; i <= thisYear; i++)
					  {
					%>
					<option value="<%=i%>" <%if (!("").equals(published_year)){if (Integer.parseInt(published_year)==i){%>selected="selected"<%}}%>><%=i%></option>
					 <%
					  }
					 %>				
					</select>
				</td>
			</tr><%}%>
			<%if (datePublished == null)
			{%>	
			<tr>
				<td><span class="dropt" title=""><label for="published_date">Date published:<font color="red">*</font></label><span><bean:message key="metadata.date.published"/></span></span></td>
				<td>
      				<select name="publication_day"> 
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
					<select name="publication_year" id="publication_year"> 
	  					<option value="" selected>yyyy</option>
						  	<c:forEach var="i" begin="1800" end="<%=thisYear%>" step="1" varStatus ="status">
								<option value="${i}">${i}</option> 
							</c:forEach>				
					</select>
				</td>
			</tr>
			<%}%>
				
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
					<option value="Government Gazette" <c:if test="${currentPub.type=='Government Gazette'}">selected="selected"</c:if>>Government Gazette</option>
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
		 		<td><span class="dropt" title="">Rights:<span><bean:message key="metadata.rights"/></span></span></td>
		 		<td><html:text property="rights" value="${currentPub.rights}" style="width: 280px;"/></td>
		 	</tr>
		 	<tr>
		 		<td><span class="dropt" title=""><label for="commons">Creative commons:</label><span><bean:message key="metadata.creativecommons"/></span></span></td>
		 		<td><input type="checkbox" name="creativeCommons" <c:if test="${currentPub.creativeCommons=='on'}">checked</c:if>/></td>
		 	</tr>
			</table>
		</fieldset>
		<div><small><font color="red">*</font> mandatory field.</small></div>
		<html:hidden property="publicationId" value="${currentPub.publicationId}" />
		<p align="right">
		<html:submit property="action" styleId="savedraft" value="Save update"/>&nbsp;&nbsp;
		<c:choose>
      		<c:when test="${member.privileged=='y'}">
				<html:submit property="action" onclick="return confirm('This publication and its contents will be live without being moderated. Are you ready to proceed?');" styleId="publish" value="Publish"/>
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
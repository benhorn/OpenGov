<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><head>
<title>Publications (NSW)</title>
<!--[if lt IE 8]><style type="text/css">@import url(/@@/default/ie7.css);</style><![endif]-->
<!--[if lt IE 7]><style type="text/css">@import url(/@@/default/ie6.css);</style><![endif]-->
<link rel="shortcut icon" type="image/x-icon" href="http://publications.nsw.gov.au/@@/default/favicon.png"/>
<link rel="stylesheet" type="text/css" media="all" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/print.css"/>
<link rel="stylesheet" type="text/css" media="all" href="css/menus.css"/>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>
<body class="yui-skin-sam">
<div class="BodyWrapper">
   <div class="Header"><a title="Home" href="http://publications.nsw.gov.au/"><img class="Logo" src="images/logo.png" alt="logo" border="0"></a></div>
   <div class="AboveSiteContent">
  	<h2 class="Cloak">Content Actions</h2>
  	<ul class="ContentActions FooterMenu">
    	<li>
      	<a href="#" title="Print this Page" onclick="window.print(); return false;">
        <img class="Logo" src="images/icon_print.gif" alt="Print this page" border="0"/>
      </a></li>
    </ul>
    
  <h2 class="Cloak">NSW Sites</h2>
    <jsp:include page="jspincludes/menu.jsp" />

  <ul class="Breadcrumbs Menu">
    
      <li class="Breadcrumb Start">
        <a href="http://publications.nsw.gov.au/" title="publications.nsw">
        
          <span>publications.nsw</span>
        </a>
      </li>
    
  </ul>
</div>
<div class="PageTitle"><h1>Log in</h1></div>

<!-- e.g. viewlets showing metadata, ftpinfo, static text -->
<div class="ContextInformation">

<div class="PublicationsInfo Portlet">
  <div class="Top"></div>
  <div class="Content">
    <h3>New user?</h3>
   	  <p>Click <a href="register">here</a> to register</p>
  </div>
  <div class="Bottom"></div>
</div>


<div class="PublicationsInfo Portlet">
  <div class="Top"></div>
  <div class="Content">
    <h3>Forgot your password?</h3>
    <p>
      Enter your email address below and click submit to have your password sent to you.<br/>
      <input type="text" name="email" />&nbsp;<input type="button" value="submit"/> 
    </p>
  </div>
  <div class="Bottom"></div>
</div>



</div>

<div class="SiteContent">
	<logic:messagesPresent>
	<div class="error">
   			<ul>
				<html:errors/>
   			</ul>
	</div>
	</logic:messagesPresent>
	<logic:messagesPresent message="true">
   		<div class="error">
    		<ul>  
    			<html:messages id="msgs" message="true">
    			<li><bean:write name="msgs"/></li>
    			</html:messages>
    		</ul>
    	</div>
    </logic:messagesPresent>
	
<html:form action="login.do" method="post">
 <fieldset>
					<legend><span>Please enter your Login name and Password</span></legend>
					<table>
						<tr><td>Login name:</td><td><html:text property="loginName" size="30"/></td></tr>
						<tr><td>Password:</td><td><html:password property="loginPwd" size="30"/></td></tr>
					</table>
</fieldset>
								<p align="right"><html:submit property="Log in" value="Log in"/></p>

</html:form>


</div>
<jsp:include page="jspincludes/footer.jsp"/>
</body></html>
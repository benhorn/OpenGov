<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>

<html lang="en">

<!-- Standard Content -->

<head>
  <title><bean:message key="admin.pageTitle"/></title> 
  <link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<!-- Body -->

<body>
<div id="header">
<h1><img src="images/logo.jpg" alt="State Records" width="247" height="70" /></h1>
</div>

<div class="loginForm">
<html:form action="auth" method="post">  
<table border="0" width="100%" cellspacing="5" align="center">
    <tr>
    <td>
     <table width="100%" border="0" cellspacing="2" cellpadding="5" STYLE="font-family: Arial; font-size: 10pt;">
     <tr><td colspan="2"><h1 STYLE="text-align:center; "><bean:message key="admin.systemName"/></h1></td>
    </tr>
     <tr><td colspan="2">
	 	<font color="red">
			<logic:messagesPresent>
   				<ul>
   					<html:messages id="error">
      				<li><bean:write name="error"/></li>
   					</html:messages>
   				</ul>
			</logic:messagesPresent>
			<logic:messagesPresent message="true">
   				<ul>
       				<html:messages id="msgs" message="true">
         				<li><bean:write name="msgs"/></li>
       				</html:messages>
   				</ul>
			</logic:messagesPresent>
		</font>
	 </td></tr>
	 <tr><td colspan="2" align="center">Please enter your login and password information to enter the system.</td></tr>
	 <tr><th align="right"><font><label for="username">User Name</label></font></th>
         <td align="left"><input type="text" name="userName" size="16"/></td>
      </tr>
    <tr><th align="right"><font><label for="password">Password</label></font></th>
        <td align="left"><input type="password" name="password" size="16"/></td>
    </tr>

    <!-- login reset buttons layout -->
    <tr>
       <td width="50%" valign="top">
       </td>
       <td width="50%" valign="top">
        <html:submit value="Login" property="login"/>&nbsp;&nbsp;&nbsp;&nbsp;<html:reset value="Reset" property="reset"/>
       </td>
     </tr>
  </table>
  <p> &nbsp;
  </td>
  </tr>
 </table>
</html:form>

<script language="JavaScript" type="text/javascript">
  <!--
    document.forms["loginForm"].elements["userName"].focus()
  // -->
</script>
</div>
</body>

<!-- Standard Footer -->



</html>

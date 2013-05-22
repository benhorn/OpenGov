<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-bean" prefix="bean"%> 
<%@ taglib uri="/struts-html" prefix="html"%> 
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html> 
	<head>
		<title></title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<link rel="stylesheet" type="text/css" href="css/displaytag.css" />		
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<style type="text/css">
			<!--
			body {
				background-color: lightgrey;
			}
			-->
		</style>
	</head>
<%
String memberList= new String("window.location.href='member.do?method=loadMember'");
%> 
<body>
<table align="center" cellpadding="0" cellspacing="5" style="background-color:lightgrey; border:0px;">
<logic:present name="member">
<html:form action="/member.do?method=switchAccount" method="post">
	<tr>
              <td width="160" height="30" align="right"><strong>Firstname:</strong></td>
              <td>&nbsp;${member.firstname}</td>
            </tr>
         <tr>
              <td width="160" height="30" align="right"><strong>Lastname:</strong></td>
              <td>&nbsp;${member.lastname}</td>
            </tr>
          <tr>
              <td width="160" height="30" align="right"><strong>Email:</strong></td>
              <td>&nbsp;${member.email}</td>
            </tr>
            <tr>
              <td width="160" height="30" align="right"><strong>Phone:</strong></td>
              <td>&nbsp;${member.phone}</td>
            </tr>
            <tr>
              <td width="160" height="30" align="right"><strong>Username:</strong></td>
              <td>&nbsp;${member.login}</td>
            </tr>
            <tr>
              <td width="160" height="30" align="right"><strong>Password:</strong></td>
              <td>&nbsp;${member.password}</td>
            </tr>
            <tr>
              <td width="160" height="30" align="right"><strong>Registration date:</strong></td>
              <td>&nbsp;${member.registrationDate}</td>
            </tr>
             <tr>
              <td width="160" height="30" align="right"><strong>Last login:</strong></td>
              <td>&nbsp;${member.lastLogin}</td>
            </tr>
            <tr>
              <td width="160" height="30" align="right"><strong>Login times:</strong></td>
              <td>&nbsp;${member.loginTimes}</td>
            </tr>
            <tr>
              <td width="160" height="30" align="right"><strong>Account status:</strong></td>
              <td>&nbsp;${member.activated}</td>
            </tr>
            <tr>
              <td width="160" height="30" align="right"><strong>Privileged:</strong></td>
              <td>&nbsp;${member.privileged}</td>
            </tr>
            <tr><td colspan="2"><html:hidden property="id" value="${member.memberId}"/></td></tr>
            <tr><td colspan="2" align="right">
			    <input type="button" onClick="<%=memberList%>" value="back"/>&nbsp;
			     <c:choose>
      					<c:when test="${member.activated=='y'}">
      							<input type="submit" name="action" onclick="return confirm('Are you sure to proceed?');" 
									   value="De-Activate this account"/>
						</c:when>
      					<c:otherwise>
      							<input type="submit" name="action" onclick="return confirm('Are you sure to proceed?');" 
									   value="Activate this account"/>
						</c:otherwise>
				</c:choose>
				<c:choose>
      					<c:when test="${member.privileged=='n'}">
      							<input type="submit" name="action" onclick="return confirm('Are you sure to proceed? Allowing privileged access to this member will bypass the moderation process.');" value="Flag this member as Privileged"/>
						</c:when>
      					<c:otherwise>
      							<input type="submit" name="action" onclick="return confirm('Are you sure to proceed?');" 
									   value="Unflag this member as Privileged"/>
						</c:otherwise>
				</c:choose>
			 </td></tr>
</html:form>
</logic:present>
</table>
  </body>
</html>
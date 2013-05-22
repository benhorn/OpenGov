<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>

<html>
<head>
<title></title>
<style type="text/css">
<!--
	body
	{
		margin: 0em;
	}
	.navPoint{
		FONT-SIZE: 9pt; CURSOR: hand; COLOR: black; FONT-FAMILY: Webdings
	}
	.main {
		font-size: 9px;
		background-color: #ffffff;
	}
//-->
</style>
</head>
<body>
<script type="text/javascript">
<!--
	if(self!=top){top.location=self.location;}
	function switchSysBar(){
		if (switchPoint.innerText==3){
			switchPoint.innerText=4;
			document.all("frmTitle").style.display="none";
		} else {
			switchPoint.innerText=3;
			document.all("frmTitle").style.display="";
		}
	}
//-->
</script>
<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
<tr><td id="frmTitle" valign="center" noWrap align="middle">
<script language="javascript">
<!--
	var table_width = 180;
	var left_url = "../adminMenu.jsp";
	
	if (navigator.appName=="Netscape") {
		left_url = "../adminMenu.jsp"; 
	}
	
	if(screen.availWidth == 800) {    
		document.write("<iframe id=menu style='z-index: 2; visibility: inherit; width: 230px; height: 100%' name=menu src='");
		document.write(left_url);
		document.write("'");
		document.write(" frameBorder=0 scrolling=no></iframe></td>");
	} else if (screen.availWidth >= 1024) {
		table_width = 180;
		document.write("<iframe id=menu style='z-index: 2; visibility: inherit; width: 230px; height: 100%' name=menu src='");
		document.write(left_url);
		document.write("'");
		document.write(" frameBorder=0 scrolling=no></iframe></td>");
	}
//-->
</script>
	<td style="width: 9pt" class="main">
		<table height="100%" border="0" cellpadding="0" cellspacing="0" background="../images/onOffLeft_back.jpg" style="border-left:1px; border-right:1px; border-color:#d4d0c8; border-style:solid;">
		<tr><td style="height: 100%" onclick="switchSysBar()">
			<font style="font-size: 9pt; cursor: default;" color="000000"><br><br><br><br><br><br><br><br><br><br><br><br><br>
			<span class="navPoint" style="font-size: 9pt;" id="switchPoint" title="Close / Open left Panel">3</span><br>
			<br><br><br><br><br><br><br>  
			</font><br>
		</td></tr>
		</table>
	</td>
	<td style="width: 100%">
		<iframe name="frame1" style="z-index:1; visibility:inherit; width:100%; height:100%; background-color:lightgrey;"  
			src="../admin_pages/background.jsp" frameBorder="0" scrolling="yes">
		</iframe>
	</td>
</tr></table>
</body>
</html>
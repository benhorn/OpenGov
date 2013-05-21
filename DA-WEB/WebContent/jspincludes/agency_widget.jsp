<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<input type="text" name="agencyNumber" id="agencyNumber" value=""/>
<%
String agency_name = request.getParameter("agencyName");
if(agency_name != null)
{
	JSONFactory JFactory = new JSONFactory();
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
     $("#agencyNumber").tokenInput("jspincludes/agencies.jsp", {
	 					preventDuplicates: true,
						tokenLimit:1,
    	                prePopulate:
<%
out.println(JFactory.AgenciesJSON(agency_name));
%>
    });
});
//-->
</script>
<%
}
else
{
%>
<script type="text/javascript">
<!--
	$(document).ready(function() {
		$("#agencyNumber").tokenInput("jspincludes/agencies.jsp", 
							{preventDuplicates: true,
							 tokenLimit: 1
							});
	});
//-->
</script>
<%
}
%>
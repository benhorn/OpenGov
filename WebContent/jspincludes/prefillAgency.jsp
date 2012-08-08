<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<input type="text" name="agencyNumber" id="agencyNumber" value=""/>
<%
	String agencyNo = request.getParameter("agency_number");
	String agencyName = request.getParameter("agencyName");
	String newAgencyNumber = "";
	JSONFactory JFactory = new JSONFactory();
	
	if (agencyName != null)
	{
	   FullAgencyListService fls = new FullAgencyListServiceImpl();
	   newAgencyNumber = fls.browseAgencyID(agencyName).get(0).toString(); 
	   agencyNo = newAgencyNumber;
	}
	
if (("undefined").equalsIgnoreCase(agencyNo.trim()))
{
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
     $("#agencyNumber").tokenInput("jspincludes/agencies.jsp", {
	 			    preventDuplicates: true,
				    tokenLimit:1,
    });
});
//-->
</script>
<%
}else{
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
     $("#agencyNumber").tokenInput("jspincludes/agencies.jsp", {
	 			    preventDuplicates: true,
				    tokenLimit:1,
    	            prePopulate:
<%
out.println(JFactory.tokenizedAgencyJSON(agencyNo.trim()));
%>
    });
});
//-->
</script>
<%
}
%>
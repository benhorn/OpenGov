<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<%@ page import="java.util.*" %>
<input type="text" name="agencyNumber" id="agencies" value=""/>
<%
	String agency = request.getParameter("agency");
	String newAgency = request.getParameter("newAgency");
	String pubID = request.getParameter("pubID");
	String newAgencyNumber = "";
	JSONFactory JFactory = new JSONFactory();
	FullAgencyListService fls = new FullAgencyListServiceImpl();
	
	if(agency != null)
	{
		if (!("").equals(newAgency))
		{
			   newAgencyNumber = fls.browseAgencyID(newAgency).get(0).toString(); 
			   newAgencyNumber = "," + newAgencyNumber;
		}
		agency = agency.concat(newAgencyNumber);
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
	$("#agencies").tokenInput("agency_pages/agencies.jsp", 
	  						 {preventDuplicates: true,
							  tokenLimit: 10,
							  prePopulate:
<%
out.println(JFactory.tokenizedAgencyJSON(agency.trim()));
%>
	 });
});
//-->
</script>
&nbsp;&nbsp;
<small><span class="tooltip" onclick="this.position=1; this.sticky=true; tooltip.add(this, 'new_agency', true); return false;">Can't find your agency?</span></small>
<%
	}
	else
	{
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
     $("#agencies").tokenInput("agency_pages/agencies.jsp", {
    	                	preventDuplicates: true,
							tokenLimit: 10,
							prePopulate:
<%
out.println(JFactory.prePopulatedAgency(pubID));
%>
    	            });
});
//-->
</script>
&nbsp;&nbsp;
<small><span class="tooltip" onclick="this.position=1; this.sticky=true; tooltip.add(this, 'new_agency', true); return false;">Can't find your agency?</span></small>
<%
	}
%>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<%@ page import="java.util.*" %>
<input type="text" name="publisherNumber" id="publisher" value=""/>
<%
	String publisher = request.getParameter("publisher");
	String newPublisher = request.getParameter("newPublisher");
	String newPublisherNumber = "";
	FullAgencyListService fls = new FullAgencyListServiceImpl();
	
	if(publisher != null)
	{
		JSONFactory JFactory = new JSONFactory();
		if (!("").equals(newPublisher))
		{
			   newPublisherNumber = fls.browseAgencyID(newPublisher).get(0).toString(); 
			   newPublisherNumber = "," + newPublisherNumber;
		}
		publisher = publisher.concat(newPublisherNumber);
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
	$("#publisher").tokenInput("agency_pages/publisher.jsp", 
	  						 {preventDuplicates: true,
							  tokenLimit: 10,
							  prePopulate:
<%
out.println(JFactory.tokenizedAgencyJSON(publisher.trim()));
%>
	 });
});
//-->
</script>
&nbsp;&nbsp;
<small><span class="tooltip" onclick="this.position=1; this.sticky=true; tooltip.add(this, 'new_publisher', true); return false;">Can't find your publisher?</span></small>
<%
	}
	else
	{
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
     $("#publisher").tokenInput("agency_pages/publisher.jsp", {
    	                	preventDuplicates: true,
							tokenLimit: 10
    	            });
});
//-->
</script>
&nbsp;&nbsp;
<small><span class="tooltip" onclick="this.position=1; this.sticky=true; tooltip.add(this, 'new_publisher', true); return false;">Can't find your publisher?</span></small>
<%
	}
%>
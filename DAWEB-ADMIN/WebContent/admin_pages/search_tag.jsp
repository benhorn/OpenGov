<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<%@ page import="java.util.*" %>
<input type="text" name="keywords" value="" id="keywords"/>
<%
	String keyword = request.getParameter("keyword");
	String newKeyword = request.getParameter("newKeyword");
	String newKeywordNumber = "";
	KeywordService ks = new KeywordServiceImpl();
	
	if(keyword != null)
	{
		JSONFactory JFactory = new JSONFactory();
		if (!("").equals(newKeyword))
		{
			newKeywordNumber = ks.browseKeywordID(newKeyword).get(0).toString(); 
			newKeywordNumber = "," + newKeywordNumber;
		}
		keyword = keyword.concat(newKeywordNumber);
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
     $("#keywords").tokenInput(<jsp:include page="./tag.jsp" flush="true"/>, {
	 					preventDuplicates: true,
    	                prePopulate:
<%
out.println(JFactory.keywordJSON(keyword));
%>
    });
});
//-->
</script>
&nbsp;&nbsp;
<small><span class="tooltip" onclick="this.position=1; this.sticky=true; tooltip.add(this, 'new_keyword', true); return false;">No matching keyword found?</span></small>
<%
	}
	else
	{
%>
<script type="text/javascript">
<!--
$(document).ready(function() {
     $("#keywords").tokenInput(<jsp:include page="./tag.jsp" flush="true"/>, {
    	                	preventDuplicates: true
    	            });
});
//-->
</script>
&nbsp;&nbsp;
<small><span class="tooltip" onclick="this.position=1; this.sticky=true; tooltip.add(this, 'new_keyword', true); return false;">No matching keyword found?</span></small>
<%
	}
%>
<%
	String searchQuery = request.getParameter("query");
	
	if (searchQuery == null || ("").equals(searchQuery)) 
	   searchQuery = "";
	
%>
<div class="searchBox">
<div class="opengov_search">
<table>
<tr><td width="100%" valign="top">
<form action="/search" name="searchForm" method="post">
		   <input title="Search OpenGov" type="text" id="searchText" name="query" value="<%=searchQuery%>"/>
		   <input class="opengov_search" name="searchButton" type="submit" value="Search"/>		   
</form>
</td>
</tr>

</table>
</div>
</div>
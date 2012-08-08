<%
	String selection = request.getParameter("selected");
	String selected = "class=\"selected\"";
	String search="";
	String about ="";
	String register="";
	String login = "";
	if (("search").equalsIgnoreCase(selection))
		search = selected;
	if (("about").equalsIgnoreCase(selection))
		about = selected;
	if (("login").equalsIgnoreCase(selection))
		login = selected;
%>

<ul class="Menu">
    <li <%=search%>>
      <a href="search" title="Search">Search</a>
    </li>
    <li <%=about%>>
      <a href="about" title="About">About</a>
    </li>
    <li>
      <a href="http://data.nsw.gov.au" target="_blank" title="Data">Data</a>
    </li>
    <li>
      <a href="http://www.sdi.nsw.gov.au/GPT9/catalog/main/home.page" target="_blank" title="Spatial">Spatial</a>
    </li>
    <li>
      <a href="http://www.records.nsw.gov.au/" target="_blank "title="State Records">State Records</a>
    </li>
    <li>
      <a href="http://www.oic.nsw.gov.au/" target="_blank" title="OIC">OIC</a>
    </li>
</ul>  
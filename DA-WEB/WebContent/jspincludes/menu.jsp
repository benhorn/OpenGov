<%
	String selection = request.getParameter("selected");
	String selected = "class=\"selected\"";
	String main="";
	String about ="";
	String register="";
	String login = "";
	if (("main").equalsIgnoreCase(selection))
		main = selected;
	if (("about").equalsIgnoreCase(selection))
		about = selected;
	if (("login").equalsIgnoreCase(selection))
		login = selected;
%>

<ul class="Menu">
    <li <%=main%>>
      <a href="main" title="Search OpenGov">Search</a>
    </li>
    <li <%=about%>>
      <a href="about" title="About OpenGov">About</a>
    </li>
    <li>
      <a href="http://data.nsw.gov.au" target="_blank" title="NSW data catalogue">Data</a>
    </li>
    <li>
      <a href="http://www.sdi.nsw.gov.au/GPT9/catalog/main/home.page" target="_blank" title="NSW spatial data catalogue">Spatial</a>
    </li>
    <li>
      <a href="http://www.records.nsw.gov.au/" target="_blank" title="Search archival information">State Records</a>
    </li>
    <li>
      <a href="http://www.ipc.nsw.gov.au/" target="_blank" title="IPC">IPC</a>
    </li>
</ul>  
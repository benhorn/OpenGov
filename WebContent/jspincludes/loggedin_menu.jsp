<%@ taglib uri="/struts-bean" prefix="bean"%>
<%
	String selection = request.getParameter("method");
	String text = "class=\"selected\"";
	String my_pub="";
	String my_details="";
	
	if (("home").equalsIgnoreCase(selection))
		my_pub = text;
	if (("myDetails").equalsIgnoreCase(selection))
		my_details = text;
%>

<ul class="Menu">
    <li <%=my_pub%>>
      <a href="my_publications" title="My publications">My publications</a>
    </li>
    <li <%=my_details%>>
      <a href="my_details" title="My details">My details</a>
    </li>
  </ul>  
  <ul class="Breadcrumbs Menu">
      <li class="Breadcrumb Start">
        <a href="#">
          <span><logic:present name="member"> Welcome&nbsp;
  				${member.firstname}&nbsp;${member.lastname}
   				</logic:present>
   		  </span>
        </a>
      </li>
</ul>
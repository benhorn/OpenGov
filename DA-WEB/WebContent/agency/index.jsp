<%if (("application/json").equalsIgnoreCase(request.getHeader("accept")))
{String redirectURL = "/agency.do?method=agencyJSON";
 response.sendRedirect(redirectURL);}
%>

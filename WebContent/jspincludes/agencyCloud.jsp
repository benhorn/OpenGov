<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="org.mcavallo.opencloud.*" %>
<%@ page import="java.util.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>

<%
Cloud cloud = new Cloud();
FullAgencyListService fls = new FullAgencyListServiceImpl();
List<AgencyPublication> apList = fls.loadAgencyWithPublication();
FullAgencyList fullAgencyList = null;
AgencyPublication agencyPublication = null;


// Sets the number of tag to display in the cloud
cloud.setMaxTagsToDisplay(30);

// so we set the maximum weight to 10.0.
cloud.setMaxWeight(9.0);

// Sets a default url to assign to tags.
// The format specifier %s will be substituted by the tag name
cloud.setDefaultLink("/DA-WEB/agency/%s");

List<String> counterList = new ArrayList<String>();
Set<String> unique = new HashSet<String>(counterList); 

Iterator iter = apList.iterator();
int j = 0;
while(iter.hasNext())
{
	agencyPublication = (AgencyPublication)iter.next();
	fullAgencyList = fls.loadFullAgencyList(agencyPublication.getFullAgencyList().getFullAgencyListId());
	counterList.add(fullAgencyList.getAgencyName());
	cloud.addTag(new Tag(fullAgencyList.getAgencyName().trim(), "/DA-WEB/agency/" + fullAgencyList.getFullAgencyListId().toString(), 1.0));
	j++;
}


%>
<div class="tagcloud" style="margin: auto; width: 80%;">
<%
// Cycles through the output tags
for (Tag tag : cloud.tags()) {
%>
	<a href="<%=tag.getLink()%>" class="t<%= tag.getWeightInt() %>"><%= tag.getName() %></a>
<%
}
%>
</div>

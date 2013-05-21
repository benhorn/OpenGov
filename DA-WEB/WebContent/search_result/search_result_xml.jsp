<%@ page contentType="application/atom+xml; charset=utf-8" %>
<%@ taglib uri="/struts-bean" prefix="bean" %>
<%@ taglib uri="/struts-html" prefix="html" %>
<%@ taglib uri="/struts-logic" prefix="logic" %>
<%@ page import="au.gov.nsw.records.digitalarchive.search.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.servlet.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.lucene.facet.search.*" %>
<%@ page import="org.apache.lucene.facet.taxonomy.*" %>
<%@ page import="org.apache.lucene.facet.search.results.FacetResult"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.lucene.facet.search.params.*"%>
<%@ page import="org.apache.lucene.facet.index.params.*"%>
<%@ page import="org.apache.lucene.facet.search.results.FacetResultNode"%>
<%!
public String trimDoubleQuotes(String text) {
    int textLength = text.length();

    if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
      return text.substring(1, textLength - 1);
    }

    return text;
 }
%>
<%
String text = trimDoubleQuotes(request.getParameter("query"));
text = "\""+text+"\"";
OpenGovLucene ogl = new OpenGovLucene();
FacetIndexingParams indexingParams = new DefaultFacetIndexingParams();
FacetSearchParams facetSearchParams = new FacetSearchParams(indexingParams);
facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("agency"), 10));
facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("type"), 10));
facetSearchParams.addFacetRequest(new CountFacetRequest(new CategoryPath("publication_year"), 10));
OpenGovSearchResult results = null;
int pageNumber = 1;
int iShowRows=2400;
String agency = request.getParameter("agency"); if (StringUtils.isEmpty(agency) || ("null").equals(agency)){agency="";}
String type =  request.getParameter("type"); if (StringUtils.isEmpty(type) || ("null").equals(type)){type="";}
String startYear = request.getParameter("from"); if (StringUtils.isEmpty(startYear) || ("null").equals(startYear)){startYear="";}
String endYear = request.getParameter("to"); if (StringUtils.isEmpty(endYear) || ("null").equals(endYear)){endYear="";}
String thisURL = request.getRequestURL().toString();
String condition = request.getParameter("condition");if (StringUtils.isEmpty(condition) || ("null").equals(condition)){condition = "relevance";}
LuceneSearchParams params = new LuceneSearchParams(text, facetSearchParams, agency, type, startYear, endYear, pageNumber, iShowRows);
results = ogl.search(params, condition);
%>
 <feed xmlns="http://www.w3.org/2005/Atom" 
       xmlns:opensearch="http://a9.com/-/spec/opensearch/1.1/">
   <title>OpenGov NSW search results</title> 
   <link href="https://www.opengov.nsw.gov.au/search.xml?query=<%=trimDoubleQuotes(text)%>"/>
   <updated><%=new Date()%></updated>
   <author> 
     <name>OpenGov NSW</name>
   </author> 
   <id>https://www.opengov.nsw.gov.au/</id>
   <opensearch:totalResults><%=ogl.getNumTotalHits()%></opensearch:totalResults>
   <opensearch:startIndex>1</opensearch:startIndex>
   <opensearch:itemsPerPage>2400</opensearch:itemsPerPage>
   <opensearch:Query role="request" searchTerms=<%=text%> startPage="1"/>
   <link rel="self" href="https://www.opengov.nsw.gov.au/search.xml?query=<%=trimDoubleQuotes(text)%>" type="application/atom+xml"/>
   <link rel="search" type="application/opensearchdescription+xml" href="https://www.opengov.gov.nsw.au/search"/>
   <%
   FileService fs = new FileServiceImpl();
	MemberService ms = new MemberServiceImpl();
	FullAgencyListService fls = new FullAgencyListServiceImpl();
	Set<Map.Entry<String, String>> resultEntities = results.getResults().entrySet();

	for(Map.Entry<String, String> resultEntity : resultEntities ) 
	{
        String key = resultEntity.getKey();
        String value = resultEntity.getValue();
        if (key != null)
        {
    		UploadedFile uFile = fs.loadFile(Integer.parseInt(key));
			Publication pub = fs.findPubViaFile(Integer.parseInt(key));		
   %>
   <entry>
     <title><%=pub.getTitle()%></title>
     <link href="https://www.opengov.nsw.gov.au/publication/<%=uFile.getFileId()%>"/>
     <id>https://www.opengov.nsw.gov.au/publication/<%=uFile.getFileId()%></id>
     <updated><%=new Date()%></updated>
   </entry>
   <%
   }
   }
   %>
 </feed>

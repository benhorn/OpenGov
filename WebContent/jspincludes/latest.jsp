<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<%@ page import="java.util.*" %>
<%
	PublicationService ps = new PublicationServiceImpl();
	List<Publication> pList = ps.browsePublication();
	for (int i = 0; i < 5; i++)
	{
%> 
     <div class="SearchResultItem">
      <a href="http://publications.nsw.gov.au/1525-office-for-children-annual-report-fy2008"
         title="Office For Children Annual Report FY2008">
        <img src="/pub/561/f50/561f50caaa686b6894d56d9fa887bf1c53a14dab/thumbnail.gif" />
      </a>
      <div class="SearchResultDetail">
        <ul class="SearchResultDetailHeader">
          <li>
            <h3>
              <a href="http://publications.nsw.gov.au/1525-office-for-children-annual-report-fy2008"
                 title="<%=pList.get(i).getTitle()%>"><%=pList.get(i).getTitle()%></a>
            </h3>
          </li>
        </ul>
        <dl>
          <dt>Published:</dt>
          <dd>30 Jun 2008</dd>
          <dt>Pages:</dt>
          <dd>40</dd>
          <dt>Type:</dt>
          <dd><%=pList.get(i).getType()%></dd>
        </dl>
      </div>
      <div class="ClearFloating Hline"></div>
    </div>
<%
	}
%>
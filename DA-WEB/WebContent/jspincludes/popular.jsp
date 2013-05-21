<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.service.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.ORM.*" %>
<%@ page import="au.gov.nsw.records.digitalarchive.base.*" %>
<%@ page import="java.util.*" %>
<%@ page import="gov.loc.repository.pairtree.Pairtree" %>
<%@ page import="java.io.File" %>

<%
	FileService fs = new FileServiceImpl();
	MemberService ms = new MemberServiceImpl();
	PublicationService ps = new PublicationServiceImpl();
	FullAgencyListService fas = new FullAgencyListServiceImpl();
	List<Publication> pList = ps.mostPopularPublication();
	Iterator<Publication> it = pList.iterator();
	int counter = 1;
		while(it.hasNext() && counter < 6)
		{
			Publication pub = (Publication)it.next();
			UploadedFile uFile = fs.loadFileViaOrder(1, pub.getPublicationId());
			List<FullAgencyList> faList = fas.loadAgencyViaPublication(pub.getPublicationId().toString());
			Pairtree pt = new Pairtree();
			String thumbName = Constants.PAIRTREE_ROOT + pt.mapToPPath(uFile.getUid()) + File.separator + "obj" + File.separator + "thumbnail.gif";					
%>
 <div class="SearchResultItem">
      <a href="/publication/<%=uFile.getFileId()%>"
         title="<%=uFile.getFileName()%>">
         <%if (new File(thumbName).exists()) {%>
        <img src="/thumb?uid=<%=uFile.getUid()%>" title="<%=pub.getTitle()%>"/>
           <%} %>
      </a>
      <div class="SearchResultDetail">
        <ul class="SearchResultDetailHeader">
          <li>
            <h3>
              <a href="/publication/<%=uFile.getFileId()%>"
                 title="<%=pub.getTitle()%>"><%=pub.getTitle()%></a>
            </h3><small>popularity: <%=pub.getPopularity()%></small>
          </li>
        </ul>
        <dl>
          <dt>Agency:</dt>
          <dd><% Iterator<FullAgencyList> aIterator = faList.iterator(); 
	  	  	 while(aIterator.hasNext())
	  	     {
	  	  		FullAgencyList fal = (FullAgencyList)aIterator.next();
	  	  		out.println(fal.getAgencyName());
	  	  	 }
	  	  	%></dd>
          <dt>Published:</dt>
          <dd><%=pub.getDatePublishedDisplay()%></dd>
          <dt>Pages:</dt>
          <dd><%=pub.getTotalPages()%></dd>
          <dt>Type:</dt>
          <dd class="End"><%=pub.getType()%></dd>
        </dl>
      </div>
      <div class="ClearFloating Hline"></div>
    </div>		
<%			
	counter = counter + 1;
		}
%>
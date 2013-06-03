package au.gov.nsw.records.digitalarchive.struts.action;

import gov.loc.repository.pairtree.Pairtree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import au.gov.nsw.records.digitalarchive.ORM.AgencyPublication;
import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Keyword;
import au.gov.nsw.records.digitalarchive.ORM.KeywordPublication;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.PublisherPublication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.base.Constants;
import au.gov.nsw.records.digitalarchive.base.NYTimesJSONFormat;
import au.gov.nsw.records.digitalarchive.base.PublicationJSONFormat;
import au.gov.nsw.records.digitalarchive.notification.EmailNotificationAction;
import au.gov.nsw.records.digitalarchive.pdf.ConvertPagesToHiResImages;
import au.gov.nsw.records.digitalarchive.pdf.PDFBoxWrapper;
import au.gov.nsw.records.digitalarchive.pdf.PDFImageGenerator;
import au.gov.nsw.records.digitalarchive.search.OpenGovLuceneIndexer;
import au.gov.nsw.records.digitalarchive.search.TikaWrapper;
import au.gov.nsw.records.digitalarchive.service.AgencyPublicationService;
import au.gov.nsw.records.digitalarchive.service.AgencyPublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;
import au.gov.nsw.records.digitalarchive.service.KeywordPublicationService;
import au.gov.nsw.records.digitalarchive.service.KeywordPublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.service.KeywordService;
import au.gov.nsw.records.digitalarchive.service.KeywordServiceImpl;
import au.gov.nsw.records.digitalarchive.service.MemberService;
import au.gov.nsw.records.digitalarchive.service.MemberServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublisherPublicationService;
import au.gov.nsw.records.digitalarchive.service.PublisherPublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.struts.form.PublicationForm;
import au.gov.nsw.records.digitalarchive.system.AUDate;
import au.gov.nsw.records.digitalarchive.system.LuceneDateFormatter;
public class PublicationAction extends BaseAction 
{
	LuceneDateFormatter ldf = new LuceneDateFormatter();
	
	public PublicationAction()
	{}
	
	
	public ActionForward newPublication(ActionMapping mapping, 
	 		    						ActionForm form, 
	 		    						HttpServletRequest request, 
	 		    						HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			Member member = (Member)request.getSession().getAttribute("member");
			if (member == null)
			{
				forward = mapping.findForward("login");	
			}else
			{	
				PublicationService ps = new PublicationServiceImpl();
				Publication publication = new Publication();
				publication.setStatus(Constants.PUBLICATION_DRAFT);
				publication.setMember(member);
				publication.setDateRegistered(new Date());
				publication.setLastUpdated(new Date());
				boolean status = ps.addPublication(publication);
				if (status)
				{
					publication = ps.loadPublication(publication.getPublicationId());
			        saveToken(request);
					forward = new ActionForward("/agency_pages/newPublication.jsp?id=" + publication.getPublicationId());
				}else
				{
					forward = mapping.findForward("home");
				}
			}
		}catch(Exception ex)
		{
			logger.info("In class PublicationAction:newPublication()\n");
			ex.printStackTrace();
		}
			return forward;
	}
	
	public ActionForward addPublication(ActionMapping mapping, 
			 				 		    ActionForm form, 
			 				 		    HttpServletRequest request, 
			 				 		    HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			Member member = (Member)request.getSession().getAttribute("member");
			if (member == null)
			{
				forward = mapping.findForward("login");	
			}else
			{	
				
				String action = request.getParameter("action");
				
				PublicationForm publicationForm = (PublicationForm)form;
				MemberService ms = new MemberServiceImpl();
				KeywordService ks = new KeywordServiceImpl();
				PublicationService ps = new PublicationServiceImpl();
				FullAgencyListService fls = new FullAgencyListServiceImpl();
				AgencyPublicationService aps = new AgencyPublicationServiceImpl();
				KeywordPublicationService kps = new KeywordPublicationServiceImpl();
				PublisherPublicationService pps = new PublisherPublicationServiceImpl();
								
				Publication publication = ps.loadPublication(Integer.parseInt(publicationForm.getPublicationId()));
				
				String publishDay = publicationForm.getPublication_date().trim(); 
				String publishMonth = publicationForm.getPublication_month().trim();
				String publishYear = publicationForm.getPublication_year().trim();
				
				if (!StringUtils.isBlank(publishDay))
					publishDay = publishDay.concat("/");
				if (!StringUtils.isBlank(publishMonth))
					publishMonth = publishMonth.concat("/");
				
				publication.setTitle(publicationForm.getTitle().trim());
				publication.setDescription(publicationForm.getDescription().trim());
				
				String publicationDate = publishDay + publishMonth + publishYear;
				String publicationDateRaw = ldf.formatDate(publicationDate);
				publication.setDatePublishedRaw(publicationDateRaw);
				publication.setDatePublishedDisplay(publicationDate);
				publication.setType(publicationForm.getType().trim());
				publication.setTypeOther(publicationForm.getType_other().trim());				
				publication.setCreativeCommons(publicationForm.getCreativeCommons());
				publication.setCoverage(publicationForm.getCoverage().trim());
				publication.setLanguage(publicationForm.getLanguage().trim());
				publication.setRights(publicationForm.getRights().trim());
				publication.setTotalPages(publicationForm.getTotalPages());
				if ("Submit publication".equalsIgnoreCase(action))
				{
					publication.setTotalPages(Integer.toString(ps.countTotalPages(publication)));
					publication.setStatus(Constants.PUBLICATION_SUBMITTED);
					FullAgencyListService fas = new FullAgencyListServiceImpl();
					Member currentMember = ms.loadMember(publication.getMember().getMemberId());
					FullAgencyList faList = fas.loadFullAgencyList(currentMember.getFullAgencyList().getFullAgencyListId());
				    List<String> recipient =  new ArrayList<String>();
					recipient.add("grace.hui@records.nsw.gov.au");
				    recipient.add("opengov@records.nsw.gov.au");
					EmailNotificationAction mailSender = new EmailNotificationAction();
					mailSender.prepare(0, request);
					mailSender.sendEmail(recipient, "A new publication has been submitted to OpenGov!", 
													"Hi:<br/>" +
													"<p>" + currentMember.getFirstname() + " " + currentMember.getLastname() + 
													" from " + faList.getAgencyName()  + " has submitted a new publication and " +
													"is <a href=\"https://www.opengov.nsw.gov.au/admin\">there</a> waiting" +
													"to be approved.");
				}
				if ("Publish".equalsIgnoreCase(action))
				{
					publication.setTotalPages(Integer.toString(ps.countTotalPages(publication)));
					FileService fs = new FileServiceImpl();
					OpenGovLuceneIndexer ogl = null;
					List<UploadedFile> uList = fs.browseFiles(publication);
					Iterator<UploadedFile> UploadedFileIterator = uList.iterator();

					while (UploadedFileIterator.hasNext())
					{
						UploadedFile uFile = (UploadedFile)UploadedFileIterator.next();
						List<String> agencyNameList = new ArrayList<String>();
						List<String> keywordList = new ArrayList<String>();
						String[] agencyList = splitAgencies(publicationForm.getAgencyNumber().trim());
						String[] keyWDs = splitKeyword(publicationForm.getKeywords().trim());
						for(int j = 0; j < agencyList.length; j++)
						{
							agencyNameList.add(fls.loadFullAgencyList(Integer.valueOf(agencyList[j])).getAgencyName());
						}
						for(int w = 0; w < keyWDs.length; w++)
						{
							keywordList.add(ks.loadKeyword(Integer.valueOf(keyWDs[w])).getKeyword());
						}
						File inboxFile = new File(uFile.getInboxUrl());
						Pairtree pt = new Pairtree();
						String pairTreeURL = Constants.PAIRTREE_ROOT  + pt.mapToPPath(uFile.getUid()) + File.separator + "obj" + File.separator;
						String imageLocation = pairTreeURL  + "images";
						String textLocation = pairTreeURL + "txt";

						new File(imageLocation).mkdirs();
						new File(textLocation).mkdirs();

						File pairTreeDIR = new File(pairTreeURL);
						File pairTreePDF = new File(pairTreeURL + "document.pdf");
						renameFile (inboxFile, pairTreePDF);
						Thread.sleep(1000);
						fs.deletePhysicalFiles(uFile); // cleans up the inbox
						Thread.sleep(1000);
						fs.cleanUpInbox(uFile);
//						System.out.println("\n\n\n"+inboxFile.getAbsolutePath().substring(0, inboxFile.getAbsolutePath().length() - uFile.getFileName().length()));
//						FileUtils.deleteDirectory(new File(inboxFile.getAbsolutePath().substring(0, inboxFile.getAbsolutePath().length() - uFile.getFileName().length())));

						/***For migration only**/
						TikaWrapper ta = new TikaWrapper();
						Integer pageNumber = Integer.parseInt(ta.getPageNumber(pairTreeURL + "document.pdf"));
						uFile.setPages(pageNumber);
						uFile.setSize(formatSize(Double.parseDouble(ta.getFileSize(pairTreeURL + "document.pdf"))));

						//String hostURL = new URLGenerator(new HostNameFinder().getMyCanonicalHostName()).getHostURL();
						String hostURL = "https://www.opengov.nsw.gov.au/";

						NYTimesJSONFormat NYTimesJSON = new NYTimesJSONFormat(uFile.getFileName().substring(0, uFile.getFileName().length()-4),
																			  uFile.getFileName().substring(0, uFile.getFileName().length()-4),
																			  pageNumber.toString(),
																			  "",
																			  hostURL.concat("doc_opener?uid=").concat(uFile.getUid()),
																			  hostURL.concat("publication/").concat(uFile.getFileId().toString()),
																			  hostURL.concat("txt_opener?uid=").concat(uFile.getUid()),
																			  hostURL.concat("thumb?uid=").concat(uFile.getUid()),
																			  hostURL.concat("doc_search?q={query}&uid=").concat(uFile.getUid()),
																			  hostURL.concat("txt_opener?uid=").concat(uFile.getUid())+"&counter={page}",
																			  hostURL.concat("pdf_image_opener?uid=").concat(uFile.getUid())+"&counter={page}");

						uFile.setReaderJson(NYTimesJSON.toJSONString());
						uFile.setPairtreeUrl(pairTreeURL + "document.pdf");
						fs.updateFile(uFile);

						PDFImageGenerator PDFGen = new PDFImageGenerator();
						PDFBoxWrapper pbw = new PDFBoxWrapper();
						ConvertPagesToHiResImages CRT= new ConvertPagesToHiResImages(); 
						PDDocument pd = PDDocument.load(uFile.getPairtreeUrl());

						if (!pd.isEncrypted())
						{
							PDFGen.generateThumbnailImage(uFile.getPairtreeUrl(), pairTreeURL);
							CRT.ConvertImages("png", uFile.getPairtreeUrl(), imageLocation, "800", "997");
							pbw.createTxtFile(uFile.getPairtreeUrl(), pairTreeURL.concat("document.txt"));
							int pageNumbers = pd.getNumberOfPages();
							for (int i = 1; i <= pageNumbers; i++)
							{
								BufferedWriter out = new BufferedWriter(new FileWriter(textLocation + File.separator + "page" + i + ".txt"));
								PDFTextStripper stripper = new PDFTextStripper();
								stripper.setSortByPosition(true);
								stripper.setStartPage(i);
								stripper.setEndPage(i+1);
								String s = stripper.getText(pd);
								out.write(s);
								out.close();
							}
							if (pd != null) {
								pd.close();
							}
							// I use close() to flush the stream.
	
							List<Keyword> kList = ks.loadKeywordViaPublication(publication.getPublicationId().toString());
							Iterator<Keyword> kIterator = kList.iterator();

							StringBuilder keywordString = new StringBuilder();

							while(kIterator.hasNext())
							{
								Keyword keywords = (Keyword)kIterator.next();
								keywordString.append(keywords.getKeyword()).append(',');
							}

							ogl = new OpenGovLuceneIndexer(publication.getTitle(), 
														   publication.getDescription(), 
														   publicationForm.getKeywords().trim(), 
														   agencyNameList,
														   publication.getDatePublishedRaw(),
														   publication.getType(),
														   publication.getCoverage(),
														   publication.getRights());			  	  	
							ogl.createIndex(uFile.getFileId().toString(), uFile.getFileName(), uFile.getPairtreeUrl(), uFile.getContentType());
						}
						publication.setStatus(Constants.PUBLICATION_PUBLISHED);
						publication.setLastViewed(new Date());
						publication.setDateApproved(new Date());
						publication.setViewedTimes(1);
					}
					Member currentMember = ms.loadMember(publication.getMember().getMemberId());
					UploadedFile aFile = fs.loadFileViaOrder(1, publication.getPublicationId());
					EmailNotificationAction mailSender = new EmailNotificationAction();
					List<String> recipient =  new ArrayList<String>();
					recipient.add(currentMember.getEmail());
					recipient.add("opengov@records.nsw.gov.au");
					mailSender.prepare(0, request);
					mailSender.sendEmail(recipient, "Your publication is now live!", 
								"Hi " + currentMember.getFirstname() + ":<br/>" +
								"<p>Your publication " + publication.getTitle() + " is now live.</p>" +
								"<p>You may view it at https://www.opengov.nsw.gov.au/publication/" + aFile.getFileId() +"</p>" + 
							    "<p>If you have any questions please contact us at opengov@records.nsw.gov.au</p>");
				}
				ps.updatePublication(publication);
				
				String[] keyword = splitKeyword(publicationForm.getKeywords().trim());
				String[] agencies = splitAgencies(publicationForm.getAgencyNumber().trim());
				String[] publisher = splitAgencies(publicationForm.getPublisherNumber().trim());
				publication = ps.loadPublication(publication.getPublicationId());
		
				for(int i = 0; i < keyword.length; i++)
				{
					KeywordPublication keywordPublication = new KeywordPublication();
					keywordPublication.setKeyword(ks.loadKeyword(Integer.valueOf(keyword[i])));
					keywordPublication.setPublication(publication);
		            kps.addKeywordPublication(keywordPublication);
				}
				
				for(int j = 0; j < agencies.length; j++)
				{
					AgencyPublication agencyPublication = new AgencyPublication();
					agencyPublication.setFullAgencyList(fls.loadFullAgencyList(Integer.valueOf(agencies[j])));
					agencyPublication.setPublication(publication);
					aps.addAgencyPublication(agencyPublication);
				}
				
				for(int k = 0; k < publisher.length; k++)
				{
					PublisherPublication publisherPublication = new PublisherPublication();
					publisherPublication.setFullAgencyList(fls.loadFullAgencyList(Integer.valueOf(publisher[k])));
					publisherPublication.setPublication(publication);
					pps.addPublisherPublication(publisherPublication);
					
				}
				forward = mapping.findForward("home");
			}
		}catch(Exception ex)
		{
			logger.info("In class PublicationAction:addPublication()\n");
			ex.printStackTrace();
		}
			return forward;
	}
	
	/*
	 * Publication opener, redirects to the viewer
	 * Does not require log in.
	 */
	public ActionForward openPublication(ActionMapping mapping,
										 ActionForm form,
										 HttpServletRequest request,
										 HttpServletResponse response)
	{
		ActionForward forward = null;
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			Integer fileID = new Integer(request.getParameter("id"));
			try
			{
				FileService fs = new FileServiceImpl();
				PublicationService ps = new PublicationServiceImpl();
				Publication publication = fs.findPubViaFile(fileID);
				AUDate au = new AUDate();
				Calendar startDate = Calendar.getInstance();startDate.setTime(publication.getLastViewed());  
				Calendar today = Calendar.getInstance();today.setTime(new Date());
				long daysLapsed = au.daysBetween(startDate, today);
				if (daysLapsed == 0)
					daysLapsed = 1;
				long popularity = publication.getViewedTimes() / daysLapsed;
				publication.setPopularity(Double.longBitsToDouble(popularity));
				publication.setViewedTimes(Integer.valueOf(publication.getViewedTimes() + 1));
				publication.setLastViewed(new Date());
				boolean status = ps.updatePublication(publication);
				if (status)
					forward = new ActionForward("/publication/index.jsp?id=" + fileID);
				else
					forward = mapping.findForward("index");
			 }catch(Exception ex)
			 {
				logger.info("In class PublicationAction:openPublication()\n");
				ex.printStackTrace();
				forward = mapping.findForward("index");
			 }
		}else
		{
			forward = mapping.findForward("index");
		}
		return forward;
	}
	
	/*
	 * Open the publication in text format
	 */
	public ActionForward publicationText(ActionMapping mapping,
										 ActionForm form,
										 HttpServletRequest request,
										 HttpServletResponse response)
	{
		ActionForward forward = null;
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			Integer fileID = new Integer(request.getParameter("id"));
			try
			{
				FileService fs = new FileServiceImpl();
				UploadedFile uFile = fs.loadFile(fileID);
				forward = new ActionForward("/txt_opener?uid=" + uFile.getUid());
			 }catch(Exception ex)
			 {
				logger.info("In class PublicationAction:publicationText()\n");
				ex.printStackTrace();
			 }
		}else
		{
			forward = mapping.findForward("index");
		}
		return forward;
	}
	
	/*
	 * Open the publication in JSON
	 */
	public ActionForward publicationJSON(ActionMapping mapping,
										 ActionForm form,
										 HttpServletRequest request,
										 HttpServletResponse response)
	{
		ActionForward forward = null;
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			Integer fileID = new Integer(request.getParameter("id"));
			try
			{
				FileService fs = new FileServiceImpl();
				KeywordService ks = new KeywordServiceImpl();
				PublicationService ps = new PublicationServiceImpl();
				FullAgencyListService fas = new FullAgencyListServiceImpl();
				PublisherPublicationService pps = new PublisherPublicationServiceImpl();
				
				Publication thisPub = fs.findPubViaFile(fileID);

				List<FullAgencyList> agencyList = fas.loadAgencyViaPublication(thisPub.getPublicationId().toString());
				Iterator<FullAgencyList> aIterator = agencyList.iterator();
				List<String> agencyNameList = new ArrayList<String>();
				while(aIterator.hasNext())
		  	    {
		  	  		FullAgencyList fal = (FullAgencyList)aIterator.next();
		  	  		agencyNameList.add(fal.getAgencyName());
		  	    }
		  	  	
		  	  	List<FullAgencyList> publisherList = pps.loadPublisherViaPublication(thisPub.getPublicationId().toString());
				Iterator<FullAgencyList> pIterator = publisherList.iterator();
				List<String> publisherNameList = new ArrayList<String>();
	  	  		while(pIterator.hasNext())
	  	  		{
		  	  		FullAgencyList pal = (FullAgencyList)pIterator.next();
		  	  		publisherNameList.add(pal.getAgencyName());
	  	  		}
				
				List<Keyword> keywordList = ks.loadKeywordViaPublication(thisPub.getPublicationId().toString());
				Iterator<Keyword> kIterator = keywordList.iterator();
				List<String> keywordNameList = new ArrayList<String>();
	  	  		while(kIterator.hasNext())
	  	  		{
	  	  			Keyword keyword = (Keyword)kIterator.next();
	  	  			keywordNameList.add(keyword.getKeyword());
	  	  		}
		  	  	
	  	  		List<UploadedFile> fileList = ps.listFilesViaPublication(thisPub); 
	  	  		Iterator<UploadedFile> ufileIterator = fileList.iterator();
	  	  		List<String> fileURLList = new ArrayList<String>();
				while (ufileIterator.hasNext()) 
				{
					UploadedFile uFile = (UploadedFile)ufileIterator.next();
					fileURLList.add(new URI("https", "www.opengov.nsw.gov.au", "/publication/" + uFile.getFileId().toString(), null, null).toASCIIString());
					
				}
	  	  		
				PublicationJSONFormat PJSON = new PublicationJSONFormat(thisPub.getPublicationId().toString(),
																		thisPub.getTitle(), agencyNameList, 
																		thisPub.getType(), 
																		thisPub.getDatePublishedDisplay(), publisherNameList, 
																		keywordNameList, 
																		thisPub.getDescription(), 
																		thisPub.getLanguage(), 
																		thisPub.getCoverage(), 
																		thisPub.getRights(),
																		thisPub.getViewedTimes().toString(),
																		thisPub.getLastViewed().toString(),
																		fileURLList);
				request.getSession().setAttribute("JSONString", PJSON.toJSONString().replace("\\/", "/").trim());
				forward = new ActionForward("/publication/publicationJSON.jsp");
			 }catch(Exception ex)
			 {
				logger.info("In class PublicationAction:publicationJSON()\n");
				ex.printStackTrace();
			 }
		}else
		{
			forward = mapping.findForward("index");
		}
		return forward;
	}
	
	public ActionForward allPublicationJSON (ActionMapping mapping, 
				 							 ActionForm form,
				 							 HttpServletRequest request,
				 							 HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			PublicationService ps = new PublicationServiceImpl();
			List<Publication> pList = ps.lastestPublication(); //latest published publications
			Iterator<Publication> pIterator = pList.iterator();
			int listSize = pList.size();
			int i = 0;
			
			StringBuilder sb = new StringBuilder();
			sb.append("{[");
			
			while (pIterator.hasNext()) 
			{
				Publication publication = (Publication) pIterator.next();
				
				KeywordService ks = new KeywordServiceImpl();
				FullAgencyListService fas = new FullAgencyListServiceImpl();
				PublisherPublicationService pps = new PublisherPublicationServiceImpl();

				List<FullAgencyList> agencyList = fas.loadAgencyViaPublication(publication.getPublicationId().toString());
				Iterator<FullAgencyList> aIterator = agencyList.iterator();
				List<String> agencyNameList = new ArrayList<String>();
				while(aIterator.hasNext())
		  	    {
		  	  		FullAgencyList fal = (FullAgencyList)aIterator.next();
		  	  		agencyNameList.add(fal.getAgencyName());
		  	    }
		  	  	
//		  	  	List<FullAgencyList> publisherList = pps.loadPublisherViaPublication(publication.getPublicationId().toString());
//				Iterator<FullAgencyList> publisherIterator = publisherList.iterator();
				List<String> publisherNameList = new ArrayList<String>();
//	  	  		while(pIterator.hasNext())
//	  	  		{
//		  	  		FullAgencyList pal = (FullAgencyList)publisherIterator.next();
//		  	  		publisherNameList.add(pal.getAgencyName());
//	  	  		}
				
				List<Keyword> keywordList = ks.loadKeywordViaPublication(publication.getPublicationId().toString());
				Iterator<Keyword> kIterator = keywordList.iterator();
				List<String> keywordNameList = new ArrayList<String>();
	  	  		while(kIterator.hasNext())
	  	  		{
	  	  			Keyword keyword = (Keyword)kIterator.next();
	  	  			keywordNameList.add(keyword.getKeyword());
	  	  		}
		  	  	
	  	  		List<UploadedFile> fileList = ps.listFilesViaPublication(publication); 
	  	  		Iterator<UploadedFile> ufileIterator = fileList.iterator();
	  	  		List<String> fileURLList = new ArrayList<String>();
				while (ufileIterator.hasNext()) 
				{
					UploadedFile uFile = (UploadedFile)ufileIterator.next();
					fileURLList.add(new URI("https", "www.opengov.nsw.gov.au", "/publication/" + uFile.getFileId().toString(), null, null).toASCIIString());
					
				}
				PublicationJSONFormat PJSON = new PublicationJSONFormat(publication.getPublicationId().toString(),
																		publication.getTitle(), agencyNameList, 
																		publication.getType(), 
																		publication.getDatePublishedDisplay(), publisherNameList, 
																		keywordNameList, 
																		publication.getDescription(), 
																		publication.getLanguage(), 
																		publication.getCoverage(), 
																		publication.getRights(),
																		publication.getViewedTimes().toString(),
																		publication.getLastViewed().toString(),
																		fileURLList);
				
				sb.append(PJSON.toJSONString());
				i++;
				if (i == listSize)
					sb.append("");
				else
					sb.append(",");				
			}
			sb.append("]}");
			request.getSession().setAttribute("JSONString", sb.toString().replace("\\/", "/"));
			forward = new ActionForward("/publication/publicationJSON.jsp");
		}catch(Exception ex)
		{
			logger.info("In class PublicationAction:allPublicationJSON()\n");
			ex.printStackTrace();
		}
		return forward;
	}
	
	public ActionForward publicationDetails (ActionMapping mapping, 
		   		 							 ActionForm form,
		   		 							 HttpServletRequest request, 
		   		 							 HttpServletResponse response) 
	{
		ActionForward forward = null;
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			Integer id = new Integer(request.getParameter("id"));
			try
			{
				Member member = (Member)request.getSession().getAttribute("member");			
				if(member == null)
				{
					forward = mapping.findForward("login");
				}else
				{
					PublicationService ps = new PublicationServiceImpl();
					Publication publication = ps.loadPublication(id);
					request.getSession().setAttribute("currentPub", publication);
					if (("draft").equalsIgnoreCase(publication.getStatus()))
							forward = mapping.findForward("edit");
					else
							forward = mapping.findForward("detail");
				}
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:publicationDetails()\n");
				ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("home");
		}
		return forward;
	}
		
	/**
	 * 
	 * Delete publication
	 */
	public ActionForward dPublication (ActionMapping mapping, 
			  						   ActionForm form,
			  						   HttpServletRequest request, 
			  						   HttpServletResponse response) 
	{
		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			Integer id = null;
			id = new Integer(request.getParameter("id"));
			try
			{
				Member member = (Member)request.getSession().getAttribute("member");
				
				if (member == null)
				{
					forward = mapping.findForward("login");			
				}else
				{
					PublicationService ps = new PublicationServiceImpl();
					FileService fs = new FileServiceImpl();
					KeywordPublicationService kps = new KeywordPublicationServiceImpl();
					Publication publication = ps.loadPublication(id);
					List<UploadedFile> fileList = ps.listFilesViaPublication(publication);
					System.out.println("No. of files " + fileList.size() + "");
					boolean keyPub_delete_status = kps.deleteKeyPubViaPublication(publication);
					boolean file_delete_status = fs.deleteFileViaPublication(publication);
					boolean publication_delete_status = ps.delPublication(id);
					for (int i = 0; i < fileList.size(); i++) 
					{
						fs.deletePhysicalFiles(fileList.get(i));
//						if(fileDeleted)
//						{
//							fs.cleanUpInbox(fileList.get(i));
//						}
						Thread.sleep(1000);
					}
					
					for (int j = 0; j < fileList.size(); j++) {
						fs.cleanUpInbox(fileList.get(j));
						Thread.sleep(1000);
					}
					
					/*if (file_delete_status && publication_delete_status && keyPub_delete_status)
					{
						msgs.add("publication.delete", new ActionMessage("publication.delete.success"));
						saveMessages(request, msgs);
					}else
					{
						//msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("batch.delete.failed"));
						//saveMessages(request, msgs);
					}*/
					forward = mapping.findForward("home");	
				}	
			 }catch(Exception ex)
			 {
				logger.info("In class PublicationAction:dPublication()\n");
				ex.printStackTrace();
			 }
		}else
		{
			forward = mapping.findForward("home");
		}
		
		return forward;
	}
	
	public ActionForward amendPublication(ActionMapping mapping, 
			  							  ActionForm form,
			  							  HttpServletRequest request, 
			  							  HttpServletResponse response) 
	{
		ActionForward forward = null;
		try
		{
			Member member = (Member)request.getSession().getAttribute("member");
			if (member == null)
			{
				forward = mapping.findForward("login");	
			}else
			{	
				String action = request.getParameter("action");
					
				MemberService ms = new MemberServiceImpl();
				KeywordService ks = new KeywordServiceImpl();
				PublicationService ps = new PublicationServiceImpl();
				FullAgencyListService fls = new FullAgencyListServiceImpl();
				AgencyPublicationService aps = new AgencyPublicationServiceImpl();
				KeywordPublicationService kps = new KeywordPublicationServiceImpl();
				PublisherPublicationService pps = new PublisherPublicationServiceImpl();
			
				Publication publication = (Publication)request.getSession().getAttribute("currentPub");
				
				//Don't delete the below code
				boolean delete_keypub_status = kps.deleteKeyPubViaPublication(publication);
				boolean delete_agencypub_status = aps.deleteAgencyPubViaPublication(publication);
				boolean delete_publisherpub_status = pps.deletePublisherViaPublication(publication);
				//Don't delete the above code
								
				String publishDay = request.getParameter("publication_day").trim();
				String publishMonth = request.getParameter("publication_month").trim();
				String publishYear = request.getParameter("publication_year").trim();
				
				if (!StringUtils.isBlank(publishDay))
					publishDay = publishDay.concat("/");
				if (!StringUtils.isBlank(publishMonth))
					publishMonth = publishMonth.concat("/");
				
				String publicationDate = publishDay + publishMonth + publishYear;
				String publicationDateRaw = ldf.formatDate(publicationDate);
				
				publication.setTitle(request.getParameter("title").trim());
				publication.setDescription(request.getParameter("description").trim());
				publication.setDatePublishedRaw(publicationDateRaw);
				publication.setDatePublishedDisplay(publicationDate);
				publication.setType(request.getParameter("type").trim());
				publication.setTypeOther(request.getParameter("type_other").trim());
				publication.setCreativeCommons(request.getParameter("creativeCommons"));
				publication.setLanguage(request.getParameter("language").trim());
				publication.setCoverage(request.getParameter("coverage").trim());
				publication.setRights(request.getParameter("rights").trim());
				
				publication.setLastUpdated(new Date());
				
				if ("Submit publication".equalsIgnoreCase(action))
				{
					publication.setTotalPages(Integer.toString(ps.countTotalPages(publication)));
					publication.setStatus(Constants.PUBLICATION_SUBMITTED);
					FullAgencyListService fas = new FullAgencyListServiceImpl();
					Member currentMember = ms.loadMember(publication.getMember().getMemberId());
					FullAgencyList faList = fas.loadFullAgencyList(currentMember.getFullAgencyList().getFullAgencyListId());
				    List<String> recipient =  new ArrayList<String>();
					recipient.add("grace.hui@records.nsw.gov.au");
				    recipient.add("opengov@records.nsw.gov.au");
					EmailNotificationAction mailSender = new EmailNotificationAction();
					mailSender.prepare(0, request);
					mailSender.sendEmail(recipient, "A new publication has been submitted to OpenGov!", 
													"Hi:<br/>" +
													"<p>" + currentMember.getFirstname() + " " + currentMember.getLastname() + 
													" from " + faList.getAgencyName()  + " has submitted a new publication and " +
													"is <a href=\"https://www.opengov.nsw.gov.au/admin\">there</a> waiting" +
													"to be approved.");
				}
				
				if ("Publish".equalsIgnoreCase(action))
				{
					publication.setTotalPages(Integer.toString(ps.countTotalPages(publication)));
					FileService fs = new FileServiceImpl();
					OpenGovLuceneIndexer ogl = null;
					List<UploadedFile> uList = fs.browseFiles(publication);
					Iterator<UploadedFile> UploadedFileIterator = uList.iterator();

					while (UploadedFileIterator.hasNext())
					{
						UploadedFile uFile = (UploadedFile)UploadedFileIterator.next();
						List<String> agencyNameList = new ArrayList<String>();
						List<String> keywordList = new ArrayList<String>();
						String[] agencyList = splitAgencies(request.getParameter("agencyNumber").trim());
						String[] keyWDs = splitKeyword(request.getParameter("keywords").trim());
						for(int j = 0; j < agencyList.length; j++)
						{
							agencyNameList.add(fls.loadFullAgencyList(Integer.valueOf(agencyList[j])).getAgencyName());
						}
						for(int w = 0; w < keyWDs.length; w++)
						{
							keywordList.add(ks.loadKeyword(Integer.valueOf(keyWDs[w])).getKeyword());
						}
		
						File inboxFile = new File(uFile.getInboxUrl());
						Pairtree pt = new Pairtree();
						String pairTreeURL = Constants.PAIRTREE_ROOT  + pt.mapToPPath(uFile.getUid()) + File.separator + "obj" + File.separator;
						String imageLocation = pairTreeURL  + "images";
						String textLocation = pairTreeURL + "txt";

						new File(imageLocation).mkdirs();
						new File(textLocation).mkdirs();

						File pairTreeDIR = new File(pairTreeURL);
						File pairTreePDF = new File(pairTreeURL + "document.pdf");
						renameFile (inboxFile, pairTreePDF);
						Thread.sleep(1000);
						
						fs.cleanUpInbox(uFile); // cleans up the inbox
						/***For migration only**/
						TikaWrapper ta = new TikaWrapper();
						Integer pageNumber = Integer.parseInt(ta.getPageNumber(pairTreeURL + "document.pdf"));
						uFile.setPages(pageNumber);
						uFile.setSize(formatSize(Double.parseDouble(ta.getFileSize(pairTreeURL + "document.pdf"))));

						//String hostURL = new URLGenerator(new HostNameFinder().getMyCanonicalHostName()).getHostURL();
						String hostURL = "https://www.opengov.nsw.gov.au/";

						NYTimesJSONFormat NYTimesJSON = new NYTimesJSONFormat(uFile.getFileName().substring(0, uFile.getFileName().length()-4),
																			  uFile.getFileName().substring(0, uFile.getFileName().length()-4),
																			  pageNumber.toString(),
																			  "",
																			  hostURL.concat("doc_opener?uid=").concat(uFile.getUid()),
																			  hostURL.concat("publication/").concat(uFile.getFileId().toString()),
																			  hostURL.concat("txt_opener?uid=").concat(uFile.getUid()),
																			  hostURL.concat("thumb?uid=").concat(uFile.getUid()),
																			  hostURL.concat("doc_search?q={query}&uid=").concat(uFile.getUid()),
																			  hostURL.concat("txt_opener?uid=").concat(uFile.getUid())+"&counter={page}",
																			  hostURL.concat("pdf_image_opener?uid=").concat(uFile.getUid())+"&counter={page}");


						uFile.setReaderJson(NYTimesJSON.toJSONString());
						uFile.setPairtreeUrl(pairTreeURL + "document.pdf");
						fs.updateFile(uFile);

						PDFImageGenerator PDFGen = new PDFImageGenerator();
						PDFBoxWrapper pbw = new PDFBoxWrapper();
						ConvertPagesToHiResImages CRT= new ConvertPagesToHiResImages(); 
						PDDocument pd = PDDocument.load(uFile.getPairtreeUrl());

						if (!pd.isEncrypted())
						{
							PDFGen.generateThumbnailImage(uFile.getPairtreeUrl(), pairTreeURL);
							CRT.ConvertImages("png", uFile.getPairtreeUrl(), imageLocation, "800", "997");
							pbw.createTxtFile(uFile.getPairtreeUrl(), pairTreeURL.concat("document.txt"));
							int pageNumbers = pd.getNumberOfPages();
							for (int i = 1; i <= pageNumbers; i++)
							{
								BufferedWriter out = new BufferedWriter(new FileWriter(textLocation + File.separator + "page" + i + ".txt"));
								PDFTextStripper stripper = new PDFTextStripper();
								stripper.setSortByPosition(true);
								stripper.setStartPage(i);
								stripper.setEndPage(i+1);
								String s = stripper.getText(pd);
								out.write(s);
								out.close();
							}
							if (pd != null) {
								pd.close();
							}
							// I use close() to flush the stream.
	
							List<Keyword> kList = ks.loadKeywordViaPublication(publication.getPublicationId().toString());
							Iterator<Keyword> kIterator = kList.iterator();

							StringBuilder keywordString = new StringBuilder();

							while(kIterator.hasNext())
							{
								Keyword keywords = (Keyword)kIterator.next();
								keywordString.append(keywords.getKeyword()).append(',');
							}

							ogl = new OpenGovLuceneIndexer(publication.getTitle(), 
														   publication.getDescription(), 
														   keywordString.toString(), 
														   agencyNameList,
														   publication.getDatePublishedRaw(),
														   publication.getType(),
														   publication.getCoverage(),
														   publication.getRights());			  	  	
							ogl.createIndex(uFile.getFileId().toString(), uFile.getFileName(), uFile.getPairtreeUrl(), uFile.getContentType());	
						}
						publication.setStatus(Constants.PUBLICATION_PUBLISHED);
						publication.setLastViewed(new Date());
						publication.setDateApproved(new Date());
						publication.setViewedTimes(1);
					}
					Member currentMember = ms.loadMember(publication.getMember().getMemberId());
					UploadedFile aFile = fs.loadFileViaOrder(1, publication.getPublicationId());
					EmailNotificationAction mailSender = new EmailNotificationAction();
					List<String> recipient =  new ArrayList<String>();
					recipient.add(currentMember.getEmail());
					recipient.add("opengov@records.nsw.gov.au");
					mailSender.prepare(0, request);
					mailSender.sendEmail(recipient, "Your publication is now live!", 
								"Hi " + currentMember.getFirstname() + ":<br/>" +
								"<p>Your publication " + publication.getTitle() + " is now live.</p>" +
								"<p>You may view it at https://www.opengov.nsw.gov.au/publication/" + aFile.getFileId() + "</p>" + 
							    "<p>If you have any questions please contact us at opengov@records.nsw.gov.au</p>");
				}
				ps.updatePublication(publication);
			
				String[] updatedKeyword = splitKeyword(request.getParameter("keywords"));
				String[] updatedAgencies = splitAgencies(request.getParameter("agencyNumber"));
				String[] publisher = splitAgencies(request.getParameter("publisherNumber"));
			
				for(int i = 0; i < updatedKeyword.length; i++)
				{
					KeywordPublication keywordPublication = new KeywordPublication();
					keywordPublication.setKeyword(ks.loadKeyword(Integer.valueOf(updatedKeyword[i])));
					keywordPublication.setPublication(publication);
					kps.addKeywordPublication(keywordPublication);
				}
			
				for(int j = 0; j < updatedAgencies.length; j++)
				{
					AgencyPublication agencyPublication = new AgencyPublication();
					agencyPublication.setFullAgencyList(fls.loadFullAgencyList(Integer.valueOf(updatedAgencies[j])));
					agencyPublication.setPublication(publication);
					aps.addAgencyPublication(agencyPublication);
				}
			
				for(int k = 0; k < publisher.length; k++)
				{
					PublisherPublication publisherPublication = new PublisherPublication();
					publisherPublication.setFullAgencyList(fls.loadFullAgencyList(Integer.valueOf(publisher[k])));
					publisherPublication.setPublication(publication);
					pps.addPublisherPublication(publisherPublication);
				}
				forward = mapping.findForward("home");
			}	
		}catch(Exception ex){
			logger.info("In class PublicationAction:amendPublication()\n");
			ex.printStackTrace();
		}
			return forward;	
	}
	
	public ActionForward agencyJSON (ActionMapping mapping, 
			  						 ActionForm form,
			  						 HttpServletRequest request, 
			  						 HttpServletResponse response) 
	{
		ActionForward forward = null;
		Integer fullAgencyListID = new Integer(request.getParameter("agency"));

		try
		{
			KeywordService ks = new KeywordServiceImpl();
			PublicationService ps = new PublicationServiceImpl();
			FullAgencyListService fas = new FullAgencyListServiceImpl();
			AgencyPublicationService aps = new AgencyPublicationServiceImpl();
			PublisherPublicationService pps = new PublisherPublicationServiceImpl();

			FullAgencyList fal = new FullAgencyList();
			fal.setFullAgencyListId(fullAgencyListID);
			
			List<String> PJSONString = new ArrayList<String>();
			List<Publication> pList= aps.loadPublicationViaFullAgency(fal);
			Iterator<Publication> pIterator = pList.iterator();
			while(pIterator.hasNext())
			{
				Publication thisPub = (Publication)pIterator.next();
				List<FullAgencyList> agencyList = fas.loadAgencyViaPublication(thisPub.getPublicationId().toString());
				Iterator<FullAgencyList> aIterator = agencyList.iterator();
				List<String> agencyNameList = new ArrayList<String>();
				while(aIterator.hasNext())
		  	    {
		  	  		FullAgencyList fal2 = (FullAgencyList)aIterator.next();
		  	  		agencyNameList.add(fal2.getAgencyName());
		  	    }
			
//				List<FullAgencyList> publisherList = pps.loadPublisherViaPublication(thisPub.getPublicationId().toString());
//				Iterator<FullAgencyList> publiserIterator = publisherList.iterator();
				List<String> publisherNameList = new ArrayList<String>();
//	  	  		while(pIterator.hasNext())
//	  	  		{
//		  	  		FullAgencyList pal = (FullAgencyList)publiserIterator.next();
//		  	  		publisherNameList.add(pal.getAgencyName());
//	  	  		}
				
				List<Keyword> keywordList = ks.loadKeywordViaPublication(thisPub.getPublicationId().toString());
				Iterator<Keyword> kIterator = keywordList.iterator();
				List<String> keywordNameList = new ArrayList<String>();
	  	  		while(kIterator.hasNext())
	  	  		{
	  	  			Keyword keyword = (Keyword)kIterator.next();
	  	  			keywordNameList.add(keyword.getKeyword());
	  	  		}
		  	  	
	  	  		List<UploadedFile> fileList = ps.listFilesViaPublication(thisPub); 
	  	  		Iterator<UploadedFile> ufileIterator = fileList.iterator();
	  	  		List<String> fileURLList = new ArrayList<String>();
				while (ufileIterator.hasNext()) 
				{
					UploadedFile uFile = (UploadedFile)ufileIterator.next();
					fileURLList.add(new URI("https", "www.opengov.nsw.gov.au", "/publication/" + uFile.getFileId().toString(), null, null).toASCIIString());
					
				}
	  	  		
				PJSONString.add(new PublicationJSONFormat(thisPub.getPublicationId().toString(),
								thisPub.getTitle(), agencyNameList, 
								thisPub.getType(), 
								thisPub.getDatePublishedDisplay(), 
								publisherNameList, 
								keywordNameList, 
								thisPub.getDescription(), 
								thisPub.getLanguage(), 
								thisPub.getCoverage(), 
								thisPub.getRights(),
								thisPub.getViewedTimes().toString(),
								thisPub.getLastViewed().toString(),
								fileURLList).toJSONString());
			}
			request.getSession().setAttribute("JSONString", PJSONString.toString().replace("\\/", "/").trim());
			forward = new ActionForward("/agency/agencyJSON.jsp");
		 }catch(Exception ex)
		 {
			logger.info("In class PublicationAction:agencyJSON()\n");
			ex.printStackTrace();
		 }
		
		return forward;
	}
	
	
	public ActionForward uploadFiles (ActionMapping mapping, 
			  						  ActionForm form,
			  						  HttpServletRequest request, 
			  						  HttpServletResponse response) 
	{
		ActionForward forward = null;
		Integer id = null;
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			id = new Integer(request.getParameter("id"));
			try
			{
				Member member = (Member)request.getSession().getAttribute("member");			
				if(member == null)
				{
					forward = mapping.findForward("login");
				}else
				{
					forward = new ActionForward("/agency_pages/file_upload.jsp?id=" + id);
				}
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:uploadFiles()\n");
				ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("home");
		}
			return forward;			
	}
	
	public ActionForward fileDownload (ActionMapping mapping, 
			  						   ActionForm form,
			  						   HttpServletRequest request, 
			  						   HttpServletResponse response) 
	{
		ActionForward forward = null;
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			String fileID = request.getParameter("id");
			if ((".pdf").equals(fileID.substring(fileID.length()-4)))
			{
				fileID = fileID.substring(0, fileID.length()-4);
			}
			try
			{
				Integer id = Integer.parseInt(fileID);
				FileService fs = new FileServiceImpl();
				UploadedFile uploadedFile = fs.loadFile(id);				
				forward = new ActionForward("/download.do?id=" + uploadedFile.getFileId());
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:fileDownload()\n");
				ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("index");
		}
			return forward;			 
	}
	
	public ActionForward deleteFile (ActionMapping mapping, 
			   						 ActionForm form,
			   						 HttpServletRequest request, 
			   						 HttpServletResponse response) 
	{
			ActionForward forward = null;
			Integer fileID = null;
			if (!("").equalsIgnoreCase(request.getParameter("id")))
			{
				fileID = new Integer(request.getParameter("id"));
				String pageType = request.getParameter("pageType");
				try
				{
					Member member = (Member)request.getSession().getAttribute("member");			
					if(member == null)
					{
						forward = mapping.findForward("login");
					}else
					{
						boolean status = false;
						boolean physical_status = false;
						FileService fs = new FileServiceImpl();
						UploadedFile up = fs.loadFile(fileID);
						Integer pubID = up.getPublication().getPublicationId();
						PublicationService ps = new PublicationServiceImpl();
						Publication publication = ps.loadPublication(pubID);
						
						Integer fileCount = Integer.parseInt(publication.getTotalFiles());
						fileCount = fileCount - 1;
						publication.setTotalFiles(fileCount.toString());
						ps.updatePublication(publication);
						status = fs.deleteFile(fileID);
						physical_status = fs.deletePhysicalFiles(up);
						List<UploadedFile> uList = fs.browseFiles(publication);
						Iterator<UploadedFile> it = uList.iterator();
						UploadedFile uFile = null;
						int i = 1;
						while(it.hasNext())
						{
							uFile = (UploadedFile)it.next();
							uFile.setFileOrder(i);
							fs.updateFile(uFile);
							i++;
						}
						
						if (status && physical_status)
						{
							if (("edit").equals(pageType))
							{
								fs.cleanUpInbox(up);
								forward = new ActionForward("/agency_pages/publicationEdit.jsp?id=" + pubID);
							}
							else
							{
								forward = new ActionForward("/agency_pages/newPublication.jsp?id=" + pubID);
							}
						}
					}
				}catch(Exception ex)
				{
					logger.info("In class PublicationAction:deleteFile()\n");
					ex.printStackTrace();
				}
			}else
			{
				forward = mapping.findForward("home");
			}
			return forward;			 
	}
	
	public ActionForward submitPublication (ActionMapping mapping, 
    		   						  		ActionForm form,
    		   						  		HttpServletRequest request, 
    		   						  		HttpServletResponse response) 
	{
		ActionForward forward = null;
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			Integer id  = new Integer(request.getParameter("id"));
			try
			{
				Member member = (Member)request.getSession().getAttribute("member");			
				if(member == null)
				{
					forward = mapping.findForward("login");
				}else
				{
					PublicationService ps = new PublicationServiceImpl();
					Publication publication = ps.loadPublication(id);
					publication.setTotalPages(Integer.toString(ps.countTotalPages(publication)));
					publication.setStatus(Constants.PUBLICATION_SUBMITTED);
					boolean status = ps.updatePublication(publication);
					if (status)
					{
						forward = mapping.findForward("home");
					}
				}
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:submitPublication()\n");
				ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("home");
		}
			return forward;
	}
		
	
	public ActionForward updateMetadata (ActionMapping mapping, 
									     ActionForm form,
									     HttpServletRequest request, 
									     HttpServletResponse response) 
	{
			ActionForward forward = null;
			Integer id = null;
			FileService fs = new FileServiceImpl();
			if (!("").equalsIgnoreCase(request.getParameter("id")))
			{
				id = new Integer(request.getParameter("id"));
				try
				{
					Member member = (Member)request.getSession().getAttribute("member");			
					if(member == null)
					{
						forward = mapping.findForward("login");
					}else
					{
						UploadedFile uploadedFile = fs.loadFile(id);
						request.getSession().setAttribute("uploadedFile", uploadedFile);
						forward = mapping.findForward("metadata");	
					}
			}catch(Exception ex){
					ex.printStackTrace();
				}
			}else
			{
				forward = mapping.findForward("home");
			}
				return forward;			
	}
		
	private String[] splitKeyword(String keyword)
	{
		String keywordArray[];
		StringTokenizer st = new StringTokenizer(keyword, ",");

		keywordArray = new String[st.countTokens()];

		int i = 0;
        while(st.hasMoreTokens())
        {
        	  keywordArray[i] = st.nextToken();
              i++;
        }
        return keywordArray;
	 }
	
	private String[] splitAgencies(String agency)
	{
		String agencyArray[];
		StringTokenizer st = new StringTokenizer(agency, ",");
		
		agencyArray = new String[st.countTokens()];
		int i = 0;
        while(st.hasMoreTokens())
        {
        	agencyArray[i] = st.nextToken();
            i++;
        }
        return agencyArray;
	}
	
	private static void renameFile(File sourceFile, File destFile) throws IOException 
	{
	  if(!destFile.exists()) {
	     destFile.createNewFile();
	  }

	  FileChannel source = null;
	  FileChannel destination = null;
	  
	  try {
	      source = new RandomAccessFile(sourceFile,"rw").getChannel();
	      destination = new RandomAccessFile(destFile,"rw").getChannel();

	      long position = 0;
	      long count = source.size();

	      source.transferTo(position, count, destination);
	  }
	  finally 
	  {
	      if(source != null) {
	    	  source.close();
	      }
	      if(destination != null) {
	       destination.close();
	      }
	  }
	}
	
	private static final double BASE = 1024, KB = BASE, MB = KB * BASE, GB = MB * BASE;
    private static final DecimalFormat df = new DecimalFormat("#.##");
	
	private static String formatSize(double size) {
        if(size >= GB) {
            return df.format(size/GB) + " GB";
        }
        if(size >= MB) {
            return df.format(size/MB) + " MB";
        }
        if(size >= KB) {
            return df.format(size/KB) + " KB";
        }
        return "" + (int)size + " bytes";
    }
	
}
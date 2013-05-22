package au.gov.nsw.records.digitalarchive.struts.action;

import gov.loc.repository.pairtree.Pairtree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import au.gov.nsw.records.digitalarchive.ORM.AgencyPublication;
import au.gov.nsw.records.digitalarchive.ORM.Archivist;
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
import au.gov.nsw.records.digitalarchive.notification.EmailNotificationAction;
import au.gov.nsw.records.digitalarchive.pdf.ConvertPagesToHiResImages;
import au.gov.nsw.records.digitalarchive.pdf.PDFBoxWrapper;
import au.gov.nsw.records.digitalarchive.pdf.PDFImageGenerator;
import au.gov.nsw.records.digitalarchive.search.OpenGovLucene;
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
import au.gov.nsw.records.digitalarchive.system.LuceneDateFormatter;


public class PublicationAction extends BaseAction 
{
	
	LuceneDateFormatter ldf = new LuceneDateFormatter();
	
	public PublicationAction()
	{}
	
	public ActionForward openPublication(ActionMapping mapping,
										 ActionForm form,
										 HttpServletRequest request,
										 HttpServletResponse response)
	{
		ActionForward forward = null;
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			Integer id = null;
			id = new Integer(request.getParameter("id"));
			try
			{
				PublicationService ps = new PublicationServiceImpl();
				Publication publication = ps.loadPublication(id);
				publication.setViewedTimes(Integer.valueOf(publication.getViewedTimes() + 1));
				publication.setLastViewed(new Date());
				ps.updatePublication(publication);
			 }catch(Exception ex)
			 {
				logger.info("In class PublicationAction:openPublication()\n");
				ex.printStackTrace();
			 }
		}else
		{
			forward = mapping.findForward("home");
		}
		return forward;
	}
		
	public ActionForward deletePublication (ActionMapping mapping, 
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
				Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");
				
				if (archivist == null)
				{
					forward = mapping.findForward("login");			
				}else
				{
					PublicationService ps = new PublicationServiceImpl();
					FileService fs = new FileServiceImpl();
					KeywordPublicationService kps = new KeywordPublicationServiceImpl();
					Publication publication = ps.loadPublication(id);
					boolean keyPub_delete_status = kps.deleteKeyPubViaPublication(publication);
					boolean file_delete_status = fs.deleteFileViaPublication(publication);
					boolean publication_delete_status = ps.delPublication(id);
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
				logger.info("In class PublicationAction:deletePublication()\n");
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
		KeywordService ks = new KeywordServiceImpl();
		PublicationService ps = new PublicationServiceImpl();
		FullAgencyListService fls = new FullAgencyListServiceImpl();
		AgencyPublicationService aps = new AgencyPublicationServiceImpl();
		KeywordPublicationService kps = new KeywordPublicationServiceImpl();
		PublisherPublicationService pps = new PublisherPublicationServiceImpl();
		ActionMessages msgs = new ActionMessages();
		
		Publication publication = (Publication)request.getSession().getAttribute("currentPub");
		
		try
		{
			boolean delete_keypub_status = kps.deleteKeyPubViaPublication(publication);
			boolean delete_agencypub_status = aps.deleteAgencyPubViaPublication(publication);
			boolean delete_publisherpub_status = pps.deletePublisherViaPublication(publication);
			System.out.println("Publication id " + publication.getPublicationId());
			
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
			publication.setLanguage(request.getParameter("language").trim());
			publication.setCoverage(request.getParameter("coverage").trim());
			publication.setRights(request.getParameter("rights").trim());
			
			String creativeCommons = "on";
			if (request.getParameter("creativeCommons") == null || ("").equalsIgnoreCase(request.getParameter("creativeCommons")));
				creativeCommons = "off";
				
			publication.setCreativeCommons(creativeCommons);
			publication.setLastUpdated(new Date());
			
			ps.updatePublication(publication);
			
			String[] updatedKeyword = splitKeyword(request.getParameter("keywords"));
			String[] updatedAgencies = splitAgencies(request.getParameter("agencyNumber"));
			String[] publisher = splitAgencies(request.getParameter("publisherNumber"));
			
			System.out.println(updatedAgencies.length);
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
			return new ActionForward("/pub.do?method=publicationDetails&id=" + publication.getPublicationId());
		

		}catch(Exception ex){
			logger.info("In class PublicationAction:amendPublication()\n");
			ex.printStackTrace();
		}
		return new ActionForward("/pub.do?method=publicationDetails&id=" + publication.getPublicationId());
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
	
	public ActionForward downloadFile (ActionMapping mapping, 
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
				Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");			
				if(archivist == null)
				{
					forward = mapping.findForward("login");
				}else
				{
					UploadedFile uploadedFile = new UploadedFile();
					FileService fs = new FileServiceImpl();
					uploadedFile = fs.loadFile(id);				
					forward = new ActionForward("/download.do?id=" + uploadedFile.getFileId());
				}
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:downloadFile()\n");
				ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("home");
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
				try
				{
					Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");			
					if(archivist == null)
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
							request.getSession().setAttribute("currentPub", publication);
							forward = mapping.findForward("metadata");
						}
					}
				}catch(Exception ex)
				{
					logger.info("In class PublicationAction:deleteFile()\n");
					ex.printStackTrace();
				}
			}else
			{
				forward = mapping.findForward("metadata");
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
				Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");			
				if(archivist == null)
				{
					forward = mapping.findForward("login");
				}else
				{
					PublicationService ps = new PublicationServiceImpl();
					Publication publication = ps.loadPublication(id);
					request.getSession().setAttribute("currentPub", publication);
					forward = mapping.findForward("metadata");
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
	
	public ActionForward metadataDetails (ActionMapping mapping, 
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
				Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");			
				if(archivist == null)
				{
					forward = mapping.findForward("login");
				}else
				{
					PublicationService ps = new PublicationServiceImpl();
					Publication publication = ps.loadPublication(id);
					request.getSession().setAttribute("currentPub", publication);
					forward = mapping.findForward("editMetadata");
				}
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:metadataDetails()\n");
				ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("home");
		}

		return forward;
	}
 
	public ActionForward dbUpdator (ActionMapping mapping, 
		   		 					ActionForm form,
		   		 					HttpServletRequest request, 
		   		 					HttpServletResponse response) 
	{
		ActionForward forward = null;
		
//		DBUpdater db_u = new DBUpdater();
//		for (int i = 11418; i < 12062; i++)
//		{db_u.updateDB(i);}
//			db_u.updateDB(9681);
//		db_u.updateDB(9681);
//		LuceneDateFormatter ldf = new LuceneDateFormatter();
//		for (int i = 9681; i < 12070; i++)
//			ldf.updateDB(i);
//		forward = mapping.findForward("loadPublication");
		
		return forward;
	}
	
	
	
	public synchronized ActionForward publishPublication (ActionMapping mapping, 
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
				Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");			
				if(archivist == null)
				{
					forward = mapping.findForward("login");
				}else
				{
					FileService fs = new FileServiceImpl();
					MemberService ms = new MemberServiceImpl();
					KeywordService ks = new KeywordServiceImpl();
					PublicationService ps = new PublicationServiceImpl();
					FullAgencyListService fal = new FullAgencyListServiceImpl();

					Publication publication = ps.loadPublication(id);
					Integer totalPageNumber = 0;
					OpenGovLucene ogl = null;
					List<UploadedFile> uList = fs.browseFiles(publication);
					Iterator<UploadedFile> UploadedFileIterator = uList.iterator();

					while (UploadedFileIterator.hasNext())
					{
						UploadedFile uFile = (UploadedFile)UploadedFileIterator.next();
						List<FullAgencyList> faList = fal.loadAgencyViaPublication(Integer.toString(publication.getPublicationId()));
						

						List<String> agencyNameList = new ArrayList<String>();
						
						Iterator<FullAgencyList> aIterator = faList.iterator(); 
				  	  	 while(aIterator.hasNext())
				  	     {
				  	  		FullAgencyList fl = (FullAgencyList)aIterator.next();
				  	  		agencyNameList.add(fl.getAgencyName());
				  	  	 }
						
						File inboxFile = new File(uFile.getInboxUrl());
						Pairtree pt = new Pairtree();
						String pairTreeURL = Constants.PAIRTREE_ROOT  + pt.mapToPPath(uFile.getUid()) + File.separator + "obj" + File.separator;
						String imageLocation = pairTreeURL  + "images";
						String textLocation = pairTreeURL + "txt";
						
						new File(imageLocation).mkdirs();
						new File(textLocation).mkdirs();
						
						File pairTreeDIR = new File(pairTreeURL);
						
						System.out.println("Readable: " + inboxFile.canRead());
						System.out.println("Writeable: " + inboxFile.canWrite());
						System.out.println("Executable: " + inboxFile.canExecute());
						
						//System.out.println("Renamed: " + inboxFile.renameTo(new File(pairTreeDIR, "document.pdf")));
						
						File pairTreePDF = new File(pairTreeURL + "document.pdf");
						renameFile (inboxFile, pairTreePDF);
						
						
						/***For migration only**/
						TikaWrapper ta = new TikaWrapper();
						Integer pageNumber = Integer.parseInt(ta.getPageNumber(pairTreeURL + "document.pdf"));
		  	  	 		totalPageNumber = totalPageNumber + pageNumber;
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
						
						System.out.println("\nIs encrypted: " + pd.isEncrypted() + "\n");
					
					if (!pd.isEncrypted())
					{
						PDFGen.generateThumbnailImage(uFile.getPairtreeUrl(), pairTreeURL);
						//PDFGen.generateNormalImage(uFile.getPairtreeUrl(), imageLocation);
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
			  	  			Keyword keyword = (Keyword)kIterator.next();
			  	  			keywordString.append(keyword.getKeyword()).append(',');
			  	  		}
						
			  	  		ogl = new OpenGovLucene(publication.getTitle(), 
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
					publication.setArchivist(archivist);
	  	  	 		publication.setTotalPages(totalPageNumber.toString()); //added for gazette migration
					ps.updatePublication(publication);
					List<Publication> list = ps.browsePublication();
					request.setAttribute("publicationList", list);
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
					
					forward = mapping.findForward("loadPublication");
				}
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:publishPublication()\n");
				EmailNotificationAction mailSender = new EmailNotificationAction();
				List<String> recipient =  new ArrayList<String>();
				recipient.add("ken.zhai@records.nsw.gov.au");
				//recipient.add("opengov@records.nsw.gov.au");
				mailSender.prepare(0, request);
				mailSender.sendEmail(recipient, "Error while publishing !", 
							"<p>Error occured while publishing publication:" + id +"</p>" + 
							 ex.toString() + "<br/>" + ex.getCause().toString() + "<br/>" + ex.getMessage());
				ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("home");
		}
			return forward;
	}
	
	public synchronized boolean migrationPublication (int publicationNo) 
	{
		System.out.println("\n\n\nID:" + publicationNo);
				
		try
		{
				FileService fs = new FileServiceImpl();
				KeywordService ks = new KeywordServiceImpl();
				PublicationService ps = new PublicationServiceImpl();
				FullAgencyListService fal = new FullAgencyListServiceImpl();
				OpenGovLucene ogl = null;
				
				Publication publication = ps.loadPublication(publicationNo);
				Integer totalPageNumber = 0;
				List<UploadedFile> uList = fs.browseFiles(publication);
				Iterator<UploadedFile> UploadedFileIterator = uList.iterator();
				while (UploadedFileIterator.hasNext())
				{
						UploadedFile uFile = (UploadedFile)UploadedFileIterator.next();
						List<FullAgencyList> faList = fal.loadAgencyViaPublication(Integer.toString(publicationNo));
						List<String> agencyNameList = new ArrayList<String>();				
						Iterator<FullAgencyList> aIterator = faList.iterator(); 
		  	  	 		while(aIterator.hasNext())
		  	  	 		{
		  	  	 			FullAgencyList fl = (FullAgencyList)aIterator.next();
		  	  	 			agencyNameList.add(fl.getAgencyName());
		  	  	 		}
							
		  	  	 		File inboxFile = new File(uFile.getInboxUrl());
		  	  	 		Pairtree pt = new Pairtree();
		  	  	 		String pairTreeURL = Constants.PAIRTREE_ROOT + pt.mapToPPath(uFile.getUid()) + File.separator + "obj" + File.separator;
		  	  	 		String imageLocation = pairTreeURL  + "images";
		  	  	 		String textLocation = pairTreeURL + "txt";
				
		  	  	 		new File(imageLocation).mkdirs();
		  	  	 		new File(textLocation).mkdirs();
				
		  	  	 		File pairTreePDF = new File(pairTreeURL + "document.pdf");
		  	  	 		renameFile (inboxFile, pairTreePDF);
				
				/***For migration only**/
		  	  	 		TikaWrapper ta = new TikaWrapper();
		  	  	 		Integer pageNumber = Integer.parseInt(ta.getPageNumber(pairTreeURL + "document.pdf"));
		  	  	 		totalPageNumber = totalPageNumber + pageNumber;
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
				
		  	  	 		System.out.println("\nIs encrypted: " + pd.isEncrypted() + "\n");
				
		  	  	 		if (!pd.isEncrypted())
		  	  	 		{
		  	  	 			PDFGen.generateThumbnailImage(uFile.getPairtreeUrl(), pairTreeURL);
		  	  	 			//PDFGen.generateNormalImage(uFile.getPairtreeUrl(), imageLocation);
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
				
		  	  	 			List<Keyword> kList = ks.loadKeywordViaPublication(publication.getPublicationId().toString());
		  	  	 			Iterator<Keyword> kIterator = kList.iterator();
				
		  	  	 			StringBuilder keywordString = new StringBuilder();
				
		  	  	 			while(kIterator.hasNext())
		  	  	 			{
		  	  	 				Keyword keyword = (Keyword)kIterator.next();
		  	  	 				keywordString.append(keyword.getKeyword()).append(',');
		  	  	 			}
		  	  	 			System.out.println("Creating index");

		  	  	 			ogl = new OpenGovLucene(publication.getTitle(), 
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
		  	  	 		publication.setTotalPages(totalPageNumber.toString()); //added for gazette migration
		  	  	 		ps.updatePublication(publication);
				
				}
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:publishPublication()\n");
				ex.printStackTrace();
			}
				
				return true;
	}
	
	public synchronized boolean createIndexAndFacets (int publicationNo) 
	{
		System.out.println("\n\n\nID:" + publicationNo);
				
				try
				{
				FileService fs = new FileServiceImpl();
				KeywordService ks = new KeywordServiceImpl();
				PublicationService ps = new PublicationServiceImpl();
				FullAgencyListService fal = new FullAgencyListServiceImpl();
				
				
				Publication publication = ps.loadPublication(publicationNo);
				OpenGovLucene ogl = null;
				List<UploadedFile> uList = fs.browseFiles(publication);
				Iterator<UploadedFile> UploadedFileIterator = uList.iterator();

				while (UploadedFileIterator.hasNext())
				{
				UploadedFile uFile = (UploadedFile)UploadedFileIterator.next();
				List<FullAgencyList> faList = fal.loadAgencyViaPublication(Integer.toString(publicationNo));

				List<String> agencyNameList = new ArrayList<String>();
				
				Iterator<FullAgencyList> aIterator = faList.iterator(); 
		  	  	 while(aIterator.hasNext())
		  	     {
		  	  		FullAgencyList fl = (FullAgencyList)aIterator.next();
		  	  		agencyNameList.add(fl.getAgencyName());
		  	  	 }
				
//				TikaWrapper ta = new TikaWrapper();
//				Integer pageNumber = Integer.parseInt(ta.getPageNumber(uFile.getInboxUrl()));
//				uFile.setPages(pageNumber);
//				uFile.setSize(formatSize(Double.parseDouble(ta.getFileSize(uFile.getInboxUrl()))));
//				
//				fs.updateFile(uFile);

				List<Keyword> kList = ks.loadKeywordViaPublication(publication.getPublicationId().toString());
				Iterator<Keyword> kIterator = kList.iterator();
				
				StringBuilder keywordString = new StringBuilder();
				
				while(kIterator.hasNext())
				{
				Keyword keyword = (Keyword)kIterator.next();
				keywordString.append(keyword.getKeyword()).append(',');
				}
								
				ogl = new OpenGovLucene(publication.getTitle(), 
										publication.getDescription(), 
										keywordString.toString(), 
										agencyNameList,
										publication.getDatePublishedRaw(),
										publication.getType(),
										publication.getCoverage(),
										publication.getRights());
				
				ogl.createIndex(uFile.getFileId().toString(), uFile.getFileName(), uFile.getInboxUrl(), uFile.getContentType()); //will create index and facets.
						
			}
							
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:createIndexAndFacets()\n");
				ex.printStackTrace();
			}
				
				return true;
	}
	
	public ActionForward rejectPublication (ActionMapping mapping, 
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
				Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");			
				if(archivist == null)
				{
					forward = mapping.findForward("login");
				}else
				{
					PublicationService ps = new PublicationServiceImpl();
					Publication publication = ps.loadPublication(id);
					String reason = request.getParameter("reason");
					String email = request.getParameter("email");
					String textbody = request.getParameter("textbody");
					List<String> recipient =  new ArrayList<String>();
					EmailNotificationAction mailSender = new EmailNotificationAction();

					if ("Does not conform OpenGov publication policy".equalsIgnoreCase(reason) ||
						"Not related to NSW government".equalsIgnoreCase(reason))
					{
						
						
						recipient.add(email.trim());
						mailSender.prepare(0, request);
						mailSender.sendEmail(recipient, "Publication to OpenGov rejected - " + reason +"", 
											 "<p>" + textbody + "</p>");
						
						FileService fs = new FileServiceImpl();
						KeywordPublicationService kps = new KeywordPublicationServiceImpl();
						boolean keyPub_delete_status = kps.deleteKeyPubViaPublication(publication);
						boolean file_delete_status = fs.deleteFileViaPublication(publication);
						boolean publication_delete_status = ps.delPublication(id);
						List<Publication> list = ps.browsePublication();
						request.setAttribute("publicationList", list);
						forward = mapping.findForward("loadPublication");
					}
					if ("Is incomplete".equalsIgnoreCase(reason))
					{
					
						recipient.add(email.trim());
						mailSender.prepare(0, request);
						mailSender.sendEmail(recipient, "Publication to OpenGov rejected - " + reason +"", 
											 "<p>" + textbody + "</p>");

						publication.setStatus(Constants.PUBLICATION_DRAFT);
						ps.updatePublication(publication);
						List<Publication> list = ps.browsePublication();
						request.setAttribute("publicationList", list);
						forward = mapping.findForward("loadPublication");
					}
				}
			}catch(Exception ex)
			{
				logger.info("In class PublicationAction:rejectPublication()\n");
				ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("home");
		}
			return forward;
	}
	
	
	public ActionForward submittedPublication (ActionMapping mapping, 
	     	 								   ActionForm form,
	     	 								   HttpServletRequest request, 
	     	 								   HttpServletResponse response) 
	{
		ActionForward forward = null;
		PublicationService ps = new PublicationServiceImpl();
		try{
			Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");			
			if( archivist == null)
			{
				forward = mapping.findForward("welcome");
			}
			else
			{
				List<Publication> list = ps.browsePublication();
				request.setAttribute("publicationList", list);
				forward = mapping.findForward("loadPublication");
			}
		}catch(Exception ex)
		{
			logger.info("In class PublicationAction:submittedPublication()\n");
			ex.printStackTrace();
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
	
	public void doMigration()
	{
		try {
			
			//for (int i = 12000; i < 12640; i++) {
				
				createIndexAndFacets(11372);
			//}
			
			
//			List<Publication> pList = ps.allPublication(1612, 2);
//			
//			for (Iterator<Publication> iterator = pList.iterator(); iterator.hasNext();) {
//				Publication pub = (Publication) iterator.next();
//				migrationPublication(pub.getPublicationId());
//				
//			}
			
//			Iterator<Publication> it = pList.iterator();
//		
//			while(it.hasNext())
//			{
//				Publication pub = (Publication)it.next();
//				//migrationPublication(pub.getPublicationId());
//					//createIndexAndFacets(pub.getPublicationId());
//				System.out.println("ID: " + pub.getPublicationId());
//			}
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public ActionForward migrationPub (ActionMapping mapping, 
											ActionForm form,
											HttpServletRequest request, 
											HttpServletResponse response) 
	{
			ActionForward forward = null;
		
			/*Integer startID  = new Integer(request.getParameter("startID"));
			Integer endID  = new Integer(request.getParameter("endID"));*/
			
			doMigration();
			forward = mapping.findForward("loadPublication");
			
		return forward;
	}
	
	 private static void renameFile(File sourceFile, File destFile) throws IOException {
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
	     finally {
	      if(source != null) {
	       source.close();
	      }
	      if(destination != null) {
	       destination.close();
	      }
	    }
	 }
	 
	 public static void main(String args[])
	 {
		PublicationAction pa = new PublicationAction();
		pa.doMigration();
	 }
	
		
}
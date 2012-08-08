package au.gov.nsw.records.digitalarchive.struts.action;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import au.gov.nsw.records.digitalarchive.ORM.AgencyPublication;
import au.gov.nsw.records.digitalarchive.ORM.KeywordPublication;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.PublisherPublication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.base.Constants;
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
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublisherPublicationService;
import au.gov.nsw.records.digitalarchive.service.PublisherPublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.struts.form.PublicationForm;


public class PublicationAction extends BaseAction 
{
	
	public PublicationAction()
	{}
	
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
				KeywordService ks = new KeywordServiceImpl();
				PublicationService ps = new PublicationServiceImpl();
				KeywordPublicationService kps = new KeywordPublicationServiceImpl();
				FullAgencyListService fls = new FullAgencyListServiceImpl();
				AgencyPublicationService aps = new AgencyPublicationServiceImpl();
				PublisherPublicationService pps = new PublisherPublicationServiceImpl();
				Publication publication = new Publication();
				PublicationForm publicationForm = (PublicationForm) form;
				
				String[] keyword;
				String[] agencies;
				String[] publisher;
				
				String publishDate = publicationForm.getPublication_date().trim(); 
				String publishMonth = publicationForm.getPublication_month().trim();
				String publishYear = publicationForm.getPublication_year().trim();
				
				if (!StringUtils.isBlank(publishDate))
					publishDate = publishDate.concat("/");
				if (!StringUtils.isBlank(publishMonth))
					publishMonth = publishMonth.concat("/");
				
				publication.setTitle(publicationForm.getTitle().trim());
				publication.setDescription(publicationForm.getDescription().trim());
				
				String publicationDate = publishDate + publishMonth + publishYear;
				publication.setDatePublished(publicationDate);
				
				publication.setType(publicationForm.getType().trim());
				publication.setTypeOther(publicationForm.getType_other().trim());
							
				publication.setCoverage(publicationForm.getCoverage().trim());
				publication.setLanguage(publicationForm.getLanguage().trim());
				publication.setRights(publicationForm.getRights().trim());
				publication.setTotalPages(publicationForm.getTotalPages());
				publication.setStatus(Constants.BATCH_DRAFT);
				publication.setMember(member);
				publication.setDateRegistered(new Date());
				boolean status = ps.addPublication(publication);
				if(status)
				{
					publication = ps.loadPublication(publication.getPublicationId());
					keyword = splitKeyword(publicationForm.getKeywords().trim());
					agencies = splitAgencies(publicationForm.getAgencyNumber().trim());
					publisher = splitAgencies(publicationForm.getPublisherNumber().trim());
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
						//System.out.println(fls.loadFullAgencyList(Integer.valueOf(agencies[j])));
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
					forward = new ActionForward("/agency_pages/file_upload.jsp?id=" +  publication.getPublicationId());
				}
				else
					forward = mapping.findForward("home");
			}
		}catch(Exception ex)
		{
			logger.info("In class PublicationAction:addPublication()\n");
			ex.printStackTrace();
		}
			return forward;
	}
	
	public ActionForward openPublication(ActionMapping mapping,
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
					boolean keyPub_delete_status = kps.deleteKeyPubViaPublication(publication);
					boolean file_delete_status = fs.deleteFileViaPublication(publication);
					boolean publication_delete_status = ps.delPublication(id);
					if (file_delete_status && publication_delete_status && keyPub_delete_status)
					{
						msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("batch.delete.success"));
						saveMessages(request, msgs);
					}else
					{
						msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("batch.delete.failed"));
						saveMessages(request, msgs);
					}
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
				Member member = (Member)request.getSession().getAttribute("member");			
				if(member == null)
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
					Member member = (Member)request.getSession().getAttribute("member");			
					if(member == null)
					{
						forward = mapping.findForward("login");
					}else
					{
						boolean status = false;
						FileService fs = new FileServiceImpl();
						UploadedFile up = fs.loadFile(fileID);
						Integer pubID = up.getPublication().getPublicationId();
						//System.out.println("Publications ID " + pubID);
						PublicationService ps = new PublicationServiceImpl();
						Publication publication = ps.loadPublication(pubID);
						
						//System.out.println("File numbers: " + publication.getTotalFiles());
						Integer fileCount = Integer.parseInt(publication.getTotalFiles());
						fileCount = fileCount - 1;
						publication.setTotalFiles(fileCount.toString());
						ps.updatePublication(publication);
						status = fs.deleteFile(fileID);				
						if (status)
						{
							forward = new ActionForward("/agency_pages/file_upload.jsp?id=" + pubID);
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
					request.getSession().setAttribute("publication", publication);
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
					publication.setStatus(Constants.BATCH_SUBMITTED);
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
	
}
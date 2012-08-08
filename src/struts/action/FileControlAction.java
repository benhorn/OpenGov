package au.gov.nsw.records.digitalarchive.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;

public class FileControlAction extends BaseAction{

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
					Integer publicationID = up.getPublication().getPublicationId();
					System.out.println("Publications ID " + publicationID);
					status = fs.deleteFile(fileID);				
					if (status)
					{
						forward = new ActionForward("/agency_pages/file_upload.jsp?id=" + publicationID);
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
	
}

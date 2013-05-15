package au.gov.nsw.records.digitalarchive.struts.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;

public class DashboardAction extends BaseAction
{
		
	public ActionForward home(ActionMapping mapping, 
			 			      ActionForm form,
			 			      HttpServletRequest request, 
			 			      HttpServletResponse response)
	{
		
		ActionForward forward = null;
		forward = mapping.findForward("home");
		return forward;			
	}
	
	public ActionForward registerPub(ActionMapping mapping, 
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
			}
			else
			{	
				forward =  mapping.findForward("newPublication");	
			}
		}catch(Exception ex)
		{
			logger.info("In class DashboardAction:registerPub()\n");
			ex.printStackTrace();
		}
			return forward;
	}
	
	public ActionForward myDetails(ActionMapping mapping, 
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
			}
			else
			{	
				forward =  mapping.findForward("myDetails");	
			}
		}catch(Exception ex)
		{
			logger.info("In class DashboardAction:myDetails()\n");
			ex.printStackTrace();
		}
			return forward;
	}
	
	public ActionForward metaData(ActionMapping mapping, 
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
					PublicationService ps = new PublicationServiceImpl();
					Publication publication = ps.loadPublication(id);
					request.getSession().setAttribute("currentPub", publication);
					forward = mapping.findForward("updateMetadata");	
				}
		}catch(Exception ex){
				ex.printStackTrace();
			}
		}else
		{
			logger.info("In class DashboardAction:metaData()\n");
			forward = mapping.findForward("home");
		}
			return forward;			
		
	}
}

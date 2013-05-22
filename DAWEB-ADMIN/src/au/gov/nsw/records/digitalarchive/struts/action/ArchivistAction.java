package au.gov.nsw.records.digitalarchive.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import au.gov.nsw.records.digitalarchive.ORM.Archivist;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.service.ArchivistService;
import au.gov.nsw.records.digitalarchive.service.ArchivistServiceImpl;

public class ArchivistAction extends BaseAction {
	
	public ArchivistAction()
	{}
		
	public ActionForward loadArchivist(ActionMapping mapping, 
				 					   ActionForm form,
				 					   HttpServletRequest request, 
				 					   HttpServletResponse response) 
	{
			List<Archivist> list = null;
			ActionForward forward = null;
			ArchivistService as = new ArchivistServiceImpl();
		try 
		{
			Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");			
			if (archivist == null)
			{
				forward = mapping.findForward("exit");	
			}else
			{	
				list = as.browseArchivist();
				request.setAttribute("archivistList", list);
				forward = mapping.findForward("loadArchivist");
			}
		}catch (Exception ex) 
		{
			logger.info("Exception at ArchivistAction.loadArchivist()\n");
			ex.printStackTrace();
		}
			return forward;
	}

	
}

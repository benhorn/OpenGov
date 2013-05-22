package au.gov.nsw.records.digitalarchive.struts.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import au.gov.nsw.records.digitalarchive.ORM.Archivist;
import au.gov.nsw.records.digitalarchive.service.ArchivistService;
import au.gov.nsw.records.digitalarchive.service.ArchivistServiceImpl;
import au.gov.nsw.records.digitalarchive.struts.form.ArchivistLoginForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthenticationAction extends Action{
	
		public ActionForward execute(ActionMapping mapping, 
									 ActionForm form,
									 HttpServletRequest request, 
									 HttpServletResponse response) 
		{	
			ArchivistLoginForm archivistLoginForm = (ArchivistLoginForm) form;
			ArchivistService as = new ArchivistServiceImpl();
			ActionForward forward = null;
			ActionMessages msgs = new ActionMessages();
			try{
				Archivist archivist = as.archivistLogin(archivistLoginForm.getUserName(), archivistLoginForm.getPassword());
				if (archivist != null)
				{
					request.getSession().setAttribute("archivist", archivist);
					request.getSession().setMaxInactiveInterval(900);
					forward = mapping.findForward("success");
				}else
				{				
					msgs.add("loginError", new ActionMessage("form.loginError"));
					saveMessages(request, msgs);
					forward = mapping.getInputForward();
				}
			}catch(Exception ex){
				//logger.info("\n");
				ex.printStackTrace();
			}
			return forward;
		}
		
}

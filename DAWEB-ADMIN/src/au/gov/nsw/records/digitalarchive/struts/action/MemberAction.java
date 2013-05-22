package au.gov.nsw.records.digitalarchive.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import au.gov.nsw.records.digitalarchive.ORM.Archivist;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.base.Constants;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;
import au.gov.nsw.records.digitalarchive.service.MemberService;
import au.gov.nsw.records.digitalarchive.service.MemberServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;

public class MemberAction extends BaseAction{
	
	public MemberAction()
	{}
	
	public ActionForward loadMember(ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response) 
	{
			ActionForward forward = null;
			MemberService ms = new MemberServiceImpl();
	try{
			Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");			
			if( archivist == null)
			{
				forward = mapping.findForward("welcome");
			}else
			{
				
				List<Member> list = ms.browseMember();
				request.setAttribute("memberList", list);
				forward = mapping.findForward("loadMember");
			}
		}catch(Exception ex){
				logger.info("In class MemberAction:loadMember()\n");
				ex.printStackTrace();
		}
		return forward;		
	}

	public ActionForward viewMember(ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response) 
	{
		MemberService service = new MemberServiceImpl();
		Member member = null;
		String p = request.getParameter("id");
		Integer id = null;
		if(p!=null)
		{
			id = new Integer(p);
		}else
		{
			id = new Integer(0);
		}		
		try{
			member = service.loadMember(id);
			if (member!=null) 
			{	
				request.getSession().setAttribute("member", member);
			}	
		}catch(Exception ex){
			logger.info("In class MemberAction:viewMember()\n");
			ex.printStackTrace();
		}
		return mapping.findForward("memberdetail");		
	}
	
	public ActionForward deleteMember (ActionMapping mapping, 
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
					MemberService ms = new MemberServiceImpl();
					boolean status = ms.delMember(id);
					if (status)
					{
						msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("member.delete.success"));
					}else
					{
						msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("member.delete.failed"));
					}
					saveMessages(request, msgs);
					forward = mapping.findForward("loadArchivist");	
				}	
			}catch(Exception ex)
			{
					logger.info("In class MemberAction:deleteMember()\n");
					ex.printStackTrace();
			}
		}else
		{
			forward = mapping.findForward("home");
		}

			return forward;
	}
	
	public ActionForward switchAccount(ActionMapping mapping, 
										   ActionForm form,
										   HttpServletRequest request, 
										   HttpServletResponse response) 
	{
		MemberService ms = new MemberServiceImpl();
		Member member = (Member)request.getSession().getAttribute("member");
		Archivist archivist = (Archivist)request.getSession().getAttribute("archivist");
		Integer id = new Integer(request.getParameter("id"));
		ActionForward forward = null;
		try
		{
			if (archivist == null){
				forward = mapping.findForward("exit");	
			}else
			{	
				if (member != null)
				{
					Member memberForm = ms.loadMember(id);
					String action = request.getParameter("action");

					if ("De-Activate this account".equalsIgnoreCase(action))
					{
						memberForm.setActivated("n");
					}
					if ("Activate this account".equalsIgnoreCase(action))
					{
						memberForm.setActivated("y");
					}
					if ("Flag this member as Privileged".equalsIgnoreCase(action))
					{
						memberForm.setPrivileged("y");
					}
					if ("Unflag this member as Privileged".equalsIgnoreCase(action))
					{
						memberForm.setPrivileged("n");
					}
					Boolean status = ms.updateMember(memberForm);
					if (status)
					{						
						forward = new ActionForward("/member.do?method=viewMember&id="+id);
					}else
					{
						forward = new ActionForward("/member.do?method=viewMember&id="+id);
					}
				}
			}
		}catch(Exception ex)
		{
			logger.info("In class MemberAction:switchAccount()\n");
			ex.printStackTrace();
		}
			return forward;
	}

}

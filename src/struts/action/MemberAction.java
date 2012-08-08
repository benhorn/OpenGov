package au.gov.nsw.records.digitalarchive.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.service.MemberService;
import au.gov.nsw.records.digitalarchive.service.MemberServiceImpl;

public class MemberAction extends BaseAction {

	
	public ActionForward updateMember(ActionMapping mapping, 
									  ActionForm form,
									  HttpServletRequest request, 
									  HttpServletResponse response) 
	{
		MemberService ms = new MemberServiceImpl();
		ActionMessages msgs = new ActionMessages();
		try
		{
			Member member = (Member)request.getSession().getAttribute("member");
			member.setFirstname(request.getParameter("firstName").trim());
			member.setLastname(request.getParameter("lastName").trim());
			member.setPhone(request.getParameter("phone").trim());
			
			if(request.getParameter("email") != null && request.getParameter("email").length() > 0)
			{
				member.setEmail(request.getParameter("email").trim());
				member.setLogin(request.getParameter("email").trim());
			}
			
			if	(request.getParameter("loginPwd") != null && request.getParameter("loginPwd").length()>0)
			{
				member.setPassword(request.getParameter("loginPwd").trim());
			}
			
			request.getSession().setAttribute("member",member);
			boolean status = ms.updateMember(member);
			if (status){
				msgs.add("modiMemberStatus",new ActionMessage("member.update.success"));
			}else{				
				msgs.add("modiMemberStatus",new ActionMessage("member.update.fail"));
			}
			saveMessages(request, msgs);
		}catch(Exception ex){
			logger.info("In class MemberAction:updateMember()\n");
			ex.printStackTrace();
		}
		return mapping.findForward("myDetails");		
	}
	
}
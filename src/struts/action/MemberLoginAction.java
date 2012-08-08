package au.gov.nsw.records.digitalarchive.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.service.MemberService;
import au.gov.nsw.records.digitalarchive.service.MemberServiceImpl;
import au.gov.nsw.records.digitalarchive.struts.form.MemberLoginForm;


public class MemberLoginAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, 
							     ActionForm form,
							     HttpServletRequest request, 
							     HttpServletResponse response) 
	{	
		MemberLoginForm memberLoginForm = (MemberLoginForm) form;
		MemberService ms = new MemberServiceImpl();
		ActionForward forward = null;
		Member member;
		ActionMessages msgs = new ActionMessages();
		String activated = null;
		try
		{
			member = ms.memberLogin(memberLoginForm.getLoginName(), memberLoginForm.getLoginPwd());
			if (member != null)
			{
				activated = (String)ms.accountStatus(member.getLogin().trim()).get(0);
				if(("y").equalsIgnoreCase(activated.toLowerCase())) 
				{
					request.getSession().setAttribute("member", member);
					request.getSession().setMaxInactiveInterval(900);
					forward = new ActionForward("/agency_pages/index.jsp");
				}else
				{
					msgs.add("account_not_activated", new ActionMessage("form.account.inactive"));
					saveMessages(request, msgs);
					forward = mapping.getInputForward();
				}
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
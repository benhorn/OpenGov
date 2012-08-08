package au.gov.nsw.records.digitalarchive.struts.action;

import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;
import au.gov.nsw.records.digitalarchive.service.MemberService;
import au.gov.nsw.records.digitalarchive.service.MemberServiceImpl;
import au.gov.nsw.records.digitalarchive.struts.form.RegistrationForm;

public class RegistrationAction extends BaseAction{
	
	private static final Pattern rfc2822_format = Pattern
			.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
	
	private static final Pattern phone_format = Pattern
			.compile("^(\\d{8})|(2[3-9]\\d{7})|(3[4-9]\\d{7})|(7[3-9]\\d{7})|(8\\d{8})|(4\\d{8})|(13\\d{4})|(1300\\d{6})|(1800\\d{6})|$");

	public ActionForward register(ActionMapping mapping, 
								  ActionForm form,
								  HttpServletRequest request, 
								  HttpServletResponse response) 
	{
		ActionMessages msgs = new ActionMessages();

		if (form instanceof RegistrationForm) 
		{
			RegistrationForm registrationForm = (RegistrationForm) form;
			MemberService ms = new MemberServiceImpl();
			FullAgencyListService fls = new FullAgencyListServiceImpl();
			ActionForward forward = null;
				
			if (("").equals(registrationForm.getAgencyNumber())
					|| ("null").equals(registrationForm.getAgencyNumber())
					|| registrationForm.getAgencyNumber() == null
					|| registrationForm.getAgencyNumber().length() < 1
					|| ("").equals(registrationForm.getFirstName())
					|| registrationForm.getFirstName() == null
					|| registrationForm.getFirstName().length() < 1
					|| ("").equals(registrationForm.getLastName())
					|| registrationForm.getLastName() == null
					|| registrationForm.getLastName().length() < 1
					|| ("").equals(registrationForm.getEmail())
					|| registrationForm.getEmail() == null
					|| registrationForm.getEmail().length() < 1
					|| ("").equals(registrationForm.getLoginPwd())
					|| registrationForm.getLoginPwd() == null
					|| registrationForm.getLoginPwd().length() < 1
					|| ("").equals(registrationForm.getPhone())
					|| registrationForm.getPhone() == null
					|| registrationForm.getPhone().length() < 1) 
				{
					msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("form.all.required"));
					request.getSession().setAttribute("agencyNumber", registrationForm.getAgencyNumber());
					saveMessages(request, msgs);
					return mapping.getInputForward();
				}

			if (!rfc2822_format.matcher(registrationForm.getEmail()).matches()) 
				{
					msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.email"));
					request.getSession().setAttribute("agencyNumber", registrationForm.getAgencyNumber());
					saveMessages(request, msgs);
					return mapping.getInputForward();
				}

			try 
			{
				FullAgencyList fullList = new FullAgencyList();
				fullList = fls.loadFullAgencyList(Integer.parseInt(registrationForm.getAgencyNumber().trim()));
				
				System.out.println("\n" + fullList.getAgencyName());
				
				Member member = new Member();
				member.setFirstname(registrationForm.getFirstName().trim());
				member.setLastname(registrationForm.getLastName().trim());
				member.setPhone(registrationForm.getPhone().trim());
				member.setEmail(registrationForm.getEmail().trim());
				member.setLogin(registrationForm.getEmail().trim());
				member.setPassword(registrationForm.getLoginPwd().trim());
				member.setLoginTimes(new Integer(0));
				member.setRegistrationDate(new Date());
				member.setActivated("y");
				member.setFullAgencyList(fullList);
			
				boolean status = ms.addMember(member);
				if (status)
				{
					return mapping.findForward("success");
				}
				else
				{
					msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.unknown"));
					saveMessages(request, msgs);
					return mapping.getInputForward();
				}
			} catch (Exception e) 
			{
				logger.info("In class RegistrationAction:register()\n");
				e.printStackTrace();
			}
			return forward;
		} else {
			msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.unknown"));
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
	}
	

}

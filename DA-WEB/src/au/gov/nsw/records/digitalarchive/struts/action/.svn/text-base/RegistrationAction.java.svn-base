package au.gov.nsw.records.digitalarchive.struts.action;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.notification.EmailNotificationAction;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;
import au.gov.nsw.records.digitalarchive.service.MemberService;
import au.gov.nsw.records.digitalarchive.service.MemberServiceImpl;
import au.gov.nsw.records.digitalarchive.struts.form.RegistrationForm;

public class RegistrationAction extends BaseAction{
	
	private static final Pattern rfc2822_format = Pattern
			.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
	
	private static final Pattern phone_format = Pattern
			.compile("^(?:\\(?(?:\\+?61|0)2\\)?[ -]?)?(?:3[ -]?[38]|[46-9][ -]?[0-9]|5[ -]?[0-35-9])(?:[ -]?[0-9]){6}|(?:\\+?61|0)4 ?(?:(?:[01] ?[0-9]|2 ?[0-57-9]|3 ?[1-9]|4 ?[7-9]|5 ?[018]) ?[0-9]|3 ?0 ?[0-5])(?: ?[0-9]){5}$");
		
	private static final Pattern pwd_format = Pattern
			.compile("^.*(?=.{4,8})(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9!@#$%]+$");

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

			if (!phone_format.matcher(registrationForm.getPhone()).matches()) 
			{
				msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.phone"));
				request.getSession().setAttribute("agencyNumber", registrationForm.getAgencyNumber());
				saveMessages(request, msgs);
				return mapping.getInputForward();
			}
						
			if (!pwd_format.matcher(registrationForm.getLoginPwd()).matches()) 
			{
				msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.password"));
				request.getSession().setAttribute("agencyNumber", registrationForm.getAgencyNumber());
				saveMessages(request, msgs);
				return mapping.getInputForward();
			}
			
			try 
			{
				FullAgencyList fullList = new FullAgencyList();
				fullList = fls.loadFullAgencyList(Integer.parseInt(registrationForm.getAgencyNumber().trim()));
				MessageDigest md;
				
				Member member = new Member();
				member.setFirstname(registrationForm.getFirstName().trim());
				member.setLastname(registrationForm.getLastName().trim());
				member.setPhone(registrationForm.getPhone().trim());
				member.setEmail(registrationForm.getEmail().trim());
				member.setLogin(registrationForm.getEmail().trim());
				
				try {
					md = MessageDigest.getInstance("md5");
                    md.update(registrationForm.getLoginPwd().getBytes("UTF-8"));
                    byte[] result =  md.digest();
                    member.setPassword(Hex.encodeHexString(result));
                    
				} catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
				}

				member.setLoginTimes(new Integer(0));
				member.setRegistrationDate(new Date());
				member.setLastReset(new Date());
				member.setActivated("y");
				member.setPrivileged("n");
				member.setFullAgencyList(fullList);
			
				boolean status = ms.addMember(member);
				if (status)
				{
					List<String> recipient =  new ArrayList<String>();
					recipient.add(registrationForm.getEmail().trim());
					EmailNotificationAction mailSender = new EmailNotificationAction();
					mailSender.prepare(0, request);
					mailSender.sendEmail(recipient, "OpenGov registration", 
										 "Hi " + registrationForm.getFirstName().trim() + ":<br/>" +
										 "<p>Thank you for registering with <a href=\"https://www.opengov.nsw.gov.au/\">OpenGov</a>. " +
										 "Please be advised that this site is for the publication of NSW government information only. " +
										 "Responsibility for ensuring that information uploaded to the site is suitable for publication lies " +
										 "with the registered user. State Records accepts no liability for any loss or damage whatsoever from " +
										 "the use or access or inability to access the site or the information contained on the site.</p> <p>Your login " +
										 "is:&nbsp;" + registrationForm.getEmail().trim() + "<br/>" + 
										 "Your password is:&nbsp;" + registrationForm.getLoginPwd().trim() + "</p>" + 
										 "<p>If you have any questions please contact us at opengov@records.nsw.gov.au</p>");
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

package au.gov.nsw.records.digitalarchive.struts.action;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.notification.EmailNotificationAction;
import au.gov.nsw.records.digitalarchive.service.MemberService;
import au.gov.nsw.records.digitalarchive.service.MemberServiceImpl;
import au.gov.nsw.records.digitalarchive.system.AUDate;

public class MemberAction extends BaseAction {

	private static final Pattern rfc2822_format = Pattern
			.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
	
	private static final Pattern phone_format = Pattern
			.compile("^(?:\\(?(?:\\+?61|0)2\\)?[ -]?)?(?:3[ -]?[38]|[46-9][ -]?[0-9]|5[ -]?[0-35-9])(?:[ -]?[0-9]){6}$");
	
	private static final Pattern pwd_format = Pattern
			.compile("^.*(?=.{4,8})(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9!@#$%]+$");
	
	
	public ActionForward chgDetail(ActionMapping mapping, 
									  ActionForm form,
									  HttpServletRequest request, 
									  HttpServletResponse response) 
	{
		MemberService ms = new MemberServiceImpl();
		ActionMessages msgs = new ActionMessages();
		Member member = (Member)request.getSession().getAttribute("member");

		if (!rfc2822_format.matcher(request.getParameter("email").trim()).matches()) 
		{
			msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.email"));
			request.getSession().setAttribute("member", member);
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
		
		if (!phone_format.matcher(request.getParameter("phone").trim()).matches()) 
		{
			msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.phone"));
			request.getSession().setAttribute("member", member);
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
		
		
		if (!pwd_format.matcher(request.getParameter("loginPwd").trim()).matches()) 
		{
			msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.password"));
			request.getSession().setAttribute("member", member);
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
		
		try
		{
			member.setFirstname(request.getParameter("firstName").trim());
			member.setLastname(request.getParameter("lastName").trim());
			member.setPhone(request.getParameter("phone").trim());
			MessageDigest md;

			if(request.getParameter("email") != null && request.getParameter("email").length() > 0)
			{
				member.setEmail(request.getParameter("email").trim());
				member.setLogin(request.getParameter("email").trim());
			}
			
			if	(request.getParameter("loginPwd") != null && request.getParameter("loginPwd").length() > 0)
			{
				try 
				{
					md = MessageDigest.getInstance("md5");
                    md.update(request.getParameter("loginPwd").trim().getBytes("UTF-8"));
                    byte[] result =  md.digest();
                    member.setPassword(Hex.encodeHexString(result)); 
				} catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
				}
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
			logger.info("In class MemberAction:chgDetail()\n");
			ex.printStackTrace();
		}
		return mapping.findForward("myDetails");		
	}
	
	public ActionForward getPwd(ActionMapping mapping, 
			  					ActionForm form,
			  					HttpServletRequest request, 
			  					HttpServletResponse response) 
	{
		String emailAddress = "";
		if(!rfc2822_format.matcher(request.getParameter("emailAddress").trim()).matches())
		{		
			request.setAttribute("warnings", "Invalid email address");
			return mapping.findForward("login");
		}
		
		try 
		{
			MemberService ms = new MemberServiceImpl();
			if (!ms.chkLoginName(request.getParameter("emailAddress").trim()))
			{
				emailAddress = request.getParameter("emailAddress").trim();
                Member foundMember = ms.loadMemberViaEmail(emailAddress);
				AUDate au = new AUDate();
				Calendar startDate = Calendar.getInstance();startDate.setTime(foundMember.getLastReset());  
				Calendar today = Calendar.getInstance();today.setTime(new Date());
				long daysLapsed = au.daysBetween(startDate, today);
				
				if (daysLapsed > 1)
				{
					String newPassword = gen8DigitPwd();
					MessageDigest md;

					md = MessageDigest.getInstance("md5");
                    md.update(newPassword.getBytes("UTF-8"));
                    byte[] result =  md.digest();
                    
                    foundMember.setPassword(Hex.encodeHexString(result));
                    foundMember.setLastReset(new Date());
                    ms.updateMember(foundMember);
                    List<String> recipient =  new ArrayList<String>();
					recipient.add(emailAddress);
					EmailNotificationAction mailSender = new EmailNotificationAction();
					mailSender.prepare(0, request);
					mailSender.sendEmail(recipient, "OpenGov Account Update", 
										 "Hi " + foundMember.getFirstname() + ":<br/>" +
										 "<p>Your new OpenGov password is: <strong>" + newPassword + "</strong></p>" + 
										 "<p>If you have any questions please contact us at opengov@records.nsw.gov.au</p>");
					request.setAttribute("warnings", "An email containing your latest password has been sent to " + emailAddress);
					return mapping.findForward("login");
				}else
				{
					request.setAttribute("warnings", "It has been less than 24 hours since a new password was generated for the same account.");
					return mapping.findForward("login");
				}
			}else
			{
					request.setAttribute("warnings", "This account does not exist");
					return mapping.findForward("login");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapping.findForward("login");
	}
		
	private String gen8DigitPwd()
	{
		String strPassword="";
		try{
			Random randomPassword = new Random();
			int i=randomPassword.nextInt(10);
			int j = randomPassword.nextInt(100);
			int k = randomPassword.nextInt(100);
			int l = randomPassword.nextInt(10);
			int m = randomPassword.nextInt(10);
			strPassword = i + "T" + j + k + "P" + l;

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return strPassword;
	}
	
}
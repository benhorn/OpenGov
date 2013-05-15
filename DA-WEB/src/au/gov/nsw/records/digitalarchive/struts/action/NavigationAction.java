package au.gov.nsw.records.digitalarchive.struts.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import au.gov.nsw.records.digitalarchive.ORM.Member;

public class NavigationAction extends DispatchAction{
	
	protected Map getKeyMethodMap()
	{
		  Map map =  new HashMap();
		  map.put("dashboard.home","home");
		  return map;
	}
	
	protected ActionForward dispatchMethod(ActionMapping mapping,
            							   ActionForm form,
            							   HttpServletRequest request,
            							   HttpServletResponse response,
            							   String name) throws Exception 
    {
		try {
				return super.dispatchMethod(mapping, form, request, response, name);
			} catch(NoSuchMethodException ex) {
				return unspecified(mapping, form, request, response);
			}
    }
	
	@Override
	protected ActionForward unspecified(ActionMapping mapping, 
										ActionForm form,
										HttpServletRequest request, 
										HttpServletResponse response)throws Exception 
	{
			return mapping.findForward("home");
	}
	
	public ActionForward home(ActionMapping mapping, 
			 			     ActionForm form, 
			 			     HttpServletRequest request, 
			 			     HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			Member member = (Member)request.getSession().getAttribute("member");			
			if(member==null)
			{
				forward = mapping.findForward("login");
			}else
			{
				forward = mapping.findForward("home");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
			return forward;			
	
	}


}

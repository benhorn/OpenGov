package au.gov.nsw.records.digitalarchive.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.log4j.*;


public class BaseAction extends DispatchAction {
	
	public static Logger logger = Logger.getLogger(BaseAction.class);
	
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
										HttpServletResponse response) throws Exception 
	{
			return mapping.findForward("home");
	}
	
}
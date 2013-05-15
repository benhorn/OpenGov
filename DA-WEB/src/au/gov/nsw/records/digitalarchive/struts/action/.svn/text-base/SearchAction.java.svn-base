package au.gov.nsw.records.digitalarchive.struts.action;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import au.gov.nsw.records.digitalarchive.struts.form.SearchForm;
import au.gov.nsw.records.digitalarchive.system.HTMLInputFilter;

public class SearchAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, 
		     					 ActionForm form,
		     					 HttpServletRequest request, 
		     					 HttpServletResponse response) throws Exception
	{	
		SearchForm searchForm = (SearchForm)form;
		ActionForward forward = null;
		String searchQuery = "";
		String inputQuery = searchForm.getQuery().trim();
		String xml = request.getParameter("xml");
		String urlSegment = "";
		final HTMLInputFilter inputFilter = new HTMLInputFilter(false);
		
		if (inputQuery == null || ("").equals(inputQuery))
		{	searchQuery = "";
			urlSegment = "";
		}
		else
		{
			if (inputQuery.contains("&"))
			{	searchQuery = inputQuery;
				urlSegment = inputFilter.filter(URLEncoder.encode(searchQuery,"ISO-8859-1"));
			}
			else
			{	searchQuery = new HTMLInputFilter().filter(searchForm.getQuery().trim());
				urlSegment = URLEncoder.encode(searchQuery,"ISO-8859-1");
			}
		}
		
		
		if (("true").equalsIgnoreCase(xml))
		{
				forward = new ActionForward("/search_result/search_result_xml.jsp?query=" + urlSegment);
		}else
		{
			
				forward = new ActionForward("/search_result/search_result.jsp?query=" + urlSegment);	
		}
		
		return forward;
	}

}

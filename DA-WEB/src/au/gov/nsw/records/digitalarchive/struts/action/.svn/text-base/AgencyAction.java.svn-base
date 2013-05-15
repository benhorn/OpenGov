package au.gov.nsw.records.digitalarchive.struts.action;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.base.AgencyJSONFormat;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.service.AgencyPublicationService;
import au.gov.nsw.records.digitalarchive.service.AgencyPublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;

public class AgencyAction extends BaseAction{
	
	public AgencyAction()
	{}
	
	
	public ActionForward agencyJSON(ActionMapping mapping, 
	 		    					ActionForm form, 
	 		    					HttpServletRequest request, 
	 		    					HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			FullAgencyListService fls = new FullAgencyListServiceImpl();
			AgencyPublicationService aps = new AgencyPublicationServiceImpl();
			List<Integer> apList = aps.listAgencyWithPublications();
			int listSize = apList.size();
			int i = 0;
			Iterator<Integer> apIt = apList.iterator();
			StringBuilder sb = new StringBuilder();
			sb.append("\"Agencies\":[");
			while (apIt.hasNext()) {
				Integer agencyID = (Integer) apIt.next();
				FullAgencyList fAgencyList = fls.loadFullAgencyList(agencyID);
				String url = "";
				if(("").equals(fAgencyList.getBosId()) || fAgencyList.getBosId() == null)
				{ 
					url = "";
				}
				else
				{ 	
					url = new URI("http", "search.records.nsw.gov.au", "/" + fAgencyList.getBosId(), null, null).toASCIIString();
				}
				
				AgencyJSONFormat AgencyJSON = new AgencyJSONFormat(agencyID.toString(), 
																   fAgencyList.getAgencyName(), 
																   url);
				
				sb.append(AgencyJSON.toJSONString());
				i++;
				if (i == listSize)
					sb.append("");
				else
					sb.append(",");
			}
			sb.append("]");
			request.getSession().setAttribute("JSONString", sb.toString().replace("\\/", "/"));
			forward = new ActionForward("/agency/agencyJSON.jsp");
		}catch(Exception ex)
		{
			logger.info("In class AgencyAction:agencyJSON()\n");
			ex.printStackTrace();
		}
		return forward;
	}	
}

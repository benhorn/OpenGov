package au.gov.nsw.records.digitalarchive.struts.action;


import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.base.Constants;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;

public class DashboardAction extends BaseAction
{
		
	public ActionForward home(ActionMapping mapping, 
			 			      ActionForm form,
			 			      HttpServletRequest request, 
			 			      HttpServletResponse response)
	{
		
		ActionForward forward = null;
		forward = mapping.findForward("home");
		return forward;			
	}
	
	public ActionForward registerPub(ActionMapping mapping, 
									 ActionForm form,
									 HttpServletRequest request,
									 HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			Member member = (Member)request.getSession().getAttribute("member");
			if (member == null)
			{
				forward = mapping.findForward("login");	
			}
			else
			{	
				forward =  mapping.findForward("newPublication");	
			}
		}catch(Exception ex)
		{
			logger.info("In class DashboardAction:registerPub()\n");
			ex.printStackTrace();
		}
			return forward;
	}
	
	public ActionForward myDetails(ActionMapping mapping, 
			 					   ActionForm form,
			 					   HttpServletRequest request,
			 					   HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			Member member = (Member)request.getSession().getAttribute("member");
			if (member == null)
			{
				forward = mapping.findForward("login");	
			}
			else
			{	
				forward =  mapping.findForward("myDetails");	
			}
		}catch(Exception ex)
		{
			logger.info("In class DashboardAction:myDetails()\n");
			ex.printStackTrace();
		}
			return forward;
	}
	
	public ActionForward myStats(ActionMapping mapping, 
			   					   ActionForm form,
			   					   HttpServletRequest request,
			   					   HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			Member member = (Member)request.getSession().getAttribute("member");
			if (member == null)
			{
				forward = mapping.findForward("login");	
			}
			else
			{	
				FullAgencyListService fal = new FullAgencyListServiceImpl();
				FullAgencyList fls = fal.loadFullAgencyList(member.getFullAgencyList().getFullAgencyListId());
				Integer agencyID = fls.getFullAgencyListId();
				HashMap<String, String> JSONMap = new HashMap<String, String>();
				
				if (hasLogs(new File(Constants.LOGGER), agencyID.toString()))
				{
					//File myStat = new File(Constants.LOGGER + agencyID + "\\" + lastFileModified(new File(Constants.LOGGER + "24135")).getName());
					FileReader lastModifiedJSONFile = new FileReader(Constants.LOGGER + agencyID + "\\" + lastFileModified(new File(Constants.LOGGER + agencyID)).getName());
					JSONParser parser = new JSONParser();
					Object obj = parser.parse(lastModifiedJSONFile);
					  
					JSONObject jsonObject = (JSONObject)obj;
					JSONArray publications = (JSONArray) jsonObject.get("publications");
					
					JSONMap.put("dateRun", (String) jsonObject.get("DateRun"));
					for (int i=0; i < publications.size(); i++) 
					{ 
				          JSONObject pubObj = (JSONObject)publications.get(i);
				          //System.out.println((String)pubObj.get("title") + " " + (String)pubObj.get("cumulative"));
				          JSONMap.put((String)pubObj.get("title"), (String)pubObj.get("cumulative"));
				    }
				}else
				{
					JSONMap.put(null, null);
				}
				System.out.println("Agency ID:" + fls.getFullAgencyListId());
				request.getSession().setAttribute("JSONMap", JSONMap);
				forward =  mapping.findForward("myStats");	
			}
		}catch(Exception ex)
		{
			logger.info("In class DashboardAction:myDetails()\n");
			ex.printStackTrace();
		}
		return forward;
	}
	
	
	
	public ActionForward metaData(ActionMapping mapping, 
			   					  ActionForm form,
			   					  HttpServletRequest request,
			   					  HttpServletResponse response)
	{
		ActionForward forward = null;
		Integer id = null;
		
		if (!("").equalsIgnoreCase(request.getParameter("id")))
		{
			id = new Integer(request.getParameter("id"));
			try
			{
				Member member = (Member)request.getSession().getAttribute("member");			
				if(member == null)
				{
					forward = mapping.findForward("login");
				}else
				{
					PublicationService ps = new PublicationServiceImpl();
					Publication publication = ps.loadPublication(id);
					request.getSession().setAttribute("currentPub", publication);
					forward = mapping.findForward("updateMetadata");	
				}
		}catch(Exception ex){
				ex.printStackTrace();
			}
		}else
		{
			logger.info("In class DashboardAction:metaData()\n");
			forward = mapping.findForward("home");
		}
			return forward;			
		
	}
	
	private boolean hasLogs(File inputDIR, String agencyNumber)
	{
		String[] DIRs = inputDIR.list();
		boolean result = false;
		for (int i = 0; i < DIRs.length; i++) {
			result = DIRs[i].contains(agencyNumber);
		}
		return result;
	}
	
	/*
	 *  This method is used find the latest file from the agency directory which contains the list of 
	 *  JSON files from monthly log 
	 */
	public File lastFileModified(File fl) {
		File[] files = fl.listFiles(new FileFilter() {			
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastMod = Long.MIN_VALUE;
		File choise = null;
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				choise = file;
				lastMod = file.lastModified();
			}
		}
		return choise;
	}
	
  public static void main(String[] args) throws Exception
  {
	  DashboardAction da = new DashboardAction();
	  FileReader lastModifiedJSONFile = new FileReader(Constants.LOGGER + "24135\\" + da.lastFileModified(new File(Constants.LOGGER + "24135")).getName());
	  JSONParser parser = new JSONParser();
	  Object obj = parser.parse(lastModifiedJSONFile);
	  
	  JSONObject jsonObject = (JSONObject)obj;
	  String dateRun = (String) jsonObject.get("DateRun");
	  JSONArray publications = (JSONArray) jsonObject.get("publications");
  
	  for (int i=0; i < publications.size(); i++) { 
          JSONObject pubObj = (JSONObject)publications.get(i);
          System.out.println((String)pubObj.get("title") + " " + (String)pubObj.get("cumulative"));
        }
  }
	
}

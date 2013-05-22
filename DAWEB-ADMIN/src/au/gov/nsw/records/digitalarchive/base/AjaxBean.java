package au.gov.nsw.records.digitalarchive.base;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import au.gov.nsw.records.digitalarchive.ORM.BosList;
import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Keyword;
import au.gov.nsw.records.digitalarchive.ORM.TempList;
import au.gov.nsw.records.digitalarchive.service.BOSListService;
import au.gov.nsw.records.digitalarchive.service.BOSListServiceImpl;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;
import au.gov.nsw.records.digitalarchive.service.KeywordService;
import au.gov.nsw.records.digitalarchive.service.KeywordServiceImpl;
import au.gov.nsw.records.digitalarchive.service.MemberService;
import au.gov.nsw.records.digitalarchive.service.MemberServiceImpl;
import au.gov.nsw.records.digitalarchive.service.TempListService;
import au.gov.nsw.records.digitalarchive.service.TempListServiceImpl;

public class AjaxBean extends BaseLog {

	private String JString = null;
	private String tagString = null;
	private String jsonString = null;
	
	public String[][] getAgencies(){
		String[][] options = null;
		BOSListService service = new BOSListServiceImpl();
		JSONObject json = new JSONObject();
		try{
			List list = service.browseBosList();
			BosList bosList = null;
			int i = 0;
			if (list!=null)
			{
				options = new String[list.size()][2];
				Iterator it = list.iterator();
				while(it.hasNext())
				{
					bosList = (BosList)it.next();
					options[i][0] = Integer.toString(bosList.getAgencyNumber());
					options[i][1] = bosList.getAgencyName();
					//json.put(agencyList.getAgencyNumber(), agencyList.getAgencyTitle());
					i++;
				}
			}else{
				options = new String[1][2];
				options[0][0] ="";
				options[0][1] ="Please select from below";
			}
			JString = json.toString();
		}catch(Exception ex){
			logger.info("In class AjaxBean:getAgencies()\n");
			ex.printStackTrace();			
		}
		return options;
	}
	
	public String returnJString()
	{
		return this.JString;
	}
	
	public String getKeywords()
	{
		KeywordService ks = new KeywordServiceImpl();
		JSONObject json = new JSONObject();
		JSONArray list1 = new JSONArray();
		try {
			List<Keyword> list = ks.browseKeywords();
			Keyword keyword = null;
			int i = 0;
			Iterator<Keyword> it = list.iterator();
			while (it.hasNext())
			{
				keyword = (Keyword)it.next();
				json.put("id", keyword.getKeywordId().toString());
				json.put("name", keyword.getKeyword());
				i++;
			}
			tagString = json.toString();
		} catch (Exception e) {
			logger.info("In class AjaxBean:getTags()\n");
			e.printStackTrace();
		}
		return this.tagString;
	}

	public boolean chkLoginName(String loginName){
		MemberService ms = new MemberServiceImpl();
		boolean result = false;
		try{
			result = ms.chkLoginName(loginName.trim());
		}catch(Exception ex){
			logger.info("In class AjaxBean:chkLoginName()\n");
			ex.printStackTrace();			
		}
		return result;
	}
	
	public boolean chkAgencyName(String agencyName){
		FullAgencyListService fls = new FullAgencyListServiceImpl();
		boolean result = false;
		try{
			result = fls.chkAgencyName(agencyName.trim());
		}catch(Exception ex){
			logger.info("In class AjaxBean:chkAgencyName()\n");
			ex.printStackTrace();			
		}
		return result;
	}
	
	public boolean chkKeyword(String keyword){
		KeywordService ts = new KeywordServiceImpl();
		boolean result = false;
		try{
			result = ts.chkKeyword(keyword);
		}catch(Exception ex){
			logger.info("In class AjaxBean:chkKeyword()\n");
			ex.printStackTrace();			
		}
		return result;
	}
	
	public boolean addKeyword(String keyword)
	{
		KeywordService ts = new KeywordServiceImpl();
		Keyword newKeyword = new Keyword();
		newKeyword.setKeyword(keyword.toLowerCase());
		boolean result = false;
		try {
			result = ts.addKeyword(newKeyword);
		} catch (Exception e) {
			logger.info("In class AjaxBean:addKeyword()\n");
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean addAgency(String agencyName)
	{
		FullAgencyListService fls = new FullAgencyListServiceImpl();
		FullAgencyList fullAgency = new FullAgencyList();
		TempListService tls = new TempListServiceImpl();
		TempList tempList = new TempList();
		tempList.setName(agencyName.trim());
		boolean result = false;
		try 
		{	
			boolean status = tls.addTempList(tempList);
			if (status) 
			{
				tempList = tls.loadTempList(tempList.getTempListId());
				fullAgency.setTempId(tempList.getTempListId());
				fullAgency.setAgencyName(tempList.getName());
				fls.addFullAgencyList(fullAgency);
				result = true;
			}else
			{
				result = false;
			}
			
		} catch (Exception e) {
			logger.info("In class AjaxBean:addAgency()\n");
			e.printStackTrace();
		}
		return result;
	}
	
	public String retrievePwd(String email) throws Exception
	{
		MemberService ms = new MemberServiceImpl();
		String result = "Not found";
		if (ms.chkLoginName(email))
		{
			result = ms.retrievePassword(email).get(0);
			
		}
		
		return result;
	}
	
}

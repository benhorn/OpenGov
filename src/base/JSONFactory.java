package au.gov.nsw.records.digitalarchive.base;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Keyword;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;
import au.gov.nsw.records.digitalarchive.service.KeywordService;
import au.gov.nsw.records.digitalarchive.service.KeywordServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublisherPublicationService;
import au.gov.nsw.records.digitalarchive.service.PublisherPublicationServiceImpl;

public class JSONFactory extends BaseLog 
{
	@SuppressWarnings("unchecked")
	public String keywordJSON() throws Exception
	{
		KeywordService ks = new KeywordServiceImpl();
		JSONArray keywords = new JSONArray();
		List<Keyword> list = ks.browseKeywords();
		Keyword keyword = null;
		int i = 0;
		Iterator<Keyword> it = list.iterator();
		while (it.hasNext())
		{
			keyword = (Keyword)it.next();
			keywords.add(new JSONFormat(keyword.getKeywordId().toString(), keyword.getKeyword()));
			i++;
		}
		  StringWriter out = new StringWriter();
		  try {
			  keywords.writeJSONString(out);
		} catch (IOException e) {
			logger.info("In class JSONFactory:tagJSON()\n");
			e.printStackTrace();
		}
		  return out.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String keywordJSON(String keyword) throws Exception
	{
		KeywordService ks = new KeywordServiceImpl();		
		JSONArray keywords = new JSONArray();
		String keywordArray[];
   		StringTokenizer st = new StringTokenizer(keyword, ",");   		
   		keywordArray = new String[st.countTokens()];
   		int i = 0;
   
   		while(st.hasMoreTokens())
    	{
    	  	keywordArray[i] = st.nextToken();
          	i++;
    	}
   		for(int j = 0; j < keywordArray.length; j++)
		{
   	   		keywords.add(new JSONFormat(ks.loadKeyword(Integer.valueOf(keywordArray[j])).getKeywordId().toString(), ks.loadKeyword(Integer.valueOf(keywordArray[j])).getKeyword()));
		}
		  StringWriter out = new StringWriter();

		  keywords.writeJSONString(out);
		  return out.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String AgenciesJSON(String agencyName) throws Exception
	{
		FullAgencyListService fas = new FullAgencyListServiceImpl();		
		JSONArray agencies = new JSONArray();
		List<FullAgencyList> list = fas.list4AutoComplete(agencyName);
		FullAgencyList fList = null;
		int i = 0;
		Iterator<FullAgencyList> it = list.iterator();
		while (it.hasNext())
		{
			fList = (FullAgencyList)it.next();
			agencies.add(new JSONFormat(Integer.toString(fList.getFullAgencyListId()), fList.getAgencyName()));
			i++;
		}
		  StringWriter out = new StringWriter();
		  try {
			  agencies.writeJSONString(out);
		} catch (IOException e) {
			logger.info("In class JSONFactory:AgenciesJSON()\n");
			e.printStackTrace();
		}
		  return out.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String tokenizedAgencyJSON(String agencyNumber) throws Exception
	{
		FullAgencyListService fls = new FullAgencyListServiceImpl();		
		JSONArray agencies = new JSONArray();
		String agencyArray[];
   		StringTokenizer st = new StringTokenizer(agencyNumber, ",");   		
   		agencyArray = new String[st.countTokens()];
   		int i = 0;
   		System.out.println("Agency number is here: " + agencyNumber);
   		while(st.hasMoreTokens())
    	{
   			agencyArray[i] = st.nextToken();
          	i++;
    	}
   		for(int j = 0; j < agencyArray.length; j++)
		{
   	   		agencies.add(new JSONFormat(fls.loadFullAgencyList(Integer.valueOf(agencyArray[j])).getFullAgencyListId().toString(), 
   	   									fls.loadFullAgencyList(Integer.valueOf(agencyArray[j])).getAgencyName()));
   			
		}
		  StringWriter out = new StringWriter();
		  agencies.writeJSONString(out);
		  return out.toString();
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public String publisherJSON(String publisher) throws Exception
	{
		FullAgencyListService fas = new FullAgencyListServiceImpl();		
		JSONArray agencies = new JSONArray();
		List<FullAgencyList> list = fas.list4AutoComplete(publisher);
		FullAgencyList fList = null;
		int i = 0;
		Iterator<FullAgencyList> it = list.iterator();
		while (it.hasNext())
		{
			fList = (FullAgencyList)it.next();
			agencies.add(new JSONFormat(Integer.toString(fList.getFullAgencyListId()), fList.getAgencyName()));
			i++;
		}
		  StringWriter out = new StringWriter();
		  try {
			  agencies.writeJSONString(out);
		} catch (IOException e) {
			logger.info("In class JSONFactory:publisherJSON()\n");
			e.printStackTrace();
		}
		  return out.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String prePopulatedAgency(String publicationID) throws Exception
	{
		FullAgencyListService fas = new FullAgencyListServiceImpl();
		List<FullAgencyList> fList = fas.loadAgencyViaPublication(publicationID.trim());
		JSONArray agencyJSON = new JSONArray();
		Iterator<FullAgencyList> it = fList.iterator();
		FullAgencyList agencyList = null;
		
		while (it.hasNext())
		{
			agencyList = (FullAgencyList)it.next();
			agencyJSON.add(new JSONFormat(agencyList.getFullAgencyListId().toString(),
										  agencyList.getAgencyName()));			
		}
		
		 StringWriter out = new StringWriter();
		 agencyJSON.writeJSONString(out);
		 return out.toString();	
	}
	
	@SuppressWarnings("unchecked")
	public String prePopulatedKeyword(String publicationID) throws Exception
	{
		KeywordService ks = new KeywordServiceImpl();
		List<Keyword> kList = ks.loadKeywordViaPublication(publicationID.trim());
		JSONArray keywordJSON = new JSONArray();
		Iterator<Keyword> it = kList.iterator();
		Keyword keyword = null;
		
		while (it.hasNext())
		{
			keyword = (Keyword)it.next();
			keywordJSON.add(new JSONFormat(keyword.getKeywordId().toString(),
										   keyword.getKeyword()));			
		}
		
		 StringWriter out = new StringWriter();
		 keywordJSON.writeJSONString(out);
		 return out.toString();	
	}
	
	@SuppressWarnings("unchecked")
	public String prePopulatedPublisher(String publicationID) throws Exception
	{
		PublisherPublicationService pps = new PublisherPublicationServiceImpl();
		List<FullAgencyList> fList = pps.loadPublisherViaPublication(publicationID.trim());
		JSONArray publisherJSON = new JSONArray();
		Iterator<FullAgencyList> it = fList.iterator();
		FullAgencyList fullAgencyList = null;
		
		while (it.hasNext())
		{
			fullAgencyList = (FullAgencyList)it.next();
			publisherJSON.add(new JSONFormat(fullAgencyList.getFullAgencyListId().toString(),
										   	 fullAgencyList.getAgencyName()));			
		}
		
		 StringWriter out = new StringWriter();
		 publisherJSON.writeJSONString(out);
		 return out.toString();	
	}
}
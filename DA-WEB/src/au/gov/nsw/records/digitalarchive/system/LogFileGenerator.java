package au.gov.nsw.records.digitalarchive.system;

import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.utils.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.base.Constants;
import au.gov.nsw.records.digitalarchive.service.AgencyPublicationService;
import au.gov.nsw.records.digitalarchive.service.AgencyPublicationServiceImpl;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;

public class LogFileGenerator implements JSONAware
{
	private Integer agencyID = 0;
	private String dateRun = "";
	private List<Publication> publications = new ArrayList<Publication>(); 	
	public LogFileGenerator(){ super();}
	
	public LogFileGenerator(Integer agencyID, String dateRun, 
							List<Publication> publications)
	{
		this.agencyID = agencyID;
		this.dateRun = dateRun;
		this.publications = publications;
	}
	
	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		int counter = 1;
		String comma = ",";
		int cumulativeTotal = 0;
		sb.append("{");
		
		sb.append(Constants.quotes + JSONObject.escape("ID") + Constants.quotes);
        sb.append(":");
        sb.append(this.agencyID);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("DateRun") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.dateRun + Constants.quotes);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("publications") + Constants.quotes);
        sb.append(":[");
        
        for (Iterator<Publication> pubIterator = publications.iterator(); pubIterator.hasNext();) 
        {
			Publication thisPublication = (Publication) pubIterator.next();
			String hits = "0";
	    	String publicationTitle = "";
	    	if (thisPublication.getViewedTimes() != null)
	    		{hits = thisPublication.getViewedTimes().toString();}
	    	
	    	if (StringUtils.isEmpty(thisPublication.getTitle()))
	    		{publicationTitle = "";}
	    	else if (thisPublication.getTitle().indexOf(",") != -1)
	    		{publicationTitle = thisPublication.getTitle().replace(",", "");}
	    	else
	    		{publicationTitle = thisPublication.getTitle();}
					
			sb.append("{");
	        sb.append(Constants.quotes + JSONObject.escape("title") + Constants.quotes);
	        sb.append(":");
	        sb.append(Constants.quotes + publicationTitle + Constants.quotes);
	        sb.append(",");
	        sb.append(Constants.quotes + JSONObject.escape("cumulative") + Constants.quotes);
	        sb.append(":");
	        sb.append(Constants.quotes + hits + Constants.quotes);
	        sb.append("}");
	        
	        if (counter == publications.size())
	        	{comma = "";}
	        sb.append(comma);
	        cumulativeTotal = cumulativeTotal + Integer.parseInt(hits);
	        counter++;
		}
        	
        sb.append("],");
        
        sb.append(Constants.quotes + JSONObject.escape("CumulativeTotal") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + cumulativeTotal + Constants.quotes);
        
        sb.append("}");
        return sb.toString();
	}
	
	
	private static void readJSON(String sFileName) throws Exception
	{
		FileReaderWrapper frw = new FileReaderWrapper();
		String line = frw.readUsingFileReader("C:\\mnt\\opengovdata\\out.txt");
		Object obj=JSONValue.parse(line);
		
		JSONObject jObject = (JSONObject)obj;
		JSONArray publications = (JSONArray)jObject.get("publications");
			
		Object[] pubs = publications.toArray();
		Integer temp = 0;
		
		
		ArrayList<JSONObject> jObjectList = new ArrayList<JSONObject> ();
		
		for (int i = 0; i < pubs.length; i++)
		{
			jObjectList.add((JSONObject)pubs[i]);
		}
		
		for (int j = 0; j < jObjectList.size(); j++)
		{
			temp = temp + Integer.parseInt(jObjectList.get(j).get("cumulative").toString());
		}
	
		System.out.println(temp);
	}
	
	
	/*
	 * Creates a directory named with agency id.
	 */
	private void createAgencyDirectory(String dirName)
	{
		 File theDir = new File(dirName);
		 if (!theDir.exists())
		 {theDir.mkdir();}
	}
	
	public String formatDate(Date inputDate)
	{
		String s;
		Format formatter = new SimpleDateFormat("ddMMyyyy");
		s = formatter.format(inputDate);
		return s;
	}

	public String rightNow()
	{
	  return formatDate(new Date());
	}
	  
	public String getLastDay(String year, String month) 
	{
	    // get a calendar object
	    GregorianCalendar calendar = new GregorianCalendar();
	    // convert the year and month to integers
	    int yearInt = Integer.parseInt(year);
	    int monthInt = Integer.parseInt(month);
	    // adjust the month for a zero based index
	    monthInt = monthInt - 1;
	    // set the date of the calendar to the date provided
	    calendar.set(yearInt, monthInt, 1);
	    int dayInt = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
	  
	    return Integer.toString(dayInt);
	}
	
	public static void main(String [] args) throws Exception
	{
		LogFileGenerator lfg;
		String now;
		Format formatter = new SimpleDateFormat("ddMMyyyy");
		now = formatter.format(new Date());
		
		
		AgencyPublicationService aps = new AgencyPublicationServiceImpl();
		FullAgencyListService fls = new FullAgencyListServiceImpl();
		List<FullAgencyList> agencyList = fls.browseFullAgencyList();
		//FileReaderWrapper frw = new FileReaderWrapper();
		Iterator<FullAgencyList> fullIterator = agencyList.iterator();
		while (fullIterator.hasNext()) 
		{
			FullAgencyList fullAgencyList = (FullAgencyList) fullIterator.next();
			List<Publication> publicationList = aps.loadPublicationViaFullAgency(fullAgencyList);
			Iterator<Publication> pubIter = publicationList.iterator();
			while(pubIter.hasNext())
			{
				Publication pub = (Publication) pubIter.next();
				try {
					lfg = new LogFileGenerator (fullAgencyList.getFullAgencyListId(), now, publicationList);
					lfg.createAgencyDirectory(Constants.LOGGER + fullAgencyList.getFullAgencyListId());
					FileWriter fw = new FileWriter(Constants.LOGGER + fullAgencyList.getFullAgencyListId() + File.separator + lfg.rightNow() + ".json");
					fw.write(lfg.toJSONString());
					fw.close();
				} catch (Exception e) {
					System.out.println("Exception " + pub.getPublicationId());
					System.out.println(e);
					
				}	
			}		
		}
	}
		
}

package au.gov.nsw.records.digitalarchive.base;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class PublicationJSONFormat implements JSONAware{

	String id;
	String title;
	List<String> agencies;
	String type;
	String datePublished;
	List<String> publishers;
	List<String> keywords;
	String description;
	String language;
	String coverage;
	String copyright;
	String viewedTimes;
	String lastViewed;
	List<String> filesURLList;
	
	public PublicationJSONFormat(String id, 
								 String title, 
								 List<String> agencies,
								 String type, 
								 String datePublished,
								 List<String> publishers,
								 List<String> keywords,
								 String description, 
								 String language, 
								 String coverage,
								 String copyright,
								 String viewedTimes,
								 String lastViewed,
								 List<String> filesURLList) 
	{
		super();
		this.id = id;
		this.title = title;
		this.agencies = agencies;
		this.type = type;
		this.datePublished = datePublished;
		this.publishers = publishers;
		this.keywords = keywords;
		this.description = description;
		this.language = language;
		this.coverage = coverage;
		this.copyright = copyright;
		this.viewedTimes = viewedTimes;
		this.lastViewed = lastViewed;
		this.filesURLList = filesURLList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		
		JSONArray agencyArray = new JSONArray();
		JSONArray publisherArray = new JSONArray();
		JSONArray keywordsArray = new JSONArray();
		JSONArray filesArray = new JSONArray();

		
		for (int i=0; i < agencies.size(); i++) {
			agencyArray.add(agencies.get(i));
		}
		
		for (int j=0; j < publishers.size(); j++) {
			publisherArray.add(publishers.get(j));
		}
		
		for (int l=0; l < keywords.size(); l++) {
			keywordsArray.add(keywords.get(l));
		}
		
		for (int k=0; k < filesURLList.size(); k++) {
			filesArray.add(filesURLList.get(k));
		}
		
		sb.append("{");
		
		sb.append(Constants.quotes + JSONObject.escape("ID") + Constants.quotes);
        sb.append(":");
        sb.append(this.id);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Title") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.title + Constants.quotes);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Agencies") + Constants.quotes);
        sb.append(":");
        sb.append(agencyArray.toString());
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Type") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.type + Constants.quotes);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Date") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.datePublished + Constants.quotes);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Publishers") + Constants.quotes);
        sb.append(":");
        sb.append(publisherArray.toString());
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Keywords") + Constants.quotes);
        sb.append(":");
        sb.append(keywordsArray.toString());
        
        sb.append(",");
                
        sb.append(Constants.quotes + JSONObject.escape("Description") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.description + Constants.quotes);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Language") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.language + Constants.quotes);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Coverage") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.coverage + Constants.quotes);
        
        sb.append(",");

        sb.append(Constants.quotes + JSONObject.escape("Copyright") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.copyright + Constants.quotes);

        sb.append(",");

        sb.append(Constants.quotes + JSONObject.escape("Viewed") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.viewedTimes + Constants.quotes);

        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Last viewed") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.lastViewed + Constants.quotes);

        sb.append(",");
        
        
        sb.append(Constants.quotes + JSONObject.escape("Files") + Constants.quotes);
        sb.append(":");
        sb.append(filesArray.toString());
        sb.append("}");
        
        
		return sb.toString();
	}

}

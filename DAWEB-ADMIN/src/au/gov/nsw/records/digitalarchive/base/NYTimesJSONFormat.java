package au.gov.nsw.records.digitalarchive.base;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class NYTimesJSONFormat implements JSONAware{

	String id;
	String title;
	String pages;
	String description;
	String canonical_url;
	
	String pdf;
	String text;
	String thumbnail;
	String search;
	
	String page_text;
	String page_image;
	
	public NYTimesJSONFormat(String id, 
							 String title, 
							 String pages,
							 String description, 
							 String canonical_url, 
							 String pdf, 
							 String text,
							 String thumbnail, 
							 String search, 
							 String page_text,
							 String page_image) {
		super();
		this.id = id;
		this.title = title;
		this.pages = pages;
		this.description = description;
		this.canonical_url = canonical_url;
		this.pdf = pdf;
		this.text = text;
		this.thumbnail = thumbnail;
		this.search = search;
		this.page_text = page_text;
		this.page_image = page_image;
	}


	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		
		sb.append(Constants.quotes + JSONObject.escape("id") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.id + Constants.quotes);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("title") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.title + Constants.quotes);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("pages") + Constants.quotes);
        sb.append(":");
        sb.append(this.pages);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("description") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.description + Constants.quotes);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("canonical_url") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.canonical_url + Constants.quotes);

        sb.append(",");

        sb.append(Constants.quotes + JSONObject.escape("resources") + Constants.quotes);
        sb.append(":");
        sb.append("{");
        
        	sb.append(Constants.quotes + JSONObject.escape("pdf") + Constants.quotes);
        	sb.append(":");
            sb.append(Constants.quotes + this.pdf + Constants.quotes);

            sb.append(",");
            
            sb.append(Constants.quotes + JSONObject.escape("text") + Constants.quotes);
        	sb.append(":");
            sb.append(Constants.quotes + this.text + Constants.quotes);
            
            sb.append(",");
            
            sb.append(Constants.quotes + JSONObject.escape("thumbnail") + Constants.quotes);
        	sb.append(":");
            sb.append(Constants.quotes + this.thumbnail + Constants.quotes);
            
            sb.append(",");
            
            sb.append(Constants.quotes + JSONObject.escape("search") + Constants.quotes);
        	sb.append(":");
            sb.append(Constants.quotes + this.search + Constants.quotes);
            
            sb.append(",");

            sb.append(Constants.quotes + JSONObject.escape("page") + Constants.quotes);
            sb.append(":");
            sb.append("{");
            	
            	sb.append(Constants.quotes + JSONObject.escape("text") + Constants.quotes);
            	sb.append(":");
            	sb.append(Constants.quotes + this.page_text + Constants.quotes);
            	
            	sb.append(",");
            	
            	sb.append(Constants.quotes + JSONObject.escape("image") + Constants.quotes);
            	sb.append(":");
            	sb.append(Constants.quotes + this.page_image + Constants.quotes);
            
            	sb.append("}");
           sb.append("}");
        sb.append("}");
		return sb.toString();
	}
	
}

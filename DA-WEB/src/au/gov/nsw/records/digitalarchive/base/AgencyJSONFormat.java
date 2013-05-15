package au.gov.nsw.records.digitalarchive.base;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class AgencyJSONFormat implements JSONAware {

	String id;
	String name;
	String url;
	public AgencyJSONFormat(String id, String name, String url) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
	}
	
	@Override
	public String toJSONString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("{");
		
		sb.append(Constants.quotes + JSONObject.escape("ID") + Constants.quotes);
        sb.append(":");
        sb.append(this.id);
        
        sb.append(",");
        
        sb.append(Constants.quotes + JSONObject.escape("Name") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + this.name + Constants.quotes);
        
        if (!("").equalsIgnoreCase(url))
        { sb.append(",");
          sb.append(Constants.quotes + JSONObject.escape("URL") + Constants.quotes);
          sb.append(":");
          sb.append(Constants.quotes + this.url + Constants.quotes);
        }
        sb.append("}");
        return sb.toString();
		
	}
}

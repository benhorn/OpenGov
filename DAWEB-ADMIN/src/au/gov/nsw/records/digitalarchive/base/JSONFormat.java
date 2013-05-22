package au.gov.nsw.records.digitalarchive.base;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

public class JSONFormat implements JSONStreamAware{
    private String id;
    private String name;
    
    public JSONFormat(String id, String name){
            this.id = id;
            this.name = name;
    }
    
   public void writeJSONString (Writer out) throws IOException{
            LinkedHashMap obj = new LinkedHashMap();
            obj.put("id", id);
            obj.put("name", name);
            JSONValue.writeJSONString(obj, out);
   }

}




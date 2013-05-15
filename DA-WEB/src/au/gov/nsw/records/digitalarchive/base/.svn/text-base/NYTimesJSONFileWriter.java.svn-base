package au.gov.nsw.records.digitalarchive.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class NYTimesJSONFileWriter {

	public void createJSONFile(String location, String JSON)
	{
		 Writer writer = null;
		 String content = "DV.loadJSON(".concat(JSON + ");");
	        try {
	            
	            File file = new File(location);
	            
	            writer = new BufferedWriter(new FileWriter(file));
	            writer.write(content);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (writer != null) {
	                    writer.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		
	}
	
}

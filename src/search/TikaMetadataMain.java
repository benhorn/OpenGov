package au.gov.nsw.records.digitalarchive.search;

import java.util.Map;

public class TikaMetadataMain {

	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		TikaWrapper ta = new TikaWrapper();

		
		System.out.println(ta.getFileContent("PublicReport.pdf"));
		
		System.out.println("Hello");
		Map<String, String> result; 
		result = ta.getMetaData("PublicReport.pdf");
		
		System.out.println(result.get("xmpTPg:NPages"));
		
		//System.out.println(ta.getFileType("PublicReport.pdf"));

	}

}

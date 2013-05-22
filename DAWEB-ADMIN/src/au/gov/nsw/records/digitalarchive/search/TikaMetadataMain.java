package au.gov.nsw.records.digitalarchive.search;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class TikaMetadataMain {

	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		TikaWrapper ta = new TikaWrapper();

		
		//System.out.println(ta.getFileContent("PublicReport.pdf"));
		
		Map<String, String> result; 
		//result = ta.getMetaData("PublicReport.pdf");
		
		int pageNumber = Integer.parseInt(ta.getPageNumber("TestFile.pdf"));
		
		//System.out.println(ta.getPageNumber("PublicReport.pdf"));
		
		for (int i = 0; i <= pageNumber; i++)
		{
			File aFile = new File("c:/file/textfile/page" + i + ".txt");
			aFile.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter("c:/file/textfile/page" + i + ".txt"));
			out.write(ta.getFileContent("TestFile.pdf"));
			out.close();
		}
	}

}

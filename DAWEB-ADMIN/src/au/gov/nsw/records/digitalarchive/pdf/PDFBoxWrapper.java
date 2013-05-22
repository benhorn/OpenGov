package au.gov.nsw.records.digitalarchive.pdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFBoxWrapper {
	
	public void createTxtFile(String inputFile, String location)
	{
		 PDDocument pd;
		 BufferedWriter wr;
		 try 
		 {
			 File input = new File(inputFile);  
		     File output = new File(location); // The text file where you are going to store the extracted data
		     pd = PDDocument.load(input);
		     PDFTextStripper stripper = new PDFTextStripper();
		     wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
		     stripper.writeText(pd, wr);
		     
		     if (pd != null) 
		     {
		          pd.close();
		     }
		     wr.close();
		 } catch (Exception e)
		 {
			 e.printStackTrace();
		 }
	}
}




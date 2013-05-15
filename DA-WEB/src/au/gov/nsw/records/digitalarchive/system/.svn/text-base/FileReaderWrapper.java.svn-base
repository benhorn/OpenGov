package au.gov.nsw.records.digitalarchive.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class FileReaderWrapper {

	    public String readUsingFileReader(String fileName) throws IOException {
	        File file = new File(fileName);
	        FileReader fr = new FileReader(file);
	        BufferedReader br = new BufferedReader(fr);
	        String line = br.readLine();
	        br.close();
	        fr.close();
	         
	        return line;
	    }
	 
	    public void readUsingBufferedReader(String fileName, Charset cs) throws IOException {
	        File file = new File(fileName);
	        FileInputStream fis = new FileInputStream(file);
	        InputStreamReader isr = new InputStreamReader(fis, cs);
	        BufferedReader br = new BufferedReader(isr);
	        String line;
	        while((line = br.readLine()) != null){
	            //process the line
	            System.out.println(line);
	        }
	        br.close();
	         
	    }
	 
	    public void readUsingBufferedReader(String fileName) throws IOException {
	        File file = new File(fileName);
	        FileReader fr = new FileReader(file);
	        BufferedReader br = new BufferedReader(fr);
	        String line;
	        while((line = br.readLine()) != null){
	            //process the line
	            System.out.println(line);
	        }
	        //close resources
	        br.close();
	        fr.close();
	    }
	 
}

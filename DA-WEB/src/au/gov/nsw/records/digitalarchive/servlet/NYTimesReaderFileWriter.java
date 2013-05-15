package au.gov.nsw.records.digitalarchive.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NYTimesReaderFileWriter extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, 
					  HttpServletResponse response) throws ServletException,IOException
	{
		 Writer writer = null;
		 
	        try {
	            
	        	String text = "This is a text file";
	 
	            File file = new File("write.txt");
	            writer = new BufferedWriter(new FileWriter(file));
	            writer.write(text);
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

	public void doPost(HttpServletRequest request, 
					   HttpServletResponse response)throws ServletException, IOException 
	{

		doGet(request, response);
	}

}

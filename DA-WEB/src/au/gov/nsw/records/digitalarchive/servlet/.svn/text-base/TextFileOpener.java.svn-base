package au.gov.nsw.records.digitalarchive.servlet;

import gov.loc.repository.pairtree.Pairtree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.gov.nsw.records.digitalarchive.base.Constants;

public class TextFileOpener extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7885119743720763290L;

	public void doGet(HttpServletRequest request, 
			  		 HttpServletResponse response) throws ServletException,IOException
	{
		response.setContentType("text/html");
		String uid = request.getParameter("uid");
		String i = request.getParameter("counter");
		String perfectName = "";
		Pairtree pt = new Pairtree();
		
		if (i == null)
			perfectName = Constants.PAIRTREE_ROOT + pt.mapToPPath(uid) + File.separator + "obj" + File.separator + "document.txt";
		else
			perfectName = Constants.PAIRTREE_ROOT + pt.mapToPPath(uid) + File.separator + "obj" + File.separator + "txt" + File.separator + "page" + i + ".txt";
				
		FileInputStream is = new FileInputStream(perfectName);
		byte data[] = new byte[is.available()];
		is.read(data);
		is.close();
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		OutputStream toClient = response.getOutputStream();
		toClient.write(data);
		toClient.close();
	}

	public void doPost(HttpServletRequest request, 
			   		   HttpServletResponse response) throws ServletException, IOException 
	{

		doGet(request, response);
	}

}

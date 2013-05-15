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

public class OpenGovThumbnail extends HttpServlet{
	
	public void doGet(HttpServletRequest request, 
					  HttpServletResponse response) throws ServletException,IOException
	{
		response.setContentType("text/html");
		String uid = request.getParameter("uid");
		Pairtree pt = new Pairtree();
				
		String perfectName = Constants.PAIRTREE_ROOT + pt.mapToPPath(uid) + File.separator + "obj" + File.separator + "thumbnail.gif";
		FileInputStream is = new FileInputStream(perfectName);
		int i = is.available();
		byte data[] = new byte[i];
		is.read(data);
		is.close();
		response.setContentType("image/gif");
		OutputStream toClient = response.getOutputStream();
		toClient.write(data);
		toClient.close();
	}

	public void doPost(HttpServletRequest request, 
					   HttpServletResponse response)throws ServletException, IOException 
	{

		     doGet(request, response);
	}

}

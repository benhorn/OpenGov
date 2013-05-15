package au.gov.nsw.records.digitalarchive.servlet;

import gov.loc.repository.pairtree.Pairtree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import au.gov.nsw.records.digitalarchive.base.Constants;

public class DocumentSearcher extends HttpServlet{

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, 
	  		  		  HttpServletResponse response) throws ServletException,IOException
	{
		String query = request.getParameter("q");
		String uid = request.getParameter("uid");
		
		Pairtree pt = new Pairtree();

		String DIRName = Constants.PAIRTREE_ROOT + pt.mapToPPath(uid) + File.separator + "obj" + File.separator + "txt" + File.separator;
		File f = new File(DIRName);
		File[] matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.startsWith("page") && name.endsWith("txt");
		    }
		});
		Set<Integer> foundPages = new TreeSet<Integer>();
		
		int i = 1;
				
		for (int j = 0; j < matchingFiles.length; j++) 
		{
			try {
					String status = "";
					String line;
					BufferedReader bf = new BufferedReader(new FileReader(DIRName + "page" + i + ".txt"));
					while (( line = bf.readLine()) != null)
					{
						int indexfound = line.toLowerCase().indexOf(query.trim());
						if (indexfound > -1) 
						{
							foundPages.add(i);
							status = "pages " + i + " found";
					 	}
					}
					bf.close();
					i++;
					System.out.println(status);
				} catch (FileNotFoundException e) 
				{
					System.out.println("File not found");
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
					System.out.println("IO Error Occurred: " + e.toString());
				}
		}
		String destination = "/result.jsp";
		 
		RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
				
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append(Constants.quotes + JSONObject.escape("query") + Constants.quotes);
        sb.append(":");
        sb.append(Constants.quotes + query + Constants.quotes);
        sb.append(",");
        sb.append(Constants.quotes + JSONObject.escape("results") + Constants.quotes);
        sb.append(":");
        sb.append(foundPages.toString());
        sb.append("}");
		
	    System.out.println(foundPages.toString());
	    request.getSession().setAttribute("query", sb.toString());
		rd.forward(request, response);
	}

	public void doPost(HttpServletRequest request, 
					   HttpServletResponse response)throws ServletException, IOException 
	{
		doGet(request, response);
	}
	
}

package au.gov.nsw.records.digitalarchive.struts.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.BaseAction;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;

public class DesktopAction extends BaseAction{

	public ActionForward execute(ActionMapping mapping, 
			 					 ActionForm form,
			 					 HttpServletRequest request, 
			 					 HttpServletResponse response) throws Exception
	{	
		ActionForward forward = null;

		String id = request.getParameter("id");
		UploadedFile uploadedFile = new UploadedFile();
		FileService fs = new FileServiceImpl();
		try {
			uploadedFile = fs.loadFile(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			System.out.println("\nNumber format\n");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		String filename=  uploadedFile.getPairtreeUrl().trim();
		if (fileExists(filename)) {
			filename = uploadedFile.getPairtreeUrl().trim();
			File pdfFile = new File(filename);
			if (pdfFile.exists()) {
	 
//				if (Desktop.isDesktopSupported()) {
//					Desktop.getDesktop().open(pdfFile);
//				} else {
//					System.out.println("Desktop missing!");
//				}
				response.setContentType("application/pdf");
				response.setHeader("Content-disposition","inline; filename="+ uploadedFile.getFileName().trim().replace(" ", "_"));
				ByteArrayOutputStream baos = getByteArrayOutputStream(new File(filename));
				response.setContentLength(baos.size());
				ServletOutputStream sos;
				sos = response.getOutputStream();
				baos.writeTo(sos);
				sos.flush();
				
			} else {
				System.out.println("File does not exists!");
			}
		} 
		
		return forward;
	}
	
	private boolean fileExists(String filePath)
	{
		File aFile = new File(filePath);
		if (aFile.exists())
			return true;
		else
			return false;
	}

	private ByteArrayOutputStream getByteArrayOutputStream(File inputFile) throws IOException 
	{
		FileInputStream fis = new FileInputStream(inputFile);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[256];
		 try {
		        for (int readNum; (readNum = fis.read(buf)) != -1;) {
		            bos.write(buf, 0, readNum); //no doubt here is 0
		            //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
		        }

		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }

		return bos;
	}

}
